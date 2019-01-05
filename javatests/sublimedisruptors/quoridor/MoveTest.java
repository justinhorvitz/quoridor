package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
/** Tests for {@link Move}. */
public final class MoveTest {

  @Test
  public void pawnMove() {
    Square destination = Square.create(Vertex.at('c', 3));
    Move pawnMove = Move.pawnMove(Player.PLAYER1, destination);
    assertThat(pawnMove.player()).isEqualTo(Player.PLAYER1);
    assertThat(pawnMove.type()).isEqualTo(Move.Type.PAWN);
    assertThat(pawnMove.destination()).isEqualTo(destination);
    assertThrows(UnsupportedOperationException.class, () -> pawnMove.wall());
  }

  @Test
  public void wallMove() {
    Wall wall = new Wall(Edge.vertical(Vertex.at('c', 3)), /*length=*/ 2);
    Move wallMove = Move.wallMove(Player.PLAYER1, wall);
    assertThat(wallMove.player()).isEqualTo(Player.PLAYER1);
    assertThat(wallMove.type()).isEqualTo(Move.Type.WALL);
    assertThat(wallMove.wall()).isEqualTo(wall);
    assertThrows(UnsupportedOperationException.class, () -> wallMove.destination());
  }
}
