package net.zekk051.securecrops;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class SecureCrops implements ModInitializer {
    // Modified code from https://github.com/A5b84/convenient-mobgriefing
    public static final GameRules.Key<BooleanRule>
            SECURE_CROPS = register("secureCrops", false), // Do not break crop or farmland when farmland has crops
            SECURE_FARMLAND_WITH_CROPS = register("secureFarmlandWithCrops", false),	// Break crop but leave farmland when farmland has crops
            SECURE_FARMLAND = register("secureFarmland", false); // Do not break farmland without crops

    private static GameRules.Key<BooleanRule> register(String name, boolean boolValue) {
        return GameRuleRegistry.register(
                name, GameRules.Category.MISC,
                GameRuleFactory.createBooleanRule(boolValue)
        );
    }

    @Override
    public void onInitialize() {
    }
}