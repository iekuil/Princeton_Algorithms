/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        int length = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }

        Iterator<String> iter = rq.iterator();

        for (int i = 0; i < length; i++) {
            if (iter.hasNext()) {
                StdOut.println(iter.next());
            }
            else {
                break;
            }
        }
    }
}
