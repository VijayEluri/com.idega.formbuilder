function selectFormField(selectedField) {
	document.forms['workspaceform1'].elements['selected_field_id'].value=selectedField.parentNode.id;
	document.getElementById('select_field_button').onclick();
}
function selectFormHeader() {
	document.getElementById('select_form_header_button').onclick();
}
function displayMessage(url) {
	messageObj.setSource(url);
	messageObj.setCssClassMessageBox(false);
	messageObj.setSize(250,100);
	messageObj.setShadowDivVisible(true);
	messageObj.display();
}
function closeMessage() {
	messageObj.close();
}
function createNewForm() {
	var name = document.forms['newFormDialogForm'].elements['formName'].value;
	if(name != '') {
		dwrmanager.createNewForm(createdNewForm,name);
		closeMessage();
		showLoadingMessage("Loading");
		switchFacets(false, true, true);
		clearDesignSpace();
	}
	
}
function createdNewForm(element) {
	$('dropBox').appendChild(createTreeNode(element.documentElement));
	closeLoadingMessage();
}
function removeComponent(element) {
	showLoadingMessage("Deleting");
	dwrmanager.removeComponent(removedComponent,element.parentNode.id);
}
function removedComponent(id) {
	if(id != '') {
		var dropBox = $('dropBox');
		var element = document.getElementById(id);
		dropBox.removeChild(element);
	}
	closeLoadingMessage();
}
function clearDesignSpace() {
	var dropBox = $('dropBox');
	var child = null;
	for(var i=dropBox.childNodes.length-1;i>-1;i--) {
		child = dropBox.childNodes[i];
		//alert(child + 'element');
		if(child.getAttribute('class') == 'formElement') {
			//alert(child + 'to be deleted');
			dropBox.removeChild(child);
			//dropBox.replaceChild(child,getDecoyNode());
		}
	}
}
function getDecoyNode() {
	var node = document.createNode('div');
	return node;
}
function switchFacets(noForm,heading,empty) {
	switchVisibility('noFormNotice',noForm);
	switchVisibility('formHeading',heading);
	switchVisibility('emptyForm',empty);
}
function switchVisibility(id,makeVisible) {
	var component = $(id);
	if(component) {
		if(component.style) {
			if(makeVisible == true) {
				component.style.display = 'block';
			} else {
				component.style.display = 'none';
			}
		} else {
			if(makeVisible == true) {
				component.display = 'block';
			} else {
				component.display = 'none';
			}
		}
	}
}
function decoy() {
	closeLoadingMessage();
}
function closeLoadingMessage() {
 	var elem = document.getElementById('busybuddy');
 	if (elem) {
  		if(elem.style) { 
       		elem.style.display = 'none';
     	} else {
       		elem.display = 'none' ;
     	}
 	}
}
function switchSelectedForm() {
	showLoadingMessage("Loading");
	return myFaces_showPanelTab(0,'workspaceform1:options_tabbed_pane_indexSubmit','workspaceform1:tab01_headerCell','workspaceform1:tab01',panelTabbedPane_5Fworkspaceform1_3Aoptions_5Ftabbed_5Fpane_5FHeadersIDs,panelTabbedPane_5Fworkspaceform1_3Aoptions_5Ftabbed_5Fpane_5FIDs,'activeTab','inactiveTab','activeSub','inactiveSub');
}
function formSwitched() {
	closeLoadingMessage();
}

/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
		