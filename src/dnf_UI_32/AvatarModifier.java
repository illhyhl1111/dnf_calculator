package dnf_UI_32;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_class.Characters;

public class AvatarModifier extends Dialog {
	Shell shell;
	final Characters character;
	final UserInfo userInfo;
	Composite itemInfo;
	Point itemInfoSize;
	Point contentSize;
	
	public AvatarModifier(Shell shell, Characters character, UserInfo userInfo)
	{
		super(shell);
		this.shell=shell;
		this.userInfo=userInfo;
		this.character=character;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		RowLayout contentLayout = new RowLayout(SWT.VERTICAL);
		contentLayout.spacing=10;
		contentLayout.wrap=false;
		content.setLayout(contentLayout);
		
		Label guideLabel = new Label(content, SWT.WRAP);
		guideLabel.setText("\n ※장착 엠블렘을 최적화시킵니다.\n\n"
				+ "   던전 물리(마법)크리티컬 97%(만크리)을 넘기는 것을 전제로 스탯을 가장 높게 하는 엠블렘을 장착합니다.\n\n"
				+ "   \'자동 적용\'을 체크할 경우 장착 아이템이 변할 때 마다 자동으로 이 기능을 사용합니다.\n\n");
		guideLabel.setLayoutData(new RowData());
		
		////////////////////설정
		
		Group enhance = new Group(content, SWT.NONE);
		enhance.setText("설정");
		enhance.setLayoutData(new RowData());
		enhance.setLayout(new FormLayout());
		
		guideLabel = new Label(enhance, SWT.WRAP);
		guideLabel.setText("물리/마법 선택 → 엠블렘 레어리티 선택 → 적용버튼 누르기\n\n");
		FormData guideData = new FormData();
		guideData.left = new FormAttachment(5, 0);
		guideData.top = new FormAttachment(5, 0);
		guideLabel.setLayoutData(guideData);	
		
		Group selectModeComposite = new Group (enhance, SWT.NO_RADIO_GROUP);
		selectModeComposite.setText("물리/마법 선택");
		FormData selectModeData = new FormData();
		selectModeData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN);
		selectModeComposite.setLayoutData(selectModeData);
		selectModeComposite.setLayout (new GridLayout(2, true));
		
