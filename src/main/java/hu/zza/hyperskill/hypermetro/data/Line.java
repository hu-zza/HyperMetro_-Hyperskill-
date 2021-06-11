package hu.zza.hyperskill.hypermetro.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Line {

  private final String name;
  private final Map<String, Station> stations = new HashMap<>();
  private Station head;

  Line(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Station getFirstStation() {
    return head;
  }

  public void setFirstStationByName(String firstStationName) {
    this.head = getStationByName(firstStationName);
  }

  public void setFirstStation(Station firstStation) {
    this.head = firstStation;
  }

  public List<Station> getAllStations() {
    if (stations.size() == 0) {
      return List.of();
    }

    Station station = head;
    boolean hasNext = true;
    ArrayList<Station> result = new ArrayList<>();

    while (hasNext) {
      result.add(station);
      hasNext = station.getNextStations().size() != 0;
      if (hasNext) {
        station = station.getNextStations().get(0);
      }
    }
    return result;
  }

  public void clear() {
    head = null;
    stations.clear();
  }

  public Station getStationByName(String stationName) {
    if (!hasStationByName(stationName)) {
      throw new IllegalArgumentException(
          String.format("Line '%s' doesn't contain station '%s'.", name, stationName));
    }
    return stations.get(stationName);
  }

  public boolean hasStationByName(String stationName) {
    return stations.containsKey(stationName);
  }

  public void createHeadStation(String stationName, int timeToNext) {
    Station station = createAndGetStation(stationName, timeToNext);
    station.addNextStation(head);
    head.addPreviousStation(station);
    head = station;
  }

  /**
   * Add a new station to the line. You should update the timeToNext of the former tail, if
   * it's necessary.
   */
  public void createTailStation(String stationName, int timeToNext) {
    Station station = createAndGetStation(stationName, timeToNext);
    Station tail =
        stations.keySet().stream()
            .map(stations::get)
            .filter(e -> e.getNextStations().size() == 0)
            .findFirst()
            .orElseThrow();

    tail.addNextStation(station);
    station.addPreviousStation(tail);
  }

  private Station createAndGetStation(String stationName, int timeToNext) {
    createStation(stationName, timeToNext);
    return getStationByName(stationName);
  }

  public void createStation(String stationName, int timeToNext) {
    if (hasStationByName(stationName)) {
      throw new IllegalArgumentException(
          String.format("Line '%s' contains station '%s' already.", name, stationName));
    }
    createAndSaveStation(stationName, timeToNext);
  }

  private void createAndSaveStation(String stationName, int timeToNext) {
    Station station = new Station(this, stationName, timeToNext);
    stations.put(stationName, station);
    setAsHeadIfFirstInsertion(station);
  }

  private void setAsHeadIfFirstInsertion(Station station) {
    if (stations.size() == 1) {
      head = station;
    }
  }

  /**
   * Deletes the Station and it's timeToNext too. You should update adjacent nodes timeToNext, if
   * it's necessary.
   */
  public void removeStationByName(String stationName) {
    if (!hasStationByName(stationName)) {
      throw new IllegalArgumentException(
          String.format("Line '%s' doesn't contain station '%s'.", name, stationName));
    }
    Station station = stations.get(stationName);
    stations.remove(stationName);
    updateLinkingForRemoving(station);
  }

  private void updateLinkingForRemoving(Station station) {
    updateHeadIfNecessary(station);
    connectPreviousAndNextStations(station);
  }

  private void updateHeadIfNecessary(Station station) {
    if (head == station) {
      head = station.getNextStations().get(0);
    }
  }

  private void connectPreviousAndNextStations(Station station) {
    List<Station> previousStations = station.getPreviousStations();
    List<Station> nextStations = station.getNextStations();
    previousStations.forEach(e -> nextStations.forEach(e::addNextStation));
    nextStations.forEach(e -> previousStations.forEach(e::addPreviousStation));
  }
}
