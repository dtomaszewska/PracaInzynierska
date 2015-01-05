import java.io.*;
//import javax.swing.JFileChooser;

public class State implements Serializable{
	static final long serialVersionUID = 12345L;
	private byte size;
	private byte BoardState[][][];
	
	public State(byte fields){
		size = fields;
		BoardState = new byte[size][size][size];
		for(byte i=0;i<size;i++){
			for(byte j=0;j<size;j++){
				for(byte k=0;k<size;k++){
					BoardState[i][j][k]=0;
				}
			}
		}
	}
	
	public void Clear(){
		for(byte i=0;i<size;i++){
			for(byte j=0;j<size;j++){
				for(byte k=0;k<size;k++){
					BoardState[i][j][k]=0;
				}
			}
		}
	}
	
	public byte[][][] ShowBoard(){
		return BoardState;
	}
	
	
	public byte ChangeState(byte who, byte[] move){
		BoardState[move[0]][move[1]][move[2]]=who;
		return BoardState[move[0]][move[1]][move[2]];
	}
	
	public byte ShowFieldState(byte[] move){
		return BoardState[move[0]][move[1]][move[2]];
	}
	
	public byte ShowFieldState(byte x, byte y, byte z){
		return BoardState[x][y][z];
	}
	
	public byte ShowSize(){
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
