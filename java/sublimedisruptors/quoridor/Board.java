package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Quoridor game board.
 *
 * <p>The state of the board is fully observable through its {@link #size()} and its four getter
 * methods: {@link #getPawns}, {@link #getWallsAvailable}, {@link #getWalledOffEdges}, and {@link
 * #getWalledOffVertices}. These methods, however, all return <em>immutable</em> views of the board
 * - the board is only mutable via {@link #movePawn} and {@link #placeWall}.
 *
 * <p>This class is oblivious to the "rules of the game". It accepts moves unconditionally,
 * performing no validity checks (including boundary checks). Accounting is performed to track how
 * many walls a player has available, however, a player is <em>not</em> prohibited from placing a
 * wall even if {@link #getWallsAvailable} indicates that the player has zero walls available.
 *
 * <p>A {@code Board} instance can be obtained via {@link #createFromSettings}. The board always
 * starts in an empty state. Pawns and walls must be added via the board's mutating methods.
 */
public final class Board {

  /** Creates a board in accordance with the given {@code settings}. */
  public static Board createFromSettings(QuoridorSettings settings) {
    Map<Player, Integer> wallsAvailable = new EnumMap<>(Player.class);
    settings.players().forEach(player -> wallsAvailable.put(player, settings.wallsPerPlayer()));
    return new Board(settings.boardSize(), wallsAvailable);
  }

  private final int size;
  private final Map<Player, Integer> wallsAvailable;
  private final Map<Player, Square> pawns = new EnumMap<>(Player.class);
  private final Set<Edge> walledOffEdges = new HashSet<>();
  private final Set<Vertex> walledOffVertices = new HashSet<>();

  private Board(int size, Map<Player, Integer> wallsAvailable) {
    this.size = size;
    this.wallsAvailable = wallsAvailable;
  }

  /** Returns the number of rows and columns on this board. */
  public int size() {
    return size;
  }

  /**
   * Moves the given player's pawn to {@code square}.
   *
   * <p>The player's pawn need not already be on this board. It will be added if necessary.
   *
   * <p>No validation is performed to determine whether the move can legally be made.
   */
  public void movePawn(Player player, Square square) {
    pawns.put(player, checkNotNull(square));
  }

  /** Returns a mapping of the positions of all pawns on this board. */
  public ImmutableMap<Player, Square> getPawns() {
    return ImmutableMap.copyOf(pawns);
  }

  /**
   * Places a wall to this board.
   *
   * <p>One wall is subtracted from the given player's remaining {@linkplain #getWallsAvailable
   * available walls}.
   *
   * <p>No validation is performed to determine whether the wall can legally be placed.
   */
  public void placeWall(Wall wall, Player player) {
    walledOffEdges.addAll(wall.coveredEdges());
    walledOffVertices.addAll(wall.coveredVertices());
    wallsAvailable.merge(player, -1, Integer::sum);
  }

  /** Returns a map indicating how many walls each player has remaining. */
  public ImmutableMap<Player, Integer> getWallsAvailable() {
    return ImmutableMap.copyOf(wallsAvailable);
  }

  /** Returns the set of edges that are walled off. */
  public ImmutableSet<Edge> getWalledOffEdges() {
    return ImmutableSet.copyOf(walledOffEdges);
  }

  /** Returns the set of vertices that are walled off. */
  public ImmutableSet<Vertex> getWalledOffVertices() {
    return ImmutableSet.copyOf(walledOffVertices);
  }
}