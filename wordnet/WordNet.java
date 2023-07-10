/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class WordNet {

    // 构造函数：
    //    建立节点编号和synset内字符串的双向映射：
    //       从文件1中读取synset编号及synset
    //       经过字符串处理之后，
    //       将synset中的字符串组织成一个单元（链表/set/变长数组），并使用数组建立从编号到单元的映射
    //       之后利用哈希表，建立synset中的每个字符串到该synset编号的映射
    //
    //    用数据结构对图进行表示：
    //       从文件2中读取节点之间的指向关系，
    //       构造Digraph对象并逐步向里面添加有向边
    //
    //    （用于distance和sap方法）
    //       利用得到的Digraph对象初始化一个SAP对象
    //
    //    （用于isNoun方法）
    //       围绕该图进行DFS
    //
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
    }

    // 进行BFS，
    // 将访问到的每个节点都加入额外的队列中
    // 随后将给节点序列中所有映射的字符串都加入到一个字符串队列中
    //
    // returns all WordNet nouns
    public Iterable<String> nouns() {
    }

    // 从word出发，向noun进行dfs
    // 如果是对逆向图进行dfs，以noun为根节点最终会形成很多分支，带来不必要的时间开销
    // 而从word出发，
    // 如果word真的是noun的子类，
    // 只需要沿着一条路径前进最终一定会在noun节点停下
    //
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
    }

    // 利用SAP类实现
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
    }

    // 利用SAP类实现
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
