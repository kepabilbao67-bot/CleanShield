package com.cleanshield.app.utils

object Constants {

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED PACKAGES - BROWSERS (25)
    // ═══════════════════════════════════════════════════════════════
    val BLOCKED_BROWSERS = listOf(
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.opera.browser",
        "com.opera.mini.native",
        "com.brave.browser",
        "com.vivaldi.browser",
        "com.duckduckgo.mobile.android",
        "com.microsoft.emmx",
        "com.UCMobile.intl",
        "com.kiwibrowser.browser",
        "org.bromite.bromite",
        "com.yandex.browser",
        "com.sec.android.app.sbrowser",
        "org.torproject.torbrowser",
        "com.cloudflare.onedotonedotonedotone",
        "org.chromium.chrome",
        "com.phlox.tvwebbrowser",
        "acr.browser.barebones",
        "acr.browser.lightning",
        "com.opera.gx",
        "mark.via.gp",
        "com.aspect.browser",
        "org.mozilla.focus",
        "com.stoutner.privacybrowser.standard",
        "org.gnu.icecat"
    )

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED PACKAGES - LOAN APPS (15)
    // ═══════════════════════════════════════════════════════════════
    val BLOCKED_LOAN_APPS = listOf(
        "com.moneyman.android",
        "mx.com.kueski.android",
        "com.dineria.app",
        "com.credito.maestro",
        "mx.com.vivus",
        "com.tala",
        "com.branch.borrower",
        "com.lana.app",
        "com.okredito.android",
        "com.zenfi.app",
        "com.pezetita.android",
        "mx.com.creditea",
        "com.cashbean.android",
        "com.moneycat.app",
        "com.rapidopeso.android"
    )

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED PACKAGES - VPN/BYPASS APPS (14)
    // ═══════════════════════════════════════════════════════════════
    val BLOCKED_VPN_APPS = listOf(
        "com.nordvpn.android",
        "com.expressvpn.vpn",
        "com.surfshark.vpnclient.android",
        "net.openvpn.openvpn",
        "org.strongswan.android",
        "com.wireguard.android",
        "de.blinkt.openvpn",
        "com.protonvpn.android",
        "com.tunnelbear.android",
        "com.hotspot.vpn.free",
        "com.windscribe.vpn",
        "com.psiphon3",
        "org.torproject.android",
        "com.ultrasurf.android"
    )

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED PACKAGES - ALTERNATIVE STORES (7)
    // ═══════════════════════════════════════════════════════════════
    val BLOCKED_STORES = listOf(
        "com.aptoide.partners",
        "cm.aptoide.pt",
        "com.aurora.store",
        "org.fdroid.fdroid",
        "com.uptodown.installer",
        "com.apkpure.aegon",
        "com.slideme.sam.manager"
    )

    // All blocked packages combined
    val ALL_BLOCKED_PACKAGES: List<String> by lazy {
        BLOCKED_BROWSERS + BLOCKED_LOAN_APPS + BLOCKED_VPN_APPS + BLOCKED_STORES
    }

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED KEYWORDS - PORNOGRAPHY (30)
    // ═══════════════════════════════════════════════════════════════
    val PORN_KEYWORDS = listOf(
        "pornhub", "xvideos", "xnxx", "xhamster", "redtube",
        "youporn", "tube8", "spankbang", "brazzers", "bangbros",
        "naughtyamerica", "realitykings", "mofos", "fakehub", "onlyfans",
        "chaturbate", "stripchat", "cam4", "livejasmin", "bongacams",
        "fapello", "hentai", "nhentai", "hanime", "rule34",
        "e621", "gelbooru", "danbooru", "sankaku", "hitomi"
    )

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED KEYWORDS - GAMBLING/LOANS (20)
    // ═══════════════════════════════════════════════════════════════
    val GAMBLING_LOAN_KEYWORDS = listOf(
        "casino", "apuesta", "apostar", "ruleta", "slots",
        "blackjack", "poker-online", "bet365", "caliente.mx", "codere",
        "prestamo-rapido", "credito-facil", "dinero-ya", "prestamos-online", "microfinanzas",
        "pagadiario", "gota-gota", "usura", "montadeuda", "empenio"
    )

    // All blocked keywords combined
    val ALL_BLOCKED_KEYWORDS: List<String> by lazy {
        PORN_KEYWORDS + GAMBLING_LOAN_KEYWORDS
    }

    // ═══════════════════════════════════════════════════════════════
    // BLOCKED DOMAINS (complete list loaded from assets)
    // ═══════════════════════════════════════════════════════════════
    val BLOCKED_DOMAINS = listOf(
        "pornhub.com", "www.pornhub.com",
        "xvideos.com", "www.xvideos.com",
        "xnxx.com", "www.xnxx.com",
        "xhamster.com", "www.xhamster.com",
        "redtube.com", "www.redtube.com",
        "youporn.com", "www.youporn.com",
        "tube8.com", "www.tube8.com",
        "spankbang.com", "www.spankbang.com",
        "brazzers.com", "www.brazzers.com",
        "onlyfans.com", "www.onlyfans.com",
        "chaturbate.com", "www.chaturbate.com",
        "stripchat.com", "www.stripchat.com",
        "cam4.com", "www.cam4.com",
        "livejasmin.com", "www.livejasmin.com",
        "bongacams.com", "www.bongacams.com",
        "nhentai.net", "www.nhentai.net",
        "hanime.tv", "www.hanime.tv",
        "rule34.xxx", "e621.net",
        "gelbooru.com", "danbooru.donmai.us",
        "hitomi.la", "fapello.com",
        "bet365.com", "www.bet365.com",
        "caliente.mx", "www.caliente.mx",
        "codere.mx", "www.codere.mx"
    )

