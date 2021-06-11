package hu.zza.hyperskill.hypermetro.manager;

public final class StationUpdateException extends StationManagerException {

  public StationUpdateException() {}

  public StationUpdateException(String message) {
    super(message);
  }

  public StationUpdateException(String message, Throwable cause) {
    super(message, cause);
  }

  public StationUpdateException(Throwable cause) {
    super(cause);
  }

  public StationUpdateException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
