package hu.zza.hyperskill.hypermetro.manager;

public final class DatasourceUnreachableException extends StationManagerException {

  public DatasourceUnreachableException() {}

  public DatasourceUnreachableException(String message) {
    super(message);
  }

  public DatasourceUnreachableException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatasourceUnreachableException(Throwable cause) {
    super(cause);
  }

  public DatasourceUnreachableException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
