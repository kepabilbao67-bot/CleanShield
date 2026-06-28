@echo off
chcp 65001 >nul
title CleanShield - Panel de Control
color 0F

:: ============================================================
:: CleanShield - Menu Principal
:: Script de entrada principal con interfaz de menu numerado
:: Todas las operaciones requieren permisos de administrador
:: ============================================================

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    pause
    exit /b 1
)

:: --- Registrar inicio en log ---
set "LOGFILE=%~dp0CleanShield-log.txt"
echo %date% %time% - [MENU] Menu principal iniciado >> "%LOGFILE%"

:MENU_PRINCIPAL
cls
color 0F
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║                                                              ║
echo   ║     ██████╗██╗     ███████╗ █████╗ ███╗   ██╗               ║
echo   ║    ██╔════╝██║     ██╔════╝██╔══██╗████╗  ██║               ║
echo   ║    ██║     ██║     █████╗  ███████║██╔██╗ ██║               ║
echo   ║    ██║     ██║     ██╔══╝  ██╔══██║██║╚██╗██║               ║
echo   ║    ╚██████╗███████╗███████╗██║  ██║██║ ╚████║               ║
echo   ║     ╚═════╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝               ║
echo   ║                                                              ║
echo   ║    ███████╗██╗  ██╗██╗███████╗██╗     ██████╗               ║
echo   ║    ██╔════╝██║  ██║██║██╔════╝██║     ██╔══██╗              ║
echo   ║    ███████╗███████║██║█████╗  ██║     ██║  ██║              ║
echo   ║    ╚════██║██╔══██║██║██╔══╝  ██║     ██║  ██║              ║
echo   ║    ███████║██║  ██║██║███████╗███████╗██████╔╝              ║
echo   ║    ╚══════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═════╝               ║
echo   ║                                                              ║
echo   ║           Proteccion Familiar Inteligente v2.0               ║
echo   ║                                                              ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.
echo   ┌──────────────────────────────────────────────────────────────┐
echo   │                                                              │
echo   │   [1]  Instalar proteccion completa                          │
echo   │   [2]  Configurar categorias                                 │
echo   │   [3]  Ver estado del bloqueo                                │
echo   │   [4]  Abrir Asistente IA                                    │
echo   │   [5]  Desinstalar proteccion (requiere contrasena)          │
echo   │   [6]  Ver registro de actividad                             │
echo   │   [7]  Salir                                                 │
echo   │                                                              │
echo   └──────────────────────────────────────────────────────────────┘
echo.
set /p "opcion=   Selecciona una opcion [1-7]: "

:: --- Validar y ejecutar la opcion seleccionada ---
if "%opcion%"=="1" goto OPCION_INSTALAR
if "%opcion%"=="2" goto OPCION_CATEGORIAS
if "%opcion%"=="3" goto OPCION_ESTADO
if "%opcion%"=="4" goto OPCION_IA
if "%opcion%"=="5" goto OPCION_DESINSTALAR
if "%opcion%"=="6" goto OPCION_LOG
if "%opcion%"=="7" goto SALIR

:: Opcion no valida
color 0E
echo.
echo   Opcion no valida. Por favor selecciona un numero del 1 al 7.
timeout /t 2 >nul
goto MENU_PRINCIPAL

:OPCION_INSTALAR
echo %date% %time% - [MENU] Usuario selecciono: Instalar proteccion >> "%LOGFILE%"
call "%~dp0instalar-bloqueo.bat"
if %errorlevel% neq 0 (
    echo %date% %time% - [MENU] ERROR: La instalacion fallo con codigo %errorlevel% >> "%LOGFILE%"
)
pause
goto MENU_PRINCIPAL

:OPCION_CATEGORIAS
echo %date% %time% - [MENU] Usuario selecciono: Configurar categorias >> "%LOGFILE%"
call "%~dp0configurar-categorias.bat"
if %errorlevel% neq 0 (
    echo %date% %time% - [MENU] ERROR: Configuracion de categorias fallo con codigo %errorlevel% >> "%LOGFILE%"
)
pause
goto MENU_PRINCIPAL

:OPCION_ESTADO
echo %date% %time% - [MENU] Usuario selecciono: Ver estado >> "%LOGFILE%"
call "%~dp0verificar-estado.bat"
if %errorlevel% neq 0 (
    echo %date% %time% - [MENU] ERROR: Verificacion de estado fallo con codigo %errorlevel% >> "%LOGFILE%"
)
pause
goto MENU_PRINCIPAL

:OPCION_IA
echo %date% %time% - [MENU] Usuario selecciono: Asistente IA >> "%LOGFILE%"
cls
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║              ASISTENTE IA - CleanShield                      ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.
echo   Abriendo el Asistente IA en tu navegador...
echo.
:: Intentar abrir el asistente IA (HTML local)
if exist "%~dp0asistente-ia\index.html" (
    start "" "%~dp0asistente-ia\index.html"
    echo   El asistente se ha abierto en tu navegador.
) else (
    echo   El archivo del asistente IA no se encontro.
    echo   Busca la carpeta "asistente-ia" en el directorio de CleanShield.
)
echo.
pause
goto MENU_PRINCIPAL

:OPCION_DESINSTALAR
echo %date% %time% - [MENU] Usuario selecciono: Desinstalar >> "%LOGFILE%"
call "%~dp0desinstalar-bloqueo.bat"
if %errorlevel% neq 0 (
    echo %date% %time% - [MENU] ERROR: Desinstalacion fallo con codigo %errorlevel% >> "%LOGFILE%"
)
pause
goto MENU_PRINCIPAL

:OPCION_LOG
cls
echo.
echo   ╔══════════════════════════════════════════════════════════════╗
echo   ║            REGISTRO DE ACTIVIDAD - CleanShield               ║
echo   ╚══════════════════════════════════════════════════════════════╝
echo.
if exist "%LOGFILE%" (
    echo   Ultimas 30 entradas del registro:
    echo   ──────────────────────────────────────────────────────────────
    echo.
    powershell -Command "Get-Content '%LOGFILE%' | Select-Object -Last 30"
    echo.
    echo   ──────────────────────────────────────────────────────────────
    echo   Archivo completo: %LOGFILE%
) else (
    echo   No se encontro el archivo de registro.
    echo   El registro se creara automaticamente al usar CleanShield.
)
echo.
pause
goto MENU_PRINCIPAL

:SALIR
echo %date% %time% - [MENU] Menu principal cerrado por el usuario >> "%LOGFILE%"
cls
echo.
echo   Gracias por usar CleanShield. Tu familia esta protegida.
echo.
timeout /t 2 >nul
exit /b 0
