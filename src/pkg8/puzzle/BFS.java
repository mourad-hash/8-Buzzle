/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8.puzzle;

import java.util.*;

public class BFS{
    private static final int[] DX = {0, 0, -1, 1};
    private static final int[] DY = {-1, 1, 0, 0};
    private static final String[] DIRECTION = {"U", "D", "L", "R"};
    public static int count=0;
    private static class State {
        int[][] board;
        int zeroRow;
        int zeroCol;
        String path;
       

        public State(int[][] board, int zeroRow, int zeroCol, String path) {
            this.board = board;
            this.zeroRow = zeroRow;
            this.zeroCol = zeroCol;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        Scanner input =new Scanner(System.in);

        int[][] initialBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                initialBoard[i][j] = input.nextInt();
            }
        }

        String solutionPath = solve(initialBoard);
        System.out.println("Solution Path: " + solutionPath);
    }

    public static String solve(int[][] initialBoard) {
      
        int[][] targetBoard = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

       Queue<State> queue = new LinkedList<>();
       Set<String> visited = new HashSet<>();

        State initialState = new State(initialBoard, findZeroRow(initialBoard), findZeroCol(initialBoard), "");
        queue.offer(initialState);
        visited.add(Arrays.deepToString(initialBoard));

        while (!queue.isEmpty()) {
            
            State currentState = queue.poll();

            if (Arrays.deepEquals(currentState.board, targetBoard)) {
                return currentState.path; 
            }

            for (int i = 0; i < 4; i++) {
                int newRow = currentState.zeroRow + DY[i];
                int newCol = currentState.zeroCol + DX[i];
                
                if (isValidMove(newRow, newCol)) {
                    int[][] newBoard = cloneBoard(currentState.board);
                    swap(newBoard, currentState.zeroRow, currentState.zeroCol, newRow, newCol);

                    String boardString = Arrays.deepToString(newBoard);
                    if (!visited.contains(boardString)) {
                        String newPath = currentState.path + DIRECTION[i];
                        State newState = new State(newBoard, newRow, newCol, newPath);
                      //  System.out.println(newPath);
                      count++;
                        queue.offer(newState);
                        visited.add(boardString);
                    }
                }
            }
        }

        return "No solution found";
    }
    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    private static int[][] cloneBoard(int[][] board) {
        int[][] clone = new int[3][3];
        for (int i = 0; i < 3; i++) {
            clone[i] = Arrays.copyOf(board[i], 3);
        }
        return clone;
    }

    private static void swap(int[][] board, int row1, int col1, int row2, int col2) {
        int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    private static int findZeroRow(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1; // Zero not found
    }

    private static int findZeroCol(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return j;
                }
            }
        }
        return -1; // Zero not found
    }
}