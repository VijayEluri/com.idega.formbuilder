package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jdom.Document;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.util.FBUtil;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.jbpm.def.Variable;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class FormComponent extends GenericComponent {
	
	public static final String BEAN_ID = "formComponent";
	
	public static final String BUTTON_TYPE = "button";
	public static final String COMPONENT_TYPE = "component";
	
	private Component component;
	
	private ProcessPalette processPalette;
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	private ProcessData processData;
	private FormPage formPage;
	
	public FormComponent() {}
	
	public FormComponent(Component component) {
		this.component = component;
	}
	
	public FormPage getFormPage() {
		return formPage;
	}
	
	public String getId() {
		return component == null ? "" : component.getId();
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}
	
	public String assignVariable(String componentId, String variable, String datatype) {
		if(componentId == null) {
			componentId = component.getId();
		}
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			PropertiesComponent properties = component.getProperties();
			if(properties != null) {
				properties.setVariable(Variable.parseDefaultStringRepresentation(datatype + CoreConstants.COLON + variable));
				return processData.bindVariable(componentId, datatype + CoreConstants.COLON + variable).getStatus();
			}
		}
		return null;
	}
	
	public String assignTransition(String buttonId, String transition) {
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					properties.setReferAction(transition);
					return processData.bindTransition(buttonId, transition).getStatus();
				}
			}
		}
		return null;
	}
	
//	public void saveComponentAction(String value) {
//
//		if(button != null)
//			button.getProperties().setReferAction(value);
//	}
	
//	public void saveComponentProcessVariableName(String value) {
//		
//		Component component = this.component != null ? this.component : this.selectComponent != null ? this.selectComponent : this.plainComponent != null ? this.plainComponent : null;
//		
//		if(component != null)
//			component.getProperties().setVariable(value);
//	}
	
	public List<AdvancedProperty> getAvailableComponentVariables(String type) {
		Set<String> variables = getProcessData().getComponentTypeVariables(type);
		List<AdvancedProperty> result = new ArrayList<AdvancedProperty>();
		for(String var : variables) {
			result.add(new AdvancedProperty(var, var));
		}
		return result;
	}
	
	public String getDataSrc() {return null;}

	public void setDataSrc(String dataSrc) {}
	
//	public Document assignComponentToVariable(String variable, String componentId, String type) {
//		FBAssignVariableComponent newAssign = new FBAssignVariableComponent();
//		newAssign.setId(componentId + "-" + type);
//		if(variable != null) {
//			Page page = formPage.getPage();
//			if(page != null) {
//				Component component = page.getComponent(componentId);
//				PropertiesComponent properties = component.getProperties();
//				if(properties != null) {
//					properties.setVariable(Variable.parseDefaultStringRepresentation(variable));
//				}
//			}
//			newAssign.setValue(variable.substring(variable.indexOf(":") + 1));
//		}
//		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), newAssign, true);
//	}
	
	public Document addComponent(String type) throws Exception {
		if(type == null) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(formPage.isSpecial()) {
			return null;
		}
		if(page != null) {
			String before = null;
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				before = area.getId();
			}
			Component component = page.addComponent(type, before);
			if(component != null) {
				Document doc = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component), true);
				return doc;
			}
		}
		return null;
	}
	
	public String moveComponent(String id, int before) throws Exception {
		if(before == -1) {
			return "append";
		} else {
			Page page = formPage.getPage();
			String beforeId = CoreConstants.EMPTY;
			if(page != null) {
				List<String> ids = page.getContainedComponentsIdList();
				if(ids.indexOf(id) != -1) {
					beforeId = ids.get(before);
					ids.remove(id);
					ids.add(before, id);
				}
				page.rearrangeComponents();
			}
			return beforeId;
		}
	}
	
	public Document addButton(String type) {
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			Button button = null;
			if(area != null) {
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			} else {
				area = page.createButtonArea(null);
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			}
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
		}
		return null;
	}
	
	public String removeComponent(String id) {
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(id);
			if(component != null) {
				component.remove();
			}
		}
		return id;
	}
	
	public String removeButton(String id) {
		if(id == null) {
			return null;
		}
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(id);
				if(button != null) {
					button.remove();
					return id;
				}
			}
		}
		return null;
	}

	public String getErrorMessage() {
		return getComponent().getProperties().getErrorMsg().getString(FBUtil.getUILocale());
	}

	public void setErrorMessage(String errorMessage) {
		LocalizedStringBean bean = getComponent().getProperties().getErrorMsg();
		bean.setString(FBUtil.getUILocale(), errorMessage);
		getComponent().getProperties().setErrorMsg(bean);
	}

	public String getLabel() {
		return getComponent().getProperties().getLabel().getString(FBUtil.getUILocale());
	}

	public void setLabel(String label) {
		LocalizedStringBean bean = getComponent().getProperties().getLabel();
		if(bean == null) {
			bean = new LocalizedStringBean();
		}
		bean.setString(FBUtil.getUILocale(), label);
		getComponent().getProperties().setLabel(bean);
	}
	
	public boolean getRequired() {
		return getComponent().getProperties().isRequired();
	}

	public void setRequired(boolean required) {
		getComponent().getProperties().setRequired(required);
	}

	public List<ItemBean> getItems() {return null;}

	public void setItems(List<ItemBean> items) {}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String getHelpMessage() {
		return getComponent().getProperties().getHelpText().getString(FBUtil.getUILocale());
	}
	
//	public String getVariableName() {
//		if(component != null) {
//			return component.getProperties().getVariable() == null ? null : component.getProperties().getVariable().getDefaultStringRepresentation();
//		} else if(selectComponent != null) {
//			return selectComponent.getProperties().getVariable() == null ? null : selectComponent.getProperties().getVariable().getDefaultStringRepresentation();
//		}
//		return null;
//	}

	public void setHelpMessage(String helpMessage) {
		LocalizedStringBean bean = getComponent().getProperties().getHelpText();
		bean.setString(FBUtil.getUILocale(), helpMessage);
		getComponent().getProperties().setHelpText(bean);
	}

	public String getAutofillKey() {
			return getComponent().getProperties().getAutofillKey();
	}

	public void setAutofillKey(String autofillKey) {
		if(autofillKey == null || CoreConstants.EMPTY.equals(autofillKey)) {
			return;
		}
		getComponent().getProperties().setAutofillKey(autofillKey);
	}

	public String getPlainText() {return null;}

	public void setPlainText(String plainText) {}
	
	public void setAddButtonLabel(String value) {}
	
	public void setRemoveButtonLabel(String value) {}
	
	public void setExternalSrc(String externalSrc) {}
	
	public String getExternalSrc() {return null;}
	
	public String getRemoveButtonLabel() {return null;}
	
	public String getAddButtonLabel() {return null;}

	public ProcessPalette getProcessPalette() {
		return processPalette;
	}

	public void setProcessPalette(ProcessPalette processPalette) {
		this.processPalette = processPalette;
	}

	public JbpmProcessBusinessBean getJbpmProcessBusiness() {
		return jbpmProcessBusiness;
	}

	public void setJbpmProcessBusiness(JbpmProcessBusinessBean jbpmProcessBusiness) {
		this.jbpmProcessBusiness = jbpmProcessBusiness;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public void setProcessData(ProcessData processData) {
		this.processData = processData;
	}

}