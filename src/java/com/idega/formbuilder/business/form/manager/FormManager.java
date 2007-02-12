package com.idega.formbuilder.business.form.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.xslt.TransformerService;
import org.chiba.xml.xslt.impl.CachingTransformerService;
import org.w3c.dom.Document;

import com.idega.block.form.business.BundleResourceResolver;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.business.form.ConstComponentCategory;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.beans.FormDocument;
import com.idega.formbuilder.business.form.beans.IFormDocument;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.generators.FormComponentsGenerator;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.idegaweb.DefaultIWBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class FormManager implements DocumentManager {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	
	public static final String COMPONENTS_XFORMS_CONTEXT_PATH = "resources/templates/form-components.xhtml";
	public static final String COMPONENTS_XSD_CONTEXT_PATH = "resources/templates/default-components.xsd";
	public static final String FORM_XFORMS_TEMPLATE_RESOURCES_PATH = "resources/templates/form-template.xhtml";

	private static boolean inited = false;
	
	private static final String NOT_INITED_MSG = "Init FormManager first";
	private static final String FB_INIT_FAILED = "Could not initialize FormManager. See \"caused by\" for details.";
	
	private IFormDocument form_document;
	
	public com.idega.formbuilder.business.form.Document createForm(String form_id, LocalizedStringBean form_name) throws NullPointerException, Exception {
		
		FormDocument form_document = FormDocument.createDocument(form_id, form_name);
		form_document.persist();
		this.form_document = form_document;
		
		return form_document.getDocument();
	}
	
	public com.idega.formbuilder.business.form.Document openForm(String form_id) throws NullPointerException, Exception {
		
		FormDocument form_document = FormDocument.loadDocument(form_id);
		this.form_document = form_document;
		return form_document.getDocument();
	}
	
	public com.idega.formbuilder.business.form.Document openForm(Document xforms_doc) throws NullPointerException, Exception {
		
		FormDocument form_document = FormDocument.loadDocument(xforms_doc);
		this.form_document = form_document;
		return form_document.getDocument();
	}
	
	protected FormManager() {	}
	
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category) {
		
		return category == null ? CacheManager.getInstance().getAvailableFormComponentsTypesList()
				: CacheManager.getInstance().getComponentTypesByCategory(category.getComponentsCategory());
	}
	
	public com.idega.formbuilder.business.form.Document getCurrentDocument() {
		
		return ((FormDocument)form_document).getDocument();
	}
	
	/**
	 * @return instance of this class. FormManager must be initiated first by calling init()
	 * @throws InitializationException - if FormManager was not initiated before.
	 */
	public static DocumentManager getInstance() throws InitializationException {
		
		if(!inited)
			throw new InitializationException(NOT_INITED_MSG);
		
		return new FormManager();
	}
	
	public static void init(FacesContext ctx) throws InitializationException {
		if(inited) {			
			logger.error("init(): tried to call, when already inited");
			throw new InitializationException("FormManager is already initialized.");
		}

		long start = System.currentTimeMillis();
		try {
			// setup where to get files from - workspace or bundle
			String bundleInWorkspace = null;
			IWBundle bundle = null;
			
			String workspaceDir = System.getProperty(DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
			if (workspaceDir != null) {
				bundleInWorkspace = workspaceDir + "/" + IWBundleStarter.IW_BUNDLE_IDENTIFIER + "/";
			}
			
			TransformerService transf_service = null;
			if(ctx != null) {
				IWMainApplication iw_app = IWMainApplication.getIWMainApplication(ctx);
				bundle = iw_app.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
				transf_service = (TransformerService) iw_app.getAttribute(TransformerService.class.getName());
			}
			else {
				transf_service = new CachingTransformerService(new BundleResourceResolver(null));
			}

			// load xml files
			Document components_xforms = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XFORMS_CONTEXT_PATH);
			Document components_xsd = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XSD_CONTEXT_PATH);
			Document form_xforms_template = getDocumentFromBundle(bundleInWorkspace, bundle, FORM_XFORMS_TEMPLATE_RESOURCES_PATH);
			
			// setup ComponentsGenerator
			FormComponentsGenerator components_generator = FormComponentsGenerator.getInstance();
			components_generator.setTransformerService(transf_service);
			components_generator.setDocument(components_xforms);
			
			Document components_xml = components_generator.generateBaseComponentsDocument();
			List<String> components_types = FormManagerUtil.gatherAvailableComponentsTypes(components_xml);
			
			// cache results
			CacheManager cache_manager = CacheManager.getInstance();
			cache_manager.initAppContext(ctx);
			
			cache_manager.setFormXformsTemplate(form_xforms_template);
			cache_manager.setAllComponentsTypes(components_types);
			cache_manager.setCategorizedComponentTypes(FormManagerUtil.getCategorizedComponentsTypes(components_xforms));
			cache_manager.setComponentsXforms(components_xforms);
			cache_manager.setComponentsXml(components_xml);
			cache_manager.setComponentsXsd(components_xsd);
			
			inited = true;
			
			long end = System.currentTimeMillis();
			logger.info("FormManager initialized in: "+(end-start));
			
		} catch (Exception e) {

			logger.error(FB_INIT_FAILED, e);
			throw new InitializationException(FB_INIT_FAILED, e);
		}
	}
	
	/**
	 * Parses xml file specified by pathWithinBundle and returns document.
	 * If bundleInWorkspace is not null, it is used, otherwise bundle should be provided.
	 * 
	 * @param bundleInWorkspace
	 * @param bundle
	 * @param pathWithinBundle
	 * @return
	 * @throws Exception
	 */
	private static Document getDocumentFromBundle(String bundleInWorkspace, IWBundle bundle, String pathWithinBundle) throws Exception {
		Document doc = null;
		InputStream stream = null;
		if (bundleInWorkspace != null) {
			stream = new FileInputStream(bundleInWorkspace + pathWithinBundle);
		}
		else {
			stream = bundle.getResourceInputStream(pathWithinBundle);
		}

		DocumentBuilder doc_builder = FormManagerUtil.getDocumentBuilder();
		doc = doc_builder.parse(stream);
		stream.close();

		return doc;
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
}