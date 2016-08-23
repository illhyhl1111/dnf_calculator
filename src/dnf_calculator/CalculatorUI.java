package dnf_calculator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class CalculatorUI {
	public static void main(String[] args) {

		try{
			GetItem.readFile();
			Characters character = new Characters(86);
			character.equip(GetItem.getEquipment("타란튤라 상의"));
			character.equip(GetItem.getEquipment("킹바분 하의"));
			character.equip(GetItem.getEquipment("골리앗 버드이터 어깨"));
			character.equip(GetItem.getEquipment("로즈헤어 벨트"));
			character.equip(GetItem.getEquipment("인디언 오너멘탈 신발"));
			character.equip(GetItem.getEquipment("탐식의 증적"));
			
			Mon object = new Mon(new Status());
			object.setBooleanStat(Monster_StatList.BACKATK, true);
			object.setBooleanStat(Monster_StatList.COUNTER, true);
			object.setStat(Monster_StatList.DIFFICULTY, DefenceIgnorePenalty.ANTON_NOR);
			object.setStat(Monster_StatList.FIRE_RESIST, 40);
			object.setStat(Monster_StatList.DEFENSIVE_PHY, 135000);
			object.setStat(Monster_StatList.DEFENSIVE_MAG, 135000);
			object.setStat(Monster_StatList.LEVEL, 115);
			object.setStat(Monster_StatList.TYPE, MonsterType.BOSS);
			
			StatusUI_Test.openStatusUI(10000, 100000, 1000, object, character, 1, false);
			StatusUI_Test.openStatusUI(10000, 100000, 1000, object, character, 1, true);
		}
		catch(StatusTypeMismatch | ItemFileNotReaded | ItemFileNotFounded e)
		{
			e.printStackTrace();
		}
	}
}

abstract class LabelAndInput
{
	Label label;
	Composite composite;
	Widget input;
	
	public LabelAndInput(Composite parent, String LabelString)
	{
		composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns=2;
		composite.setLayout(compositeLayout);
		
		label = new Label(composite, SWT.NONE);
		GridData labelData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		labelData.grabExcessHorizontalSpace=true;
		labelData.minimumWidth=80;
		labelData.heightHint=20;
		label.setLayoutData(labelData);
		label.setText(LabelString);
	}
	public void setLabelString(String str) { label.setText(str);}
	public abstract void setInputEnable(boolean bool);
	public abstract void setTextString(String str);
	public void setGridLayout(GridLayout layout)
	{
		composite.setLayout(layout);
	}
	public void setLabelData(GridData gridData)
	{
		label.setLayoutData(gridData);
	}
}

class LabelAndText extends LabelAndInput
{
	
	public LabelAndText(Composite parent, String LabelString, String TextString)
	{
		super(parent, LabelString);
		
		input = new Text(composite, SWT.RIGHT);
		GridData textData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		textData.grabExcessHorizontalSpace=true;
		textData.minimumWidth=80;
		textData.heightHint=20;;
		((Text)input).setLayoutData(textData);
		((Text)input).setText(TextString);
		//text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        //text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setInputEnable(boolean bool) { ((Text)input).setEnabled(bool); }
	public void setTextString(String str) { ((Text)input).setText(str);}
	public void setTextData(GridData gridData)
	{
		((Text)input).setLayoutData(gridData);
	}
}

class LabelAndCheckButton extends LabelAndInput
{
	public LabelAndCheckButton(Composite parent, String LabelString, String ButtonString)
	{
		super(parent, LabelString);
		
		input = new Button(composite, SWT.CHECK);
		GridData buttonData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		buttonData.heightHint=20;
		((Button)input).setLayoutData(buttonData);
		((Button)input).setText(ButtonString);
	}
	
	public void setInputEnable(boolean bool) { ((Button)input).setEnabled(bool); }
	public void setTextString(String str) { ((Button)input).setText(str);}
	public void setButtonCheck(boolean bool) { ((Button)input).setSelection(bool);}
}

class TextInputOnlyNumbers implements VerifyListener
{
	@Override
    public void verifyText(VerifyEvent e) {

        Text text = (Text)e.getSource();

        // get old text and create new text by using the VerifyEvent.text
        final String oldS = text.getText();
        String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

        boolean isFloat = true;
        try
        {
            Float.parseFloat(newS);
        }
        catch(NumberFormatException ex)
        {
            isFloat = false;
        }

        //System.out.println(newS);

        if(!isFloat || Character.isLetter(e.character))
            e.doit = false;
        	
        if(newS.isEmpty() || newS.equals("-")){
        	e.doit = true;
        }
    }
}

class StatusUI_Test{
	
