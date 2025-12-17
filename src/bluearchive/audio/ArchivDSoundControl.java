package bluearchive.audio;

import arc.Core;
import arc.Events;
import arc.audio.Filters;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.Fi;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.*;
import bluearchive.ui.overrides.ArchivDLoadingFragment;
import mindustry.audio.SoundControl;
import mindustry.content.StatusEffects;
import mindustry.game.EventType;
import mindustry.gen.Musics;
import mindustry.gen.Sounds;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bluearchive.ArchiveDustry.recollectionMusic;
import static bluearchive.audio.ArchivDMusic.*;
import static bluearchive.ui.ArchivDUI.*;
import static mindustry.Vars.*;
import static mindustry.Vars.tree;

public class ArchivDSoundControl extends SoundControl {
    public static Seq<Music> ambientMusic, darkMusic, waveMusic, bossMusic = Seq.with();
    protected @Nullable Music current;
    private boolean previousLoadFragShow = false;

    public static void loadSoundControl() {
        control.sound = new ArchivDSoundControl();
        control.sound.stop();
    }

    @Override
    protected void reload() {
        current = null;
        fade = 0f;
        ambientMusic = Seq.with(Musics.game1, Musics.game3, Musics.game6, Musics.game8, Musics.game9, Musics.fine, dawn, cat, bunny, game10, honey, amplify, t171, theme220);
        darkMusic = Seq.with(Musics.game7, Musics.game4, aira, sugar, hare, oriental, dreamer, game11,moment, somedaySometime, theme228);
        waveMusic = Seq.with(Musics.game2, Musics.game5, wave1, aspiration, wave2, wave3, wave4);
        bossMusic = Seq.with(Musics.boss1, Musics.boss2, boss3, boss4);

        for(var sound : Core.assets.getAll(Sound.class, new Seq<>())) {
            var file = Fi.get(Core.assets.getAssetFileName(sound));
            if (file.parent().name().equals("ui")) {
                sound.setBus(uiBus);
            }
        }
        Events.fire(new EventType.MusicRegisterEvent());
    }

    @Override
    public void update() {
        boolean paused = state.isGame() && Core.scene.hasDialog();
        boolean playing = state.isGame();

        //check if current track is finished
        if (current != null && !current.isPlaying()) {
            current = null;
            fade = 0f;
        }

        //fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
        if (timer.get(1, 30f)) {
            Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
        }

        //play/stop ordinary effects
        if (playing != wasPlaying) {
            wasPlaying = playing;

            if (playing) {
                Core.audio.soundBus.play();
                setupFilters();
            } else {
                //stopping a single audio bus stops everything else, yay!
                Core.audio.soundBus.stop();
                //play music bus again, as it was stopped above
                Core.audio.musicBus.play();

                Core.audio.soundBus.play();
            }
        }

        Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());

        if(ArchivDLoadingFragment.loadFragShow && !previousLoadFragShow){
                Sounds.uiChat.play();
        }
        previousLoadFragShow = ArchivDLoadingFragment.loadFragShow;

        // Menu state
        if (state.isMenu()) {
            silenced = false;

            // Planet view
            if (ui.planet.isShown()) {
                // Database handling in planet view
                if (ui.database.isShown() && !ui.research.isShown()) {
                    this.stop();
                    if(!database.isPlaying()) database.play();
                } else {
                    if(database != null) database.stop();
                }

                // Research handling in planet view
                if (ui.research.isShown()) {
                    this.stop();
                    if(!research.isPlaying()) research.play();
                } else {
                    if(research != null) research.stop();
                }

                // Loadouts handling in planet view
                if (ui.planet.loadouts.isShown()) {
                    this.stop();
                    if(!loadout.isPlaying()) loadout.play();
                } else {
                    if(loadout != null) loadout.stop();
                }

                // Play planet music
                play(ui.planet.state.planet.launchMusic);
            } else if (ui.editor.isShown()) {
                play(Musics.editor);
            } else {
                 if (ui.database.isShown()) {
                     this.stop();
                     if(!database.isPlaying()) database.play();
                } else if (creditsDialog.isShown()) {
                     this.stop();
                     if(!re_aoh.isPlaying()) re_aoh.play();
                 } else {
                     if(database != null) database.stop();
                     if(re_aoh != null) re_aoh.stop();
                 }
                play(Musics.menu);
            }
        } 
        // Editor state
        else if (state.rules.editor) {
            silenced = false;
            play(Musics.editor);
        } 
        // In-game state
        else {
            // Fade out the last track to make way for ingame music
            silence();

            // Database handling in game
            if (ui.database.isShown()) {
                this.stop();
                if(!database.isPlaying()) database.play();
            } else {
                if(database != null) database.stop();
            }

            // Research handling in game
            if (ui.research.isShown()) {
                this.stop();
                if(!research.isPlaying()) research.play();
            } else {
                if(research != null) research.stop();
            }

            // Schematics handling in game
            if (ui.schematics.isShown()) {
                this.stop();
                if(!loadout.isPlaying()) loadout.play();
            } else {
                if(loadout != null) loadout.stop();
            }

            // Random music playback logic
            if (Core.settings.getBool("alwaysmusic")) {
                if (current == null) {
                    playRandom();
                }
            } else if (Time.timeSinceMillis(lastPlayed) > 1000 * musicInterval / 60f) {
                // Chance to play music per interval
                if (Mathf.chance(musicChance)) {
                    lastPlayed = Time.millis();
                    playRandom();
                }
            }
        }
        updateLoops();
    }

    public static void replaceMainMenu() {
        switch (Core.settings.getString("selectedSong")) {
            case "menucm":
                if (Musics.menu != tree.loadMusic("menucm")) Musics.menu = !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04") ? tree.loadMusic("menucm") : ArchivDMusic.funnyAhh;
                break;
            case "menure-aoh":
                if (Musics.menu != tree.loadMusic("menure-aoh")) Musics.menu = !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04") ? tree.loadMusic("menure-aoh") : ArchivDMusic.funnyAhh;
                break;
            case "recollection":
                if (Musics.menu != recollectionMusic && recollectionMusic != null) Musics.menu = !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04") ? recollectionMusic : ArchivDMusic.funnyAhh;
                break;
        }
    }
}
