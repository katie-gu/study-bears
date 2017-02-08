/**
 * Created by jhinukbarman on 2/7/17.
 */
public class Palindrome{
    public static Deque<Character> wordToDeque(String word){
        //.charAt(index); .charAt(1) gives u character at index
        Deque<Character> chars = new ArrayDequeSolution<>();
        for (int i = word.length() - 1; i >= 0; i--){
            chars.addFirst(word.charAt(i));
        }
        chars.printDeque();
        return chars;
    }
    public static boolean isPalindrome(String word){
        return true;

    }

}
