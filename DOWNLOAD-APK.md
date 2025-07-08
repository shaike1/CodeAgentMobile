# 📱 Download CodeAgents Mobile APK

## 🚀 **3 Ways to Get Your APK**

### **Method 1: GitHub Actions (Automatic)**
1. **Go to**: https://github.com/shaike1/CodeAgentMobile/actions
2. **Click** on the latest successful build (green checkmark)
3. **Scroll down** to "Artifacts" section
4. **Download** "debug-apk" or "release-apk"

### **Method 2: GitHub Releases (If Available)**
1. **Go to**: https://github.com/shaike1/CodeAgentMobile/releases
2. **Download** the latest APK from releases

### **Method 3: Build Locally**
```bash
# Clone repository
git clone https://github.com/shaike1/CodeAgentMobile.git
cd CodeAgentMobile

# Build APK
./build-apk.sh

# APK will be in: app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 **If GitHub Actions Failed**

### **Online Build Services**
1. **Gitpod**: https://gitpod.io/#https://github.com/shaike1/CodeAgentMobile
2. **Replit**: Import the repository and run `./gradlew assembleDebug`
3. **CodeSandbox**: Load project and build

### **Local Android Studio**
1. **Download** Android Studio
2. **Open** this project
3. **Build** → Build APK(s)

## 📱 **App Features**
- **Claude Account Integration** (uses your subscription)
- **SSH Server Connections** for remote development
- **File Browser & Editor** for mobile coding
- **Terminal Access** for command execution
- **Conversation Sync** across devices

## 🔍 **Troubleshooting**

### **No APK in Artifacts**
- Check if build succeeded (green checkmark)
- Look for error messages in build logs
- Try Method 3 (build locally)

### **Build Failed**
- Check GitHub Actions logs
- Common issues: missing dependencies, SDK problems
- Use local build as backup

### **APK Won't Install**
- Enable "Unknown sources" in Android settings
- Check Android version (requires 8.0+)
- Clear any cached APK files

## 🎯 **Quick Links**
- **Repository**: https://github.com/shaike1/CodeAgentMobile
- **Actions**: https://github.com/shaike1/CodeAgentMobile/actions
- **Issues**: https://github.com/shaike1/CodeAgentMobile/issues

**Need help? Check the GitHub Actions tab for the latest build status!**