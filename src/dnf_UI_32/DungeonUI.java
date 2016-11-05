package dnf_UI_32;

import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
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
import dnf_infomation.GetDictionary;

public class DungeonUI extends DnFComposite{
	Button toVillageButton;
	Button calculateSettings;
	Characters character;
	UserInfo itemInfo;
	UserInfo avatarInfo;
	DealChart dealChart;
	TrainingRoom trainingRoom;
	BuffInventory buffInventory;
	BestSettingFinder bestSettingFinder;
	Inventory inventory;
	
	Combo settingsCombo;
	
	private Shell shell;
	private Shell saveComposite;
	
	public DungeonUI(Shell shell, Characters character)
	{	
		this.shell=shell;
		this.character=character;
		saveComposite = new Shell(Display.getCurrent()); 
		
		toVillageButton = new Button(saveComposite, SWT.PUSH);
		toVillageButton.setText("마을로 돌아가기");
	}
	
	public void makeComposite(SkillTree skillTree, Vault vault)
	{
		mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setLayout(new FormLayout());
		mainComposite.setBackgroundImage(GetDictionary.getBackground(character.getJob(), shell));
		
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
		
		inventory = new Inventory(mainComposite, character, this, Location.DUNGEON);
		inventory.setListener(vault);
		vault.setInventory(inventory);
		
		/*infoFolder.addSelectionListener(new SelectionAdapter() {
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
		});*/
		
		dealChart = new DealChart(mainComposite, character);
		formData = new FormData();
		formData.right = new FormAttachment(100, -3);
		formData.height=InterfaceSize.DEALCHART_Y;
		dealChart.setDealChart();
		dealChart.getComposite().setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(0, 145);
		formData.top = new FormAttachment(0, 32);
		dealChart.settingEvaluate.setLayoutData(formData);
		dealChart.settingEvaluate.moveAbove(null);
		
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
		
		formData = new FormData();
		formData.top = new FormAttachment(buffInventory.getComposite(), 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.left = new FormAttachment(infoFolder, 3);
		inventory.getComposite().setLayoutData(formData);
		
		setItemSettingControls(infoFolder, dealChart);
		
		calculateSettings = new Button(mainComposite, SWT.PUSH);
		calculateSettings.setText("세팅 최적화");
		
		bestSettingFinder = new BestSettingFinder(shell, character, settingsCombo);
		calculateSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				bestSettingFinder.open();
			}
		});
		
		toVillageButton.setParent(mainComposite);
		FormData buttonData = new FormData(100, 100);
		buttonData.bottom = new FormAttachment(100, -10);
		buttonData.right = new FormAttachment(100, -10);
		toVillageButton.setLayoutData(buttonData);
		
		buttonData = new FormData(100, 100);
		buttonData.bottom = new FormAttachment(toVillageButton, -10);
		buttonData.right = new FormAttachment(100, -10);
		calculateSettings.setLayoutData(buttonData);
		
		Canvas version = new Canvas(mainComposite, SWT.NO_REDRAW_RESIZE | SWT.TRANSPARENT);
		formData = new FormData(200, 40);
		formData.right = new FormAttachment(toVillageButton, -10);
		formData.bottom = new FormAttachment(100, 0);
		version.setLayoutData(formData);
		version.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	         e.gc.drawImage(GetDictionary.versionImage, 0, 0);
	        }
	    });
		
		skillTree.superInfo=this;
		shell.setText("인포창");
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
		
		Button deleteFindedSettings = new Button(mainComposite, SWT.NONE);
		deleteFindedSettings.setText("최적화세팅 삭제");
		formData = new FormData(100, 35);
		formData.top = new FormAttachment(setSettings, 5);
		formData.right = new FormAttachment(deleteSettings, -5);
		deleteFindedSettings.setLayoutData(formData);
		
		settingsCombo = new Combo(mainComposite, SWT.READ_ONLY);
		LinkedList<String> settings = new LinkedList<String>();
		for(Setting setting : character.userItemList.settingList)
			settings.add(setting.setting_name);
		settingsCombo.setItems(settings.toArray(new String[0]));
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(infoFolder, 5);
		comboData.left = new FormAttachment(separator, 3);
		comboData.right = new FormAttachment(getSettings, -3);
		settingsCombo.setLayoutData(comboData);
		
		saveSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveSettingDialog dialog = new SaveSettingDialog(shell, character.userItemList.settingList);
				dialog.create();
				
				if (dialog.open() == Window.OK) {
			        Setting setting = character.getItemSetting().saveToClone(dialog.getName());
			        character.userItemList.settingList.add(setting);
			        LinkedList<String> settings = new LinkedList<String>(Arrays.asList(settingsCombo.getItems()));
			        settings.add(dialog.getName());
			        settingsCombo.setItems(settings.toArray(new String[0]));
				}
			}
		});
		
		getSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = settingsCombo.getText();
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
				String name = settingsCombo.getText();
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
				String name = settingsCombo.getText();
				Setting setting = null;
				for(Setting s : character.userItemList.settingList){
					if(s.setting_name.equals(name)){
						setting=s;
						break;
					}
				}
				
				if(setting!=null){
					character.userItemList.settingList.remove(setting);
					LinkedList<String> settings = new LinkedList<String>(Arrays.asList(settingsCombo.getItems()));
					settings.remove(name);
					settingsCombo.setItems(settings.toArray(new String[0]));
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
		
		deleteFindedSettings.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkedList<String> items = new LinkedList<String>();
				LinkedList<Setting> newSettings = new LinkedList<Setting>();
				for(Setting setting : character.userItemList.settingList){
					if(!setting.setting_name.matches("^.*-\\d+위$")){
						items.add(setting.setting_name);
						newSettings.add(setting);
					}
				}

				settingsCombo.setItems(items.toArray(new String[0]));
				character.userItemList.settingList=newSettings;
				MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
					    "모든 최적화세팅을 삭제하였습니다.",
					    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				dialog.open();
				
			}
		});
	}
	
	@Override
	public void renew()
	{
		buffInventory.renew();		//가장 먼저
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
        private LinkedList<Setting> settingList;

        public SaveSettingDialog(Shell parentShell, LinkedList<Setting> settingList) {
        	super(parentShell);
        	this.settingList=settingList;
        }

        @Override
        public void create() {
                super.create();
                setTitle("아이템 세팅 저장");
                setMessage("현재 아이템 세팅을 복제하여 저장합니다", IMessageProvider.INFORMATION);
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
        	else{
        		for(Setting setting : settingList)
        			if(setting.setting_name.equals(name)) return false;
        	}
        	return true;
        }

        @Override
        protected void okPressed() {
        	if(saveInput())
        		super.okPressed();
        	else{
        		warning.setText("저장할 세팅의 이름은 최대 10글자 / 중복 불가능입니다");
        	}
        }

        public String getName() {
                return name;
        }

	}
}
