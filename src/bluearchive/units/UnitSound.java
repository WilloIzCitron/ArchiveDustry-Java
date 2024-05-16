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
    public static Seq<Sound> arrivalSound = Seq.with();
    public static Seq<Sound> hitSound = Seq.with();
    public static Sound arrivalAssignedSound;
    public static Seq<Sound> shootSound = Seq.with();
    public static Sound pickedSound = new Sound(Vars.tree.get("sounds/units/collaris-pickup.ogg"));
    static Interval interval = new Interval(5);
    public static void init() {
        // collaris atlas start
        Events.on(PayloadDropEvent.class, e -> {
            /* pass if the e.unit */
            if (e.unit != null) {
                /* pass if the e.unit is specified */
                if (e.unit.type == Vars.content.unit("collaris")) {
                    pickedSound.stop();
                    arrivalSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-arrival1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-arrival2.ogg")));
                    arrivalAssignedSound =  arrivalSound.random();
                    arrivalAssignedSound.play();
                }
            }
        });
        Events.on(PickupEvent.class, e -> {
            /* pass if the e.unit */
            if (e.unit != null) {
                /* pass if the e.unit is specified */
                if (e.unit.type == Vars.content.unit("collaris")) {
                    arrivalAssignedSound.stop();
                    pickedSound.play();
                }
            }
        });
        Events.on(UnitDamageEvent.class, e -> {
            /* Check if the unit is same as intended, hit sound was being interval */
            if (e.unit.type == Vars.content.unit("collaris") && interval.get(300)) {
                hitSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-hit1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit2.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit3.ogg")));
                if (!e.unit.dead) {
                    Time.run(0f, () -> {
                        hitSound.random().play();
                    });
                }
            }
        });
        Timer.schedule(() -> {
            UnitTypes.collaris.deathSound = new Sound(Vars.tree.get("sounds/units/collaris-death.ogg"));
            /* 4/1 chance to get unique sound */
            shootSound = Seq.with(Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, Sounds.pulseBlast, new Sound(Vars.tree.get("sounds/units/collaris-attack1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-attack2.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-attack3.ogg")));
            Sound assignedSound = shootSound.random();
            UnitTypes.collaris.weapons.get(0).shootSound = assignedSound;
            UnitTypes.collaris.weapons.get(1).shootSound = assignedSound;
            UnitTypes.collaris.weapons.get(0).soundPitchMin = 1f;
            UnitTypes.collaris.weapons.get(1).soundPitchMin = 1f;
        }, 0, 2.15f);
        // collaris atlas end

        Log.infoTag("ArchiveDustry", "Unit Sounds Loaded!");
    }
}