package domain.game;

import domain.board.Board;
import domain.board.BoardInitializer;
import domain.board.Position;
import domain.piece.Piece;
import domain.piece.PieceColor;

import java.util.Map;

import static domain.piece.PieceColor.WHITE;

public class ChessGame {
    private final Board board;

    private PieceColor currentColor = WHITE;
    private GameStatus gameStatus = GameStatus.WAITING;

    public ChessGame(final Board board) {
        this.board = board;
    }

    public static ChessGame initGame() {
        return new ChessGame(BoardInitializer.initBoard());
    }

    public void gameStart() {
        gameStatus = GameStatus.RUNNING;
    }

    public void gameEnd() {
        gameStatus = GameStatus.END;
    }

    public boolean isWaiting() {
        return gameStatus == GameStatus.WAITING;
    }

    public boolean isRunning() {
        return gameStatus == GameStatus.RUNNING;
    }

    public boolean isEnd() {
        return gameStatus == GameStatus.END;
    }

    public Map<Position, Piece> piecePositions() {
        return board.piecePositions();
    }

    public void movePiece(final Position source, final Position destination) {
        board.movePiece(currentColor, source, destination);
        currentColor = currentColor.toggle();
    }
}
