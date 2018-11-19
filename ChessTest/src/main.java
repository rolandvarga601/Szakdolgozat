import chess.core.ChessGame;
import chess.core.Move;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("alma");
		ChessGame cg = new ChessGame();
		System.out.println(cg.getMoveCount());
		Move move = new Move(1, 1, 1, 3);
		cg.movePiece(move);
		var algorithm = cg.algorithm.reply(false);
		System.out.println(algorithm.x1);
		System.out.println(algorithm.y1);
		System.out.println(algorithm.x2);
		System.out.println(algorithm.y2);
		cg.movePiece(algorithm);
		System.out.println(cg.getTurn());
	}

}
