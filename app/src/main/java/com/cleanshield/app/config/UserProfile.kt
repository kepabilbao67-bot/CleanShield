package com.cleanshield.app.config

/**
 * Tipos de perfil de usuario que determinan la configuración predeterminada
 * de la aplicación y las categorías de bloqueo activas.
 */
enum class ProfileType(
    val displayName: String,
    val description: String,
    val defaultCategories: List<BlockCategory>,
    val defaultRestrictionLevel: RestrictionLevel
) {
    PORN_FREE(
        displayName = "Libre de Pornografía",
        description = "Bloqueo de contenido pornográfico y erótico. Ideal para quien quiere dejar el consumo de pornografía.",
        defaultCategories = listOf(
            BlockCategory.PORNOGRAPHY,
            BlockCategory.ADULT_CONTENT,
            BlockCategory.DATING_HOOKUPS,
            BlockCategory.NSFW_SOCIAL
        ),
        defaultRestrictionLevel = RestrictionLevel.HIGH
    ),
    FINANCIAL_CONTROL(
        displayName = "Control Financiero",
        description = "Bloqueo de sitios de apuestas, casinos y compras compulsivas.",
        defaultCategories = listOf(
            BlockCategory.GAMBLING,
            BlockCategory.SHOPPING_COMPULSIVE
        ),
        defaultRestrictionLevel = RestrictionLevel.HIGH
    ),
    DIGITAL_DETOX(
        displayName = "Detox Digital",
        description = "Reducción del uso de redes sociales y contenido adictivo. Para recuperar tu tiempo.",
        defaultCategories = listOf(
            BlockCategory.SOCIAL_MEDIA,
            BlockCategory.VIDEO_STREAMING,
            BlockCategory.NSFW_SOCIAL
        ),
        defaultRestrictionLevel = RestrictionLevel.MEDIUM
    ),
    FULL_PROTECTION(
        displayName = "Protección Completa",
        description = "Bloqueo máximo de todas las categorías. Para quien necesita el nivel más alto de protección.",
        defaultCategories = BlockCategory.entries.toList(),
        defaultRestrictionLevel = RestrictionLevel.MAXIMUM
    ),
    PARENTAL(
        displayName = "Control Parental",
        description = "Configuración para proteger a menores de contenido inapropiado.",
        defaultCategories = listOf(
            BlockCategory.PORNOGRAPHY,
            BlockCategory.ADULT_CONTENT,
            BlockCategory.GAMBLING,
            BlockCategory.VIOLENCE_GORE,
            BlockCategory.DRUGS_ALCOHOL,
            BlockCategory.DATING_HOOKUPS,
            BlockCategory.NSFW_SOCIAL
        ),
        defaultRestrictionLevel = RestrictionLevel.MAXIMUM
    ),
    CUSTOM(
        displayName = "Personalizado",
        description = "Configura manualmente las categorías y nivel de restricción según tus necesidades.",
        defaultCategories = emptyList(),
        defaultRestrictionLevel = RestrictionLevel.MEDIUM
    )
}

/**
 * Categorías de contenido que pueden ser bloqueadas.
 * Las categorías premium requieren suscripción Pro o Familia.
 */
enum class BlockCategory(
    val displayName: String,
    val description: String,
    val isPremium: Boolean,
    val domainListFile: String
) {
    PORNOGRAPHY(
        displayName = "Pornografía",
        description = "Sitios web y apps de contenido pornográfico explícito",
        isPremium = false,
        domainListFile = "blocklist_pornography.txt"
    ),
    ADULT_CONTENT(
        displayName = "Contenido Adulto",
        description = "Contenido erótico, webcams, tiendas para adultos",
        isPremium = false,
        domainListFile = "blocklist_adult.txt"
    ),
    GAMBLING(
        displayName = "Apuestas y Casinos",
        description = "Casinos online, apuestas deportivas, loterías, poker",
        isPremium = true,
        domainListFile = "blocklist_gambling.txt"
    ),
    SOCIAL_MEDIA(
        displayName = "Redes Sociales",
        description = "Instagram, TikTok, Twitter/X, Facebook, Snapchat",
        isPremium = true,
        domainListFile = "blocklist_social.txt"
    ),
    VIDEO_STREAMING(
        displayName = "Streaming de Vídeo",
        description = "YouTube, Twitch, Netflix y plataformas de vídeo",
        isPremium = true,
        domainListFile = "blocklist_streaming.txt"
    ),
    DATING_HOOKUPS(
        displayName = "Citas y Encuentros",
        description = "Tinder, Bumble, Grindr y apps de citas casuales",
        isPremium = true,
        domainListFile = "blocklist_dating.txt"
    ),
    NSFW_SOCIAL(
        displayName = "NSFW en Redes",
        description = "Subreddits NSFW, Tumblr adulto, Twitter sin filtro",
        isPremium = false,
        domainListFile = "blocklist_nsfw_social.txt"
    ),
    VIOLENCE_GORE(
        displayName = "Violencia y Gore",
        description = "Contenido violento extremo y gore",
        isPremium = true,
        domainListFile = "blocklist_violence.txt"
    ),
    DRUGS_ALCOHOL(
        displayName = "Drogas y Alcohol",
        description = "Sitios de venta, promoción o información sobre drogas y alcohol",
        isPremium = true,
        domainListFile = "blocklist_drugs.txt"
    ),
    SHOPPING_COMPULSIVE(
        displayName = "Compras Compulsivas",
        description = "Marketplaces, tiendas online, ofertas flash",
        isPremium = true,
        domainListFile = "blocklist_shopping.txt"
    ),
    PIRACY(
        displayName = "Piratería",
        description = "Sitios de descargas ilegales, torrents, streaming pirata",
        isPremium = true,
        domainListFile = "blocklist_piracy.txt"
    );

    companion object {
        /**
         * Devuelve solo las categorías gratuitas disponibles sin suscripción.
         */
        fun freeCategories(): List<BlockCategory> = entries.filter { !it.isPremium }

        /**
         * Devuelve las categorías premium que requieren suscripción.
         */
        fun premiumCategories(): List<BlockCategory> = entries.filter { it.isPremium }

        /**
         * Máximo de categorías permitidas en el plan gratuito.
         */
        const val FREE_PLAN_MAX_CATEGORIES = 2
    }
}

