/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // 对于一个队列，初始化顺序为字母表顺序
    // 输入一个字符串，
    // 每读取一个字母，输出队列中该字母对应的位置，
    // 并将该字母移动到队列的顶部
    //
    // 记n为字符串长度，R为字母表大小（此处为256，即扩展ascii的大小）
    // 对于编码和解码，都有：
    //    要求最坏情况下时间复杂度为nR，
    //    典型文本场景下时间复杂度为n+R，
    //    空间复杂度为n+R
    //
    // ->
    //    维护一个大小为R的链表，
    //    每读取一个新的字母，从开头开始搜寻链表，并将其移动到数组开头
    //    在最坏情况下（字符串中极少有连续的字符）
    //    平均每个字母需要R/2次查找，常数次链表指针赋值操作
    //    即时间复杂度满足nR的需求

    private static int radix = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        CharSequence sequence = new CharSequence();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(sequence.getIndexAndSetFirst(c));
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        CharSequence sequence = new CharSequence();
        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            BinaryStdOut.write(sequence.getCharAndSetFirst(i));
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        if (args[0].equals("+")) {
            decode();
        }
        if (args[0].equals("-")) {
            encode();
        }
    }

}
