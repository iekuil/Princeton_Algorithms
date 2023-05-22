/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;

public class Percolation {
    // 矩阵边长n
    private int N;

    // 连通状态
    private WeightedQuickUnionUF wqn;

    // 记录site的状态（open/close）
    private Boolean state[][];

    // 统计当前处于open状态的site数量
    private int open_sites_number;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // 初始化矩阵边长n
        // 初始化site状态矩阵（open/close）
        // 初始化 WeightedQuickUnionUF 对象，需要在开头和结尾各增加一个虚拟site

        if (n <= 0) {
            throw new IllegalArgumentException("initial n <= 0 for percolation\n");
        }

        N = n;
        wqn = new WeightedQuickUnionUF(n * n + 2);
        state = new Boolean[n][n];
        open_sites_number = 0;

        for (Boolean[] row : state)
            Arrays.fill(row, Boolean.FALSE);

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // 检查指定site的状态
        // 检查指定site四周的状态（上下左右）
        // 当指定site周围有site处于open状态时，使用 WeightedQuickUnionUF 创建连接
        // 当指定site位于第一排或者最后一排的时候，还需要将指定site和开头或结尾的虚拟site进行连接

        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IllegalArgumentException("row/col out of range in open()\n");
        }
        if (isOpen(row, col)) {
            return;
        }

        state[row][col] = true;
        open_sites_number += 1;

        if (row == 0) {
            wqn.union(0, col + 1);
        }
        else {
            if (isOpen(row - 1, col)) {
                wqn.union(row * N + col + 1, (row - 1) * N + col + 1);
            }
        }

        if (row == N - 1) {
            wqn.union(row * N + col + 1, N * N + 1);
        }
        else {
            if (isOpen(row + 1, col)) {
                wqn.union(row * N + col + 1, (row + 1) * N + col + 1);
            }
        }

        if (col != 0) {
            if (isOpen(row, col - 1)) {
                wqn.union(row * N + col + 1, row * N + col - 1 + 1);
            }
        }

        if (col != N - 1) {
            if (isOpen(row, col + 1)) {
                wqn.union(row * N + col + 1, row * N + col + 1 + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IllegalArgumentException("row/col out of range in isOpen()\n");
        }
        return state[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // 满足“full”的site指的是能够间接连通到第一排的site
        // 相当于是能够和开头的虚拟site连通的site
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IllegalArgumentException("row/col out of range in isFull()\n");
        }
        return wqn.find(0) == wqn.find(row * N + col + 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // 需要维护一个额外的成员变量用于统计
        return open_sites_number;
    }

    // does the system percolate?
    public boolean percolates() {
        // 测试开头的虚拟site和末端的虚拟site能否连通
        return wqn.find(0) == wqn.find(N * N + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
