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
            UnitTypes.toxopid.parts.addAll(
                    new HaloPart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        haloRotateSpeed = 3f;
                        shapes = 8;
                        y = -5f;
                    }},
                    new ShapePart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        circle = true;
                        stroke = 3;
                        hollow = true;
                        radius = 15f;
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
                        radius = 9;
                        y = -5f;
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
                    stroke = 1f;
                    rotation = 90f;
                    y = -2f;
            }});
            UnitTypes.crawler.parts.addAll(
                    new ShapePart() {{
                        color = Pal.sapBullet;
                        layer = Layer.effect;
                        sides = 8;
                        hollow = true;
                        stroke = 1f;
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
                    }});
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
                UnitTypes.aegires.abilities
        });
    }

}