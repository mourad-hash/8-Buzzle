package pkg8.puzzle;

import java.util.*;

class pair{
    public Node1 state;
    public String state_path;

    pair(Node1 state , String state_path){
        this.state = state;
        this.state_path = state_path;
    }
}
class Node1{ // data structure for the node
    private int[][] node = new int[3][3];
    private int row , col = 3;

    protected Node1 n1 = null , n2 = null , n3 = null , n4 = null;

    Node1(int node[][]){
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3 ; j++)
                this.node[i][j] = node[i][j];
        }
    }

    public void print_node(){
        int j = 0;
        for(int i = 0 ; i < 3 ; i++){
            System.out.println("+---+---+---+");
            do{
                System.out.println("| " + node[i][j] + " | " + node[i][j+1] + " | " + node[i][j+2] + " |");
            }while(false);
            if(i == 2)System.out.println("+---+---+---+");
        }
    }

    public int at(int x , int y){
        assert(x < 3 && y < 3);
        return node[x][y];
    }

    public Vector<Integer> getIndexZero(){
        Vector <Integer> pair = new Vector<>(2);
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3 ; j++)
                if(node[i][j] == 0){
                    pair.add(i);
                    pair.add(j);
                    return pair;
                };
        }
        pair.add(-1);
        pair.add(-1);
        return pair;
    }

    boolean canMoveToRight(){
        Vector <Integer> zero = getIndexZero();
        return zero.get(1) < 2;
    }

    void set(int x , int y , int value){
        node[x][y] = value;
    }

    boolean canMoveToLeft(){
        Vector <Integer> zero = getIndexZero();
        return zero.get(1) > 0 && zero.get(1) <= 2;
    }

    boolean canMoveToTop(){
        Vector <Integer> zero = getIndexZero();
        return zero.get(0) > 0 && zero.get(1) <= 2;
    }

    boolean canMoveToBottom(){
        Vector <Integer> zero = getIndexZero();
        return zero.get(0) < 2 && zero.get(1) >= 0;
    }

    void moveToRight(){
        if(canMoveToRight()){
            Vector <Integer> zero = getIndexZero();
            //index for value zero
            int zeroX = zero.get(0);
            int zeroY = zero.get(1);
            //index for value Right
            int RightRowIndex = zeroX;
            int RightColIndex = zeroY + 1;
            int value = at(RightRowIndex , RightColIndex);
            //swap
            set(zeroX , zeroY , value);
            set(RightRowIndex , RightColIndex , 0);
        }
    }

    void moveToLeft(){
        if(canMoveToLeft()){
            Vector <Integer> zero = getIndexZero();
            //index for value zero
            int zeroX = zero.get(0);
            int zeroY = zero.get(1);
            //index for value Left
            int LeftRowIndex = zeroX;
            int LeftColIndex = zeroY - 1;
            int value = at(LeftRowIndex , LeftColIndex);
            //swap
            set(zeroX , zeroY , value);
            set(LeftRowIndex , LeftColIndex , 0);
        }
    }

    void moveToBottom(){
        if(canMoveToBottom()){
            Vector <Integer> zero = getIndexZero();
            //index for value zero
            int zeroX = zero.get(0);
            int zeroY = zero.get(1);
            //index for value Bottom
            int BottomRowIndex = zeroX + 1;
            int BottomColIndex = zeroY;
            int value = at(BottomRowIndex , BottomColIndex);
            //swap
            set(zeroX , zeroY , value);
            set(BottomRowIndex , BottomColIndex , 0);
        }
    }

    void moveToTop(){
        if(canMoveToTop()){
            Vector <Integer> zero = getIndexZero();
            //index for value zero
            int zeroX = zero.get(0);
            int zeroY = zero.get(1);
            //index for value Bottom
            int TopRowIndex = zeroX - 1;
            int TopColIndex = zeroY;
            int value = at(TopRowIndex , TopColIndex);
            //swap
            set(zeroX , zeroY , value);
            set(TopRowIndex , TopColIndex , 0);
        }
    }

    Node1 GetMoveToRight(Node1 node1){
        Node1 node2 = new Node1(node);
        node2.moveToRight();
        return node2;
    }

    Node1 GetMoveToLeft(Node1 node1){
        Node1 node2 = new Node1(node);
        node2.moveToLeft();
        return node2;
    }

    Node1 GetMoveToTop(Node1 node1){
        Node1 node2 = new Node1(node);
        node2.moveToTop();
        return node2;
    }

    Node1 GetMoveToBottom(Node1 node1){
        Node1 node2 = new Node1(node);
        node2.moveToBottom();
        return node2;
    }

    boolean isEqual(Node1 node2){
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3 ; j++){
                if(this.node[i][j] != node2.at(i,j)) return false;
            }
        }
        return true;
    }
}

class Tree{
    private Node1 root;
    public static int count = 0; // counts the expanded nodes

