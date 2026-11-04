package com.bzead.india.model

import com.bzead.india.data.Product

data class NotificationItem(
    val id: String,
    val type: String, // "SHIPPING", "PROMOTION", "ORDER"
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class ProductVariant(
    val id: String,
    val name: String,
    val priceDelta: Double = 0.0,
    val size: String? = null,
    val color: String? = null
)

data class Review(
    val author: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val verifiedBuyer: Boolean = true
)

data class DeliveryAddress(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val streetAddress: String,
    val locality: String = "",
    val city: String,
    val state: String = "Karnataka",
    val pinCode: String,
    val isDefault: Boolean = false
) {
    val formatted: String
        get() = listOfNotNull(
            streetAddress,
            locality.ifBlank { null },
            "$city, $state - $pinCode"
        ).joinToString(", ")
}

data class CountryItem(
    val id: String,
    val countryName: String,
    val shortCode: String,
    val currencyCode: String,
    val phoneCode: String
)

enum class OrderStatus(val display: String) {
    PLACED("Order Placed"),
    IN_TRANSIT("In Transit"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

data class OrderItem(
    val product: Product,
    val quantity: Int,
    val unitPrice: Double = product.price,
    val totalPrice: Double = unitPrice * quantity
)

data class Order(
    val id: String,
    val orderNumber: String,
    val date: String,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val subtotal: Double,
    val shippingFee: Double,
    val discount: Double = 0.0,
    val totalAmount: Double = subtotal + shippingFee - discount,
    val shippingAddress: DeliveryAddress,
    val courierName: String = "Shiprocket Express",
    val trackingNumber: String = "SR${(1000000..9999999).random()}",
    val estimatedDeliveryDate: String = "2-3 Days",
    val paymentMethod: String = "UPI (Google Pay / PhonePe)"
)

data class LoginResult(
    val success: Boolean,
    val errorMessage: String? = null,
    val role: String? = "buyer"
)

data class CartItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val product: Product,
    val quantity: Int = 1,
    val selectedVariant: ProductVariant? = null,
    val selectedSize: String? = null,
    val selectedColor: String? = null,
    val unitPrice: Double = product.price + (selectedVariant?.priceDelta ?: 0.0)
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}
