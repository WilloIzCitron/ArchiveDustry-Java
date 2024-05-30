package bluearchive.units;

import arc.*;
import arc.audio.Sound;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.Vars;
import mindustry.gen.Sounds;


public class UnitSound {
    //Collaris Assets
    static Seq<Sound> CollarisArrivalSound = Seq.with();
    static Seq<Sound> CollarisHitSound = Seq.with();
    static Sound CollarisArrivalAssignedSound;
    static Seq<Sound> CollarisShootSound = Seq.with();
    static Sound CollarisPickedSound = new Sound(Vars.tree.get("sounds/units/collaris-pickup.ogg"));
    //Toxopid Assets
    static Seq<Sound> ToxopidArrivalSound = Seq.with();
    static Seq<Sound> ToxopidHitSound = Seq.with();
    static Sound ToxopidArrivalAssignedSound;
    static Seq<Sound> ToxopidShootSound = Seq.with();
    static Sound ToxopidPickedSound = new Sound(Vars.tree.get("sounds/units/toxopid-pickup.ogg"));
    // Interval
    static Interval interval = new Interval(5);
    public static void init() {
        Events.on(PayloadDropEvent.class, e -> {
            /* pass if the e.unit */
            if (e.unit != null) {
                /* pass if the e.unit is specified */
                if (e.unit.type == Vars.content.unit("collaris")) {
                    if(CollarisPickedSound != null) CollarisPickedSound.stop();
                    CollarisArrivalSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-arrival1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-arrival2.ogg")));
                    CollarisArrivalAssignedSound =  CollarisArrivalSound.random();
                    CollarisArrivalAssignedSound.play();
                }
                if (e.unit.type == Vars.content.unit("toxopid")) {
                    if(ToxopidPickedSound != null) ToxopidPickedSound.stop();
                    ToxopidArrivalSound = Seq.with(new Sound(Vars.tree.get("sounds/units/toxopid-arrival1.ogg")), new Sound(Vars.tree.get("sounds/units/toxopid-arrival2.ogg")));
                    ToxopidArrivalAssignedSound =  ToxopidArrivalSound.random();
                    ToxopidArrivalAssignedSound.play();
                }
            }
        });
        Events.on(PickupEvent.class, e -> {
            /* pass if the e.unit */
            if (e.unit != null) {
                /* pass if the e.unit is specified */
                if (e.unit.type == Vars.content.unit("collaris")) {
                    if (CollarisArrivalAssignedSound != null) CollarisArrivalAssignedSound.stop();
                    CollarisPickedSound.play();
                }
                if (e.unit.type == Vars.content.unit("toxopid")) {
                    if (ToxopidArrivalAssignedSound != null) ToxopidArrivalAssignedSound.stop();
                    ToxopidPickedSound.play();
                }
            }

        });
        Events.on(UnitDamageEvent.class, e -> {
            /* Check if the unit is same as intended, hit sound was being interval */
            if (e.unit.type == Vars.content.unit("collaris") && interval.get(300)) {
                CollarisHitSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-hit1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit2.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit3.ogg")));
                if (!e.unit.dead) {
                    Time.run(0f, () -> {
                        CollarisHitSound.random().play();
                    });
                }
            }
            if (e.unit.type == Vars.content.unit("toxopid") && interval.get(300)) {
                ToxopidHitSound = Seq.with(new Sound(Vars.tree.get("sounds/units/toxopid-hit1.ogg")), new Sound(Vars.tree.get("sounds/units/toxopid-hit2.ogg")), new Sound(Vars.tree.get("sounds/units/toxopid-hit3.ogg")));
                if (!e.unit.dead) {
                    Time.run(0f, () -> {
                        ToxopidHitSound.random().play();
                    });
                }
            }
        });
        Timer.schedule(() -> {
            UnitTypes.collaris.deathSound = new Sound(Vars.tree.get("sounds/units/collaris-death.ogg"));
            /* 4/1 chance to get unique sound */
            CollarisShootSound = Seq.with(Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, new Sound(Vars.tree.get("sounds/units/collaris-attack1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-attack2.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-attack3.ogg")));
            Sound CollarisAssignedSound = CollarisShootSound.random();
            if(CollarisAssignedSound != Sounds.pulseBlast) {
                UnitTypes.collaris.weapons.get(0).soundPitchMin = 1f;
                UnitTypes.collaris.weapons.get(1).soundPitchMin = 1f;
            } else {
                UnitTypes.collaris.weapons.get(0).soundPitchMin = 0.8f;
                UnitTypes.collaris.weapons.get(1).soundPitchMin = 0.8f;
            }
            UnitTypes.collaris.weapons.get(0).shootSound = CollarisAssignedSound;
            UnitTypes.collaris.weapons.get(1).shootSound = CollarisAssignedSound;
        }, 0, 2.15f);

        Timer.schedule(() -> {
            UnitTypes.toxopid.deathSound = new Sound(Vars.tree.get("sounds/units/toxopid-death.ogg"));
            /* 6/1 chance to get unique sound */
            ToxopidShootSound = Seq.with(Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, new Sound(Vars.tree.get("sounds/units/toxopid-attack1.ogg")), new Sound(Vars.tree.get("sounds/units/toxopid-attack2.ogg")), new Sound(Vars.tree.get("sounds/units/toxopid-attack3.ogg")));
            Sound ToxopidAssignedSound = ToxopidShootSound.random();
            if(ToxopidAssignedSound != Sounds.shootBig) {
                UnitTypes.toxopid.weapons.get(0).soundPitchMin = 1f;
                UnitTypes.toxopid.weapons.get(1).soundPitchMin = 1f;
            } else {
                UnitTypes.toxopid.weapons.get(0).soundPitchMin = 0.8f;
                UnitTypes.toxopid.weapons.get(1).soundPitchMin = 0.8f;
            }
            UnitTypes.toxopid.weapons.get(0).shootSound = ToxopidAssignedSound;
            UnitTypes.toxopid.weapons.get(1).shootSound = ToxopidAssignedSound;
         }, 0, 0.5f);

        Timer.schedule(() -> {
            /* 4/1 chance to get unique sound */
            Seq<Sound> ToxopidArtillerySound = Seq.with(Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, Sounds.shootBig, new Sound(Vars.tree.get("sounds/units/toxopid-artillery.ogg")));
            Sound ToxopidArtilleryAssignedSound = ToxopidArtillerySound.random();
            if(ToxopidArtilleryAssignedSound != Sounds.shootBig) {
                UnitTypes.toxopid.weapons.get(2).soundPitchMin = 1f;
            } else {
                UnitTypes.toxopid.weapons.get(2).soundPitchMin = 0.8f;
            }
            UnitTypes.toxopid.weapons.get(2).shootSound = ToxopidArtilleryAssignedSound;
        }, 0, 3.5f);

        Log.infoTag("ArchiveDustry", "Unit Sounds Loaded!");
    }
}