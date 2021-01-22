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
package com.mediasite.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class MediaSiteHeaderModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MediaSiteHeaderModel.class);

	@Inject
	String headerTitle;

	@Inject
	String headerDesc;

	@Inject
	String fileReference;

	@Inject
	String buttonText;

	@Inject
	String buttonLink;




	@PostConstruct
	protected void init() {

		LOGGER.debug("Inside Post Construct method");

	}

	

	public String getFileReference() {
		return fileReference;
	}





	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}




	public String getHeaderDesc() {
		return headerDesc;
	}




	public void setHeaderDesc(String headerDesc) {
		this.headerDesc = headerDesc;
	}




	public String getButtonText() {
		return buttonText;
	}




	public void setButtonText(String buttonText) {
		
		this.buttonText = buttonText;
	}




	public String getButtonLink() {
		if(buttonLink.contains("/content")) {
			buttonLink = buttonLink+".html";			
		}
		return buttonLink;
	}




	public void setButtonLink(String buttonLink) {
		this.buttonLink = buttonLink;
	}




	public String getHeaderTitle() {
		return headerTitle;
	}




	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}






}
