/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CharSequence {

    private static char radix = 256;

    private Node first;

    private class Node {
        private final char c;
        private Node prev;
        private Node next;

        public Node(char c) {
            this.c = c;
            this.prev = null;
            this.next = null;
        }
    }

    public CharSequence() {
        // 初始化链表为ascii顺序
        for (char i = 0; i < radix; i++) {
            Node newNode = new Node((char) (radix - 1 - i));
            insertHead(newNode);
        }
    }

    public boolean isEmpty() {
        return first == null;
    }

    private void insertHead(Node node) {
        if (!isEmpty()) {
            first.prev = node;
        }
        Node oldFirst = first;
        first = node;
        first.next = oldFirst;
        first.prev = null;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }
    }

    public int getIndexAndSetFirst(char c) {
        // 沿着链表顺序查找字符c
        // 并把c设置为链表起点
        if (isEmpty()) {
            throw new IllegalArgumentException("");
        }
        int index = 0;
        for (Node node = first; node != null; node = node.next) {
            if (node.c == c) {
                removeNode(node);
                insertHead(node);
                return index;
            }
            index += 1;
        }
        return -1;
    }

}
