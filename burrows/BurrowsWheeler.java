/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.ResizingArrayQueue;

import java.util.ArrayList;

public class BurrowsWheeler {

    // 正向变换：
    //    给定一个字符串，
    //    从上到下输出排序后的CircularSuffixArray中最后一列的字符t[]，
    //    并给出原字符串在排序后的CircularSuffixArray中的序号
    // 逆向变换：
    //    给定一个从正向变换中得到的字符串，
    //    尝试还原得到原字符串，
    //
    // 性能要求：
    //    空间复杂度要在n+R,
    //    时间复杂度：
    //       正向变换：
    //          （不包含创建CircularSuffixArray的部分）
    //           最坏情况下n+R
    //       逆向变换：
    //            最坏情况下n+R
    //
    // 对于正向变换：
    //    对于str，
    //    变化得到的newStr[i] = str[(str.length + CircularSuffixArray.index(i) - 1) % str.length]
    //    CircularSuffixArray的index方法时间复杂度为常数，
    //    一共需要调用n次，
    //    很容易满足要求，
    //
    // 对于逆向变换：
    //    给定的str为CircularSuffixArray中的最后一列,视为tail[],
    //    需要进行一次排序得到第一列head[]，
    //      使用基数排序的时间复杂度为n，
    //
    //    之后需要得到一个next[]数组，
    //    next[i]中存放着 [排序后的字符串数组]中的[第i行] 在 [排序前的数组]中的[下一行] 在 [排序后的数组]中的[序号]
    //    由于这些字符串都是通过将首字符移动到尾部产生的，
    //    意味着，在[排序前的数组]中，某一行的开头字符将会出现在该行的下一行的末尾，
    //    对于那些在整个字符串之间只出现过一次的字符，
    //    可以根据这一点确定[排序后的数组]中哪两个字符串在[排序前的数组]中是相邻的，
    //
    //    而对于出现过多次的字符，
    //    意味着在[排序后的数组]中，
    //    对于以相同字母开头的字符串i和j，
    //    它们在进行字符串比较的时候实际上比较的是不含开头的部分，
    //    若i<j,
    //    即意味着[i的下一行]在字符串比较中将会[小于][j的下一行]，
    //    即next[i] < next[j]，
    //
    //    为了利用这个规律，
    //    需要维护R个先进先出队列，
    //    首先遍历由CircularSuffixArray中的最后一列组成的字符串，tail[]，
    //    当读取到某个字符时，将该字符所在位置压入该字符对应的队列，
    //    之后遍历head[],
    //    对于head[i]，从该字符中对应的队列中出列得到index，next[i] = index
    //
    //    这个过程需要建立R个先进先出队列，时间复杂度R，
    //    需要两次遍历长度为n的数组，时间复杂度n，
    //    涉及到的队列操作为链表操作，单次操作时间复杂度为常数，所有涉及的队列操作时间复杂度为n

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output

    private static int radix = 256;

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length();
        char[] transformed = new char[length];
        int first = 0;

        for (int i = 0; i < length; i++) {
            int index = csa.index(i);
            if (index == 0) {
                first = i;
            }
            transformed[i] = s.charAt((length + index - 1) % length);
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(transformed[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] tail = BinaryStdIn.readString().toCharArray();
        int length = tail.length;
        char[] head = radixSort(tail);

        ArrayList<ResizingArrayQueue<Integer>> queues = new ArrayList<>();
        for (char i = 0; i < radix; i++) {
            queues.add(new ResizingArrayQueue<>());
        }

        for (int i = 0; i < length; i++) {
            queues.get(tail[i]).enqueue(i);
        }

        int[] next = new int[length];
        for (int i = 0; i < length; i++) {
            next[i] = queues.get(head[i]).dequeue();
        }

        char[] origin = new char[length];
        int originIndex = first;
        for (int i = 0; i < length; i++) {
            origin[i] = head[originIndex];
            originIndex = next[originIndex];
        }

        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(origin[i]);
        }
        BinaryStdOut.close();
    }

    private static char[] radixSort(char[] origin) {
        if (origin == null) {
            throw new IllegalArgumentException("");
        }
        int length = origin.length;

        int[] count = new int[radix + 1];
        char[] res = new char[length];

        for (int i = 0; i < length; i++) {
            count[origin[i] + 1]++;
        }

        for (int r = 0; r < radix; r++) {
            count[r + 1] += count[r];
        }

        for (int i = 0; i < length; i++) {
            res[count[origin[i]]++] = origin[i];
        }

        return res;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }
        if (args[0].equals("-")) {
            transform();
        }
    }

}
