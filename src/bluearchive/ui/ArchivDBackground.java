package bluearchive.ui;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import arc.scene.*;
import arc.scene.ui.Image;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;

public class ArchivDBackground {
    public static TextureRegion img;
    static Image animBG = new Image(new TextureRegion());

    public static void buildL2D(String name, int frames){
        Element tmp = Vars.ui.menuGroup.getChildren().first();
        if (!(tmp instanceof Group group)) return;
        Element render = group.getChildren().first();
        if (!(render.getClass().isAnonymousClass()
                && render.getClass().getEnclosingClass() == Group.class
                && render.getClass().getSuperclass() == Element.class)) return;
        render.visible = false;

        Events.on(EventType.ClientLoadEvent.class, e -> {
            animBG.setFillParent(true);
            TextureRegion[] tex = new TextureRegion[frames];
            Time.runTask(12f, () -> {
                group.addChildAt(0, animBG);
                for (int i = 0; i<=frames-1; i++){
                    tex[i] = Core.atlas.find("bluearchive-" +name+ (1+i));
                }
                img = tex[0];
                Events.run(EventType.Trigger.update, () -> {
                    int animBGFrame = (int)((Time.globalTime / 3.5f) % tex.length);
                    img.set(tex[animBGFrame]);
                    setRegion(animBG, img);
                });
        });
    });
    }
    private static void setRegion(Image img, TextureRegion reg) {
        img.getRegion().set(reg);
    }
}
