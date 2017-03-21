package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by jhinukbarman on 3/19/17.
 */
public class Solver {
    private int moves;
    private WorldState curr;
    private MinPQ<SearchNode> m;
    private ArrayList<WorldState> arr;
    //private Comparator<SearchNode> comp;


    public Solver(WorldState initial) {
        this.curr = initial;
        m = new MinPQ<>();
        arr = new ArrayList<>();
        SearchNode currNode = new SearchNode(curr, 0, null);
        m.insert(currNode);

        while (!currNode.isGoal()) {
            currNode = m.delMin();
            //System.out.println("CurrNode : " + currNode.w);
            arr.add(currNode.w);
            moves += 1;
            //System.out.println(currNode.neighbors());
            for (WorldState n : currNode.neighbors()) {
                if (!(arr.contains(n))) {
                    SearchNode next = new SearchNode(n, moves, currNode);
                    //arr.add(n);
                    m.insert(next);
                }
            }


        }

    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return arr;
    }

    public class SearchNode implements WorldState, Comparable<SearchNode> {
        private WorldState w;
        private int moves;
        private SearchNode prev;

        public SearchNode(WorldState w, int moves, SearchNode prev) {
            this.w = w; //u can't instantiate an interface!
            this.moves = moves;
            this.prev = prev;
        }

        public int estimatedDistanceToGoal() {
            return w.estimatedDistanceToGoal();
        }

        public Iterable<WorldState> neighbors() {
            return w.neighbors();
        }

        @Override
        public int compareTo(SearchNode s) {
            if ((this.estimatedDistanceToGoal() + this.moves) < (s.estimatedDistanceToGoal() + s.moves)) {
                return -1;
            } else if ((this.estimatedDistanceToGoal() + this.moves) > (s.estimatedDistanceToGoal() + s.moves)) {
                return 1;
            } else {
                return 0;
            }
        }



    }




}

