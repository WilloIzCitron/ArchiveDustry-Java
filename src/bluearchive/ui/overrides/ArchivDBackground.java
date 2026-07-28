package bluearchive.ui.overrides;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.files.ZipFi;
import arc.func.Boolp;
import arc.func.Floatc;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.ui.Image;
import arc.util.*;
import arc.util.serialization.Jval;
import bluearchive.ArchiveDustry;
import bluearchive.l2d.Live2DBackgroundsExperimental;
import mindustry.game.EventType;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static mindustry.Vars.*;

public class ArchivDBackground implements Disposable {
    private static float l2dImportProg;
    static boolean cancel = false;
    static final String version = "v1.5";
    static TextureRegion frame = new TextureRegion();
    static Image animBG = new Image(frame);

    private static String selectedName = null;

    public static void buildL2D(String name) {
        // Nullable, can kill every mod with custom MenuRenderer
        try {
            if(!headless) {
                Log.infoTag("L2D Selected", name);
                Live2DBackgroundsExperimental.LoadedL2D l2dLoaded = Live2DBackgroundsExperimental.getL2D(name);
                selectedName = name;
                int frameIndex = (int)(Time.globalTime / l2dLoaded.frameSpeed) % l2dLoaded.frames;

                Texture texture = l2dLoaded.getFrame(frameIndex);

                if (texture != null) {
                    frame.set(texture);
                    animBG.getRegion().set(frame);
                }
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

                float requestedFps = Math.min(120f, l2dLoaded.frameSpeed); // Max 120FPS
                l2dLoaded.setTargetFps(requestedFps);

                Timer timer = Timer.instance();
                Timer.Task task = new Timer.Task() {
                    @Override
                    public void run() {
                        if (!state.isMenu()) {
                            cancel();
                            return;
                        }

                        float fps = (float) l2dLoaded.effectiveFps();
                        if (fps <= 0f) return;

                        int frameIndex = (int)(Time.globalTime * requestedFps / 60f) % l2dLoaded.frames;

                        Texture texture = l2dLoaded.getFrame(frameIndex);

                        var stats = l2dLoaded;
                        String rawMessage = Strings.format(
                            Ansi.CYAN + "FrameBuffer" +
                            Ansi.WHITE + " | " +
                            Ansi.GREEN + "Buf: " +
                            Ansi.WHITE + "@  " +
                            Ansi.BLUE + "Workers: " +
                            Ansi.WHITE + "@/@  " +
                            Ansi.CYAN + "Req: " +
                            Ansi.WHITE + "@fps  " +
                            Ansi.CYAN + "Eff: " +
                            Ansi.WHITE + "@fps  " +
                            Ansi.GREEN + "Sus: " +
                            Ansi.WHITE + "@fps  " +
                            Ansi.RED + "Miss: " +
                            Ansi.WHITE + "@  " +
                            Ansi.WHITE + "Sat: " +
                            Ansi.WHITE + "@" +
                            Ansi.RESET,
                            stats.bufferedFrames(),
                            stats.activeWorkers(),
                            stats.maxWorkers(),
                            Strings.fixed((float) stats.requestedFps(), 1),
                            Strings.fixed((float) stats.effectiveFps(), 1),
                            Strings.fixed((float) stats.sustainableFps(), 1),
                            stats.misses(),
                            stats.saturated()
                        );
                        System.out.print("\033[2K\r" + rawMessage);
                        System.out.flush();


                        if (texture != null) {
                            l2dLoaded.setDisplayTexture(texture);
                            frame.set(texture);
                            animBG.getRegion().set(frame);
                        
                            if (!animBG.hasParent()) {
                                animBG.setFillParent(true);
                                group.addChildAt(0, animBG);
                                Log.infoTag("ArchiveDustry", "Background Loaded!");
                            }
                        }
                    }
                };
                Events.run(EventType.Trigger.update, () -> {
                    float fps = (float)l2dLoaded.effectiveFps();
                    if (state.isMenu() && !task.isScheduled() && fps > 0f) {
                        // Reschedule using the latest effective FPS.
                        timer.scheduleTask(task, 1f / fps);
                    }
                });

                float initialFps = (float)l2dLoaded.effectiveFps();
                if (initialFps > 0f) {
                    timer.scheduleTask(task, 1f / initialFps);
                }
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
        Live2DBackgroundsExperimental.LoadedL2D l2d = Live2DBackgroundsExperimental.getL2D(selectedName);
        if (l2d != null) {
            l2d.dispose();
        }
    }

    @Override
    public boolean isDisposed() {   
        return Disposable.super.isDisposed();
    }

    private static class Ansi {
        public static final String RESET = "\u001B[0m";

        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String MAGENTA = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
    }
}