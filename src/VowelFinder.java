import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andreas on 2014-04-03.
 */
public class VowelFinder {
    private static final List<String> VOWELS = new ArrayList<String>(Arrays.asList("u", "@U", "y", "i", "I", "U", "Y",
            "o", "2", "e", "@", "V", "O", "3", "9", "E", "6", "{", "Q", "A", "&", "=", "~"));

    public static boolean isVowel(String phoneme) {
        for (char c: phoneme.toCharArray()) {
            if (VOWELS.contains(String.valueOf(c))) {
                return true;
            }
        }
         return false;
    }

}
