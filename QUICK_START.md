# Quick Start Guide - AppLock NFC

## What You Have

A complete, production-ready Android app with:

✅ App locking functionality
✅ Virtual NFC toggle (button for testing)
✅ Real NFC tag support (when you buy a tag)
✅ 20-minute auto-unlock timer
✅ PIN code protection
✅ Usage statistics
✅ Beautiful Material Design 3 UI
✅ Modern, minimalist design

## Project Structure

```
AppLockNFC/
├── app/
│   ├── src/main/
│   │   ├── java/com/applock/nfc/
│   │   │   ├── MainActivity.kt              # Main app entry
│   │   │   ├── AppLockApplication.kt        # App initialization
│   │   │   ├── data/                        # Database & models
│   │   │   │   ├── LockedApp.kt            # Locked app entity
│   │   │   │   ├── UsageStatistic.kt       # Stats entity
│   │   │   │   ├── AppLockDao.kt           # Database queries
│   │   │   │   ├── AppLockDatabase.kt      # Room database
│   │   │   │   └── AppLockRepository.kt    # Data management
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt       # Main screen with app list
│   │   │   │   │   ├── HomeViewModel.kt    # Home logic
│   │   │   │   │   ├── LockOverlayActivity.kt # Lock screen
│   │   │   │   │   ├── SettingsScreen.kt   # Settings UI
│   │   │   │   │   └── StatisticsScreen.kt # Stats UI
│   │   │   │   └── theme/                   # Material Design 3 theme
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   ├── service/
│   │   │   │   └── AppLockAccessibilityService.kt # App detection
│   │   │   └── utils/
│   │   │       ├── NFCHandler.kt           # NFC tag handling
│   │   │       └── PreferencesManager.kt   # Settings storage
│   │   ├── res/                             # Resources
│   │   └── AndroidManifest.xml              # App configuration
│   └── build.gradle.kts                     # Dependencies
├── build.gradle.kts                         # Project config
├── settings.gradle.kts
├── README.md                                # Full documentation
├── UI_DESIGN.md                            # Design mockups
└── QUICK_START.md                          # This file
```

## How to Build & Run

### Option 1: Using Android Studio (Recommended)

1. **Open Android Studio**
2. **File → Open** → Select `AppLockNFC` folder
3. **Wait for Gradle sync** to complete
4. **Connect your Android device** (or use emulator)
5. **Click Run** ▶️ button
6. **Install on device**

### Option 2: Using Command Line

```bash
cd AppLockNFC

# Build the app
./gradlew build

# Install on connected device
./gradlew installDebug

# Or run directly
./gradlew installDebug && adb shell am start -n com.applock.nfc/.MainActivity
```

## First-Time Setup (IMPORTANT!)

### Step 1: Enable Accessibility Service ⚠️

**CRITICAL**: Without this, the app won't work!

1. Open the app
2. Go to **Settings** tab (bottom right)
3. Tap **"Open Accessibility Settings"**
4. Find **"AppLock NFC"** in the list
5. Toggle it **ON**
6. Accept the permission dialog

### Step 2: Select Apps to Lock

1. Go to **Home** tab
2. Check boxes next to apps you want to lock
   - Instagram
   - TikTok
   - Facebook
   - Games
   - Etc.

### Step 3: Test the Lock

1. Tap the big circular button (it will turn RED and show "LOCKED")
2. Try to open one of the selected apps
3. You should see the lock overlay screen!
4. Press "Go Back" to return to home

### Step 4 (Optional): Set a PIN

1. Go to **Settings** tab
2. Tap **"Set PIN"**
3. Enter a 4-6 digit PIN
4. Confirm it
5. Now you can unlock apps manually with PIN

## How to Use

### Daily Usage:

1. **Lock apps**: Tap the virtual NFC button (turns RED)
2. **Auto-unlock**: Apps unlock after 20 minutes automatically
3. **Manual unlock**: Enter PIN on lock screen
4. **Toggle anytime**: Tap button to unlock early

### With Real NFC Tag (When You Buy One):

