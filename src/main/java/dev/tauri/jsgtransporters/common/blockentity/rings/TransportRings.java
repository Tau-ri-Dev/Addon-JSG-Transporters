package dev.tauri.jsgtransporters.common.blockentity.rings;

import java.util.ArrayList;
import java.util.function.Predicate;

public interface TransportRings {
    static int[] getRandomSymbolsToDisplay(int maxSymbols, Predicate<Integer> shouldDisplaySymbol) {
        var symbolsToDisplay = new ArrayList<Integer>();
        if (maxSymbols < 1) return new int[0];
        if (maxSymbols > 4) maxSymbols = 4;
        for (var i = 1; i <= maxSymbols; i++) {
            if (shouldDisplaySymbol.test(i))
                symbolsToDisplay.add(i);
        }
        if (shouldDisplaySymbol.test(9))
            symbolsToDisplay.add(9);
        var symbolToDisplayArray = new int[symbolsToDisplay.size()];
        for (var i = 0; i < symbolsToDisplay.size(); i++) {
            symbolToDisplayArray[i] = symbolsToDisplay.get(i);
        }
        return symbolToDisplayArray;
    }
}
