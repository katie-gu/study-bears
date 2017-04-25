import edu.princeton.cs.algs4.Picture;

//import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Created by jhinukbarman on 4/22/17.
 */
public class SeamCarver {
    private Picture picture;
    private Picture original;
    //WeightedQuickUnionUF w;
    //int[][] energies;

    public SeamCarver(Picture picture) {
        original = new Picture(picture);
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




    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        Picture oldPicture = original;
        original = picture();
        int[] result = new int[picture.width()];
        result = findVerticalSeam();
        original = oldPicture;

        System.out.println("result: ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(i + " ");
        }

        return result;

        //picture = new Picture(picture.width())
        //picture().set(picture().width(), picture().height(), picture().get(0,0));
        //return new int[4];
    }


   // public int[][] getMinCostArray;

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seq = new int[height()];
        double[][] minPath = new double[height()][width()];
        int row = 0;
        while (row < height()) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) {
                    minPath[row][col] = energy(col, row);
                } else {
                    if (col == 0) {
                        System.out.println("row :" + row);
                        System.out.println("col :" + col);
                        if (col == width() - 1) {
                            minPath[row][col] = energy(col, row)
                                    + minPath[row - 1][col];
                        } else {
                            minPath[row][col] = energy(col, row)
                                    + Math.min(minPath[row - 1][col], minPath[row - 1][col + 1]);
                        }
                    } else if (col == width() - 1) {
                        minPath[row][col] = energy(col, row)
                                + Math.min(minPath[row - 1][col], minPath[row - 1][col - 1]);
                    } else {
                        minPath[row][col] = energy(col, row)
                                + Math.min(minPath[row - 1][col],
                                Math.min(minPath[row - 1][col + 1],
                                        minPath[row - 1][col - 1]));
                    }
                }
            }
            row += 1;
        }

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
                if (chosenCol == width() - 1) {
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
        int[] newResult = new int[height()];
        for (int i = 0; i < newResult.length; i++) {
            newResult[i] = result[result.length - 1 - i];
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
