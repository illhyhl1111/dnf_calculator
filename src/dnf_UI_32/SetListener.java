package dnf_UI_32;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.Location;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Buff;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.IconObject;
import dnf_class.Item;
import dnf_class.Monster;
import dnf_class.MonsterOption;
import dnf_class.Skill;
import dnf_class.SwitchingSkill;

@SuppressWarnings("unchecked")
public class SetListener {
	ItemButton<? extends IconObject> itemButton_wildCard;
	Characters character;
	DnFComposite superInfo;
	Composite itemInfo;
	Boolean hasSetOption;
	Composite setInfo;
	Point mousePoint;
	Composite parent;
	int x0;
	int y0;
	static final int mouseInterval_hor = 7;
	static final int mouseInterval_ver = 5;
	
	public SetListener(ItemButton<? extends IconObject> itemButton, Characters character, DnFComposite superInfo, Composite parent)
	{
		this.itemButton_wildCard=itemButton;
		this.character=character;
		this.superInfo=superInfo;
		this.itemInfo=null;
		this.setInfo=null;
		this.parent=parent;
		
		hasSetOption=false;
	}
	
	private void setMousePoint(Event e, Composite background, Point itemInfoSize, Point setInfoSize)
	{
		Point mousePoint = ((Control) e.widget).toDisplay(0, 0);
    	mousePoint.x-=background.toDisplay(0, 0).x;
    	mousePoint.y-=background.toDisplay(0, 0).y;
   		
   		if(setInfoSize!=null) y0 = mousePoint.y-Math.max(setInfoSize.y, itemInfoSize.y)-mouseInterval_hor;
		else y0=mousePoint.y-itemInfoSize.y-mouseInterval_hor;
		if(hasSetOption)
			x0 = mousePoint.x-InterfaceSize.ITEM_INFO_SIZE-InterfaceSize.SET_INFO_SIZE-mouseInterval_ver-InterfaceSize.SET_ITEM_INTERVAL;
		else x0 = mousePoint.x-InterfaceSize.ITEM_INFO_SIZE-mouseInterval_ver;
		if(x0<0) x0 = mousePoint.x+mouseInterval_ver;
		if(y0<0) y0 = mousePoint.y+mouseInterval_hor;
	}
	
