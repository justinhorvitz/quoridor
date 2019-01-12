package sublimedisruptors.quoridor;

import static sublimedisruptors.quoridor.testing.LocatableSubject.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link Vertex}. */
@RunWith(JUnit4.class)
public final class VertexTest {

  @Test
  public void at() {
    Vertex vertex = Vertex.at('c', 3);
    assertThat(vertex).isLocatedAt('c', 3);
  }
}

