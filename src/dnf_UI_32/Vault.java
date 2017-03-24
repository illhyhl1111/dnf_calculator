package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.DnFColor;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Location;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Characters;
import dnf_class.Item;
import dnf_class.ItemConstraint;

public class Vault extends Dialog 
{
	private Composite itemInfo;
	private Integer X0;
	private Integer Y0;
	private Inventory inventory;
	static final int mouseInterval_hor = 7;
	static final int mouseInterval_ver = 5;
	private Characters character;
	
	private Shell parent;
	Shell save;
	
	private TabFolder tabFolder;
	private VaultInventory[] inventoryList;
	private Composite[] pack;
	private TabItem[] inventoryTabList;
	
	@SuppressWarnings("unchecked")
	public Vault(Shell parent, Characters character)
	{
		super(parent);
		this.parent=parent;
		this.character=character;
		
		save = new Shell(Display.getCurrent());
		
		String[] tabNameList = {"무기1", "무기2", "방어구 1", "방어구 2", "방어구 3", "퀘전방어구", "악세서리1", "악세서리 2", "퀘전악세", "특수장비1", "특수장비2", "칭호", "기타", "아바타", "크리쳐/휘장"};
		final int inventoryListNum = tabNameList.length;
		
		tabFolder = new TabFolder(save, SWT.NONE);
		inventoryList = new VaultInventory[inventoryListNum];
		inventoryTabList = new TabItem[inventoryListNum];
		pack = new Composite[inventoryListNum];
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1-2, character);
		LinkedList<Item>[] equipList = character.userItemList.separateList(constraintList);
		
		LinkedList<Item>[] itemList = (LinkedList<Item>[]) new LinkedList<?>[equipList.length+2];
		for(int i=0; i<equipList.length; i++)
			itemList[i]=equipList[i];
		itemList[equipList.length] = character.userItemList.getSortedList(character.userItemList.avatarList);
		itemList[equipList.length+1] = character.userItemList.getSortedList(character.userItemList.creatureList);
		itemList[equipList.length+1].addAll((LinkedList<? extends Item>) character.userItemList.getSortedList(character.userItemList.drapeList)); 
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(tabFolder, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem((TabFolder) tabFolder, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);

			inventoryList[i] = new VaultInventory(pack[i], character, (LinkedList<Item>) itemList[i]);
			for(ItemButton<Item> item : inventoryList[i].inventoryList){

				// add MouseExit Event - dispose composite
				item.getButton().addListener(SWT.MouseExit, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(itemInfo!=null && !itemInfo.isDisposed()){
		        			 itemInfo.dispose();
		        		 }
			         }
			     });
				
