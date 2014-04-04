import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.*;

/**
 * Created by Andreas on 2014-04-03.
 */
public class FormantPitchMatcher {

    Map<String, NormalDistribution> map = new HashMap<String, NormalDistribution>();
    private final double CLOSED_FORMANT_X_VALUE = -2;
    private final double NEAR_CLOSED_FORMANT_X_VALUE = 0;
    private final double CLOSED_MID_FORMANT_X_VALUE = 4;
    private final double MID_FORMANT_X_VALUE = 4;
    private final double OPEN_MID_FORMANT_X_VALUE = 7;
    private final double NEAR_OPEN_FORMANT_X_VALUE = 7;
    private final double OPEN_FORMANT_X_VALUE = 14;
    private final double VARIANCE = 1;

    private final NormalDistribution CLOSED_FORMANT_DIST = new NormalDistribution(CLOSED_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution NEAR_CLOSED_FORMANT_DIST = new NormalDistribution(NEAR_CLOSED_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution CLOSED_MID_FORMANT_DIST = new NormalDistribution(CLOSED_MID_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution MID_FORMANT_DIST = new NormalDistribution(MID_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution OPEN_MID_FORMANT_DIST = new NormalDistribution(OPEN_MID_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution NEAR_OPEN_FORMANT_DIST = new NormalDistribution(NEAR_OPEN_FORMANT_X_VALUE, VARIANCE);
    private final NormalDistribution OPEN_FORMANT_DIST = new NormalDistribution(OPEN_FORMANT_X_VALUE, VARIANCE);

    private final List<String> CLOSED_PHONEMES = new ArrayList<String>(Arrays.asList("u", "y", "i", "I"));
    private final List<String> NEAR_CLOSED_PHONEMES = new ArrayList<String>(Arrays.asList("U", "Y"));
    private final List<String> CLOSED_MID_PHONEMES = new ArrayList<String>(Arrays.asList("o", "2", "e"));
    private final List<String> MID_PHONEMES = new ArrayList<String>(Arrays.asList("@"));
    private final List<String> OPEN_MID_PHONEMES = new ArrayList<String>(Arrays.asList("V", "O", "3", "9", "E"));
    private final List<String> NEAR_OPEN_PHONEMES = new ArrayList<String>(Arrays.asList("6", "{"));
    private final List<String> OPEN_PHONEMES =new ArrayList<String>(Arrays.asList("Q", "A", "&"));

    public FormantPitchMatcher() {
    }

    public double getPitchPoint(String phoneme, int pitch) {
        phoneme = phoneme.substring(0,1);
        if (CLOSED_PHONEMES.contains(phoneme)) {
            return CLOSED_FORMANT_DIST.density(pitch);
        } else if (NEAR_CLOSED_PHONEMES.contains(phoneme)) {
            return NEAR_CLOSED_FORMANT_DIST.density(pitch);
        } else if (CLOSED_MID_PHONEMES.contains(phoneme)) {
            return CLOSED_MID_FORMANT_DIST.density(pitch);
        } else if (MID_PHONEMES.contains(phoneme)) {
            return MID_FORMANT_DIST.density(pitch);
        } else if (OPEN_MID_PHONEMES.contains(phoneme)) {
            return OPEN_MID_FORMANT_DIST.density(pitch);
        } else if (NEAR_OPEN_PHONEMES.contains(phoneme)) {
            return NEAR_OPEN_FORMANT_DIST.density(pitch);
        } else if (OPEN_PHONEMES.contains(phoneme)) {
            return OPEN_FORMANT_DIST.density(pitch);
        } else {
            return 0;
        }
    }

}
