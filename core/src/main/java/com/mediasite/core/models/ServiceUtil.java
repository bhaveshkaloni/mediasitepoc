package com.mediasite.core.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true,service=ServiceUtil.class)

public class ServiceUtil {

	private final static Logger logger = LoggerFactory.getLogger(ServiceUtil.class);

	private static String SUBERVICE = "datawrite";

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	public  ResourceResolver getResourceReslver() {

		logger.info("Inside getResourceReslver method of  ServiceUserUtil..");
		Map<String, Object> serviceParams = new HashMap<String, Object>();
		serviceParams.put(ResourceResolverFactory.SUBSERVICE, SUBERVICE);
		ResourceResolver resolver = null;
		try {
			resolver = resourceResolverFactory.getServiceResourceResolver(serviceParams);
			logger.debug("Got the ResourceResolver, returning it..");
		} catch (LoginException e) {
			logger.error("LoginException occured while getting resource resolver...");
		} catch (Exception e) {
			logger.error("Exception occured while getting resource resolver...");
		}

		return resolver;
	}


}
