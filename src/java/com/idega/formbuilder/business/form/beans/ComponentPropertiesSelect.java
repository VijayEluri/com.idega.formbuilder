package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.PropertiesSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentPropertiesSelect extends ComponentProperties implements PropertiesSelect {
	
	private LocalizedStringBean empty_element_label;
	private String external_data_src;
	private ILocalizedItemset itemset;
	private Integer data_src_used = LOCAL_DATA_SRC;
	
	public ILocalizedItemset getItemset() {
		return itemset;
	}
	public void setItemsetPlain(ILocalizedItemset itemset) {
		this.itemset = itemset;
	}
	public LocalizedStringBean getEmptyElementLabel() {
		return empty_element_label;
	}
	public void setEmptyElementLabel(LocalizedStringBean empty_element_label) {
		this.empty_element_label = empty_element_label;
		parent_component.update(new ConstUpdateType(ConstUpdateType.empty_element_label));
	}
	public void setEmptyElementLabelPlain(LocalizedStringBean empty_element_label) {
		this.empty_element_label = empty_element_label;
	}
	public String getExternalDataSrc() {
		return external_data_src;
	}
	/**
	 * @param external_data_src - source to xml, containing data source<br />
	 * <b>IMPORTANT:</b> xml data structure should validate against 
	 * select-data-src.xsd (look somewhere around for it, probably in resources/templates).
	 * See also select-data-src.xml for example.
	 * 
	 */
	public void setExternalDataSrc(String external_data_src) {
		this.external_data_src = external_data_src;
		parent_component.update(new ConstUpdateType(ConstUpdateType.external_data_src));
	}
	public void setExternalDataSrcPlain(String external_data_src) {
		this.external_data_src = external_data_src;
	}
	public Integer getDataSrcUsed() {
		return data_src_used;
	}
	public void setDataSrcUsed(Integer data_src_used) {
		
		if(data_src_used != LOCAL_DATA_SRC && data_src_used != EXTERNAL_DATA_SRC)
			throw new IllegalArgumentException("Neither LOCAL_DATA_SRC, nor EXTERNAL_DATA_SRC provided.");
		
		this.data_src_used = data_src_used;
		parent_component.update(new ConstUpdateType(ConstUpdateType.data_src_used));
	}
	public void setDataSrcUsedPlain(Integer data_src_used) {
		
		if(data_src_used != null && data_src_used != LOCAL_DATA_SRC && data_src_used != EXTERNAL_DATA_SRC)
			throw new IllegalArgumentException("Neither LOCAL_DATA_SRC, nor EXTERNAL_DATA_SRC provided.");
		
		this.data_src_used = data_src_used;
	}
}