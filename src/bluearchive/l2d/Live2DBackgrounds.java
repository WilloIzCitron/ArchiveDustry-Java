package bluearchive.l2d;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.graphics.Texture;
import arc.struct.*;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.serialization.*;

import java.util.Objects;

public class Live2DBackgrounds {
    public static Seq<LoadedL2D> live2ds = new Seq<>();
    public static JsonReader reader = new JsonReader();

    public static void load(Fi live2d) throws Exception {
        // Android 14
        if(!live2d.file().setReadOnly()) Log.warn("Could not make @ read-only.", live2d.name());

        ZipFi f = new ZipFi(live2d);
        Seq<Texture> loadedL2ds = new Seq<>();
        if (!f.child("live2d.hjson").exists()) throw new L2DMetaMissingException("Live2d \'"+ live2d.nameWithoutExtension()+ "\' has no live2d.hjson in metadata");
        String s = f.child("live2d.hjson").readString();
        if(!s.startsWith("{")) s = "{\n" + s + "\n}";
        L2DMeta meta = metaBuild(reader.parse(s));
        for (int i = 0; i <= meta.frames - 1; i++) {
            if(!f.child("l2d").exists()) throw new L2DNoFramesException("No frames found inside the Live2D zip file");
            else {
                loadedL2ds.add(new Texture(f.child("l2d").child((i+1)+".png")));
            }
        }
        //TODO: Make it more functional, using bool state as active l2d indicator. Must disable previous one.
        LoadedL2D l2d = new LoadedL2D(meta.name, live2d, meta, meta.frames, meta.frameSpeed, loadedL2ds);
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
        meta.frames = root.getInt("frames", 0);
        meta.frameSpeed = root.getFloat("frameSpeed", 0f);
        return meta;
    }

    public static class LoadedL2D{
        public final String name;
        public final Fi file;
        public final L2DMeta meta;
        public final int frames;
        public final float frameSpeed;
        public final Seq<Texture> loadedL2ds;

        public LoadedL2D(String name, Fi file, L2DMeta meta, int frames, float frameSpeed, Seq<Texture> loadedL2ds){
            this.name = name;
            this.file = file;
            this.meta = meta;
            this.frames = frames;
            this.frameSpeed = frameSpeed;
            this.loadedL2ds = loadedL2ds;
        }
    }

    public static class L2DMeta{
        String name;
        int frames;
        float frameSpeed;
        @Nullable String displayName, author, description;

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