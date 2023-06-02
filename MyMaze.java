// Name: Alexander Sulistyo
// x500: sulis008
import java.util.Scanner;
public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        // Set up maze
        maze = new Cell[rows][cols];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = new Cell();
            }
        }
        // Other variables
        this.startRow = startRow;
        this.endRow = endRow;
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {
        // Implements Scanner to get maze size from user.
        Scanner scanner = new Scanner(System.in);
        System.out.println("|---|     Make a Maze     |---|");
        System.out.print("Amount of Rows (5-20): ");
        // Receive input from user
        int rows = scanner.nextInt();
        if (rows < 5 || rows > 20) {
            System.out.print("       "+rows + " is not in the range 5-20\n       Default 5 used.\n");
            rows = 5;
        }
        System.out.print("Amount of Columns (5-20): ");
        int cols = scanner.nextInt();
        if (cols <5 || cols > 20) {
            System.out.print("       "+cols + " is not in the range 5-20\n       Default 20 used.\n");
            cols = 20;
        }
        // Start maze generation
        MyMaze newMaze = new MyMaze(rows, cols, (int)(Math.random()*rows), (int)(Math.random()*rows));
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        stack.push(new int[]{newMaze.startRow, 0});
        newMaze.maze[newMaze.startRow][0].setVisited(true);
        while (!stack.isEmpty()) {
            int[] top = stack.top();
            int row = top[0];
            int col = top[1];
            boolean up = row - 1 >= 0 && !newMaze.maze[row - 1][col].getVisited();
            boolean down = row + 1 < rows && !newMaze.maze[row + 1][col].getVisited();
            boolean left = col - 1 >= 0 && !newMaze.maze[row][col - 1].getVisited();
            boolean right = col + 1 < cols && !newMaze.maze[row][col + 1].getVisited();
            boolean hasNeighbor = up || down || left || right;
            if (hasNeighbor) {
                // At least one direction is valid, choose a random valid direction
                // This process goes by chance, meaning it is not certain that the first
                // random choice will actually be valid. However, mathematically this loop
                // WILL EVENTUALLY break.
                int randomChoice = (int) (Math.random() * 4);
                while (true) {
                    if (up && randomChoice == 0) break;
                    if (down && randomChoice == 1) break;
                    if (left && randomChoice == 2) break;
                    if (right && randomChoice == 3) break;
                    // Loop until we get one of these matches
                    else randomChoice = (int) (Math.random() * 4);
                }
                switch (randomChoice) {
                    case 0:
                        stack.push(new int[]{row - 1, col});
                        newMaze.maze[row - 1][col].setVisited(true);
                        // Open Top
                        newMaze.maze[row - 1][col].setBottom(false);
                        break;
                    case 1:
                        stack.push(new int[]{row + 1, col});
                        newMaze.maze[row + 1][col].setVisited(true);
                        // Open Bottom
                        newMaze.maze[row][col].setBottom(false);
                        break;
                    case 2:
                        stack.push(new int[]{row, col - 1});
                        newMaze.maze[row][col - 1].setVisited(true);
                        // Open left
                        newMaze.maze[row][col - 1].setRight(false);
                        break;
                    case 3:
                        stack.push(new int[]{row, col + 1});
                        newMaze.maze[row][col + 1].setVisited(true);
                        // Open right
                        newMaze.maze[row][col].setRight(false);
                        break;
                }
            } else stack.pop();
        }
        // Set visited booleans to false
        for (int i = 0; i < newMaze.maze.length; i++) {
            for (int j = 0; j < newMaze.maze[0].length; j++) {
                newMaze.maze[i][j].setVisited(false);
            }
        }
        return newMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        String output = "";
        // Ceiling
        for (int i = 0; i < maze[0].length; i++) {
            output += "|---";
        }
        output += "|\n";
        // Row by Row
        for (int row = 0; row < maze.length; row++) {
            if (row != startRow) output += "|";
            else output += " ";
            for (int col = 0; col < maze[0].length; col++) {
                // Deal with visited
                if (maze[row][col].getVisited()) output += " * ";
                else output += "   ";
                // Deal with walls
                if (maze[row][col].getRight()) output += "|";
                else output += " ";
            }
            if (row == endRow) {
                output = output.substring(0, output.length()-1)+" ";
            }
            output += "\n";
            // In between rows
            if (row != maze.length-1) {
                for (int i = 0; i < maze[0].length; i++) {
                    if (maze[row][i].getBottom()) output += "|---";
                    else output += "|   ";
                }
                output += "|\n";
            }
        }
        // Floor
        for (int i = 0; i < maze[0].length; i++) {
            output += "|---";
        }
        output += "|";
        System.out.println(output);
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{startRow, 0});
        while (queue.length() > 0) {
            int[] front = queue.remove();
            int row = front[0];
            int col = front[1];
            // Current index is now visited
            maze[row][col].setVisited(true);
            // Break if visiting the exit index
            if (row == endRow && col == maze[0].length-1) break;
            boolean up = row - 1 >= 0 && !maze[row - 1][col].getVisited() && !maze[row-1][col].getBottom();
            boolean down = row + 1 < maze.length && !maze[row + 1][col].getVisited() && !maze[row][col].getBottom();
            boolean left = col - 1 >= 0 && !maze[row][col - 1].getVisited() && !maze[row][col-1].getRight();
            boolean right = col + 1 < maze[0].length && !maze[row][col + 1].getVisited() && !maze[row][col].getRight();
            if (up) queue.add(new int[]{row-1, col});
            if (down) queue.add(new int[]{row+1, col});
            if (left) queue.add(new int[]{row, col-1});
            if (right) queue.add(new int[]{row, col+1});
        }
        printMaze();
    }

    public static void main(String[] args){
        /*Make and solve maze */
        MyMaze maze = makeMaze();
        maze.solveMaze();
    }
}
