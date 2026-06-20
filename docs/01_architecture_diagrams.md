# FashionStore — Architecture & Design Diagrams

> **Tech Stack**: Java 21 · Jakarta Servlet 6.0 · JSP + JSTL · MySQL 8 · Maven · Tomcat  
> **Pattern**: MVC (Model-View-Controller) with DAO abstraction layer

---

## 1. High-Level Architecture (MVC)

```mermaid
graph TB
    subgraph CLIENT["🖥️ Client Browser"]
        B["Browser (HTTP)"]
    end

    subgraph VIEW["📄 View Layer (JSP + JSTL)"]
        V1["home.jsp"]
        V2["products.jsp"]
        V3["product-details.jsp"]
        V4["login.jsp"]
        V5["register.jsp"]
        V6["cart.jsp"]
        V7["checkout.jsp"]
        V8["order-confirmation.jsp"]
        V9["partials/navbar.jsp"]
        V10["partials/footer.jsp"]
    end

    subgraph CONTROLLER["⚙️ Controller Layer (Servlets)"]
        C1["HomeServlet /home"]
        C2["ProductsServlet /products"]
        C3["ProductDetailsServlet /product"]
        C4["LoginServlet /login"]
        C5["RegisterServlet /register"]
        C6["CartServlet /cart"]
        C7["CheckoutServlet /checkout"]
        C8["PlaceOrderServlet /place-order"]
        C9["OrderConfirmationServlet /order-confirmation"]
    end

    subgraph MODEL["📦 Model Layer (POJOs)"]
        M1["User"]
        M2["Product"]
        M3["Category"]
        M4["ProductVariant"]
        M5["Cart"]
        M6["CartItem"]
        M7["Order"]
        M8["OrderItem"]
    end

    subgraph DAO["🗄️ DAO Layer (Interface + Impl)"]
        D1["UserDAO → UserDAOImpl"]
        D2["ProductDAO → ProductDAOImpl"]
        D3["CategoryDAO → CategoryDAOImpl"]
        D4["ProductVariantDAO → ProductVariantDAOImpl"]
        D5["CartDAO → CartDAOImpl"]
        D6["CartItemDAO → CartItemDAOImpl"]
        D7["OrderDAO → OrderDAOImpl"]
        D8["OrderItemDAO → OrderItemDAOImpl"]
    end

    subgraph UTIL["🔧 Utility"]
        U1["DBConnection"]
    end

    subgraph DB["🛢️ MySQL Database"]
        DB1["fashion_store"]
    end

    B -- "HTTP Request" --> CONTROLLER
    CONTROLLER -- "setAttribute + forward" --> VIEW
    VIEW -- "HTML Response" --> B
    CONTROLLER -- "calls" --> DAO
    DAO -- "returns" --> MODEL
    DAO -- "getConnection" --> U1
    U1 -- "JDBC" --> DB1
```

---

## 2. Package Structure

```
com.fashionstore
├── controller/                  ← Servlet controllers (handle HTTP)
│   ├── HomeServlet.java
│   ├── LoginServlet.java
│   ├── RegisterServlet.java
│   ├── ProductsServlet.java
│   ├── ProductDetailsServlet.java
│   ├── CartServlet.java
│   ├── CheckoutServlet.java
│   ├── PlaceOrderServlet.java
│   └── OrderConfirmationServlet.java
│
├── dao/                         ← DAO interfaces
│   ├── UserDAO.java
│   ├── ProductDAO.java
│   ├── CategoryDAO.java
│   ├── ProductVariantDAO.java
│   ├── CartDAO.java
│   ├── CartItemDAO.java
│   ├── OrderDAO.java
│   ├── OrderItemDAO.java
│   └── impl/                   ← DAO implementations (JDBC)
│       ├── UserDAOImpl.java
│       ├── ProductDAOImpl.java
│       ├── CategoryDAOImpl.java
│       ├── ProductVariantDAOImpl.java
│       ├── CartDAOImpl.java
│       ├── CartItemDAOImpl.java
│       ├── OrderDAOImpl.java
│       └── OrderItemDAOImpl.java
│
├── model/                       ← POJO model classes
│   ├── User.java
│   ├── Product.java
│   ├── Category.java
│   ├── ProductVariant.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Order.java
│   └── OrderItem.java
│
└── util/                        ← Utility classes
    ├── DBConnection.java
    ├── TestConnection.java
    └── TestDAO.java

webapp/
├── index.html                   ← Landing redirect
├── assets/
│   ├── css/                     ← Page-specific stylesheets
│   └── images/
├── WEB-INF/
│   ├── web.xml                  ← Deployment descriptor
│   └── views/                   ← Protected JSP views
│       ├── home.jsp
│       ├── products.jsp
│       ├── product-details.jsp
│       ├── login.jsp
│       ├── register.jsp
│       ├── cart.jsp
│       ├── checkout.jsp
│       ├── order-confirmation.jsp
│       └── partials/
│           ├── navbar.jsp
│           └── footer.jsp
```

