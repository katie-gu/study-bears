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
        //chars.printDeque();
        return chars;
    }
    public static boolean isPalindrome(String word){
        Deque<Character> chars = wordToDeque(word);
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }

        int first = 0;
        int last = word.length()-1;
        char compare1 = chars.get(first);
        char compare2 = chars.get(last);

        while(compare1 == compare2){
            first++;
            last--;
            compare1 = chars.get(first);
            compare2 = chars.get(last);
            if(first == last || first>last){
                return true;
            }
        }
        return false;
    }

}
