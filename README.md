# TheLocker (AppLock NFC)

A modern, minimal Android app that locks selected applications using NFC tags. Built with Jetpack Compose and Material Design 3.

## Features

- **App Locking**: Select which apps to lock from your installed applications
- **NFC Integration**: Toggle lock/unlock by tapping an NFC tag (or use virtual toggle for testing)
- **Auto-Unlock Timer**: Automatically unlock apps after a configurable duration (default: 20 minutes)
- **PIN Protection**: Optional PIN code for manual unlocking
- **Usage Statistics**: Track locked apps, blocked attempts, and unlock events
- **Beautiful UI**: Modern, minimalist design with Material Design 3
- **Clean Architecture**: Built with MVVM, Room database, and Jetpack Compose

## How It Works

1. **Select Apps**: Choose which apps you want to lock
2. **Tap to Lock**: Use the virtual NFC button (or real NFC tag) to enable the lock
3. **Auto-Unlock**: Apps automatically unlock after the configured time (20 minutes by default)
4. **Manual Unlock**: Use PIN code to unlock individual apps when needed

## Setup Instructions

### 1. Build the App

Open the project in Android Studio and build it:

```bash
cd AppLockNFC
./gradlew build
```

### 2. Install on Your Device

Connect your Android device (running Android 15/API 35+) and install:

```bash
./gradlew installDebug
```

### 3. Enable Accessibility Service

**CRITICAL STEP**: The app requires Accessibility Service to detect app launches.

1. Open the app
2. Go to the **Settings** tab
3. Tap "Open Accessibility Settings"
4. Find "AppLock NFC" in the list
5. Enable the service
6. Grant permission when prompted

Without this, the app cannot intercept app launches!

### 4. Select Apps to Lock

1. Go to the **Home** tab
2. Check the boxes next to apps you want to lock
3. Tap the big circular button to toggle lock on/off

### 5. Configure Settings (Optional)

- **Auto-Unlock Duration**: Change how long apps stay locked (5-120 minutes)
- **PIN Code**: Set a 4-6 digit PIN for extra security
- **Statistics**: View your locking history in the Stats tab

## NFC Tag Recommendations

When you're ready to use a physical NFC tag, here are the recommended options:

### Best Options (Cheap & Reliable):

1. **NTAG213 Tags** (Amazon/eBay)
   - Cost: $0.50 - $1.50 per tag
   - Memory: 144 bytes (more than enough)
   - Compatible with all NFC-enabled Android phones
   - Available as stickers, cards, or keychains

2. **NTAG215 Tags**
   - Cost: $0.75 - $2.00 per tag
   - Memory: 504 bytes (overkill for this app, but works great)
   - Also fully compatible

3. **NTAG216 Tags**
   - Cost: $1.00 - $2.50 per tag
   - Memory: 888 bytes
   - Best for future expansion

### Where to Buy:

- **Amazon**: Search "NTAG213 NFC stickers"
- **eBay**: Often cheaper in bulk (10-50 pack)
- **AliExpress**: Very cheap but longer shipping

### Form Factors:

- **Stickers**: Great for placing on desk, wall, or object
- **Cards**: Credit card size, fits in wallet
- **Keychains**: Attach to your keys
- **Wristbands**: Wear it (less convenient for this use case)

### Recommendation:

Get **NTAG213 stickers** (pack of 10) for ~$8-15. Stick one on your desk or any object you want to use as the "lock trigger".

## How the App Works Technically

### Lock Flow:

1. User selects apps to lock
2. User taps virtual button (or NFC tag)
3. Lock state is saved to DataStore
4. Auto-unlock timer starts (20 minutes countdown)
5. AccessibilityService monitors all app launches
6. When locked app is detected, LockOverlayActivity is shown
7. User must unlock via NFC tap or PIN entry

### Architecture:

```
UI Layer (Jetpack Compose)
    ↓
ViewModel Layer
    ↓
Repository Layer
    ↓
Data Layer (Room Database + DataStore)
```

### Key Components:

- **MainActivity**: Main entry point with bottom navigation
- **HomeScreen**: App list and virtual NFC toggle
- **LockOverlayActivity**: Fullscreen overlay when locked app is accessed
- **AppLockAccessibilityService**: Monitors app launches
- **NFCHandler**: Handles NFC tag detection
- **Room Database**: Stores locked apps and usage statistics
- **DataStore**: Stores lock state, PIN, and preferences

## Permissions

- **NFC**: To read NFC tags
- **QUERY_ALL_PACKAGES**: To list installed apps
- **SYSTEM_ALERT_WINDOW**: To show lock overlay
- **ACCESSIBILITY_SERVICE**: To detect app launches (requires manual enable)

## Customization

### Change Auto-Unlock Duration:

Settings → Auto-Unlock Duration → Select time (5-120 minutes)

### Change Lock Timer Default:

Edit `PreferencesManager.kt` line 24:
```kotlin
preferences[AUTO_UNLOCK_DURATION] ?: 20 // Change 20 to your default
```

## Troubleshooting

### Apps aren't getting locked:
- Make sure Accessibility Service is enabled
- Check that apps are selected (checkboxes checked)
- Verify lock is enabled (button shows "LOCKED" in red)

### NFC not working:
- Make sure NFC is enabled on your phone (Settings → Connected devices → NFC)
- Try a different NFC tag (some tags are incompatible)
- Use virtual toggle button for testing

### Timer not working:
- The timer runs in the background
- Force closing the app may reset the timer
- Check Settings to verify the duration

## Technology Stack

- **Kotlin**: Programming language
- **Jetpack Compose**: Modern UI toolkit
- **Material Design 3**: Design system
- **Room**: Local database
- **DataStore**: Preferences storage
- **Coroutines & Flow**: Async programming
- **MVVM Architecture**: Clean architecture pattern
- **AccessibilityService**: App launch detection
- **NFC API**: Physical tag support

## Future Enhancements

Potential features for future versions:

- Schedule-based locking (auto-lock during work hours)
- Multiple NFC tag support (different tags for different app groups)
- Biometric unlock (fingerprint)
- Password unlock option
- Widget for quick toggle
- Themes (dark/light customization)
- Export/import settings
- Cloud sync

## License

This project is open source. Use it, modify it, share it!

## Support

For issues or questions, feel free to open an issue on the repository.

---

**Built with ❤️ using Jetpack Compose and Material Design 3**