	public static void openStatusUI(int skillPercent, int skillFixedValue, int usedIndepValue,
			Monster object, Characters character, int mode, boolean dungeon) throws StatusTypeMismatch
	{	
        Display display = new Display();

        Shell shell = new Shell(display);
        if(dungeon) shell.setText("스탯 계산 - 던전 스탯");
        else shell.setText("스탯 계산 - 마을 스탯");
        
        // the layout manager handle the layout
        // of the widgets in the container
        RowLayout shellLayout = new RowLayout();
        shellLayout.type=SWT.VERTICAL;
        shellLayout.marginHeight=10;
        shellLayout.spacing=10;
        shellLayout.justify=false;											// 균등하게 분포
        shellLayout.wrap=false;											// x축 크기 줄일 시 y축으로 내려가는가?
        shellLayout.pack=true;	
        shell.setLayout(shellLayout);
        
        WholeStatus wholeStat;
        if(dungeon) wholeStat = new WholeStatus(shell, character.dungeonStatus);
        else wholeStat = new WholeStatus(shell, character.villageStatus);
      
        Button button = new Button(shell, SWT.PUSH);
	    button.setText("Press Me");
	    
	    LabelAndText[] damageDisplay = new LabelAndText[4];
	    damageDisplay[0] = new LabelAndText(shell, "물리퍼센트데미지 -", "");
	    damageDisplay[0].setInputEnable(false);
	    damageDisplay[1] = new LabelAndText(shell, "물리고정데미지 -", "");
	    damageDisplay[1].setInputEnable(false);
	    damageDisplay[2] = new LabelAndText(shell, "마법퍼센트데미지 -", "");
	    damageDisplay[2].setInputEnable(false);
	    damageDisplay[3] = new LabelAndText(shell, "마법고정데미지 -", "");
	    damageDisplay[3].setInputEnable(false);
	    
	    button.setLayoutData(new RowData(100, 40));
	    button.addSelectionListener(new SelectionAdapter() {
	          @Override
	          public void widgetSelected(SelectionEvent e) {
	        	  wholeStat.setStatus();
	              damageDisplay[0].setTextString(String.valueOf(Calculator.percentDamage_physical(skillPercent, object, character, 1)));
	              damageDisplay[1].setTextString(String.valueOf(Calculator.fixedDamage_physical(skillFixedValue, usedIndepValue, object, character, 1)));
	              damageDisplay[2].setTextString(String.valueOf(Calculator.percentDamage_magical(skillPercent, object, character, 1)));
	              damageDisplay[3].setTextString(String.valueOf(Calculator.fixedDamage_magical(skillFixedValue, usedIndepValue, object, character, 1)));
	          }
	        }); 
	    
        
        // set widgets size to their preferred size
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}

class WholeStatus
{
	InfoStatus infoStat;
	NonInfoStatus nonInfoStat;
	Composite wholeStatusComposite;
	public WholeStatus(Composite parent, Status stat)
	{
		wholeStatusComposite = new Composite(parent, SWT.BORDER);
		RowLayout wholeLayout = new RowLayout();
		wholeLayout.spacing=10;
		wholeLayout.wrap=false;
		wholeLayout.pack=true;
		wholeStatusComposite.setLayout(wholeLayout);
		
		infoStat = new InfoStatus(wholeStatusComposite, stat);
		nonInfoStat = new NonInfoStatus(wholeStatusComposite, stat);
	}
	
