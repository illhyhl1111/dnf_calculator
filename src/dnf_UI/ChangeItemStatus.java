package dnf_UI;

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
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Weapon;
import dnf_infomation.GetItemDictionary;

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
	
	public ChangeItemStatus(Shell parent, Item item, boolean hasSet)
	{
		super(parent);
		this.item=item;
		this.hasSet=hasSet;
		try {
			originalItem=GetItemDictionary.getEquipment(item.getName());
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
		Label aidStatLabel;
		
		Text dimStatText;
		Text phyIgnStatText;
		Text magIgnStatText;
		Text aidStatText;
	
		final StatusAndName dimStat;
		final StatusAndName phyIgnStat;
		final StatusAndName magIgnStat;
		final StatusAndName aidStat;
		
		Group selectModeComposite;
		Group reinforceComposite;
		final Spinner reinforce;
		if(item instanceof Equipment)
		{	
			currentDimStat = ((Equipment)item).getDimentionStat();
			currentReinforce = ((Equipment)item).getReinforce();
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
		}
		else {
			reinforce=null;
			selectModeComposite=null;
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
			Label tempLabel=null;
			Label tempLabel2=null;
			Text tempText=null;
			Text tempText2=null;
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
					GetItemDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
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
					GetItemDictionary.getReinforceInfo_phy(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
				}
				tempText.setLayoutData(textData);
				
				tempLabel2 = new Label(composite, SWT.WRAP);
				tempLabel2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				
				tempText2 = new Text(composite, SWT.NONE);
				tempText2.setEditable(false);
				try{
					GetItemDictionary.getReinforceInfo_mag(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				}catch(UnknownInformationException e){
					tempText.setEditable(true);
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
				tempStat = item.vStat.statList.get(item.getAidStatIndex());
				tempLabel = new Label(composite, SWT.WRAP);
				tempLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 1, 1));
				
				tempText = new Text(composite, SWT.NONE);
				tempText.setEditable(false);
				try{
					GetItemDictionary.getReinforceAidInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
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
				
			} catch(IndexOutOfBoundsException e)
			{
				tempLabel=null;
				tempText=null;
				tempStat=null;
			} finally{
				aidStat=tempStat;
				aidStatLabel=tempLabel;
				aidStatText=tempText;
			}
			
			Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));	
			
			int index = item.getItemStatIndex();
			Iterator<StatusAndName> maxS = originalItem.vStat.statList.subList(index, item.vStat.statList.size()).iterator();
			List<StatusAndName> itemStatList = item.vStat.statList.subList(index, item.vStat.statList.size());
			for(StatusAndName s : itemStatList) {
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(index++, setText(composite, s, maxS.next()));
				vStatEntry.add(entry);
			}
			
			if(!item.dStat.statList.isEmpty())
			{
				label = new Label(composite, SWT.WRAP);
				label.setText("\n――――――던전 입장 시 적용――――――\n\n");
				label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1));
				
				maxS = originalItem.dStat.statList.iterator();
				int dIndex=0;
				for(StatusAndName s : item.dStat.statList){
					Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(dIndex++, setText(composite, s, maxS.next()));
					dStatEntry.add(entry);
				}
			}
			
			if(item instanceof Equipment)
			{
				reinforce.addModifyListener(event-> {
			    	currentReinforce=reinforce.getSelection();
			    	setItemName(name);
			    	
			    	if(dimStat!=null){
				    	try {
							setDimStat(GetItemDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level), dimStatLabel, dimStatText);
							dimStatText.setEditable(false);
						} catch (UnknownInformationException e){
							dimStatText.setEditable(true);
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
						}
			    	}
			    	
			    	if(phyIgnStat!=null){
				    	try {
				    		int phyStat=GetItemDictionary.getReinforceInfo_phy(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
				    		int magStat=GetItemDictionary.getReinforceInfo_mag(currentReinforce, item.getRarity(), ((Equipment)item).level, ((Weapon)item).weaponType);
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
							setAidStat(GetItemDictionary.getReinforceAidInfo(currentReinforce, item.getRarity(), ((Equipment)item).level), aidStatLabel, aidStatText);
							aidStatText.setEditable(false);
						} catch (UnknownInformationException e){
							aidStatText.setEditable(true);
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
						
						int str = GetItemDictionary.getDimensionInfo(currentReinforce, item.getRarity(), ((Equipment)item).level);
						setDimStat(str, dimStatLabel, dimStatText);
						dimStatText.setEditable(false);
					}
					catch(UnknownInformationException e)
					{
						dimStatText.setEditable(true);
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
		Label stat2;
		
		strength = String.valueOf(s.stat.getStatToDouble());
		maxStrength = String.valueOf(maxS.stat.getStatToDouble());
		if(s.stat instanceof ElementInfo && maxStrength.equals("0.0")){
			statNum=null;
			enable=null;
		}
		else{
			if(strength.contains(".0")) strength=strength.substring(0, strength.length()-2);
			if(maxStrength.contains(".0")) maxStrength=maxStrength.substring(0, maxStrength.length()-2);
			
			statName  = new Label(itemInfo, SWT.NONE);
			statName.setText(StatusAndName.getStatHash().get(s.name));
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
				Button enable2 = new Button(itemInfo, SWT.CHECK);
				enable2.setText("활성화");
				enable2.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
				
				enable2.setSelection(true);
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
		
		return new Wrapper(statNum, enable);
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
		}
		
		for(Entry<Integer, Wrapper> e : vStatEntry)
			if(e.getValue()!=null){
				if(e.getValue().hasButton==false){
					String value = e.getValue().getText().getText();
					if(value==null || value.isEmpty()) item.vStat.changeStat(e.getKey(), 0, true);
					else item.vStat.changeStat(e.getKey(), Double.valueOf(value), true);
				}
				else{
					String value = e.getValue().getText().getText();
					if(value==null || value.isEmpty()) item.vStat.changeStat(e.getKey(), 0, e.getValue().getButton().getSelection());
					else item.vStat.changeStat(e.getKey(), Double.valueOf(value), e.getValue().getButton().getSelection());
				}
			}
				
		for(Entry<Integer, Wrapper> e : dStatEntry)
			if(e.getValue()!=null){
				if(e.getValue().hasButton==false){
					String value = e.getValue().getText().getText();
					if(value==null || value.isEmpty()) item.dStat.changeStat(e.getKey(), 0, true);
					else item.dStat.changeStat(e.getKey(), Double.valueOf(value), true);
				}
				else{
					String value = e.getValue().getText().getText();
					if(value==null || value.isEmpty()) item.dStat.changeStat(e.getKey(), 0, e.getValue().getButton().getSelection());
					else item.dStat.changeStat(e.getKey(), Double.valueOf(value), e.getValue().getButton().getSelection());
				}
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
			text.addVerifyListener(new TextInputOnlyNumbers());
			text.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
		}
	}

	private void setIgnStat(int phyIgnStat, Label phyStat, Text phyText, int magIgnStat, Label magStat, Text magText) throws StatusTypeMismatch
	{
		phyStat.setText(StatusAndName.getStatHash().get(StatList.WEP_NODEF_PHY));
		phyStat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		String strength = String.valueOf(phyIgnStat);
		phyText.setText(strength);
		phyText.addVerifyListener(new TextInputOnlyNumbers());
		phyText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		magStat.setText(StatusAndName.getStatHash().get(StatList.WEP_NODEF_MAG));
		magStat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		strength = String.valueOf(magIgnStat);
		magText.setText(strength);
		magText.addVerifyListener(new TextInputOnlyNumbers());
		magText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	private void setAidStat(int aidStat, Label aidStatLabel, Text text) throws StatusTypeMismatch
	{
		aidStatLabel.setText(" 힘,지능,체력,정신력 +");
		aidStatLabel.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		
		String strength = String.valueOf(aidStat);
		text.setText(strength);
		text.addVerifyListener(new TextInputOnlyNumbers());
		text.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	private void setItemName(Label name)
	{
		String temp = item.getName();
		if(item instanceof Equipment && currentReinforce!=0) temp = "+"+currentReinforce+" "+temp;
		name.setText(temp);
		name.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 4, 1));
	}
}