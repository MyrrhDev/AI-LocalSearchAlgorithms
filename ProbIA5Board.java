package IA.ProbIA5;

/**
 * Created by bejar on 17/01/17.
 */
public class ProbIA5Board {
    /* Class independent from AIMA classes
       - It has to implement the state of the problem and its operators
     *

    /* State data structure
        vector with the parity of the coins (we can assume 0 = heads, 1 = tails
     */

    private int [] board; //tamaño 5
    private static int [] solution; //tamaño 5

    /* Constructor */
    public ProbIA5Board(int []init, int[] goal) {

        board = new int[init.length];
        solution = new int[init.length];

        for (int i = 0; i< init.length; i++) {
            board[i] = init[i];
            solution[i] = goal[i];
        }

    }

    /* vvvvv TO COMPLETE vvvvv */
    public void flip_it(int i){
        // flip the coins i and i + 1
        board[i] = 1 - board[i];
        if(i == board.length)
            board[0] = 1 - board[0];
        else
            board[i+1] = 1 - board[i+1];
    }

    /* Heuristic function */
    public double heuristic(){
        // compute the number of coins out of place respect to solution
        double places = 0;
        for(int i = 0; i < board.length; i++) {
            if(board[i] != solution[i]) ++places;
        }
        return places;
    }

     /* Goal test */
     public boolean is_goal(){
         // compute if board = solution
         return heuristic() == 0.00;
     }

     /* auxiliary functions */

     // Some functions will be needed for creating a copy of the state
     public int [] CopiaBoard(int [] board) {
         int [] copia = new int [board.length];
         for(int i = 0; i < board.length; i++) {
             copia[i] = board[i];
         }
         return copia;
     }

    /* ^^^^^ TO COMPLETE ^^^^^ */

}
