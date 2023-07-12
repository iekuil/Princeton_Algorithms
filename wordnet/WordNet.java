/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class WordNet {

    // string到所在的synset的id的映射
    // 一个string可能存在于多个synset中
    private HashMap<String, ArrayList<Integer>> stringToId;

    // synset的id到synset中的具体字符串的映射
    // ArrayList相当于长度可变的数组
    // 使用ArrayList的随机访问来提高查找效率
    private ArrayList<String> idToSynset;

    // SAP类的一个对象，
    // 用于distance和sap方法的实现
    private SAP sap;

    // 存储所有名词的synset id号
    // 使用红黑树的符号表是为了提高查找效率以使isNoun能够满足对数级别的性能要求
    private TreeSet<Integer> nounsSynsetId;

    private class MutatedBFS {
        private boolean[] marked;

        public MutatedBFS(Digraph G, Iterable<Integer> nounSet, TreeSet<Integer> nounsSynsetId) {
            marked = new boolean[G.V()];

            mbfs(G, nounSet, nounsSynsetId);
        }

        private void mbfs(Digraph G, Iterable<Integer> nounSet, TreeSet<Integer> nounsSynsetId) {
            Queue<Integer> queue = new Queue<>();

            for (int s : nounSet) {
                marked[s] = true;
                queue.enqueue(s);
                nounsSynsetId.add(s);
            }

            while (!queue.isEmpty()) {
                int v = queue.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        marked[w] = true;
                        queue.enqueue(w);
                        nounsSynsetId.add(w);
                    }
                }
            }
        }
    }


    // 构造函数：
    //    建立节点编号和synset内字符串的双向映射：
    //       从文件1中读取synset编号及synset
    //       经过字符串处理之后，
    //       将synset中的字符串组织成一个单元（链表/set/变长数组），并使用数组建立从编号到单元的映射
    //       之后利用哈希表，建立synset中的每个字符串到该synset编号的映射
    //
    //    用数据结构对图进行表示：
    //       从文件2中读取节点之间的指向关系，
    //       构造Digraph对象并逐步向里面添加有向边
    //
    //    （用于distance和sap方法）
    //       利用得到的Digraph对象初始化一个SAP对象
    //
    //    （用于isNoun方法）
    //       围绕该图进行DFS
    //
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("");
        }

        stringToId = new HashMap<>();
        idToSynset = new ArrayList<>();
        nounsSynsetId = new TreeSet<>();

        In synsetsInput = new In(synsets);

        int nodesNum = 0;

        while (!synsetsInput.isEmpty()) {
            nodesNum += 1;

            String line = synsetsInput.readLine();
            String[] splitLine = line.split(",");
            String[] words = splitLine[1].split(" ");

            int synsetId = Integer.parseInt(splitLine[0]);

            idToSynset.add(splitLine[1]);

            for (String word : words) {
                ArrayList<Integer> ids = stringToId.get(word);
                if (ids == null) {
                    ids = new ArrayList<>();
                    stringToId.put(word, ids);
                }
                ids.add(synsetId);
            }
        }

        Digraph G = new Digraph(nodesNum);

        In hypernymsInput = new In(hypernyms);

        while (!hypernymsInput.isEmpty()) {
            String line = hypernymsInput.readLine();
            String[] splitLine = line.split(",");

            int id = Integer.parseInt(splitLine[0]);

            for (int i = 1; i < splitLine.length; i++) {
                int hypernym = Integer.parseInt(splitLine[i]);
                G.addEdge(id, hypernym);
            }
        }

        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("");
        }
        sap = new SAP(G);
        MutatedBFS mbfs = new MutatedBFS(G.reverse(), stringToId.get("noun"), nounsSynsetId);
    }


    // 对图的逆进行BFS，
    // 将访问到的每个节点都加入额外的队列中
    // 随后将给节点序列中所有映射的字符串都加入到一个字符串队列中
    //
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (int id : nounsSynsetId) {
            String[] synset = idToSynset.get(id).split(" ");
            Collections.addAll(nouns, synset);
        }
        return nouns;
    }

    // 要求时间复杂度为对数级别
    //
    // 从word出发，向noun进行dfs
    // 如果是对逆向图进行dfs，以noun为根节点最终会形成很多分支，带来不必要的时间开销
    // 而从word出发，
    // 如果word真的是noun的子类，
    // 只需要沿着一条路径前进最终一定会在noun节点停下
    //
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("");
        }
        Iterable<Integer> ids = stringToId.get(word);
        if (ids == null) {
            return false;
        }
        for (int id : ids) {
            if (nounsSynsetId.contains(id)) {
                return true;
            }
        }
        return false;
    }

    // 利用SAP类实现
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("");
        }
        ArrayList<Integer> idSet1 = stringToId.get(nounA);
        ArrayList<Integer> idSet2 = stringToId.get(nounB);

        return sap.length(idSet1, idSet2);
    }

    // 利用SAP类实现
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("");
        }
        ArrayList<Integer> idSet1 = stringToId.get(nounA);
        ArrayList<Integer> idSet2 = stringToId.get(nounB);

        int sca = sap.ancestor(idSet1, idSet2);

        return idToSynset.get(sca);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        ArrayList<String> nouns = (ArrayList<String>) wordNet.nouns();

        StdOut.printf("number of nouns = %d\n", nouns.size());
        StdOut.printf("zebra is nouns? = %b\n", wordNet.isNoun("zebra"));

        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine();
            String[] splitLine = line.split(" ");
            StdOut.printf("distance = %d, ancestor = %s\n",
                          wordNet.distance(splitLine[0], splitLine[1]),
                          wordNet.sap(splitLine[0], splitLine[1]));
        }
    }
}
