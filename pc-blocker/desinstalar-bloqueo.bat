@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title CleanShield - Desinstalar Proteccion
color 0F

:: ============================================================
:: CleanShield - Desinstalador Seguro
:: Requiere contrasena o frase de confirmacion para desinstalar
:: Opcion de desbloqueo parcial por categorias
:: Registra todos los intentos de desinstalacion
:: ============================================================

:: --- Variables globales ---
set "LOGFILE=%~dp0CleanShield-log.txt"
set "HOSTS=%SystemRoot%\System32\drivers\etc\hosts"
set "SCRIPTDIR=%~dp0"
set "ERRORES=0"
set "CLAVE_REG=HKLM\SOFTWARE\CleanShield"
set "FRASE_SEGURIDAD=DESBLOQUEAR CLEANSHIELD"

:: --- Registrar intento de desinstalacion ---
echo %date% %time% - [DESINSTALAR] Intento de desinstalacion detectado >> "%LOGFILE%"

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    echo %date% %time% - [DESINSTALAR] ERROR: Sin permisos de administrador >> "%LOGFILE%"
    pause
    exit /b 1
)

:: --- Banner ---
cls
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║                                                              ║
echo   ║         CleanShield - Desinstalar Proteccion                 ║
echo   ║                                                              ║
echo   ║   ATENCION: Esta accion eliminara la proteccion del equipo   ║
echo   ║                                                              ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.

:: ============================================================
:: VERIFICACION DE CONTRASENA
:: ============================================================

:: Verificar si hay contrasena establecida en el registro
set "PASS_GUARDADA="
for /f "tokens=2*" %%a in ('reg query "%CLAVE_REG%" /v Password 2^>nul ^| findstr /i "Password"') do (
    set "PASS_GUARDADA=%%b"
)

if defined PASS_GUARDADA (
    :: Contrasena configurada - solicitar contrasena
    echo   Se requiere la contrasena de administrador de CleanShield.
    echo.
    set /p "PASS_INPUT=   Introduce la contrasena: "
    
    :: Comparar contrasena (usando hash simple almacenado)
    if not "!PASS_INPUT!"=="!PASS_GUARDADA!" (
        color 0C
        echo.
        echo   ERROR: Contrasena incorrecta.
        echo.
        echo %date% %time% - [DESINSTALAR] FALLO: Contrasena incorrecta >> "%LOGFILE%"
        pause
        exit /b 1
    )
    echo.
    echo   Contrasena correcta.
    echo %date% %time% - [DESINSTALAR] Contrasena verificada correctamente >> "%LOGFILE%"
) else (
    :: Sin contrasena - requerir frase de seguridad
    echo   No hay contrasena configurada.
    echo   Para continuar, escribe exactamente la siguiente frase:
    echo.
    echo   "%FRASE_SEGURIDAD%"
    echo.
    set /p "FRASE_INPUT=   Escribe aqui: "
    
    if /i not "!FRASE_INPUT!"=="%FRASE_SEGURIDAD%" (
        color 0C
        echo.
        echo   ERROR: La frase no coincide. Desinstalacion cancelada.
        echo.
        echo %date% %time% - [DESINSTALAR] FALLO: Frase de seguridad incorrecta >> "%LOGFILE%"
        pause
        exit /b 1
    )
    echo.
    echo   Frase de seguridad correcta.
    echo %date% %time% - [DESINSTALAR] Frase de seguridad verificada >> "%LOGFILE%"
)

:: ============================================================
:: SELECCIONAR TIPO DE DESINSTALACION
:: ============================================================
echo.
echo   ┌──────────────────────────────────────────────────────────────┐
echo   │                                                              │
echo   │   [1]  Desinstalar TODA la proteccion                        │
echo   │   [2]  Desbloquear categorias especificas                    │
echo   │   [3]  Cancelar                                              │
echo   │                                                              │
echo   └──────────────────────────────────────────────────────────────┘
echo.
set /p "TIPO=   Selecciona una opcion [1-3]: "

if "%TIPO%"=="1" goto DESINSTALAR_TODO
if "%TIPO%"=="2" goto DESINSTALAR_PARCIAL
if "%TIPO%"=="3" goto CANCELAR

