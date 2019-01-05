package sublimedisruptors.quoridor.testing;

/** Test utilities for Quoridor. */
public final class TestUtils {

  public static void assertThrows(Class<? extends RuntimeException> expected, Runnable runnable) {
    try {
      runnable.run();
      throw new AssertionError(String.format("no %s thrown", expected.getName()));
    } catch (RuntimeException e) {
      if (!expected.isInstance(e)) {
        throw e;
      }
    }
  }

  private TestUtils() {}
}
