package hu.zza.hyperskill.hypermetro.config;

import static hu.zza.hyperskill.hypermetro.config.MenuParameter.COMMAND;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.LINE_A_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.LINE_B_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.LINE_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.STATION_A_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.STATION_B_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.STATION_NAME;
import static hu.zza.hyperskill.hypermetro.config.MenuParameter.TIME;

import hu.zza.clim.HeaderStyle;
import hu.zza.clim.Menu;
import hu.zza.clim.MenuBuilder;
import hu.zza.clim.MenuStructureBuilder;
import hu.zza.clim.ParameterMatcherBuilder;
import hu.zza.clim.UserInterface;
import hu.zza.clim.menu.MenuStructure;
import hu.zza.clim.parameter.Parameter;
import hu.zza.clim.parameter.ParameterMatcher;
import hu.zza.clim.parameter.ParameterName;
import hu.zza.clim.parameter.Parameters;
import hu.zza.hyperskill.hypermetro.Main;
import hu.zza.hyperskill.hypermetro.manager.StationManager;
import java.util.List;

public abstract class MenuInitializer {

  public static Menu init() {
    MenuStructure menuStructure =
        new MenuStructureBuilder()
            .setRawMenuStructure(
                "{\"HyperMetro\" : [\"append\", \"add-head\", \"connect\", \"exit\", \"fastest-route\", \"output\", \"remove\", \"route\"]}")
            .setLeaf("append", StationManager::addTailStation, "HyperMetro")
            .setLeaf("add-head", StationManager::addHeadStation, "HyperMetro")
            .setLeaf("connect", StationManager::connectStation, "HyperMetro")
            .setLeaf("exit", Main::exit, "HyperMetro")
            .setLeaf("fastest-route", StationManager::getFastestRoute, "HyperMetro")
            .setLeaf("output", StationManager::printStationsOfLine, "HyperMetro")
            .setLeaf("remove", StationManager::removeStation, "HyperMetro")
            .setLeaf("route", StationManager::getRoute, "HyperMetro")
            .build();

    final String commandRegex = "/([-\\w]+)";
    final String wordRegex = "\"([-&. \\w]+?)\"";
    final String integerRegex = "(\\d+)";

    final Parameter commandParameter = Parameters.of(commandRegex);
    final Parameter wordParameter = Parameters.of(wordRegex);
    final Parameter optionalIntegerParameter = Parameters.of(integerRegex, "0");

    final List<ParameterName> standardParamNames = List.of(COMMAND, LINE_NAME, STATION_NAME, TIME);

    final List<ParameterName> doubleStationParamNames =
        List.of(COMMAND, LINE_A_NAME, STATION_A_NAME, LINE_B_NAME, STATION_B_NAME);

    final List<Parameter> standardParameters =
        List.of(commandParameter, wordParameter, wordParameter, optionalIntegerParameter);

    final List<Parameter> doubleStationParameters =
        List.of(commandParameter, wordParameter, wordParameter, wordParameter, wordParameter);

    ParameterMatcher parameterMatcher =
        new ParameterMatcherBuilder()
            .setCommandRegex("^" + commandRegex)
            .setLeafParameters("append", " ", standardParamNames, standardParameters)
            .setLeafParameters("add-head", " ", standardParamNames, standardParameters)
            .setLeafParameters("connect", " ", doubleStationParamNames, doubleStationParameters)
            .setLeafParameters("exit", " ", List.of(COMMAND), List.of(commandParameter))
            .setLeafParameters(
                "fastest-route", " ", doubleStationParamNames, doubleStationParameters)
            .setLeafParameters(
                "output",
                " ",
                List.of(COMMAND, LINE_NAME),
                List.of(commandParameter, wordParameter))
            .setLeafParameters("remove", " ", standardParamNames, standardParameters)
            .setLeafParameters("route", " ", doubleStationParamNames, doubleStationParameters)
            .build();

    return new MenuBuilder()
        .setMenuStructure(menuStructure)
        .setParameterMatcher(parameterMatcher)
        .setClimOptions(UserInterface.PARAMETRIC, HeaderStyle.HIDDEN)
        .build();
  }
}
