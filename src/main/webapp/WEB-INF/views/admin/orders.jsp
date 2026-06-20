<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <meta charset="UTF-8">
    <title>Manage Orders | Admin Panel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <style>
        .admin-layout {
            display: grid;
            grid-template-columns: 240px 1fr;
            min-height: 100vh;
        }
        .admin-sidebar {
            background: var(--bg-card);
            border-right: 1px solid var(--border);
            padding: 32px 20px;
        }
        .admin-main {
            padding: 40px;
            background: var(--bg-dark);
        }
        .admin-nav {
            margin-top: 32px;
        }
        .admin-nav-link {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px;
            color: var(--text-secondary);
            text-decoration: none;
            border-radius: var(--radius-md);
            margin-bottom: 8px;
            transition: var(--transition-fast);
        }
        .admin-nav-link:hover, .admin-nav-link.active {
            background: var(--bg-pill);
            color: var(--primary);
        }
        
        /* Table Styling */
        .orders-table-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            padding: 24px;
            margin-top: 24px;
            overflow-x: auto;
            box-shadow: var(--shadow-sm);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }
        th, td {
            padding: 14px 16px;
            border-bottom: 1px solid var(--border);
            font-size: 14px;
        }
        th {
            font-weight: 700;
            color: var(--text-secondary);
            text-transform: uppercase;
            font-size: 12px;
            letter-spacing: 0.5px;
        }
        td {
            color: var(--text-primary);
        }
        tr:last-child td {
            border-bottom: none;
        }
        
        /* Dropdown status styling */
        .status-select {
            padding: 8px 12px;
            background: var(--bg-dark);
            border: 1px solid var(--border);
            border-radius: var(--radius-sm);
            color: var(--text-primary);
            font-family: inherit;
            font-size: 13px;
            cursor: pointer;
        }
        
        .status-badge {
            display: inline-block;
            padding: 4px 10px;
            border-radius: var(--radius-pill);
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
        }
        .status-placed {
            background: rgba(30, 144, 255, 0.15);
            color: #1e90ff;
            border: 1px solid rgba(30, 144, 255, 0.3);
        }
        .status-shipped {
            background: rgba(255, 165, 0, 0.15);
            color: #ffa500;
            border: 1px solid rgba(255, 165, 0, 0.3);
        }
        .status-delivered {
            background: rgba(46, 213, 115, 0.15);
            color: #2ed573;
            border: 1px solid rgba(46, 213, 115, 0.3);
        }
        .status-cancelled {
            background: rgba(255, 71, 87, 0.12);
            color: #ff4757;
            border: 1px solid rgba(255, 71, 87, 0.3);
        }
    </style>
</head>
<body>
    <div class="admin-layout">
        <!-- SIDEBAR -->
        <div class="admin-sidebar">
            <h2 style="font-size: 18px; font-weight: 800; color: var(--primary); margin-bottom: 32px;">CORE PANEL</h2>
            <nav class="admin-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-link">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link">Manage Products</a>
                <a href="#" class="admin-nav-link active">Manage Orders</a>
                <a href="#" class="admin-nav-link">User Directory</a>
                <div style="margin-top: 40px; border-top: 1px solid var(--border); padding-top: 20px;">
                    <a href="${pageContext.request.contextPath}/home" class="admin-nav-link">Back to Store</a>
                </div>
            </nav>
        </div>

        <!-- MAIN CONTENT -->
        <div class="admin-main">
            <header>
                <h1 style="font-size: 32px; font-weight: 800; margin-bottom: 4px;">Orders</h1>
                <p style="color: var(--text-secondary); font-size: 14px;">View order logs, update shipping statuses, or check payment methods.</p>
            </header>

            <!-- ORDERS TABLE -->
            <div class="orders-table-card fade-in">
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Recipient Name</th>
                            <th>Order Date</th>
                            <th>Total Amount</th>
                            <th>Payment</th>
                            <th>Status Badge</th>
                            <th>Change Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Order> orders = (List<Order>) request.getAttribute("orders");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
                            if (orders != null && !orders.isEmpty()) {
                                for (Order o : orders) {
                                    String st = o.getOrderStatus() != null ? o.getOrderStatus().toUpperCase() : "PLACED";
                                    String badgeClass = "status-placed";
                                    if (st.equals("SHIPPED")) badgeClass = "status-shipped";
                                    else if (st.equals("DELIVERED")) badgeClass = "status-delivered";
                                    else if (st.equals("CANCELLED")) badgeClass = "status-cancelled";
                        %>
                        <tr>
                            <td>#<%= o.getOrderId() %></td>
                            <td>
                                <div style="font-weight: 600;"><%= o.getDeliveryName() %></div>
                                <div style="font-size: 12px; color: var(--text-muted);"><%= o.getDeliveryPhone() %></div>
                            </td>
                            <td><%= o.getOrderDate() != null ? sdf.format(o.getOrderDate()) : "N/A" %></td>
                            <td style="font-weight: 700; color: var(--primary);">₹<%= String.format("%.2f", o.getTotalAmount()) %></td>
                            <td><%= o.getPaymentMethod() %></td>
                            <td>
                                <span class="status-badge <%= badgeClass %>"><%= st %></span>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/orders" method="post" style="display: flex; gap: 8px; align-items: center;">
                                    <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                                    <select name="status" class="status-select" onchange="this.form.submit()">
                                        <option value="PLACED" <%= "PLACED".equals(st) ? "selected" : "" %>>Placed</option>
                                        <option value="SHIPPED" <%= "SHIPPED".equals(st) ? "selected" : "" %>>Shipped</option>
                                        <option value="DELIVERED" <%= "DELIVERED".equals(st) ? "selected" : "" %>>Delivered</option>
                                        <option value="CANCELLED" <%= "CANCELLED".equals(st) ? "selected" : "" %>>Cancelled</option>
                                    </select>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 32px;">No orders placed yet</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
