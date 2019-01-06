package foc.eclipseplugin;

import java.util.Iterator;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import foc.eclipseplugin.field.FieldDef;
import foc.eclipseplugin.fieldtypes.FieldType;
import foc.eclipseplugin.fieldtypes.FieldTypeFactory;

public class FieldDefDialog extends TitleAreaDialog {
	
	private Text  fieldName = null;
	private Combo type = null;
	
	private Label  mandatoryLabel = null;
	private Button mandatory = null;

	private Label sizeLabel = null;
	private Text  size = null;
	
	private Label decimalLabel = null;
	private Text  decimal = null;

	private Label  cascadeLabel = null;
	private Button cascade = null;

	private Label  detachLabel = null;
	private Button detach = null;

	private Label  saveOnebyOneLabel = null;
	private Button saveOnebyOne = null;

	private Label fileNameFieldLabel = null;
	private Text  fileNameField = null;

	private Label tableLabel = null;
	private Text  table = null;

	private Label  cachedListLabel = null;
	private Button cachedList = null;

	private Label  allowNullLabel = null;
	private Button allowNull = null;

	private Label listFilterPropertyLabel = null;
	private Text  listFilterProperty = null;

	private Label listFilterValueLabel = null;
	private Text  listFilterValue = null;
	
	private FieldDef fieldDef = null;

	public FieldDefDialog(Shell parentShell) {
		super(parentShell);
		fieldDef = new FieldDef();
	}

