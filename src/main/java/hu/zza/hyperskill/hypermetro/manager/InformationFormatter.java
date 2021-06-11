package hu.zza.hyperskill.hypermetro.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class InformationFormatter {
  public static List<String> formatForStationDisplay(List<String> stations) {
    return formatForStationDisplay(new LinkedList<String>(stations));
  }

  public static List<String> formatForStationDisplay(LinkedList<String> stations) {
    List<String> result = new ArrayList<>();
    int stationCount = stations.size();

    if (stationCount < 2) {
      // throw new IllegalArgumentException("List 'stations' contains not enough (min. 2)
      // elements.");
      return List.of();
    }

    String previous = "depot";
    String current;
    String next;

    for (int i = 0; i < stationCount; i++) {
      current = stations.get(i);
      next = i < stationCount - 1 ? stations.get(i + 1) : "depot";
      result.add(String.format("%s - %s - %s", previous, current, next));
      previous = current;
    }

    return result;
  }

  public static List<String> formatForDetailedDisplay(List<String> stations) {
    return formatForDetailedDisplay(new LinkedList<String>(stations));
  }

  public static List<String> formatForDetailedDisplay(LinkedList<String> stations) {
    List<String> result = new ArrayList<>();

    if (stations.size() < 2) {
      // throw new IllegalArgumentException("List 'stations' contains not enough (min. 2)
      // elements.");
      return List.of();
    }

    result.add("depot");
    result.addAll(stations);
    result.add("depot");
    return result;
  }
}
