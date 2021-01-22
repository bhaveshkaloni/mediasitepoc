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
import java.util.Scanner;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
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
		"sling.servlet.paths=" + "/bin/getProductDetails"
}, immediate=true)

@ServiceDescription("Get Weather Servlet")
public class GetProductDetails extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	

	/**
	 *
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest req,
			final SlingHttpServletResponse resp){
		resp.setContentType("application/json");
		try {
			PrintWriter out = resp.getWriter();
			URL url = new URL("https://www.philips.com/prx/product/B2C/en_GB/CONSUMER/products/HD9650_99.summary");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			conn.connect();

			//Getting the response code
			int responsecode = conn.getResponseCode();
			
			if (responsecode != 200) {
			    throw new RuntimeException("HttpResponseCode: " + responsecode);
			} else {
			  
			    String inline = "";
			    Scanner scanner = new Scanner(url.openStream());
			  
			   //Write all the JSON data into a string using a scanner
			    while (scanner.hasNext()) {
			       inline += scanner.nextLine();
			    }
			    
			    JSONObject obj = new JSONObject(inline); 
			    JSONObject tempObj = new JSONObject();
			    JSONObject data = obj.getJSONObject("data");
			    String imageURL = data.get("imageURL").toString();
						String subcatCode = data.get("subcatCode").toString(); 
						String marketingTextHeader = data.get("marketingTextHeader").toString();
						tempObj.put("imageURL",imageURL); tempObj.put("subcatCode",subcatCode);
						tempObj.put("marketingTextHeader",marketingTextHeader); 
						JSONObject priceData =  data.getJSONObject("price"); 
						String price = priceData.get("formattedPrice").toString(); 
						tempObj.put("price",price);
			    
			    //Close the scanner
			    scanner.close();
			    
			  //out.print(tempObj);
				conn.disconnect();
				
				out.print(tempObj);
			}

			   
			/*
			 * InputStreamReader in = new InputStreamReader(conn.getInputStream());
			 * BufferedReader br = new BufferedReader(in); String output= StringUtils.EMPTY;
			 * JSONObject tempObj = new JSONObject(); while ((output = br.readLine()) !=
			 * null) { JSONObject obj = new JSONObject(output); JSONObject data =
			 * obj.getJSONObject("data"); String imageURL = data.get("imageURL").toString();
			 * String subcatCode = data.get("subcatCode").toString(); String
			 * marketingTextHeader = data.get("marketingTextHeader").toString();
			 * tempObj.put("imageURL",imageURL); tempObj.put("subcatCode",subcatCode);
			 * tempObj.put("marketingTextHeader",marketingTextHeader); JSONObject priceData
			 * = obj.getJSONObject("price"); String price =
			 * priceData.get("formattedPrice").toString(); tempObj.put("price",price); }
			 */
		}	
			
		
		catch(Exception e) {
			logger.error("Error while process the request::"+ e.getMessage());
		}
		
		
		
	
	
	
	 
	}
}
