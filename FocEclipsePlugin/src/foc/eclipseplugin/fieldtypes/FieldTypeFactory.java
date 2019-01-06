package foc.eclipseplugin.fieldtypes;

import java.util.HashMap;
import java.util.Iterator;

public class FieldTypeFactory {
	
	private HashMap<String, FieldType> map = null;
	
	public FieldTypeFactory(){
		map = new HashMap<String, FieldType>();
		
		addType(new FieldTypeString());
		addType(new FieldTypeInteger());
		addType(new FieldTypeDouble());
		addType(new FieldTypeDate());
		addType(new FieldTypeTime());
		addType(new FieldTypeBoolean());
		addType(new FieldTypeForeignEntity());
		addType(new FieldTypeMultipleChoice());
		addType(new FieldTypeMultipleChoiceString());
		addType(new FieldTypeBlob());
		addType(new FieldTypeImage());
		addType(new FieldTypeFile());
	}
	
	public FieldType getType(String typeKey){
		return map != null ? map.get(typeKey) : null;
	}

	public int size(){
		return map.size();
	}

	public Iterator<FieldType> iterator(){
		return map.values().iterator();
	}
	
	private void addType(FieldType type){
		map.put(type.getType(), type);
	}
	
	public static FieldTypeFactory instance = null;
	public static FieldTypeFactory getInstance(){
		if(instance == null){
			instance = new FieldTypeFactory();
		}
		return instance;
	}
}
