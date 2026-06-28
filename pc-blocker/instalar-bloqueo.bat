@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title CleanShield - Instalador de Proteccion
color 0F

:: ============================================================
:: CleanShield - Instalador Profesional de Proteccion
:: Carga dinamicamente todos los archivos dominios-*.txt
:: Bloquea DNS-over-HTTPS, aplica politicas de navegador
:: Crea tarea programada para reaplicar bloqueos diariamente
:: ============================================================

:: --- Variables globales ---
set "LOGFILE=%~dp0CleanShield-log.txt"
set "HOSTS=%SystemRoot%\System32\drivers\etc\hosts"
set "SCRIPTDIR=%~dp0"
set "TOTAL_DOMINIOS=0"
set "CATEGORIAS_PROCESADAS=0"
set "ERRORES=0"
set "RESPECT_CATEGORIES=0"

:: --- Comprobar si se ejecuta en modo respetar categorias ---
if "%~1"=="--respect-categories" set "RESPECT_CATEGORIES=1"

:: --- Registrar inicio ---
echo %date% %time% - [INSTALAR] Inicio de instalacion de proteccion >> "%LOGFILE%"

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    echo %date% %time% - [INSTALAR] ERROR: Sin permisos de administrador >> "%LOGFILE%"
    pause
    exit /b 1
)

:: --- Banner de inicio ---
cls
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║                                                              ║
echo   ║         CleanShield - Instalador de Proteccion               ║
echo   ║                                                              ║
echo   ║         Proteccion familiar contra contenido danino          ║
echo   ║                                                              ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.
echo   Iniciando instalacion de proteccion completa...
echo.

:: ============================================================
:: PASO 1: Crear backup del archivo hosts
:: ============================================================
echo   [1/7] Creando copia de seguridad del archivo hosts...
copy "%HOSTS%" "%HOSTS%.bak" >nul 2>&1
if %errorlevel% neq 0 (
    echo          ADVERTENCIA: No se pudo crear backup del hosts
    echo %date% %time% - [INSTALAR] ADVERTENCIA: Fallo backup de hosts >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Backup creado correctamente.
    echo %date% %time% - [INSTALAR] Backup de hosts creado >> "%LOGFILE%"
)

:: ============================================================
:: PASO 2: Limpiar entradas anteriores de CleanShield
:: ============================================================
echo   [2/7] Limpiando entradas anteriores de CleanShield...
powershell -Command "$lines = Get-Content '%HOSTS%'; $inBlock = $false; $result = @(); foreach ($line in $lines) { if ($line -match '# =+ CleanShield') { $inBlock = $true; continue } if ($inBlock -and $line -match '# =+ FIN CleanShield') { $inBlock = $false; continue } if (-not $inBlock) { $result += $line } }; Set-Content '%HOSTS%' $result" >nul 2>&1
if %errorlevel% neq 0 (
    echo          ADVERTENCIA: Error al limpiar entradas anteriores
    echo %date% %time% - [INSTALAR] ADVERTENCIA: Error limpiando hosts >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Entradas anteriores eliminadas.
    echo %date% %time% - [INSTALAR] Entradas anteriores limpiadas >> "%LOGFILE%"
)

:: ============================================================
:: PASO 3: Agregar SafeSearch forzado y bloqueo de buscadores
:: ============================================================
echo   [3/7] Forzando SafeSearch en buscadores...

echo. >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo # CleanShield - PROTECCION ACTIVA >> "%HOSTS%"
echo # Instalado: %date% %time% >> "%HOSTS%"
echo # NO MODIFICAR MANUALMENTE - Gestionado por CleanShield >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo. >> "%HOSTS%"
echo # --- FORZAR SAFESEARCH EN BUSCADORES --- >> "%HOSTS%"
echo 216.239.38.120 www.google.com >> "%HOSTS%"
echo 216.239.38.120 google.com >> "%HOSTS%"
echo 216.239.38.120 www.google.es >> "%HOSTS%"
echo 216.239.38.120 google.es >> "%HOSTS%"
echo 216.239.38.120 www.google.com.mx >> "%HOSTS%"
echo 216.239.38.120 google.com.mx >> "%HOSTS%"
echo 216.239.38.120 www.google.com.ar >> "%HOSTS%"
echo 216.239.38.120 google.com.ar >> "%HOSTS%"
echo 216.239.38.120 www.google.co >> "%HOSTS%"
echo 216.239.38.120 google.co >> "%HOSTS%"
echo # Bloquear buscadores sin SafeSearch forzable >> "%HOSTS%"
echo 0.0.0.0 www.bing.com >> "%HOSTS%"
echo 0.0.0.0 bing.com >> "%HOSTS%"
echo 0.0.0.0 duckduckgo.com >> "%HOSTS%"
echo 0.0.0.0 www.duckduckgo.com >> "%HOSTS%"
echo 0.0.0.0 yandex.com >> "%HOSTS%"
echo 0.0.0.0 www.yandex.com >> "%HOSTS%"
echo 0.0.0.0 yandex.ru >> "%HOSTS%"
echo 0.0.0.0 search.yahoo.com >> "%HOSTS%"
echo. >> "%HOSTS%"