	public FieldDef getFieldDef() {
		return fieldDef;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Foc Framework");
		setMessage("Generate getter setter for field", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		addTypeField(container);
		addName(container);
		
		mandatoryLabel = addLabel(container, "Mandatory");
		mandatory = new Button(container, SWT.CHECK);
		setLayoutField(mandatory);
		
		sizeLabel = addLabel(container, "Size");
		size = new Text(container, SWT.BORDER);
		setLayoutField(size);

		decimalLabel = addLabel(container, "Decimal");
		decimal = new Text(container, SWT.BORDER);
		setLayoutField(decimal);

		fileNameFieldLabel = addLabel(container, "File name field");
		fileNameField = new Text(container, SWT.BORDER);
		setLayoutField(fileNameField);
		
		tableLabel = addLabel(container, "Foreign table");
		table = new Text(container, SWT.BORDER);
		setLayoutField(table);

		cascadeLabel = addLabel(container, "Cascade");
		cascade = new Button(container, SWT.CHECK);
		setLayoutField(cascade);

		cachedListLabel = addLabel(container, "Cached list");
		cachedList = new Button(container, SWT.CHECK);
		cachedList.setSelection(true);
		setLayoutField(cachedList);
		
		detachLabel = addLabel(container, "Detach");
		detach = new Button(container, SWT.CHECK);
		setLayoutField(detach);
		
		saveOnebyOneLabel = addLabel(container, "One by One");
		saveOnebyOne = new Button(container, SWT.CHECK);
		saveOnebyOne.setSelection(true);
		setLayoutField(saveOnebyOne);
		
		allowNullLabel = addLabel(container, "Allow Null");
		allowNull = new Button(container, SWT.CHECK);
		allowNull.setSelection(true);
		setLayoutField(allowNull);
		
		listFilterPropertyLabel = addLabel(container, "List Filter Property");
		listFilterProperty = new Text(container, SWT.BORDER);
		setLayoutField(listFilterProperty);

		listFilterValueLabel = addLabel(container, "List Filter Value");
		listFilterValue = new Text(container, SWT.BORDER);
		setLayoutField(listFilterValue);
		
		type.setText(FieldType.TYPE_String);
		adjustVisible();
		
		return area;
	}

	private Label addLabel(Composite container, String title) {
		Label lbl = new Label(container, SWT.NONE);
		lbl.setText(title);
		return lbl;
	}
	
	private void setLayoutField(Control control) {
		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;
		control.setLayoutData(dataFirstName);
	}

	private void addTypeField(Composite container) {
		Label lbtFirstName = addLabel(container, "Field Type");

		type = new Combo(container, SWT.BORDER);
		setLayoutField(type);
		
		String[] items = new String[FieldTypeFactory.getInstance().size()];
		int i = 0;
		Iterator<FieldType> iter = FieldTypeFactory.getInstance().iterator();
		while (iter != null && iter.hasNext()){
			FieldType type = iter.next();
			items[i++] = type.getComboBoxItem();
		}
		type.setItems(items);
		
		type.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				adjustVisible();
			}
		});
		
	}

	private void addName(Composite container) {
		Label lbtFirstName = addLabel(container, "Field Name");

		fieldName = new Text(container, SWT.BORDER);
		setLayoutField(fieldName);
		
		fieldName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(type.getText().equals(FieldType.TYPE_ForeignEntity)){
					table.setText(fieldName.getText());
				}
			}
		});
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		FieldType fieldType = FieldTypeFactory.getInstance().getType(type.getText());
		fieldDef.setFieldType(fieldType);
		fieldDef.setName(fieldName.getText());
		
		fieldDef.setMandatory(mandatory.getSelection());
		try{
			fieldDef.setSize(Integer.valueOf(size.getText()));
			fieldDef.setDecimal(Integer.valueOf(decimal.getText()));
		}catch(Exception e){
			
		}
		
		fieldDef.setFileNameFieldName(fileNameField.getText());
		
		fieldDef.setCascade(cascade.getSelection());
		fieldDef.setDetach(detach.getSelection());
		fieldDef.setSaveOnebyOne(saveOnebyOne.getSelection());
		fieldDef.setTable(table.getText());
		fieldDef.setCachedList(cachedList.getSelection());
		fieldDef.setAllowNull(allowNull.getSelection());
		fieldDef.setListFilterProperty(listFilterProperty.getText());
		fieldDef.setListFilterValue(listFilterValue.getText());
		
	}
	
	public void adjustVisible(){
		boolean sizeVisible = 
			   type.getText().equals(FieldType.TYPE_Double) 
			|| type.getText().equals(FieldType.TYPE_Integer) 
			|| type.getText().equals(FieldType.TYPE_String) 
			|| type.getText().equals(FieldType.TYPE_FocMultipleChoice)
			|| type.getText().equals(FieldType.TYPE_FocMultipleChoiceString);  
		size.setVisible(sizeVisible);
		sizeLabel.setVisible(sizeVisible);
		
		decimal.setVisible(type.getText().equals(FieldType.TYPE_Double));
		decimalLabel.setVisible(type.getText().equals(FieldType.TYPE_Double));
		
		boolean visibleFile = type.getText().equals(FieldType.TYPE_File);
		fileNameField.setVisible(visibleFile);
		fileNameFieldLabel.setVisible(visibleFile);
		
		boolean visibleObject = type.getText().equals(FieldType.TYPE_ForeignEntity);
		table.setVisible(visibleObject);
		tableLabel.setVisible(visibleObject);
		cascade.setVisible(visibleObject);
		cascadeLabel.setVisible(visibleObject);
		cascade.setVisible(visibleObject);
		cascadeLabel.setVisible(visibleObject);				
		detach.setVisible(visibleObject);
		detachLabel.setVisible(visibleObject);
		saveOnebyOne.setVisible(visibleObject);
		saveOnebyOneLabel.setVisible(visibleObject);
		table.setVisible(visibleObject);
		tableLabel.setVisible(visibleObject);
		cachedList.setVisible(visibleObject);
		cachedListLabel.setVisible(visibleObject);
		allowNull.setVisible(visibleObject);
		allowNullLabel.setVisible(visibleObject);
		listFilterProperty.setVisible(visibleObject);
		listFilterPropertyLabel.setVisible(visibleObject);
		listFilterValue.setVisible(visibleObject);
		listFilterValueLabel.setVisible(visibleObject);
	}
}
