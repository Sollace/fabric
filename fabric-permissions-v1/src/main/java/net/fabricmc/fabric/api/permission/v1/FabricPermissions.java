/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.permission.v1;

import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.permission.v1.engine.PermissionEngine;
import net.fabricmc.fabric.api.permission.v1.engine.PermissionRoot;
import net.fabricmc.fabric.impl.permission.FabricPermissionsImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

/**
 * Registry for querying and interacting with permissions.
 */
public interface FabricPermissions {
    static FabricPermissions getInstance() {
        return FabricPermissionsImpl.INSTANCE;
    }

    /**
     * Gets the global permissions instance.
     */
    static Permissions getPermissions(UUID source) {
        return FabricPermissionsImpl.INSTANCE.getPermissions(source);
    }

    /**
     * Gets the global permissions instance.
     */
    static Permissions getPermissions(PlayerEntity source) {
        return getPermissions(source.getCommandSource());
    }

    /**
     * Gets the global permissions instance.
     */
    static Permissions getPermissions(ServerCommandSource source) {
        return FabricPermissionsImpl.INSTANCE.getPermissions(source);
    }

    /**
     * Returns a COPY of the set containing all known permission IDs
     * for use by engines when saving/loading permissions.
     */
    Set<Identifier> getAll();

    /**
     * Registers a permission.
     *
     * It's advised for mods that add their own permissions to register them as well
     * so any permission engines know of their existence.
     *
     * @param permission The permission ID to register.
     */
    void registerPermission(Identifier permission);

    /**
     * Gets the ID for the current permission engines root.
     */
    Identifier getActiveRootEngineId();

    /**
     * Sets the root engine, responsible for managing engine order and priority.
     * <p>
     * The default engine goes by the natural ordering of {@link PermissionEngine#getId}.
     * Modders are, however, allowed to change this if they so desire.
     *
     * @param newRoot The new root.
     */
    void setRootEngine(PermissionRoot newRoot);

    /**
     * Adds an engine to be queried when checking for permissions.
     *
     * @param engine The engine to register.
     */
    void addEngine(PermissionEngine engine);
}
