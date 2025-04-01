package bluearchive.expansions.exoprosopa.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import arc.util.Time;
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
        //legs
        UnitType ares = Vars.content.getByName(ContentType.unit, "exoprosopa-16p-01-ares");
        UnitType rhitle = Vars.content.getByName(ContentType.unit, "exoprosopa-16p-02-rhitle");
        UnitType sender = Vars.content.getByName(ContentType.unit, "exoprosopa-16p-03-sender");
        UnitType carranger = Vars.content.getByName(ContentType.unit, "exoprosopa-16p-04-carragher");
        UnitType xenoct = Vars.content.getByName(ContentType.unit, "exoprosopa-16p-05-xenoct");

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
        rancor.parts.addAll(
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 9f;
                    hollow = true;
                    circle = false;
                    sides = 6;
                    stroke = 2f;
                    rotation = 90;
                }},
                new HaloPart(){{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 6;
                    shapes = 8;
                    tri = true;
                    triLength = 5;
                    stroke = 3;
                    haloRadius = 15;
                    haloRotateSpeed = 1;
                }},
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 15f;
                    hollow = true;
                    circle = true;
                    stroke = 1.7f;
                    rotateSpeed = 1;
                }},
                new DrawPart() {
                    @Override
                    public void draw(PartParams partParams) {
                        Lines.stroke(2f, Color.valueOf("93e2ee"));
                        Draw.z(Layer.effect);
                        Lines.spikes(partParams.x, partParams.y, 10, 3, 6, partParams.rotation);
                        Lines.stroke(2f, Color.valueOf("93e2ee"));
                        Lines.lineAngle(partParams.x, partParams.y, partParams.rotation + partParams.smoothReload * 360, 5);
                        Lines.lineAngle(partParams.x, partParams.y, partParams.rotation + (Time.time/360), 3);
                        Lines.stroke(3f, Color.valueOf("93e2ee"));
                        Lines.spikes(partParams.x, partParams.y, 16, 3f, 8, (partParams.rotation + 1 * Time.time));
                    }

                    @Override
                    public void load(String s) {

                    }
                },
                new ShapePart() {{
                    color = Color.valueOf("93e2ee");
                    layer = Layer.effect;
                    radius = 10f;
                    hollow = true;
                    circle = true;
                    stroke = 1.7f;
                    rotation = 90;
                }}
        );
        ares.parts.add(
                new ShapePart(){{
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                    radius = 4f;
                    hollow = true;
                    circle = true;
                    stroke = 1.7f;
                    y = -2f;
                    rotation = 90;
                }}
        );
        rhitle.parts.add(
                new FlarePart(){{
                    color1 = Color.valueOf("d45050");
                    sides = 5;
                    followRotation = true;
                    radius = 8f;
                    y = -2f;
                    stroke = 4f;
                }},
        new ShapePart(){{
            color = Color.valueOf("d45050");
            layer = Layer.effect;
            radius = 6f;
            hollow = true;
            circle = true;
            stroke = 1.7f;
            rotation = 90;
            y = -2f;
        }}
        );
        sender.parts.addAll(
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.2f;
                    radius = 6;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.7f;
                    radius = 9;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 45;
                    triLength = 9;
                    tri = true;
                    shapes = 4;
                    radius = 2f;
                    haloRadius = 7.5f;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 45;
                    shapeRotation = 180;
                    triLength = 3;
                    tri = true;
                    shapes = 4;
                    radius = 2f;
                    haloRadius = 7.5f;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
        new HaloPart(){{
            haloRotation = 45;
            shapeRotation = 180;
            triLength = 6;
            tri = true;
            shapes = 4;
            radius = 1.5f;
            haloRadius = 9;
            y = -4;
            color = Color.valueOf("d45050");
            layer = Layer.effect;
        }}
        );
        carranger.parts.addAll(
                new FlarePart(){{
                    color1 = Color.valueOf("d45050");
                    sides = 4;
                    followRotation = true;
                    radius = 8f;
                    y = -4f;
                    stroke = 4f;
                }},
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.7f;
                    radius = 7;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.7f;
                    radius = 11;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 45;
                    triLength = 8;
                    tri = true;
                    shapes = 4;
                    radius = 1.5f;
                    haloRadius = 10;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 90;
                    shapeRotation = 180;
                    tri = true;
                    triLength = 4;
                    shapes = 6;
                    radius = 1.5f;
                    haloRadius = 10;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }}
        );
        xenoct.parts.addAll(
                new FlarePart(){{
                    color1 = Color.valueOf("d45050");
                    sides = 4;
                    followRotation = true;
                    radius = 12f;
                    y = -4f;
                    stroke = 2f;
                }},
                new FlarePart(){{
                    color1 = Color.valueOf("d45050");
                    sides = 8;
                    followRotation = true;
                    radius = 8f;
                    y = -4f;
                    stroke = 4f;
                }},
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.7f;
                    radius = 12;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new ShapePart(){{
                    hollow = true;
                    stroke = 1.7f;
                    radius = 16;
                    circle = true;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 45;
                    triLength = 15;
                    shapes = 4;
                    tri = true;
                    radius = 1.5f;
                    haloRadius = 15;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }},
                new HaloPart(){{
                    haloRotation = 90;
                    shapeRotation = 180;
                    triLength = 9;
                    tri = true;
                    shapes = 6;
                    radius = 1.5f;
                    haloRadius = 15;
                    y = -4;
                    color = Color.valueOf("d45050");
                    layer = Layer.effect;
                }}
        );
    }
}
