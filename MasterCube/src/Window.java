import javax.swing.*;
import java.awt.Font;
import java.awt.event.*; // potrzebne do reakcji na przyciski, menu.
import java.io.*;

public class Window extends JFrame implements ActionListener{

	private static final long serialVersionUID = 123456L;
	private JMenuBar menu_bar;
	private JFileChooser file_chooser;
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
	int field_count;
	float difficulty;
	int player;
	int computer;
	int[] next;
	boolean player_turn;

		public Window(String title, Settings my_settings) 
		{
			super(title);
			next = new int[3];
			set = my_settings;
			file_chooser = new JFileChooser();
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
			workspace = new String("D:/Workspace/MasterCube/src/");
			
			icons = new ImageIcon[3];			
			icons[0] = new ImageIcon(workspace+"images/empty.png");
			icons[1] = new ImageIcon(workspace+"images/circle.png");
			icons[2] = new ImageIcon(workspace+"images/cross.png");
			
			board = new State(field_count);
			ai = new Strategy(board, difficulty, set.difficultyOptions.length);
			message = new JLabel("", JLabel.CENTER);
			message.setSize(500,50);
			message.setFont(new Font("Verdana", Font.BOLD, 23));
			this.add(message);
		}
		
		public void run() 
		{
			setLayout(null);	// Wylacz automatyczne rozkaladanie kontrolek
			setWindowSize();			
			buildMenu();
			animation anim = new animation();
			//anim.run();
			createButtons();
			setVisible(true);
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
				setSize(1000,750);
				width = 1100;
				height = 720;
			}
		}
		
		private void buildMenu() 
		{
			menu_bar = new JMenuBar();
			menu_bar.add(MenuBar.createJMenu(set.gameOptions, "GAME", this, null));
			menu_bar.add(MenuBar.createJMenu(set.fieldsOptions, "FIELDS COUNT", this, null));
			menu_bar.add(MenuBar.createJMenu(set.difficultyOptions, "DIFFICULTY", this, null));
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
						field[x][y][z].setBounds(coordinations[z][0]+button_size*x,coordinations[z][1]+button_size*y,button_size,button_size);
						this.add(field[x][y][z]);	
		    		}
		    	}
			}
			
		}

		private void makeMove(int who, int[] were)
		{
			field[were[0]][were[1]][were[2]].setIcon(icons[who]);
			board.ChangeState(who, were);
			if(ai.actual_node.win)
			{
				if(ai.actual_node.player == player)
				{
					message.setText("YOU WIN!");
					message.setVisible(true);
				}
				else
				{
					message.setText("YOU LOSE");
					message.setVisible(true);
				}
			}
			else
			{
				if(ai.actual_node.deep < field_count*field_count*field_count)
				{
					if(who == player)
						makeComputerMove();
					else
					{
						message.setText("YOUR TURN");
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
			next = ai.computerRand();
			while(board.ShowFieldState(next) != 0)
			{
				next = ai.computerRand();
			}
			ai.computerMove(next);
			makeMove(computer, next);
		}
		
		private void startGame()
		{
			if(player != 1)
			{
				makeComputerMove();
			}
			
			message.setText("YOUR MOVE");
			message.setVisible(true);
			player_turn = true;
		}
		
		private void end()
		{
			
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			Object event = e.getSource();
			/*try {
				if(event == element_menu_zapisz) {
					chooser.setDialogTitle("Wybierz plik"); // Ustawienie tytu³u okienka
			        int result = chooser.showDialog(null, "Wybierz"); //Otwarcie okienka. Metoda ta blokuje siê do czasu wybrania pliku lub zamkniêcia okna
			        if (JFileChooser.APPROVE_OPTION == result){ //Jeœli u¿tytkownik wybra³ plik
			            System.out.println("Zapisano do pliku: " + chooser.getSelectedFile());
			        }else {
			            System.out.println("Nie wybrano pliku");
			        }
					mojeikony.zapisz(chooser.getSelectedFile());
				}
				if(co_kliknieto == element_menu_wczytaj) {
					chooser.setDialogTitle("Wybierz plik"); // Ustawienie tytu³u okienka
			        int result = chooser.showDialog(null, "Wybierz"); //Otwarcie okienka. Metoda ta blokuje siê do czasu wybrania pliku lub zamkniêcia okna
			        if (JFileChooser.APPROVE_OPTION == result){ //Jeœli u¿tytkownik wybra³ plik
			            System.out.println("Odczytano z pliku: " + chooser.getSelectedFile());
			        }else {
			            System.out.println("Nie wybrano pliku");
			        }
					mojeikony=tlo.wczytaj(chooser.getSelectedFile());
					for(int i=0;i<1600;i++){
						przycisk[i].setIcon(ikona[mojeikony.odczytaj(i)]);				
					}
				}
			}
			catch(IOException e1) {
				System.out.println("Blad we/wy: "+e1.getMessage());
			}
			catch(ClassNotFoundException e1) {
				System.out.println("Nie znaleziono klasy: "+e1.getMessage());
			}*/
			for(int x=0; x<field_count; x++){
		    	for(int y=0; y<field_count; y++){
		    		for(int z=0; z<field_count; z++)
		    		{
			    		if(event == field[x][y][z] && player_turn == true)
			    		{
			    			player_turn = false;
			    			next[0] = x;
			    			next[1] = y;
			    			next[2] = z;
			    			ai.userMove(next);
			    			makeMove(player,next);
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