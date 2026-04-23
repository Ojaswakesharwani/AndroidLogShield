# 🛡️ AndroidLogShield

> **Zero-dependency Android logging solution — 2 files, copy-paste, done.**  
> Auto-disables ALL logs in production. No more sensitive data leaking in release APKs.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

---

## 😰 The Problem Every Android Developer Faces

You build your app. You add `Log.d(TAG, "user token: $token")` everywhere for debugging.  
Then you ship to production and... **those logs are still there. Visible to anyone with ADB.**

```kotlin
// ❌ This is in your production APK right now
Log.d("Auth", "User token: eyJhbGciOiJIUzI1NiJ9...")
Log.d("Payment", "Card number: 4111-1111-1111-1111")
Log.e("API", "Server response: $sensitiveUserData")
```

**AndroidLogShield fixes this with 2 files and zero configuration.**

---

## ✅ The Solution

```kotlin
// ✅ After AndroidLogShield — safe everywhere
logD("Auth", "User token: $token")      // shows in debug, SILENT in production
logE("Payment", "Error: $message")      // shows in debug, SILENT in production
logW("API", "Warning: $response")       // shows in debug, SILENT in production
```

**Debug build** → logs visible in Logcat ✅  
**Release/Production build** → ALL logs completely silent ✅  
**Zero imports needed** → just call `logD()` anywhere ✅

---

## 📦 Setup — 3 Steps, 2 Minutes

### Step 1 — Copy `LogManager.kt` into your project

Create file at:
```
app/src/main/java/com/yourpackage/utils/LogManager.kt
```

```kotlin
package com.yourpackage.utils  // 👈 change this to your package

import android.util.Log
import com.yourpackage.BuildConfig  // 👈 change this to your package

/**
 * ------------------------------------------------------------
 *  LogManager - Global Logging Controller
 *
 *  HOW IT WORKS:
 *  - Debug build   → isLoggingEnabled = true  → logs visible
 *  - Release build → isLoggingEnabled = false → logs silent
 *
 *  BuildConfig.DEBUG is automatically set by Gradle.
 *  You never need to change anything manually.
 * ------------------------------------------------------------
 */
object LogManager {

    // ✅ Auto-controlled by Gradle — true in debug, false in release
    @Volatile
    var isLoggingEnabled: Boolean = BuildConfig.DEBUG

    // -------------------- AUTO CLASS TAG --------------------
    // Automatically picks the calling class name as tag
    // So you don't need to pass TAG manually if you don't want to

    private fun autoTag(): String {
        return Throwable().stackTrace
            .firstOrNull { it.className != LogManager::class.java.name }
            ?.className
            ?.substringAfterLast(".")
            ?: "APP_LOG"
    }

    // -------------------- DEBUG --------------------

    fun d(message: String) {
        if (isLoggingEnabled) Log.d(autoTag(), message)
    }

    fun d(tag: String, message: String) {
        if (isLoggingEnabled) Log.d(tag, message)
    }

    // -------------------- ERROR --------------------

    fun e(message: String) {
        if (isLoggingEnabled) Log.e(autoTag(), message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) Log.e(tag, message, throwable)
            else Log.e(tag, message)
        }
    }

    // -------------------- INFO --------------------

    fun i(message: String) {
        if (isLoggingEnabled) Log.i(autoTag(), message)
    }

    fun i(tag: String, message: String) {
        if (isLoggingEnabled) Log.i(tag, message)
    }

    // -------------------- WARNING --------------------

    fun w(message: String) {
        if (isLoggingEnabled) Log.w(autoTag(), message)
    }

    fun w(tag: String, message: String) {
        if (isLoggingEnabled) Log.w(tag, message)
    }

    // -------------------- VERBOSE --------------------

    fun v(message: String) {
        if (isLoggingEnabled) Log.v(autoTag(), message)
    }

    fun v(tag: String, message: String) {
        if (isLoggingEnabled) Log.v(tag, message)
    }

    // -------------------- MANUAL CONTROL --------------------
    // Use these if you want to override BuildConfig behavior

    fun enable()  { isLoggingEnabled = true  }
    fun disable() { isLoggingEnabled = false }
}
```

