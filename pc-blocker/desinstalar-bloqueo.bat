@echo off
echo ============================================
echo   CleanShield - Quitar Bloqueo
echo ============================================
echo.
echo ATENCION: Esto eliminara el bloqueo de contenido.
echo.

:: Check for admin rights
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Necesitas ejecutar como Administrador.
    echo Clic derecho en este archivo - Ejecutar como administrador
    pause
    exit /b 1
)

set /p confirm="Escribe DESBLOQUEAR para confirmar: "
if /i not "%confirm%"=="DESBLOQUEAR" (
    echo Operacion cancelada.
    pause
    exit /b 0
)

:: Restore backup
if exist "%SystemRoot%\System32\drivers\etc\hosts.backup" (
    copy /y "%SystemRoot%\System32\drivers\etc\hosts.backup" "%SystemRoot%\System32\drivers\etc\hosts" >nul
    echo Archivo hosts restaurado desde la copia de seguridad.
) else (
    :: Remove CleanShield lines manually
    powershell -Command "(Get-Content '%SystemRoot%\System32\drivers\etc\hosts') | Where-Object { $_ -notmatch 'CleanShield' -and $_ -notmatch '0.0.0.0 pornhub' -and $_ -notmatch '0.0.0.0 xvideos' -and $_ -notmatch '0.0.0.0 xnxx' -and $_ -notmatch '0.0.0.0 xhamster' -and $_ -notmatch '0.0.0.0 redtube' -and $_ -notmatch '0.0.0.0 bet365' -and $_ -notmatch '0.0.0.0 porn' } | Set-Content '%SystemRoot%\System32\drivers\etc\hosts'"
    echo Lineas de CleanShield eliminadas.
)

ipconfig /flushdns >nul 2>&1

echo.
echo Bloqueo eliminado. Los sitios volveran a ser accesibles.
echo.
pause
