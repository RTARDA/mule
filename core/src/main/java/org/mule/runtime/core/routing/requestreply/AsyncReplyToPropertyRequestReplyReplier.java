/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.routing.requestreply;

import static org.mule.runtime.core.api.MessageExchangePattern.REQUEST_RESPONSE;
import org.mule.runtime.core.api.Event;
import org.mule.runtime.core.api.MessageExchangePattern;
import org.mule.runtime.core.api.connector.DefaultReplyToHandler;
import org.mule.runtime.core.api.construct.FlowConstruct;
import org.mule.runtime.core.api.construct.Pipeline;
import org.mule.runtime.core.api.endpoint.LegacyImmutableEndpoint;

public class AsyncReplyToPropertyRequestReplyReplier extends AbstractReplyToPropertyRequestReplyReplier {

  private final FlowConstruct flowConstruct;

  public AsyncReplyToPropertyRequestReplyReplier(FlowConstruct flowConstruct) {
    super();
    this.flowConstruct = flowConstruct;
  }

  @Override
  protected boolean shouldProcessEvent(Event event) {
    // Only process ReplyToHandler is running one-way and standard ReplyToHandler is being used.
    MessageExchangePattern mep = REQUEST_RESPONSE;
    if (getFlowConstruct() instanceof Pipeline
        && ((Pipeline) getFlowConstruct()).getSource() instanceof LegacyImmutableEndpoint) {
      mep = ((LegacyImmutableEndpoint) ((Pipeline) getFlowConstruct()).getSource()).getExchangePattern();
    }
    return !mep.hasResponse() && event.getReplyToHandler() instanceof DefaultReplyToHandler;
  }

  @Override
  public FlowConstruct getFlowConstruct() {
    return flowConstruct;
  }
}
