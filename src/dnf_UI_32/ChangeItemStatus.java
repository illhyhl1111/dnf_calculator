package dnf_UI_32;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_class.Card;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Title;
import dnf_class.Weapon;
import dnf_infomation.GetDictionary;

class Wrapper {
	private Text text;
	private Button button;
	public boolean hasButton;
	    
    public Wrapper(Text text, Button button) {
       this.text=text;
       this.button = button;
       hasButton=true;
    }
    
    public Wrapper(Text text, boolean hasButton)
    {
    	this.text=text;
    	this.hasButton=hasButton;
    	button=null;
    }

    public Text getText() { return this.text; }
    public Button getButton() { return this.button; }
    public boolean textHasData() {
    	if(text==null) return false;
    	return !text.getText().isEmpty();
    }
}

public class ChangeItemStatus extends Dialog{
	public Item item;
	private Item originalItem;
	private Composite composite;
	private LinkedList<Entry<Integer, Wrapper>> vStatEntry;
	private LinkedList<Entry<Integer, Wrapper>> dStatEntry;
	private boolean hasSet;
	private Dimension_stat currentDimStat;
	private int currentReinforce;
	private int currentReforge;
	private boolean replicateEnabled;
	
	public ChangeItemStatus(Shell parent, Item item, boolean hasSet, boolean replicateEnabled)
	{
		super(parent);
		this.item=item;		
		this.hasSet=hasSet;
		this.replicateEnabled=replicateEnabled;
		try {
			if(item instanceof Equipment) originalItem=GetDictionary.getEquipment(item.getItemName());
			else if(item instanceof Title) originalItem=GetDictionary.getTitle(item.getItemName());
			else if(item instanceof Card) originalItem=GetDictionary.getCard(item.getItemName());
			else originalItem=null;
		} catch (ItemFileNotReaded | ItemFileNotFounded e) {
			e.printStackTrace();
		}
		vStatEntry = new LinkedList<Entry<Integer, Wrapper>>();
		dStatEntry = new LinkedList<Entry<Integer, Wrapper>>();
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);

		Label dimStatLabel;
		Label phyIgnStatLabel;
		Label magIgnStatLabel;
		Label reforgeStatLabel;
		Label aidStatLabel;
		Label[] earringStatLabel = new Label[3];
		
		Text dimStatText;
		Text phyIgnStatText;
		Text magIgnStatText;
		Text reforgeStatText;
		Text aidStatText;
		Text[] earringStatText = new Text[3];
	
		final StatusAndName dimStat;
		final StatusAndName phyIgnStat;
		final StatusAndName magIgnStat;
		final StatusAndName reforgeStat;
		final StatusAndName aidStat;
		final StatusAndName[] earringStat = new StatusAndName[3];
		
		Group selectModeComposite;
		Group reinforceComposite;
		Group reforgeComposite;
		final Spinner reinforce;
		final Spinner reforge;
		if(item instanceof Equipment)
		{	
			currentDimStat = ((Equipment)item).getDimentionStat();
			currentReinforce = ((Equipment)item).getReinforce();
			
			if(item instanceof Weapon) currentReforge = ((Weapon)item).getReforge();
			else currentReforge=0;
		}
		
