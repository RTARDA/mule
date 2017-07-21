/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.api.exception;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.message.ErrorType;

import java.util.Optional;

/**
 * @since 4.0
 */
public class CompositeErrorTypeRepository implements ErrorTypeRepository {

  private ErrorTypeRepository childErrorTypeRepository;
  private ErrorTypeRepository parentErrorTypeRepository;

  /**
   *
   * @param childErrorTypeRepository
   * @param parentErrorTypeRepository
   */
  public CompositeErrorTypeRepository(ErrorTypeRepository childErrorTypeRepository,
                                      ErrorTypeRepository parentErrorTypeRepository) {
    this.childErrorTypeRepository = childErrorTypeRepository;
    this.parentErrorTypeRepository = parentErrorTypeRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType addErrorType(ComponentIdentifier errorTypeIdentifier, ErrorType parentErrorType) {
    return childErrorTypeRepository.addErrorType(errorTypeIdentifier, parentErrorType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType addInternalErrorType(ComponentIdentifier errorTypeIdentifier, ErrorType parentErrorType) {
    return childErrorTypeRepository.addInternalErrorType(errorTypeIdentifier, parentErrorType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ErrorType> lookupErrorType(ComponentIdentifier errorTypeComponentIdentifier) {
    Optional<ErrorType> errorType = childErrorTypeRepository.lookupErrorType(errorTypeComponentIdentifier);
    if (!errorType.isPresent()) {
      errorType = parentErrorTypeRepository.lookupErrorType(errorTypeComponentIdentifier);
    }
    return errorType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ErrorType> getErrorType(ComponentIdentifier errorTypeIdentifier) {
    Optional<ErrorType> errorType = childErrorTypeRepository.getErrorType(errorTypeIdentifier);
    if (!errorType.isPresent()) {
      errorType = parentErrorTypeRepository.getErrorType(errorTypeIdentifier);
    }
    return errorType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType getAnyErrorType() {
    return childErrorTypeRepository.getAnyErrorType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType getSourceErrorType() {
    return childErrorTypeRepository.getSourceErrorType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType getSourceResponseErrorType() {
    return childErrorTypeRepository.getSourceResponseErrorType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ErrorType getCriticalErrorType() {
    return childErrorTypeRepository.getCriticalErrorType();
  }
}
