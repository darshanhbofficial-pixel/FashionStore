<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.fashionstore.model.Order" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Order Confirmation</title>

    <!-- GLOBAL CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">

    <!-- PAGE CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order-confirmation.css">
</head>

<body>

<!-- ✅ NAVBAR (IMPORTANT) -->
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<%
    Order order = (Order) request.getAttribute("order");
%>

<div class="container">
<div class="page-container">

    <div class="wrapper">

        <div class="card">

            <!-- ICON -->
            <div class="icon">✔</div>

            <h2>Order Placed Successfully</h2>
            <p>Your order has been placed successfully and is now being processed.</p>

            <% if(order != null) { %>

            <!-- ORDER DETAILS -->
            <div class="details">

                <div class="row">
                    <span>Order ID</span>
                    <span><%= order.getOrderId() %></span>
                </div>

                <div class="row">
                    <span>Order Date</span>
                    <span><%= order.getOrderDate() %></span>
                </div>

                <div class="row">
                    <span>Payment Method</span>
                    <span><%= order.getPaymentMethod() %></span>
                </div>

                <div class="row">
                    <span>Status</span>
                    <span><%= order.getOrderStatus() %></span>
                </div>

                <div class="row total">
                    <span>Total Amount</span>
                    <span>₹ <%= order.getTotalAmount() %></span>
                </div>

            </div>

            <!-- DELIVERY DATE ESTIMATE -->
            <%
                java.util.Calendar estimatedDelivery = java.util.Calendar.getInstance();
                if (order.getOrderDate() != null) {
                    estimatedDelivery.setTime(order.getOrderDate());
                }
                int daysAdded = 0;
                while (daysAdded < 5) {
                    estimatedDelivery.add(java.util.Calendar.DAY_OF_MONTH, 1);
                    int dayOfWeek = estimatedDelivery.get(java.util.Calendar.DAY_OF_WEEK);
                    if (dayOfWeek != java.util.Calendar.SATURDAY && dayOfWeek != java.util.Calendar.SUNDAY) {
                        daysAdded++;
                    }
                }
                java.text.SimpleDateFormat deliveryFmt = new java.text.SimpleDateFormat("EEEE, dd MMM yyyy");
                String estimatedDate = deliveryFmt.format(estimatedDelivery.getTime());
            %>
            <div class="delivery-estimate">
                <span class="icon">🚚</span>
                <div class="text">
                    Expected Delivery by<br>
                    <span class="date"><%= estimatedDate %></span>
                </div>
            </div>

            <!-- DELIVERY -->
            <div class="delivery">
                <h3>Delivery Details</h3>

                <p><%= order.getDeliveryName() %></p>
                <p><%= order.getDeliveryPhone() %></p>
                <p><%= order.getDeliveryAddressLine1() %></p>
                <p><%= order.getDeliveryCity() %> - <%= order.getDeliveryPincode() %></p>
            </div>

            <% } else { %>

                <!-- ✅ fallback (no crash) -->
                <p style="color:red;">Order details not found.</p>

            <% } %>

        </div>

    </div>

</div>
</div>

<!-- ✅ FOOTER (IMPORTANT) -->
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>