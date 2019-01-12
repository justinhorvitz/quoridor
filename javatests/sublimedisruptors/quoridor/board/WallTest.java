package sublimedisruptors.quoridor.board;

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
    Wall wall = new Wall(Groove.vertical('c', 3), /*length=*/ 2);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves).containsExactly(Groove.vertical('c', 3), Groove.vertical('c', 4));
  }

  @Test
  public void coveredGrooves_length2_horizontal() {
    Wall wall = new Wall(Groove.horizontal('c', 3), /*length=*/ 2);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(Groove.horizontal('c', 3), Groove.horizontal('d', 3));
  }

  @Test
  public void coveredGrooves_length3_vertical() {
    Wall wall = new Wall(Groove.vertical('c', 3), /*length=*/ 3);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(Groove.vertical('c', 3), Groove.vertical('c', 4), Groove.vertical('c', 5));
  }

  @Test
  public void coveredGrooves_length3_horizontal() {
    Wall wall = new Wall(Groove.horizontal('c', 3), /*length=*/ 3);
    List<Groove> coveredGrooves = wall.coveredGrooves();
    assertThat(coveredGrooves)
        .containsExactly(
            Groove.horizontal('c', 3), Groove.horizontal('d', 3), Groove.horizontal('e', 3));
  }

  @Test
  public void coveredVertices_length2_vertical() {
    Wall wall = new Wall(Groove.vertical('c', 3), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length2_horizontal() {
    Wall wall = new Wall(Groove.horizontal('c', 3), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length3_vertical() {
    Wall wall = new Wall(Groove.vertical('c', 3), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('c', 4));
  }

  @Test
  public void coveredVertices_length3_horizontal() {
    Wall wall = new Wall(Groove.horizontal('c', 3), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('d', 3));
  }
}
