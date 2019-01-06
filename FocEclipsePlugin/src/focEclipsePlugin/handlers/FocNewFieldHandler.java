package focEclipsePlugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import foc.eclipseplugin.FocGetterSetter;
import foc.eclipseplugin.FocObjectCodeWriter;
import foc.eclipseplugin.FieldDefDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class FocNewFieldHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public FocNewFieldHandler() {
	}

	protected CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		parser.setResolveBindings(true); // we need bindings later on
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}

	private ICompilationUnit getCompilationUnit(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (workbenchWindow == null) return null;

		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor == null) return null;

		IJavaElement element = JavaUI.getEditorInputJavaElement(editor.getEditorInput());
		ICompilationUnit compilationUnit = element.getAdapter(ICompilationUnit.class);
		return compilationUnit;
	}
	
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*
		FocObjectCodeWriter focWriter = new FocObjectCodeWriter(event);
		
		MethodDeclaration newMethod = focWriter.newMethod("setCode", Modifier.PUBLIC);
		Block getterBlock = newMethod.getBody();

		focWriter.addText(getterBlock, "//New Edited \n ");
		focWriter.addText(getterBlock, "  getPropertyString(FNAME_Name);");
	    
		MethodInvocation focGetterInvocation = focWriter.newMethodInvocation(getterBlock, "getPropertyString");
		
		// for getting insertion position
		TypeDeclaration typeDecl = (TypeDeclaration) focWriter.getAstRoot().types().get(0);
		MethodDeclaration methodDecl = typeDecl.getMethods()[0];
		Block block = methodDecl.getBody();
		MethodInvocation addInvocation = focWriter.newMethodInvocation(block, "add");
 
		focWriter.close();
		*/
		
		Shell activeShell = HandlerUtil.getActiveShell(event);
		FieldDefDialog dialog = new FieldDefDialog(activeShell);
		dialog.create();
		if (dialog.open() == Window.OK) {
		    
		}
		
		return null;
	}
}
