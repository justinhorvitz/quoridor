package sublimedisruptors.quoridor.move;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;

/** Tests for {@link PathFinder}. */
@RunWith(JUnit4.class)
public final class PathFinderTest {

  private Board board;
  private RulesGovernor governor;

  @Test
  public void noWalls() {
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
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    board.movePawn(Player.PLAYER1, Square.at('e', 9));
    assertThat(pathToGoalExists(Player.PLAYER2)).isTrue();
    assertThat(pathToGoalExists(Player.PLAYER1)).isTrue();
  }

  @Test
  public void onePlayerWalledIn() {
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
     *               ----------------+++++++++++++++++++++
     *             8 |   |   |   |   +   |   |   |   |   |
     *               ----------------+--------------------
     *             9 |   |   |   |   + 1 |   |   |   |   |
     *               ----------------+--------------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    board.movePawn(Player.PLAYER1, Square.at('e', 9));
    board.placeWall(Wall.horizontal('e', 7).withLength(5), Player.PLAYER1);
    board.placeWall(Wall.vertical('d', 8).withLength(2), Player.PLAYER2);
    assertThat(pathToGoalExists(Player.PLAYER2)).isTrue();
    assertThat(pathToGoalExists(Player.PLAYER1)).isFalse();
  }

  @Test
  public void bothPlayersWalledIn() {
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
     *               +++++++++++++++++++++++++++++++++++++
     *             8 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             9 |   |   |   |   | 1 |   |   |   |   |
     *               -------------------------------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    board.movePawn(Player.PLAYER1, Square.at('e', 9));
    board.placeWall(Wall.horizontal('a', 7).withLength(9), Player.PLAYER1);
    assertThat(pathToGoalExists(Player.PLAYER2)).isFalse();
    assertThat(pathToGoalExists(Player.PLAYER1)).isFalse();
  }

