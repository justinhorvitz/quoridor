package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Groove.Orientation;

/** Tests for {@link Groove}. */
@RunWith(JUnit4.class)
public final class GrooveTest {

  @Test
  public void vertical() {
    Groove groove = Groove.vertical(Vertex.at('c', 3));
    assertThat(groove.vertex()).isEqualTo(Vertex.at('c', 3));
    assertThat(groove.orientation()).isEqualTo(Orientation.VERTICAL);
  }

  @Test
  public void horizontal() {
    Groove groove = Groove.horizontal(Vertex.at('c', 3));
    assertThat(groove.vertex()).isEqualTo(Vertex.at('c', 3));
    assertThat(groove.orientation()).isEqualTo(Orientation.HORIZONTAL);
  }
}

