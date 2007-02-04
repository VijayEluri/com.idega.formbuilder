package com.idega.formbuilder.business.form.manager.generators;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.adapter.ui.UIGenerator;
import org.chiba.adapter.ui.XSLTGenerator;
import org.chiba.xml.dom.DOMUtil;
import org.chiba.xml.xforms.exception.XFormsException;
import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.block.formreader.business.FormReader;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 * Initial components description is kept in xforms document.<br />
 * This class parses all those components to html format components into xml document.
 * 
 */
public class FormComponentsGenerator implements Singleton, IComponentsGenerator  {
	
	protected static FormComponentsGenerator me = null;
	
	private final URI final_xml_stylesheet_uri =
		URI.create("bundle://"+IWBundleStarter.IW_BUNDLE_IDENTIFIER+"/"+"resources/xslt/components.xsl");
	private final URI temporal_xml_stylesheet_uri =
		URI.create("bundle://"+IWBundleStarter.IW_BUNDLE_IDENTIFIER+"/"+"resources/xslt/htmlxml.xsl");
	
	private TransformerService transf_service;
	private Document xforms_doc;

//	private String base_form_uri;

	private UIGenerator temporal_xml_components_generator;
	private UIGenerator final_xml_components_generator;
	private FormReader form_reader;	
	
	public static FormComponentsGenerator getInstance() {
		
		if (me == null) {
			
			synchronized (FormComponentsGenerator.class) {
				if (me == null) {
					me = new FormComponentsGenerator();
				}
			}
		}

		return me;
	}
	
	protected FormComponentsGenerator() { }

	public boolean isInitiated() {
		return xforms_doc != null && transf_service != null;
	}
	
	public void setDocument(Document xforms_doc) {
		this.xforms_doc = xforms_doc;
	}
	
	public Document generateBaseComponentsDocument() throws NullPointerException, ParserConfigurationException, XFormsException, Exception {
		
		if(!isInitiated()) {
			
			String err_msg = new StringBuffer("Either is not provided:")
			.append("\nxforms doc: ")
			.append(xforms_doc)
			.append("\ntransformer service: ")
			.append(transf_service)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
        /*
         * generate temporal xml document from components xforms document
         */
        UIGenerator gen = getTemporalXmlComponentsGenerator();
        
    	copyLocalizationKeysToElements(xforms_doc);
    	gen.setInput(xforms_doc);
    	
    	DocumentBuilder document_builder = FormManagerUtil.getDocumentBuilder();
        Document temp_xml_doc = document_builder.newDocument();
        gen.setOutput(temp_xml_doc);
    	gen.generate();
    	
    	
    	/*
    	 * generate final components xml
    	 */
    	gen = getFinalXmlComponentsGenerator();
    	gen.setInput(temp_xml_doc);
    	
    	temp_xml_doc = document_builder.newDocument();
    	gen.setOutput(temp_xml_doc);
    	
    	gen.generate();
    	
    	return temp_xml_doc;
	}
/*	
	public Document generateFormHtmlDocument() throws NullPointerException, ParserConfigurationException, XFormsException, Exception {
		
		if(!isInitiated()) {
			
			String err_msg = new StringBuffer("Either is not provided:")
			.append("\nstylesheet uri: ")
			.append(final_xml_stylesheet_uri)
			.append("\nxforms doc: ")
			.append(xforms_doc)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
		
//      generate temporal xml document from components xforms document
         
        FormReader form_reader = getFormReader();
        form_reader.setBaseFormURI(base_form_uri);
        
        FormManagerUtil.clearLocalizedMessagesFromDocument(xforms_doc);
    	copyLocalizationKeysToElements(xforms_doc);
    	
    	form_reader.setFormDocument(xforms_doc);
    	
    	DocumentBuilder document_builder = FormManagerUtil.getDocumentBuilder();
        Document temp_xml_doc = document_builder.newDocument();

        form_reader.setOutput(temp_xml_doc);
        
        form_reader.generate();
        
//    	generate final components xml
    	UIGenerator gen = getFinalXmlComponentsGenerator();
    	gen.setInput(temp_xml_doc);
    	
    	temp_xml_doc = document_builder.newDocument();
    	gen.setOutput(temp_xml_doc);
    	
    	gen.generate();
    	
    	return temp_xml_doc;
	}
*/
	
	private static void copyLocalizationKeysToElements(Document managed_doc) {
		
		Element components_container = (Element)managed_doc.getElementsByTagName(FormManagerUtil.group_tag).item(0);
		
		NodeList child_elements = components_container.getElementsByTagName("*");
		
		for (int i = 0; i < child_elements.getLength(); i++) {
			Element child = (Element)child_elements.item(i);
			
			if(child.hasAttribute(FormManagerUtil.ref_s_att)) {
				
				String ref = child.getAttribute(FormManagerUtil.ref_s_att);
				
				if(!FormManagerUtil.isRefFormCorrect(ref))
					continue;
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				Node key_node = managed_doc.createTextNode(key);
				child.appendChild(key_node);
			}
		}
	}
	
	public void setTransformerService(TransformerService transf_service) {
		this.transf_service = transf_service;
	}
	
	protected UIGenerator getTemporalXmlComponentsGenerator() {
		
		if(temporal_xml_components_generator == null) {
			
			synchronized (this) {
				
				if(temporal_xml_components_generator == null) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transf_service);
					gen.setStylesheetURI(temporal_xml_stylesheet_uri);
					
					temporal_xml_components_generator = gen;
				}
			}
		}
		return temporal_xml_components_generator;
	}
	
	protected FormReader getFormReader() throws Exception {
		if(form_reader == null) {
			synchronized (this) {
				if(form_reader == null) {
					this.form_reader = new FormReader();
				}
			}
		}
		return form_reader;
	}
	
//	public void setFormComponentsBaseUri(String base_uri) {
//		base_form_uri = base_uri;
//	}
	
	protected UIGenerator getFinalXmlComponentsGenerator() {
		
		if(final_xml_components_generator == null) {
			
			synchronized (this) {
				
				if(final_xml_components_generator == null) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transf_service);
					gen.setStylesheetURI(final_xml_stylesheet_uri);
					
					final_xml_components_generator = gen;
				}
			}
		}
		return final_xml_components_generator;
	}
}
