package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;
 
public class ProductDAOImpl implements ProductDAO {

    // ================= SQL QUERIES =================

    private static final String GET_ALL_PRODUCTS_SQL =
            "SELECT * FROM products WHERE is_active = TRUE";

    private static final String GET_PRODUCT_BY_ID_SQL =
            "SELECT * FROM products WHERE product_id = ?";

    private static final String GET_PRODUCTS_BY_CATEGORY_SQL =
            "SELECT * FROM products WHERE category_id = ? AND is_active = TRUE";

    private static final String SEARCH_PRODUCTS_SQL =
            "SELECT DISTINCT p.* FROM products p LEFT JOIN categories c ON p.category_id = c.category_id WHERE (p.product_name LIKE ? OR p.brand LIKE ? OR c.category_name LIKE ?) AND p.is_active = TRUE";

    private static final String FILTER_PRODUCTS_SQL =
            "SELECT * FROM products WHERE category_id = ? AND price BETWEEN ? AND ? AND is_active = TRUE";

    private static final String FILTER_WITH_KEYWORD_SQL =
            "SELECT * FROM products WHERE category_id = ? AND (product_name LIKE ? OR brand LIKE ?) AND price BETWEEN ? AND ? AND is_active = TRUE";

    private static final String ADD_PRODUCT_SQL =
            "INSERT INTO products (category_id, product_name, brand, description, price, image_url, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE products SET category_id = ?, product_name = ?, brand = ?, description = ?, price = ?, image_url = ?, is_active = ? WHERE product_id = ?";

    private static final String DELETE_PRODUCT_SQL =
            "UPDATE products SET is_active = FALSE WHERE product_id = ?";

    // ================= METHODS =================

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_PRODUCTS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractProduct(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public Product getProductById(int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_PRODUCT_BY_ID_SQL)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractProduct(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_PRODUCTS_BY_CATEGORY_SQL)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_PRODUCTS_SQL)) {

            String searchKey = "%" + keyword + "%";

            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<Product> filterProducts(int categoryId, double minPrice, double maxPrice) {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FILTER_PRODUCTS_SQL)) {

            ps.setInt(1, categoryId);
            ps.setDouble(2, minPrice);
            ps.setDouble(3, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<Product> filterProducts(int categoryId, String keyword, double minPrice, double maxPrice) {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FILTER_WITH_KEYWORD_SQL)) {

            String searchKey = "%" + keyword + "%";

            ps.setInt(1, categoryId);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setDouble(4, minPrice);
            ps.setDouble(5, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    // ================= 🔥 IMPORTANT FIX =================
    // CATEGORY NAME FILTER (JOIN)

    @Override
    public List<Product> getProductsByCategoryName(String category) {

        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT p.* FROM products p " +
                         "JOIN categories c ON p.category_id = c.category_id " +
                         "WHERE c.category_name = ? AND p.is_active = TRUE";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, category);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(extractProduct(rs));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<Product> filterByPrice(double minPrice, double maxPrice) {
        List<Product> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE price BETWEEN ? AND ? AND is_active = TRUE")) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

    // ================= VARIANTS =================

    @Override
    public List<ProductVariant> getVariantsByProductId(int productId) {

        List<ProductVariant> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM product_variants WHERE product_id = ?")) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariant v = new ProductVariant();
                    v.setVariantId(rs.getInt("variant_id"));
                    v.setProductId(rs.getInt("product_id"));
                    v.setSize(rs.getString("size"));
                    v.setStockQuantity(rs.getInt("stock_quantity"));

                    list.add(v);
                }
            }

            list.sort((v1, v2) -> compareSizes(v1.getSize(), v2.getSize()));

        } catch (Exception e) {
            throw new RuntimeException("Database error in ProductDAO: " + e.getMessage(), e);
        }

        return list;
    }

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

    // ================= HELPER =================

    private Product extractProduct(ResultSet rs) throws Exception {
        Product p = new Product();

        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setProductName(rs.getString("product_name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("is_active"));

        return p;
    }

	@Override
	public boolean addProduct(Product product) {
		try (Connection con = DBConnection.getConnection();
		     PreparedStatement ps = con.prepareStatement(ADD_PRODUCT_SQL)) {
			ps.setInt(1, product.getCategoryId());
			ps.setString(2, product.getProductName());
			ps.setString(3, product.getBrand());
			ps.setString(4, product.getDescription());
			ps.setDouble(5, product.getPrice());
			ps.setString(6, product.getImageUrl());
			ps.setBoolean(7, product.isActive());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			throw new RuntimeException("Database error in ProductDAO.addProduct: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean updateProduct(Product product) {
		try (Connection con = DBConnection.getConnection();
		     PreparedStatement ps = con.prepareStatement(UPDATE_PRODUCT_SQL)) {
			ps.setInt(1, product.getCategoryId());
			ps.setString(2, product.getProductName());
			ps.setString(3, product.getBrand());
			ps.setString(4, product.getDescription());
			ps.setDouble(5, product.getPrice());
			ps.setString(6, product.getImageUrl());
			ps.setBoolean(7, product.isActive());
			ps.setInt(8, product.getProductId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			throw new RuntimeException("Database error in ProductDAO.updateProduct: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean deleteProduct(int productId) {
		try (Connection con = DBConnection.getConnection();
		     PreparedStatement ps = con.prepareStatement(DELETE_PRODUCT_SQL)) {
			ps.setInt(1, productId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			throw new RuntimeException("Database error in ProductDAO.deleteProduct: " + e.getMessage(), e);
		}
	}
}