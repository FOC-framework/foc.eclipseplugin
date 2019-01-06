package foc.eclipseplugin;


import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import foc.eclipseplugin.field.FieldDef;
import foc.eclipseplugin.fieldtypes.FieldType;

public class FocObjectCodeWriter {
	private ICompilationUnit compilationUnit = null;
	private CompilationUnit astRoot = null;
	private AST ast = null;
	private ASTRewrite rewriter = null; 
			
	public FocObjectCodeWriter(ExecutionEvent event) throws ExecutionException {
		this(getCompilationUnit(event));
	}
	
	public FocObjectCodeWriter(ICompilationUnit compilationUnit) throws ExecutionException {
		this.compilationUnit = compilationUnit;
		// parse compilation unit
		astRoot = parse(compilationUnit);		
		// create a ASTRewrite
		ast = astRoot.getAST();
		rewriter = ASTRewrite.create(ast);
	}
	
	public AST getAst() {
		return ast;
	}

	public void close(){
		try{
			TextEdit edits = rewriter.rewriteAST();
			// apply the text edits to the compilation unit
			Document document = new Document(compilationUnit.getSource());
			edits.apply(document);
			// this is the code for adding statements
			compilationUnit.getBuffer().setContents(document.get());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public CompilationUnit getAstRoot() {
		return astRoot;
	}

	protected CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		parser.setResolveBindings(true); // we need bindings later on
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}

	private static ICompilationUnit getCompilationUnit(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (workbenchWindow == null) return null;

		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor == null) return null;

		IJavaElement element = JavaUI.getEditorInputJavaElement(editor.getEditorInput());
		ICompilationUnit compilationUnit = element.getAdapter(ICompilationUnit.class);
		return compilationUnit;
	}

	public void addConstantDeclaration(FieldDef fieldDef, String name, String type, PrimitiveType.Code primitiveCode){
		FieldType fieldType = fieldDef.getFieldType();
		if(fieldType != null){
			TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
	
			VariableDeclarationFragment newDeclFrag= ast.newVariableDeclarationFragment();
			newDeclFrag.setName(ast.newSimpleName(name));
			StringLiteral literal = ast.newStringLiteral();
	    literal.setLiteralValue(fieldDef.getName());
	    newDeclFrag.setInitializer(literal);
			
			FieldDeclaration newDecl = ast.newFieldDeclaration(newDeclFrag);
		  if(type == null || type.isEmpty()){
		  	newDecl.setType(ast.newPrimitiveType(primitiveCode));
		  }else{
		  	newDecl.setType(ast.newSimpleType(ast.newSimpleName(type)));
		  }
	
			//Annotation Section
			//----------------------	  
		  Annotation annotation = (Annotation) fieldType.addAnnotation(ast, rewriter, fieldDef);
		  if(annotation != null) newDecl.modifiers().add(annotation);
		  /*
			NormalAnnotation annotation = ast.newNormalAnnotation();
			annotation.setTypeName(ast.newSimpleName("Foc"+fieldType.getType()));
			
	    MemberValuePair mvp = rewriter.getAST().newMemberValuePair();
	    mvp.setName(rewriter.getAST().newSimpleName("size"));
	    NumberLiteral nl = rewriter.getAST().newNumberLiteral(String.valueOf(15));
	    mvp.setValue(nl);
	    annotation.values().add(mvp);
	    */
	    
			//----------------------
		  
			newDecl.modifiers().addAll(getAst().newModifiers(Modifier.PUBLIC));
			newDecl.modifiers().addAll(getAst().newModifiers(Modifier.STATIC));
			newDecl.modifiers().addAll(getAst().newModifiers(Modifier.FINAL));
			
			ListRewrite lrw = rewriter.getListRewrite(typeDecl, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
			if(typeDecl.getFields().length <= 0){
				lrw.insertAt(newDecl, 0, null);
			}else{
				lrw.insertAfter(newDecl, typeDecl.getFields()[typeDecl.getFields().length-1], null);
			}
		}
	}
	
	public MethodDeclaration newMethod(String methodName, int modifier, FieldDef returnFieldDef){
		// for getting insertion position
		TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
 
		//Create a new Method Declaration
		MethodDeclaration newMethod = ast.newMethodDeclaration();
		newMethod.setName(ast.newSimpleName(methodName));
		if(returnFieldDef != null){
			FieldType fieldType = returnFieldDef.getFieldType();
			if(fieldType != null && fieldType.isPrimitive()){
				newMethod.setReturnType2(ast.newPrimitiveType(fieldType.getPrimitiveCode()));
			}else if(fieldType != null && !fieldType.isPrimitive()){
				String className = fieldType.getReturnedClassName(returnFieldDef);
				newMethod.setReturnType2(ast.newSimpleType(ast.newSimpleName(className)));
			}
		}else{
			newMethod.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
		}
		
		newMethod.modifiers().addAll(getAst().newModifiers(modifier));
		ListRewrite lrw = rewriter.getListRewrite(typeDecl, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		lrw.insertLast(newMethod, null);
		Block getterBlock = ast.newBlock();
		newMethod.setBody(getterBlock);
		
		return newMethod;
	}

	public void addParameter(MethodDeclaration methodDeclaration, FieldDef fieldDef, String name){
	  SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
	  if(fieldDef.getFieldType().isPrimitive()){
	  	variableDeclaration.setType(ast.newPrimitiveType(fieldDef.getFieldType().getPrimitiveCode()));
	  }else{
	  	String className = fieldDef.getFieldType().getReturnedClassName(fieldDef);
	  	variableDeclaration.setType(ast.newSimpleType(ast.newSimpleName(className)));
	  }
    variableDeclaration.setName(ast.newSimpleName(name));
    methodDeclaration.parameters().add(variableDeclaration);
	}
	
	public void addText(Block getterBlock, String comment){
	    ListRewrite getterBlockListRewrite = rewriter.getListRewrite(getterBlock, Block.STATEMENTS_PROPERTY);
	    Statement blaStatement = (Statement) rewriter.createStringPlaceholder(comment, ASTNode.EMPTY_STATEMENT);
	    getterBlockListRewrite.insertLast(blaStatement, null);
	}

	public MethodInvocation newMethodInvocation(Block block, String methodName){ 
		// create new statements for insertion
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setName(ast.newSimpleName(methodName));
		Statement focGetterStatement = ast.newExpressionStatement(methodInvocation);
		//create ListRewrite
		ListRewrite getterBlockRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
		getterBlockRewrite.insertLast(focGetterStatement, null);
		return methodInvocation; 
	}
}
