import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JTree;

public class MovesTree extends JTree{
	Node root;
	Win is_win;
	byte move[];
	
	public MovesTree(byte fields)
	{
		is_win = new Win(fields);
		move = new byte[3];
		root = new Node(fields);
	}
	
	public void addNode(byte[] new_move)
	{
		root.addChild(new Node(root, new_move));
		root = root.children.getLast();
	}
	public void buildTree()
	{
		build(root);
	}
	private void build(Node actual)
	{
		if(actual.deep<actual.size*actual.size*actual.size)
		{
			if(actual.win == false)
			{
				// 2 INTEND
				actual.addAllChild(actual, is_win);
			}
			for(Node child : actual.children)
			{
				build(child);
			}
		}
	}
}


class Node implements Comparable<Node>{
	LinkedList<Node> children;
	Node parent;
	boolean win;
	short win_chance;
	byte move[];
	byte player;
	byte deep;
	byte size;
	static int i;
	
	public Node(byte fields)
	{
		children = new LinkedList<Node>();
		win = false;
		win_chance = 0;
		player = 0;
		move = new byte[3];
		deep = 0;
		size = fields;
		i=0;
	}
	
	public Node(Node par, byte[] new_move)
	{
		parent = par;
		i = i+1;
		children = new LinkedList<Node>();
		win = false;
		win_chance = 0;
		if(parent.player == 1)
			player = 2;
		else
			player = 1;
		move = new byte[3];
		System.arraycopy(new_move, 0, move, 0, new_move.length);
		deep = (byte) (parent.deep+1);
		size = parent.size;
	}

	public Node(Node par, byte[] new_move, Win win){
		parent = par;
		i = parent.i+1;
		System.out.println(i);
		move = new byte[3];
		System.arraycopy(new_move, 0, move, 0, new_move.length);
		deep = (byte) (parent.deep+1);
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
		}
	}
	
	public void addAllChild(Node parent, Win win){
		byte x, y, z;
		byte[] move = new byte[3];
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
	public static boolean isEmpty(Node node, byte[] move)
	{
		boolean is = true;
		while(node.deep > 0)
		{
			if(arraysEquals(node.move, move))
			{
				is = false;
				break;
			}
			node = node.parent;
		}
		for(Node child : node.children)
		{
			if(arraysEquals(node.move, move))
			{
				is = false;
				break;
			}
		}
		return is;
	}
	public int compareTo(Node node) {
		int chance = node.win_chance;
		if(this == null || node == null)
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
	
	public static boolean arraysEquals(byte[] move1, byte[] move2)
	{
		if(move1[0]==move2[0] && move1[1]==move2[1] && move1[2]==move2[2] )
			return true;
		else
			return false;
	}
}

class Win{
	private byte[][][] board;
	private byte size;
	public Win(byte fields)
	{
		board = new byte[fields][fields][fields];
		size = fields;
	}
	
	private void clear(){
		for(byte i=0;i<size;i++){
			for(byte j=0;j<size;j++){
				for(byte k=0;k<size;k++){
					board[i][j][k]=0;
				}
			}
		}
	}
	
	public boolean isWin(Node in_node)
	{
		byte sum;
		byte x, y, z;
		Node node = in_node;
		clear();
		board[node.move[0]][node.move[1]][node.move[2]] = 1;
		Node parent = node.parent;
		while(parent.deep > 1)
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
