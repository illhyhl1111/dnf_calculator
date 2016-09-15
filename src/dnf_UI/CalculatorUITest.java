package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.DefenceIgnorePenalty;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.Calculator;
import dnf_calculator.Status;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_infomation.GetDictionary;

public class CalculatorUITest {
	public static void main(String[] args) {

		try{

			GetDictionary.readFile();
			Characters character = new Characters(86, JobList.LAUNCHER_F, "명속은거들뿐");

			Display display = new Display();

	        Shell shell = new Shell(display);
	        shell.setText("인포창");
	        shell.setLayout(new FillLayout());
			
	        /*
			character.equip(character.userItemList.getEquipment("우요의 황금 캐넌"));
			
			character.equip(character.userItemList.getEquipment("타란튤라 상의"));
			character.equip(character.userItemList.getEquipment("킹바분 하의"));
			character.equip(character.userItemList.getEquipment("골리앗 버드이터 어깨"));
			character.equip(character.userItemList.getEquipment("로즈헤어 벨트"));
			character.equip(character.userItemList.getEquipment("인디언 오너멘탈 신발"));
			character.equip(character.userItemList.getEquipment("탐식의 증적"));
			
			character.equip(character.userItemList.getEquipment("무한한 탐식의 형상"));
			character.equip(character.userItemList.getEquipment("필리르 - 꺼지지 않는 화염"));
			character.equip(character.userItemList.getEquipment("무한한 탐식의 잔재"));
			character.equip(character.userItemList.getEquipment("탐식의 근원"));
			character.equip(character.userItemList.getEquipment("임시귀걸이"));*/
			
			Monster object = new Monster(new Status());
			object.setBooleanStat(Monster_StatList.BACKATK, true);
			object.setBooleanStat(Monster_StatList.COUNTER, true);
			object.setStat(Monster_StatList.DIFFICULTY, DefenceIgnorePenalty.ANTON_NOR);
			object.setStat(Monster_StatList.FIRE_RESIST, 40);
			object.setStat(Monster_StatList.DEFENSIVE_PHY, 135000);
			object.setStat(Monster_StatList.DEFENSIVE_MAG, 135000);
			object.setStat(Monster_StatList.LEVEL, 115);
			object.setStat(Monster_StatList.TYPE, MonsterType.BOSS);
			
			VillageUI villageUI = new VillageUI(shell, character);
	        
	        display.addFilter(SWT.KeyDown, new Listener() {
	            @Override
	            public void handleEvent(Event event) {
	                char c = event.character;
	                //System.out.println(c);
	                if(c=='i'){
	                	if(villageUI.getVault().getShell()==null)
	                		villageUI.getVault().open();
	                	else
	                		villageUI.getVault().close();
	                }
	            }
	        });
	        
	        shell.pack();
	        shell.open();
	        
	        shell.addShellListener(new ShellAdapter(){
				@Override
				public void shellClosed(ShellEvent e) {
					villageUI.save(character);
				}
	        });
	        
	        while (!shell.isDisposed()) {
	            if (!display.readAndDispatch())
	                display.sleep();
	        }
	        display.dispose();
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
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
        wholeStat = new WholeStatus(shell, character, dungeon);
      
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