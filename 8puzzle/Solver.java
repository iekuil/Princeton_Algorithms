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

    // private static class HammingComparator implements Comparator<Board> {
    //     public int compare(Board b1, Board b2) {
    //         return Integer.compare(b1.hamming(), b2.hamming());
    //     }
    // }

    private class SearchNode {
        private Board board;
        private int currentMoves;
        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.currentMoves = moves;
            this.previous = previous;
        }

        public int manhattanPriority() {
            return board.manhattan() + currentMoves;
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
            if (this.board.equals(other.board) && this.currentMoves == other.currentMoves) {
                return true;
            }

            return false;
        }
    }

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

        Board twin = initial.twin();
        SearchNode searchNode = new SearchNode(initial, moves, null);
        SearchNode twinSearchNode = new SearchNode(twin, moves, null);

        MinPQ<SearchNode> gameTree = new MinPQ<>(new ManhattanComparator());
        MinPQ<SearchNode> twinGameTree = new MinPQ<>(new ManhattanComparator());

        gameTree.insert(searchNode);
        twinGameTree.insert(twinSearchNode);

        while (true) {

            searchNode = gameTree.delMin();
            twinSearchNode = twinGameTree.delMin();

            if ((searchNode.getBoard().isGoal()) || (twinSearchNode.getBoard().isGoal())) {
                break;
            }
            moves = searchNode.getCurrentMoves() + 1;
            twinMoves = twinSearchNode.getCurrentMoves() + 1;

            ArrayList<Board> neighbors = (ArrayList<Board>) searchNode.getBoard().neighbors();
            for (Board neighbor : neighbors) {
                SearchNode neighborNode = new SearchNode(neighbor, moves, searchNode);
                if (!neighborNode.equals(searchNode.previous)) {
                    gameTree.insert(neighborNode);
                }
            }

            ArrayList<Board> twinNeighbors = (ArrayList<Board>) twinSearchNode.getBoard()
                                                                              .neighbors();
            for (Board twinNeighbor : twinNeighbors) {
                SearchNode twinNeighborNode = new SearchNode(twinNeighbor, twinMoves,
                                                             twinSearchNode);
                if (!twinNeighborNode.equals(twinSearchNode.previous)) {
                    twinGameTree.insert(twinNeighborNode);
                }
            }
        }

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
