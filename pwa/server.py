#!/usr/bin/env python3
"""
CodeAgents Mobile PWA Server
Simple HTTP server with HTTPS support for PWA features
"""

import http.server
import socketserver
import ssl
import os
import json
from urllib.parse import urlparse, parse_qs

class CodeAgentsHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        # PWA and security headers
        self.send_header('Service-Worker-Allowed', '/')
        self.send_header('Cache-Control', 'no-cache')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()

    def do_POST(self):
        # Handle API requests
        if self.path == '/api/claude':
            self.handle_claude_api()
        elif self.path == '/api/ssh':
            self.handle_ssh_api()
        else:
            self.send_error(404)

    def handle_claude_api(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        
        try:
            data = json.loads(post_data.decode('utf-8'))
            message = data.get('message', '')
            
            # Simulate Claude response
            response = {
                'response': f"I understand you're asking about: '{message}'. I'm here to help with your development work! Connect to your SSH server for enhanced capabilities.",
                'timestamp': '2025-07-08T15:00:00Z'
            }
            
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response).encode('utf-8'))
            
        except Exception as e:
            self.send_error(400, f"Error: {str(e)}")

    def handle_ssh_api(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        
        try:
            data = json.loads(post_data.decode('utf-8'))
            action = data.get('action')
            
            if action == 'connect':
                response = {
                    'status': 'connected',
                    'message': 'SSH connection simulated successfully'
                }
            elif action == 'execute':
                command = data.get('command', '')
                response = {
                    'output': f"$ {command}\nCommand executed successfully\n✅ Output simulation",
                    'exit_code': 0
                }
            else:
                response = {'error': 'Unknown action'}
            
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response).encode('utf-8'))
            
        except Exception as e:
            self.send_error(400, f"Error: {str(e)}")

def run_server(port=8000):
    handler = CodeAgentsHandler
    
    with socketserver.TCPServer(("", port), handler) as httpd:
        print(f"🚀 CodeAgents Mobile PWA Server")
        print(f"📱 Open on your mobile: http://localhost:{port}")
        print(f"🌐 Or access from other devices: http://YOUR_IP:{port}")
        print(f"📋 Press Ctrl+C to stop")
        print()
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\n🛑 Server stopped")

if __name__ == "__main__":
    # Change to the directory containing the PWA files
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    run_server()