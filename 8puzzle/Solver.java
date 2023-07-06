/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    private int moves;
    private ArrayList<Board> solution;
    private boolean solvable;

    // 定义一个内部类作为搜索节点，
    // 包含格板布局board、到这个格板的移动次数moves、当前节点的优先级priority、前一个节点previous
    private class SearchNode {
        private Board board;
        private int currentMoves;
        private int priority;
        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.currentMoves = moves;
            this.previous = previous;

            // 优先级来自于manhattan距离和步数moves的和
            this.priority = board.manhattan() + moves;
        }

        public int manhattanPriority() {
            return priority;
        }

        public Board getBoard() {
            return board;
        }

        public int getCurrentMoves() {
            return currentMoves;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }

            SearchNode other = (SearchNode) obj;

            if (this.board.equals(other.board)) {
                return true;
            }

            return false;
        }
    }

    // 定义一个比较器
    // 用于比较用manhattan距离衡量的节点优先级
    private static class ManhattanComparator implements Comparator<SearchNode> {
        public int compare(SearchNode n1, SearchNode n2) {
            return Integer.compare(n1.manhattanPriority(), n2.manhattanPriority());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException("null for Solver.Solver()");
        }

        moves = 0;
        int twinMoves = 0;

        solution = new ArrayList<>();
        solvable = true;

        // 创建一个twin，之后twin会和原格板同步求解，以确定原格板是否有解
        Board twin = initial.twin();

        // 初始化当前搜索节点
        SearchNode searchNode = new SearchNode(initial, moves, null);
        SearchNode twinSearchNode = new SearchNode(twin, twinMoves, null);

        // 初始化搜索节点的优先级队列，即gameTree
        MinPQ<SearchNode> gameTree = new MinPQ<>(new ManhattanComparator());
        MinPQ<SearchNode> twinGameTree = new MinPQ<>(new ManhattanComparator());

        gameTree.insert(searchNode);
        twinGameTree.insert(twinSearchNode);

        while (true) {

            // 更新当前搜索节点
            searchNode = gameTree.delMin();
            twinSearchNode = twinGameTree.delMin();

            // 检查求解是否完成
            if ((searchNode.getBoard().isGoal()) || (twinSearchNode.getBoard().isGoal())) {
                moves = searchNode.getCurrentMoves();
                break;
            }

            // 在求解过程中，可能需要回溯到之前的某个节点
            // 因此步数moves需要更新为比节点的步数多一步，而不是一直自增
            moves = searchNode.getCurrentMoves() + 1;
            twinMoves = twinSearchNode.getCurrentMoves() + 1;

            // 将当前搜索节点的邻居加入到优先级队列中
            Iterable<Board> neighbors = searchNode.getBoard().neighbors();
            for (Board neighbor : neighbors) {
                SearchNode neighborNode = new SearchNode(neighbor, moves, searchNode);

                // 如果邻居和当前节点的前一个节点布局相同，不会加入队列
                // 避免走“回头路”
                if (!neighborNode.equals(searchNode.previous)) {
                    gameTree.insert(neighborNode);
                }
            }

            Iterable<Board> twinNeighbors = twinSearchNode.getBoard()
                                                          .neighbors();
            for (Board twinNeighbor : twinNeighbors) {
                SearchNode twinNeighborNode = new SearchNode(twinNeighbor, twinMoves,
                                                             twinSearchNode);
                if (!twinNeighborNode.equals(twinSearchNode.previous)) {
                    twinGameTree.insert(twinNeighborNode);
                }
            }
        }

        // 如果twin完成求解，说明原格板无解
        if (twinSearchNode.getBoard().isGoal()) {
            moves = -1;
            solution = null;
            solvable = false;
        }
        else {
            while (searchNode != null) {
                solution.add(0, searchNode.getBoard());
                searchNode = searchNode.previous;
            }
        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
