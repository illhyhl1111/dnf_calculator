package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.Location;
import dnf_class.Characters;
import dnf_class.Setting;

public class DungeonUI extends DnFComposite{
	Button toVillageButton;
	Button calculateSettings;
	Characters character;
	UserInfo itemInfo;
	UserInfo avatarInfo;
	DealChart dealChart;
	TrainingRoom trainingRoom;
	BuffInventory buffInventory;
	
	TabFolder inventoryFolder;
	
	private Shell shell;
	private Shell saveComposite;
	
	public DungeonUI(Shell shell, Characters character)
	{	
		this.shell=shell;
		this.character=character;
		saveComposite = new Shell(Display.getCurrent()); 
		
		toVillageButton = new Button(saveComposite, SWT.PUSH);
		toVillageButton.setText("asdf");
	}
	
	public void makeComposite()
	{
		mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setLayout(new FormLayout());
		
		TabFolder infoFolder = new TabFolder(mainComposite, SWT.NONE);
		FormData formData = new FormData();
		infoFolder.setLayoutData(formData);
		
		TabItem itemInfoTab = new TabItem(infoFolder, SWT.NONE);
		itemInfoTab.setText("장비");
		itemInfo = new UserInfo(infoFolder, character, Location.DUNGEON, this, 0);
		itemInfoTab.setControl(itemInfo.getComposite());
		
		TabItem avatarInfoTab = new TabItem(infoFolder, SWT.NONE);
		avatarInfoTab.setText("아바타/크리쳐/휘장");
		avatarInfo = new UserInfo(infoFolder, character, Location.DUNGEON, this, 1);
		avatarInfoTab.setControl(avatarInfo.getComposite());
		
		inventoryFolder = new TabFolder(mainComposite, SWT.NONE);
		InventoryCardPack inventoryPack = new InventoryCardPack(inventoryFolder, character);
		inventoryPack.setDungeonMode(this);
		inventoryPack.setDungeonListener(mainComposite);
		formData = new FormData();
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(infoFolder, 10);
		inventoryFolder.setLayoutData(formData);
		
		infoFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if(infoFolder.getSelection()[0].getText().equals(itemInfoTab.getText())){
					itemInfo.renew();
					inventoryFolder.setSelection(inventoryPack.inventoryTabList[0]);
				}
				else if(infoFolder.getSelection()[0].getText().equals(avatarInfoTab.getText())){
					avatarInfo.renew();
					inventoryFolder.setSelection(inventoryPack.inventoryTabList[inventoryPack.getAvatarTabIndex()]);
				}
			}
		});
		
		inventoryFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				boolean avatarMode=false;
				String selectedTab = inventoryFolder.getSelection()[0].getText();
				for(String str : inventoryPack.getAvatarModeList()){
					if(selectedTab.equals(str)){
						avatarInfo.renew();
						infoFolder.setSelection(avatarInfoTab);
						avatarMode=true;
						break;
					}
				}
				if(!avatarMode){
					itemInfo.renew();
					infoFolder.setSelection(itemInfoTab);
				}
			}
		});
		
		dealChart = new DealChart(mainComposite, character);
		formData = new FormData();
		formData.left = new FormAttachment(inventoryFolder, 10);
		formData.height=InterfaceSize.DEALCHART_Y;
		dealChart.setDealChart();
		dealChart.getComposite().setLayoutData(formData);
		
		trainingRoom = new TrainingRoom(mainComposite, this, character);
		formData = new FormData(InterfaceSize.TRAININGROOM_SIZE_X, InterfaceSize.TRAININGROOM_SIZE_Y);
		formData.left = new FormAttachment(infoFolder, 10);
		formData.top = new FormAttachment(0, 5);
		trainingRoom.getComposite().setLayoutData(formData);
		
		buffInventory = new BuffInventory(mainComposite, character, trainingRoom, this);
		buffInventory.setListener(this.getComposite());
		formData = new FormData();
		formData.top = new FormAttachment(trainingRoom.getComposite(), 5);
		formData.left = new FormAttachment(infoFolder, 10);
		buffInventory.getComposite().setLayoutData(formData);
		
		setItemSettingControls(infoFolder, dealChart);
		
		toVillageButton.setParent(mainComposite);
		FormData buttonData = new FormData(100, 100);
		buttonData.bottom = new FormAttachment(100, -10);
		buttonData.right = new FormAttachment(100, -10);
		toVillageButton.setLayoutData(buttonData);
	}
	
	private void setItemSettingControls(Composite infoFolder, DealChart dealChart)
	{
		Button saveSettings = new Button(mainComposite, SWT.NONE);
		saveSettings.setText("세팅 저장");
		FormData formData = new FormData(80, 70);
		formData.top = new FormAttachment(infoFolder, 5);
		saveSettings.setLayoutData(formData);
		
		Label separator = new Label(mainComposite, SWT.SEPARATOR);
		formData = new FormData();
		formData.top = new FormAttachment(infoFolder, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(saveSettings, 3);
		separator.setLayoutData(formData);
		
		Button getSettings = new Button(mainComposite, SWT.NONE);
		getSettings.setText("불러오기");
		formData = new FormData(80, 35);
		formData.top = new FormAttachment(infoFolder, 5);
		formData.right = new FormAttachment(infoFolder, 0, SWT.RIGHT);
		getSettings.setLayoutData(formData);
		
		Button setSettings = new Button(mainComposite, SWT.NONE);
		setSettings.setText("비교세팅 설정");
		formData = new FormData(80, 35);
		formData.top = new FormAttachment(getSettings, 5);
		formData.right = new FormAttachment(infoFolder, 0, SWT.RIGHT);
		setSettings.setLayoutData(formData);
		
		Button deleteSettings = new Button(mainComposite, SWT.NONE);
		deleteSettings.setText("세팅 삭제");
		formData = new FormData(80, 35);
		formData.top = new FormAttachment(setSettings, 5);
		formData.right = new FormAttachment(infoFolder, 0, SWT.RIGHT);
		deleteSettings.setLayoutData(formData);
		
		final Combo combo = new Combo(mainComposite, SWT.READ_ONLY);
		LinkedList<String> settings = new LinkedList<String>();
		for(Setting setting : character.userItemList.settingList)
			settings.add(setting.setting_name);
		combo.setItems(settings.toArray(new String[0]));
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(infoFolder, 5);
		comboData.left = new FormAttachment(separator, 3);
		comboData.right = new FormAttachment(getSettings, -3);
		combo.setLayoutData(comboData);
		
		saveSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveSettingDialog dialog = new SaveSettingDialog(shell);
				dialog.create();
				
				if (dialog.open() == Window.OK) {
			        Setting setting = character.getItemSetting().saveToClone(dialog.getName());
			        character.userItemList.settingList.add(setting);
			        settings.add(dialog.getName());
					combo.setItems(settings.toArray(new String[0]));
				}
			}
		});
		
		getSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = combo.getText();
				Setting setting = null;
				for(Setting s : character.userItemList.settingList){
					if(s.setting_name.equals(name)){
						setting=s;
						break;
					}
				}
				
				if(setting!=null){
					character.setItemSettings(setting, true);
					renew();
					MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
						    "세팅 "+name+" 을 불러왔습니다.",
						    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
					dialog.open();
				}
				else{
					MessageDialog dialog = new MessageDialog(shell, "실★패", null,
						    "세팅을 불러오는데 실패하였습니다.",
						    MessageDialog.ERROR, new String[] { "납득" }, 0);
					dialog.open();
				}
			}
		});
		
		setSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = combo.getText();
				Setting setting = null;
				for(Setting s : character.userItemList.settingList){
					if(s.setting_name.equals(name)){
						setting=s;
						break;
					}
				}
				
				if(setting!=null){
					dealChart.setCompareSetting(setting);
					MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
						    "세팅 "+name+" 을 비교세팅으로 설정하였습니다.",
						    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
					dialog.open();
				}
				else{
					MessageDialog dialog = new MessageDialog(shell, "실★패", null,
						    "세팅을 불러오는데 실패하였습니다.",
						    MessageDialog.ERROR, new String[] { "납득" }, 0);
					dialog.open();
				}
			}
		});
		
		deleteSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = combo.getText();
				Setting setting = null;
				for(Setting s : character.userItemList.settingList){
					if(s.setting_name.equals(name)) setting=s;
					break;
				}
				
				if(setting!=null){
					character.userItemList.settingList.remove(setting);
					settings.remove(name);
					combo.setItems(settings.toArray(new String[0]));
					MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
						    "세팅 "+name+" 을 삭제하였습니다.",
						    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
					dialog.open();
				}
				else{
					MessageDialog dialog = new MessageDialog(shell, "실★패", null,
						    "세팅을 삭제하는데 실패하였습니다.",
						    MessageDialog.ERROR, new String[] { "납득" }, 0);
					dialog.open();
				}
			}
		});
	}
	
	@Override
	public void renew()
	{
		itemInfo.renew();
		avatarInfo.renew();
		dealChart.setMonster(trainingRoom.getMonster());
		dealChart.renew();
		trainingRoom.renew();
	}
	
	public void disposeContent()
	{
		toVillageButton.setParent(saveComposite);
		mainComposite.dispose();
	}
	
	public Button get_toVillageButton() {return toVillageButton;}
	
	
	public class SaveSettingDialog extends TitleAreaDialog {

        private Text text;
        private String name;
        private Label warning;

        public SaveSettingDialog(Shell parentShell) {
        	super(parentShell);
        }

        @Override
        public void create() {
                super.create();
                setTitle("아이템 세팅 저장");
                setMessage("현제 아이템 세팅을 복제하여 저장합니다", IMessageProvider.INFORMATION);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
                Composite area = (Composite) super.createDialogArea(parent);
                Composite container = new Composite(area, SWT.NONE);
                container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                GridLayout layout = new GridLayout(2, false);
                container.setLayout(layout);

                createFirstName(container);
                warning = new Label(container, SWT.NONE);
                warning.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

                return area;
        }

        private void createFirstName(Composite container) {
                Label lbtFirstName = new Label(container, SWT.NONE);
                lbtFirstName.setText("세팅 이름(최대 10글자)");

                GridData dataFirstName = new GridData();
                dataFirstName.grabExcessHorizontalSpace = true;
                dataFirstName.horizontalAlignment = GridData.FILL;

                text = new Text(container, SWT.BORDER);
                text.setLayoutData(dataFirstName);
        }
        
        @Override
        protected boolean isResizable() {
                return true;
        }

        // save content of the Text fields because they get disposed
        // as soon as the Dialog closes
        private boolean saveInput() {
        	name = text.getText();
        	if(name.length()>10) return false;
        	return true;
        }

        @Override
        protected void okPressed() {
        	if(saveInput())
        		super.okPressed();
        	else{
        		warning.setText("저장할 세팅의 이름은 최대 10글자입니다");
        	}
        }

        public String getName() {
                return name;
        }

	}
}
