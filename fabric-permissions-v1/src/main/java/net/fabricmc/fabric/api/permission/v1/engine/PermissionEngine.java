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

package net.fabricmc.fabric.api.permission.v1.engine;

import java.util.Set;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

/**
 * Represents an engine for querying specific permissions.
 * <p>
 * Implementors are left to their own devices as to how they implement loading and storing of their permissions
 * however it is critical that they implement the below methods and register themselves
 * using {@link FabricPermissionRegistry#addEngine} if they wish to serve as a valid
 * source for fulfilling permission requests.
 *
 * @see FabricPermissionRegistry#addEngine
 */
public interface PermissionEngine {
    /**
     * Gets the unique id for this permission engine.
     * This is used for sorting, and every engine should provide its own ID.
     */
    Identifier getId();

    boolean checkPermission(UUID uuid, Identifier permission);

    void appendAllPermissions(UUID source, Set<Identifier> output);

    default boolean checkPermission(ServerCommandSource source, Identifier permission) throws CommandSyntaxException {
        return checkPermission(source.getPlayer().getGameProfile().getId(), permission);
    }

    default void appendAllPermissions(ServerCommandSource source, Set<Identifier> output) throws CommandSyntaxException {
        appendAllPermissions(source.getPlayer().getGameProfile().getId(), output);
    }
}
