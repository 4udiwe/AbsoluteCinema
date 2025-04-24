package com.example.data.logger

import android.util.Log
import com.example.domain.logger.Logger

class ErrorLogger : Logger {
    override fun log(tag: String, message: String) {
        Log.e(tag, message)
    }
}