package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import dnf_calculator.Status;
import dnf_class.Characters;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

public class NonInfoStatus extends StatusUI
{	
	ScrolledComposite scrollComposite;
	
	public NonInfoStatus(Composite parent, Characters character, Boolean isDungeon)
	{
		this.character=character;
		this.isDungeon=isDungeon;
		
		scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
        scrollComposite.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event e) {
				scrollComposite.setFocus();
			}
		});
        
		
		infoStatusComposite = new Composite(scrollComposite, SWT.NONE);
		scrollComposite.setContent(infoStatusComposite);
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
			}
			else
			{
				infoStatusText[i] = new LabelAndText(infoStatusComposite, Status.nonInfoStatOrder[i], "");
				infoStatusText[i].composite.setLayoutData(statusGridData);
				((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
			}
		}
		renew();
		
		infoStatusComposite.setSize(infoStatusComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setMinSize(infoStatusComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void setStatus()
	{
		try{
			Status stat;
			if(isDungeon) stat = character.dungeonStatus;
			else stat = character.villageStatus;
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
	
	public void renew()
	{
		try
		{
			Status stat;
			if(isDungeon) stat = character.dungeonStatus;
			else stat = character.villageStatus;
			for(int i=0; i<Status.nonInfoStatNum; i++){
				if(Status.nonInfoStatOrder[i].contains("속성부여"))
				{
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
					String temp = String.valueOf(stat.getStat(Status.nonInfoStatOrder[i]));
					if(temp.contains(".0")) temp=temp.substring(0, temp.length()-2);
					infoStatusText[i].setTextString(temp);
				}
				infoStatusText[i].setInputEnable(false);
			}
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	
	public Composite getComposite() {return scrollComposite;}
}
