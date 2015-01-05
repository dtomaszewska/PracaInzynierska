import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*; // potrzebne do reakcji na przyciski, menu.
import java.io.*;

public class Window extends JFrame implements ActionListener{

	private static final long serialVersionUID = 123456L;
	private JMenuBar menu_bar;
	private JMenuItem menu_item_reset;
	private JButton[][][] field; 
	private ImageIcon[] icons;
	private JLabel message;
	Settings set;
	State board;
	Strategy ai;
	private String workspace;
	int width;
	int height;
	int[][] coordinations;
	int button_size;
	byte field_count;
	float difficulty;
	byte player;
	byte computer;
	byte[] next;
	boolean player_turn;
	byte x, y, z;

		public Window(String title, Settings my_settings) 
		{
			super(title);
			next = new byte[3];
			set = my_settings;
			button_size = 50;
			if(set.options[0]==1)
				field_count = 4;
			else
				field_count = 6;
			
			player = set.options[1];
			if (player == 1)
				computer = 2;
			else
				computer = 1;
			
			player_turn = false;
			difficulty = (float)set.options[2]/(float)set.difficultyOptions.length;
			//workspace = new String("D:/Workspace/MasterCube/src/");
			workspace = new String("");
			
			icons = new ImageIcon[3];			
			icons[0] = new ImageIcon(workspace+"images/empty.png");
			icons[1] = new ImageIcon(workspace+"images/circle.png");
			icons[2] = new ImageIcon(workspace+"images/cross.png");
			
			board = new State(field_count);
			ai = new Strategy(board, difficulty, (byte)set.difficultyOptions.length);
			message = new JLabel("", JLabel.CENTER);
			message.setFont(new Font("Verdana", Font.BOLD, 23));
			this.add(message);
		}
		
		public void run() 
		{
			setLayout(null);	// Wylacz automatyczne rozkaladanie kontrolek
			setWindowSize();			
			buildMenu();
			setVisible(true);
			createButtons();
			startGame();
		}
		
		private void setWindowSize()
		{
			if(field_count == 4)
			{
				setSize(500,600);
				width = 500;
				height = 550;
			}
			
			else
			{			
				setSize(1050,750);
				width = 1100;
				height = 720;
			}
			message.setSize(width,50);
		}
		
		private void buildMenu() 
		{
			menu_bar = new JMenuBar();		
			menu_item_reset = new JMenuItem("RESET GAME");
			menu_item_reset.addActionListener(this);
			menu_bar.add(menu_item_reset);
			this.setJMenuBar(menu_bar);
		}
			
		public void createButtons() 
		{
			
			int w_space = (width - (button_size*field_count*field_count/2)) / (field_count);
			int h_space = (height - (button_size*field_count*2)) / 3;
			coordinations = new int[field_count][2];
			
			for (int i = 0; i<field_count/2; i++)
			{	
				coordinations[i][0] = w_space*(i+1) + (button_size*field_count*i);
				coordinations[i][1] = h_space;
			}
			
			for (int i = 0; i<field_count/2; i++)
			{	
				coordinations[i+field_count/2][0] = w_space*(i+1) + (button_size*field_count*i);
				coordinations[i+field_count/2][1] = h_space*2 + (button_size*field_count);
			}
			
			field = new JButton[field_count][field_count][field_count];
			for(int x=0; x<field_count; x++){
		    	for(int y=0; y<field_count; y++){
		    		for(int z=0; z<field_count; z++)
		    		{
			    		field[x][y][z] = new JButton(icons[0]);
						field[x][y][z].addActionListener(this);
						field[x][y][z].setBounds(coordinations[field_count-1][0]+button_size*x,coordinations[field_count-1][1]+button_size*y,button_size,button_size);
						//field[x][y][z].setBounds(coordinations[z][0]+button_size*x,coordinations[z][1]+button_size*y,button_size,button_size);
						this.add(field[x][y][z]);	
		    		}
		    	}
			}
			animation();
		}
		
		private void clearButtons()
		{
			for(int x=0; x<field_count; x++){
		    	for(int y=0; y<field_count; y++){
		    		for(int z=0; z<field_count; z++)
		    		{
			    		field[x][y][z].setIcon(icons[0]);
		    		}
		    	}
			}
		}

