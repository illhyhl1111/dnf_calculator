package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Buff;
import dnf_class.Characters;
import dnf_class.IconObject;
import dnf_class.Item;
import dnf_class.Skill;

public class BuffInventory extends DnFComposite{
	LinkedList<Buff> itemList;
	LinkedList<Skill> passiveList;
	LinkedList<Skill> buffList;
	ItemButton<IconObject>[] inventoryList;
	final static int inventoryCol=17;
	final static int inventoryRow=3;
	final static int inventorySize=inventoryCol*inventoryRow;
	
	private Characters character;
	private DnFComposite superInfo;
	private Composite parent;
	private final TrainingRoom trainingRoom;

	Composite background;
	
	@SuppressWarnings("unchecked")
	public BuffInventory(Composite parent, Characters character, TrainingRoom trainingRoom, DnFComposite superInfo)
	{
		this.character=character;
		this.superInfo=superInfo;
		this.parent=parent;
		this.trainingRoom=trainingRoom;
		
		mainComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		mainComposite.setLayout(inventoryLayout);
		mainComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		inventoryList = (ItemButton<IconObject>[]) new ItemButton<?>[inventorySize];
	}
	
	public void setListener(Composite background)
	{
		this.background=background;
		setInventoryBlocks(background);
	}
	
	private void setInventoryBlocks(Composite background)
	{	
		itemList = character.userItemList.getAllBuffList();
		passiveList = character.getBuffSkillList();
		buffList = trainingRoom.getBuffList();
		LinkedList<LinkedList<? extends IconObject>> allList = new LinkedList<LinkedList<? extends IconObject>>();
		allList.add(itemList);
		allList.add(passiveList);
		allList.add(buffList);
		
		for(Control control : mainComposite.getChildren())
			control.dispose();
		
		int row=0;
		int index=0;
		for(LinkedList<? extends IconObject> list : allList)
		{
			for(IconObject i : list)
			{
				inventoryList[index] =
						new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
				if(i instanceof Item)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));	// add MouseEnter Event - make composite
				else if(i instanceof Skill)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeSkillInfoListener(background));	// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 			// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());				// add MouseMove Event - move composite
				
				index++;
			}
		
			for(; index/inventoryCol==row; index++){
				IconObject i;
				if(row==0) i = new Item();
				else i = new Skill();
				inventoryList[index] = new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
				
				if(i instanceof Item)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));	// add MouseEnter Event - make composite
				else if(i instanceof Skill)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeSkillInfoListener(background));	// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 			// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());				// add MouseMove Event - move composite
			}
			
			row++;
		}
	}
		
	public IconObject getBuff(String name) throws ItemNotFoundedException
	{
		for(ItemButton<IconObject> i : inventoryList)
		{
			if(i.getItem().getName().equals(name)) return i.getItem();
		}
		throw new ItemNotFoundedException(name);
	}

	@Override
	public void renew() {
		passiveList = character.getBuffSkillList();
		buffList = trainingRoom.getBuffList();
		LinkedList<LinkedList<? extends IconObject>> allList = new LinkedList<LinkedList<? extends IconObject>>();
		allList.add(passiveList);
		allList.add(buffList);
	
		int row=1;
		int index=inventoryCol;
		for(LinkedList<? extends IconObject> list : allList)
		{
			for(IconObject i : list){
				inventoryList[index].setItem(i);
				inventoryList[index].renewImage(true);
				index++;
			}
		
			for(; index/inventoryCol==row; index++){
				inventoryList[index].setItem(new Skill());
				inventoryList[index].renewImage(true);;
			}
			row++;
		}
	}
}
