package com.cleanshield.app.vpn

import android.util.Log
import com.cleanshield.app.utils.Constants
import java.util.concurrent.ConcurrentHashMap

class DnsFilter {

    private val blockedDomains: ConcurrentHashMap<String, Boolean> = ConcurrentHashMap()
    private val whitelistedDomains: ConcurrentHashMap<String, Boolean> = ConcurrentHashMap()
    private val blockedKeywords: List<String>
    private val cache: ConcurrentHashMap<String, Boolean> = ConcurrentHashMap(256)

    init {
        // Load blocked domains
        Constants.BLOCKED_DOMAINS.forEach { domain ->
            blockedDomains[domain.lowercase()] = true
        }

        // Load whitelisted domains
        Constants.WHITELISTED_DOMAINS.forEach { domain ->
            whitelistedDomains[domain.lowercase()] = true
        }

        // Load keywords
        blockedKeywords = Constants.ALL_BLOCKED_KEYWORDS.map { it.lowercase() }

        Log.d(TAG, "DnsFilter initialized: ${blockedDomains.size} domains, ${blockedKeywords.size} keywords blocked")
    }

    /**
     * Check if a domain should be blocked.
     * Uses a 3-tier check: whitelist → exact match → parent domain → keyword match
     */
    fun shouldBlock(domain: String): Boolean {
        val normalizedDomain = domain.lowercase().trimEnd('.')

        // Check cache first
        cache[normalizedDomain]?.let { return it }

        val result = performCheck(normalizedDomain)

        // Cache the result (limit cache size)
        if (cache.size < MAX_CACHE_SIZE) {
            cache[normalizedDomain] = result
        }

        if (result) {
            Log.d(TAG, "BLOCKED: $normalizedDomain")
        }

        return result
    }

    private fun performCheck(domain: String): Boolean {
        // 1. Whitelist check - never block whitelisted domains
        if (isWhitelisted(domain)) return false

        // 2. Exact match
        if (blockedDomains.containsKey(domain)) return true

        // 3. Parent domain match (check if any parent is blocked)
        if (isParentBlocked(domain)) return true

        // 4. Keyword match
        if (containsBlockedKeyword(domain)) return true

        return false
    }

    private fun isWhitelisted(domain: String): Boolean {
        // Check exact whitelist
        if (whitelistedDomains.containsKey(domain)) return true

        // Check if domain ends with a whitelisted domain
        for (whitelisted in whitelistedDomains.keys()) {
            if (domain.endsWith(".$whitelisted")) return true
        }
        return false
    }

    private fun isParentBlocked(domain: String): Boolean {
        val parts = domain.split(".")
        if (parts.size <= 2) return false

        // Build parent domains progressively
        for (i in 1 until parts.size - 1) {
            val parent = parts.subList(i, parts.size).joinToString(".")
            if (blockedDomains.containsKey(parent)) return true
        }
        return false
    }

    private fun containsBlockedKeyword(domain: String): Boolean {
        for (keyword in blockedKeywords) {
            if (domain.contains(keyword)) return true
        }
        return false
    }

    /**
     * Add a domain to the blocklist at runtime
     */
    fun addBlockedDomain(domain: String) {
        val normalized = domain.lowercase().trimEnd('.')
        blockedDomains[normalized] = true
        // Invalidate cache entries that might be affected
        cache.keys.removeAll { it == normalized || it.endsWith(".$normalized") }
    }

    /**
     * Remove a domain from the blocklist
     */
    fun removeBlockedDomain(domain: String) {
        val normalized = domain.lowercase().trimEnd('.')
        blockedDomains.remove(normalized)
        cache.remove(normalized)
    }

    /**
     * Add a domain to whitelist
     */
    fun addWhitelistedDomain(domain: String) {
        val normalized = domain.lowercase().trimEnd('.')
        whitelistedDomains[normalized] = true
        // Invalidate related cache entries
        cache.keys.removeAll { it == normalized || it.endsWith(".$normalized") }
    }

    /**
     * Clear the resolution cache
     */
    fun clearCache() {
        cache.clear()
    }

    /**
     * Get statistics about the filter
     */
    fun getStats(): FilterStats {
        return FilterStats(
            blockedDomainsCount = blockedDomains.size,
            keywordsCount = blockedKeywords.size,
            whitelistedCount = whitelistedDomains.size,
            cacheSize = cache.size
        )
    }

    data class FilterStats(
        val blockedDomainsCount: Int,
        val keywordsCount: Int,
        val whitelistedCount: Int,
        val cacheSize: Int
    )

    companion object {
        private const val TAG = "DnsFilter"
        private const val MAX_CACHE_SIZE = 1024
    }
}
