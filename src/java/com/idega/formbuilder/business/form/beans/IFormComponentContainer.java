package com.idega.formbuilder.business.form.beans;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.Component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentContainer extends IFormComponent {

	public abstract Document getXformsDocument();
	
	public abstract IFormComponent getContainedComponent(String component_id);
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();
	
	public abstract Locale getDefaultLocale();
	
	public abstract Element getWizardElement();
	
	public abstract void setWizardElement(Element wizard_element);
	
	public abstract Component addComponent(String component_type, String component_after_this_id) throws NullPointerException;

	public abstract Component getComponent(String component_id);
	
	public abstract void tellComponentId(String component_id);
	
	public abstract String generateNewComponentId();
	
	public abstract void rearrangeComponents();
	
	public abstract List<String> getContainedComponentsIdList();
	
	public abstract void componentsOrderChanged();
	
	public abstract void unregisterComponent(String component_id);
}