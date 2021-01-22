package com.mediasite.core.models;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;

@Model(adaptables = {SlingHttpServletRequest.class}, resourceType = {"mediasite/components/content/productDetails" },
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(name = "jackson", extensions = "json")

public class PhilipsProductDetailModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhilipsProductDetailModel.class);



	ProductDetailBean pd = new ProductDetailBean();


	@Inject
	@Via("resource")
	@Optional
	String productId;
	
	@ScriptVariable
	private Page currentPage;
	
	@Inject
    private InheritanceValueMap pageProperties;

	@Inject
	private ServiceUtil serviceUtil;
	
	


	@PostConstruct
	protected void init() {

		LOGGER.debug("Inside Post Construct method");

		getProductDetails(productId);

	}


	private void getProductDetails(String productId) {		
		try 
		{	
			ResourceResolver resourceResolver = serviceUtil.getResourceReslver();
			
			Resource res = resourceResolver.getResource("/content/products/"+productId);	
			String imageURL = StringUtils.EMPTY;
			String subcategoryName = StringUtils.EMPTY;
			
			String pageString = pageProperties.getInherited("jcr:language", "en_GB");
			
			
			String language =pageString.split("_")[0]+"_"+ pageString.split("_")[1].toLowerCase();
			
			if(res==null) {
				URL url = new URL("https://www.philips.com/prx/product/B2C/"+language+"/CONSUMER/products/"+productId+".summary");
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

					JSONObject data = obj.getJSONObject("data");
					imageURL = data.get("imageURL").toString();

					subcategoryName = data.get("subcategoryName").toString(); 
					String marketingTextHeader = data.get("marketingTextHeader").toString();
					JSONObject priceData =  data.getJSONObject("price"); 
					String price = priceData.get("formattedPrice").toString(); 				

					pd.setImageURL(imageURL);
					pd.setMarketingTextHeader(marketingTextHeader);
					pd.setPrice(price);
					pd.setSubcatCode(subcategoryName);	


					Node node = res.adaptTo(Node.class);			
					Node productNode = node.addNode(productId);

					productNode.setProperty("imageURL", imageURL);

					productNode.setProperty("productName", subcategoryName);

					resourceResolver.commit();

					//Close the scanner
					scanner.close();

					conn.disconnect();
				}
			}else {
				ValueMap prodVm = res.adaptTo(ValueMap.class);				
				imageURL = prodVm.get("imageURL").toString();
				subcategoryName = prodVm.get("productName").toString();
				pd.setImageURL(imageURL);
				pd.setSubcatCode(subcategoryName);
			}

		}


		catch(Exception e) {
			LOGGER.error("Error in getProductDetailsMethod------", e);
		}
	}

	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public ProductDetailBean getPd() {
		return pd;
	}


	public void setPd(ProductDetailBean pd) {
		this.pd = pd;
	}

	@Activate
	void getEhcacheManger() {

	}




}
