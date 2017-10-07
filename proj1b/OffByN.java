/**
 * This class implements CharacterComparator interface. It creates
 * a new object in the constructor and checks if the difference
 * between 2 charactesr is the integer than is passed as a parameter.
 * Created by jhinukbarman on 2/9/17.
 */
public class OffByN implements CharacterComparator {
    private int numOfBy;

    /**
     * Constructor creates a new object which takes the integer N
     * as the parameter.
     * @param N integer which is the difference between two characters
     */
    public OffByN(int N) {
        numOfBy = N;
    }

    /**
     * Calculates the difference between characters x and y.
     * If the difference is the given integer, returns true or else returns false.
     * @param x one character
     * @param y another character
     * @return if difference is given integer, return true, else false
     */
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == numOfBy) {
            return true;
        }
        return false;
    }

}
