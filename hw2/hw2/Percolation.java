package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] arr;
    private WeightedQuickUnionUF weightedU; //keep track of what is full and what isnt
    private WeightedQuickUnionUF percolateTracker;
    private int topUnion;
    private int bottomUnion;
    private int count;


    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new IllegalArgumentException("Error: Illegal Argument");
        }
        arr = new boolean[N][N];
        weightedU = new WeightedQuickUnionUF(10000000);
        percolateTracker = new WeightedQuickUnionUF(10000000);
        topUnion = 9999;
        bottomUnion = 8888;
        count = 0;
    }

    public void open(int row, int col) {      // open the site (row, col) if it is not open already
        if (((row > arr.length - 1) || row < 0) || ((col > arr.length - 1) || col < 0)) {
            throw new IndexOutOfBoundsException("Row or Col Index out of bounds!");
        }
        if (!arr[row][col]) { // changed to simpler expression
            int num = rcto1D(row, col);
            if (row == 0) {
                weightedU.union(num, topUnion);
                percolateTracker.union(num, topUnion);
            }

            arr[row][col] = true;
            checkNeighbors(row, col, num);
            if (row == (arr.length - 1)) {
                //if (isFull(row, col)) {
                weightedU.union(num, bottomUnion);
                // else {
                    //percolateTracker.union(num, bottomUnion);
                    //bottomCheck = num;
                //}
            }
            //if (weightedU.connected(num, bottomCheck) && isFull(row, col)) {
               // weightedU.union(num, bottomUnion);
            //}
            //prev = arr[row][col];

            count += 1;
        }
    }


    private void checkNeighbors(int row, int col, int numConvert) {
        int newNum;
        if (col == 0) {
            checkNeighborsZeroCol(row, col, numConvert);
        } else if (row == 0) {
            checkNeighborsZeroRow(row, col, numConvert);
        } else if (col == arr[0].length - 1) {
            checkNeighborsLastRow(row, col, numConvert);
        } else if (row == 0) {
            if (isOpen(row, col + 1)) {
                newNum = rcto1D(row, col + 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row, col - 1)) {
                newNum = rcto1D(row, col - 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row + 1, col)) {
                newNum = rcto1D(row + 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        } else if (row == arr.length - 1) {
            if (isOpen(row, col + 1)) {
                newNum = rcto1D(row, col + 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row, col - 1)) {
                newNum = rcto1D(row, col - 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        } else {
            if (isOpen(row, col + 1)) {
                newNum = rcto1D(row, col + 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row, col - 1)) {
                newNum = rcto1D(row, col - 1);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row + 1, col)) {
                newNum = rcto1D(row + 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        }

    }

    private void checkNeighborsZeroCol(int row, int col, int numConvert) {
        int newNum;
        if ((isOpen(row, col + 1))) {
            newNum = rcto1D(row, col + 1);
            weightedU.union(numConvert, newNum);
            percolateTracker.union(numConvert, newNum);
        }
        if ((row != 0) && (row != arr.length - 1)) {
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row + 1, col)) {
                newNum = rcto1D(row + 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        }

    }

    private void checkNeighborsZeroRow(int row, int col, int numConvert) {
        int newNum;
        if (isOpen(row + 1, col)) {
            newNum = rcto1D(row + 1, col);
            weightedU.union(numConvert, newNum);
            percolateTracker.union(numConvert, newNum);
        } else { // row = arr.length - 1
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        }
    }

    private void checkNeighborsLastRow(int row, int col, int numConvert) {
        int newNum;
        if ((isOpen(row, col - 1))) {
            newNum = rcto1D(row, col - 1);
            weightedU.union(numConvert, newNum);
            percolateTracker.union(numConvert, newNum);
        }
        if ((row != 0) && (row != (arr.length - 1))) {
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
            if (isOpen(row + 1, col)) {
                newNum = rcto1D(row + 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }

        } else if (row == 0) {
            if (isOpen(row + 1, col)) {
                newNum = rcto1D(row + 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        } else { // row = arr.length - 1
            if (isOpen(row - 1, col)) {
                newNum = rcto1D(row - 1, col);
                weightedU.union(numConvert, newNum);
                percolateTracker.union(numConvert, newNum);
            }
        }

    }


    private int rcto1D(int r, int c) {
        return (r * arr[0].length) + (c + 1);
    }

    public boolean isOpen(int row, int col) {  // is the site (row, col) open?
        //System.out.println("Length of array: " + arr.length);
        if (((row > arr.length - 1) || row < 0) || ((col > arr.length - 1) || col < 0)) {
            throw new IndexOutOfBoundsException("Row or Col Index out of bounds!");
        }
        return arr[row][col];
    }
    public boolean isFull(int row, int col) { // is the site (row, col) full?
        if (((row > arr.length - 1) || row < 0) || ((col > arr.length - 1) || col < 0)) {
            throw new IndexOutOfBoundsException("Row or Col Index out of bounds!");
        }
        int num = rcto1D(row, col);
        return percolateTracker.connected(num, topUnion);


    }

    public int numberOfOpenSites() {        // number of open sites
        return count;
    }

    //fix percolates method!
    public boolean percolates() { // does the system percolate?
        return ((weightedU.connected(topUnion, bottomUnion))
                && (percolateTracker.find(topUnion) != topUnion));
            //return true;
        //} else {
        //    return false;
        //}
    }


    public static void main(String[] args) {
        //junit tests
        //testOpen();
        Percolation p = new Percolation(3);
        System.out.println(p.isFull(0, 2));
        //p.open(2, 2);
        //p.open(0, 1);
        //p.open(0, 2);
        //p.open(1, 2);
        //p.open(0,2);
        //p.open(1, 1);
        //p.open(1, 2);
        //p.open(2, 0);
        //p.open(2, 1);
        //p.open(2, 2);
        System.out.println("Percolates? " + p.percolates());

        System.out.println(p.numberOfOpenSites());

        System.out.println("********");
        /*
        WeightedQuickUnionUF w = new WeightedQuickUnionUF(10000);
        w.union(3,4);
        w.union(4,5);
        System.out.println("Are 3 and 6 now connected? : " + w.connected(3,6));
        */
    }




}                       
