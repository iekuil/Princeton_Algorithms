/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {

    // 对于求解最大流最小割问题，可以使用FordFulkerson类解决问题
    // 而要使用这个类，需要自行构建好用FlowNetwork类表示的图
    // FordFulkerson类本身不保存指向FlowNetwork对象的指针，
    // 而是直接通过传给构造函数的FlowNetwork指针访问各个FlowEdge对象并对各个边进行修改
    //
    // 即，
    // 从文件中读取各个队伍的赛况，
    //    每两个队伍之间的比赛用一个节点表示，
    //    每个队伍都用一个单独的节点表示，
    //    源节点连接到该竞赛节点，容量为两个队伍之间剩余的比赛数，
    //    竞赛节点连接到两个相关的队伍节点，容量为正无穷，
    //    队伍节点连接到汇节点，容量为在该队伍不胜过目标队伍的情况下最多还可赢的比赛数量
    // 从转化成图中定点和边的关系，构建FlowNetwork对象
    // 将FlowNetwork交给FordFulkerson处理，解决最大流最小割问题
    // 最后检查从源节点s出发的有向边是否达到容量上限

    // 所有队伍的当前胜场
    private int[] wins;

    // 所有队伍的当前败场
    private int[] losses;

    // 所有队伍的剩余场次
    // 不能通过remainingGames计算出来的原因是，
    // remainingGames这个二维矩阵给出的是某队伍与当前小组中的其他队伍的剩余比赛情况，
    // 而一支队伍可能与小组外的队伍进行比赛，
    // 因此总的剩余场次可能多于与当前小组中其他队伍进行的比赛的总场次
    private int[] remainings;

    // 队伍两两之间即将进行的比赛
    private int[][] remainingGames;

    // 用字符串数组实现id到字符串之间的映射
    private String[] idToString;

    // 实现字符串到队伍id之间的映射
    private HashMap<String, Integer> stringToId;

    // 记录某个队伍在数学意义上是否已经被淘汰
    private boolean[] elimated;

    // 记录使特定队伍被淘汰的割集
    private Iterable<Integer>[] mincuts;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();

        wins = new int[n];
        losses = new int[n];
        remainings = new int[n];
        remainingGames = new int[n][n];
        idToString = new String[n];
        stringToId = new HashMap<>();
        elimated = new boolean[n];
        mincuts = new ArrayList<Integer>[n];


        for (int i = 0; i < n; i++) {
            String line = StdIn.readLine();
            String[] splitLine = line.split("\\s+");
            String name = splitLine[0];
            int win = Integer.parseInt(splitLine[1]);
            int lose = Integer.parseInt(splitLine[2]);

        }
    }

    // number of teams
    public int numberOfTeams() {
    }

    // all teams
    public Iterable<String> teams() {
    }

    // 给定队名，返回指定的队伍id
    private int getTeamId(String team) {

    }

    // number of wins for given team
    public int wins(String team) {
    }

    // number of losses for given team
    public int losses(String team) {
    }

    // number of losses for given team
    public int remaining(String team) {
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
