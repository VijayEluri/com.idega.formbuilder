package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.idega.builder.business.BuilderLogic;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormPage;
import com.idega.formbuilder.presentation.components.FBVariableViewer;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_ID = "formPage";
	
	private static final String FORM_ELEMENT = "formElement formElementHover";
	
	private Page page;
	private String id;
	private boolean special;
	
	private FormDocument formDocument;
	private Workspace workspace;
	
	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public FormDocument getFormDocument() {
		return formDocument;
	}

	public void setFormDocument(FormDocument formDocument) {
		this.formDocument = formDocument;
	}

	public Page initializeBeanInstance(Page page) {
		if(page != null) {
			this.page = page;
			this.id = page.getId();
			this.special = false;
		} else {
			this.page = null;
			this.id = null;
			this.special = false;
		}
		
		return page;
	}
	
	public Page initializeBeanInstance(Page page, boolean special) {
		initializeBeanInstance(page);
		this.special = special;
		
		return page;
	}
	
	public boolean hasRegularComponents() {
		if(page == null) {
			return false;
		}
		
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			return page.getContainedComponentsIds().size() > 1;
		} else {
			return !page.getContainedComponentsIds().isEmpty();
		}
	}
	
	public org.jdom.Document[] getThxPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = null;//document.getThxPage();
			if(page != null) {
				initializeBeanInstance(page, true);
			}
		}
		
		ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
		propertyManager.resetComponent();
		
		org.jdom.Document[] result = new org.jdom.Document[2];
		result[0] = getDesignView(FORM_ELEMENT);
		result[1] = getPropertiesPanel(null);

		return result;
	}
	
	private org.jdom.Document getDesignView(String elementStyleClass) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(elementStyleClass), false);
	}
	
	private org.jdom.Document getPropertiesPanel(GenericComponent component) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component),true);
	}
	
	public void updateComponentList(List<String> idSequence) throws Exception {
		if(page != null) {
			List<String> ids = page.getContainedComponentsIds();
			ButtonArea area = page.getButtonArea();
			String buttonAreaId = null;
			if(area != null) {
				buttonAreaId = area.getId();
			}
			ids.clear();
			ids.addAll(idSequence);
			if(buttonAreaId != null) {
				ids.add(buttonAreaId);
			}
			page.rearrangeComponents();
		} else {
			throw new Exception("Page component missing");
		}
	}
	
	public void updateButtonList(List<String> idSequence) throws Exception {
		if(page == null) {
			return;
		}
		
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			List<String> ids = area.getContainedComponentsIds();
			ids.clear();
			ids.addAll(idSequence);
			area.rearrangeComponents();
		}
	}
	
	public Object[] removePage(String id) throws Exception {
		Object[] result = new Object[4];
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getPage(id);
			if(page != null) {
				List<String> ids = formDocument.getCommonPagesIdList();
				int index = ids.indexOf(id);
				String newPageId = CoreConstants.EMPTY;
				if(index < 1 && ids.size() > 1) {
					newPageId = ids.get(1);
				} else if(index >= 1) {
					newPageId = ids.get(index - 1);
				}
				
				if(workspace.isProcessMode()) {
					ProcessData processData = (ProcessData) WFUtil.getBeanInstance(ProcessData.BEAN_ID);
					
					processData.unbindVariables(page.getContainedComponentsIds());
					
					ButtonArea buttonArea = page.getButtonArea();
					
					if(buttonArea != null) {
						processData.unbindTransitions(buttonArea.getContainedComponentsIds());
					}
					
					result[3] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBVariableViewer(),true);
				}
				
				if(!StringUtils.isEmpty(newPageId)) {
					page.remove();
					page = document.getPage(newPageId);
					initializeBeanInstance(page);
				}
				
				workspace.setView(FBViewPanel.DESIGN_VIEW);
				
				ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
				propertyManager.resetComponent();
				
				result[0] = newPageId;
				result[1] = getDesignView(FORM_ELEMENT);
				result[2] = getPropertiesPanel(null);
			}
		}
		return result;
	}
	
	public org.jdom.Document[] getFormPageInfo(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		Document document = formDocument.getDocument();
		if(document != null) {
			initializeBeanInstance(document.getPage(id));
		}
		
		ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
		propertyManager.resetComponent();
		
		org.jdom.Document[] result = new org.jdom.Document[2];
		result[0] = getDesignView(FORM_ELEMENT);
		result[1] = getPropertiesPanel(null);
		
		return result;
	}
	
	public org.jdom.Document[] getConfirmationPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = null;//document.getConfirmationPage();
			if(page != null) {
				initializeBeanInstance(page, true);
			}
		}
		
		ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
		propertyManager.resetComponent();
		
		org.jdom.Document[] result = new org.jdom.Document[2];
		result[0] = getDesignView(FORM_ELEMENT);
		result[1] = getPropertiesPanel(null);

		return result;
	}
	
	public org.jdom.Document[] createNewPage() throws Exception {
		Document document = formDocument.getDocument();
		
		org.jdom.Document[] result = new org.jdom.Document[3];
		if(document != null) {
//			String temp = null;
//			if(document.getConfirmationPage() != null) {
//				temp = document.getConfirmationPage().getId();
//			} else {
//				temp = document.getThxPage().getId();
//			}
			Page page = document.addPage(null);
//			TODO: no null here ever. Don't keep silent about errors, if those are tracked (or don't track them)!! 
			if(page != null) {
				initializeBeanInstance(page, false);
				workspace.setView(FBViewPanel.DESIGN_VIEW);
				
				ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
				propertyManager.resetComponent();
				
				result[0] = getDesignView(FORM_ELEMENT);
				result[1] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormPage(id + "_P", page.getProperties().getLabel().getString(FBUtil.getUILocale())), true);
				result[2] = getPropertiesPanel(null);
			}
		}
		
		return result;
	}
	
	public List<String> getAssignedVariables(Page page) {
		List<String> list = new ArrayList<String>();
		if(page != null) {
			List<String> components = page.getContainedComponentsIds();
			for(Iterator<String> it = components.iterator(); it.hasNext(); ) {
				String componentId = it.next();
				Component component = page.getComponent(componentId);
				String variableName = component.getProperties().getVariable().getDefaultStringRepresentation();
				if(variableName != null) {
					list.add(variableName);
				}
			}
		}
		return list;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}

	public String getTitle() {
		if(page == null) {
			return CoreConstants.EMPTY;
		}
		return FBUtil.getPropertyString(page.getProperties().getLabel().getString(FBUtil.getUILocale()));
	}

	public void setTitle(String title) {
		if(page != null) {
			LocalizedStringBean bean = page.getProperties().getLabel();
			bean.setString(FBUtil.getUILocale(), title);
			page.getProperties().setLabel(bean);
		}
	}
	
	public String saveTitle(String title) {
		setTitle(title);
		return title;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
	

}
