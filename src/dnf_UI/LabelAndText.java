package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

abstract class LabelAndInput
{
	Label label;
	Composite composite;
	Widget input;
	Composite parent;
	
	public LabelAndInput(Composite parent, String LabelString)
	{
		this.parent=parent;
		composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns=2;
		composite.setLayout(compositeLayout);
		
		label = new Label(composite, SWT.NONE);
		GridData labelData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		labelData.grabExcessHorizontalSpace=true;
		labelData.minimumWidth=80;
		labelData.heightHint=20;
		label.setLayoutData(labelData);
		label.setText(LabelString);
	}
	public void setLabelString(String str) { label.setText(str);}
	public abstract void setInputEnable(boolean bool);
	public abstract void setTextString(String str);
	public void setGridLayout(GridLayout layout)
	{
		composite.setLayout(layout);
	}
	public void setLabelData(GridData gridData)
	{
		label.setLayoutData(gridData);
	}
}

public class LabelAndText extends LabelAndInput
{
	
	public LabelAndText(Composite parent, String LabelString, String TextString)
	{
		super(parent, LabelString);
		
		input = new Text(composite, SWT.RIGHT);
		GridData textData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		textData.grabExcessHorizontalSpace=true;
		textData.minimumWidth=80;
		textData.heightHint=20;
		((Text)input).setLayoutData(textData);
		((Text)input).setText(TextString);
		//text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        //text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setInputEnable(boolean bool) {
		((Text)input).setEnabled(bool);
		if(!bool) ((Text)input).setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLACK));
	}
	public void setTextString(String str) {((Text)input).setText(str);}
	public void setTextData(GridData gridData)
	{
		((Text)input).setLayoutData(gridData);
	}
}
