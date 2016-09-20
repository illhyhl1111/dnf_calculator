package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.Location;
import dnf_class.Characters;
import dnf_class.Item;

interface Info{
	public Composite getComposite();
	public void renew();
}

class UserItemInfo implements Info
{
	private Composite itemInfoComposite;
	private Composite leftItemInfoComposite;
	private Composite rightItemInfoComposite;
	private ItemButton<Item>[] itemButtonList;
	static final int ITEMNUM=13;
	private Characters character;
	private Composite itemInfo;
	private Composite setInfo;
	
	@SuppressWarnings("unchecked")
	public UserItemInfo(Composite parent, Characters character, UserInfo superInfo)
	{
		this.character=character;
		itemInfoComposite = new Composite(parent, SWT.BORDER);
		FillLayout wholeLayout = new FillLayout();
		wholeLayout.marginWidth=0;
		//wholeLayout.wrap=false;
		//wholeLayout.pack=true;
		itemInfoComposite.setLayout(wholeLayout);
		
		//TODO itemInfoComposite.setBackgroundImage(배경그림);
		
		leftItemInfoComposite = new Composite(itemInfoComposite, SWT.NONE);
		Composite characterImageComposite = new Composite(itemInfoComposite, SWT.NONE);
		rightItemInfoComposite = new Composite(itemInfoComposite, SWT.NONE);
		
		//characterImageComposite.setBackgroundImage(new Image(parent.getDisplay(), character.getCharImageAddress()));		//가운데 이미지 ->캐릭터 이미지
		
		GridLayout itemInfoLayout = new GridLayout(2, true);
		itemInfoLayout.horizontalSpacing=3;
		itemInfoLayout.verticalSpacing=3;
		itemInfoLayout.marginHeight=0;
		itemInfoLayout.marginWidth=0;
		itemInfoLayout.makeColumnsEqualWidth=true;
		leftItemInfoComposite.setLayout(itemInfoLayout);
		rightItemInfoComposite.setLayout(itemInfoLayout);

		itemButtonList = (ItemButton<Item>[]) new ItemButton<?>[ITEMNUM];

		int BUTTON_SIZE = InterfaceSize.INFO_BUTTON_SIZE;
		itemButtonList[0] = new ItemButton<Item>(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOULDER), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[1] = new ItemButton<Item>(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.ROBE), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[2] = new ItemButton<Item>(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.TROUSER), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[3] = new ItemButton<Item>(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.BELT), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[4] = new ItemButton<Item>(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOES), BUTTON_SIZE, BUTTON_SIZE, true);
		
