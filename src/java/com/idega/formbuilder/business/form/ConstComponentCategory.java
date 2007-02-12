package com.idega.formbuilder.business.form;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public final class ConstComponentCategory {

	public static final String BASIC = "BASIC";
	public static final String BUTTONS = "BUTTONS";
	public static final String NON_DISPLAY = "NON_DISPLAY";
	private static List<String> components_categories = new ArrayList<String>();
	
	private String components_category;
	
	static {
		components_categories.add(BASIC);
		components_categories.add(BUTTONS);
		components_categories.add(NON_DISPLAY);
	}
	
	public ConstComponentCategory(String button_type) {
		
		if(!components_categories.contains(button_type))
			throw new NullPointerException("Provided category not supported: "+button_type);
		
		this.components_category = button_type;
	}
	
	public String getComponentsCategory() {
		return components_category;
	}
	
	public static List<String> getComponentsCategories() {
		return components_categories;
	}
	
	@Override
	public String toString() {
		return "components category set: "+getComponentsCategory();
	}
}