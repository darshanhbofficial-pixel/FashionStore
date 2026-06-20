<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.fashionstore.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <meta charset="UTF-8">
    <title>Admin Dashboard | Fashion Store</title>
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
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 24px;
            margin-bottom: 40px;
        }
        .stat-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            padding: 24px;
            border-radius: var(--radius-lg);
            transition: var(--transition);
        }
        .stat-card:hover {
            transform: translateY(-5px);
            border-color: var(--primary);
        }
        .stat-val {
            font-size: 32px;
            font-weight: 800;
            color: var(--text-primary);
            margin-bottom: 4px;
        }
        .stat-label {
            color: var(--text-secondary);
            font-size: 14px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
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
        .quick-actions {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 24px;
        }
        .action-card {
            background: var(--bg-card);
            padding: 32px;
            border-radius: var(--radius-xl);
            border: 1px solid var(--border);
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="admin-layout">
        <!-- SIDEBAR -->
        <div class="admin-sidebar">
            <h2 style="font-size: 18px; font-weight: 800; color: var(--primary); margin-bottom: 32px;">CORE PANEL</h2>
            <nav class="admin-nav">
                <a href="#" class="admin-nav-link active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link">Manage Products</a>
                <a href="${pageContext.request.contextPath}/admin/orders" class="admin-nav-link">Manage Orders</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="admin-nav-link">User Directory</a>
                <div style="margin-top: 40px; border-top: 1px solid var(--border); padding-top: 20px;">
                    <a href="${pageContext.request.contextPath}/home" class="admin-nav-link">Back to Store</a>
                </div>
            </nav>
        </div>

        <!-- MAIN CONTENT -->
        <div class="admin-main">
            <header style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 40px;">
                <h1 style="font-size: 32px; font-weight: 800;">Overview</h1>
                <div class="glass" style="padding: 10px 20px; border-radius: var(--radius-pill); font-size: 14px;">
                    Logged in as: <strong>Admin</strong>
                </div>
            </header>

            <div class="stats-grid">
                <div class="stat-card fade-in" style="animation-delay: 0.1s;">
                    <div class="stat-val"><%= request.getAttribute("totalOrders") %></div>
                    <div class="stat-label">Total Orders</div>
                </div>
                <div class="stat-card fade-in" style="animation-delay: 0.2s;">
                    <div class="stat-val">₹<%= request.getAttribute("totalRevenue") %></div>
                    <div class="stat-label">Revenue</div>
                </div>
                <div class="stat-card fade-in" style="animation-delay: 0.3s;">
                    <div class="stat-val"><%= request.getAttribute("activeUsers") %></div>
                    <div class="stat-label">Active Users</div>
                </div>
                <div class="stat-card fade-in" style="animation-delay: 0.4s;">
                    <div class="stat-val" style="color: #ff4757;"><%= request.getAttribute("lowStockItems") %></div>
                    <div class="stat-label">Low Stock</div>
                </div>
            </div>

            <!-- SALES CHART -->
            <div class="stat-card fade-in" style="margin-bottom: 40px; padding: 24px; animation-delay: 0.45s;">
                <h3 style="margin-bottom: 20px; font-weight: 700; font-size: 16px;">Sales Trend (Last 6 Months)</h3>
                <div style="height: 220px; position: relative;">
                    <canvas id="salesChart"></canvas>
                </div>
            </div>

            <h2 style="margin-bottom: 24px; font-weight: 700;">Quick Management</h2>
            <div class="quick-actions">
                <div class="action-card fade-in" style="animation-delay: 0.5s;">
                    <h3>Inventory Control</h3>
                    <p style="color: var(--text-secondary); margin: 12px 0 24px;">Update stock, change prices, or add new arrivals.</p>
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-small">Go to Products</a>
                </div>
                <div class="action-card fade-in" style="animation-delay: 0.6s;">
                    <h3>Order Fullfillment</h3>
                    <p style="color: var(--text-secondary); margin: 12px 0 24px;">Update shipping status and manage returns.</p>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-small">Go to Orders</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const ctx = document.getElementById('salesChart').getContext('2d');
            const gradient = ctx.createLinearGradient(0, 0, 0, 200);
            gradient.addColorStop(0, 'rgba(224, 169, 109, 0.4)');
            gradient.addColorStop(1, 'rgba(224, 169, 109, 0.0)');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Sales (₹)',
                        data: [12000, 19000, 15000, 25000, 22000, 30000],
                        borderColor: '#e0a96d',
                        borderWidth: 3,
                        backgroundColor: gradient,
                        fill: true,
                        tension: 0.4,
                        pointBackgroundColor: '#e0a96d',
                        pointHoverRadius: 7
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        x: {
                            grid: {
                                display: false
                            },
                            ticks: {
                                color: '#b3abbf'
                            }
                        },
                        y: {
                            grid: {
                                color: 'rgba(224, 169, 109, 0.08)'
                            },
                            ticks: {
                                color: '#b3abbf'
                            }
                        }
                    }
                }
            });
        });
    </script>
</body>
</html>