---

## 3. Class Diagram — Model Layer

```mermaid
classDiagram
    class User {
        -int userId
        -String fullName
        -String email
        -String phone
        -String password
        -String addressLine1
        -String addressLine2
        -String city
        -String state
        -String pincode
        -String country
        +getters/setters()
    }

    class Category {
        -int categoryId
        -String categoryName
        -String description
        +getters/setters()
    }

    class Product {
        -int productId
        -int categoryId
        -String productName
        -String brand
        -String description
        -double price
        -String imageUrl
        -String categoryName
        -boolean isActive
        +getters/setters()
    }

    class ProductVariant {
        -int variantId
        -int productId
        -String size
        -int stockQuantity
        +getters/setters()
    }

    class Cart {
        -int cartId
        -int userId
        +getters/setters()
    }

    class CartItem {
        -int cartItemId
        -int cartId
        -int variantId
        -int quantity
        -int productId
        -String productName
        -String brand
        -BigDecimal price
        -String imageUrl
        -String size
        -String color
        +getTotalPrice() BigDecimal
    }

    class Order {
        -int orderId
        -int userId
        -Timestamp orderDate
        -double totalAmount
        -String paymentMethod
        -String orderStatus
        -String deliveryName
        -String deliveryPhone
        -String deliveryAddressLine1
        -String deliveryAddressLine2
        -String deliveryCity
        -String deliveryState
        -String deliveryPincode
        -String deliveryCountry
        +getters/setters()
    }

    class OrderItem {
        -int orderItemId
        -int orderId
        -int variantId
        -int quantity
        -double price
        +getters/setters()
    }

    Category "1" --> "*" Product : has many
    Product "1" --> "*" ProductVariant : has variants
    User "1" --> "1" Cart : owns
    Cart "1" --> "*" CartItem : contains
    CartItem "*" --> "1" ProductVariant : references
    User "1" --> "*" Order : places
    Order "1" --> "*" OrderItem : contains
    OrderItem "*" --> "1" ProductVariant : references
```

---

## 4. Class Diagram — DAO Layer

