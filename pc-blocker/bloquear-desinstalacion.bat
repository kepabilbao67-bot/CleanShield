@echo off
echo ============================================
echo   CleanShield - Bloquear desinstalacion
echo ============================================
echo.
echo Esto hara que NO puedas quitar la extension
echo CleanShield de Chrome ni Brave.
echo.

net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Ejecutar como Administrador.
    pause
    exit /b 1
)

echo Obteniendo ID de la extension...
echo.

:: Get extension ID from Chrome
for /f "tokens=*" %%i in ('dir /b /ad "%LOCALAPPDATA%\Google\Chrome\User Data\Default\Extensions\" 2^>nul ^| findstr /v "Temp"') do (
    if exist "%LOCALAPPDATA%\Google\Chrome\User Data\Default\Extensions\%%i\1.0.0_0\manifest.json" (
        findstr /c:"CleanShield" "%LOCALAPPDATA%\Google\Chrome\User Data\Default\Extensions\%%i\1.0.0_0\manifest.json" >nul 2>&1
        if not errorlevel 1 set EXTID=%%i
    )
)

:: Force install extension policy for Chrome
echo Aplicando politica en Chrome...
reg add "HKLM\SOFTWARE\Policies\Google\Chrome\ExtensionInstallForcelist" /v 1 /t REG_SZ /d "%EXTID%;https://clients2.google.com/service/update2/crx" /f >nul 2>&1

:: Block chrome://extensions page
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v URLBlocklist /t REG_SZ /d "[\"chrome://extensions\"]" /f >nul 2>&1

:: Block access to extensions page in Chrome
reg add "HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist" /v 1 /t REG_SZ /d "chrome://extensions" /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist" /v 2 /t REG_SZ /d "chrome://extensions/*" /f >nul 2>&1

:: Same for Brave
echo Aplicando politica en Brave...
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist" /v 1 /t REG_SZ /d "brave://extensions" /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist" /v 2 /t REG_SZ /d "brave://extensions/*" /f >nul 2>&1

:: Same for Edge
echo Aplicando politica en Edge...
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist" /v 1 /t REG_SZ /d "edge://extensions" /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist" /v 2 /t REG_SZ /d "edge://extensions/*" /f >nul 2>&1

:: Block developer mode
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1

echo.
echo ============================================
echo   PROTECCION APLICADA
echo ============================================
echo.
echo - No podras acceder a chrome://extensions
echo - No podras acceder a brave://extensions
echo - Las herramientas de desarrollador estan
echo   desactivadas (no se puede quitar la extension)
echo.
echo Para revertir, ejecuta: desbloquear-desinstalacion.bat
echo.
pause
