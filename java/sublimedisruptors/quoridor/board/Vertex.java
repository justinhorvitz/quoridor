package sublimedisruptors.quoridor.board;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

/** The intersection of a distinct row and column on a quoridor board. */
@AutoValue
public abstract class Vertex implements Locatable {

  private static final Interner<Vertex> interner = Interners.newStrongInterner();

  static Vertex at(char column, int row) {
    return interner.intern(new AutoValue_Vertex(column, row));
  }
}
