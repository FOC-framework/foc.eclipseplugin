package foc.eclipseplugin.fieldtypes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import foc.eclipseplugin.field.FieldDef;

public class FieldTypeDouble extends FieldType {

	public FieldTypeDouble() {
		super(TYPE_Double);
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

	@Override
	public PrimitiveType.Code getPrimitiveCode() {
		return PrimitiveType.DOUBLE;
	}

	@Override
	public Annotation addAnnotation(AST ast, ASTRewrite rewriter, FieldDef def) {
		NormalAnnotation annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName("Foc"+getType()));

		if(def.isMandatory()){
			addAnnotationMember_Boolean(annotation, rewriter, "mandatory", def.isMandatory());
		}
		if(def.getSize() > 0){
			addAnnotationMember_Number(annotation, rewriter, "size", def.getSize());
		}
		if(def.getDecimal() > 0){
			addAnnotationMember_Number(annotation, rewriter, "decimal", def.getDecimal());			
		}
		
		return annotation;
	}

}
