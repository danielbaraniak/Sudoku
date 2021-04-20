package pl.cp.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.cp.sudoku.elements.SudokuBox;
import pl.cp.sudoku.elements.SudokuColumn;
import pl.cp.sudoku.elements.SudokuField;
import pl.cp.sudoku.elements.SudokuRow;



/**
 * Sudoku Board.
 */

public class SudokuBoard {

    public SudokuBoard(SudokuSolver solver) {
        this.solver = solver;
    }

    public void solveGame() {
        solver.solve(this);
    }

    public int[][] getBoard() {

        int[][] copy = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = board[i][j].getValue();
            }
        }

        return copy;
    }

    public int get(int x, int y) {
        return board[x][y].getValue();
    }

    public boolean set(int x, int y, int value) {

        int v = board[x][y].getValue();

        try {
            board[x][y].setValue(value);

            if (!checkBoard()) {
                board[x][y].setValue(v);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private final SudokuSolver solver;

    public SudokuRow getRow(int y) {
        return new SudokuRow(Arrays.asList(board[y]));
    }

    public SudokuColumn getColumn(int x) {
        ArrayList<SudokuField> column = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            column.add(board[i][x]);
        }

        return new SudokuColumn(column);
    }

    public SudokuBox getBox(int x, int y) {
        SudokuField[] box = new SudokuField[9];
        x *= 3;
        y *= 3;
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.board[y + i], x, box, i * 3, 3);
        }
        return new SudokuBox(Arrays.asList(box));
    }

    private final SudokuField[][] board = new SudokuField[9][9];

    {   //initialization block

        //initialize rows and columns
        SudokuRow[] rows = new SudokuRow[9];
        SudokuColumn[] columns = new SudokuColumn[9];
        SudokuBox[] boxes = new SudokuBox[9];

        for (int i = 0; i < 9; i++) {
            rows[i] = new SudokuRow();
            columns[i] = new SudokuColumn();
            boxes[i] = new SudokuBox();
        }

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                board[y][x] = new SudokuField(rows[y], columns[x], boxes[(y / 3) * 3 + (x / 3)]);

                columns[x].addField(board[y][x]);
                rows[y].addField(board[y][x]);
                boxes[(y / 3) * 3 + (x / 3)].addField(board[y][x]);
            }
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SudokuBoard) {
            final SudokuBoard other = (SudokuBoard) obj;
            return new EqualsBuilder()
                    .append(board, other.board)
                    .isEquals();
        } else {
            return false;
        }
    }

    private boolean checkBoard() {
        for (int i = 0; i < 9; i++) {
            if (!getRow(i).verify()) {
                return false;
            }
            if (!getColumn(i).verify()) {
                return false;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!getBox(i, j).verify()) {
                    return false;
                }
            }
        }

        return true;
    }

}