/**
 * Nivel de restricción que determina la agresividad del filtrado.
 */
enum class RestrictionLevel(
    val displayName: String,
    val description: String,
    val blockSubdomains: Boolean,
    val blockSimilarDomains: Boolean,
    val blockMixedContent: Boolean,
    val requireCodeToDisable: Boolean
) {
    MEDIUM(
        displayName = "Medio",
        description = "Bloquea dominios exactos de las listas. Permite desactivar sin código.",
        blockSubdomains = true,
        blockSimilarDomains = false,
        blockMixedContent = false,
        requireCodeToDisable = false
    ),
    HIGH(
        displayName = "Alto",
        description = "Bloquea dominios, subdominios y contenido mixto. Requiere código para desactivar.",
        blockSubdomains = true,
        blockSimilarDomains = true,
        blockMixedContent = true,
        requireCodeToDisable = true
    ),
    MAXIMUM(
        displayName = "Máximo",
        description = "Bloqueo total con protección anti-desinstalación. Requiere código + espera de 24h para desactivar.",
        blockSubdomains = true,
        blockSimilarDomains = true,
        blockMixedContent = true,
        requireCodeToDisable = true
    )
}

/**
 * Idiomas soportados por la aplicación.
 */
enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String
) {
    SPANISH("es", "Spanish", "Español"),
    ENGLISH("en", "English", "English"),
    PORTUGUESE("pt", "Portuguese", "Português"),
    FRENCH("fr", "French", "Français"),
    ITALIAN("it", "Italian", "Italiano"),
    GERMAN("de", "German", "Deutsch");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: SPANISH
        }
    }
}

/**
 * Funcionalidades habilitadas según el perfil y plan del usuario.
 */
data class EnabledFeatures(
    val dnsFiltering: Boolean = true,
    val panicButton: Boolean = true,
    val streakCounter: Boolean = true,
    val basicMeditation: Boolean = true,
    val journal: Boolean = false,
    val rpgSystem: Boolean = false,
    val fullMeditations: Boolean = false,
    val patternDetection: Boolean = false,
    val tutorPanel: Boolean = false,
    val nightMode: Boolean = false,
    val challenges: Boolean = false,
    val knowledgeBase: Boolean = false,
    val customBlocklists: Boolean = false,
    val multiDevice: Boolean = false,
    val parentalControls: Boolean = false,
    val realTimeAlerts: Boolean = false
) {
    companion object {
        /**
         * Funcionalidades disponibles en el plan gratuito.
         */
        fun forFreePlan(): EnabledFeatures = EnabledFeatures(
            dnsFiltering = true,
            panicButton = true,
            streakCounter = true,
            basicMeditation = true,
            journal = false,
            rpgSystem = false,
            fullMeditations = false,
            patternDetection = false,
            tutorPanel = false,
            nightMode = false,
            challenges = false,
            knowledgeBase = false,
            customBlocklists = false,
            multiDevice = false,
            parentalControls = false,
            realTimeAlerts = false
        )

        /**
         * Funcionalidades disponibles en el plan Pro.
         */
        fun forProPlan(): EnabledFeatures = EnabledFeatures(
            dnsFiltering = true,
            panicButton = true,
            streakCounter = true,
            basicMeditation = true,
            journal = true,
            rpgSystem = true,
            fullMeditations = true,
            patternDetection = true,
            tutorPanel = true,
            nightMode = true,
            challenges = true,
            knowledgeBase = true,
            customBlocklists = true,
            multiDevice = false,
            parentalControls = false,
            realTimeAlerts = false
        )

        /**
         * Funcionalidades disponibles en el plan Familia.
         */
        fun forFamilyPlan(): EnabledFeatures = EnabledFeatures(
            dnsFiltering = true,
            panicButton = true,
            streakCounter = true,
            basicMeditation = true,
            journal = true,
            rpgSystem = true,
            fullMeditations = true,
            patternDetection = true,
            tutorPanel = true,
            nightMode = true,
            challenges = true,
            knowledgeBase = true,
            customBlocklists = true,
            multiDevice = true,
            parentalControls = true,
            realTimeAlerts = true
        )
    }
}
