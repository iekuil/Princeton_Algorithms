/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segements;

    private class QuickSort {
        public void sort(Point[] points, Comparator<Point> comparator) {
            StdRandom.shuffle(points);
            sort(points, 0, points.length - 1, comparator);
        }

        private void sort(Point[] points, int lo, int hi, Comparator<Point> comparator) {
            if (hi <= lo) {
                return;
            }

            int j = partition(points, lo, hi, comparator);
            sort(points, lo, j - 1, comparator);
            sort(points, j + 1, hi, comparator);
        }

        private boolean less(Comparator<Point> comparator, Point p1, Point p2) {
            return comparator.compare(p1, p2) < 0;
        }

        private void exch(Point[] points, int i, int j) {
            Point tmp = points[i];
            points[i] = points[j];
            points[j] = tmp;
        }

        private int partition(Point[] points, int lo, int hi, Comparator<Point> comparator) {
            int i = lo, j = hi + 1;
            Point v = points[lo];
            while (true) {
                while (less(comparator, points[++i], v)) if (i == hi) break;
                while (less(comparator, v, points[--j])) if (j == lo) break;

                if (i >= j) break;
                exch(points, i, j);
            }
            exch(points, lo, j);
            return j;
        }

    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null for FastCollinearPoints.FastCollinearPoints");
        }

        segements = new ArrayList<>();

        Point[] samePoints = Arrays.copyOf(points, points.length);
        for (Point p0 : points) {
            // 遍历点集中的所有点，分别以每个点为基准点，依据基准点到每个点的斜率大小对数组进行排序
            // 对于排序后的数组，尽可能地多匹配具有相同斜率的点
            //      同时使用两个数组下标i和j，
            //      array[i]指向某一组点中的第一个
            //      array[i+j]指向某一组点中的最后一个
            //      j + 2即为当前具有相同斜率的点的数量（j + 1是数组中连续的、斜率相同的点，还有一个基准点p0）
            // 检查线段经过的点的数量，至少四个
            // 将这些点组成新的数组，交给getMinPoint()和getMaxPoint()找出线段的端点
            // 检查segments是否已存在相应的线段
            QuickSort qs = new QuickSort();
            qs.sort(samePoints, p0.slopeOrder());

            int i, j;
            for (i = 0; i < samePoints.length - 3; i += j) {
                double slope0 = p0.slopeTo(samePoints[i]);
                j = 0;
                while (p0.slopeTo(samePoints[i + (++j)]) == slope0)
                    if (i + j == samePoints.length - 1) break;
                if (j < 3) {
                    continue;
                }

                ArrayList<Point> tmpList = new ArrayList<>();
                tmpList.add(p0);
                tmpList.addAll(Arrays.asList(samePoints).subList(i, i + j));

                Point min = getMinPoint(tmpList.toArray(new Point[0]));
                Point max = getMaxPoint(tmpList.toArray(new Point[0]));

                LineSegment newLine = new LineSegment(min, max);

                if (checkSegmentExist(newLine)) {
                    segements.add(newLine);
                }
            }
        }
    }

    private Point getMinPoint(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null for FastCollinearPoints.getMinPoint");
        }
        Point min = points[0];
        for (Point p : points) {
            if (p.compareTo(min) < 0) {
                min = p;
            }
        }
        return min;
    }

    private Point getMaxPoint(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null for FastCollinearPoints.getMinPoint");
        }
        Point max = points[0];
        for (Point p : points) {
            if (p.compareTo(max) > 0) {
                max = p;
            }
        }
        return max;
    }

    private boolean checkSegmentExist(LineSegment segment) {
        String line = segment.toString();
        for (LineSegment seg : segements) {
            if (line.equals(seg.toString())) {
                return false;
            }
        }
        return true;
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
