package dnf_UI;

import java.util.AbstractMap;
import java.util.HashMap;
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
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_class.SetOption;
import dnf_infomation.GetDictionary;
import dnf_infomation.ItemDictionary;

public class ChangeSetOptionStatus extends Dialog{
	public LinkedList<SetOption> setOption;
	private LinkedList<SetOption> originalSet;
	private Composite composite;
	private HashMap<Integer, LinkedList<Entry<Integer, Wrapper>>> vStatEntry;		//순서대로 세트숫자, 해당세트의 각 스탯, 해당스탯, 해당스텟의 변경사항
	private HashMap<Integer, LinkedList<Entry<Integer, Wrapper>>> dStatEntry;
	ItemDictionary itemDictionary;
	
	public ChangeSetOptionStatus(Shell parent, SetName setName, ItemDictionary itemDictionary)
	{
		super(parent);
		this.itemDictionary=itemDictionary;
		try {
			this.setOption=itemDictionary.getSetOptions(setName);
			originalSet=GetDictionary.getSetOptions(setName);
		} catch (ItemFileNotFounded e1) {
			e1.printStackTrace();
		} catch (ItemFileNotReaded e) {
			e.printStackTrace();
		}
		vStatEntry = new HashMap<Integer, LinkedList<Entry<Integer, Wrapper>>>();
		dStatEntry = new HashMap<Integer, LinkedList<Entry<Integer, Wrapper>>>();
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		
		composite = new Composite(content, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.verticalSpacing=3;
		composite.setLayout(layout);
		
		Label option;
		
		try
		{
			Iterator<SetOption> iterator = originalSet.iterator();
			
			for(SetOption set : setOption)													//해당 숫자의 세트옵션(3세트옵, 5세트옵, ..)
			{
				int requireNum=set.requireNum;												//필요 세트 숫자
				SetOption set2= iterator.next();											//비교 세트옵션
				LinkedList<Entry<Integer, Wrapper>> vStatList = new LinkedList<Entry<Integer, Wrapper>>();			//HashMap에 변경정보 저장
				vStatEntry.put(requireNum, vStatList);
				
				option = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
				
				option = new Label(composite, SWT.WRAP);
				option.setText("["+set.requireNum+"]세트 효과");
				option.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_GREEN));
				option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
				
				Iterator<StatusAndName> maxS = set2.vStat.statList.iterator();
				for(StatusAndName statName : set.vStat.statList){
					Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(statName.name, setText(composite, statName, maxS.next()));
					vStatList.add(entry);
				}
				if(!set.dStat.statList.isEmpty() || !set.fStat.explanation.isEmpty())
				{
					option = new Label(composite, SWT.WRAP);
					option.setText("\n――――――던전 입장 시 적용――――――\n\n");
					option.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1));
					
					LinkedList<Entry<Integer, Wrapper>> dStatList = new LinkedList<Entry<Integer, Wrapper>>();			//HashMap에 변경정보 저장
					dStatEntry.put(requireNum, dStatList);
					
					maxS = set2.dStat.statList.iterator();
					for(StatusAndName statName : set.dStat.statList){
						Entry<Integer, Wrapper> entry = new AbstractMap.SimpleEntry<Integer, Wrapper>(statName.name, setText(composite, statName, maxS.next()));
						dStatList.add(entry);
					}
					
					for(String s : set.fStat.explanation){
						option = new Label(composite, SWT.WRAP);
						option.setText(s);
						option.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1));
					}
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
	    newShell.setText("세트옵션 능력치 변경");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, "변경", true);
	    createButton(parent, IDialogConstants.CANCEL_ID, "취소", false);
	}
	
	@Override
	protected void okPressed() {
		for(SetOption s : setOption)													//각 세트옵션(3셋옵, 5셋옵)
		{
			for(Entry<Integer, Wrapper> e : vStatEntry.get(s.requireNum))				//for each - 해당 숫자에 해당되는 마을옵션리스트
				if(e.getValue()!=null)
					s.vStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), e.getValue().getButton().getSelection());
			
			for(Entry<Integer, Wrapper> e : dStatEntry.get(s.requireNum))				//for each - 해당 숫자에 해당되는 던전옵션리스트
				if(e.getValue()!=null)
					s.dStat.changeStat(e.getKey(), Double.valueOf(e.getValue().getText().getText()), e.getValue().getButton().getSelection());
		}
		
	    super.okPressed();
	}

	/*@Override
	protected Point getInitialSize() {
	    return new Point(InterfaceSize.ITEM_INFO_SIZE+30, composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+50);
	}*/
	
}