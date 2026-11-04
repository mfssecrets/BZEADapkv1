package com.bzead.india.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzead.india.data.BzeadRepository
import com.bzead.india.model.DeliveryAddress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressManagementScreen(
    onNavigateBack: () -> Unit
) {
    val addresses by BzeadRepository.addresses.collectAsState()
    
    var showFormDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<DeliveryAddress?>(null) }
    
    // Form fields
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var streetAddress by remember { mutableStateOf("") }
    var streetAddress2 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("Karnataka") }
    var pinCode by remember { mutableStateOf("") }
    var addressType by remember { mutableStateOf("home") }
    var isDefault by remember { mutableStateOf(false) }
    
    val openFormForNew = {
        editingAddress = null
        fullName = ""
        phoneNumber = ""
        email = ""
        streetAddress = ""
        streetAddress2 = ""
        city = ""
        state = "Karnataka"
        pinCode = ""
        addressType = "home"
        isDefault = addresses.isEmpty()
        showFormDialog = true
    }
    
    val openFormForEdit = { addr: DeliveryAddress ->
        editingAddress = addr
        fullName = addr.fullName
        phoneNumber = addr.phoneNumber
        email = addr.email
        streetAddress = addr.streetAddress
        streetAddress2 = addr.streetAddress2
        city = addr.city
        state = addr.state
        pinCode = addr.pinCode
        addressType = addr.addressType
        isDefault = addr.isDefault
        showFormDialog = true
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Address Management", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = openFormForNew,
                containerColor = Color(0xFFFFD700),
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
        ) {
            if (addresses.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Saved Addresses", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Add your delivery address for faster checkout.", fontSize = 14.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = openFormForNew,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Address", color = Color.White)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Delivery Addresses (${addresses.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            TextButton(onClick = openFormForNew) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add New")
                            }
                        }
                    }
                    
                    items(addresses, key = { it.id }) { address ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = when (address.addressType.lowercase()) {
                                                "work" -> Color(0xFFDBEAFE)
                                                "other" -> Color(0xFFF3E8FF)
                                                else -> Color(0xFFFEF3C7)
                                            },
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = when (address.addressType.lowercase()) {
                                                        "work" -> Icons.Default.Business
                                                        "other" -> Icons.Default.Place
                                                        else -> Icons.Default.Home
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                    tint = when (address.addressType.lowercase()) {
                                                        "work" -> Color(0xFF1E40AF)
                                                        "other" -> Color(0xFF6B21A8)
                                                        else -> Color(0xFF92400E)
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = address.addressType.uppercase(),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = when (address.addressType.lowercase()) {
                                                        "work" -> Color(0xFF1E40AF)
                                                        "other" -> Color(0xFF6B21A8)
                                                        else -> Color(0xFF92400E)
                                                    }
                                                )
                                            }
                                        }
                                        
                                        if (address.isDefault) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                color = Color(0xFFDCFCE7),
                                                shape = RoundedCornerShape(16.dp)
                                            ) {
                                                Text(
                                                    text = "DEFAULT",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF166534),
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                    
                                    Row {
                                        IconButton(
                                            onClick = { openFormForEdit(address) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF475569))
                                        }
                                        IconButton(
                                            onClick = { BzeadRepository.deleteAddress(address.id) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = address.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = address.formatted, fontSize = 14.sp, color = Color(0xFF475569))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Phone: ${address.phoneNumber}", fontSize = 13.sp, color = Color(0xFF64748B))
                                
                                if (!address.isDefault) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedButton(
                                        onClick = { BzeadRepository.setDefaultAddress(address.id) },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Set as Default Address", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showFormDialog) {
        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = {
                Text(
                    if (editingAddress != null) "Edit Delivery Address" else "Add New Delivery Address",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Mobile Number *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = streetAddress,
                        onValueChange = { streetAddress = it },
                        label = { Text("Flat, House No., Building *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = streetAddress2,
                        onValueChange = { streetAddress2 = it },
                        label = { Text("Area, Street, Locality") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City *") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = { Text("State *") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = { Text("PIN Code (6 digits) *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Address Type", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("home", "work", "other").forEach { type ->
                            FilterChip(
                                selected = addressType == type,
                                onClick = { addressType = type },
                                label = { Text(type.replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it }
                        )
                        Text("Make this my default address", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (fullName.isNotBlank() && phoneNumber.isNotBlank() && streetAddress.isNotBlank() && city.isNotBlank() && pinCode.isNotBlank()) {
                            val newAddr = DeliveryAddress(
                                id = editingAddress?.id ?: "addr_${System.currentTimeMillis()}",
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                streetAddress = streetAddress,
                                streetAddress2 = streetAddress2,
                                city = city,
                                state = state,
                                pinCode = pinCode,
                                addressType = addressType,
                                isDefault = isDefault,
                                email = email
                            )
                            if (editingAddress != null) {
                                BzeadRepository.updateAddress(newAddr)
                            } else {
                                BzeadRepository.addAddress(newAddr)
                            }
                            showFormDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                ) {
                    Text("Save Address", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
