<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Order History - Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order-history.css">
</head>
<body>

<!-- NAVBAR -->
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container page-container">
    <div class="history-header">
        <h1>Order History</h1>
        <p>All your past orders in one place</p>
    </div>

    <%
        List<Order> recentOrders = (List<Order>) request.getAttribute("recentOrders");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");

        if (recentOrders == null || recentOrders.isEmpty()) {
    %>
        <div class="empty-state">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            <h2>No Orders Yet</h2>
            <p>You haven't placed any orders yet. Start shopping!</p>
            <a href="${pageContext.request.contextPath}/products" class="btn">Start Shopping</a>
        </div>
    <%
        } else {
    %>
        <div class="orders-list">
            <% for (Order order : recentOrders) {
                String status = order.getOrderStatus() != null ? order.getOrderStatus().toUpperCase() : "PLACED";
                // Determine stepper state
                boolean isPlaced    = true;
                boolean isShipped   = status.equals("SHIPPED") || status.equals("DELIVERED") || status.equals("OUT_FOR_DELIVERY");
                boolean isDelivered = status.equals("DELIVERED");
                boolean isCancelled = status.equals("CANCELLED");
            %>
            <div class="order-card fade-in">
                <div class="order-header">
                    <div>
                        <span class="order-id">Order #<%= order.getOrderId() %></span>
                        <span class="order-date"><%= order.getOrderDate() != null ? sdf.format(order.getOrderDate()) : "N/A" %></span>
                    </div>
                    <span class="order-status status-<%= order.getOrderStatus().toLowerCase() %>"><%= order.getOrderStatus() %></span>
                </div>

                <%-- Visual Order Status Stepper --%>
                <% if (!isCancelled) { %>
                <div class="status-stepper">
                    <div class="step <%= isPlaced ? "done" : "" %>">
                        <div class="step-dot">✓</div>
                        <span class="step-label">Order Placed</span>
                    </div>
                    <div class="step <%= isShipped ? "done" : (isPlaced ? "current" : "") %>">
                        <div class="step-dot"><%= isShipped ? "✓" : "🚚" %></div>
                        <span class="step-label">Shipped</span>
                    </div>
                    <div class="step <%= isDelivered ? "done" : (isShipped ? "current" : "") %>">
                        <div class="step-dot"><%= isDelivered ? "✓" : "📦" %></div>
                        <span class="step-label">Delivered</span>
                    </div>
                </div>
                <% } else { %>
                <div style="padding: 12px 0; color: #ff4757; font-weight: 600; font-size: 13px;">
                    ❌ This order was cancelled.
                </div>
                <% } %>

                <div class="order-body">
                    <div class="order-details">
                        <p><strong>Payment Method:</strong> <%= order.getPaymentMethod() %></p>
                        <p><strong>Total Amount:</strong> <span class="price">₹<%= String.format("%.2f", order.getTotalAmount()) %></span></p>
                    </div>
                    <div class="delivery-details">
                        <p><strong>Delivery To:</strong></p>
                        <p><%= order.getDeliveryName() %></p>
                        <p><%= order.getDeliveryAddressLine1() %><%= order.getDeliveryAddressLine2() != null && !order.getDeliveryAddressLine2().isEmpty() ? ", " + order.getDeliveryAddressLine2() : "" %></p>
                        <p><%= order.getDeliveryCity() %>, <%= order.getDeliveryState() %> <%= order.getDeliveryPincode() %></p>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    <%
        }
    %>
    
    <div class="back-link-wrapper">
        <a href="${pageContext.request.contextPath}/profile" class="btn-outline">← Back to Profile</a>
    </div>
</div>

<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
