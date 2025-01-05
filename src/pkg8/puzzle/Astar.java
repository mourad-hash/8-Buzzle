/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8.puzzle;

import java.util.*;

class Node {
    int[][] state;
    Node parent;
    int g;
    int h;
    int f;

    public Node(int[][] state) {
        this.state = state;
        this.parent = null;
        this.g = 0;
        this.h = 0;
        this.f = 0;
    }
}

public class Astar {
    private int[][] startState;
    private int[][] goalState;

    public Astar(int[][] startState, int[][] goalState) {
        this.startState = startState;
        this.goalState = goalState;
    }

    private int calculateManhattanDistance(Node node) {
        int distance = 0;
        int goalRow = 0;
        int goalCol = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = node.state[i][j];

                if (value != 0) {
                    
                 for (int k = 0; k < 3; k++)
                    for (int l = 0; l < 3; l++)
		      if (goalState[k][l] == value)
                      {
                       goalRow = k;
                       goalCol = l;
                      }
                    distance += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                 //   System.out.println(distance+" when i="+i+" j="+j+" grow="+goalRow+" gcol="+goalCol);
                }
            }
        }
   //     System.out.println(" fasl ");
//        System.out.print(distance+"-");
        return distance;
    }

    private int calculateEuclideanDistance(Node node) {
        int distance = 0;
        int goalRow = 0;
        int goalCol = 0;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = node.state[i][j];

                if (value != 0) {
                    
                    for (int k = 0; k <3; k++)
                    for (int l = 0; l <3; l++)
		      if (goalState[k][l] == value)
                      {
                       goalRow = k;
                       goalCol = l;
                      }
                    distance += Math.sqrt(Math.pow(i - goalRow, 2) + Math.pow(j - goalCol, 2));
                //    System.out.println(distance+" when i="+i+" j="+j+" grow="+goalRow+" gcol="+goalCol);
                }
            }
        }
       // System.out.println(" fasl ");
        //System.out.print(distance+"-");
        return distance;
    }

    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        Node current = node;

        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }

        return path;
    }
