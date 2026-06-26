# Plan de Negocio - CleanShield

## Resumen Ejecutivo

CleanShield es una aplicación Android de bienestar digital que combina bloqueo inteligente de contenido para adultos con herramientas de recuperación basadas en neurociencia y gamificación. Modelo freemium con planes Pro (4,99€/mes) y Familia (9,99€/mes).

---

## 1. Análisis de Mercado

### Tamaño del Mercado

- **Mercado total direccionable (TAM):** +50 millones de hispanohablantes que buscan activamente dejar el consumo de pornografía (basado en tráfico de comunidades como NoFap, búsquedas en Google y estadísticas de apps competidoras).
- **Mercado serviceable (SAM):** ~8 millones de usuarios Android en España y Latinoamérica que han descargado apps de bloqueo o bienestar digital.
- **Mercado objetivo inicial (SOM):** 50.000 descargas en el primer año, con conversión del 3-5% a planes de pago.

### Tendencias del Mercado

- Crecimiento del 340% en búsquedas de "dejar pornografía" en español (2020-2024).
- El mercado de bienestar digital creció un 25% anual.
- Regulaciones europeas cada vez más estrictas sobre contenido para adultos.
- Mayor concienciación social sobre adicciones conductuales.

### Problema que Resolvemos

Las soluciones existentes solo bloquean contenido. El 87% de usuarios que solo usan bloqueadores recaen en 30 días porque no abordan la causa raíz: el mecanismo neurológico de la adicción.

---

## 2. Modelo de Precios (Freemium)

| Plan | Precio | Características |
|------|--------|----------------|
| **Gratis** | 0€ | 2 categorías de bloqueo, botón de pánico básico, 1 meditación, contador de racha |
| **Pro** | 4,99€/mes (47,99€/año) | Todo ilimitado: 11 categorías, RPG, IA, meditaciones, tutor, modo nocturno |
| **Familia** | 9,99€/mes (95,99€/año) | Todo Pro + 5 dispositivos, control parental avanzado, alertas en tiempo real |

**Prueba gratuita:** 7 días para planes de pago.  
**Ahorro anual:** 20% vs. pago mensual.

---

## 3. Proyecciones de Ingresos (Año 1)

### Escenario Conservador

| Mes | Descargas Acum. | Suscriptores Pro | Suscriptores Familia | MRR |
|-----|-----------------|------------------|-----------------------|-----|
| 1 | 1.000 | 15 | 3 | 105€ |
| 2 | 3.000 | 40 | 8 | 280€ |
| 3 | 6.000 | 80 | 15 | 550€ |
| 4 | 10.000 | 140 | 25 | 950€ |
| 5 | 15.000 | 220 | 40 | 1.500€ |
| 6 | 22.000 | 330 | 60 | 2.250€ |
| 9 | 35.000 | 600 | 100 | 4.000€ |
| 12 | 50.000 | 900 | 160 | 6.100€ |

**Ingresos anuales (conservador):** ~35.000€  
**Tasa de conversión asumida:** 2-3%

### Escenario Optimista

| Mes | Descargas Acum. | Suscriptores Pro | Suscriptores Familia | MRR |
|-----|-----------------|------------------|-----------------------|-----|
| 1 | 3.000 | 45 | 10 | 325€ |
| 2 | 8.000 | 120 | 25 | 850€ |
| 3 | 15.000 | 250 | 50 | 1.750€ |
| 4 | 25.000 | 450 | 80 | 3.050€ |
| 5 | 38.000 | 700 | 120 | 4.700€ |
| 6 | 55.000 | 1.000 | 180 | 6.800€ |
| 9 | 90.000 | 1.800 | 300 | 12.000€ |
| 12 | 130.000 | 2.800 | 500 | 19.000€ |

**Ingresos anuales (optimista):** ~100.000€  
**Tasa de conversión asumida:** 4-5%

---

## 4. Costes Mensuales

| Concepto | Coste |
|----------|-------|
| Firebase (Spark → Blaze) | 0-50€ |
| Dominio + hosting landing | 10€ |
| Google Play Developer | 2€ (25€/año) |
| Servidor panel tutor (Vercel/Netlify) | 0€ |
| Email transaccional (Resend) | 0€ (tier gratuito) |
| Herramientas de desarrollo | 0€ |
| Marketing pagado (primeros meses) | 100-200€ |
| **Total mensual** | **<300€** |

---

## 5. Punto de Equilibrio (Break-Even)

Con costes mensuales de ~250€:
- **Conservador:** Mes 3-4 (60 suscriptores Pro equivalentes)
- **Optimista:** Mes 2 (50 suscriptores Pro equivalentes)

**Cálculo:** 250€ / 4,99€ × 0.70 (Google toma 30%) = ~72 suscriptores Pro para break-even

