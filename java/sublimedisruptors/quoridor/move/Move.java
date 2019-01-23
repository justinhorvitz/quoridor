package sublimedisruptors.quoridor.move;

import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;

/**
 * A single move in a game of Quoridor.
 *
 * <p>A move can either be a {@linkplain Type#PAWN pawn move}, indicating that a pawn is to be moved
 * to a particular {@link Square}, or a {@linkplain Type#WALL wall move}, indicating that a {@link
 * Wall} is to be added to the board.
 *
 * <p>Each move is associated with the {@link Player} responsible for making it. In the case of a
 * pawn move, this also indicates which player's pawn is to be moved.
 */
@AutoValue
public abstract class Move {

  /** Creates a move indicating that {@code player} is moving their pawn to {@code destination}. */
  public static Move pawnMove(Player player, Square destination) {
    return new AutoValue_Move(player, AutoOneOf_Move_PawnOrWall.pawn(destination));
  }

  /** Creates a move indicating that {@code player} is placing {@code wall} on the board. */
  public static Move wallMove(Player player, Wall wall) {
    return new AutoValue_Move(player, AutoOneOf_Move_PawnOrWall.wall(wall));
  }

  /** The two possible types of moves in Quoridor. */
  public enum Type {
    /** A pawn is to be moved to a different {@link Square}. */
    PAWN,
    /** A {@link Wall} is to be placed on the board. */
    WALL
  }

  /**
   * Returns the {@link Player} responsible for this move.
   *
   * <p>If this is a {@linkplain Type#PAWN pawn move}, this also represents the player whose pawn is
   * to be moved.
   */
  public abstract Player player();

  /** Returns the {@link Type} of this move. */
  public final Type type() {
    return pawnOrWall().type();
  }

  /**
   * If this is a {@linkplain Type#PAWN pawn move}, returns the {@link Square} that the pawn is to
   * be moved to.
   *
   * <p>This method throws {@link UnsupportedOperationException} if this is a {@linkplain Type#WALL
   * wall move}.
   */
  public final Square destination() {
    if (type() != Type.PAWN) {
      throw new UnsupportedOperationException("Not a pawn move");
    }
    return pawnOrWall().pawn();
  }

  /**
   * If this is a {@linkplain Type#WALL wall move}, returns the {@link Wall} that is to be placed on
   * the board.
   *
   * <p>This method throws {@link UnsupportedOperationException} if this is a {@linkplain Type#PAWN
   * pawn move}.
   */
  public final Wall wall() {
    if (type() != Type.WALL) {
      throw new UnsupportedOperationException("Not a wall move");
    }
    return pawnOrWall().wall();
  }

  abstract PawnOrWall pawnOrWall();

  @AutoOneOf(Type.class)
  abstract static class PawnOrWall {
    abstract Type type();
    abstract Square pawn();
    abstract Wall wall();
  }
}
