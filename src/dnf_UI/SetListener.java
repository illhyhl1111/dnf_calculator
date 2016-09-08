package dnf_UI;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Card;
import dnf_class.Characters;

public class SetListener {
	ItemButton itemButton;
	Characters character;
	UserInfo superInfo;
	Composite itemInfo;
	Boolean hasSetOption;
	Composite setInfo;
	Integer xPoint;
	Integer yPoint;
	Composite parent;
	int x0;
	int y0;
	
	public SetListener(ItemButton itemButton, Characters character, UserInfo superInfo, Composite itemInfo,
			Composite setInfo, int xPoint, int yPoint, Composite parent)
	{
		this.itemButton=itemButton;
		this.character=character;
		this.superInfo=superInfo;
		this.itemInfo=itemInfo;
		this.setInfo=setInfo;
		this.xPoint=xPoint;
		this.yPoint=yPoint;
		this.parent=parent;
	}
	
	public Listener unequipListener()
	{
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
	        			 if(((UserItemInfo)superInfo.userItemInfo).equiped(itemButton.getItem())){
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
		return new Listener() {
			@Override
	        public void handleEvent(Event e) {
				if(e.button==3 && itemButton.getItem().getEnabled()){
	        		character.equip(itemButton.getItem());
	        		superInfo.renew();
	        	}
	        }
		};
	}
	
	public Listener modifyListener()
	{
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(itemButton.getItem().getEnabled())
	        	 {
	        		ChangeItemStatus changeItem = new ChangeItemStatus(parent.getShell(), itemButton.getItem(), itemButton.hasSetOption());
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
							try {
								character.userItemList.setSetOptions(setName, changeSet.setOption);
								itemButton.setItem(changeItem.item);
								superInfo.renew();
							} catch (ItemFileNotFounded e1) {
								e1.printStackTrace();
							}				
						}
					}
	        	 }
			}
		}; 
	}
	
	public Listener makeItemInfoListener(Composite background)
	{	     
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
	        		 MakeComposite.setItemInfoComposite(itemInfo, itemButton.getItem());
	        		 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
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
	        		 
	        		 if(setInfoSize!=null) y0 = yPoint-Math.max(setInfoSize.y, itemInfoSize.y)-5;
	        		 else y0=yPoint-itemInfoSize.y-5;
	        		 if(hasSetOption)
	        			 x0 = xPoint-InterfaceSize.ITEM_INFO_SIZE-InterfaceSize.SET_INFO_SIZE-5-InterfaceSize.SET_ITEM_INTERVAL;
	        		 else x0 = xPoint-InterfaceSize.ITEM_INFO_SIZE-5;
	        		 if(x0<0) x0 = xPoint+5;
	        		 if(y0<0) y0 = yPoint+5;
	        		 itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
	        		 if(hasSetOption) setInfo.setBounds((e.x+x0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+y0), InterfaceSize.SET_INFO_SIZE, setInfoSize.y);
	        	 }
	         }
	     };
	}
	
	public Listener makeCardInfoListener(Composite background)
	{
		return new Listener() {
	        @Override
	        public void handleEvent(Event e) {
	        	Card card= itemButton.getItem().getCard();
       		
       		if( card.getName().contains("없음")) return;
       		itemInfo = new Composite(background, SWT.BORDER);
       		GridLayout layout = new GridLayout(1, false);
       		layout.verticalSpacing=3;
       		itemInfo.setLayout(layout);
       		MakeComposite.setItemInfoComposite(itemInfo, card);
       		Point itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
       		y0 = yPoint-itemInfoSize.y-5;
       		x0 = xPoint-InterfaceSize.ITEM_INFO_SIZE-5;
       		if(x0<0) x0 = xPoint+5;
       		if(y0<0) y0 = yPoint+5;
       		itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
       		itemInfo.moveAbove(null);
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
	
	public void setDrag()
	{
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
