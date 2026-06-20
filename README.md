# FashionStore 🛍️

A modern, responsive e-commerce web application for a fashion boutique, built using the standard Jakarta EE (Java Servlets & JSP) architecture with a MySQL database.

---

## 🚀 Key Features

### 🌐 Global UI & Theme
- **Dark/Light Mode Theme**: Fully responsive dark/light mode toggle with preference persistence via `localStorage`.
- **Search Autocomplete**: Debounced, interactive autocomplete search dropdown in the navigation bar using the search suggestions API.
- **Toast Notifications**: Built-in notification toaster for actions like adding items to the cart, profile updates, and authentication.
- **Back-to-Top Button**: Smooth scrolling navigation assistant visible on long pages.

### 🛍️ Product Catalog & Details
- **Browsing & Filtering**: Filter the product grid by Category, Brand, Price Range (Min/Max), and search keywords.
- **Dynamic Stock Badges**: Real-time product availability badges ("In Stock", "Low Stock", "Out of Stock") based on variant totals.
- **Detailed View**: View individual product description, brands, and select specific variants (size/color).
- **Related Products Section**: Smart recommendation carousel of related products belonging to the same category.

### 👤 User Accounts & Profile
- **Authentication**: Secure User Registration and Login.
- **Profile Management**: Inline profile detail editing (Username, Email, Address).
- **Change Password**: Dedicated secure change password workflow.
- **Order History**: Check previous purchases with a visual progress tracking stepper (Placed → Shipped → Delivered).

### 🛒 Cart & Checkout
- **Interactive Shopping Cart**: Add, edit, or remove item variants and quantities.
- **Checkout coupon codes**: Support for promo coupons (apply `FASHION10` for a 10% discount).
- **Address Auto-save**: Store address info during order placement.
- **Delivery Estimates**: Shows expected delivery date calculated dynamically (+5 business days).

### 🖥️ Admin Dashboard
- **Product Management (CRUD)**: Insert, modify, or delete products in the database catalog via modal forms.
- **Order Management**: View and modify order status across the entire system.
- **Analytics**: Chart.js sales tracking graph rendering current business trends.

---

## 🛠️ Technology Stack
- **Backend**: Java 17+, Jakarta EE Servlets, JDBC
- **Frontend**: JSP, EL (Expression Language), Vanilla CSS, JavaScript
- **Database**: MySQL
- **Build & Dependency Tool**: Maven
- **Assets**: Chart.js, Google Fonts (Outfit / Inter)

---

## ⚙️ How to Setup & Run

### 1. Database Configuration
1. Install and run **MySQL Server**.
2. Create a database called `fashionstore` (or update credentials inside `com.fashionstore.util.DBConnection.java`).
3. Execute the SQL script located at:
   - `src/main/resources/db_setup.sql` (Creates tables, indexes, and populates the database with initial mock catalog products, variants, and admin credentials).

### 2. Running the Application
- **IDE Run (Eclipse / IntelliJ)**:
  1. Import the project as a **Maven Project**.
  2. Configure a **Tomcat 10+ server** runtime environment in your IDE.
  3. Add the project to Tomcat and start the server.
  4. Access the store at: `http://localhost:8080/FashionStore/` (or your configured context path).
  
- **Maven Build**:
  ```bash
  mvn clean package
  ```
  Deploy the generated `.war` file inside the `target/` directory directly to your Tomcat server's `webapps` folder.

---

## 👤 Initial Admin Credentials
To log in as an administrator and test the Admin Dashboard, use the following credentials:
- **Email**: `admin@fashionstore.com`
- **Password**: `admin123`
