package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IPersistenceManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class FormDocument implements IFormDocument, IFormComponentParent {
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Document form_xforms;
	private List<String> form_components_id_sequence;
	private List<String> form_xsd_contained_types_declarations;
	
	private int last_component_id = 0;
	private String form_id;
	private IPersistenceManager persistence_manager;
	
	private Map<String, IFormComponent> form_components;
	
	public void createDocument(String form_id, LocalizedStringBean form_name) throws NullPointerException {
		
		if(form_id == null)
			throw new NullPointerException("Form_id is not provided");
		
		this.form_id = form_id;
		
		Document form_xforms_template = CacheManager.getInstance().getFormXformsTemplateCopy();
		
		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms_template, "head", "form_id");
		model.setAttribute(FormManagerUtil.id_name, form_id);
		
		form_xforms = form_xforms_template;
		
		if(form_name != null) {
			
			Element title = (Element)form_xforms.getElementsByTagName("title").item(0);
			Element output = (Element)title.getElementsByTagName(FormManagerUtil.output).item(0);
			
			try {
				
				FormManagerUtil.putLocalizedText(null, null, output, form_xforms, form_name);
				
			} catch (Exception e) {
				logger.error("Could not set localized text for title element", e);
			}
		}
	}
	
	public void addComponent(IFormComponent component) {
		
		String component_id = FormManagerUtil.CTID+generateNewComponentId();
		
		component.setId(component_id);
		component.setFormDocument(this);
		
		component.render();
		
		getFormComponents().put(component_id, component);
	}
	
	protected int generateNewComponentId() {
		
		return ++last_component_id;
	}
	
	public Document getXformsDocument() {
		return form_xforms;
	}

	public List<String> getFormComponentsIdList() {
		
		if(form_components_id_sequence == null)
			form_components_id_sequence = new LinkedList<String>();
		
		return form_components_id_sequence;
	}

	public List<String> getFormXsdContainedTypesDeclarations() {
		
		if(form_xsd_contained_types_declarations == null)
			form_xsd_contained_types_declarations = new LinkedList<String>();
		
		return form_xsd_contained_types_declarations;
	}

	public IFormComponent getFormComponent(String component_id) {
		
//		TODO: if not found, scan xforms document and load if found
		return getFormComponents().get(component_id);
	}
	
	protected Map<String, IFormComponent> getFormComponents() {
		
		if(form_components == null)
			form_components = new HashMap<String, IFormComponent>();
		
		return form_components;
	}
	
	public Exception[] getSavedExceptions() {

		return persistence_manager.getSavedExceptions(); 
	}
	
	public void setPersistenceManager(IPersistenceManager persistance_manager) {
		
		if(persistance_manager != null)
			this.persistence_manager = persistance_manager;
	}
	
	public void persist() throws NullPointerException, InitializationException {
		
		if(persistence_manager == null)
			throw new NullPointerException("Persistance manager is not provided");
		
		if(!persistence_manager.isInitiated())
				persistence_manager.init(form_id);
		
		persistence_manager.persistDocument(form_xforms);
	}
	
	public void rearrangeDocument() throws NullPointerException {
		
		int size = form_components_id_sequence.size();
		
		for (int i = size-1; i >= 0; i--) {
			
			String component_id = form_components_id_sequence.get(i);
			
			if(form_components.containsKey(component_id)) {
				
				IFormComponent comp = form_components.get(component_id);
				
				if(i != size-1) {
					
					comp.setComponentAfterThisRerender(
						form_components.get(
								form_components_id_sequence.get(i+1)
						)
					);
				} else
					comp.setComponentAfterThisRerender(null);
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
		}
	}
}