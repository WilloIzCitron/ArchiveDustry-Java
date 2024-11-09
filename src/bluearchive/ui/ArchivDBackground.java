package bluearchive.ui;

import arc.Core;
import arc.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.util.*;
import arc.util.serialization.*;
import mindustry.Vars;
import mindustry.game.EventType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import static mindustry.Vars.*;

public class ArchivDBackground implements Disposable {
    public static TextureRegion img;
    static Image animBG = new Image(new TextureRegion());
    private static float l2dImportProg;
    static boolean cancel = false;
    static final String version = "v1.4";
    protected static int animBGFrame;

    public static void buildL2D(String name, int frames, float framespeed){
        // Nullable, can be kill every mod with custom MenuRenderer
        AtomicReference<Float> speed = new AtomicReference<>(framespeed);
        Reflect.set(Vars.ui.menufrag, "renderer", null);
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
            final TextureAtlas.AtlasRegion[] region = new TextureAtlas.AtlasRegion[frames];
            for (int i = 0; i <= frames - 1; i++) {
                region[i] = Core.atlas.addRegion ("bluearchive-" + name + (1 + i), new TextureRegion (new Texture (Fi.get (dataDirectory + "/live2d/" + name + "/"+(1 + i) + ".png"))));
                tex[i] = region[i];
                if (i == frames - 1) {
                    img = tex[0];
                    break;
                }
            }
                group.addChildAt (0, animBG);
                Log.infoTag ("ArchiveDustry", "Background Loaded!");
                Events.run (EventType.Trigger.update, () -> {
                    if(!state.isMenu()){
                        speed.set(0f);
                        img.set(tex[0]);
                        return;
                    } else {
                        speed.set(framespeed);
                        animBGFrame = (int) ((Time.globalTime / speed.get()) % tex.length);
                        img.set(tex[animBGFrame]);
                    }
                    setRegion(animBG, img);
                });
            });
    }
    public static void downloadLive2D() {
        l2dImportProg = 0f;
        ui.loadfrag.show();
        ui.loadfrag.setText(Core.bundle.get("l2dDownload"));
        ui.loadfrag.setProgress(() -> l2dImportProg);
        ui.loadfrag.setButton(() -> {
            cancel = true;
            ui.loadfrag.hide();
        });
        if (!OS.is64Bit) {
            ui.showInfo(Core.bundle.get("unsupported32Bit"));
        } else {
            Http.get(ghApi + "/repos/WilloIzCitron/ArchiveDustryLive2DRepo/releases/latest", res -> {
                var json = Jval.read(res.getResultAsString());
                var value = json.get("assets").asArray().find(v -> v.getString("name", "").startsWith("ArchivDLive2D-" + version + ".zip"));
                var downloadZip = value.getString("browser_download_url");
                var dest = dataDirectory + "/live2dzip/";
                var toDest = dataDirectory + "/live2d/";
                download(downloadZip, new Fi(dest + "ArchivDLive2D-" + version + ".zip"), i -> l2dImportProg = i, () -> cancel, () -> {
                    ui.loadfrag.setText(Core.bundle.get("l2dInstall"));
                    unzip(dest + "ArchivDLive2D-" + version + ".zip", toDest);
                    ui.loadfrag.setText(Core.bundle.get("l2dComplete"));
                    ui.loadfrag.hide();
                    Vars.ui.showInfoFade(Core.bundle.get("l2dRestartRequired"));
                    Core.settings.put("live2dinstalled", true);
                    Core.settings.put("setSong", 3);
                    Fi.get(dest).deleteDirectory();
                });
            }, e -> ui.showException(e));
        }
    }
    private static void setRegion(Image img, TextureRegion reg) {
        img.getRegion().set(reg);
        img.clear();
    }

    private static void download(String furl, Fi dest, Floatc progressor, Boolp canceled, Runnable done) {
        mainExecutor.submit(() -> {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(furl).openConnection();
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                OutputStream out = dest.write(false, 4096);

                byte[] data = new byte[4096];
                long size = con.getContentLength();
                long counter = 0;
                int x;
                while ((x = in.read(data, 0, data.length)) >= 0 && !canceled.get()) {
                    counter += x;
                    progressor.get((float) counter / (float) size);
                    out.write(data, 0, x);
                }
                out.close();
                in.close();
                if (!canceled.get()) done.run();
            } catch (Throwable e) {
                ui.showException(e);
            }
        });
    }


    public static void unzip(String zipFile, String destFolder) {
        try {
            ZipFi zip = new ZipFi(Fi.get(zipFile));
            zip.walk(c -> {
                Fi newDir = new Fi(destFolder+ "/" + c.path());
                if(c.isDirectory()){
                    newDir.mkdirs();
                } else {
                    c.copyTo(newDir);
                }
            });
        } catch (Exception e) {
            ui.showException(e);
        }
    }
    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}
