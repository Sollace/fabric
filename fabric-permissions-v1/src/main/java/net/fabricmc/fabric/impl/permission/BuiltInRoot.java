package net.fabricmc.fabric.impl.permission;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.api.permission.v1.engine.PermissionEngine;
import net.fabricmc.fabric.api.permission.v1.engine.PermissionRoot;
import net.minecraft.util.Identifier;

class BuiltInRoot implements PermissionRoot {
    private static final Identifier ID = new Identifier("fabric", "builtin_root");
    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Identifier, PermissionEngine> engines = new TreeMap<>();

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void addEngine(PermissionEngine engine) {
        Preconditions.checkNotNull(engine, "engine cannot be null");
        Identifier id = Preconditions.checkNotNull(engine.getId(), "permission engine was missing an id");
        if (engines.containsKey(id)) {
            LOGGER.warn("Duplicate permission engine for id {0}. Mapping {1} -> {2}",
                    id,
                    engines.get(id).getClass().getCanonicalName(),
                    engine.getClass().getCanonicalName()
            );
        }
        engines.put(id, engine);
    }

    @Override
    public Iterator<PermissionEngine> iterator() {
        return engines.values().iterator();
    }
}
