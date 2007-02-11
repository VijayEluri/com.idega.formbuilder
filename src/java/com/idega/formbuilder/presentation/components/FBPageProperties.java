package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.ajax4jsf.ajax.html.HtmlAjaxCommandButton;
import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;

public class FBPageProperties extends FBComponentBase {
	
public static final String COMPONENT_TYPE = "PageProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	
	public FBPageProperties() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		Table2 table = new Table2();
		table.setStyleAttribute("width: 300px;");
		table.setCellpadding(0);
		TableRowGroup group = table.createBodyRowGroup();
		TableRow row = null;
		TableCell2 cell = null;
		
		HtmlOutputText titleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		titleLabel.setValue("Page title");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("pageTitle");
		title.setValueBinding("value", application.createValueBinding("#{formPage.title}"));
//		title.setOnblur("savePageTitle();");
		title.setOnblur("$('workspaceform1:savePageTitle').click();");
		
		cell = row.createCell();
		cell.add(title);
		
		HtmlAjaxCommandButton newPageButton = (HtmlAjaxCommandButton) application.createComponent(HtmlAjaxCommandButton.COMPONENT_TYPE);
		newPageButton.setValue("New page");
		newPageButton.setId("newPB_PP");
		newPageButton.setOnclick("$('workspaceform1:createNewPage').click();");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(newPageButton);
		
		addFacet(CONTENT_FACET, table);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		UIComponent body = getFacet(CONTENT_FACET);
		if(body != null) {
			renderChild(context, body);
		}
	}

}
