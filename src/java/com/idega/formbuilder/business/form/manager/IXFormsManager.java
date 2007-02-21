package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.ConstUpdateType;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormComponentContainer;
import com.idega.formbuilder.business.form.beans.IFormComponentDocument;
import com.idega.formbuilder.business.form.beans.IFormComponentPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public interface IXFormsManager {

	public abstract void setCacheManager(CacheManager cache_manager);

	public abstract void setComponentParent(IFormComponentContainer component_parent);

	/**
	 * Gets full component by component type. 
	 * 
	 * @param component_type - used to find correct xforms component implementation
	 * @return element node.
	 * @throws NullPointerException - component implementation could not be found by component type
	 */
	public abstract void loadXFormsComponentByType(String component_type)
			throws NullPointerException;

	public abstract void loadXFormsComponentFromDocument(String component_id);

	public abstract void addComponentToDocument();

	public abstract void update(ConstUpdateType what);
	
	public abstract void setFormComponent(IFormComponent component);

	public abstract void moveComponent(String before_component_id);

	public abstract void removeComponentFromXFormsDocument();

	public abstract String insertBindElement(Element new_bind_element,
			String bind_id);

	public abstract void changeBindName(String new_bind_name);
	
	public abstract LocalizedStringBean getLocalizedStrings();
	
	public abstract LocalizedStringBean getErrorLabelLocalizedStrings();
	
	public abstract Element getComponentElement();
	
	public abstract Element getComponentNodeset();
	
	public abstract Element getComponentBind();
	
	public abstract Element getComponentPreview();
	
	public abstract void setFormDocument(IFormComponentDocument form_document);
	
	public abstract void loadConfirmationElement(IFormComponentPage confirmation_page);
	
	public abstract String getAutofillKey();
}