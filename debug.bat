call gradlew jar
call copy "build\libs\ArchiveDustry-javaDesktop.jar" "%appdata%\Mindustry\mods"
start /wait java -jar Mindustry.jar