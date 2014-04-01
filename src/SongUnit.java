/**
 * Created by Dexter on 2014-03-15.
 */
public class SongUnit {
    public static final int MS_PER_MINUTE = 60000;
    private final float pitch;
    private final int duration;

    public SongUnit(float pitch, float relativeDuration, int tempo) {
        this.pitch = pitch;
        this.duration = (int) (relativeDuration * MS_PER_MINUTE / tempo); //TODO Decide on exactly how this will work, is tempo in bpm or what?
    }

    public float getPitch() {
        return pitch;
    }

    public int getDuration() {
        return duration;
    }
}
