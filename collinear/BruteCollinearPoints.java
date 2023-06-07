/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;
    private ArrayList<Point> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // 四重循环：
        //  首先利用Point.compareTo()检查四个点是否两两各不相同，同时确定这条线段（假如构成线段）的两个端点
        //  从其中一个点出发，利用Point.slopeTo()检查这个点到其余三个点的斜率是否相等
        //  检查segments中是否已经存在了相同的线段（相同的起点、终点）

        if (points == null) {
            throw new IllegalArgumentException("null for BruteCollinearPoints");
        }
        Point[] myPoints = checkPoints(points);

        segments = new ArrayList<>();
        lineSegments = new ArrayList<>();

        for (int p1 = 0; p1 < myPoints.length; p1++) {

            for (int p2 = 0; p2 < myPoints.length; p2++) {
                if (p2 == p1) {
                    continue;
                }
                double slope1 = myPoints[p1].slopeTo(myPoints[p2]);

                for (int p3 = 0; p3 < myPoints.length; p3++) {
                    if (p3 == p1 || p3 == p2) {
                        continue;
                    }

                    double slope2 = myPoints[p1].slopeTo(myPoints[p3]);

                    if (Double.compare(slope1, slope2) != 0) {
                        continue;
                    }

                    for (int p4 = 0; p4 < myPoints.length; p4++) {
                        if (p4 == p1 || p4 == p2 || p4 == p3) {
                            continue;
                        }
                        int[] indexArray = new int[4];
                        indexArray[0] = p1;
                        indexArray[1] = p2;
                        indexArray[2] = p3;
                        indexArray[3] = p4;
                        Arrays.sort(indexArray);

                        Point min = myPoints[indexArray[0]];
                        Point max = myPoints[indexArray[3]];

                        if (!checkSegment(min, max)) {
                            continue;
                        }

                        double slope3 = myPoints[p1].slopeTo(myPoints[p4]);

                        if (Double.compare(slope2, slope3) == 0) {
                            LineSegment seg = new LineSegment(min, max);

                            segments.add(seg);
                            lineSegments.add(min);
                            lineSegments.add(max);
                        }
                    }
                }
            }
        }
    }


    private Point[] checkPoints(Point[] points) {
        Point[] myPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(
                        "null in array for BruteCollinearPoints.BruteCollinearPoints");
            }
            myPoints[i] = points[i];
        }
        Arrays.sort(myPoints);
        for (int i = 0; i < myPoints.length; i++) {
            if (i < myPoints.length - 1 && myPoints[i].compareTo(myPoints[i + 1]) == 0) {
                throw new IllegalArgumentException(
                        "duplicate points for BruteCollinearPoints.BruteCollinearPoints");
            }
        }
        return myPoints;
    }

    private boolean checkSegment(Point min, Point max) {
        for (int i = 0; i < lineSegments.size(); i += 2) {
            if (lineSegments.get(i).compareTo(min) == 0
                    && lineSegments.get(i + 1).compareTo(max) == 0) {
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
