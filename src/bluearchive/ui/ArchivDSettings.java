package bluearchive.ui;

import arc.Core;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.utils.Elem;
import arc.util.OS;
import arc.util.Scaling;
import bluearchive.ArchiveDustry;
import bluearchive.ui.dialogs.ArchivDCreditsDialog;
import mindustry.gen.Icon;
import mindustry.gen.Musics;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static mindustry.Vars.tree;
import static mindustry.Vars.ui;

public class ArchivDSettings {

    public static void loadSettings(){
        ui.settings.addCategory("ArchiveDustry", t -> {
            t.pref(new Banner("bluearchive-logo", -1));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.general-setting")));
            t.pref(new Separator(4));
            if (Core.settings.getBool("enableL2D")) {
                t.sliderPref("setL2D", 2, 1, 5, 1, i -> Core.bundle.get("ba-l2d" + (int) i + ".name"));
                t.sliderPref("setSong", 1, 1, 3, 1, i -> {
                    switch (i) {
                        case 1:
                            Musics.menu = tree.loadMusic("menucm");
                            break;
                        case 2:
                            Musics.menu = tree.loadMusic("menure-aoh");
                            break;
                        case 3:
                            if (Core.settings.getBool("enableL2D")) {
                                Musics.menu = ArchiveDustry.recollectionMusic;
                            }
                            break;
                    }
                    return Core.bundle.get("ba-music" + (int) i + ".name");
                });
            } else {
                t.sliderPref("setSong", 1, 1, 2, 1, i -> {
                    switch (i) {
                        case 1:
                            Musics.menu = tree.loadMusic("menucm");
                            break;
                        case 2:
                            Musics.menu = tree.loadMusic("menure-aoh");
                            break;
                    }
                    return Core.bundle.get("ba-music" + (int) i + ".name");
                });
            }
            if(Core.settings.getBool("live2dinstalled", false)) {
                t.checkPref("enableL2D", false);
            }
            t.checkPref("ba-firstTime", true);
            t.checkPref("ba-addHalo", true);
            t.pref(new ButtonSetting("ba-downloadLive2D", Icon.download, ArchivDBackground::downloadLive2D, 32));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.links")));
            t.pref(new Separator(4));
            t.pref(new ButtonSetting("ba-youtube", Icon.play, () -> {
                Core.app.openURI("https://www.youtube.com/channel/UCsrnDYrkovQhCCE8kwKcvKQ");
            }, 32));
            t.pref(new ButtonSetting("ba-github", Icon.github, () -> {
                Core.app.openURI("https://www.github.com/willoizcitron/archivedustry-java");
            }, 32));
            t.pref(new ButtonSetting(Core.bundle.get("credits"), Icon.info, ArchivDCreditsDialog::new, 32));
            t.pref(new TextSeparator(Core.bundle.get("setting.category.mixer")));
            t.pref(new Separator(4));
            int defaultVolume = Core.settings.has("musicvol") ? Core.settings.getInt("musicvol") : (int) Core.settings.getDefault("musicvol");
            t.sliderPref("gameOver", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("win").setVolume(i / 100f);
                tree.loadMusic("lose").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("research", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("research").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("coreDatabase", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("database").setVolume(i / 100f);
                return i + "%";
            });
            t.sliderPref("loadout", defaultVolume,0, 100,1,i -> {
                tree.loadMusic("loadout").setVolume(i / 100f);
                return i + "%";
            });
            t.pref(new Separator(2));
            if (OS.username.startsWith("willoizcitron")){
                t.pref(new Separator(2));
                t.pref(new TextSeparator("< Developer Settings >"));
            };
        });
    }

    /** Not a setting, but rather a button in the settings menu. */
    static class ButtonSetting extends SettingsMenuDialog.SettingsTable.Setting {
        Drawable icon;
        Runnable listener;
        float iconSize;

        public ButtonSetting(String name, Drawable icon, Runnable listener, float iconSize){
            super(name);
            this.icon = icon;
            this.listener = listener;
            this.iconSize = iconSize;
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table){
            ImageButton b = Elem.newImageButton(icon, listener);
            b.resizeImage(iconSize);
            b.label(() -> title).padLeft(6).growX();
            b.left();

            addDesc(table.add(b).left().padTop(3f).get());
            table.row();
        }
    }

    static class TextSeparator extends SettingsMenuDialog.SettingsTable.Setting {
        String text;
        public TextSeparator(String text){
            super("");
            this.text = text;
        }
        @Override
        public void add(SettingsMenuDialog.SettingsTable table){
            TextField b = new TextField(text);
            b.setText(text);
            b.setDisabled(true);
            b.setAlignment(1);
            table.add(b).growX();
            table.row();
        }
    }
    static class Separator extends SettingsMenuDialog.SettingsTable.Setting {
        float height;
        public Separator(float height){
            super("");
            this.height = height;
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table){
            table.image(Tex.clear).height(height).padTop(3f);
            table.row();
        }
    }
    /** Not a setting, but rather adds an image to the settings menu. */
    static class Banner extends SettingsMenuDialog.SettingsTable.Setting {
        float width;

        public Banner(String name, float width){
            super(name);
            this.width = width;
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table){
            Image i = new Image(new TextureRegionDrawable(Core.atlas.find(name)), Scaling.fit);
            Cell<Image> ci = table.add(i).padTop(3f);

            if(width > 0){
                ci.width(width);
            }else{
                ci.grow();
            }

            table.row();
        }
    }
}
