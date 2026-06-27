// ═══════════════════════════════════════════════════════════════
// CleanShield Dashboard - Recovery Tools
// ═══════════════════════════════════════════════════════════════

const QUOTES = [
  "Cada dia que resistes te hace mas fuerte. Sigue adelante.",
  "No mires atras, tu ya no vas en esa direccion.",
  "La libertad empieza donde termina la adiccion.",
  "Eres mas fuerte de lo que crees. Tu racha lo demuestra.",
  "El progreso no es lineal, pero cada intento cuenta.",
  "Tu futuro yo te agradecera esta decision.",
  "La disciplina es el puente entre metas y logros.",
  "Un paso a la vez. Un dia a la vez. Tu puedes.",
  "No necesitas ser perfecto, solo necesitas no rendirte.",
  "Tu valor como persona no depende de tus errores pasados.",
  "Cada momento de tentacion superado es una victoria.",
  "Recuerda: buscas progreso, no perfeccion.",
  "Las cadenas del habito son demasiado ligeras para sentirse hasta que son demasiado pesadas para romperse.",
  "Hoy es un buen dia para ser libre.",
  "Tu cerebro se esta reconectando. Dale tiempo y paciencia."
];

// ═══════════════════════════════════════════════════════════════
// TAB NAVIGATION
// ═══════════════════════════════════════════════════════════════

document.querySelectorAll('.nav-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById(`tab-${btn.dataset.tab}`).classList.add('active');
  });
});

// ═══════════════════════════════════════════════════════════════
// LOAD STATS
// ═══════════════════════════════════════════════════════════════

function loadStats() {
  chrome.runtime.sendMessage({ type: 'GET_STATS' }, (response) => {
    if (response) {
      document.getElementById('streakDays').textContent = response.streakDays;
      document.getElementById('totalBlocked').textContent = response.totalBlocked;
      document.getElementById('blockedToday').textContent = response.blockedToday;
      document.getElementById('protectionStatus').textContent = 
        response.protectionEnabled ? 'Activa' : 'Inactiva';
      document.getElementById('protectionToggle').checked = response.protectionEnabled;
    }
  });
}

function loadDailyQuote() {
  const today = new Date().toDateString();
  const seed = today.split('').reduce((a, c) => a + c.charCodeAt(0), 0);
  const index = seed % QUOTES.length;
  document.getElementById('dailyQuote').textContent = `"${QUOTES[index]}"`;
}

loadStats();
loadDailyQuote();


// ═══════════════════════════════════════════════════════════════
// PANIC BUTTON
// ═══════════════════════════════════════════════════════════════

document.getElementById('savePanicBtn').addEventListener('click', () => {
  const contact = document.getElementById('panicContact').value.trim();
  if (contact) {
    chrome.storage.local.set({ panicContact: contact });
    alert('Contacto de emergencia guardado.');
  }
});

chrome.storage.local.get(['panicContact'], (data) => {
  if (data.panicContact) {
    document.getElementById('panicContact').value = data.panicContact;
  }
});

document.getElementById('panicBtn').addEventListener('click', () => {
  chrome.storage.local.get(['panicContact'], (data) => {
    if (data.panicContact) {
      const contact = data.panicContact;
      if (contact.match(/^\+?\d+$/)) {
        window.open(`tel:${contact}`);
      } else {
        alert(`Contacta a: ${contact}\n\nRespira profundo. Este momento pasara.`);
      }
    } else {
      alert('No has configurado un contacto de emergencia.\nVe a la seccion de arriba para anadirlo.');
    }
  });
});

// ═══════════════════════════════════════════════════════════════
// JOURNAL
// ═══════════════════════════════════════════════════════════════

let selectedMood = 'neutral';

document.querySelectorAll('.mood-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.mood-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    selectedMood = btn.dataset.mood;
  });
});

