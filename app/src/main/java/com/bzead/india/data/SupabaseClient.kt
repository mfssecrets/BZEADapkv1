package com.bzead.india.data

import com.bzead.india.model.CountryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

object SupabaseClient {

    private const val BASE_URL = "https://aiiefgjfftmerayihpbv.supabase.co/rest/v1"
    private const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFpaWVmZ2pmZnRtZXJheWlocGJ2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzExOTc2MDksImV4cCI6MjA4Njc3MzYwOX0.sbXOq4-CAezq-6mbF7wi5XYVYBakzjb9xjGmU77AZ2M"

    suspend fun fetchBanners(): Pair<List<HeroBanner>, List<AdBanner>> = withContext(Dispatchers.IO) {
        val heroList = mutableListOf<HeroBanner>()
        val adList = mutableListOf<AdBanner>()
        try {
            val url = URL("$BASE_URL/banners?select=*&is_active=eq.true")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("apikey", ANON_KEY)
            conn.setRequestProperty("Authorization", "Bearer $ANON_KEY")
            conn.connectTimeout = 8000
            conn.readTimeout = 8000

            if (conn.responseCode == 200) {
                val jsonText = conn.inputStream.bufferedReader().use { it.readText() }
                val array = JSONArray(jsonText)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.optString("id")
                    val title = obj.optString("title")
                    val imageUrl = obj.optString("image_url")
                    val link = if (obj.has("link") && !obj.isNull("link")) obj.optString("link") else null
                    val bannerType = obj.optString("banner_type", "hero")
                    val adSlot = if (obj.has("ad_slot") && !obj.isNull("ad_slot")) obj.optInt("ad_slot") else 1

                    if (bannerType == "hero") {
                        heroList.add(HeroBanner(id = id, title = title, imageUrl = imageUrl, link = link))
                    } else if (bannerType == "ad") {
                        adList.add(AdBanner(id = id, title = title, imageUrl = imageUrl, link = link, adSlot = adSlot))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Pair(heroList, adList)
    }

    suspend fun fetchCategories(): List<CategoryItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<CategoryItem>()
        try {
            val url = URL("$BASE_URL/categories?select=*&is_active=eq.true&order=display_order.asc&limit=1000")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("apikey", ANON_KEY)
            conn.setRequestProperty("Authorization", "Bearer $ANON_KEY")
            conn.connectTimeout = 8000
            conn.readTimeout = 8000

            if (conn.responseCode == 200) {
                val jsonText = conn.inputStream.bufferedReader().use { it.readText() }
                val array = JSONArray(jsonText)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.optString("id")
                    val name = obj.optString("name")
                    val tag = obj.optString("slug", "Category")
                    val parentId = if (obj.has("parent_id") && !obj.isNull("parent_id")) obj.optString("parent_id") else null
                    val level = obj.optInt("level", 1)
                    val iconUrl = if (obj.has("image_url") && !obj.isNull("image_url")) obj.optString("image_url") 
                                  else if (obj.has("icon_url") && !obj.isNull("icon_url")) obj.optString("icon_url")
                                  else ""
                    list.add(CategoryItem(id = id, name = name, iconUrl = iconUrl, tag = tag, parentId = parentId, level = level))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        list
    }

    suspend fun fetchCategoryTree(): Pair<List<CategoryItem>, Map<String, List<SubCategorySection>>> = withContext(Dispatchers.IO) {
        val allCategories = fetchCategories()
        val mainList = mutableListOf<CategoryItem>()
        val treeMap = mutableMapOf<String, List<SubCategorySection>>()

        if (allCategories.isEmpty()) {
            return@withContext Pair(SampleCatalogData.mainDepartments, SampleCatalogData.departmentSectionsMap)
        }

        // Level 1 Categories (no parent or level 1)
        val l1List = allCategories.filter { it.parentId == null || it.level == 1 }
        
        for (l1 in l1List) {
            val l2Sections = allCategories.filter { it.parentId == l1.id }
            val sectionList = mutableListOf<SubCategorySection>()

            if (l2Sections.isNotEmpty()) {
                for (l2 in l2Sections) {
                    val l3Items = allCategories.filter { it.parentId == l2.id }.map { l3 ->
                        SubCategoryItem(
                            id = l3.id,
                            name = l3.name,
                            iconUrl = l3.iconUrl,
                            parentId = l3.parentId ?: l2.id,
                            slug = l3.tag
                        )
                    }
                    val finalItems = if (l3Items.isNotEmpty()) l3Items else listOf(
                        SubCategoryItem(id = l2.id, name = l2.name, iconUrl = l2.iconUrl, parentId = l2.id, slug = l2.tag)
                    )
                    sectionList.add(SubCategorySection(id = l2.id, title = l2.name, items = finalItems))
                }
            } else {
                // If L1 has no L2 subcategories, create a default section for L1 itself
                sectionList.add(
                    SubCategorySection(
                        id = "${l1.id}_sec",
                        title = l1.name,
                        items = listOf(SubCategoryItem(id = l1.id, name = l1.name, iconUrl = l1.iconUrl, parentId = l1.id, slug = l1.tag))
                    )
                )
            }

            mainList.add(l1)
            treeMap[l1.id] = sectionList
            treeMap[l1.name] = sectionList
            if (l1.tag.isNotBlank()) {
                treeMap[l1.tag] = sectionList
            }
        }

        if (mainList.isEmpty()) {
            Pair(SampleCatalogData.mainDepartments, SampleCatalogData.departmentSectionsMap)
        } else {
            Pair(mainList, treeMap)
        }
    }


    suspend fun fetchProducts(): List<Product> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Product>()
        try {
            val url = URL("$BASE_URL/products?select=*&is_active=eq.true&limit=100")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("apikey", ANON_KEY)
            conn.setRequestProperty("Authorization", "Bearer $ANON_KEY")
            conn.connectTimeout = 8000
            conn.readTimeout = 8000

            if (conn.responseCode == 200) {
                val jsonText = conn.inputStream.bufferedReader().use { it.readText() }
                val array = JSONArray(jsonText)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.optString("id")
                    val title = obj.optString("name", obj.optString("title", "Product"))
                    val brand = obj.optString("brand", "BZEAD")
                    val price = obj.optDouble("default_selling_price", obj.optDouble("price", 499.0))
                    val mrp = if (obj.has("mrp") && !obj.isNull("mrp")) obj.optDouble("mrp") else price * 1.3
                    val description = obj.optString("short_description", obj.optString("description", ""))
                    val imageUrl = obj.optString("image_url", "")
                    val categoryId = if (obj.has("category") && !obj.isNull("category")) obj.optString("category") else "All"
                    val subCategoryId = if (obj.has("sub_category") && !obj.isNull("sub_category")) obj.optString("sub_category") else ""
                    val isFeatured = obj.optBoolean("is_featured", i % 2 == 0)

                    list.add(
                        Product(
                            id = id,
                            title = title,
                            brand = brand,
                            price = price,
                            mrp = mrp,
                            originalPrice = mrp,
                            rating = 4.5f,
                            reviewCount = (20..500).random(),
                            imageUrl = imageUrl,
                            description = description,
                            category = categoryId,
                            categoryId = categoryId,
                            subCategoryId = subCategoryId,
                            isFeatured = isFeatured,
                            isTrending = i % 3 == 0,
                            isDeal = mrp > price
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        list
    }

    suspend fun fetchCountries(): List<CountryItem> = withContext(Dispatchers.IO) {
        listOf(
            CountryItem("in", "India", "IN", "INR", "+91"),
            CountryItem("us", "United States", "US", "USD", "+1"),
            CountryItem("ae", "United Arab Emirates", "AE", "AED", "+971"),
            CountryItem("sg", "Singapore", "SG", "SGD", "+65"),
            CountryItem("uk", "United Kingdom", "GB", "GBP", "+44")
        )
    }
}
