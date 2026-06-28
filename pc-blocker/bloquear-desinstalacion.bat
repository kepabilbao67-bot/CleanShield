@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title CleanShield - Proteccion Anti-Manipulacion
color 0F

:: ============================================================
:: CleanShield - Bloqueo de Desinstalacion y Anti-Manipulacion
:: Bloquea paginas de extensiones, herramientas de desarrollador,
:: paginas de configuracion, modifica permisos del archivo hosts,
:: bloquea extensiones VPN conocidas, crea tarea de vigilancia
:: ============================================================

:: --- Variables globales ---
set "LOGFILE=%~dp0CleanShield-log.txt"
set "HOSTS=%SystemRoot%\System32\drivers\etc\hosts"
set "ERRORES=0"

:: --- Registrar inicio ---
echo %date% %time% - [ANTITAMPER] Inicio de proteccion anti-manipulacion >> "%LOGFILE%"

:: --- Verificar permisos de administrador ---
net session >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo   ERROR: Este programa requiere permisos de Administrador.
    echo   Haz clic derecho y selecciona "Ejecutar como administrador"
    echo.
    echo %date% %time% - [ANTITAMPER] ERROR: Sin permisos de administrador >> "%LOGFILE%"
    pause
    exit /b 1
)

:: --- Banner ---
cls
echo.
echo   +==============================================================+
echo   !                                                              !
echo   !     CleanShield - Proteccion Anti-Manipulacion               !
echo   !                                                              !
echo   !     Esto hara que la proteccion sea muy dificil de quitar    !
echo   !     sin la contrasena de administrador.                      !
echo   !                                                              !
echo   +==============================================================+
echo.
echo   Aplicando proteccion anti-manipulacion...
echo.

:: ============================================================
:: PASO 1: Bloquear paginas de extensiones en todos los navegadores
:: ============================================================
echo   [1/7] Bloqueando paginas de extensiones...

:: Chrome - bloquear extensiones y configuracion
set "CHROME_KEY=HKLM\SOFTWARE\Policies\Google\Chrome\URLBlocklist"
reg add "%CHROME_KEY%" /v 1 /t REG_SZ /d "chrome://extensions" /f >nul 2>&1
reg add "%CHROME_KEY%" /v 2 /t REG_SZ /d "chrome://extensions/*" /f >nul 2>&1
reg add "%CHROME_KEY%" /v 3 /t REG_SZ /d "chrome://settings" /f >nul 2>&1
reg add "%CHROME_KEY%" /v 4 /t REG_SZ /d "chrome://settings/*" /f >nul 2>&1
reg add "%CHROME_KEY%" /v 5 /t REG_SZ /d "chrome://flags" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo aplicar politica en Chrome
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo politica Chrome >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Chrome: extensiones y configuracion bloqueadas.
)

:: Brave - bloquear extensiones y configuracion
set "BRAVE_KEY=HKLM\SOFTWARE\Policies\BraveSoftware\Brave\URLBlocklist"
reg add "%BRAVE_KEY%" /v 1 /t REG_SZ /d "brave://extensions" /f >nul 2>&1
reg add "%BRAVE_KEY%" /v 2 /t REG_SZ /d "brave://extensions/*" /f >nul 2>&1
reg add "%BRAVE_KEY%" /v 3 /t REG_SZ /d "brave://settings" /f >nul 2>&1
reg add "%BRAVE_KEY%" /v 4 /t REG_SZ /d "brave://settings/*" /f >nul 2>&1
reg add "%BRAVE_KEY%" /v 5 /t REG_SZ /d "brave://flags" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo aplicar politica en Brave
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo politica Brave >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Brave: extensiones y configuracion bloqueadas.
)

:: Edge - bloquear extensiones y configuracion
set "EDGE_KEY=HKLM\SOFTWARE\Policies\Microsoft\Edge\URLBlocklist"
reg add "%EDGE_KEY%" /v 1 /t REG_SZ /d "edge://extensions" /f >nul 2>&1
reg add "%EDGE_KEY%" /v 2 /t REG_SZ /d "edge://extensions/*" /f >nul 2>&1
reg add "%EDGE_KEY%" /v 3 /t REG_SZ /d "edge://settings" /f >nul 2>&1
reg add "%EDGE_KEY%" /v 4 /t REG_SZ /d "edge://settings/*" /f >nul 2>&1
reg add "%EDGE_KEY%" /v 5 /t REG_SZ /d "edge://flags" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo aplicar politica en Edge
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo politica Edge >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Edge: extensiones y configuracion bloqueadas.
)

