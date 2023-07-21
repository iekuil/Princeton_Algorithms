import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.SET;

import java.util.TreeSet;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MyDFS {
    private char[] board; // board本身不会变，为了避免重复计算应该使用传入的adj作参数
    private Bag<Integer>[] adj; // 一个board的邻接关系不会变，但是会多次生成MyDFS对象以不同的起点进行搜索，

    private SET<String> res;

    // 需要传入MtTrieST作为参数

    public MyDFS(char[] board, Bag<Integer>[] adj, MyTrieST dictionary, int src, SET<String> res) {

        if (board == null || adj == null || dictionary == null || res == null) {
            throw new IllegalArgumentException("");
        }

        this.board = board;
        this.adj = adj;
        // 为了避免重复计算邻接关系，这里应该只计算一次，使用传入的adj作为参数

        int length = board.length;

        this.res = res;

        if (src < 0 || src >= length) {
            throw new IllegalArgumentException("");
        }

        String emptyPrefix = "";
        dfs(new TreeSet<Integer>(), dictionary, emptyPrefix, src);
    }

    // DFS：从一点出发，对该点的所有邻接节点进行tries前缀查询
    private void dfs(TreeSet<Integer> lastMarked, MyTrieST dict, String prefix, int v) {
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

        if (subDict.isStrTail() && current.length() >= 3) {
            res.add(current);
        }
        for (int w : adj[v]) {
            if (!marked.contains(w)) {
                dfs(marked, subDict, current, w);
            }
        }

    }

    // 需要不重复的插入
    // 使用SET
    public SET<String> getRes() {
        return res;
    }
}
