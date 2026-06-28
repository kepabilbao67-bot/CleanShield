@echo off
echo ============================================
echo   CleanShield - Quitar Bloqueo
echo ============================================
echo.

net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Ejecutar como Administrador.
    pause
    exit /b 1
)

echo ATENCION: Vas a quitar toda la proteccion.
echo.
set /p confirm="Escribe DESBLOQUEAR para confirmar: "
if /i not "%confirm%"=="DESBLOQUEAR" (
    echo Cancelado.
    pause
    exit /b 0
)

if exist "%SystemRoot%\System32\drivers\etc\hosts.bak" (
    copy /y "%SystemRoot%\System32\drivers\etc\hosts.bak" "%SystemRoot%\System32\drivers\etc\hosts" >nul
) else (
    powershell -Command "(Get-Content '%SystemRoot%\System32\drivers\etc\hosts') | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '0.0.0.0' } | Set-Content '%SystemRoot%\System32\drivers\etc\hosts'"
)

:: Remove browser policies
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Opera\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome" /v DeveloperToolsAvailability /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DeveloperToolsAvailability /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DeveloperToolsAvailability /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v DisableDeveloperTools /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v BlockAboutAddons /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v BlockAboutConfig /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Opera" /v DeveloperToolsAvailability /f >nul 2>&1

ipconfig /flushdns >nul 2>&1

echo.
echo Bloqueo eliminado.
pause
