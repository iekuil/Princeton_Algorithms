/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final int teamsNumber;

    // 所有队伍的当前胜场
    private final int[] wins;

    // 所有队伍的当前败场
    private final int[] losses;

    // 所有队伍的剩余场次
    // 不能通过remainingGames计算出来的原因是，
    // remainingGames这个二维矩阵给出的是某队伍与当前小组中的其他队伍的剩余比赛情况，
    // 而一支队伍可能与小组外的队伍进行比赛，
    // 因此总的剩余场次可能多于与当前小组中其他队伍进行的比赛的总场次
    private final int[] remainings;

    // 队伍两两之间即将进行的比赛
    private final int[][] remainingGames;

    // 用字符串数组实现id到字符串之间的映射
    private final String[] idToString;

    // 实现字符串到队伍id之间的映射
    private final HashMap<String, Integer> stringToId;

    // 记录某个队伍在数学意义上是否已经被淘汰
    private boolean[] elimated;

    // 记录使特定队伍被淘汰的割集,
    private boolean[][] mincuts;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = Integer.parseInt(in.readLine());

        teamsNumber = n;
        wins = new int[n];
        losses = new int[n];
        remainings = new int[n];
        remainingGames = new int[n][n];
        idToString = new String[n];
        stringToId = new HashMap<>();
        elimated = new boolean[n];
        mincuts = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            String line = in.readLine();
            String[] splitLine = line.trim().split("\\s+");
            String name = splitLine[0];

            idToString[i] = name;
            stringToId.put(name, i);

            wins[i] = Integer.parseInt(splitLine[1]);
            losses[i] = Integer.parseInt(splitLine[2]);
            remainings[i] = Integer.parseInt(splitLine[3]);

            for (int j = 0; j < n; j++) {
                remainingGames[i][j] = Integer.parseInt(splitLine[4 + j]);
            }
        }

        for (int i = 0; i < teamsNumber; i++) {
            testATeam(i);
        }
    }

    private boolean isTrivialSituation(int id) {
        boolean elimatedFlag = false;
        for (int i = 0; i < teamsNumber; i++) {
            if (i == id) {
                continue;
            }
            if (wins[i] > wins[id] + remainings[id]) {
                elimatedFlag = true;
                mincuts[id][i] = true;
            }
        }
        elimated[id] = elimatedFlag;
        return elimatedFlag;
    }

    private void testATeam(int id) {
        // 这个函数需要检查序号为id的队伍是否在数学意义上已经被淘汰
        // 并将结果写入elimated和mincuts这两个数组中相应的位置
        //
        // 首先生成相应的流网络图FlowNetwork对象
        // 流网络图中共有teamsNumber - 1个队伍节点
        // 最多有 n*(n-1)/2个比赛节点
        //
        // 在图中，
        // 令节点0到节点n-1-1为队伍节点
        // 比赛节点编号从n - 1开始，最后一个比赛节点编号是n-1 + n(n-1)/2 - 1
        // 源节点编号为n-1 + n(n-1)/2，汇节点编号为n-1 + n(n-1)/2 + 1
        //
        // 利用
        // int gameId = 0
        // for (int i = 0.....) {
        //    for (int j = i + 1......) {
        //        ...
        //        gameId += 1;
        //    }
        //  }
        // 的形式，
        // 将所有比赛节点连接到相关的两个队伍节点,
        // 同时将源节点连接到所有比赛节点，

        if (isTrivialSituation(id)) {
            return;
        }

        int teamVerticesNumber = teamsNumber - 1;
        int gameVerticesNumber = (teamVerticesNumber * (teamVerticesNumber - 1)) / 2;

        FlowNetwork flowNet = new FlowNetwork(teamVerticesNumber + gameVerticesNumber + 1 + 1);

        int[] verticeidToId = new int[teamVerticesNumber];

        for (int i = 0; i < teamsNumber; i++) {
            if (i < id) {
                verticeidToId[i] = i;
            }
            else if (i == id) {
                continue;
            }
            else {
                verticeidToId[i - 1] = i;
            }
        }

        int gameVerticeId = 0;
        int srcId = gameVerticesNumber + teamVerticesNumber;
        int terminateId = srcId + 1;

        for (int i = 0; i < teamVerticesNumber; i++) {
            for (int j = i + 1; j < teamVerticesNumber; j++) {
                FlowEdge gameToTeam1 = new FlowEdge(gameVerticeId, gameVerticesNumber + i,
                                                    Double.POSITIVE_INFINITY);
                flowNet.addEdge(gameToTeam1);

                FlowEdge gameToTeam2 = new FlowEdge(gameVerticeId, gameVerticesNumber + j,
                                                    Double.POSITIVE_INFINITY);
                flowNet.addEdge(gameToTeam2);

                FlowEdge srcTogame = new FlowEdge(srcId, gameVerticeId,
                                                  remainingGames[verticeidToId[i]][verticeidToId[j]]);
                flowNet.addEdge(srcTogame);

                gameVerticeId += 1;
            }
        }

        for (int i = 0; i < teamVerticesNumber; i++) {
            int maxWins = wins[id] + remainings[id] - wins[verticeidToId[i]];
            FlowEdge teamToTerminate = new FlowEdge(gameVerticesNumber + i, terminateId, maxWins);
            flowNet.addEdge(teamToTerminate);
        }


        FordFulkerson solvement = new FordFulkerson(flowNet, srcId, terminateId);

        boolean elimatedFlag = false;
        for (FlowEdge e : flowNet.adj(srcId)) {
            int gameId = e.to();
            if (e.residualCapacityTo(gameId) != 0) {
                elimatedFlag = true;
            }
        }
        elimated[id] = elimatedFlag;

        if (elimatedFlag) {
            for (int i = 0; i < teamVerticesNumber; i++) {
                if (solvement.inCut(i + gameVerticesNumber)) {
                    mincuts[id][verticeidToId[i]] = true;
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamsNumber;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(idToString.clone());
    }

    // 给定队名，返回指定的队伍id
    private int checkAndGetTeamId(String team) {
        if (team == null) {
            throw new IllegalArgumentException("");
        }
        if (!stringToId.containsKey(team)) {
            throw new IllegalArgumentException("");
        }
        return stringToId.get(team);
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[checkAndGetTeamId(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return losses[checkAndGetTeamId(team)];
    }

    // number of losses for given team
    public int remaining(String team) {
        return remainings[checkAndGetTeamId(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return remainingGames[checkAndGetTeamId(team1)][checkAndGetTeamId(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return elimated[checkAndGetTeamId(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int id = checkAndGetTeamId(team);
        if (!elimated[id]) {
            return null;
        }
        ArrayList<String> subset = new ArrayList<>();

        for (int i = 0; i < teamsNumber; i++) {
            if (mincuts[id][i]) {
                subset.add(idToString[i]);
            }
        }
        return subset;
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
