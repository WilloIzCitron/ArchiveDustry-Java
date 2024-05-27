package bluearchive.units;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.content.*;
import mindustry.entities.part.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;


public class UnitHalo {
    public static void init(){
        Events.on(ClientLoadEvent.class, event -> {
            // Serpulo

            UnitTypes.dagger.parts.addAll(
                new ShapePart() {{
                    radius = 3;
                    color = Pal.engine;
                    layer = Layer.effect;
                    hollow = true;
                    sides = 5;
                    stroke = 1f;
                    rotation = 90f;
                    y = -2f;
                }}
            );
            UnitTypes.mace.parts.addAll(
                new ShapePart() {{
                    radius = 3.5f;
                    color = Pal.engine;
                    layer = Layer.effect;
                    hollow = true;
                    sides = 5;
                    stroke = 1.3f;
                    rotation = 90f;
                    y = -3f;
                }},
                new HaloPart() {{
                    color = Pal.engine;
                    layer = Layer.effect;
                    radius = 1f;
                    tri = true;
                    triLength = 2f;
                    shapes = 4;
                    haloRadius = 3.8f;
                    y = -3f;
                }}
            );
            UnitTypes.fortress.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    hollow = true;
                    radius = 4.5f;
                    stroke = 1.5f;
                    y = -2.5f;
                    rotation = 90;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    y = -2.5f;
                    radius = 1.5f;
                    triLength = 3.5f;
                    tri = true;
                    shapes = 5;
                    haloRadius = 4.4f;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    y = -2.5f;
                    haloRotation = 180;
                    sides = 4;
                    shapes = 5;
                    radius = 1.5f;
                    haloRadius = 4.4f;
                }}
            );
            UnitTypes.scepter.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    hollow = true;
                    radius = 4.5f;
                    stroke = 1.5f;
                    y = -2.5f;
                    rotation = 90;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    triLength = 5;
                    radius = 1.3f;
                    haloRadius = 6.7f;
                    y = -2.5f;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    circle = true;
                    hollow = true;
                    radius = 6.5f;
                    stroke = 1.5f;
                    y = -2.5f;
                    rotation = 90;
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