echo   Opcion no valida.
pause
exit /b 1

:: ============================================================
:: DESINSTALACION COMPLETA
:: ============================================================
:DESINSTALAR_TODO
echo.
echo   Iniciando desinstalacion completa...
echo %date% %time% - [DESINSTALAR] Desinstalacion completa iniciada >> "%LOGFILE%"

:: Paso 1: Restaurar archivo hosts
echo   [1/4] Restaurando archivo hosts...
if exist "%HOSTS%.bak" (
    copy /y "%HOSTS%.bak" "%HOSTS%" >nul 2>&1
    if %errorlevel% neq 0 (
        echo          ERROR: No se pudo restaurar el backup
        echo %date% %time% - [DESINSTALAR] ERROR: Fallo restaurar backup hosts >> "%LOGFILE%"
        set /a ERRORES+=1
    ) else (
        echo          Archivo hosts restaurado desde backup.
        echo %date% %time% - [DESINSTALAR] Hosts restaurado desde backup >> "%LOGFILE%"
    )
) else (
    :: Si no hay backup, limpiar entradas de CleanShield
    powershell -Command "$c = Get-Content '%HOSTS%' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' }; Set-Content '%HOSTS%' $c" >nul 2>&1
    if %errorlevel% neq 0 (
        echo          ERROR: No se pudo limpiar el archivo hosts
        echo %date% %time% - [DESINSTALAR] ERROR: Fallo limpiar hosts >> "%LOGFILE%"
        set /a ERRORES+=1
    ) else (
        echo          Entradas de CleanShield eliminadas del hosts.
        echo %date% %time% - [DESINSTALAR] Entradas CleanShield eliminadas de hosts >> "%LOGFILE%"
    )
)

:: Paso 2: Eliminar politicas de navegador
echo   [2/4] Eliminando politicas de navegador...
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceGoogleSafeSearch /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceYouTubeRestrict /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome" /v DnsOverHttpsMode /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Google\Chrome" /v DeveloperToolsAvailability /f >nul 2>&1

reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceGoogleSafeSearch /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceYouTubeRestrict /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DnsOverHttpsMode /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DeveloperToolsAvailability /f >nul 2>&1

reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceGoogleSafeSearch /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceYouTubeRestrict /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DnsOverHttpsMode /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DeveloperToolsAvailability /f >nul 2>&1

reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v DisableDeveloperTools /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v BlockAboutAddons /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v BlockAboutConfig /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /f >nul 2>&1

reg delete "HKLM\SOFTWARE\Policies\Opera\URLBlocklist" /f >nul 2>&1
reg delete "HKLM\SOFTWARE\Policies\Opera" /v DeveloperToolsAvailability /f >nul 2>&1

echo          Politicas de navegador eliminadas.
echo %date% %time% - [DESINSTALAR] Politicas de navegador eliminadas >> "%LOGFILE%"

:: Paso 3: Eliminar tarea programada
echo   [3/4] Eliminando tareas programadas...
schtasks /delete /tn "CleanShield_ReaplicarBloqueo" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ADVERTENCIA: No se encontro la tarea programada (puede no existir)
    echo %date% %time% - [DESINSTALAR] ADVERTENCIA: Tarea programada no encontrada >> "%LOGFILE%"
) else (
    echo          Tarea programada eliminada.
    echo %date% %time% - [DESINSTALAR] Tarea programada eliminada >> "%LOGFILE%"
)

:: Eliminar tarea de vigilancia si existe
schtasks /delete /tn "CleanShield_VigilarHosts" /f >nul 2>&1

:: Paso 4: Limpiar DNS
echo   [4/4] Limpiando cache DNS...
ipconfig /flushdns >nul 2>&1
if %errorlevel% neq 0 (
    echo          ADVERTENCIA: No se pudo limpiar el cache DNS
    set /a ERRORES+=1
) else (
    echo          Cache DNS limpiado.
)

:: Restaurar permisos del archivo hosts
echo   Restaurando permisos del archivo hosts...
icacls "%HOSTS%" /grant Everyone:F >nul 2>&1
echo %date% %time% - [DESINSTALAR] Permisos de hosts restaurados >> "%LOGFILE%"

