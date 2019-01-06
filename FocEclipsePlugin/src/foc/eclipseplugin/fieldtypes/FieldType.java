package foc.eclipseplugin.fieldtypes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import foc.eclipseplugin.field.FieldDef;

public abstract class FieldType {

	public abstract boolean isPrimitive();
	public abstract PrimitiveType.Code getPrimitiveCode();
	public abstract Annotation addAnnotation(AST ast, ASTRewrite rewriter, FieldDef def);
		
	public static final String TYPE_String    = "String";
	public static final String TYPE_Integer   = "Integer";
	public static final String TYPE_Blob      = "Blob";
	public static final String TYPE_Image     = "Image";
	public static final String TYPE_File      = "File";
	public static final String TYPE_Double    = "Double";
	public static final String TYPE_Boolean   = "Boolean";
	public static final String TYPE_ForeignEntity = "ForeignEntity";
	public static final String TYPE_Date      = "Date";
	public static final String TYPE_Time      = "Time";
	public static final String TYPE_FocMultipleChoice = "MultipleChoice";
	public static final String TYPE_FocMultipleChoiceString = "MultipleChoiceString";	
	
	private String type = null;

	public FieldType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getReturnedClassName(FieldDef def){
		return getType();
	}
	
	public String getComboBoxItem(){
		return type;
	}
	
	public String getPropertyMethodFragment(){
		return type;
	}
	
	public void addAnnotationMember_Number(NormalAnnotation annotation, ASTRewrite rewriter, String key, double numberValue){
	  MemberValuePair mvp = rewriter.getAST().newMemberValuePair();
	  mvp.setName(rewriter.getAST().newSimpleName(key));
	  NumberLiteral nl = rewriter.getAST().newNumberLiteral(String.valueOf(numberValue));
	  mvp.setValue(nl);
	  annotation.values().add(mvp);
  }

	public void addAnnotationMember_Integer(NormalAnnotation annotation, ASTRewrite rewriter, String key, int numberValue){
	  MemberValuePair mvp = rewriter.getAST().newMemberValuePair();
	  mvp.setName(rewriter.getAST().newSimpleName(key));
	  NumberLiteral nl = rewriter.getAST().newNumberLiteral(String.valueOf(numberValue));
	  mvp.setValue(nl);
	  annotation.values().add(mvp);
  }
	
	public void addAnnotationMember_String(NormalAnnotation annotation, ASTRewrite rewriter, String key, String stringValue){
	  MemberValuePair mvp = rewriter.getAST().newMemberValuePair();
	  mvp.setName(rewriter.getAST().newSimpleName(key));
	  StringLiteral nl = rewriter.getAST().newStringLiteral();
	  nl.setLiteralValue(stringValue);
	  mvp.setValue(nl);
	  annotation.values().add(mvp);
  }
	
	public void addAnnotationMember_Boolean(NormalAnnotation annotation, ASTRewrite rewriter, String key, boolean booleanValue){
	  MemberValuePair mvp = rewriter.getAST().newMemberValuePair();
	  mvp.setName(rewriter.getAST().newSimpleName(key));
	  BooleanLiteral nl = rewriter.getAST().newBooleanLiteral(booleanValue);
	  mvp.setValue(nl);
	  annotation.values().add(mvp);
  }
}
