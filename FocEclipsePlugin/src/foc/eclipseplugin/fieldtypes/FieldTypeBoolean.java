package foc.eclipseplugin.fieldtypes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import foc.eclipseplugin.field.FieldDef;

public class FieldTypeBoolean extends FieldType {

	public FieldTypeBoolean() {
		super(FieldType.TYPE_Boolean);
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

	@Override
	public PrimitiveType.Code getPrimitiveCode() {
		return PrimitiveType.BOOLEAN;
	}

	@Override
	public Annotation addAnnotation(AST ast, ASTRewrite rewriter, FieldDef def) {
		NormalAnnotation annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName("Foc"+getType()));
		
		return annotation;
	}
}
