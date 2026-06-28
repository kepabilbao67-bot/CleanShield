/**
 * CleanShield - Respuestas Offline del Asistente
 * 
 * Cuando no hay API key configurada o no hay conexion,
 * el asistente usa estas respuestas predefinidas.
 */

const OFFLINE_RESPONSES = {
    // === INSTALACION Y USO ===
    instalacion: [
        {
            keywords: ["instalar", "como se instala", "instalacion", "setup", "configurar"],
            response: `**Como instalar CleanShield:**

1. Descarga la carpeta pc-blocker completa
2. Haz clic derecho en **menu-principal.bat** y selecciona "Ejecutar como administrador"
3. Elige la opcion **1) Instalar proteccion completa**
4. Espera a que termine (bloquea 1000+ dominios)
5. Reinicia el navegador

Si tienes problemas, asegurate de:
- Ejecutar SIEMPRE como administrador
- Tener Windows 10 o superior
- No tener otro bloqueador activo que interfiera`
        },
        {
            keywords: ["ejecutar como admin", "administrador", "permisos", "permiso"],
            response: `**Para ejecutar como Administrador:**

1. Busca el archivo .bat que quieras ejecutar
2. Haz clic DERECHO sobre el
3. Selecciona **"Ejecutar como administrador"**
4. Si aparece el aviso de Windows (UAC), haz clic en "Si"

Esto es necesario porque CleanShield modifica archivos del sistema (hosts) y el registro de Windows, que requieren permisos elevados.`
        },
        {
            keywords: ["menu", "opciones", "que puedo hacer", "funciones"],
            response: `**Menu Principal de CleanShield:**

1. **Instalar proteccion completa** - Bloquea todos los dominios daninos
2. **Configurar categorias** - Elige que tipos de contenido bloquear
3. **Ver estado del bloqueo** - Comprueba si la proteccion esta activa
4. **Abrir Asistente IA** - Este chat de ayuda
5. **Desinstalar proteccion** - Requiere contrasena
6. **Ver registro de actividad** - Historial de acciones
7. **Salir**

Ejecuta menu-principal.bat como administrador para acceder.`
        }
    ],

    // === ERRORES COMUNES ===
    errores: [
        {
            keywords: ["error", "no funciona", "no bloquea", "sigue entrando", "no me bloquea"],
            response: `**Solucion de problemas comunes:**

1. **Verificar que se ejecuto como administrador**
   - Haz clic derecho -> Ejecutar como administrador

2. **Limpiar cache DNS:**
   - Abre CMD como admin
   - Escribe: \`ipconfig /flushdns\`

3. **Reiniciar navegador completamente** (cerrar TODAS las ventanas)

4. **Verificar el archivo hosts:**
   - Abre: C:\\Windows\\System32\\drivers\\etc\\hosts
   - Debe tener lineas con "CleanShield" y "0.0.0.0"

5. **Ejecutar verificar-estado.bat** como admin para diagnosticar

Si nada funciona, ejecuta instalar-bloqueo.bat de nuevo como administrador.`
        },
        {
            keywords: ["hosts", "archivo hosts", "no puedo editar", "acceso denegado"],
            response: `**Problemas con el archivo hosts:**

El archivo hosts esta en: \`C:\\Windows\\System32\\drivers\\etc\\hosts\`

Si no puedes editarlo:
1. Asegurate de ejecutar como **administrador**
2. Si usaste "bloquear-desinstalacion.bat", los permisos estan protegidos
3. Para verificar: ejecuta \`verificar-estado.bat\` como admin

Si el antivirus bloquea cambios en hosts:
- Anade una excepcion para el archivo hosts en tu antivirus
- O desactiva temporalmente la proteccion en tiempo real

Algunos antivirus (Avast, Norton, Kaspersky) protegen el archivo hosts por defecto.`
        },
        {
            keywords: ["dns", "cache", "flush", "limpiar dns"],
            response: `**Limpiar cache DNS:**

1. Presiona **Windows + R**
2. Escribe \`cmd\` y presiona Ctrl+Shift+Enter (ejecutar como admin)
3. Escribe: \`ipconfig /flushdns\`
4. Deberia decir: "Se vacio correctamente la cache del DNS"

Si los sitios siguen cargando despues de limpiar DNS:
- Cierra TODAS las ventanas del navegador
- Abre el navegador de nuevo
- Tambien limpia la cache del navegador (Ctrl+Shift+Delete)

En algunos casos necesitas reiniciar el ordenador.`
        },
        {
            keywords: ["navegador", "chrome", "brave", "firefox", "edge", "politica"],
            response: `**Problemas con navegadores:**

CleanShield aplica politicas a: Chrome, Brave, Edge, Firefox y Opera.

Si un navegador no respeta el bloqueo:
1. **Cerrar y reabrir** completamente el navegador
2. Verificar que las politicas estan activas:
   - Chrome: escribe \`chrome://policy\` en la barra
   - Edge: escribe \`edge://policy\`
   - Brave: escribe \`brave://policy\`
3. Si no aparecen politicas, re-ejecutar \`instalar-bloqueo.bat\` como admin

Para Firefox: las politicas se aplican por registro. Si no funciona, puede necesitar un reinicio del PC.`
        },
        {
            keywords: ["desinstalar", "quitar", "eliminar", "desbloquear"],
            response: `**Para desinstalar CleanShield:**

1. Ejecuta \`menu-principal.bat\` como administrador
2. Selecciona la opcion 5 (Desinstalar)
3. Introduce la contrasena configurada
4. O escribe "DESBLOQUEAR CLEANSHIELD" si no configuraste contrasena

**Importante:** Antes de desinstalar, piensa por que quieres hacerlo. Si estas teniendo un momento de debilidad, prueba primero:
- El ejercicio de respiracion (preguntame)
- Llamar a un amigo de confianza
- Esperar 15 minutos antes de decidir

Recuerda: instalaste esto por una razon. Tu yo del futuro te lo agradecera.`
        }
    ],

    // === ADICCIONES Y RECUPERACION ===
    adicciones: [
        {
            keywords: ["adiccion", "adicto", "no puedo parar", "recaida", "recai", "he recaido"],
            response: `**Sobre la recaida:**

Primero: una recaida NO es un fracaso. Es parte del proceso de recuperacion para la mayoria de personas.

Lo importante ahora:
1. **No te castigues** - La culpa excesiva puede provocar mas recaidas
2. **Identifica el disparador** - Que paso antes? Estres? Soledad? Aburrimiento?
3. **Actua ya** - Sal de donde estes, llama a alguien, haz ejercicio
4. **Reinicia tu contador** - Cada dia limpio cuenta, empieza de nuevo HOY

Tecnica del "urge surfing": cuando sientas la urgencia, observala como una ola. Sube, llega al pico, y SIEMPRE baja. Dura 15-20 minutos maximo.

Si necesitas ayuda profesional, no dudes en contactar a un especialista.`
        },
        {
            keywords: ["pornografia", "porno", "nofap", "fap"],
            response: `**Sobre la adiccion a la pornografia:**

Es una de las adicciones mas comunes y menos reconocidas. No estas solo/a.

Estrategias que funcionan:
1. **Bloqueo tecnico** (CleanShield te ayuda con esto)
2. **Identificar disparadores**: soledad nocturna, estres, aburrimiento
3. **Sustituir el habito**: ejercicio, lectura, llamar a alguien
4. **Cuenta de dias limpios**: cada dia es una victoria
5. **Rendicion de cuentas**: comparte tu progreso con alguien de confianza

Efectos negativos documentados:
- Disfuncion erectil inducida por porno
- Aislamiento social
- Distorsion de relaciones reales
- Ansiedad y depresion

**Recurso**: El libro "Your Brain on Porn" de Gary Wilson explica la neurociencia detras.

Cada dia sin porno, tu cerebro se recupera un poco mas.`
        },
        {
            keywords: ["alcohol", "beber", "borracho", "alcoholismo", "copa"],
            response: `**Sobre el alcohol:**

Si sientes que el alcohol controla tu vida, hay ayuda disponible.

Senales de alarma:
- Beber solo/a regularmente
- Necesitar mas cantidad para el mismo efecto
- No poder parar una vez empiezas
- Esconder cuanto bebes

Pasos inmediatos:
1. **No intentes dejarlo de golpe** si bebes mucho (puede ser peligroso - consulta un medico)
2. **Contacta Alcoholicos Anonimos**: 91 341 82 82
3. **Habla con tu medico** - hay medicamentos que ayudan
4. **Evita disparadores**: bares, reuniones con alcohol, tener alcohol en casa

Alcoholicos Anonimos: reuniones gratuitas, anonimas, sin juicio.
Web: alcoholicosanonimos.org

**Recuerda**: pedir ayuda es de valientes, no de debiles.`
        },
        {
            keywords: ["drogas", "droga", "cocaina", "marihuana", "cannabis", "fumar", "sustancias"],
            response: `**Sobre adiccion a sustancias:**

Cada sustancia tiene sus particularidades, pero el proceso de recuperacion comparte principios comunes.

Recursos profesionales en Espana:
- **Narcoticos Anonimos**: 902 114 147
- **Proyecto Hombre**: 902 88 55 55
- **FAD (Fundacion de Ayuda contra la Drogadiccion)**: 900 16 15 15
- **Tu centro de salud** puede derivarte a un CAD (Centro de Atencion a Drogodependientes)

Pasos importantes:
1. **Busca ayuda profesional** - la desintoxicacion puede necesitar supervision medica
2. **Alejate de los entornos** donde consumes
3. **Encuentra un grupo de apoyo** (NA tiene reuniones semanales)
4. **Trabaja en las causas** - muchas adicciones ocultan dolor emocional

NO intentes dejarlo solo si usas opioides, benzodiacepinas o alcohol en cantidades altas. Busca supervision medica.`
        },
        {
            keywords: ["juego", "apuestas", "apostar", "casino", "ludopatia", "ludopata"],
            response: `**Sobre la ludopatia (adiccion al juego):**

La adiccion al juego destruye vidas economica y emocionalmente. Es una enfermedad reconocida, no un "vicio".

Actuacion inmediata:
1. **Autoexcluirte** de casas de apuestas (en Espana es un derecho legal)
2. **Bloquear acceso** - CleanShield bloquea las webs de apuestas
3. **Controlar el dinero** - deja tarjetas a alguien de confianza
4. **Jugadores Anonimos**: 900 200 225 (gratuito)

La trampa del "voy a recuperar lo perdido":
- NUNCA funciona a largo plazo
- La casa siempre gana matematicamente
- Cada apuesta es dinero perdido

Recursos:
- **FEJAR** (Federacion Espanola de Jugadores Rehabilitados)
- **Jugadores Anonimos**: jugadoresanonimos.org
- Tu medico puede derivarte a una unidad de conductas adictivas

Has dado un gran paso al buscar ayuda.`
        },
        {
            keywords: ["autolesion", "cortarme", "hacerme dano", "suicidio", "suicidarme", "morir", "no quiero vivir"],
            response: `**IMPORTANTE - Contacta ahora:**

**024** - Linea de Atencion a la Conducta Suicida (24h, gratuito, confidencial)
**112** - Emergencias
**717 003 717** - Telefono de la Esperanza

Lo que sientes ahora es temporal, aunque no lo parezca. El dolor pasa.

Mientras tanto:
1. **No estes solo/a** - llama a alguien AHORA (familiar, amigo, 024)
2. **Aleja objetos peligrosos** de tu alcance
3. **Respira**: inhala 4 segundos, manten 7, exhala 8
4. **Pon hielo en tus munecas** - da una sensacion intensa sin dano

No estas loco/a ni eres debil. Estas sufriendo y necesitas apoyo profesional.

Un profesional puede ayudarte a encontrar alternativas. El 024 es gratuito, anonimo y confidencial.

**Tu vida importa. Hay personas que quieren ayudarte.**`
        }
    ],

    // === TECNICAS Y EJERCICIOS ===
    tecnicas: [
        {
            keywords: ["respiracion", "respirar", "calmar", "ansiedad", "ansioso", "nervioso", "panico"],
            response: `**Ejercicio de Respiracion 4-7-8:**

Este ejercicio calma el sistema nervioso en minutos.

1. **INHALA** por la nariz contando hasta 4
   ... 1 ... 2 ... 3 ... 4 ...

2. **MANTEN** el aire contando hasta 7
   ... 1 ... 2 ... 3 ... 4 ... 5 ... 6 ... 7 ...

3. **EXHALA** por la boca contando hasta 8
   ... 1 ... 2 ... 3 ... 4 ... 5 ... 6 ... 7 ... 8 ...

Repite 4 veces.

Otros trucos rapidos para la ansiedad:
- Pon las manos bajo agua fria 30 segundos
- Nombra 5 cosas que ves, 4 que tocas, 3 que oyes, 2 que hueles, 1 que saboreas
- Camina rapido durante 5 minutos
- Llama a alguien y habla de cualquier cosa

La urgencia/ansiedad SIEMPRE pasa. Dura maximo 15-20 minutos.`
        },
        {
            keywords: ["motivacion", "motivame", "animo", "puedo", "fuerza", "debil"],
            response: `**Recuerda esto:**

Instalaste esta proteccion porque quieres ser mejor. Ese deseo sigue vivo en ti.

Hechos sobre la recuperacion:
- Cada dia limpio, tu cerebro forma nuevas conexiones
- Las urgencias se hacen MAS DEBILES con el tiempo
- La mayoria de personas exitosas tuvieron multiples intentos antes de lograrlo
- El simple hecho de que estes aqui muestra que no te has rendido

**"No tienes que ver toda la escalera, solo da el siguiente paso."** - Martin Luther King Jr.

Tu siguiente paso ahora mismo:
1. Respira profundo 3 veces
2. Bebe un vaso de agua
3. Haz algo con las manos (pasear, ejercicio, limpiar, cocinar)
4. Vuelve aqui si necesitas hablar mas

Eres mas fuerte de lo que crees. Cada momento que resistes es una victoria.`
        },
        {
            keywords: ["dormir", "insomnio", "noche", "nocturno", "madrugada"],
            response: `**Gestion de momentos nocturnos:**

La noche es el momento mas vulnerable para muchas adicciones. Estrategias:

**Prevencion (antes de dormir):**
- Deja el telefono/PC en otra habitacion
- Establece una rutina: ducha, lectura, meditacion
- No uses pantallas 30 min antes de dormir
- CleanShield esta protegiendo tu PC 24/7

**Si no puedes dormir:**
1. No te quedes en la cama dando vueltas
2. Levantate, bebe agua, lee algo aburrido
3. Haz el ejercicio 4-7-8 de respiracion
4. Escucha un podcast o audiolibro relajante
5. Escribe en un diario lo que sientes

**Si sientes urgencia:**
- La urgencia nocturna dura MAX 20 minutos
- Haz flexiones hasta cansarte
- Llama a tu persona de confianza (aunque sea tarde)
- Recuerda como te sentiras manana si resistes vs si no`
        },
        {
            keywords: ["estres", "trabajo", "agobiado", "presion", "overwhelm"],
            response: `**Manejar el estres sin recurrir a la adiccion:**

El estres es un disparador comun. Alternativas saludables:

**Inmediato (0-5 min):**
- Respiracion 4-7-8 (3 repeticiones)
- Agua fria en la cara o munecas
- 20 sentadillas o flexiones

**Corto plazo (5-30 min):**
- Caminar al aire libre
- Musica a todo volumen y bailar
- Llamar a un amigo
- Escribir todo lo que te agobia en un papel

**Habitual:**
- Ejercicio regular (3-4 veces/semana)
- Meditacion diaria (5-10 min)
- Hobbies manuales (cocinar, dibujar, jardineria)
- Limitar noticias y redes sociales

La adiccion NUNCA soluciona el estres. Solo lo pospone y le anade culpa despues.`
        }
    ],

    // === SOBRE CLEANSHIELD ===
    cleanshield: [
        {
            keywords: ["que es cleanshield", "que hace", "como funciona", "bloqueo", "bloquear"],
            response: `**Como funciona CleanShield:**

CleanShield es un bloqueador de contenido a nivel de sistema para Windows que protege contra contenido danino.

**Mecanismo de bloqueo:**
- Modifica el archivo \`hosts\` de Windows para redirigir dominios daninos a 0.0.0.0 (la nada)
- Aplica politicas de registro para todos los navegadores
- Fuerza SafeSearch en Google
- Bloquea YouTube restringido
- Bloquea DNS-over-HTTPS (para evitar bypass)

**Categorias que bloquea:**
1. Pornografia (+200 dominios)
2. Prostitucion/Escorts
3. Apuestas y prestamos abusivos
4. Drogas
5. Alcohol
6. Autolesion
7. Dark Web / Anonimizadores
8. Pirateria
9. Dating/Hookup
10. Violencia extrema

**Total: +1000 dominios bloqueados**

Puedes configurar que categorias activar con \`configurar-categorias.bat\``
        },
        {
            keywords: ["seguro", "seguridad", "pueden quitar", "saltarse", "bypass", "evitar"],
            response: `**Seguridad de CleanShield:**

Niveles de proteccion:
1. **Archivo hosts** - Bloqueo a nivel de sistema operativo
2. **Politicas de navegador** - Registros de Windows que los navegadores DEBEN obedecer
3. **Bloqueo de extensiones** - No se puede acceder a chrome://extensions
4. **Herramientas de desarrollador** - Desactivadas por politica
5. **DNS-over-HTTPS** - Bloqueado (evita bypass por DNS alternativo)
6. **SafeSearch forzado** - Por politica de registro
7. **Tarea programada** - Re-aplica bloqueos diariamente
8. **Proteccion anti-desinstalacion** - Requiere contrasena

Para maxima seguridad:
- Ejecuta \`bloquear-desinstalacion.bat\` como admin
- Que otra persona ponga la contrasena
- Esto hace practicamente imposible quitar la proteccion sin conocer la contrasena`
        },
        {
            keywords: ["categoria", "categorias", "activar", "desactivar", "personalizar"],
            response: `**Configurar categorias:**

1. Ejecuta \`menu-principal.bat\` como administrador
2. Selecciona opcion **2) Configurar categorias**
3. Veras la lista de categorias disponibles:
   - Pornografia
   - Prostitucion
   - Apuestas/Prestamos
   - Drogas
   - Alcohol
   - Autolesion
   - Dark Web
   - Pirateria
   - Dating
   - Violencia

4. Selecciona el numero de la categoria para activar/desactivar
5. Los cambios se aplican inmediatamente

**Recomendacion**: mantener TODAS las categorias activas para maxima proteccion. Solo desactiva una categoria si tienes una razon valida (ej: necesitas acceder a un sitio de citas porque estas en una relacion seria).`
        }
    ],

    // === EMERGENCIA ===
    emergencia: [
        {
            keywords: ["emergencia", "contacto", "telefono", "ayuda profesional", "psicologo", "terapeuta"],
            response: `**Contactos de Emergencia y Ayuda:**

**CRISIS INMEDIATA:**
- **024** - Linea de Atencion a la Conducta Suicida (24h, gratuito)
- **112** - Emergencias generales
- **717 003 717** - Telefono de la Esperanza (24h)

**ADICCIONES ESPECIFICAS:**
- **Alcoholicos Anonimos**: 91 341 82 82 | alcoholicosanonimos.org
- **Narcoticos Anonimos**: 902 114 147 | narcoticosanonimos.es
- **Jugadores Anonimos**: 900 200 225 | jugadoresanonimos.org
- **Proyecto Hombre**: 902 88 55 55 | proyectohombre.es
- **FAD**: 900 16 15 15 | fad.es

**ATENCION PROFESIONAL:**
- Tu medico de cabecera puede derivarte a salud mental (gratuito por la Seguridad Social)
- CAD (Centro de Atencion a Drogodependientes) - gratuito
- CAID (Centro de Atencion Integral a Drogodependientes)

No tienes que hacerlo solo/a. Pedir ayuda es el primer paso mas valiente.`
        }
    ]
};

