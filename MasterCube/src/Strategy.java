import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Strategy implements Serializable{
	static final long serialVersionUID = 12345L;
	State board;
	byte size;
	float inteligence;
	MovesTree tree;
	Node actual_node;
	float inteligence_step;
	Random generator;
	float up_to;
	float down_from;
	byte move[];
	
	public Strategy(State state, float in, byte step){
		board = state;
		size = board.ShowSize();
		inteligence = in;
		tree = new MovesTree(size);
		actual_node = tree.root;
		inteligence_step = 1/(float)step;
		generator = new Random();	
		move = new byte[3];
	}
	
	public void nextMove(byte[] next){
		Node child;
		for(int i=0; i<actual_node.childrenCount(); i++)
		{
			child = actual_node.children.get(i);
			if(Arrays.equals(child.move, next))
			{
				actual_node = child;
				break;
			}
		}
	}
	
	public byte[] computerRand(){
		if(actual_node.deep>2)
		{
			up_to = ((float)actual_node.childrenCount()*inteligence);
			down_from = ((float)actual_node.childrenCount()*(inteligence-inteligence_step));
			if(actual_node.children.get(generator.nextInt((int)((up_to-down_from)+down_from))) != null)
				return actual_node.children.get(generator.nextInt((int)((up_to-down_from)+down_from))).move;
			else 
				return actual_node.children.get(0).move;
		}
		else
		{
			move[0]=(byte) generator.nextInt(size);
			move[1]=(byte) generator.nextInt(size);
			move[2]=(byte) generator.nextInt(size);
			return move;
		}
	}
	
	public void firstMove(byte[] move)
	{
		tree.addNode(move);
		actual_node = tree.root;
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
