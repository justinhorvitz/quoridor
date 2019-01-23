package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Configurable settings for a game of Quoridor.
 *
 * <p>Default settings may be obtained by calling {@link #defaultTwoPlayer} or {@link
 * #defaultFourPlayer}.
 *
 * <p>Custom settings may be constructed via the {@link Builder}. Settings are validated when
 * calling {@link Builder#build}. See documentation for each of the builder's setter methods for
 * details on what is required for validity.
 *
 * <p>To guarantee validity, classes that depend on one or more of these settings should consider
 * having an initializer that accepts a {@code QuoridorSettings} object as opposed to accepting the
 * settings as arguments directly.
 */
@AutoValue
public abstract class QuoridorSettings {

  /** Creates default settings for a two-player game. */
  public static QuoridorSettings defaultTwoPlayer() {
    return builderWithCommonDefaults()
        .setPlayers(Player.PLAYER1, Player.PLAYER2)
        .setWallsPerPlayer(8)
        .build();
  }

  /** Creates default settings for a four-player game. */
  public static QuoridorSettings defaultFourPlayer() {
    return builderWithCommonDefaults()
        .setPlayers(Player.PLAYER1, Player.PLAYER2, Player.PLAYER3, Player.PLAYER4)
        .setWallsPerPlayer(4)
        .build();
  }

  private static Builder builderWithCommonDefaults() {
    return builder().setBoardSize(9).setWallLength(2);
  }

  public static Builder builder() {
    return new AutoValue_QuoridorSettings.Builder();
  }

  /** The number of rows and columns in the board. */
  public abstract int boardSize();

  /**
   * The players participating in the game.
   *
   * <p>The order of this list represents the order in which players will take turns making moves.
   */
  public abstract ImmutableList<Player> players();

  /**
   * The number of {@linkplain sublimedisruptors.quoridor.board.Wall walls} each player has at the
   * beginning of the game.
   */
  public abstract int wallsPerPlayer();

  /**
   * The length of each {@link sublimedisruptors.quoridor.board.Wall}, in terms of the number of
   * {@linkplain sublimedisruptors.quoridor.board.Groove grooves} it covers.
   */
  public abstract int wallLength();

  /** Converts these settings to a {@link Builder} with the same settings. */
  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Sets the {@linkplain #boardSize size} of the board.
     *
     * <p>The size must be at least 3, and it must be odd so that starting positions can be in a
     * middle column or row.
     */
    public abstract Builder setBoardSize(int boardSize);

    /**
     * Sets the players participating in the game, in the order in which they will take turns.
     *
     * <p>Quoridor supports 2-4 players. The {@code players} parameter must not contain {@code
     * null}, nor duplicate players.
     */
    public abstract Builder setPlayers(Player... players);

    /**
     * Sets the number of walls allocated to each player at the beginning of the game.
     *
     * <p>This value must not be negative. It may be 0, in which case the game will be played
     * without walls.
     */
    public abstract Builder setWallsPerPlayer(int wallsPerPlayer);

    /**
     * Sets the {@linkplain #wallLength length} of each {@link
     * sublimedisruptors.quoridor.board.Wall}.
     *
     * <p>This value must be at least 1.
     */
    public abstract Builder setWallLength(int wallLength);

    /**
     * Builds the settings.
     *
     * <p>This method performs several validity checks. It throws {@link IllegalStateException} if
     * any setting is invalid.
     */
    public final QuoridorSettings build() {
      QuoridorSettings settings = autoBuild();
      validateBoardSize(settings.boardSize());
      validatePlayers(settings.players());
      validateWallsPerPlayer(settings.wallsPerPlayer());
      validateWallLength(settings.wallLength());
      return settings;
    }

    private static void validateBoardSize(int boardSize) {
      checkState(boardSize >= 3, "Board size must be at least 3, got %s", boardSize);
      checkState(boardSize % 2 != 0, "Board size must be odd, got %s", boardSize);
    }

    private static void validatePlayers(ImmutableList<Player> players) {
      checkState(players.size() >= 2, "Need at least two players, got %s", players);
      checkState(players.size() <= 4, "Maximum of four players, got %s", players);
      checkState(
          players.stream().distinct().count() == players.size(), "Duplicate player: %s", players);
    }

    private static void validateWallsPerPlayer(int wallsPerPlayer) {
      checkState(
          wallsPerPlayer >= 0, "Walls per player must be non-negative, got %s", wallsPerPlayer);
    }

    private static void validateWallLength(int wallLength) {
      checkState(wallLength >= 1, "Wall length must be at least 1, got %s", wallLength);
    }

    abstract QuoridorSettings autoBuild();
  }
}
