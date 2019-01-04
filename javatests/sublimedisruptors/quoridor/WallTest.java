package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import org.junit.Test;

/** Tests for {@link Wall}. */
public final class WallTest {

  @Test
  public void coveredEdges_length2_vertical() {
    Wall wall = new Wall(Edge.vertical(Vertex.at('c', 3)), /*length=*/ 2);
    List<Edge> coveredEdges = wall.coveredEdges();
    assertThat(coveredEdges)
        .containsExactly(Edge.vertical(Vertex.at('c', 3)), Edge.vertical(Vertex.at('c', 4)));
  }

  @Test
  public void coveredEdges_length2_horizontal() {
    Wall wall = new Wall(Edge.horizontal(Vertex.at('c', 3)), /*length=*/ 2);
    List<Edge> coveredEdges = wall.coveredEdges();
    assertThat(coveredEdges)
        .containsExactly(Edge.horizontal(Vertex.at('c', 3)), Edge.horizontal(Vertex.at('d', 3)));
  }

  @Test
  public void coveredEdges_length3_vertical() {
    Wall wall = new Wall(Edge.vertical(Vertex.at('c', 3)), /*length=*/ 3);
    List<Edge> coveredEdges = wall.coveredEdges();
    assertThat(coveredEdges)
        .containsExactly(
            Edge.vertical(Vertex.at('c', 3)),
            Edge.vertical(Vertex.at('c', 4)),
            Edge.vertical(Vertex.at('c', 5)));
  }

  @Test
  public void coveredEdges_length3_horizontal() {
    Wall wall = new Wall(Edge.horizontal(Vertex.at('c', 3)), /*length=*/ 3);
    List<Edge> coveredEdges = wall.coveredEdges();
    assertThat(coveredEdges)
        .containsExactly(
            Edge.horizontal(Vertex.at('c', 3)),
            Edge.horizontal(Vertex.at('d', 3)),
            Edge.horizontal(Vertex.at('e', 3)));
  }

  @Test
  public void coveredVertices_length2_vertical() {
    Wall wall = new Wall(Edge.vertical(Vertex.at('c', 3)), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length2_horizontal() {
    Wall wall = new Wall(Edge.horizontal(Vertex.at('c', 3)), /*length=*/ 2);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3));
  }

  @Test
  public void coveredVertices_length3_vertical() {
    Wall wall = new Wall(Edge.vertical(Vertex.at('c', 3)), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('c', 4));
  }

  @Test
  public void coveredVertices_length3_horizontal() {
    Wall wall = new Wall(Edge.horizontal(Vertex.at('c', 3)), /*length=*/ 3);
    List<Vertex> coveredVertices = wall.coveredVertices();
    assertThat(coveredVertices).containsExactly(Vertex.at('c', 3), Vertex.at('d', 3));
  }
}
