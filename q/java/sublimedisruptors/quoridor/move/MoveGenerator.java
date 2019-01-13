package sublimedisruptors.quoridor.move;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Direction;
import sublimedisruptors.quoridor.board.Groove;
import sublimedisruptors.quoridor.board.Square;

/** Responsible for generating valid moves according to the rules of Quoridor. */
public final class MoveGenerator {

  /** Creates a {@code MoveGenerator} and sets up pawns in their initial positions. */
  public static MoveGenerator createAndSetUpPawns(Board board, QuoridorSettings settings) {
    MoveGenerator moveGenerator = new MoveGenerator(checkNotNull(board), settings.wallSize());
    settings.players().forEach(moveGenerator::placePawnInInitialSquare);
    return moveGenerator;
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
      if (!isInBounds(adjacentSquare)) {
        continue;
      }
      if (!pawns.containsValue(adjacentSquare)) {
        moves.add(Move.pawnMove(player, adjacentSquare));
        continue;
      }
      // An adjacent square is occupied by a pawn. Check whether that pawn can be jumped.
      if (!walledOffGrooves.contains(adjacentSquare.borderingGroove(direction))) {
        Square jump = adjacentSquare.adjacentSquare(direction);
        if (isInBounds(jump) && !pawns.containsValue(jump)) {
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
        if (isInBounds(diagonalSquare) && !pawns.containsValue(diagonalSquare)) {
          moves.add(Move.pawnMove(player, diagonalSquare));
        }
      }
    }
    return moves.build();
  }

  private void placePawnInInitialSquare(Player player) {
    Square initialSquare;
    switch (player) {
      case PLAYER1:
        initialSquare = Square.at(middleColumn(), bottomRow());
        break;
      case PLAYER2:
        initialSquare = Square.at(middleColumn(), topRow());
        break;
      case PLAYER3:
        initialSquare = Square.at(firstColumn(), middleRow());
        break;
      case PLAYER4:
        initialSquare = Square.at(lastColumn(), middleRow());
        break;
      default:
        throw new IllegalStateException("Unknown player: " + player);
    }
    board.movePawn(player, initialSquare);
  }

  private boolean isInBounds(Square square) {
    return Range.closed(firstColumn(), lastColumn()).contains(square.column())
        && Range.closed(topRow(), bottomRow()).contains(square.row());
  }

  private char firstColumn() {
    return 'a';
  }

  private char middleColumn() {
    return (char) (firstColumn() + board.size() / 2);
  }

  private char lastColumn() {
    return (char) (firstColumn() + board.size() - 1);
  }

  private int topRow() {
    return 1;
  }

  private int middleRow() {
    return topRow() + board.size() / 2;
  }

  private int bottomRow() {
    return board.size();
  }
}
