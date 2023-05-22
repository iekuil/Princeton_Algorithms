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
    // 使用两个wqn是因为有可能出现
    //      [head virtual site] -> [...] -> [one site in the last row] -> [tail virtual site] -> [another site in the last row]
    // 在这样的连接通路下，
    // 当调用isFull()时，
    // 即使位于最后一排的另一个site本身并没有真的处于full状态，也会返回true
    //
    // 另外一种防止这种情况出现的思路是去除末端的虚拟site，
    // 当调用percolate()方法时，
    // 利用for循环逐个检查起始虚拟site和最后一排的site之间的连通关系，
    // 但是这种做法的效率很低，
    // 过不了timing和内存访问的tests
    private WeightedQuickUnionUF wqnWithTailVirtualSite;
    private WeightedQuickUnionUF wqnWithoutTailVirtualSite;

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
        wqnWithTailVirtualSite = new WeightedQuickUnionUF(n * n + 1 + 1);
        wqnWithoutTailVirtualSite = new WeightedQuickUnionUF(n * n + 1);

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

        if (colInArray != 0) {
            if (isOpen(row, col - 1)) {
                wqnWithTailVirtualSite.union(convert(rowInArray, colInArray),
                                             convert(rowInArray, colInArray - 1));
                wqnWithoutTailVirtualSite.union(convert(rowInArray, colInArray),
                                                convert(rowInArray, colInArray - 1));
            }
        }

        if (colInArray != nSize - 1) {
            if (isOpen(row, col + 1)) {
                wqnWithTailVirtualSite.union(convert(rowInArray, colInArray),
                                             convert(rowInArray, colInArray + 1));
                wqnWithoutTailVirtualSite.union(convert(rowInArray, colInArray),
                                                convert(rowInArray, colInArray + 1));
            }
        }

        if (rowInArray == 0) {
            wqnWithTailVirtualSite.union(0, convert(rowInArray, colInArray));
            wqnWithoutTailVirtualSite.union(0, convert(rowInArray, colInArray));
        }
        else {
            if (isOpen(row - 1, col)) {
                wqnWithTailVirtualSite.union(convert(rowInArray, colInArray),
                                             convert(rowInArray - 1, colInArray));
                wqnWithoutTailVirtualSite.union(convert(rowInArray, colInArray),
                                                convert(rowInArray - 1, colInArray));
            }
        }

        if (rowInArray == nSize - 1) {

            wqnWithTailVirtualSite.union(convert(rowInArray, colInArray), nSize * nSize + 1);
        }
        else {
            if (isOpen(row + 1, col)) {
                wqnWithTailVirtualSite.union(convert(rowInArray, colInArray),
                                             convert(rowInArray + 1, colInArray));
                wqnWithoutTailVirtualSite.union(convert(rowInArray, colInArray),
                                                convert(rowInArray + 1, colInArray));
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
        return wqnWithoutTailVirtualSite.find(0) == wqnWithoutTailVirtualSite.find(
                convert(row - 1, col - 1));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // 需要维护一个额外的成员变量用于统计
        return openSitesNumber;
    }

    // does the system percolate?
    public boolean percolates() {
        // 测试开头的虚拟site和末端的虚拟site能否连通
        return wqnWithTailVirtualSite.find(0) == wqnWithTailVirtualSite.find(nSize * nSize + 1);
    }

    private int convert(int row, int col) {
        return row * nSize + col + 1;
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
