package bluearchive.expansions.exoprosopa.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.Vars;
import mindustry.ctype.*;
import mindustry.entities.part.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class ExopUnitHalo {
    public static void load() {
        //tanks
        UnitType mason = Vars.content.getByName(ContentType.unit, "exoprosopa-15o-01-mason");
        UnitType oktarav = Vars.content.getByName(ContentType.unit, "exoprosopa-15o-02-oktarav");
        UnitType vicient = Vars.content.getByName(ContentType.unit, "exoprosopa-15o-03-vicient");
        UnitType siphon = Vars.content.getByName(ContentType.unit, "exoprosopa-15o-04-siphon");
        UnitType rancor = Vars.content.getByName(ContentType.unit, "exoprosopa-15o-05-rancor");
        mason.parts.addAll(
                new ShapePart(){{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 3f;
                    hollow = true;
                    circle = false;
                    sides = 6;
                    stroke = 1.2f;
                    rotation = 90;
                }}
        );
        oktarav.parts.addAll(
                new ShapePart(){{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 5f;
                    hollow = true;
                    circle = false;
                    sides = 6;
                    stroke = 1.2f;
                    rotation = 90;
                }},
                new HaloPart(){{
                    color = Color.valueOf("93e2ee");
                    haloRadius = 6f;
                    radius = 2f;
                    sides = 4;
                    layer = Layer.effect;
                    shapes = 6;
                    haloRotation = 0;
                }}
        );
        vicient.parts.addAll(
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 7f;
                    hollow = true;
                    circle = false;
                    sides = 6;
                    stroke = 2f;
                    rotation = 90;
                }},
                new DrawPart() {
                    @Override
                    public void draw(PartParams partParams) {
                        Lines.stroke(2f, Color.valueOf("93e2ee"));
                        Draw.z(Layer.effect);
                        Lines.spikes(partParams.x, partParams.y, 8, 3, 6, partParams.rotation);
                    }

                    @Override
                    public void load(String s) {

                    }
                }
        );
        siphon.parts.addAll(
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 6f;
                    hollow = true;
                    circle = true;
                    stroke = 1.5f;
                    rotation = 90;
                }},
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 10f;
                    hollow = true;
                    circle = true;
                    stroke = 1.7f;
                    rotation = 90;
                }},
                new DrawPart() {
                    @Override
                    public void draw(PartParams partParams) {
                        Lines.stroke(3f, Color.valueOf("93e2ee"));
                        Draw.z(Layer.effect);
                        Lines.spikes(partParams.x, partParams.y, 11, 3f, 8, partParams.rotation);
                    }

                    @Override
                    public void load(String s) {

                    }
                },
                new HaloPart(){{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 6;
                    shapes = 8;
                    tri = true;
                    triLength = 5;
                    stroke = 3;
                    haloRadius = 10;
                }}
        );

    }
}