		itemButtonList[5] = new ItemButton<Item>(rightItemInfoComposite, character.getWeapon(), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[6] = new ItemButton<Item>(rightItemInfoComposite, character.getTitle(), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[7] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.BRACELET), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[8] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.NECKLACE), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[9] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.AIDEQUIPMENT), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[10] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.RING), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[11] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.EARRING), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[12] = new ItemButton<Item>(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.MAGICSTONE), BUTTON_SIZE, BUTTON_SIZE, true);
		
		
		Point buttonS = itemButtonList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		buttonS.x+=3; buttonS.y+=3;
		for(int i=0; i<ITEMNUM; i++)
		{
			SetListener listenerGroup = new SetListener(itemButtonList[i], character, superInfo, itemInfo, setInfo, parent);
			
			itemButtonList[i].getButton().addListener(SWT.MouseDown, listenerGroup.unequipListener()); 				// add MouseDown Event - unequip
			itemButtonList[i].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());		// add MouseDoubleClick - modify
			itemButtonList[i].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(superInfo.getComposite().getParent()));		// add MouseEnter Event - make composite
			itemButtonList[i].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
			itemButtonList[i].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
		}
		
		GridData buttonGridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		for(int i=0; i<ITEMNUM; i++){
			itemButtonList[i].getButton().setData(buttonGridData);
		}
	}
	
	public void renew()
	{
		itemButtonList[0].setItem(character.getEquipmentList().get(Equip_part.SHOULDER));
		itemButtonList[1].setItem(character.getEquipmentList().get(Equip_part.ROBE));
		itemButtonList[2].setItem(character.getEquipmentList().get(Equip_part.TROUSER));
		itemButtonList[3].setItem(character.getEquipmentList().get(Equip_part.BELT));
		itemButtonList[4].setItem(character.getEquipmentList().get(Equip_part.SHOES));
		itemButtonList[5].setItem(character.getWeapon());
		itemButtonList[6].setItem(character.getTitle());
		itemButtonList[7].setItem(character.getEquipmentList().get(Equip_part.BRACELET));
		itemButtonList[8].setItem(character.getEquipmentList().get(Equip_part.NECKLACE));
		itemButtonList[9].setItem(character.getEquipmentList().get(Equip_part.AIDEQUIPMENT));
		itemButtonList[10].setItem(character.getEquipmentList().get(Equip_part.RING));
		itemButtonList[11].setItem(character.getEquipmentList().get(Equip_part.EARRING));
		itemButtonList[12].setItem(character.getEquipmentList().get(Equip_part.MAGICSTONE));
		
		
		for(int i=0; i<ITEMNUM; i++)
		{
			itemButtonList[i].renewImage(true);
		}
	}
	
	public Composite getComposite() {return itemInfoComposite;}
	
	public boolean equiped(Item item)
	{
		for(ItemButton<Item> i : itemButtonList)
			if(i.getItem().getName().equals(item.getName())) return true;
		return false;
	}
}

class UserAvatarInfo implements Info
{
	private Composite wholeComposite;
	private Composite avatarInfoComposite;
	private ItemButton<Item>[] itemButtonList;
	private ItemButton<Item> creatureButton;
	private ItemButton<Item> drapeButton;
	static final int AVATARNUM=10;
	private Characters character;
	private Composite avatarInfo;
	private Composite setInfo;
	Avatar_part[] partOrder= { Avatar_part.CAP, Avatar_part.HAIR, Avatar_part.FACE, Avatar_part.NECK, Avatar_part.COAT,
			Avatar_part.SKIN, Avatar_part.BELT, Avatar_part.PANTS, Avatar_part.SHOES, Avatar_part.AURA};
	
