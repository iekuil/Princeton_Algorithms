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

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // 四重循环：
        //  首先利用Point.compareTo()检查四个点是否两两各不相同，同时确定这条线段（假如构成线段）的两个端点
        //  从其中一个点出发，利用Point.slopeTo()检查这个点到其余三个点的斜率是否相等
        //  检查segments中是否已经存在了相同的线段（相同的起点、终点）

        if (points == null) {
            throw new IllegalArgumentException("null for BruteCollinearPoints");
        }

        segments = new ArrayList<>();

        for (Point p1 : points) {
            for (Point p2 : points) {
                if (p1.compareTo(p2) == 0) {
                    continue;
                }
                for (Point p3 : points) {
                    if (p1.compareTo(p3) == 0 || p2.compareTo(p3) == 0) {
                        continue;
                    }
                    for (Point p4 : points) {
                        if (p1.compareTo(p4) == 0 || p2.compareTo(p4) == 0
                                || p3.compareTo(p4) == 0) {
                            continue;
                        }
                        Point min = getMinPoint(p1, p2, p3, p4);
                        Point max = getMaxPoint(p1, p2, p3, p4);

                        if (p1.slopeTo(p2) == p1.slopeTo(p3) && p1.slopeTo(p3) == p1.slopeTo(p4)) {
                            LineSegment seg = new LineSegment(min, max);
                            if (checkSegment(seg)) {
                                segments.add(seg);
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

    private boolean checkSegment(LineSegment newLine) {
        for (LineSegment line : segments) {
            if (line.toString().equals(newLine.toString())) {
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