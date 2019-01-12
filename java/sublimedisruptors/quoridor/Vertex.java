package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/** The intersection of a distinct row and column on a quoridor board. */
@AutoValue
public abstract class Vertex implements Locatable {

  static Vertex at(Locatable locatable) {
    return at(locatable.column(), locatable.row());
  }

  static Vertex at(char column, int row) {
    return new AutoValue_Vertex(column, row);
  }
}
