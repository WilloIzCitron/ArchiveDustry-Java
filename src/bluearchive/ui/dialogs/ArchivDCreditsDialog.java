package bluearchive.ui.dialogs;

import arc.Core;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;

public class ArchivDCreditsDialog extends BaseDialog {
    Table in = new Table();
    ScrollPane pane = new ScrollPane(in);
    public ArchivDCreditsDialog(){
        super("Credits");
        addCloseButton();
        cont.pane(new Table() {{
            center();
            image(Tex.clear).height(25).padTop(3f).row();
            image(Core.atlas.find("bluearchive-logo")).row();
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
            add("Blue Archive is copyrighted to Nexon, Nexon Games and Yostar. All Rights Reserved").row();
            image(Core.atlas.find("bluearchive-creditpart")).row();
            image(Tex.clear).height(25f).padTop(25f).row();
            //logo for
        }}).growX();
        show();
    }
}
