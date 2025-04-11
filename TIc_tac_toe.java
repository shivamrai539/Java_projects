import java.util.Scanner;

public class TIc_tac_toe {

    public static void printBoard(char [][] board){
        for(int i=0; i<board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
        }
    }
    
    public static boolean haveWon(char [][] board , char player){
        for(int i=0; i< board.length; i++){
            if(board[i][0] == player && board[i][1] == player && board[i][2] == player){
                return true;
            }
        }


        for(int i=0; i< board[0].length; i++){
            if(board[0][i] == player && board[1][i] == player && board[2][i] == player){
                return true;
            }
        }

        if(board[0][0] == player && board[1][1] == player && board[2][2] == player){
            return true;
        }

        if(board[0][2] == player && board[1][1] == player && board[2][0] == player){
            return true;
        }
        return false;
    }

    public static void main(String args[]){
        char [][] board = new char[3][3];

        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                board[i][j] = ' ';
            }
        }

        char player = 'X';
        boolean gameover = false;
        Scanner sc = new Scanner(System.in);

        while(!gameover){
            printBoard(board);
            System.out.print("Player " + player + " enter : ");
            int row = sc.nextInt();
            int col = sc.nextInt();

            if(board[row][col] == ' '){
                board[row][col] = player;
                gameover = haveWon(board,player);

                if(gameover){
                    System.out.println("player " + player + " has won.");
                }else{
                    if(player == 'X'){
                        player = 'O';
                    }else{
                        player = 'X';
                    }
                }

            }else{
                System.out.println("Invalid input: Try again!");
            }
        }
        printBoard(board);
    }
}