echo          SafeSearch forzado correctamente.
echo %date% %time% - [INSTALAR] SafeSearch configurado >> "%LOGFILE%"

:: ============================================================
:: PASO 4: Cargar TODAS las listas de dominios dinamicamente
:: ============================================================
echo   [4/7] Cargando listas de dominios por categoria...
echo.

:: Recorrer todos los archivos dominios-*.txt dinamicamente
for %%F in ("%SCRIPTDIR%dominios-*.txt") do (
    set "ARCHIVO=%%~nxF"
    set "CATEGORIA=%%~nF"
    set /a CATEGORIAS_PROCESADAS+=1
    set "DOMINIOS_CATEGORIA=0"
    
    :: Si estamos en modo respetar categorias, verificar si esta habilitada
    set "SKIP_CAT=0"
    if "!RESPECT_CATEGORIES!"=="1" (
        for /f "tokens=3" %%v in ('reg query "HKLM\SOFTWARE\CleanShield\Categorias" /v "%%~nF" 2^>nul ^| findstr /i "%%~nF"') do (
            if "%%v"=="0x0" set "SKIP_CAT=1"
        )
    )
    
    if "!SKIP_CAT!"=="0" (
        :: Extraer nombre legible de la categoria
        call :PROCESAR_CATEGORIA "%%F" "%%~nF"
    ) else (
        echo          [-] %%~nF (desactivada por configuracion)
    )
)

echo.
echo          Total de categorias procesadas: !CATEGORIAS_PROCESADAS!
echo          Total de dominios bloqueados: !TOTAL_DOMINIOS!
echo %date% %time% - [INSTALAR] !CATEGORIAS_PROCESADAS! categorias, !TOTAL_DOMINIOS! dominios bloqueados >> "%LOGFILE%"

:: Agregar cierre al hosts
echo. >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo # FIN CleanShield - Total dominios: !TOTAL_DOMINIOS! >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"

:: ============================================================
:: PASO 5: Aplicar politicas de navegador y bloquear DoH
:: ============================================================
echo   [5/7] Aplicando politicas de navegador y bloqueando DNS-over-HTTPS...

:: --- SafeSearch por politica de navegador ---
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceGoogleSafeSearch /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v ForceYouTubeRestrict /t REG_DWORD /d 2 /f >nul 2>&1

:: --- Bloquear DNS-over-HTTPS (DoH) en todos los navegadores ---
:: Chrome: Deshabilitar DoH
reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v DnsOverHttpsMode /t REG_SZ /d "off" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo bloquear DoH en Chrome
    echo %date% %time% - [INSTALAR] ERROR: Fallo bloqueo DoH Chrome >> "%LOGFILE%"
    set /a ERRORES+=1
)

:: Edge: Deshabilitar DoH
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DnsOverHttpsMode /t REG_SZ /d "off" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo bloquear DoH en Edge
    echo %date% %time% - [INSTALAR] ERROR: Fallo bloqueo DoH Edge >> "%LOGFILE%"
    set /a ERRORES+=1
)

:: Brave: Deshabilitar DoH
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DnsOverHttpsMode /t REG_SZ /d "off" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo bloquear DoH en Brave
    echo %date% %time% - [INSTALAR] ERROR: Fallo bloqueo DoH Brave >> "%LOGFILE%"
    set /a ERRORES+=1
)

:: Firefox: Deshabilitar TRR (Trusted Recursive Resolver = DoH)
:: network.trr.mode = 5 significa deshabilitado permanentemente
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox" /v DNSOverHTTPS /t REG_DWORD /d 0 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Enabled /t REG_DWORD /d 0 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Mozilla\Firefox\DNSOverHTTPS" /v Locked /t REG_DWORD /d 1 /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo bloquear DoH en Firefox
    echo %date% %time% - [INSTALAR] ERROR: Fallo bloqueo DoH Firefox >> "%LOGFILE%"
    set /a ERRORES+=1
)

