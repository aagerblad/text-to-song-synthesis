import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Created by Andreas on 2014-03-04.
*/
public class Note {

    public static final Float C0 = 16.35f;
    public static final Float C_SHARP0 = 17.32f;
    public static final Float D0 = 18.35f;
    public static final Float D_SHARP0 = 19.45f;
    public static final Float E0 = 20.6f;
    public static final Float F0 = 21.83f;
    public static final Float F_SHARP0 = 23.12f;
    public static final Float G0 = 24.5f;
    public static final Float G_SHARP0 = 25.96f;
    public static final Float A0 = 27.5f;
    public static final Float A_SHARP0 = 29.14f;
    public static final Float B0 = 30.87f;
    public static final Float C1 = 32.7f;
    public static final Float C_SHARP1 = 34.65f;
    public static final Float D1 = 36.71f;
    public static final Float D_SHARP1 = 38.89f;
    public static final Float E1 = 41.2f;
    public static final Float F1 = 43.65f;
    public static final Float F_SHARP1 = 46.25f;
    public static final Float G1 = 49f;
    public static final Float G_SHARP1 = 51.91f;
    public static final Float A1 = 55f;
    public static final Float A_SHARP1 = 58.27f;
    public static final Float B1 = 61.74f;
    public static final Float C2 = 65.41f;
    public static final Float C_SHARP2 = 69.3f;
    public static final Float D2 = 73.42f;
    public static final Float D_SHARP2 = 77.78f;
    public static final Float E2 = 82.41f;
    public static final Float F2 = 87.31f;
    public static final Float F_SHARP2 = 92.5f;
    public static final Float G2 = 98f;
    public static final Float G_SHARP2 = 103.83f;
    public static final Float A2 = 110f;
    public static final Float A_SHARP2 = 116.54f;
    public static final Float B2 = 123.47f;
    public static final Float C3 = 130.81f;
    public static final Float C_SHARP3 = 138.59f;
    public static final Float D3 = 146.83f;
    public static final Float D_SHARP3 = 155.56f;
    public static final Float E3 = 164.81f;
    public static final Float F3 = 174.61f;
    public static final Float F_SHARP3 = 185f;
    public static final Float G3 = 196f;
    public static final Float G_SHARP3 = 207.65f;
    public static final Float A3 = 220f;
    public static final Float A_SHARP3 = 233.08f;
    public static final Float B3 = 246.94f;
    public static final Float C4 = 261.63f;
    public static final Float C_SHARP4 = 277.18f;
    public static final Float D4 = 293.66f;
    public static final Float D_SHARP4 = 311.13f;
    public static final Float E4 = 329.63f;
    public static final Float F4 = 349.23f;
    public static final Float F_SHARP4 = 369.99f;
    public static final Float G4 = 392f;
    public static final Float G_SHARP4 = 415.3f;
    public static final Float A4 = 440f;
    public static final Float A_SHARP4 = 466.16f;
    public static final Float B4 = 493.88f;
    public static final Float C5 = 523.25f;
    public static final Float C_SHARP5 = 554.37f;
    public static final Float D5 = 587.33f;
    public static final Float D_SHARP5 = 622.25f;
    public static final Float E5 = 659.25f;
    public static final Float F5 = 698.46f;
    public static final Float F_SHARP5 = 739.99f;
    public static final Float G5 = 783.99f;
    public static final Float G_SHARP5 = 830.61f;
    public static final Float A5 = 880f;
    public static final Float A_SHARP5 = 932.33f;
    public static final Float B5 = 987.77f;
    public static final Float C6 = 1046.5f;
    public static final Float C_SHARP6 = 1108.73f;
    public static final Float D6 = 1174.66f;
    public static final Float D_SHARP6 = 1244.51f;
    public static final Float E6 = 1318.51f;
    public static final Float F6 = 1396.91f;
    public static final Float F_SHARP6 = 1479.98f;
    public static final Float G6 = 1567.98f;
    public static final Float G_SHARP6 = 1661.22f;
    public static final Float A6 = 1760f;
    public static final Float A_SHARP6 = 1864.66f;
    public static final Float B6 = 1975.53f;
    public static final Float C7 = 2093f;
    public static final Float C_SHARP7 = 2217.46f;
    public static final Float D7 = 2349.32f;
    public static final Float D_SHARP7 = 2489.02f;
    public static final Float E7 = 2637.02f;
    public static final Float F7 = 2793.83f;
    public static final Float F_SHARP7 = 2959.96f;
    public static final Float G7 = 3135.96f;
    public static final Float G_SHARP7 = 3322.44f;
    public static final Float A7 = 3520f;
    public static final Float A_SHARP7 = 3729.31f;
    public static final Float B7 = 3951.07f;
    public static final Float C8 = 4186.01f;
    public static final Float C_SHARP8 = 4434.92f;
    public static final Float D8 = 4698.63f;
    public static final Float D_SHARP8 = 4978.03f;
    public static final Float E8 = 5274.04f;
    public static final Float F8 = 5587.65f;
    public static final Float F_SHARP8 = 5919.91f;
    public static final Float G8 = 6271.93f;
    public static final Float G_SHARP8 = 6644.88f;
    public static final Float A8 = 7040f;
    public static final Float A_SHARP8 = 7458.62f;
    public static final Float B8 = 7902.13f;

    public static final List<Float> NOTE_LIST = new ArrayList<Float>(Arrays.asList(C0, C_SHARP0, D0, D_SHARP0, E0, F0, F_SHARP0, G0, G_SHARP0, A0, A_SHARP0,
            B0, C1, C_SHARP1, D1, D_SHARP1, E1, F1, F_SHARP1, G1, G_SHARP1, A1, A_SHARP1, B1, C2, C_SHARP2, D2,
            D_SHARP2, E2, F2, F_SHARP2, G2, G_SHARP2, A2, A_SHARP2, B2, C3, C_SHARP3, D3, D_SHARP3, E3, F3, F_SHARP3,
            G3, G_SHARP3, A3, A_SHARP3, B3, C4, C_SHARP4, D4, D_SHARP4, E4, F4, F_SHARP4, G4, G_SHARP4, A4, A_SHARP4,
            B4, C5, C_SHARP5, D5, D_SHARP5, E5, F5, F_SHARP5, G5, G_SHARP5, A5, A_SHARP5, B5, C6, C_SHARP6, D6,
            D_SHARP6, E6, F6, F_SHARP6, G6, G_SHARP6, A6, A_SHARP6, B6, C7, C_SHARP7, D7, D_SHARP7, E7, F7, F_SHARP7,
            G7, G_SHARP7, A7, A_SHARP7, B7, C8, C_SHARP8, D8, D_SHARP8, E8, F8, F_SHARP8, G8, G_SHARP8, A8, A_SHARP8,
            B8));

    public static Float getFrequency(Float key, int index) {
        index += NOTE_LIST.indexOf(key);
        if (index < 0 || index > NOTE_LIST.size()-1) {
            return 1.0f;
        }
        return NOTE_LIST.get(index);
    }

}
