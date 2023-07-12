/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {

    private final Digraph graph;

    private class Node {
        private final int v;
        private final boolean fromSet1;

        public Node(int v, boolean fromSet1) {
            this.v = v;
            this.fromSet1 = fromSet1;
        }

        public int getV() {
            return v;
        }

        public boolean isFromSet1() {
            return fromSet1;
        }
    }

    private class MutatedBFS {
        private boolean[] markedBySet1;
        private boolean[] markedBySet2;
        private int[] lengthFromSet1;
        private int[] lengthFromSet2;

        private int sca;
        private int shortestPathLen;


        public MutatedBFS(Digraph G, Iterable<Integer> srcSet1, Iterable<Integer> srcSet2) {
            int numberOfV = G.V();

            sca = -1;
            shortestPathLen = -1;

            markedBySet1 = new boolean[numberOfV];
            markedBySet2 = new boolean[numberOfV];

            lengthFromSet1 = new int[numberOfV];
            lengthFromSet2 = new int[numberOfV];

            bfs(G, srcSet1, srcSet2);

        }

        public int sca() {
            return sca;
        }

        public int shortestPathLength() {
            return shortestPathLen;
        }

        private void bfs(Digraph G, Iterable<Integer> srcSet1, Iterable<Integer> srcSet2) {
            Queue<Node> queue = new Queue<>();
            for (int v : srcSet1) {
                queue.enqueue(new Node(v, true));
                markedBySet1[v] = true;
                lengthFromSet1[v] = 0;
            }
            for (int v : srcSet2) {
                queue.enqueue(new Node(v, false));
                markedBySet2[v] = true;
                lengthFromSet2[v] = 0;
                if (isAncestor(v)) {
                    sca = v;
                    shortestPathLen = lengthFromSet1[v] + lengthFromSet2[v];
                    return;
                }
            }

            while (!queue.isEmpty()) {
                Node currentNode = queue.dequeue();
                int v = currentNode.getV();
                boolean fromSet1 = currentNode.isFromSet1();
                int currentLength = getLength(v, fromSet1);

                for (int w : G.adj(v)) {
                    if (!marked(w, fromSet1)) {
                        mark(w, fromSet1);
                        setLength(w, fromSet1, currentLength + 1);
                        queue.enqueue(new Node(w, fromSet1));
                    }
                    if (isAncestor(w)) {
                        int currentPathLen = lengthFromSet1[w] + lengthFromSet2[w];

                        if (sca == -1 || currentPathLen < shortestPathLen) {
                            sca = w;
                            shortestPathLen = currentPathLen;
                        }
                    }
                }
                if (sca != -1 && currentLength > 2 * getLength(sca, fromSet1)) {
                    break;
                }
            }
        }

        private void mark(int v, boolean fromSet1) {
            if (fromSet1) {
                markedBySet1[v] = true;
            }
            else {
                markedBySet2[v] = true;
            }
        }

        private boolean marked(int v, boolean fromSet1) {
            if (fromSet1) {
                return markedBySet1[v];
            }
            else {
                return markedBySet2[v];
            }
        }

        private int getLength(int v, boolean fromSet1) {
            if (!marked(v, fromSet1)) {
                return -1;
            }

            if (fromSet1) {
                return lengthFromSet1[v];
            }
            else {
                return lengthFromSet2[v];
            }
        }

        private void setLength(int v, boolean fromSet1, int length) {

            if (fromSet1) {
                lengthFromSet1[v] = length;
            }
            else {
                lengthFromSet2[v] = length;
            }
        }

        private boolean isAncestor(int v) {
            return markedBySet1[v] && markedBySet2[v];
        }


    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("");
        }
        this.graph = new Digraph(G);
    }

    // 通过调用重载的另一个length实现
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(v);

        ArrayList<Integer> set2 = new ArrayList<>();
        set2.add(w);

        return length(set1, set2);
    }

    // 通过调用重载的另一个ancestor实现
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> set1 = new ArrayList<Integer>();
        set1.add(v);

        ArrayList<Integer> set2 = new ArrayList<>();
        set2.add(w);

        return ancestor(set1, set2);
    }

    // 通过调用ancestor方法完成，
    // ancestor方法中在进行bfs时要记录当前路径长度
    // 方便length方法计算最近路径长度
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgument(v);
        checkArgument(w);
        MutatedBFS mbfs = new MutatedBFS(graph, v, w);
        return mbfs.shortestPathLength();
    }


    //  两个集合的最近祖先节点的定义：
    //     从两个集合中各自任取一个节点，
    //     在所有可能的节点对中，某对节点的最短路径所对应的共同祖先节点即为所求
    //
    //  对于从单点出发的BFS，
    //  队列中的初始节点只有一个，
    //  在这个情景中我们则需要将两个集合的所有节点都加入到队列中作为初始节点，
    //  并用额外的数据结构标记某个节点属于哪个集合的祖先
    //  当出现某个节点同时拥有两个标记时，
    //  即找到了目标的共同祖先节点
    //
    //  由于采用BFS，
    //  当某个集合中的节点第一次访问到某个祖先节点时的路径长度即为该集合到该祖先节点的最短路径长度
    //
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgument(v);
        checkArgument(w);
        MutatedBFS mbfs = new MutatedBFS(graph, v, w);
        return mbfs.sca();
    }

    private void checkArgument(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException("");
        }
        int max = graph.V() - 1;
        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException("");
            }
            if (i < 0 || i > max) {
                throw new IllegalArgumentException("");
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