		composite = new Composite(content, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.verticalSpacing=3;
		composite.setLayout(layout);
		
		Label name = new Label(composite, SWT.WRAP);
		setItemName(name);
		GridData nameData = new GridData(SWT.LEFT, SWT.TOP, true, false, 4, 1);
		nameData.grabExcessHorizontalSpace=true;
		nameData.minimumWidth=400;
		name.setLayoutData(nameData);
		
		if(item instanceof Equipment)
		{
			selectModeComposite = new Group (composite, SWT.NO_RADIO_GROUP);
			selectModeComposite.setText("변이된 왜곡서");
			selectModeComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
			selectModeComposite.setLayout (new GridLayout(5, true));
			
			reinforceComposite = new Group (composite, SWT.NONE);
			reinforceComposite.setText("강화/증폭기");
			reinforceComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			reinforceComposite.setLayout (new FillLayout());
			
			reinforce = new Spinner(reinforceComposite, SWT.READ_ONLY);
		    reinforce.setMinimum(0);
		    reinforce.setMaximum(20);
		    reinforce.setSelection(currentReinforce);
		    reinforce.setIncrement(1);
		    reinforce.setPageIncrement(5);
		    
		    if(item instanceof Weapon)
		    {
		    	selectModeComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		    	
		    	reforgeComposite = new Group (composite, SWT.NONE);
				reforgeComposite.setText("용화덕");
				reforgeComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
				reforgeComposite.setLayout (new FillLayout());
				
				reforge = new Spinner(reforgeComposite, SWT.READ_ONLY);
			    reforge.setMinimum(0);
			    reforge.setMaximum(8);
			    reforge.setSelection(currentReforge);
			    reforge.setIncrement(1);
			    reforge.setPageIncrement(1);
		    }
		    else reforge=null;
		}
		else {
			reinforce=null;
			selectModeComposite=null;
			reforge=null;
		}
		
		Label rarity = new Label(composite, SWT.WRAP);
		rarity.setText(item.getRarity().getName());
		rarity.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 4, 1));
		
		//Color nameColor = new Color(composite.getDisplay());
		switch(item.getRarity())
		{
		case EPIC:
			name.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			break;
		case UNIQUE:
			name.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			break;
		case LEGENDARY:
			name.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			break;
		case RARE:
			name.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			break;
			
		default:
		}
		
		Label type = new Label(composite, SWT.WRAP);
		type.setText(item.getTypeName());
		type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 4, 1));
		if(item.getTypeName2()!=null)
		{
			type = new Label(composite, SWT.WRAP);
			type.setText(item.getTypeName2());
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 4, 1));
		}
		if(item instanceof Equipment){
			type = new Label(composite, SWT.WRAP);
			type.setText(String.valueOf("레벨제한 "+ ((Equipment)item).level) );
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 4, 1));
		}
		
		try
		{
			StatusAndName tempStat=null;
			StatusAndName tempStat2=null;
			StatusAndName tempStat3=null;
			Label tempLabel=null;
			Label tempLabel2=null;
			Label tempLabel3=null;
			Text tempText=null;
			Text tempText2=null;
			Text tempText3=null;
			GridData textData = new GridData(SWT.LEFT, SWT.TOP, true, false, 3, 1);
			textData.grabExcessHorizontalSpace=true;
			textData.minimumWidth=50;
			try{
				tempStat = item.vStat.statList.get(item.getDimStatIndex());
				tempLabel = new Label(composite, SWT.WRAP);
				GridData labelData = new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1);
				labelData.grabExcessHorizontalSpace=true;
				labelData.minimumWidth=100;
				tempLabel.setLayoutData(labelData);
				
				tempText = new Text(composite, SWT.NONE);
				tempText.setEditable(false);
				try{
					GetDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
				}
				tempText.setLayoutData(textData);
				
				setDimStat((int)tempStat.stat.getStatToDouble(), tempLabel, tempText);				
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getDimStatIndex(), new Wrapper(tempText, false));
				vStatEntry.add(entry);
		
			} catch(IndexOutOfBoundsException e){
				tempStat=null;
				tempLabel=null;
				tempText=null;
				
			} finally{
				dimStat=tempStat;
				dimStatLabel=tempLabel;
				dimStatText=tempText;
			}
			
			try{
				tempStat = item.vStat.statList.get(item.getIgnIndex());
				tempStat2 = item.vStat.statList.get(item.getIgnIndex()+1);
				
				tempLabel = new Label(composite, SWT.WRAP);
				tempLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));

				tempText = new Text(composite, SWT.NONE);
				tempText.setEditable(false);
				try{
					GetDictionary.getReinforceInfo_phy(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
				}
				tempText.setLayoutData(textData);
				
				tempLabel2 = new Label(composite, SWT.WRAP);
				tempLabel2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				
				tempText2 = new Text(composite, SWT.NONE);
				tempText2.setEditable(false);
				try{
					GetDictionary.getReinforceInfo_mag(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				}catch(UnknownInformationException e){
					tempText2.setEditable(true);
				}
				tempText2.setLayoutData(textData);
				
				setIgnStat((int)tempStat.stat.getStatToDouble(), tempLabel, tempText, (int)tempStat2.stat.getStatToDouble(), tempLabel2, tempText2);
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getIgnIndex(), new Wrapper(tempText, false));
				vStatEntry.add(entry);
				entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getIgnIndex()+1, new Wrapper(tempText2, false));
				vStatEntry.add(entry);
				
			} catch(IndexOutOfBoundsException e){
				tempLabel=null;
				tempLabel2=null;
				tempText=null;
				tempText2=null;
				tempStat=null;
				tempStat2=null;
			} finally{
				phyIgnStat=tempStat;
				phyIgnStatLabel=tempLabel;
				phyIgnStatText=tempText;
				magIgnStat=tempStat2;
				magIgnStatLabel=tempLabel2;
				magIgnStatText=tempText2;
			}
			
			try{
				tempStat = item.vStat.statList.get(item.getReforgeIndex());
				tempLabel = new Label(composite, SWT.WRAP);
				GridData labelData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
				labelData.grabExcessHorizontalSpace=true;
				labelData.minimumWidth=100;
				tempLabel.setLayoutData(labelData);
				
				tempText = new Text(composite, SWT.NONE);
				tempText.setEditable(false);
				try{
					GetDictionary.getReforgeInfo(currentReforge, item.getRarity(), ((Equipment)item).level);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
				}
				tempText.setLayoutData(textData);
				
				setRefStat((int)tempStat.stat.getStatToDouble(), tempLabel, tempText);				
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getReforgeIndex(), new Wrapper(tempText, false));
				vStatEntry.add(entry);
		
			} catch(IndexOutOfBoundsException e){
				tempStat=null;
				tempLabel=null;
				tempText=null;
				
			} finally{
				reforgeStat=tempStat;
				reforgeStatLabel=tempLabel;
				reforgeStatText=tempText;
			}
			
			try{
				tempStat = item.vStat.statList.get(item.getAidStatIndex());
				tempLabel = new Label(composite, SWT.WRAP);
				tempLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				
				tempText = new Text(composite, SWT.NONE);
				tempText.setEditable(false);
				try{
					GetDictionary.getReinforceAidInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
				}
				tempText.setLayoutData(textData);
				
				setAidStat((int)tempStat.stat.getStatToDouble(), tempLabel, tempText);
				int index = item.getAidStatIndex();
				Wrapper wrapper = new Wrapper(tempText, false);
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(index, wrapper);
				vStatEntry.add(entry);
				vStatEntry.add(new AbstractMap.SimpleEntry<Integer, Wrapper>(index+1, wrapper));
				vStatEntry.add(new AbstractMap.SimpleEntry<Integer, Wrapper>(index+2, wrapper));
				vStatEntry.add(new AbstractMap.SimpleEntry<Integer, Wrapper>(index+3, wrapper));
				
			} catch(IndexOutOfBoundsException e) {
				tempLabel=null;
				tempText=null;
				tempStat=null;
			} finally{
				aidStat=tempStat;
				aidStatLabel=tempLabel;
				aidStatText=tempText;
			}
			
			try{
				tempStat = item.vStat.statList.get(item.getEarringStatIndex());
				tempStat2 = item.vStat.statList.get(item.getEarringStatIndex()+1);
				tempStat3 = item.vStat.statList.get(item.getEarringStatIndex()+2);

				tempLabel = new Label(composite, SWT.WRAP);
				tempText = new Text(composite, SWT.NONE);
				tempLabel2 = new Label(composite, SWT.WRAP);
				tempText2 = new Text(composite, SWT.NONE);
				tempLabel3 = new Label(composite, SWT.WRAP);
				tempText3 = new Text(composite, SWT.NONE);
				
				tempText.setEditable(false);
				tempText.setLayoutData(textData);
				tempText2.setEditable(false);
				tempText2.setLayoutData(textData);
				tempText3.setEditable(false);
				tempText3.setLayoutData(textData);
				try{
					GetDictionary.getReinforceEarringInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
					tempText2.setEditable(true);
					tempText3.setEditable(true);
				}
				
				
				tempLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				tempLabel2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				tempLabel3.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				
				int[] box = {(int)tempStat.stat.getStatToDouble(), (int)tempStat2.stat.getStatToDouble(), (int)tempStat3.stat.getStatToDouble()};
				setEarringStat(box, new Label[] {tempLabel, tempLabel2, tempLabel3}, new Text[] {tempText, tempText2, tempText3});
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getEarringStatIndex(), new Wrapper(tempText, false));
				vStatEntry.add(entry);
				entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getEarringStatIndex()+1, new Wrapper(tempText2, false));
				vStatEntry.add(entry);
				entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(item.getEarringStatIndex()+2, new Wrapper(tempText3, false));
				vStatEntry.add(entry);
				
			} catch(IndexOutOfBoundsException e) {
				tempLabel=null;
				tempText=null;
				tempStat=null;
				tempLabel2=null;
				tempText2=null;
				tempStat2=null;
				tempLabel3=null;
				tempText3=null;
				tempStat3=null;
			} finally{
				earringStat[0]=tempStat;
				earringStatLabel[0]=tempLabel;
				earringStatText[0]=tempText;
				earringStat[1]=tempStat2;
				earringStatLabel[1]=tempLabel2;
				earringStatText[1]=tempText2;
				earringStat[2]=tempStat3;
				earringStatLabel[2]=tempLabel3;
				earringStatText[2]=tempText3;
			}
			
			Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));	
			
			int index = item.getItemStatIndex();
			Iterator<StatusAndName> maxS;
			if(originalItem!=null)
			{
				maxS = originalItem.vStat.statList.subList(index, item.vStat.statList.size()).iterator();
				List<StatusAndName> itemStatList = item.vStat.statList.subList(index, item.vStat.statList.size());
				for(StatusAndName s : itemStatList) {
					Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(index++, setText(composite, s, maxS.next()));
					vStatEntry.add(entry);
				}
			}
			else{
				List<StatusAndName> itemStatList = item.vStat.statList.subList(index, item.vStat.statList.size());
				for(StatusAndName s : itemStatList) {
					Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(index++, setText(composite, s, new StatusAndName(0, new StatusInfo(0)) ));
					vStatEntry.add(entry);
				}
			}
			
			if(!item.dStat.statList.isEmpty() || !item.fStat.statList.isEmpty())
			{
				label = new Label(composite, SWT.WRAP);
				label.setText("\n――――――던전 입장 시 적용――――――\n\n");
				label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1));
				
				if(originalItem!=null)
				{
					maxS = originalItem.dStat.statList.iterator();
					int dIndex=0;
					for(StatusAndName s : item.dStat.statList){
						Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(dIndex++, setText(composite, s, maxS.next()));
						dStatEntry.add(entry);
					}
				}
				else{
					int dIndex=0;
					for(StatusAndName s : item.dStat.statList){
						Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(dIndex++, setText(composite, s, new StatusAndName(0, new StatusInfo(0)) ));
						dStatEntry.add(entry);
					}
				}
			}
			
			if(item instanceof Equipment)
			{
				reinforce.addModifyListener(event-> {
			    	currentReinforce=reinforce.getSelection();
			    	setItemName(name);
			    	
			    	if(dimStat!=null){
				    	try {
							setDimStat(GetDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level), dimStatLabel, dimStatText);
							dimStatText.setEditable(false);
						} catch (UnknownInformationException e){
							if(currentDimStat!=Dimension_stat.NONE) dimStatText.setEditable(true);
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
						}
			    	}
			    	
			    	if(phyIgnStat!=null){
				    	try {
				    		int phyStat=GetDictionary.getReinforceInfo_phy(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				    		int magStat=GetDictionary.getReinforceInfo_mag(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
							setIgnStat(phyStat, phyIgnStatLabel, phyIgnStatText, magStat, magIgnStatLabel, magIgnStatText);
							phyIgnStatText.setEditable(false);
				    		magIgnStatText.setEditable(false);
				    	} catch (UnknownInformationException e){
				    		phyIgnStatText.setEditable(true);
				    		magIgnStatText.setEditable(true);
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
						}
			    	}
			    	
			    	if(aidStat!=null){
				    	try {
							setAidStat(GetDictionary.getReinforceAidInfo(currentReinforce, item.getRarity(), ((Equipment)item).level), aidStatLabel, aidStatText);
							aidStatText.setEditable(false);
						} catch (UnknownInformationException e){
							aidStatText.setEditable(true);
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
						}
			    	}
			    	
			    	if(earringStat[0]!=null){
				    	try {
				    		int[] result = GetDictionary.getReinforceEarringInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
							setEarringStat(new int[] {result[0], result[0], result[1]}, earringStatLabel, earringStatText);
							earringStatText[0].setEditable(false);
							earringStatText[1].setEditable(false);
							earringStatText[2].setEditable(false);
						} catch (UnknownInformationException e){
							earringStatText[0].setEditable(true);
							earringStatText[1].setEditable(true);
							earringStatText[2].setEditable(true);
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
						}
			    	}
			    	
				});
				
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
					try{
						if(button2.getText().equals("없음")){
							currentDimStat=Dimension_stat.NONE;
							dimStatText.setEditable(false);
						}
						else if(button2.getText().equals("힘")){
							currentDimStat=Dimension_stat.STR;
						}
						else if(button2.getText().equals("지능")){
							currentDimStat=Dimension_stat.INT;
						}
						else if(button2.getText().equals("체력")){
							currentDimStat=Dimension_stat.STA;
						}
						else if(button2.getText().equals("정신력")){
							currentDimStat=Dimension_stat.WILL;
						}
						
						int str = GetDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
						setDimStat(str, dimStatLabel, dimStatText);
						dimStatText.setEditable(false);
					}
					catch(UnknownInformationException e)
					{
						if(currentDimStat!=Dimension_stat.NONE) dimStatText.setEditable(true);
						try {
							if(dimStatText.getText().isEmpty()) setDimStat(0, dimStatLabel, dimStatText);
							else setDimStat(Integer.valueOf(dimStatText.getText()), dimStatLabel, dimStatText);
						} catch (NumberFormatException | StatusTypeMismatch e1) {
							e1.printStackTrace();
						}
					}
					catch(StatusTypeMismatch e)
					{
						e.printStackTrace();
					}		
				};
			
				Button noneButton = new Button (selectModeComposite, SWT.RADIO);
				noneButton.setText("없음");
				noneButton.addListener(SWT.Selection, radioGroup);
				GridData gridData = new GridData();
				gridData.horizontalAlignment = GridData.CENTER;
				gridData.grabExcessHorizontalSpace = true;
				noneButton.setLayoutData(gridData);
				if(((Equipment)item).getDimentionStat()==Dimension_stat.NONE) noneButton.setSelection(true);
				
				Button strButton = new Button (selectModeComposite, SWT.RADIO);
				strButton.setText("힘");
				strButton.addListener(SWT.Selection, radioGroup);
				strButton.setLayoutData(gridData);
				if(((Equipment)item).getDimentionStat()==Dimension_stat.STR) strButton.setSelection(true);
				
				Button intButton = new Button (selectModeComposite, SWT.RADIO);
				intButton.setText("지능");
				intButton.addListener(SWT.Selection, radioGroup);
				intButton.setLayoutData(gridData);
				if(((Equipment)item).getDimentionStat()==Dimension_stat.INT) intButton.setSelection(true);
				
				Button staButton = new Button (selectModeComposite, SWT.RADIO);
				staButton.setText("체력");
				staButton.addListener(SWT.Selection, radioGroup);
				staButton.setLayoutData(gridData);
				if(((Equipment)item).getDimentionStat()==Dimension_stat.STA) staButton.setSelection(true);
				
				Button willButton = new Button (selectModeComposite, SWT.RADIO);
				willButton.setText("정신력");
				willButton.addListener(SWT.Selection, radioGroup);
				willButton.setLayoutData(gridData);
				if(((Equipment)item).getDimentionStat()==Dimension_stat.WILL) willButton.setSelection(true);

			}
			
			if(item instanceof Weapon){
				reforge.addModifyListener(event-> {
			    	currentReforge=reforge.getSelection();
			    	
			    	try {
						setRefStat(GetDictionary.getReforgeInfo(currentReforge, item.getRarity(), ((Equipment)item).level), reforgeStatLabel, reforgeStatText);
						reforgeStatText.setEditable(false);
					} catch (UnknownInformationException e){
						reforgeStatText.setEditable(true);
					}
				});
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		return content;
	}

	public Wrapper setText(Composite itemInfo, StatusAndName s, StatusAndName maxS) throws StatusTypeMismatch
	{
		String strength;
		String maxStrength;
		Label statName;
		Text statNum;
		Label maxStatNum;
		Button enable;
		Button enable2;
		Label stat2;
		
		strength = String.format("%.1f", s.stat.getStatToDouble());
		maxStrength = String.format("%.1f", maxS.stat.getStatToDouble());
		if(s.stat instanceof ElementInfo && maxStrength.equals("0.0")){
			statNum=null;
			enable=null;
		}
		else{
			if(strength.contains(".0")) strength=strength.substring(0, strength.length()-2);
			if(maxStrength.contains(".0")) maxStrength=maxStrength.substring(0, maxStrength.length()-2);
			
			statName  = new Label(itemInfo, SWT.NONE);
			String name = StatusAndName.getStatHash().get(s.name);
			if(s.name==StatList.SKILL || s.name==StatList.SKILL_RANGE)
				name = s.stat.getStatToString()+name;
			statName.setText(name);
			statName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
			
			statNum = new Text(itemInfo, SWT.NONE);
			statNum.setText(strength);
			GridData textData = new GridData(SWT.LEFT, SWT.TOP, true, false);
			textData.grabExcessHorizontalSpace=true;
			textData.minimumWidth=50;
			statNum.setLayoutData(textData);
			
			maxStatNum = new Label(itemInfo, SWT.NONE);
			maxStatNum.setText("(max : "+maxStrength+")");
			maxStatNum.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
			
			enable = new Button(itemInfo, SWT.CHECK);
			enable.setText("옵션 켜기");
			enable.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
			enable.setSelection(s.enabled);
			
			
			if(s.changeable){
				statNum.addVerifyListener(new TextInputOnlyNumbers(Integer.valueOf(maxStrength)));
			}
			else{
				statNum.setEditable(false);
				statNum.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				maxStatNum.setVisible(false);
			}
			
			if(s.enableable){
				enable.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e) {
						boolean enabled = enable.getSelection();
						statName.setEnabled(enabled);
						statNum.setEnabled(enabled);
						maxStatNum.setEnabled(enabled);
					}
				});
			}
			else{
				enable.setVisible(false);
			}
		}
		
		if(s.stat instanceof ElementInfo && ((ElementInfo)s.stat).getElementEnabled()==true)
		{
			stat2 = new Label(itemInfo, SWT.WRAP);
			switch(s.name)
			{
			case StatList.ELEM_FIRE:
				stat2.setText(" 무기에 화속성 부여");
				break;
			case StatList.ELEM_WATER:
				stat2.setText(" 무기에 수속성 부여");
				break;
			case StatList.ELEM_LIGHT:
				stat2.setText(" 무기에 명속성 부여");
				break;
			case StatList.ELEM_DARKNESS:
				stat2.setText(" 무기에 암속성 부여");
				break;
			}
			stat2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 3, 1));
			if(enable == null){
				enable2 = new Button(itemInfo, SWT.CHECK);
				enable2.setText("활성화");
				enable2.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
				
				enable2.setSelection(s.enabled);
				enable2.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e) {
						boolean enabled = enable2.getSelection();
						stat2.setEnabled(enabled);
					}
				});
			}
			else{
				enable2=null;
				enable.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e) {
						boolean enabled = enable.getSelection();
						stat2.setEnabled(enabled);
					}
				});
			}
		}
		else enable2=null;
		if(enable!=null) return new Wrapper(statNum, enable);
		return new Wrapper(statNum, enable2);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("아이템 능력치 변경");
	}
	
	@Override
	protected Control createButtonBar(final Composite parent)
	{
	    final Composite buttonBar = new Composite(parent, SWT.NONE);

	    final GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = false;
	    layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
	    buttonBar.setLayout(layout);

	    final GridData data = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = false;
	    buttonBar.setLayoutData(data);

	    buttonBar.setFont(parent.getFont());

	    // place a button on the left
	    if(hasSet){
		    final Button leftButton = createButton(buttonBar, 2, "세트옵션 변경", false);
		    leftButton.setText("세트옵션 변경");
	
		    final GridData leftButtonData = new GridData(SWT.LEFT, SWT.CENTER, true, true);
		    leftButton.setLayoutData(leftButtonData);
	    }
	    
	    if(replicateEnabled){
	    	int mode=3;
		    String buttonStr="아이템 복제";
		    if(item.replicateNum!=0){
		    	mode=4;
		    	buttonStr="아이템 삭제";
		    }
		    
		    final Button replicateButton = createButton(buttonBar, mode, buttonStr, false);
		    replicateButton.setText(buttonStr);
		    
		    final GridData leftButtonData = new GridData(SWT.LEFT, SWT.CENTER, true, true);
		    replicateButton.setLayoutData(leftButtonData);
	    }

	    // add the dialog's button bar to the right
	    final Control buttonControl = super.createButtonBar(buttonBar);
	    buttonControl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

	    return buttonBar;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, "변경", true);
	    createButton(parent, IDialogConstants.CANCEL_ID, "취소", false);
	}
	
	@Override
	protected void okPressed() {
		if(item instanceof Equipment){
			((Equipment)item).setReinforceNum(currentReinforce);
			((Equipment)item).setDimensionType(currentDimStat);
			
			if(item instanceof Weapon)
				((Weapon)item).setReforgeNum(currentReforge);
		}
		
		for(Entry<Integer, Wrapper> e : vStatEntry)
			if(e.getValue()!=null){
				boolean enable = e.getValue().hasButton;
				if(enable) enable = e.getValue().getButton().getSelection();
				if(e.getValue().textHasData())
					item.vStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), enable);
				else item.vStat.changeStat(e.getKey(), 0, enable);
			}
				
		for(Entry<Integer, Wrapper> e : dStatEntry)
			if(e.getValue()!=null){
				boolean enable = e.getValue().hasButton;
				if(enable) enable = e.getValue().getButton().getSelection();
				if(e.getValue().textHasData())
					item.dStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), enable);
				else item.dStat.changeStat(e.getKey(), 0, enable);
			}
	    super.okPressed();
	}
	
	protected void buttonPressed(int buttonId) {
	    setReturnCode(buttonId);
	    if(buttonId==IDialogConstants.OK_ID) okPressed(); 
	    close();
	}

	/*@Override
	protected Point getInitialSize() {
	    return new Point(InterfaceSize.ITEM_INFO_SIZE+30, composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+50);
	}*/
	
	private void setDimStat(int str, Label stat, Text text) throws StatusTypeMismatch
	{
		int name=0;
		switch(currentDimStat)
		{
		case NONE:
			name=StatList.NONE;
			break;
		case STR:
			name=StatList.STR;
			break;
		case INT:
			name=StatList.INT;
			break;
		case STA:
			name=StatList.STA;
			break;
		case WILL:
			name=StatList.WILL;
			break;
		}
		if(currentDimStat==Dimension_stat.NONE){
			stat.setText(" 차원 스탯 없음");
			stat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			text.setText("");
		}
		
		else{
			stat.setText(" 차원의 "+StatusAndName.getStatHash().get(name));
			stat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			
			String strength = String.valueOf(str);
			text.setText(strength);
			text.addVerifyListener(new TextInputOnlyInteger());
			text.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
		}
	}

	private void setIgnStat(int phyIgnStat, Label phyStat, Text phyText, int magIgnStat, Label magStat, Text magText) throws StatusTypeMismatch
	{
		phyStat.setText("+"+currentReinforce+" 강화: 방어무시 물리 공격력 +");
		phyStat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		String strength = String.valueOf(phyIgnStat);
		phyText.setText(strength);
		phyText.addVerifyListener(new TextInputOnlyInteger());
		phyText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		magStat.setText("+"+currentReinforce+" 강화: 방어무시 마법 공격력 +");
		magStat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		strength = String.valueOf(magIgnStat);
		magText.setText(strength);
		magText.addVerifyListener(new TextInputOnlyInteger());
		magText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	private void setAidStat(int aidStat, Label aidStatLabel, Text text) throws StatusTypeMismatch
	{
		aidStatLabel.setText(" 힘,지능,체력,정신력 +");
		aidStatLabel.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		String strength = String.valueOf(aidStat);
		text.setText(strength);
		text.addVerifyListener(new TextInputOnlyInteger());
		text.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	private void setEarringStat(int[] aidStat, Label[] aidStatLabel, Text[] text) throws StatusTypeMismatch
	{
		aidStatLabel[0].setText(StatusAndName.getStatHash().get(StatList.WEP_PHY));
		aidStatLabel[1].setText(StatusAndName.getStatHash().get(StatList.WEP_MAG));
		aidStatLabel[2].setText(StatusAndName.getStatHash().get(StatList.WEP_IND));
		
		for(int i=0; i<3; i++){
			aidStatLabel[i].setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
			
			String strength = String.valueOf(aidStat[i]);
			text[i].setText(strength);
			text[i].addVerifyListener(new TextInputOnlyInteger());
			text[i].setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		}
	}
	
	private void setRefStat(int refStat, Label refStatLabel, Text refText)
	{
		refStatLabel.setText("+"+currentReforge+" 제련: 독립 공격력 +");
		refStatLabel.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
		
		String strength = String.valueOf(refStat);
		refText.setText(strength);
		refText.addVerifyListener(new TextInputOnlyInteger());
		refText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
	}
	
	private void setItemName(Label name)
	{
		String temp = item.getName();
		if(item instanceof Equipment && currentReinforce!=0) temp = "+"+currentReinforce+" "+temp;
		name.setText(temp);
		name.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 4, 1));
	}
}
