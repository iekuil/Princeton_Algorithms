/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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
    // 对于低位优先和高位优先的字符串排序，运行时间跟NW或Nw有关系，N为字符串数量，w为平均长度，W为最大长度
    // 在此处w == W == N，相当于会有平方级别的时间复杂度
    //
    // 考虑通过重新实现三向快速排序来实现字符串排序（时间复杂度N到NlogN之间，空间复杂度logN）
    //

    // circular suffix array of s
    public CircularSuffixArray(String s)

    // length of s
    public int length()

    // returns index of ith sorted suffix
    public int index(int i)

    // unit testing (required)
    public static void main(String[] args)

}
