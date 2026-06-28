@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title CleanShield - Configurar Categorias
color 0F

:: ============================================================
:: CleanShield - Configurador de Categorias
:: Permite habilitar/deshabilitar categorias individuales
:: Almacena configuracion en registro de Windows
:: Reaplicar archivo hosts con solo las categorias habilitadas
:: ============================================================

:: --- Variables globales ---
set "LOGFILE=%~dp0CleanShield-log.txt"
set "HOSTS=%SystemRoot%\System32\drivers\etc\hosts"
set "SCRIPTDIR=%~dp0"
set "CLAVE_REG=HKLM\SOFTWARE\CleanShield\Categorias"
set "ERRORES=0"

:: --- Registrar inicio ---
echo %date% %time% - [CATEGORIAS] Configuracion de categorias iniciada >> "%LOGFILE%"

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    echo %date% %time% - [CATEGORIAS] ERROR: Sin permisos de administrador >> "%LOGFILE%"
    pause
    exit /b 1
)

:MENU_CATEGORIAS
cls
echo.
echo   +==============================================================+
echo   !                                                              !
echo   !     CleanShield - Configuracion de Categorias                !
echo   !                                                              !
echo   !     Selecciona las categorias que deseas bloquear.           !
echo   !     Las marcadas con [X] estan activas.                      !
echo   !                                                              !
echo   +==============================================================+
echo.
echo   Estado actual de las categorias:
echo   ──────────────────────────────────────────────────────────────
echo.

:: Listar categorias y su estado actual
set "NUM=0"
for %%F in ("%SCRIPTDIR%dominios-*.txt") do (
    set /a NUM+=1
    set "CAT_!NUM!=%%~nF"
    set "FILE_!NUM!=%%F"
    
    :: Verificar estado en registro (1=habilitado, 0=deshabilitado)
    set "ESTADO_!NUM!=1"
    for /f "tokens=2*" %%a in ('reg query "%CLAVE_REG%" /v "%%~nF" 2^>nul ^| findstr /i "%%~nF"') do (
        if "%%b"=="0" set "ESTADO_!NUM!=0"
    )
    
    :: Mostrar estado
    if "!ESTADO_!NUM!!"=="1" (
        echo   [!NUM!] [X] %%~nF
    ) else (
        echo   [!NUM!] [ ] %%~nF
    )
)

set "TOTAL_CATS=%NUM%"

echo.
echo   ──────────────────────────────────────────────────────────────
echo.
echo   Opciones:
echo   - Escribe el numero de una categoria para activar/desactivar
echo   - Escribe "A" para activar TODAS las categorias
echo   - Escribe "N" para desactivar TODAS las categorias
echo   - Escribe "G" para guardar cambios y aplicar
echo   - Escribe "S" para salir sin cambios
echo.
set /p "OPCION=   Tu eleccion: "

:: Validar opcion
if /i "%OPCION%"=="A" goto ACTIVAR_TODAS
if /i "%OPCION%"=="N" goto DESACTIVAR_TODAS
if /i "%OPCION%"=="G" goto GUARDAR_APLICAR
if /i "%OPCION%"=="S" goto SALIR_SIN_CAMBIOS

:: Verificar si es un numero valido
set /a "NUMCHECK=%OPCION%" 2>nul
if %NUMCHECK% lss 1 (
    echo   Opcion no valida.
    timeout /t 2 >nul
    goto MENU_CATEGORIAS
)
if %NUMCHECK% gtr %TOTAL_CATS% (
    echo   Numero fuera de rango.
    timeout /t 2 >nul
    goto MENU_CATEGORIAS
)

:: Toggle la categoria seleccionada
if "!ESTADO_%OPCION%!"=="1" (
    set "ESTADO_%OPCION%=0"
    echo   Categoria !CAT_%OPCION%! DESACTIVADA
    echo %date% %time% - [CATEGORIAS] Desactivada: !CAT_%OPCION%! >> "%LOGFILE%"
) else (
    set "ESTADO_%OPCION%=1"
    echo   Categoria !CAT_%OPCION%! ACTIVADA
    echo %date% %time% - [CATEGORIAS] Activada: !CAT_%OPCION%! >> "%LOGFILE%"
)
timeout /t 1 >nul
goto MENU_CATEGORIAS

:: ============================================================
:: ACTIVAR TODAS LAS CATEGORIAS
:: ============================================================
:ACTIVAR_TODAS
for /L %%i in (1,1,%TOTAL_CATS%) do (
    set "ESTADO_%%i=1"
)
echo   Todas las categorias ACTIVADAS.
echo %date% %time% - [CATEGORIAS] Todas las categorias activadas >> "%LOGFILE%"
timeout /t 1 >nul
goto MENU_CATEGORIAS

:: ============================================================
:: DESACTIVAR TODAS LAS CATEGORIAS
:: ============================================================
:DESACTIVAR_TODAS
for /L %%i in (1,1,%TOTAL_CATS%) do (
    set "ESTADO_%%i=0"
)
echo   Todas las categorias DESACTIVADAS.
echo %date% %time% - [CATEGORIAS] Todas las categorias desactivadas >> "%LOGFILE%"
timeout /t 1 >nul
goto MENU_CATEGORIAS

