package hu.zza.hyperskill.hypermetro.manager;

public abstract class StationManagerException extends RuntimeException {

  public StationManagerException() {}

  public StationManagerException(String message) {
    super(message);
  }

  public StationManagerException(String message, Throwable cause) {
    super(message, cause);
  }

  public StationManagerException(Throwable cause) {
    super(cause);
  }

  public StationManagerException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
