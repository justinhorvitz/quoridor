package sublimedisruptors.quoridor.game;

import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.move.Move;

/**
 * An exception that occurs when a {@link sublimedisruptors.quoridor.player.QuoridorPlayer} requests
 * an illegal move.
 */
final class IllegalMoveException extends RuntimeException {

  IllegalMoveException(Move move, Player player, Board.Snapshot board) {
    super(String.format("%s requested an illegal move: %s\n%s", player, move, board));
  }
}