	public Composite getComposite() {return wholeStatusComposite;}
	public void setStatus()
	{
		infoStat.setStatus();
		nonInfoStat.setStatus();
	}
}


abstract class StatusUI
{
	Status stat;
	Composite infoStatusComposite;
	LabelAndInput[] infoStatusText;
	public abstract void setStatus(); 
	public Composite getComposite() {return infoStatusComposite;}
}

class InfoStatus extends StatusUI
{
	public InfoStatus(Composite parent, Status stat)
	{
		this.stat=stat;
		try{
			infoStatusComposite = new Composite(parent, SWT.BORDER);
			GridLayout infoLayout = new GridLayout(2, true);
			infoLayout.horizontalSpacing=5;
			infoStatusComposite.setLayout(infoLayout);
			infoStatusText = new LabelAndInput[Status.infoStatNum];
			GridData statusGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
			
			int i;
			TextInputOnlyNumbers floatFormat = new TextInputOnlyNumbers();
			for(i=0; i<Status.infoStatNum; i++){
				infoStatusText[i] = new LabelAndText(infoStatusComposite, Status.infoStatOrder[i], "");
				infoStatusText[i].composite.setLayoutData(statusGridData);
				((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
				if(Status.infoStatOrder[i].equals("독립공격")){																//독공
					infoStatusText[i].composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
					GridData textData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
					textData.grabExcessHorizontalSpace=true;
					textData.minimumWidth=80;
					textData.heightHint=20;;
					((LabelAndText)infoStatusText[i]).setTextData(textData);
				}
			}
			
			for(i=0; i<Status.infoStatNum; i++){
				if(Status.infoStatOrder[i].equals("마을물공")){
					//infoStatusText[i].setTextString(String.valueOf(Calculator.physicalATK(this)));						//TODO, 마을물공
					infoStatusText[4].setInputEnable(false);
				}
				else if(Status.infoStatOrder[i].equals("마을마공")){
					//infoStatusText[5].setTextString(String.valueOf(Calculator.physicalATK(this)));						//TODO, 마을물공
					infoStatusText[i].setInputEnable(false);
				}
				else{	
					String temp = String.valueOf(stat.getStat(Status.infoStatOrder[i]));
					if(temp.contains(".0")) temp=temp.substring(0, temp.length()-2);
					infoStatusText[i].setTextString(temp);
				}
			}
			
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	
	public void setStatus()
	{
		try{
			for(int i=0; i<Status.infoStatNum; i++)
			{
				if(Status.infoStatOrder[i].equals("마을물공") || Status.infoStatOrder[i].equals("마을마공")) continue;
				String temp = ((Text) infoStatusText[i].input).getText();
				if(temp.isEmpty() || temp.equals("-")) stat.setDoubleStat(Status.infoStatOrder[i], 0);
				else stat.setDoubleStat(Status.infoStatOrder[i], Double.parseDouble(temp));
			}
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			System.out.println("Parsing Error(to Double)");
			e.printStackTrace();
		}
	}
}

class NonInfoStatus extends StatusUI
{	
	public NonInfoStatus(Composite parent, Status stat)
	{
		this.stat=stat;
		try{
			infoStatusComposite = new Composite(parent, SWT.BORDER);
			GridLayout infoLayout = new GridLayout(2, true);
			infoLayout.verticalSpacing=0;
			infoStatusComposite.setLayout(infoLayout);
			infoStatusText = new LabelAndInput[Status.nonInfoStatNum];
			GridData statusGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
			
			int i;
			TextInputOnlyNumbers floatFormat = new TextInputOnlyNumbers();
			for(i=0; i<Status.nonInfoStatNum; i++){
				if(Status.nonInfoStatOrder[i].contains("속성부여"))
				{
					infoStatusText[i] = new LabelAndCheckButton(infoStatusComposite, Status.nonInfoStatOrder[i], "");
					infoStatusText[i].composite.setLayoutData(statusGridData);
					infoStatusText[i].setTextString("속성부여");
					
					if(Status.nonInfoStatOrder[i].equals("화속성부여"))
						((LabelAndCheckButton)infoStatusText[i]).setButtonCheck(stat.getEnabled("화속성"));
					if(Status.nonInfoStatOrder[i].equals("수속성부여"))
						((LabelAndCheckButton)infoStatusText[i]).setButtonCheck(stat.getEnabled("수속성"));
					if(Status.nonInfoStatOrder[i].equals("명속성부여"))
						((LabelAndCheckButton)infoStatusText[i]).setButtonCheck(stat.getEnabled("명속성"));
					if(Status.nonInfoStatOrder[i].equals("암속성부여"))
						((LabelAndCheckButton)infoStatusText[i]).setButtonCheck(stat.getEnabled("암속성"));
				}
				else
				{
					infoStatusText[i] = new LabelAndText(infoStatusComposite, Status.nonInfoStatOrder[i], "");
					infoStatusText[i].composite.setLayoutData(statusGridData);
					((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
					String temp = String.valueOf(stat.getStat(Status.nonInfoStatOrder[i]));
					if(temp.contains(".0")) temp=temp.substring(0, temp.length()-2);
					infoStatusText[i].setTextString(temp);
				}
			}
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	
	public void setStatus()
	{
		try{
			for(int i=0; i<Status.nonInfoStatNum; i++)
			{
				if(Status.nonInfoStatOrder[i].equals("화속성부여"))
					stat.setElementStat("화속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("수속성부여"))
					stat.setElementStat("수속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("명속성부여"))
					stat.setElementStat("명속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("암속성부여"))
					stat.setElementStat("암속성", ((Button)infoStatusText[i].input).getSelection());
				
				else{
					String temp = ((Text) infoStatusText[i].input).getText();
					if(temp.isEmpty() || temp.equals("-")) stat.setDoubleStat(Status.nonInfoStatOrder[i], 0);
					else stat.setDoubleStat(Status.nonInfoStatOrder[i], Double.parseDouble(temp));
				}
			}
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			System.out.println("Parsing Error(to Double)");
			e.printStackTrace();
		}
	}
}