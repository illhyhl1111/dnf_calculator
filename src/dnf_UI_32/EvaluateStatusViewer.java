package dnf_UI_32;

import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.DnFColor;
import dnf_calculator.Calculator;
import dnf_class.Characters;

public class EvaluateStatusViewer extends Dialog{
	private Characters character;
	private Shell parent;
	private double skillInc;
	
	public EvaluateStatusViewer(Shell shell, Characters character){
		super(shell);
		parent=shell;
		this.character=character;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		LinkedList<Entry<String, Double>> evaluateResult = Calculator.evaluateStatus(character.getRepresentSkill(), character.target, character, 10);
		skillInc=10;
		content.setLayout(new FormLayout());
		
		Label skillIncLabel = new Label(content, SWT.NONE);
		skillIncLabel.setFont(DnFColor.TEMP2);
		skillIncLabel.setText("- ("+character.getRepresentSkill().getName()+" 기준) 스증뎀 ");//    10     %와 같은 가치의 스탯 수치 - ");
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		skillIncLabel.setLayoutData(formData);
		
		Text skillIncText = new Text(content, SWT.RIGHT);
		skillIncText.setFont(DnFColor.TEMP2);
		skillIncText.setText("10");
		skillIncText.addVerifyListener(new TextInputOnlyNumbers());
		formData = new FormData(40, -1);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(skillIncLabel, 5);
		skillIncText.setLayoutData(formData);
		
		skillIncLabel = new Label(content, SWT.NONE);
		skillIncLabel.setFont(DnFColor.TEMP2);
		skillIncLabel.setText(" %와 같은 가치의 스탯 수치 - ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(skillIncText, 5);
		skillIncLabel.setLayoutData(formData);
		
		Button skillIncButton = new Button(content, SWT.PUSH);
		skillIncButton.setText("스증뎀 수치 설정");
		formData = new FormData();
		formData.top = new FormAttachment(skillIncLabel, 10);
		formData.left = new FormAttachment(50, -80);
		formData.right = new FormAttachment(50, 80);
		skillIncButton.setLayoutData(formData);
		
		Composite valueComposite = new Composite(content, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(skillIncButton, 10);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		valueComposite.setLayoutData(formData);
		valueComposite.setLayout(new FormLayout());
		
		skillIncButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {				
				LinkedList<Entry<String, Double>> evaluateResult
					= Calculator.evaluateStatus(character.getRepresentSkill(), character.target, character, Double.parseDouble(skillIncText.getText()));
				skillInc=Double.parseDouble(skillIncText.getText());
				for(Control c : valueComposite.getChildren()) c.dispose();
				Label top = null;
				for(Entry<String, Double> entry : evaluateResult)
					top = makeLabel(valueComposite, top, entry);
				valueComposite.layout();
			}
		});		
		
		Label top = null;
		for(Entry<String, Double> entry : evaluateResult)
			top = makeLabel(valueComposite, top, entry);
		
		return content;
	}
	
	private Label makeLabel(Composite parent, Label top, Entry<String, Double> entry)
	{
		Label label = new Label(parent, SWT.NONE);
		label.setFont(DnFColor.TEMP2);
		String temp = String.format("%.2f", entry.getValue()); 
		if(temp.contains(".00")) temp=temp.substring(0, temp.length()-3);
		else if(temp.endsWith("0")) temp=temp.substring(0, temp.length()-1);
		label.setText("스증뎀 "+skillInc+"% == "+entry.getKey()+" "+temp);
		FormData formData = new FormData();
		if(top==null) formData.top = new FormAttachment(0, 10); 
		else formData.top = new FormAttachment(top, 20);
		formData.left = new FormAttachment(0, 10);
		label.setLayoutData(formData);
		return label;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("스탯 효율 비교");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.marginHeight = 10;
	}
	
	
	@Override
	protected void initializeBounds() 
	{ 
		super.initializeBounds(); 
		Shell shell = this.getShell(); 
		Rectangle bounds = parent.getBounds(); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + 40; 
		shell.setLocation (x, y); 
	} 
}
