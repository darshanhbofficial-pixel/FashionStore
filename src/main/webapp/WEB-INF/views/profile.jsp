<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.fashionstore.model.User" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>My Profile - Fashion Store</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
</head>
<body>

<!-- NAVBAR -->
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container">
<div class="page-container">

    <%
        User user = (User) request.getAttribute("user");
    %>

    <div class="profile-wrapper">

        <!-- PROFILE HEADER -->
        <div class="profile-header">
            <div class="profile-avatar">
                <%= user.getFullName().substring(0, 1).toUpperCase() %>
            </div>
            <div class="profile-header-info">
                <h1><%= user.getFullName() %></h1>
                <p class="profile-email"><%= user.getEmail() %></p>
            </div>
        </div>

        <!-- PROFILE DETAILS — VIEW MODE -->
        <div class="profile-sections" id="profileView">

            <!-- PERSONAL INFO -->
            <div class="profile-card">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
                    <h3>Personal Information</h3>
                    <button class="btn-edit" onclick="toggleEdit()" id="editBtn">✏️ Edit Profile</button>
                </div>

                <div class="info-grid profile-view" id="profileViewGrid">
                    <div class="info-item">
                        <span class="info-label">Full Name</span>
                        <span class="info-value"><%= user.getFullName() %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Email Address</span>
                        <span class="info-value"><%= user.getEmail() %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Phone Number</span>
                        <span class="info-value"><%= user.getPhone() != null ? user.getPhone() : "Not provided" %></span>
                    </div>
                </div>

                <!-- EDIT FORM (hidden by default) -->
                <form action="<%= request.getContextPath() %>/profile" method="post" class="profile-edit-form" id="profileEditForm">
                    <h4 style="margin-bottom:16px; color:var(--text-secondary); font-size:14px; font-weight:600;">Personal Details</h4>
                    <div class="profile-input-group">
                        <input type="text" name="fullName" placeholder="Full Name" value="<%= user.getFullName() %>" required>
                        <input type="text" name="phone" placeholder="Phone Number" value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
                    </div>

                    <h4 style="margin:16px 0 12px; color:var(--text-secondary); font-size:14px; font-weight:600;">Shipping Address</h4>
                    <div class="profile-input-group">
                        <input type="text" name="addressLine1" placeholder="Address Line 1" value="<%= user.getAddressLine1() != null ? user.getAddressLine1() : "" %>">
                        <input type="text" name="addressLine2" placeholder="Address Line 2 (optional)" value="<%= user.getAddressLine2() != null ? user.getAddressLine2() : "" %>">
                        <input type="text" name="city" placeholder="City" value="<%= user.getCity() != null ? user.getCity() : "" %>">
                        <input type="text" name="state" placeholder="State" value="<%= user.getState() != null ? user.getState() : "" %>">
                        <input type="text" name="pincode" placeholder="Pincode" value="<%= user.getPincode() != null ? user.getPincode() : "" %>">
                        <input type="text" name="country" placeholder="Country" value="<%= user.getCountry() != null ? user.getCountry() : "" %>">
                    </div>

                    <div style="display:flex; gap:12px; margin-top:8px;">
                        <button type="submit" class="btn btn-small">💾 Save Changes</button>
                        <button type="button" class="btn-edit" onclick="toggleEdit()">Cancel</button>
                    </div>
                </form>
            </div>

            <!-- ADDRESS INFO (View) -->
            <div class="profile-card" id="addressViewCard">
                <h3>Shipping Address</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-label">Address Line 1</span>
                        <span class="info-value"><%= user.getAddressLine1() != null ? user.getAddressLine1() : "Not provided" %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Address Line 2</span>
                        <span class="info-value"><%= user.getAddressLine2() != null ? user.getAddressLine2() : "—" %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">City</span>
                        <span class="info-value"><%= user.getCity() != null ? user.getCity() : "Not provided" %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">State</span>
                        <span class="info-value"><%= user.getState() != null ? user.getState() : "Not provided" %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Pincode</span>
                        <span class="info-value"><%= user.getPincode() != null ? user.getPincode() : "Not provided" %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Country</span>
                        <span class="info-value"><%= user.getCountry() != null ? user.getCountry() : "Not provided" %></span>
                    </div>
                </div>
            </div>

            <!-- CHANGE PASSWORD SECTION -->
            <div class="profile-card" id="change-password">
                <div class="change-password-section">
                    <h3>🔒 Change Password</h3>
                    <form action="<%= request.getContextPath() %>/change-password" method="post">
                        <div class="profile-input-group">
                            <input type="password" name="currentPassword" placeholder="Current Password" required>
                            <input type="password" name="newPassword" placeholder="New Password (min 6 chars)" required minlength="6">
                            <input type="password" name="confirmPassword" placeholder="Confirm New Password" required>
                        </div>
                        <button type="submit" class="btn btn-small" style="margin-top:8px;">Update Password</button>
                    </form>
                </div>
            </div>

        </div>

        <!-- ACTIONS -->
        <div class="profile-actions">
            <a href="${pageContext.request.contextPath}/order-history" class="btn btn-outline" style="border-color: #111; color: #111;">Order History</a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Continue Shopping</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
        </div>

    </div>

</div>
</div>

<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

<script>
function toggleEdit() {
    const editForm = document.getElementById("profileEditForm");
    const viewGrid = document.getElementById("profileViewGrid");
    const addressCard = document.getElementById("addressViewCard");
    const editBtn = document.getElementById("editBtn");

    const isOpen = editForm.classList.contains("open");

    if (isOpen) {
        editForm.classList.remove("open");
        viewGrid.classList.remove("hidden");
        addressCard.style.display = "";
        editBtn.textContent = "✏️ Edit Profile";
    } else {
        editForm.classList.add("open");
        viewGrid.classList.add("hidden");
        addressCard.style.display = "none";
        editBtn.textContent = "✖ Cancel";
        editForm.scrollIntoView({ behavior: "smooth", block: "start" });
    }
}
</script>

</body>
</html>