	@SuppressWarnings("unchecked")
	public UserAvatarInfo(Composite parent, Characters character, UserInfo superInfo)
	{
		this.character=character;
		int BUTTON_SIZE = InterfaceSize.INFO_BUTTON_SIZE;
		wholeComposite = new Composite(parent, SWT.BORDER);
		wholeComposite.setLayout(new FormLayout());
		
		//TODO wholeComposite.setBackgroundImage(배경그림);
		
		avatarInfoComposite = new Composite(wholeComposite, SWT.NONE);

		GridLayout avatarInfoLayout = new GridLayout(3, true);
		avatarInfoLayout.horizontalSpacing=3;
		avatarInfoLayout.verticalSpacing=3;
		avatarInfoLayout.marginHeight=0;
		avatarInfoLayout.marginWidth=0;
		avatarInfoLayout.makeColumnsEqualWidth=true;
		avatarInfoComposite.setLayout(avatarInfoLayout);

		itemButtonList = (ItemButton<Item>[]) new ItemButton<?>[AVATARNUM]; 
		for(int i=0; i<AVATARNUM; i++)
			itemButtonList[i] = new ItemButton<Item>(avatarInfoComposite, character.getAvatarList().get(partOrder[i]), BUTTON_SIZE, BUTTON_SIZE);
		
		creatureButton = new ItemButton<Item>(wholeComposite, character.getCreature(), BUTTON_SIZE, BUTTON_SIZE);
		drapeButton = new ItemButton<Item>(wholeComposite, character.getDrape(), BUTTON_SIZE, BUTTON_SIZE);
		
		Point buttonS = itemButtonList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		buttonS.x+=3; buttonS.y+=3;
		
		avatarInfoComposite.setLayoutData(new FormData());
		
		FormData creatureInfoData = new FormData();
		creatureInfoData.left = new FormAttachment(0, buttonS.x);
		creatureInfoData.top = new FormAttachment(0, buttonS.y*3);
		creatureButton.getButton().setLayoutData(creatureInfoData);
		creatureButton.getButton().moveAbove(avatarInfoComposite);
		
		FormData drapeInfoData = new FormData();
		drapeInfoData.left = new FormAttachment(creatureButton.getButton(), 3);
		drapeInfoData.top = new FormAttachment(0, buttonS.y*3);
		drapeButton.getButton().setLayoutData(drapeInfoData);
		drapeButton.getButton().moveAbove(avatarInfoComposite);
		
		
		for(int i=0; i<AVATARNUM; i++)
		{
			SetListener listenerGroup = new SetListener(itemButtonList[i], character, superInfo, avatarInfo, setInfo, parent);
			
			itemButtonList[i].getButton().addListener(SWT.MouseDown, listenerGroup.unequipListener()); 				// add MouseDown Event - unequip
			itemButtonList[i].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());		// add MouseDoubleClick - modify
			itemButtonList[i].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(superInfo.getComposite().getParent()));		// add MouseEnter Event - make composite
			itemButtonList[i].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
			itemButtonList[i].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
		}
		
		SetListener listenerGroup = new SetListener(creatureButton, character, superInfo, avatarInfo, null, parent);
		creatureButton.getButton().addListener(SWT.MouseDown, listenerGroup.unequipListener());					// add MouseDown Event - unequip
		creatureButton.getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());			// add MouseDoubleClick - modify
		creatureButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(superInfo.getComposite().getParent()));		// add MouseEnter Event - make composite
		creatureButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
		creatureButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
		
		listenerGroup = new SetListener(drapeButton, character, superInfo, avatarInfo, null, parent);
		drapeButton.getButton().addListener(SWT.MouseDown, listenerGroup.unequipListener());					// add MouseDown Event - unequip
		drapeButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(superInfo.getComposite().getParent()));		// add MouseEnter Event - make composite
		drapeButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
		drapeButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
		
		
		GridData buttonGridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		for(int i=0; i<AVATARNUM; i++){
			itemButtonList[i].getButton().setData(buttonGridData);
		}
	}
	
	public void renew()
	{
		for(int i=0; i<AVATARNUM; i++){
			itemButtonList[i].setItem(character.getAvatarList().get(partOrder[i]));
			itemButtonList[i].renewImage(true);
		}
		creatureButton.setItem(character.getCreature());
		creatureButton.renewImage(true);
		drapeButton.setItem(character.getDrape());
		drapeButton.renewImage(true);
	}
	
	public Composite getComposite() {return wholeComposite;}
	
	public boolean equiped(Item item)
	{
		for(ItemButton<Item> i : itemButtonList)
			if(i.getItem().getName().equals(item.getName())) return true;
		return false;
	}
}


public class UserInfo
{
	Info userItemInfo;
	Composite selectModeComposite;
	InfoStatus infoStatus;
	NonInfoStatus nonInfoStatus;
	private Composite userInfoComposite;
	private boolean dungeonMode = false;
	private Characters character;
	int mode;
	Location location;
	
	public UserInfo(Composite parent, Characters character, Location location, int mode)
	{
		this.character=character;
		this.location=location;
		this.mode=mode;
		userInfoComposite = new Composite(parent, SWT.BORDER);
		userInfoComposite.setLayout(new FormLayout());
		
		if(mode==0)				//장비
			userItemInfo = new UserItemInfo(userInfoComposite, character, this);
		else if(mode==1)		//아바타
			userItemInfo = new UserAvatarInfo(userInfoComposite, character, this);
		
		infoStatus = new InfoStatus(userInfoComposite, character, dungeonMode);
		nonInfoStatus = new NonInfoStatus(userInfoComposite, character, dungeonMode);
		
		switch(location)
		{
		case VILLAGE:
			init_village();
			break;
		case DUNGEON:
			init_dungeon();
			break;
		}
	}
	
