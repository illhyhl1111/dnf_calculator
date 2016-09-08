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
import dnf_class.Characters;
import dnf_class.Item;

class UserItemInfo
{
	private Composite itemInfoComposite;
	private Composite leftItemInfoComposite;
	private Composite rightItemInfoComposite;
	private ItemButton[] itemButtonList;
	static final int ITEMNUM=13;
	private Characters character;
	private Composite itemInfo;
	private Composite setInfo;
	
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

		itemButtonList = new ItemButton[ITEMNUM];

		int BUTTON_SIZE = InterfaceSize.INFO_BUTTON_SIZE;
		itemButtonList[0] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOULDER), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[1] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.ROBE), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[2] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.TROUSER), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[3] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.BELT), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[4] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOES), BUTTON_SIZE, BUTTON_SIZE, true);
		
		itemButtonList[5] = new ItemButton(rightItemInfoComposite, character.getWeapon(), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[6] = new ItemButton(rightItemInfoComposite, character.getTitle(), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[7] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.BRACELET), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[8] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.NECKLACE), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[9] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.AIDEQUIPMENT), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[10] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.RING), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[11] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.EARRING), BUTTON_SIZE, BUTTON_SIZE, true);
		itemButtonList[12] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.MAGICSTONE), BUTTON_SIZE, BUTTON_SIZE, true);
		
		
		Point buttonS = itemButtonList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		buttonS.x+=3; buttonS.y+=3;
		for(int i=0; i<ITEMNUM; i++)
		{
			Integer x0;
			if(i>4) x0 = InterfaceSize.USER_INFO_ITEM_SIZE_X-2*buttonS.x+((i-5)%2)*buttonS.x-6;
			else x0 = (i%2)*buttonS.x+7;
			
			Integer y0;
			if(i>4) y0 = (int)((i-5)/2)*buttonS.y+7;
			else y0 = (int)(i/2)*buttonS.y+7;
			
			SetListener listenerGroup = new SetListener(itemButtonList[i], character, superInfo, itemInfo, setInfo, x0, y0, parent);
			
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
		for(ItemButton i : itemButtonList)
			if(i.getItem().getName().equals(item.getName())) return true;
		return false;
	}
}

class UserAvatarInfo
{
	private Composite wholeComposite;
	private Composite avatarInfoComposite;
	private Composite creatureInfoComposite;
	private Composite drapeInfoComposite;
	private ItemButton[] itemButtonList;
	static final int AVATARNUM=9;
	private Characters character;
	private Composite avatarInfo;
	private Composite setInfo;
	
