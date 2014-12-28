import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class MovesTree{
	Node root;
	Win is_win;
	int move[];
	
	public MovesTree(int fields)
	{
		is_win = new Win(fields);
		int x, y, z;
		move = new int[3];
		root = new Node(fields);
		for(x=0; x<fields; x++)
		{
			move[0]=x;
			for(y=0; y<fields; y++)
			{
				move[1]=y;
				for(z=0; z<fields; z++)
				{
					move[2]=z;
					root.addChild(new Node(root, move, is_win));
				}
			}
		}
	}
}


class Node implements Comparable<Node>{
	LinkedList<Node> children;
	Node parent;
	boolean win;
	int win_chance;
	int move[];
	int player;
	int deep;
	int size;
	
	public Node(int fields)
	{
		children = new LinkedList<Node>();
		win = false;
		win_chance = 0;
		player = 0;
		move = new int[3];
		deep = 0;
		size = fields;
	}

	public Node(Node par, int[] new_move, Win win){
		parent = par;
		move = new int[3];
		System.arraycopy(new_move, 0, move, 0, new_move.length);
		deep = parent.deep+1;
		size = parent.size;
		children = new LinkedList<Node>();
		if(parent.player == 1)
			player = 2;
		else
			player = 1;
		
		if(win.isWin(this))
		{
			this.win = true;
			addWin(this);
		}
		else
		{
			this.win = false;
			addAllChild(this, win);
			Collections.sort(children);
		}
	}
	
	public void addAllChild(Node parent, Win win){
		int x, y, z;
		int[] move = new int[3];
		for (x = 0; x<parent.size; x++){
			move[0] = x;
			for (y = 0; y<parent.size; y++){
				move[1] = y;
				for (z = 0; z<parent.size; z++){
					move[2] = z;
					if(isEmpty(parent, move))
					{
						parent.addChild(new Node(parent, move, win));
					}
				}
			}
		}
	}
	
	public void addChild(Node new_child){
		children.add(new_child);
	}
	
	public int childrenCount(){
		return children.size();
	}
	
	private void addWin(Node node)
	{
		Node parent;
		while(node.parent.deep > 1)
		{
			parent = node.parent;
			parent.parent.win_chance++;
			node = parent.parent;
		}	
	}
	private boolean isEmpty(Node node, int[] move)
	{
		boolean is = true;
		while(node.deep > 0)
		{
			if(Arrays.equals(node.move, move))
			{
				is = false;
				break;
			}
			node = node.parent;
		}
		return is;
	}
	public int compareTo(Node node) {
		int chance = node.win_chance;
		if(this == null && node == null)
			return 0;
		else if (this.win_chance > chance) {
			return 1;
		} else if (this.win_chance == chance) {
			if(this.win)
				return 1;
			else
				return 0;
		} else {
			return -1;
		}
	}
}

class Win{
	private int[][][] board;
	private int size;
	public Win(int fields)
	{
		board = new int[fields][fields][fields];
		size = fields;
	}
	
	private void clear(){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				for(int k=0;k<size;k++){
					board[i][j][k]=0;
				}
			}
		}
	}
	
	public boolean isWin(Node in_node)
	{
		int sum;
		int x, y, z;
		Node node = in_node;
		clear();
		board[node.move[0]][node.move[1]][node.move[2]] = 1;
		Node parent = node.parent;
		while(parent.deep > 2)
		{
			board[parent.move[0]][parent.move[1]][parent.move[2]] = 1;
			node = parent.parent;
			parent = node.parent;
		}
		
		//pion
		sum = 0;
		for(y=0; y<size; y++)
		{
			if(board[in_node.move[0]][y][in_node.move[2]] == 1)
				sum++;
			else
				break;
		}
		if(sum==size)
			return true;
		
		//poziom
		sum = 0;
		for(x=0; x<size; x++)
		{
			if(board[x][in_node.move[1]][in_node.move[2]] == 1)
				sum++;
			else
				break;
		}
		if(sum==size)
			return true;
		
		//w glab
		sum = 0;
		for(z=0; z<size; z++)
		{
			if(board[in_node.move[0]][in_node.move[1]][z] == 1)
				sum++;
			else
				break;
		}
		if(sum==size)
			return true;
		
		//przekatne
		//xy
		if(in_node.move[0]==in_node.move[1])
		{
			sum = 0;
			for(x=0;x<size;x++)
			{
				if(board[x][x][in_node.move[2]] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		if(in_node.move[0]+in_node.move[1] == size-1)
		{
			sum = 0;
			for(x=0;x<size;x++)
			{
				if(board[x][size-x-1][in_node.move[2]] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		//yz
		if(in_node.move[1]==in_node.move[2])
		{
			sum = 0;
			for(y=0;y<size;y++)
			{
				if(board[in_node.move[0]][y][y] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		if(in_node.move[1]+in_node.move[2] == size-1)
		{
			sum = 0;
			for(y=0;y<size;y++)
			{
				if(board[in_node.move[0]][y][size-y-1] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		//xz
		if(in_node.move[0]==in_node.move[2])
		{
			sum = 0;
			for(z=0;z<size;z++)
			{
				if(board[z][in_node.move[1]][z] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		if(in_node.move[0]+in_node.move[2] == size-1)
		{
			sum = 0;
			for(z=0;z<size;z++)
			{
				if(board[z][in_node.move[0]][size-z-1] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		//przekatne calej bryly
		if(in_node.move[0]==in_node.move[2] && in_node.move[1]==in_node.move[2])
		{
			sum = 0;
			for(x=0;x<size;x++)
			{
				if(board[x][x][x] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		if(in_node.move[1]==in_node.move[2] && in_node.move[0]+in_node.move[2] == size-1)
		{
			sum = 0;
			for(y=0;y<size;y++)
			{
				if(board[y][y][size-y-1] == 1)
					sum++;
				else
					break;
			}
			if(sum==size)
				return true;
		}
		
		return false;
	}
}
