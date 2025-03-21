plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
    id("dev.kikugie.j52j") version "2.0" apply false // Enables asset processing by writing json5 files
}
stonecutter active "1.20.1" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter parameters {
    // Swaps replace the scope with a predefined value
    swap("mod_version", "\"${property("mod.version")}\";")
    // Dependencies add targets to check versions against
    // Using `node.property()` in this block gets the versioned property
    dependency("fapi", node!!.property("deps.fabric_api").toString())
}
