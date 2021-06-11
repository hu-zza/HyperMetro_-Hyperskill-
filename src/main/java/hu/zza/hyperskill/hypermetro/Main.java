package hu.zza.hyperskill.hypermetro;

import hu.zza.clim.ClimException;
import hu.zza.clim.Menu;
import hu.zza.clim.menu.ProcessedInput;
import hu.zza.hyperskill.hypermetro.config.MenuInitializer;
import hu.zza.hyperskill.hypermetro.manager.StationManager;
import java.util.Scanner;

public abstract class Main {
  private static final Scanner scanner = new Scanner(System.in);
  private static String[] arguments = {};
  private static Menu menu;
  private static boolean waitingForUserInput = true;

  public static void main(String[] args) {
    arguments = args;
    try {
      checkArguments();
      initializeStaticObjects();
      startMainLoop();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void checkArguments() {
    if (arguments.length < 1) {
      throw new IllegalArgumentException("Missing first argument: fileName.");
    }
  }

  private static void initializeStaticObjects() {
    StationManager.setMetroNetworkByPath(arguments[0]);
    menu = MenuInitializer.init();
  }

  private static void startMainLoop() {
    try (scanner) {
      while (waitingForUserInput) {
        doOneIteration();
      }
    }
  }

  private static void doOneIteration() {
    if (scanner.hasNext()) {
      try {
        menu.chooseOption(scanner.nextLine());
      } catch (ClimException e) {
        e.printStackTrace();
        System.out.println("Invalid command");
      }
    } else {
      waitingForUserInput = false;
    }
  }

  public static Integer exit(ProcessedInput input) {
    waitingForUserInput = false;
    return 0;
  }
}
