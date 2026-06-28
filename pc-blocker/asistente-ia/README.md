# CleanShield - Asistente IA de Recuperacion

## Que es?

El Asistente IA de CleanShield es un chatbot que te ayuda con:

- **Soporte tecnico**: resolver errores, instalar, configurar CleanShield
- **Recuperacion**: apoyo para adicciones (pornografia, alcohol, drogas, juego, autolesion)
- **Tecnicas**: ejercicios de respiracion, motivacion, manejo del estres
- **Emergencias**: contactos de ayuda profesional

## Como funciona?

El asistente tiene **dos modos**:

### Modo Offline (sin configurar)
Funciona sin internet con respuestas predefinidas para las preguntas mas comunes. Solo abre `index.html` en tu navegador.

### Modo IA Completa (con API key)
Conecta con un modelo de inteligencia artificial para respuestas personalizadas y conversaciones naturales.

## Como configurar la IA completa (Gratis)

### Paso 1: Crear cuenta en Groq
1. Ve a [https://console.groq.com](https://console.groq.com)
2. Crea una cuenta gratuita (puedes usar Google)
3. No requiere tarjeta de credito

### Paso 2: Obtener API Key
1. Una vez dentro, ve a [https://console.groq.com/keys](https://console.groq.com/keys)
2. Haz clic en "Create API Key"
3. Ponle un nombre (ej: "CleanShield")
4. Copia la clave que aparece (empieza con `gsk_...`)

### Paso 3: Configurar
1. Abre el archivo `config.js` con un editor de texto (Bloc de notas)
2. Busca la linea: `apiKey: ""`
3. Pega tu clave entre las comillas: `apiKey: "gsk_tu_clave_aqui"`
4. Guarda el archivo

### Paso 4: Verificar
1. Abre `index.html` en tu navegador
2. El indicador de abajo deberia decir "Modo: IA Activa"
3. Escribe cualquier mensaje para probar

## Alternativas a Groq

Si prefieres otro proveedor:

| Proveedor | Gratis? | Velocidad | Configuracion |
|-----------|---------|-----------|---------------|
| Groq | Si (limite de uso) | Muy rapida | Recomendado |
| OpenAI | No (de pago) | Rapida | Cambiar endpoint en config.js |
| Ollama | Si (local) | Depende de tu PC | Instalar desde ollama.ai |

Para Ollama (todo local, sin internet):
1. Instalar desde https://ollama.ai
2. Ejecutar: `ollama pull llama3.2`
3. En config.js cambiar:
   - endpoint: `http://localhost:11434/v1/chat/completions`
   - model: `llama3.2`
   - apiKey: `ollama` (cualquier texto)

## Limites del plan gratuito de Groq

- ~30 peticiones por minuto
- ~14,400 peticiones por dia
- Suficiente para uso personal normal

## Contactos de Emergencia

Si estas en crisis, no esperes a un chatbot. Llama ahora:

- **024** - Linea de Atencion a la Conducta Suicida (24h, gratuito, confidencial)
- **717 003 717** - Telefono de la Esperanza
- **112** - Emergencias generales
- **91 341 82 82** - Alcoholicos Anonimos
- **902 114 147** - Narcoticos Anonimos
- **900 200 225** - Jugadores Anonimos
- **902 88 55 55** - Proyecto Hombre
- **900 16 15 15** - FAD (Fundacion de Ayuda contra la Drogadiccion)

## Privacidad

- Tus conversaciones se guardan SOLO en tu navegador (localStorage)
- Si usas Groq/OpenAI, los mensajes se envian a sus servidores para generar respuestas
- Si usas Ollama, todo es 100% local
- Puedes borrar el historial en cualquier momento con el boton de papelera
- Puedes exportar tus conversaciones como archivo .txt