:: ============================================================
:: GUARDAR CAMBIOS Y REAPLICAR
:: ============================================================
:GUARDAR_APLICAR
echo.
echo   Guardando configuracion y aplicando cambios...
echo.

:: Guardar estado en registro
for /L %%i in (1,1,%TOTAL_CATS%) do (
    reg add "%CLAVE_REG%" /v "!CAT_%%i!" /t REG_DWORD /d !ESTADO_%%i! /f >nul 2>&1
    if %errorlevel% neq 0 (
        echo   ERROR: No se pudo guardar estado de !CAT_%%i!
        set /a ERRORES+=1
    )
)

echo   Configuracion guardada en registro.
echo %date% %time% - [CATEGORIAS] Configuracion guardada en registro >> "%LOGFILE%"

:: Reaplicar archivo hosts solo con categorias habilitadas
echo   Reaplicando archivo hosts...

:: Limpiar entradas CleanShield del hosts
powershell -Command "$c = Get-Content '%HOSTS%' | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '^0\.0\.0\.0' -and $_ -notmatch '^216\.239\.38\.120' -and $_ -notmatch '^# ---' -and $_ -notmatch '^# ===' }; Set-Content '%HOSTS%' $c" >nul 2>&1
if %errorlevel% neq 0 (
    echo   ERROR: No se pudo limpiar el archivo hosts
    echo %date% %time% - [CATEGORIAS] ERROR: Fallo limpiar hosts >> "%LOGFILE%"
    set /a ERRORES+=1
    goto RESUMEN_CATS
)

:: Escribir encabezado
echo. >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo # CleanShield - PROTECCION POR CATEGORIAS >> "%HOSTS%"
echo # Configurado: %date% %time% >> "%HOSTS%"
echo # NO MODIFICAR MANUALMENTE - Gestionado por CleanShield >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"
echo. >> "%HOSTS%"

:: SafeSearch siempre activo
echo # --- SAFESEARCH FORZADO --- >> "%HOSTS%"
echo 216.239.38.120 www.google.com >> "%HOSTS%"
echo 216.239.38.120 google.com >> "%HOSTS%"
echo 216.239.38.120 www.google.es >> "%HOSTS%"
echo 216.239.38.120 google.es >> "%HOSTS%"
echo 216.239.38.120 www.google.com.mx >> "%HOSTS%"
echo 216.239.38.120 google.com.mx >> "%HOSTS%"
echo 0.0.0.0 www.bing.com >> "%HOSTS%"
echo 0.0.0.0 bing.com >> "%HOSTS%"
echo 0.0.0.0 duckduckgo.com >> "%HOSTS%"
echo 0.0.0.0 www.duckduckgo.com >> "%HOSTS%"
echo 0.0.0.0 yandex.com >> "%HOSTS%"
echo 0.0.0.0 www.yandex.com >> "%HOSTS%"
echo. >> "%HOSTS%"

:: Agregar solo categorias habilitadas
set "CATS_ACTIVAS=0"
set "TOTAL_DOMS=0"
for /L %%i in (1,1,%TOTAL_CATS%) do (
    if "!ESTADO_%%i!"=="1" (
        set /a CATS_ACTIVAS+=1
        echo # --- !CAT_%%i! --- >> "%HOSTS%"
        for /f "usebackq tokens=*" %%D in ("!FILE_%%i!") do (
            echo 0.0.0.0 %%D >> "%HOSTS%"
            set /a TOTAL_DOMS+=1
        )
        echo. >> "%HOSTS%"
        echo   [+] !CAT_%%i! aplicada
    ) else (
        echo   [-] !CAT_%%i! omitida (desactivada)
    )
)

:: Cierre del hosts
echo # ================================================================= >> "%HOSTS%"
echo # FIN CleanShield - Categorias: %CATS_ACTIVAS%, Dominios: %TOTAL_DOMS% >> "%HOSTS%"
echo # ================================================================= >> "%HOSTS%"

:: Limpiar DNS
ipconfig /flushdns >nul 2>&1

echo.
echo   Archivo hosts actualizado.
echo   Categorias activas: %CATS_ACTIVAS%
echo   Dominios bloqueados: %TOTAL_DOMS%
echo %date% %time% - [CATEGORIAS] Hosts actualizado: %CATS_ACTIVAS% categorias, %TOTAL_DOMS% dominios >> "%LOGFILE%"

:RESUMEN_CATS
echo.
if %ERRORES% gtr 0 (
    color 0E
    echo   Se encontraron %ERRORES% errores. Revisa el log para detalles.
) else (
    color 0A
    echo   Configuracion aplicada correctamente.
)
echo.
pause
exit /b 0

:: ============================================================
:: SALIR SIN CAMBIOS
:: ============================================================
:SALIR_SIN_CAMBIOS
echo.
echo   Saliendo sin aplicar cambios.
echo %date% %time% - [CATEGORIAS] Salida sin cambios >> "%LOGFILE%"
pause
exit /b 0
