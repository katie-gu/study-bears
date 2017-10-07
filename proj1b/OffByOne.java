/**
 * This class implements CharacterComparator interface so it
 * must have the method equalChars which checks if the difference
 * between 2 characters is 1.
 * Created by jhinukbarman on 2/8/17.
 */

public class OffByOne implements CharacterComparator {
    /**
     * Calculates the difference between characters x and y.
     * If the difference is 1, returns true or else returns false.
     * @param x one character
     * @param y another character
     * @return if difference is 1, return true, else false
     */
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == 1) {
            return true;
        }
        return false;
    }

}