:: Firefox - bloquear about:addons y about:config
set "FF_KEY=HKLM\SOFTWARE\Policies\Mozilla\Firefox"
reg add "%FF_KEY%\URLBlocklist" /v 1 /t REG_SZ /d "about:addons" /f >nul 2>&1
reg add "%FF_KEY%\URLBlocklist" /v 2 /t REG_SZ /d "about:config" /f >nul 2>&1
reg add "%FF_KEY%\URLBlocklist" /v 3 /t REG_SZ /d "about:preferences" /f >nul 2>&1
reg add "%FF_KEY%" /v BlockAboutAddons /t REG_DWORD /d 1 /f >nul 2>&1
reg add "%FF_KEY%" /v BlockAboutConfig /t REG_DWORD /d 1 /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo aplicar politica en Firefox
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo politica Firefox >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Firefox: addons y config bloqueados.
)

:: Opera - bloquear extensiones y configuracion
set "OPERA_KEY=HKLM\SOFTWARE\Policies\Opera\URLBlocklist"
reg add "%OPERA_KEY%" /v 1 /t REG_SZ /d "opera://extensions" /f >nul 2>&1
reg add "%OPERA_KEY%" /v 2 /t REG_SZ /d "opera://extensions/*" /f >nul 2>&1
reg add "%OPERA_KEY%" /v 3 /t REG_SZ /d "opera://settings" /f >nul 2>&1
reg add "%OPERA_KEY%" /v 4 /t REG_SZ /d "opera://settings/*" /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo aplicar politica en Opera
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo politica Opera >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Opera: extensiones y configuracion bloqueadas.
)

echo %date% %time% - [ANTITAMPER] Paginas de extensiones y configuracion bloqueadas >> "%LOGFILE%"

:: ============================================================
:: PASO 2: Bloquear herramientas de desarrollador
:: ============================================================
echo   [2/7] Bloqueando herramientas de desarrollador...

reg add "HKLM\SOFTWARE\Policies\Google\Chrome" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\BraveSoftware\Brave" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Microsoft\Edge" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1
reg add "%FF_KEY%" /v DisableDeveloperTools /t REG_DWORD /d 1 /f >nul 2>&1
reg add "HKLM\SOFTWARE\Policies\Opera" /v DeveloperToolsAvailability /t REG_DWORD /d 2 /f >nul 2>&1

if %errorlevel% neq 0 (
    echo          ERROR: No se pudieron bloquear herramientas de desarrollador
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo bloqueo DevTools >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Herramientas de desarrollador bloqueadas en todos los navegadores.
    echo %date% %time% - [ANTITAMPER] DevTools bloqueadas >> "%LOGFILE%"
)

:: ============================================================
:: PASO 3: Bloquear acceso al archivo hosts (permisos NTFS)
:: ============================================================
echo   [3/7] Protegiendo archivo hosts contra modificacion...

:: Quitar permisos de escritura a usuarios normales
icacls "%HOSTS%" /inheritance:r >nul 2>&1
icacls "%HOSTS%" /grant SYSTEM:F >nul 2>&1
icacls "%HOSTS%" /grant Administrators:R >nul 2>&1
icacls "%HOSTS%" /deny Users:W >nul 2>&1
icacls "%HOSTS%" /deny Everyone:W >nul 2>&1

if %errorlevel% neq 0 (
    echo          ERROR: No se pudieron modificar los permisos del archivo hosts
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo permisos hosts >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Archivo hosts protegido contra escritura no autorizada.
    echo %date% %time% - [ANTITAMPER] Permisos hosts modificados >> "%LOGFILE%"
)

:: ============================================================
:: PASO 4: Bloquear extensiones VPN conocidas
:: ============================================================
echo   [4/7] Bloqueando extensiones VPN conocidas...

