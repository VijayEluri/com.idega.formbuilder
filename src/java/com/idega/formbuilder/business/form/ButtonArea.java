package com.idega.formbuilder.business.form;


/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 *
 */
public interface ButtonArea extends Container {

	public abstract Button getButton(ConstButtonType button_type);
	
	public abstract Button addButton(ConstButtonType button_type, String component_after_this_id) throws NullPointerException;
}