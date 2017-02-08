/**
 * Created by jhinukbarman on 2/7/17.
 */
public interface Deque<Item> {
    public void addFirst(Item x); //Item parameter?
    public void addLast(Item x); //Item parameter?
    public boolean isEmpty();
    public int size();
    public void printDeque();
    public Item removeFirst();
    public Item removeLast();
    public Item get(int index);

}
