package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Edge.Orientation;

/** Tests for {@link Edge}. */
@RunWith(JUnit4.class)
public final class EdgeTest {

  @Test
  public void vertical() {
    Edge edge = Edge.vertical(Vertex.at('c', 3));
    assertThat(edge.vertex()).isEqualTo(Vertex.at('c', 3));
    assertThat(edge.orientation()).isEqualTo(Orientation.VERTICAL);
  }

  @Test
  public void horizontal() {
    Edge edge = Edge.horizontal(Vertex.at('c', 3));
    assertThat(edge.vertex()).isEqualTo(Vertex.at('c', 3));
    assertThat(edge.orientation()).isEqualTo(Orientation.HORIZONTAL);
  }
}

