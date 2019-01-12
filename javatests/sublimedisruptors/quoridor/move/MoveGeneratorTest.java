package sublimedisruptors.quoridor.move;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Groove;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;

/** Tests for {@link MoveGenerator}. */
@RunWith(JUnit4.class)
public final class MoveGeneratorTest {

  private final QuoridorSettings settings =
      QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1).build();
  private final Board board = Board.createFromSettings(settings);
  private final MoveGenerator moveGenerator = MoveGenerator.create(board, settings);

  @Test
  public void allFourDirectionsFree() {
    /*
     *                 a   b   c
     *               -------------
     *             1 | 2 |   |   |
     *               -------------
     *             2 |   | 1 |   |
     *               -------------
     *             3 |   |   |   |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('b', 2));
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('b', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('a', 2)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 2)));
  }

  @Test
  public void threeDirectionsFree() {
    /*
     *                 a   b   c
     *               -------------
     *             1 | 2 |   |   |
     *               -------------
     *             2 |   |   | 1 |
     *               -------------
     *             3 |   |   |   |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 2));
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('c', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 2)));
  }

  @Test
  public void twoDirectionsFree() {
    /*
     *                 a   b   c
     *               -------------
     *             1 | 2 |   |   |
     *               -------------
     *             2 |   |   |   |
     *               -------------
     *             3 |   |   | 1 |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('c', 2)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 3)));
  }

  @Test
  public void jumpOverOpponent() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   |   |   |
     *               -------------
     *             2 |   |   | 2 |
     *               -------------
     *             3 |   |   | 1 |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('c', 2));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('c', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 3)));
  }

  @Test
  public void blockedByWallAbove() {
    /*
     *                 a   b   c
     *               -------------
     *             1 | 2 |   |   |
     *               -------------
     *             2 |   |   |   |
     *               --------+++++
     *             3 |   |   | 1 |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    board.placeWall(new Wall(Groove.horizontal('c', 2), /*length=*/ 1), Player.PLAYER2);
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves).containsExactly(Move.pawnMove(Player.PLAYER1, Square.at('b', 3)));
  }

  @Test
  public void blockedByWallBeside() {
    /*
     *                 a   b   c
     *               -------------
     *             1 | 2 |   |   |
     *               -------------
     *             2 |   |   |   |
     *               --------+----
     *             3 |   |   + 1 |
     *               --------+----
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    board.placeWall(new Wall(Groove.vertical('b', 3), /*length=*/ 1), Player.PLAYER2);
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves).containsExactly(Move.pawnMove(Player.PLAYER1, Square.at('c', 2)));
  }

  @Test
  public void diagonalMoveDueToWall() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   |   |   |
     *               ----+++++----
     *             2 |   | 2 |   |
     *               -------------
     *             3 |   | 1 |   |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('b', 2));
    board.movePawn(Player.PLAYER1, Square.at('b', 3));
    board.placeWall(new Wall(Groove.horizontal('b', 1), /*length=*/ 1), Player.PLAYER2);
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('a', 2)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 2)),
            Move.pawnMove(Player.PLAYER1, Square.at('a', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 3)));
  }

  @Test
  public void diagonalMoveDueToBoundary() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   |   |   |
     *               -------------
     *             2 | 2 | 1 |   |
     *               -------------
     *             3 |   |   |   |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 2));
    board.movePawn(Player.PLAYER1, Square.at('b', 2));
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('a', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('a', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 2)));
  }

  @Test
  public void diagonalMoveBlockedByWall() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   |   |   |
     *               +++++--------
     *             2 | 2 | 1 |   |
     *               -------------
     *             3 |   |   |   |
     *               -------------
     *                 a   b   c
     */
    board.movePawn(Player.PLAYER2, Square.at('a', 2));
    board.movePawn(Player.PLAYER1, Square.at('b', 2));
    board.placeWall(new Wall(Groove.horizontal('a', 1), /*length=*/ 1), Player.PLAYER2);
    Set<Move> moves = moveGenerator.generateValidPawnMoves(Player.PLAYER1);
    assertThat(moves)
        .containsExactly(
            Move.pawnMove(Player.PLAYER1, Square.at('a', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 1)),
            Move.pawnMove(Player.PLAYER1, Square.at('b', 3)),
            Move.pawnMove(Player.PLAYER1, Square.at('c', 2)));
  }

  @Test
  public void noPawnOnBoard_throws() {
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    assertThrows(
        IllegalStateException.class, () -> moveGenerator.generateValidPawnMoves(Player.PLAYER3));
  }
}
