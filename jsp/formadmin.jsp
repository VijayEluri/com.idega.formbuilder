<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:fa="http://xmlns.idega.com/com.idega.block.formadmin">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder">
			<h:form id="workspaceform1">
				<fa:FormViewer />
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>