package foc.eclipseplugin.field;

import foc.eclipseplugin.fieldtypes.FieldType;

public class FieldDef {
	private FieldType fieldType = null;
	private String name = null;
	private int size = 0;
	private int decimal = 0;
	private String fileNameFieldName = null;
	private boolean mandatory = false;
	private boolean cascade = false;
	private boolean detach = false;
	private boolean saveOnebyOne = true;
	private String table = null;
	private boolean cachedList = true;
	private boolean allowNull = true;
	private String listFilterProperty = null;
	private String listFilterValue = null;
	
	public FieldDef(){
		
	}
	
	public FieldDef(FieldType fieldType, String name){
		this.fieldType = fieldType;
		this.name = name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDecimal() {
		return decimal;
	}

	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	public String getFileNameFieldName() {
		return fileNameFieldName;
	}

	public void setFileNameFieldName(String fileNameFieldName) {
		this.fileNameFieldName = fileNameFieldName;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isCascade() {
		return cascade;
	}

	public void setCascade(boolean cascade) {
		this.cascade = cascade;
	}

	public boolean isDetach() {
		return detach;
	}

	public void setDetach(boolean detach) {
		this.detach = detach;
	}

	public boolean isSaveOnebyOne() {
		return saveOnebyOne;
	}

	public void setSaveOnebyOne(boolean saveOnebyOne) {
		this.saveOnebyOne = saveOnebyOne;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isCachedList() {
		return cachedList;
	}

	public void setCachedList(boolean cachedList) {
		this.cachedList = cachedList;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	public String getListFilterProperty() {
		return listFilterProperty;
	}

	public void setListFilterProperty(String listFilterProperty) {
		this.listFilterProperty = listFilterProperty;
	}

	public String getListFilterValue() {
		return listFilterValue;
	}

	public void setListFilterValue(String listFilterValue) {
		this.listFilterValue = listFilterValue;
	}
}
