import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.HashMap;
/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Node</code>s representing the shortest path from st to dest.
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */



    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {

        Long goalID = g.closest(destlon, destlat);
        Node goalNode = g.h.get(goalID);
     //   System.out.println("goal id: " + goalID);

        Long startID = g.closest(stlon, stlat);
        Node startNode = g.h.get(startID);
     //   System.out.println("start id: " + startID);


        class SearchNode implements Comparable<SearchNode> {
            private Node n;
            private double estimatedDistFromStart; //moves from initial to current node..heuristic
            private SearchNode prev;
            private double estimatedDistToGoal;

            SearchNode(Node n, double estimatedDistanceFromStart, SearchNode prev) {
                this.n = n;
                this.estimatedDistFromStart = estimatedDistanceFromStart;
                this.prev = prev;
                this.estimatedDistToGoal = g.distance(n.getId(), goalID);
            }

            @Override
            public int compareTo(SearchNode s) {
                Double nodeA =  this.estimatedDistToGoal + this.estimatedDistFromStart;
                Double nodeB =  s.estimatedDistToGoal + s.estimatedDistFromStart;
                if (nodeA < nodeB) {
                    return -1;
                } else if (nodeA > nodeB) {
                    return 1;
                } else {
                    return 0;
                }
                //return (int) (nodeA - nodeB);
            }

        }

        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        LinkedList<Long> nodeList = new LinkedList<>();
        HashMap<Long, SearchNode> visitedNodes = new HashMap<>();

        SearchNode parent = null;
        Node curr = startNode;
        SearchNode currNode = new SearchNode(curr, 0, null);
        pq.add(currNode);

        //pq.add(currNode);
        while (!pq.isEmpty()) {
            currNode = pq.poll();
            //visitedNodes.put(currNode.n.getId(), currNode);
            if ((currNode.n.getId()).equals(goalID)) {
                break;
            }

            for (Node n : currNode.n.adj) {
                if ((currNode.n.getId()).equals(startNode.getId()) || (!(n.getId().equals(currNode.prev.n.getId())))) {
                    if (visitedNodes.keySet().contains(n.getId())) {
                        SearchNode next = new SearchNode(n, currNode.estimatedDistFromStart + g.distance(currNode.n.getId(), n.getId()), currNode);
                        if (next.estimatedDistFromStart < visitedNodes.get(n.getId()).estimatedDistFromStart) {
                            pq.add(next);
                            visitedNodes.put(currNode.n.getId(), currNode);
                        }

                    } else {
                        SearchNode next = new SearchNode(n, currNode.estimatedDistFromStart + g.distance(currNode.n.getId(), n.getId()), currNode);
                        pq.add(next);
                        visitedNodes.put(currNode.n.getId(), currNode);
                    }
                }


            }


            /*
            for (Node n: curr.adj) {
                if (visitedNodes.keySet().contains(n.getId())) {
                    //first node equals, not equals to second node
                    SearchNode next = new SearchNode(n, currNode.estimatedDistFromStart + g.distance(curr.getId(), n.getId()), currNode);
                    if (next.estimatedDistFromStart < visitedNodes.get(n.getId()).estimatedDistFromStart) {
                        pq.add(next);
                    }
                } else {
                    if (currNode.prev == null) { //change. keep the prev val somehow?
                        SearchNode next = new SearchNode(n, currNode.estimatedDistFromStart + g.distance(curr.getId(), n.getId()), currNode);
                        pq.add(next);
                    } else if (!(n.equals(currNode.prev.n))) {
                        SearchNode next = new SearchNode(n, currNode.estimatedDistFromStart + g.distance(curr.getId(), n.getId()), currNode);
                        pq.add(next);
                    }
                }

            }
            */

            parent = currNode;

        }

        while (parent != null) {
            nodeList.addFirst(parent.n.getId());
            //System.out.println("Goal dist: " + parent.w.estimatedDistanceToGoal());
            parent = parent.prev;
        }

        nodeList.addLast(goalID);



       /*

       public Solver(WorldState initial) {
        this.curr = initial;
        m = new MinPQ<>();
        arr = new LinkedList<>();
        SearchNode currNode = new SearchNode(curr, 0, null);
        m.insert(currNode);

        while (!m.isEmpty()) {
            currNode = m.delMin();
            if (currNode.w.isGoal()) {
                break;
            }
            for (WorldState n : currNode.w.neighbors()) {
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
            //System.out.println("Goal dist: " + parent.w.estimatedDistanceToGoal());
            parent = parent.prev;
        }
    }



        */
        return nodeList;


    }
}
