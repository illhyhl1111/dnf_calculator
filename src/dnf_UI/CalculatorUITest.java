package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.JobList;
import dnf_class.Characters;
import dnf_infomation.GetDictionary;

public class CalculatorUITest {
	public static void main(String[] args) {
		final boolean openSleak = false;
		Display display;
		
		if(openSleak)
		{
			DeviceData data = new DeviceData();
			data.tracking = true;
		    display = new Display(data);
		    Sleak sleak = new Sleak();
		    sleak.open();
		}
		else display = new Display(); 
		
		GetDictionary.readFile(JobList.LAUNCHER_F);
		Characters character = new Characters(90, JobList.LAUNCHER_F, "명속은거들뿐");

		Shell shell = new Shell(display);
		final StackLayout stackLayout = new StackLayout();
		shell.setLayout(stackLayout);
		
		VillageUI villageUI = new VillageUI(shell, character);
		DungeonUI dungeonUI = new DungeonUI(shell, character);
		SkillTree skillTree = new SkillTree(shell, character, villageUI);
		
		villageUI.makeComposite(skillTree);
		
		
		
		stackLayout.topControl = villageUI.getComposite();
		shell.setText("인포창");
		
		villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
			villageUI.disposeContent();
			dungeonUI.makeComposite();
			stackLayout.topControl = dungeonUI.getComposite();
			skillTree.superInfo=dungeonUI;
			shell.setText("수련의 방");
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree);
			stackLayout.topControl = villageUI.getComposite();
			shell.setText("인포창");
			shell.layout();
		});
		
		display.addFilter(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				char c = event.character;
				// System.out.println(c);
				if ((c == 'i' || c == 'I') && stackLayout.topControl == villageUI.getComposite()) {
					if (villageUI.getVault().getShell() == null)
						villageUI.getVault().open();
					else
						villageUI.getVault().close();
				}

				else if (c == 'k' || c == 'K') {
					if (skillTree.getShell() == null)
						skillTree.open();
					else
						skillTree.close();
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
}

/*
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
}*/