import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by jhinukbarman on 4/17/17.
 */
/*
//https://codereview.stackexchange.com/questions/67970/graph-implementation-in-java-8
public class Node<T> implements Comparable{

    private T vertex;

    private List<Edge<T>> edges;

    private Node<T> parent;

    private boolean isVisited;

    public Node(T vertex) {
        this.vertex = vertex;
        this.edges = new ArrayList<>();
    }

    public T vertex() {
        return vertex;
    }

    //fix later. comparison for a priority queue
    public int compareTo(Node n1, Node n2) {
        return 1;
    }

    public boolean addEdge(Node<T> node, int weight) {
        if (hasEdge(node)) {
            return false;
        }
        Edge<T> newEdge = new Edge<>(this, node, weight);
        return edges.add(newEdge);
    }

    public boolean removeEdge(Node<T> node) {
        Optional<Edge<T>> optional = findEdge(node);
        if (optional.isPresent()) {
            return edges.remove(optional.get());
        }
        return false;
    }

    public boolean hasEdge(Node<T> node) {
        return findEdge(node).isPresent();
    }

    private Optional<Edge<T>> findEdge(Node<T> node) {
        return edges.stream()
                .filter(edge -> edge.isBetween(this, node))
                .findFirst();
    }

    public List<Edge<T>> edges() {
        return edges;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public Node<T> parent() {
        return parent;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }
}
*/