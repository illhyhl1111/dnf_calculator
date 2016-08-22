package dnf_calculator;
import org.eclipse.swt.SWT;
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
			publicStat.setDoubleStat("물공마스터리", 20);
			publicStat.setDoubleStat("독공뻥", 15);
			publicStat.setDoubleStat("물공마스터리2", 1);
			
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
			
			StatusUI.openStatusUI(10000, 100000, 1000, object, character, 1);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
}

class LabelAndText
{
	Label label;
	Text text;
	Composite composite;
	
	public LabelAndText(Composite parent, String LabelString, String TextString)
	{
		composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns=2;
		composite.setLayout(compositeLayout);
		
		label = new Label(composite, SWT.NONE);
		GridData labelData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		labelData.grabExcessHorizontalSpace=true;
		labelData.minimumWidth=80;
		labelData.heightHint=20;
		label.setLayoutData(labelData);
		label.setAlignment(SWT.RIGHT);
		
		text = new Text(composite, SWT.NONE);
		GridData textData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		textData.grabExcessHorizontalSpace=true;
		textData.minimumWidth=80;
		textData.heightHint=20;;
		text.setLayoutData(textData);
		
		label.setText(LabelString);
		text.setText(TextString);
		//text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        //text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setTextEnable(boolean bool) { text.setEnabled(bool); }
	public void setTextString(String str) { text.setText(str);}
	public void setLabelString(String str) { label.setText(str);}
}

class StatusUI{
	
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
        
        Composite statComposite = new Composite(shell, SWT.BORDER);
        RowData statCompoiteGridData = new RowData();
	    statComposite.setLayoutData(statCompoiteGridData);
	    statComposite.setLayout(new GridLayout(1, false));
        
        LabelAndText[] statusDisplay = new LabelAndText[10];
       
        statusDisplay[0] = new LabelAndText(statComposite, "힘", "2000");
        statusDisplay[1] = new LabelAndText(statComposite, "힘", "2001");
        statusDisplay[2] = new LabelAndText(statComposite, "힘", "2002");
        statusDisplay[3] = new LabelAndText(statComposite, "힘", "2003");
        statusDisplay[4] = new LabelAndText(statComposite, "힘", "2004");
        statusDisplay[5] = new LabelAndText(statComposite, "힘", "2005");
        statusDisplay[6] = new LabelAndText(statComposite, "힘", "2006");
        statusDisplay[7] = new LabelAndText(statComposite, "힘", "2007");
        statusDisplay[8] = new LabelAndText(statComposite, "힘", "2008");
        statusDisplay[9] = new LabelAndText(statComposite, "힘", "2009");
      
        Button button = new Button(shell, SWT.PUSH);
	    button.setText("Press Me");
	    
	    LabelAndText[] damageDisplay = new LabelAndText[4];
	    damageDisplay[0] = new LabelAndText(shell, "물리퍼센트데미지 -", "");
	    damageDisplay[0].setTextEnable(false);
	    damageDisplay[1] = new LabelAndText(shell, "물리고정데미지 -", "");
	    damageDisplay[1].setTextEnable(false);
	    damageDisplay[2] = new LabelAndText(shell, "마법퍼센트데미지 -", "");
	    damageDisplay[2].setTextEnable(false);
	    damageDisplay[3] = new LabelAndText(shell, "마법고정데미지 -", "");
	    damageDisplay[3].setTextEnable(false);
	    
	    button.setLayoutData(new RowData(100, 40));
	    button.addSelectionListener(new SelectionAdapter() {
	          @Override
	          public void widgetSelected(SelectionEvent e) {
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
