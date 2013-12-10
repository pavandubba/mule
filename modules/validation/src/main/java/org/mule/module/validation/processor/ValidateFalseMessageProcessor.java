/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.validation.processor;

import org.mule.api.MuleEvent;
import org.mule.module.validation.Validator;

/**
 * If the given <code>expression</code> resolves to a <code>true</code>
 * {@link Boolean} value throw an Exception
 */
public class ValidateFalseMessageProcessor extends AbstractValidationMessageProcessor
{

    /**
     * If the given <code>expression</code> resolves to a <code>true</code>
     * {@link Boolean} value throw an Exception
     */
    private String expression;
    

    @Override
    protected Validator getValidator(MuleEvent event) throws Exception
    {
        final Boolean evaluatedExpression = (Boolean) this.evaluateAndTransform(this.muleContext, event,
            Boolean.class, null, this.expression);

        return new Validator()
        {

            @Override
            public boolean isValid(MuleEvent event)
            {
                return !evaluatedExpression;
            }

            @Override
            public String getErrorMessage(MuleEvent event)
            {
                return String.format("Expression '%s' was true", expression);
            }
        };
    }

    public void setExpression(String expression)
    {
        this.expression = expression;
    }
}

