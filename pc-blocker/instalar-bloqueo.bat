@echo off
echo ============================================
echo   CleanShield - Bloqueo Total del Sistema
echo ============================================
echo.

net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Ejecutar como Administrador.
    pause
    exit /b 1
)

copy "%SystemRoot%\System32\drivers\etc\hosts" "%SystemRoot%\System32\drivers\etc\hosts.bak" >nul 2>&1

echo Limpiando entradas anteriores...
powershell -Command "$c = Get-Content '%SystemRoot%\System32\drivers\etc\hosts' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' }; Set-Content '%SystemRoot%\System32\drivers\etc\hosts' $c" >nul 2>&1

set H=%SystemRoot%\System32\drivers\etc\hosts

echo Instalando bloqueo de +250 dominios...
echo. >> %H%
echo # ===== CleanShield BLOQUEO TOTAL ===== >> %H%

echo # --- FORZAR SAFESEARCH EN TODOS LOS BUSCADORES --- >> %H%
echo 216.239.38.120 www.google.com >> %H%
echo 216.239.38.120 google.com >> %H%
echo 216.239.38.120 www.google.es >> %H%
echo 216.239.38.120 google.es >> %H%
echo 216.239.38.120 www.google.com.mx >> %H%
echo 216.239.38.120 google.com.mx >> %H%
echo # Bloquear Bing sin SafeSearch
echo 0.0.0.0 www.bing.com >> %H%
echo 0.0.0.0 bing.com >> %H%
echo # Bloquear DuckDuckGo (no tiene SafeSearch forzable)
echo 0.0.0.0 duckduckgo.com >> %H%
echo 0.0.0.0 www.duckduckgo.com >> %H%
echo # Bloquear Yandex (muestra porno)
echo 0.0.0.0 yandex.com >> %H%
echo 0.0.0.0 www.yandex.com >> %H%
echo 0.0.0.0 yandex.ru >> %H%

echo # --- PORNOGRAFIA --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-bloqueados.txt) do echo 0.0.0.0 %%a >> %H%

echo # --- PROSTITUCION Y ESCORTS --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-prostitucion.txt) do echo 0.0.0.0 %%a >> %H%

echo # --- APUESTAS Y PRESTAMOS --- >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-apuestas-prestamos.txt) do echo 0.0.0.0 %%a >> %H%

echo # ===== FIN CleanShield ===== >> %H%

:: Limpiar cache DNS
ipconfig /flushdns >nul 2>&1

echo.
echo ============================================
echo   BLOQUEO INSTALADO - +250 DOMINIOS
echo ============================================
echo.
echo - Porno bloqueado
echo - Prostitucion bloqueado
echo - Apuestas bloqueado
echo - Prestamos bloqueado
echo - SafeSearch forzado en Google
echo - Bing, DuckDuckGo y Yandex bloqueados
echo.
pause
