package sublimedisruptors.quoridor.game;

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
import sublimedisruptors.quoridor.player.CommandLineQuoridorPlayer;
import sublimedisruptors.quoridor.player.QuoridorPlayer;
import sublimedisruptors.quoridor.player.RandomQuoridorPlayer;

public final class GameMaster {

  public static GameMaster createFromSettings(QuoridorSettings settings) {
    Board board = Board.createFromSettings(settings);
    Map<Player, QuoridorPlayer> playerImpls = new EnumMap<>(Player.class);
    playerImpls.put(Player.PLAYER1, new CommandLineQuoridorPlayer());
    playerImpls.put(Player.PLAYER2, new RandomQuoridorPlayer());
    playerImpls.entrySet().forEach(entry -> entry.getValue().setUp(settings, entry.getKey()));
    return new GameMaster(board, RulesGovernor.createAndSetUpPawns(board, settings), Iterators.cycle(settings.players()), playerImpls);
  }

  private final Board board;
  private final RulesGovernor governor;
  private final Iterator<Player> players;
  private final Map<Player, QuoridorPlayer> playerImpls;
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

  public void playGame() {
    checkState(winner == null, "Game already played");
    Player lastPlayerToMove = null;
    Square lastPawnMove = null;
    while (!gameOver(lastPlayerToMove, lastPawnMove)) {
      Player player = players.next();
      ImmutableSet<Move> validPawnMoves = governor.generateValidPawnMoves(player);
      Move move = playerImpls.get(player).getMove(board.snapshot(), validPawnMoves);
      checkLegalMove(move, mv -> mv.player() == player);
      if (move.type() == Move.Type.PAWN) {
        checkLegalMove(move, validPawnMoves::contains);
        Square destination = move.destination();
        board.movePawn(player, destination);
        lastPawnMove = destination;
      } else {
        checkLegalMove(move, governor::isValidWallMove);
        board.placeWall(move.wall(), player);
        lastPawnMove = null;
      }
      lastPlayerToMove = player;
    }
    winner = lastPlayerToMove;
  }

  public Player winner() {
    checkState(winner != null, "Game not yet played");
    return winner;
  }

  private boolean gameOver(Player lastPlayerToMove, Square lastPawnMove) {
    return lastPawnMove != null && governor.isGoal(lastPlayerToMove, lastPawnMove);
  }

  private static void checkLegalMove(Move move, Predicate<Move> predicate) {
    if (!predicate.test(move)) {
      throw new IllegalArgumentException("Illegal move: " + move);
    }
  }
}
