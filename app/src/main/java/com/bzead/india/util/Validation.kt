package com.bzead.india.util

object Validation {

    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email address is required"
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(email)) return "Please enter a valid email address"
        return null
    }

    fun validatePassword(password: String): List<String> {
        val errors = mutableListOf<String>()
        if (password.length < 8) {
            errors.add("Password must be at least 8 characters long")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("Include at least one uppercase letter (A-Z)")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("Include at least one lowercase letter (a-z)")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Include at least one number (0-9)")
        }
        return errors
    }

    fun validateFullName(name: String): String? {
        if (name.isBlank()) return "Full name is required"
        if (name.trim().length < 2) return "Full name must be at least 2 characters long"
        return null
    }
}
