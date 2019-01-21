package sublimedisruptors.quoridor.move;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Sets;
import java.util.Set;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Square;

/** Performs searches to determine whether a player can reach their goal. */
final class PathFinder {

  /**
   * Determines whether {@code player} can possibly reach their goal on {@code board} by performing
   * valid moves.
   */
  static boolean pathToGoalExists(Player player, Board board, RulesGovernor governor) {
    return new DepthFirstSearch(player, board, governor).search();
  }

  private static final class DepthFirstSearch {

    private final Player player;
    private final Board board;
    private final RulesGovernor governor;

    DepthFirstSearch(Player player, Board board, RulesGovernor governor) {
      this.player = player;
      this.board = board;
      this.governor = governor;
    }

    boolean search() {
      Square currentSquare = board.snapshot().pawns().get(player);
      checkState(currentSquare != null, "%s has no pawn on board", player);
      try {
        return search(currentSquare, Sets.newHashSet(currentSquare));
      } finally {
        board.movePawn(player, currentSquare); // Ensure the pawn is placed back where it was.
      }
    }

    private boolean search(Square currentSquare, Set<Square> visited) {
      if (governor.isGoal(player, currentSquare)) {
        return true;
      }
      for (Move move : governor.generateValidPawnMoves(player)) {
        Square destination = move.destination();
        if (!visited.add(destination)) {
          continue;
        }
        board.movePawn(player, destination);
        if (search(destination, visited)) {
          return true;
        }
      }
      return false;
    }
  }

  private PathFinder() {}
}
