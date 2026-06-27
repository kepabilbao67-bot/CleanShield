// ═══════════════════════════════════════════════════════════════
// CleanShield Chrome Extension - Background Service Worker
// ═══════════════════════════════════════════════════════════════

const BLOCKED_DOMAINS = [
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
  "codere.mx", "www.codere.mx",
  "888casino.com", "www.888casino.com",
  "pokerstars.com", "www.pokerstars.com",
  "betway.com", "www.betway.com",
  "williamhill.com", "www.williamhill.com",
  "bwin.com", "www.bwin.com",
  "1xbet.com", "www.1xbet.com",
  "mostbet.com", "www.mostbet.com"
];

const PORN_KEYWORDS = [
  "pornhub", "xvideos", "xnxx", "xhamster", "redtube",
  "youporn", "tube8", "spankbang", "brazzers", "bangbros",
  "naughtyamerica", "realitykings", "mofos", "fakehub", "onlyfans",
  "chaturbate", "stripchat", "cam4", "livejasmin", "bongacams",
  "fapello", "hentai", "nhentai", "hanime", "rule34",
  "e621", "gelbooru", "danbooru", "sankaku", "hitomi",
  "porn", "xxx", "sex", "nude", "naked", "nsfw",
  "camgirl", "webcam-adult", "escort", "prostitut"
];

const GAMBLING_LOAN_KEYWORDS = [
  "casino", "apuesta", "apostar", "ruleta", "slots",
  "blackjack", "poker-online", "bet365", "caliente.mx", "codere",
  "prestamo-rapido", "credito-facil", "dinero-ya", "prestamos-online", "microfinanzas",
  "pagadiario", "gota-gota", "usura", "montadeuda", "empenio",
  "gambling", "betting", "jackpot", "slot-machine",
  "prestamo-personal", "credito-express", "dinero-rapido", "microprestamo"
];

const ALL_KEYWORDS = [...PORN_KEYWORDS, ...GAMBLING_LOAN_KEYWORDS];

const WHITELISTED_DOMAINS = [
  "google.com", "www.google.com",
  "googleapis.com", "gstatic.com",
  "chrome.google.com", "chromewebstore.google.com",
  "github.com", "stackoverflow.com",
  "youtube.com", "www.youtube.com",
  "wikipedia.org", "cleanshield.app"
];

// ═══════════════════════════════════════════════════════════════
// INITIALIZATION
// ═══════════════════════════════════════════════════════════════

chrome.runtime.onInstalled.addListener(async () => {
  const data = await chrome.storage.local.get(null);
  
  if (!data.installed) {
    await chrome.storage.local.set({
      installed: true,
      protectionEnabled: true,
      passwordHash: null,
      streakStart: new Date().toISOString(),
      totalBlocked: 0,
      blockedToday: 0,
      lastBlockDate: null,
      journalEntries: [],
      panicContact: '',
      nightModeEnabled: true,
      nightModeStart: 22,
      nightModeEnd: 6,
      customBlockedDomains: [],
      lastResetDate: new Date().toDateString()
    });
  }
  
  // Set up daily reset alarm
  chrome.alarms.create('dailyReset', { periodInMinutes: 60 });
  chrome.alarms.create('streakCheck', { periodInMinutes: 1440 });
});

// ═══════════════════════════════════════════════════════════════
// URL CHECKING & BLOCKING
// ═══════════════════════════════════════════════════════════════

function isWhitelisted(url) {
  try {
    const hostname = new URL(url).hostname.toLowerCase();
    return WHITELISTED_DOMAINS.some(domain => 
      hostname === domain || hostname.endsWith('.' + domain)
    );
  } catch {
    return false;
  }
}

function isDomainBlocked(url) {
  try {
    const hostname = new URL(url).hostname.toLowerCase();
    return BLOCKED_DOMAINS.some(domain => 
      hostname === domain || hostname.endsWith('.' + domain)
    );
  } catch {
    return false;
  }
}

function hasBlockedKeyword(url) {
  try {
    const fullUrl = url.toLowerCase();
    const urlObj = new URL(url);
    const hostname = urlObj.hostname.toLowerCase();
    const path = urlObj.pathname.toLowerCase();
    const search = urlObj.search.toLowerCase();
    
    const textToCheck = hostname + path + search;
    
    return ALL_KEYWORDS.some(keyword => textToCheck.includes(keyword));
  } catch {
    return false;
  }
}

async function shouldBlock(url) {
  const data = await chrome.storage.local.get(['protectionEnabled', 'customBlockedDomains']);
  
  if (!data.protectionEnabled) return false;
  if (isWhitelisted(url)) return false;
  
  // Check custom blocked domains
  if (data.customBlockedDomains && data.customBlockedDomains.length > 0) {
    try {
      const hostname = new URL(url).hostname.toLowerCase();
      if (data.customBlockedDomains.some(d => hostname.includes(d))) return true;
    } catch {}
  }
  
  if (isDomainBlocked(url)) return true;
  if (hasBlockedKeyword(url)) return true;
  
  return false;
}

