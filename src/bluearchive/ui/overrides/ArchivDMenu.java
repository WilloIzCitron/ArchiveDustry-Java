package bluearchive.ui.overrides;

import arc.Core;
import arc.scene.Group;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.graphics.MenuRenderer;
import mindustry.ui.fragments.MenuFragment;

import static mindustry.Vars.ui;

public class ArchivDMenu extends MenuFragment {
    private Table container, submenu;
    private Button currentMenu;
    private MenuRenderer renderer;
    private Seq<MenuButton> customButtons = new Seq<>();
    public Seq<MenuButton> desktopButtons = null;

    public static void init() {
        ui.menufrag = new ArchivDMenu();
        ui.menufrag.build(Core.scene.root);
    }
    @Override
    public void build(Group parent) {
        renderer = new MenuRenderer();
        super.build(parent);
    }
}
