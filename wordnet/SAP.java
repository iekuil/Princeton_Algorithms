/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
    }

    // 通过调用ancestor方法完成，
    // ancestor方法中在进行bfs时要记录当前路径长度
    // 方便length方法计算最近路径长度
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
    }

    // 从两个节点出发，交替进行BFS
    // 将两个节点各自访问到的祖先加入到同一个符号表中
    // 如果当一个节点尝试向符号表中添加祖先节点时发现祖先节点已经存在，
    // 该祖先节点即为最近的共同祖先节点
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
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

