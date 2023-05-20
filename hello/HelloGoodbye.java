/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }
        StdOut.printf("Hello %s and %s.\n", args[0], args[1]);
        StdOut.printf("Goodbye %s and %s.", args[1], args[0]);
        return;
    }
}
