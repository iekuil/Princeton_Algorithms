import edu.princeton.cs.algs4.Bag;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MyDFS {
    private boolean[] marked;
    private char[] board; // board本身不会变，为了避免重复计算应该使用传入的adj作参数
    private Bag<Integer>[] adj; // 一个board的邻接关系不会变，但是会多次生成MyDFS对象以不同的起点进行搜索，

    private Bag<String> res;

    // 需要传入MtTrieST作为参数

    public MyDFS(char[] board, Bag<Integer>[] adj, MyTrieST dictionary, int src) {

        if (board == null || adj == null || dictionary == null) {
            throw new IllegalArgumentException("");
        }

        this.board = board;
        this.adj = adj;
        // 为了避免重复计算邻接关系，这里应该只计算一次，使用传入的adj作为参数

        int length = board.length;
        marked = new boolean[length];

        res = new Bag<>();

        if (src < 0 || src >= length) {
            throw new IllegalArgumentException("");
        }

        String emptyPrefix = "";
        dfs(dictionary, emptyPrefix, src);
    }

    // DFS：从一点出发，对该点的所有邻接节点进行tries前缀查询
    private void dfs(MyTrieST dict, String prefix, int v) {
        if (prefix == null) {
            throw new IllegalArgumentException("");
        }
        if (dict == null) {
            return;
        }

        marked[v] = true;
        String current = prefix.concat(String.valueOf(board[v]));

        MyTrieST subDict = dict.getSubTrie(board[v]);

        if (subDict.isEmpty()) {
            return;
        }
        if (subDict.isStrTail()) {
            res.add(current);
        }
        for (int w : adj[v]) {
            if (!marked[w]) {
                dfs(subDict, current, w);
            }
        }

    }

    // 用bag应该就行，底层是扩容数组，常数级别的插入效率
    // 如果用红黑树/哈希表开销估计更大
    public Bag<String> getRes() {
        return res;
    }
}
