package sublimedisruptors.quoridor.game;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.move.Move;
import sublimedisruptors.quoridor.move.RulesGovernor;
import sublimedisruptors.quoridor.player.QuoridorPlayer;

/** Plays a game of Quoridor. */
public final class GameMaster {

  /**
   * Creates a {@code GameMaster}.
   *
   * <p>The game master is designed to play a single game. A new instance should be constructed for
   * each new game, even if the {@code settings} and {@code playerFactories} do not change.
   *
   * <p>{@code playerFactories} must contain an entry for each player in {@link
   * QuoridorSettings#players}.
   */
  public static GameMaster setUpGame(
      QuoridorSettings settings, Map<Player, QuoridorPlayer.Factory> playerFactories) {
    Board board = Board.createFromSettings(settings);
    return new GameMaster(
        board,
        RulesGovernor.createAndSetUpPawns(board, settings),
        Iterators.cycle(settings.players()),
        createPlayerImpls(playerFactories, settings));
  }

  private static Map<Player, QuoridorPlayer> createPlayerImpls(
      Map<Player, QuoridorPlayer.Factory> playerFactories, QuoridorSettings settings) {
    Map<Player, QuoridorPlayer> playerImpls = new EnumMap<>(Player.class);
    for (Player player : settings.players()) {
      QuoridorPlayer.Factory factory =
          checkNotNull(playerFactories.get(player), "No factory for %s", player);
      QuoridorPlayer playerImpl =
          checkNotNull(
              factory.createPlayer(player, settings), "%s factory created a null player", player);
      playerImpls.put(player, playerImpl);
    }
    return playerImpls;
  }

  private final Board board;
  private final RulesGovernor governor;
  private final Iterator<Player> players;
  private final Map<Player, QuoridorPlayer> playerImpls;
  private boolean played = false;
  private Player winner = null;

  private GameMaster(
      Board board,
      RulesGovernor governor,
      Iterator<Player> players,
      Map<Player, QuoridorPlayer> playerImpls) {
    this.board = board;
    this.governor = governor;
    this.players = players;
    this.playerImpls = playerImpls;
  }

  /**
   * Plays the game.
   *
   * <p>It is an error to call this method twice on the same {@code GameMaster} instance.
   *
   * <p>If any player requests an illegal move, this method throws {@link IllegalMoveException} and
   * there will be no winner.
   */
  public void playGame() {
    checkState(!played, "Game already played");
    played = true;
    Player lastPlayerToMove = null;
    Square lastPawnMove = null;
    while (!gameOver(lastPlayerToMove, lastPawnMove)) {
      Player player = players.next();
      ImmutableSet<Move> validPawnMoves = governor.generateValidPawnMoves(player);
      Move move = playerImpls.get(player).getMove(board.snapshot(), validPawnMoves);
      checkLegalMove(move, mv -> mv.player() == player, player);
      if (move.type() == Move.Type.PAWN) {
        checkLegalMove(move, validPawnMoves::contains, player);
        Square destination = move.destination();
        board.movePawn(player, destination);
        lastPawnMove = destination;
      } else {
        checkLegalMove(move, governor::isValidWallMove, player);
        board.placeWall(move.wall(), player);
        lastPawnMove = null;
      }
      lastPlayerToMove = player;
    }
    winner = lastPlayerToMove;
  }

  /**
   * Returns the winner of this game.
   *
   * <p>This method must only be called after {@link #playGame}.
   */
  public Player getWinner() {
    checkState(played && winner != null, "Game not played to completion");
    return winner;
  }

  private boolean gameOver(Player lastPlayerToMove, Square lastPawnMove) {
    return lastPawnMove != null && governor.isGoal(lastPlayerToMove, lastPawnMove);
  }

  private void checkLegalMove(Move move, Predicate<Move> predicate, Player player) {
    if (!predicate.test(move)) {
      throw new IllegalMoveException(move, player, board.snapshot());
    }
  }
}
