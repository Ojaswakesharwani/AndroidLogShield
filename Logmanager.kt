package com.ojaswa.utils  // 👈 Change to your package name

import android.util.Log
import com.ojaswa.BuildConfig  // 👈 Change to your package name

/**
 * ------------------------------------------------------------
 *  LogManager - Global Logging Controller
 *  Part of: AndroidLogShield
    https://github.com/Ojaswakesharwani/AndroidLogShield.git *
 *  HOW IT WORKS:
 *  ┌─────────────────────────────────────────────┐
 *  │  Debug Build   → logs visible in Logcat  ✅  │
 *  │  Release Build → ALL logs silent         ✅  │
 *  └─────────────────────────────────────────────┘
 *
 *  BuildConfig.DEBUG is automatically set by Gradle.
 *  true  = debug build
 *  false = release/production build
 *
 *  ⚠️ IMPORTANT: This file must call android.util.Log directly.
 *  Never call AppLog (logD/logE etc.) from here — infinite loop!
 * ------------------------------------------------------------
 */
object LogManager {

    // ✅ Automatically true in debug, false in release
    // Gradle sets BuildConfig.DEBUG for you — no manual changes needed
    @Volatile
    var isLoggingEnabled: Boolean = BuildConfig.DEBUG

    // -------------------- AUTO CLASS TAG --------------------
    // Reads the call stack to find which class called the log
    // So you don't need to pass a TAG manually

    private fun autoTag(): String {
        return Throwable().stackTrace
            .firstOrNull { it.className != LogManager::class.java.name }
            ?.className
            ?.substringAfterLast(".")
            ?: "APP_LOG"
    }

    // -------------------- DEBUG --------------------

    /** Log debug message with auto-detected class name as tag */
    fun d(message: String) {
        if (isLoggingEnabled) Log.d(autoTag(), message)
    }

    /** Log debug message with custom tag */
    fun d(tag: String, message: String) {
        if (isLoggingEnabled) Log.d(tag, message)
    }

    // -------------------- ERROR --------------------

    /** Log error message with auto-detected class name as tag */
    fun e(message: String) {
        if (isLoggingEnabled) Log.e(autoTag(), message)
    }

    /** Log error message with optional throwable */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) Log.e(tag, message, throwable)
            else Log.e(tag, message)
        }
    }

    // -------------------- INFO --------------------

    /** Log info message with auto-detected class name as tag */
    fun i(message: String) {
        if (isLoggingEnabled) Log.i(autoTag(), message)
    }

    /** Log info message with custom tag */
    fun i(tag: String, message: String) {
        if (isLoggingEnabled) Log.i(tag, message)
    }

    // -------------------- WARNING --------------------

    /** Log warning message with auto-detected class name as tag */
    fun w(message: String) {
        if (isLoggingEnabled) Log.w(autoTag(), message)
    }

    /** Log warning message with custom tag */
    fun w(tag: String, message: String) {
        if (isLoggingEnabled) Log.w(tag, message)
    }

    // -------------------- VERBOSE --------------------

    /** Log verbose message with auto-detected class name as tag */
    fun v(message: String) {
        if (isLoggingEnabled) Log.v(autoTag(), message)
    }

    /** Log verbose message with custom tag */
    fun v(tag: String, message: String) {
        if (isLoggingEnabled) Log.v(tag, message)
    }

    // -------------------- MANUAL CONTROL --------------------
    // Use these to override the BuildConfig.DEBUG default
    // Useful for testing or special conditions

    /** Force-enable logging (overrides BuildConfig.DEBUG) */
    fun enable()  { isLoggingEnabled = true  }

    /** Force-disable logging (overrides BuildConfig.DEBUG) */
    fun disable() { isLoggingEnabled = false }
}