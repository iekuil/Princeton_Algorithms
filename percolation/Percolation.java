/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // 矩阵边长n
    private int nSize;

    // 连通状态
    private WeightedQuickUnionUF wqn;

    // 记录site的状态（open/close）
    private boolean[][] state;

    // 统计当前处于open状态的site数量
    private int openSitesNumber;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // 初始化矩阵边长n
        // 初始化site状态矩阵（open/close）
        // 初始化 WeightedQuickUnionUF 对象，需要在开头和结尾各增加一个虚拟site

        if (n <= 0) {
            throw new IllegalArgumentException("initial n <= 0 for percolation\n");
        }

        nSize = n;
        wqn = new WeightedQuickUnionUF(n * n + 2);
        state = new boolean[n][n];
        openSitesNumber = 0;

        for (int i = 0; i < nSize; i++) {
            for (int j = 0; j < nSize; j++) {
                state[i][j] = false;
            }
        }

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // 检查指定site的状态
        // 检查指定site四周的状态（上下左右）
        // 当指定site周围有site处于open状态时，使用 WeightedQuickUnionUF 创建连接
        // 当指定site位于第一排或者最后一排的时候，还需要将指定site和开头或结尾的虚拟site进行连接

        if (row < 1 || col < 1 || row > nSize || col > nSize) {
            throw new IllegalArgumentException("row/col out of range in open()\n");
        }

        if (isOpen(row, col)) {
            return;
        }

        int rowInArray = row - 1;
        int colInArray = col - 1;

        state[row - 1][col - 1] = true;
        openSitesNumber += 1;

        if (rowInArray == 0) {
            wqn.union(0, convert(rowInArray, colInArray));
        }
        else {
            if (isOpen(row - 1, col)) {
                wqn.union(convert(rowInArray, colInArray), convert(rowInArray - 1, colInArray));
            }
        }

        if (rowInArray == nSize - 1) {
            wqn.union(convert(rowInArray, colInArray), nSize * nSize + 1);
        }
        else {
            if (isOpen(row + 1, col)) {
                wqn.union(convert(rowInArray, colInArray), convert(rowInArray + 1, colInArray));
            }
        }

        if (colInArray != 0) {
            if (isOpen(row, col - 1)) {
                wqn.union(convert(rowInArray, colInArray), convert(rowInArray, colInArray - 1));
            }
        }

        if (colInArray != nSize - 1) {
            if (isOpen(row, col + 1)) {
                wqn.union(convert(rowInArray, colInArray), convert(rowInArray, colInArray + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > nSize || col > nSize) {
            throw new IllegalArgumentException("row/col out of range in isOpen()\n");
        }
        return state[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // 满足“full”的site指的是能够间接连通到第一排的site
        // 相当于是能够和开头的虚拟site连通的site
        if (row < 1 || col < 1 || row > nSize || col > nSize) {
            throw new IllegalArgumentException("row/col out of range in isFull()\n");
        }
        return wqn.find(0) == wqn.find(convert(row - 1, col - 1));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // 需要维护一个额外的成员变量用于统计
        return openSitesNumber;
    }

    // does the system percolate?
    public boolean percolates() {
        // 测试开头的虚拟site和末端的虚拟site能否连通
        return wqn.find(0) == wqn.find(nSize * nSize + 1);
    }

    private int convert(int row, int col) {
        return row * nSize + col + 1;
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
