/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

    private Picture picture;

    private double[][] energy;

    // 要求最坏情况下具有宽x高的时间复杂度
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        // 保存指向picture的指针
        // 计算energy数组:
        //    四周一圈像素的能量记为1000
        //    其余的像素的能量为RGB分别对x、y求导，平方，加总，再开根号

        if (picture == null) {
            throw new IllegalArgumentException("");
        }
        this.picture = picture;
        getAndSetEnergy(picture);
    }

    private void getAndSetEnergy(Picture newPicture) {
        int width = newPicture.width();
        int height = newPicture.height();

        double[][] newEnergy = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
                    newEnergy[i][j] = 1000;
                }
                else {
                    newEnergy[i][j] = computeEnergy(newPicture.getRGB(j, i - 1),
                                                    newPicture.getRGB(j, i + 1),
                                                    newPicture.getRGB(j - 1, i),
                                                    newPicture.getRGB(j + 1, i));
                }
            }
        }

        this.energy = newEnergy;
    }

    private double computeEnergy(int rgbU, int rgbD, int rgbL, int rgbR) {
        return Math.sqrt(computeSquareGradient(rgbU, rgbD) + computeSquareGradient(rgbL, rgbR));
    }

    private double computeSquareGradient(int rgb1, int rgb2) {
        double r1 = getRfromInt(rgb1);
        double g1 = getGfromInt(rgb1);
        double b1 = getBfromInt(rgb1);

        double r2 = getRfromInt(rgb2);
        double g2 = getGfromInt(rgb2);
        double b2 = getBfromInt(rgb2);

        return Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2);
    }

    private double getRfromInt(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private double getGfromInt(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private double getBfromInt(int rgb) {
        return (rgb) & 0xFF;
    }

    // 要求最坏情况下具有宽x高的时间复杂度
    // current picture
    public Picture picture() {
        return picture;
    }

    // 要求最坏情况下具有常数级的时间复杂度
    // width of current picture
    public int width() {
        return picture.width();
    }

    // 要求最坏情况下具有常数级的时间复杂度
    // height of current picture
    public int height() {
        return picture.height();
    }

    // 要求最坏情况下具有常数级的时间复杂度
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > this.picture.width() - 1) {
            throw new IllegalArgumentException("");
        }
        if (y < 0 || y > this.picture.height() - 1) {
            throw new IllegalArgumentException("");
        }
        return energy[x][y];
    }

    // 要求最坏情况下具有宽x高的时间复杂度
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // 当寻找水平接缝时，从每个像素会有三条有向边出发，
        // 分别指向右上方、正右方、右下方，权重均为该像素的能量值
        // 此时在最左侧和最右侧分别添加一个虚拟节点，
        // 最左的虚拟节点连接到左侧第一列所有像素，而最右侧一列的所有像素都连接到最右的虚拟节点
        // 构成一个无环、加权、无负权重的DAG
        // 寻找一条有效的接缝等价于寻找该图中从虚拟起点到虚拟终点的最短路径，
        // 总耗时与E+V成正比，相当于和宽x高成正比

        int height = height();
        int width = width();

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int i = 0; i < height; i++) {
            edgeTo[i][0] = 0;
            distTo[i][0] = 0;
        }

        for (int col = 0; col < width - 1; col++) {
            for (int row = 0; row < height; row++) {
                if (row != 0) {
                    if (distTo[row - 1][col + 1] > distTo[row][col] + energy[row][col]) {
                        edgeTo[row - 1][col + 1] = row;
                        distTo[row - 1][col + 1] = distTo[row][col] + energy[row][col];
                    }
                }
                if (distTo[row][col + 1] > distTo[row][col] + energy[row][col]) {
                    edgeTo[row][col + 1] = row;
                    distTo[row][col + 1] = distTo[row][col] + energy[row][col];
                }
                if (row != height - 1) {
                    if (distTo[row + 1][col + 1] > distTo[row][col] + energy[row][col]) {
                        edgeTo[row + 1][col + 1] = row;
                        distTo[row + 1][col + 1] = distTo[row][col] + energy[row][col];
                    }
                }
            }
        }

        int end = 0;
        for (int row = 0; row < height; row++) {
            if (distTo[row][width - 1] < distTo[end][width - 1]) {
                end = row;
            }
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(end);
        int last = end;
        for (int col = width - 1; col > 0; col--) {
            stack.push(edgeTo[last][col]);
            last = edgeTo[last][col];
        }

        int[] res = new int[width];
        for (int i = 0; i < width; i++) {
            res[i] = stack.pop();
        }

        return res;
    }

    // 要求最坏情况下具有宽x高的时间复杂度
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // 类似于findHorizontalSeam的实现

        int height = height();
        int width = width();

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int i = 0; i < width; i++) {
            edgeTo[0][i] = 0;
            distTo[0][i] = 0;
        }

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                if (col != 0) {
                    if (distTo[row + 1][col - 1] > distTo[row][col] + energy[row][col]) {
                        edgeTo[row + 1][col - 1] = row;
                        distTo[row + 1][col - 1] = distTo[row][col] + energy[row][col];
                    }
                }
                if (distTo[row + 1][col] > distTo[row][col] + energy[row][col]) {
                    edgeTo[row + 1][col] = row;
                    distTo[row + 1][col] = distTo[row][col] + energy[row][col];
                }
                if (col != width - 1) {
                    if (distTo[row + 1][col + 1] > distTo[row][col] + energy[row][col]) {
                        edgeTo[row + 1][col + 1] = row;
                        distTo[row + 1][col + 1] = distTo[row][col] + energy[row][col];
                    }
                }
            }
        }

        int end = 0;
        for (int col = 0; col < height; col++) {
            if (distTo[height - 1][col] < distTo[height - 1][end]) {
                end = col;
            }
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(end);
        int last = end;
        for (int row = height - 1; row > 0; row--) {
            stack.push(edgeTo[row][last]);
            last = edgeTo[row][last];
        }

        int[] res = new int[height];
        for (int i = 0; i < height; i++) {
            res[i] = stack.pop();
        }

        return res;
    }

    // 要求最坏情况下具有宽x高的时间复杂度
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // 由于Picture对象不能去除像素
        // 这里需要创建一个新的picture对象，其宽度与原picture相等，高度为原高度减1
        // 利用Picture.setRGB进行逐个像素拷贝，当数组下标访问到seam数组中出现的下标中时，不进行拷贝
        // 之后再把this.picture指向新的picture对象

        if (seam == null) {
            throw new IllegalArgumentException("");
        }
        if (seam.length != this.picture.width()) {
            throw new IllegalArgumentException("");
        }
        if (this.picture.height() <= 1) {
            throw new IllegalArgumentException("");
        }
        checkSeam(seam);

        int oldWidth = this.picture.width();
        int oldHeight = this.picture.height();

        int newHeight = oldHeight - 1;

        Picture newPicture = new Picture(oldWidth, newHeight);

        for (int i = 0; i < oldWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                if (j >= seam[i]) {
                    newPicture.setRGB(i, j, this.picture.getRGB(i, j + 1));
                }
                else {
                    newPicture.setRGB(i, j, this.picture.getRGB(i, j));
                }
            }
        }

        this.picture = newPicture;
        getAndSetEnergy(newPicture);
    }

    // 要求最坏情况下具有宽x高的时间复杂度
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // 类似于removeHorizontalSeam的实现

        if (seam == null) {
            throw new IllegalArgumentException("");
        }
        if (seam.length != this.picture.height()) {
            throw new IllegalArgumentException("");
        }
        if (this.picture.width() <= 1) {
            throw new IllegalArgumentException("");
        }
        checkSeam(seam);

        int oldWidth = this.picture.width();
        int oldHeight = this.picture.height();
        int newWidth = oldWidth - 1;

        Picture newPicture = new Picture(newWidth, oldHeight);

        for (int i = 0; i < oldHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (j > seam[i]) {
                    newPicture.setRGB(j, i, this.picture.getRGB(j + 1, i));
                }
                else {
                    newPicture.setRGB(j, i, this.picture.getRGB(j, i));
                }
            }
        }
    }

    private void checkSeam(int[] seam) {
        int last = seam[0];
        for (int i = 1; i < seam.length; i++) {
            int current = seam[i];
            if (Math.abs(current - last) > 1) {
                throw new IllegalArgumentException("");
            }
            last = seam[i];
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }

}
