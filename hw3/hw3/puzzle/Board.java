package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] tiles; // final int[][] tiles?
    private int dimensions;
    private int BLANK = 0;


    public Board(int[][] tiles) {
        this.tiles = (int[][]) tiles.clone(); //deep copy the array to make immutable
        dimensions = tiles.length - 1;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > dimensions || j < 0 || j > dimensions) {
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
        return 1;
    }

    public int manhattan() {
        return 1;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean isGoal() {
        return this.isGoal();
    }

    public boolean equals(Object y) {
        Board b = (Board) y;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (this.tileAt(row, col) != b.tileAt(row, col)) {
                    return false;
                }

            }
        }
        return true;
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
