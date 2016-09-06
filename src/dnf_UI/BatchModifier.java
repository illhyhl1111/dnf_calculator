package dnf_UI;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Title;

public class BatchModifier extends Dialog {
	Shell shell;
	final Characters character;
	final InventoryCardPack inventory;
	final UserInfo userInfo;
	Composite itemInfo;
	Point itemInfoSize;
	Point contentSize;
	Equipment[] equipList;
	
	public BatchModifier(Shell shell, Characters character, UserInfo userInfo, InventoryCardPack inventory)
	{
		super(shell);
		this.shell=shell;
		this.userInfo=userInfo;
		this.character=character;
		this.inventory=inventory;
		
		Equip_part[] partList = {
				Equip_part.SHOULDER, Equip_part.ROBE, Equip_part.TROUSER, Equip_part.BELT, Equip_part.SHOES, Equip_part.WEAPON, Equip_part.TITLE,
				Equip_part.BRACELET, Equip_part.NECKLACE, Equip_part.AIDEQUIPMENT, Equip_part.RING, Equip_part.EARRING, Equip_part.MAGICSTONE
		};
		equipList = new Equipment[partList.length];
		for(int i=0; i<equipList.length; i++)
			equipList[i]=new Equipment(partList[i]);
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		RowLayout contentLayout = new RowLayout(SWT.VERTICAL);
		contentLayout.spacing=10;
		contentLayout.wrap=false;
		content.setLayout(contentLayout);
		
		Label guideLabel = new Label(content, SWT.WRAP);
		guideLabel.setText("\n ※\'인벤토리\' 내에 있는 모든 아이템을 일괄 강화 / 차원작 / 마법부여 합니다\n"
				+ "   (제외해야 할 아이템이 있으면 창고에 집어넣으시면 됩니다)\n\n");
		guideLabel.setLayoutData(new RowData());
		
		////////////////////강화
		
		Group enhance = new Group(content, SWT.NONE);
		enhance.setText("강화(증폭)");
		enhance.setLayoutData(new RowData());
		enhance.setLayout(new FormLayout());
		
		guideLabel = new Label(enhance, SWT.WRAP);
		guideLabel.setText("일괄변경할 장비 부위 선택 → 강화 수치 / 차원스탯 설정 → 적용버튼 누르기");
		FormData guideData = new FormData();
		guideData.left = new FormAttachment(12, 0);
		guideLabel.setLayoutData(guideData);
		
		Label part = new Label(enhance, SWT.WRAP);
		part.setText("장비 부위 선택");
		FormData partData = new FormData();
		partData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN*2);
		part.setLayoutData(partData);
		
