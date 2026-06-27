const QUOTES = [
  "Cada dia que resistes te hace mas fuerte.",
  "No mires atras, tu ya no vas en esa direccion.",
  "La libertad empieza donde termina la adiccion.",
  "Eres mas fuerte de lo que crees.",
  "Tu futuro yo te agradecera esta decision.",
  "Un paso a la vez. Un dia a la vez. Tu puedes.",
  "No necesitas ser perfecto, solo no rendirte.",
  "Hoy es un buen dia para ser libre.",
  "Tu cerebro se esta reconectando. Dale tiempo."
];

// Load stats
chrome.runtime.sendMessage({ type: 'GET_STATS' }, (response) => {
  if (response) {
    document.getElementById('streak').textContent = response.streakDays;
    document.getElementById('blocked').textContent = response.blockedToday;
    
    const statusText = document.getElementById('statusText');
    const statusDot = document.getElementById('statusDot');
    
    if (response.protectionEnabled) {
      statusText.textContent = 'Proteccion Activa';
      statusDot.className = 'status-dot';
    } else {
      statusText.textContent = 'Proteccion Inactiva';
      statusDot.className = 'status-dot off';
    }
  }
});

// Random quote
document.getElementById('quote').textContent = 
  `"${QUOTES[Math.floor(Math.random() * QUOTES.length)]}"`;

// Dashboard button
document.getElementById('dashboardBtn').addEventListener('click', () => {
  chrome.tabs.create({ url: chrome.runtime.getURL('dashboard.html') });
});

// Breathe button
document.getElementById('breatheBtn').addEventListener('click', () => {
  chrome.tabs.create({ url: chrome.runtime.getURL('dashboard.html#meditation') });
});
