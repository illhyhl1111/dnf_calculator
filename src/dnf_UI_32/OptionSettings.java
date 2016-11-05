package dnf_UI_32;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class OptionSettings extends Dialog {
	Options options;
	public OptionSettings(Shell shell, Options options){
		super(shell);
		this.options=options;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FormLayout());
		
		Label backgroundLabel = new Label(content, SWT.NONE);
		backgroundLabel.setText("아이템 정보창 투명 배경 사용\n(아이템 정보창 생성 속도가 다소 늦어질 수 있습니다)");
		FormData formData;
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		backgroundLabel.setLayoutData(formData);
		
		Button backgroundButton = new Button(content, SWT.CHECK);
		backgroundButton.setText("사용");
		backgroundButton.setSelection(options.transparentBackground);
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(backgroundLabel, 20);
		backgroundButton.setLayoutData(formData);
		backgroundButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				options.transparentBackground = backgroundButton.getSelection();
			}
		});
		
		return content;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("환경설정");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.CANCEL_ID, "나가기", false);
	}
}
