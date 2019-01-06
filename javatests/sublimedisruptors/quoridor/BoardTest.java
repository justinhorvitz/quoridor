package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link Board}. */
@RunWith(JUnit4.class)
public final class BoardTest {

  private final QuoridorSettings.Builder settings = QuoridorSettings.defaultTwoPlayer().toBuilder();

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
    assertThat(board.getWalledOffEdges()).isEmpty();
    assertThat(board.getWalledOffVertices()).isEmpty();
  }

  @Test
  public void movePawn_notPreviouslyOnBoard() {
    Board board = Board.createFromSettings(settings.build());
    Square e1 = Square.create(Vertex.at('e', 1));
    board.movePawn(Player.PLAYER2, e1);
    assertThat(board.getPawns()).containsExactly(Player.PLAYER2, e1);
  }

  @Test
  public void movePawn_previouslyOnBoard() {
    Board board = Board.createFromSettings(settings.build());
    Square e1 = Square.create(Vertex.at('e', 1));
    Square e9 = Square.create(Vertex.at('e', 9));
    board.movePawn(Player.PLAYER2, e1);
    board.movePawn(Player.PLAYER1, e9);
    Square e8 = Square.create(Vertex.at('e', 8));
    board.movePawn(Player.PLAYER1, e8);
    assertThat(board.getPawns()).containsExactly(Player.PLAYER1, e8, Player.PLAYER2, e1);
  }

  @Test
  public void placeWall() {
    settings.setPlayers(Player.PLAYER1, Player.PLAYER2).setWallsPerPlayer(2).setWallSize(3);
    Board board = Board.createFromSettings(settings.build());
    Wall wall = new Wall(Edge.vertical(Vertex.at('a', 1)), /*length=*/ 3);
    board.placeWall(wall, Player.PLAYER1);
    assertThat(board.getWalledOffEdges()).containsExactlyElementsIn(wall.coveredEdges());
    assertThat(board.getWalledOffVertices()).containsExactlyElementsIn(wall.coveredVertices());
    assertThat(board.getWallsAvailable()).containsExactly(Player.PLAYER1, 1, Player.PLAYER2, 2);
  }
}