document.getElementById('saveJournalBtn').addEventListener('click', () => {
  const text = document.getElementById('journalText').value.trim();
  if (!text) {
    alert('Escribe algo en tu diario antes de guardar.');
    return;
  }
  
  chrome.runtime.sendMessage({
    type: 'SAVE_JOURNAL',
    text: text,
    mood: selectedMood
  }, (response) => {
    if (response && response.success) {
      document.getElementById('journalText').value = '';
      loadJournalEntries();
      alert('Entrada guardada. Bien hecho por expresarte.');
    }
  });
});

function loadJournalEntries() {
  chrome.runtime.sendMessage({ type: 'GET_JOURNAL' }, (response) => {
    const container = document.getElementById('entriesList');
    if (!response || !response.entries || response.entries.length === 0) {
      container.innerHTML = '<p style="color:#718096;font-size:0.9rem;">Aun no hay entradas. Empieza a escribir tu primera.</p>';
      return;
    }
    
    const moodEmojis = { great: '😊', good: '🙂', neutral: '😐', bad: '😟', terrible: '😢' };
    
    container.innerHTML = response.entries.slice(0, 20).map(entry => `
      <div class="journal-entry">
        <div class="journal-entry-header">
          <span>${moodEmojis[entry.mood] || '😐'} ${entry.mood || 'neutral'}</span>
          <span>${new Date(entry.date).toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })}</span>
        </div>
        <div class="journal-entry-text">${escapeHtml(entry.text)}</div>
      </div>
    `).join('');
  });
}

function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

loadJournalEntries();


// ═══════════════════════════════════════════════════════════════
// MEDITATION
// ═══════════════════════════════════════════════════════════════

let meditationInterval = null;
let meditationTimeout = null;

document.querySelectorAll('.meditation-card').forEach(card => {
  card.addEventListener('click', () => {
    const duration = parseInt(card.dataset.duration);
    startMeditation(duration);
  });
});

function startMeditation(durationSecs) {
  const player = document.getElementById('meditationPlayer');
  const options = document.querySelector('.meditation-options');
  options.style.display = 'none';
  player.style.display = 'block';
  
  const circle = document.getElementById('breathCircle');
  const instruction = document.getElementById('breathInstruction');
  const timer = document.getElementById('breathTimer');
  
  let elapsed = 0;
  let phase = 'inhale'; // inhale, hold, exhale
  let phaseTime = 0;
  
  meditationInterval = setInterval(() => {
    elapsed++;
    const mins = Math.floor(elapsed / 60);
    const secs = elapsed % 60;
    timer.textContent = `${mins}:${secs.toString().padStart(2, '0')}`;
    
    phaseTime++;
    
    if (phase === 'inhale') {
      instruction.textContent = '🌬️ Inspira...';
      circle.className = 'breath-circle inhale';
      if (phaseTime >= 4) { phase = 'hold'; phaseTime = 0; }
    } else if (phase === 'hold') {
      instruction.textContent = '⏸️ Manten...';
      if (phaseTime >= 4) { phase = 'exhale'; phaseTime = 0; }
    } else {
      instruction.textContent = '💨 Exhala...';
      circle.className = 'breath-circle exhale';
      if (phaseTime >= 4) { phase = 'inhale'; phaseTime = 0; }
    }
    
    if (elapsed >= durationSecs) {
      stopMeditation();
      instruction.textContent = '✅ Sesion completada. Bien hecho.';
    }
  }, 1000);
}

function stopMeditation() {
  if (meditationInterval) clearInterval(meditationInterval);
  meditationInterval = null;
  
  setTimeout(() => {
    document.getElementById('meditationPlayer').style.display = 'none';
    document.querySelector('.meditation-options').style.display = 'grid';
    document.getElementById('breathTimer').textContent = '0:00';
    document.getElementById('breathCircle').className = 'breath-circle';
  }, 2000);
}

document.getElementById('stopMeditation').addEventListener('click', () => {
  if (meditationInterval) clearInterval(meditationInterval);
  meditationInterval = null;
  document.getElementById('meditationPlayer').style.display = 'none';
  document.querySelector('.meditation-options').style.display = 'grid';
});

// ═══════════════════════════════════════════════════════════════
// SETTINGS
// ═══════════════════════════════════════════════════════════════

