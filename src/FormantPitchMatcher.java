import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andreas on 2014-04-03.
 */
public class FormantPitchMatcher {

    Map<String, NormalDistribution> map = new HashMap<String, NormalDistribution>();
    private final int CLOSED_FORMANT_X_VALUE =
    private final int CLOSED_MID_FORMANT_X_VALUE =
    private final int OPEN_MID_FORMANT_X_VALUE =
    private final int OPEN_FORMANT_X_VALUE =

    public FormantPitchMatcher() {
        map.put("A", new NormalDistribution(0, 1));
        map.put("{", new NormalDistribution(0, 1));
        map.put("6", new NormalDistribution(0, 1));
        map.put("Q", new NormalDistribution(0, 1));
        map.put("E", new NormalDistribution(0, 1));
        map.put("@", new NormalDistribution(0, 1));
        map.put("3", new NormalDistribution(0, 1));
        map.put("I", new NormalDistribution(0, 1));
        map.put("O", new NormalDistribution(0, 1));
        map.put("2", new NormalDistribution(0, 1));
        map.put("9", new NormalDistribution(0, 1));
        map.put("&", new NormalDistribution(0, 1));
        map.put("U", new NormalDistribution(0, 1));
        map.put("}", new NormalDistribution(0, 1));
        map.put("V", new NormalDistribution(0, 1));
        map.put("Y", new NormalDistribution(0, 1));
        map.put("=", new NormalDistribution(0, 1));
        map.put("~", new NormalDistribution(0, 1));
    }

    public double getPitchPoint(String phoneme, int pitch) {
        return map.get(phoneme).probability(pitch);
    }

}
