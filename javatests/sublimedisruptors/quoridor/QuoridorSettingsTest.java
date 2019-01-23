package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link QuoridorSettings}. */
@RunWith(JUnit4.class)
public final class QuoridorSettingsTest {

  private final QuoridorSettings.Builder customSettings =
      QuoridorSettings.builder()
          .setBoardSize(3)
          .setPlayers(Player.PLAYER1, Player.PLAYER2)
          .setWallLength(2)
          .setWallsPerPlayer(1);

  @Test
  public void defaultTwoPlayer_valid() {
    QuoridorSettings settings = QuoridorSettings.defaultTwoPlayer();
    assertThat(settings).isNotNull();
  }

  @Test
  public void defaultFourPlayer_valid() {
    QuoridorSettings settings = QuoridorSettings.defaultFourPlayer();
    assertThat(settings).isNotNull();
  }

  @Test
  public void customSettings_valid() {
    QuoridorSettings settings = customSettings.build();
    assertThat(settings).isNotNull();
  }

  @Test
  public void boardSizeTooSmall_invalid() {
    customSettings.setBoardSize(1);
    assertThrows(customSettings::build);
  }

  @Test
  public void boardSizeEven_invalid() {
    customSettings.setBoardSize(8);
    assertThrows(customSettings::build);
  }

  @Test
  public void tooFewPlayers_invalid() {
    customSettings.setPlayers(Player.PLAYER1);
    assertThrows(customSettings::build);
  }

  @Test
  public void duplicatePlayer_invalid() {
    customSettings.setPlayers(Player.PLAYER1, Player.PLAYER2, Player.PLAYER2);
    assertThrows(customSettings::build);
  }

  @Test
  public void negativeWallsPerPlayer_invalid() {
    customSettings.setWallsPerPlayer(-1);
    assertThrows(customSettings::build);
  }

  @Test
  public void wallLengthZero_invalid() {
    customSettings.setWallLength(0);
    assertThrows(customSettings::build);
  }
}
