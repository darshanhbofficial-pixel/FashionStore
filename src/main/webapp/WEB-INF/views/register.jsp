<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Register - Fashion Store</title>

    <link rel="stylesheet" href="/FashionStore/assets/css/style.css?v=13">
    <link rel="stylesheet" href="/FashionStore/assets/css/auth.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />
 
<div class="auth-wrapper">

    <!-- LEFT SIDE -->
    <div class="auth-left">
        <h1>Join Fashion Store</h1>
        <p>Create your account and start shopping</p>
    </div>

    <!-- RIGHT SIDE -->
    <div class="auth-right">
        <div class="auth-card">

            <h2>Create Account</h2>

            <form action="register" method="post" class="auth-form">

                <input type="text" name="fullName" placeholder="Full Name" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="text" name="phone" placeholder="Phone" required>
                <input type="password" name="password" placeholder="Password" required>

                <input type="text" name="address1" placeholder="Address Line 1">
                <input type="text" name="address2" placeholder="Address Line 2">
                <input type="text" name="city" placeholder="City">
                <input type="text" name="state" placeholder="State">
                <input type="text" name="pincode" placeholder="Pincode">

                <button type="submit">Register</button>

            </form>

            <div class="auth-footer">
                Already have an account? <a href="login">Login</a>
            </div>

        </div>
    </div>

</div>
</body>
</html>