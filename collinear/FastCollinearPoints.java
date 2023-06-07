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
import java.util.Comparator;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segements;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null for FastCollinearPoints.FastCollinearPoints");
        }

        segements = new ArrayList<>();

        Point[] samePoints = checkPoints(points);
        Point[] backup = Arrays.copyOf(samePoints, samePoints.length);

        for (Point p0 : backup) {
            // 遍历点集中的所有点，分别以每个点为基准点，依据基准点到每个点的斜率大小对数组进行排序
            // 对于排序后的数组，尽可能地多匹配具有相同斜率的点
            //      同时使用两个数组下标i和j，
            //      array[i]指向某一组点中的第一个
            //      array[i+j]指向某一组点中的最后一个
            //      j + 2即为当前具有相同斜率的点的数量（j + 1是数组中连续的、斜率相同的点，还有一个基准点p0）
            // 检查线段经过的点的数量，至少四个
            // 将这些点组成新的数组，交给getMinPoint()和getMaxPoint()找出线段的端点
            // 检查segments是否已存在相应的线段
            Comparator<Point> comparator = p0.slopeOrder();

            samePoints = Arrays.copyOf(backup, backup.length);

            Arrays.sort(samePoints, comparator);

            int i, j = 1;

            double lastSlope = p0.slopeTo(samePoints[0]);
            for (i = 1; i < samePoints.length; i++) {

                double thisSlope = p0.slopeTo(samePoints[i]);

                if (Double.compare(lastSlope, thisSlope) == 0) {
                    j++;
                }
                else {
                    if (j < 3) {
                        j = 1;
                        lastSlope = thisSlope;
                        continue;
                    }
                    if (p0.compareTo(samePoints[i - j]) < 0) {
                        tryingAddSegment(p0, samePoints[i - 1]);
                    }

                    j = 1;
                }

                lastSlope = thisSlope;

            }
            if (j >= 3) {
                if (p0.compareTo(samePoints[i - j]) < 0) {
                    tryingAddSegment(p0, samePoints[i - 1]);
                }
            }
        }
    }

    private void tryingAddSegment(Point min, Point max) {

        LineSegment newLine = new LineSegment(min, max);
        segements.add(newLine);
    }

    private Point[] checkPoints(Point[] points) {
        Point[] myPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(
                        "null in array for FastCollinearPoints.FastCollinearPoints");
            }
            myPoints[i] = points[i];
        }
        Arrays.sort(myPoints);
        for (int i = 0; i < myPoints.length; i++) {
            if (i < myPoints.length - 1 && myPoints[i].compareTo(myPoints[i + 1]) == 0) {
                throw new IllegalArgumentException(
                        "duplicate points for FastCollinearPoints.FastCollinearPoints");
            }
        }
        return myPoints;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segements.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segements.toArray(new LineSegment[0]);
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
