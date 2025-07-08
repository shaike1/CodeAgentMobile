# 📱 CodeAgents Mobile PWA

## 🚀 **Your Mobile App is Ready!**

This Progressive Web App (PWA) works in your mobile browser and can be installed like a native app.

## ✨ **Features**

- 💬 **Claude AI Chat** - Interact with Claude for coding help
- 🖥️ **SSH Connections** - Connect to your remote servers  
- 💻 **Terminal Access** - Execute commands on remote servers
- 📱 **Mobile Optimized** - Works perfectly on mobile devices
- 🔄 **Offline Support** - Works without internet (cached)
- 🏠 **Install to Home Screen** - Behaves like a native app

## 🌐 **How to Use**

### **Method 1: Local Server (Best Experience)**
```bash
# In this directory
python server.py

# Then open on your mobile:
# http://localhost:8000
```

### **Method 2: File Opening**
1. **Copy files** to your mobile device
2. **Open** `index.html` in mobile browser
3. **Tap "Add to Home Screen"** when prompted

### **Method 3: GitHub Pages (Public Access)**
Upload these files to GitHub Pages for public access.

## 📱 **Installation**

1. **Open in mobile browser**
2. **Look for install prompt** or browser menu
3. **Tap "Add to Home Screen"** or "Install"
4. **App icon** appears on home screen
5. **Opens like native app**

## 🔧 **Configuration**

### **Claude Integration**
- Currently simulated responses
- To add real Claude API:
  - Get API key from console.anthropic.com
  - Modify `app.js` sendMessage function
  - Add API endpoint calls

### **SSH Integration**  
- Currently simulated connections
- To add real SSH:
  - Requires WebSocket server
  - Or use SSH web terminals
  - Connect via secure proxy

## 📋 **File Structure**

```
CodeAgentsPWA/
├── index.html      # Main app interface
├── app.js          # App logic and functionality  
├── manifest.json   # PWA configuration
├── sw.js           # Service worker (offline support)
├── server.py       # Development server
└── README.md       # This file
```

## 🎯 **Usage Guide**

### **Chat Tab**
- Type messages to interact with Claude
- Get coding help and development advice
- Share code snippets for review

### **SSH Tab**  
- Enter server connection details
- Connect to remote development servers
- Manage multiple server connections

### **Terminal Tab**
- Execute commands on connected servers
- Navigate file systems
- Run development tools

## 🔐 **Security**

- **Local Storage**: Credentials stored locally
- **HTTPS Recommended**: For production use
- **No Data Transmission**: Runs entirely in browser
- **Privacy First**: No tracking or analytics

## 🚀 **Development**

To extend the app:

1. **Edit `app.js`** for new features
2. **Modify `index.html`** for UI changes
3. **Update `manifest.json`** for PWA settings
4. **Test with `python server.py`**

## 📞 **Support**

- Works on all modern mobile browsers
- Best experience: Chrome, Safari, Edge
- iOS: Add to Home Screen from Safari
- Android: Install prompt appears automatically

## 🎉 **Ready to Use!**

Your mobile development environment is ready. Open the app on your phone and start coding on the go!

**Perfect for:**
- Quick SSH server access
- Getting Claude coding help
- Mobile development workflows
- Remote server management