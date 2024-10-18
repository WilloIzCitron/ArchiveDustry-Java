package bluearchive.ui;

import arc.scene.Element;
import mindustry.core.UI;
import mindustry.ui.*;

import arc.scene.style.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class ArchivDStyles extends Styles{

    public static void load(){
        var whiteui = (TextureRegionDrawable)Tex.whiteui;
        accentDrawable = whiteui.tint(Pal.techBlue);

        nonet.overFontColor = Pal.techBlue;
        emptyi.imageDownColor = Pal.techBlue;
        geni.imageDownColor = Pal.techBlue;
        defaultDialog.titleFontColor = Pal.techBlue;
        fullDialog.titleFontColor = Pal.techBlue;
        techLabel.fontColor = Pal.techBlue;

    }
}

