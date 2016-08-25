package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ImageSize;
import dnf_class.Characters;

class UserItemInfo {
	private Composite itemInfoComposite;
	private Composite leftItemInfoComposite;
	private Composite rightItemInfoComposite;
	private ItemButton[] itemButtonList;
	private static int ITEMNUM=12;
	private Characters character;
	
	public UserItemInfo(Composite parent, Characters character, UserInfo superInfo)
	{
		this.character=character;
		itemInfoComposite = new Composite(parent, SWT.BORDER);
		RowLayout wholeLayout = new RowLayout();
		wholeLayout.spacing=10;
		wholeLayout.wrap=false;
		wholeLayout.pack=true;
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

		int BUTTON_SIZE = ImageSize.INFO_BUTTON_SIZE;
		itemButtonList[0] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOULDER), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[1] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.ROBE), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[2] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.TROUSER), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[3] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.BELT), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[4] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.SHOES), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[5] = new ItemButton(leftItemInfoComposite, character.getEquipmentList().get(Equip_part.AIDEQUIPMENT), BUTTON_SIZE, BUTTON_SIZE);
		
		itemButtonList[6] = new ItemButton(rightItemInfoComposite, character.getWeapon(), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[7] = new ItemButton(rightItemInfoComposite, character.getTitle(), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[8] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.BRACELET), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[9] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.NECKLACE), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[10] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.MAGICSTONE), BUTTON_SIZE, BUTTON_SIZE);
		itemButtonList[11] = new ItemButton(rightItemInfoComposite, character.getEquipmentList().get(Equip_part.RING), BUTTON_SIZE, BUTTON_SIZE);
		for(int i=0; i<ITEMNUM; i++)
		{
			ItemButton temp = itemButtonList[i];
			itemButtonList[i].getButton().addListener(SWT.MouseDown, new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(e.button==3){
		        		 character.unequip(temp.getItem());
		        		 superInfo.renew();
		        	 }
		        	 //System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
		         }
		     });
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
		itemButtonList[5].setItem(character.getEquipmentList().get(Equip_part.AIDEQUIPMENT));
		itemButtonList[6].setItem(character.getWeapon());
		itemButtonList[7].setItem(character.getTitle());
		itemButtonList[8].setItem(character.getEquipmentList().get(Equip_part.BRACELET));
		itemButtonList[9].setItem(character.getEquipmentList().get(Equip_part.NECKLACE));
		itemButtonList[10].setItem(character.getEquipmentList().get(Equip_part.MAGICSTONE));
		itemButtonList[11].setItem(character.getEquipmentList().get(Equip_part.RING));
		
		for(int i=0; i<ITEMNUM; i++)
		{
			itemButtonList[i].renewImage();
		}
	}
	
	public Composite getComposite() {return itemInfoComposite;}
}


public class UserInfo
{
	UserItemInfo userItemInfo;
	InfoStatus infoStatus;
	NonInfoStatus nonInfoStatus;
	private Composite userInfoComposite;
	
	public UserInfo(Composite parent, Characters character){
		userInfoComposite = new Composite(parent, SWT.BORDER);
		//userInfoComposite.setLayout(new RowLayout());

		userItemInfo = new UserItemInfo(userInfoComposite, character, this);
		userItemInfo.getComposite().setBounds(0, 0, 380, 160);
		
		infoStatus = new InfoStatus(userInfoComposite, character, true);
		infoStatus.getComposite().setBounds(0, 170, 380, 220);
		
		
		nonInfoStatus = new NonInfoStatus(userInfoComposite, character, true);
		nonInfoStatus.getComposite().setBounds(390, 0, 400, 160+10+220);
		nonInfoStatus.getComposite().addListener(SWT.MouseDown, new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
	         }
	     });
	}
	
	public void renew()
	{
		userItemInfo.renew();
		infoStatus.renew();
		nonInfoStatus.renew();
	}
	
	public Composite getComposite()
	{
		return userInfoComposite;
	}
}