/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

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

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("");
        }
        this.dictionary = new MyTrieST();
        for (String word : dictionary) {
            if (word.contains("Q") && (!word.contains("QU"))) {
                continue;
            }
            this.dictionary.put(word.replace("QU", "Q"));
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

        char[] board1d = new char[length];

        Bag<Integer>[] adj = (Bag<Integer>[]) new Bag[length];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = i * width + j;

                // 将board转化成一维数组
                board1d[index] = board.getLetter(i, j);

                // 创建邻接关系
                adj[index] = new Bag<>();

                if (i != 0) {
                    adj[index].add((i - 1) * width + j);
                }
                if (i != height - 1) {
                    adj[index].add((i + 1) * width + j);
                }
                if (j != 0) {
                    adj[index].add(i * width + j - 1);
                }
                if (j != width - 1) {
                    adj[index].add(i * width + j + 1);
                }

                if (i != 0 && j != 0) {
                    adj[index].add((i - 1) * width + j - 1);
                }

                if (i != 0 && j != width - 1) {
                    adj[index].add((i - 1) * width + j + 1);
                }

                if (i != height - 1 && j != 0) {
                    adj[index].add((i + 1) * width + j - 1);
                }
                if (i != height - 1 && j != width - 1) {
                    adj[index].add((i + 1) * width + j + 1);
                }
            }
        }

        SET<String> res = new SET<>();

        for (int i = 0; i < length; i++) {
            MyDFS dfs = new MyDFS(board1d, adj, dictionary, i, res);
        }

        return res;
        // 对board中的每个字符进行DFS，并将过程中得到的有效字符串保存下来

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("");
        }
        if (!dictionary.contains(word.replace("QU", "Q"))) {
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

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            int tmpScore = solver.scoreOf(word);
            StdOut.printf("%s : %d\n", word, tmpScore);
            score += tmpScore;
        }
        StdOut.println("Score = " + score);
    }

}
