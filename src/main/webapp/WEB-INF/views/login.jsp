<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <meta charset="UTF-8">
    <title>Login - Fashion Store</title>

    <!-- CSS -->
    <link rel="stylesheet" href="assets/css/style.css?v=13">
    <link rel="stylesheet" href="assets/css/auth.css">
</head>

<body>

<!-- NAVBAR -->
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="auth-wrapper">

    <!-- LEFT SIDE -->
    <div class="auth-left">
        <h1>Fashion Store</h1>
        <p>Shop the latest trends with us</p>
    </div>

    <!-- RIGHT SIDE -->
    <div class="auth-right">
        <div class="auth-card">

            <h2>Login</h2>

            <form action="login" method="post" class="auth-form">

                <input type="email" name="email" placeholder="Email Address" required>
                <input type="password" name="password" placeholder="Password" required>

                <button type="submit">Login</button>

            </form>

            <div class="auth-footer">
                Don’t have an account? <a href="register">Register</a>
            </div>

        </div>
    </div>

</div>
</body>
</html>