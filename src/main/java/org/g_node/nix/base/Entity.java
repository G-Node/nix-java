package org.g_node.nix.base;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;

import java.util.Date;

/**
 * <h1>Entity</h1>
 * An abstract class that declares a basic features of most entities.
 */

@Properties(value = {
        @Platform(value = "linux"),
        @Platform(value = "windows")})
public abstract class Entity extends ImplContainer {

    /**
     * Getter for the id of the entity.
     * <p>
     * The id is generated at creation time of an entity. It contains
     * a randomly generated sequence of characters with low collision
     * probability.
     *
     * @return The id of the entity.
     */
    abstract public String getId();

    /**
     * Gets the time of the last update.
     *
     * @return Time of the last update.
     */
    abstract public Date getUpdatedAt();

    /**
     * Gets the creation time.
     *
     * @return The creation time of the entity.
     */
    abstract public Date getCreatedAt();

    /**
     * Sets the time of the last update to the current time if
     * the field is not set.
     */
    abstract public void setUpdatedAt();

    /**
     * Sets the time of the last update to the current time.
     */
    abstract public void forceUpdatedAt();

    /**
     * Sets the creation time to the current time if the creation
     * time is not set.
     */
    abstract public void setCreatedAt();

    /**
     * Sets the creation time to the provided value even if the
     * field is already set.
     *
     * @param date The creation time to set.
     */
    abstract public void forceCreatedAt(Date date);

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Entity)) {
            return false;
        }

        Entity entity = (Entity) obj;

        return this.getId().equals(entity.getId());
    }
}
