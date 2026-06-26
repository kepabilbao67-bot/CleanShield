# CleanShield - Quick Start Guide

## 6-Step Setup

### Step 1: Open in Android Studio

```bash
# Clone and open
git clone https://github.com/yourusername/CleanShield.git
```

Open the project folder in Android Studio Hedgehog or later.
Wait for Gradle sync to complete.

### Step 2: Configure Firebase

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named "CleanShield"
3. Add an Android app with package name `com.cleanshield.app`
4. Download `google-services.json`
5. Place it in the `app/` directory
6. Enable Authentication, Firestore, and Analytics in Firebase Console

### Step 3: Build the Project

```bash
./gradlew assembleDebug
```

Or use Android Studio: Build > Make Project (Ctrl+F9)

### Step 4: Deploy to Device

**Important:** VPN features require a physical device or Android 11+ emulator.

1. Enable Developer Options on your device
2. Enable USB Debugging
3. Connect via USB
4. Run the app from Android Studio (Shift+F10)

### Step 5: App First Launch Setup

When the app first launches, follow the setup wizard:

1. **Set Protection Password** - Required to disable any protection
2. **Enable VPN** - Accept the VPN permission dialog
3. **Enable Accessibility** - Follow prompts to enable in Settings
4. **Enable Device Admin** - Accept admin privileges
5. **Grant Usage Stats** - Allow usage access permission
6. **Disable Battery Optimization** - Ensure background persistence

### Step 6: Verify Protection

After setup, verify all layers are working:

- [ ] VPN notification appears in status bar
- [ ] Opening Chrome/Firefox shows blocked screen
- [ ] Trying to uninstall shows admin warning
- [ ] Blocked domains return 0.0.0.0 in DNS
- [ ] App survives device reboot

## Development Tips

### Running Tests
```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

### Building Release
```bash
./gradlew assembleRelease
```

### Common Issues

| Issue | Solution |
|-------|----------|
| VPN not connecting | Check no other VPN app is active |
| Accessibility not detecting | Re-enable service in Settings |
| App killed in background | Disable battery optimization for CleanShield |
| Firebase errors | Verify google-services.json is in app/ |
| Build fails | Run `./gradlew clean` then rebuild |

## Project Structure

```
CleanShield/
├── app/
│   ├── build.gradle.kts          # App-level build config
│   └── src/main/
│       ├── AndroidManifest.xml    # All permissions & components
│       ├── java/com/cleanshield/  # Kotlin source code
│       └── res/                   # Resources (layouts, values, xml)
├── build.gradle.kts               # Root build file
├── settings.gradle.kts            # Module settings
├── gradle.properties              # Gradle configuration
└── gradle/wrapper/                # Gradle wrapper
```
