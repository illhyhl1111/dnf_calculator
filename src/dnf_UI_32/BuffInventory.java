package dnf_UI_32;

import java.util.HashSet;
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
import dnf_class.Skill;

public class BuffInventory extends DnFComposite{
	HashSet<Buff> itemSet;
	HashSet<Skill> passiveSet;
	HashSet<Buff> buffSet;
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
	
	private <T extends IconObject> HashSet<T> getBuffListToSet(LinkedList<T> list)
	{
		HashSet<T> set = new HashSet<T>();
		for(T item : list)
			set.add(item);
		
		return set;
	}
	
	private void setInventoryBlocks(Composite background)
	{	
		itemSet = getBuffListToSet(character.userItemList.getAllBuffList());
		passiveSet = getBuffListToSet(character.getBuffSkillList());
		buffSet = getBuffListToSet(trainingRoom.getBuffList());
		LinkedList<HashSet<? extends IconObject>> allList = new LinkedList<HashSet<? extends IconObject>>();
		allList.add(itemSet);
		allList.add(passiveSet);
		allList.add(buffSet);
		
		for(Control control : mainComposite.getChildren())
			control.dispose();
		
		int row=0;
		int index=0;
		for(HashSet<? extends IconObject> set : allList)
		{
			for(IconObject i : set)
			{
				inventoryList[index] =
						new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
				
				if(i instanceof Skill) inventoryList[index].setOnOffImage(!((Skill)i).buffEnabled(true));
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
				if(i instanceof Buff)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));	// add MouseEnter Event - make composite
				else if(i instanceof Skill)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeSkillInfoListener(background));	// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 			// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());				// add MouseMove Event - move composite
				inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.buffInventoryClickListener());		// add MouseMove Event - move composite
				
				index++;
			}
		
			for(; index/inventoryCol==row; index++){
				IconObject i;
				if(row==1) i = new Skill();
				else i = new Buff();
				inventoryList[index] = new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
				
				if(i instanceof Buff)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));	// add MouseEnter Event - make composite
				else if(i instanceof Skill)
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeSkillInfoListener(background));	// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 			// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());				// add MouseMove Event - move composite
				inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.buffInventoryClickListener());		// add MouseMove Event - move composite
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
		passiveSet = getBuffListToSet(character.getBuffSkillList());
		buffSet = getBuffListToSet(trainingRoom.getBuffList());
		LinkedList<HashSet<? extends IconObject>> allList = new LinkedList<HashSet<? extends IconObject>>();
		allList.add(passiveSet);
		allList.add(buffSet);
	
		int row=1;
		int index=inventoryCol;
		for(HashSet<? extends IconObject> set : allList)
		{
			for(IconObject i : set){
				inventoryList[index].setItem(i);
				inventoryList[index].renewImage(true);
				index++;
			}
		
			for(; index/inventoryCol==row; index++){
				if(row==1) inventoryList[index].setItem(new Skill());
				else inventoryList[index].setItem(new Buff());
				inventoryList[index].renewImage(true);
			}
			row++;
		}
	}
}
