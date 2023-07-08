/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    // 用BST存放点集
    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // TreeSet通过给定的比较器或者对象的compareTo方法判断是否相等
        // 且不会添加重复的元素
        if (p == null) {
            throw new IllegalArgumentException("null for PointSET.insert");
        }
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null for PointSET.contains");
        }
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : set) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("null for PointSET.range");
        }
        ArrayList<Point2D> pointsInside = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.distanceSquaredTo(p) == 0) {
                pointsInside.add(p);
            }
        }
        return pointsInside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null for PointSET.nearest");
        }
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D neighbor = null;
        for (Point2D pSet : set) {
            if (pSet.distanceSquaredTo(p) < minDistance) {
                minDistance = pSet.distanceSquaredTo(p);
                neighbor = pSet;
            }
        }
        return neighbor;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        PointSET pointSET = new PointSET();
        Point2D lastPoint = new Point2D(0.5, 0.5);

        String line;
        while ((line = in.readLine()) != null && !line.equals("")) {
            String[] tokens = line.trim().split("\\s+"); // 使用空白字符分割行数据
            if (tokens.length >= 2) {
                try {
                    double number1 = Double.parseDouble(tokens[0]);
                    double number2 = Double.parseDouble(tokens[1]);

                    lastPoint = new Point2D(number1, number2);
                    pointSET.insert(lastPoint);
                }
                catch (NumberFormatException e) {
                    System.err.println("Invalid number format: " + line);
                }
            }
        }

        StdOut.printf("isEmpty = %b\n", pointSET.isEmpty());
        StdOut.printf("size = %d\n", pointSET.size());
        StdOut.printf("contains = %b\n", pointSET.contains(lastPoint));

        RectHV rect = new RectHV(0.2, 0.2, 0.8, 0.8);
        StdOut.printf("range contains:\n");
        Iterable<Point2D> range = pointSET.range(rect);
        for (Point2D p : range) {
            StdOut.println(p.toString());
        }

        StdOut.printf("neighbor of (0.5, 0.5) = %s\n", pointSET.nearest(new Point2D(0.5, 0.5)));

        pointSET.draw();
    }

}
