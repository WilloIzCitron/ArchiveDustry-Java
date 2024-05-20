package bluearchive;

import arc.*;
import arc.audio.*;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.audio.SoundControl;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class ArchivDMusic {
    public static Seq<Music> waveMusic = Seq.with();
    private static @Nullable Music current;
    private static Music lastMusicPlayed;
    protected static long lastPlayed;
    private static Music currentPlay;

    // i hope this work :)
    protected static void playMusic(Music music){
        if(current != null || music == null || !(boolean)(Core.settings.getInt("musicvol") > 0)) return;
        lastMusicPlayed = music;
        current = music;
        current.setVolume(1f);
        current.setLooping(false);
        current.play();

    }

    public static void load(){
        Events.on(EventType.ClientLoadEvent.class, e -> {
            current = null;
            //music loader
            Music wave1, cat, aspiration, dawn, bunny, aira, sugar, hare, oriental;
            try {
                wave1 = new Music(tree.get("music/wave1.ogg"));
                cat = new Music(tree.get("music/cat.ogg"));
                aspiration = new Music(tree.get("music/aspiration.ogg"));
                dawn = new Music(tree.get("music/dawn.ogg"));
                bunny = new Music(tree.get("music/bunny.ogg"));
                aira = new Music(tree.get("music/menuaira.ogg"));
                sugar = new Music(tree.get("music/menurcl.ogg"));
                hare = new Music(tree.get("music/hare.ogg"));
                oriental = new Music(tree.get("music/oriental.ogg"));
            } catch (Exception ex) {
                // Music has exception throw, why it was created
                throw new RuntimeException(ex);
            }
            // add custom music contents to vanilla SoundControl's music sequences
            control.sound.ambientMusic.addAll(dawn, cat, bunny);
            control.sound.darkMusic.addAll(aira, sugar, hare, oriental);
            // create wave music soundtrack
            waveMusic = Seq.with(Musics.game2, Musics.game5, wave1, aspiration);
            // pls don't insult me btw... remove duplicates with by array itself
            control.sound.bossMusic.remove(2);
            control.sound.bossMusic.remove(2);
            control.sound.darkMusic.remove(0);
            control.sound.darkMusic.remove(0);
            // music updater
            Timer.schedule(() -> {
                // stops if there's no enemy
                if (current != null && state.enemies == 0){
                    current.stop();
                    current = null;
                }
                // no interruption from ambient soundtrack
                if(current != null && Reflect.get(control.sound, "current") != null) {
                    currentPlay = Reflect.get(control.sound, "current");
                    currentPlay.stop();
                }
            }, 0f, 0.01f);
        });
            Events.on(EventType.WaveEvent.class, e -> {
                currentPlay = Reflect.get(control.sound, "current");
                if(currentPlay != null) currentPlay.stop();
                boolean boss = state.rules.spawns.contains(group -> group.getSpawned(state.wave - 2) > 0 && group.effect == StatusEffects.boss);
                Time.run(Mathf.random(0.5f, 1f) * 60f, () -> {
                    playMusic(waveMusic.random(lastMusicPlayed));
                    if (boss) {
                        current.stop();
                        current = null;
                    }
            });
            });
    }
}
