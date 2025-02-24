package org.foxesworld.lvm.loader;

/**
 * Interface for resource loading.
 * <p>
 * This interface allows for easy replacement of the resource loading implementation,
 * for example, for testing purposes or loading from alternative sources.
 * </p>
 */
public interface ResourceLoader {

    /**
     * Loads a resource from the specified path.
     *
     * @param resourcePath the path to the resource relative to the base directory; must not be {@code null}
     * @return the content of the resource as a {@link String}
     */
    String loadResource(String resourcePath);
}
