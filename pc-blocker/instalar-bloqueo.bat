@echo off
setlocal enabledelayedexpansion
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

set "H=%SystemRoot%\System32\drivers\etc\hosts"
set "LOGFILE=%~dp0CleanShield-log.txt"
set "REAPLICAR=%~dp0reaplicar-bloqueo.bat"
echo %date% %time% - [INSTALAR] Instalacion iniciada >> "%LOGFILE%"

echo   [1/9] Creando backup del archivo hosts...
if not exist "%H%.bak" copy "%H%" "%H%.bak" >nul 2>&1

echo   [2/9] Limpiando entradas anteriores...
powershell -Command "$c = Get-Content '%H%' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' }; Set-Content '%H%' $c" >nul 2>&1

echo   [3/9] Anadiendo dominios bloqueados al hosts...
echo. >> "%H%"
echo # ===== CleanShield BLOQUEO TOTAL v2.0 ===== >> "%H%"

echo # --- FORZAR SAFESEARCH --- >> "%H%"
echo 216.239.38.120 www.google.com >> "%H%"
echo 216.239.38.120 google.com >> "%H%"
echo 216.239.38.120 www.google.es >> "%H%"
echo 216.239.38.120 google.es >> "%H%"
echo 216.239.38.120 www.google.com.mx >> "%H%"
echo 216.239.38.120 google.com.mx >> "%H%"
echo 0.0.0.0 www.bing.com >> "%H%"
echo 0.0.0.0 bing.com >> "%H%"
echo 0.0.0.0 duckduckgo.com >> "%H%"
echo 0.0.0.0 www.duckduckgo.com >> "%H%"
echo 0.0.0.0 yandex.com >> "%H%"
echo 0.0.0.0 www.yandex.com >> "%H%"
echo 0.0.0.0 yandex.ru >> "%H%"
echo 0.0.0.0 search.brave.com >> "%H%"

echo # --- PORNOGRAFIA --- >> "%H%"
for /f "tokens=*" %%a in (%~dp0dominios-bloqueados.txt) do echo 0.0.0.0 %%a >> "%H%"

echo # --- PROSTITUCION --- >> "%H%"
for /f "tokens=*" %%a in (%~dp0dominios-prostitucion.txt) do echo 0.0.0.0 %%a >> "%H%"

echo # --- APUESTAS Y PRESTAMOS --- >> "%H%"
for /f "tokens=*" %%a in (%~dp0dominios-apuestas-prestamos.txt) do echo 0.0.0.0 %%a >> "%H%"

for %%C in (drogas alcohol autolesion darkweb pirateria dating violencia) do (
    if exist "%~dp0dominios-%%C.txt" (
        echo # --- %%C --- >> "%H%"
        for /f "tokens=*" %%a in (%~dp0dominios-%%C.txt) do echo 0.0.0.0 %%a >> "%H%"
    )
)

echo # ===== FIN CleanShield ===== >> "%H%"

echo   [4/9] Forzando SafeSearch por politica...
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1

echo   [5/9] Desactivando DNS-over-HTTPS (clave para que el hosts funcione)...
REM Sin esto, los navegadores resuelven dominios por su cuenta y se saltan el hosts.
for %%P in ("Google\Chrome" "Microsoft\Edge" "BraveSoftware\Brave") do (
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v DnsOverHttpsMode /t REG_SZ /d off /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v BuiltInDnsClientEnabled /t REG_DWORD /d 0 /f >nul 2>&1
)
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Enabled /t REG_DWORD /d 0 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Locked /t REG_DWORD /d 1 /f >nul 2>&1
REM DoH a nivel de Windows tambien apagado
netsh dns add global doh=no >nul 2>&1

echo   [6/9] Bloqueando DevTools y extensiones (evita saltarse el bloqueo)...
for %%P in ("Google\Chrome" "Microsoft\Edge" "BraveSoftware\Brave") do (
    reg add "HKLM\SOFTWARE\Policies\%%~P" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\%%~P\ExtensionInstallBlocklist" /v 1 /t REG_SZ /d * /f >nul 2>&1
)
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v DisableDeveloperTools /t REG_DWORD /d 1 /f >nul 2>&1

