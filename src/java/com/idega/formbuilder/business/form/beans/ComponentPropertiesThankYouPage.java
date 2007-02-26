package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.PropertiesThankYouPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ComponentPropertiesThankYouPage extends ComponentPropertiesPage implements PropertiesThankYouPage {

	private LocalizedStringBean text;

	public LocalizedStringBean getText() {
		return text;
	}

	public void setText(LocalizedStringBean text) {
		this.text = text;
		parent_component.update(new ConstUpdateType(ConstUpdateType.thankyou_text));
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}