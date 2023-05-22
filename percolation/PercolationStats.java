/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    // 记录每个矩阵在达到percolates状态时处于open状态的百分比
    private double open_percent[];

    // 平均值
    private double mean_value;

    // 标准差
    private double std_dev_value;

    // 置信区间下界
    private double confidence_interval_lo;

    // 置信区间上届
    private double confidence_interval_hi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // 进行trials次for循环，
        //    在每个for循环中，使用while循环直到percolation的percolates()返回真
        //    将每个percolation的number of open sites记录到数组
        // 计算平均数、标准差、置信区间并存储到成员变量

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("initial n <= 0 for percolationStats\n");
        }

        double sum_value = 0;

        open_percent = new double[trials];
        mean_value = 0;
        std_dev_value = 0;
        confidence_interval_lo = 0;
        confidence_interval_hi = 0;

        for (int i = 0; i < trials; i++) {
            Percolation single_test = new Percolation(n);
            while (!single_test.percolates()) {
                single_test.open(StdRandom.uniformInt(n), StdRandom.uniformInt(n));
            }
            open_percent[i] = (double) single_test.numberOfOpenSites() / (n * n);
            sum_value += open_percent[i];
        }

        mean_value = sum_value / trials;
        for (int i = 0; i < trials; i++) {
            std_dev_value += Math.pow(open_percent[i] - mean_value, 2) / (trials - 1);
        }
        double tmp = 1.96 * Math.sqrt(std_dev_value) / Math.sqrt(trials);
        confidence_interval_lo = mean_value - tmp;
        confidence_interval_hi = mean_value + tmp;
    }

    // sample mean of percolation threshold
    public double mean() {
        // 返回记录平均数的成员变量
        return mean_value;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // 返回记录标准差的成员变量
        return std_dev_value;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        // 返回记录置信区间左端的成员变量
        return confidence_interval_lo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // 返回记录置信区间右端的成员变量
        return confidence_interval_hi;
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
