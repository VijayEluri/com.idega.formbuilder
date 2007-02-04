package com.idega.formbuilder.business.form;

import java.util.List;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 *
 */
public interface Container extends Component {

	public abstract Component getComponent(String component_id);
	
	public abstract List<String> getContainedComponentsIdList();
	
	public abstract Component addComponent(String component_type, String component_after_this_id) throws NullPointerException;
}