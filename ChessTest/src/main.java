
import chess.core.ChessGame;
import chess.core.Move;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("alma");
		ChessGame cg = new ChessGame();
		System.out.println(cg.getMoveCount());
		Move move = new Move(6, 1, 6, 3);
		cg.movePiece(move);
		var algorithm = cg.algorithm.reply(false);
		System.out.println(algorithm.toString());
		cg.movePiece(algorithm);
		System.out.println();
		move = new Move(5, 1, 5, 2);
		if (cg.checkIfLegalMove(move))
			cg.movePiece(move);

//		algorithm = cg.algorithm.reply(false);
//		System.out.println(algorithm.toString());
//		cg.movePiece(algorithm);
	}

}
