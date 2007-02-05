package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.div.Div;

public class FBDivision extends Div {
	
	public static final String COMPONENT_TYPE = "Division";
	
	public String id;
	public String styleClass;
	public String onclick;
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = onclick;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		onclick = (String) values[3];
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		if(id != null && !id.equals("")) {
			writer.writeAttribute("id", getId(), null);
		}
		if(styleClass != null && !styleClass.equals("")) {
			writer.writeAttribute("styleClass", getStyleClass(), null);
		}
		if(onclick != null && !onclick.equals("")) {
			writer.writeAttribute("onClick", getOnclick(), null);
		}
	}
		
	
	/*public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		super.encodeChildren(context);
	}*/

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
