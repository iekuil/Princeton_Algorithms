/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // 平均值
    private double meanValue;

    // 标准差
    private double stdDevValue;

    // 置信区间下界
    private double confidenceIntervalLo;

    // 置信区间上届
    private double confidenceIntervalHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // 进行trials次for循环，
        //    在每个for循环中，使用while循环直到percolation的percolates()返回真
        //    将每个percolation的number of open sites记录到数组
        // 计算平均数、标准差、置信区间并存储到成员变量

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("initial n <= 0 for percolationStats\n");
        }

        double[] openPercent = new double[trials];
        meanValue = 0;
        stdDevValue = 0;
        confidenceIntervalLo = 0;
        confidenceIntervalHi = 0;

        for (int i = 0; i < trials; i++) {
            Percolation singleTest = new Percolation(n);
            while (!singleTest.percolates()) {
                singleTest.open(StdRandom.uniformInt(1, n + 1), StdRandom.uniformInt(1, n + 1));
            }
            openPercent[i] = (double) singleTest.numberOfOpenSites() / (n * n);
        }

        meanValue = StdStats.mean(openPercent);
        stdDevValue = StdStats.stddev(openPercent);

        double tmp = 1.96 * Math.sqrt(stdDevValue) / Math.sqrt(trials);
        confidenceIntervalLo = meanValue - tmp;
        confidenceIntervalHi = meanValue + tmp;
    }

    // sample mean of percolation threshold
    public double mean() {
        // 返回记录平均数的成员变量
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // 返回记录标准差的成员变量
        return stdDevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        // 返回记录置信区间左端的成员变量
        return confidenceIntervalLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // 返回记录置信区间右端的成员变量
        return confidenceIntervalHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }
        PercolationStats perc = new PercolationStats(Integer.parseInt(args[0]),
                                                     Integer.parseInt(args[1]));
        StdOut.printf("mean                    = %f\n", perc.mean());
        StdOut.printf("stddev                  = %f\n", perc.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", perc.confidenceLo(),
                      perc.confidenceHi());
    }

}
