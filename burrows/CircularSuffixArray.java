/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    // 对于一个长度为n的字符串，
    // 每次将开头的字符移动至队尾，重复n-1次，
    // 形成一个由回环字符串组成的数组，共有n个字符串，
    // 对该数组从小到大进行字符串排序，
    // index()方法需要返回排序后的第n个字符串在排序前的数组中的序号
    //
    // 要求空间复杂度为n+R,
    // 构造函数的时间复杂度为nlogn,
    // length()方法和index()方法的时间复杂度为常数，
    //
    // 对于排序算法的选择有点麻烦，
    // 低位优先的运行时间为NW，额外空间N
    // 高位优先的运行时间N到Nw之间，额外空间N+WR
    // 三向字符串快排的运行时间N到Nw之间，额外空间W+logN
    //
    // 有W=w=N，
    // 可以先排除低位优先，运行时间相当于N的平方
    // 以及高位优先，额外空间相当于N+NR，
    // 从运行时间的要求上看，
    // 三种算法在最坏情况下都没法保证nlogn，
    // （实际上也没明确提出要求保证最坏情况下的运行时间）
    // 高位优先平均需要检查Nlog_R^N个字符
    // 三向字符串快速排序平均需要比较字符2NlnN次
    // 这里考虑三向字符串快速排序
    //

    private String s;
    private int[] index;
    private int length;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("");
        }

        this.s = s;
        this.length = s.length();
        this.index = new int[length];

        for (int i = 0; i < length; i++) {
            index[i] = i;
        }
        sort();
    }

    private int charAt(int stringId, int index) {
        if (stringId < 0 || stringId >= length) {
            throw new IllegalArgumentException("");
        }
        if (index < 0 || index >= length) {
            return -1;
        }
        return s.charAt((stringId + index) % length);
    }

    private void sort() {
        sort(0, length - 1, 0);
    }

    private void sort(int lo, int hi, int d) {
        if (hi <= lo) return;

        int lt = lo, gt = hi;
        int v = charAt(index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(index[i], d);
            if (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }

        sort(lo, lt - 1, d);
        if (v >= 0) sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    private void exch(int i, int j) {
        if (i < 0 || i >= length || j < 0 || j >= length) {
            throw new IllegalArgumentException("");
        }
        int tmp = index[i];
        index[i] = index[j];
        index[j] = tmp;
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = StdIn.readAll();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length;

        StdOut.printf("length = \n", length);
        for (int i = 0; i < length; i++) {
            StdOut.printf("%d ", csa.index(i));
        }
        StdOut.println();
    }


}
