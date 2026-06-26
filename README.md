# CleanShield - Content Blocker & Recovery App

## Overview

CleanShield is a comprehensive Android application designed to help users break free from addictive content (pornography, gambling, predatory loans) while providing recovery and self-improvement tools. It combines multiple layers of protection with behavioral tools to support lasting change.

## Key Features

### Protection Layer
- **VPN DNS Filtering** - Local VPN that intercepts DNS queries and blocks 350+ harmful domains using exact match, parent domain, and keyword-based filtering
- **App Blocker** - Monitors and blocks 61+ dangerous applications (browsers, VPN bypass tools, loan apps, alternative app stores)
- **Accessibility Service** - Real-time app detection that immediately redirects to a blocked screen
- **Device Admin** - Anti-uninstall protection requiring password to disable
- **Auto-Start on Boot** - Protection resumes automatically after device restart
- **Night Mode** - Enhanced restrictions during vulnerable hours (10pm-6am)

### Recovery Tools
- **Recovery Journal** - Daily journaling for tracking thoughts and progress
- **Guided Meditation** - Breathing exercises and mindfulness sessions
- **Emergency Button** - Quick contact to accountability partner
- **Community Support** - Connection to support resources
- **Motivational Quotes** - 15 daily Spanish-language motivational messages

### Statistics & Gamification
- **Clean Day Counter** - Track consecutive days of sobriety
- **Streak Records** - Personal best tracking
- **Blocked Attempts** - See how many times protection saved you
- **Weekly Progress** - Visual progress charts

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin 1.9.20 |
| UI | Jetpack Compose + Material3 |
| DI | Hilt 2.48 |
| Database | Room |
| Background | WorkManager |
| Security | AndroidX Security-Crypto (AES-256) |
| Backend | Firebase (Auth, Firestore, Analytics, Crashlytics) |
| Network | Retrofit + OkHttp |
| Architecture | MVVM + Clean Architecture |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## Architecture

```
com.cleanshield.app/
├── accessibility/     # AccessibilityService for app detection
├── admin/            # DeviceAdminReceiver for uninstall protection
├── api/              # Remote API interfaces
├── blocker/          # Foreground service app monitoring
├── config/           # App configuration
├── data/             # Repository pattern (Room + Firebase)
│   ├── local/        # Room database, DAOs, entities
│   ├── remote/       # Firebase data sources
│   └── repository/   # Data repositories
├── di/               # Hilt dependency injection modules
├── domain/           # Business logic and models
├── feedback/         # User feedback system
├── notifications/    # Firebase Messaging
├── onboarding/       # First-time setup flow
├── receiver/         # Boot receiver
├── referral/         # Referral system
├── subscription/     # Premium subscription management
├── ui/               # Compose UI screens
│   ├── blocked/      # Full-screen block overlay
│   ├── browser/      # Safe built-in browser
│   ├── main/         # Main dashboard
│   ├── recovery/     # Recovery tools
│   ├── settings/     # App settings
│   ├── stats/        # Statistics
│   └── theme/        # Material3 theming
├── utils/            # Constants, encryption, helpers
├── vpn/              # VPN service + DNS filtering
├── widget/           # Home screen widgets
└── worker/           # Background workers
```

## Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Gradle 8.2

### Setup Steps

1. Clone the repository:
```bash
git clone https://github.com/yourusername/CleanShield.git
cd CleanShield
```

2. Add your `google-services.json` to `app/` directory (from Firebase Console)

3. Open in Android Studio and sync Gradle

4. Build and run on a device (VPN requires physical device or API 30+ emulator)

### Required Permissions Setup
After installation, the app guides through enabling:
1. VPN Permission (DNS filtering)
2. Accessibility Service (app detection)
3. Usage Stats Access (foreground monitoring)
4. Device Administrator (uninstall protection)
5. Battery Optimization exemption (background persistence)
6. Notification Permission (Android 13+)

## Business Model

- **Free Tier** - Basic protection (VPN + app blocker)
- **Premium ($4.99/month)** - Full recovery tools, meditation, community, advanced stats
- **Lifetime ($49.99)** - All premium features forever

## Security

- All local preferences encrypted with AES-256-GCM via AndroidX Security-Crypto
- Passwords stored as double SHA-256 hash with salt
- No sensitive data leaves the device without encryption
- VPN filtering is 100% local (no remote proxy)
- Device Admin prevents unauthorized uninstallation

## Privacy

- DNS filtering happens entirely on-device
- No browsing history is sent to remote servers
- Firebase Analytics tracks only aggregate usage patterns
- Users can export/delete all their data at any time

## Contributing

This is a commercial project. For bug reports and feature requests, please open an issue.

## License

Proprietary - All rights reserved.