	public UserAvatarInfo(Composite parent, Characters character, UserInfo superInfo)
	{
		this.character=character;
		wholeComposite = new Composite(parent, SWT.BORDER);
		wholeComposite.setLayout(new FormLayout());
		
		//TODO wholeComposite.setBackgroundImage(배경그림);
		
		avatarInfoComposite = new Composite(wholeComposite, SWT.NONE);
		avatarInfoComposite.setLayoutData(new FormData());
		
		creatureInfoComposite = new Composite(wholeComposite, SWT.NONE);
		FormData creatureInfoData = new FormData();
		creatureInfoData.left = new FormAttachment(avatarInfoComposite, 10);
		creatureInfoComposite.setLayoutData(creatureInfoData);
		
		drapeInfoComposite = new Composite(wholeComposite, SWT.NONE);
		FormData drapeInfoData = new FormData();
		drapeInfoData.left = new FormAttachment(avatarInfoComposite, 10);
		drapeInfoData.top = new FormAttachment(creatureInfoComposite, 10);
		drapeInfoComposite.setLayoutData(creatureInfoData);
		
		GridLayout avatarInfoLayout = new GridLayout(3, true);
		avatarInfoLayout.horizontalSpacing=3;
		avatarInfoLayout.verticalSpacing=3;
		avatarInfoLayout.marginHeight=0;
		avatarInfoLayout.marginWidth=0;
		avatarInfoLayout.makeColumnsEqualWidth=true;
		avatarInfoComposite.setLayout(avatarInfoLayout);
		
		creatureInfoComposite.setLayout(new FillLayout());
		drapeInfoComposite.setLayout(new FillLayout());

		itemButtonList = new ItemButton[AVATARNUM];
		int BUTTON_SIZE = InterfaceSize.INFO_BUTTON_SIZE;
		Avatar_part[] partOrder = { Avatar_part.CAP, Avatar_part.HAIR, Avatar_part.FACE, Avatar_part.NECK, Avatar_part.COAT,
				Avatar_part.AURA, Avatar_part.BELT, Avatar_part.PANTS, Avatar_part.SHOES };
		for(int i=0; i<AVATARNUM; i++)
			itemButtonList[i] = new ItemButton(avatarInfoComposite, character.getAvatarList().get(partOrder[i]), BUTTON_SIZE, BUTTON_SIZE);
		
		
		Point buttonS = itemButtonList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		buttonS.x+=3; buttonS.y+=3;
		for(int i=0; i<AVATARNUM; i++)
		{
			Integer x0 = (i%3)*buttonS.x;
			Integer y0 = (int)(i/3)*buttonS.y;
			SetListener listenerGroup = new SetListener(itemButtonList[i], character, superInfo, avatarInfo, setInfo, x0, y0, parent);
			
			itemButtonList[i].getButton().addListener(SWT.MouseDown, listenerGroup.unequipListener()); 				// add MouseDown Event - unequip
			itemButtonList[i].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());		// add MouseDoubleClick - modify
			itemButtonList[i].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(superInfo.getComposite().getParent()));		// add MouseEnter Event - make composite
			itemButtonList[i].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
			itemButtonList[i].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
		}
		
		GridData buttonGridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		for(int i=0; i<AVATARNUM; i++){
			itemButtonList[i].getButton().setData(buttonGridData);
		}
	}
	
	public void renew()
	{
		Avatar_part[] partOrder = { Avatar_part.CAP, Avatar_part.HAIR, Avatar_part.FACE, Avatar_part.NECK, Avatar_part.COAT,
				Avatar_part.AURA, Avatar_part.BELT, Avatar_part.PANTS, Avatar_part.SHOES };
		for(int i=0; i<AVATARNUM; i++){
			itemButtonList[i].setItem(character.getAvatarList().get(partOrder[i]));
			itemButtonList[i].renewImage(true);
		}
	}
	
	public Composite getComposite() {return wholeComposite;}
	
	public boolean equiped(Item item)
	{
		for(ItemButton i : itemButtonList)
			if(i.getItem().getName().equals(item.getName())) return true;
		return false;
	}
}


public class UserInfo
{
	UserItemInfo userItemInfo;
	Composite selectModeComposite;
	InfoStatus infoStatus;
	NonInfoStatus nonInfoStatus;
	private Composite userInfoComposite;
	private boolean dungeonMode = false;
	private Characters character;
	
	public UserInfo(Composite parent, Characters character){
		this.character=character;
		userInfoComposite = new Composite(parent, SWT.BORDER);
		userInfoComposite.setLayout(new FormLayout());
		
		int interval = InterfaceSize.USER_INFO_INTERVAL;

		userItemInfo = new UserItemInfo(userInfoComposite, character, this);
		userItemInfo.getComposite().setLayoutData(new FormData(InterfaceSize.USER_INFO_ITEM_SIZE_X, InterfaceSize.USER_INFO_ITEM_SIZE_Y));
		/*userItemInfo.getComposite().addListener(SWT.MouseDown, new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
	         }
	     });*/
		
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
		
		infoStatus = new InfoStatus(userInfoComposite, character, dungeonMode);
		FormData infoStatusData = new FormData(InterfaceSize.USER_INFO_STAT_SIZE_X, InterfaceSize.USER_INFO_STAT_SIZE_Y);
		infoStatusData.top = new FormAttachment(selectModeComposite, interval);
		infoStatus.getComposite().setLayoutData(infoStatusData);
		
		nonInfoStatus = new NonInfoStatus(userInfoComposite, character, dungeonMode);
		FormData nonInfoStatusData = new FormData(InterfaceSize.USER_INFO_NONSTAT_SIZE_X, InterfaceSize.USER_INFO_NONSTAT_SIZE_Y);
		nonInfoStatusData.left = new FormAttachment(userItemInfo.getComposite(), interval);
		nonInfoStatus.getComposite().setLayoutData(nonInfoStatusData);
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