---

### Step 2 — Copy `AppLog.kt` into your root package

Create file at:
```
app/src/main/java/com/yourpackage/AppLog.kt
```

```kotlin
package com.yourpackage  // 👈 ROOT package — NOT inside any subfolder

import com.yourpackage.utils.LogManager  // 👈 change this to your package

/**
 * ------------------------------------------------------------
 *  AppLog — Project-wide Top-Level Log Functions
 *
 *  WHY ROOT PACKAGE?
 *  Top-level functions in root package are visible everywhere
 *  in your project WITHOUT any import statement.
 *
 *  Just call logD(), logE(), logW() anywhere — it works!
 * ------------------------------------------------------------
 */

// -------------------- DEBUG --------------------
fun logD(message: String)                    = LogManager.d(message)
fun logD(tag: String, message: String)       = LogManager.d(tag, message)

// -------------------- ERROR --------------------
fun logE(message: String)                    = LogManager.e(message)
fun logE(
    tag: String,
    message: String,
    throwable: Throwable? = null
)                                            = LogManager.e(tag, message, throwable)

// -------------------- INFO --------------------
fun logI(message: String)                    = LogManager.i(message)
fun logI(tag: String, message: String)       = LogManager.i(tag, message)

// -------------------- WARNING --------------------
fun logW(message: String)                    = LogManager.w(message)
fun logW(tag: String, message: String)       = LogManager.w(tag, message)

// -------------------- VERBOSE --------------------
fun logV(message: String)                    = LogManager.v(message)
fun logV(tag: String, message: String)       = LogManager.v(tag, message)
```

---

### Step 3 — Use anywhere in your project

```kotlin
// In ANY Activity, Fragment, ViewModel, Repository
// ✅ NO import needed — just call directly

class HomeFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logD("User opened HomeFragment")                    // auto tag
        logD("HomeFragment", "User opened home screen")    // custom tag
        logI("HomeFragment", "Data loaded successfully")
        logW("HomeFragment", "Network is slow")
        logE("HomeFragment", "Failed to load", exception)
    }
}
```

---

## 🔁 Already Have `Log.*` Everywhere? Migrate in 5 Minutes

### Open Find & Replace in Android Studio
```
Windows/Linux : Ctrl + Shift + R
Mac           : Cmd  + Shift + R
```

**Enable Regex ✅ | Scope: In Project ✅**

---

### Run These Replacements One by One

**1. Replace `Log.d`**
```
Find    : Log\.d\(([^,]+),\s*(.+?)\)
Replace : logD($1, $2)
```

**2. Replace `Log.e` with throwable**
```
Find    : Log\.e\(([^,]+),\s*([^,]+),\s*(.+?)\)
Replace : logE($1, $2, $3)
```

**3. Replace `Log.e` without throwable**
```
Find    : Log\.e\(([^,]+),\s*(.+?)\)
Replace : logE($1, $2)
```

**4. Replace `Log.w`**
```
Find    : Log\.w\(([^,]+),\s*(.+?)\)
Replace : logW($1, $2)
```

**5. Replace `Log.i`**
```
Find    : Log\.i\(([^,]+),\s*(.+?)\)
Replace : logI($1, $2)
```

**6. Replace `Log.v`**
```
Find    : Log\.v\(([^,]+),\s*(.+?)\)
Replace : logV($1, $2)
```

**7. Replace Timber (if used)**
```
Find    : Timber\.tag\(([^)]+)\)\.d\((.+?)\)
Replace : logD($1, $2)

Find    : Timber\.tag\(([^)]+)\)\.e\((.+?)\)
Replace : logE($1, $2)

Find    : Timber\.d\((.+?)\)
Replace : logD($1)

Find    : Timber\.e\((.+?)\)
Replace : logE($1)
```

---

### Remove Old Imports

**Option A — Regex Replace**
```
Find    : import android\.util\.Log\n
Replace : (leave empty)

Find    : import timber\.log\.Timber\n
Replace : (leave empty)
```