    Tree(Node1 root){this.root = root;}

    private boolean checkIfExist(Vector <pair> vec , Node1 node){
        for(int i = 0 ; i < vec.size() ; i++){
            if(vec.get(i).state.isEqual(node)) return true;
        }
        return false;
    }

    private boolean checkIfExistInFrontier(Stack<Node1> stack , Node1 node){
        if(stack.contains(node)) return true;
        return false;
    }
    //priority (Top , Bottom , Left , Right)
    public String DFS(Node1 initialState , Node1 goal){
        Stack<Node1> frontier = new Stack<>();
        Vector <pair> path = new Stack<>(); // Take The True Path
     //   Vector <Node> explored = new Vector<>();
        Stack <String> Alphabet = new Stack<>();
        frontier.push(initialState);
        //explored.add(initialState);
        Alphabet.push(" ");
        while(!frontier.isEmpty()){
            Node1 state = frontier.peek();
            String state_path = Alphabet.peek();
            Alphabet.pop();
            frontier.pop();

            System.out.println("==================================");
            System.out.println("Current State");
            state.print_node();
            pair p = new pair(state , state_path);
            path.add(p);
            System.out.println("==================================");

            if(state.isEqual(goal)){
                System.out.println("Goal: ");
                goal.print_node();
                break;
            }

            if(state.canMoveToRight()){
                System.out.println("Right: ");
                Node1 next_state = state.GetMoveToRight(state);
                //check if not exist in the Path or the Frontier
                if(!checkIfExist(path , next_state)  && !checkIfExistInFrontier(frontier , next_state)){
                    state.n4 = next_state;
                    frontier.push(state.n4);
                    count++;
                   // explored.add(next_state);
                    Alphabet.push("R");
                    next_state.print_node();
                }else{
                    System.out.println("Already Exist");
                }
                System.out.println("======================");
            }else{
                System.out.println("Can't Move To The Right");
                System.out.println("======================");
            }

            if(state.canMoveToLeft()){
                System.out.println("Left: ");
                Node1 next_state = state.GetMoveToLeft(state);
                //check if not exist in the Path or the Frontier
                if(!checkIfExist(path , next_state)  && !checkIfExistInFrontier(frontier , next_state)){
                    state.n3 = next_state;
                    frontier.push(state.n3);
                    count++;
                    //explored.add(next_state);
                    Alphabet.push("L");
                    next_state.print_node();
                }else{
                    System.out.println("Already Exist");
                }
                System.out.println("======================");
            }else{
                System.out.println("Can't Move To The Left");
                System.out.println("======================");
            }

            if(state.canMoveToBottom()){
                System.out.println("Bottom: ");
                Node1 next_state = state.GetMoveToBottom(state);
                //check if not exist in the Path or the Frontier
                if(!checkIfExist(path , next_state)  && !checkIfExistInFrontier(frontier , next_state)){
                    state.n2 = next_state;
                    frontier.push(state.n2);
                    count++;
                   // explored.add(next_state);
                    Alphabet.push("D");
                    next_state.print_node();
                }else{
                    System.out.println("Already Exist");
                }
                System.out.println("======================");
            }else{
                System.out.println("Can't Move To The Bottom");
                System.out.println("======================");
            }

            if(state.canMoveToTop()){
                System.out.println("Top: ");
                Node1 next_state = state.GetMoveToTop(state);
                if(next_state.isEqual(goal)){
                    System.out.println("Founded: ");
                    pair pr = new pair(next_state , "U");
                    path.add(pr);
                    next_state.print_node();
                    break;
                }
                //check if not exist in the Path or the Frontier
                if(!checkIfExist(path , next_state)  && !checkIfExistInFrontier(frontier , next_state)){
                    state.n1 = next_state;
                    frontier.push(state.n1);
                   // explored.add(next_state);
                    Alphabet.push("U");
                    next_state.print_node();
                }else{
                    System.out.println("Already Exist");
                }
                System.out.println("======================");
            }else{
                System.out.println("Can't Move To The Top");
                System.out.println("======================");
            }

        }
        String Path="";
        // print the real path
        System.out.println("===========================");
        System.out.println("Path");
        for(int i = 1 ; i < path.size() ; i++){
        System.out.print(path.get(i).state_path);
            Path +=path.get(i).state_path;
        }
        System.out.println();
        System.out.println("the number of expanded nodes: " + count);
        
         return Path;
    }
}


public class DFS{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int initialState[][] = new int[3][3];
        System.out.println("Print The Initial State: ");
        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3 ; j++){
                initialState[i][j] = input.nextInt();
            }
        }
       int[][]goal={
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };

       

        Node1 initialStateNode1  = new Node1(initialState);
        Node1 goalNode1 = new Node1(goal);
        Tree t = new Tree(initialStateNode1);
        t.DFS(initialStateNode1 , goalNode1);
    }
}