:: Chrome - Bloquear extensiones VPN por ID
set "CHROME_BLOCK=HKLM\SOFTWARE\Policies\Google\Chrome\ExtensionInstallBlocklist"
:: Hola VPN
reg add "%CHROME_BLOCK%" /v 1 /t REG_SZ /d "gkojfkhlekighikafcpjkiklfbnlmeio" /f >nul 2>&1
:: Touch VPN
reg add "%CHROME_BLOCK%" /v 2 /t REG_SZ /d "bihmplhobchoageeokmgbdihknkjbknd" /f >nul 2>&1
:: Browsec VPN
reg add "%CHROME_BLOCK%" /v 3 /t REG_SZ /d "omghfjlpggmjjaagoclmmobgdodcjboh" /f >nul 2>&1
:: ZenMate VPN
reg add "%CHROME_BLOCK%" /v 4 /t REG_SZ /d "fdcgdnkidjaadafnichfpabhfomcebme" /f >nul 2>&1
:: Windscribe VPN
reg add "%CHROME_BLOCK%" /v 5 /t REG_SZ /d "ocllfofhklbbfhgllkgknfknbeibiekh" /f >nul 2>&1
:: SetupVPN
reg add "%CHROME_BLOCK%" /v 6 /t REG_SZ /d "oofgbpoabipfcfjapgnbbjjaenockbdp" /f >nul 2>&1
:: Urban VPN
reg add "%CHROME_BLOCK%" /v 7 /t REG_SZ /d "eppiocemhmnlbhjplcgkofciiegomcon" /f >nul 2>&1
:: ProtonVPN
reg add "%CHROME_BLOCK%" /v 8 /t REG_SZ /d "jplgfhpmjnbigmhklmmbgecoobifkmpa" /f >nul 2>&1

:: Edge - Bloquear extensiones VPN
set "EDGE_BLOCK=HKLM\SOFTWARE\Policies\Microsoft\Edge\ExtensionInstallBlocklist"
reg add "%EDGE_BLOCK%" /v 1 /t REG_SZ /d "gkojfkhlekighikafcpjkiklfbnlmeio" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 2 /t REG_SZ /d "bihmplhobchoageeokmgbdihknkjbknd" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 3 /t REG_SZ /d "omghfjlpggmjjaagoclmmobgdodcjboh" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 4 /t REG_SZ /d "fdcgdnkidjaadafnichfpabhfomcebme" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 5 /t REG_SZ /d "ocllfofhklbbfhgllkgknfknbeibiekh" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 6 /t REG_SZ /d "oofgbpoabipfcfjapgnbbjjaenockbdp" /f >nul 2>&1
reg add "%EDGE_BLOCK%" /v 7 /t REG_SZ /d "eppiocemhmnlbhjplcgkofciiegomcon" /f >nul 2>&1

:: Brave - Bloquear extensiones VPN (usa mismos IDs que Chrome)
set "BRAVE_BLOCK=HKLM\SOFTWARE\Policies\BraveSoftware\Brave\ExtensionInstallBlocklist"
reg add "%BRAVE_BLOCK%" /v 1 /t REG_SZ /d "gkojfkhlekighikafcpjkiklfbnlmeio" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 2 /t REG_SZ /d "bihmplhobchoageeokmgbdihknkjbknd" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 3 /t REG_SZ /d "omghfjlpggmjjaagoclmmobgdodcjboh" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 4 /t REG_SZ /d "fdcgdnkidjaadafnichfpabhfomcebme" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 5 /t REG_SZ /d "ocllfofhklbbfhgllkgknfknbeibiekh" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 6 /t REG_SZ /d "oofgbpoabipfcfjapgnbbjjaenockbdp" /f >nul 2>&1
reg add "%BRAVE_BLOCK%" /v 7 /t REG_SZ /d "eppiocemhmnlbhjplcgkofciiegomcon" /f >nul 2>&1

if %errorlevel% neq 0 (
    echo          ADVERTENCIA: Algunos bloqueos de VPN pueden no haberse aplicado
    echo %date% %time% - [ANTITAMPER] ADVERTENCIA: Fallo parcial bloqueo VPN >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Extensiones VPN bloqueadas en Chrome, Edge y Brave.
    echo %date% %time% - [ANTITAMPER] Extensiones VPN bloqueadas >> "%LOGFILE%"
)

:: ============================================================
:: PASO 5: Crear tarea de vigilancia del archivo hosts
:: ============================================================
echo   [5/7] Creando tarea de vigilancia del archivo hosts...

:: Eliminar tarea anterior si existe
schtasks /delete /tn "CleanShield_VigilarHosts" /f >nul 2>&1