    // ═══════════════════════════════════════════════════════════════
    // WHITELIST DOMAINS (never block these)
    // ═══════════════════════════════════════════════════════════════
    val WHITELISTED_DOMAINS = listOf(
        "google.com", "www.google.com",
        "googleapis.com",
        "gstatic.com",
        "firebase.google.com",
        "firebaseio.com",
        "play.google.com",
        "android.com",
        "cleanshield.app",
        "api.cleanshield.app"
    )

    // ═══════════════════════════════════════════════════════════════
    // MOTIVATIONAL QUOTES (Spanish) - 15
    // ═══════════════════════════════════════════════════════════════
    val MOTIVATIONAL_QUOTES = listOf(
        "Cada día que resistes te hace más fuerte. Sigue adelante.",
        "No mires atrás, tú ya no vas en esa dirección.",
        "La libertad empieza donde termina la adicción.",
        "Eres más fuerte de lo que crees. Tu racha lo demuestra.",
        "El progreso no es lineal, pero cada intento cuenta.",
        "Tu futuro yo te agradecerá esta decisión.",
        "La disciplina es el puente entre metas y logros.",
        "Un paso a la vez. Un día a la vez. Tú puedes.",
        "No necesitas ser perfecto, solo necesitas no rendirte.",
        "Tu valor como persona no depende de tus errores pasados.",
        "Cada momento de tentación superado es una victoria.",
        "Recuerda: buscas progreso, no perfección.",
        "Las cadenas del hábito son demasiado ligeras para sentirse hasta que son demasiado pesadas para romperse.",
        "Hoy es un buen día para ser libre.",
        "Tu cerebro se está reconectando. Dale tiempo y paciencia."
    )

    // ═══════════════════════════════════════════════════════════════
    // VPN CONSTANTS
    // ═══════════════════════════════════════════════════════════════
    object Vpn {
        const val VPN_ADDRESS = "10.0.0.2"
        const val VPN_ROUTE = "0.0.0.0"
        const val VPN_DNS_PRIMARY = "8.8.8.8"
        const val VPN_DNS_SECONDARY = "8.8.4.4"
        const val VPN_MTU = 1500
        const val DNS_PORT = 53
        const val BLOCKED_IP = "0.0.0.0"
        const val SESSION_NAME = "CleanShieldVPN"
        const val DNS_QUERY_TIMEOUT_MS = 5000L
        const val MAX_PACKET_SIZE = 32767
    }

    // ═══════════════════════════════════════════════════════════════
    // DNS DE FILTRO FAMILIAR (bloqueo a nivel de sistema)
    // ═══════════════════════════════════════════════════════════════
    object Dns {
        // DNS con filtro familiar (bloquea porno en todo el movil, cifrado).
        const val DEFAULT_HOST = "family.adguard-dns.com"
        // Alternativa: "family.cleanbrowsing.org" / "family.cloudflare-dns.com"

        // Claves del sistema Android (Settings.Global) para el "DNS privado".
        const val SETTING_MODE = "private_dns_mode"
        const val SETTING_SPECIFIER = "private_dns_specifier"
        const val MODE_HOSTNAME = "hostname"
        const val MODE_OFF = "off"

        // Intervalo de vigilancia del DNS (ms)
        const val ENFORCE_INTERVAL_MS = 3000L
        // Tiempo minimo entre avisos al tutor (ms)
        const val ALERT_THROTTLE_MS = 5 * 60 * 1000L
    }

    // ═══════════════════════════════════════════════════════════════
    // PREFERENCES KEYS
    // ═══════════════════════════════════════════════════════════════
    object Prefs {
        const val FILE_NAME = "cleanshield_secure_prefs"
        const val KEY_PASSWORD_HASH = "password_hash"
        const val KEY_VPN_ENABLED = "vpn_enabled"
        const val KEY_APP_BLOCKER_ENABLED = "app_blocker_enabled"
        const val KEY_NIGHT_MODE = "night_mode_enabled"
        const val KEY_STREAK_START = "streak_start_date"
        const val KEY_TOTAL_BLOCKED = "total_blocked_count"
        const val KEY_FIRST_LAUNCH = "is_first_launch"
        const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
        const val KEY_EMERGENCY_CONTACT = "emergency_contact"
        const val KEY_LAST_RELAPSE = "last_relapse_date"
        // Modo tutor / proteccion anti-manipulacion
        const val KEY_GUARDIAN_PHONE = "guardian_phone"
        const val KEY_DNS_LOCK_ENABLED = "dns_lock_enabled"
        const val KEY_DNS_HOST = "dns_host"
        const val KEY_LAST_TAMPER_ALERT = "last_tamper_alert"
    }

    // ═══════════════════════════════════════════════════════════════
    // WORKER TAGS
    // ═══════════════════════════════════════════════════════════════
    object Workers {
        const val DAILY_MOTIVATION = "daily_motivation_worker"
        const val STREAK_CHECK = "streak_check_worker"
        const val BLOCKLIST_UPDATE = "blocklist_update_worker"
        const val STATS_SYNC = "stats_sync_worker"
    }
}
