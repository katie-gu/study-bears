package synthesizer;

/**
 * Created by jhinukbarman on 2/19/17.
 */
public abstract class AbstractBoundedDequeue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;

    public int capacity(){
        return capacity;
    }

    public int fillCount(){
        return fillCount;
    }

    public abstract void enqueue(T x);
}
