/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.config;

import static org.mule.module.extensions.internal.config.XmlExtensionParserUtils.applyLifecycle;
import static org.mule.module.extensions.internal.config.XmlExtensionParserUtils.setNoRecurseOnDefinition;
import static org.mule.module.extensions.internal.config.XmlExtensionParserUtils.toElementDescriptorBeanDefinition;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.extensions.introspection.Configuration;
import org.mule.extensions.introspection.Extension;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Generic implementation of {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * capable of parsing any {@link Configuration}
 * <p/>
 * It supports simple attributes, pojos, lists/sets of simple attributes, list/sets of beans,
 * and maps of simple attributes
 * <p/>
 * It the given config doesn't provide a name, then one will be automatically generated in order to register the config
 * in the {@link org.mule.api.registry.Registry}
 *
 * @since 3.7.0
 */
public final class ExtensionConfigurationBeanDefinitionParser implements BeanDefinitionParser
{

    private final Extension extension;
    protected final Configuration configuration;

    public ExtensionConfigurationBeanDefinitionParser(Extension extension, Configuration configuration)
    {
        this.extension = extension;
        this.configuration = configuration;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext)
    {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ExtensionConfigurationFactoryBean.class);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);

        parseConfigName(element, builder);
        builder.addConstructorArgValue(configuration);
        builder.addConstructorArgValue(toElementDescriptorBeanDefinition(element));

        applyLifecycle(builder);
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);

        return definition;
    }

    private void parseConfigName(Element element, BeanDefinitionBuilder builder)
    {
        String name = AutoIdUtils.getUniqueName(element, "mule-bean");
        element.setAttribute("name", name);
        builder.addConstructorArgValue(name);
    }
}
