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
		Status charStat = new Status();
		Status.PublicStatus publicStat = charStat.new PublicStatus();
		
		try{
			publicStat.setElementStat("화속", 250, true);
			publicStat.setElementStat("수속", 120, true);
			publicStat.setElementStat("암속", 300, false);
		
			publicStat.setStat("무기물공", 988+270+20+40+50+110+34);
			publicStat.setStat("물리방무", 500);
			publicStat.setStat("독공", 2350-449);
			publicStat.setStat("재련독공", 449);
		
			publicStat.setStat("고정물방깍", 12000);
			publicStat.setDoubleStat("퍼물방깍_스킬", 27.0);
			publicStat.setStat("퍼물방깍_템", 10);
		
			publicStat.setStat("힘", 2780);
			publicStat.setStat("힘뻥", 10);
			publicStat.setStat("증뎀", 60);
			publicStat.setStat("크증뎀", 30);
			publicStat.setDoubleStat("스증", 10.0);
			publicStat.setStat("추뎀", 35);
		
			publicStat.setDoubleStat("증뎀버프", 224);
			publicStat.setDoubleStat("크증뎀버프", 1);
			publicStat.setDoubleStat("증뎀버프", 224);
		
			publicStat.setStat("화속추", 1);
			publicStat.setStat("수속추", 2);
			publicStat.setStat("명속추", 3);
			publicStat.setStat("암속추", 4);
		
			publicStat.setStat("화속깍", 20);
			publicStat.setStat("수속깍", 20);
			publicStat.setStat("명속깍", 20);
			publicStat.setStat("암속깍", 20);
			publicStat.setStat("투함포항", 12);
			
			publicStat.setDoubleStat("물크", 80);
			publicStat.setDoubleStat("백물크", 50);
			publicStat.setDoubleStat("크리저항", 5);
			publicStat.setDoubleStat("물리마스터리", 20);
			publicStat.setDoubleStat("독공뻥", 15);
			publicStat.setDoubleStat("물리마스터리2", 1);
			
			charStat.setStatus(publicStat);
			Char character = new Char(charStat, 86);
			
			Mon object = new Mon(new Status());
			object.setBooleanStat(Monster_StatList.BACKATK, true);
			object.setBooleanStat(Monster_StatList.COUNTER, true);
			object.setStat(Monster_StatList.DIFFICULTY, DefenceIgnorePenalty.ANTON_NOR);
			object.setStat(Monster_StatList.FIRE_RESIST, 40);
			object.setStat(Monster_StatList.DEFENSIVE_PHY, 135000);
			object.setStat(Monster_StatList.LEVEL, 115);
			object.setStat(Monster_StatList.TYPE, MonsterType.BOSS);
			
			StatusUI_Test.openStatusUI(10000, 100000, 1000, object, character, 1);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
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
}

class LabelAndCheckButton extends LabelAndInput
{
	public LabelAndCheckButton(Composite parent, String LabelString, String ButtonString)
	{
		super(parent, LabelString);
		
		input = new Button(composite, SWT.CHECK);
		GridData buttonData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		buttonData.widthHint=20;
		buttonData.heightHint=20;
		((Button)input).setLayoutData(buttonData);
		((Button)input).setText(ButtonString);
	}
	
	public void setInputEnable(boolean bool) { ((Button)input).setEnabled(bool); }
	public void setTextString(String str) { ((Button)input).setText(str);}
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

        if(!isFloat)
            e.doit = false;
    }
}

class StatusUI_Test{
	
	public static void openStatusUI(int skillPercent, int skillFixedValue, int usedIndepValue, Monster object, Char character, int mode) throws StatusTypeMismatch
	{	
        Display display = new Display();

        Shell shell = new Shell(display);
        shell.setText("스탯 계산");
        
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
        
        WholeStatus wholeStat = new WholeStatus(shell, character.finalStatus); 
      
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
	Status.PublicStatus publicStat;
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
		publicStat = stat.new PublicStatus();
		try{
			infoStatusComposite = new Composite(parent, SWT.BORDER);
			infoStatusComposite.setLayout(new GridLayout(2, true));
			infoStatusText = new LabelAndInput[Status.infoStatNum];
			GridData statusGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
			
			int i;
			TextInputOnlyNumbers floatFormat = new TextInputOnlyNumbers();
			for(i=0; i<Status.infoStatNum; i++){
				infoStatusText[i] = new LabelAndText(infoStatusComposite, Status.infoStatOrder[i], "");
				infoStatusText[i].composite.setLayoutData(statusGridData);
				((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
				if(i==6)																					//독공
					infoStatusText[i].composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			}
			
			for(i=0; i<4; i++) infoStatusText[i].setTextString(String.valueOf(publicStat.getStat(Status.infoStatOrder[i])));		//힘지체정
			//infoStatusText[4].setTextString(String.valueOf(Calculator.physicalATK(this)));						//TODO, 마을물공
			infoStatusText[4].setInputEnable(false);
			//infoStatusText[5].setTextString(String.valueOf(Calculator.physicalATK(this)));						//TODO, 마을물공
			infoStatusText[5].setInputEnable(false);
			for(i=6; i<Status.infoStatNum; i++) infoStatusText[i].setTextString(String.valueOf(publicStat.getStat(Status.infoStatOrder[i])));
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
				if(i==5 || i==4) continue;
				publicStat.setDoubleStat(Status.infoStatOrder[i], Double.parseDouble(((Text)infoStatusText[i].input).getText()));
			}
			stat.setStatus(publicStat);
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
		publicStat = stat.new PublicStatus();
		try{
			infoStatusComposite = new Composite(parent, SWT.BORDER);
			infoStatusComposite.setLayout(new GridLayout(2, true));
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
				}
				else
				{
					infoStatusText[i] = new LabelAndText(infoStatusComposite, Status.nonInfoStatOrder[i], "");
					infoStatusText[i].composite.setLayoutData(statusGridData);
					((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
					infoStatusText[i].setTextString(String.valueOf(publicStat.getStat(Status.nonInfoStatOrder[i])));
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
					publicStat.setElementStat("화속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("수속성부여"))
					publicStat.setElementStat("수속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("명속성부여"))
					publicStat.setElementStat("명속성", ((Button)infoStatusText[i].input).getSelection());
				
				else if(Status.nonInfoStatOrder[i].equals("암속성부여"))
					publicStat.setElementStat("암속성", ((Button)infoStatusText[i].input).getSelection());
				
				else
					publicStat.setDoubleStat(Status.nonInfoStatOrder[i], Double.parseDouble(((Text) infoStatusText[i].input).getText()));
			}
			stat.setStatus(publicStat);
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