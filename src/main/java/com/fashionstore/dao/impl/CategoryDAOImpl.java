package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    // ================= SQL QUERIES =================

    private static final String GET_ALL_CATEGORIES_SQL = """
        SELECT * FROM categories
        """;

    private static final String GET_CATEGORY_BY_ID_SQL = """
        SELECT * FROM categories WHERE category_id = ?
        """;

    private static final String GET_CATEGORY_BY_NAME_SQL = """
        SELECT * FROM categories WHERE category_name = ?
        """;

    private static final String INSERT_CATEGORY_SQL = """
        INSERT INTO categories (category_name, description)
        VALUES (?, ?)
        """;

    private static final String UPDATE_CATEGORY_SQL = """
        UPDATE categories
        SET category_name = ?, description = ?
        WHERE category_id = ?
        """;

    private static final String DELETE_CATEGORY_SQL = """
        DELETE FROM categories WHERE category_id = ?
        """;

    // ================= METHODS =================

    @Override
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_CATEGORIES_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractCategory(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_CATEGORY_BY_ID_SQL)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCategory(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_CATEGORY_BY_NAME_SQL)) {

            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCategory(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean addCategory(Category category) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_CATEGORY_SQL)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateCategory(Category category) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_CATEGORY_SQL)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_CATEGORY_SQL)) {

            ps.setInt(1, categoryId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database error in CategoryDAO: " + e.getMessage(), e);
        }
    }

    // ================= HELPER =================

    private Category extractCategory(ResultSet rs) throws Exception {
        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));

        return category;
    }
}