import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Strategy implements Serializable{
	static final long serialVersionUID = 12345L;
	State board;
	int size;
	float inteligence;
	MovesTree tree;
	Node actual_node;
	float inteligence_step;
	Random generator;
	int up_to;
	int down_from;
	
	public Strategy(State state, float in, int step){
		board = state;
		size = board.ShowSize();
		inteligence = in;
		tree = new MovesTree(size);
		actual_node = tree.root;
		inteligence_step = 1/(float)step;
		generator = new Random();	
	}
	
	public void userMove(int[] move){
		Node child;
		for(int i=0; i<actual_node.childrenCount(); i++)
		{
			child = actual_node.children.get(i);
			if(Arrays.equals(child.move, move))
			{
				actual_node = child;
				break;
			}
		}
	}
	
	public int[] computerRand(){
		up_to = (int)((float)actual_node.childrenCount()*inteligence);
		down_from = (int)((float)actual_node.childrenCount()*(inteligence-inteligence_step));
		return actual_node.children.get(generator.nextInt(up_to-down_from)+down_from).move;
	}
	public void computerMove(int[] move){
		Node child;
		for(int i=0; i<actual_node.childrenCount(); i++)
		{
			child = actual_node.children.get(i);
			if(Arrays.equals(child.move, move))
			{
				actual_node = child;
				break;
			}
		}
	}
	
	public void save(File plik) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(plik));
		out.writeObject(this);
		out.close();
	}
	public static Strategy load(File plik) throws IOException, FileNotFoundException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(plik));
		Strategy odczytany = (Strategy) in.readObject();
		in.close();
		return odczytany;
	}
}