	public Listener unequipListener()
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
	        public void handleEvent(Event e) {
	       	 if(e.button==3){
	       		 character.unequip(itemButton.getItem());
	       		 superInfo.renew();
	       		 if(itemInfo!=null && !itemInfo.isDisposed()){
	       			 itemInfo.dispose();
		        		 if(hasSetOption) setInfo.dispose();
		        	 }
	       	 	}
	        }
		};
	}
	
	public Listener equipListener(Vault vault)
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item && superInfo instanceof UserInfo) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 if(e.button==3 && itemButton.getItem().getEnabled()){
	        		 if(vault.getShell()==null){
	        			 character.equip(itemButton.getItem());
	        			 superInfo.renew();
	        		 }
	        		 else{
	        			 itemButton.getItem().setEnabled(false);
	        			 itemButton.renewImage(false);
	        			 if(((UserItemInfo)((UserInfo)superInfo).userItemInfo).equiped(itemButton.getItem())){
	        				 character.unequip(itemButton.getItem());
	        				 superInfo.renew();
	        				 if(!itemInfo.isDisposed()) itemInfo.dispose();
	        			 }
	        			 if(itemInfo!=null && !itemInfo.isDisposed()){
			        		 itemInfo.dispose();
			        		 if(hasSetOption) setInfo.dispose();
			        	 }
	        		 }
	        	 }
	         }
		};
	}

	public Listener equipListener()
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
	        public void handleEvent(Event e) {
				if(e.button==3){
	        		character.equip(itemButton.getItem());
	        		superInfo.renew();
	        	}
	        }
		};
	}
	
	public Listener modifyListener(Inventory inventory)
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.button== 1 && itemButton.getItem().getEnabled())
	        	 {
					boolean replicateEnabled=false;
					if(inventory!=null) replicateEnabled=true;
					
	        		ChangeItemStatus changeItem = new ChangeItemStatus(parent.getShell(), itemButton.getItem(), itemButton.hasSetOption(), replicateEnabled);
	        		int result = changeItem.open();
					if (Window.OK == result) {
						itemButton.setItem(changeItem.item);
						superInfo.renew();
					}
					else if(result == 2)
					{
						SetName setName = itemButton.getItem().getSetName();
						ChangeSetOptionStatus changeSet = new ChangeSetOptionStatus(parent.getShell(), setName, character.userItemList);
						if (Window.OK == changeSet.open()) {
							character.userItemList.setSetOptions(setName, changeSet.setOption);
							itemButton.setItem(changeItem.item);
							superInfo.renew();			
						}
					}
					else if(result == 3 && inventory!=null)
					{
						Item replicate = character.userItemList.makeReplicate(itemButton.getItem(), inventory.inventoryList2.length-inventory.itemList2.size());
						if(replicate!=null)
						{
							inventory.itemList2.add(replicate);
							inventory.renew();
							MessageDialog dialog = new MessageDialog(parent.getShell(), "성☆공", null,
								    "아이템 복제에 성공하였습니다!\n\n아이템 : "+itemButton.getItem().getName(),
								    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
							dialog.open();
						}
						else
						{
							MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
									"아이템 복제에 실패하였습니다.\n\n아이템 : "+itemButton.getItem().getName(),
								    MessageDialog.ERROR, new String[] { "납득" }, 0);
							dialog.open();
						}
					}
					else if(result == 4 && inventory!=null)
					{
						boolean success = character.userItemList.deleteReplicate(itemButton.getItem());
						if(success)
						{
							if(character.unequip(itemButton.getItem()))
								superInfo.renew();;
							inventory.itemList2.remove(itemButton.getItem());
							inventory.renew();
							MessageDialog dialog = new MessageDialog(parent.getShell(), "성☆공", null,
								    "아이템을 삭제하였습니다!\n\n아이템 : "+itemButton.getItem().getName(),
								    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
							dialog.open();
						}
						else
						{
							MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
									"아이템 삭제에 실패하였습니다.\n\n아이템 : "+itemButton.getItem().getName(),
								    MessageDialog.ERROR, new String[] { "납득" }, 0);
							dialog.open();
						}
					}
					
	        	 }
			}
		}; 
	}
	
	public Listener makeItemInfoListener(Composite background, Location location)
	{	     
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
	     return new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 if(itemButton.getItem().getName().contains("없음")) return;
	        	 
	        	Point setInfoSize=null;
	        	Point itemInfoSize=null;

	        	if(itemButton.getItem().getEnabled()){
	        		itemInfo = new Composite(background, SWT.BORDER);
	        		GridLayout layout = new GridLayout(1, false);
	        		layout.verticalSpacing=3;
	        		itemInfo.setLayout(layout);
	        		MakeComposite.setItemInfoComposite(itemInfo, itemButton.getItem(), location, character);
	        		itemInfoSize = itemInfo.computeSize(InterfaceSize.ITEM_INFO_SIZE, SWT.DEFAULT);
	        		itemInfo.moveAbove(null);
	        		
	        		hasSetOption = itemButton.hasSetOption();
	        		if(hasSetOption){
	        			setInfo = new Composite(background, SWT.BORDER);
	        			setInfo.setLayout(layout);
	        			int setNum;
	        			if(character.getSetOptionList().get( itemButton.getItem().getSetName() )==null) setNum=0;
	        			else setNum=character.getSetOptionList().get( itemButton.getItem().getSetName() );
	        			
	        			MakeComposite.setSetInfoComposite(setInfo, itemButton.getItem(), setNum, character.userItemList);
		        		setInfoSize = setInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		        		setInfo.moveAbove(null);
	        		}
	        		 
	        		setMousePoint(e, background, itemInfoSize, setInfoSize);
	        		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	        		if(hasSetOption) setInfo.setBounds((e.x+x0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+y0), InterfaceSize.SET_INFO_SIZE, setInfoSize.y);
	        	}
	        }
	    };
	}
	public Listener makeItemInfoListener(Composite background)
	{
		return makeItemInfoListener(background, Location.VILLAGE);
	}
	
	public Listener makeCardInfoListener(Composite background)
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
	        @Override
	        public void handleEvent(Event e) {
	        	Card card= itemButton.getItem().getCard();
	       		
	       		if( card.getName().contains("없음")) return;
	       		itemInfo = new Composite(background, SWT.BORDER);
	       		GridLayout layout = new GridLayout(1, false);
	       		layout.verticalSpacing=3;
	       		itemInfo.setLayout(layout);
	       		MakeComposite.setItemInfoComposite(itemInfo, card, Location.VILLAGE, character);
	       		Point itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	       		setMousePoint(e, background, itemInfoSize, null);
	       		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	       		itemInfo.moveAbove(null);
	        }
	    };
	}
	
	public Listener makeSkillInfoListener(Composite background)
	{
		ItemButton<Skill> skillButton;
		if(itemButton_wildCard.getItem() instanceof Skill) skillButton = (ItemButton<Skill>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
	        @Override
	        public void handleEvent(Event e) {
	        	if(skillButton.getItem().getName().contains("없음")) return;
	       		itemInfo = new Composite(background, SWT.BORDER);
	       		GridLayout layout = new GridLayout(1, false);
	       		layout.verticalSpacing=3;
	       		itemInfo.setLayout(layout);
	       		MakeComposite.setSkillInfoComposite(itemInfo, skillButton.getItem(), character.dungeonStatus, character.isBurning());
	       		Point itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	       		setMousePoint(e, background, itemInfoSize, null);
	       		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	       		itemInfo.moveAbove(null);
	        }
	    };
	}
	
	public Listener makeMonsterInfoListener(Composite background)
	{
		ItemButton<Monster> monsterButton;
		if(itemButton_wildCard.getItem() instanceof Monster) monsterButton = (ItemButton<Monster>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				itemInfo = new Composite(background, SWT.BORDER);
        		GridLayout layout = new GridLayout(1, false);
        		layout.verticalSpacing=3;
        		itemInfo.setLayout(layout);
        		MakeComposite.setMonsterInfoComposite(itemInfo, monsterButton.getItem());
        		Point itemInfoSize = itemInfo.computeSize(InterfaceSize.MONSTER_INFO_SIZE, SWT.DEFAULT);
        		itemInfo.moveAbove(null);
        		 
        		setMousePoint(e, background, itemInfoSize, null);
        		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.MONSTER_INFO_SIZE, itemInfoSize.y);
			}
		};
	}
	
	public Listener makeMonsterOptionInfoListener(Composite background)
	{
		ItemButton<MonsterOption> optionButton;
		if(itemButton_wildCard.getItem() instanceof MonsterOption) optionButton = (ItemButton<MonsterOption>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				itemInfo = new Composite(background, SWT.BORDER);
        		GridLayout layout = new GridLayout(1, false);
        		layout.verticalSpacing=3;
        		itemInfo.setLayout(layout);
        		MakeComposite.setMonsterOptionInfoComposite(itemInfo, optionButton.getItem());
        		Point itemInfoSize = itemInfo.computeSize(InterfaceSize.MONSTER_INFO_SIZE, SWT.DEFAULT);
        		itemInfo.moveAbove(null);
        		 
        		setMousePoint(e, background, itemInfoSize, null);
        		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.MONSTER_INFO_SIZE, itemInfoSize.y);
			}
		};
	}
	
	public Listener skillLevelModifyListener(Composite background, boolean TPMode)
	{
		ItemButton<Skill> skillButton;
		if(itemButton_wildCard.getItem() instanceof Skill) skillButton = (ItemButton<Skill>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				if(e.button==1){
					if(TPMode) skillButton.getItem().increaseLevel_char();
					else skillButton.getItem().masterSkill(character.getLevel(), character.hasContract());
				}
				else if(e.button==3)
					if(TPMode) skillButton.getItem().decreaseLevel_char();
					else skillButton.getItem().setSkillLevel(0);
				
				//dispose
				if(itemInfo !=null && !itemInfo.isDisposed())
	        		 itemInfo.dispose();
				
				//same with makeSkillInfoListener
				itemInfo = new Composite(background, SWT.BORDER);
	       		GridLayout layout = new GridLayout(1, false);
	       		layout.verticalSpacing=3;
	       		itemInfo.setLayout(layout);
	       		MakeComposite.setSkillInfoComposite(itemInfo, skillButton.getItem(), character.dungeonStatus, character.isBurning());
	       		Point itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	       		setMousePoint(e, background, itemInfoSize, null);
	       		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	       		itemInfo.moveAbove(null);
	       		
	       		skillButton.renewImage(true);
			}
		};
	}
	
	public Listener disposeItemInfoListener()
	{
		return new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 if(itemInfo !=null && !itemInfo.isDisposed()){
	        		 itemInfo.dispose();
	        		 if(hasSetOption && setInfo!=null) setInfo.dispose();
	        	 }
	         }
	     };
	}
	
	public Listener moveItemInfoListener()
	{
		return new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 if(itemInfo !=null && !itemInfo.isDisposed()){
	        		 itemInfo.setLocation((e.x+x0), (e.y+y0));
	        		 if(hasSetOption) setInfo.setLocation((e.x+x0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+y0));
	        	 }
	         }
	     };
	}
	
	public Listener unequipCardListener()
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
	         @Override
	         public void handleEvent(Event e) {
	        	 if(e.button==3){
	        		 itemButton.getItem().setCard(new Card());
	        		 if(itemInfo!=null && !itemInfo.isDisposed()){
		        		 itemInfo.dispose();
		        	 }
	        	 }
	         }
		};
	}
	
	public Listener buffInventoryClickListener()
	{
		ItemButton<Buff> itemButton;
		ItemButton<Skill> skillButton;
		if(itemButton_wildCard.getItem() instanceof Buff){
			itemButton = (ItemButton<Buff>) itemButton_wildCard;
			
			return new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(e.button==3){
		        		 if(itemButton.getItem().enabled){
		        			 itemButton.getItem().enabled=false;
		        			 character.buffList.remove(itemButton.getItem());
		        		 }
		        		 else{
		        			 itemButton.getItem().enabled=true;
		        			 character.buffList.add(itemButton.getItem());
		        		 }
		        		 superInfo.renew();
		        	 }
		         }
			};
		}
		else if(itemButton_wildCard.getItem() instanceof Skill){
			skillButton = (ItemButton<Skill>) itemButton_wildCard;
			
			return new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(e.button==3){
		        		 skillButton.getItem().setBuffEnabled(!skillButton.getItem().getBuffEnabled());
		        		 if(skillButton.getItem().getBuffEnabled()) skillButton.setOnOffImage(false);
		        		 else skillButton.setOnOffImage(true);
		        		 superInfo.renew();
		        	 }
		         }
			};
		}
		else return null;
	}
	
	public Listener skillModifyListener(String[] statList){
		ItemButton<SwitchingSkill> skillButton;
		if(itemButton_wildCard.getItem() instanceof Skill) skillButton = (ItemButton<SwitchingSkill>) itemButton_wildCard;
		else return null;
		
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				ChangeSkillDialog modifyDialog = new ChangeSkillDialog(parent.getShell(), statList);
				modifyDialog.create();
				
				if (modifyDialog.open() == Window.OK) {
					double[] num = modifyDialog.getValue();
					skillButton.getItem().setSkillPercent(num);
					superInfo.renew();
				}
			}
		};
	}
	
	public void setItemDrag()
	{
		ItemButton<Item> itemButton;
		if(itemButton_wildCard.getItem() instanceof Item) itemButton = (ItemButton<Item>) itemButton_wildCard;
		else return;
		
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(itemButton.getButton(), operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceAdapter() {
			public void dragStart(DragSourceEvent event) {
				event.doit = (itemButton.getButton().getImage() != null);
				event.image = itemButton.getButton().getImage();
				if(itemInfo!=null) itemInfo.dispose();
			}

			public void dragSetData(DragSourceEvent event) {
				event.data = itemButton.getItem().getName();
			}
		});
	}
}