// Set Password
document.getElementById('setPasswordBtn').addEventListener('click', () => {
  const pass = document.getElementById('newPassword').value;
  const confirm = document.getElementById('confirmPassword').value;
  const status = document.getElementById('passwordStatus');
  
  if (!pass || pass.length < 4) {
    status.textContent = 'La contrasena debe tener al menos 4 caracteres.';
    status.className = 'status-text error';
    return;
  }
  if (pass !== confirm) {
    status.textContent = 'Las contrasenas no coinciden.';
    status.className = 'status-text error';
    return;
  }
  
  chrome.runtime.sendMessage({ type: 'SET_PASSWORD', password: pass }, (response) => {
    if (response && response.success) {
      status.textContent = '✅ Contrasena establecida correctamente.';
      status.className = 'status-text';
      document.getElementById('newPassword').value = '';
      document.getElementById('confirmPassword').value = '';
    }
  });
});

// Protection Toggle
document.getElementById('protectionToggle').addEventListener('change', (e) => {
  if (!e.target.checked) {
    // Want to disable - check if password exists
    chrome.runtime.sendMessage({ type: 'VERIFY_PASSWORD', password: '' }, (res) => {
      if (res && res.noPassword) {
        chrome.runtime.sendMessage({ type: 'TOGGLE_PROTECTION', enabled: false });
      } else {
        e.target.checked = true; // Revert toggle
        document.getElementById('disablePasswordPrompt').style.display = 'flex';
        document.getElementById('disablePasswordPrompt').style.gap = '0.5rem';
      }
    });
  } else {
    chrome.runtime.sendMessage({ type: 'TOGGLE_PROTECTION', enabled: true });
    document.getElementById('disablePasswordPrompt').style.display = 'none';
  }
});

document.getElementById('confirmDisableBtn').addEventListener('click', () => {
  const pass = document.getElementById('disablePassword').value;
  chrome.runtime.sendMessage({ type: 'TOGGLE_PROTECTION', enabled: false, password: pass }, (res) => {
    if (res && res.success) {
      document.getElementById('protectionToggle').checked = false;
      document.getElementById('disablePasswordPrompt').style.display = 'none';
      document.getElementById('disablePassword').value = '';
    } else {
      alert('Contrasena incorrecta.');
    }
  });
});

// Reset Streak
document.getElementById('resetStreakBtn').addEventListener('click', () => {
  if (confirm('¿Seguro que quieres reiniciar tu racha? Esto no se puede deshacer.')) {
    chrome.runtime.sendMessage({ type: 'RESET_STREAK' }, () => {
      loadStats();
      alert('Racha reiniciada. Hoy es un nuevo comienzo.');
    });
  }
});

// Custom Domains
document.getElementById('addDomainBtn').addEventListener('click', () => {
  const domain = document.getElementById('customDomain').value.trim().toLowerCase();
  if (!domain) return;
  
  chrome.storage.local.get(['customBlockedDomains'], (data) => {
    const domains = data.customBlockedDomains || [];
    if (!domains.includes(domain)) {
      domains.push(domain);
      chrome.storage.local.set({ customBlockedDomains: domains }, () => {
        document.getElementById('customDomain').value = '';
        loadCustomDomains();
      });
    }
  });
});

function loadCustomDomains() {
  chrome.storage.local.get(['customBlockedDomains'], (data) => {
    const list = document.getElementById('customDomainList');
    const domains = data.customBlockedDomains || [];
    
    if (domains.length === 0) {
      list.innerHTML = '';
      return;
    }
    
    list.innerHTML = domains.map(d => `
      <li>
        <span>${d}</span>
        <button onclick="removeDomain('${d}')">✕</button>
      </li>
    `).join('');
  });
}

window.removeDomain = function(domain) {
  chrome.storage.local.get(['customBlockedDomains'], (data) => {
    const domains = (data.customBlockedDomains || []).filter(d => d !== domain);
    chrome.storage.local.set({ customBlockedDomains: domains }, loadCustomDomains);
  });
};

loadCustomDomains();
