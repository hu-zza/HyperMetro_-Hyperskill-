package hu.zza.hyperskill.hypermetro.manager;

import java.util.List;
import java.util.function.UnaryOperator;

public abstract class DisplayManager {
  public static void printLinesToConsole(List<String> lines) {
    printLinesToConsole(lines, UnaryOperator.identity());
  }

  public static void printLinesToConsole(
      List<String> lines, UnaryOperator<List<String>> formatter) {
    formatter.apply(lines).forEach(System.out::println);
  }
}
