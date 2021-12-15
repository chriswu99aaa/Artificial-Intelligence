import java.util.ArrayList;  

public class MoveChooser {
  
    public static Move chooseMove(BoardState boardState)
    {
    
        //associate with infinity and negative inifinity in alpha beta pruning.
        int beta = Integer.MAX_VALUE; 
        int alpha = Integer.MIN_VALUE;
        int searchDepth= Othello.searchDepth;
        Move returnMove = new Move(0,0); 

        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
        }
        int bestMove = Integer.MIN_VALUE;
        
        for(Move move:moves)
        {
            BoardState boardCopy = boardState.deepCopy();
            int x = move.x;
            int y = move.y;
            boardCopy.makeLegalMove(x, y);

            int currentMove = minimax(boardCopy, searchDepth, 1, alpha, beta); //call minimax with searchdepth defined in Othello and 1 (as a maximazing player)

            if(bestMove < currentMove)
            {
                bestMove = currentMove;
                returnMove = move;
            }
        }

        return returnMove;
    }

    /***
     * evaluate the static value of a given board position
     * @param board: Type BoardState representing all squares occupied by white, black or empty with 1,-1,0.
     * @return return the utility value of a given board state as an integer
     */
    public static int evaluate(BoardState board)
    {
        int utility = 0;
        int[][] values = {{120, -20, 20,5,5,20,-20,120},
                            {-20, -40,-5,-5,-5,-5,-40, -40},
                            {20, -5, 15, 3, 3, 15, -5, 20},
                            {5, -5, 3, 3, 3, 3, -5, 5},
                            {5, -5, 3, 3, 3, 3, -5, 5},
                            {20, -5, 15, 3, 3, 15, -5, 20},
                            {-20, -40,-5,-5,-5,-5,-40, -40},
                            {120, -20, 20,5,5,20,-20,120}
                        };
        
        for(int i=0; i<8;i++)
        {
            for(int j=0; j<8; j++)
            {
                 utility += values[i][j]*(board.getContents(i, j));
            }
        }
        return utility;
    }

    /**
     * minimax alogorithm used for solving the best move questions
     * @param board Type BoardState, representing the current board state, i.e which square has white or black for neither piece.
     * @param depth Integer:depth of search
     * @param player Integer: 1 is white piece(maxmizing piece), -1 is black piece(minimizing piece)
     * @param alpha Integer: Current best value for maximzing piece
     * @param beta Integer: Current best value for minimizing piece
     * @return The best static value of a board state resulted from choosing a sequence of legal move
     */
    public static int minimax(BoardState board, int depth, int player,int alpha,int beta)
    {
        if(depth==0 || board.gameOver())
        {
            return evaluate(board);
        }
        if(player==1) //white piece
        {
            int bestAlpha = Integer.MIN_VALUE;

            ArrayList<Move> moves = board.getLegalMoves(); //get legal moves under given board state.
            
            for(Move move: moves) 
            {
                BoardState boardCopy = board.deepCopy(); //get a fresh copy for each move.
                
                boardCopy.makeLegalMove(move.x, move.y);
                
                int utility = minimax(boardCopy, depth-1, -1, alpha, beta); // player = -1 means the choice of white piece depends on black piece
                 
                bestAlpha = Math.max(utility,bestAlpha);
                
                alpha = Math.max(utility,alpha); //get the max board state value
                
                if(alpha>=beta){
                    break;
                }               
            }
            return bestAlpha;
        }
        else if(player==-1) //black piece
        {
            int bestBeta = Integer.MAX_VALUE;   

            ArrayList<Move> moves = board.getLegalMoves();

            for(Move move:moves) //looping through all child
            {
                BoardState boardCopy = board.deepCopy();
                
                boardCopy.makeLegalMove(move.x, move.y);
                
                int utility = minimax(boardCopy, depth-1, 1, alpha, beta); //1 means this is a maxmizing player.

                bestBeta = Math.min(utility, bestBeta); 

                beta = Math.min(utility,beta); //change beta if the utility value is smaller than beta

                if(alpha>=beta){ //alpha beta pruning
                    break;
                }
            }
            return bestBeta;

        }
        return 0;
    }

}