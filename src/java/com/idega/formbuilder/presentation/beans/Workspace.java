package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class Workspace implements Serializable, ActionListener {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String selectedMenu;
	private boolean renderedMenu;
	private String view;
	private String designViewStatus;
	private boolean pagesPanelVisible;
	
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Workspace() {
		this.renderedMenu = false;
		this.selectedMenu = "0";
		this.view = "design";
		this.designViewStatus = "noform";
		this.pagesPanelVisible = false;
		this.locale = new Locale("en");
	}
	
	public void changeForm(ActionEvent ae) {
		IFormManager formManagerInstance = ActionManager.getFormManagerInstance();
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		String formId = formDocument.getFormId();
		if(formId != null && !formId.equals("") && !formId.equals("INACTIVE")) {
			try {
				formManagerInstance.openFormDocument(formId);
				if(formManagerInstance.getFormComponentsIdsList().size() > 0) {
					workspace.setDesignViewStatus("active");
				} else {
					workspace.setDesignViewStatus("empty");
				}
				workspace.setView("design");
				workspace.setRenderedMenu(true);
				workspace.setSelectedMenu("0");
				String title = formManagerInstance.getFormTitle().getString(locale);
				String submit = formManagerInstance.getSubmitButtonProperties().getLabel().getString(locale);
				formDocument.clearFormDocumentInfo();
				formDocument.setFormTitle(title);
				formDocument.setSubmitLabel(submit);
				
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void togglePagesPanel(ActionEvent ae) {
		if(pagesPanelVisible) {
			pagesPanelVisible = false;
		} else {
			pagesPanelVisible = true;
		}
	}
	
	public void switchMenu() {
		Set keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
		if(keys != null) {
			if(keys.contains("workspaceform1:tab1Title")) {
				setSelectedMenu("0");
			} else if(keys.contains("workspaceform1:tab2Title")) {
				setSelectedMenu("1");
			} else if(keys.contains("workspaceform1:tab3Title")) {
				setSelectedMenu("2");
			}
		}
	}
	
	public void changeMenu(ActionEvent ae) {
		String senderId = ae.getComponent().getId();
		String menuPanelId = senderId.substring(0, 4);
		if(menuPanelId.equals("tab1")) {
			setSelectedMenu("0");
		} else if(menuPanelId.equals("tab2")) {
			setSelectedMenu("1");
		} else if(menuPanelId.equals("tab3")) {
			setSelectedMenu("2");
		} else if(menuPanelId.equals("tab4")) {
			setSelectedMenu("3");
		}
	}
	
	public void processAction(ActionEvent ae) {
		String senderId = ae.getComponent().getId();
		String menuPanelId = senderId.substring(0, 4);
		if(menuPanelId.equals("tab1")) {
			setSelectedMenu("0");
		} else if(menuPanelId.equals("tab2")) {
			setSelectedMenu("1");
		} else if(menuPanelId.equals("tab3")) {
			setSelectedMenu("2");
		} else if(menuPanelId.equals("tab4")) {
			setSelectedMenu("3");
		}
	}

	public boolean isRenderedMenu() {
		return renderedMenu;
	}

	public void setRenderedMenu(boolean renderedMenu) {
		this.renderedMenu = renderedMenu;
	}

	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getDesignViewStatus() {
		return designViewStatus;
	}

	public void setDesignViewStatus(String designViewStatus) {
		this.designViewStatus = designViewStatus;
	}

	public boolean isPagesPanelVisible() {
		return pagesPanelVisible;
	}

	public void setPagesPanelVisible(boolean pagesPanelVisible) {
		this.pagesPanelVisible = pagesPanelVisible;
	}

}
