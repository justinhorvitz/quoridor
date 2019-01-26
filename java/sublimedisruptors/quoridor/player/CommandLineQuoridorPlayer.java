package sublimedisruptors.quoridor.player;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import sublimedisruptors.quoridor.Player;
import sublimedisruptors.quoridor.QuoridorSettings;
import sublimedisruptors.quoridor.board.Board;
import sublimedisruptors.quoridor.board.Groove;
import sublimedisruptors.quoridor.board.Square;
import sublimedisruptors.quoridor.board.Wall;
import sublimedisruptors.quoridor.move.Move;

/**
 * A {@link QuoridorPlayer} that takes its moves from the command line.
 *
 * <p>When it is this player's turn, the board is printed to the console along with a prompt for
 * which move to make. A human is expected to enter the desired move in <a
 * href="https://en.wikipedia.org/wiki/Quoridor#Notation">Glendenning notation</a>.
 */
public final class CommandLineQuoridorPlayer implements QuoridorPlayer {

  public static CommandLineQuoridorPlayer create(Player self, QuoridorSettings settings) {
    return new CommandLineQuoridorPlayer(self, settings.wallLength());
  }

  private static final Scanner scanner = new Scanner(System.in);

  private final Player me;
  private final int wallLength;

  private CommandLineQuoridorPlayer(Player me, int wallLength) {
    this.me = me;
    this.wallLength = wallLength;
  }

  @Override
  public Move getMove(Board.Snapshot board, ImmutableSet<Move> validPawnMoves) {
    printBoard(board);
    promptForMove(me);
    return scanMove(me);
  }

  private static void printBoard(Board.Snapshot board) {
    printColumnHeaders(board.size());
    printTopBorder(board.size(), board.walledOffGrooves());
    Map<Square, Player> reversePawns =
        board.pawns().entrySet().stream().collect(toImmutableMap(Entry::getValue, Entry::getKey));
    for (int row = 1; row <= board.size(); row++) {
      printRow(row, board.size(), reversePawns, board.walledOffGrooves());
    }
    printColumnHeaders(board.size());
    printWallsAvailable(board.wallsAvailable());
  }

  private static void promptForMove(Player player) {
    System.out.format("Enter move for %s --> ", player);
  }

  private Move scanMove(Player player) {
    String input = scanner.nextLine();
    if (input.length() < 2 || input.length() > 3) {
      throw invalidMove(input);
    }
    char column = input.charAt(0);
    int row = Character.getNumericValue(input.charAt(1));
    if (input.length() == 2) {
      return Move.pawnMove(player, Square.at(column, row));
    } else {
      char orientation = input.charAt(2);
      Wall wall;
      if (orientation == 'v') {
        wall = Wall.vertical(column, row).withLength(wallLength);
      } else if (orientation == 'h') {
        wall = Wall.horizontal(column, row).withLength(wallLength);
      } else {
        throw invalidMove(input);
      }
      return Move.wallMove(player, wall);
    }
  }

  private static IllegalArgumentException invalidMove(String input) {
    return new IllegalArgumentException("Invalid move: " + input);
  }

  private static void printColumnHeaders(int boardSize) {
    print(' ', 4);
    forEachColumn(
        boardSize,
        column -> {
          print(column);
          print(' ', 3);
        });
    print('\n');
  }

  private static void printWallsAvailable(Map<Player, Integer> wallsAvailable) {
    System.out.println("Walls available:");
    for (Map.Entry entry : wallsAvailable.entrySet()) {
      System.out.format("  %s: %s\n", entry.getKey(), entry.getValue());
    }
  }

  private static void printTopBorder(int boardSize, Set<Groove> walledOffGrooves) {
    print(' ', 2);
    print('-');
    forEachColumn(
        boardSize,
        column -> {
          print('-', 3);
          if (walledOffGrooves.contains(Groove.vertical(column, 1))) {
            print('+');
          } else {
            print('-');
          }
        });
    print('\n');
  }

  private static void printRow(
      int row, int boardSize, Map<Square, Player> pawns, Set<Groove> walledOffGrooves) {
    print(row);
    print(' ');
    print('|');
    forEachColumn(
        boardSize,
        column -> {
          print(' ');
          Player player = pawns.get(Square.at(column, row));
          if (player == null) {
            print(' ');
          } else {
            print(player.toString().charAt(player.toString().length() - 1));
          }
          print(' ');
          if (walledOffGrooves.contains(Groove.vertical(column, row))) {
            print('+');
          } else {
            print('|');
          }
        });
    print('\n');
    print(' ', 2);
    AtomicBoolean previousColumnHadBottomBorderWall = new AtomicBoolean(false);
    forEachColumn(
        boardSize,
        column -> {
          if (walledOffGrooves.contains(Groove.horizontal(column, row))) {
            print('+', 4);
            previousColumnHadBottomBorderWall.set(true);
          } else {
            if (previousColumnHadBottomBorderWall.getAndSet(false)
                || walledOffGrooves.contains(Groove.vertical((char) (column - 1), row))
                || walledOffGrooves.contains(Groove.vertical((char) (column - 1), row + 1))) {
              print('+');
            } else {
              print('-');
            }
            print('-', 3);
          }
        });
    if (previousColumnHadBottomBorderWall.get()) {
      print('+');
    } else {
      print('-');
    }
    print('\n');
  }

  private static void forEachColumn(int boardSize, Consumer<Character> columnConsumer) {
    for (char column = 'a'; column < 'a' + boardSize; column++) {
      columnConsumer.accept(column);
    }
  }

  private static void print(char charToPrint, int numTimes) {
    for (int i = 0; i < numTimes; i++) {
      print(charToPrint);
    }
  }

  private static void print(char charToPrint) {
    System.out.print(charToPrint);
  }

  private static void print(int intToPrint) {
    System.out.print(intToPrint);
  }
}
