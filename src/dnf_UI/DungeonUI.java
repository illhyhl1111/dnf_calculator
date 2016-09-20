package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.Location;
import dnf_class.Characters;

public class DungeonUI {
	Composite dungeonComposite;
	Button toVillageButton;
	Button calculateSettings;
	Characters character;
	UserInfo itemInfo;
	UserInfo avatarInfo;
	
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
	
	public void renew()
	{
		dungeonComposite = new Composite(shell, SWT.NONE);
		dungeonComposite.setLayout(new FormLayout());
		
		TabFolder infoFolder = new TabFolder(dungeonComposite, SWT.NONE);
		FormData formData = new FormData();
		infoFolder.setLayoutData(formData);
		
		TabItem itemInfoTab = new TabItem(infoFolder, SWT.NONE);
		itemInfoTab.setText("장비");
		itemInfo = new UserInfo(infoFolder, character, Location.DUNGEON, 0);
		itemInfoTab.setControl(itemInfo.getComposite());
		
		TabItem avatarInfoTab = new TabItem(infoFolder, SWT.NONE);
		avatarInfoTab.setText("아바타/크리쳐/휘장");
		avatarInfo = new UserInfo(infoFolder, character, Location.DUNGEON, 1);
		avatarInfoTab.setControl(avatarInfo.getComposite());
		
		Button saveSettings = new Button(dungeonComposite, SWT.NONE);
		saveSettings.setText("세팅 저장");
		formData = new FormData(100, 70);
		formData.top = new FormAttachment(infoFolder, 10);
		saveSettings.setLayoutData(formData);
		
		inventoryFolder = new TabFolder(dungeonComposite, SWT.NONE);
		InventoryCardPack inventoryPack = new InventoryCardPack(inventoryFolder, character, itemInfo);
		inventoryPack.setDungeonMode();
		inventoryPack.setDungeonListener(dungeonComposite);
		formData = new FormData();
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(infoFolder, 10);
		inventoryFolder.setLayoutData(formData);
		
		toVillageButton.setParent(dungeonComposite);
		FormData buttonData = new FormData(100, 100);
		buttonData.bottom = new FormAttachment(100, -10);
		buttonData.right = new FormAttachment(100, -10);
		toVillageButton.setLayoutData(buttonData);
		toVillageButton.moveAbove(null);
	}
	
	public void disposeContent()
	{
		toVillageButton.setParent(saveComposite);
		dungeonComposite.dispose();
	}
	
	public Composite getComposite() { return dungeonComposite;}
	public Button get_toVillageButton() {return toVillageButton;}
}
