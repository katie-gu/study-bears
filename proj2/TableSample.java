/**
 * Created by jhinukbarman on 2/16/17.
 */
public class TableSample {
    private int row;
    private int col;
    private int[][] tableArr;


    public TableSample(int row, int col){
        this.row = row;
        this.col = col;
        tableArr = new int[row][col];
    }

    public void addRow(){
        tableArr[row][col] = tableArr[row+1][col];
    }

    public static void main(String[] args){
        TableSample T1 = new TableSample(2,3);
    }


}

