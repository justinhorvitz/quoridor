package sublimedisruptors.quoridor.move;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Board.Snapshot;
import sublimedisruptors.quoridor.board.Direction;
import sublimedisruptors.quoridor.board.Groove;
import sublimedisruptors.quoridor.board.Groove.Orientation;
import sublimedisruptors.quoridor.board.Locatable;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;
import sublimedisruptors.quoridor.move.Move.Type;

/** Governs the game according to the rules of Quoridor. */
public final class RulesGovernor {

  /** Creates a {@code RulesGovernor} and sets up pawns in their initial positions. */
  public static RulesGovernor createAndSetUpPawns(Board board, QuoridorSettings settings) {
    RulesGovernor rulesGovernor = new RulesGovernor(checkNotNull(board));
    settings.players().forEach(rulesGovernor::placePawnInInitialSquare);
    return rulesGovernor;
  }

  private final Board board;

  private RulesGovernor(Board board) {
    this.board = board;
  }

  /**
   * Generates the set of all valid {@linkplain Move.Type#PAWN pawn moves} that the given {@code
   * player} can legally make.
   *
   * <p>{@code player} must be participating in the game (i.e. has a pawn on the board).
   */
  public ImmutableSet<Move> generateValidPawnMoves(Player player) {
    Board.Snapshot snapshot = board.snapshot();
    Map<Player, Square> pawns = snapshot.pawns();
    Square currentSquare = pawns.get(player);
    checkState(currentSquare != null, "%s has no pawn on the board", player);
    Set<Groove> walledOffGrooves = snapshot.walledOffGrooves();

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

  /**
   * Determines whether {@code wallMove} is valid.
   *
   * <p>A wall move may be invalid for any of the following reasons:
   *
   * <ol>
   *   <li>The player responsible for the move has no {@linkplain Snapshot#wallsAvailable} walls
   *       available}.
   *   <li>The wall is on or extends past an edge of the board.
   *   <li>The wall overlaps groove(s) of a wall already on the board.
   *   <li>The wall overlaps a vertex of a wall already on the board.
   *   <li>The wall prevents one or more participants from possibly reaching their goal.
   * </ol>
   */
  public boolean isValidWallMove(Move wallMove) {
    checkArgument(wallMove.type() == Type.WALL, "%s not a wall move", wallMove);
    Board.Snapshot snapshot = board.snapshot();
    Integer wallsAvailable = snapshot.wallsAvailable().get(wallMove.player());
    checkState(wallsAvailable != null, "%s not participating", wallMove.player());
    if (wallsAvailable < 1) {
      return false;
    }
    Wall wall = wallMove.wall();
    for (Groove groove : wall.coveredGrooves()) {
      if (!isInBounds(groove)
          || (groove.orientation() == Orientation.VERTICAL && groove.column() == lastColumn())
          || (groove.orientation() == Orientation.HORIZONTAL && groove.row() == bottomRow())
          || snapshot.walledOffGrooves().contains(groove)) {
        return false;
      }
    }
    if (!Collections.disjoint(snapshot.walledOffVertices(), wall.coveredVertices())) {
      return false;
    }
    Board boardWithWall = Board.fromSnapshot(snapshot);
    boardWithWall.placeWall(wall, wallMove.player());
    for (Player player : snapshot.pawns().keySet()) {
      if (!PathFinder.pathToGoalExists(player, boardWithWall, new RulesGovernor(boardWithWall))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns {@code true} if the given {@code player} wins the game by reaching {@code square}.
   *
   * <p>No validation is performed to ensure that {@code player} has a pawn on the board.
   */
  public boolean isGoal(Player player, Square square) {
    switch (player) {
      case PLAYER1:
        return square.row() == topRow();
      case PLAYER2:
        return square.row() == bottomRow();
      case PLAYER3:
        return square.column() == lastColumn();
      case PLAYER4:
        return square.column() == firstColumn();
    }
    throw new IllegalArgumentException("Unknown player: " + player);
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

  private boolean isInBounds(Locatable location) {
    return Range.closed(firstColumn(), lastColumn()).contains(location.column())
        && Range.closed(topRow(), bottomRow()).contains(location.row());
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
