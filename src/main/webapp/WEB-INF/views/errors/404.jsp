<%@ page contentType="text/html; charset=UTF-8" isErrorPage="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <meta charset="UTF-8">
    <title>404 - Page Not Found | Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <style>
        .error-container {
            height: 80vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
        }
        .error-code {
            font-size: 120px;
            font-weight: 800;
            line-height: 1;
            margin-bottom: 20px;
            background: linear-gradient(135deg, #fff 0%, #71717a 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .error-msg {
            font-size: 24px;
            color: var(--text-primary);
            margin-bottom: 12px;
        }
        .error-desc {
            color: var(--text-secondary);
            max-width: 500px;
            margin-bottom: 32px;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <div class="container">
        <div class="error-container fade-in">
            <h1 class="error-code">404</h1>
            <h2 class="error-msg">Lost in Style?</h2>
            <p class="error-desc">The page you're looking for doesn't exist or has been moved. Let's get you back to the collection.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn">Return Home</a>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />
</body>
</html>
