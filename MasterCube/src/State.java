import java.io.*;
//import javax.swing.JFileChooser;

public class State implements Serializable{
	static final long serialVersionUID = 12345L;
	private int size;
	private int BoardState[][][];
	
	public State(int fields){
		size = fields;
		BoardState = new int[size][size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				for(int k=0;k<size;k++){
					BoardState[i][j][k]=0;
				}
			}
		}
	}
	
	public void Clear(){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				for(int k=0;k<size;k++){
					BoardState[i][j][k]=0;
				}
			}
		}
	}
	
	public int[][][] ShowBoard(){
		return BoardState;
	}
	
	
	public int ChangeState(int who, int[] move){
		BoardState[move[0]][move[1]][move[2]]=who;
		return BoardState[move[0]][move[1]][move[2]];
	}
	
	public int ShowFieldState(int[] move){
		return BoardState[move[0]][move[1]][move[2]];
	}
	
	public int ShowSize(){
		return size;
	}
	
	public void Serialize(File out) throws IOException{
		ObjectOutputStream wyjscie = new ObjectOutputStream(new FileOutputStream(out));
		wyjscie.writeObject(this);
		wyjscie.close();
		
	}
	
	public State Read(File in) throws IOException, FileNotFoundException, ClassNotFoundException{
		ObjectInputStream Board = new ObjectInputStream(new FileInputStream(in));
		State readed = (State) Board.readObject();
		Board.close();
		
		return readed;
		
	}
}
