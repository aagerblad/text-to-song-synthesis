/**
 * Created by Dexter on 2014-03-15.
 */
public class SongUnit {
    private final float pitch;
    private final int duration;

    public SongUnit(float pitch, int relativeDuration, int tempo) {
        this.pitch = pitch;
        this.duration = relativeDuration * tempo; //TODO Decide on exactly how this will work, is tempo in bpm or what?
    }

    public float getPitch() {
        return pitch;
    }

    public int getDuration() {
        return duration;
    }
}