				// add MouseMove Event - move composite
				item.getButton().addListener(SWT.MouseMove, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(itemInfo!=null && !itemInfo.isDisposed()){
			        		 itemInfo.setLocation((e.x+X0), (e.y+Y0));
		        		 }
			         }
			     });
			}
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	
	public static ItemConstraint[] setConstraintList(int length, Characters character)
	{
		ItemConstraint[] constraintList = new ItemConstraint[length];
		
		int num;
		//75~85 에픽무기
		num=0;
		constraintList[num] = new ItemConstraint(75, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.WEAPON);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//85~90 레전무기
		num++;
		constraintList[num] = new ItemConstraint(85, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.WEAPON);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~85 에픽방어구
		num++;
		constraintList[num] = new ItemConstraint(80, 85, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//90 에픽방어구
		num++;
		constraintList[num] = new ItemConstraint(90, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//85~90 레전방어구(단일)
		num++;
		constraintList[num] = new ItemConstraint(85, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].typeList.add(Equip_type.FABRIC);
		constraintList[num].typeList.add(Equip_type.LEATHER);
		constraintList[num].typeList.add(Equip_type.MAIL);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].typeList.add(Equip_type.HEAVY);
		constraintList[num].typeList.add(Equip_type.PLATE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].setList.add(SetName.NONE);
		
		//85~90 레전방어구(세트)
		num++;
		constraintList[num] = new ItemConstraint(85, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].typeList.add(Equip_type.FABRIC);
		constraintList[num].typeList.add(Equip_type.LEATHER);
		constraintList[num].typeList.add(Equip_type.MAIL);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].typeList.add(Equip_type.HEAVY);
		constraintList[num].typeList.add(Equip_type.PLATE);
		constraintList[num].setList.add(SetName.BURIEDSCREAM);
		constraintList[num].setList.add(SetName.CURSEOFSEAGOD);
		constraintList[num].setList.add(SetName.DEVASTEDGRIEF);
		constraintList[num].setList.add(SetName.GODOFFIGHT);
		constraintList[num].setList.add(SetName.GRACIA);
		constraintList[num].setList.add(SetName.GREATGLORY);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_FIRE);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_WATER);
		constraintList[num].setList.add(SetName.HUGEFORM);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_ACCESSORY);
		constraintList[num].setList.add(SetName.ROMANTICE);
		constraintList[num].setList.add(SetName.ROOTOFDISEASE);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_FABRIC);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_HARMOR);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_LEATHER);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_MAIL);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_PLATE);
		constraintList[num].setList.add(SetName.EKERN);
		
		//80~90 에픽 악세(단일)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//80~90 레전 악세(단일)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].setList.add(SetName.NONE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~90 레전 악세(세트)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].setList.add(SetName.BURIEDSCREAM);
		constraintList[num].setList.add(SetName.CURSEOFSEAGOD);
		constraintList[num].setList.add(SetName.DEVASTEDGRIEF);
		constraintList[num].setList.add(SetName.GODOFFIGHT);
		constraintList[num].setList.add(SetName.GRACIA);
		constraintList[num].setList.add(SetName.GREATGLORY);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_FIRE);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_WATER);
		constraintList[num].setList.add(SetName.HUGEFORM);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_ACCESSORY);
		constraintList[num].setList.add(SetName.ROMANTICE);
		constraintList[num].setList.add(SetName.ROOTOFDISEASE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~90 특수장비
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].rarityList.add(Item_rarity.UNIQUE);
		constraintList[num].rarityList.add(Item_rarity.RARE);
		
		//칭호
		num++;
		constraintList[num] = new ItemConstraint(0, 0, character.getJob());
		constraintList[num].partList.add(Equip_part.TITLE);
		constraintList[num].rarityList.add(Item_rarity.RARE);
		
		return constraintList;
	}
	
	private void setMousePoint(Event e, Composite background, Point itemInfoSize)
	{
		Point mousePoint = ((Control) e.widget).toDisplay(0, 0);
    	mousePoint.x-=background.toDisplay(0, 0).x;
    	mousePoint.y-=background.toDisplay(0, 0).y;
   		
    	Y0=mousePoint.y-itemInfoSize.y-mouseInterval_hor;
		X0 = mousePoint.x-InterfaceSize.ITEM_INFO_SIZE-mouseInterval_ver;
		if(X0<0) X0 = mousePoint.x+mouseInterval_ver;
		if(Y0<0) Y0 = mousePoint.y+mouseInterval_hor;
	}
	
	public void setInventory(Inventory inventory)
	{
		this.inventory=inventory;
	}
	
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);
		tabFolder.setParent(composite);
		composite.getShell().setBackground(DnFColor.infoBackground);
		
		for(int i=0; i<inventoryList.length; i++)
		{
			for(ItemButton<Item> item : inventoryList[i].inventoryList){
				item.getButton().addListener(SWT.MouseDown, new Listener() {
					@Override
			        public void handleEvent(Event e) {
						if(e.button==3){
							int index = inventory.getIndex(item.getItem());
							if(index==-1) inventory.addItem(item.getItem());
						}
			        }
			    });
				
				// add MouseEnter Event - make composite
				item.getButton().addListener(SWT.MouseEnter, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 Point itemInfoSize=null;

			        	 itemInfo = new Composite(composite.getShell(), SWT.BORDER);
			        	 GridLayout layout = new GridLayout(1, false);
			        	 layout.verticalSpacing=3;
			        	 itemInfo.setLayout(layout);
			        	 MakeComposite.setItemInfoComposite(itemInfo, item.getItem(), Location.VILLAGE, character);
			        	 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			        	 itemInfo.moveAbove(null);
			        		 
			        	 setMousePoint(e, composite.getShell(), itemInfoSize);
			        	 itemInfo.setBounds((e.x+X0), (e.y+Y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
			        }
			    });
			}
		}
		return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("금고금고금고");
	}

	/*@Override
	protected Point getInitialSize() {
	    return new Point(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x+60, InterfaceSize.VAULT_SIZE_Y);
	}*/
	
	@Override
	protected void initializeBounds() 
	{ 
		super.initializeBounds(); 
		Shell shell = this.getShell(); 
		Rectangle bounds = parent.getBounds(); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + 40; 
		shell.setLocation (x, y); 
	} 
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent)
	{ 
	  GridLayout layout = (GridLayout)parent.getLayout();
	  layout.marginHeight = 0;
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS| SWT.BORDER | SWT.TITLE | SWT.SHELL_TRIM);
	    setBlockOnOpen(false);
	}
	
	@Override
	protected boolean isResizable() {
	    return true;
	}
	
	@Override
	public boolean close()
	{
		tabFolder.setParent(save);
		for(int i=0; i<inventoryList.length; i++)
		{
			for(ItemButton<Item> item : inventoryList[i].inventoryList){
				Listener[] list = item.getButton().getListeners(SWT.MouseEnter);
				if(list.length!=0)
					item.getButton().removeListener(SWT.MouseEnter, list[0]);
				list = item.getButton().getListeners(SWT.MouseDown);
				if(list.length!=0)
					item.getButton().removeListener(SWT.MouseDown, list[0]);
			}
		}
		return super.close();
	}
}

class VaultInventory extends DnFComposite
{
	LinkedList<Item> itemList;
	ItemButton<Item>[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=5;
	final int inventorySize;
	Characters character;
	Composite parent;
	Composite inventory;

	@SuppressWarnings("unchecked")
	public VaultInventory(Composite parent, Characters character, LinkedList<Item> itemList)
	{
		this.itemList=itemList;
		this.character=character;
		this.parent=parent;

		inventorySize = inventoryCol*inventoryRow<itemList.size() ? inventoryCol*inventoryRow : itemList.size();
		
		mainComposite = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns=2;
		mainLayout.horizontalSpacing=20;
		mainLayout.marginWidth=0;
		mainLayout.marginHeight=0;
		mainComposite.setLayout(mainLayout);
		
		inventory = new Composite(mainComposite, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventory.setLayout(inventoryLayout);
		GridData data = new GridData(SWT.LEFT, SWT.TOP, false, false);
		data.heightHint = (InterfaceSize.INFO_BUTTON_SIZE+inventoryLayout.verticalSpacing ) *5;
		inventory.setLayoutData(data);
		
		inventory.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		
		inventoryList = (ItemButton<Item>[]) new ItemButton<?>[inventorySize];
		int index=0;
		for(Item i : itemList)
			inventoryList[index++] = new ItemButton<Item>(inventory, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
	}
		
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : inventoryList)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}
	@Override
	public void renew() {	
	}
}