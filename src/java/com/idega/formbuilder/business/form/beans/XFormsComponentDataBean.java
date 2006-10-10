package com.idega.formbuilder.business.form.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentDataBean implements Cloneable {
	
	private Element element;
	private Element bind;
	private Element nodeset;
	
	public Element getBind() {
		return bind;
	}
	public void setBind(Element bind) {
		this.bind = bind;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Element getNodeset() {
		return nodeset;
	}
	public void setNodeset(Element nodeset) {
		this.nodeset = nodeset;
	}
	
	public Object clone() {
		
		XFormsComponentDataBean clone = new XFormsComponentDataBean();
		
		clone.setElement((Element)element.cloneNode(true));
		clone.setBind((Element)bind.cloneNode(true));
		clone.setNodeset((Element)nodeset.cloneNode(true));
		
		return clone;
	}
}