echo          Politicas de navegador aplicadas.
echo          DNS-over-HTTPS bloqueado en todos los navegadores.
echo %date% %time% - [INSTALAR] Politicas de navegador y DoH aplicadas >> "%LOGFILE%"

:: ============================================================
:: PASO 6: Crear tarea programada para reaplicar bloqueos diariamente
:: ============================================================
echo   [6/7] Creando tarea programada (reaplicar bloqueos a las 3:00 AM)...

:: Eliminar tarea anterior si existe
schtasks /delete /tn "CleanShield_ReaplicarBloqueo" /f >nul 2>&1

:: Crear nueva tarea programada que ejecuta este script diariamente a las 3AM
schtasks /create /tn "CleanShield_ReaplicarBloqueo" /tr "\"%~f0\"" /sc daily /st 03:00 /ru SYSTEM /rl HIGHEST /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo crear la tarea programada
    echo %date% %time% - [INSTALAR] ERROR: Fallo creacion tarea programada >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Tarea programada creada: Ejecucion diaria a las 3:00 AM
    echo %date% %time% - [INSTALAR] Tarea programada creada correctamente >> "%LOGFILE%"
)

:: ============================================================
:: PASO 7: Limpiar cache DNS
:: ============================================================
echo   [7/7] Limpiando cache DNS del sistema...
ipconfig /flushdns >nul 2>&1
if %errorlevel% neq 0 (
    echo          ADVERTENCIA: No se pudo limpiar el cache DNS
    echo %date% %time% - [INSTALAR] ADVERTENCIA: Fallo flush DNS >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Cache DNS limpiado correctamente.
    echo %date% %time% - [INSTALAR] Cache DNS limpiado >> "%LOGFILE%"
)

:: ============================================================
:: RESUMEN FINAL
:: ============================================================
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║                                                              ║
echo   ║           INSTALACION COMPLETADA CON EXITO                   ║
echo   ║                                                              ║
echo   ╠══════════════════════════════════════════════════════════════╣
echo   ║                                                              ║
echo   ║   Categorias bloqueadas:  !CATEGORIAS_PROCESADAS!                                ║
echo   ║   Dominios bloqueados:    !TOTAL_DOMINIOS!                              ║
echo   ║   Errores encontrados:    !ERRORES!                                ║
echo   ║                                                              ║
echo   ║   SafeSearch:             FORZADO                            ║
echo   ║   DNS-over-HTTPS:         BLOQUEADO                          ║
echo   ║   YouTube Restringido:    ACTIVADO                           ║
echo   ║   Tarea programada:       ACTIVA (3:00 AM diario)            ║
echo   ║                                                              ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.

if !ERRORES! gtr 0 (
    color 0E
    echo   ADVERTENCIA: Se encontraron !ERRORES! errores durante la instalacion.
    echo   Revisa el archivo de registro para mas detalles: %LOGFILE%
) else (
    color 0A
    echo   La proteccion se instalo correctamente sin errores.
)

echo.
echo %date% %time% - [INSTALAR] Instalacion finalizada. Dominios: !TOTAL_DOMINIOS!, Errores: !ERRORES! >> "%LOGFILE%"
goto :EOF

:: ============================================================
:: FUNCION: Procesar una categoria de dominios
:: Parametro 1: Ruta completa del archivo
:: Parametro 2: Nombre del archivo sin extension
:: ============================================================
:PROCESAR_CATEGORIA
set "ARCHIVO_CAT=%~1"
set "NOMBRE_CAT=%~2"

:: Contar dominios en esta categoria
set "COUNT=0"
for /f "usebackq tokens=*" %%L in ("%ARCHIVO_CAT%") do (
    set /a COUNT+=1
)

:: Mostrar progreso
echo          [+] %NOMBRE_CAT% (%COUNT% dominios)

:: Escribir encabezado de categoria en hosts
echo # --- %NOMBRE_CAT% --- >> "%HOSTS%"

:: Escribir cada dominio
for /f "usebackq tokens=*" %%D in ("%ARCHIVO_CAT%") do (
    echo 0.0.0.0 %%D >> "%HOSTS%"
    set /a TOTAL_DOMINIOS+=1
)

echo. >> "%HOSTS%"
echo %date% %time% - [INSTALAR] Categoria %NOMBRE_CAT%: %COUNT% dominios cargados >> "%LOGFILE%"
goto :EOF
