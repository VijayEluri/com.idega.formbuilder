package com.idega.formbuilder.media;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.core.file.util.MimeTypeUtil;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.io.DownloadWriter;
import com.idega.presentation.IWContext;
import com.idega.util.FileUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

public class FormSourceDownloader extends DownloadWriter {

	@Autowired
	private FormDocument formDocument;
	
	private byte[] formSourceCode;
	
	@Override
	public String getMimeType() {
		return MimeTypeUtil.MIME_TYPE_XML;
	}

	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		String resourceId = iwc.getParameter("resourceToExportId");
		if (StringUtil.isEmpty(resourceId)) {
			return;
		}
		
		try {
			ELUtil.getInstance().autowire(this);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		//	Body
		String code = formDocument.getOnlySourceCode();
		if (StringUtil.isEmpty(code)) {
			return;
		}
		formSourceCode = code.getBytes();
		
		//	Title
		String fileName = null;
		try {
			fileName = formDocument.getDocument().getFormTitle().getString(iwc.getCurrentLocale());
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isEmpty(fileName)) {
			fileName = new StringBuilder("form_").append(resourceId).toString();
		}
		
		setAsDownload(iwc, new StringBuilder(StringHandler.shortenToLength(fileName, 64)).append(".xhtml").toString(), formSourceCode.length);
	}

	@Override
	public void writeTo(OutputStream streamOut) throws IOException {
		InputStream streamIn = new ByteArrayInputStream(formSourceCode);
		FileUtil.streamToOutputStream(streamIn, streamOut);
		
		streamOut.flush();
		streamOut.close();
		streamIn.close();
	}

}