		Button[] modeButton = setRaidoButton(selectModeComposite, new String[] {"물리", "마법"});
		
		
		Group selectRarityComposite = new Group (enhance, SWT.NONE);
		selectRarityComposite.setText("엠블렘 레어리티 선택");
		FormData rarityData = new FormData();
		rarityData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN);
		rarityData.left = new FormAttachment(selectModeComposite, InterfaceSize.MARGIN);
		selectRarityComposite.setLayoutData(rarityData);
		selectRarityComposite.setLayout(new GridLayout(3, true));
		
		Button[] rarityButton = setRaidoButton(selectRarityComposite, new String[] {"찬란한", "화려한", "빛나는"});
		
		if(character.autoOptimize){
			if(character.getAutoOptimizeMode()==1){
				modeButton[0].setSelection(false);
				modeButton[1].setSelection(true);
			}
			if(character.getAutoOptimizeRarity()==Item_rarity.RARE){
				rarityButton[0].setSelection(false);
				rarityButton[1].setSelection(true);
			}
			else if(character.getAutoOptimizeRarity()==Item_rarity.UNCOMMON){
				rarityButton[0].setSelection(false);
				rarityButton[2].setSelection(true);
			}
		}
		
	    Button okButton = new Button(enhance, SWT.PUSH);
	    okButton.setText("적용");
	    FormData okButtonData = new FormData(100, 30);
	    okButtonData.top = new FormAttachment(selectRarityComposite, InterfaceSize.MARGIN*4);
	    okButtonData.right = new FormAttachment(100, -InterfaceSize.MARGIN);
	    okButton.setLayoutData(okButtonData);
	    
	    okButton.addListener(SWT.Selection, new Listener(){
	    	@Override
	        public void handleEvent(Event e) {
				int mode = getMode(modeButton);
				Item_rarity rarity = getRarity(rarityButton);
				
				character.optimizeEmblem(mode, rarity);
				userInfo.renew();
				
				MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
					    "엠블렘 최적화 완료",
					    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				dialog.open();
			 }
		});
	    
	    //////////////////자동적용
	    
	    Group autoApply = new Group(content, SWT.NONE);
	    autoApply.setText("자동 적용");
	    autoApply.setLayoutData(new RowData());
	    autoApply.setLayout(new FormLayout());
		
		guideLabel = new Label(autoApply, SWT.WRAP);
		guideLabel.setText("장비 장착/해제시마다 자동으로 위 설정의 엠블렘 최적화를 적용시킵니다.  \n\n"
				+ "(아이템 옵션 해제 혹은 마법부여와 같은 변화에는 자동으로 적용되지 않습니다)\n\n");
		guideData = new FormData();
		guideData.left = new FormAttachment(5, 0);
		guideData.top = new FormAttachment(5, 0);
		guideLabel.setLayoutData(guideData);	
		
		Button autoApplyButton = new Button(autoApply, SWT.CHECK);
		autoApplyButton.setText("자동 적용하기");
		autoApplyButton.setSelection(character.autoOptimize);
		FormData autoData = new FormData();
		autoData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN*2);
		autoApplyButton.setLayoutData(autoData);
	    
	    Button okButton2 = new Button(autoApply, SWT.PUSH);
	    okButton2.setText("적용");
	    okButtonData = new FormData(100, 30);
	    okButtonData.top = new FormAttachment(autoApplyButton, InterfaceSize.MARGIN*4);
	    okButtonData.right = new FormAttachment(100, -InterfaceSize.MARGIN);
	    okButton2.setLayoutData(okButtonData);
	    
	    okButton2.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				character.autoOptimize=autoApplyButton.getSelection();
				character.setAutoOptimizeMode(getMode(modeButton));
				character.setAutoOptimizeRarity(getRarity(rarityButton));
				character.optimizeEmblem(getMode(modeButton), getRarity(rarityButton));
				userInfo.renew();
				
				MessageDialog dialog;
				if(autoApplyButton.getSelection())
					dialog = new MessageDialog(shell, "성☆공", null,
							"설정하였습니다",
							MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				else
					dialog = new MessageDialog(shell, "성☆공", null,
						    "설정 해제하였습니다",
						    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				
				dialog.open();
			}
	    });
	    
	    contentSize = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    
		return content;
	}
	
	public int getMode(Button[] modeButton)
	{
		String text=null;
		int mode;
		for(Button b : modeButton)
			if(b.getSelection()){
				text=b.getText();
				break;
			}
		
		if(text.equals("물리")) mode=0;
		else mode=1;
		
		return mode;
	}
	
	public Item_rarity getRarity(Button[] rarityButton)
	{
		String text=null;
		Item_rarity rarity;
		for(Button b : rarityButton)
			if(b.getSelection()){
				text=b.getText();
				break;
			}
		
		if(text.equals("찬란한")) rarity=Item_rarity.UNIQUE;
		else if(text.equals("화려한")) rarity=Item_rarity.RARE;
		else rarity=Item_rarity.UNCOMMON;
		
		return rarity;
	}
	
	public Button[] setRaidoButton(Composite selectModeComposite, String[] buttonNameList)
	{
		Listener radioGroup1 = event -> {
			Control [] children = selectModeComposite.getChildren ();
			for (int j=0; j<children.length; j++) {
				Control child = children [j];
				if (child instanceof Button) {
					Button button1 = (Button) child;
					if ((button1.getStyle() & SWT.RADIO) != 0) button1.setSelection (false);
				}
			}
			Button button2 = (Button) event.widget;
			button2.setSelection (true);
		};
	
		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = GridData.CENTER;
		buttonData.grabExcessHorizontalSpace = true;
		
		Button[] buttonList = new Button[buttonNameList.length];
		
		for(int i=0; i<buttonList.length; i++)
		{
			buttonList[i] = new Button (selectModeComposite, SWT.RADIO);
			buttonList[i].setText(buttonNameList[i]);
			buttonList[i].addListener(SWT.Selection, radioGroup1);
			buttonList[i].setLayoutData(buttonData);
		}
		buttonList[0].setSelection(true);
		
		return buttonList;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("아이템 일괄변경");
	}
		
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS| SWT.BORDER | SWT.TITLE);
	    setBlockOnOpen(false);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.CANCEL_ID, "나가기", false);
	}
	
	@Override
	protected Point getInitialSize() {
	    return new Point(contentSize.x+130, contentSize.y+100);
	}
}