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
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;

/** Tests for {@link MoveGenerator}. */
@RunWith(JUnit4.class)
public final class MoveGeneratorTest {

  private Board board;
  private MoveGenerator moveGenerator;

  @Test
  public void setUpPawns_twoPlayer_boardSize3() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   | 2 |   |
     *               -------------
     *             2 |   |   |   |
     *               -------------
     *             3 |   | 1 |   |
     *               -------------
     *                 a   b   c
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3));
    assertThat(board.getPawns())
        .containsExactly(Player.PLAYER2, Square.at('b', 1), Player.PLAYER1, Square.at('b', 3));
  }

  @Test
  public void setUpPawns_twoPlayer_boardSize9() {
    /*
     *                 a   b   c   d   e   f   g   h   i
     *               -------------------------------------
     *             1 |   |   |   |   | 2 |   |   |   |   |
     *               -------------------------------------
     *             2 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             3 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             4 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             5 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             6 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             7 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             8 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             9 |   |   |   |   | 1 |   |   |   |   |
     *               -------------------------------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    assertThat(board.getPawns())
        .containsExactly(Player.PLAYER2, Square.at('e', 1), Player.PLAYER1, Square.at('e', 9));
  }

  @Test
  public void setUpPawns_fourPlayer_boardSize3() {
    /*
     *                 a   b   c
     *               -------------
     *             1 |   | 2 |   |
     *               -------------
     *             2 | 3 |   | 4 |
     *               -------------
     *             3 |   | 1 |   |
     *               -------------
     *                 a   b   c
     */
    setUpBoard(QuoridorSettings.defaultFourPlayer().toBuilder().setBoardSize(3));
    assertThat(board.getPawns())
        .containsExactly(
            Player.PLAYER2, Square.at('b', 1),
            Player.PLAYER3, Square.at('a', 2),
            Player.PLAYER4, Square.at('c', 2),
            Player.PLAYER1, Square.at('b', 3));
  }

  @Test
  public void setUpPawns_fourPlayer_boardSize9() {
    /*
     *                 a   b   c   d   e   f   g   h   i
     *               -------------------------------------
     *             1 |   |   |   |   | 2 |   |   |   |   |
     *               -------------------------------------
     *             2 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             3 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             4 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             5 | 3 |   |   |   |   |   |   |   | 4 |
     *               -------------------------------------
     *             6 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             7 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             8 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             9 |   |   |   |   | 1 |   |   |   |   |
     *               -------------------------------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultFourPlayer().toBuilder().setBoardSize(9));
    assertThat(board.getPawns())
        .containsExactly(
            Player.PLAYER2, Square.at('e', 1),
            Player.PLAYER3, Square.at('a', 5),
            Player.PLAYER4, Square.at('i', 5),
            Player.PLAYER1, Square.at('e', 9));
  }

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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    board.placeWall(Wall.horizontal('c', 2).withLength(1), Player.PLAYER2);
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
    board.movePawn(Player.PLAYER2, Square.at('a', 1));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    board.placeWall(Wall.vertical('b', 3).withLength(1), Player.PLAYER2);
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
    board.movePawn(Player.PLAYER2, Square.at('b', 2));
    board.movePawn(Player.PLAYER1, Square.at('b', 3));
    board.placeWall(Wall.horizontal('b', 1).withLength(1), Player.PLAYER2);
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(3).setWallSize(1));
    board.movePawn(Player.PLAYER2, Square.at('a', 2));
    board.movePawn(Player.PLAYER1, Square.at('b', 2));
    board.placeWall(Wall.horizontal('a', 1).withLength(1), Player.PLAYER2);
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
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER1, Square.at('c', 3));
    assertThrows(
        IllegalStateException.class, () -> moveGenerator.generateValidPawnMoves(Player.PLAYER3));
  }

  private void setUpBoard(QuoridorSettings.Builder settingsBuilder) {
    QuoridorSettings settings = settingsBuilder.build();
    board = Board.createFromSettings(settings);
    moveGenerator = MoveGenerator.createAndSetUpPawns(board, settings);
  }
}
