package bluearchive.audio;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.audio.Sound;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.*;
import mindustry.Vars;
import bluearchive.audio.ArchivDSoundControl.*;
import mindustry.content.StatusEffects;
import mindustry.game.EventType;
import mindustry.gen.Musics;
import mindustry.gen.Sounds;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bluearchive.ArchiveDustry.recollectionMusic;
import static bluearchive.ui.overrides.ArchivDSettings.waveVolume;
import static mindustry.Vars.*;

public class ArchivDMusic {
    public static @Nullable Music current;
    public static Seq<Music> waveMusic;
    private static Seq<Music> bossMusic;
    public static Music lastMusicPlayed;
    private static Music currentPlay;
    public static Music
            wave1, wave2, wave3, wave4,
            cat, aspiration, dawn, bunny,
            aira, sugar, hare, oriental,
            boss3, boss4, dreamer, game10,
            game11, honey, amplify, moment, somedaySometime, t171, theme220, theme228, re_aoh, constMod,
            funnyAhh, loadout, database, research, win, lose;

    // I hope this works :)
    protected static void playMusic(Music music){
        if(current != null || music == null || !(boolean)((Core.settings.getInt("musicvol") > 0) || waveVolume > 0)) return;
        lastMusicPlayed = music;
        current = music;
        current.setVolume(waveVolume);
        current.setLooping(false);
        current.play();

    }

    public static void load(){
        Events.on(EventType.ClientLoadEvent.class, e -> {
            current = null;
            //music loader
            try {
                wave1 = new Music(tree.get("music/wave1.ogg"));
                wave2 = new Music(tree.get("music/wave2.ogg"));
                wave3 = new Music(tree.get("music/wave3.ogg"));
                wave4 = new Music(tree.get("music/wave4.ogg"));
                cat = new Music(tree.get("music/cat.ogg"));
                aspiration = new Music(tree.get("music/aspiration.ogg"));
                dawn = new Music(tree.get("music/dawn.ogg"));
                bunny = new Music(tree.get("music/bunny.ogg"));
                aira = new Music(tree.get("music/menuaira.ogg"));
                sugar = new Music(tree.get("music/menurcl.ogg"));
                hare = new Music(tree.get("music/hare.ogg"));
                oriental = new Music(tree.get("music/oriental.ogg"));
                game10 = new Music(tree.get("music/game10.ogg"));
                game11 = new Music(tree.get("music/game11.ogg"));
                honey = new Music(tree.get("music/honey.ogg"));
                dreamer = new Music(tree.get("music/dreamer.ogg"));
                boss3 = new Music(tree.get("music/boss3.ogg"));
                boss4 = new Music(tree.get("music/boss4.ogg"));
                amplify = new Music(tree.get("music/amplify.ogg"));
                moment = new Music(tree.get("music/moment.ogg"));
                somedaySometime = new Music(tree.get("music/somedaySometime.ogg"));
                t171 = new Music(tree.get("music/t171.ogg"));
                theme220 = new Music(tree.get("music/theme220.ogg"));
                theme228 = new Music(tree.get("music/theme228.ogg"));
                re_aoh = new Music(tree.get("music/menure-aoh.ogg"));
                constMod = new Music(tree.get("music/menucm.ogg"));
                if (LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04")) {
                    funnyAhh = new Music(tree.get("music/donpolo.ogg"));
                    Sounds.press = new Sound(tree.get("sounds/queBom.mp3"));
                    Sounds.back = new Sound(tree.get("sounds/queBom.mp3"));
                }
                research = Vars.tree.loadMusic("research");
                database = Vars.tree.loadMusic("database");
                loadout = Vars.tree.loadMusic("loadout");
                win = Vars.tree.loadMusic("win");
                lose = Vars.tree.loadMusic("lose");
            } catch (Exception ex) {
                // Music has exception throw, why it was created
                throw new RuntimeException(ex);
            }

            // create wave music soundtrack
            waveMusic = Seq.with(Musics.game2, Musics.game5, wave1, aspiration, wave2, wave3, wave4);
            // music updater
            Log.infoTag("ArchiveDustry", "Music has been loaded!");
            Timer.schedule(() -> {
                if(state.isPlaying()) {
                    // stops if there's no enemy
                    if (current != null && state.enemies == 0) {
                        current.stop();
                        current = null;
                    }
                    // no interruption from ambient soundtrack
                    if (current != null && Reflect.get(control.sound, "current") != null) {
                        currentPlay = Reflect.get(control.sound, "current");
                        currentPlay.stop();
                    }
                }
            }, 0f, 0.01f);
        });
        Events.on(EventType.SectorCaptureEvent.class, e -> {
            if (current != null) {
                current.stop();
            }
            if (!tree.loadMusic("win").isPlaying()) tree.loadMusic("win").play();
            Time.run(306f, () -> {
                if (tree.loadMusic("win").isPlaying()) tree.loadMusic("win").stop();
                Time.clear();
            });
        });

        Events.on(EventType.WinEvent.class, winner -> {
            if (current != null) {
                current.stop();
            }
            if (!tree.loadMusic("win").isPlaying()) tree.loadMusic("win").play();
            ui.restart.hidden(() -> {
                if (tree.loadMusic("win").isPlaying()) tree.loadMusic("win").stop();
            });
        });
        // sector captured = win
        Events.on(EventType.LoseEvent.class, loser -> {
            if (current != null) {
                current.stop();
            }
            if (!tree.loadMusic("lose").isPlaying()) tree.loadMusic("lose").play();
            ui.restart.hidden(() -> {
                if (tree.loadMusic("lose").isPlaying()) tree.loadMusic("lose").stop();
            });
        });
            Events.on(EventType.WaveEvent.class, e -> {
                if(state.isPlaying()) {
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
                }
            });

    }
}