```mermaid
classDiagram
    class UserDAO {
        <<interface>>
        +registerUser(User) boolean
        +loginUser(email, password) User
        +getUserById(int) User
        +getUserByEmail(String) User
        +getUserByPhone(String) User
        +updateUser(User) boolean
        +updatePassword(int, String) boolean
        +deleteUser(int) boolean
        +emailExists(String) boolean
        +phoneExists(String) boolean
        +getAllUsers() List~User~
    }

    class ProductDAO {
        <<interface>>
        +getAllProducts() List~Product~
        +getProductById(int) Product
        +getProductsByCategory(int) List~Product~
        +searchProducts(String) List~Product~
        +filterProducts(int, double, double) List~Product~
        +filterProducts(int, String, double, double) List~Product~
        +getProductsByCategoryName(String) List~Product~
        +getVariantsByProductId(int) List~ProductVariant~
        +addProduct(Product) boolean
        +updateProduct(Product) boolean
        +deleteProduct(int) boolean
    }

    class CategoryDAO {
        <<interface>>
        +getAllCategories() List~Category~
        +getCategoryById(int) Category
        +getCategoryByName(String) Category
        +addCategory(Category) boolean
        +updateCategory(Category) boolean
        +deleteCategory(int) boolean
    }

    class ProductVariantDAO {
        <<interface>>
        +getVariantsByProductId(int) List~ProductVariant~
        +getVariantById(int) ProductVariant
        +getVariantByProductAndSize(int, String) ProductVariant
        +getStockByVariantId(int) int
        +updateStock(int, int) boolean
        +reduceStock(int, int) void
        +addVariant(ProductVariant) boolean
        +updateVariant(ProductVariant) boolean
        +deleteVariant(int) boolean
    }

    class CartDAO {
        <<interface>>
        +createCart(int) boolean
        +getCartById(int) Cart
        +getCartByUserId(int) Cart
        +getOrCreateCartByUserId(int) Cart
        +deleteCart(int) boolean
        +cartExistsByUserId(int) boolean
    }

    class CartItemDAO {
        <<interface>>
        +addCartItem(CartItem) boolean
        +updateCartItemQuantity(int, int) boolean
        +updateCartItemQuantityByCartAndVariant(int, int, int) boolean
        +removeCartItem(int) boolean
        +getCartItems(int) List~CartItem~
        +getCartTotal(int) BigDecimal
    }

    class OrderDAO {
        <<interface>>
        +createOrder(Order) int
        +getOrderById(int) Order
        +getOrdersByUserId(int) List~Order~
        +updateOrderStatus(int, String) boolean
        +deleteOrder(int) boolean
    }

    class OrderItemDAO {
        <<interface>>
        +addOrderItem(OrderItem) boolean
        +addOrderItems(List~OrderItem~) boolean
        +getItemsByOrderId(int) List~OrderItem~
        +deleteOrderItem(int) boolean
        +clearOrderItems(int) boolean
    }

    UserDAO <|.. UserDAOImpl
    ProductDAO <|.. ProductDAOImpl
    CategoryDAO <|.. CategoryDAOImpl
    ProductVariantDAO <|.. ProductVariantDAOImpl
    CartDAO <|.. CartDAOImpl
    CartItemDAO <|.. CartItemDAOImpl
    OrderDAO <|.. OrderDAOImpl
    OrderItemDAO <|.. OrderItemDAOImpl
```

---

## 5. Sequence Diagrams

### 5.1 User Registration Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant RS as RegisterServlet
    participant UD as UserDAOImpl
    participant DB as MySQL

    User->>RS: GET /register
    RS->>User: Forward → register.jsp

    User->>RS: POST /register (form data)
    RS->>RS: Extract form params (name, email, phone, password, address...)
    RS->>RS: Create User POJO & set fields
    RS->>UD: registerUser(user)
    UD->>DB: INSERT INTO users (...)
    DB-->>UD: rows affected
    UD-->>RS: true / false

    alt Registration Success
        RS->>User: Redirect → /login?success=1
    else Registration Failed
        RS->>User: Forward → register.jsp (with error)
    end
```

### 5.2 User Login Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant LS as LoginServlet
    participant UD as UserDAOImpl
    participant DB as MySQL
    participant Session as HttpSession

    User->>LS: GET /login
    LS->>User: Forward → login.jsp

    User->>LS: POST /login (email, password)
    LS->>UD: loginUser(email, password)
    UD->>DB: SELECT * FROM users WHERE email=? AND password=?
    DB-->>UD: ResultSet
    UD-->>LS: User object (or null)

    alt Login Success
        LS->>Session: setAttribute("user", user)
        LS->>User: Redirect → /products
    else Login Failed
        LS->>User: Forward → login.jsp (with error)
    end
```

### 5.3 Browse Products Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant PS as ProductsServlet
    participant PD as ProductDAOImpl
    participant DB as MySQL

    User->>PS: GET /products?keyword=X or ?category=Y or ?categoryId=Z&minPrice&maxPrice

    alt keyword present
        PS->>PD: searchProducts(keyword)
        PD->>DB: SELECT * FROM products WHERE product_name LIKE ? OR brand LIKE ?
    else category name present
        PS->>PD: getProductsByCategoryName(category)
        PD->>DB: SELECT p.* FROM products p JOIN categories c ... WHERE c.category_name = ?
    else categoryId + price range
        PS->>PD: filterProducts(categoryId, min, max)
        PD->>DB: SELECT * FROM products WHERE category_id=? AND price BETWEEN ? AND ?
    else categoryId only
        PS->>PD: getProductsByCategory(categoryId)
        PD->>DB: SELECT * FROM products WHERE category_id=?
    else no params
        PS->>PD: getAllProducts()
        PD->>DB: SELECT * FROM products WHERE is_active = TRUE
    end

    DB-->>PD: ResultSet
    PD-->>PS: List of Product
    PS->>User: Forward → products.jsp (with products list)
