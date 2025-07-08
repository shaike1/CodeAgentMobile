// CodeAgents Mobile PWA - JavaScript

// App State
let currentTab = 'chat';
let sshConnected = false;
let claudeApiKey = localStorage.getItem('claudeApiKey') || '';
let messages = [];

// PWA Installation
let deferredPrompt;

window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    document.getElementById('install-prompt').style.display = 'block';
});

document.getElementById('install-btn').addEventListener('click', () => {
    if (deferredPrompt) {
        deferredPrompt.prompt();
        deferredPrompt.userChoice.then((choiceResult) => {
            if (choiceResult.outcome === 'accepted') {
                console.log('User accepted the install prompt');
                document.getElementById('install-prompt').style.display = 'none';
            }
            deferredPrompt = null;
        });
    }
});

// Tab Navigation
function showTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Remove active class from all buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected tab
    document.getElementById(tabName + '-tab').classList.add('active');
    
    // Add active class to clicked button
    event.target.classList.add('active');
    
    currentTab = tabName;
}

// Chat Functions
function sendMessage() {
    const input = document.getElementById('message-input');
    const message = input.value.trim();
    
    if (!message) return;
    
    // Add user message
    addMessage('user', message);
    input.value = '';
    
    // Simulate Claude response
    setTimeout(() => {
        if (message.toLowerCase().includes('ssh') || message.toLowerCase().includes('server')) {
            addMessage('assistant', `I can help you with SSH connections! ${sshConnected ? 'You\'re currently connected to your server.' : 'Connect to your SSH server first using the SSH tab, then I can help you with file operations and terminal commands.'}`);
        } else if (message.toLowerCase().includes('help')) {
            addMessage('assistant', `I can help you with:
• 🖥️ SSH server management and connections
• 📁 File operations and code review
• 💻 Terminal commands and debugging
• 🔧 Development workflow optimization
• 🤖 General programming questions

${sshConnected ? 'Since you\'re connected to SSH, I can also help analyze your remote files and execute commands!' : 'Connect to your SSH server for enhanced capabilities!'}`);
        } else if (message.toLowerCase().includes('file') || message.toLowerCase().includes('code')) {
            addMessage('assistant', `I'd be happy to help with your files and code! ${sshConnected ? 'I can see you\'re connected to your server. You can share file contents with me or use the terminal to navigate your project.' : 'Connect to your SSH server first, then I can help you browse files, review code, and suggest improvements.'}`);
        } else {
            addMessage('assistant', `I understand you're asking about: "${message}". I'm here to help with your development work! ${sshConnected ? 'Since you\'re connected to your server, I can help with specific files or commands.' : 'For enhanced capabilities, connect to your SSH server using the SSH tab.'}`);
        }
    }, 1000);
}

function addMessage(sender, text) {
    const messagesDiv = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;
    messageDiv.textContent = text;
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
    
    messages.push({ sender, text, timestamp: new Date() });
}

// SSH Functions
function connectSSH() {
    const host = document.getElementById('ssh-host').value;
    const port = document.getElementById('ssh-port').value;
    const username = document.getElementById('ssh-username').value;
    const password = document.getElementById('ssh-password').value;
    
    if (!host || !username || !password) {
        alert('Please fill in all SSH connection details');
        return;
    }
    
    // Simulate SSH connection
    const statusDiv = document.getElementById('ssh-status');
    statusDiv.textContent = '🟡 Connecting...';
    statusDiv.className = 'status';
    
    setTimeout(() => {
        sshConnected = true;
        statusDiv.textContent = `🟢 Connected to ${host}`;
        statusDiv.className = 'status connected';
        
        // Add connection info to terminal
        const terminal = document.getElementById('terminal');
        terminal.innerHTML += `<div>🔗 Connected to ${username}@${host}:${port}</div>`;
        terminal.innerHTML += `<div>✅ SSH connection established</div>`;
        terminal.innerHTML += `<div><br/>user@${host}:~$ <input type="text" class="terminal-input" onkeypress="handleTerminalInput(event)" placeholder="Enter command..."/></div>`;
        
        // Notify chat
        addMessage('assistant', `Great! I'm now connected to your SSH server (${host}). I can help you with:
• Navigate and explore your files
• Review and analyze code
• Execute commands and troubleshoot
• Optimize your development workflow

What would you like to do first?`);
    }, 2000);
}

// Terminal Functions
function handleTerminalInput(event) {
    if (event.key === 'Enter') {
        const command = event.target.value.trim();
        if (!command) return;
        
        const terminal = document.getElementById('terminal');
        
        // Add command to terminal
        terminal.innerHTML += `<div>$ ${command}</div>`;
        
        // Simulate command execution
        setTimeout(() => {
            if (command === 'help') {
                terminal.innerHTML += `<div>Available commands:</div>`;
                terminal.innerHTML += `<div>  ls - list files</div>`;
                terminal.innerHTML += `<div>  pwd - current directory</div>`;
                terminal.innerHTML += `<div>  cat [file] - show file content</div>`;
                terminal.innerHTML += `<div>  cd [dir] - change directory</div>`;
                terminal.innerHTML += `<div>  clear - clear terminal</div>`;
            } else if (command === 'ls') {
                terminal.innerHTML += `<div>README.md  src/  package.json  .git/</div>`;
            } else if (command === 'pwd') {
                terminal.innerHTML += `<div>/home/user/project</div>`;
            } else if (command.startsWith('cat ')) {
                const filename = command.substring(4);
                terminal.innerHTML += `<div>// ${filename}</div>`;
                terminal.innerHTML += `<div>function hello() {</div>`;
                terminal.innerHTML += `<div>    console.log("Hello from ${filename}");</div>`;
                terminal.innerHTML += `<div>}</div>`;
            } else if (command === 'clear') {
                terminal.innerHTML = `<div>Terminal cleared 🧹</div>`;
            } else if (sshConnected) {
                terminal.innerHTML += `<div>Command executed: ${command}</div>`;
                terminal.innerHTML += `<div>✅ Output would appear here</div>`;
            } else {
                terminal.innerHTML += `<div>❌ Not connected to SSH server</div>`;
            }
            
            // Add new input line
            terminal.innerHTML += `<div><br/>user@${sshConnected ? 'server' : 'local'}:~$ <input type="text" class="terminal-input" onkeypress="handleTerminalInput(event)" placeholder="Enter command..."/></div>`;
            
            // Focus on new input
            const inputs = terminal.querySelectorAll('.terminal-input');
            inputs[inputs.length - 1].focus();
            
            // Scroll to bottom
            terminal.scrollTop = terminal.scrollHeight;
        }, 500);
        
        event.target.value = '';
    }
}

// Message input enter key
document.getElementById('message-input').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
});

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    // Check if running as PWA
    if (window.matchMedia('(display-mode: standalone)').matches) {
        document.getElementById('install-prompt').style.display = 'none';
    }
    
    // Load saved messages
    const savedMessages = localStorage.getItem('messages');
    if (savedMessages) {
        messages = JSON.parse(savedMessages);
        messages.forEach(msg => addMessage(msg.sender, msg.text));
    }
    
    // Auto-save messages
    setInterval(() => {
        localStorage.setItem('messages', JSON.stringify(messages));
    }, 5000);
});

// Service Worker Registration
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('sw.js')
        .then(registration => console.log('SW registered'))
        .catch(error => console.log('SW registration failed'));
}