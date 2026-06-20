<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.fashionstore.model.CartItem" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Cart</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css">
</head>

<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container">
<div class="page-container" style="border-radius: var(--radius-lg); overflow: hidden; position: relative; background: linear-gradient(rgba(18, 18, 20, 0.7), rgba(18, 18, 20, 0.7)), url('${pageContext.request.contextPath}/assets/images/empty_cart_bg.png'); background-size: cover; background-position: center; border: 1px solid var(--border); padding: 40px;">

    <h2 style="position: relative; z-index: 10; margin-bottom: 30px;">Your Cart</h2>

    <div style="position: relative; z-index: 10;">
    <%
        List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");

        if (cartItems != null && !cartItems.isEmpty()) {

            double grandTotal = 0;
    %>

    <table>
        <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Size</th>
            <th>Color</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Total</th>
            <th>Action</th>
        </tr>

    <%
        for (CartItem item : cartItems) {

            double price = item.getPrice().doubleValue();
            int qty = item.getQuantity();
            double itemTotal = price * qty;

            grandTotal += itemTotal;
    %>

    <tr>
        <td><img src="<%= item.getImageUrl() %>" /></td>
        <td><%= item.getProductName() %></td>
        <td><%= item.getBrand() %></td>
        <td><%= item.getSize() %></td>
        <td><%= item.getColor() %></td>
        <td>₹ <%= price %></td>

        <td>
            <form action="cart" method="get">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="cartItemId" value="<%= item.getCartItemId() %>">
                <input type="number" name="quantity" value="<%= qty %>" min="1" style="width:50px;">
                <button class="btn update">Update</button>
            </form>
        </td>

        <td>₹ <%= itemTotal %></td>

        <td>
            <a href="cart?action=remove&cartItemId=<%= item.getCartItemId() %>">
                <button class="btn remove">Remove</button>
            </a>
        </td>
    </tr>

    <%
        }
    %>

    </table>

    <div class="cart-summary" style="display: flex; flex-direction: column; align-items: flex-end; width: 100%; margin-top: 30px; gap: 12px;">
        <div class="total" style="font-size: 24px; font-weight: 800; color: var(--text-primary);">
            Grand Total: ₹ <%= grandTotal %>
        </div>
        <a href="checkout" style="width: 100%; max-width: 250px;">
            <button class="btn" style="width: 100%; padding: 14px; background: white; color: black; border: none; border-radius: var(--radius-pill); font-weight: 800; cursor: pointer; box-shadow: var(--shadow-md);">
                Proceed to Checkout
            </button>
        </a>
    </div>

    <%
        } else {
            Object loggedInUser = session.getAttribute("user");
    %>

    <div style="text-align:center; padding: 60px 20px;">
        <h3 style="margin-bottom: 20px; font-size: 28px; font-weight: 800;">Your cart is empty 🛒</h3>
        <% if (loggedInUser == null) { %>
            <p style="color: var(--text-primary); margin-bottom: 30px; font-size: 16px; opacity: 0.9;">Login to see your saved items and start shopping!</p>
            <a href="login" class="btn">Login Now</a>
        <% } else { %>
            <p style="color: var(--text-primary); margin-bottom: 30px; font-size: 16px; opacity: 0.9;">Looks like you haven't added anything yet.</p>
            <a href="products" class="btn">Shop Now</a>
        <% } %>
    </div>

    <%
        }
    %>
    </div>
</div>
</div>

</div>
</div>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>