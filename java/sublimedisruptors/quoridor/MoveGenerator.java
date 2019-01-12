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
      Square borderingSquare = currentSquare.borderingSquare(direction);
      if (!isInBounds(borderingSquare, board)
          || walledOffGrooves.contains(currentSquare.borderingGroove(direction))) {
        continue;
      }
      if (!pawns.containsValue(borderingSquare)) {
        moves.add(Move.pawnMove(player, borderingSquare));
        continue;
      }
      // A bordering square is occupied by a pawn. Check whether that pawn can be jumped.
      Square jump = borderingSquare.borderingSquare(direction);
      if (isInBounds(jump, board)
          && !walledOffGrooves.contains(borderingSquare.borderingGroove(direction))
          && !pawns.containsValue(jump)) {
        moves.add(Move.pawnMove(player, jump));
        continue;
      }
      // A jump cannot be made. Check whether a diagonal move can be made.
      List<Direction> orthogonals =
          direction.equals(Direction.UP) || direction.equals(Direction.DOWN)
              ? ImmutableList.of(Direction.LEFT, Direction.RIGHT)
              : ImmutableList.of(Direction.UP, Direction.DOWN);
      for (Direction orthogonal : orthogonals) {
        Square diagonalSquare = borderingSquare.borderingSquare(orthogonal);
        if (isInBounds(diagonalSquare, board)
            && !walledOffGrooves.contains(borderingSquare.borderingGroove(orthogonal))
            && !pawns.containsValue(diagonalSquare)) {
          moves.add(Move.pawnMove(player, diagonalSquare));
        }
      }
    }
    return moves.build();
  }

  private static boolean isInBounds(Square square, Board board) {
    Vertex vertex = square.vertex();
    return Range.closed('a', (char) ('a' + board.size() - 1)).contains(vertex.column())
        && Range.closed(1, board.size()).contains(vertex.row());
  }
}
