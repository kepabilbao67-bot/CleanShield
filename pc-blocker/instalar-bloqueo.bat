@echo off
echo ============================================
echo   CleanShield - Bloqueo Total del Sistema
echo ============================================
echo.
echo Este script bloqueara paginas de pornografia,
echo apuestas y prestamos abusivos en TODOS los
echo navegadores (Chrome, Brave, Firefox, Edge...)
echo.
echo Se necesitan permisos de administrador.
echo.

:: Check for admin rights
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Necesitas ejecutar como Administrador.
    echo Clic derecho en este archivo - Ejecutar como administrador
    pause
    exit /b 1
)

echo Creando copia de seguridad del archivo hosts...
copy "%SystemRoot%\System32\drivers\etc\hosts" "%SystemRoot%\System32\drivers\etc\hosts.backup" >nul 2>&1

echo Anadiendo dominios bloqueados...

:: ======= PORNOGRAFIA =======
echo. >> "%SystemRoot%\System32\drivers\etc\hosts"
echo # ===== CleanShield - BLOQUEO PORNOGRAFIA ===== >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 pornhub.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.pornhub.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 es.pornhub.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 xvideos.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.xvideos.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 xnxx.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.xnxx.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 xhamster.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.xhamster.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 es.xhamster.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 xhamster.desi >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 redtube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.redtube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 youporn.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.youporn.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 tube8.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.tube8.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 spankbang.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.spankbang.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 brazzers.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.brazzers.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 bangbros.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.bangbros.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 naughtyamerica.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.naughtyamerica.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 realitykings.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.realitykings.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 mofos.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.mofos.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 fakehub.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.fakehub.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 onlyfans.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.onlyfans.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 chaturbate.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.chaturbate.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 stripchat.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.stripchat.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 cam4.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.cam4.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 livejasmin.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.livejasmin.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 bongacams.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.bongacams.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 nhentai.net >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.nhentai.net >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 hanime.tv >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.hanime.tv >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 rule34.xxx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.rule34.xxx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 e621.net >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.e621.net >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 gelbooru.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.gelbooru.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 danbooru.donmai.us >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 hitomi.la >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.hitomi.la >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 fapello.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.fapello.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 sankakucomplex.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.sankakucomplex.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 hentaihaven.xxx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.hentaihaven.xxx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 porn.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.porn.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 4tube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.4tube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 motherless.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.motherless.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 eporner.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.eporner.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 txxx.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.txxx.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 tnaflix.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.tnaflix.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 drtuber.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.drtuber.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 alohatube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.alohatube.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 thumbzilla.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.thumbzilla.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 beeg.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.beeg.com >> "%SystemRoot%\System32\drivers\etc\hosts"

:: ======= APUESTAS =======
echo # ===== CleanShield - BLOQUEO APUESTAS ===== >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 bet365.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.bet365.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 caliente.mx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.caliente.mx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 codere.mx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.codere.mx >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 codere.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.codere.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 888casino.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.888casino.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 888casino.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.888casino.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 pokerstars.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.pokerstars.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 pokerstars.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.pokerstars.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 betway.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.betway.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 williamhill.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.williamhill.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 williamhill.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.williamhill.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 bwin.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.bwin.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 bwin.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.bwin.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 1xbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.1xbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 mostbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.mostbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 sportingbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.sportingbet.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 betfair.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.betfair.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 betfair.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.betfair.es >> "%SystemRoot%\System32\drivers\etc\hosts"

:: ======= PRESTAMOS ABUSIVOS =======
echo # ===== CleanShield - BLOQUEO PRESTAMOS ===== >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 kueski.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.kueski.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 dineria.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.dineria.com >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 vivus.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.vivus.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 moneyman.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.moneyman.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 creditea.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.creditea.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 quebueno.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.quebueno.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 wandoo.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo 0.0.0.0 www.wandoo.es >> "%SystemRoot%\System32\drivers\etc\hosts"
echo # ===== FIN CleanShield ===== >> "%SystemRoot%\System32\drivers\etc\hosts"

:: Flush DNS cache
ipconfig /flushdns >nul 2>&1

echo.
echo ============================================
echo   BLOQUEO INSTALADO CORRECTAMENTE
echo ============================================
echo.
echo Se han bloqueado +120 dominios en TODOS los navegadores.
echo.
echo Para desbloquear, ejecuta: desinstalar-bloqueo.bat
echo.
pause
