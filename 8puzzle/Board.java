/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {

    private int dimension;
    private int[][] board;

    // 在Board的生存期间不会被改变，hamming距离和manhattan距离也不会被改变
    // 只计算一次并保存下来，避免重复计算带来不必要的开销
    private int hammingDistance;
    private int manhattanDistance;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // 可以假设tiles是一个n*n的数组
    // 0所在的位置视为空位
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        board = new int[dimension][dimension];

        hammingDistance = 0;
        manhattanDistance = 0;

        int i, j;
        for (i = 0; i < dimension; i++) {
            for (j = 0; j < dimension; j++) {

                // 将二维数组拷贝到对象的私有成员中
                board[i][j] = tiles[i][j];

                // 计算每个格板的hamming距离，忽视空格占位
                if (board[i][j] != i * dimension + j + 1 && !(i == dimension - 1
                        && j == dimension - 1)) {
                    hammingDistance += 1;
                }

                // 计算每个格板的manhattan距离，同样忽视空格占位
                int expectedRow;
                int expectedCol;

                if (board[i][j] == 0) {
                    expectedRow = i;
                    expectedCol = j;
                }
                else {
                    expectedRow = (board[i][j] - 1) / dimension;
                    expectedCol = (board[i][j] - 1) % dimension;
                }
                manhattanDistance += Math.abs(expectedRow - i) + Math.abs(expectedCol - j);
            }
        }

    }

    // string representation of this board
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append(dimension);
        sbuild.append("\n");

        int i, j;
        for (i = 0; i < dimension; i++) {
            sbuild.append(" ");
            for (j = 0; j < dimension; j++) {
                sbuild.append(board[i][j]);
                if (j != dimension - 1) {
                    sbuild.append("  ");
                }
            }
            sbuild.append("\n");
        }

        return sbuild.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {

        // 判断是否空指针
        if (y == null) {
            return false;
        }

        // 判断对象是否同一类型
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board other = (Board) y;

        // 分别先检测数组长度、hamming距离、manhattan距离
        // 初步判断两个board是否相等，减少进入两层循环的次数
        if (other.dimension() != dimension) {
            return false;
        }
        if (this.hammingDistance != other.hammingDistance) {
            return false;
        }
        if (this.manhattanDistance != other.manhattanDistance) {
            return false;
        }
        if (this == other) {
            return true;
        }

        // 使用二重循环，遍历检查数组中的每个位置是否相等
        int i, j;
        for (i = 0; i < dimension; i++) {
            for (j = 0; j < dimension; j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // 首先查找0所在的位置，即空格所在的行和列
        // 使用四个if，对行号和列号分别进行边界检查，判断能否将空格往上、下、左、右移动，相当于和上、下、左、右的板块交换位置
        // 在可以移动的方向上new一个Board，加入到iterable中

        ArrayList<Board> set = new ArrayList<>();
        int[][] neighbor = new int[dimension][dimension];

        int i, j;
        int row = 0, col = 0;

        // 查找0（空格占位）所在的行和列
        for (i = 0; i < dimension; i++) {
            for (j = 0; j < dimension; j++) {
                neighbor[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        // 尝试将空格上下左右的格板移动到空格所在位置，得到一个邻居
        if (row != 0) {
            neighbor[row - 1][col] = board[row][col];
            neighbor[row][col] = board[row - 1][col];

            set.add(new Board(neighbor));

            neighbor[row][col] = board[row][col];
            neighbor[row - 1][col] = board[row - 1][col];
        }

        if (row != dimension - 1) {
            neighbor[row + 1][col] = board[row][col];
            neighbor[row][col] = board[row + 1][col];

            set.add(new Board(neighbor));

            neighbor[row][col] = board[row][col];
            neighbor[row + 1][col] = board[row + 1][col];
        }

        if (col != 0) {
            neighbor[row][col - 1] = board[row][col];
            neighbor[row][col] = board[row][col - 1];

            set.add(new Board(neighbor));

            neighbor[row][col] = board[row][col];
            neighbor[row][col - 1] = board[row][col - 1];
        }

        if (col != dimension - 1) {
            neighbor[row][col + 1] = board[row][col];
            neighbor[row][col] = board[row][col + 1];

            set.add(new Board(neighbor));

            neighbor[row][col] = board[row][col];
            neighbor[row][col + 1] = board[row][col + 1];
        }

        return set;
    }

    // a board that is obtained by exchanging any pair of tiles
    // 可以假设边长在2到128之间
    public Board twin() {
        int[][] tiles = new int[dimension][dimension];

        // 拷贝格板的布局
        int row = 0, col = 0;
        for (row = 0; row < dimension; row++) {
            for (col = 0; col < dimension; col++) {
                tiles[row][col] = board[row][col];
            }
        }

        int row1 = 0, col1 = 0, row2 = 0, col2 = 0;

        // 从左到右、从上到下查找第二个不为0的格板位置从左到右、从上到下查找第一个不为0的格板位置
        for (row1 = 0; row1 < dimension; row1++) {
            for (col1 = 0; col1 < dimension; col1++) {
                if (tiles[row1][col1] != 0) {
                    break;
                }
            }
            if (col1 == dimension) continue;
            if (tiles[row1][col1] != 0) {
                break;
            }
        }

        // 从左到右、从上到下查找第二个不为0的格板位置
        for (row2 = 0; row2 < dimension; row2++) {
            for (col2 = 0; col2 < dimension; col2++) {
                if ((tiles[row2][col2] != 0) && !(row1 == row2
                        && col1 == col2)) {
                    break;
                }
            }
            if (col2 == dimension) continue;
            if ((tiles[row2][col2] != 0) && !(row1 == row2
                    && col1 == col2)) {
                break;
            }
        }

        // 交换两个格板的位置
        tiles[row1][col1] = board[row2][col2];
        tiles[row2][col2] = board[row1][col1];

        return new Board(tiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board test = new Board(tiles);

        System.out.println(test.toString());
        System.out.printf("isGoal=%b\n", test.isGoal());
        System.out.printf("hamming=%d\n", test.hamming());
        System.out.printf("manhattan=%d\n", test.manhattan());

        System.out.println("-----twin-----");
        System.out.println(test.twin().toString());

        System.out.println("-----neighbors-----");
        ArrayList<Board> neighbors = (ArrayList<Board>) test.neighbors();
        for (Board neighbor : neighbors) {
            System.out.println(neighbor.toString());
        }

    }

}
