package net.zekk051;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class SecureCrops implements ModInitializer {
    // Modified code from:
    // https://github.com/A5b84/convenient-mobgriefing
    // https://github.com/FabricMC/fabric/tree/1.21.4/fabric-game-rule-api-v1/src/testmod

    public static final CustomGameRuleCategory SECURE_CROPS_CATEGORY = new CustomGameRuleCategory(Identifier.of("secure_crops", "gamerule_category"), Text.literal("Secure Crops").styled(style -> style.withBold(true).withColor(Formatting.YELLOW)));

    public static final GameRules.Key<BooleanRule>
            SECURE_FARMLAND_AND_CROPS = register("secureFarmlandAndCrops"), // If crop present, secure farmland and crop
            SECURE_FARMLAND_BREAK_CROPS = register("secureFarmlandBreakCrops"),	// If crop present, secure farmland but trample crop
            SECURE_FARMLAND_IF_EMPTY = register("secureFarmlandIfEmpty"); // If no crop present, secure farmland

    private static GameRules.Key<BooleanRule> register(String name) {
        return GameRuleRegistry.register(
                name, SECURE_CROPS_CATEGORY,
                GameRuleFactory.createBooleanRule(false)
        );
    }

    @Override
    public void onInitialize() {
    }
}