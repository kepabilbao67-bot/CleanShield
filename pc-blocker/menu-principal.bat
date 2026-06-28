@echo off
title CleanShield - Panel de Control
color 0A

:: Verificar permisos de administrador
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Ejecutar como Administrador.
    echo   Clic derecho - Ejecutar como administrador
    echo.
    pause
    exit /b 1
)

set "LOGFILE=%~dp0CleanShield-log.txt"

:MENU_PRINCIPAL
cls
color 0A
echo.
echo   ====================================================
echo   =                                                  =
echo   =          CLEANSHIELD v2.0                        =
echo   =          Proteccion Familiar Inteligente         =
echo   =                                                  =
echo   ====================================================
echo.
echo   [1] Instalar bloqueo completo (+1100 dominios)
echo   [2] Configurar categorias (activar/desactivar)
echo   [3] Verificar estado del bloqueo
echo   [4] Proteccion anti-desinstalacion
echo   [5] Abrir Asistente IA
echo   [6] Desinstalar bloqueo
echo   [0] Salir
echo.
echo   ====================================================
echo.
set /p opcion="   Elige una opcion (0-6): "

if "%opcion%"=="1" goto INSTALAR
if "%opcion%"=="2" goto CATEGORIAS
if "%opcion%"=="3" goto VERIFICAR
if "%opcion%"=="4" goto PROTECCION
if "%opcion%"=="5" goto ASISTENTE
if "%opcion%"=="6" goto DESINSTALAR
if "%opcion%"=="0" goto SALIR

echo   Opcion no valida.
timeout /t 2 >nul
goto MENU_PRINCIPAL

:INSTALAR
echo.
echo   Instalando bloqueo...
call "%~dp0instalar-bloqueo.bat"
pause
goto MENU_PRINCIPAL

:CATEGORIAS
echo.
call "%~dp0configurar-categorias.bat"
pause
goto MENU_PRINCIPAL

:VERIFICAR
echo.
call "%~dp0verificar-estado.bat"
pause
goto MENU_PRINCIPAL

:PROTECCION
echo.
call "%~dp0bloquear-desinstalacion.bat"
pause
goto MENU_PRINCIPAL

:ASISTENTE
echo.
echo   Abriendo asistente IA en el navegador...
start "" "%~dp0asistente-ia\index.html"
goto MENU_PRINCIPAL

:DESINSTALAR
echo.
call "%~dp0desinstalar-bloqueo.bat"
pause
goto MENU_PRINCIPAL

:SALIR
echo.
echo   Hasta luego. Mantente fuerte.
echo.
timeout /t 2 >nul
exit /b 0
