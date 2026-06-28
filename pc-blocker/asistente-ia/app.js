/**
 * CleanShield - Asistente IA Principal
 * Aplicacion de chat con soporte de IA y modo offline
 */

class ChatApp {
    constructor() {
        // DOM Elements
        this.chatArea = document.getElementById('chat-area');
        this.messageInput = document.getElementById('message-input');
        this.sendBtn = document.getElementById('btn-send');
        this.clearBtn = document.getElementById('btn-clear');
        this.exportBtn = document.getElementById('btn-export');
        this.typingIndicator = document.getElementById('typing-indicator');
        this.modeIndicator = document.getElementById('mode-indicator');
        this.emergencyPanel = document.getElementById('emergency-panel');
        this.closePanel = document.getElementById('close-panel');

        // State
        this.messages = [];
        this.isProcessing = false;
        this.isOnline = this.checkApiConfig();

        // Initialize
        this.init();
    }

    init() {
        // Load history
        this.loadHistory();

        // Event listeners
        this.sendBtn.addEventListener('click', () => this.handleSend());
        this.clearBtn.addEventListener('click', () => this.handleClear());
        this.exportBtn.addEventListener('click', () => this.exportChat());
        this.closePanel.addEventListener('click', () => this.toggleEmergencyPanel(false));

        // Input handling
        this.messageInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.handleSend();
            }
        });

        // Auto-resize textarea
        this.messageInput.addEventListener('input', () => {
            this.messageInput.style.height = 'auto';
            this.messageInput.style.height = Math.min(this.messageInput.scrollHeight, 120) + 'px';
        });

        // Quick action buttons
        document.querySelectorAll('.quick-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const message = btn.getAttribute('data-message');
                if (message) {
                    this.messageInput.value = message;
                    this.handleSend();
                }
            });
        });

        // Emergency banner click
        document.querySelector('.emergency-banner').addEventListener('click', () => {
            this.toggleEmergencyPanel(true);
        });

        // Update mode indicator
        this.updateModeIndicator();

        // Show welcome message if no history
        if (this.messages.length === 0) {
            this.showWelcomeMessage();
        } else {
            this.renderAllMessages();
        }
    }

    checkApiConfig() {
        // Check localStorage first (more secure), then fall back to config.js
        const storedKey = localStorage.getItem('cleanshield_api_key');
        if (storedKey && storedKey.trim().length > 0) {
            return true;
        }
        return !!(CLEANSHIELD_AI_CONFIG.apiKey && CLEANSHIELD_AI_CONFIG.apiKey.trim().length > 0);
    }

    getApiKey() {
        // Prefer localStorage over config.js file
        const storedKey = localStorage.getItem('cleanshield_api_key');
        if (storedKey && storedKey.trim().length > 0) {
            return storedKey.trim();
        }
        return CLEANSHIELD_AI_CONFIG.apiKey || '';
    }

    updateModeIndicator() {
        if (this.isOnline) {
            this.modeIndicator.textContent = '\u25CF Modo: IA Activa (' + CLEANSHIELD_AI_CONFIG.model + ')';
            this.modeIndicator.className = 'mode-indicator online';
        } else {
            this.modeIndicator.textContent = '\u25CF Modo: Offline - Configura API key en config.js para IA completa';
            this.modeIndicator.className = 'mode-indicator offline';
        }
    }

    showWelcomeMessage() {
        const welcomeText = `**Hola! Soy tu Asistente de Recuperacion de CleanShield.**

Estoy aqui para ayudarte con:

- **Soporte tecnico**: errores, instalacion, configuracion
- **Recuperacion**: adicciones, tecnicas de relajacion, motivacion
- **Emergencias**: contactos profesionales de ayuda

Puedes escribirme lo que necesites o usar los botones rapidos de abajo.

*Recuerda: si estas en crisis, llama al 024 (gratuito, 24h, confidencial).*`;

        this.addMessage('assistant', welcomeText, true);
    }

    async handleSend() {
        const text = this.messageInput.value.trim();
        if (!text || this.isProcessing) return;

        // Handle /apikey command
        if (text.startsWith('/apikey ')) {
            const key = text.substring(8).trim();
            if (key.length > 0) {
                localStorage.setItem('cleanshield_api_key', key);
                this.isOnline = true;
                this.updateModeIndicator();
                this.messageInput.value = '';
                this.messageInput.style.height = 'auto';
                this.addMessage('assistant', '**API Key configurada correctamente.** El asistente ahora funciona con IA completa.\n\nLa clave se ha guardado de forma segura en tu navegador (localStorage) y no esta en ningun archivo.');
                return;
            }
        }

        if (text === '/apikey') {
            this.messageInput.value = '';
            this.messageInput.style.height = 'auto';
            this.addMessage('assistant', '**Uso:** `/apikey TU_CLAVE_AQUI`\n\nObtener clave gratis: https://console.groq.com/keys\n\nLa clave se guarda en localStorage (no en archivos).');
            return;
        }

        // Clear input
        this.messageInput.value = '';
        this.messageInput.style.height = 'auto';

        // Add user message
        this.addMessage('user', text);

        // Process response
        this.isProcessing = true;
        this.showTypingIndicator();

        try {
            let response;
            if (this.isOnline) {
                response = await this.callAI(text);
            }
            
            if (!response) {
                response = this.handleOfflineResponse(text);
            }

            this.hideTypingIndicator();
            this.addMessage('assistant', response, !this.isOnline && !this.checkApiConfig());
        } catch (error) {
            this.hideTypingIndicator();
            console.error('Error en el asistente:', error);
            const offlineResponse = this.handleOfflineResponse(text);
            this.addMessage('assistant', offlineResponse, true);
        }

        this.isProcessing = false;
    }

    async callAI(userMessage) {
        const config = CLEANSHIELD_AI_CONFIG;
        const apiKey = this.getApiKey();
        
        if (!apiKey) {
            return null;
        }

        // Build messages array with history
        const apiMessages = [
            { role: 'system', content: config.systemPrompt }
        ];

        // Add recent history (limited)
        const historyLimit = config.maxHistoryMessages || 20;
        const recentMessages = this.messages.slice(-historyLimit);
        for (const msg of recentMessages) {
            apiMessages.push({
                role: msg.role,
                content: msg.content
            });
        }

        // Add current message (truncate to prevent oversized requests)
        const truncatedMessage = userMessage.length > 4000 ? userMessage.substring(0, 4000) + '...[mensaje truncado]' : userMessage;
        apiMessages.push({ role: 'user', content: truncatedMessage });

        try {
            const controller = new AbortController();
            const timeout = setTimeout(() => controller.abort(), config.timeout || 30000);

            const response = await fetch(config.apiEndpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + apiKey
                },
                body: JSON.stringify({
                    model: config.model,
                    messages: apiMessages,
                    max_tokens: config.maxTokens || 1024,
                    temperature: config.temperature || 0.7
                }),
                signal: controller.signal
            });

            clearTimeout(timeout);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                console.error('API Error:', response.status, errorData);
                
                if (response.status === 401) {
                    return '**Error de autenticacion.** La API key no es valida. Verifica tu clave en `config.js`.\n\nPuedes obtener una gratis en: https://console.groq.com/keys';
                }
                if (response.status === 429) {
                    return '**Limite de uso alcanzado.** Espera unos minutos e intenta de nuevo.\n\nMientras tanto, puedo responder con mis respuestas predefinidas.';
                }
                
                return null; // Fall back to offline
            }

            const data = await response.json();
            
            if (data.choices && data.choices[0] && data.choices[0].message) {
                return data.choices[0].message.content;
            }
            
            return null;
        } catch (error) {
            if (error.name === 'AbortError') {
                console.warn('API timeout');
                return '**Tiempo de espera agotado.** El servidor tardo demasiado en responder. Usando modo offline.';
            }
            console.error('Fetch error:', error);
            return null; // Fall back to offline
        }
    }

    handleOfflineResponse(text) {
        const response = findOfflineResponse(text);
        return response;
    }

    addMessage(role, content, isOffline = false) {
        const message = {
            role: role,
            content: content,
            timestamp: new Date().toISOString(),
            isOffline: isOffline
        };

        this.messages.push(message);
        this.renderMessage(message);
        this.saveHistory();
        this.scrollToBottom();
    }

    renderMessage(message) {
        const messageEl = document.createElement('div');
        messageEl.className = `message ${message.role}`;
        
        if (message.role === 'assistant' && this.messages.indexOf(message) === 0) {
            messageEl.classList.add('welcome-message');
        }

        const avatar = document.createElement('div');
        avatar.className = 'message-avatar';
        avatar.textContent = message.role === 'user' ? '\uD83D\uDC64' : '\uD83D\uDEE1\uFE0F';

        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        bubble.innerHTML = this.formatMarkdown(message.content);

        // Add offline badge if applicable
        if (message.isOffline && message.role === 'assistant') {
            const badge = document.createElement('span');
            badge.className = 'offline-badge';
            badge.textContent = 'Respuesta offline';
            bubble.appendChild(badge);
        }

        // Timestamp
        const time = document.createElement('div');
        time.className = 'message-time';
        time.textContent = this.formatTime(message.timestamp);
        bubble.appendChild(time);

        messageEl.appendChild(avatar);
        messageEl.appendChild(bubble);
        
        // Insert before typing indicator
        this.chatArea.insertBefore(messageEl, this.typingIndicator);
    }

    renderAllMessages() {
        // Clear existing messages (keep typing indicator)
        const existingMessages = this.chatArea.querySelectorAll('.message');
        existingMessages.forEach(el => el.remove());

        // Re-render all
        for (const message of this.messages) {
            this.renderMessage(message);
        }
        this.scrollToBottom();
    }

    formatMarkdown(text) {
        if (!text) return '';
        
        let html = text
            // Escape HTML
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            // Bold
            .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
            // Italic
            .replace(/\*(.+?)\*/g, '<em>$1</em>')
            // Inline code
            .replace(/`(.+?)`/g, '<code>$1</code>')
            // Line breaks
            .replace(/\n\n/g, '</p><p>')
            .replace(/\n/g, '<br>')
            // Lists
            .replace(/^- (.+)/gm, '<li>$1</li>')
            .replace(/^(\d+)\. (.+)/gm, '<li>$2</li>');

        // Wrap in paragraphs
        html = '<p>' + html + '</p>';
        
        // Clean up list items into ul
        html = html.replace(/(<li>.*?<\/li>)+/gs, (match) => '<ul>' + match + '</ul>');

        return html;
    }

    formatTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
    }

    showTypingIndicator() {
        this.typingIndicator.style.display = 'flex';
        this.scrollToBottom();
    }

    hideTypingIndicator() {
        this.typingIndicator.style.display = 'none';
    }

    scrollToBottom() {
        requestAnimationFrame(() => {
            this.chatArea.scrollTop = this.chatArea.scrollHeight;
        });
    }

    saveHistory() {
        if (!CLEANSHIELD_AI_CONFIG.saveHistory) return;

        const maxStored = CLEANSHIELD_AI_CONFIG.maxStoredMessages || 100;
        const toStore = this.messages.slice(-maxStored);
        
        try {
            localStorage.setItem('cleanshield_chat_history', JSON.stringify(toStore));
        } catch (e) {
            console.warn('No se pudo guardar el historial:', e);
        }
    }

    loadHistory() {
        try {
            const stored = localStorage.getItem('cleanshield_chat_history');
            if (stored) {
                this.messages = JSON.parse(stored);
            }
        } catch (e) {
            console.warn('No se pudo cargar el historial:', e);
            this.messages = [];
        }
    }

    handleClear() {
        if (confirm('Quieres borrar toda la conversacion? Esta accion no se puede deshacer.')) {
            this.messages = [];
            localStorage.removeItem('cleanshield_chat_history');
            
            // Clear DOM
            const existingMessages = this.chatArea.querySelectorAll('.message');
            existingMessages.forEach(el => el.remove());
            
            // Show welcome again
            this.showWelcomeMessage();
        }
    }

    exportChat() {
        if (this.messages.length === 0) {
            alert('No hay mensajes para exportar.');
            return;
        }

        let text = '=== CleanShield - Historial del Asistente ===\n';
        text += 'Exportado: ' + new Date().toLocaleString('es-ES') + '\n';
        text += '='.repeat(50) + '\n\n';

        for (const msg of this.messages) {
            const time = new Date(msg.timestamp).toLocaleString('es-ES');
            const role = msg.role === 'user' ? 'Tu' : 'Asistente';
            text += `[${time}] ${role}:\n${msg.content}\n\n`;
        }

        text += '='.repeat(50) + '\n';
        text += 'Si necesitas ayuda inmediata: 024 (Linea de Atencion a la Conducta Suicida)\n';

        // Download
        const blob = new Blob([text], { type: 'text/plain;charset=utf-8' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'CleanShield-Chat-' + new Date().toISOString().slice(0, 10) + '.txt';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    }

    toggleEmergencyPanel(show) {
        this.emergencyPanel.style.display = show ? 'block' : 'none';
    }
}

// Initialize app when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    window.chatApp = new ChatApp();
});
