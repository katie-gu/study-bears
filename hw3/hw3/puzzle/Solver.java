package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;

/**
 * Created by jhinukbarman on 3/19/17.
 */
public class Solver {
    private WorldState curr;
    private MinPQ<SearchNode> m;
    private SearchNode parent;
    private LinkedList<WorldState> arr;


    public Solver(WorldState initial) {
        this.curr = initial;
        m = new MinPQ<>();
        arr = new LinkedList<>();
        SearchNode currNode = new SearchNode(curr, 0, null);
        m.insert(currNode);


        while (!m.isEmpty()) {
            currNode = m.delMin();
            //System.out.println("CurrNode : " + currNode.w);
            if (currNode.isGoal()) {
                break;
            }
            //System.out.println(currNode.moves);

            for (WorldState n : currNode.neighbors()) {
                if (currNode.prev == null) {
                    SearchNode next = new SearchNode(n, currNode.moves + 1, currNode);
                    m.insert(next);
                } else if (!(n.equals(currNode.prev.w))) {
                    SearchNode next = new SearchNode(n, currNode.moves + 1, currNode);
                    m.insert(next);
                }
            }
        }
        parent = currNode;
        while (parent != null) {
            arr.addFirst(parent.w);
            parent = parent.prev;
        }


    }

    public int moves() {
        return arr.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return arr;
    }

    private class SearchNode implements WorldState, Comparable<SearchNode> {
        private WorldState w;
        private int moves; //moves from original to current world state
        private SearchNode prev;
        private int estimatedDistance;

        public SearchNode(WorldState w, int moves, SearchNode prev) {
            this.w = w; //u can't instantiate an interface!
            this.moves = moves;
            this.prev = prev;
            this.estimatedDistance = this.estimatedDistanceToGoal();
        }

        public int estimatedDistanceToGoal() {
            return w.estimatedDistanceToGoal();
        }

        public Iterable<WorldState> neighbors() {
            return w.neighbors();
        }

        @Override
        public int compareTo(SearchNode s) {
            int nodeA = this.estimatedDistance + this.moves;
            int nodeB = s.estimatedDistance + s.moves;

            return nodeA - nodeB;
        }



    }




}

