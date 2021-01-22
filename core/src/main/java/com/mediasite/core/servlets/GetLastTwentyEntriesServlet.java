/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mediasite.core.servlets;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import javax.jcr.Node;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
property={
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/getEntries"
})
@ServiceDescription("Get Weather Servlet")
public class GetLastTwentyEntriesServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	protected void doGet(final SlingHttpServletRequest req,
			final SlingHttpServletResponse resp){
		resp.setContentType("application/json");
		
		try {
			PrintWriter out = resp.getWriter();	
			ArrayList<Integer> arrayL = new ArrayList<Integer>();			
			ResourceResolver resolver = req.getResourceResolver();
			Resource res = resolver.getResource("/content/mediasite/us/en/water-heights/jcr:content/parcontent/waterheightdetails/mediaheights");			
			Iterator<Resource> resourceIterator = res.listChildren();			
			JSONObject json = new JSONObject();
			while(resourceIterator.hasNext()) {				
				Resource resIt = resourceIterator.next();
				Node itemNode = resIt.adaptTo(Node.class);
				String height = itemNode.getProperty("heights").getString();			
				Calendar startDate = itemNode.getProperty("startDate").getDate();				
				Date date = startDate.getTime();				
				int heightInt = Integer.valueOf(height);				
				arrayL.add(heightInt);	
				Collections.sort(arrayL);
				
			}
			out.println(arrayL)	;
		}
		catch(Exception e) {
			logger.error("Error while executing the function :",e.getMessage());
		}
	}
}