```

### 5.4 View Product Details Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant PDS as ProductDetailsServlet
    participant PD as ProductDAOImpl
    participant DB as MySQL

    User->>PDS: GET /product?id=5
    PDS->>PD: getProductById(5)
    PD->>DB: SELECT * FROM products WHERE product_id = 5
    DB-->>PD: Product row
    PD-->>PDS: Product object

    PDS->>PD: getVariantsByProductId(5)
    PD->>DB: SELECT * FROM product_variants WHERE product_id = 5
    DB-->>PD: Variant rows
    PD-->>PDS: List of ProductVariant

    PDS->>User: Forward → product-details.jsp (product + variants)
```

### 5.5 Add to Cart Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant CS as CartServlet
    participant CID as CartItemDAOImpl
    participant DB as MySQL
    participant Session as HttpSession

    User->>CS: GET /cart?action=add&variantId=3&quantity=2
    CS->>Session: getCartId (default = 1)
    CS->>CS: Create CartItem (cartId, variantId, quantity)

    CS->>CID: addCartItem(cartItem)
    CID->>DB: SELECT cart_item_id, quantity FROM cart_items WHERE cart_id=? AND variant_id=?

    alt Item already exists
        CID->>DB: UPDATE cart_items SET quantity = existing + new WHERE cart_item_id=?
    else New item
        CID->>DB: INSERT INTO cart_items (cart_id, variant_id, quantity)
    end

    DB-->>CID: success
    CID-->>CS: true
    CS->>User: Redirect → /cart
```

### 5.6 View Cart Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant CS as CartServlet
    participant CID as CartItemDAOImpl
    participant DB as MySQL

    User->>CS: GET /cart
    CS->>CS: getCartId from session
    CS->>CID: getCartItems(cartId)
    CID->>DB: SELECT ci.*, p.*, v.size FROM cart_items ci JOIN product_variants v JOIN products p WHERE ci.cart_id=?
    DB-->>CID: Joined rows
    CID-->>CS: List of CartItem (with product info)
    CS->>User: Forward → cart.jsp
```

### 5.7 Checkout Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant COS as CheckoutServlet
    participant CID as CartItemDAOImpl
    participant Session as HttpSession
    participant DB as MySQL

    User->>COS: GET /checkout
    COS->>Session: getCartId
    alt No cartId
        COS->>User: Redirect → /cart
    end

    COS->>Session: getUser
    alt Not logged in
        COS->>User: Redirect → /login
    end

    COS->>Session: Auto-fill delivery fields from User object
    COS->>CID: getCartItems(cartId)
    CID->>DB: SELECT (joined query)
    DB-->>CID: Cart items
    CID-->>COS: List of CartItem

    COS->>User: Forward → checkout.jsp (with cart items + auto-filled address)
