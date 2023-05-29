/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// deque: 双向队列，可以选择在队列的[头/尾]进行[新增/删除]
// 性能要求:
//          在最坏情况下deque的每个操作都是常量时间
//          包含n个元素的deque最多占用48n+192 bytes
//          迭代器的每个操作在最坏情况下都是常量时间
//
// 想法: 声明一个内联类，作双向链表
//      同时维护指向链表头和尾的两个引用，从而在新增或删除的时候可以获得常量级的时间
//      对于迭代器，需要维护指向当前对象的引用，从而在hasNext()和next()的时候有常量级的时间
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    private Node first, last;

    private int currentSize;

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("empty in iterator");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("unsupported: remove");
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        currentSize = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null || last == null;
    }

    // return the number of items on the deque
    public int size() {
        return currentSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null for addFirst");
        }

        currentSize += 1;
        Node oldfirst = first;

        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;

        if (isEmpty()) {
            last = first;
        }
        else {
            oldfirst.prev = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null for addLast");
        }

        currentSize += 1;
        Node oldlast = last;

        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;

        if (isEmpty()) {
            first = last;
        }
        else {
            oldlast.next = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty in removeFirst");
        }

        currentSize -= 1;
        Item item = first.item;
        first = first.next;

        if (isEmpty()) {
            last = null;
        }
        else {
            first.prev = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty when removeLast");
        }

        currentSize -= 1;
        Item item;
        item = last.item;
        last = last.prev;

        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");

        StdOut.printf("there should be 1,2,3\n");
        for (String i : deque) {
            StdOut.printf("%s", i);
        }
        StdOut.println();

        deque.addFirst("4");
        deque.addFirst("5");
        deque.addFirst("6");

        StdOut.printf("there should be 6,5,4,1,2,3\n");
        for (String i : deque) {
            StdOut.printf("%s", i);
        }
        StdOut.println();

        StdOut.printf("pop head two: %s, %s\n", deque.removeFirst(), deque.removeFirst());

        StdOut.printf("there should be 4,1,2,3\n");
        for (String i : deque) {
            StdOut.printf("%s", i);
        }
        StdOut.println();

        StdOut.printf("pop last one: %s\n", deque.removeLast());

        StdOut.printf("there should be 4,1,2\n");
        for (String i : deque) {
            StdOut.printf("%s", i);
        }
        StdOut.println();

        StdOut.printf("there should be 3 elements\n");
        StdOut.printf("%d\n", deque.size());

        StdOut.printf("empty? %b \n", deque.isEmpty());

    }
}
