/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class BoggleSolver {

    // 用tries存储字典字符串
    //   由于字符串仅使用26个字母，相当于R=26
    //   可以使用R向单词查找树
    //
    // 对于board中每一个位置的字母，
    //   进行DFS，对八个方向上的字符进行搜索，
    //   若某个方向上的字母与当前出发点组成的前缀在tries中不存在，停止对该方向的DFS
    //
    // FAQ里面提到的优化方法：
    //   1. 利用“利用前缀查询时，后一次查询只比前一次查询多一个字母”这一点
    //         -> 避免对tries中的节点进行重复访问。
    //            重新实现tries，使其提供特定方法能从tries的某个节点开始向下查询
    //   2. 考虑非递归的前缀查询方法 -> 大概是为了减少函数调用的开销。重新实现tries
    //   3. 预先计算boggle图中每个板块的邻接关系
    //          -> 避免重复的条件判断和算数运算
    //             同时也为了方便DFS，直接将二维数组一维化
    //             使用一维的bag数组，每个bag对象存储该index对应的节点的邻接节点的编号


    private MyTrieST dictionary;
    private char[] board;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("");
        }
        this.dictionary = new MyTrieST();
        for (String word : dictionary) {
            this.dictionary.put(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        if (board == null) {
            throw new IllegalArgumentException("");
        }

        int width = board.cols();
        int height = board.rows();

        int length = width * height;

        this.board = new char[length];

        // 将board转化成一维数组
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.board[i * width + j] = board.getLetter(i, j);
            }
        }

        // 创建邻接关系

        // 对board中的每个字符进行DFS，并将过程中得到的有效字符串保存下来

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("");
        }
        if (!dictionary.contains(word)) {
            return 0;
        }
        int length = word.length();
        if (length < 3) {
            return 0;
        }
        else if (length >= 8) {
            return 11;
        }
        else {
            switch (length) {
                case 3:
                    return 1;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 0;
            }
        }

    }
}
