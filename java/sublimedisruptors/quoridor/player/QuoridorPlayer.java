package sublimedisruptors.quoridor.player;

import com.google.common.collect.ImmutableSet;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.move.Move;

/** A participant in a game of quoridor capable of deciding on its next move. */
public interface QuoridorPlayer {

  interface Factory {
    /**
     * Creates a {@link QuoridorPlayer} prepared to participate as {@code self} in a game with the
     * given {@code settings}.
     */
    QuoridorPlayer createPlayer(Player self, QuoridorSettings settings);
  }

  /**
   * Returns the {@link Move} that this player wishes to make.
   *
   * <p>This method is called when it is this player's turn. The current state of the game is given
   * in the {@code board} parameter.
   *
   * <p>It is the implementation's responsibility to return a legal move. For convenience (and
   * because it is relatively cheap to compute) the set of all valid {@linkplain Move.Type#PAWN pawn
   * moves} is provided. If the implementation wishes to make a {@linkplain Move.Type#WALL wall
   * move}, it is responsible for determining validity itself.
   */
  Move getMove(Board.Snapshot board, ImmutableSet<Move> validPawnMoves);
}
