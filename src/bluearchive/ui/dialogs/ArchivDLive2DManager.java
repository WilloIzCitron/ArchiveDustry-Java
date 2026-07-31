package bluearchive.ui.dialogs;

import arc.Core;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.serialization.Json;
import bluearchive.ArchiveDustry;
import bluearchive.experimentals.decoder.MP4Decoder;
import bluearchive.l2d.Live2DBackgroundsExperimental;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.FileChooser.FileChooserParams;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;
import static bluearchive.l2d.Live2DBackgroundsExperimental.live2ds;
import static mindustry.Vars.dataDirectory;
import static mindustry.Vars.platform;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchivDLive2DManager extends BaseDialog {
    private Table l2dtabl;
    private boolean L2DLoaded = false;
    //TODO: make another dialog in order to view Live2D(Recollection) Metadata
    Dialog restartDialog = new Dialog(){{
        cont.add(new Table(){{
            cont.image(atlas.find("bluearchive-arona-happy")).size(256,256).center().row();
            new Table(){{
                    cont.add("Arona").left().row();
                    cont.add(bundle.get("ba-restartDialogText")).right().row();
                    cont.button(bundle.get("ba-restartConfirm"), () -> Core.app.exit()).size(250f, 50f);
                }};
        }});
    }};

    public ArchivDLive2DManager(){
        super(bundle.get("ba-l2dManager"));
        addCloseButton();
        onResize(this::rebuild);
        cont.pane(tablbrw -> {
            tablbrw.margin(10f).top();
            l2dtabl = tablbrw;
        }).grow().scrollX(false).top();
        cont.row();
        makeButtonOverlayHelper();
        shown(this::rebuild);
        show();
        hidden(() -> {L2DLoaded = false;});
    }

    private void rebuild(){
        live2ds.each(l -> l.dispose());
        live2ds.clear();
        l2dtabl.clear();
        l2dtabl.add("@loading");

        if(!L2DLoaded){
            dataDirectory.child("live2d").walk(f -> {
                ArchiveDustry.foundL2D++;
                try {
                    Live2DBackgroundsExperimental.load(f);
                    ArchiveDustry.loadedL2D++;
                } catch (Exception e) {
                    Log.err(e);
                    ArchiveDustry.erroredL2D++;
                }
            });
            l2dtabl.clear();
            live2ds.each(l -> l2dtabl.button(con -> {
                con.margin(3f);
                con.row();
                con.add(l.displayName).row();
                con.add(bundle.formatString(bundle.get("ba-l2d.author"), l.author)).color(Color.gray).bottom().row();
            }, Styles.flatBordert, () -> { Core.settings.put("setL2D-new", l.name); restartDialog.show();}).growX().row());
            L2DLoaded = true;
        }
    }
    private void makeButtonOverlayHelper() {
        Table buttons = new Table();

        buttons.defaults()
                .height(64f)
                .width(250f)
                .pad(6f);

        // Import button
        buttons.button("Import From File", Icon.upload, Styles.defaultt, this::showImportDialog);

        // Back button
        buttons.button("@back", Icon.left, Styles.defaultt, this::hide);

        cont.add(buttons).padTop(10f);
    }
    private void showImportDialog() {
        FileChooserParams params = new FileChooserParams().extensions("mp4").open(true).title("@ba-importFile");
        params.submit(file -> {
            try {
                openModal(folder -> {
                    BaseDialog dialog = new BaseDialog("Please Wait");
                    dialog.cont.table(t -> {
                        t.defaults().growX().pad(6);
                        t.labelWrap("Please wait for a moment sensei! This will take a while...");
                    });
                    dialog.addCloseButton();
                    dialog.show();
                    importLive2D(file, folder);
                    rebuild();
                    Vars.ui.showInfoFade("@ba-import.success");
                });

            } catch (Throwable t) {
                Vars.ui.showException(t);
            }
        });
    }
    private void importLive2D(Fi file, Fi folder) {
        // Run heavy lifting on a background thread to prevent freezing the game/UI
        Core.app.post(() -> {
            try {
                folder.mkdirs();
                Fi l2dFolder = folder.child("l2d");
                l2dFolder.mkdirs();
                if (MP4Decoder.describe(file.file(), l2dFolder.file())) {

                    Fi zipFile = folder.parent().child(folder.nameWithoutExtension() + ".zip");

                    zipFolder(folder.file().toPath(), zipFile.file().toPath());

                    // Clean up unzipped source directory
                    folder.deleteDirectory();
                    return;
                }

                throw new DecodeFailedException("Failed to decode file.", folder);

            } catch (DecodeFailedException e) {
                if (e.folder != null) e.folder.deleteDirectory();
                // UI thread required for showing game dialogs/toasts
                Core.app.post(() -> Vars.ui.showErrorMessage(e.getMessage()));
            } catch (Exception e) {
                Core.app.post(() -> Vars.ui.showException(e));
            }
        });
    }
    private void openModal(Cons<Fi> onComplete) {
        BaseDialog dialog = new BaseDialog("Settings");

        TextField[] fields = new TextField[6];
        CheckBox[] isLocalSoundtrack = new CheckBox[1];
        
        dialog.cont.table(t -> {
            t.defaults().growX().pad(6);
        
            t.add("Discoverable L2D Name");
            t.row();
            fields[0] = t.field("", text -> {}).growX().get();
            t.row();

            t.add("L2D Display Name");
            t.row();
            fields[1] = t.field("", text -> {}).growX().get();
            t.row();
        
            t.add("Author");
            t.row();
            fields[2] = t.field("", text -> {}).growX().get();
            t.row();

            t.add("Description");
            t.row();
            fields[3] = t.field("", text -> {}).growX().get();
            t.row();

            t.add("Frame Speed (in decimals/real)");
            t.row();
            fields[4] = t.field("", text -> {}).growX().get();
            t.row();

            t.row();
            isLocalSoundtrack[0] = t.check("Local Soundtrack", false, b -> {}).get();
            t.row();

            t.add("Soundtrack filename").visible(() -> isLocalSoundtrack[0].isChecked());
            t.row();
            fields[5] = t.field("", text -> {}).growX().get();
            fields[5].visible(() -> isLocalSoundtrack[0].isChecked());
        });

        dialog.addCloseButton();

        dialog.buttons.button("OK", () -> {
            Config conf = new Config();
            conf.name = fields[0].getText();
            conf.displayName = fields[1].getText();
            conf.author = fields[2].getText();
            conf.description = fields[3].getText();
            try {
                conf.frameSpeed = Float.parseFloat(fields[4].getText());
            } catch (NumberFormatException e) {
                Vars.ui.showErrorMessage("Frame speed must be a number.");
                return;
            }
            conf.isSoundTrackLocal = isLocalSoundtrack[0].isChecked();
            conf.localSoundTrack = fields[5].getText();
            Fi live2dJsonFolder = Vars.dataDirectory.child("live2d").child(conf.name);
            live2dJsonFolder.mkdirs();
            Json json = new Json();
            Fi live2DJson = live2dJsonFolder.child("live2d.json");
            live2DJson.writeString(json.toJson(conf), false);
            onComplete.get(live2dJsonFolder);
            dialog.hide();
        });
        
        dialog.show();
    }
    private class Config {
        public String name;
        public float frameSpeed;
        public @Nullable String displayName, author, description, localSoundTrack;
        public boolean isSoundTrackLocal;
    }
    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws IOException {
        // Ensure the parent directories for the zip file exist
        if (zipPath.getParent() != null) {
            Files.createDirectories(zipPath.getParent());
        }

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (Files.isSameFile(file, zipPath)) {
                        return FileVisitResult.CONTINUE;
                    }

                    Path targetRelativePath = sourceFolderPath.relativize(file);
                    ZipEntry zipEntry = new ZipEntry(targetRelativePath.toString().replace("\\", "/"));
                    
                    zos.putNextEntry(zipEntry);
                    
                    // Normal file write
                    Files.copy(file, zos);
                    
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // Skip the root directory itself to keep contents in the zip root
                    if (dir.equals(sourceFolderPath)) {
                        return FileVisitResult.CONTINUE;
                    }

                    Path targetRelativePath = sourceFolderPath.relativize(dir);
                    String dirEntryName = targetRelativePath.toString().replace("\\", "/") + "/";
                    
                    zos.putNextEntry(new ZipEntry(dirEntryName));
                    zos.closeEntry();
                    
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private class DecodeFailedException extends Exception {
        public Fi folder;
        public DecodeFailedException(String message, Fi folder){
            super(message);
            this.folder = folder;
        }
    }
}
