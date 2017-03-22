package hw3.puzzle;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.io.File;

public class WordPuzzleSolver {
    /***********************************************************************
     * Test routine for your Solver class. Uncomment and run to test
     * your basic functionality. Make sure to set your current working directory
     * to be the one containing words10000.txt.
     **********************************************************************/
    public static void main(String[] args) {
        //File f = new File ("words10000.txt");
        //File.setWorkingDir("words10000.txt");

        String start = "horse";
        String goal = "nurse";

        Word startState = new Word(start, goal);
        Solver solver = new Solver(startState);

        StdOut.println("Minimum number of moves = " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }
    }

}
