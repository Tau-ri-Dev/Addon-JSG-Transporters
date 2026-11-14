package dev.tauri.jsgtransporters;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.neoforge.NeoForgeContext;

import java.util.Arrays;

public class JSGTMetrics {
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware()
            .addExclusionRule((thread, error) -> Arrays.stream(error.getStackTrace())
                    .noneMatch(element -> element.getClassName().startsWith(JSGTMetrics.class.getPackageName() + ".")));


    private static final NeoForgeContext CONTEXT = new NeoForgeContext.Factory(JSGTransporters.MOD_ID)
            .requireConsent()
            .errorTrackerService(ERROR_TRACKER)
            .metrics(Metrics.Factory::create)
            .create();

    public static void load() {
    }
}
