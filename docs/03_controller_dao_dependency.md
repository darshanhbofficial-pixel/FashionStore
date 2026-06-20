# FashionStore — Controller ↔ DAO Dependency Map

> This document maps every servlet controller to the exact DAO classes it depends on, the model objects it works with, the JSP views it renders, and the HTTP session attributes it reads/writes.

---

## 1. Complete Dependency Matrix

```mermaid
graph LR
    subgraph Controllers
        C1["HomeServlet"]
        C2["LoginServlet"]
        C3["RegisterServlet"]
        C4["ProductsServlet"]
        C5["ProductDetailsServlet"]
        C6["CartServlet"]
        C7["CheckoutServlet"]
        C8["PlaceOrderServlet"]
        C9["OrderConfirmationServlet"]
    end

    subgraph DAOs
        D1["ProductDAOImpl"]
        D2["UserDAOImpl"]
        D3["CartItemDAOImpl"]
        D4["OrderDAOImpl"]
        D5["OrderItemDAOImpl"]
        D6["ProductVariantDAOImpl"]
    end

    C1 --> D1
    C2 --> D2
    C3 --> D2
    C4 --> D1
    C5 --> D1
    C6 --> D3
    C7 --> D3
    C8 --> D3
    C8 --> D4
    C8 --> D5
    C8 --> D6
    C9 --> D4
    C9 --> D5
```

### Summary Table

| Controller | DAO Dependencies | Models Used | View |
|---|---|---|---|
| HomeServlet | ProductDAO | Product | home.jsp |
| LoginServlet | UserDAO | User | login.jsp |
| RegisterServlet | UserDAO | User | register.jsp |
| ProductsServlet | ProductDAO | Product | products.jsp |
| ProductDetailsServlet | ProductDAO | Product, ProductVariant | product-details.jsp |
| CartServlet | CartItemDAO | CartItem | cart.jsp |
| CheckoutServlet | CartItemDAO | CartItem, User | checkout.jsp |
| PlaceOrderServlet | CartItemDAO, OrderDAO, OrderItemDAO, ProductVariantDAO | CartItem, Order, OrderItem, User | *(redirect only)* |
| OrderConfirmationServlet | OrderDAO, OrderItemDAO | Order, OrderItem | order-confirmation.jsp |

---

## 2. Per-Controller Deep Dive

### 2.1 HomeServlet (`/home`)

**Source**: [HomeServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/HomeServlet.java)

```mermaid
graph LR
    HS["HomeServlet"] -- "init()" --> PD["ProductDAOImpl"]
    HS -- "doGet()" --> PD
    PD -- "getAllProducts()" --> DB["products table"]
    HS -- "forward" --> JSP["home.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET |
| **DAO Injected** | `ProductDAO` → `new ProductDAOImpl()` |
| **DAO Methods Called** | `getAllProducts()` |
| **Request Attributes Set** | `products` → `List<Product>` |
| **Session Usage** | None |
| **Forward/Redirect** | Forward → `/WEB-INF/views/home.jsp` |

---

### 2.2 LoginServlet (`/login`)

**Source**: [LoginServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/LoginServlet.java)

```mermaid
graph LR
    LS["LoginServlet"] -- "init()" --> UD["UserDAOImpl"]

    subgraph "doGet()"
        G["Forward → login.jsp"]
    end

    subgraph "doPost()"
        LS -- "loginUser(email, pwd)" --> UD
        UD -- "User or null" --> LS
        LS -- "success" --> S["Session: user=User"]
        S --> R["Redirect → /products"]
        LS -- "failure" --> F["Forward → login.jsp + error"]
    end
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET, POST |
| **DAO Injected** | `UserDAO` → `new UserDAOImpl()` |
| **DAO Methods Called** | `loginUser(email, password)` |
| **Request Attributes Set** | `error` → `String` (on failure) |
| **Session Writes** | `user` → `User` object (on success) |
| **Forward/Redirect** | GET: Forward → login.jsp · POST success: Redirect → `/products` · POST fail: Forward → login.jsp |

---

### 2.3 RegisterServlet (`/register`)

**Source**: [RegisterServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/RegisterServlet.java)

