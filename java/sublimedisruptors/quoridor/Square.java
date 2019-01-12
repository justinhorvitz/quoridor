package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.auto.value.AutoValue;
import sublimedisruptors.quoridor.Groove.Orientation;

/**
 * An individual square on the board.
 *
 * <p>A {@code Square} is bordered by a {@link Groove} and another {@code Square} in each {@link
 * Direction}.
 */
@AutoValue
public abstract class Square implements Locatable {

  public static Square at(char column, int row) {
    return new AutoValue_Square(column, row);
  }

  /** Returns the {@link Groove} that borders this square in the given {@link Direction}. */
  public final Groove borderingGroove(Direction direction) {
    checkNotNull(direction);
    Locatable grooveLocation =
        direction == Direction.UP || direction == Direction.LEFT ? direction.apply(this) : this;
    Orientation grooveOrientation =
        direction == Direction.UP || direction == Direction.DOWN
            ? Orientation.HORIZONTAL
            : Orientation.VERTICAL;
    return Groove.groove(grooveLocation.column(), grooveLocation.row(), grooveOrientation);
  }

  /** Returns the adjacent {@link Square} in the given {@link Direction}. */
  public final Square adjacentSquare(Direction direction) {
    Vertex adjacent = direction.apply(this);
    return Square.at(adjacent.column(), adjacent.row());
  }
}