**Nota:** Google cobra 15% para los primeros $1M de ingresos anuales (Google Play Small Business Program), por lo que el fee efectivo es del 15%:
- Con 15% fee: 250€ / (4,99€ × 0.85) = ~59 suscriptores Pro

---

## 6. Canales de Marketing

### Orgánico (Coste 0€)

| Canal | Estrategia | Impacto Esperado |
|-------|-----------|------------------|
| **ASO (App Store Optimization)** | Keywords en español: "bloquear porno", "bienestar digital", "NoFap" | 40% de descargas |
| **Reddit** | Posts en r/NoFap, r/pornfree, r/nofap_es, r/adicciones | 20% de descargas |
| **SEO Landing** | Blog con artículos sobre recuperación y neurociencia | 15% de descargas |
| **Word of mouth** | Sistema de referidos in-app (7 días Pro gratis) | 15% de descargas |

### Pagado (100-200€/mes inicialmente)

| Canal | Presupuesto | CPI Esperado |
|-------|-------------|--------------|
| **Google Ads (UAC)** | 100€/mes | 0,50-1,50€ |
| **Instagram/TikTok** | 50-100€/mes | 1,00-2,00€ |

### Influencers y Comunidad

- Colaboraciones con creadores en nicho de desarrollo personal, salud mental y masculinidad positiva.
- Alianzas con psicólogos y terapeutas que recomienden la app.
- Presencia en foros y comunidades en español.

---

## 7. Ventaja Competitiva

| Aspecto | CleanShield | Competencia |
|---------|-------------|-------------|
| Bloqueo + Recuperación | ✅ Integrado | ❌ Solo bloqueo |
| Idioma nativo español | ✅ | ❌ Traducciones básicas |
| Gamificación RPG | ✅ Sistema completo | ❌ Básico o inexistente |
| Neurociencia | ✅ Lecciones interactivas | ❌ No incluido |
| Precio | Desde 0€ | Desde 7,99€/mes |
| Privacidad RGPD | ✅ Datos locales | ⚠️ Envío a servidores |

**Moat principal:** Ser la única app que combina bloqueo efectivo con un sistema completo de recuperación, gamificación y ciencia, diseñada nativamente en español.

---

## 8. Roadmap de Lanzamiento (5 semanas)

### Semana 1: Fundamentos
- [x] Arquitectura del proyecto
- [x] Sistema de bloqueo DNS (VPN local)
- [x] UI principal con Jetpack Compose
- [x] Configuración Firebase

### Semana 2: Funcionalidades Core
- [ ] Botón de pánico completo
- [ ] Sistema RPG (XP, niveles, logros)
- [ ] Meditaciones guiadas (audio)
- [ ] Panel de tutor web

### Semana 3: Inteligencia
- [ ] Detección de patrones con IA local
- [ ] Modo nocturno inteligente
- [ ] Sistema de notificaciones motivacionales
- [ ] Integración suscripciones Google Play

### Semana 4: Pulido
- [ ] Testing completo
- [ ] Optimización de batería
- [ ] Diseño final (animaciones, microinteracciones)
- [ ] Contenido educativo (10 lecciones)

### Semana 5: Lanzamiento
- [ ] Beta cerrada (50 usuarios)
- [ ] Corrección de bugs
- [ ] Publicación en Google Play
- [ ] Campaña de lanzamiento en redes

---

## 9. Métricas Clave (KPIs)

| Métrica | Objetivo Mes 1 | Objetivo Mes 6 |
|---------|---------------|----------------|
| Descargas | 1.000 | 22.000 |
| DAU (Daily Active Users) | 200 | 5.000 |
| Retención D7 | 40% | 50% |
| Retención D30 | 20% | 30% |
| Conversión a Pro | 2% | 3% |
| Churn mensual | <10% | <8% |
| ARPU | 0,10€ | 0,15€ |
| LTV (Pro) | 25€ | 40€ |

---

## 10. Riesgos y Mitigación

| Riesgo | Probabilidad | Mitigación |
|--------|-------------|------------|
| Rechazo de Google Play por permisos | Media | Documentación detallada, declaraciones de permisos exhaustivas |
| Baja conversión a pago | Media | A/B testing de paywall, contenido gratuito atractivo |
| Competencia lanza features similares | Baja | Velocidad de iteración, comunidad fidelizada |
| Problemas con actualizaciones de Android | Media | Testing en múltiples versiones, adaptación rápida |
| Churn alto | Media | Onboarding fuerte, notificaciones inteligentes, gamificación |

---

## Conclusión

CleanShield tiene potencial para capturar un nicho desatendido: hispanohablantes que buscan dejar la pornografía con herramientas reales, no solo bloqueadores. Con costes operativos mínimos (<300€/mes) y un mercado de +50M de personas, el break-even se alcanza con apenas 60-140 suscriptores (mes 3-4). El modelo freemium permite crecimiento orgánico mientras la gamificación y las herramientas de recuperación generan retención y conversión superiores a la competencia.
