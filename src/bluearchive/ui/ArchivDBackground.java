package bluearchive.ui;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import arc.scene.*;
import arc.scene.ui.Image;
import arc.util.Scaling;
import arc.util.Time;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.game.EventType;

import static mindustry.Vars.state;

public class ArchivDBackground {
    static boolean Animran = false;
    public static TextureRegion img;
    static Image bg = new Image();
    public static void init(){
        Events.on(EventType.ClientLoadEvent.class, e -> {
        Element tmp = Vars.ui.menuGroup.getChildren().first();
        if (!(tmp instanceof Group group)) return;
        Element render = group.getChildren().first();
        if (!(render.getClass().isAnonymousClass()
                && render.getClass().getEnclosingClass() == Group.class
                && render.getClass().getSuperclass() == Element.class)) return;
        render.visible = false;


        bg.setFillParent(true);
            TextureRegion[] tex = new TextureRegion[68];
            Time.runTask(12f, () -> {
                group.addChildAt(0, bg);
                for (int i = 0; i<=67; i++){
                    tex[i] = Core.atlas.find("bluearchive-noa" + (1+i));
                }
                img = tex[0];
                Events.run(EventType.Trigger.update, () -> {
                    int animBGFrame = (int)((Time.globalTime / 3.5f) % tex.length);
                    img.set(tex[animBGFrame]);
                    bg.getRegion().set(img);
                });
        });
    });
    }
}