package bluearchive.units;

import arc.Events;
import arc.util.Log;
import mindustry.content.*;
import mindustry.entities.part.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;


public class UnitHalo {
    public static void init(){
        Events.on(ClientLoadEvent.class, event -> {
            Log.info("Unit has been loaded!");
            UnitTypes.conquer.parts.addAll(
                    new ShapePart() {{
                        circle = true;
                        radius = 20;
                        hollow = true;
                        color = Pal.accentBack;
                        layer = Layer.effect;
                        stroke = 3;
                    }},
                    new HaloPart() {{
                        shapes = 4;
                        haloRotation = 45f;
                        haloRadius = 11f;
                        color = Pal.accentBack;
                        layer = Layer.effect;
                        radius = 3f;
                    }},
                    new HaloPart() {{
                        shapes = 4;
                        sides = 4;
                        haloRadius = 10f;
                        color = Pal.accentBack;
                        layer = Layer.effect;
                        radius = 3f;
                    }},
                    new ShapePart() {{
                        circle = true;
                        radius = 10;
                        hollow = true;
                        color = Pal.accentBack;
                        layer = Layer.effect;
                        stroke = 2;
                    }}
            );
            UnitTypes.corvus.parts.addAll(
                    new ShapePart() {{
                        radius = 15;
                        hollow = true;
                        rotation = 90;
                        color = Pal.heal;
                        layer = Layer.effect;
                        stroke = 3;
                        y = -3f;
                    }},
                    new HaloPart() {{
                        shapes = 3;
                        haloRotation = 60;
                        haloRadius = 15f;
                        color = Pal.heal;
                        layer = Layer.effect;
                        radius = 5f;
                        y = -3f;
                    }}
            );
            UnitTypes.toxopid.parts.addAll(
                    new HaloPart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        haloRotateSpeed = 3f;
                        shapes = 8;
                    }},
                    new ShapePart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        circle = true;
                        stroke = 3;
                        hollow = true;
                        radius = 15f;
                    }},
                    new HaloPart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        haloRadius = 18f;
                        radius = 3f;
                        shapes = 2;
                        haloRotation = 90;
                        triLength = 15f;
                        tri = true;
                    }},
                    new HaloPart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        haloRadius = 18f;
                        radius = 3f;
                        shapes = 2;
                        haloRotation = 90;
                        triLength = 1f;
                        shapeRotation = 180;
                        tri = true;
                    }},
                    new ShapePart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        sides = 8;
                        hollow = true;
                        stroke = 2;
                        radius = 9;
                    }}
            );
            UnitTypes.reign.parts.addAll(
                    new HaloPart() {{
                        haloRadius = 10f;
                        color = Pal.engine;
                        layer = Layer.effect;
                        tri = true;
                        triLength = 9f;
                        haloRotation = 45;
                        shapes = 4;
                        y = -5f;
                    }},
                    new ShapePart() {{
                        circle = true;
                        color = Pal.engine;
                        layer = Layer.effect;
                        hollow = true;
                        radius = 9f;
                        stroke = 2f;
                        y = -5f;
                    }},
                    new ShapePart() {{
                        radius = 5;
                        color = Pal.engine;
                        layer = Layer.effect;
                        hollow = true;
                        sides = 5;
                        stroke = 1.5f;
                        rotation = 90f;
                        y = -5f;
                    }},
                    new ShapePart() {{
                        color = Pal.engine;
                        layer = Layer.effect;
                        radius = 15;
                        hollow = true;
                        sides = 8;
                        stroke = 2;
                        rotation = 90;
                        y = -5f;

                    }}
            );
            UnitTypes.dagger.parts.addAll(
                    new ShapePart() {{
                    radius = 3;
                    color = Pal.engine;
                    layer = Layer.effect;
                    hollow = true;
                    sides = 5;
                    stroke = 1.5f;
                    rotation = 90f;
                    y = -2f;
            }});
            UnitTypes.crawler.parts.addAll(
                    new ShapePart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        sides = 8;
                        hollow = true;
                        stroke = 1.5f;
                        radius = 3;
                        y = -1f;
                    }});
            UnitTypes.nova.parts.addAll(
                    new ShapePart() {{
                        radius = 3;
                        hollow = true;
                        rotation = 90;
                        color = Pal.heal;
                        layer = Layer.effect;
                        stroke = 1f;
                        y = -2f;
                    }});
        });
    }

}