            UnitTypes.nova.parts.addAll(
                new ShapePart() {{
                    radius = 3;
                    hollow = true;
                    rotation = 90;
                    color = Pal.heal;
                    layer = Layer.effect;
                    stroke = 1f;
                    y = -2f;
                }}
            );
            UnitTypes.pulsar.parts.addAll(
                new ShapePart() {{
                    radius = 3.5f;
                    hollow = true;
                    rotation = 90;
                    color = Pal.heal;
                    layer = Layer.effect;
                    stroke = 1.3f;
                    y = -3f;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 1.5f;
                    tri = true;
                    triLength = 3;
                    haloRotation = 180f;
                    haloRadius = 4.3f;
                    y = -3;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 1.5f;
                    tri = true;
                    triLength = 2;
                    haloRotation = 180f;
                    haloRadius = 4.3f;
                    shapeRotation = 180f;
                    y = -3f;
                }}
            );
            UnitTypes.quasar.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 3;
                    hollow = true;
                    rotation = 90;
                    stroke = 1.3f;
                    y = -3;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 1.3f;
                    hollow = false;
                    haloRadius = 5;
                    shapeRotation = 180;
                    haloRotation = 90;
                    shapes = 6;
                    y = -3;
                    triLength = 2;
                    tri = true;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 5.5f;
                    hollow = true;
                    rotation = 180;
                    stroke = 1.3f;
                    sides = 6;
                    y = -3;
                }}
            );
            UnitTypes.vela.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 9.5f;
                    y = -4;
                    rotation = 90;
                    hollow = true;
                    stroke = 1.7f;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 4f;
                    y = -4;
                    rotation = 90;
                    hollow = true;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 3.5f;
                    y = -4;
                    haloRotation = 180;
                    haloRadius = 6;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    y = -4;
                    radius = 3.5f;
                    haloRadius = 9.4f;
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
                    y = -5f;
                }},
                new HaloPart() {{
                    shapes = 3;
                    haloRotation = 60;
                    haloRadius = 15f;
                    color = Pal.heal;
                    layer = Layer.effect;
                    radius = 5f;
                    y = -5f;
                }}
            );

            UnitTypes.crawler.parts.addAll(
                new ShapePart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 8;
                    hollow = true;
                    stroke = 1f;
                    radius = 3;
                    y = -1f;
                }}
            );
            UnitTypes.atrax.parts.addAll(
                new ShapePart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 8;
                    hollow = true;
                    stroke = 1.3f;
                    radius = 3.5f;
                    y = -3f;
                }},
                new HaloPart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    shapes = 2;
                    radius = 1.5f;
                    y = -5f;
                    haloRotation = 90;
                    haloRadius = 3.8f;
                }}
            );
            UnitTypes.spiroct.parts.addAll(
                new ShapePart(){{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 8;
                    hollow = true;
                    stroke = 1.5f;
                    radius = 4.5f;
                    y = -4.5f;
                }},
                new HaloPart(){{
                    y = -4.5f;
                    haloRadius = 4.7f;
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                }}
            );
            UnitTypes.arkyid.parts.addAll(
                new HaloPart(){{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 3;
                    shapes = 8;
                    haloRadius = 7.7f;
                    radius = 2.5f;
                    y = -3.5f;
                }},
                new ShapePart(){{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 8;
                    hollow = true;
                    stroke = 2;
                    radius = 7.5f;
                    y = -3.5f;
                }}
            );
            UnitTypes.toxopid.parts.addAll(
                new HaloPart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    shapes = 16;
                    shapeRotation = 90;
                    radius = 3f;
                    haloRadius = 13f;
                    y = -5f;
                }},
                new ShapePart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 4;
                    stroke = 3;
                    radius = 5f;
                    y = -5f;
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
                    y = -5f;
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
                    y = -5f;
                }},
                new ShapePart() {{
                    color = Pal.sapBullet;
                    layer = Layer.effect;
                    sides = 8;
                    hollow = true;
                    stroke = 2;
                    radius = 12;
                    y = -5f;
                }}
            );

            // skip air units

            UnitTypes.risso.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    rotation = 90;
                    radius = 4;
                    hollow = true;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 3;
                    tri = true;
                    triLength = 5;
                    radius = 1.2f;
                    haloRadius = 1.5f;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 3;
                    tri = true;
                    triLength = 1;
                    radius = 1.2f;
                    haloRadius = 1.5f;
                    shapeRotation = 180;
                }}
            );
            UnitTypes.minke.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    rotation = 90;
                    radius = 4;
                    hollow = true;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 3;
                    tri = true;
                    triLength = 1;
                    shapes = 5;
                    radius = 2;
                    radius = 3f;
                    haloRadius = 1.5f;
                    shapeRotation = 180;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 16;
                    rotation = 90;
                    radius = 6;
                    hollow = true;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 3;
                    shapes = 5;
                    tri = true;
                    triLength = 5;
                    radius = 3f;
                    haloRadius = 1.5f;
                }}
            );
            UnitTypes.bryde.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    radius = 6;
                    hollow = true;
                    rotation = 90;
                    stroke = 1.5f;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    rotation = -90;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 1;
                    haloRotateSpeed= -1;
                    triLength = 0;
                    triLengthTo = 3;
                    shapeRotation = 180;
                    shapes = 5;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 1;
                    haloRotateSpeed= 1;
                    triLength = 0;
                    triLengthTo = 4;
                    shapeRotation = 0;
                    shapes = 5;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    circle = true;
                    radius = 10;
                    hollow = true;
                    rotation = 90;
                    stroke = 0;
                    strokeTo = 1;
                }}
            );
            UnitTypes.sei.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    radius = 6.5f;
                    hollow = true;
                    rotation = 90;
                    stroke = 1.5f;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    rotation = -90;
                    rotateSpeed = 2;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 1;
                    haloRotateSpeed= -1;
                    triLength = 0;
                    triLengthTo = 3;
                    shapeRotation = 180;
                    shapes = 8;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 1;
                    haloRotateSpeed= 1;
                    triLength = 0;
                    triLengthTo = 4;
                    shapeRotation = 0;
                    shapes = 8;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    circle = true;
                    radius = 10;
                    hollow = true;
                    rotation = 90;
                    stroke = 1;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 2f;
                    triLength = 3;
                    triLengthTo = 5;
                    shapeRotation = 0;
                    shapes = 3;
                    haloRadius = 15;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 2f;
                    triLength = 2;
                    triLengthTo = 3;
                    shapeRotation = 180;
                    shapes = 3;
                    haloRadius = 15;
                }}
            );
            UnitTypes.omura.parts.addAll(
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 8;
                    radius = 14f;
                    hollow = true;
                    rotation = 90;
                    stroke = 1.5f;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    rotation = -90;
                    rotateSpeed = 2;
                    radius = 2f;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    sides = 5;
                    radius = 5f;
                    hollow = true;
                    rotation = 90;
                    stroke = 1.5f;
                    rotateSpeed = -2;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 2;
                    haloRotateSpeed= -1;
                    triLength = 0;
                    triLengthTo = 3;
                    haloRadius = 9f;
                    shapeRotation = 180;
                    shapes = 8;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 1;
                    haloRotateSpeed= 1;
                    triLength = 0;
                    triLengthTo = 4;
                    shapeRotation = 0;
                    haloRadius = 9f;
                    shapes = 8;
                }},
                new ShapePart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    circle = true;
                    radius = 9f;
                    hollow = true;
                    rotation = 90;
                    stroke = 1;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 2f;
                    triLength = 5;
                    triLengthTo = 7;
                    shapeRotation = 0;
                    shapes = 5;
                    haloRadius = 19;
                }},
                new HaloPart(){{
                    color = Pal.engine;
                    layer = Layer.effect;
                    tri = true;
                    radius = 2f;
                    triLength = 2;
                    triLengthTo = 4;
                    shapeRotation = 180;
                    shapes = 5;
                    haloRadius = 19;
                }}
            );

            UnitTypes.retusa.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    radius = 4;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 180;
                    radius = 1;
                    shapes = 9;
                    triLength = 1;
                    tri = true;
                    haloRadius = 4;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 0;
                    radius = 1;
                    shapes = 9;
                    triLength = 3;
                    tri = true;
                    haloRadius = 4;
                }}
            );
            UnitTypes.oxynoe.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    radius = 4;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    circle = true;
                    radius = 6;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 180;
                    radius = 1;
                    shapes = 12;
                    triLength = 1;
                    tri = true;
                    haloRadius = 6;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 0;
                    radius = 1;
                    shapes = 12;
                    triLength = 3;
                    tri = true;
                    haloRadius = 6;
                }}
            );
            UnitTypes.cyerce.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = false;
                    rotation = 90;
                    radius = 2;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    rotateSpeed = 2f;
                    radius = 4;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 0;
                    rotateSpeed = -2f;
                    radius = 6;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    circle = true;
                    radius = 9;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 180;
                    radius = 4;
                    shapes = 3;
                    triLength = 3;
                    tri = true;
                    haloRadius = 11;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 0;
                    radius = 4;
                    shapes = 3;
                    triLength = 5f;
                    tri = true;
                    haloRadius = 11;
                }}
            );
            UnitTypes.aegires.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    circle = true;
                    radius = 9;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 180;
                    radius = 3;
                    shapes = 4;
                    triLength = 4;
                    tri = true;
                    haloRadius = 9;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 0;
                    radius = 3;
                    shapes = 4;
                    triLength = 7f;
                    tri = true;
                    haloRadius = 9;
                }}
            );
            UnitTypes.navanax.parts.addAll(
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    rotateSpeed = -2f;
                    radius = 8;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = false;
                    rotation = 90;
                    radius = 6;
                    rotateSpeed = 2f;
                    stroke = 4;
                }},
                new ShapePart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    hollow = true;
                    rotation = 90;
                    circle = true;
                    radius = 14;
                    stroke = 2;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 180;
                    radius = 3;
                    shapes = 10;
                    triLength = 4;
                    tri = true;
                    haloRadius = 15;
                }},
                new HaloPart(){{
                    color = Pal.heal;
                    layer = Layer.effect;
                    shapeRotation = 0;
                    radius = 1.5f;
                    shapes = 10;
                    triLength = 7;
                    tri = true;
                    haloRadius = 15;
                }}
            );

            // Erekir

            UnitTypes.stell.parts.addAll(
                    new ShapePart(){{
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 3f;
                        hollow = true;
                        circle = true;
                        stroke = 1.2f;
                    }}
            );

            UnitTypes.locus.parts.addAll(
                    new ShapePart(){{
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 5f;
                        hollow = true;
                        circle = true;
                        stroke = 1.2f;
                    }},
                    new HaloPart() {{
                        shapes = 2;
                        haloRotation = 0f;
                        haloRadius = 4.9f;
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        sides = 4;
                        radius = 3f;
                    }}
            );

            UnitTypes.precept.parts.addAll(
                    new ShapePart(){{
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 6f;
                        hollow = true;
                        circle = true;
                        stroke = 1.5f;
                    }},
                    new HaloPart() {{
                        shapes = 3;
                        haloRotation = 0f;
                        haloRadius = 5.9f;
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        sides = 4;
                        radius = 3f;
                    }}
            );

            UnitTypes.vanquish.parts.addAll(
                    new ShapePart(){{
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 8f;
                        hollow = true;
                        circle = true;
                        stroke = 1.5f;
                    }},
                    new HaloPart() {{
                        shapes = 4;
                        haloRotation = 0f;
                        haloRadius = 6.9f;
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        sides = 4;
                        radius = 3f;
                    }},
                    new ShapePart(){{
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 12f;
                        hollow = true;
                        circle = true;
                        stroke = 1.2f;
                    }}
            );
            // iroha
            UnitTypes.conquer.parts.addAll(
                new HaloPart() {{
                    shapes = 1;
                    tri = true;
                    triLength = 7.7f;
                    haloRadius = 13f;
                    color = Color.valueOf("feb380");
                    layer = Layer.effect;
                    radius = 7f;
                    haloRotation = 0f;
                }},
                    new HaloPart() {{
                        shapes = 1;
                        tri = true;
                        triLength = 7.7f;
                        haloRadius = 13.4f;
                        shapeRotation = 180f;
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 7f;
                        haloRotation = 0f;
                    }},
                new HaloPart() {{
                    shapes = 1;
                    tri = true;
                    triLength = 7.7f;
                    haloRadius = 12.7f;
                    haloRotation = 45f;
                    color = Color.valueOf("feb380");
                    layer = Layer.effect;
                    radius = 5f;
                    mirror = true;
                }},
                    new HaloPart() {{
                        shapes = 1;
                        tri = true;
                        triLength = 7.7f;
                        haloRadius = 14.7f;
                        haloRotation = 135f;
                        color = Color.valueOf("feb380");
                        layer = Layer.effect;
                        radius = 5f;
                        mirror = true;
                    }},
                new ShapePart() {{
                    circle = true;
                    radius = 13;
                    hollow = true;
                    color = Color.valueOf("feb380");
                    layer = Layer.effect;
                    stroke = 1.5f;
                }}
            );

            UnitTypes.merui.parts.addAll(
                    new ShapePart(){{
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        radius = 3f;
                        hollow = true;
                        stroke = 1.2f;
                        rotation = 45f;
                        sides = 4;
                        y = -1f;
                    }}
            );

            UnitTypes.cleroi.parts.addAll(
                    new ShapePart(){{
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        radius = 3f;
                        hollow = true;
                        stroke = 1.2f;
                        rotation = 0f;
                        sides = 4;
                        y = -2f;
                    }},
            new ShapePart(){{
                color = Pal.techBlue;
                rotation = 45f;
                layer = Layer.effect;
                radius = 6f;
                hollow = true;
                stroke = 1.2f;
                sides = 4;
                y = -2f;
            }}
            );

            UnitTypes.anthicus.parts.addAll(
                    new ShapePart(){{
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        radius = 2f;
                        rotateSpeed = 3f;
                        sides = 6;
                        y = -3f;
                    }},
                    new ShapePart(){{
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        radius = 4f;
                        hollow = true;
                        stroke = 1.2f;
                        rotation = 0f;
                        sides = 4;
                        y = -3f;
                    }},
                    new ShapePart(){{
                        color = Pal.techBlue;
                        rotation = 45f;
                        layer = Layer.effect;
                        radius = 8f;
                        hollow = true;
                        stroke = 1.5f;
                        sides = 4;
                        y = -3f;
                    }},
            new ShapePart(){{
                color = Pal.techBlue;
                rotation = 0f;
                layer = Layer.effect;
                radius = 8f;
                hollow = true;
                stroke = 1.5f;
                sides = 4;
                y = -3f;
            }}
            );

            //kether
            UnitTypes.tecta.parts.addAll(
                    new RegionPart(){{
                        name = "bluearchive-tecta";
                        suffix = "-halo";
                        regions = new TextureRegion[]{Core.atlas.find("bluearchive-tecta-halo"), Core.atlas.find("bluearchive-tecta-halo")};
                        heat = Core.atlas.find("bluearchive-tecta-halo-heat");
                        y = -6f;
                        xScl = 1.5f;
                        yScl = 1.5f;
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        outline = false;
                        drawRegion = true;
                    }}
            );

            //arisu
            UnitTypes.collaris.parts.addAll(
                    new RegionPart(){{
                        name = "bluearchive-collaris";
                        suffix = "-halo";
                        regions = new TextureRegion[]{Core.atlas.find("bluearchive-collaris-halo"), Core.atlas.find("bluearchive-collaris-halo")};
                        heat = Core.atlas.find("bluearchive-collaris-halo-heat");
                        y = -7f;
                        xScl = 2.5f;
                        yScl = 2.5f;
                        color = Pal.techBlue;
                        layer = Layer.effect;
                        outline = false;
                        drawRegion = true;
                    }}
            );

            // skip air units

            Log.infoTag("ArchiveDustry", "Unit halos have been loaded!");
        });
    }

}