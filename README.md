# CodeAgents Mobile - Android

An Android port of the CodeAgents Mobile iOS app, providing a mobile IDE experience with Claude Code integration.

## Features

- Connect to remote servers via SSH
- Interact with Claude Code through a native Android interface
- Browse and edit files on remote servers
- Execute terminal commands
- Real-time chat with Claude

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **SSH Library**: JSch
- **HTTP Client**: OkHttp + Retrofit
- **Database**: Room
- **Dependency Injection**: Manual/Factory pattern

## Requirements

- Android API 26+ (Android 8.0)
- Kotlin 1.9.0+
- Android Studio Hedgehog or newer

## Project Structure

```
app/src/main/java/com/codeagents/mobile/
├── models/          # Data models (Server, Project, Message, etc.)
├── services/        # SSH, Claude API, and other services
├── ui/              # Jetpack Compose UI components
│   ├── theme/       # App theming
│   ├── ChatScreen.kt
│   ├── FileBrowserScreen.kt
│   ├── TerminalScreen.kt
│   └── MainActivity.kt
├── viewmodels/      # ViewModels for MVVM architecture
└── utils/           # Utility classes
```

## Getting Started

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd CodeAgentsMobileAndroid
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Choose "Open an existing project"
   - Navigate to the project directory

3. **Build and run**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use `./gradlew assembleDebug`

## Configuration

1. **Claude API Key**: Set your Claude API key in the appropriate configuration
2. **SSH Configuration**: Configure your server connections through the app UI

## Architecture

This app follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Models**: Data classes representing servers, projects, messages, and files
- **Services**: Business logic for SSH connections, Claude API, file operations
- **ViewModels**: State management and business logic for UI screens
- **UI (Compose)**: Declarative UI components using Jetpack Compose

## SSH Integration

The app uses JSch library for SSH connections, supporting:
- Password authentication
- File browsing via SFTP
- Command execution
- Multiple concurrent connections

## Claude Integration

Integration with Claude API for:
- Natural language conversations
- Code assistance
- File editing suggestions
- Terminal command help

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request