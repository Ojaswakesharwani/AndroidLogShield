package com.ojaswa  // 👈 ROOT package — do NOT put inside any subfolder!

import com.ojaswa.utils.LogManager  // 👈 Change to your package name

/**
 * ------------------------------------------------------------
 *  AppLog — Project-wide Top-Level Log Functions
 *  Part of: AndroidLogShield
 *  com/Ojaswakesharwani/AndroidLogShield.git
 *
 *  WHY PLACE IN ROOT PACKAGE?
 *  ┌─────────────────────────────────────────────────────┐
 *  │  Kotlin top-level functions in the ROOT package      │
 *  │  are visible EVERYWHERE in your project              │
 *  │  WITHOUT any import statement.                       │
 *  │                                                      │
 *  │  Just call logD(), logE(), logW() anywhere — done!   │
 *  └─────────────────────────────────────────────────────┘
 *
 *  FILE LOCATION:
 *  ✅ app/src/main/java/com/ojaswa/AppLog.kt
 *  ❌ app/src/main/java/com/ojaswa/utils/AppLog.kt  ← needs imports
 *
 *  ⚠️ WARNING: These functions must call LogManager.*
 *  Never call android.util.Log directly here.
 *  Never call these functions from inside LogManager — infinite loop!
 * ------------------------------------------------------------
 */

// ==================== DEBUG ====================

/** Log a debug message. Tag is auto-detected from calling class. */
fun logD(message: String) = LogManager.d(message)

/** Log a debug message with a custom tag. */
fun logD(tag: String, message: String) = LogManager.d(tag, message)

// ==================== ERROR ====================

/** Log an error message. Tag is auto-detected from calling class. */
fun logE(message: String) = LogManager.e(message)

/** Log an error message with optional throwable/exception. */
fun logE(
    tag: String,
    message: String,
    throwable: Throwable? = null
) = LogManager.e(tag, message, throwable)

// ==================== INFO ====================

/** Log an info message. Tag is auto-detected from calling class. */
fun logI(message: String) = LogManager.i(message)

/** Log an info message with a custom tag. */
fun logI(tag: String, message: String) = LogManager.i(tag, message)

// ==================== WARNING ====================

/** Log a warning message. Tag is auto-detected from calling class. */
fun logW(message: String) = LogManager.w(message)

/** Log a warning message with a custom tag. */
fun logW(tag: String, message: String) = LogManager.w(tag, message)

// ==================== VERBOSE ====================

/** Log a verbose message. Tag is auto-detected from calling class. */
fun logV(message: String) = LogManager.v(message)

/** Log a verbose message with a custom tag. */
fun logV(tag: String, message: String) = LogManager.v(tag, message)