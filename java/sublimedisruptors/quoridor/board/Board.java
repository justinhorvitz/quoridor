package sublimedisruptors.quoridor.board;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;

/**
 * A Quoridor game board.
 *
 * <p>The state of the board is fully observable by taking a {@link #snapshot} of its current state.
 * The board is only mutable via {@link #movePawn} and {@link #placeWall}.
 *
 * <p>This class is oblivious to the "rules of the game". It accepts moves unconditionally,
 * performing no validity checks (including boundary checks). Accounting is performed to track how
 * many walls a player has available, however, a player is <em>not</em> prohibited from placing a
 * wall even if {@link Snapshot#wallsAvailable} indicates that the player has zero walls available.
 *
 * <p>A {@code Board} instance can be obtained via {@link #createFromSettings}. The board always
 * starts in an empty state. Pawns and walls must be added via the board's mutating methods.
 */
public final class Board {

  /** Creates a board in accordance with the given {@code settings}. */
  public static Board createFromSettings(QuoridorSettings settings) {
    Map<Player, Integer> wallsAvailable = new EnumMap<>(Player.class);
    settings.players().forEach(player -> wallsAvailable.put(player, settings.wallsPerPlayer()));
    return new Board(
        settings.boardSize(),
        /*pawns=*/ new EnumMap<>(Player.class),
        wallsAvailable,
        /*walledOffGrooves=*/ new HashSet<>(),
        /*walledOffVertices=*/ new HashSet<>(),
        /*currentSnapshot=*/ null);
  }

  /**
   * Creates a board in the same state as the given {@code snapshot}.
   *
   * <p>This method provides a way to obtain a deep copy of a board, such that future mutations to
   * one board will not be reflected in the other.
   */
  public static Board fromSnapshot(Snapshot snapshot) {
    return new Board(
        snapshot.size(),
        new EnumMap<>(snapshot.pawns()),
        new HashMap<>(snapshot.wallsAvailable()),
        new HashSet<>(snapshot.walledOffGrooves()),
        new HashSet<>(snapshot.walledOffVertices()),
        snapshot);
  }

  private final int size;
  private final Map<Player, Square> pawns;
  private final Map<Player, Integer> wallsAvailable;
  private final Set<Groove> walledOffGrooves;
  private final Set<Vertex> walledOffVertices;
  @Nullable private Snapshot currentSnapshot;

  private Board(
      int size,
      Map<Player, Square> pawns,
      Map<Player, Integer> wallsAvailable,
      Set<Groove> walledOffGrooves,
      Set<Vertex> walledOffVertices,
      @Nullable Snapshot currentSnapshot) {
    this.size = size;
    this.pawns = pawns;
    this.wallsAvailable = wallsAvailable;
    this.walledOffGrooves = walledOffGrooves;
    this.walledOffVertices = walledOffVertices;
    this.currentSnapshot = currentSnapshot;
  }

  /** Returns the number of rows and columns on this board. */
  public int size() {
    return size;
  }

  /** Returns an immutable {@link Snapshot} of the board's current state. */
  public Snapshot snapshot() {
    if (currentSnapshot == null) {
      currentSnapshot =
          new AutoValue_Board_Snapshot.Builder()
              .setSize(size)
              .setPawns(pawns)
              .setWallsAvailable(wallsAvailable)
              .setWalledOffGrooves(walledOffGrooves)
              .setWalledOffVertices(walledOffVertices)
              .build();
    }
    return currentSnapshot;
  }

  /**
   * Moves the given player's pawn to {@code square}.
   *
   * <p>The player's pawn need not already be on this board. It will be added if necessary.
   *
   * <p>No validation is performed to determine whether the move can legally be made.
   */
  public void movePawn(Player player, Square square) {
    currentSnapshot = null;
    pawns.put(player, checkNotNull(square));
  }

  /**
   * Places a wall to this board.
   *
   * <p>One wall is subtracted from the given player's remaining {@linkplain Snapshot#wallsAvailable
   * available walls}.
   *
   * <p>No validation is performed to determine whether the wall can legally be placed.
   */
  public void placeWall(Wall wall, Player player) {
    currentSnapshot = null;
    walledOffGrooves.addAll(wall.coveredGrooves());
    walledOffVertices.addAll(wall.coveredVertices());
    wallsAvailable.merge(player, -1, Integer::sum);
  }

  /** An immutable snapshot of a board at a given point in time. */
  @AutoValue
  public abstract static class Snapshot {

    /** Returns the number of rows and columns on this board. */
    public abstract int size();

    /** Returns a mapping of the positions of all pawns on this board. */
    public abstract ImmutableMap<Player, Square> pawns();

    /** Returns a map indicating how many walls each player has remaining. */
    public abstract ImmutableMap<Player, Integer> wallsAvailable();

    /** Returns the set of grooves that are walled off. */
    public abstract ImmutableSet<Groove> walledOffGrooves();

    /** Returns the set of vertices that are walled off. */
    public abstract ImmutableSet<Vertex> walledOffVertices();

    @AutoValue.Builder
    abstract static class Builder {
      abstract Builder setSize(int size);
      abstract Builder setPawns(Map<Player, Square> pawns);
      abstract Builder setWallsAvailable(Map<Player, Integer> wallsAvailable);
      abstract Builder setWalledOffGrooves(Set<Groove> walledOffGrooves);
      abstract Builder setWalledOffVertices(Set<Vertex> walledOffVertices);
      abstract Snapshot build();
    }
  }
}
