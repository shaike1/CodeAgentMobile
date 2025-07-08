#!/bin/bash

echo "🔧 Building CodeAgents Mobile APK..."

# Make gradlew executable
chmod +x gradlew

# Build debug APK
echo "📱 Building debug APK..."
./gradlew assembleDebug

# Check if APK was built successfully
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "✅ Debug APK built successfully!"
    echo "📍 Location: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Debug APK build failed"
    exit 1
fi

# Build release APK
echo "📱 Building release APK..."
./gradlew assembleRelease

# Check if release APK was built successfully
if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
    echo "✅ Release APK built successfully!"
    echo "📍 Location: app/build/outputs/apk/release/app-release-unsigned.apk"
else
    echo "⚠️  Release APK build failed (debug APK is still available)"
fi

echo "🎉 Build process complete!"
echo ""
echo "📥 To install on your device:"
echo "1. Enable 'Unknown sources' in Android settings"
echo "2. Transfer APK to your device"
echo "3. Install the APK"
echo "4. Sign in with your Claude account"