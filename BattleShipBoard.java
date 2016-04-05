import java.util.Random;
import java.util.Scanner;

/**
 * Created by mbp on 2/28/16.
 */
public class BattleShipBoard {

    private Tile[][] GameBoard;

    private int m;
    private int n;

    private int ship_count;
    private int firecount;

    public BattleShipBoard()
    {
        System.out.print("Please enter dimensions for a gameboard: ");
    }

    public BattleShipBoard(int rows, int columns)
    {
        if(rows * columns >= 9 && rows * columns <= 100)     // Creates an array of Tile objects as a gameboard.
        {                                                   // Tiles allow for easy checking of mines, ships, etc.
            m = rows;
            n = columns;
            int count = 0;
            GameBoard = new Tile[rows][columns];
            for(int i = 0; i < GameBoard.length; i++)
            {
                for(int j = 0; j < GameBoard[i].length;j++)
                {
                    Tile t1 = new Tile();
                    GameBoard[i][j] = t1;
                    GameBoard[i][j].tile_row = i;
                    GameBoard[i][j].tile_columns = count;
                    count++;
                }
                count = 0;
                System.out.println("\n");
            }
            createShips();
            createMines();
            displayBoard();
        }
        else{System.out.println("Please enter a valid game board coordinate: ");}

    }
    public boolean checkSunk(int row, int column)
    {
        // Checks the two adjacent tiles LEFT and Right as well as Two tiles Above and Below
        // To see if they are of the same ship, if they have are the same ship and already both hit then
        // The ship is sunk
        boolean sunk = false;
        if(row - 1 >= 0 && row + 1 < m && GameBoard[row][column].ship == GameBoard[row - 1][column].ship && GameBoard[row][column].ship == GameBoard[row + 1][column].ship && GameBoard[row - 1][column].notHit == false && GameBoard[row + 1][column].notHit == false){sunk = true;}
        else if(row - 2 >= 0 && GameBoard[row][column].ship == GameBoard[row - 1][column].ship && GameBoard[row][column].ship == GameBoard[row - 2][column].ship && GameBoard[row - 1][column].notHit == false && GameBoard[row - 2][column].notHit == false){sunk = true;}
        else if(row + 2 < m && GameBoard[row][column].ship == GameBoard[row + 1][column].ship && GameBoard[row][column].ship == GameBoard[row + 2][column].ship && GameBoard[row + 1][column].notHit == false && GameBoard[row + 2][column].notHit == false){sunk = true;}

        if(column - 1 >=0 && column + 1 < n && GameBoard[row][column].ship == GameBoard[row][column - 1].ship && GameBoard[row][column].ship == GameBoard[row][column + 1].ship && GameBoard[row][column - 1].notHit == false && GameBoard[row][column + 1].notHit == false){sunk = true;}
        else if(column - 2 >= 0 && GameBoard[row][column].ship == GameBoard[row][column - 1].ship && GameBoard[row][column].ship == GameBoard[row][column - 2].ship && GameBoard[row][column - 1].notHit == false && GameBoard[row][column - 2].notHit == false){sunk = true;}
        else if(column + 2 < n && GameBoard[row][column].ship == GameBoard[row][column + 1].ship && GameBoard[row][column].ship == GameBoard[row][column + 2].ship && GameBoard[row][column + 1].notHit == false && GameBoard[row][column + 2].notHit == false){sunk = true;}

        return sunk;
    }
    public int canMakeShip(int row, int column) // return -1 if can't make ship, 0 if horizontal ship, 1 if vertical ship.
    {
        int ship_orientation = -1;
        // Makes a ship using ROW, COl as a midpoint and finding two tiles either above or below it that
        // Don't have a ship or mine to create a shipp.
        // If the two are below/above then ship is vertical....etc
        if(row - 1 >= 0 && row + 1 < m)
        {
            if(GameBoard[row-1][column].hasMine == false && GameBoard[row -1][column].hasShip == false && GameBoard[row+1][column].hasMine == false && GameBoard[row+1][column].hasShip == false)
            {
                ship_orientation = 0; // vertical ship
            }
        }
        else if(column - 1 >= 0 && column + 1 < n)
        {
            if(GameBoard[row][column-1].hasMine == false && GameBoard[row][column -1].hasShip == false && GameBoard[row][column + 1].hasMine == false && GameBoard[row][column + 1].hasShip == false)
            {
                ship_orientation = 1; // horizontal ship
            }
        }
        return ship_orientation;
    }
    public void createShips()  // Finds three consecutive spaces on the grid, then sends them to ship Constructor.
    {
        // Generates a random number between m (game board width array) and n - 1 (game board length array)
        // Use these two numbers as a coordinate to go to and create a ship there based on the result of
        //canMakeSHip().....
        // Increments shipcount and decrements ships_to_make.

        Random random;
        int tile_rows;
        int tile_columns;
        int ships_to_make;

        if(m * n >= 9 && m * n <= 16){ships_to_make = 1;}
        else if(m * n >= 17 && m * n <= 36){ships_to_make = 2;}
        else{ships_to_make = 3;}

        int i = 1;

        while(ships_to_make > 0)
        {
            random = new Random();
            tile_rows = random.nextInt(m - 1);
            tile_columns = random.nextInt(n - 1);

            if(GameBoard[tile_rows][tile_columns].hasMine == false && GameBoard[tile_rows][tile_columns].hasShip == false)
            {
                if(canMakeShip(tile_rows, tile_columns) == 0)
                {

                    GameBoard[tile_rows][tile_columns].hasShip = true;
                    GameBoard[tile_rows - 1][tile_columns].hasShip = true;
                    GameBoard[tile_rows + 1][tile_columns].hasShip = true;

                    GameBoard[tile_rows][tile_columns].ship = i;
                    GameBoard[tile_rows - 1][tile_columns].ship = i;
                    GameBoard[tile_rows + 1][tile_columns].ship = i;

                    i++;
                    ships_to_make--;
                    ship_count++;
                }
                else if(canMakeShip(tile_rows, tile_columns) == 1)
                {
                    GameBoard[tile_rows][tile_columns].hasShip = true;
                    GameBoard[tile_rows][tile_columns - 1].hasShip = true;
                    GameBoard[tile_rows][tile_columns + 1].hasShip = true;

                    GameBoard[tile_rows][tile_columns].ship = i;
                    GameBoard[tile_rows][tile_columns - 1].ship = i;
                    GameBoard[tile_rows][tile_columns + 1].ship = i;

                    i++;
                    ships_to_make--;
                    ship_count++;
                }
            }
        }
    }
    public void createMines()
    {
        // Finds a coordinate that doesn't have a ship or a mine
        // Creates a mine there and changed the value of the Tiles boolean hasMine to true.
        Random random;
        int tile_rows;
        int tile_columns;
        int number_of_mines;


        if(m * n >= 9 && m * n <= 16){number_of_mines = 1;}
        else if(m * n >= 17 && m * n <= 36){number_of_mines = 2;}
        else{number_of_mines = 3;}

        while(number_of_mines > 0)
        {
            random = new Random();
            tile_rows = random.nextInt(m);
            tile_columns = random.nextInt(n);

            if(GameBoard[tile_rows][tile_columns].hasShip == false && GameBoard[tile_rows][tile_columns].hasMine == false)
            {
                GameBoard[tile_rows][tile_columns].hasMine = true;
                number_of_mines--;
            }
        }

    }
    public void fire(int row, int column)
    {

        if(GameBoard[row][column].hasMine && GameBoard[row][column].mineHit == false)
        {
            System.out.println("You hit a mine! ");firecount = firecount + 2;
            GameBoard[row][column].mineHit =true;
        }
        else if(GameBoard[row][column].hasShip && GameBoard[row][column].notHit == true)
        {
            firecount++;
            GameBoard[row][column].notHit = false; //change notHit to false
            System.out.println("HIT!");
            if(checkSunk(row, column)){ship_count--;System.out.println("You have sunk a ship!");}
        }
        else if(GameBoard[row][column].hasShip && GameBoard[row][column].notHit == false)
        {
            System.out.println("You already hit this tile! ");
            if(checkSunk(row, column)){ship_count--;System.out.println("You have sunk a ship!");}
            firecount = firecount + 2;
        }
        else
        {
            firecount++;
            if(veryCloseToShip(row, column)){System.out.println("A Miss, but Very Close");} // Check nearby tiles in 1 square radius
            else if(closeToShip(row, column)){System.out.println("A Miss, but Close");} // Check nearby tiles in 2 square radius
            else{System.out.println("Miss!");}
        }
    }
    public boolean veryCloseToShip(int row, int column)
    {
        boolean veryClose = false;
        //row - 1, col
        if(row - 1 >= 0 && GameBoard[row - 1][column].hasShip && GameBoard[row - 1][column].notHit){veryClose = true;}
        // row + 1 , col
        else if(row + 1 < m && GameBoard[row + 1][column].hasShip && GameBoard[row + 1][column].notHit){veryClose = true;}
        // row, col -1
        else if(column - 1 >= 0 && GameBoard[row][column - 1].hasShip && GameBoard[row][column - 1].notHit){veryClose = true;}
        //row , col + 1
        else if(column + 1 < n && GameBoard[row][column + 1].hasShip && GameBoard[row][column + 1].notHit){veryClose = true;}
        return veryClose;
    }
    public boolean closeToShip(int row, int column)
    {
        boolean close = false;
        //row - 1, col
        if(row - 2 >= 0 && GameBoard[row - 2][column].hasShip && GameBoard[row - 2][column].notHit){close = true;}
        // row + 1 , col
        else if(row + 2 < m && GameBoard[row + 2][column].hasShip && GameBoard[row + 2][column].notHit){close = true;}
        // row, col -1
        else if(column - 2 >= 0 && GameBoard[row][column - 2].hasShip && GameBoard[row][column - 2].notHit){close = true;}
        //row , col + 1
        else if(column + 2 < n && GameBoard[row][column + 2].hasShip && GameBoard[row][column + 2].notHit){close = true;}

        return close;
    }
    public void displayBoard()
    {
        for(int i = 0; i < GameBoard.length; i++)
        {
            for(int j = 0; j < GameBoard[i].length;j++)
            {

                if(GameBoard[i][j].hasMine && GameBoard[i][j].mineHit)
                {
                    System.out.print("M" + " ");
                }
                else if(GameBoard[i][j].hasShip && GameBoard[i][j].notHit == false)
                {
                    System.out.print("S" + " ");
                }
                else{System.out.print("X" + " ");}
            }
            System.out.println("\n");
        }
    }
    public void debugDisplayBoard()
    {
        for(int i = 0; i < GameBoard.length; i++)
        {
            for(int j = 0; j < GameBoard[i].length;j++)
            {

                if(GameBoard[i][j].hasMine)
                {
                    System.out.print(" M" + " " );
                }
                else if(GameBoard[i][j].hasShip && GameBoard[i][j].notHit)
                {
                    System.out.print(" S" + " ");
                }
                else if(GameBoard[i][j].hasShip && GameBoard[i][j].notHit == false){System.out.print("H" + " ");}
                else{System.out.print(" N" + " ");}
            }
            System.out.println("\n");
        }
    }
    public int getShipCount(){return ship_count;}
    public int getFireCount(){return firecount;}
    public static void main(String[] args)
    {
        boolean play_again = true;
        boolean debugmode = false;

        int ships_remaining;
        int shots_fired;
        int high_score;
        int times_played;
        int game_row;
        int game_column;

        Scanner scanner;
        Scanner again;
        Scanner debug;


        times_played = 1;
        high_score = 0;
        while(play_again)
        {
            scanner = new Scanner(System.in);
            System.out.print("Enter the Dimensions: ");
            game_row = scanner.nextInt();
            game_column = scanner.nextInt();
            BattleShipBoard board = new BattleShipBoard(game_row, game_column);
            ships_remaining = board.getShipCount();
            shots_fired = board.getFireCount();

            System.out.println("Welcome to Battleship!");
            System.out.println("To begin playing enter your coordinates like ex 1 5 ");
            System.out.println("0 0 corresponds to the top leftmost corner");
            System.out.print("\n");
            System.out.print("\n");
            System.out.println("There are: " + ships_remaining + " ships on the board");

            if(times_played == 1){System.out.println("Best Score: not set");}
            else{System.out.println("Best Score: " + high_score);}
            System.out.print("Press Y for debug mode: ");
            debug = new Scanner(System.in);
            if(debug.next().charAt(0) == "y".charAt(0) ||debug.next().charAt(0) == "Y".charAt(0))
            {
                debugmode = true;
                System.out.println("Ok, in Debug Mode");
            }

            while(ships_remaining > 0)
            {
                System.out.print("Fire at Coordinates: ");
                scanner = new Scanner(System.in);
                game_row = scanner.nextInt();
                game_column = scanner.nextInt();
                if(game_row >= 0 && game_row < board.m && game_column >= 0 && game_column < board.n)
                {
                    board.fire(game_row, game_column);
                }
                else
                {
                    System.out.println("Please enter a valid coordinate. ");
                }
                if(debugmode)
                {
                    board.debugDisplayBoard();
                }
                else
                {
                    board.displayBoard();
                }
                ships_remaining = board.getShipCount();

                System.out.println("Ships remaining: " + ships_remaining);
                shots_fired = board.getFireCount();
                System.out.println("Shots Fired: " + shots_fired);
            }
            System.out.println("Congratulations! You won the game!");
            System.out.print("\n");
            System.out.print("Would you like to play again? Press n or N to stop: ");
            again = new Scanner(System.in);

            if(times_played == 1){high_score = shots_fired;}
            else if(times_played > 1 && shots_fired < high_score){high_score = shots_fired;}
            times_played++;
            if(again.next().toLowerCase().charAt(0) == "n".charAt(0))
            {
                play_again = false;
                times_played = 0;
                System.out.println("This was your best score: " + high_score);
            }

        }
    }
}