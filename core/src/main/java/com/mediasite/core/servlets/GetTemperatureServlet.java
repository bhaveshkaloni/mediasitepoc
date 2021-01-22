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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mediasite.core.configurations.ApiConfiguration;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
property={
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/getWeatherDetails"
}, immediate=true)

@Designate(ocd = ApiConfiguration.class, factory=true)
@ServiceDescription("Get Weather Servlet")
public class GetTemperatureServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private ApiConfiguration config;
	
	private String data;
	
	@Activate
	protected void activate(ApiConfiguration config) {
		  this.config = config;
	}
	

	/**
	 *
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest req,
			final SlingHttpServletResponse resp){
		final Resource resource = req.getResource();
		resp.setContentType("application/json");
		try {
			PrintWriter out = resp.getWriter();
			String key = config.api_key();
			URL url = new URL("http://openweathermap.org/data/2.5/onecall?lat=52.37&lon=4.89&units=metric&appid="+key);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output= StringUtils.EMPTY;
			JSONObject tempObj = new JSONObject();
			while ((output = br.readLine()) != null) {				
				JSONObject obj = new JSONObject(output);
				JSONObject data = obj.getJSONObject("current");	
				String temp = data.get("temp").toString();			
				tempObj.put("temp",temp);
			}
			
			out.print(tempObj);
			conn.disconnect();
		}
		catch(Exception e) {
			logger.error("Error while process the request::"+ e.getMessage());
		}
		
		
		
	}
	
	
	 
}
