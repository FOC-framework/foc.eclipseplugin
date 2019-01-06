package foc.eclipseplugin;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import foc.eclipseplugin.field.FieldDef;

public class FocGetterSetter extends FocObjectCodeWriter {
	
	private FieldDef fieldDef = null;
	
	public FocGetterSetter(ExecutionEvent event, FieldDef fieldDef) throws ExecutionException {
		super(event);
		this.fieldDef = fieldDef;
	}
	
	public void dispose(){
		fieldDef = null;
	}
	
	public void generate() throws ExecutionException {
		String constantName = "FIELD_"+fieldDef.getName();
		addConstantDeclaration(fieldDef, constantName, "String", null);
		
		MethodDeclaration declaration = newMethod("get"+fieldDef.getName(), Modifier.PUBLIC, fieldDef);
		Block body = declaration.getBody();
		addText(body, "return getProperty"+fieldDef.getFieldType().getPropertyMethodFragment()+"(FIELD_"+fieldDef.getName()+");");
		
		declaration = newMethod("set"+fieldDef.getName(), Modifier.PUBLIC, null);
		body = declaration.getBody();
		addText(body, "setProperty"+fieldDef.getFieldType().getPropertyMethodFragment()+"(FIELD_"+fieldDef.getName()+", value);");
		addParameter(declaration, fieldDef, "value");
	}
}
