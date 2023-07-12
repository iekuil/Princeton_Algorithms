/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // 输入是一个文件名
    // 需要从文件中读取多个字符串
    // 每个字符串都可能属于多个synset，
    // 即，文件中的每个字符串 <---> 由多个节点号组成的Iterable对象
    //
    // 两两组合，
    // 通过调用WordNet的SAP，求出所有Iterable对象之间的距离
    //
    // 对于一个字符串，它到其余所有字符串的距离即是它所在的Iterable到其余每个Iterable的距离之和
    // 总距离最大的字符串即为所求
    //
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int num = nouns.length;
        int[][] distance = new int[num][num];

        for (int i = 0; i < num; i++) {
            distance[i][i] = 0;
            for (int j = i + 1; j < num; j++) {
                int d = wordNet.distance(nouns[i], nouns[j]);
                distance[i][j] = d;
                distance[j][i] = d;
            }
        }

        int[] totalDistance = new int[num];
        int max = 0;

        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                totalDistance[i] += distance[i][j];
            }
            if (totalDistance[i] > totalDistance[max]) {
                max = i;
            }
        }

        return nouns[max];

    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
