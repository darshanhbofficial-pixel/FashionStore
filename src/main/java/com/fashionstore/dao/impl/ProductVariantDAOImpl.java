package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    // ================= SQL QUERIES =================

    private static final String GET_VARIANTS_BY_PRODUCT_SQL = """
        SELECT * FROM product_variants WHERE product_id = ?
        """;

    private static final String GET_VARIANT_BY_ID_SQL = """
        SELECT * FROM product_variants WHERE variant_id = ?
        """;

    private static final String GET_VARIANT_BY_PRODUCT_AND_SIZE_SQL = """
        SELECT * FROM product_variants
        WHERE product_id = ? AND size = ?
        """;

    private static final String GET_STOCK_SQL = """
        SELECT stock_quantity FROM product_variants WHERE variant_id = ?
        """;

    private static final String UPDATE_STOCK_SQL = """
        UPDATE product_variants SET stock_quantity = ? WHERE variant_id = ?
        """;

    private static final String INSERT_VARIANT_SQL = """
        INSERT INTO product_variants (product_id, size, stock_quantity)
        VALUES (?, ?, ?)
        """;

    private static final String UPDATE_VARIANT_SQL = """
        UPDATE product_variants
        SET product_id = ?, size = ?, stock_quantity = ?
        WHERE variant_id = ?
        """;

    private static final String DELETE_VARIANT_SQL = """
        DELETE FROM product_variants WHERE variant_id = ?
        """;

    // ================= METHODS =================

    @Override
    public List<ProductVariant> getVariantsByProductId(int productId) {
        List<ProductVariant> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_VARIANTS_BY_PRODUCT_SQL)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractVariant(rs));
            }

            list.sort((v1, v2) -> compareSizes(v1.getSize(), v2.getSize()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ProductVariant getVariantById(int variantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_VARIANT_BY_ID_SQL)) {

            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractVariant(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ProductVariant getVariantByProductAndSize(int productId, String size) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_VARIANT_BY_PRODUCT_AND_SIZE_SQL)) {

            ps.setInt(1, productId);
            ps.setString(2, size);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractVariant(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getStockByVariantId(int variantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_STOCK_SQL)) {

            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock_quantity");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean updateStock(int variantId, int newQuantity) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STOCK_SQL)) {

            ps.setInt(1, newQuantity);
            ps.setInt(2, variantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addVariant(ProductVariant variant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_VARIANT_SQL)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSize());
            ps.setInt(3, variant.getStockQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateVariant(ProductVariant variant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_VARIANT_SQL)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSize());
            ps.setInt(3, variant.getStockQuantity());
            ps.setInt(4, variant.getVariantId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteVariant(int variantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_VARIANT_SQL)) {

            ps.setInt(1, variantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    @Override
    public void reduceStock(int variantId, int quantity) {

        String sql = "UPDATE product_variants SET stock_quantity = stock_quantity - ? WHERE variant_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, variantId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPER =================

    private static final java.util.Map<String, Integer> CLOTHING_SIZE_ORDER = new java.util.HashMap<>();
    static {
        CLOTHING_SIZE_ORDER.put("XXS", 1);
        CLOTHING_SIZE_ORDER.put("XS", 2);
        CLOTHING_SIZE_ORDER.put("S", 3);
        CLOTHING_SIZE_ORDER.put("M", 4);
        CLOTHING_SIZE_ORDER.put("L", 5);
        CLOTHING_SIZE_ORDER.put("XL", 6);
        CLOTHING_SIZE_ORDER.put("XXL", 7);
        CLOTHING_SIZE_ORDER.put("XXXL", 8);
    }

    private int compareSizes(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        boolean isN1 = false;
        boolean isN2 = false;
        int n1 = 0;
        int n2 = 0;

        try {
            n1 = Integer.parseInt(s1.trim());
            isN1 = true;
        } catch (NumberFormatException e) {}

        try {
            n2 = Integer.parseInt(s2.trim());
            isN2 = true;
        } catch (NumberFormatException e) {}

        if (isN1 && isN2) {
            return Integer.compare(n1, n2);
        }
        if (isN1) {
            return -1;
        }
        if (isN2) {
            return 1;
        }

        String norm1 = s1.trim().toUpperCase();
        String norm2 = s2.trim().toUpperCase();
        Integer r1 = CLOTHING_SIZE_ORDER.get(norm1);
        Integer r2 = CLOTHING_SIZE_ORDER.get(norm2);

        if (r1 != null && r2 != null) {
            return Integer.compare(r1, r2);
        }
        if (r1 != null) {
            return -1;
        }
        if (r2 != null) {
            return 1;
        }

        return norm1.compareTo(norm2);
    }

    private ProductVariant extractVariant(ResultSet rs) throws Exception {
        ProductVariant v = new ProductVariant();

        v.setVariantId(rs.getInt("variant_id"));
        v.setProductId(rs.getInt("product_id"));
        v.setSize(rs.getString("size"));
        v.setStockQuantity(rs.getInt("stock_quantity"));

        return v;
    }
}