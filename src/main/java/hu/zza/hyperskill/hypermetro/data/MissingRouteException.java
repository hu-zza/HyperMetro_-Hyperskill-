package hu.zza.hyperskill.hypermetro.data;

public class MissingRouteException extends RuntimeException {
  public MissingRouteException() {
  }

  public MissingRouteException(String message) {
    super(message);
  }

  public MissingRouteException(String message, Throwable cause) {
    super(message, cause);
  }

  public MissingRouteException(Throwable cause) {
    super(cause);
  }
}
