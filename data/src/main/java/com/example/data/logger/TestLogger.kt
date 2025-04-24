package com.example.data.logger

import com.example.domain.logger.Logger

class TestLogger : Logger {
    val errors = mutableListOf<String>()

    override fun log(tag: String, message: String) {
        errors.add("$tag: $message")
    }
}