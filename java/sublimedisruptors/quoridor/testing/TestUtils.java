package sublimedisruptors.quoridor.testing;

import com.google.common.truth.Subject;
import sublimedisruptors.quoridor.Locatable;

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

  public static final class LocatableSubject extends Subject<LocatableSubject, Locatable> {

    protected LocatableSubject(Locatable actual) {
      super(null, actual);
    }
  }

  private TestUtils() {}
}
