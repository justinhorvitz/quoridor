package sublimedisruptors.quoridor.testing;

import static com.google.common.truth.Fact.simpleFact;
import static com.google.common.truth.Truth.assertAbout;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import sublimedisruptors.quoridor.Locatable;

/** {@link Truth} extension for making assertions against {@link Locatable} objects. */
public final class LocatableSubject extends Subject<LocatableSubject, Locatable> {

  public static LocatableSubject assertThat(Locatable locatable) {
    return assertAbout(LocatableSubject::new).that(locatable);
  }

  private LocatableSubject(FailureMetadata failureMetadata, Locatable subject) {
    super(failureMetadata, subject);
  }

  public void isLocatedAt(char column, int row) {
    if (actual().column() != column || actual().row() != row) {
      failWithActual(simpleFact("expected to be located at " + column + row));
    }
  }
}