:: Crear tarea que verifica el hosts cada 30 minutos y lo reaplicar si fue modificado
schtasks /create /tn "CleanShield_VigilarHosts" /tr "\"%~dp0instalar-bloqueo.bat\"" /sc minute /mo 30 /ru SYSTEM /rl HIGHEST /f >nul 2>&1
if %errorlevel% neq 0 (
    echo          ERROR: No se pudo crear la tarea de vigilancia
    echo %date% %time% - [ANTITAMPER] ERROR: Fallo tarea vigilancia >> "%LOGFILE%"
    set /a ERRORES+=1
) else (
    echo          Tarea de vigilancia creada (verifica cada 30 minutos).
    echo %date% %time% - [ANTITAMPER] Tarea vigilancia hosts creada >> "%LOGFILE%"
)

:: ============================================================
:: PASO 6: Establecer contrasena de proteccion (si no existe)
:: ============================================================
echo   [6/7] Configurando contrasena de proteccion...

set "CLAVE_REG=HKLM\SOFTWARE\CleanShield"
set "PASS_EXISTENTE="
for /f "tokens=2*" %%a in ('reg query "%CLAVE_REG%" /v Password 2^>nul ^| findstr /i "Password"') do (
    set "PASS_EXISTENTE=%%b"
)

if defined PASS_EXISTENTE (
    echo          Ya existe una contrasena configurada.
    echo %date% %time% - [ANTITAMPER] Contrasena ya existente, no se modifica >> "%LOGFILE%"
) else (
    echo.
    echo   IMPORTANTE: Establece una contrasena para proteger la desinstalacion.
    echo   Esta contrasena sera necesaria para quitar la proteccion.
    echo.
    set /p "NUEVA_PASS=   Nueva contrasena (minimo 6 caracteres): "
    if defined NUEVA_PASS (
        reg add "%CLAVE_REG%" /v Password /t REG_SZ /d "!NUEVA_PASS!" /f >nul 2>&1
        if %errorlevel% neq 0 (
            echo          ERROR: No se pudo guardar la contrasena
            echo %date% %time% - [ANTITAMPER] ERROR: Fallo guardar contrasena >> "%LOGFILE%"
            set /a ERRORES+=1
        ) else (
            echo          Contrasena configurada correctamente.
            echo %date% %time% - [ANTITAMPER] Contrasena configurada >> "%LOGFILE%"
        )
    ) else (
        echo          No se establecio contrasena. Se usara frase de seguridad.
        echo %date% %time% - [ANTITAMPER] Sin contrasena, se usara frase >> "%LOGFILE%"
    )
)

:: ============================================================
:: PASO 7: Proteccion adicional del registro
:: ============================================================
echo   [7/7] Aplicando protecciones adicionales...

:: Marcar que anti-tamper esta activo
reg add "%CLAVE_REG%" /v AntiTamper /t REG_DWORD /d 1 /f >nul 2>&1
reg add "%CLAVE_REG%" /v InstallDate /t REG_SZ /d "%date% %time%" /f >nul 2>&1

echo          Protecciones adicionales aplicadas.
echo %date% %time% - [ANTITAMPER] Protecciones adicionales aplicadas >> "%LOGFILE%"

:: ============================================================
:: RESUMEN FINAL
:: ============================================================
echo.
echo   +==============================================================+
echo   !                                                              !
echo   !           PROTECCION ANTI-MANIPULACION APLICADA              !
echo   !                                                              !
echo   +--------------------------------------------------------------+
echo   !                                                              !
echo   !   Paginas de extensiones:   BLOQUEADAS                       !
echo   !   Paginas de configuracion: BLOQUEADAS                       !
echo   !   Herramientas desarrollo:  BLOQUEADAS                       !
echo   !   Archivo hosts:            PROTEGIDO                        !
echo   !   Extensiones VPN:          BLOQUEADAS                       !
echo   !   Tarea de vigilancia:      ACTIVA (cada 30 min)             !
echo   !   Contrasena:               CONFIGURADA                      !
echo   !                                                              !
echo   !   Errores encontrados:      %ERRORES%                                !
echo   !                                                              !
echo   +==============================================================+
echo.

if %ERRORES% gtr 0 (
    color 0E
    echo   ADVERTENCIA: Se encontraron %ERRORES% errores.
    echo   Revisa el log: %LOGFILE%
) else (
    color 0A
    echo   Todas las protecciones se aplicaron correctamente.
)

echo.
echo %date% %time% - [ANTITAMPER] Finalizacion. Errores: %ERRORES% >> "%LOGFILE%"
pause
exit /b 0
