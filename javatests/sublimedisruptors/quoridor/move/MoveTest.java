package sublimedisruptors.quoridor.move;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;

/** Tests for {@link Move}. */
@RunWith(JUnit4.class)
public final class MoveTest {

  @Test
  public void pawnMove() {
    Square destination = Square.at('c', 3);
    Move pawnMove = Move.pawnMove(Player.PLAYER1, destination);
    assertThat(pawnMove.player()).isEqualTo(Player.PLAYER1);
    assertThat(pawnMove.type()).isEqualTo(Move.Type.PAWN);
    assertThat(pawnMove.destination()).isEqualTo(destination);
    assertThrows(pawnMove::wall);
  }

  @Test
  public void wallMove() {
    Wall wall = Wall.vertical('c', 3).withLength(2);
    Move wallMove = Move.wallMove(Player.PLAYER1, wall);
    assertThat(wallMove.player()).isEqualTo(Player.PLAYER1);
    assertThat(wallMove.type()).isEqualTo(Move.Type.WALL);
    assertThat(wallMove.wall()).isEqualTo(wall);
    assertThrows(wallMove::destination);
  }
}