```

### 5.8 Place Order Flow (Most Complex)

```mermaid
sequenceDiagram
    actor User as Browser
    participant POS as PlaceOrderServlet
    participant CID as CartItemDAOImpl
    participant OD as OrderDAOImpl
    participant OID as OrderItemDAOImpl
    participant PVD as ProductVariantDAOImpl
    participant DB as MySQL
    participant Session as HttpSession

    User->>POS: POST /place-order (name, phone, address, city, pincode, paymentMethod)
    POS->>Session: Get cartId & user

    POS->>CID: getCartItems(cartId)
    CID->>DB: SELECT (joined query)
    DB-->>CID: Cart items with prices
    CID-->>POS: List of CartItem

    alt Cart empty
        POS->>User: Redirect → /cart
    end

    POS->>POS: Calculate totalAmount (sum of price × qty)
    POS->>POS: Build Order object with delivery details

    POS->>OD: createOrder(order)
    OD->>DB: INSERT INTO orders (...) RETURNING generated key
    DB-->>OD: orderId
    OD-->>POS: orderId

    loop For each CartItem
        POS->>POS: Build OrderItem (orderId, variantId, qty, price)
        POS->>PVD: reduceStock(variantId, quantity)
        PVD->>DB: UPDATE product_variants SET stock_quantity = stock_quantity - ?
    end

    POS->>OID: addOrderItems(orderItems)
    OID->>DB: INSERT INTO order_items (multiple)

    loop Clear cart
        POS->>CID: removeCartItem(cartItemId)
        CID->>DB: DELETE FROM cart_items WHERE cart_item_id=?
    end

    POS->>User: Redirect → /order-confirmation?orderId=X
```

### 5.9 Order Confirmation Flow

```mermaid
sequenceDiagram
    actor User as Browser
    participant OCS as OrderConfirmationServlet
    participant OD as OrderDAOImpl
    participant OID as OrderItemDAOImpl
    participant DB as MySQL

    User->>OCS: GET /order-confirmation?orderId=12
    OCS->>OD: getOrderById(12)
    OD->>DB: SELECT * FROM orders WHERE order_id = 12
    DB-->>OD: Order row
    OD-->>OCS: Order object

    alt Order is null
        OCS->>User: Redirect → /products
    end

    OCS->>OID: getItemsByOrderId(12)
    OID->>DB: SELECT * FROM order_items WHERE order_id = 12
    DB-->>OID: OrderItem rows
    OID-->>OCS: List of OrderItem

    OCS->>User: Forward → order-confirmation.jsp (order + items)
```

---

## 6. Request Routing Map

| HTTP Method | URL Pattern | Servlet | View (JSP) |
|---|---|---|---|
| GET | `/home` | HomeServlet | home.jsp |
| GET | `/login` | LoginServlet | login.jsp |
| POST | `/login` | LoginServlet | Redirect → `/products` |
| GET | `/register` | RegisterServlet | register.jsp |
| POST | `/register` | RegisterServlet | Redirect → `/login` |
| GET | `/products` | ProductsServlet | products.jsp |
| GET | `/product?id=X` | ProductDetailsServlet | product-details.jsp |
| GET | `/cart` | CartServlet | cart.jsp |
| GET | `/cart?action=add` | CartServlet | Redirect → `/cart` |
| GET | `/cart?action=update` | CartServlet | Redirect → `/cart` |
| GET | `/cart?action=remove` | CartServlet | Redirect → `/cart` |
| GET | `/checkout` | CheckoutServlet | checkout.jsp |
| POST | `/place-order` | PlaceOrderServlet | Redirect → `/order-confirmation` |
| GET | `/order-confirmation?orderId=X` | OrderConfirmationServlet | order-confirmation.jsp |

---

## 7. Session Management

```mermaid
graph LR
    subgraph Session["HttpSession Attributes"]
        S1["user → User object (set on login)"]
        S2["cartId → Integer (default 1)"]
        S3["name → String (auto-filled at checkout)"]
        S4["phone → String (auto-filled at checkout)"]
        S5["address → String (auto-filled at checkout)"]
        S6["city → String (auto-filled at checkout)"]
        S7["pincode → String (auto-filled at checkout)"]
    end
```

| Attribute | Type | Set By | Used By |
|---|---|---|---|
| `user` | `User` | LoginServlet (on success) | CheckoutServlet, PlaceOrderServlet |
| `cartId` | `Integer` | CartServlet (default=1) | CartServlet, CheckoutServlet, PlaceOrderServlet |
| `name` | `String` | CheckoutServlet (auto-fill) | checkout.jsp |
| `phone` | `String` | CheckoutServlet (auto-fill) | checkout.jsp |
| `address` | `String` | CheckoutServlet (auto-fill) | checkout.jsp |
| `city` | `String` | CheckoutServlet (auto-fill) | checkout.jsp |
| `pincode` | `String` | CheckoutServlet (auto-fill) | checkout.jsp |