List<Node> findPath(String heuristic) {
    Node startNode = new Node(startState);
    Node goalNode = new Node(goalState);

    PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
    List<Node> closedList = new ArrayList<>();

    startNode.h = calculateHeuristic(startNode, heuristic);
    startNode.f = startNode.h;

    openList.add(startNode);

    while (!openList.isEmpty()) {
        Node currentNode = openList.poll();
        closedList.add(currentNode);

        if (Arrays.deepEquals(currentNode.state, goalNode.state)) {
            return reconstructPath(currentNode);
        }

        List<Node> neighbors = generateNeighbors(currentNode);

        for (Node neighbor : neighbors) {
            if (closedList.stream().anyMatch(n -> Arrays.deepEquals(n.state, neighbor.state))) {
                continue;
            }

            int tentativeGScore = currentNode.g + 1;

            if (openList.stream().noneMatch(n -> Arrays.deepEquals(n.state, neighbor.state)) || tentativeGScore < neighbor.g) {
                neighbor.parent = currentNode;
                neighbor.g = tentativeGScore;
                neighbor.h = calculateHeuristic(neighbor, heuristic);
                neighbor.f = neighbor.g + neighbor.h;

                if (openList.stream().noneMatch(n -> Arrays.deepEquals(n.state, neighbor.state))) {
                    openList.add(neighbor);
                }
            }
        }
    }

    return null;
}
int calculateHeuristic(Node node, String heuristic) {
    if (heuristic.equals("Manhattan")) {
        return calculateManhattanDistance(node);
    } else if (heuristic.equals("Euclidean")) {
        return calculateEuclideanDistance(node);
    }
    return 0;
}
    

    private List<Node> generateNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[] dx = { -1, 1, 0, 0 };
        int[] dy = { 0, 0, -1, 1 };
        String[] moves = { "U", "D", "L", "R" };

        int emptyRow = 0;
        int emptyCol = 0;

        // Find the position of the empty tile (0)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (node.state[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int newRow = emptyRow + dx[i];
            int newCol = emptyCol + dy[i];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int[][] newState = cloneState(node.state);
                swap(newState, emptyRow, emptyCol, newRow, newCol);

                Node newNode = new Node(newState);
                neighbors.add(newNode);
            }
        }

        return neighbors;
    }

    private int[][] cloneState(int[][] state) {
        int[][] newState = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newState[i][j] = state[i][j];
            }
        }

        return newState;
    }

    private void swap(int[][] state, int row1, int col1, int row2, int col2) {
        int temp = state[row1][col1];
        state[row1][col1] = state[row2][col2];
        state[row2][col2] = temp;
    }

    private String printPath(List<Node> path) {
         String Path="";
        if (path == null) {
            System.out.println("No path found.");
        } else {
            for (int i = 0; i < path.size() - 1; i++) {
                Node current = path.get(i);
                Node next = path.get(i + 1);

                int emptyRow = 0;
                int emptyCol = 0;
                int nextEmptyRow = 0;
                int nextEmptyCol = 0;

                // Find the position of the empty tile (0) in the current and next state
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        if (current.state[j][k] == 0) {
                            emptyRow = j;
                            emptyCol = k;
                        }

                        if (next.state[j][k] == 0) {
                            nextEmptyRow = j;
                            nextEmptyCol = k;
                        }
                    }
                }

                // Determine the movement direction based on the difference in empty tile positions
                int rowDiff = nextEmptyRow - emptyRow;
                int colDiff = nextEmptyCol - emptyCol;

                String direction = "";

                if (rowDiff == -1) {
                    direction = "U";
                } else if (rowDiff == 1) {
                    direction = "D";
                } else if (colDiff == -1) {
                    direction = "L";
                } else if (colDiff == 1) {
                    direction = "R";
                }

 Path +=direction;
                System.out.print(direction);
            }

            System.out.println();
        }return Path;
    }

    public String solvePuzzle(String heuristic) {
        List<Node> path;
String p="";
        if (heuristic.equals("Manhattan")) {
            path = findPath("Manhattan");
            System.out.print("Manhattan Path: ");
            p=printPath(path);
        } else if (heuristic.equals("Euclidean")) {
            path = findPath("Euclidean");
            System.out.print("Euclidean Path: ");
            p=printPath(path);
        } else {
            System.out.println("Invalid heuristic.");
        }
        return p;
    }
    private void printPath(List<Node> path, String heuristic) {
        System.out.println("Path using " + heuristic + " heuristic:");
        if (path != null) {
            for (Node node : path) {
                System.out.println(Arrays.deepToString(node.state));
            }
        } else {
            System.out.println("No path found.");
        }
    }

    public static void main(String[] args) {
//         Scanner input =new Scanner(System.in);
//        int[][] startState = new int[3][3];
//          for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                startState[i][j] = input.nextInt();
//            }
//        }
//todo: change initial state
        int[][] startState = {
            {2, 3, 0},
            {4, 5, 1},
            {6, 7, 8}
        };

        int[][] goalState = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

        Astar astar = new Astar(startState, goalState);
        astar.solvePuzzle("Manhattan");
        astar.solvePuzzle("Euclidean");
        
         String heuristic = "Manhattan";
        List<Node> pathManhattan = astar.findPath(heuristic);
//        astar.printPath(pathManhattan, heuristic);
//
        heuristic = "Euclidean";
        List<Node> pathEuclidean = astar.findPath(heuristic);
//        astar.printPath(pathEuclidean, heuristic);
//
        System.out.println("\nNumber of nodes expanded (Manhattan): " + pathManhattan.size());
        System.out.println("Number of nodes expanded (Euclidean): " + pathEuclidean.size());
//        
//       
//        
        if (pathManhattan.size() < pathEuclidean.size()) {
            System.out.println("\nManhattan heuristic is more admissible.");
        } else if (pathManhattan.size() > pathEuclidean.size()) {
            System.out.println("\nEuclidean heuristic is more admissible.");
        } else {
            System.out.println("\nBoth heuristics are equally admissible.");
        }
    }
}