@echo off
chcp 65001 >nul
title CleanShield - Verificar Estado
color 0F

:: ============================================================
:: CleanShield - Verificador de Estado de Proteccion
:: Comprueba si los bloqueos estan activos y muestra estado
:: con colores: ACTIVO (verde) o INACTIVO (rojo)
:: ============================================================

:: --- Variables globales ---
set "LOGFILE=%~dp0CleanShield-log.txt"
set "HOSTS=%SystemRoot%\System32\drivers\etc\hosts"
set "ERRORES=0"
set "ACTIVOS=0"
set "INACTIVOS=0"

:: --- Registrar verificacion ---
echo %date% %time% - [ESTADO] Verificacion de estado iniciada >> "%LOGFILE%"

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    echo %date% %time% - [ESTADO] ERROR: Sin permisos de administrador >> "%LOGFILE%"
    pause
    exit /b 1
)

:: --- Banner ---
cls
echo.
echo   +==============================================================+
echo   !                                                              !
echo   !         CleanShield - Estado de la Proteccion                !
echo   !                                                              !
echo   +==============================================================+
echo.
echo   Verificando componentes de proteccion...
echo.
echo   ==============================================================
echo.

:: ============================================================
:: VERIFICACION 1: Archivo hosts con entradas CleanShield
:: ============================================================
set "HOSTS_COUNT=0"
for /f %%A in ('findstr /c:"CleanShield" "%HOSTS%" 2^>nul ^| find /c /v ""') do set "HOSTS_MARKER=%%A"
for /f %%A in ('findstr /c:"0.0.0.0" "%HOSTS%" 2^>nul ^| find /c /v ""') do set "HOSTS_COUNT=%%A"

if %HOSTS_COUNT% gtr 0 (
    echo   [OK] Archivo hosts: ACTIVO (%HOSTS_COUNT% dominios bloqueados)
    set /a ACTIVOS+=1
) else (
    echo   [!!] Archivo hosts: INACTIVO (sin dominios bloqueados)
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 2: Politicas de SafeSearch en Chrome
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceGoogleSafeSearch >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] SafeSearch Chrome: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] SafeSearch Chrome: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 3: Politicas de SafeSearch en Edge
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceGoogleSafeSearch >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] SafeSearch Edge: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] SafeSearch Edge: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 4: Politicas de SafeSearch en Brave
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceGoogleSafeSearch >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] SafeSearch Brave: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] SafeSearch Brave: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 5: DNS-over-HTTPS bloqueado en Chrome
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Google\Chrome" /v DnsOverHttpsMode >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Bloqueo DNS-over-HTTPS Chrome: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Bloqueo DNS-over-HTTPS Chrome: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 6: DNS-over-HTTPS bloqueado en Edge
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DnsOverHttpsMode >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Bloqueo DNS-over-HTTPS Edge: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Bloqueo DNS-over-HTTPS Edge: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 7: DNS-over-HTTPS bloqueado en Firefox
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Enabled >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Bloqueo DNS-over-HTTPS Firefox: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Bloqueo DNS-over-HTTPS Firefox: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 8: Herramientas de desarrollador bloqueadas
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Google\Chrome" /v DeveloperToolsAvailability >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] DevTools bloqueadas: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] DevTools bloqueadas: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 9: Tarea programada de reaplicacion
:: ============================================================
schtasks /query /tn "CleanShield_ReaplicarBloqueo" >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Tarea programada (reaplicar diario): ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Tarea programada (reaplicar diario): INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 10: Tarea de vigilancia del hosts
:: ============================================================
schtasks /query /tn "CleanShield_VigilarHosts" >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Tarea vigilancia hosts: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Tarea vigilancia hosts: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 11: Extensiones VPN bloqueadas
:: ============================================================
reg query "HKLM\SOFTWARE\Policies\Google\Chrome\ExtensionInstallBlocklist" >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Bloqueo extensiones VPN: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Bloqueo extensiones VPN: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: VERIFICACION 12: Proteccion anti-tamper
:: ============================================================
reg query "HKLM\SOFTWARE\CleanShield" /v AntiTamper >nul 2>&1
if %errorlevel% equ 0 (
    echo   [OK] Proteccion anti-manipulacion: ACTIVO
    set /a ACTIVOS+=1
) else (
    echo   [!!] Proteccion anti-manipulacion: INACTIVO
    set /a INACTIVOS+=1
)

:: ============================================================
:: RESUMEN
:: ============================================================
echo.
echo   ==============================================================
echo.
set /a TOTAL=%ACTIVOS%+%INACTIVOS%
echo   RESUMEN: %ACTIVOS%/%TOTAL% componentes activos
echo.

if %INACTIVOS% equ 0 (
    color 0A
    echo   ESTADO GENERAL: PROTECCION COMPLETA
    echo.
    echo   Todos los componentes de proteccion estan funcionando
    echo   correctamente. Tu equipo esta completamente protegido.
) else if %ACTIVOS% gtr %INACTIVOS% (
    color 0E
    echo   ESTADO GENERAL: PROTECCION PARCIAL
    echo.
    echo   Algunos componentes no estan activos. Se recomienda
    echo   ejecutar la instalacion completa desde el menu principal.
) else (
    color 0C
    echo   ESTADO GENERAL: PROTECCION INSUFICIENTE
    echo.
    echo   La mayoria de componentes estan inactivos. Es urgente
    echo   ejecutar la instalacion completa desde el menu principal.
)

echo.
echo %date% %time% - [ESTADO] Verificacion completada: %ACTIVOS%/%TOTAL% activos >> "%LOGFILE%"
pause
exit /b 0
