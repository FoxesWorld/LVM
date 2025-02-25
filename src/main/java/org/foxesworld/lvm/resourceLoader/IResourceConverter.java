package org.foxesworld.lvm.resourceLoader;

import java.io.InputStream;

/**
 * Interface for a converter that transforms an InputStream into the required object type.
 *
 * @param <T> the type of the converted result
 */
interface IResourceConverter<T> {
    T convert(InputStream is) throws Exception;
}