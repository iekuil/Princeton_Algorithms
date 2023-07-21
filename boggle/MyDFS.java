import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.TreeSet;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MyDFS {

    private SET<String> res;

    // 需要传入MtTrieST作为参数

    public MyDFS(char[] board, ArrayList<Bag<Integer>> adj, MyTrieST dictionary, int src,
                 SET<String> res) {

        if (board == null || adj == null || dictionary == null || res == null) {
            throw new IllegalArgumentException("");
        }

        // 为了避免重复计算邻接关系，这里应该只计算一次，使用传入的adj作为参数

        int length = board.length;

        this.res = res;

        if (src < 0 || src >= length) {
            throw new IllegalArgumentException("");
        }

        String emptyPrefix = "";
        dfs(board, adj, new TreeSet<Integer>(), dictionary, emptyPrefix, src);
    }

    // DFS：从一点出发，对该点的所有邻接节点进行tries前缀查询
    private void dfs(char[] board, ArrayList<Bag<Integer>> adj, TreeSet<Integer> lastMarked,
                     MyTrieST dict, String prefix, int v) {
        if (prefix == null) {
            throw new IllegalArgumentException("");
        }
        if (dict == null) {
            return;
        }

        String current = prefix.concat(String.valueOf(board[v]));

        MyTrieST subDict = dict.getSubTrie(board[v]);

        if (subDict.isEmpty()) {
            return;
        }

        // 关于marked：
        // 有可能从同一个字母出发能组成不同的单词，但是这些单词用到同一行、同一列的某个字母
        // 但是又要保证在搜索一个单词的路径中不重复访问同一个位置
        TreeSet<Integer> marked = (TreeSet<Integer>) lastMarked.clone();
        marked.add(v);

        String tmp = current.replace("Q", "QU");
        if (subDict.isStrTail() && tmp.length() >= 3) {
            res.add(tmp);
        }
        for (int w : adj.get(v)) {
            if (!marked.contains(w)) {
                dfs(board, adj, marked, subDict, current, w);
            }
        }

    }
}
