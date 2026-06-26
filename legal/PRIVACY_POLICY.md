# Política de Privacidad de CleanShield

**Última actualización:** Enero 2024  
**Versión:** 1.0

## 1. Responsable del Tratamiento

CleanShield App  
Email: privacidad@cleanshield.app  
Ubicación: España, Unión Europea

## 2. Datos que Recopilamos

### 2.1 Datos procesados localmente (nunca salen del dispositivo)

- **Historial de navegación filtrado:** Los dominios visitados se comparan contra listas de bloqueo localmente. No se almacena ni transmite el historial.
- **Patrones de uso:** Horarios y frecuencia de intentos de acceso a contenido bloqueado, procesados exclusivamente en el dispositivo.
- **Contenido de diario personal:** Las entradas del diario se almacenan cifradas localmente con AES-256.
- **Datos de meditación:** Duración y frecuencia de sesiones, almacenados localmente.

### 2.2 Datos sincronizados con Firebase (solo si se activa Modo Tutor)

- **Estadísticas agregadas:** Número de bloqueos diarios, días de racha, uso del botón de pánico. Sin detalles de URLs o contenido.
- **Estado del dispositivo:** Online/offline, última conexión.
- **Identificador de dispositivo:** UUID generado aleatoriamente, no vinculado a identidad real.
- **Progreso de gamificación:** Nivel, XP, logros desbloqueados.

### 2.3 Datos de cuenta (solo usuarios registrados)

- **Email:** Para autenticación y recuperación de cuenta.
- **Identificador de Firebase Auth:** Token anónimo para gestión de sesión.

## 3. Datos que NO Recopilamos

- ❌ **Contenido visitado:** Jamás almacenamos, transmitimos ni analizamos el contenido de las páginas web.
- ❌ **Contraseñas de terceros:** No tenemos acceso a credenciales de otros servicios.
- ❌ **Ubicación GPS:** No solicitamos ni utilizamos datos de geolocalización.
- ❌ **Contactos, fotos o archivos:** No accedemos al almacenamiento personal.
- ❌ **Datos biométricos:** No recopilamos huellas, reconocimiento facial ni datos de voz.
- ❌ **Historial de llamadas o SMS:** No accedemos a comunicaciones personales.

## 4. Base Legal del Tratamiento (RGPD Art. 6)

| Tratamiento | Base Legal |
|---|---|
| Filtrado local de contenido | Interés legítimo (funcionamiento del servicio) |
| Sincronización con tutor | Consentimiento explícito del usuario |
| Gestión de cuenta | Ejecución del contrato |
| Estadísticas agregadas | Interés legítimo (mejora del servicio) |
| Comunicaciones de soporte | Consentimiento |

## 5. Medidas de Seguridad

- **Cifrado en reposo:** AES-256 para datos locales sensibles (diario, configuración).
- **Cifrado en tránsito:** TLS 1.3 para toda comunicación con servidores.
- **Hashing:** SHA-256 para dominios en listas de bloqueo (imposible reconstruir URLs originales).
- **Autenticación:** Firebase Auth con tokens JWT de corta duración.
- **Minimización de datos:** Solo se recopila lo estrictamente necesario.
- **Acceso restringido:** Los datos del tutor solo son accesibles por el tutor vinculado.

## 6. Retención de Datos

| Tipo de Dato | Período de Retención |
|---|---|
| Datos locales del dispositivo | Hasta que el usuario desinstale la app o los elimine manualmente |
| Estadísticas de tutor en Firebase | 90 días desde la última sincronización |
| Datos de cuenta | Hasta eliminación de la cuenta + 30 días de gracia |
| Datos de suscripción | Según requerimientos fiscales (5 años) |
| Logs de servidor | 30 días (anonimizados después de 7 días) |

## 7. Derechos del Usuario (RGPD Arts. 15-22)

Como usuario, tienes derecho a:

### 7.1 Derecho de Acceso
Solicitar una copia completa de todos los datos personales que procesamos sobre ti.

### 7.2 Derecho de Rectificación
Corregir cualquier dato personal inexacto o incompleto.

### 7.3 Derecho de Supresión ("Derecho al Olvido")
Solicitar la eliminación completa de tus datos personales. Puedes hacerlo desde la app (Ajustes > Eliminar cuenta) o contactándonos por email.

### 7.4 Derecho de Portabilidad
Recibir tus datos en formato estructurado (JSON) para transferirlos a otro servicio.

### 7.5 Derecho de Oposición
Oponerte al procesamiento de tus datos basado en interés legítimo.

### 7.6 Derecho a Limitar el Tratamiento
Solicitar la restricción temporal del procesamiento mientras se resuelve una reclamación.

### 7.7 Derecho a No Ser Objeto de Decisiones Automatizadas
CleanShield no toma decisiones automatizadas con efectos legales sobre los usuarios.

**Plazo de respuesta:** 30 días naturales desde la recepción de la solicitud.  
**Contacto:** privacidad@cleanshield.app

## 8. Transferencias Internacionales

Los datos sincronizados con Firebase se almacenan en servidores de Google Cloud ubicados en la Unión Europea (región europe-west1). No se realizan transferencias fuera del Espacio Económico Europeo.

## 9. Menores de Edad

CleanShield puede ser utilizado por menores de 14 años únicamente con la configuración de control parental activada y el consentimiento verificable de un padre o tutor legal, conforme al Art. 8 del RGPD y la LOPDGDD española.

## 10. Cookies y Tecnologías Similares

La aplicación móvil no utiliza cookies. El panel web de tutor utiliza cookies técnicas esenciales para mantener la sesión activa (Firebase Auth token).

## 11. Cambios en esta Política

Notificaremos cualquier cambio material en esta política mediante:
- Notificación in-app
- Email al correo registrado (si aplica)
- Actualización de la fecha "Última actualización" en este documento

## 12. Contacto y Reclamaciones

**Responsable de Privacidad:**  
Email: privacidad@cleanshield.app

**Autoridad de Control:**  
Agencia Española de Protección de Datos (AEPD)  
https://www.aepd.es  
C/ Jorge Juan, 6 – 28001 Madrid

Tienes derecho a presentar una reclamación ante la AEPD si consideras que el tratamiento de tus datos no se ajusta a la normativa vigente.
