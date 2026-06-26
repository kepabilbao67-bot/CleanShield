package com.cleanshield.app.subscription

import com.cleanshield.app.config.BlockCategory
import com.cleanshield.app.config.EnabledFeatures

/**
 * Planes de suscripción disponibles en CleanShield.
 * Los precios se gestionan a través de Google Play Billing.
 */
enum class SubscriptionPlan(
    val planId: String,
    val displayName: String,
    val monthlyPriceEuros: Double,
    val yearlyPriceEuros: Double,
    val maxCategories: Int,
    val maxDevices: Int,
    val features: List<String>,
    val googlePlayProductId: String
) {
    FREE(
        planId = "free",
        displayName = "Gratis",
        monthlyPriceEuros = 0.0,
        yearlyPriceEuros = 0.0,
        maxCategories = 2,
        maxDevices = 1,
        features = listOf(
            "Bloqueo de 2 categorías",
            "Botón de pánico básico",
            "Contador de racha",
            "1 sesión de meditación guiada",
            "Lecciones introductorias (3)",
            "Estadísticas básicas"
        ),
        googlePlayProductId = ""
    ),
    PRO(
        planId = "pro",
        displayName = "Pro",
        monthlyPriceEuros = 4.99,
        yearlyPriceEuros = 47.99,
        maxCategories = BlockCategory.entries.size,
        maxDevices = 1,
        features = listOf(
            "Bloqueo ilimitado de categorías (11)",
            "Botón de pánico avanzado (llamada + redirección)",
            "Sistema RPG completo (niveles, XP, logros)",
            "Meditaciones ilimitadas (+20 sesiones)",
            "Detección de patrones con IA",
            "Panel de tutor web",
            "Modo nocturno inteligente",
            "Diario personal cifrado",
            "Lecciones completas de neurociencia (30+)",
            "Desafíos diarios y semanales",
            "Base de conocimiento completa",
            "Listas de bloqueo personalizadas",
            "Soporte prioritario"
        ),
        googlePlayProductId = "cleanshield_pro_monthly"
    ),
    FAMILY(
        planId = "family",
        displayName = "Familia",
        monthlyPriceEuros = 9.99,
        yearlyPriceEuros = 95.99,
        maxCategories = BlockCategory.entries.size,
        maxDevices = 5,
        features = listOf(
            "Todo lo incluido en Pro",
            "Hasta 5 dispositivos vinculados",
            "Control parental avanzado",
            "Panel de tutor multi-dispositivo",
            "Alertas en tiempo real",
            "Perfiles individuales por dispositivo",
            "Horarios de bloqueo por dispositivo",
            "Informes semanales por email",
            "Soporte familiar 24/7",
            "Configuración remota de dispositivos"
        ),
        googlePlayProductId = "cleanshield_family_monthly"
    );

    companion object {
        /**
         * Días de prueba gratuita para planes de pago.
         */
        const val TRIAL_DAYS = 7

        /**
         * Porcentaje de descuento del plan anual vs mensual.
         */
        const val YEARLY_DISCOUNT_PERCENT = 20

        /**
         * ID de producto de Google Play para el plan Pro anual.
         */
        const val PRO_YEARLY_PRODUCT_ID = "cleanshield_pro_yearly"

        /**
         * ID de producto de Google Play para el plan Familia anual.
         */
        const val FAMILY_YEARLY_PRODUCT_ID = "cleanshield_family_yearly"

        /**
         * Obtiene el plan por su ID.
         */
        fun fromPlanId(planId: String): SubscriptionPlan {
            return entries.find { it.planId == planId } ?: FREE
        }

        /**
         * Obtiene el plan por su ID de producto de Google Play.
         */
        fun fromGooglePlayProductId(productId: String): SubscriptionPlan {
            return when (productId) {
                PRO.googlePlayProductId, PRO_YEARLY_PRODUCT_ID -> PRO
                FAMILY.googlePlayProductId, FAMILY_YEARLY_PRODUCT_ID -> FAMILY
                else -> FREE
            }
        }
    }

    /**
     * Calcula el ahorro anual al elegir el plan anual vs mensual.
     * @return Ahorro en euros por año.
     */
    fun getYearlySavings(): Double {
        if (monthlyPriceEuros == 0.0) return 0.0
        val yearlyIfMonthly = monthlyPriceEuros * 12
        return yearlyIfMonthly - yearlyPriceEuros
    }

    /**
     * Precio mensual equivalente del plan anual.
     */
    fun getMonthlyEquivalentFromYearly(): Double {
        if (yearlyPriceEuros == 0.0) return 0.0
        return yearlyPriceEuros / 12.0
    }

    /**
     * Verifica si el usuario puede usar una categoría específica con este plan.
     */
    fun canUseCategory(category: BlockCategory): Boolean {
        return when (this) {
            FREE -> !category.isPremium
            PRO, FAMILY -> true
        }
    }

    /**
     * Obtiene las funcionalidades habilitadas para este plan.
     */
    fun getEnabledFeatures(): EnabledFeatures {
        return when (this) {
            FREE -> EnabledFeatures.forFreePlan()
            PRO -> EnabledFeatures.forProPlan()
            FAMILY -> EnabledFeatures.forFamilyPlan()
        }
    }

    /**
     * Verifica si el plan tiene período de prueba.
     */
    fun hasTrialPeriod(): Boolean = this != FREE

    /**
     * Descripción formateada del precio para la UI.
     */
    fun getFormattedPrice(): String {
        return when (this) {
            FREE -> "Gratis"
            else -> "${String.format("%.2f", monthlyPriceEuros)}€/mes"
        }
    }

    /**
     * Descripción formateada del precio anual para la UI.
     */
    fun getFormattedYearlyPrice(): String {
        return when (this) {
            FREE -> "Gratis"
            else -> "${String.format("%.2f", yearlyPriceEuros)}€/año"
        }
    }
}

/**
 * Estado actual de la suscripción del usuario.
 */
data class SubscriptionState(
    val currentPlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val isTrialActive: Boolean = false,
    val trialDaysRemaining: Int = 0,
    val expirationTimestamp: Long = 0L,
    val isAutoRenewing: Boolean = false,
    val purchaseToken: String = "",
    val billingPeriod: BillingPeriod = BillingPeriod.MONTHLY
) {
    val isActive: Boolean
        get() = currentPlan != SubscriptionPlan.FREE &&
                (isTrialActive || System.currentTimeMillis() < expirationTimestamp)

    val isPro: Boolean
        get() = currentPlan == SubscriptionPlan.PRO && isActive

    val isFamily: Boolean
        get() = currentPlan == SubscriptionPlan.FAMILY && isActive

    val hasAnyPaidPlan: Boolean
        get() = isPro || isFamily
}

/**
 * Período de facturación.
 */
enum class BillingPeriod(val months: Int) {
    MONTHLY(1),
    YEARLY(12)
}
