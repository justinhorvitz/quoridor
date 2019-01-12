package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Responsible for generating valid moves according to the rules of Quoridor. */
public final class MoveGenerator {

  public static MoveGenerator create(Board board, QuoridorSettings settings) {
    return new MoveGenerator(checkNotNull(board), settings.wallSize());
  }

  private final Board board;
  private final int wallSize;

  private MoveGenerator(Board board, int wallSize) {
    this.board = board;
    this.wallSize = wallSize;
  }

  public ImmutableSet<Move> generateValidPawnMoves(Player player) {
    Map<Player, Square> pawns = board.getPawns();
    Square currentSquare = pawns.get(player);
    checkState(currentSquare != null, "%s has no pawn on the board", player);
    Set<Groove> walledOffGrooves = board.getWalledOffGrooves();

    ImmutableSet.Builder<Move> moves = ImmutableSet.builder();
    for (Direction direction : Direction.values()) {
      if (walledOffGrooves.contains(currentSquare.borderingGroove(direction))) {
        continue;
      }
      Square adjacentSquare = currentSquare.adjacentSquare(direction);
      if (!isInBounds(adjacentSquare, board)) {
        continue;
      }
      if (!pawns.containsValue(adjacentSquare)) {
        moves.add(Move.pawnMove(player, adjacentSquare));
        continue;
      }
      // An adjacent square is occupied by a pawn. Check whether that pawn can be jumped.
      if (!walledOffGrooves.contains(adjacentSquare.borderingGroove(direction))) {
        Square jump = adjacentSquare.adjacentSquare(direction);
        if (isInBounds(jump, board) && !pawns.containsValue(jump)) {
          moves.add(Move.pawnMove(player, jump));
          continue;
        }
      }
      // A jump cannot be made. Check whether a diagonal move can be made.
      List<Direction> orthogonals =
          direction.equals(Direction.UP) || direction.equals(Direction.DOWN)
              ? ImmutableList.of(Direction.LEFT, Direction.RIGHT)
              : ImmutableList.of(Direction.UP, Direction.DOWN);
      for (Direction orthogonal : orthogonals) {
        if (walledOffGrooves.contains(adjacentSquare.borderingGroove(orthogonal))) {
          continue;
        }
        Square diagonalSquare = adjacentSquare.adjacentSquare(orthogonal);
        if (isInBounds(diagonalSquare, board) && !pawns.containsValue(diagonalSquare)) {
          moves.add(Move.pawnMove(player, diagonalSquare));
        }
      }
    }
    return moves.build();
  }

  private static boolean isInBounds(Square square, Board board) {
    return Range.closed('a', (char) ('a' + board.size() - 1)).contains(square.column())
        && Range.closed(1, board.size()).contains(square.row());
  }
}
