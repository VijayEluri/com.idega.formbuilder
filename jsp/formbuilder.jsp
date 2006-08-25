<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2">

	<jsp:directive.page contentType="text/html" />

	<f:view>
		<ws:page id="formbuilder">
			<h:form id="workspaceform1">
				<fb:container id="level0">
					<fb:container id="lftBar">
						<fb:fbpanel title="antanas" styleClass="fb_panel left_panel" />
					</fb:container>
					<fb:container id="level1">
						<fb:container id="level2">
							<fb:container id="rgtBar">
								<fb:fbpanel title="X X X" styleClass="fb_panel right_panel" />
							</fb:container>
							<fb:container id="level3">
								<fb:container id="toolBar">
									<fb:toolbar id="FBtoolbar" buttonsStyleClass="main_toolbar_buttons" styleClass="main_toolbar">
										<fb:toolbarbutton image="toolbar_new.gif" tooltip="New Form" onmouseover="toolbar_save.gif" onmousedown="toolbar_delete.gif" />
										<fb:toolbarbutton image="toolbar_save.gif" tooltip="Save Form" />
										<fb:toolbarbutton image="toolbar_delete.gif" tooltip="Delete Form" />
									</fb:toolbar>
								</fb:container>
								<fb:container id="main">
									<f:verbatim>CONTENT -</f:verbatim>
								</fb:container>
							</fb:container>
						</fb:container>
					</fb:container>
				</fb:container>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>
