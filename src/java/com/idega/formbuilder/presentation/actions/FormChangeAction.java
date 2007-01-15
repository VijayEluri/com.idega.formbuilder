package com.idega.formbuilder.presentation.actions;

import java.util.Locale;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class FormChangeAction implements ActionListener {
	
	public void processAction(ActionEvent ae) {
		String formId = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getFormId();
		if(formId != null && !formId.equals("") && !formId.equals("INACTIVE")) {
			try {
				ActionManager.getFormManagerInstance().openFormDocument(formId);
				if(ActionManager.getFormManagerInstance().getFormComponentsIdsList().size() > 0) {
					((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus("active");
				} else {
					((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus("empty");
				}
				((Workspace) WFUtil.getBeanInstance("workspace")).setView("design");
				((Workspace) WFUtil.getBeanInstance("workspace")).setRenderedMenu(true);
				((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("0");
				IFormManager formManagerInstance = ActionManager.getFormManagerInstance();
				((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormTitle(formManagerInstance.getFormTitle().getString(new Locale("en")));
				((FormDocument) WFUtil.getBeanInstance("formDocument")).setSubmitLabel(formManagerInstance.getSubmitButtonProperties().getLabel().getString(new Locale("en")));
//				((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormTitle(ActionManager.getFormManagerInstance().getFormTitle().getString(new Locale("en")));
//				((FormDocument) WFUtil.getBeanInstance("formDocument")).setSubmitLabel(ActionManager.getFormManagerInstance().getFormTitle().getString(new Locale("en")));
//				this.currentComponent = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}