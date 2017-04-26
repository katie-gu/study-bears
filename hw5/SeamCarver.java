import edu.princeton.cs.algs4.Picture;

//import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Created by jhinukbarman on 4/22/17.
 */
public class SeamCarver {
    private Picture picture;
    private Picture original;
    private boolean transposed;
    private int width;
    private int height;
    private double[][] energies;
    //WeightedQuickUnionUF w;
    //int[][] energies;

    public SeamCarver(Picture picture) {
        original = new Picture(picture);
        energies = new double[height()][width()];
       // System.out.println("original energies: ");
        for (int r = 0; r < energies.length; r++) {
            for (int c = 0; c < energies[0].length; c++) {
                energies[r][c] = energy(c, r);
               // System.out.print(energies[r][c] + " ");
            }
           // System.out.println();
        }
        //this.picture = picture;
        //this.energies = new int[picture.height()][picture.width()];
    }

    public Picture picture() {
        return new Picture(original);
    }

    // width of current picture
    public int width()   {
        return original.width();
    }

    // height of current picture
    public int height() {
        return original.height();
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


        double redX = original.get(colNext, y).getRed() - original.get(colPrev, y).getRed();
        double greenX = original.get(colNext, y).getGreen() - original.get(colPrev, y).getGreen();
        double blueX = original.get(colNext, y).getBlue() - original.get(colPrev, y).getBlue();

        double redY = original.get(x, rowNext).getRed() - original.get(x, rowPrev).getRed();
        double greenY = original.get(x, rowNext).getGreen() - original.get(x, rowPrev).getGreen();
        double blueY = original.get(x, rowNext).getBlue() - original.get(x, rowPrev).getBlue();

        double xSquared = (redX * redX) + (greenX * greenX) + (blueX * blueX);
        double ySquared = (redY * redY) + (greenY * greenY) + (blueY * blueY);

        return xSquared + ySquared;
    }

    public double getEnergy(int col, int row, boolean transposed) {
        if (transposed) {
            return energy(col, row);
        } else {
            return energy(col, row);
        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposed = true;
        energies = transposeMatrix(energies);
        /*
        System.out.println("new energies: ");
        for (int r = 0; r < energies.length; r++) {
            for (int c = 0; c < energies[0].length; c++) {
                System.out.print(energies[r][c] + " ");
            }
            System.out.println();
        }
        */

        int[] result = findVerticalSeam();
        energies = transposeMatrix(energies);
        transposed = false;
        return result;
        /*
        transposed = true;
        Picture oldPicture = original;
        //System.out.println("height: " + original.height());
        //System.out.println("width: " + original.width());
        original = new Picture(original.height(), original.width());
        //System.out.println("height: " + original.height());
        //System.out.println("width: " + original.width());
        //original.set(original.width(), original.height(), original.getRBG())
        int[] result;
        result = findVerticalSeam();
        original = oldPicture;

        System.out.println("result: ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        */


        //picture = new Picture(picture.width())
        //picture().set(picture().width(), picture().height(), picture().get(0,0));
        //return new int[4];
    }

    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    // public int[][] getMinCostArray;

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] minPath;
        if (transposed) {
            minPath = new double[width()][height()];
        } else {
            minPath = new double[height()][width()];

        }
        int row = 0;
        while (row < minPath.length) {
            for (int col = 0; col < minPath[0].length; col++) {
                if (row == 0) {
                    minPath[row][col] = energies[row][col];
                } else {
                    if (col == 0) {
                        if (col == energies[0].length - 1) {
                            minPath[row][col] = energies[row][col]
                                    + minPath[row - 1][col];
                        } else {
                            minPath[row][col] = energies[row][col]
                                    + Math.min(minPath[row - 1][col], minPath[row - 1][col + 1]);
                        }
                    } else if (col == energies[0].length - 1) {
                        minPath[row][col] = energies[row][col]
                                + Math.min(minPath[row - 1][col], minPath[row - 1][col - 1]);
                    } else {
                        minPath[row][col] = energies[row][col]
                                + Math.min(minPath[row - 1][col],
                                Math.min(minPath[row - 1][col + 1], minPath[row - 1][col - 1]));
                    }
                }
            }
            row += 1;
        }

        /*
        for (int r = 0; r < minPath.length; r++) {
            for (int c = 0; c < minPath[0].length; c++) {
                System.out.print(minPath[r][c] + " ");
            }
            System.out.println();
        }
        */

        int[] result = new int[minPath.length];
        double min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < minPath[0].length; i++) {
            if (minPath[minPath.length - 1][i] < min) {
                min = minPath[minPath.length - 1][i];
                minIndex = i;
            }
        }
        int index = 0;
        int chosenCol = minIndex;
        int r = minPath.length - 1;
        while (r >= 0) {
            if (chosenCol == 0) {
                if (chosenCol == minPath[0].length - 1) {
                    min = minPath[r][chosenCol];
                } else {
                    min = Math.min(minPath[r][chosenCol], minPath[r][chosenCol + 1]);
                }
                if (minPath[r][chosenCol] == min) {
                    result[index] = chosenCol;
                } else {
                    result[index] = chosenCol + 1;
                    chosenCol += 1;
                }
            } else if (chosenCol == minPath[0].length - 1) {
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
                } else if (minPath[r][chosenCol - 1] == min) {
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
        return reverseArray(result);
    }

    private int[] reverseArray(int[] arr) {
        int[] newResult = new int[energies.length];
        for (int i = 0; i < newResult.length; i++) {
            newResult[i] = arr[arr.length - 1 - i];
        }
        return newResult;
    }



    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        //for (int row = 0; row < height(); row++) {
        // picture.get(seam[row], row).
        //}
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {

    }


}
