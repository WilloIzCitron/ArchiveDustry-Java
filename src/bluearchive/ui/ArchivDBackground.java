package bluearchive.ui;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.util.*;
import arc.util.serialization.*;
import bluearchive.ArchiveDustry;
import bluearchive.l2d.Live2DBackgrounds;
import mindustry.game.EventType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static bluearchive.ui.overrides.ArchivDLoadingFragment.*;
import static mindustry.Vars.*;

public class ArchivDBackground implements Disposable {
    private static float l2dImportProg;
    static boolean cancel = false;
    static final String version = "v1.5";
    static TextureRegion frame = new TextureRegion();
    static Image animBG = new Image(frame);

    public static void buildL2D(String name) {
        // Nullable, can kill every mod with custom MenuRenderer
        try {
            if(!headless) {
                Live2DBackgrounds.LoadedL2D l2dLoaded = Live2DBackgrounds.getL2D(name);
                if (l2dLoaded.isSoundTrackLocal) {
                    ArchiveDustry.recollectionMusic = tree.loadMusic(l2dLoaded.localSoundTrack);
                } else {
                    ArchiveDustry.recollectionMusic = l2dLoaded.soundTrack;
                }
                Reflect.set(ui.menufrag, "renderer", null);
                Element tmp = ui.menuGroup.getChildren().first();
                if (!(tmp instanceof Group group)) return;
                Element render = group.getChildren().first();
                if (!(render.getClass().isAnonymousClass()
                        && render.getClass().getEnclosingClass() == Group.class
                        && render.getClass().getSuperclass() == Element.class)) return;
                render.visible = false;

                Events.on(EventType.ClientLoadEvent.class, e -> {
                    animBG.setFillParent(true);
                    group.addChildAt(0, animBG);
                    Log.infoTag("ArchiveDustry", "Background Loaded!");
                    Timer timer = Timer.instance();
                    Timer.Task task = new Timer.Task() {
                        @Override
                        public void run() {
                            //Log.infoTag("ArchivDebug", "Background Running....");
                            if(!state.isMenu()) this.cancel();
                            frame.set(l2dLoaded.loadedL2ds.get((int) (Time.globalTime / l2dLoaded.frameSpeed) % l2dLoaded.loadedL2ds.size));
                            animBG.getRegion().set(frame);
                        }
                    };
                    Events.run(EventType.Trigger.update, () -> {
                        if (!state.isMenu()) {
                            //failsafe if it is still running in
                            //Log.infoTag("ArchivDebug", "Background stopped for real");
                            return;
                        }
                        if(state.isMenu() & timer.isEmpty() || !task.isScheduled()) timer.scheduleTask(task, 0, 0.001f);
                    });
                    timer.scheduleTask(task, 0, 0.001f);
                });
            } else {
                Log.infoTag("ArchiveDustry", "Headless detected! Background loading skipped.");
            }
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
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
                ui.showInfoFade(Core.bundle.get("l2dRestartRequired"));
                Core.settings.put("live2dinstalled", true);
                Fi.get(dest).deleteDirectory();
            });
        }, e -> ui.showException(e));
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
        if (animBG.getRegion() != null && animBG.getRegion().texture != null) {
            animBG.getRegion().texture.getTextureData().disposePixmap();
            animBG.getRegion().texture.dispose();
        }
    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}