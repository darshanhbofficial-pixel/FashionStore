<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page isELIgnored="false" %>
        <%@ page import="java.util.*, java.math.BigDecimal, com.fashionstore.model.CartItem" %>

            <!DOCTYPE html>
            <html>

            <head>
                <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
                <title>Checkout</title>

                <!-- GLOBAL CSS -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">

                <!-- CHECKOUT CSS -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/checkout.css?v=2">
            </head>

            <body>

                <!-- NAVBAR -->
                <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

                <div class="container">
                    <div class="page-container">

                        <h2>Checkout</h2>

                        <div class="checkout-container">

                            <!-- LEFT: FORM -->
                            <div class="checkout-card">

                                <h3>Shipping Details</h3>

                                <form action="place-order" method="post" id="checkoutForm">

                                    <!-- AUTO-FILLED FIELDS -->
                                    <input type="text" name="name" value="${sessionScope.name}" placeholder="Full Name"
                                        required>

                                    <input type="text" name="phone" value="${sessionScope.phone}"
                                        placeholder="Phone Number" required>

                                    <input type="text" name="address" value="${sessionScope.address}"
                                        placeholder="Address" required>

                                    <input type="text" name="city" value="${sessionScope.city}" placeholder="City"
                                        required>

                                    <input type="text" name="pincode" value="${sessionScope.pincode}"
                                        placeholder="Pincode" required>

                                    <!-- SAVE ADDRESS CHECKBOX -->
                                    <label class="save-address-row">
                                        <input type="checkbox" name="saveAddress" value="true">
                                        💾 Save this address for future orders
                                    </label>

                                    <h3>Payment Method</h3>

                                    <div class="payment-method">
                                        <label class="payment-option">
                                            <input type="radio" name="paymentMethod" value="COD" required>
                                            <span>Cash on Delivery</span>
                                        </label>
                                        <label class="payment-option">
                                            <input type="radio" name="paymentMethod" value="UPI">
                                            <span>UPI</span>
                                        </label>
                                        <label class="payment-option">
                                            <input type="radio" name="paymentMethod" value="CARD">
                                            <span>Card</span>
                                        </label>
                                    </div>

                                    <!-- COUPON CODE -->
                                    <h3 style="margin-top:24px;">Coupon Code</h3>
                                    <div class="coupon-row">
                                        <input type="text" id="couponInput"
                                            placeholder="Enter coupon code (e.g. FASHION10)" maxlength="20">
                                        <button type="button" class="btn btn-small"
                                            onclick="applyCoupon()">Apply</button>
                                    </div>
                                    <p class="coupon-success" id="couponSuccess">✅ Coupon applied! 10% discount added.
                                    </p>
                                    <p class="coupon-error" id="couponError">❌ Invalid coupon code.</p>
                                    <!-- Hidden field to pass applied coupon to servlet -->
                                    <input type="hidden" name="couponCode" id="couponCodeHidden" value="">

                                    <button type="submit" class="btn-checkout">
                                        Place Order
                                    </button>

                                </form>

                            </div>

                            <!-- RIGHT: ORDER SUMMARY -->
                            <div class="order-summary">

                                <h3>Order Summary</h3>

                                <% List<CartItem> items = (List<CartItem>) request.getAttribute("cartItems");
                                        double total = 0;

                                        if (items != null && !items.isEmpty()) {
                                        for (CartItem item : items) {

                                        double price = item.getPrice().doubleValue();
                                        int qty = item.getQuantity();
                                        double itemTotal = price * qty;

                                        total += itemTotal;
                                        %>

                                        <div class="summary-item">
                                            <span>
                                                <%= item.getProductName() %> × <%= qty %>
                                            </span>
                                            <span>₹ <%= itemTotal %></span>
                                        </div>

                                        <% } } else { %>

                                            <p>Your cart is empty</p>

                                            <% } %>

                                                <hr>

                                                <div class="summary-total" id="summaryTotal">
                                                    <span>Total</span>
                                                    <span id="totalDisplay">₹ <%= total %></span>
                                                </div>

                                                <!-- Discount row (hidden until coupon applied) -->
                                                <div class="summary-item" id="discountRow"
                                                    style="display:none; color:#2ed573;">
                                                    <span>🎉 Discount (10%)</span>
                                                    <span id="discountAmount">-₹0</span>
                                                </div>
                                                <div class="summary-item" id="finalRow"
                                                    style="display:none; font-weight:800; font-size:16px;">
                                                    <span>Final Total</span>
                                                    <span id="finalAmount">₹ <%= total %></span>
                                                </div>

                                                <a href="${pageContext.request.contextPath}/cart" class="back-cart">
                                                    ← Back to Cart
                                                </a>

                            </div>

                        </div>

                    </div>
                </div>

                <!-- FOOTER -->
                <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

                <script>
                    const VALID_COUPONS = { "FASHION10": 10 }; // code -> discount%
                    const rawTotal = Number("<%= total %>");

                    function applyCoupon() {
                        const code = document.getElementById("couponInput").value.trim().toUpperCase();
                        const successEl = document.getElementById("couponSuccess");
                        const errorEl = document.getElementById("couponError");
                        const discountRow = document.getElementById("discountRow");
                        const finalRow = document.getElementById("finalRow");

                        successEl.style.display = "none";
                        errorEl.style.display = "none";

                        if (VALID_COUPONS[code] !== undefined) {
                            const pct = VALID_COUPONS[code];
                            const discount = (rawTotal * pct / 100).toFixed(2);
                            const finalTotal = (rawTotal - discount).toFixed(2);

                            document.getElementById("couponCodeHidden").value = code;
                            document.getElementById("discountAmount").textContent = "-₹" + discount;
                            document.getElementById("finalAmount").textContent = "₹ " + finalTotal;

                            discountRow.style.display = "";
                            finalRow.style.display = "";
                            successEl.style.display = "block";
                            showToast("Coupon FASHION10 applied! 10% off 🎉", "success");
                        } else {
                            errorEl.style.display = "block";
                            document.getElementById("couponCodeHidden").value = "";
                            discountRow.style.display = "none";
                            finalRow.style.display = "none";
                            showToast("Invalid coupon code", "error");
                        }
                    }

                    // Allow pressing Enter in coupon input
                    document.getElementById("couponInput").addEventListener("keydown", function (e) {
                        if (e.key === "Enter") { e.preventDefault(); applyCoupon(); }
                    });
                </script>

            </body>

            </html>