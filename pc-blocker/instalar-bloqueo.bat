@echo off
title CleanShield - Instalando Bloqueo
color 0A
echo.
echo   ====================================================
echo   =   CleanShield - Instalando Bloqueo Total         =
echo   ====================================================
echo.

net session >nul 2>&1
if %errorlevel% neq 0 (
    echo   ERROR: Ejecutar como Administrador.
    pause
    exit /b 1
)

echo   [1/5] Creando backup del archivo hosts...
copy "%SystemRoot%\System32\drivers\etc\hosts" "%SystemRoot%\System32\drivers\etc\hosts.bak" >nul 2>&1

echo   [2/5] Limpiando entradas anteriores...
powershell -Command "$c = Get-Content '%SystemRoot%\System32\drivers\etc\hosts' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' }; Set-Content '%SystemRoot%\System32\drivers\etc\hosts' $c" >nul 2>&1

set H=%SystemRoot%\System32\drivers\etc\hosts

echo   [3/5] Anadiendo dominios bloqueados...
echo. >> %H%
echo # ===== CleanShield BLOQUEO TOTAL v2.0 ===== >> %H%

echo # --- FORZAR SAFESEARCH --- >> %H%
echo 216.239.38.120 www.google.com >> %H%
echo 216.239.38.120 google.com >> %H%
echo 216.239.38.120 www.google.es >> %H%
echo 216.239.38.120 google.es >> %H%
echo 216.239.38.120 www.google.com.mx >> %H%
echo 216.239.38.120 google.com.mx >> %H%
echo 0.0.0.0 www.bing.com >> %H%
echo 0.0.0.0 bing.com >> %H%
echo 0.0.0.0 duckduckgo.com >> %H%
echo 0.0.0.0 www.duckduckgo.com >> %H%
echo 0.0.0.0 yandex.com >> %H%
echo 0.0.0.0 www.yandex.com >> %H%
echo 0.0.0.0 yandex.ru >> %H%
echo 0.0.0.0 search.brave.com >> %H%

echo # --- PORNOGRAFIA --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-bloqueados.txt) do echo 0.0.0.0 %%a >> %H%

echo # --- PROSTITUCION --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-prostitucion.txt) do echo 0.0.0.0 %%a >> %H%

echo # --- APUESTAS Y PRESTAMOS --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-apuestas-prestamos.txt) do echo 0.0.0.0 %%a >> %H%

if exist "%~dp0dominios-drogas.txt" (
    echo # --- DROGAS --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-drogas.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-alcohol.txt" (
    echo # --- ALCOHOL --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-alcohol.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-autolesion.txt" (
    echo # --- AUTOLESION --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-autolesion.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-darkweb.txt" (
    echo # --- DARK WEB --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-darkweb.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-pirateria.txt" (
    echo # --- PIRATERIA --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-pirateria.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-dating.txt" (
    echo # --- DATING --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-dating.txt) do echo 0.0.0.0 %%a >> %H%
)

if exist "%~dp0dominios-violencia.txt" (
    echo # --- VIOLENCIA --- >> %H%
    for /f "tokens=*" %%a in (%~dp0dominios-violencia.txt) do echo 0.0.0.0 %%a >> %H%
)

echo # ===== FIN CleanShield ===== >> %H%

echo   [4/5] Forzando SafeSearch por politica...
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1

echo   [5/5] Limpiando cache DNS...
ipconfig /flushdns >nul 2>&1

echo.
echo   ====================================================
echo   =   BLOQUEO INSTALADO CORRECTAMENTE                =
echo   ====================================================
echo.
echo   - Porno: BLOQUEADO
echo   - Prostitucion: BLOQUEADO
echo   - Apuestas/Prestamos: BLOQUEADO
echo   - Drogas: BLOQUEADO
echo   - Alcohol: BLOQUEADO
echo   - Autolesion: BLOQUEADO
echo   - Dark Web: BLOQUEADO
echo   - Pirateria: BLOQUEADO
echo   - Dating: BLOQUEADO
echo   - Violencia: BLOQUEADO
echo   - SafeSearch: FORZADO
echo   - YouTube: MODO RESTRINGIDO
echo   - Brave Search: BLOQUEADO
echo.
echo   Cierra y abre los navegadores para aplicar.
echo.
pause
