package sublimedisruptors.quoridor.board;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.QuoridorSettings.Builder;

/** Tests for {@link Board}. */
@RunWith(JUnit4.class)
public final class BoardTest {

  private final Builder settings = QuoridorSettings.defaultTwoPlayer().toBuilder();

  @Test
  public void createFromSettings_size() {
    settings.setBoardSize(5);
    Board board = Board.createFromSettings(settings.build());
    assertThat(board.size()).isEqualTo(5);
  }

  @Test
  public void createFromSettings_wallsAvailable() {
    settings.setPlayers(Player.PLAYER1, Player.PLAYER2).setWallsPerPlayer(7);
    Board board = Board.createFromSettings(settings.build());
    assertThat(board.getWallsAvailable()).containsExactly(Player.PLAYER1, 7, Player.PLAYER2, 7);
  }

  @Test
  public void boardInitiallyEmpty() {
    Board board = Board.createFromSettings(settings.build());
    assertThat(board.getPawns()).isEmpty();
    assertThat(board.getWalledOffGrooves()).isEmpty();
    assertThat(board.getWalledOffVertices()).isEmpty();
  }

  @Test
  public void movePawn_notPreviouslyOnBoard() {
    Board board = Board.createFromSettings(settings.build());
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    assertThat(board.getPawns()).containsExactly(Player.PLAYER2, Square.at('e', 1));
  }

  @Test
  public void movePawn_previouslyOnBoard() {
    Board board = Board.createFromSettings(settings.build());
    board.movePawn(Player.PLAYER2, Square.at('e', 1));
    board.movePawn(Player.PLAYER1, Square.at('e', 9));
    board.movePawn(Player.PLAYER1, Square.at('e', 8));
    assertThat(board.getPawns())
        .containsExactly(Player.PLAYER1, Square.at('e', 8), Player.PLAYER2, Square.at('e', 1));
  }

  @Test
  public void placeWall() {
    settings.setPlayers(Player.PLAYER1, Player.PLAYER2).setWallsPerPlayer(2).setWallSize(3);
    Board board = Board.createFromSettings(settings.build());
    Wall wall = new Wall(Groove.vertical('a', 1), /*length=*/ 3);
    board.placeWall(wall, Player.PLAYER1);
    assertThat(board.getWalledOffGrooves()).containsExactlyElementsIn(wall.coveredGrooves());
    assertThat(board.getWalledOffVertices()).containsExactlyElementsIn(wall.coveredVertices());
    assertThat(board.getWallsAvailable()).containsExactly(Player.PLAYER1, 1, Player.PLAYER2, 2);
  }
}
