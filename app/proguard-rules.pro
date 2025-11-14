# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Room entities
-keep class com.applock.nfc.data.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }

# Keep NFC classes
-keep class android.nfc.** { *; }
