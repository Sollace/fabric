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
import com.google.common.base.Preconditions;

import net.minecraft.util.Identifier;

/**
 * The public interface for querying permissions for either a specific player, or a command source.
 */
public interface Permissions {
    /**
     * Gets all the permissions currently available
     *
     * @return A set of all permissions available
     */
    Set<Identifier> getAll();

    /**
     * Checks whether a specific permission is granted
     *
     * @param permission The permission to check for.
     *
     * @return True if the permission is available
     */
    boolean has(Identifier permission);

    /**
     * Checks whether all of the indicated permissions are granted.
     * <p>
     * This method is a simple convenience utility wrapping calls to {@link Permissions#has}.
     *
     * @param permissions The permissions to check for.
     *
     * @return True if all of the requested permissions are available
     */
    default boolean hasAll(Identifier... permissions) {
        Preconditions.checkArgument(permissions.length > 0, "cannot check against an empty permission set");
        for (Identifier permission : permissions) {
            if (!has(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether all of the indicated permissions are granted.
     * <p>
     * This method is a simple convenience utility wrapping calls to {@link Permissions#has}.
     *
     * @param permissions The permissions to check for.
     *
     * @return True if all of the requested permissions are available
     */
    default boolean hasAny(Identifier... permissions) {
        Preconditions.checkArgument(permissions.length > 0, "cannot check against an empty permission set");
        for (Identifier permission : permissions) {
            if (has(permission)) {
                return true;
            }
        }
        return false;
    }
}
