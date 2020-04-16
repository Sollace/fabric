package net.fabricmc.fabric.api.permission.v1.engine;

import net.minecraft.util.Identifier;

/**
 * Represents a root permissions engine.
 */
public interface PermissionRoot extends Iterable<PermissionEngine> {
    /**
     * Gets the unique id for this permission engine.
     * This is used for sorting, and every engine should provide its own ID.
     */
    Identifier getId();

    /**
     * Whether this engine allows itself to be replaced.
     *
     * @param newRoot The new root that this engine will be replaced with.
     * @return True if the new root can replace this one.
     */
    default boolean canBeReplaced(PermissionRoot newRoot) {
        return true;
    }

    /**
     * Adds a permission engine to the current pool of available permission engines.
     *
     * @param engine The engine to add.
     */
    void addEngine(PermissionEngine engine);
}
