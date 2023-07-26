/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MultiQueues {
    private Node[] first;
    private Node[] last;

    private int[] num;

    private class Node {
        int index;
        Node next;
    }

    public MultiQueues() {
        int radix = 256;
        first = new Node[radix];
        last = new Node[radix];
        num = new int[radix];
    }

    public boolean isEmpty(char c) {
        return first[c] == null;
    }

    public int size(char c) {
        return num[c];
    }

    public void enqueue(char c, int index) {
        Node oldlast = last[c];
        last[c] = new Node();
        last[c].index = index;
        last[c].next = null;

        if (isEmpty(c)) {
            first[c] = last[c];
        }
        else {
            oldlast.next = last[c];
        }
        num[c]++;
    }

    public int dequeue(char c) {
        int index = first[c].index;
        first[c] = first[c].next;
        if (isEmpty(c)) {
            last[c] = null;
        }
        num[c]--;
        return index;
    }
}
