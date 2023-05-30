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
// 想法：
//      对于随机队列本身，
//          利用内联类维护双向链表，
//          在每次插入新元素时，随机插入到链表中的某个位置，
//          在返回时，返回当前的链表头即可
//      对于迭代器，
//          构造函数：
//              从链表头开始，复制每个节点，重复链表随机插入新元素的过程，得到一条新链表
//          hasNext和next：
//              需要维护一个指向当前迭代器的链表拷贝中的当前节点的引用，
//
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Deque<Item> deque;

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Iterator<Item> iterForDeque;

        public RandomizedQueueIterator() {
            Deque<Item> iterDeque = new Deque<Item>();
            for (Item i : deque) {
                if (StdRandom.bernoulli()) {
                    iterDeque.addFirst(i);
                }
                else {
                    iterDeque.addLast(i);
                }
            }
            iterForDeque = iterDeque.iterator();
        }


        public boolean hasNext() {
            return iterForDeque.hasNext();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("empty in iterator");
            }
            return iterForDeque.next();
        }

        public void remove() {
            throw new UnsupportedOperationException("unsupported: remove");
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        deque = new Deque<Item>();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    // return the number of items on the randomized queue
    public int size() {
        return deque.size();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null for RandomizedQueue.enqueue");
        }

        if (StdRandom.bernoulli()) {
            deque.addFirst(item);
        }
        else {
            deque.addLast(item);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty when dequeue");
        }

        if (StdRandom.bernoulli()) {
            return deque.removeFirst();
        }
        else {
            return deque.removeLast();
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty when sample");
        }

        Item item;
        item = dequeue();
        enqueue(item);
        return item;
    }

    // return an independent iterator over items in random order
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
