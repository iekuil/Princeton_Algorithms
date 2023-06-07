/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // 这里的比较器原本声明为public，但是会报错
    //
    // 来自chatgpt的解释：
    //      在Java中，当一个字段被声明为public时，它可以被所有类直接访问。
    //      然而，内部类（例如BySlope）可以直接访问外部类（例如Point）的私有字段，而不需要使用字段的访问修饰符。
    //      由于bySlope字段是内部类BySlope所使用的字段，它不需要被其他类直接访问。
    //      因此，将其访问修饰符设置为public是不必要的，并且会导致编译错误。
    //      根据Java的封装原则，应该尽量将字段声明为私有（private）或受保护（protected），以限制对其的直接访问，并通过公共的方法来访问和操作字段的值。
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */

        if (this.x == that.x) {
            if (this.y == that.y) {
                // that点和当前点完全重合, 属于退化情况
                return Double.NEGATIVE_INFINITY;
            }
            else {
                // that点和当前点构成一条直线，斜率无穷大
                return Double.POSITIVE_INFINITY;
            }
        }

        if (this.y == that.y) {
            // 横坐标不同，纵坐标相同，形成的线段和x轴平行
            return +0.0;
        }

        return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */

        if (this.y < that.y) {
            return -1;
        }

        if (this.y == that.y) {
            return Integer.compare(this.x, that.x);
        }

        return 1;
    }

    private class BySlope implements Comparator<Point> {
        // 这里的Comparator实际上会被作为一个单独的对象使用
        // 需要以一个点o0为基准，比较o0到o1、o0到o2的斜率区别
        // 因此需要存储o0的信息

        public int compare(Point p1, Point p2) {
            double slopeTo1 = Point.this.slopeTo(p1);
            double slopeTo2 = Point.this.slopeTo(p2);

            return Double.compare(slopeTo1, slopeTo2);
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new BySlope();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

        // (1,1),(1,2),(2,1),(2,2)
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(2, 1);
        Point p4 = new Point(2, 2);

        // 测试slopeTo:
        //  常规计算：(1,1)与(2,2);(2,2)与(1,1)
        //  平行：(1,1)与(2,1)
        //  垂直：(1,1)与(1,2)
        //  退化：(1,1)与(1,1)
        StdOut.println("testing slopeTo:");
        StdOut.printf("%s to %s = %f\n", p1.toString(), p4.toString(), p1.slopeTo(p4));
        StdOut.printf("%s to %s = %f\n", p4.toString(), p1.toString(), p4.slopeTo(p1));
        StdOut.printf("%s to %s = %f\n", p1.toString(), p2.toString(), p1.slopeTo(p2));
        StdOut.printf("%s to %s = %f\n", p1.toString(), p1.toString(), p1.slopeTo(p1));

        // 测试compareTo:
        //  小于情况1：this.y比that.y小
        //      x不同：(1,1)与(2,2)
        //      x相同：(2,1)与(2,2)
        //  小于情况2：y相同，this.x比that.x小
        //      (1,2)与(2,2)
        //  其他情况
        StdOut.println("testing compareTo:");
        StdOut.printf("%s compared to %s = %d\n", p1.toString(), p2.toString(), p1.compareTo(p2));
        StdOut.printf("%s compared to %s = %d\n", p3.toString(), p4.toString(), p3.compareTo(p4));
        StdOut.printf("%s compared to %s = %d\n", p4.toString(), p3.toString(), p4.compareTo(p3));
        StdOut.printf("%s compared to %s = %d\n", p2.toString(), p4.toString(), p2.compareTo(p4));
        StdOut.printf("%s compared to %s = %d\n", p1.toString(), p1.toString(),
                      p1.compareTo(new Point(1, 1)));


        // 测试slopeOrder:
        // 以(1,1)为起点，斜率的比较应当为(1,2) < (2,2) < (2,1)
        Comparator<Point> c = p1.slopeOrder();
        StdOut.println("testing slopeOrder:");
        StdOut.printf("%s compared to %s = %d\n", p2.toString(), p3.toString(), c.compare(p2, p3));
        StdOut.printf("%s compared to %s = %d\n", p3.toString(), p2.toString(), c.compare(p3, p2));
        StdOut.printf("%s compared to %s = %d\n", p2.toString(), p4.toString(), c.compare(p2, p4));
        StdOut.printf("%s compared to %s = %d\n", p4.toString(), p2.toString(), c.compare(p4, p2));
        StdOut.printf("%s compared to %s = %d\n", p3.toString(), p4.toString(), c.compare(p3, p4));
        StdOut.printf("%s compared to %s = %d\n", p4.toString(), p3.toString(), c.compare(p4, p3));
        StdOut.printf("%s compared to %s = %d\n", p1.toString(), p3.toString(), c.compare(p1, p3));
        StdOut.printf("%s compared to %s = %d\n", p2.toString(), p2.toString(), c.compare(p2, p2));
    }
}
