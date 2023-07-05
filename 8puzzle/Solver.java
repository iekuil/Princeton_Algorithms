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

    private static class ManhattanComparator implements Comparator<Board> {
        public int compare(Board b1, Board b2) {
            return Integer.compare(b1.manhattan(), b2.manhattan());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException("null for Solver.Solver()");
        }

        moves = 0;
        solution = new ArrayList<>();
        solvable = true;

        Board twin = initial.twin();
        Board searchNode = initial;
        Board twinSearchNode = twin;

        Board previous = initial;
        Board twinPrevious = twin;

        MinPQ<Board> gameTree = new MinPQ<>(new ManhattanComparator());
        MinPQ<Board> twinGameTree = new MinPQ<>(new ManhattanComparator());

        gameTree.insert(initial);
        twinGameTree.insert(twin);

        while ((!searchNode.isGoal()) && (!twinSearchNode.isGoal())) {
            moves += 1;

            previous = searchNode;
            twinPrevious = twinSearchNode;

            searchNode = gameTree.delMin();
            twinSearchNode = twinGameTree.delMin();

            solution.add(searchNode);

            ArrayList<Board> neighbors = (ArrayList<Board>) searchNode.neighbors();
            for (Board neighbor : neighbors) {
                if (!neighbor.equals(previous)) {
                    gameTree.insert(neighbor);
                }
            }

            ArrayList<Board> twinNeighbors = (ArrayList<Board>) twinSearchNode.neighbors();
            for (Board twinNeighbor : twinNeighbors) {
                if (!twinNeighbor.equals(twinPrevious)) {
                    twinGameTree.insert(twinNeighbor);
                }
            }
        }

        if (twinSearchNode.isGoal()) {
            moves = -1;
            solution = null;
            solvable = false;
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
