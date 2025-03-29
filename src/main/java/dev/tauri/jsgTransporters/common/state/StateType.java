package dev.tauri.jsgTransporters.common.state;

import java.util.HashMap;
import java.util.Map;

import dev.tauri.jsg.state.StateTypeEnum;

public enum StateType {
  RENDERER_STATE(0),
  GUI_STATE(1),
  GUI_UPDATE(2),
  CAMO_STATE(3);

  public final int id;

  private StateType(int id) {
    this.id = id;
  }

  private static Map<Integer, StateType> ID_MAP = new HashMap<>();
  static {
    for (StateType stateType : values())
      ID_MAP.put(stateType.id, stateType);
  }

  public static StateType byId(int id) {
    return ID_MAP.get(id);
  }

  public static StateType convert(StateTypeEnum type) {
    return switch (type) {
      case RENDERER_STATE -> RENDERER_STATE;
      case GUI_STATE -> GUI_STATE;
      case GUI_UPDATE -> GUI_UPDATE;
      case CAMO_STATE -> CAMO_STATE;
      default -> throw new IllegalArgumentException("Unsupported state type: " + type);
    };
  }
}
