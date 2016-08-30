package dnf_UI;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_infomation.GetItemDictionary;

class Wrapper {
	private Text text;
	private Button button;
	    
    public Wrapper(Text text, Button button) {
       this.text=text;
       this.button = button;
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
	
	public ChangeItemStatus(Shell parent, Item item)
	{
		super(parent);
		this.item=item;
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
		
		composite = new Composite(content, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.verticalSpacing=3;
		composite.setLayout(layout);
		
		Label stat = new Label(composite, SWT.WRAP);
		String temp = item.getName();
		if(item instanceof Equipment && ((Equipment)item).reinforce!=0) temp = "+"+((Equipment)item).reinforce+" "+temp;
		stat.setText(temp);
		stat.setLayoutData(new GridData(SWT.LEFT, SWT.TOP,false, false, 4, 1));
		
		Label rarity = new Label(composite, SWT.WRAP);
		rarity.setText(item.getRarity().getName());
		rarity.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 4, 1));
		
		//Color nameColor = new Color(composite.getDisplay());
		switch(item.getRarity())
		{
		case EPIC:
			stat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			break;
		case UNIQUE:
			stat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			rarity.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			break;
		case LEGENDARY:
			stat.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
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
		
		stat = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		stat.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		
		try
		{
			Iterator<StatusAndName> maxS = originalItem.vStat.statList.iterator();
			for(StatusAndName s : item.vStat.statList){
				Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(s.name, setText(composite, s, maxS.next()));
				vStatEntry.add(entry);
			}
			
			if(!item.dStat.statList.isEmpty())
			{
				stat = new Label(composite, SWT.WRAP);
				stat.setText("\n――――――던전 입장 시 적용――――――\n\n");
				stat.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1));
				
				maxS = originalItem.dStat.statList.iterator();
				for(StatusAndName s : item.dStat.statList){
					Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(s.name, setText(composite, s, maxS.next()));
					dStatEntry.add(entry);
				}
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
			enable.setText("활성화");
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
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, "변경", true);
	    createButton(parent, IDialogConstants.CANCEL_ID, "취소", false);
	}
	
	@Override
	protected void okPressed() {
		for(Entry<Integer, Wrapper> e : vStatEntry)
			if(e.getValue()!=null)
				item.vStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), e.getValue().getButton().getSelection());
				
		for(Entry<Integer, Wrapper> e : dStatEntry)
			if(e.getValue()!=null)
				item.dStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), e.getValue().getButton().getSelection());
	    super.okPressed();
	}

	/*@Override
	protected Point getInitialSize() {
	    return new Point(InterfaceSize.ITEM_INFO_SIZE+30, composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+50);
	}*/
	
}
