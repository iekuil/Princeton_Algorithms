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

    // 要实现的是一个二维的BST，而不是2-3树/红黑树/b树这些更复杂的东西

    // 需要自定义搜索节点类
    // 节点信息包括：
    //    当前点坐标Point2D
    //    该点所在的（所分割的）矩形RectHV
    //    当前节点的分割方式（横/纵）
    //    左子节点指针
    //    右子节点指针
    // 还需要实现：
    //    横坐标比较器
    //    纵坐标比较器
    //    总比较器（根据当前节点的分割方式选择相应的比较器）

    // 构想：
    //    叶子节点都是点Point2D指针为空的节点，
    //    意味着叶子节点都是被上一个点分割的、不含有任何点的矩形空间
    //    而所有内部节点的Point2D指针和RectHV指针都不为空，
    //    意味着内部节点都是被父节点分割的矩形空间，同时空间中存在着一个点将当前空间分割成两部分
    //
    //    insert操作：
    //       通过递归地将要插入的点和当前节点的点依据当前节点的分割方式进行比较，
    //       找到一个叶子节点（不含点的节点），并设置该节点的点，
    //       在该节点下方插入两个不含点的子节点，
    //       并根据当前节点的分割方式，为两个子节点设置不同的分割方式
    //
    //    isEmpty:
    //       检查根节点是否设置了Point2D指针
    //
    //    size:
    //       在正常的BST的size上进行修改，
    //       仍然进行递归计算，但只有设置了Point2D指针的节点才会被视作拥有非0的size
    //
    //    contains:
    //       递归查找，
    //       每个节点会根据当前节点的分割方式使用相应的比较器进行比较，
    //       并到相应的子树中进行查找
    //
    //

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