		private void makeMove(byte who)
		{
			ai.nextMove(next);

			field[next[0]][next[1]][next[2]].setIcon(icons[who]);
			board.ChangeState(who, next);
			if(ai.actual_node.win)
			{
				if(ai.actual_node.player == player)
				{
					message.setText("YOU WIN!");
					message.setVisible(true);
					end();
				}
				else
				{
					message.setText("YOU LOSE");
					message.setVisible(true);
					end();
				}
			}
			else
			{
				if(ai.actual_node.deep < field_count*field_count*field_count)
				{
					if(who == player)
					{
						ai.tree.newRoot(ai.actual_node);
						ai.tree.buildTwoLevels();
						makeComputerMove();
					}
					else
					{
						message.setText("YOUR MOVE");
						message.setVisible(true);
						player_turn = true;
					}
						
				}
				else
				{
					message.setText("DEAD-HEAT");
					message.setVisible(true);
				}
			}
		}
		
		private void makeComputerMove()
		{
			do{
				next = ai.computerRand();
			}while(board.ShowFieldState(next) != 0);
			makeMove(computer);
		}
		
		private void firstMove(byte who)
		{
			ai.firstMove(next);
			field[next[0]][next[1]][next[2]].setIcon(icons[who]);
			board.ChangeState(who, next);
			if(ai.actual_node.deep<2)
			{
				if(who == player)
				{
					do{
						next = ai.computerRand();
					}while(board.ShowFieldState(next) != 0);
					firstMove(computer);
				}
				else
				{
					message.setText("YOUR MOVE");
					message.setVisible(true);
					player_turn = true;
				}
			}
			else
			{
				ai.tree.buildTree();
				message.setText("YOUR MOVE");
				message.setVisible(true);
				player_turn = true;
			}
		}
		
		private void startGame()
		{
			if(player != 1)
			{
				makeComputerMove();
				firstMove(computer);
			}
			else
			{
				message.setText("YOUR MOVE");
				message.setVisible(true);
				player_turn = true;
			}
		}
		
