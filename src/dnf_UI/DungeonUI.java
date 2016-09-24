package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Location;
import dnf_class.Characters;
import dnf_infomation.GetDictionary;

public class DungeonUI extends DnFComposite{
	Button toVillageButton;
	Button calculateSettings;
	Characters character;
	UserInfo itemInfo;
	UserInfo avatarInfo;
	DealChart dealChart;
	
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
		
		Button saveSettings = new Button(mainComposite, SWT.NONE);
		saveSettings.setText("세팅 저장");
		formData = new FormData(100, 70);
		formData.top = new FormAttachment(infoFolder, 10);
		saveSettings.setLayoutData(formData);
		
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
		try {
			dealChart.setDealChart(GetDictionary.charDictionary.getMonsterInfo("임시몬스터"));
			dealChart.getComposite().setLayoutData(formData);
		} catch (ItemNotFoundedException e) {
			e.printStackTrace();
		}
		
		toVillageButton.setParent(mainComposite);
		FormData buttonData = new FormData(100, 100);
		buttonData.bottom = new FormAttachment(100, -10);
		buttonData.right = new FormAttachment(100, -10);
		toVillageButton.setLayoutData(buttonData);
	}
	
	@Override
	public void renew()
	{
		itemInfo.renew();
		avatarInfo.renew();
		dealChart.renew();
	}
	
	public void disposeContent()
	{
		toVillageButton.setParent(saveComposite);
		mainComposite.dispose();
	}
	
	public Button get_toVillageButton() {return toVillageButton;}
}
