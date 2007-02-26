package com.idega.formbuilder.business.form;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
 */
public interface PropertiesThankYouPage extends PropertiesPage {
	
	public abstract LocalizedStringBean getText();
	
	public abstract void setText(LocalizedStringBean text);
}