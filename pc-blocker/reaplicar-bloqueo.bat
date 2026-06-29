@echo off
REM ============================================================
REM CleanShield - Reaplicacion silenciosa del bloqueo
REM Lo ejecutan las tareas programadas (vigilancia cada 15 min,
REM diaria y al iniciar sesion). NO muestra ventanas ni pide nada.
REM Reconstruye el bloque del hosts si alguien o el antivirus lo borra
REM y vuelve a asegurar las politicas clave (DoH off, SafeSearch).
REM ============================================================
setlocal
set "H=%SystemRoot%\System32\drivers\etc\hosts"
set "LOGFILE=%~dp0CleanShield-log.txt"

echo %date% %time% - [VIGILANCIA] Reaplicacion ejecutada >> "%LOGFILE%"

REM --- Reconstruir el bloque del hosts ---
powershell -Command "$c = Get-Content '%H%' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' }; Set-Content '%H%' $c" >nul 2>&1

echo. >> "%H%"
echo # ===== CleanShield BLOQUEO TOTAL v2.0 ===== >> "%H%"
echo # --- FORZAR SAFESEARCH --- >> "%H%"
echo 216.239.38.120 www.google.com >> "%H%"
echo 216.239.38.120 google.com >> "%H%"
echo 216.239.38.120 www.google.es >> "%H%"
echo 216.239.38.120 google.es >> "%H%"
echo 0.0.0.0 www.bing.com >> "%H%"
echo 0.0.0.0 bing.com >> "%H%"
echo 0.0.0.0 duckduckgo.com >> "%H%"
echo 0.0.0.0 yandex.com >> "%H%"
echo 0.0.0.0 search.brave.com >> "%H%"

if exist "%~dp0dominios-bloqueados.txt" (
    echo # --- PORNOGRAFIA --- >> "%H%"
    for /f "tokens=*" %%a in (%~dp0dominios-bloqueados.txt) do echo 0.0.0.0 %%a >> "%H%"
)
if exist "%~dp0dominios-prostitucion.txt" (
    echo # --- PROSTITUCION --- >> "%H%"
    for /f "tokens=*" %%a in (%~dp0dominios-prostitucion.txt) do echo 0.0.0.0 %%a >> "%H%"
)
if exist "%~dp0dominios-apuestas-prestamos.txt" (
    echo # --- APUESTAS Y PRESTAMOS --- >> "%H%"
    for /f "tokens=*" %%a in (%~dp0dominios-apuestas-prestamos.txt) do echo 0.0.0.0 %%a >> "%H%"
)
for %%C in (drogas alcohol autolesion darkweb pirateria dating violencia) do (
    if exist "%~dp0dominios-%%C.txt" (
        echo # --- %%C --- >> "%H%"
        for /f "tokens=*" %%a in (%~dp0dominios-%%C.txt) do echo 0.0.0.0 %%a >> "%H%"
    )
)
echo # ===== FIN CleanShield ===== >> "%H%"

REM --- Reasegurar politicas clave (baratas) ---
for %%P in ("Google\Chrome" "Microsoft\Edge" "BraveSoftware\Brave") do (
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v DnsOverHttpsMode /t REG_SZ /d off /f >nul 2>&1
)
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Enabled /t REG_DWORD /d 0 /f >nul 2>&1

REM --- Reasegurar exclusion de Defender por si se reseteo ---
powershell -Command "Add-MpPreference -ExclusionPath '%H%' -ErrorAction SilentlyContinue" >nul 2>&1

ipconfig /flushdns >nul 2>&1
exit /b 0