  @Test
  public void separatePathsToGoal() {
    /*
     *                 a   b   c   d   e   f   g   h   i
     *               --------------------------------+----
     *             1 |   |   |   |   | 2 |   |   |   +   |
     *               --------------------------------+----
     *             2 |   |   |   |   |   |   |   |   +   |
     *               --------------------------------+----
     *             3 |   |   |   |   |   |   |   |   +   |
     *               --------------------------------+----
     *             4 |   |   |   |   |   |   |   |   +   |
     *               --------------------------------+----
     *             5 |   |   |   |   |   |   |   |   +   |
     *               --------------------------------+----
     *             6 |   |   |   |   |   |   |   |   +   |
     *               ----------------+++++++++++++++++----
     *             7 |   |   |   |   +   |   |   |   |   |
     *               ----------------+--------------------
     *             8 |   |   |   |   +   |   |   |   |   |
     *               ----------------+--------------------
     *             9 |   |   |   |   + 1 |   |   |   |   |
     *               ----------------+--------------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    board.movePawn(Player.PLAYER1, Square.at('e', 9));
    board.placeWall(Wall.vertical('h', 1).withLength(6), Player.PLAYER1);
    board.placeWall(Wall.horizontal('e', 6).withLength(4), Player.PLAYER2);
    board.placeWall(Wall.vertical('d', 7).withLength(3), Player.PLAYER2);
    assertThat(pathToGoalExists(Player.PLAYER2)).isTrue();
    assertThat(pathToGoalExists(Player.PLAYER1)).isTrue();
  }

  @Test
  public void trickyMazeToGoal() {
    /*
     *                 a   b   c   d   e   f   g   h   i
     *               -------------------------------------
     *             1 |   |   |   |   |   |   |   |   |   |
     *               -------------------------------------
     *             2 |   |   |   |   |   |   |   |   |   |
     *               --------------------------------+++++
     *             3 |   |   |   |   |   |   |   | 2 |   |
     *               --------------------+++++++++++++----
     *             4 |   |   |   |   |   +   |   |   |   |
     *               --------------------+----------------
     *             5 |   |   |   |   |   +   |   |   |   |
     *               --------+++++++++++++++++++++++++----
     *             6 |   |   + 1 +   |   |   |   |   +   |
     *               --------+---+-------------------+----
     *             7 |   |   +   +   |   |   |   |   +   |
     *               --------+---+---+++++++++-------+----
     *             8 |   |   +   +   |   |   +   |   +   |
     *               --------+---+++++++++---+-------+----
     *             9 |   |   +   |   |   |   +   |   |   |
     *               --------+---------------+------------
     *                 a   b   c   d   e   f   g   h   i
     */
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('h', 3));
    board.movePawn(Player.PLAYER1, Square.at('c', 6));
    board.placeWall(Wall.horizontal('i', 2).withLength(1), Player.PLAYER1);
    board.placeWall(Wall.horizontal('f', 3).withLength(3), Player.PLAYER2);
    board.placeWall(Wall.vertical('e', 4).withLength(2), Player.PLAYER1);
    board.placeWall(Wall.horizontal('c', 5).withLength(6), Player.PLAYER2);
    board.placeWall(Wall.vertical('b', 6).withLength(4), Player.PLAYER1);
    board.placeWall(Wall.vertical('c', 6).withLength(3), Player.PLAYER2);
    board.placeWall(Wall.horizontal('d', 8).withLength(2), Player.PLAYER1);
    board.placeWall(Wall.horizontal('e', 7).withLength(2), Player.PLAYER2);
    board.placeWall(Wall.vertical('f', 8).withLength(2), Player.PLAYER1);
    board.placeWall(Wall.vertical('h', 6).withLength(3), Player.PLAYER2);
    assertThat(pathToGoalExists(Player.PLAYER2)).isTrue();
    assertThat(pathToGoalExists(Player.PLAYER1)).isTrue();
  }

  @Test
  public void boardNotMutatedBySearch() {
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('b', 3));
    board.movePawn(Player.PLAYER1, Square.at('f', 8));
    Board.Snapshot snapshot = board.snapshot();
    assertThat(pathToGoalExists(Player.PLAYER2)).isTrue();
    assertThat(board.snapshot()).isEqualTo(snapshot);
    assertThat(pathToGoalExists(Player.PLAYER1)).isTrue();
    assertThat(board.snapshot()).isEqualTo(snapshot);
  }

  @Test
  public void pawnNotOnBoard_throws() {
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('b', 3));
    board.movePawn(Player.PLAYER1, Square.at('f', 8));
    assertThrows(() -> pathToGoalExists(Player.PLAYER3));
  }

  @Test
  public void nullPlayer_throws() {
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('b', 3));
    board.movePawn(Player.PLAYER1, Square.at('f', 8));
    assertThrows(() -> pathToGoalExists(null));
  }

  @Test
  public void nullBoard_throws() {
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('b', 3));
    board.movePawn(Player.PLAYER1, Square.at('f', 8));
    assertThrows(() -> PathFinder.pathToGoalExists(Player.PLAYER1, null, governor));
  }

  @Test
  public void nullRulesGovernor_throws() {
    setUpBoard(QuoridorSettings.defaultTwoPlayer().toBuilder().setBoardSize(9));
    board.movePawn(Player.PLAYER2, Square.at('b', 3));
    board.movePawn(Player.PLAYER1, Square.at('f', 8));
    assertThrows(() -> PathFinder.pathToGoalExists(Player.PLAYER1, board, null));
  }

  private void setUpBoard(QuoridorSettings.Builder settingsBuilder) {
    QuoridorSettings settings = settingsBuilder.build();
    board = Board.createFromSettings(settings);
    governor = RulesGovernor.createAndSetUpPawns(board, settings);
  }

  private boolean pathToGoalExists(Player player) {
    return PathFinder.pathToGoalExists(player, board, governor);
  }
}
