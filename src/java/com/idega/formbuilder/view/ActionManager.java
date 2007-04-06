package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.webface.WFUtil;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.documentmanager.business.DocumentManagerService;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.manager.util.InitializationException;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	private static Log logger = LogFactory.getLog(ActionManager.class);
	private PersistenceManager persistence_manager;
	public static final String ACTION_MANAGER_MANAGED_BEAN = "actionManager";
	
	private DocumentManager documentManagerInstance;
	
	public ActionManager() {
		
	}
	
	public DocumentManager getDocumentManagerInstance() {
		if(documentManagerInstance == null) {
			return createNewInstance();
		} else {
			return documentManagerInstance;
		}
	}
	
	private DocumentManager createNewInstance() {
		try {
			documentManagerInstance = getDocumentManagerService().newDocumentManager(FacesContext.getCurrentInstance());
			documentManagerInstance.setPersistenceManager(getPersistenceManager());
			
		} catch(InitializationException ie) {
			ie.printStackTrace();
		}
		return documentManagerInstance;
	}
	
	protected DocumentManagerService getDocumentManagerService() {
		
		try {
			IWApplicationContext iwc = IWMainApplication.getDefaultIWApplicationContext();
			return (DocumentManagerService) IBOLookup.getServiceInstance(iwc, DocumentManagerService.class);
		} catch (IBOLookupException e) {
			logger.error("Could not find DocumentManagerService", e);
		}
		return null;
	}
	
	public static ActionManager getCurrentInstance() {
		
		return (ActionManager)WFUtil.getBeanInstance(ACTION_MANAGER_MANAGED_BEAN);
	}
	
	private PersistenceManager getPersistenceManager() {
		
		return persistence_manager;
	}
	
	public void setPersistenceManager(PersistenceManager persistence_manager) {
		this.persistence_manager = persistence_manager;
	}
}