/**
 * Busca la mejor respuesta offline basada en palabras clave
 * @param {string} userMessage - Mensaje del usuario
 * @returns {string|null} - Respuesta encontrada o null
 */
function findOfflineResponse(userMessage) {
    const message = userMessage.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    
    // Buscar en todas las categorias
    const allCategories = [
        ...OFFLINE_RESPONSES.instalacion,
        ...OFFLINE_RESPONSES.errores,
        ...OFFLINE_RESPONSES.adicciones,
        ...OFFLINE_RESPONSES.tecnicas,
        ...OFFLINE_RESPONSES.cleanshield,
        ...OFFLINE_RESPONSES.emergencia
    ];
    
    let bestMatch = null;
    let bestScore = 0;
    
    for (const entry of allCategories) {
        let score = 0;
        for (const keyword of entry.keywords) {
            const normalizedKeyword = keyword.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
            if (message.includes(normalizedKeyword)) {
                score += normalizedKeyword.length; // Palabras mas largas tienen mas peso
            }
        }
        if (score > bestScore) {
            bestScore = score;
            bestMatch = entry;
        }
    }
    
    // Si hay coincidencia con puntuacion minima
    if (bestMatch && bestScore >= 3) {
        return bestMatch.response;
    }
    
    // Respuesta por defecto
    return `Entiendo tu mensaje, pero no estoy seguro de como ayudarte con eso en modo offline.

Puedo ayudarte con:
- **Instalacion y uso** de CleanShield
- **Errores comunes** y como solucionarlos
- **Adicciones**: pornografia, alcohol, drogas, juego, autolesion
- **Tecnicas**: respiracion, motivacion, manejo del estres
- **Emergencias**: numeros de ayuda profesional

Intenta preguntar sobre alguno de estos temas, o configura una API key en \`config.js\` para tener respuestas IA completas.

Si estas en crisis: llama al **024** (gratuito, 24h, confidencial).`;
}
