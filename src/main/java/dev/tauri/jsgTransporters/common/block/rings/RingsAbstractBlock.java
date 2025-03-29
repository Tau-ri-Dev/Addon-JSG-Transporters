package dev.tauri.jsgTransporters.common.block.rings;

import dev.tauri.jsg.block.TickableBEBlock;
import dev.tauri.jsg.item.ITabbedItem;

public abstract class RingsAbstractBlock extends TickableBEBlock implements ITabbedItem{

  public static final Properties RINGS_BASE_PROPS = Properties.of();

  public RingsAbstractBlock(Properties properties) {
    super(properties);
  }
}
