package bluearchive.ui.dialogs;

import arc.Core;
import arc.input.KeyCode;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import bluearchive.ArchivDMusic;
import mindustry.gen.*;
import mindustry.ui.*;

import static bluearchive.ArchiveDustry.soundControlPlaying; //inherited
import static mindustry.Vars.*;

public class ArchivDCreditsDialog extends Dialog {
    final Image modLogo = new Image(new TextureRegionDrawable(Core.atlas.find("bluearchive-logo")), Scaling.fit);
    final Image nekoUILogo = new Image(new TextureRegionDrawable(Core.atlas.find("bluearchive-nekoui")), Scaling.fit);
    final Image nexonLogo = new Image(new TextureRegionDrawable(Core.atlas.find("bluearchive-creditpart")), Scaling.fit);
    Table in = new Table(){{
        center();
        add(modLogo).size(768, 153).row();
        image(Tex.clear).height(25f).padTop(3f).row();
        add("Developed by").row();
        image(Tex.clear).height(5f).padTop(3f).row();
        add("WilloIzCitron").row();
        image(Tex.clear).height(25f).padTop(3f).row();
        image(Tex.clear).height(5f).padTop(3f).row();
        add("Music by").row();
        add("Nor").row();
        add("Mitsukiyo").row();
        add("KARUT").row();
        image(Tex.clear).height(25f).padTop(3f).row();
        add("Music list").row();
        image(Tex.clear).height(5f).padTop(3f).row();
        add("menu.ogg = Constant Moderato & RE Aoharu").row();
        add("launch.ogg = Shooting Stars").row();
        add("game1.ogg = Rolling Beat").row();
        add("game2.ogg = Acceleration").row();
        add("game3.ogg = KIRISAME").row();
        add("game4.ogg = Midnight Trip").row();
        add("game6.ogg = Formless Dream").row();
        add("game7.ogg = Vivid Night").row();
        add("game8.ogg = Crucial Issue").row();
        add("game9.ogg = KARAKURhythm").row();
        add("editor.ogg = Mischievous Step").row();
        add("land.ogg = Aoharu (Intro Sampling)").row();
        add("boss1.ogg = Endless Carnival").row();
        add("boss2.ogg = Out of Control").row();
        add("fine.ogg = Alkaline Tears").row();
        image(Tex.clear).height(10f).padTop(3f).row();
        add("lose.ogg (additional) = Fade Out").row();
        add("win.ogg (additional) = Party Time").row();
        add("research.ogg (additional) = Future Lab").row();
        add("database.ogg (additional) = Future Bossa").row();
        add("loadout.ogg (additional) = MX Adventure").row();
        image(Tex.clear).height(25f).padTop(3f).row();
        add("Mod Contributor and Translator").row();
        image(Tex.clear).height(5f).padTop(3f).row();
        add("VDGaster").row();
        image(Tex.clear).height(25f).padTop(3f).row();
        add("Legal Notice").row();
        image(Tex.clear).height(5f).padTop(3f).row();
        add("This is a fanmade mod! it obeys the Fankit Guidelines.").row();
        add("THIS MOD IS NOT INTENDED FOR COMMERCIAL USE!").row();
        image(Tex.clear).height(2f).padTop(3f).row();
        image(Tex.clear).height(1f).padTop(3f).row();
        add("Mindustry is developed by Anuke, and is licensed under GNU GPLv3.0").row();
        image(Tex.clear).height(1f).padTop(3f).row();
        add("This mod is MIT Licensed").row();
        image(Tex.clear).height(25f).padTop(25f).row();
        add("Special Thanks for: NekoUI by Hans404 and BetMC by BetSoi2411").row();
        add(nekoUILogo).size(420f/2,185f/2).row();
        image(Tex.clear).height(25f).padTop(25f).row();
        add("Blue Archive is copyrighted to Nexon, Nexon Games and Yostar. All Rights Reserved").row();
        add(nexonLogo).size(522, 82).row();
        image(Tex.clear).height(1280).padTop(25f).row();
        //logo for
    }};
    float scrollBar;
    float tableHeight = in.getHeight();
    float halfTableHeight = tableHeight / 2;
    DialogStyle creditDialog = new DialogStyle(){{
        background = Styles.none;
    }};
    public ArchivDCreditsDialog(){
        super();
        //addCloseButton();
        scrollBar = 0f;

        ArchivDMusic.re_aoh.play();
        show();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(tableHeight <= 0) {
            tableHeight = in.getHeight();
            halfTableHeight = tableHeight / 2;
        }

        scrollBar += 0.5f * Time.delta;
        cont.clearChildren();
        in.update(()-> setTranslation(0f, scrollBar - (halfTableHeight + Core.camera.height)));
        cont.add(in).align(Align.bottom);
        setStyle(creditDialog);
        this.update(() -> {
            if (state.isMenu() || ui.planet.isShown() || ui.editor.isShown() || state.rules.editor) {
                control.sound.stop();
                if (soundControlPlaying() != null) {
                    control.sound.stop();
                } //Counteract fade in
            }
        });
        this.hidden(() -> ArchivDMusic.re_aoh.stop());
        if(((scrollBar > (halfTableHeight * 2f)) && tableHeight > 0) || Core.input.keyDown(KeyCode.escape) || Core.input.isTouched()) {
            this.hide();
        }
    }

    @Override
    public void draw() {
        Styles.black.draw(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        super.draw();
    }
}

