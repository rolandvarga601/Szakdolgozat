
import chess.core.ChessGame;
import chess.core.Move;
import chess.core.Constants;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("alma");
		ChessGame cg = new ChessGame();
		System.out.println(cg.getMoveCount());
		Move move = new Move(5, 1, 5, 3);
		System.out.println(move.toString());
		//Move foundMove = move.FindMove(move.WhitePiecesAt(cg.board.b), move.WhitePiecesAt(cg.board.b));
		cg.movePiece(move);
		var algorithm = cg.algorithm.reply(false);
		System.out.println(algorithm.toString());
		cg.movePiece(algorithm);
		System.out.println();
		move = new Move(6,0,5,2);
		move.type = cg.board.b[move.x1][move.y1].type;
		System.out.println(move.toString());
		if (cg.checkIfLegalMove(move))
			cg.movePiece(move);

//		algorithm = cg.algorithm.reply(false);
//		System.out.println(algorithm.toString());
//		cg.movePiece(algorithm);
	}

}