```mermaid
graph LR
    RS["RegisterServlet"] -- "init()" --> UD["UserDAOImpl"]

    subgraph "doPost()"
        RS -- "Extract 10 form fields" --> U["new User()"]
        U -- "set all fields" --> RS
        RS -- "registerUser(user)" --> UD
        UD -- "true" --> R1["Redirect → /login?success=1"]
        UD -- "false" --> R2["Forward → register.jsp + error"]
    end
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET, POST |
| **DAO Injected** | `UserDAO` → `new UserDAOImpl()` |
| **DAO Methods Called** | `registerUser(user)` |
| **Form Parameters Read** | `fullName`, `email`, `phone`, `password`, `addressLine1`, `addressLine2`, `city`, `state`, `pincode`, `country` |
| **Request Attributes Set** | `error` → `String` (on failure) |
| **Forward/Redirect** | GET: Forward → register.jsp · POST success: Redirect → `/login?success=1` · POST fail: Forward → register.jsp |

---

### 2.4 ProductsServlet (`/products`)

**Source**: [ProductsServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/ProductsServlet.java)

```mermaid
graph TD
    PS["ProductsServlet doGet()"] --> CHECK{"Check params"}
    CHECK -- "keyword present" --> S["searchProducts(keyword)"]
    CHECK -- "category name present" --> CN["getProductsByCategoryName(name)"]
    CHECK -- "categoryId + min + max" --> F["filterProducts(id, min, max)"]
    CHECK -- "categoryId only" --> C["getProductsByCategory(id)"]
    CHECK -- "no params" --> A["getAllProducts()"]

    S --> PD["ProductDAOImpl"]
    CN --> PD
    F --> PD
    C --> PD
    A --> PD

    PD --> JSP["Forward → products.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET |
| **DAO Injected** | `ProductDAO` → `new ProductDAOImpl()` |
| **DAO Methods Called** | `searchProducts()`, `getProductsByCategoryName()`, `filterProducts()`, `getProductsByCategory()`, `getAllProducts()` |
| **Query Parameters** | `keyword`, `category`, `categoryId`, `minPrice`, `maxPrice` |
| **Request Attributes Set** | `products` → `List<Product>` |
| **Forward/Redirect** | Forward → `/WEB-INF/views/products.jsp` |

---

### 2.5 ProductDetailsServlet (`/product`)

**Source**: [ProductDetailsServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/ProductDetailsServlet.java)

```mermaid
graph LR
    PDS["ProductDetailsServlet"] -- "init()" --> PD["ProductDAOImpl"]
    PDS -- "getProductById(id)" --> PD
    PDS -- "getVariantsByProductId(id)" --> PD
    PD -- "Product + List~ProductVariant~" --> PDS
    PDS -- "forward" --> JSP["product-details.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET |
| **DAO Injected** | `ProductDAO` → `new ProductDAOImpl()` |
| **DAO Methods Called** | `getProductById(id)`, `getVariantsByProductId(id)` |
| **Query Parameters** | `id` (product ID) |
| **Request Attributes Set** | `product` → `Product`, `variants` → `List<ProductVariant>` |
| **Forward/Redirect** | Forward → `/WEB-INF/views/product-details.jsp` |

---

### 2.6 CartServlet (`/cart`)

**Source**: [CartServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/CartServlet.java)

```mermaid
graph TD
    CS["CartServlet doGet()"] --> CHECK{"action param"}
    CHECK -- "add" --> ADD["addToCart()"]
    CHECK -- "update" --> UPD["updateCart()"]
    CHECK -- "remove" --> REM["removeItem()"]
    CHECK -- "view / null" --> VIEW["viewCart()"]

    ADD -- "addCartItem(item)" --> CID["CartItemDAOImpl"]
    UPD -- "updateCartItemQuantity(id, qty)" --> CID
    REM -- "removeCartItem(id)" --> CID
    VIEW -- "getCartItems(cartId)" --> CID

    ADD --> R1["Redirect → /cart"]
    UPD --> R2["Redirect → /cart"]
    REM --> R3["Redirect → /cart"]
    VIEW --> JSP["Forward → cart.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET (all actions via query params) |
| **DAO Injected** | `CartItemDAO` → `new CartItemDAOImpl()` |
| **DAO Methods Called** | `addCartItem()`, `updateCartItemQuantity()`, `removeCartItem()`, `getCartItems()` |
| **Query Parameters** | `action` (add/update/remove/view), `variantId`, `quantity`, `cartItemId` |
| **Session Reads** | `cartId` (defaults to 1 if absent) |
| **Request Attributes Set** | `cartItems` → `List<CartItem>` (view action only) |
| **Forward/Redirect** | add/update/remove: Redirect → `/cart` · view: Forward → cart.jsp |

> [!NOTE]
> The `getCartId()` helper currently defaults to `cartId = 1` when no session value exists. This is a temporary implementation — in production, it should be linked to the logged-in user's cart.

---

### 2.7 CheckoutServlet (`/checkout`)

**Source**: [CheckoutServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/CheckoutServlet.java)

```mermaid
graph TD
    COS["CheckoutServlet doGet()"] --> S1{"cartId in session?"}
    S1 -- "No" --> R1["Redirect → /cart"]
    S1 -- "Yes" --> S2{"user in session?"}
    S2 -- "No" --> R2["Redirect → /login"]
    S2 -- "Yes" --> AF["Auto-fill session with user address"]
    AF --> CI["cartItemDAO.getCartItems(cartId)"]
    CI --> CID["CartItemDAOImpl"]
    CID --> JSP["Forward → checkout.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET |
| **DAO Injected** | `CartItemDAO` → `new CartItemDAOImpl()` |
| **DAO Methods Called** | `getCartItems(cartId)` |
| **Session Reads** | `cartId`, `user` |
| **Session Writes** | `name`, `phone`, `address`, `city`, `pincode` (auto-filled from User) |
| **Request Attributes Set** | `cartItems` → `List<CartItem>` |
| **Guard Redirects** | No cartId → `/cart` · No user → `/login` |
| **Forward/Redirect** | Forward → `/WEB-INF/views/checkout.jsp` |

---

### 2.8 PlaceOrderServlet (`/place-order`) — Most Complex

**Source**: [PlaceOrderServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/PlaceOrderServlet.java)

```mermaid
graph TD
    POS["PlaceOrderServlet doPost()"] --> S["Read session: cartId, user"]
    S --> CI["cartItemDAO.getCartItems(cartId)"]
    CI --> CID["CartItemDAOImpl"]
    CID --> CALC["Calculate total amount"]
    CALC --> BUILD["Build Order object"]
    BUILD --> OD["orderDAO.createOrder(order)"]
    OD --> ODAO["OrderDAOImpl"]
    ODAO --> LOOP["For each CartItem:"]
    LOOP --> OI["Build OrderItem"]
    LOOP --> RS["productVariantDAO.reduceStock()"]
    RS --> PVD["ProductVariantDAOImpl"]
    OI --> SAVE["orderItemDAO.addOrderItems(list)"]
    SAVE --> OIDAO["OrderItemDAOImpl"]
    OIDAO --> CLEAR["Remove all cart items"]
    CLEAR --> CID2["CartItemDAOImpl"]
    CID2 --> REDIR["Redirect → /order-confirmation?orderId=X"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | POST |
| **DAOs Injected (4)** | `CartItemDAO` → `CartItemDAOImpl`, `OrderDAO` → `OrderDAOImpl`, `OrderItemDAO` → `OrderItemDAOImpl`, `ProductVariantDAO` → `ProductVariantDAOImpl` |
| **DAO Methods Called** | `getCartItems()`, `createOrder()`, `addOrderItems()`, `reduceStock()`, `removeCartItem()` |
| **Form Parameters Read** | `name`, `phone`, `address`, `city`, `pincode`, `paymentMethod` |
| **Session Reads** | `cartId`, `user` |
| **Forward/Redirect** | Redirect → `/order-confirmation?orderId=X` |

**Execution Steps (in order):**

| Step | Action | DAO Used | Method |
|---|---|---|---|
| 1 | Get cart items | CartItemDAOImpl | `getCartItems(cartId)` |
| 2 | Calculate total | *(in-memory)* | Sum of `price × quantity` |
| 3 | Create order record | OrderDAOImpl | `createOrder(order)` → returns `orderId` |
| 4 | Reduce stock per item | ProductVariantDAOImpl | `reduceStock(variantId, qty)` |
| 5 | Save order items | OrderItemDAOImpl | `addOrderItems(list)` |
| 6 | Clear cart | CartItemDAOImpl | `removeCartItem(id)` per item |
| 7 | Redirect to confirmation | — | HTTP 302 |

---

### 2.9 OrderConfirmationServlet (`/order-confirmation`)

**Source**: [OrderConfirmationServlet.java](file:///c:/Users/Admin/Desktop/FashionStore/src/main/java/com/fashionstore/controller/OrderConfirmationServlet.java)

```mermaid
graph LR
    OCS["OrderConfirmationServlet"] -- "init()" --> OD["OrderDAOImpl"]
    OCS -- "init()" --> OID["OrderItemDAOImpl"]
    OCS -- "getOrderById(id)" --> OD
    OCS -- "getItemsByOrderId(id)" --> OID
    OCS -- "forward" --> JSP["order-confirmation.jsp"]
```

| Aspect | Detail |
|---|---|
| **HTTP Methods** | GET |
| **DAOs Injected (2)** | `OrderDAO` → `OrderDAOImpl`, `OrderItemDAO` → `OrderItemDAOImpl` |
| **DAO Methods Called** | `getOrderById(orderId)`, `getItemsByOrderId(orderId)` |
| **Query Parameters** | `orderId` |
| **Request Attributes Set** | `order` → `Order`, `orderItems` → `List<OrderItem>` |
| **Guard Redirects** | No orderId or order not found → `/products` |
| **Forward/Redirect** | Forward → `/WEB-INF/views/order-confirmation.jsp` |

---

## 3. DAO Instantiation Pattern

All controllers follow the same instantiation pattern — **manual `new` in `init()`**, no dependency injection framework:

```java
@Override
public void init() {
    productDAO = new ProductDAOImpl();   // interface reference, concrete impl
}
```

```mermaid
graph TB
    subgraph "DAO Instantiation Pattern"
        INIT["Servlet.init()"] --> NEW["new XxxDAOImpl()"]
        NEW --> FIELD["Stored as interface-typed field"]
        FIELD --> USE["Used in doGet() / doPost()"]
    end
```

| Controller | Field Declaration | Initialization |
|---|---|---|
| HomeServlet | `private ProductDAO productDAO` | `new ProductDAOImpl()` |
| LoginServlet | `private UserDAO userDAO` | `new UserDAOImpl()` |
| RegisterServlet | `private UserDAO userDAO` | `new UserDAOImpl()` |
| ProductsServlet | `private ProductDAO productDAO` | `new ProductDAOImpl()` |
| ProductDetailsServlet | `private ProductDAO productDAO` | `new ProductDAOImpl()` |
| CartServlet | `private CartItemDAO cartItemDAO` | `new CartItemDAOImpl()` |
| CheckoutServlet | `private CartItemDAO cartItemDAO` | `new CartItemDAOImpl()` |
| PlaceOrderServlet | `private CartItemDAO cartItemDAO` | `new CartItemDAOImpl()` |
| PlaceOrderServlet | `private OrderDAO orderDAO` | `new OrderDAOImpl()` |
| PlaceOrderServlet | `private OrderItemDAO orderItemDAO` | `new OrderItemDAOImpl()` |
| PlaceOrderServlet | `private ProductVariantDAO productVariantDAO` | `new ProductVariantDAOImpl()` |
| OrderConfirmationServlet | `private OrderDAO orderDAO` | `new OrderDAOImpl()` |
| OrderConfirmationServlet | `private OrderItemDAO orderItemDAO` | `new OrderItemDAOImpl()` |

---

## 4. DAO Method Usage Heatmap

Shows which DAO methods are actually invoked by controllers (vs. defined but unused):

### UserDAO

| Method | Used By |
|---|---|
| `registerUser(User)` | RegisterServlet |
| `loginUser(email, pwd)` | LoginServlet |
| `getUserById(int)` | *Not used by any controller* |
| `getUserByEmail(String)` | *Not used by any controller* |
| `getUserByPhone(String)` | *Not used by any controller* |
| `updateUser(User)` | *Not used by any controller* |
| `updatePassword(int, String)` | *Not used by any controller* |
| `deleteUser(int)` | *Not used by any controller* |
| `emailExists(String)` | *Not used by any controller* |
| `phoneExists(String)` | *Not used by any controller* |
| `getAllUsers()` | *Not used by any controller* |

### ProductDAO

| Method | Used By |
|---|---|
| `getAllProducts()` | HomeServlet, ProductsServlet |
| `getProductById(int)` | ProductDetailsServlet |
| `getProductsByCategory(int)` | ProductsServlet |
| `searchProducts(String)` | ProductsServlet |
| `filterProducts(int, double, double)` | ProductsServlet |
| `filterProducts(int, String, double, double)` | *Not used by any controller* |
| `getProductsByCategoryName(String)` | ProductsServlet |
| `getVariantsByProductId(int)` | ProductDetailsServlet |
| `addProduct(Product)` | *Not implemented (returns false)* |
| `updateProduct(Product)` | *Not implemented (returns false)* |
| `deleteProduct(int)` | *Not implemented (returns false)* |

### CartItemDAO

| Method | Used By |
|---|---|
| `addCartItem(CartItem)` | CartServlet |
| `updateCartItemQuantity(int, int)` | CartServlet |
| `updateCartItemQuantityByCartAndVariant(...)` | *Not used (returns false)* |
| `removeCartItem(int)` | CartServlet, PlaceOrderServlet |
| `getCartItems(int)` | CartServlet, CheckoutServlet, PlaceOrderServlet |
| `getCartTotal(int)` | *Not used (returns zero)* |

### OrderDAO

| Method | Used By |
|---|---|
| `createOrder(Order)` | PlaceOrderServlet |
| `getOrderById(int)` | OrderConfirmationServlet |
| `getOrdersByUserId(int)` | *Not used by any controller* |
| `updateOrderStatus(int, String)` | *Not used by any controller* |
| `deleteOrder(int)` | *Not used by any controller* |

### OrderItemDAO

| Method | Used By |
|---|---|
| `addOrderItem(OrderItem)` | *(used internally by addOrderItems)* |
| `addOrderItems(List)` | PlaceOrderServlet |
| `getItemsByOrderId(int)` | OrderConfirmationServlet |
| `deleteOrderItem(int)` | *Not used by any controller* |
| `clearOrderItems(int)` | *Not used by any controller* |

### ProductVariantDAO

| Method | Used By |
|---|---|
| `reduceStock(int, int)` | PlaceOrderServlet |
| `getVariantsByProductId(int)` | *Not used directly (via ProductDAO)* |
| All other methods | *Not used by any controller* |

### CartDAO & CategoryDAO

> [!IMPORTANT]
> `CartDAO` and `CategoryDAO` are **fully defined with complete implementations** but are **not directly used by any controller**. `CartDAO` could be used to manage user-specific carts (currently hardcoded to `cartId=1`). `CategoryDAO` could be used for category listing/filtering UI.

---

## 5. End-to-End User Journey

```mermaid
graph TD
    START["User visits site"] --> HOME["GET /home → HomeServlet"]
    HOME --> REG["GET /register → RegisterServlet"]
    REG --> REGPOST["POST /register → RegisterServlet"]
    REGPOST --> LOGIN["GET /login → LoginServlet"]
    LOGIN --> LOGINPOST["POST /login → LoginServlet"]
    LOGINPOST --> PRODUCTS["GET /products → ProductsServlet"]
    PRODUCTS --> DETAIL["GET /product?id=X → ProductDetailsServlet"]
    DETAIL --> ADDCART["GET /cart?action=add → CartServlet"]
    ADDCART --> VIEWCART["GET /cart → CartServlet"]
    VIEWCART --> CHECKOUT["GET /checkout → CheckoutServlet"]
    CHECKOUT --> PLACE["POST /place-order → PlaceOrderServlet"]
    PLACE --> CONFIRM["GET /order-confirmation?orderId=X → OrderConfirmationServlet"]

    style START fill:#4CAF50,color:#fff
    style CONFIRM fill:#2196F3,color:#fff
```