	private void init_village()
	{
		int interval = InterfaceSize.USER_INFO_INTERVAL;
		
		userItemInfo.getComposite().setLayoutData(new FormData(InterfaceSize.USER_INFO_ITEM_SIZE_X, InterfaceSize.USER_INFO_ITEM_SIZE_Y));
		
		selectModeComposite = new Composite (userInfoComposite, SWT.BORDER | SWT.NO_RADIO_GROUP);
		selectModeComposite.setLayout (new GridLayout(2, true));
		Listener radioGroup = event -> {
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
			if(button2.getText().equals("마을인포")) dungeonMode=false;
			else dungeonMode=true;
			nonInfoStatus.isDungeon=dungeonMode;
			infoStatus.isDungeon=dungeonMode;
			infoStatus.renew();
			nonInfoStatus.renew();
		};
		
		FormData selectModeData = new FormData(InterfaceSize.USER_STAT_MODE_SIZE_X, InterfaceSize.USER_STAT_MODE_SIZE_Y);
		selectModeData.top = new FormAttachment(userItemInfo.getComposite(), interval);
		selectModeComposite.setLayoutData(selectModeData);
		
		Button setVillageMode = new Button (selectModeComposite, SWT.RADIO);
		setVillageMode.setText("마을인포");
		setVillageMode.addListener(SWT.Selection, radioGroup);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		setVillageMode.setLayoutData(gridData);
		setVillageMode.setSelection (true);
		
		Button setDungeonMode = new Button (selectModeComposite, SWT.RADIO);
		setDungeonMode.setText("던전인포");
		setDungeonMode.addListener(SWT.Selection, radioGroup);
		setDungeonMode.setLayoutData(gridData);
		
		FormData infoStatusData = new FormData(InterfaceSize.USER_INFO_STAT_SIZE_X, InterfaceSize.USER_INFO_STAT_SIZE_Y);
		infoStatusData.top = new FormAttachment(selectModeComposite, interval);
		infoStatus.getComposite().setLayoutData(infoStatusData);
		
		FormData nonInfoStatusData = new FormData(InterfaceSize.USER_INFO_NONSTAT_SIZE_X, InterfaceSize.USER_INFO_NONSTAT_SIZE_Y);
		nonInfoStatusData.left = new FormAttachment(userItemInfo.getComposite(), interval);
		nonInfoStatus.getComposite().setLayoutData(nonInfoStatusData);
	}
	
	private void init_dungeon()
	{
		int interval = InterfaceSize.USER_INFO_INTERVAL;
		
		userItemInfo.getComposite().setLayoutData(new FormData(InterfaceSize.USER_INFO_ITEM_SIZE_X, InterfaceSize.USER_INFO_ITEM_SIZE_Y));
		
		FormData infoStatusData = new FormData(InterfaceSize.USER_INFO_STAT_SIZE_X, InterfaceSize.USER_INFO_STAT_SIZE_Y);
		infoStatusData.top = new FormAttachment(userItemInfo.getComposite(), interval);
		infoStatus.getComposite().setLayoutData(infoStatusData);
		
		FormData nonInfoStatusData = new FormData(InterfaceSize.USER_INFO_NONSTAT_SIZE_INDUNGEON_X, InterfaceSize.USER_INFO_NONSTAT_SIZE_INDUNGEON_Y);
		nonInfoStatusData.top = new FormAttachment(infoStatus.getComposite(), interval);
		nonInfoStatus.getComposite().setLayoutData(nonInfoStatusData);
		
		infoStatus.isDungeon=true;
		nonInfoStatus.isDungeon=true;
	}
	
	public void renew()
	{
		character.setStatus();
		userItemInfo.renew();
		infoStatus.renew();
		nonInfoStatus.renew();
	}
	
	public Composite getComposite()
	{
		return userInfoComposite;
	}
}