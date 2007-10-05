package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.presentation.FormViewer;
import com.idega.documentmanager.business.Document;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class FBFormPreview extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormPreview.class);
	
	public static final String COMPONENT_TYPE = "FormPreview";
	public static final String FORM_VIEWER = "FORM_VIEWER";
	
	private static final String container_tag = "div";
	
	public void encodeBegin(FacesContext ctx) throws IOException {
		super.encodeBegin(ctx);
		ResponseWriter writer = ctx.getResponseWriter();
		writer.startElement(container_tag, this);
		writer.writeAttribute("id", getId(), null);
	}
	
	public void encodeEnd(FacesContext ctx) throws IOException {
		ResponseWriter writer = ctx.getResponseWriter();
//		
//		try {
//			
//			Document document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
//			
//			if(document != null) {
//				
//				org.w3c.dom.Document x = document.getXformsDocument();
//				
//				FormReader form_reader = new FormReader();
//				
//				form_reader.setBaseFormURI(FBUtil.getWebdavServerUrl(ctx)+"/files/public/");
//				form_reader.setFormDocument(x);
//				
//				form_reader.setOutput(writer);
//				form_reader.generate();
//			}
//			
//		} catch (Exception e) {
//			logger.error("Error when parsing form to response writer", e);
//		}
		writer.endElement(container_tag);
		super.encodeEnd(ctx);
	}
	
	protected void initializeComponent(FacesContext context) {
		
		FormDocument formDocument = (FormDocument)WFUtil.getBeanInstance("formDocument");
		Document xformsDocument = formDocument.getDocument();
		
		if(xformsDocument == null) {
			logger.error("Form document got form "+FormDocument.class.getName()+" was null");
			return;
		}
			
		FormViewer formViewer = new FormViewer();
		formViewer.setFormId(((FormDocument) WFUtil.getBeanInstance("formDocument")).getFormId());
		formViewer.setXFormsDocument((org.w3c.dom.Document)xformsDocument.getXformsDocument().cloneNode(true));
		
		add(formViewer);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
}