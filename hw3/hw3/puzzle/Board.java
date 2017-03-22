package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] tiles; // final int[][] tiles?
    private int[][] goalBoardArr;
    //private Board goalBoard;
    private int N;
    private int BLANK = 0;


    public Board(int[][] tiles) {
        this.tiles = (int[][]) tiles.clone(); //deep copy the array to make immutable
        //this.tiles = tiles;
        N = tiles.length;
        goalBoardArr = new int[N][N];
        int count = 1;

        //set the goalBoard
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if ((row == N - 1) && (col == N - 1)) {
                    goalBoardArr[row][col] = 0;
                } else {
                    goalBoardArr[row][col] = count;
                    count += 1;
                }
            }
        }


    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException("ERROR: index out of bounds");
        }
        if (tiles[i][j] == BLANK) {
            return 0;
        }
        return tiles[i][j];
    }

    public int size() {
        return tiles.length;
    }

    /*
    Code borrowed from Professor Hug : http://joshh.ug/neighbors.html
    */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;

    }

    public int hamming() {
        int incorrectPos = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tiles[row][col] != goalBoardArr[row][col]) {
                    incorrectPos += 1;
                }
            }
        }
        return incorrectPos;
    }

    private int column(int num, int n) {
        return Math.abs((num % n) - 1);
    }

    private int row(int num, int n) {
        return Math.abs(((num - 1) / n));
    }

    public int manhattan() {
        int num = 0;
        int goalCol = 0;
        int goalRow = 0;
        int manhattanCol = 0;
        int manhattanRow = 0;
        int manhattanEstimate = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if ((tiles[row][col] != goalBoardArr[row][col]) && tiles[row][col] != 0) {
                    num = tiles[row][col];
                    goalCol = column(num, N);
                    goalRow = row(num, N);
                    manhattanCol = Math.abs(row - goalRow);
                    manhattanRow = Math.abs(col - goalCol);
                    manhattanEstimate += manhattanCol + manhattanRow;
                }
            }
        }

        return manhattanEstimate;

    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean isGoal() {
       // System.out.println(this.toString());
        //System.out.println("Manhattan: " + manhattan());
        //goalBoard = new Board(goalBoardArr);
        return manhattan() == 0;
       // return this.equals(goalBoard);
    }

    public boolean equals(Object y) {
        Board b = (Board) y;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (this.tiles[row][col] != b.tiles[row][col]) { //or should i use tilesAt()?
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = size();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
