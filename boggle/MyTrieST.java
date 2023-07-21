/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MyTrieST {
    // 缓存节点：存储上一次查询后的节点
    // 构造函数
    // 插入新节点 （采用循环实现）
    // 查找节点（采用循环实现，从根节点开始，并刷新缓存）
    // 查找节点（采用循环实现，从缓存节点开始，并更新缓存

    private static int radix = 26;
    private final Node root;

    private class Node {
        private boolean isTail;
        private Node[] next = new Node[radix];
    }

    public MyTrieST() {
        root = new Node();
    }

    public MyTrieST(Node sub) {
        root = sub;
    }

    private int modLetterA(char letter) {
        return letter - 'A';
    }

    // 插入一条新的字符串
    public void put(String s) {
        Node node = root;
        for (int d = 0; d <= s.length(); d++) {
            if (d == s.length()) {
                node.isTail = true;
                return;
            }
            int index = modLetterA(s.charAt(d));
            if (node.next[index] == null) {
                node.next[index] = new Node();
            }
            node = node.next[index];
        }
    }

    // 判断当前的Trie中是否存在与s完全对应的有效字符串
    public boolean contains(String s) {
        Node node = root;
        for (int d = 0; d <= s.length(); d++) {
            if (node == null) {
                return false;
            }
            if (d == s.length()) {
                return node.isTail;
            }
            int index = modLetterA(s.charAt(d));
            node = node.next[index];
        }
        return false;
    }

    // 判断s是否是Trie中某个有效字符串的一部分
    public boolean containsPrefix(String s) {
        Node node = root;
        for (int d = 0; d <= s.length(); d++) {
            if (node == null) {
                return false;
            }
            if (d == s.length()) {
                return true;
            }
            int index = modLetterA(s.charAt(d));
            node = node.next[index];
        }
        return true;
    }

    // 根据给定的字符串查找子节点，并生成一个以该子节点为根节点的Trie
    public MyTrieST getSubTrie(char c) {
        if (root == null) {
            return new MyTrieST(null);
        }
        return new MyTrieST(root.next[modLetterA(c)]);
    }

    public boolean isStrTail() {
        return root.isTail;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
