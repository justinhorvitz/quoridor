package sublimedisruptors.quoridor.testing;

/** Test utilities for Quoridor. */
public final class TestUtils {

  /** Asserts that {@code runnable} throws a {@link RuntimeException}. */
  public static void assertThrows(Runnable runnable) {
    try {
      runnable.run();
      throw new AssertionError(String.format("no exception thrown"));
    } catch (RuntimeException e) {
      // Expected.
    }
  }

  private TestUtils() {}
}
