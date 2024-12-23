package bluearchive.ui.overrides;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.Nullable;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.fragments.*;
import arc.math.*;

import static mindustry.Vars.*;

public class ArchivDLoadingFragment extends LoadingFragment {
    private Table table;
    private TextButton button;
    private Bar bar;
    private Label nameLabel;
    private float progValue;
    private Label tooltipTitle;
    private Label tooltipInfo;
    private static float scale;

    public static void init() {
        ui.loadfrag = new ArchivDLoadingFragment();
        ui.loadfrag.build(Core.scene.root);
        if(!mobile){
            scale = 45f;
        } else {
            scale = 25f;
        }
    }

    public void build(Group parent){
        parent.fill(t -> {
            //rect must fill screen completely.
            t.rect((x, y, w, h) -> {
                Draw.alpha(t.color.a);
                Styles.black8.draw(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
            });
            t.visible = false;
            t.touchable = Touchable.enabled;
            t.add().height((Core.graphics.getHeight())/2f).row();
            t.row();
            tooltipTitle = t.add("Lorem Ipsum").color(Pal.techBlue).fontScale(1.5f).style(Styles.techLabel).pad(10).left().get();
            t.row();
            tooltipInfo = t.add("Line1\nline2\nline3\n").pad(10).left().get();
            t.row();
            t.add().height((Core.graphics.getHeight() - scale)/2f).row();
            t.row();
            nameLabel = t.add("@loading").color(Pal.techBlue).pad(10f).style(Styles.techLabel).left().get();
            t.row();
            t.add(new WarningBar()).color(Pal.techBlue).growX().height(24f);
            t.row();
            updateLabel(false,"@loading");
            bar = t.add(new Bar()).pad(3).padTop(6).height(40f).growX().visible(false).get();
            t.row();
            button = t.button("@cancel", () -> {
            }).size(250f, 50f).visible(false).get();
            table = t;
        });
    }

    public void toFront(){
        table.toFront();
    }

    public void setProgress(Floatp progress){
        bar.reset(0f);
        bar.visible = true;
        bar.set(() -> ((int)(progress.get() * 100) + "%"), progress, Pal.techBlue);
    }

    public void snapProgress(){
        bar.snap();
    }

    public void setProgress(float progress){
        progValue = progress;
        if(!bar.visible){
            setProgress(() -> progValue);
        }
    }

    public void setButton(Runnable listener){
        button.visible = true;
        button.getListeners().remove(button.getListeners().size - 1);
        button.clicked(listener);
    }

    public void setText(String text){
        updateLabel(false, text);
        nameLabel.setColor(Pal.accent);
    }

    public void show(){
        tree.loadSound("chatMessage").play();
        show("@loading");
    }

    public void show(String text){
        tree.loadSound("chatMessage").play();
        button.visible = false;
        nameLabel.setColor(Color.white);
        tooltipTitle.setColor(Color.white);
        bar.visible = false;
        table.clearActions();
        table.touchable = Touchable.enabled;
        updateLabel(false, text);
        updateLabel(true, null);
        table.visible = true;
        table.color.a = 1f;
        table.toFront();
    }

    public void hide(){
        table.clearActions();
        table.toFront();
        table.touchable = Touchable.disabled;
        table.actions(Actions.fadeOut(0.5f), Actions.visible(false));
    }

    private void updateLabel(boolean isTooltip, @Nullable String text) {
        Label label = isTooltip ? tooltipTitle : nameLabel;
        label.setText(text);
        label.setColor(Pal.techBlue);

        CharSequence realText = label.getText();
        for (int i = 0; i < realText.length(); i++) {
            if (Fonts.tech.getData().getGlyph(realText.charAt(i)) == null) {
                label.setStyle(Styles.defaultLabel);
                return;
            }
        }
        label.setStyle(Styles.techLabel);

        if (isTooltip) {
            int randomNum = Mathf.random(7);
            tooltipTitle.setText(Core.bundle.get("tooltipTitle-" + randomNum));
            tooltipInfo.setText(Core.bundle.get("tooltipInfo-" + randomNum));
        }
    }
}

