package com.idega.formbuilder.business.form;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface Page extends Container {

	public abstract PropertiesPage getProperties();
	
	public abstract ButtonArea getButtonArea();
	
	public abstract ButtonArea createButtonArea(String component_after_this_id) throws NullPointerException;
}