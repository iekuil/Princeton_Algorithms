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

    private Node root;

    private class Node {
        private Point2D point;
        private RectHV rect;
        private boolean verticle;
        private Node lChild;
        private Node rChild;
        private int size;

        public Node(RectHV rect, boolean splitMode) {
            this.point = null;
            this.rect = rect;
            this.verticle = splitMode;
            this.lChild = null;
            this.rChild = null;
        }

        public void setPoint(Point2D point) {
            if (point == null) {
                throw new IllegalArgumentException("null for Node.setPoint");
            }
            if (this.rect.distanceTo(point) != 0) {
                throw new IllegalArgumentException(
                        "in Node.setPoint: point locates not in the rect");
            }
            if (this.point != null) {
                throw new IllegalArgumentException("in Node.setPoint:point exists in the node");
            }
            this.point = point;
            if (this.verticle) {
                // 以垂直方向进行分割

                // 分割后的左子节点为矩形的左半部分
                this.lChild = new Node(
                        new RectHV(this.rect.xmin(), this.rect.ymin(), point.x(), this.rect.ymax()),
                        false);

                // 分割后的右子节点为矩形的右半部分
                this.rChild = new Node(
                        new RectHV(point.x(), this.rect.ymin(), this.rect.xmax(), this.rect.ymax()),
                        false);
            }
            else {
                // 以水平方向进行分割

                // 分割后的左子节点为矩形的下半部分
                this.lChild = new Node(
                        new RectHV(this.rect.xmin(), this.rect.ymin(), this.rect.xmax(), point.y()),
                        true);

                // 分割后的右子节点为矩形的上半部分
                this.rChild = new Node(new RectHV(this.rect.xmin(), point.y(), this.rect.xmax(),
                                                  this.rect.ymax()), true);
            }
        }

        public int compareToPoint(Point2D p) {
            if (this.verticle) {
                // 当前节点将所在的矩形进行垂直分割
                // 需要比较x坐标
                return Double.compare(this.point.x(), p.x());
            }
            else {
                // 当前节点将所在的矩形进行水平分割
                // 需要比较y坐标
                return Double.compare(this.point.y(), p.y());
            }
        }

        public boolean noPoint() {
            return this.point == null;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = new Node(new RectHV(0, 0, 1, 1), true);
    }

    // is the set empty?
    public boolean isEmpty() {
        return root.point == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null || x.point == null) {
            return 0;
        }
        else {
            return x.size;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        put(root, p);
    }

    private void put(Node node, Point2D p) {
        if (node.noPoint()) {
            node.setPoint(p);
            return;
        }
        int cmp = node.compareToPoint(p);
        if (cmp < 0) {
            put(node.rChild, p);
        }
        else if (cmp > 0) {
            put(node.lChild, p);
        }
        else {
            // 点p没有落在被分割出的两个子区域内
            // 而是落在了分割线上
            // 此时视作落在左子节点区域内
            if (node.point.compareTo(p) != 0) {
                put(node.lChild, p);
            }

            // 当该点已经被插入到BST中时，
            // 不会重复插入该节点
        }

        node.size = size(node.lChild) + size(node.rChild) + 1;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node.noPoint()) {
            return false;
        }

        int cmp = node.compareToPoint(p);
        if (cmp < 0) {
            return contains(node.rChild, p);
        }
        else if (cmp > 0) {
            return contains(node.lChild, p);
        }
        else {
            if (node.point.compareTo(p) == 0) {
                return true;
            }
            else {
                return contains(node.lChild, p);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        // 先画外框
        // 然后开始递归：
        //    先画point，
        //    根据划分方式画一条过point的横线或竖线，长度由当前node的rect决定
        //    递归左子树，
        //    递归右子树，

        // 初始设置
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.005);
        StdDraw.setXscale(-0.5, 1.5);
        StdDraw.setYscale(-0.5, 1.5);

        // 绘制外框
        StdDraw.line(0, 0, 0, 1);
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(0, 1, 1, 1);
        StdDraw.line(1, 0, 1, 1);

        // 递归绘制每个节点
        drawTree(root);

        StdDraw.show();
    }

    private void drawTree(Node node) {
        if (node == null) {
            return;
        }
        if (node.noPoint()) {
            return;
        }
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        StdDraw.setPenRadius(0.005);
        if (node.verticle) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        drawTree(node.lChild);
        drawTree(node.rChild);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, ArrayList<Point2D> points) {
        if (node == null) {
            return;
        }
        if (node.noPoint()) {
            return;
        }
        if (rect.distanceTo(node.point) == 0) {
            points.add(node.point);
        }
        if (rect.intersects(node.lChild.rect)) {
            range(node.lChild, rect, points);
        }
        if (rect.intersects(node.rChild.rect)) {
            range(node.rChild, rect, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return nearest(root, p);
    }

    private Point2D nearest(Node node, Point2D p) {
        // 剪枝的策略是：
        //    计算当前最近的点到p的距离，
        //    并和p到另一个子节点的rect的距离进行比较，
        //    如果前者更小，就说明不用再到另一个子节点以及下面的子树进行查找
        //
        // 最多可能需要比较三个point：
        //    当前node的point，
        //    左子树递归计算并返回的point，
        //    右子树递归计算并返回的point，
        //
        // 具体实现：
        //    如果当前node为空指针，返回null
        //    如果当前node中不含有point，返回null
        //    接下来先查找左子树：
        //       如果左子树返回null，当前最近点为分割点
        //       如果左子树返回了一个有效的点，将当前最近点设置为左子树最近点与分割点中距离目标更近的那一个
        //    将当前最近点到右子树的矩形的距离进行比较，
        //       小于：返回当前最近点
        //       大于等于：
        //          查找右子树
        //             若右子树返回null，返回当前最近点
        //             若右子树返回有效的点，比较得出当前树的最近点，并返回最近点

        if (node == null) {
            return null;
        }
        if (node.noPoint()) {
            return null;
        }

        Point2D currentNearest;
        double currentDistance;

        currentNearest = nearest(node.lChild, p);

        if (currentNearest == null) {
            currentNearest = node.point;
            currentDistance = currentNearest.distanceTo(p);
        }
        else {
            currentDistance = currentNearest.distanceTo(p);
            double splitPointDistance = node.point.distanceTo(p);
            if (splitPointDistance < currentDistance) {
                currentNearest = node.point;
                currentDistance = splitPointDistance;
            }
        }

        double rightRectDistance = node.rChild.rect.distanceTo(p);

        if (currentDistance < rightRectDistance) {
            return currentNearest;
        }

        Point2D rightNearest = nearest(node.rChild, p);

        if (rightNearest == null) {
            return currentNearest;
        }

        double rightNearestDistance = rightNearest.distanceTo(p);

        if (rightNearestDistance < currentDistance) {
            currentNearest = rightNearest;
        }

        return currentNearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        KdTree kdTree = new KdTree();
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
                    kdTree.insert(lastPoint);
                }
                catch (NumberFormatException e) {
                    System.err.println("Invalid number format: " + line);
                }
            }
        }

        StdOut.printf("isEmpty = %b\n", kdTree.isEmpty());
        StdOut.printf("size = %d\n", kdTree.size());
        StdOut.printf("contains = %b\n", kdTree.contains(lastPoint));

        RectHV rect = new RectHV(0.2, 0.2, 0.8, 0.8);
        StdOut.printf("range contains:\n");
        Iterable<Point2D> range = kdTree.range(rect);
        for (Point2D p : range) {
            StdOut.println(p.toString());
        }

        StdOut.printf("neighbor of (0.5, 0.5) = %s\n", kdTree.nearest(new Point2D(0.5, 0.5)));

        kdTree.draw();
    }
}