async function recordBlock(url) {
  const data = await chrome.storage.local.get(['totalBlocked', 'blockedToday', 'lastResetDate']);
  
  const today = new Date().toDateString();
  let blockedToday = data.blockedToday || 0;
  
  if (data.lastResetDate !== today) {
    blockedToday = 0;
  }
  
  await chrome.storage.local.set({
    totalBlocked: (data.totalBlocked || 0) + 1,
    blockedToday: blockedToday + 1,
    lastBlockDate: new Date().toISOString(),
    lastResetDate: today
  });
}

// ═══════════════════════════════════════════════════════════════
// NAVIGATION LISTENER
// ═══════════════════════════════════════════════════════════════

chrome.tabs.onUpdated.addListener(async (tabId, changeInfo, tab) => {
  if (changeInfo.url || (changeInfo.status === 'loading' && tab.url)) {
    const url = changeInfo.url || tab.url;
    
    if (url.startsWith('chrome://') || url.startsWith('chrome-extension://')) return;
    
    const blocked = await shouldBlock(url);
    if (blocked) {
      await recordBlock(url);
      const blockedUrl = chrome.runtime.getURL('blocked.html');
      chrome.tabs.update(tabId, { url: blockedUrl });
    }
  }
});

chrome.webNavigation?.onBeforeNavigate?.addListener(async (details) => {
  if (details.frameId !== 0) return;
  
  const url = details.url;
  if (url.startsWith('chrome://') || url.startsWith('chrome-extension://')) return;
  
  const blocked = await shouldBlock(url);
  if (blocked) {
    await recordBlock(url);
    const blockedUrl = chrome.runtime.getURL('blocked.html');
    chrome.tabs.update(details.tabId, { url: blockedUrl });
  }
});

// ═══════════════════════════════════════════════════════════════
// ALARMS
// ═══════════════════════════════════════════════════════════════

chrome.alarms.onAlarm.addListener(async (alarm) => {
  if (alarm.name === 'dailyReset') {
    const data = await chrome.storage.local.get(['lastResetDate']);
    const today = new Date().toDateString();
    if (data.lastResetDate !== today) {
      await chrome.storage.local.set({ blockedToday: 0, lastResetDate: today });
    }
  }
});

// ═══════════════════════════════════════════════════════════════
// MESSAGE HANDLER
// ═══════════════════════════════════════════════════════════════

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === 'GET_STATS') {
    chrome.storage.local.get(null).then(data => {
      const streakDays = data.streakStart 
        ? Math.floor((Date.now() - new Date(data.streakStart).getTime()) / 86400000)
        : 0;
      sendResponse({
        streakDays,
        totalBlocked: data.totalBlocked || 0,
        blockedToday: data.blockedToday || 0,
        protectionEnabled: data.protectionEnabled !== false
      });
    });
    return true;
  }
  
  if (message.type === 'TOGGLE_PROTECTION') {
    chrome.storage.local.get(['passwordHash']).then(async (data) => {
      if (data.passwordHash && message.password) {
        const hash = await hashPassword(message.password);
        if (hash === data.passwordHash) {
          await chrome.storage.local.set({ protectionEnabled: message.enabled });
          sendResponse({ success: true });
        } else {
          sendResponse({ success: false, error: 'Contraseña incorrecta' });
        }
      } else if (!data.passwordHash) {
        await chrome.storage.local.set({ protectionEnabled: message.enabled });
        sendResponse({ success: true });
      } else {
        sendResponse({ success: false, error: 'Se requiere contraseña' });
      }
    });
    return true;
  }
  
  if (message.type === 'SET_PASSWORD') {
    hashPassword(message.password).then(async (hash) => {
      await chrome.storage.local.set({ passwordHash: hash });
      sendResponse({ success: true });
    });
    return true;
  }
  
  if (message.type === 'RESET_STREAK') {
    chrome.storage.local.set({ streakStart: new Date().toISOString() }).then(() => {
      sendResponse({ success: true });
    });
    return true;
  }
  
  if (message.type === 'SAVE_JOURNAL') {
    chrome.storage.local.get(['journalEntries']).then(async (data) => {
      const entries = data.journalEntries || [];
      entries.unshift({
        id: Date.now(),
        date: new Date().toISOString(),
        text: message.text,
        mood: message.mood
      });
      // Keep last 100 entries
      await chrome.storage.local.set({ journalEntries: entries.slice(0, 100) });
      sendResponse({ success: true });
    });
    return true;
  }
  
  if (message.type === 'GET_JOURNAL') {
    chrome.storage.local.get(['journalEntries']).then(data => {
      sendResponse({ entries: data.journalEntries || [] });
    });
    return true;
  }

  if (message.type === 'VERIFY_PASSWORD') {
    chrome.storage.local.get(['passwordHash']).then(async (data) => {
      if (!data.passwordHash) {
        sendResponse({ valid: true, noPassword: true });
      } else {
        const hash = await hashPassword(message.password);
        sendResponse({ valid: hash === data.passwordHash });
      }
    });
    return true;
  }
});

// ═══════════════════════════════════════════════════════════════
// UTILITY
// ═══════════════════════════════════════════════════════════════

async function hashPassword(password) {
  const encoder = new TextEncoder();
  const data = encoder.encode(password + '_cleanshield_salt_2024');
  const hashBuffer = await crypto.subtle.digest('SHA-256', data);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}
