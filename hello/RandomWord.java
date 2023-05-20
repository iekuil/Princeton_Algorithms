/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        String tmp = "";
        double i = 0;
        double p;
        while (!StdIn.isEmpty()) {
            i += 1;
            p = 1 / i;
            tmp = StdIn.readString();
            if (StdRandom.bernoulli(p)) {
                champion = tmp;
            }
        }

        StdOut.println(champion);
    }
}