class ChangeSkillDialog extends TitleAreaDialog {

    private Text[] text;
    private double[] value;
    private String[] statName;
    private Label warning;

    public ChangeSkillDialog(Shell parentShell, String[] statName) {
    	super(parentShell);
    	text = new Text[statName.length];
    	value = new double[statName.length];
    	this.statName=statName;
    }

    @Override
    public void create() {
            super.create();
            setTitle("스위칭 스킬 수치 설정");
            setMessage("스위칭 스킬의 수치를 입력받아 설정합니다", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
            Composite area = (Composite) super.createDialogArea(parent);
            Composite container = new Composite(area, SWT.NONE);
            container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            GridLayout layout = new GridLayout(2, false);
            container.setLayout(layout);

            createInput(container);
            warning = new Label(container, SWT.NONE);
            warning.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

            return area;
    }

    private void createInput(Composite container) {
    	for(int i=0; i<statName.length; i++)
        {
    		Label lbtFirstName = new Label(container, SWT.NONE);
            lbtFirstName.setText(statName[i]);

            GridData textData = new GridData();
            textData.grabExcessHorizontalSpace = true;
            textData.horizontalAlignment = GridData.FILL;

        	text[i] = new Text(container, SWT.NONE);
        	text[i].setLayoutData(textData);
        	text[i].addVerifyListener(new TextInputOnlyNumbers());
        }
    }
    
    @Override
    protected boolean isResizable() {
            return true;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private boolean saveInput() {
    	try{
        	for(int i=0; i<statName.length; i++)
        		value[i] = Double.valueOf(text[i].getText());
    	}catch(NumberFormatException e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }

    @Override
    protected void okPressed() {
    	if(saveInput())
    		super.okPressed();
    	else{
    		warning.setText("저장할 세팅의 이름은 최대 10글자입니다");
    	}
    }

    public double[] getValue() {
            return value;
    }

}