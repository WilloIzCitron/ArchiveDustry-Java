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
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.fragments.*;
import arc.math.*;

import static mindustry.Vars.*;

public class ArchivDLoadingFragment extends LoadingFragment {
    private Table table;
    private static TextButton button;
    private Bar bar;
    private static Label nameLabel;
    private float progValue;
    private Label tooltipTitle;
    private Label tooltipInfo;
    public static boolean loadFragShow;
    private static Table tabl;

    public static void init() {
        ui.loadfrag = new ArchivDLoadingFragment();
        ui.loadfrag.build(Core.scene.root);
    }

    public void build(Group parent){
        if(tabl == null) {
            tabl = new Table(table -> {
                nameLabel = table.add("@loading").color(Pal.techBlue).pad(10f).style(Styles.techLabel).left().get();
                table.image(Tex.clear).growX();
                button = table.button("@cancel", () -> {
                }).size(250f, 50f).visible(false).right().get();
                updateLabel(false, "@loading");
            });
        }
        parent.fill(t -> {
            //rect must fill screen completely.
            t.rect((x, y, w, h) -> {
                Draw.alpha(t.color.a);
                Styles.black8.draw(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
            });
            t.visible = false;
            t.touchable = Touchable.enabled;
            t.add().growY();
            t.row();
            t.table(a -> {
                tooltipTitle = a.add("Lorem Ipsum").color(Pal.techBlue).fontScale(1.5f).style(Styles.techLabel).pad(10).left().get();
                a.row();
                tooltipInfo = a.add("Line1\nline2\nline3\n").pad(10).left().get();
            }).left().row();
            t.add().growY();
            t.row();
            if(tabl != null) t.add(tabl).growX();
            t.row();
            t.table(a -> {
                a.add(new WarningBar()).color(Pal.techBlue).growX().height(24f).row();
                bar = a.add(new Bar()).pad(3).padTop(6).height(40f).growX().visible(false).get();
            }).bottom().growX().row();
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
        loadFragShow = true;
    }

    public void hide(){
        table.clearActions();
        table.toFront();
        table.touchable = Touchable.disabled;
        table.actions(Actions.fadeOut(0.5f), Actions.visible(false));
        tabl = null;
        loadFragShow = false;
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

