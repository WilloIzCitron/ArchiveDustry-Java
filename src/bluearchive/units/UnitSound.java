package bluearchive.units;

import arc.*;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import mindustry.content.UnitTypes;
import mindustry.game.EventType.*;
import mindustry.Vars;
import mindustry.gen.Sounds;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UnitSound {
    // Tendou Arisu (Collaris) Assets
    static Seq<Sound> ArisuArrivalSound = Seq.with();
    static Seq<Sound> ArisuHitSound = Seq.with();
    static Sound ArisuArrivalAssignedSound;
    static Seq<Sound> ArisuShootSound = Seq.with();
    static Sound ArisuPickedSound = new Sound(Vars.tree.get("sounds/units/collaris-pickup.ogg"));
    static Sound ArisuArrival1 = new Sound(Vars.tree.get("sounds/units/collaris-arrival1.ogg"));
    static Sound ArisuArrival2 = new Sound(Vars.tree.get("sounds/units/collaris-arrival2.ogg"));
    static Sound ArisuHit1 = new Sound(Vars.tree.get("sounds/units/collaris-hit1.ogg"));
    static Sound ArisuHit2 = new Sound(Vars.tree.get("sounds/units/collaris-hit2.ogg"));
    static Sound ArisuHit3 = new Sound(Vars.tree.get("sounds/units/collaris-hit3.ogg"));
    static Sound ArisuAttack1 = new Sound(Vars.tree.get("sounds/units/collaris-attack1.ogg"));
    static Sound ArisuAttack2 = new Sound(Vars.tree.get("sounds/units/collaris-attack2.ogg"));
    static Sound ArisuAttack3 = new Sound(Vars.tree.get("sounds/units/collaris-attack3.ogg"));
    static Sound ArisuDeath = new Sound(Vars.tree.get("sounds/units/collaris-death.ogg"));

    // Sorasaki Hina (Toxopid) Assets
    static Seq<Sound> HinaArrivalSound = Seq.with();
    static Seq<Sound> HinaHitSound = Seq.with();
    static Sound HinaArrivalAssignedSound;
    static Seq<Sound> HinaShootSound = Seq.with();
    static Sound HinaPickedSound = new Sound(Vars.tree.get("sounds/units/toxopid-pickup.ogg"));
    static Sound HinaArrival1 = new Sound(Vars.tree.get("sounds/units/toxopid-arrival1.ogg"));
    static Sound HinaArrival2 = new Sound(Vars.tree.get("sounds/units/toxopid-arrival2.ogg"));
    static Sound HinaHit1 = new Sound(Vars.tree.get("sounds/units/toxopid-hit1.ogg"));
    static Sound HinaHit2 = new Sound(Vars.tree.get("sounds/units/toxopid-hit2.ogg"));
    static Sound HinaHit3 = new Sound(Vars.tree.get("sounds/units/toxopid-hit3.ogg"));
    static Sound HinaAttack1 = new Sound(Vars.tree.get("sounds/units/toxopid-attack1.ogg"));
    static Sound HinaAttack2 = new Sound(Vars.tree.get("sounds/units/toxopid-attack2.ogg"));
    static Sound HinaAttack3 = new Sound(Vars.tree.get("sounds/units/toxopid-attack3.ogg"));
    static Sound HinaArtillery = new Sound(Vars.tree.get("sounds/units/toxopid-artillery.ogg"));
    static Sound HinaDeath = new Sound(Vars.tree.get("sounds/units/toxopid-death.ogg"));

    // Interval
    static Interval interval = new Interval(5);

    public static void init() {
        HinaShootSound = Seq.with(Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, HinaAttack1, HinaAttack2, HinaAttack3);
        ArisuShootSound = Seq.with(Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, ArisuAttack1, ArisuAttack2, ArisuAttack3);
        Seq<Sound> HinaArtillerySound = Seq.with(Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, HinaArtillery);


        UnitTypes.collaris.deathSound = ArisuDeath;
        UnitTypes.toxopid.deathSound = HinaDeath;
        Events.on(PayloadDropEvent.class, e -> {
            if (Vars.state.isPlaying() || Vars.state.isGame()) {
                if (e.unit != null) {
                    if (e.unit.type == Vars.content.unit("collaris") && (Core.settings.getBool("ArisuVoiceEnable") && !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04"))) {
                        if (ArisuPickedSound != null) ArisuPickedSound.stop();
                        ArisuArrivalSound = Seq.with(ArisuArrival1, ArisuArrival2);
                        ArisuArrivalAssignedSound = ArisuArrivalSound.random();
                        ArisuArrivalAssignedSound.play();
                    }
                    if (e.unit.type == Vars.content.unit("toxopid") && Core.settings.getBool("HinaVoiceEnable")) {
                        if (HinaPickedSound != null) HinaPickedSound.stop();
                        HinaArrivalSound = Seq.with(HinaArrival1, HinaArrival2);
                        HinaArrivalAssignedSound = HinaArrivalSound.random();
                        HinaArrivalAssignedSound.play();
                    }
                }
            }
        });
        Events.on(PickupEvent.class, e -> {
            if (Vars.state.isPlaying() || Vars.state.isGame()) {
                if (e.unit != null) {
                    if (e.unit.type == Vars.content.unit("collaris") && (Core.settings.getBool("ArisuVoiceEnable") && !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04"))) {
                        if (ArisuArrivalAssignedSound != null) ArisuArrivalAssignedSound.stop();
                        ArisuPickedSound.play();
                    }
                    if (e.unit.type == Vars.content.unit("toxopid") && Core.settings.getBool("HinaVoiceEnable")) {
                        if (HinaArrivalAssignedSound != null) HinaArrivalAssignedSound.stop();
                        HinaPickedSound.play();
                    }
                }
            }
        });
        Events.on(UnitDamageEvent.class, e -> {
            if (Vars.state.isPlaying() || Vars.state.isGame()) {
                if ((e.unit.type == Vars.content.unit("collaris") && interval.get(300)) && (Core.settings.getBool("ArisuVoiceEnable") && !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04"))) {
                    ArisuHitSound = Seq.with(ArisuHit1, ArisuHit2, ArisuHit3);
                    if (!e.unit.dead) {
                        Time.run(0f, () -> ArisuHitSound.random().play());
                    }
                }
                if ((e.unit.type == Vars.content.unit("toxopid") && interval.get(300)) && Core.settings.getBool("HinaVoiceEnable")) {
                    HinaHitSound = Seq.with(HinaHit1, HinaHit2, HinaHit3);
                    if (!e.unit.dead) {
                        Time.run(0f, () -> HinaHitSound.random().play());
                    }
                }
            }
        });
        Timer.schedule(() -> {
            Sound ArisuAssignedSound = (Core.settings.getBool("ArisuVoiceEnable") && !LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM")).equals("01-04")) ? ArisuShootSound.random() : Sounds.pulseBlast;
            if (ArisuAssignedSound != Sounds.pulseBlast) {
                UnitTypes.collaris.weapons.get(0).soundPitchMin = 1f;
                UnitTypes.collaris.weapons.get(1).soundPitchMin = 1f;
            } else {
                UnitTypes.collaris.weapons.get(0).soundPitchMin = 0.8f;
                UnitTypes.collaris.weapons.get(1).soundPitchMin = 0.8f;
            }
            UnitTypes.collaris.weapons.get(0).shootSound = ArisuAssignedSound;
            UnitTypes.collaris.weapons.get(1).shootSound = ArisuAssignedSound;
        }, 0, 2.15f);

        Timer.schedule(() -> {
            Sound HinaAssignedSound = Core.settings.getBool("HinaVoiceEnable") ? HinaShootSound.random() : Sounds.shootBig;
            if (HinaAssignedSound != Sounds.shootBig) {
                UnitTypes.toxopid.weapons.get(0).soundPitchMin = 1f;
                UnitTypes.toxopid.weapons.get(1).soundPitchMin = 1f;
            } else {
                UnitTypes.toxopid.weapons.get(0).soundPitchMin = 0.8f;
                UnitTypes.toxopid.weapons.get(1).soundPitchMin = 0.8f;
            }
            UnitTypes.toxopid.weapons.get(0).shootSound = HinaAssignedSound;
            UnitTypes.toxopid.weapons.get(1).shootSound = HinaAssignedSound;
        }, 0, 0.5f);

        Timer.schedule(() -> {
            Sound HinaArtilleryAssignedSound = Core.settings.getBool("HinaVoiceEnable") ? HinaArtillerySound.random() : Sounds.shootBig;
            if (HinaArtilleryAssignedSound != Sounds.shootBig) {
                UnitTypes.toxopid.weapons.get(2).soundPitchMin = 1f;
            } else {
                UnitTypes.toxopid.weapons.get(2).soundPitchMin = 0.8f;
            }
            UnitTypes.toxopid.weapons.get(2).shootSound = HinaArtilleryAssignedSound;
        }, 0, 3.5f);

        Log.infoTag("ArchiveDustry", "Unit Sounds Loaded!");
    }
}