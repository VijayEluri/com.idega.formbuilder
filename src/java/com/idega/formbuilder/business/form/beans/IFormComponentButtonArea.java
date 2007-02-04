package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentButtonArea extends IFormComponentContainer {

	public abstract void setButtonMapping(String button_type, String button_id);
	
	public abstract void setPageSiblings(IFormComponentPage previous, IFormComponentPage next);
	
	public abstract IFormComponentPage getPreviousPage();
	
	public abstract IFormComponentPage getNextPage();
	
	public abstract IFormComponentPage getCurrentPage();
}