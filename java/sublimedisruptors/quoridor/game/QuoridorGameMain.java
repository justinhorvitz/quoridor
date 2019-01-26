package sublimedisruptors.quoridor.game;

import com.google.common.collect.ImmutableMap;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.player.CommandLineQuoridorPlayer;

public final class QuoridorGameMain {

  public static void main(String[] args) {
    GameMaster gameMaster =
        GameMaster.setUpGame(
            QuoridorSettings.defaultTwoPlayer(),
            ImmutableMap.of(
                Player.PLAYER1,
                CommandLineQuoridorPlayer::create,
                Player.PLAYER2,
                CommandLineQuoridorPlayer::create));
    gameMaster.playGame();
    System.out.println(gameMaster.getWinner() + " wins!");
  }
}
