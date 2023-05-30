/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// 随机队列: 类似于栈或队列，区别在于每次进行pop操作删除的是随机元素
// 迭代器要求:
//          每个迭代器都会有不同的随机顺序
// 性能要求:
//          每个操作平摊下来拥有常量级的时间（对于常数c，m个操作最多总共执行cm个步骤）
//          n个元素的随机队列最多占用48n + 192 bytes
//          迭代器的每个操作在最坏情况下要拥有常量级的时间，迭代器的构造函数是线性时间的
//          对于每个迭代器，将会需要线性比例的额外内存空间占用
//
// 想法0：
//      对于随机队列本身，
//          利用内联类维护双向链表，
//          在每次插入新元素时，随机插入到链表中的某个位置，
//          在返回时，返回当前的链表头即可
//      对于迭代器，
//          构造函数：
//              从链表头开始，复制每个节点，重复链表随机插入新元素的过程，得到一条新链表
//          hasNext和next：
//              需要维护一个指向当前迭代器的链表拷贝中的当前节点的引用，
// （实际上，因为“插入到列表某个位置”这个动作要求一定程度的链表遍历，带来的时间复杂度没法满足要求）
// （所以对于这个想法，选择有一半的几率插入到deque前方，另外一半插入到后方）
// （这个方法带来的问题是，随机队列的随机程度不够高，各种组合的可能性是不等的，导致有些用例过不了）
// （另外一个问题是关于迭代器的，迭代器的内存占用超出了8n+72的要求，达到了48n+64）
//
// 想法1：
//      对于随机队列本身：
//          采用数组进行实现，
//          数组的好处在于可以便捷地进行随机存取，
//          从而在较低的开销下达到足够均匀的随机（uniformly random）
//      对于迭代器：
//          存储一个int型的数组，随机生成不重复的数字作为随机队列数组的下标，
//          int型的大小是4个字节，能够满足8n+72的要求
//
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;

    private int currentSize;

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int[] indexArray;
        private int indexForIndexArray;

        public RandomizedQueueIterator() {
            indexArray = StdRandom.permutation(currentSize, currentSize);
            indexForIndexArray = 0;
        }

        public boolean hasNext() {
            return indexForIndexArray != indexArray.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("empty when RandomizedQueueIterator.Next()");
            }

            Item returnValue = array[indexArray[indexForIndexArray]];
            indexForIndexArray += 1;
            return returnValue;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "unsupported: RandomizedQueueIterator.remove()");
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[4];
        currentSize = 0;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < currentSize; i++) {
            temp[i] = array[i];
        }
        array = temp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return currentSize;
    }

    // add the item
    public void enqueue(Item item) {
        // 生成一个不大于currentSize的随机数作为新元素在数组中的位置
        // 如果该位置处已经有元素存在，将旧元素移动到末尾，将新元素插入到该位置
        // 否则直接将新元素插入到数组中
        // currentSize加一

        if (item == null) {
            throw new IllegalArgumentException("null for RandomizedQueue.enqueue");
        }

        if (currentSize == array.length) {
            resize(2 * array.length);
        }

        int index = StdRandom.uniformInt(currentSize + 1);

        // 得到的index刚好是当前数组的有效部分的下一个位置
        if (index == currentSize) {
            array[currentSize] = item;
        }
        else {
            Item old = array[index];
            array[index] = item;
            array[currentSize] = old;
        }

        currentSize += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        // 生成一个不大于currentSize的随机数作为将要删除的元素的下标
        // 将该位置的item保存下来，用于之后的返回值
        // 将array[currentSize]处的元素拷贝到该处，array[currentSize]设为null
        // currentSize减一
        // 返回之前保存的item

        if (isEmpty()) {
            throw new NoSuchElementException("empty when RandomizedQueue.dequeue");
        }

        Item returnValue;

        returnValue = array[currentSize - 1];
        array[currentSize - 1] = null;
        currentSize -= 1;

        if (currentSize > 0 && currentSize == array.length / 4) {
            resize(array.length / 2);
        }

        return returnValue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // 生成一个不大于currentSize的随机数作为目标元素的下标
        // 读取对应item进行返回
        if (isEmpty()) {
            throw new NoSuchElementException("empty when RandomizedQueue.sample");
        }

        int index = StdRandom.uniformInt(currentSize);
        return array[index];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<String>();

        StdOut.printf("enqueue: 0,1,2,3,4,5\n");
        for (int i = 0; i < 6; i++) {
            test.enqueue(Integer.toString(i));
        }

        StdOut.printf("try sample for six times\n");
        for (int i = 0; i < 6; i++) {
            StdOut.printf("%s", test.sample());
        }
        StdOut.println();

        StdOut.printf("current size: %d, empty? %b\n", test.size(), test.isEmpty());

        StdOut.printf("remove 3 randon elements\n");
        for (int i = 0; i < 3; i++) {
            StdOut.printf("%s", test.dequeue());
        }
        StdOut.println();

        StdOut.printf("try iterator:\n");
        for (String s : test) {
            StdOut.printf("%s", s);
        }
        StdOut.println();


    }

}
