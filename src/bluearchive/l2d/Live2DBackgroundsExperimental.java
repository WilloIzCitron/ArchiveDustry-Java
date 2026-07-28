package bluearchive.l2d;

import bluearchive.experimentals.FrameBuffer;

import arc.Core;
import arc.audio.Music;
import arc.files.*;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.struct.*;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.serialization.*;
import java.util.Comparator;
import java.util.Objects;

public class Live2DBackgroundsExperimental {
    public static Seq<LoadedL2D> live2ds = new Seq<>();
    public static JsonReader reader = new JsonReader();
    public static Music soundTrack = new Music();

    public static void load(Fi live2d) throws Exception {
        ZipFi f = new ZipFi(live2d);
        String s;

        if (!f.child("live2d.hjson").exists() && !f.child("live2d.json").exists()) {
            throw new L2DMetaMissingException("Live2D '" + live2d.nameWithoutExtension() + "' has no live2d.hjson or live2d.json");
        }

        if (!f.child("live2d.hjson").exists() && !f.child("live2d.json").exists()) throw new L2DMetaMissingException("Live2d \'"+ live2d.nameWithoutExtension()+ "\' has no live2d.hjson or live2d.json in metadata");
        if (f.child("live2d.hjson").exists()) {s = f.child("live2d.hjson").readString();} else {s = f.child("live2d.json").readString();}

        if(!s.startsWith("{")) s = "{\n" + s + "\n}";
        L2DMeta meta = metaBuild(reader.parse(s));

        FrameBuffer<Pixmap> buffer = null;
        int frameCount = 0;

        // Don't load all frames on every pack, only selected frames on pack
        if(Objects.equals(Core.settings.getString("setL2D-new"), meta.name)) {

            Seq<Fi> discoveredFrames = new Seq<>();
            f.child("l2d").walk(l2dFound -> {
                if(l2dFound.isDirectory()) return;
                discoveredFrames.add(l2dFound);
            });

            if(discoveredFrames.isEmpty()) {
                throw new L2DNoFramesException("No Live2Ds frame found.");
            }

            // Sort once instead of O(n^2)
            discoveredFrames.sort(Comparator.comparingInt(file -> Integer.parseInt(file.nameWithoutExtension())));

            frameCount = discoveredFrames.size;
            Fi[] files = discoveredFrames.toArray(Fi.class);
            buffer = new FrameBuffer<>(files, Pixmap::new, (frame, pixmap) -> pixmap.dispose(), 0, 30, 60, 15, true);
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
        LoadedL2D loaded = new LoadedL2D(meta.name, live2d, meta, buffer, frameCount, soundTrack);
        live2ds.add(loaded);

        Log.infoTag("ArchiveDustry", "Loaded Live2D package: " + meta.displayName);
        // LoadedL2D l2d = new LoadedL2D(meta.name, live2d, meta, meta.frameSpeed, meta.isSoundTrackLocal, meta.localSoundTrack, loadedL2ds, soundTrack);
        //Log.infoTag("ArchiveDustry", (meta.displayName)+ " has been loaded!");
        // live2ds.add(l2d);
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

    public static class LoadedL2D {
    // Package metadata.
    public final String name;
    public final String displayName;
    public final String author;

    private int lastMissedFrame = -1;

    public final Fi file;
    public final L2DMeta meta;

    // Playback mechanism

    public final float frameSpeed;
    public final @Nullable Music soundTrack;
    public final boolean isSoundTrackLocal;
    public final @Nullable String localSoundTrack;

    // Cache
    private final IntMap<Texture> textures = new IntMap<>();
    
     // to prevent being prematurely evicted
    private Texture displayedTexture;
    private final Queue<Texture> delayedDisposal = new Queue<>();

    /*
     * Internal buffering.
     *
     * Null means this package is installed but wasn't the
     * selected lobby background, so its textures were never
     * initialized.
     */

    private final @Nullable FrameBuffer<Pixmap> frameBuffer;
    /*
     * Immutable frame count.
     */

    public final int frames;

    /*
     * Cached dimensions.
     */

    public final int width;
    public final int height;

    public LoadedL2D(String name, Fi file, L2DMeta meta, @Nullable FrameBuffer<Pixmap> frameBuffer, int frameCount, @Nullable Music soundTrack) {
            this.name = name;
            this.file = file;
            this.meta = meta;

            this.displayName = meta.displayName;
            this.author = meta.author;
            this.frameSpeed = meta.frameSpeed;

            this.soundTrack = soundTrack;
            this.isSoundTrackLocal = meta.isSoundTrackLocal;
            this.localSoundTrack = meta.localSoundTrack;

            this.frameBuffer = frameBuffer;
            this.frames = frameCount;
            /*
             * Width/height used to come from the first loaded Texture.
             *
             * With lazy buffering we no longer have every Texture
             * resident.
             *
             * We opportunistically query frame zero if it exists.
             */

            Texture first = getFrame(0);

            if (first != null) {
                this.width = first.width;
                this.height = first.height;
            } else {
                this.width = 0;
                this.height = 0;
            }
            // If L2D is not selected, buffer may be null.
            if(this.frameBuffer != null) {
                frameBuffer.setDeallocator((frame, pixmap) -> {
                    Core.app.post(() -> {
                        Texture tex = textures.remove(frame);
                    
                        if (tex != null) {
                            if (tex == displayedTexture) {
                                if (!delayedDisposal.contains(tex)) {
                                    delayedDisposal.add(tex);
                                }
                            } else {
                                tex.dispose();
                            }
                        }
                    
                        pixmap.dispose();
                        flushDelayedDisposals();
                    });
                });
            }
        }
        /**
         * Returns the requested frame.
         *
         * Returns null when the frame has not finished loading yet.
         */
        public @Nullable Pixmap getPixmap(int frame) {

            if (frameBuffer == null)
                return null;

            frameBuffer.setCurrentFrame(frame);
            Pixmap pix = frameBuffer.get(frame);

            if (pix == null) {
                if (lastMissedFrame != frame) {
                    lastMissedFrame = frame;
                    frameBuffer.reportPlayback(false);
                }
                return null;
            }

            lastMissedFrame = -1;
            frameBuffer.reportPlayback(true);

            return pix;
        }

        public @Nullable Texture getFrame(int frame) {
            if (frameBuffer == null) return null;
            Pixmap pix = getPixmap(frame);
            if (pix == null) return null;

            Texture tex = textures.get(frame);
            if (tex == null) {
                tex = new Texture(pix);
                textures.put(frame, tex);
            }
        
            frameBuffer.pin(frame);
            return tex;
        }

        /**
         * Jump playback to another frame.
         */
        public void seek(int frame) {
            if (frameBuffer != null) {
                frameBuffer.seek(frame);
            }
        }

        /**
         * Stop background workers and dispose remaining cached textures.
         */
        public void dispose() {
            if (frameBuffer == null)
                return;

            frameBuffer.stop();
            for (Texture texture : textures.values()) {
                texture.dispose();
            }
            textures.clear();
        }

        public boolean buffered() {
            return frameBuffer != null;
        }

        public int bufferedFrames() {
            if (frameBuffer == null)
                return 0;

            return frameBuffer.getStats().bufferedFrames();
        }

        public double effectiveFps() {
            if (frameBuffer == null)
                return 0.0;

            return frameBuffer.getStats().effectiveFps();
        }

        public double sustainableFps() {
            if (frameBuffer == null)
                return 0.0;

            return frameBuffer.getStats().sustainableFps();
        }

        public long misses() {
            if (frameBuffer == null)
                return 0;

            return frameBuffer.getStats().misses();
        }

        public boolean saturated() {
            if (frameBuffer == null)
                return false;

            return frameBuffer.getStats().saturated();
        }

        public int maxWorkers() {
            if(frameBuffer == null) 
                return -1;

            return frameBuffer.getStats().maxWorkers();
        }

        public int activeWorkers (){
            if(frameBuffer == null)
                return -1;

            return frameBuffer.getStats().activeWorkers();
        }

        public double requestedFps() {
            if(frameBuffer == null)
                return -1.1;
            return frameBuffer.getStats().requestedFps();
        }
        public void setTargetFps(double fps) {
            frameBuffer.setTargetFps(fps);
        }
        public void setDisplayTexture(Texture t) {
            this.displayedTexture = t;
        }

        private void flushDelayedDisposals() {
            for (int i = delayedDisposal.size - 1; i >= 0; i--) {
                Texture texture = delayedDisposal.get(i);
            
                if (texture != displayedTexture) {
                    texture.dispose();
                    delayedDisposal.remove(texture);
                }
            }
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