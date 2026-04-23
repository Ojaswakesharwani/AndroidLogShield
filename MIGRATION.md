# 🔁 Migration Guide — Replace All `Log.*` Calls

> Already have `Log.d`, `Log.e`, `Timber` everywhere?  
> This guide migrates your entire project in under 5 minutes using regex.

---

## Before You Start — Checklist

- [ ] `LogManager.kt` added to your project
- [ ] `AppLog.kt` added to ROOT package
- [ ] Build compiles successfully with 0 errors
- [ ] Create a Git commit/backup before migrating (just in case)

---

## Step 1 — Open Find & Replace in Android Studio

```
Windows / Linux : Ctrl + Shift + R
Mac             : Cmd  + Shift + R
```

In the panel that opens, make sure:
```
☑ Regex     ← must be ON  (Rr button)
☑ Scope     ← set to "In Project"
```

---

## Step 2 — Run Each Replacement

> Copy the Find line → paste → copy Replace line → paste → click **Replace All**

---

### 🔵 Log.d → logD

```
Find    : Log\.d\(([^,]+),\s*(.+?)\)
Replace : logD($1, $2)
```

**Example:**
```kotlin
// Before
Log.d(TAG, "User opened screen")
Log.d("MyFragment", "Data loaded: $data")

// After
logD(TAG, "User opened screen")
logD("MyFragment", "Data loaded: $data")
```

---

### 🔴 Log.e with throwable → logE

> Run this BEFORE the Log.e without throwable rule

```
Find    : Log\.e\(([^,]+),\s*([^,]+),\s*(.+?)\)
Replace : logE($1, $2, $3)
```

**Example:**
```kotlin
// Before
Log.e(TAG, "Network failed", exception)

// After
logE(TAG, "Network failed", exception)
```

---

### 🔴 Log.e without throwable → logE

```
Find    : Log\.e\(([^,]+),\s*(.+?)\)
Replace : logE($1, $2)
```

**Example:**
```kotlin
// Before
Log.e(TAG, "Something went wrong")

// After
logE(TAG, "Something went wrong")
```

---

### 🟡 Log.w → logW

```
Find    : Log\.w\(([^,]+),\s*(.+?)\)
Replace : logW($1, $2)
```

**Example:**
```kotlin
// Before
Log.w(TAG, "Network is slow")

// After
logW(TAG, "Network is slow")
```

---

### 🟢 Log.i → logI

```
Find    : Log\.i\(([^,]+),\s*(.+?)\)
Replace : logI($1, $2)
```

**Example:**
```kotlin
// Before
Log.i(TAG, "Service started")

// After
logI(TAG, "Service started")
```

---

### ⚪ Log.v → logV

```
Find    : Log\.v\(([^,]+),\s*(.+?)\)
Replace : logV($1, $2)
```

**Example:**
```kotlin
// Before
Log.v(TAG, "Verbose detail")

// After
logV(TAG, "Verbose detail")
```

---

### 🌲 Timber → logD / logE (if you used Timber)

**Timber with tag:**
```
Find    : Timber\.tag\(([^)]+)\)\.d\((.+?)\)
Replace : logD($1, $2)

Find    : Timber\.tag\(([^)]+)\)\.e\((.+?)\)
Replace : logE($1, $2)

Find    : Timber\.tag\(([^)]+)\)\.w\((.+?)\)
Replace : logW($1, $2)

Find    : Timber\.tag\(([^)]+)\)\.i\((.+?)\)
Replace : logI($1, $2)
```

**Timber without tag:**
```
Find    : Timber\.d\((.+?)\)
Replace : logD($1)

Find    : Timber\.e\((.+?)\)
Replace : logE($1)

Find    : Timber\.w\((.+?)\)
Replace : logW($1)

Find    : Timber\.i\((.+?)\)
Replace : logI($1)
```

---

## Step 3 — Remove Old Imports

### Option A — Regex Replace (removes from all files at once)
```
Find    : import android\.util\.Log\n
Replace : (leave completely empty)

Find    : import timber\.log\.Timber\n
Replace : (leave completely empty)

Find    : import android\.annotation\.SuppressLint\n
Replace : (leave completely empty — only if solely used for LogNotTimber)
```

### Option B — Optimize Imports (Easier)
```
Ctrl + Alt + O
```
This removes ALL unused imports across the entire project at once.

---

## Step 4 — Remove `@SuppressLint("LogNotTimber")`

```
Find    : @SuppressLint\("LogNotTimber"\)\n
Replace : (leave completely empty)
```

---

## Step 5 — Verify Migration Complete

Turn Regex **OFF** and search for these — all must return **0 results**:

```
Log.d(
Log.e(
Log.i(
Log.w(
Log.v(
Timber.d(
Timber.e(
Timber.tag(
import android.util.Log
import timber.log.Timber
@SuppressLint("LogNotTimber")
```

---

## Step 6 — Build & Test

```
Build → Clean Project
Build → Rebuild Project
Run on device/emulator
```

If 0 build errors → ✅ Migration complete!

---

## ⚠️ Edge Cases — Manual Fix Needed

The regex handles ~95% of cases. These need manual fixing:

### Multi-line log calls
```kotlin
// ❌ Regex won't catch this
Log.d(TAG, "User: ${user.name} " +
           "Age: ${user.age}")

// ✅ Fix manually
logD(TAG, "User: ${user.name} Age: ${user.age}")
```

### Log calls with complex expressions
```kotlin
// ❌ May not parse correctly
Log.e(TAG, buildString { append("Error: ").append(code) })

// ✅ Fix manually
logE(TAG, buildString { append("Error: ").append(code) })
```

### Log inside string templates
```kotlin
// ❌ Won't match
val msg = "State: ${Log.d(TAG, state)}"  // bad practice anyway

// ✅ Refactor to
logD(TAG, "State: $state")
```

---

## Quick Reference Card

| Old Code | New Code |
|----------|----------|
| `Log.d(TAG, msg)` | `logD(TAG, msg)` |
| `Log.e(TAG, msg)` | `logE(TAG, msg)` |
| `Log.e(TAG, msg, ex)` | `logE(TAG, msg, ex)` |
| `Log.w(TAG, msg)` | `logW(TAG, msg)` |
| `Log.i(TAG, msg)` | `logI(TAG, msg)` |
| `Log.v(TAG, msg)` | `logV(TAG, msg)` |
| `Timber.d(msg)` | `logD(msg)` |
| `Timber.tag(t).d(msg)` | `logD(t, msg)` |

---

*Migration complete? Give the repo a ⭐ — it helps others find it!*
