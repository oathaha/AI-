import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class NPuzzle {

    public static class Pair<L, R> {
        public L l;
        public R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }
    }

    public static int parity(NPuzzleState state) {
        int inversions = 0;
        ArrayList<Integer> nums = new ArrayList<>();
        int[][] board = state.getBoard();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                nums.add(board[i][j]);
            }
        }
        Integer[] copy = new Integer[nums.size()];
        nums.toArray(copy);
        for (int i = 0; i < copy.length; i++){
            for (int j = i + 1; j < copy.length; j++){
                if (copy[i] != 0 && copy[j] != 0 && copy[i]> copy[j]){
                    inversions++;
                }
            }
        }
        return inversions % 2;
    }

    public static boolean isSolvable(NPuzzleState initState, NPuzzleState goalState) {
        return parity(initState) == parity(goalState);
    }

    public static enum Action{ 
        // Define possible actions for the N-Puzzle search problem
        // [start:1]
    	UP,DOWN,LEFT,RIGHT
        // [end:1]
    }

    public static NPuzzleState successor(NPuzzleState state, Action action) {
        int[][] board = state.getBoard();
        int s = board.length;
        int r = state.getR();
        int c = state.getC();
        // Implement a successor function
        // Your code should return a new state if the action is value
        // otherwise return null
        // [start:2]
        int[][] newBoard = state.copyBoard();

        if (action == Action.UP) {
          if (r-1 < 0) return null;

          newBoard[r][c] = newBoard[r-1][c];
          newBoard[r-1][c] = 0;
          return new NPuzzleState(newBoard);
        }

        if (action == Action.DOWN) {
          if (r+1 >= s) return null;

          newBoard[r][c] = newBoard[r+1][c];
          newBoard[r+1][c] = 0;
          return new NPuzzleState(newBoard);
        }

        if (action == Action.LEFT) {
          if (c-1 < 0) return null;

          newBoard[r][c] = newBoard[r][c-1];
          newBoard[r][c-1] = 0;
          return new NPuzzleState(newBoard);
        }

        if (action == Action.RIGHT) {
          if (c+1 >= s) return null;

          newBoard[r][c] = newBoard[r][c+1];
          newBoard[r][c+1] = 0;
          return new NPuzzleState(newBoard);
        }

        // [end:2]
        return null; // <- action is invalid
      }

    public static ArrayList<TreeNode<NPuzzleState>> expandSuccessors(TreeNode<NPuzzleState> node) {
        ArrayList<TreeNode<NPuzzleState>> successors = new ArrayList<>();
        NPuzzleState state = node.getState();
        // Define a successor function for the N-Puzzle search problem
        // Your code should add all child nodes to "successors"
        // Hint: use successor(.,.) function above
        // [start:3]
        NPuzzleState sucUp = successor(state,Action.UP);
        NPuzzleState sucDown = successor(state,Action.DOWN);
        NPuzzleState sucLeft = successor(state,Action.LEFT);
        NPuzzleState sucRight = successor(state,Action.RIGHT);
        
        if(sucUp != null) successors.add(new TreeNode<NPuzzleState>(sucUp,node,Action.UP,0));
        if(sucDown != null) successors.add(new TreeNode<NPuzzleState>(sucDown,node,Action.DOWN,0));
        if(sucLeft != null) successors.add(new TreeNode<NPuzzleState>(sucLeft,node,Action.LEFT,0));
        if(sucRight != null) successors.add(new TreeNode<NPuzzleState>(sucRight,node,Action.RIGHT,0));
        // [end:3]
        return successors;
    }

    public static boolean isGoal(NPuzzleState state, NPuzzleState goalState) {
        boolean desiredState = true;
        // Implement a goal test function
        // Your code should change desiredState to false if the state is not a goal 
        // [start:4]
        int board[][] = state.getBoard();
        int goal[][] = goalState.getBoard();
        int len = state.getBoard().length;
        int a,b;
        for(a=0; a<len; a++)
        {
        	for(b=0; b<len; b++)
        		if(board[a][b] != goal[a][b])
        		{
        			//desiredState = false;
        			//break;
        			return false;
        		}
        }
        // [end:4]
        return desiredState;
    }

    //static int countgoal = 1;
    public static Pair<ArrayList<Action>, Integer> solve(
            NPuzzleState initState, NPuzzleState goalState, 
            Queue<TreeNode<NPuzzleState>> frontier, boolean checkClosedSet, int limit) {
        HashSet<String> closed = new HashSet<>();
        ArrayList<Action> solution = new ArrayList<>();
        int numSteps = 0;
        
        // Implement Graph Search algorithm
        // Your algorithm should add action to 'solution' and
        // for every node you remove from the frontier add 1 to 'numSteps'
        // [start:5]
        //NPuzzleState currentstate = initState;
        TreeNode<NPuzzleState> curnode = new TreeNode<NPuzzleState>(initState);
        ArrayList<TreeNode<NPuzzleState>> expand; 
        //TreeNode<NPuzzleState> curnode = init;
        frontier.add(curnode);
        
        //while(!isGoal(curnode.getState(), goalState))
        while(!frontier.isEmpty())
        {
        	curnode = frontier.remove();
        	numSteps++;
        	if(curnode.getState() == null) continue;
        	//System.out.println("current state\n"+curnode.getState() + "\n");
        	if(isGoal(curnode.getState(), goalState) && numSteps <= limit)
        	{
        		//System.out.println("reach goal " + countgoal);
//        		countgoal++;
//        		if(countgoal > 1000)
//        			countgoal -= 1000;
        		while(curnode.getParent() != null)
        		{
        			solution.add(0,(Action)curnode.getAction());
        			curnode = curnode.getParent();
        		}
        		break;
        	}
        	else
        	{
        		expand = expandSuccessors(curnode);
        		if(checkClosedSet)
        		{
        			if(!closed.contains(curnode.getState().toString()))
        			{
        				closed.add(curnode.getState().toString());	
        				for(TreeNode<NPuzzleState> child: expand)
        					frontier.add(child);
        			}
//        			System.out.println(frontier.size());
        		}
        		else
        		{
        			for(TreeNode<NPuzzleState> child: expand)
    					frontier.add(child);
        		}		
        	}
        	//System.out.println("---------------------------");
        }
        //System.out.println(solution);
        // [end:5]
        return new Pair<ArrayList<Action>, Integer>(solution, numSteps);
    }

    public static class HeuristicComparator implements Comparator<TreeNode<NPuzzleState>> {

        private NPuzzleState goalState;
        private int heuristicNum;
        private boolean isAStar;
        private HashMap<Integer, Pair<Integer, Integer>> goalStateMap = null;

        public HeuristicComparator(NPuzzleState goalState, int heuristicNum, boolean isAStar) {
            this.goalState = goalState;
            this.heuristicNum = heuristicNum;
            this.isAStar = isAStar;
        }

        public int compare(TreeNode<NPuzzleState> n1, TreeNode<NPuzzleState> n2) {
            Double s1V = 0.0;
            Double s2V = 0.0;
            if (this.heuristicNum == 1) {
                s1V = h1(n1.getState());
                s2V = h1(n2.getState());
            } else {
                s1V = h2(n1.getState());
                s2V = h2(n2.getState());
            }
            if (this.isAStar) {  // AStar h(n) + g(n)
                s1V += n1.getPathCost();
                s2V += n2.getPathCost();
            }
            return s1V.compareTo(s2V);
        }

        public double h1(NPuzzleState s) {
            double h = 0.0;
            int[][] board = s.getBoard();
            int[][] goalBoard = goalState.getBoard();
            int a,b;
            // Implement misplaced tiles heuristic
            // Your code should update 'h'
            // [start:6]
            for(a=0; a<board.length; a++)
            {
            	for(b=0; b<board.length; b++)
            	{
            		if(board[a][b] != 0 && board[a][b] != goalBoard[a][b])
            			h++;
            	}
            }
            // [end:6]
            s.setEstimatedCostToGoal(h);
            return h;
        }

        public double h2(NPuzzleState s) {
            double h = 0.0;
            int[][] board = s.getBoard();
            int[][] goalBoard = goalState.getBoard();
            // Implement number-of-blocks-away heuristic
            // Your code should update 'value'
            // [start:7]
            int a,b,c,d;
            // a,b are position of board
            // c,d are position of goal board
            int len = board.length;
            
            for(a=0; a<len; a++)
            {
            	for(b=0; b<len; b++)
            	{
            		if(board[a][b] == 0 || board[a][b] == goalBoard[a][b])
            			continue;
            		for(c=0; c<len; c++)
            		{
            			for(d=0; d<len; d++)
            			{
            				if(board[a][b] == goalBoard[c][d])
            					h += Math.abs(a-c) + Math.abs(b-d);
            			}
            		}
            	}
            }
            // [end:7]
            s.setEstimatedCostToGoal(h);
            return h;
        }

    }

    public static void testRun(
            NPuzzleState initState, NPuzzleState goalState, 
            Queue<TreeNode<NPuzzleState>> frontier) {
        if (NPuzzle.isSolvable(initState, goalState)) {
            Pair<ArrayList<Action>, Integer> solution = solve(
                initState, goalState, frontier, true, 500000);
            System.out.println(initState);
            NPuzzleState curState = initState;
            for (Action action : solution.l) {
                curState = successor(curState, action);
                System.out.print("Action: ");
                System.out.println(action.toString());
                System.out.println(curState);
            }
            System.out.print("Number of steps in the solution: ");
            System.out.println(solution.l.size());
            System.out.print("Number of nodes expanded: ");
            System.out.println(solution.r);
        } else{
            System.out.println("Not solvable!");
        }
    }
    
    public static void experiment(NPuzzleState goalState, Queue<TreeNode<NPuzzleState>> frontier,
    		Queue<TreeNode<NPuzzleState>> frontier2) 
    {
    	int sumh1[] = new int[300];
        int sumh2[] = new int[300];
        int numh1[] = new int[300];
        int numh2[] = new int[300];
        StringBuilder sb = new StringBuilder();
        sb.append("length,step_h1,step_h2,num_h1,num_h2\n");
        for (int i = 0; i < 1000; i++){
            NPuzzleState initState = new NPuzzleState(8);  // random
            if (!NPuzzle.isSolvable(initState, goalState)) {
                i--;
                continue;
            }
            // Experiment to evaluate a search setting
            // [start:8]
            // length is size of ArrayList<Action> 
            Pair<ArrayList<Action>, Integer> solution = solve(initState, goalState, frontier, true, 500000);
            Pair<ArrayList<Action>, Integer> solution2 = solve(initState, goalState, frontier2, true, 500000);
            
            int len1 = solution.l.size();
            int len2 = solution2.l.size();
            
            numh1[len1]++;
            numh2[len2]++;
            sumh1[len1]+=solution.r;
            sumh2[len2]+=solution2.r;
            frontier.clear();
            frontier2.clear();
            // [end:8]
        }
        for(int i = 0; i < 300; i++) 
        {
            if (numh1[i] != 0 && numh2[i] != 0) 
            {
              int steph1 =  sumh1[i]/numh1[i];
              int steph2 =  sumh2[i]/numh2[i];
              sb.append(i + "," + steph1 + "," + steph2 + "," + numh1[i] + "," + numh2[i]+"\n");
            }
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        NPuzzleState.studentID = 179;

        int[][] goalBoard = {{0, 1, 2},{3, 4, 5},{6, 7, 8}};
       // System.out.println(goalBoard.length);
        NPuzzleState goalState = new NPuzzleState(goalBoard);

        /*
         *  Select an implementation of a frontier from the code below
         */
        
        // Stack
        // Queue<TreeNode<NPuzzleState>> frontier = Collections.asLifoQueue(
        //     new LinkedList<TreeNode<NPuzzleState>>());

        // Queue
//        Queue<TreeNode<NPuzzleState>> frontier = 
//            new LinkedList<TreeNode<NPuzzleState>>();
        
        // Priority Queue: A* with h1  
//         Queue<TreeNode<NPuzzleState>> frontier = new PriorityQueue<>(
//             new HeuristicComparator(goalState, 1, true));
        
        // Priority Queue: A* with h2
         Queue<TreeNode<NPuzzleState>> frontier2 = new PriorityQueue<>(
             new HeuristicComparator(goalState, 2, true));
        

        int[][] easy = {{1, 4, 2},{3, 0, 5},{6, 7, 8}};
//        NPuzzleState initState = new NPuzzleState(easy);

         int[][] hard = {{7, 2, 4}, {5, 0, 6}, {8, 3, 1}};
         NPuzzleState initState = new NPuzzleState(hard);

         testRun(initState, goalState, frontier2);
         //experiment(goalState, frontier, frontier2);

    }
}

