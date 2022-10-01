import java.util.Arrays;

/**
 * Asterisk Sudoku solver.
 *
 * Prints the number of solutions of a Sudoku if there are multiple solutions.
 * If there is only a single solution, it prints this solution instead.
 *
 * @author NAME
 * @id ID
 * @author NAME
 * @id ID
 */
class SudokuSolver {
    // Size of the grid.
    static final int SUDOKU_SIZE = 9;
    // Minimum digit to be filled in.
    static final int SUDOKU_MIN_NUMBER = 1;
    // Maximum digit to be filled in.
    static final int SUDOKU_MAX_NUMBER = 9;
    // Dimension of the boxes, i.e., the sub-grids that should contain all digits.
    static final int SUDOKU_BOX_DIMENSION = 3;

    // The puzzle grid; 0 represents empty.
    // This particular grid has exactly one solution.
    // Other grids might have multiple solutions.
    int[][] grid = new int[][] {
            {0, 9, 0, 7, 3, 0, 4, 0, 0},
            {0, 0, 0, 0, 0, 0, 5, 0, 0},
            {3, 0, 0, 0, 0, 6, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 6, 4, 0},
            {0, 0, 0, 6, 5, 1, 0, 0, 0},
            {0, 0, 6, 9, 0, 7, 0, 0, 0},
            {5, 8, 0, 0, 0, 0, 0, 0, 0},
            {9, 0, 0, 0, 0, 3, 0, 2, 5},
            {6, 0, 3, 0, 0, 0, 8, 0, 0},
    };

    // Solution counter
    int solutionCounter = 0;

    /**
     * Checks if the current position is an Asterisk square or not.
     *
     * @param r row index
     * @param c column index
     */
    boolean isAsteriskPosition(int r, int c) {
        if (c == 4) {
            return r == 1 || r == 4 || r == 7;
        } else if (r == 4) {
            return c == 1 || c == 7;
        } else {
            return (r == 2 && c == 2) || (r == 2 && c == 6) || (r == 6 && c == 2) || (r == 6 && c == 6);
        }
    }

    /**
     * Prints this Sudoku.
     */
    void print() {
        int ok = 1;
        int l = -1;
        for (int i = 0; i < 13; i++) {
            if (i == 0 || i == 4 || i == 8 || i == 12) {
                System.out.println("+-----------------+");
            }
            int k = 0;
            for (int j = 0; j < 19 && i != 0 && i != 4 && i != 8 && i != 12; j++) {
                if (j == 0 || j == 6 || j == 12) {
                    System.out.print("|");
                } else if (j == 18) {
                    System.out.println("|");
                } else if (j == 5 || j == 11 || j == 17) {
                    System.out.print(grid[l][k]);
                    k++;
                } else {
                    System.out.print(grid[l][k] + " ");
                    k++;
                    j++;
                }
                ok =1;
            }
            if (ok == 1) {
                l++;
            }
            ok = 0;
        }
    }

    /**
     * Determine if there's a conflict when we fill in d at position (r, c).
     *
     * @param r row index
     * @param c column index
     * @param d value
     * @return true if there's a conflict, false otherwise
     */
    boolean givesConflict(int r, int c, int d) {
        return grid[r][c] == d;
    }

    /**
     * Determine if there's a conflict when we fill in d in row r.
     *
     * @param r row index
     * @param d value
     * @return true if there's a conflict, false otherwise
     */
    boolean rowConflict(int r, int d) {
        for(int i = 0; i < SUDOKU_SIZE; i++){
            if (grid[r][i] == d) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if there's a conflict when we fill d in column c.
     *
     * @param c column index
     * @param d value
     * @return true if there's a conflict, false otherwise
     */
    boolean colConflict(int c, int d) {
        for(int i = 0; i < SUDOKU_SIZE; i++){
            if (grid[i][c] == d) {
                return true;
            }
        }
        return false;
    }

    int[][] centers = {{1, 1}, {1, 4}, {1, 7}, {4, 1}, {4, 4}, {4, 7}, {7, 1}, {7, 4}, {7, 7}};

    /**
     * Finds the center of the box the point is in.
     * @param r the row index
     * @param c the column index
     * @return returns the coordinate of the center of the box
     */
    int[] closestCenter(int r, int c) {
        int[][] positionsNear = {
                {r-1, c-1},{r-1, c},{r-1, c+1},{r, c-1},
                {r, c+1},{r+1, c-1},{r+1, c},{r+1, c+1}
        };
        for(int[] position:positionsNear) {
            for(int[] center:centers){
                 if(Arrays.equals(position, center)){
                     return position;
                 }
            }
        }
        return new int[]{r, c};
    }

    /**
     * Determine if there's a conflict when we fill d in box at (r, c).
     *
     * @param r row index
     * @param c column index
     * @param d value
     * @return true if there's a conflict, false otherwise
     */
    boolean boxConflict(int r, int c, int d) {
        int[] center = closestCenter(r, c);
        int[][] positionsNear = {
                {center[0] - 1, center[1] - 1},{center[0] - 1, center[1]},{center[0] - 1, center[1] + 1},{center[0], center[1] - 1},
                {center[0], center[1] + 1},{center[0] + 1, center[1] - 1},{center[0] + 1, center[1]},{center[0] + 1, center[1] + 1}
        };

        for (int[] position : positionsNear) {
            if (grid[position[0]][position[1]] == d) {
                return true;
            }
        }
        return false;
    }
    /**
     * Determine if there's a conflict in the asterisk when we fill in d.
     *
     * @param row row index
     * @param col col index
     * @param d value
     * @return true if there's a conflict, false otherwise
     */
    boolean asteriskConflict(int row, int col, int d) {
        for(int i = 1; i < SUDOKU_SIZE - 1; i++) {
            for(int j = 1; j < SUDOKU_SIZE - 1; j++) {
                if (isAsteriskPosition(i, j) && grid[i][j] == d && i != row && j != col) {
                    return true;
                }
            }
        }
        return false;
    }
    int rEmpty;
    int cEmpty;

    /**
     * Find the next empty square in "reading order".
     *
     * @return coordinates of the next empty square
     */
    int[] findEmptySquare() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (grid[i][j] == 0){
                    rEmpty = i;
                    cEmpty = j;
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    /**
     * Determine if sudoku is filled in completely or not.
     *
     * @return true if there are no empty cells left.
     */
    boolean filledSudoku() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (grid[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Find all solutions for the grid.
     *
     * Stores the final solution.
     */
    void solve() {
    }
    /**
     * Run the solver and output the results.
     */
    void solveIt() {
        solve();
        print();
    }
    public static void main(String[] args) {
        (new SudokuSolver()).solveIt();
    }
}
