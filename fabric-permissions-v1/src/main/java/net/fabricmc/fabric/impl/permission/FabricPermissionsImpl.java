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

package net.fabricmc.fabric.impl.permission;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.api.permission.v1.FabricPermissions;
import net.fabricmc.fabric.api.permission.v1.Permissions;
import net.fabricmc.fabric.api.permission.v1.engine.PermissionEngine;
import net.fabricmc.fabric.api.permission.v1.engine.PermissionRoot;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public final class FabricPermissionsImpl implements FabricPermissions {
    public static final FabricPermissionsImpl INSTANCE = new FabricPermissionsImpl();

    private final Set<Identifier> registeredPermissions = new HashSet<>();

    private PermissionRoot root = new BuiltInRoot();

    private FabricPermissionsImpl() {}

    public Permissions getPermissions(UUID uuid) {
        return new Permissions() {
            @Override
            public Set<Identifier> getAll() {
                Set<Identifier> output = new HashSet<>();
                for (PermissionEngine i : root) {
                    i.appendAllPermissions(uuid, output);
                }
                return output;
            }

            @Override
            public boolean has(Identifier permission) {
                for (PermissionEngine i : root) {
                    if (i.checkPermission(uuid, permission)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public Permissions getPermissions(ServerCommandSource source) {
        return new Permissions() {
            @Override
            public Set<Identifier> getAll() {
                try {
                    Set<Identifier> output = new HashSet<>();
                    for (PermissionEngine i : root) {
                        i.appendAllPermissions(source, output);
                    }
                    return output;
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean has(Identifier permission) {
                try {
                    for (PermissionEngine i : root) {
                        if (i.checkPermission(source, permission)) {
                            return true;
                        }
                    }
                    return false;
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public void registerPermission(Identifier permission) {
        registeredPermissions.add(Preconditions.checkNotNull(permission, "null is not a valid permission id"));
    }

    @Override
    public Identifier getActiveRootEngineId() {
        return root.getId();
    }

    @Override
    public void setRootEngine(PermissionRoot newRoot) {
        Preconditions.checkNotNull(newRoot, "new root cannot be null");
        Preconditions.checkArgument(root.canBeReplaced(newRoot), "the current root (" + root.getId() + ") cannot be replaced by (" + newRoot.getId() + ")");
        for (PermissionEngine i : root) {
            newRoot.addEngine(i);
        }
        root = newRoot;
    }

    @Override
    public void addEngine(PermissionEngine engine) {
        root.addEngine(engine);
    }

    @Override
    public Set<Identifier> getAll() {
        return new HashSet<>(registeredPermissions);
    }
}
