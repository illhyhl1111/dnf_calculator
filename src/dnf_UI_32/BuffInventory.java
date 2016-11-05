package dnf_UI_32;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.Characters;
import dnf_class.IconObject;
import dnf_class.SelectionBuff;
import dnf_class.Skill;

public class BuffInventory extends DnFComposite{
	LinkedList<Buff> itemSet;
	LinkedList<Skill> passiveSet;
	LinkedList<Buff> buffSet;
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
	
	private <T extends IconObject> LinkedList<T> removeDuplicate(LinkedList<T> list)
	{
		HashSet<T> hashSet = new HashSet<T>();
		Iterator<T> iter = list.iterator();
		while(iter.hasNext()){
			boolean result = hashSet.add(iter.next());
			if(!result) iter.remove();
		}
		
		return list;
	}
	
	private void setInventoryBlocks(Composite background)
	{	
		itemSet = character.userItemList.getAllBuffList();
		passiveSet = removeDuplicate(character.getBuffSkillList());
		buffSet = removeDuplicate(trainingRoom.getBuffList());
		LinkedList<Collection<? extends IconObject>> allList = new LinkedList<Collection<? extends IconObject>>();
		allList.add(itemSet);
		allList.add(passiveSet);
		allList.add(buffSet);
		
		for(Control control : mainComposite.getChildren())
			control.dispose();
		
		int row=0;
		int index=0;
		for(Collection<? extends IconObject> set : allList)
		{
			for(IconObject i : set)
			{
				inventoryList[index] =
						new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
				
				if(i instanceof Skill) inventoryList[index].setOnOffImage(!((Skill)i).buffEnabled(true));
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
				if(i instanceof Buff){
					inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));	// add MouseEnter Event - make composite
					if(i instanceof SelectionBuff)
						inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, new Listener(){
							@Override
							public void handleEvent(Event event) {
								if(event.button==1){
									SelectionBuffModifier dialog = new SelectionBuffModifier(parent.getShell(), (SelectionBuff)i, superInfo);
									dialog.open();
								}
							}
						});
					else if(i.getName().contains("수련의 방 버프"))
						inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(null));
				}
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
				inventoryList[index] = new ItemButton<IconObject>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
				
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
		passiveSet = removeDuplicate(character.getBuffSkillList());
		buffSet = removeDuplicate(trainingRoom.getBuffList());
		LinkedList<Collection<? extends IconObject>> allList = new LinkedList<Collection<? extends IconObject>>();
		allList.add(itemSet);
		allList.add(passiveSet);
		allList.add(buffSet);
		
		character.buffList = new LinkedList<Buff>();
		character.buffList.addAll(itemSet);
		character.buffList.addAll(buffSet);
	
		int row=0;
		int index=0;
		for(Collection<? extends IconObject> set : allList)
		{
			for(IconObject i : set){
				inventoryList[index].setItem(i);
				inventoryList[index].renewImage();
				index++;
			}
		
			for(; index/inventoryCol==row; index++){
				if(row==1) inventoryList[index].setItem(new Skill());
				else inventoryList[index].setItem(new Buff());
				inventoryList[index].renewImage();
			}
			row++;
		}
	}
}

class SelectionBuffModifier extends Dialog
{
	private SelectionBuff item;
	private Label infoLabel;
	private Label[] label;
	private Button[] button;
	private DnFComposite superInfo;
	
	public SelectionBuffModifier(Shell parent, SelectionBuff item, DnFComposite superInfo)
	{
		super(parent);
		this.item=item;
		this.superInfo=superInfo;
		label = new Label[item.selectionList.size()];
		button = new Button[item.selectionList.size()];
	}
	
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		GridLayout contentLayout = new GridLayout(2, false);
		content.setLayout(contentLayout);
		
		infoLabel = new Label(content, SWT.NONE);
		infoLabel.setText("- 도핑/오라 아이템 사용 설정\n\n");
		infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		
		int i=0;
		for(Entry<String, Entry<StatusList, Boolean>> entry : item.selectionList.entrySet()) {
			label[i] = new Label(content, SWT.NONE);
			label[i].setText(entry.getKey());
			label[i].setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
			button[i] = new Button(content, SWT.CHECK);
			button[i].setText("사용 설정");
			button[i].setSelection(entry.getValue().getValue());
			button[i].setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
			i++;
		}
		
		if(item.selectOneOption){
			Listener radioGroup = event -> {
				Control [] children = content.getChildren ();
				for (int j=0; j<children.length; j++) {
					Control child = children [j];
					if (child instanceof Button) {
						Button button1 = (Button) child;
						button1.setSelection (false);
					}
				}
				Button button2 = (Button) event.widget;
				button2.setSelection (true);
			};
			
			for(Button b : button) b.addListener(SWT.Selection, radioGroup);
		}
		
		return content;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("설정");
	}
	
	@Override
    protected void okPressed()
	{
		boolean[] boolList = new boolean[label.length];
		String[] nameList = new String[label.length];
		for(int i=0; i<label.length; i++){
			boolList[i] = button[i].getSelection();
			nameList[i] = label[i].getText();
		}
		
		for(int i=0; i<label.length; i++)
			item.setSelection(nameList, boolList);
		
		superInfo.renew();
		super.close();
	}
}