**Option B — Optimize Imports (Easier)**
```
Ctrl + Alt + O   →  removes all unused imports at once
```

---

### Remove `@SuppressLint("LogNotTimber")`
```
Find    : @SuppressLint\("LogNotTimber"\)\n
Replace : (leave empty)
```

---

### Verify — 0 Results for All of These
```
Search (Regex OFF):

Log.d(
Log.e(
Log.i(
Log.w(
Log.v(
Timber.
import android.util.Log
import timber.log.Timber
@SuppressLint("LogNotTimber")
```

---

### Before & After Migration

**Before ❌**
```kotlin
import android.util.Log
import android.annotation.SuppressLint

class MyFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Fragment started")
        Log.w(TAG, "Something might be wrong")
        Log.e(TAG, "Error occurred", exception)
    }

    @SuppressLint("LogNotTimber")
    fun doSomething() {
        Log.d(TAG, "Doing something")
    }
}
```

**After ✅**
```kotlin
// No imports needed!

class MyFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        logD(TAG, "Fragment started")
        logW(TAG, "Something might be wrong")
        logE(TAG, "Error occurred", exception)
    }

    fun doSomething() {
        logD(TAG, "Doing something")
    }
}
```

---

## 📁 File Structure

```
app/
└── src/main/java/
    └── com.yourpackage/
        ├── AppLog.kt                   ← 👈 Place in ROOT package
        └── utils/
            └── LogManager.kt           ← 👈 Place anywhere
```

> ⚠️ **Important:** `AppLog.kt` MUST be in the root package for zero-import usage.  
> If placed inside a sub-package, you'll need to add an import in each file.

---

## ⚙️ How It Works Internally

```
Your code calls logD("tag", "message")
         ↓
    AppLog.kt (top-level function)
         ↓
    LogManager.d("tag", "message")
         ↓
    Checks: isLoggingEnabled?
         ↓              ↓
      YES (debug)     NO (release)
         ↓              ↓
  android.util.Log   silent — nothing happens
```

---

## 🆚 AndroidLogShield vs Timber vs Raw Log

| Feature                        | `android.util.Log` | Timber        | AndroidLogShield |
|-------------------------------|-------------------|---------------|-----------------|
| Auto-disable in production     | ❌                 | ✅             | ✅               |
| Zero imports needed            | ❌                 | ❌             | ✅               |
| Zero dependencies              | ✅                 | ❌             | ✅               |
| Auto class tag                 | ❌                 | ✅             | ✅               |
| Send logs to Crashlytics       | ❌                 | ✅             | ❌               |
| Migration regex guide          | ❌                 | ❌             | ✅               |
| Application class setup needed | ❌                 | ✅             | ❌               |
| 2 files, copy-paste ready      | ✅                 | ❌             | ✅               |

---

## ❓ FAQ

**Q: Does this work with Java files too?**  
A: `LogManager.kt` works from Java. For zero-import usage, call `LogManagerKt.logD()` from Java files, or just import `AppLog.kt` functions manually.

**Q: What if I want logs in release for crash reporting?**  
A: Modify `LogManager.e()` to always log errors regardless of `isLoggingEnabled`, or integrate with Firebase Crashlytics inside the error method.

**Q: What about multi-line Log statements that regex didn't catch?**  
A: Search for remaining `Log.` occurrences (Regex OFF) and fix those manually — usually less than 5 in any project.

**Q: Is this a replacement for Timber?**  
A: For basic production log stripping — yes. For advanced use cases like Crashlytics integration — use Timber alongside this or instead.

**Q: Why not just use ProGuard to strip logs?**  
A: ProGuard stripping only works with `minifyEnabled = true`. Many projects have this off. This solution works regardless of ProGuard settings.

---

## 📜 License

```
Copyright 2024 AndroidLogShield Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 🤝 Contributing

PRs welcome! If you find a Log pattern the regex doesn't handle, open an issue with the pattern and we'll add it to the migration guide.

---

## ⭐ If This Helped You

Give it a star ⭐ — it helps others find this when they search  
*"how to disable Android logs in production"*

---

*Built with ❤️ for Android developers tired of manually removing logs before every release.*
