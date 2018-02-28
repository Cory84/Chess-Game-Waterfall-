package Pieces;

public class Pawn extends Piece{
	
	boolean hasMoved;
	
	public Pawn(int ally) {
		hasMoved = false;
		if(ally == 0) {
			setAlly(0);
			setName("White Pawn");
			setImageName("WPawn");
		}
		else {
			setAlly(1);
			setName("Black Pawn");
			setImageName("BPawn");
		}
	}
	
	public boolean validMove(int start, int end) {
		boolean valid = false;
		if(this.getAlly()==0) {
			int product = end-start;
			if(product == -8) {
				valid = true;
			}
			else if(product == -16 && hasMoved == false) {
				valid = true;
			}
		}
		else if(this.getAlly()==1) {
			int product = end-start;
			if(product == 8) {
				valid = true;
			}
			else if(product == 16 && hasMoved == false) {
				valid = true;
			}
		}
		hasMoved = true;
		return valid;
    }

}
