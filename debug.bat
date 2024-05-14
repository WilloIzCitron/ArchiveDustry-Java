call gradlew jar
call copy "build\libs\ArchiveDustry-javaDesktop.jar" "%appdata%\Mindustry\mods"
call java -jar Mindustry.jar