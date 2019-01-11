package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link Wall}. */
@RunWith(JUnit4.class)
public final class WallTest {

  @Test
  public void coveredGrooves_length2_vertical() {
    Wall wall = new Wall(Groove.vertical(Vertex.at('c', 3)), /*length=*/ 2);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(Groove.vertical(Vertex.at('c', 3)), Groove.vertical(Vertex.at('c', 4)));
  }

  @Test
  public void coveredGrooves_length2_horizontal() {
    Wall wall = new Wall(Groove.horizontal(Vertex.at('c', 3)), /*length=*/ 2);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(
            Groove.horizontal(Vertex.at('c', 3)), Groove.horizontal(Vertex.at('d', 3)));
  }

  @Test
  public void coveredGrooves_length3_vertical() {
    Wall wall = new Wall(Groove.vertical(Vertex.at('c', 3)), /*length=*/ 3);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(
            Groove.vertical(Vertex.at('c', 3)),
            Groove.vertical(Vertex.at('c', 4)),
            Groove.vertical(Vertex.at('c', 5)));
  }

  @Test
  public void coveredGrooves_length3_horizontal() {
    Wall wall = new Wall(Groove.horizontal(Vertex.at('c', 3)), /*length=*/ 3);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(
            Groove.horizontal(Vertex.at('c', 3)),
            Groove.horizontal(Vertex.at('d', 3)),
            Groove.horizontal(Vertex.at('e', 3)));
  }

  @Test
  public void coveredVertices_length2_vertical() {
    Wall wall = new Wall(Groove.vertical(Vertex.at('c', 3)), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length2_horizontal() {
    Wall wall = new Wall(Groove.horizontal(Vertex.at('c', 3)), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length3_vertical() {
    Wall wall = new Wall(Groove.vertical(Vertex.at('c', 3)), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('c', 4));
  }

  @Test
  public void coveredVertices_length3_horizontal() {
    Wall wall = new Wall(Groove.horizontal(Vertex.at('c', 3)), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('d', 3));
  }
}