1. Buy an NTAG213 tag (see README.md for recommendations)
2. Just tap your phone on the tag
3. Lock toggles on/off automatically!

## Key Features Explained

### 🎯 Virtual NFC Toggle
- The big circular button at the top
- Simulates an NFC tap
- Tap to lock, tap again to unlock
- Shows countdown timer when locked
- Green = unlocked, Red = locked

### ⏱️ Auto-Unlock Timer
- Default: 20 minutes
- Changeable in Settings (5-120 minutes)
- Countdown shown on virtual button
- Apps automatically unlock when timer expires

### 🔒 Lock Overlay
- Full-screen blocker
- Appears when you try to open a locked app
- Can unlock with PIN (if set)
- Or tap NFC tag to unlock all apps

### 📊 Statistics
- Tracks every lock/unlock event
- Shows blocked app attempts
- Displays usage history
- Useful to see how often you try to open apps!

### ⚙️ Settings
- Enable/disable PIN
- Change auto-unlock duration
- Direct link to Accessibility settings

## Customization

### Change Default Timer Duration

Edit `app/src/main/java/com/applock/nfc/utils/PreferencesManager.kt`:

```kotlin
val autoUnlockDuration: Flow<Int> = context.dataStore.data.map { preferences ->
    preferences[AUTO_UNLOCK_DURATION] ?: 20  // Change 20 to your default
}
```

### Change App Name

Edit `app/src/main/res/values/strings.xml`:

```xml
<string name="app_name">Your App Name</string>
```

### Change Colors

Edit `app/src/main/java/com/applock/nfc/ui/theme/Color.kt`:

```kotlin
val Primary = Color(0xFF6750A4)  // Change to your color
val LockedRed = Color(0xFFE53935)
val UnlockedGreen = Color(0xFF43A047)
```

## Buying an NFC Tag

### What to Buy:
- **NTAG213 NFC Stickers** (pack of 10-20)
- Cost: ~$8-15 on Amazon
- Search: "NTAG213 NFC tags"

### Where to Buy:
- Amazon (fast shipping)
- eBay (cheaper in bulk)
- AliExpress (cheapest but slow)

### Form Factor:
- **Stickers**: Best for desk, wall, door
- **Cards**: Fits in wallet
- **Keychains**: Attach to keys

### Recommendation:
Buy NTAG213 **stickers** and stick one on your desk. Tap phone on it to lock/unlock!

## Troubleshooting

### "Apps aren't locking"
→ Enable Accessibility Service (Settings tab)

### "Virtual button is gray"
→ Select some apps first (checkboxes)

### "Lock screen doesn't show"
→ Check Accessibility Service is enabled
→ Make sure lock is enabled (red button)

### "Timer doesn't work"
→ Don't force-close the app
→ Check auto-unlock duration in Settings

### "Build error"
→ Make sure you're using Android Studio Arctic Fox or newer
→ Sync Gradle files
→ Clean and rebuild project

## Tech Stack Summary

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Database**: Room (SQLite)
- **Preferences**: DataStore
- **Architecture**: MVVM
- **Async**: Coroutines + Flow
- **Min SDK**: 35 (Android 15)
- **Target SDK**: 35

## File Sizes

- APK size: ~5-8 MB (estimated)
- Database size: <1 MB
- Total install: ~10 MB

## Performance

- Minimal battery usage
- No background services (except Accessibility)
- Lightweight Room database
- Efficient Compose UI
- No network calls (fully offline)

## Next Steps

1. Build and install the app
2. Enable Accessibility Service
3. Select apps to lock
4. Test with virtual toggle
5. Buy NFC tags (optional)
6. Enjoy distraction-free productivity!

## Support & Documentation

- **Full docs**: See `README.md`
- **UI design**: See `UI_DESIGN.md`
- **Code**: All files are well-commented

## Future Ideas

Want to enhance the app? Consider adding:
- Widgets
- Biometric unlock
- Schedule-based locking
- Multiple NFC tags
- App usage time limits
- Themes customization

---

**You're all set! Build, install, and enjoy your new app lock! 🚀**
