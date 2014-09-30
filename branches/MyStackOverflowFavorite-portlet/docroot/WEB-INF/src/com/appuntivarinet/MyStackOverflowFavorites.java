package com.appuntivarinet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.liferay.portal.util.PortalUtil;

public class MyStackOverflowFavorites extends GenericPortlet {
	
	public static Log log = LogFactory.getLog("MyStackOverflowFavorites");
	
	/* initialize the default parameter of "portlet.xml" */
	protected String editJSP;
	protected String viewJSP;
	
	public void init() throws PortletException{
		editJSP = getInitParameter("edit-jsp");
		viewJSP = getInitParameter("view-jsp");
	}
	
	
	//set the Portlet's default View
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException{
		PortletPreferences prefs = renderRequest.getPreferences();
		
		String urlFavorite = (String) prefs.getValue("urlFavorite", "none");
		String urlProfile = (String) prefs.getValue("urlProfile", "none");
		
		/* Default value */
		if(urlFavorite.equalsIgnoreCase("none")){
			urlFavorite = "http://stackoverflow.com/users/2723164/pastuweb?tab=favorites";
		}
		if(urlProfile.equalsIgnoreCase("none")){
			urlProfile = "http://stackoverflow.com/users/2723164/pastuweb";
		}
		

		String listaFavorite = getListaFavorite(urlFavorite);
		
		
		renderRequest.setAttribute("urlFavorite", urlFavorite);
		renderRequest.setAttribute("urlProfile", urlProfile);
		renderRequest.setAttribute("listaFavorite", listaFavorite);
		
		
		include(viewJSP, renderRequest, renderResponse);
	}
	
	
	/* special method: used to dispatch to right JSP */
	protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException{
		PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(path);
		if(portletRequestDispatcher == null){
			log.info("path : "+path+" non e valido.");
		}else{
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}
	
	/* set the Portlet's default Edit: it's a simple <form> */
	public void doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException{
		
		renderResponse.setContentType("text/html");
		
		PortletURL saveStackOverflowParametersURL = renderResponse.createActionURL();
		saveStackOverflowParametersURL.setParameter("saveStackOverflowParameters", "saveStackOverflowParameters");
		renderRequest.setAttribute("saveStackOverflowParametersURL", saveStackOverflowParametersURL.toString());
		
		/*You can add other ACTION URL for the EDIT JSP*/
		
		include(editJSP, renderRequest, renderResponse);
		
	}
	
	
	/* ACTION call from Portlet's <form> of EDIT JSP */
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException{
		
		String saveStackOverflowParameters = actionRequest.getParameter("saveStackOverflowParameters");
		/*You can add other getParameter of EDIT JSP*/
		
		if(saveStackOverflowParameters != null){
			PortletPreferences prefs = actionRequest.getPreferences();
			prefs.setValue("urlFavorite", actionRequest.getParameter("inUrlFavorite"));
			prefs.setValue("urlProfile", actionRequest.getParameter("inUrlProfile"));

			prefs.store();
			actionResponse.setPortletMode(PortletMode.VIEW);
		}
		
		/*You can test other getParameter of EDIT JSP*/
		
	}
	
	
	
	public String getListaFavorite(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);
		
		String val_meta_info = new String("");		
		String val_titolo = new String("");
		String val_link = new String("");
		String val_votes = new String("");
		
		String content = new String("");
		String temp = new String("");

		
		HtmlPage page = browser.getPage(url);
		
		log.info("lettura pagina");
		HtmlElement favorite = page.getElementById("user-tab-favorites");		
		DomNodeList<HtmlElement> divs = favorite.getElementsByTagName("div");
		
		for (HtmlElement div : divs) {
			if(div.getAttribute("class").equals("user-tab-content")){
						
						DomNodeList<HtmlElement> divs_user_tab_content = div.getElementsByTagName("div");
						for (HtmlElement div_u_t_c : divs_user_tab_content) {

							if(div_u_t_c.getAttribute("class").equals("user-questions")){
								
								DomNodeList<HtmlElement> divs_user_questions= div_u_t_c.getElementsByTagName("div");
								for (HtmlElement div_u_q : divs_user_questions) {
									
									DomNodeList<HtmlElement> divs_q_summ= div_u_q.getElementsByTagName("div");
									for (HtmlElement div_q_summ : divs_q_summ) {
										
										if(div_q_summ.getAttribute("class").contains("question-counts")){
											//Voti
												HtmlElement votes = (HtmlElement)div_q_summ.getElementsByTagName("div").item(0);
												val_votes = ((HtmlElement)votes.getElementsByTagName("div").item(0)).getTextContent().trim();
												log.info("votes: "+ val_votes);
										}
										
										if(div_q_summ.getAttribute("class").contains("summary")){
																						
											//titolo
											val_titolo =  ((HtmlElement)div_q_summ.getFirstChild()).getTextContent().trim();
											log.info("titolo: "+val_titolo);
											
											//link del titolo
											val_link =  "http://stackoverflow.com"+((HtmlElement)((HtmlElement)div_q_summ.getFirstChild()).getFirstChild()).getAttribute("href").toString();
											log.info("link: "+val_link);
											
											
											temp = "<li><p style=\"position:relative;\"><a href=\""+val_link+"\" target=\"_blank\"> "+val_titolo+"</a>  <br> <span style=\"color:#FF8563;\"> ";

											
											//meta-info
											DomNodeList<HtmlElement> divs_meta_info= div_q_summ.getElementsByTagName("div");
											for (HtmlElement item_meta_info : divs_meta_info) {
												if(item_meta_info.getAttribute("class").contains("tags")){
												
													val_meta_info = new String("");
													DomNodeList<HtmlElement> meta_info= item_meta_info.getElementsByTagName("a");
													for (HtmlElement a_meta_info : meta_info) {
														if(a_meta_info.getAttribute("class").contains("post-tag")){
															val_meta_info = val_meta_info+""+a_meta_info.getTextContent().trim()+",";
															
														}
													}
												}
												
												
											}
											log.info("meta-info: "+val_meta_info.substring(0, val_meta_info.length()-1));
											temp = temp + " "+val_meta_info.substring(0, val_meta_info.length()-1)+"</span> <span class=\"pastuweb_score\"> "+val_votes+" </span></p></li>";
											
											content = content + temp;
											temp = new String("");
										}
									}
									
									
									
								}
								
							}
						}

					}
				}
		
		
		return content;
	}
	
	
			
	
}
