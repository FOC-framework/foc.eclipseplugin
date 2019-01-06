package foc.eclipseplugin.fieldtypes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import foc.eclipseplugin.field.FieldDef;

public class FieldTypeForeignEntity extends FieldType {

	public FieldTypeForeignEntity() {
		super(TYPE_ForeignEntity);
	}

	@Override	
	public String getReturnedClassName(FieldDef def){
		return def.getTable();
	}

	@Override
	public boolean isPrimitive() {
		return false;
	}

	@Override
	public PrimitiveType.Code getPrimitiveCode() {
		return null;
	}
	
	@Override
	public String getPropertyMethodFragment(){
		return "Object";
	}

	@Override
	public Annotation addAnnotation(AST ast, ASTRewrite rewriter, FieldDef def) {
		NormalAnnotation annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName("Foc"+getType()));

		if(def.isMandatory()){
			addAnnotationMember_Boolean(annotation, rewriter, "mandatory", def.isMandatory());
		}
		if(!def.getTable().isEmpty()){
			addAnnotationMember_String(annotation, rewriter, "table", def.getTable());
		}
		if(def.isCascade()){
			addAnnotationMember_Boolean(annotation, rewriter, "cascade", def.isCascade());
		}
		if(!def.isCachedList()){
			addAnnotationMember_Boolean(annotation, rewriter, "cachedList", def.isCachedList());
		}
		
		return annotation;
	}
}
