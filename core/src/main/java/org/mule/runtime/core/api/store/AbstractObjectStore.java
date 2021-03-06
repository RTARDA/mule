/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.api.store;

import static org.mule.runtime.core.api.config.i18n.CoreMessages.objectIsNull;
import static org.slf4j.LoggerFactory.getLogger;

import org.mule.runtime.api.store.ObjectAlreadyExistsException;
import org.mule.runtime.api.store.ObjectDoesNotExistException;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreException;
import org.mule.runtime.core.api.config.i18n.CoreMessages;

import java.io.Serializable;

import org.slf4j.Logger;

/**
 * This is an abstract superclass for {@link ObjectStore} implementations that conforms to the contract defined in the interface's
 * javadocs. Subclasses only need to implement storing the actual objects.
 */
public abstract class AbstractObjectStore<T extends Serializable> implements ObjectStore<T> {

  protected final Logger logger = getLogger(getClass());

  @Override
  public boolean contains(Serializable key) throws ObjectStoreException {
    if (key == null) {
      throw new ObjectStoreException(objectIsNull("key"));
    }
    return doContains(key);
  }

  protected abstract boolean doContains(Serializable key) throws ObjectStoreException;

  @Override
  public void store(Serializable key, T value) throws ObjectStoreException {
    if (key == null) {
      throw new ObjectStoreException(objectIsNull("key"));
    }

    if (contains(key)) {
      throw new ObjectAlreadyExistsException();
    }

    doStore(key, value);
  }

  protected abstract void doStore(Serializable key, T value) throws ObjectStoreException;

  @Override
  public T retrieve(Serializable key) throws ObjectStoreException {
    if (key == null) {
      throw new ObjectStoreException(objectIsNull("key"));
    }

    if (contains(key) == false) {
      String message = "Key does not exist: " + key;
      throw new ObjectDoesNotExistException(CoreMessages.createStaticMessage(message));
    }

    return doRetrieve(key);
  }

  protected abstract T doRetrieve(Serializable key) throws ObjectStoreException;

  @Override
  public T remove(Serializable key) throws ObjectStoreException {
    if (key == null) {
      throw new ObjectStoreException(objectIsNull("key"));
    }

    if (contains(key) == false) {
      throw new ObjectDoesNotExistException();
    }

    return doRemove(key);
  }

  protected abstract T doRemove(Serializable key) throws ObjectStoreException;
}
