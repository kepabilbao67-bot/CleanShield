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

powershell -Command "(Get-Content '%SystemRoot%\System32\drivers\etc\hosts') | Where-Object { $_ -notmatch 'CleanShield' } | Set-Content '%SystemRoot%\System32\drivers\etc\hosts'" >nul 2>&1

set H=%SystemRoot%\System32\drivers\etc\hosts

echo. >> %H%
echo # ===== CleanShield BLOQUEO TOTAL ===== >> %H%
echo # FORZAR SAFESEARCH GOOGLE >> %H%
echo 216.239.38.120 www.google.com >> %H%
echo 216.239.38.120 google.com >> %H%
echo 216.239.38.120 www.google.es >> %H%
echo 216.239.38.120 google.es >> %H%
echo # PORNOGRAFIA >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-bloqueados.txt) do echo 0.0.0.0 %%a >> %H%
echo # PROSTITUCION >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-prostitucion.txt) do echo 0.0.0.0 %%a >> %H%
echo # APUESTAS Y PRESTAMOS >> %H%
for /f "tokens=*" %%a in (%~dp0dominios-apuestas-prestamos.txt) do echo 0.0.0.0 %%a >> %H%
echo # ===== FIN CleanShield ===== >> %H%

ipconfig /flushdns >nul 2>&1

echo.
echo ============================================
echo   BLOQUEO INSTALADO - +250 DOMINIOS
echo ============================================
echo Porno, prostitucion, apuestas, prestamos
echo bloqueados en TODOS los navegadores.
echo SafeSearch forzado en Google.
echo.
pause
