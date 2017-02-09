/**
 * This class converts a String to a Deque and checks if the
 * string is a palindrome or a generalized palindrome.
 * Created by jhinukbarman on 2/7/17.
 */
public class Palindrome {
    /**
     * Converts String to a Deque. Creates an object with static type
     * Deque and dynamic type ArrayDequeSolution because dynamic type
     * cannot be Deque which is an interface.
     * @param word
     * @return deque with characters of the string in order
     */
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> chars = new ArrayDequeSolution<Character>();
        for (int i = word.length() - 1; i >= 0; i--) {
            chars.addFirst(word.charAt(i));
        }
        return chars;
    }

    /**
     * Checks if string is a palindrome. Converts string to deque.
     * If length of string is 0 or 1 it is a palindrome. Else, compares
     * outside characters and works inwards until it hits the same
     * letter (odd length) or switches indexes (even length).
     * @param word string
     * @return true if palindrome, else false
     */
    public static boolean isPalindrome(String word) {
        Deque<Character> chars = wordToDeque(word);
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }

        int first = 0;
        int last = word.length() - 1;
        char compare1 = chars.get(first);
        char compare2 = chars.get(last);

        while (compare1 == compare2) {
            first++;
            last--;
            compare1 = chars.get(first);
            compare2 = chars.get(last);
            if (first == last || first > last) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if string is the type of palindrome specified by an object
     * of the CharacterComparator interface.
     * interface. Returns true if palindrome, else false.
     * @param word string
     * @param cc any object that implements CharacterComparator interface
     * @return true if palindrome, else false.
     */
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> chars = wordToDeque(word);
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }

        int first = 0;
        int last = word.length() - 1;
        char compare1 = chars.get(first);
        char compare2 = chars.get(last);

        while (cc.equalChars(compare1, compare2)) {
            first++;
            last--;
            compare1 = chars.get(first);
            compare2 = chars.get(last);
            if (first == last || first > last) {
                return true;
            }
        }
        return false;

    }

}
