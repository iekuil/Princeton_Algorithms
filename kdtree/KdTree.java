/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
    }

    // number of points in the set
    public int size() {
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
    }

    // draw all points to standard draw
    public void draw() {
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
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
                    // 执行你的逻辑操作，例如打印这两个数字
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
