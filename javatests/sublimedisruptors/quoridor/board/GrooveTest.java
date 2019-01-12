package sublimedisruptors.quoridor.board;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.LocatableSubject.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.board.Groove.Orientation;

/** Tests for {@link Groove}. */
@RunWith(JUnit4.class)
public final class GrooveTest {

  @Test
  public void vertical() {
    Groove groove = Groove.vertical('c', 3);
    assertThat(groove).isLocatedAt('c', 3);
    assertThat(groove.orientation()).isEqualTo(Orientation.VERTICAL);
  }

  @Test
  public void horizontal() {
    Groove groove = Groove.horizontal('c', 3);
    assertThat(groove).isLocatedAt('c', 3);
    assertThat(groove.orientation()).isEqualTo(Orientation.HORIZONTAL);
  }
}

