// ═══════════════════════════════════════════════════════════════
// CleanShield Content Script - Keyword detection in page content
// ═══════════════════════════════════════════════════════════════

const DANGER_KEYWORDS = [
  "pornhub", "xvideos", "xnxx", "xhamster", "redtube",
  "youporn", "brazzers", "onlyfans", "chaturbate", "stripchat",
  "porn", "xxx", "nude", "naked", "nsfw", "hentai",
  "casino", "apuesta", "apostar", "bet365", "slots",
  "prestamo-rapido", "credito-facil", "prestamos-online"
];

// Check URL immediately on load
(function checkCurrentPage() {
  const url = window.location.href.toLowerCase();
  const hostname = window.location.hostname.toLowerCase();
  
  const isBlocked = DANGER_KEYWORDS.some(kw => 
    url.includes(kw) || hostname.includes(kw)
  );
  
  if (isBlocked) {
    chrome.runtime.sendMessage({ type: 'PAGE_BLOCKED', url: window.location.href });
    window.stop(); // Stop loading the page immediately
    window.location.href = chrome.runtime.getURL('blocked.html');
  }
})();

// Monitor dynamic navigation (SPA sites) - wait for DOM
function startObserver() {
  let lastUrl = window.location.href;
  const observer = new MutationObserver(() => {
    if (window.location.href !== lastUrl) {
      lastUrl = window.location.href;
      const url = lastUrl.toLowerCase();
      const isBlocked = DANGER_KEYWORDS.some(kw => url.includes(kw));
      if (isBlocked) {
        window.stop();
        window.location.href = chrome.runtime.getURL('blocked.html');
      }
    }
  });

  const target = document.body || document.documentElement;
  if (target) {
    observer.observe(target, { childList: true, subtree: true });
  }
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', startObserver);
} else {
  startObserver();
}
