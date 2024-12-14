package bluearchive.l2d;

import arc.audio.Music;
import arc.files.Fi;
import arc.files.ZipFi;
import arc.graphics.Texture;
import arc.struct.*;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.serialization.*;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Live2DBackgrounds {
    public static Seq<LoadedL2D> live2ds = new Seq<>();
    public static JsonReader reader = new JsonReader();

    public static void load(Fi live2d) throws Exception {
        ZipFi f = new ZipFi(live2d);
        Seq<Texture> loadedL2ds = new Seq<>();
        Seq<Fi> rawL2d = new Seq<>();
        Music soundTrack = null;
        if (!f.child("live2d.hjson").exists()) throw new L2DMetaMissingException("Live2d \'"+ live2d.nameWithoutExtension()+ "\' has no live2d.hjson in metadata");
        String s = f.child("live2d.hjson").readString();
        if(!s.startsWith("{")) s = "{\n" + s + "\n}";
        L2DMeta meta = metaBuild(reader.parse(s));
        AtomicInteger L2DCount = new AtomicInteger();
        // Single line? why?
        f.child("l2d").walk(l2dFound -> {L2DCount.getAndIncrement(); /*Log.infoTag("ArchiveDustry Debug", "L2D loaded no: "+(L2DCount.get())+" with filename "+(l2dFound.name())+" from "+(meta.name));*/ try {rawL2d.add(l2dFound);}catch(Exception e){ throw new RuntimeException("No frames found inside the Live2D zip file");}});
        for (int i = 0; i <= L2DCount.get()-1; i++) {
            int finalI = i;
            loadedL2ds.add(new Texture(rawL2d.find(m -> Objects.equals(m.nameWithoutExtension(), String.valueOf(finalI+1)))));
        }
        if(!meta.isSoundTrackLocal) {
            soundTrack = new Music(f.child("soundtrack.ogg"));
        };
        LoadedL2D l2d = new LoadedL2D(meta.name, live2d, meta, meta.frameSpeed, meta.isSoundTrackLocal, meta.localSoundTrack, loadedL2ds, soundTrack);
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
        //meta.frames = root.getInt("frames", 0);
        meta.frameSpeed = root.getFloat("frameSpeed", 0f);
        meta.isSoundTrackLocal = root.getBoolean("isSoundTrackLocal", false);
        meta.localSoundTrack = root.getString("localSoundTrack", null);
        return meta;
    }

    public static class LoadedL2D{
        public final String name;
        public final Fi file;
        public final L2DMeta meta;
        //public final int frames;
        public final float frameSpeed;
        public final Seq<Texture> loadedL2ds;
        public final @Nullable Music soundTrack;
        /* Local Soundtrack means the mod's soundtrack */
        public final boolean isSoundTrackLocal;
        public final @Nullable String localSoundTrack;

        public LoadedL2D(String name, Fi file, L2DMeta meta/*, int frames*/, float frameSpeed, boolean isSoundTrackLocal, @Nullable String localSoundTrack, Seq<Texture> loadedL2ds, Music soundTrack) {
            this.name = name;
            this.file = file;
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
        //int frames;
        float frameSpeed;
        @Nullable String displayName, author, description, localSoundTrack;
        boolean isSoundTrackLocal;
        // Blank, because there's no script were used on Live2D(Recollection Lobby)

    }

    public static class L2DMetaMissingException extends Exception{

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
    public static class L2DNoFramesException extends Exception{
        public L2DNoFramesException(){
        }
        public L2DNoFramesException(String message){super(message);}
        public L2DNoFramesException(Throwable cause){super(cause);}
        public L2DNoFramesException(String message, Throwable cause){super(message, cause);}
    }
}