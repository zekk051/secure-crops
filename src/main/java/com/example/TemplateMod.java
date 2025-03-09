package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class TemplateMod implements ModInitializer {
    // Modified code from https://github.com/A5b84/convenient-mobgriefing
    public static final GameRules.Key<BooleanRule>
            SECURE_FARMLAND_AND_CROPS = register("secureFarmlandAndCrops"), // If crop present, secure farmland and crop
            SECURE_FARMLAND_BREAK_CROPS = register("secureFarmlandBreakCrops"),	// If crop present, secure farmland but trample crop
            SECURE_FARMLAND_IF_EMPTY = register("secureFarmlandIfEmpty"); // If no crop present, secure farmland

    private static GameRules.Key<BooleanRule> register(String name) {
        return GameRuleRegistry.register(
                name, GameRules.Category.MISC,
                GameRuleFactory.createBooleanRule(false)
        );
    }

    @Override
    public void onInitialize() {
    }
}