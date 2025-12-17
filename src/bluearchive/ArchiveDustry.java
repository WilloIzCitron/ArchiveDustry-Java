package bluearchive;

import arc.*;
import arc.audio.*;
import arc.files.Fi;
import arc.struct.*;
import arc.util.*;
import bluearchive.audio.ArchivDMusic;
import bluearchive.audio.ArchivDSoundControl;
import bluearchive.audio.UnitSound;
import bluearchive.expansions.exoprosopa.units.ExopUnitHalo;
import bluearchive.l2d.Live2DBackgrounds;
import bluearchive.ui.ArchivDUI;
import bluearchive.ui.overrides.ArchivDBackground;
import bluearchive.ui.overrides.ArchivDLoadingFragment;
import bluearchive.ui.overrides.ArchivDSettings;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.mod.*;
import bluearchive.units.*;
import arc.math.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static bluearchive.ui.ArchivDUI.*;
import static mindustry.Vars.*;

public class ArchiveDustry extends Mod {
    public static Music recollectionMusic;

    public static int foundL2D, loadedL2D, erroredL2D;

    public ArchiveDustry() {

    }

    @Override
    public void init(){
        //Use Pal.accent first... experimental Styles
        //ArchivDStyles.load();
        if(!mobile || !headless) Core.graphics.setTitle(Core.settings.getAppName()+" v"+Version.buildString()+" | ArchiveDustry v"+mods.getMod("bluearchive").meta.version+ " | "+RandomMessage());
        ArchivDLoadingFragment.init();
        ArchivDSettings.loadSettings();
        if(Core.settings.getBool("ba-addHalo", true)) {
            if((mods.getMod("exoprosopa") != null) && (mods.getMod("exoprosopa").enabled())) {
                    ExopUnitHalo.load();
            }
            UnitHalo.init();
        }
        if(Core.settings.getBool("HinaVoiceEnable") || Core.settings.getBool("ArisuVoiceEnable")) UnitSound.init();
        if(Core.settings.getBool("enableL2D")) {
            dataDirectory.child("live2d").walk(f -> {
                foundL2D++;
                try {
                    Live2DBackgrounds.load(f);
                    loadedL2D++;
                } catch (Exception e) {
                    Log.err(e);
                    erroredL2D++;
                }
            });
        }
        ArchivDMusic.load();
        if (Core.settings.getString("selectedSong") == null) {
            Core.settings.put("selectedSong", "menucm");
        }
        if (!Core.graphics.isPortrait() && Core.settings.getBool("enableL2D") && Core.settings.has("setL2D-new")) {
            if (!Core.settings.getString("setL2D-new").isEmpty()) {
                ArchivDBackground.buildL2D(Core.settings.getString("setL2D-new"));
            } else {
                Core.settings.put("enableL2D", false); //fallback if setl2d is blank
                Core.settings.put("selectedSong", "menucm");
            }
        }
            Events.on(EventType.ClientLoadEvent.class, event -> {
                ArchivDUI.init();
                ArchivDSoundControl.loadSoundControl();
                ArchivDSoundControl.replaceMainMenu();
                Log.infoTag("ArchiveDustry", "Fully Loaded!");
                if (Core.settings.getBool("ba-firstTime")) {
                    firstTimeDialog.show();
                }
                Timer.schedule(() -> control.sound.stop(), 0.1f);

            });
    }

    static String RandomMessage(){
        Fi msg = tree.get("text/messages.txt");
        if(!msg.exists()) return "null";
        Seq<String> strings = Seq.with(msg.readString("UTF-8").split("\n"));
        int stringLength = strings.size - 1;
        return (!LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04")) ? strings.get(Mathf.random(stringLength)) : "Que Bom!";
    }
}