		final Combo combo = new Combo(enhance, SWT.READ_ONLY);
		String[] items = 
			{"전부위", "무기", "방어구", "악세서리", "보장/마법석/귀걸이", "상의", "하의", "머리어깨", "벨트", "신발", "목걸이", "팔찌", "반지", "보조장비", "마법석", "귀걸이"};  
		combo.setItems(items);
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(part, InterfaceSize.MARGIN);
		combo.setLayoutData(comboData);
		/*combo.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		      }
		    });*/
		
		
		Group selectModeComposite = new Group (enhance, SWT.NO_RADIO_GROUP);
		selectModeComposite.setText("변이된 왜곡서");
		FormData selectModeData = new FormData();
		selectModeData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN);
		selectModeData.left = new FormAttachment(combo, InterfaceSize.MARGIN*2);
		selectModeComposite.setLayoutData(selectModeData);
		selectModeComposite.setLayout (new GridLayout(5, true));
		
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
		};
	
		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = GridData.CENTER;
		buttonData.grabExcessHorizontalSpace = true;
		
		Button[] dimensionList = new Button[5];
		String[] dimensionNameList = {"없음", "힘", "지능", "체력", "정신력"};
		
		for(int i=0; i<dimensionList.length; i++)
		{
			dimensionList[i] = new Button (selectModeComposite, SWT.RADIO);
			dimensionList[i].setText(dimensionNameList[i]);
			dimensionList[i].addListener(SWT.Selection, radioGroup);
			dimensionList[i].setLayoutData(buttonData);
		}
		dimensionList[0].setSelection(true);
		
		
		Group reinforceComposite = new Group (enhance, SWT.NONE);
		reinforceComposite.setText("강화/증폭");
		FormData reinforceData = new FormData();
		reinforceData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN);
		reinforceData.left = new FormAttachment(selectModeComposite, InterfaceSize.MARGIN);
		reinforceComposite.setLayoutData(reinforceData);
		reinforceComposite.setLayout (new FillLayout());
		
		Spinner reinforce = new Spinner(reinforceComposite, SWT.READ_ONLY);
	    reinforce.setMinimum(0);
	    reinforce.setMaximum(20);
	    reinforce.setSelection(0);
	    reinforce.setIncrement(1);
	    reinforce.setPageIncrement(5);
		
	    Button okButton = new Button(enhance, SWT.PUSH);
	    okButton.setText("적용");
	    FormData okButtonData = new FormData(100, 30);
	    okButtonData.top = new FormAttachment(reinforceComposite, InterfaceSize.MARGIN*4);
	    okButtonData.right = new FormAttachment(100, -InterfaceSize.MARGIN);
	    okButton.setLayoutData(okButtonData);
	    
	    okButton.addListener(SWT.Selection, new Listener(){
	    	@Override
	        public void handleEvent(Event e) {
	    		LinkedList<Equip_part> partList = new LinkedList<Equip_part>();
	    		LinkedList<Item> equipmentList = new LinkedList<Item>();
				String partSelection = combo.getText();
				//"전부위", "무기", "방어구", "악세서리", "보장/마법석/귀걸이", "상의", "하의", "머리어깨", "벨트", "신발", "목걸이", "팔찌", "반지", "보조장비", "마법석", "귀걸이"
				
				if(partSelection.equals("전부위")){
					partList.add(Equip_part.WEAPON);
					partList.add(Equip_part.ROBE);
					partList.add(Equip_part.TROUSER);
					partList.add(Equip_part.SHOULDER);
					partList.add(Equip_part.BELT);
					partList.add(Equip_part.SHOES);
					partList.add(Equip_part.BRACELET);
					partList.add(Equip_part.NECKLACE);
					partList.add(Equip_part.RING);
					partList.add(Equip_part.AIDEQUIPMENT);
					partList.add(Equip_part.MAGICSTONE);
					partList.add(Equip_part.EARRING);
				}
				else if(partSelection.equals("무기"))
					partList.add(Equip_part.WEAPON);
				else if(partSelection.equals("방어구")){
					partList.add(Equip_part.ROBE);
					partList.add(Equip_part.TROUSER);
					partList.add(Equip_part.SHOULDER);
					partList.add(Equip_part.BELT);
					partList.add(Equip_part.SHOES);
				}
				else if(partSelection.equals("악세서리")){
					partList.add(Equip_part.BRACELET);
					partList.add(Equip_part.NECKLACE);
					partList.add(Equip_part.RING);
				}
				else if(partSelection.equals("보장/마법석/귀걸이")){
					partList.add(Equip_part.AIDEQUIPMENT);
					partList.add(Equip_part.MAGICSTONE);
					partList.add(Equip_part.EARRING);
				}
				else if(partSelection.equals("상의"))
					partList.add(Equip_part.ROBE);
				else if(partSelection.equals("하의"))
					partList.add(Equip_part.TROUSER);
				else if(partSelection.equals("어깨"))
					partList.add(Equip_part.SHOULDER);
				else if(partSelection.equals("벨트"))
					partList.add(Equip_part.BELT);
				else if(partSelection.equals("신발"))
					partList.add(Equip_part.SHOES);
				else if(partSelection.equals("목걸이"))
					partList.add(Equip_part.NECKLACE);
				else if(partSelection.equals("팔찌"))
					partList.add(Equip_part.BRACELET);
				else if(partSelection.equals("반지"))
					partList.add(Equip_part.RING);
				else if(partSelection.equals("보조장비"))
					partList.add(Equip_part.AIDEQUIPMENT);
				else if(partSelection.equals("마법석"))
					partList.add(Equip_part.MAGICSTONE);
				else if(partSelection.equals("귀걸이"))
					partList.add(Equip_part.EARRING);
					 
				equipmentList=inventory.getEnabledEquipment(partList);
				
				String selected=null;
				Dimension_stat selectedStat=Dimension_stat.NONE;
				for(Button b : dimensionList)
					if(b.getSelection()){
						selected=b.getText();
						break;
					}
				
				if(selected.equals("없음")) selectedStat=Dimension_stat.NONE;
				else if(selected.equals("힘")) selectedStat=Dimension_stat.STR;
				else if(selected.equals("지능")) selectedStat=Dimension_stat.INT;
				else if(selected.equals("체력")) selectedStat=Dimension_stat.STA;
				else if(selected.equals("정신력")) selectedStat=Dimension_stat.WILL;
				
				for(Item item : equipmentList)
				{
					Equipment equipment = (Equipment)item;
					try{
						equipment.setDimension(selectedStat);
					}
					catch(UnknownInformationException exception){
						equipment.setDimensionType(selectedStat);
					}
					
					try{
						equipment.setReinforce(reinforce.getSelection());
					}
					catch(UnknownInformationException exception){
						equipment.setReinforceNum(reinforce.getSelection());
					}
				}
				userInfo.renew();
				
				MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
					    partSelection+" 강화(증폭) 완료",
					    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				dialog.open();
			 }
		});
	    
	    //////////////////마법부여
	    
	    Group enchant = new Group(content, SWT.NONE);
	    enchant.setText("마법부여");
	    enchant.setLayoutData(new RowData());
	    enchant.setLayout(new FormLayout());
		
		guideLabel = new Label(enchant, SWT.WRAP);
		guideLabel.setText("각 부위에 원하는 보주를 드래그&드롭 → 적용버튼 누르기");
		guideData = new FormData();
		guideData.left = new FormAttachment(20, 0);
		guideLabel.setLayoutData(guideData);
		
		
		Composite leftItemInfoComposite = new Composite(enchant, SWT.NONE);
		FormData UIData = new FormData();
		UIData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN*2);
		leftItemInfoComposite.setLayoutData(UIData);
		
		Composite characterImageComposite = new Composite(enchant, SWT.BORDER);
		UIData = new FormData(200, 100);
		UIData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN*2);
		UIData.left = new FormAttachment(leftItemInfoComposite, InterfaceSize.MARGIN);
		characterImageComposite.setLayoutData(UIData);
		
		Composite rightItemInfoComposite = new Composite(enchant, SWT.NONE);
		UIData = new FormData();
		UIData.top = new FormAttachment(guideLabel, InterfaceSize.MARGIN*2);
		UIData.left = new FormAttachment(characterImageComposite, InterfaceSize.MARGIN);
		rightItemInfoComposite.setLayoutData(UIData);
		
		//characterImageComposite.setBackgroundImage(new Image(parent.getDisplay(), character.getCharImageAddress()));		//가운데 이미지 ->캐릭터 이미지
		
		GridLayout itemInfoLayout = new GridLayout(2, true);
		itemInfoLayout.horizontalSpacing=3;
		itemInfoLayout.verticalSpacing=3;
		itemInfoLayout.marginHeight=0;
		itemInfoLayout.marginWidth=0;
		itemInfoLayout.makeColumnsEqualWidth=true;
		leftItemInfoComposite.setLayout(itemInfoLayout);
		rightItemInfoComposite.setLayout(itemInfoLayout);

		ItemButton[] itemButtonList = new ItemButton[UserItemInfo.ITEMNUM];
		int BUTTON_SIZE = InterfaceSize.INFO_BUTTON_SIZE;
		int i;
		for(i=0; i<5; i++)
			itemButtonList[i] = new ItemButton(leftItemInfoComposite, equipList[i], BUTTON_SIZE, BUTTON_SIZE, true);
		for(; i<UserItemInfo.ITEMNUM; i++){
			if(i==6) itemButtonList[i] = new ItemButton(rightItemInfoComposite, new Title(), BUTTON_SIZE, BUTTON_SIZE, true);
			else itemButtonList[i] = new ItemButton(rightItemInfoComposite, equipList[i], BUTTON_SIZE, BUTTON_SIZE, true);
		}
		
		GridData buttonGridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		for(i=0; i<UserItemInfo.ITEMNUM; i++){
			itemButtonList[i].getButton().setData(buttonGridData);
		}
		
		Point buttonS = itemButtonList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		buttonS.x+=3; buttonS.y+=3;
		
		for(i=0; i<UserItemInfo.ITEMNUM; i++)
		{
			setDrop(itemButtonList[i]);
			
			Integer x0;
			if(i>4) x0 = leftItemInfoComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x+225+((i-5)%2)*buttonS.x;
			else x0 = (i%2)*buttonS.x+10;
			
			Integer y0;
			if(i>4) y0 = (int)((i-5)/2)*buttonS.y + enhance.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+116;
			else y0 = (int)(i/2)*buttonS.y+ enhance.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+116;
			Integer indexBox = i;
			
			itemButtonList[i].getButton().addListener(SWT.MouseDown, new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(e.button==3){
		        		 if((Equipment)itemButtonList[indexBox].getItem() instanceof Equipment) ((Equipment)itemButtonList[indexBox].getItem()).setCard(new Card());
		        		 else ((Title)itemButtonList[indexBox].getItem()).setCard(new Card());
		        		 if(itemInfo!=null && !itemInfo.isDisposed()){
			        		 itemInfo.dispose();
			        	 }
		        	 }
		         }
		     });
			
			// add MouseEnter Event - make composite
			itemButtonList[i].getButton().addListener(SWT.MouseEnter, new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 Card card= itemButtonList[indexBox].getItem().getCard();
	        		 
	        		 if( card.getName().contains("없음")) return;
	        		 itemInfo = new Composite(parent, SWT.BORDER);
	        		 GridLayout layout = new GridLayout(1, false);
	        		 layout.verticalSpacing=3;
	        		 itemInfo.setLayout(layout);
	        		 MakeComposite.setItemInfoComposite(itemInfo, card);
	        		 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	        		 itemInfo.setBounds((e.x+x0), (e.y+y0-itemInfoSize.y), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	        		 itemInfo.moveAbove(null);
		         }
		     });
			
			// add MouseExit Event - dispose composite
			itemButtonList[i].getButton().addListener(SWT.MouseExit, new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(itemInfo !=null && !itemInfo.isDisposed()){
		        		 //System.out.println("Mouse Exited "+i.getName());
		        		 itemInfo.dispose();
		        	 }
		         }
		     });
			
			// add MouseMove Event - move composite
			itemButtonList[i].getButton().addListener(SWT.MouseMove, new Listener() {
		         @Override
		         public void handleEvent(Event e) {
		        	 if(itemInfo !=null && !itemInfo.isDisposed()){
		        		 //System.out.println("Mouse Move (button: " + e.button + " x: " + (e.x+x0) + " y: " + (e.y+y0) + ")");
		        		 itemInfo.setLocation((e.x+x0), (e.y+y0-itemInfoSize.y));
		        	 }
		         }
		     });
		}
		
		okButton = new Button(enchant, SWT.PUSH);
	    okButton.setText("적용");
	    okButtonData = new FormData(100, 30);
	    okButtonData.top = new FormAttachment(rightItemInfoComposite, InterfaceSize.MARGIN*4);
	    okButtonData.right = new FormAttachment(100, -InterfaceSize.MARGIN);
	    okButton.setLayoutData(okButtonData);
		
	    okButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				for(ItemButton b : itemButtonList){
					if(b.getItem().getCard().getName().contains("없음")) continue;
					
					Card tempCard = b.getItem().getCard();
					
					LinkedList<Equip_part> tempList = new LinkedList<Equip_part>();
					tempList.add(b.getItem().getPart());
					
					for(Item e : inventory.getEnabledEquipment(tempList))
						e.setCard(tempCard);
				}
				
				userInfo.renew();
				MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
					    "마법부여 완료", MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
				dialog.open();
			}
	    });
	    
	    
	    contentSize = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    
		return content;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("아이템 일괄변경");
	}
		
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS| SWT.BORDER | SWT.TITLE);
	    setBlockOnOpen(false);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.CANCEL_ID, "나가기", false);
	}
	
	@Override
	protected Point getInitialSize() {
	    return new Point(contentSize.x+130, contentSize.y+100);
	}
	
	public void setDrop(final ItemButton itemButton) {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		DropTarget target = new DropTarget(itemButton.getButton(), operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				if (itemButton.getItem() instanceof Equipment || itemButton.getItem() instanceof Title) {
					try {
						Card card = character.userItemList.getCard((String)event.data);
						boolean succeed;
						if(itemButton.getItem() instanceof Equipment) succeed = ((Equipment)itemButton.getItem()).setCard(card);
						else succeed = ((Title)itemButton.getItem()).setCard(card);
						if(succeed){
							MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
								    "마법부여 지정에 성공하였습니다!\n\n보주 : "+(String)event.data+"\n부위 : "+((Equipment)itemButton.getItem()).part,
								    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
							dialog.open();
						}
						else{
							MessageDialog dialog = new MessageDialog(shell, "실★패", null,
								    "마법부여 지정에 실패하였습니다\n\n보주 : "+(String)event.data+"\n가능한 장비 부위 : "+card.getPartToString(),
								    MessageDialog.ERROR, new String[] { "납득" }, 0);
							dialog.open();
						}
					} catch (ItemFileNotFounded e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
