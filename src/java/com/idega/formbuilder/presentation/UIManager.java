package com.idega.formbuilder.presentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.component.html.ext.HtmlInputHidden;
import org.apache.myfaces.component.html.ext.HtmlPanelGrid;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTabbedPane;
import org.apache.myfaces.custom.tabbedpane.TabChangeEvent;
import org.apache.myfaces.custom.tabbedpane.TabChangeListener;

import com.idega.formbuilder.business.ComponentPalette;
import com.idega.formbuilder.business.FormField;
import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.manager.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.presentation.IWContext;

public class UIManager implements TabChangeListener {
	
	private ComponentPalette palette;
	private static List fields = new ArrayList();
	private int elementCount;
	private static IFormManager fb = null;
	
	private HtmlPanelTabbedPane optionsPane = null;
	private HtmlInputHidden selectedFieldType = null;
	private HtmlInputHidden selectedFieldId = null;
	private Div formView = null;
	
	private String selectedFieldTypeValue;
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSelectedFieldTypeValue() {
		return selectedFieldTypeValue;
	}

	public void setSelectedFieldTypeValue(String selectedFieldTypeValue) {
		this.selectedFieldTypeValue = selectedFieldTypeValue;
	}

	public HtmlInputHidden getSelectedFieldType() {
		return selectedFieldType;
	}

	public void setSelectedFieldType(HtmlInputHidden selectedFieldType) {
		this.selectedFieldType = selectedFieldType;
	}

	public UIManager() {
		if(fb == null) {
			System.out.println("XIXIXIXIXIXIXIXIXIXIXIXI");
			try {
				System.out.println("XIXIXIXIXIXIXIXIXIXIXIXI");
				fb = FormManagerFactory.newFormManager(IWContext.getInstance());
				if(fb == null) {
					System.out.println("XIXIXIXIXIXIXIXIXIXIXIXI");
				}
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("formbuilderInstance", fb);
			} catch(InstantiationException e) {
				e.printStackTrace();
			}
		}
		if(fields == null || fields.size() == 0) {
			List components = fb.getAvailableFormComponentsList();
			Iterator it = components.iterator();
			FormField temp = null;
			while(it.hasNext()) {
				temp = new FormField((String) it.next());
				fields.add(temp);
			}
		}
	}

	public ComponentPalette getPalette() {
		return palette;
	}
	
	public void saveForm() {
		
	}
	
	public void newForm() throws Exception {
		this.saveForm();
		clearFormView();
		FormPropertiesBean formProperties = new FormPropertiesBean();
		formProperties.setId(123L);
		fb.createFormDocument(formProperties);
		
	}
	
	public void deleteForm() {
		
	}
	
	private void clearFormView() {
		getFormView().getChildren().clear();
	}
	
	public void addFormField() {
		System.out.println("INSIDE METHOD");
		Application application = FacesContext.getCurrentInstance().getApplication();
		try {
			String id = "form_component_" + new Integer(elementCount++).toString();
			List children = getFormView().getChildren();
	        Div field = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        /*AjaxSupport uias = (AjaxSupport) application.createComponent(UIAjaxSupport.COMPONENT_TYPE);
	        uias.setEvent("onclick");
	        uias.setReRender(optionsPane);
	        field.getChildren().add(uias);*/
	        /*HtmlAjaxSupport support = (HtmlAjaxSupport) application.createComponent(UIAjaxSupport.COMPONENT_TYPE);
	        support.setEvent("onclick");
	        support.setReRender(optionsPane);
	        field.getFacets().put(AjaxSupportTag.AJAX_SUPPORT_FACET + "onclick", support);*/
	        field.setStyleClass("form_element");
	        field.setId(id);
	        field.getAttributes().put("forceId", "true");
	        HtmlPanelGrid fieldInnerStructure = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
	        fieldInnerStructure.setColumns(2);
	        fieldInnerStructure.setOnclick("selectFormField(this)");
	        //fieldInnerStructure.setOnmouseover("hoverOverComponent(this)");
	        fieldInnerStructure.setStyleClass("field_inner_structure");
	        Div fieldInnerHtml = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        fieldInnerHtml.setStyleClass("field_html_zone");
	        Div fieldButtons = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        fieldButtons.setStyleClass("hot_button_zone");
	        HtmlCommandLink deleteButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
	        //UIAjaxCommandLink deleteButton = (UIAjaxCommandLink) application.createComponent(UIAjaxCommandLink.COMPONENT_TYPE);
	        deleteButton.setValue("DELETE");
	        deleteButton.setStyleClass("hot_button");
	        HtmlCommandLink cloneButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
	        cloneButton.setValue("CLONE");
	        cloneButton.setStyleClass("hot_button");
	        //fieldButtons.getChildren().add(deleteButton);
	        //fieldButtons.getChildren().add(cloneButton);
	        FBGenericFormComponent genericField = (FBGenericFormComponent) application.createComponent(FBGenericFormComponent.COMPONENT_TYPE);
	        genericField.setType(this.getSelectedFieldTypeValue());
	        fieldInnerHtml.getChildren().add(genericField);
	        fieldInnerStructure.getChildren().add(fieldInnerHtml);
	        fieldInnerStructure.getChildren().add(fieldButtons);
	        field.getChildren().add(fieldInnerStructure);
	        children.add(field);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectFormField() {
		Application application = FacesContext.getCurrentInstance().getApplication();
		this.getOptionsPane().setSelectedIndex(2);
		/*if (AAUtils.isAjaxRequest(getRequest())) {
            AAUtils.addZonesToRefresh(getRequest(), "panel");
        }
		this.getOptionsPane().setSelectedIndex(2);*/
	}
	
	private HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
	
	public void processTabChange(TabChangeEvent e) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("--------" + e.getComponent().getAttributes().get("id"));
	}

	public Div getFormView() {
		return formView;
	}

	public void setFormView(Div formView) {
		this.formView = formView;
	}

	public void setPalette(ComponentPalette palette) {
		this.palette = palette;
	}

	public HtmlInputHidden getSelectedFieldId() {
		return selectedFieldId;
	}

	public void setSelectedFieldId(HtmlInputHidden selectedFieldId) {
		this.selectedFieldId = selectedFieldId;
	}

	public HtmlPanelTabbedPane getOptionsPane() {
		return optionsPane;
	}

	public void setOptionsPane(HtmlPanelTabbedPane optionsPane) {
		this.optionsPane = optionsPane;
	}

	public List getFields() {
		return fields;
	}

	public void setFields(List fields) {
		this.fields = fields;
	}
}