goto RESUMEN

:: ============================================================
:: DESINSTALACION PARCIAL POR CATEGORIAS
:: ============================================================
:DESINSTALAR_PARCIAL
echo.
echo   Desbloqueo parcial por categorias.
echo   Se eliminaran solo las categorias seleccionadas del archivo hosts.
echo.
echo %date% %time% - [DESINSTALAR] Desinstalacion parcial iniciada >> "%LOGFILE%"

:: Listar categorias disponibles
echo   Categorias disponibles:
echo   ──────────────────────────────────────────────────────────────
set "NUM=0"
for %%F in ("%SCRIPTDIR%dominios-*.txt") do (
    set /a NUM+=1
    echo   [!NUM!] %%~nF
)
echo   ──────────────────────────────────────────────────────────────
echo.
set /p "CATS=   Escribe los numeros a desbloquear separados por coma (ej: 1,3,5): "

:: Procesar desbloqueo parcial - limpiar las categorias seleccionadas del hosts
:: Para simplicidad, regeneramos el hosts sin las categorias indicadas
echo.
echo   Procesando desbloqueo parcial...

:: Crear lista de categorias a mantener
set "NUM=0"
for %%F in ("%SCRIPTDIR%dominios-*.txt") do (
    set /a NUM+=1
    set "CAT_!NUM!=%%~nF"
)

:: Limpiar hosts completamente de entradas CleanShield
powershell -Command "$c = Get-Content '%HOSTS%' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' -and $_ -notmatch '^# ---' }; Set-Content '%HOSTS%' $c" >nul 2>&1

:: Reagregar solo las categorias NO seleccionadas
echo. >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo # CleanShield - PROTECCION PARCIAL >> "%HOSTS%"
echo # Modificado: %date% %time% >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"

set "NUM=0"
for %%F in ("%SCRIPTDIR%dominios-*.txt") do (
    set /a NUM+=1
    :: Verificar si este numero esta en la lista de desbloqueo
    echo %CATS% | findstr /c:"!NUM!" >nul 2>&1
    if errorlevel 1 (
        :: No esta en la lista de desbloqueo, mantener esta categoria
        echo # --- %%~nF --- >> "%HOSTS%"
        for /f "usebackq tokens=*" %%D in ("%%F") do (
            echo 0.0.0.0 %%D >> "%HOSTS%"
        )
        echo. >> "%HOSTS%"
    ) else (
        echo   [-] Desbloqueada: %%~nF
        echo %date% %time% - [DESINSTALAR] Categoria desbloqueada: %%~nF >> "%LOGFILE%"
    )
)

echo # ================================================================= >> "%HOSTS%"
echo # FIN CleanShield >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"

ipconfig /flushdns >nul 2>&1
echo.
echo   Desbloqueo parcial completado.
echo %date% %time% - [DESINSTALAR] Desbloqueo parcial completado >> "%LOGFILE%"
goto RESUMEN

:: ============================================================
:: CANCELAR
:: ============================================================
:CANCELAR
echo.
echo   Desinstalacion cancelada.
echo %date% %time% - [DESINSTALAR] Desinstalacion cancelada por el usuario >> "%LOGFILE%"
pause
exit /b 0

:: ============================================================
:: RESUMEN FINAL
:: ============================================================
:RESUMEN
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║                                                              ║
echo   ║           DESINSTALACION COMPLETADA                          ║
echo   ║                                                              ║
echo   ╠══════════════════════════════════════════════════════════════╣
echo   ║                                                              ║
echo   ║   Errores encontrados: %ERRORES%                                    ║
echo   ║                                                              ║
echo   ║   Se recomienda reiniciar el navegador para aplicar          ║
echo   ║   los cambios completamente.                                 ║
echo   ║                                                              ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.

if %ERRORES% gtr 0 (
    color 0E
    echo   ADVERTENCIA: Se encontraron %ERRORES% errores.
    echo   Revisa el log: %LOGFILE%
) else (
    echo   Proteccion eliminada correctamente.
)

echo.
echo %date% %time% - [DESINSTALAR] Desinstalacion finalizada. Errores: %ERRORES% >> "%LOGFILE%"
pause
exit /b 0
