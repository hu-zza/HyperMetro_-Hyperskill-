package hu.zza.hyperskill.hypermetro.data;

import java.util.ArrayList;
import java.util.List;

public final class Station {
  private final Line line;
  private final String name;
  private int avgTimeToTransfer;
  private int timeToNext;
  private final List<Station> previousStations = new ArrayList<>();
  private final List<Station> nextStations = new ArrayList<>();
  private final List<Station> connectedStations = new ArrayList<>();

  Station(Line line, String name, int timeToNext) {
    this(line, name, timeToNext, 5, List.of(), List.of(), List.of());
  }

  Station(
      Line line,
      String name,
      int timeToNext,
      int avgTimeToTransfer,
      List<Station> previousStations,
      List<Station> nextStations,
      List<Station> connectedStations) {
    this.line = line;
    this.name = name;
    this.timeToNext = timeToNext;
    this.avgTimeToTransfer = avgTimeToTransfer;
    this.previousStations.addAll(previousStations);
    this.nextStations.addAll(nextStations);
    this.connectedStations.addAll(connectedStations);
  }

  public Line getLine() {
    return line;
  }

  public String getName() {
    return name;
  }

  public String getNameWithLineName() {
    return String.format("%s (%s)", name, line.getName());
  }

  public String getNameWithConnectedStations() {
    StringBuilder result = new StringBuilder(name);
    for (Station conn : connectedStations) {
      result.append(" - ").append(conn.getNameWithLineName());
    }
    return result.toString();
  }

  public int getTimeToNext() {
    return timeToNext;
  }

  public void setTimeToNext(int timeToNext) {
    this.timeToNext = timeToNext;
  }

  public int getAvgTimeToTransfer() {
    return avgTimeToTransfer;
  }

  public void setAvgTimeToTransfer(int avgTimeToTransfer) {
    this.avgTimeToTransfer = avgTimeToTransfer;
  }

  public boolean hasPreviousStation(Station station) {
    return previousStations.contains(station);
  }

  public List<Station> getPreviousStations() {
    return List.copyOf(previousStations);
  }

  public void addPreviousStation(Station station) {
    previousStations.add(station);
  }

  public void removePreviousStation(Station station) {
    previousStations.remove(station);
  }

  public boolean hasNextStation(Station station) {
    return nextStations.contains(station);
  }

  public List<Station> getNextStations() {
    return List.copyOf(nextStations);
  }

  public void addNextStation(Station station) {
    nextStations.add(station);
  }

  public void removeNextStation(Station station) {
    nextStations.remove(station);
  }

  public boolean hasConnectedStation(Station station) {
    return connectedStations.contains(station);
  }

  public List<Station> getConnectedStations() {
    return List.copyOf(connectedStations);
  }

  public void addConnectedStation(Station station) {
    connectedStations.add(station);
  }

  public void removeConnectedStation(Station station) {
    connectedStations.remove(station);
  }
}
