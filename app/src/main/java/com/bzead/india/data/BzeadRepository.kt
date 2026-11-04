package com.bzead.india.data

import com.bzead.india.model.CartItem
import com.bzead.india.model.DeliveryAddress
import com.bzead.india.model.LoginResult
import com.bzead.india.model.NotificationItem
import com.bzead.india.model.Order
import com.bzead.india.model.OrderItem
import com.bzead.india.model.OrderStatus
import com.bzead.india.model.ProductVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object BzeadRepository {

    private val _heroBanners = MutableStateFlow<List<HeroBanner>>(SampleCatalogData.liveHeroBanners)
    val heroBanners: StateFlow<List<HeroBanner>> = _heroBanners.asStateFlow()

    private val _adBanners = MutableStateFlow<List<AdBanner>>(SampleCatalogData.liveAdBanners)
    val adBanners: StateFlow<List<AdBanner>> = _adBanners.asStateFlow()

    private val _liveCategories = MutableStateFlow<List<CategoryItem>>(SampleCatalogData.categories)
    val liveCategories: StateFlow<List<CategoryItem>> = _liveCategories.asStateFlow()

    private val _featuredProducts = MutableStateFlow<List<Product>>(SampleCatalogData.products.filter { it.isFeatured })
    val featuredProducts: StateFlow<List<Product>> = _featuredProducts.asStateFlow()

    private val _trendingProducts = MutableStateFlow<List<Product>>(SampleCatalogData.products.filter { it.isTrending })
    val trendingProducts: StateFlow<List<Product>> = _trendingProducts.asStateFlow()

    private val _hotDealsProducts = MutableStateFlow<List<Product>>(SampleCatalogData.products.filter { it.isDeal })
    val hotDealsProducts: StateFlow<List<Product>> = _hotDealsProducts.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(SampleCatalogData.products)
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _mainDepartments = MutableStateFlow<List<CategoryItem>>(SampleCatalogData.mainDepartments)
    val mainDepartments: StateFlow<List<CategoryItem>> = _mainDepartments.asStateFlow()

    private val _categorySectionsMap = MutableStateFlow<Map<String, List<SubCategorySection>>>(SampleCatalogData.departmentSectionsMap)
    val categorySectionsMap: StateFlow<Map<String, List<SubCategorySection>>> = _categorySectionsMap.asStateFlow()

    val allCategories: List<CategoryItem> get() = _liveCategories.value


    private val _isHomeLoading = MutableStateFlow(false)
    val isHomeLoading: StateFlow<Boolean> = _isHomeLoading.asStateFlow()

    private val _isCartLoading = MutableStateFlow(false)
    val isCartLoading: StateFlow<Boolean> = _isCartLoading.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(product = SampleCatalogData.products[0], quantity = 2),
            CartItem(product = SampleCatalogData.products[1], quantity = 1),
            CartItem(product = SampleCatalogData.products[2], quantity = 1)
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _wishlistProductIds = MutableStateFlow<Set<String>>(setOf("prod_1", "prod_3"))
    val wishlistProductIds: StateFlow<Set<String>> = _wishlistProductIds.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow<String?>("Rahul Sharma")
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>("rahul.sharma@example.in")
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _userPhone = MutableStateFlow<String?>("+91 98765 43210")
    val userPhone: StateFlow<String?> = _userPhone.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem("n1", "SHIPPING", "Order Dispatched!", "Your order #BZ-98214 has been dispatched via Shiprocket Express.", "10 mins ago", false),
            NotificationItem("n2", "PROMOTION", "Mega Super Saver Deal 🔥", "Get up to 60% off on Smart Electronics today only.", "2 hours ago", false),
            NotificationItem("n3", "ORDER", "Order Delivered", "Order #BZ-87612 delivered successfully. Tap to review.", "1 day ago", true)
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _addresses = MutableStateFlow<List<DeliveryAddress>>(
        listOf(
            DeliveryAddress("addr_1", "Rahul Sharma", "+91 98765 43210", "Flat 402, Green Avenue", "", "Bellandur", "Bengaluru", "Karnataka", "560103", "home", true),
            DeliveryAddress("addr_2", "Rahul Sharma", "+91 98765 43210", "Plot 12, Cyber City", "", "Gachibowli", "Hyderabad", "Telangana", "500032", "work", false)
        )
    )
    val addresses: StateFlow<List<DeliveryAddress>> = _addresses.asStateFlow()

    val selectedAddress = MutableStateFlow<DeliveryAddress?>(_addresses.value.firstOrNull())

    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                id = "ord_1",
                orderNumber = "BZ-98214",
                date = "03 Nov 2026",
                status = OrderStatus.IN_TRANSIT,
                items = listOf(
                    OrderItem(SampleCatalogData.products[0], 1),
                    OrderItem(SampleCatalogData.products[1], 1)
                ),
                subtotal = 4998.0,
                shippingFee = 0.0,
                shippingAddress = _addresses.value[0],
                courierName = "Shiprocket Express",
                trackingNumber = "SR8492019",
                estimatedDeliveryDate = "Delivery Expected Tomorrow"
            ),
            Order(
                id = "ord_2",
                orderNumber = "BZ-87612",
                date = "28 Oct 2026",
                status = OrderStatus.DELIVERED,
                items = listOf(
                    OrderItem(SampleCatalogData.products[2], 2)
                ),
                subtotal = 898.0,
                shippingFee = 40.0,
                shippingAddress = _addresses.value[0],
                courierName = "Delhivery Air",
                trackingNumber = "DL7392011",
                estimatedDeliveryDate = "Delivered on 30 Oct 2026"
            )
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(
        listOf("Headphones", "Mamaearth Sunscreen", "Smartwatch", "Sneakers", "Coffee Machine")
    )
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    suspend fun refreshHomeContent() {
        _isHomeLoading.value = true
        try {
            val (hero, ad) = SupabaseClient.fetchBanners()
            if (hero.isNotEmpty()) _heroBanners.value = hero
            if (ad.isNotEmpty()) _adBanners.value = ad

            val (mains, tree) = SupabaseClient.fetchCategoryTree()
            if (mains.isNotEmpty()) _mainDepartments.value = mains
            if (tree.isNotEmpty()) _categorySectionsMap.value = tree

            val cats = SupabaseClient.fetchCategories()
            if (cats.isNotEmpty()) _liveCategories.value = cats

            val prods = SupabaseClient.fetchProducts()
            if (prods.isNotEmpty()) {
                _allProducts.value = prods
                val feat = prods.filter { it.isFeatured }
                if (feat.isNotEmpty()) _featuredProducts.value = feat
                val trend = prods.filter { it.isTrending }
                if (trend.isNotEmpty()) _trendingProducts.value = trend
                val deals = prods.filter { it.isDeal }
                if (deals.isNotEmpty()) _hotDealsProducts.value = deals
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isHomeLoading.value = false
        }
    }


    fun syncCartWithBackend() {
        // Keeps cart synced
    }

    fun addToCart(product: Product, variant: ProductVariant? = null) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.product.id == product.id && it.selectedVariant?.id == variant?.id }
        if (existingIndex >= 0) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else {
            currentList.add(CartItem(product = product, quantity = 1, selectedVariant = variant))
        }
        _cartItems.value = currentList
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId)
            return
        }
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == cartItemId || it.product.id == cartItemId }
        if (index >= 0) {
            currentList[index] = currentList[index].copy(quantity = newQuantity)
            _cartItems.value = currentList
        }
    }

    fun removeFromCart(cartItemId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.id == cartItemId || it.product.id == cartItemId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun toggleWishlist(productId: String) {
        val currentSet = _wishlistProductIds.value.toMutableSet()
        if (currentSet.contains(productId)) {
            currentSet.remove(productId)
        } else {
            currentSet.add(productId)
        }
        _wishlistProductIds.value = currentSet
    }

    fun isInCart(productId: String): Boolean {
        return _cartItems.value.any { it.product.id == productId }
    }

    fun getProductById(id: String): Product? {
        return _allProducts.value.firstOrNull { it.id == id }
    }

    fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return emptyList()
        return _allProducts.value.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.brand.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true)
        }
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.value = _notifications.value.map { if (it.id == id) it.copy(isRead = true) else it }
    }

    fun addAddress(address: DeliveryAddress) {
        val list = _addresses.value.toMutableList()
        val isFirst = list.isEmpty()
        val formattedAddress = if (isFirst) address.copy(isDefault = true) else address
        list.add(formattedAddress)
        _addresses.value = list
        if (formattedAddress.isDefault || selectedAddress.value == null) {
            selectedAddress.value = formattedAddress
        }
    }

    fun updateAddress(address: DeliveryAddress) {
        _addresses.value = _addresses.value.map { if (it.id == address.id) address else it }
        if (address.isDefault || selectedAddress.value?.id == address.id) {
            selectedAddress.value = address
        }
    }

    fun deleteAddress(id: String) {
        val updated = _addresses.value.filter { it.id != id }
        _addresses.value = updated
        if (selectedAddress.value?.id == id) {
            selectedAddress.value = updated.firstOrNull()
        }
    }

    fun setDefaultAddress(id: String) {
        _addresses.value = _addresses.value.map { it.copy(isDefault = (it.id == id)) }
        _addresses.value.firstOrNull { it.id == id }?.let {
            selectedAddress.value = it
        }
    }

    fun getProductsBySection(section: String): List<Product> {
        val lowerSection = section.lowercase()
        return _allProducts.value.filter { product ->
            when (lowerSection) {
                "featured" -> product.isFeatured
                "hot-deals", "deals" -> product.isDeal || product.discountPercent > 15
                "trending" -> product.isTrending || product.rating >= 4.5f
                else -> true
            }
        }
    }

    suspend fun submitReview(productId: String, rating: Int, title: String, comment: String): Boolean {
        return true
    }

    suspend fun updateUserProfile(fullName: String, phone: String): Boolean {
        _userName.value = fullName
        _userPhone.value = phone
        return true
    }

    fun placeOrder(paymentMethod: String, address: DeliveryAddress): Order {
        val currentCart = _cartItems.value
        val items = currentCart.map { OrderItem(product = it.product, quantity = it.quantity, unitPrice = it.unitPrice) }
        val subtotal = items.sumOf { it.totalPrice }
        val newOrder = Order(
            id = "ord_${System.currentTimeMillis()}",
            orderNumber = "BZ-${(10000..99999).random()}",
            date = "03 Nov 2026",
            status = OrderStatus.PLACED,
            items = items,
            subtotal = subtotal,
            shippingFee = if (subtotal >= 499.0) 0.0 else 40.0,
            shippingAddress = address,
            paymentMethod = paymentMethod
        )
        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }

    suspend fun login(email: String, password: String): LoginResult {
        _userEmail.value = email
        _userName.value = email.substringBefore("@").replace(".", " ").capitalize()
        _isLoggedIn.value = true
        return LoginResult(success = true)
    }

    suspend fun signUp(email: String, password: String, fullName: String, countryId: String, currency: String): Pair<Boolean, String?> {
        _userEmail.value = email
        _userName.value = fullName
        return Pair(true, null)
    }

    suspend fun verifyOtp(email: String, code: String, type: String): Pair<Boolean, String?> {
        _isLoggedIn.value = true
        return Pair(true, null)
    }

    suspend fun resendOtp(email: String, type: String): Pair<Boolean, String?> {
        return Pair(true, null)
    }

    suspend fun resetPassword(email: String): Pair<Boolean, String?> {
        return Pair(true, null)
    }

    fun logout() {
        _isLoggedIn.value = false
        _userName.value = null
        _userEmail.value = null
    }
}
