package bluearchive.units;

import arc.*;
import arc.audio.Sound;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Time;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.Vars;
import mindustry.gen.Unit;


public class UnitSound {
    public static Seq<Sound> arrivalSound = Seq.with();
    public static Seq<Sound> hitSound = Seq.with();
    static Interval hitInterval = new Interval(5);
    public static void init() {
        // collaris atlas start
        Events.on(PayloadDropEvent.class, e -> {
            if (e.unit.type == Vars.content.unit("collaris")) {
                arrivalSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-arrival1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-arrival2.ogg")));
                arrivalSound.random().play();
            }
        });
        Events.on(PickupEvent.class, e -> {
            if (e.unit.type == Vars.content.unit("collaris")) {
                Sound pickedSound = new Sound(Vars.tree.get("sounds/units/collaris-pickup.ogg"));
                pickedSound.play();
            }
        });
        Events.on(UnitDamageEvent.class, e -> {
            /* Check if the unit is same as intended, hit sound was being interval */
            if (e.unit.type == Vars.content.unit("collaris") && hitInterval.get(300)) {
                hitSound = Seq.with(new Sound(Vars.tree.get("sounds/units/collaris-hit1.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit2.ogg")), new Sound(Vars.tree.get("sounds/units/collaris-hit3.ogg")));
                if (!e.unit.dead) {
                    Time.run(0f, () -> {
                        hitSound.random().play();
                    });
                }
            }
        });
        UnitTypes.collaris.deathSound = new Sound(Vars.tree.get("sounds/units/collaris-death.ogg"));
        // collaris atlas end

        Log.infoTag("ArchiveDustry", "Unit Sounds Loaded!");
    }
}