echo   [7/9] Bloqueo a nivel de navegador (URLBlocklist, inmune a trucos de DNS)...
set "N=0"
call :BUILD_BLOCKLIST dominios-bloqueados.txt
call :BUILD_BLOCKLIST dominios-prostitucion.txt
call :BUILD_BLOCKLIST dominios-apuestas-prestamos.txt
for %%C in (drogas alcohol autolesion darkweb pirateria dating violencia) do (
    if exist "%~dp0dominios-%%C.txt" call :BUILD_BLOCKLIST dominios-%%C.txt
)
echo          %N% reglas de URL aplicadas a Chrome, Edge y Brave.

echo   [8/9] Creando tareas de vigilancia y proteccion anti-manipulacion...
REM Anti-tamper
reg add "HKLM\SOFTWARE\CleanShield" /v AntiTamper /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\CleanShield" /v Installed /t REG_SZ /d "%date% %time%" /f >nul 2>&1
REM Tarea diaria de reaplicacion
schtasks /create /tn "CleanShield_ReaplicarBloqueo" /tr "cmd /c \"%REAPLICAR%\"" /sc daily /st 09:00 /ru SYSTEM /rl HIGHEST /f >nul 2>&1
REM Tarea vigilante: cada 15 min restaura el hosts si alguien (o el antivirus) lo borra
schtasks /create /tn "CleanShield_VigilarHosts" /tr "cmd /c \"%REAPLICAR%\"" /sc minute /mo 15 /ru SYSTEM /rl HIGHEST /f >nul 2>&1
REM Tambien al iniciar sesion
schtasks /create /tn "CleanShield_AlIniciar" /tr "cmd /c \"%REAPLICAR%\"" /sc onlogon /ru SYSTEM /rl HIGHEST /f >nul 2>&1

echo   [9/9] Protegiendo hosts del antivirus y limpiando cache DNS...
REM Excluir el hosts de Defender para que no lo borre como "HostsFileHijack"
powershell -Command "Add-MpPreference -ExclusionPath '%H%' -ErrorAction SilentlyContinue" >nul 2>&1
powershell -Command "Add-MpPreference -ExclusionPath '%~dp0' -ErrorAction SilentlyContinue" >nul 2>&1
ipconfig /flushdns >nul 2>&1

echo %date% %time% - [INSTALAR] Instalacion completada (%N% reglas URL) >> "%LOGFILE%"
echo.
echo   ====================================================
echo   =   BLOQUEO INSTALADO CORRECTAMENTE                =
echo   ====================================================
echo.
echo   - Porno / Prostitucion: BLOQUEADO
echo   - Apuestas / Prestamos: BLOQUEADO
echo   - Drogas / Alcohol / Autolesion: BLOQUEADO
echo   - Dark Web / Pirateria / Dating / Violencia: BLOQUEADO
echo   - SafeSearch + YouTube restringido: FORZADO
echo   - DNS-over-HTTPS: DESACTIVADO (ya no se salta el bloqueo)
echo   - DevTools y extensiones: BLOQUEADAS
echo   - Bloqueo por URL en navegador: %N% reglas
echo   - Vigilancia automatica del hosts: CADA 15 MIN
echo   - Proteccion frente al antivirus: ACTIVA
echo.
echo   IMPORTANTE: Cierra y vuelve a abrir TODOS los navegadores.
echo   Ejecuta "verificar-estado.bat" para comprobar que todo este en verde.
echo.
pause
exit /b 0

:: ============================================================
:: Subrutina: anade los dominios de un archivo a URLBlocklist
:: de Chrome, Edge y Brave usando un contador compartido (%N%)
:: ============================================================
:BUILD_BLOCKLIST
if not exist "%~dp0%1" exit /b 0
for /f "tokens=*" %%d in (%~dp0%1) do (
    set /a N+=1
    reg add "HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist" /v !N! /t REG_SZ /d "%%d" /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist" /v !N! /t REG_SZ /d "%%d" /f >nul 2>&1
    reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist" /v !N! /t REG_SZ /d "%%d" /f >nul 2>&1
)
exit /b 0
