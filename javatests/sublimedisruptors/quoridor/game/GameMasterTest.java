package sublimedisruptors.quoridor.game;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Direction;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.move.Move;
import sublimedisruptors.quoridor.player.QuoridorPlayer;

/** Tests for {@link GameMaster}. */
@RunWith(JUnit4.class)
public final class GameMasterTest {

  @Test
  public void twoPlayerGame_player1Wins() {
    GameMaster gameMaster =
        GameMaster.setUpGame(
            QuoridorSettings.defaultTwoPlayer(),
            ImmutableMap.of(
                Player.PLAYER1, SmartPlayer.FACTORY, Player.PLAYER2, DumbPlayer.FACTORY));
    gameMaster.playGame();
    Player winner = gameMaster.getWinner();
    assertThat(winner).isEqualTo(Player.PLAYER1);
  }

  @Test
  public void twoPlayerGame_player2Wins() {
    GameMaster gameMaster =
        GameMaster.setUpGame(
            QuoridorSettings.defaultTwoPlayer(),
            ImmutableMap.of(
                Player.PLAYER1, DumbPlayer.FACTORY, Player.PLAYER2, SmartPlayer.FACTORY));
    gameMaster.playGame();
    Player winner = gameMaster.getWinner();
    assertThat(winner).isEqualTo(Player.PLAYER2);
  }

  @Test
  public void getWinnerBeforePlayingGame_throws() {
    GameMaster gameMaster =
        GameMaster.setUpGame(
            QuoridorSettings.defaultTwoPlayer(),
            ImmutableMap.of(
                Player.PLAYER1, DumbPlayer.FACTORY, Player.PLAYER2, SmartPlayer.FACTORY));
    assertThrows(gameMaster::getWinner);
  }

  @Test
  public void playGameTwice_throws() {
    GameMaster gameMaster =
        GameMaster.setUpGame(
            QuoridorSettings.defaultTwoPlayer(),
            ImmutableMap.of(
                Player.PLAYER1, DumbPlayer.FACTORY, Player.PLAYER2, SmartPlayer.FACTORY));
    gameMaster.playGame();
    assertThrows(gameMaster::playGame);
  }

  @Test
  public void missingPlayerFactory_throws() {
    assertThrows(
        () ->
            GameMaster.setUpGame(
                QuoridorSettings.defaultTwoPlayer(),
                ImmutableMap.of(Player.PLAYER1, DumbPlayer.FACTORY)));
  }

  @Test
  public void factoryReturnsNullPlayer_throws() {
    assertThrows(
        () ->
            GameMaster.setUpGame(
                QuoridorSettings.defaultTwoPlayer(),
                ImmutableMap.of(
                    Player.PLAYER1, DumbPlayer.FACTORY, Player.PLAYER2, (self, settings) -> null)));
  }

  /** A player that advances toward its goal. */
  private static final class SmartPlayer implements QuoridorPlayer {

    static final QuoridorPlayer.Factory FACTORY =
        (self, settings) -> {
          checkArgument(self == Player.PLAYER1 || self == Player.PLAYER2);
          return new SmartPlayer(self, self == Player.PLAYER1 ? Direction.UP : Direction.DOWN);
        };

    private final Player me;
    private final Direction goalDirection;

    private SmartPlayer(Player me, Direction goalDirection) {
      this.me = me;
      this.goalDirection = goalDirection;
    }

    @Override
    public Move getMove(Board.Snapshot board, ImmutableSet<Move> validPawnMoves) {
      Square desired = board.pawns().get(me).adjacentSquare(goalDirection);
      return validPawnMoves.stream()
          .filter(move -> move.destination().equals(desired))
          .findAny()
          .orElseThrow(() -> new IllegalStateException("Cannot move to " + desired));
    }
  }

  /** A player that buries itself in the corner. */
  private static final class DumbPlayer implements QuoridorPlayer {

    static final QuoridorPlayer.Factory FACTORY =
        (self, settings) -> {
          checkArgument(self == Player.PLAYER1 || self == Player.PLAYER2);
          return new DumbPlayer(self);
        };

    private final Player me;

    private DumbPlayer(Player me) {
      this.me = me;
    }

    @Override
    public Move getMove(Board.Snapshot board, ImmutableSet<Move> validPawnMoves) {
      Square current = board.pawns().get(me);
      Square desired = current.adjacentSquare(Direction.LEFT);
      Square backup = current.adjacentSquare(Direction.RIGHT);
      return validPawnMoves.stream()
          .filter(move -> move.destination().equals(desired))
          .findAny()
          .orElseGet(
              () ->
                  validPawnMoves.stream()
                      .filter(move -> move.destination().equals(backup))
                      .findAny()
                      .orElseThrow(
                          () -> new IllegalStateException("Neither left nor right available")));
    }
  }
}
