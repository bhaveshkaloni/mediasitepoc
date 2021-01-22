package com.mediasite.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "File Factory Service Configuration", description = "Factory Service Configurations")
public @interface ApiConfiguration {

 @AttributeDefinition(name = "apikey", description = "API Key", type = AttributeType.STRING)
 String api_key() default "439d4b804bc8187953eb36d2a8c26a02";
}
