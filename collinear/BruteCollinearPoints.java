/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;
    private ArrayList<MyLineSegment> mySegments;

    private class MyLineSegment {
        private final Point p;   // one endpoint of this line segment
        private final Point q;   // the other endpoint of this line segment

        public MyLineSegment(Point p, Point q) {
            if (p == null || q == null) {
                throw new IllegalArgumentException("argument to LineSegment constructor is null");
            }
            if (p.equals(q)) {
                throw new IllegalArgumentException(
                        "both arguments to LineSegment constructor are the same point: " + p);
            }
            this.p = p;
            this.q = q;
        }

        public Point getP() {
            return p;
        }

        public Point getQ() {
            return q;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj.getClass() == this.getClass())) {
                return false;
            }
            if ((this.p.compareTo(((MyLineSegment) obj).getP()) == 0
                    && this.q.compareTo(((MyLineSegment) obj).getQ()) == 0) || (
                    this.p.compareTo(((MyLineSegment) obj).getQ()) == 0
                            && this.q.compareTo(((MyLineSegment) obj).getP()) == 0)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            throw new UnsupportedOperationException("hashCode() is not supported");
        }
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // 四重循环：
        //  首先利用Point.compareTo()检查四个点是否两两各不相同，同时确定这条线段（假如构成线段）的两个端点
        //  从其中一个点出发，利用Point.slopeTo()检查这个点到其余三个点的斜率是否相等
        //  检查segments中是否已经存在了相同的线段（相同的起点、终点）

        if (points == null) {
            throw new IllegalArgumentException("null for BruteCollinearPoints");
        }
        checkNullInArray(points);

        segments = new ArrayList<>();
        mySegments = new ArrayList<>();

        for (int p1 = 0; p1 < points.length; p1++) {
            for (int p2 = 0; p2 < points.length; p2++) {
                if (p2 == p1) {
                    continue;
                }
                for (int p3 = 0; p3 < points.length; p3++) {
                    if (p3 == p1 || p3 == p2) {
                        continue;
                    }
                    for (int p4 = 0; p4 < points.length; p4++) {
                        if (p4 == p1 || p4 == p2 || p4 == p3) {
                            continue;
                        }
                        Point min = getMinPoint(points[p1], points[p2], points[p3], points[p4]);
                        Point max = getMaxPoint(points[p1], points[p2], points[p3], points[p4]);

                        if (points[p1].slopeTo(points[p2]) == points[p1].slopeTo(points[p3])
                                &&
                                points[p1].slopeTo(points[p3]) == points[p1].slopeTo(points[p4])) {
                            LineSegment seg = new LineSegment(min, max);
                            MyLineSegment mySeg = new MyLineSegment(min, max);
                            if (checkSegment(mySeg)) {
                                segments.add(seg);
                                mySegments.add(mySeg);
                            }
                        }
                    }
                }
            }
        }
    }

    private Point getMinPoint(Point p1, Point p2, Point p3, Point p4) {
        Point min = p1;

        if (p2.compareTo(min) < 0) {
            min = p2;
        }

        if (p3.compareTo(min) < 0) {
            min = p3;
        }

        if (p4.compareTo(min) < 0) {
            min = p4;
        }

        return min;
    }

    private Point getMaxPoint(Point p1, Point p2, Point p3, Point p4) {
        Point max = p1;

        if (p2.compareTo(max) > 0) {
            max = p2;
        }

        if (p3.compareTo(max) > 0) {
            max = p3;
        }

        if (p4.compareTo(max) > 0) {
            max = p4;
        }

        return max;
    }

    private void checkNullInArray(Point[] points) {
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException(
                        "null in array for BruteCollinearPoints.BruteCollinearPoints");
            }
        }
    }

    private boolean checkSegment(MyLineSegment newLine) {
        for (MyLineSegment myLine : mySegments) {
            if (myLine.equals(newLine)) {
                return false;
            }
        }
        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
