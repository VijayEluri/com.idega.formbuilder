package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManagerPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentPage extends FormComponentContainer implements Page, IFormComponentPage {
	
	protected boolean first = false;
	protected String button_area_id;
	protected IFormComponentPage previous_page;
	protected IFormComponentPage next_page;
	
	public PropertiesPage getProperties() {
		
		if(properties == null) {
			ComponentPropertiesPage properties = new ComponentPropertiesPage();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesPage)properties;
	}
	
	@Override
	protected IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerPage();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
		}
		
		return xforms_manager;
	}
	
	public void setFirst(boolean first) {
		super.setFirst(first);
		((XFormsManagerPage)getXFormsManager()).changePageRelevance(first);
	}
	
	@Override
	public void remove() {
		super.remove();
		parent.componentsOrderChanged();
	}
	
	public ButtonArea getButtonArea() {
		
		return button_area_id == null ? null : (ButtonArea)getContainedComponent(button_area_id);
	}
	public ButtonArea createButtonArea(String component_after_this_id) throws NullPointerException {
		
		if(getButtonArea() != null)
			throw new IllegalArgumentException("Button area already exists in the page, remove first");
		
		return (ButtonArea)addComponent(FormComponentFactory.fbcomp_button_area, component_after_this_id);
	}
	public void setButtonAreaComponentId(String button_area_id) {
		this.button_area_id = button_area_id;
	}
	public void setPageSiblings(IFormComponentPage previous, IFormComponentPage next) {
		
		next_page = next;
		previous_page = previous;
	}
	public void pagesSiblingsChanged() {
		IFormComponentButtonArea button_area = (IFormComponentButtonArea)getButtonArea();
		if(button_area == null)
			return;
		
		button_area.setPageSiblings(previous_page, next_page);
	}
	public IFormComponentPage getPreviousPage() {
		return previous_page;
	}
	public IFormComponentPage getNextPage() {
		return next_page;
	}
}