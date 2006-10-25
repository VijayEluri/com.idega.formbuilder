package com.idega.formbuilder.business.form.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.business.IBOLookup;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.business.form.beans.FormComponentFactory;
import com.idega.formbuilder.business.form.beans.FormDocument;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormDocument;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.generators.ComponentsGeneratorFactory;
import com.idega.formbuilder.business.form.manager.generators.IComponentsGenerator;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.formbuilder.sandbox.SandboxUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideSession;
import com.idega.slide.business.IWSlideSessionBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 * Class responsible for managing user's currently editing form.
 * 
 */
public class FormManager implements IFormManager {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	
	private static InputStream components_xforms_stream = null;
	private static InputStream components_xsd_stream = null;
	private static InputStream form_xforms_template_stream = null;
	
	private static boolean inited = false;
	
	private static final String NOT_INITED_MSG = "Init FormManager first";
	private static final String FB_INIT_FAILED = "Could not initialize FormManager. See \"caused by\" for details.";
	
	private IFormDocument form_document;
	
	public void createFormDocument(String form_id, LocalizedStringBean form_name) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		form_document.createDocument(form_id, form_name);
		form_document.persist();
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		throw new NullPointerException("__Not implemented__");
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component was not found");
		
		return component.getHtmlRepresentationByLocale(locale);
	}
	
	public String createFormComponent(String component_type, String component_after_this_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		IFormComponent component = FormComponentFactory.getInstance().getFormComponentByType(component_type);

		if(component_after_this_id != null) {
			
			IFormComponent comp_after_new = form_document.getFormComponent(component_after_this_id);
			
			if(comp_after_new == null)
				throw new NullPointerException("Component after not found");
			
			component.setComponentAfterThis(comp_after_new);
		}
		
		form_document.addComponent(component);
		
		form_document.persist();
		
		return component.getId();
	}
	
	public void updateFormComponent(String component_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component with such an id was not found on document");
		
		component.render();
		form_document.persist();
	}
	
	protected FormManager() {	}
	
	public List<String> getAvailableFormComponentsTypesList() throws FBPostponedException {
		
		return CacheManager.getInstance().getAvailableFormComponentsTypesList();
	}
	
	public List<String> getFormComponentsIdsList() {
		
		return form_document.getFormComponentsIdList();
	}
	
	public IComponentProperties getComponentProperties(String component_id) {
		
		return form_document.getFormComponent(component_id).getProperties();
	}
	
	/**
	 * @return instance of this class. FormManager must be initiated first by calling init()
	 * @throws InitializationException - if FormManager was not initiated before.
	 */
	public static IFormManager getInstance() throws InitializationException {
		
		if(!inited)
			throw new InitializationException(NOT_INITED_MSG);
		
		FormManager fm = new FormManager();
		
		IFormDocument form_document = new FormDocument();
		form_document.setPersistenceManager(PersistenceManagerFactory.newPersistenceManager());
		fm.form_document = form_document;
			
		return fm;
	}
	
	public static void init(FacesContext ctx) throws InitializationException {
		
		try {

			if(ctx != null) {
				
				IWMainApplication iw_app = IWMainApplication.getIWMainApplication(ctx);
				
//				temporary disabled
				if(false) {
					
//					get streams from webdav
					IWContext iwc = IWContext.getInstance();
					IWSlideSessionBean ses_bean = (IWSlideSessionBean)IBOLookup.getSessionInstance(iwc, IWSlideSession.class);			
					components_xforms_stream = ses_bean.getInputStream((String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_XFORMS_CONTEXT_PATH));
					components_xsd_stream = ses_bean.getInputStream((String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_XSD_CONTEXT_PATH));
				}

				components_xforms_stream = iw_app.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceInputStream((String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_XFORMS_CONTEXT_PATH));
				components_xsd_stream = iw_app.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceInputStream((String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_XSD_CONTEXT_PATH));
				form_xforms_template_stream = iw_app.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceInputStream((String)iw_app.getAttribute(IWBundleStarter.FORM_XFORMS_TEMPLATE_RESOURCES_PATH));
				
			} else {
				
				components_xforms_stream = new FileInputStream(SandboxUtil.COMPONENTS_XFORMS_CONTEXT_PATH);
				components_xsd_stream = new FileInputStream(SandboxUtil.COMPONENTS_XSD_CONTEXT_PATH);
				form_xforms_template_stream = new FileInputStream(SandboxUtil.FORM_XFORMS_TEMPLATE_CONTEXT_PATH);
			}
			
			ComponentsGeneratorFactory.init(ctx);
			CacheManager.getInstance().initAppContext(ctx);
			
		} catch (Exception e) {
			logger.error(FB_INIT_FAILED, e);
			throw new InitializationException(FB_INIT_FAILED, e);
		}
		
		init();
	}
	
	/**
	 * Should be called, before getting an instance of this class
	 * @throws InstantiationException - smth bad happened during init phase
	 */
	protected static void init() throws InitializationException {
		
		long start = 0;
		
		if(logger.isDebugEnabled())
			start = System.currentTimeMillis();
			
		if(inited) {
			
			logger.error("init(): tried to call, when already inited");
			throw new InitializationException("FormManager is already initialized.");
		}
		
		try {
			
			IComponentsGenerator components_generator = ComponentsGeneratorFactory.createComponentsGenerator();
			
			Document components_xforms = null;
			Document components_xsd = null;
			Document components_xml = null;
			List<String> components_types = null;
			Document form_xforms_template = null;
			
			DocumentBuilder doc_builder = FormManagerUtil.getDocumentBuilder();
			
			components_xforms = doc_builder.parse(components_xforms_stream);
			components_xforms_stream.close();
			components_xforms_stream = null;
			
			components_xsd = doc_builder.parse(components_xsd_stream);
			components_xsd_stream.close();
			components_xsd_stream = null;
			
			components_generator.setDocument(components_xforms);
			components_xml = components_generator.generateBaseComponentsDocument();
			
			components_types = FormManagerUtil.gatherAvailableComponentsTypes(components_xml);
			
			form_xforms_template = doc_builder.parse(form_xforms_template_stream);
			form_xforms_template_stream.close();
			form_xforms_template_stream = null;
			
			CacheManager cache_manager = CacheManager.getInstance();
			cache_manager.setFormXformsTemplate(form_xforms_template);
			cache_manager.setComponentsTypes(components_types);
			cache_manager.setComponentsXforms(components_xforms);
			cache_manager.setComponentsXml(components_xml);
			cache_manager.setComponentsXsd(components_xsd);
			
			inited = true;
			
			if(logger.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				
				logger.debug("init() time: "+(end-start));
			}
			
		} catch (Exception e) {

			logger.error(FB_INIT_FAILED, e);
			throw new InitializationException(FB_INIT_FAILED, e);
		}
	}
	
	/**
	 * Check for exceptions thrown during previous requests
	 * @throws FBPostponedException - if some kind of exception happened during previous request. 
	 */
	protected void checkForPendingErrors() throws FBPostponedException {
		
		Exception[] saved_exceptions = form_document.getSavedExceptions();
		
		if(saved_exceptions != null && saved_exceptions.length != 0)
			throw new FBPostponedException(saved_exceptions[0]);
	}
	
	public static boolean isInited() {
		return inited;
	}
	
	public void rearrangeDocument() throws FBPostponedException, Exception {
		
		checkForPendingErrors();
		
		form_document.rearrangeDocument();
		form_document.persist();
	}
	
	public static void main(String[] args) {

		try {
			IFormManager fm = FormManagerFactory.newFormManager(null);
			
			LocalizedStringBean title = new LocalizedStringBean();
			title.setString(new Locale("en"), "eng title");
			title.setString(new Locale("is"), "isl title");
			
			fm.createFormDocument("11", title);
			
			fm.createFormComponent("fbcomp_text", null);
			fm.createFormComponent("fbcomp_text", null);
			fm.createFormComponent("fbcomp_text", null);
			
			List<String> comp_ids = fm.getFormComponentsIdsList();
			
			System.out.println("comp id list: "+comp_ids);
			
			title = new LocalizedStringBean();
			title.setString(new Locale("en"), "eng title xxxx");
			title.setString(new Locale("is"), "isl title xxx");
			
			fm.createFormDocument("22", title);
			
			System.out.println("comp id list     2222222: "+comp_ids);
			
			fm.createFormComponent("fbcomp_text", null);
			fm.createFormComponent("fbcomp_text", null);
			
			comp_ids = fm.getFormComponentsIdsList();
			
			System.out.println("comp id list     333333333: "+comp_ids);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}