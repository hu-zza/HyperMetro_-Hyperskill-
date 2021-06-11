package hu.zza.hyperskill.hypermetro.data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Network {
  private final Map<String, Line> lines = new HashMap<>();
  private String name;
  private int remainingTime;

  public Network(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void clear() {
    lines.forEach((k, v) -> v.clear());
    lines.clear();
  }

  public void addLineByName(String lineName) {
    if (hasLineByName(lineName)) {
      throw new IllegalArgumentException(
          String.format("Network '%s' contains line '%s' already.", name, lineName));
    }
    lines.put(lineName, new Line(lineName));
  }

  public boolean hasLineByName(String lineName) {
    return lines.containsKey(lineName);
  }

  public void removeLineByName(String lineName) {
    if (!hasLineByName(lineName)) {
      throw new IllegalArgumentException(
          String.format("Network '%s' doesn't contain line '%s'.", name, lineName));
    }
    lines.remove(lineName);
  }

  public void addHeadStationByName(String lineName, String stationName, int timeToNextStation) {
    if (hasStationByName(lineName, stationName)) {
      throw new IllegalArgumentException(
          String.format(
              "Network '%s' contains station '%s' on line '%s' already.",
              name, stationName, lineName));
    }
    lines.get(lineName).createHeadStation(stationName, timeToNextStation);
  }

  public boolean hasStationByName(String lineName, String stationName) {
    return lines.containsKey(lineName) && lines.get(lineName).hasStationByName(stationName);
  }

  public void addTailStationByName(String lineName, String stationName, int timeToNextStation) {
    if (hasStationByName(lineName, stationName)) {
      throw new IllegalArgumentException(
          String.format(
              "Network '%s' contains station '%s' on line '%s' already.",
              name, stationName, lineName));
    }
    lines.get(lineName).createTailStation(stationName, timeToNextStation);
  }

  public void removeStationByName(String lineName, String stationName) {
    if (!hasStationByName(lineName, stationName)) {
      throw new IllegalArgumentException(
          String.format("Network '%s' doesn't contain line '%s'.", name, stationName));
    }
    lines.get(lineName).removeStationByName(stationName);
  }

  public void makeStationsAdjacentByNames(
      String line, String station, String nextLine, String nextStation) {
    try { // TODO: 2021. 06. 02. Delete it... Find a better solution
      makeStationsAdjacent(
          getStationByName(line, station), getStationByName(nextLine, nextStation));
    } catch (Exception e) {
      // For the missing stations of london.json
    }
  }

  public void makeStationsAdjacent(Station station, Station nextStation) {
    station.addNextStation(nextStation);
    nextStation.addPreviousStation(station);
  }

  public void connectStationsByNames(String lineA, String stationA, String lineB, String stationB) {
    try { // TODO: 2021. 06. 02. Delete it... Find a better solution
      connectStations(getStationByName(lineA, stationA), getStationByName(lineB, stationB));
    } catch (Exception e) {
      // For the missing stations of london.json
    }
  }

  public void connectStations(Station stationA, Station stationB) {
    stationA.addConnectedStation(stationB);
    stationB.addConnectedStation(stationA);
  }

  public Station getStationByName(String lineName, String stationName) {
    if (!hasStationByName(lineName, stationName)) {
      throw new IllegalArgumentException(
          String.format(
              "Network '%s' doesn't contain station '%s' on line '%s'.",
              name, stationName, lineName));
    }
    return getLineByName(lineName).getStationByName(stationName);
  }

  public Line getLineByName(String lineName) {
    if (!hasLineByName(lineName)) {
      throw new IllegalArgumentException(
          String.format("Network '%s' doesn't contain line '%s'.", name, lineName));
    }
    return lines.get(lineName);
  }

  public void disconnectStationsByNames(
      String lineA, String stationA, String lineB, String stationB) {
    disconnectStations(getStationByName(lineA, stationA), getStationByName(lineB, stationB));
  }

  public void disconnectStations(Station stationA, Station stationB) {
    stationA.removeConnectedStation(stationB);
    stationB.removeConnectedStation(stationA);
  }

  public List<String> getGuideBetweenByName(
      String lineA, String stationA, String lineB, String stationB) {
    List<Station> route = getRouteBetweenByName(lineA, stationA, lineB, stationB);

    Station station;
    ArrayList<String> result = new ArrayList<>();

    for (int i = 0; i < route.size(); i++) {
      station = route.get(i);
      result.add(station.getName());
      if (i + 1 < route.size() && station.hasConnectedStation(route.get(i + 1))) {
        result.add(String.format("Transition to line %s", route.get(i + 1).getLine().getName()));
      }
    }
    return result;
  }

  public List<Station> getRouteBetweenByName(
      String lineA, String stationA, String lineB, String stationB) {
    return getRouteBetween(getStationByName(lineA, stationA), getStationByName(lineB, stationB));
  }

  public List<Station> getRouteBetween(Station stationA, Station stationB) {
    return getRouteAsNodeList(stationA, stationB).stream()
        .map(Node::getRealObject)
        .collect(Collectors.toList());
  }

  private List<Node<Station>> getRouteAsNodeList(Station stationA, Station stationB) {
    return Stream.iterate(getRouteAsItsTail(stationA, stationB), Objects::nonNull, Node::getParent)
        .collect(ArrayList::new, (r, e) -> r.add(0, e), ArrayList::addAll);
  }

  private Node<Station> getRouteAsItsTail(Station stationA, Station stationB) {
    HashSet<Station> visited = new HashSet<>();
    ArrayDeque<Node<Station>> queue = new ArrayDeque<>(List.of(new Node<>(stationA)));
    Node<Station> node;

    while (!queue.isEmpty()) {
      node = queue.poll();
      if (node.getRealObject() == stationB) {
        return node;
      }
      List<Station> nextStations =
          Stream.concat(
                  Stream.concat(
                      node.getRealObject().getConnectedStations().stream(),
                      node.getRealObject().getNextStations().stream()),
                  node.getRealObject().getPreviousStations().stream())
              .filter(Predicate.not(visited::contains))
              .collect(Collectors.toList());

      visited.addAll(nextStations);

      Node<Station> parent = node;
      queue.addAll(
          nextStations.stream()
              .map(Node::new)
              .peek(n -> n.setParent(parent))
              .collect(Collectors.toList()));
    }
    throw new MissingRouteException(
        String.format(
            "There is no route between %s and %s.",
            stationA.getNameWithLineName(), stationB.getNameWithLineName()));
  }

  public List<String> getFastestGuideBetweenByName(
      String lineA, String stationA, String lineB, String stationB) {
    List<Station> route = getFastestRouteBetweenByName(lineA, stationA, lineB, stationB);

    Station station;
    ArrayList<String> result = new ArrayList<>();
    int totalTime = remainingTime; // TODO: 2021. 06. 01. only for testing!

    for (int i = 0; i < route.size(); i++) {
      station = route.get(i);
      result.add(station.getName());
      if (i + 1 < route.size() && station.hasConnectedStation(route.get(i + 1))) {
        result.add(String.format("Transition to line %s", route.get(i + 1).getLine().getName()));
      }
    }
    result.add(
        String.format("Total: %d minute%s in the way", totalTime, totalTime != 1 ? "s" : ""));
    return result;
  }

  public List<Station> getFastestRouteBetweenByName(
      String lineA, String stationA, String lineB, String stationB) {
    return getFastestRouteBetween(
        getStationByName(lineA, stationA), getStationByName(lineB, stationB));
  }

  public List<Station> getFastestRouteBetween(Station stationA, Station stationB) {
    return getFastestRouteAsNodeList(stationA, stationB).stream()
        .map(Node::getRealObject)
        .collect(Collectors.toList());
  }

  private List<Node<Station>> getFastestRouteAsNodeList(Station stationA, Station stationB) {
    return Stream.iterate(
            getFastestRouteAsItsTail(stationA, stationB), Objects::nonNull, Node::getParent)
        .collect(ArrayList::new, (r, e) -> r.add(0, e), ArrayList::addAll);
  }

  private Node<Station> getFastestRouteAsItsTail(Station stationA, Station stationB) {
    PriorityQueue<PriorityNode<Station>> queue = new PriorityQueue<>();
    HashSet<Station> visited = new HashSet<>();
    PriorityNode<Station> node;
    Station station;

    queue.add(new PriorityNode<>(stationA, 0));

    while (!queue.isEmpty()) {
      node = queue.poll();
      station = node.getRealObject();
      if (!visited.contains(station)) {

        if (station == stationB) {
          remainingTime = node.getPriority(); // TODO: 2021. 06. 01. only for testing!
          return node;
        }

        visited.add(station);
        PriorityNode<Station> parent = node;

        queue.addAll(
            node.getRealObject().getPreviousStations().stream()
                .map(PriorityNode::new)
                .peek(n -> n.setParent(parent))
                .peek(n -> n.setPriority(parent.getPriority() + n.getRealObject().getTimeToNext()))
                .collect(Collectors.toList()));

        int nextWeight = node.getPriority() + station.getTimeToNext();
        queue.addAll(
            node.getRealObject().getNextStations().stream()
                .map(PriorityNode::new)
                .peek(n -> n.setParent(parent))
                .peek(n -> n.setPriority(nextWeight))
                .collect(Collectors.toList()));

        int transferWeight = node.getPriority() + station.getAvgTimeToTransfer();
        queue.addAll(
            node.getRealObject().getConnectedStations().stream()
                .map(PriorityNode::new)
                .peek(n -> n.setParent(parent))
                .peek(n -> n.setPriority(transferWeight))
                .collect(Collectors.toList()));
      }
    }
    throw new MissingRouteException(
        String.format(
            "There is no route between %s and %s.",
            stationA.getNameWithLineName(), stationB.getNameWithLineName()));
  }
}
