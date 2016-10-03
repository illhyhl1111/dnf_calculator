package dnf_UI_32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class LabelAndCheckButton extends LabelAndInput
{
	public LabelAndCheckButton(Composite parent, String LabelString, String ButtonString)
	{
		super(parent, LabelString);
		
		input = new Button(composite, SWT.CHECK);
		GridData buttonData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		buttonData.heightHint=20;
		((Button)input).setLayoutData(buttonData);
		((Button)input).setText(ButtonString);
	}
	
	public void setInputEnable(boolean bool) { ((Button)input).setEnabled(bool); }
	public void setTextString(String str) { ((Button)input).setText(str);}
	public void setButtonCheck(boolean bool) { ((Button)input).setSelection(bool);}
}
