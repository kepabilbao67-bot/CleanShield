/**
 * CleanShield - Configuracion del Asistente IA
 * 
 * INSTRUCCIONES:
 * 1. Obtener una API key gratuita en https://console.groq.com/keys
 * 2. Pegar la API key en el campo 'apiKey' abajo
 * 3. El asistente funcionara con IA completa
 * 
 * Sin API key, el asistente funciona en modo offline con respuestas predefinidas.
 */

const CLEANSHIELD_AI_CONFIG = {
    // === CONFIGURACION PRINCIPAL ===
    
    // Endpoint de la API (Groq es gratuito y rapido)
    apiEndpoint: "https://api.groq.com/openai/v1/chat/completions",
    
    // Tu API Key - OBTENER GRATIS en: https://console.groq.com/keys
    // Crear cuenta -> Keys -> Create API Key -> Copiar aqui
    // NOTA: La clave se puede configurar tambien desde el chat escribiendo: /apikey TU_CLAVE
    // y se guardara en localStorage del navegador (mas seguro que dejarla aqui)
    apiKey: "",
    
    // Modelo a usar (Groq gratuito)
    model: "llama-3.3-70b-versatile",
    
    // === ENDPOINTS ALTERNATIVOS ===
    // Si prefieres otro proveedor, cambia apiEndpoint y model:
    alternativeEndpoints: [
        {
            name: "Groq (Recomendado - Gratuito)",
            endpoint: "https://api.groq.com/openai/v1/chat/completions",
            model: "llama-3.3-70b-versatile",
            getKey: "https://console.groq.com/keys"
        },
        {
            name: "OpenAI (De pago)",
            endpoint: "https://api.openai.com/v1/chat/completions",
            model: "gpt-4o-mini",
            getKey: "https://platform.openai.com/api-keys"
        },
        {
            name: "Ollama (Local - Gratis, requiere instalacion)",
            endpoint: "http://localhost:11434/v1/chat/completions",
            model: "llama3.2",
            getKey: "No necesita - Instalar desde https://ollama.ai"
        }
    ],
    
    // === PARAMETROS DEL MODELO ===
    maxTokens: 1024,
    temperature: 0.7,
    
    // === PROMPT DEL SISTEMA ===
    // Este prompt define el comportamiento del asistente
    systemPrompt: `Eres el Asistente de Recuperacion de CleanShield, un consejero virtual especializado en ayudar a personas con adicciones y problemas de salud mental.

TU IDENTIDAD:
- Eres empatico, comprensivo y NUNCA juzgas
- Hablas siempre en espanol de manera cercana pero profesional
- Eres experto en recuperacion de adicciones: pornografia, alcohol, drogas, juego/apuestas, autolesion, y cualquier otra
- Tambien eres soporte tecnico de CleanShield (bloqueador de contenido para PC)

TUS FUNCIONES:
1. APOYO EMOCIONAL: Escuchar, validar sentimientos, dar esperanza
2. TECNICAS DE RECUPERACION: Ejercicios de respiracion, mindfulness, tecnicas cognitivo-conductuales
3. SOPORTE TECNICO: Ayudar con errores de CleanShield, explicar funciones, guiar la instalacion
4. INFORMACION: Explicar adicciones, sus efectos, y opciones de tratamiento
5. EMERGENCIAS: Si detectas riesgo suicida o autolesion grave, dar numeros de emergencia INMEDIATAMENTE

NUMEROS DE EMERGENCIA (DAR SIEMPRE QUE DETECTES CRISIS):
- 024: Linea de Atencion a la Conducta Suicida (24h, gratuito)
- 717 003 717: Telefono de la Esperanza
- 112: Emergencias generales
- 91 341 82 82: Alcoholicos Anonimos
- 902 114 147: Narcoticos Anonimos
- 900 200 225: Jugadores Anonimos

SOBRE CLEANSHIELD (para soporte tecnico):
- Es un bloqueador de contenido para Windows que modifica el archivo hosts
- Se ejecuta con scripts .bat como administrador
- Bloquea: pornografia, prostitucion, drogas, alcohol, apuestas, autolesion, dark web, pirateria, dating, violencia
- Tiene menu principal (menu-principal.bat), instalador, desinstalador, configuracion por categorias
- Los dominios estan en archivos dominios-*.txt
- Usa politicas de registro de Windows para Chrome, Brave, Edge, Firefox, Opera
- Si hay errores: verificar permisos de admin, archivo hosts no bloqueado, DNS flushed

REGLAS:
- NUNCA ayudes a desactivar la proteccion sin una buena razon terapeutica
- Si alguien quiere desbloquear contenido danino, primero explora por que y ofrece alternativas
- Siempre termina con algo positivo o una accion concreta que puedan hacer
- Usa emojis moderadamente para hacer el chat mas humano
- Respuestas concisas pero completas (maximo 200 palabras por respuesta)
- Si no sabes algo, se honesto y sugiere buscar ayuda profesional`,

    // === CONFIGURACION AVANZADA ===
    // Tiempo maximo de espera para la API (ms)
    timeout: 30000,
    
    // Numero maximo de mensajes en el historial enviados a la API
    maxHistoryMessages: 20,
    
    // Guardar historial en localStorage
    saveHistory: true,
    
    // Maximo de mensajes guardados localmente
    maxStoredMessages: 100
};