		private void end()
		{
			byte sum;
			byte x, y, z;
			
			//pion
			sum = 0;
			for(y=0; y<field_count; y++)
			{
				if(ai.board.ShowFieldState(ai.actual_node.move[0],y,ai.actual_node.move[2]) == ai.actual_node.player)
					sum++;
			}
			if(sum==field_count)
				for(y=0; y<field_count; y++)
				{
					field[ai.actual_node.move[0]][y][ai.actual_node.move[2]].setBackground(Color.RED);
				}
			
			//poziom
			sum = 0;
			for(x=0; x<field_count; x++)
			{
				if(ai.board.ShowFieldState(x,ai.actual_node.move[1],ai.actual_node.move[2]) == ai.actual_node.player)
					sum++;
			}
			if(sum==field_count)
				for(x=0; x<field_count; x++)
				{
					field[x][ai.actual_node.move[1]][ai.actual_node.move[2]].setBackground(Color.RED);
				}
			//w glab
			sum = 0;
			for(z=0; z<field_count; z++)
			{
				if(ai.board.ShowFieldState(ai.actual_node.move[0],ai.actual_node.move[1],z) == ai.actual_node.player)
					sum++;
			}
			if(sum==field_count)
				for(z=0; z<field_count; z++)
				{
					field[ai.actual_node.move[0]][ai.actual_node.move[1]][z].setBackground(Color.RED);
				}
			
			//przekatne
			//xy
			if(ai.actual_node.move[0]==ai.actual_node.move[1])
			{
				sum = 0;
				for(x=0;x<field_count;x++)
				{
					if(ai.board.ShowFieldState(x,x,ai.actual_node.move[2]) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(x=0;x<field_count;x++)
					{
						field[x][x][ai.actual_node.move[2]].setBackground(Color.RED);
					}
			}
			
			if(ai.actual_node.move[0]+ai.actual_node.move[1] == field_count-1)
			{
				sum = 0;
				for(x=0;x<field_count;x++)
				{
					if(ai.board.ShowFieldState(x,(byte)(field_count-x-1),ai.actual_node.move[2]) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(x=0;x<field_count;x++)
					{
						field[x][(byte)(field_count-x-1)][ai.actual_node.move[2]].setBackground(Color.RED);
					}
			}
			
			//yz
			if(ai.actual_node.move[1]==ai.actual_node.move[2])
			{
				sum = 0;
				for(y=0;y<field_count;y++)
				{
					if(ai.board.ShowFieldState(ai.actual_node.move[0],y,y) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(y=0;y<field_count;y++)
					{
						field[ai.actual_node.move[0]][y][y].setBackground(Color.RED);
					}
			}
			
			if(ai.actual_node.move[1]+ai.actual_node.move[2] == field_count-1)
			{
				sum = 0;
				for(y=0;y<field_count;y++)
				{
					if(ai.board.ShowFieldState(ai.actual_node.move[0],y,(byte)(field_count-y-1)) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(y=0;y<field_count;y++)
					{
						field[ai.actual_node.move[0]][y][(byte)(field_count-y-1)].setBackground(Color.RED);
					}
			}
			
			//xz
			if(ai.actual_node.move[0]==ai.actual_node.move[2])
			{
				sum = 0;
				for(z=0;z<field_count;z++)
				{
					if(ai.board.ShowFieldState(z, ai.actual_node.move[1],z) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(z=0;z<field_count;z++)
					{
						field[z][ai.actual_node.move[1]][z].setBackground(Color.RED);
					}
			}
			
			if(ai.actual_node.move[0]+ai.actual_node.move[2] == field_count-1)
			{
				sum = 0;
				for(z=0;z<field_count;z++)
				{
					if(ai.board.ShowFieldState(z,ai.actual_node.move[0],(byte)(field_count-z-1)) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(z=0;z<field_count;z++)
					{
						field[z][ai.actual_node.move[0]][(byte)(field_count-z-1)].setBackground(Color.RED);
					}
			}
			
			//przekatne calej bryly
			if(ai.actual_node.move[0]==ai.actual_node.move[2] && ai.actual_node.move[1]==ai.actual_node.move[2])
			{
				sum = 0;
				for(x=0;x<field_count;x++)
				{
					if(ai.board.ShowFieldState(x,x,x) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(x=0;x<field_count;x++)
					{
						field[x][x][x].setBackground(Color.RED);
					}
			}
			
			if(ai.actual_node.move[1]==ai.actual_node.move[2] && ai.actual_node.move[0]+ai.actual_node.move[2] == field_count-1)
			{
				sum = 0;
				for(y=0;y<field_count;y++)
				{
					if(ai.board.ShowFieldState(y,y,(byte)(field_count-y-1)) == ai.actual_node.player)
						sum++;
				}
				if(sum==field_count)
					for(y=0;y<field_count;y++)
					{
						field[y][y][(byte)(field_count-y-1)].setBackground(Color.RED);
					}
			}
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			Object event = e.getSource();
			if(event == menu_item_reset) {
				ai.board.Clear();
				clearButtons();
				ai.newTree();
				ai.actual_node = ai.tree.root;
				startGame();
			}

			for(x=0; x<field_count; x++){
		    	for(y=0; y<field_count; y++){
		    		for(z=0; z<field_count; z++)
		    		{
			    		if(event == field[x][y][z] && player_turn == true && ai.board.ShowFieldState(x,y,z) == 0)
			    		{
			    			player_turn = false;
			    			next = new byte[3];
			    			next[0] = x;
			    			next[1] = y;
			    			next[2] = z;
			    			if(ai.actual_node.deep<2)
			    				firstMove(player);
			    			
			    			else
			    				makeMove(player);
			    			break;
			    		}
		    		}
		    	}
			}
		}
		private void animation()
		{
			for(byte i = 0; i<field_count; i++)
			{
				//ustawienie pierwszej tarczy
				while(field[0][0][i].getBounds().x > coordinations[i][0])
				{
					for(int x=0; x<field_count; x++){
				    	for(int y=0; y<field_count; y++){
							field[x][y][i].setLocation(field[x][y][i].getBounds().x-1, field[x][y][i].getBounds().y);
							try {
								Thread.sleep(4/field_count);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				    	}
					}
				}
				while(field[0][0][i].getBounds().y > coordinations[i][1])
				{
					for(int x=0; x<field_count; x++){
				    	for(int y=0; y<field_count; y++){
							field[x][y][i].setLocation(field[x][y][i].getBounds().x, field[x][y][i].getBounds().y-1);
							try {
								Thread.sleep(4/field_count);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				    	}
					}
				}
			}
		}
}

class MenuBar {
	  private MenuBar() {}

	  public static int getNumberOfElement(String field, String[] container){
		  int number = 0;
		    for (int i = 0, n = container.length; i < n; i++) {
		      if (field == container[i]) {
		          number = i;
		      }
		    }
		    return number;
	  }

	   public static JMenu createJMenu(String elements[], String title, ActionListener actionListener, ItemListener itemListener) {
	    JMenu menu = new JMenu(title);
		
	    JMenuItem menu_item;
	    for (int i = 0, n = elements.length; i < n; i++) {
	      menu_item = new JMenuItem(elements[i]);
	      menu.add(menu_item);
	      if (actionListener != null) {
	        menu_item.addActionListener(actionListener);
	      }
	      if (itemListener != null) {
	        menu_item.addItemListener(itemListener);
	      }
	    }
	    return menu;
	  }
	}