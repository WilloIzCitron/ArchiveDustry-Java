package bluearchive.l2d;

import arc.Core;
import arc.audio.Music;
import arc.files.*;
import arc.graphics.Texture;
import arc.struct.*;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.serialization.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Live2DBackgrounds {
    public static Seq<LoadedL2D> live2ds = new Seq<>();
    public static JsonReader reader = new JsonReader();
    public static Music soundTrack = new Music();

    public static void load(Fi live2d) throws Exception {
        ZipFi f = new ZipFi(live2d);
        Seq<Texture> loadedL2ds = new Seq<>();
        Seq<Fi> rawL2d = new Seq<>();
        String s;
        if (!f.child("live2d.hjson").exists() && !f.child("live2d.json").exists()) throw new L2DMetaMissingException("Live2d \'"+ live2d.nameWithoutExtension()+ "\' has no live2d.hjson or live2d.json in metadata");
        if (f.child("live2d.hjson").exists()) {s = f.child("live2d.hjson").readString();} else {s = f.child("live2d.json").readString();}
        if(!s.startsWith("{")) s = "{\n" + s + "\n}";
        L2DMeta meta = metaBuild(reader.parse(s));
        AtomicInteger L2DCount = new AtomicInteger();
        // Don't load all frames on every pack, only selected frames on pack
        if(Objects.equals(Core.settings.getString("setL2D-new"), meta.name)) {
            f.child("l2d").walk(l2dFound -> {
                L2DCount.getAndIncrement(); /*Log.infoTag("ArchiveDustry Debug", "L2D loaded no: "+(L2DCount.get())+" with filename "+(l2dFound.name())+" from "+(meta.name));*/
                try {
                    rawL2d.add(l2dFound);
                } catch (RuntimeException e) {
                    throw new L2DNoFramesException("No frames found inside the Live2D zip file");
                }
            });
            for (int i = 0; i <= L2DCount.get() - 1; i++) {
                int finalI = i;
                loadedL2ds.add(new Texture(rawL2d.find(m -> Objects.equals(m.nameWithoutExtension(), String.valueOf(finalI + 1)))));
            }
            rawL2d.clear();
        }
        if(!meta.isSoundTrackLocal) {
            try {
                Fi track = f.child("soundtrack.ogg");
                if (track.exists()) {
                    soundTrack = new Music(track);
                } else {
                    Fi trackMP3 = f.child("soundtrack.mp3");
                    if (trackMP3.exists()) {
                        soundTrack = new Music(trackMP3);
                    }
                }
            } catch (RuntimeException r) {
                throw new RuntimeException("Live2d \'"+(meta.name)+ "\' uses an external soundtrack file, but it does not have one. (is it was named incorrectly?)");

            }
        }
        LoadedL2D l2d = new LoadedL2D(meta.name, live2d, meta, meta.frameSpeed, meta.isSoundTrackLocal, meta.localSoundTrack, loadedL2ds, soundTrack);
        Log.infoTag("ArchiveDustry", (meta.displayName)+ " has been loaded!");
        live2ds.add(l2d);
    }

    public static @Nullable LoadedL2D getL2D(String name){
        return live2ds.find(m -> Objects.equals(m.name, name));
    }

    private static L2DMeta metaBuild(JsonValue root) {
        L2DMeta meta = new L2DMeta();
        meta.name = root.getString("name");
        meta.displayName = root.getString("displayName");
        meta.author = root.getString("author", null);
        meta.description = root.getString("description", null);
        meta.frameSpeed = root.getFloat("frameSpeed", 0f);
        meta.isSoundTrackLocal = root.getBoolean("isSoundTrackLocal", false);
        meta.localSoundTrack = root.getString("localSoundTrack", null);
        return meta;
    }

    public static class LoadedL2D{
        /* Internal name, display name and author who made this package. */
        public final String name, displayName, author;
        public final Fi file;
        public final L2DMeta meta;
        /* Frame speed of Live2D, how fast could it run. */
        public final float frameSpeed;
        /* Loaded Live2D frames are inside of Sequence, they can be loaded on `ArchivDBackground.java` */
        public final Seq<Texture> loadedL2ds;
        /* Custom Soundtrack for Live2D, must be named "soundtrack.ogg" or "soundtrack.mp3". */
        public final @Nullable Music soundTrack;
        /*
        * Local Soundtrack means the internal mod soundtrack.
        * `isSoundTrackLocal` is a boolean that Live2D package use internal mod soundtrack as main menu music.
        * `localSoundTrack` is a music name of local mod soundtrack.
        */
        public final boolean isSoundTrackLocal;
        public final @Nullable String localSoundTrack;

        public LoadedL2D(String name, Fi file, L2DMeta meta/*, int frames*/, float frameSpeed, boolean isSoundTrackLocal, @Nullable String localSoundTrack, Seq<Texture> loadedL2ds, Music soundTrack) {
            this.name = name;
            this.file = file;
            this.displayName = meta.displayName;
            this.author = meta.author;
            this.meta = meta;
            //this.frames = frames;
            this.frameSpeed = frameSpeed;
            this.loadedL2ds = loadedL2ds;
            this.soundTrack = soundTrack;
            this.isSoundTrackLocal = isSoundTrackLocal;
            this.localSoundTrack = localSoundTrack;
        }
    }

    public static class L2DMeta{
        String name;
        float frameSpeed;
        @Nullable String displayName, author, description, localSoundTrack;
        boolean isSoundTrackLocal;
        // Blank, because there's no script were used on Live2D(Recollection Lobby)

    }

    public static class L2DMetaMissingException extends RuntimeException{

        public L2DMetaMissingException(){
        }

        public L2DMetaMissingException(String message){
            super(message);
        }

        public L2DMetaMissingException(String message, Throwable cause){
            super(message, cause);
        }

        public L2DMetaMissingException(Throwable cause){
            super(cause);
        }

    }
    public static class L2DNoFramesException extends RuntimeException{
        public L2DNoFramesException(){
        }
        public L2DNoFramesException(String message){super(message);}
        public L2DNoFramesException(Throwable cause){super(cause);}
        public L2DNoFramesException(String message, Throwable cause){super(message, cause);}
    }
}