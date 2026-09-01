import common.Constants.PLUGIN_KSP
import common.Constants.ROOM_COMPILER
import common.Constants.ROOM_KTX
import common.Constants.ROOM_RUNTIME
import extensions.addDependencies
import extensions.addKspDependency
import extensions.getPluginId
import extensions.getVersionCatalog

/**
 * Wires Room into a module: the runtime plus the KSP processor that generates the DAO and
 * database implementations.
 *
 * The schema directory is not set here — a module that exports its schema declares it with
 * `ksp { arg("room.schemaLocation", ...) }`, so only the modules that own a database carry
 * a schemas folder.
 */

val libs = getVersionCatalog()

pluginManager.apply(libs.getPluginId(PLUGIN_KSP))

val roomDependencies = listOf(
    ROOM_RUNTIME,
    ROOM_KTX
)

dependencies {
    addDependencies(libs, roomDependencies)
    addKspDependency(libs, ROOM_COMPILER)
}
