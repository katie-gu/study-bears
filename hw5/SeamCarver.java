import edu.princeton.cs.algs4.Picture;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
//import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Created by jhinukbarman on 4/22/17.
 */
public class SeamCarver {
    Picture picture;
    //WeightedQuickUnionUF w;

    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width()   {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y)  {
        int colNext = x + 1;
        int colPrev = x - 1;

        int rowPrev = y - 1;
        int rowNext = y + 1;

        if (colNext >= width()) {
            colNext = 0;
        }

        if (colPrev < 0) {
            colPrev = width() - 1;
        }

        if (rowNext >= height()) {
            rowNext = 0;
        }

        if (rowPrev < 0) {
            rowPrev = height() - 1;
        }


        double redX = picture.get(colNext, y).getRed() - picture.get(colPrev, y).getRed();
        double greenX = picture.get(colNext, y).getGreen() - picture.get(colPrev, y).getGreen();
        double blueX = picture.get(colNext, y).getBlue() - picture.get(colPrev, y).getBlue();

        double redY = picture.get(x, rowNext).getRed() - picture.get(x, rowPrev).getRed();
        double greenY = picture.get(x, rowNext).getGreen() - picture.get(x, rowPrev).getGreen();
        double blueY = picture.get(x, rowNext).getBlue() - picture.get(x, rowPrev).getBlue();

        double xSquared = (redX * redX) + (greenX * greenX) + (blueX * blueX);
        double ySquared = (redY * redY) + (greenY * greenY) + (blueY * blueY);

        return xSquared + ySquared;
    }



    /*
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }
    */

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seq = new int[height()];

        double[][] minPath = new double[height()][width()];
        int row = 0;
        //int col = 0;

        //System.out.println("here");
        //check edge/corner/null cases
        while (row < height()) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) {
                    minPath[row][col] = energy(col, row);
                } else {
                    if (col == 0) {
                        minPath[row][col] = energy(col, row) +
                                Math.min(minPath[row - 1][col], minPath[row - 1][col + 1]);
                        //w.union();
                    } else if (col == width() - 1) {
                        minPath[row][col] = energy(col, row) +
                                Math.min(minPath[row - 1][col], minPath[row - 1][col - 1]);
                    } else {
                        minPath[row][col] = energy(col, row) +
                                Math.min(minPath[row - 1][col], Math.min(minPath[row - 1][col + 1],
                                        minPath[row - 1][col - 1]));
                    }
                }
            }
            row += 1;
        }


        /*
        for (int r = 0; r < height(); r++) {
            for (int c = 0; c < width(); c++) {
                System.out.print(minPath[r][c] + " ");
            }
            System.out.println();
        }
        */

        //find min of last row and work upward
        int[] result = new int[height()];

        double min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < width(); i++) {
            if (minPath[height() - 1][i] < min) {
                min = minPath[height() - 1][i];
                minIndex = i;
            }
        }

        int index = 0;
        int chosenCol = minIndex;
        int r = height() - 1;
        while (r >= 0) {
            if (chosenCol == 0) {

                min = Math.min(minPath[r][chosenCol], minPath[r][chosenCol + 1]);

                if (minPath[r][chosenCol] == min) {
                    result[index] = chosenCol;
                } else {
                    result[index] = chosenCol + 1;
                    chosenCol += 1;
                }
            } else if (chosenCol == width() - 1) {

                min = Math.min(minPath[r][chosenCol], minPath[r][chosenCol - 1]);

                if (minPath[r][chosenCol] == min) {
                    result[index] = chosenCol;
                } else {
                    result[index] = chosenCol - 1;
                    chosenCol -= 1;
                }

            } else {

                min = Math.min(minPath[r][chosenCol],
                        Math.min(minPath[r][chosenCol - 1], minPath[r][chosenCol + 1]));

                if (minPath[r][chosenCol] == min) {
                    result[index] = chosenCol;
                } else if (minPath[r][chosenCol - 1] == min){
                    result[index] = chosenCol - 1;
                    chosenCol -= 1;
                } else {
                    result[index] = chosenCol + 1;
                    chosenCol += 1;
                }

            }

            index += 1;
            r -= 1;


        }
        /*
        TreeMap<Double, int[]> h = new TreeMap<>();

        double min = 0;
        int c = 0;
        for (int r = 0; r < minPath.length; r++) {
            int[] indices = new int[height()];
            double total = 0;

            while (c < height() - 1) {
                if (c == 0) {
                    min = Math.min(minPath[r + 1][c], minPath[r + 1][c + 1]);

                    if (minPath[r + 1][c] == min) {
                        indices[c] = c;
                    } else {
                        indices[c] = c + 1;
                    }

                } else if (c == width() - 1) {
                    min = Math.min(minPath[r + 1][c], minPath[r + 1][c - 1]);

                    if (minPath[r + 1][c] == min) {
                        indices[c] = c;
                    } else {
                        indices[c] = c - 1;
                    }

                } else {
                    min = Math.min(minPath[r + 1][c],
                            Math.min(minPath[r + 1][c - 1], minPath[r + 1][c + 1]));

                    if (minPath[r + 1][c] == min) {
                        indices[c] = c;
                    } else if (minPath[r + 1][c - 1] == min) {
                        indices[c] = c - 1;
                    } else {
                        indices[c] = c + 1;
                    }
                }

                total += min;

                h.put(total, indices);
                c += 1;
            }

        }


        System.out.println("result: ");
        for (int i = 0; i < h.get(h.firstKey()).length; i++) {
            System.out.print(i + " ");
        }

        return h.get(h.firstKey());
        */



       // List<Object> listResult = Arrays.asList(result);
       // Collections.reverse(listResult);

        int[] newResult = new int[height()];
        for (int i = 0; i < newResult.length; i++) {
            newResult[i] = result[result.length - 1 - i];
        }
        //listResult.toArray();

        /*
        System.out.println("result: ");
        for (int i = 0; i < newResult.length; i++) {
            System.out.print(newResult[i] + " ");
        }
        */

        return newResult;
    }


    /*
    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {

    }
    */

}
