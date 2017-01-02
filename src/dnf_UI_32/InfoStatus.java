package dnf_UI_32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import dnf_calculator.Calculator;
import dnf_calculator.Status;
import dnf_class.Characters;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

abstract class StatusUI extends DnFComposite
{
	Characters character;
	LabelAndInput[] infoStatusText;
	public abstract void setStatus(); 
	public boolean isDungeon;
}

public class InfoStatus extends StatusUI
{
	public InfoStatus(Composite parent, Characters character, Boolean isDungeon)
	{
		this.character=character;
		this.isDungeon=isDungeon;
		mainComposite = new Composite(parent, SWT.BORDER);
		GridLayout infoLayout = new GridLayout(2, true);
		infoLayout.horizontalSpacing=5;
		infoLayout.verticalSpacing=0;
		mainComposite.setLayout(infoLayout);
		infoStatusText = new LabelAndInput[Status.infoStatNum];
		GridData statusGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		
		int i;
		//TextInputOnlyNumbers floatFormat = new TextInputOnlyNumbers();
		for(i=0; i<Status.infoStatNum; i++){
			infoStatusText[i] = new LabelAndText(mainComposite, Status.infoStatOrder[i], "");
			infoStatusText[i].composite.setLayoutData(statusGridData);
			//((Text) infoStatusText[i].input).addVerifyListener(floatFormat);
			
			if(Status.infoStatOrder[i].equals("독립공격")){																//독공
				infoStatusText[i].composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
				GridData textData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
				textData.grabExcessHorizontalSpace=true;
				textData.minimumWidth=80;
				textData.heightHint=20;;
				((LabelAndText)infoStatusText[i]).setTextData(textData);
			}
		}
		
		renew();
	}
	
	public void setStatus()
	{
		try{
			Status stat;
			if(isDungeon) stat = character.dungeonStatus;
			else stat = character.villageStatus;
			for(int i=0; i<Status.infoStatNum; i++)
			{
				if(Status.infoStatOrder[i].equals("인포물공") || Status.infoStatOrder[i].equals("인포마공")) continue;
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
	
	public void renew()
	{
		try{
			Status stat;
			if(isDungeon) stat = character.dungeonStatus;
			else stat = character.villageStatus;
			for(int i=0; i<Status.infoStatNum; i++){
				if(Status.infoStatOrder[i].equals("인포물공")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoPhysicalATK(stat)));
				}
				else if(Status.infoStatOrder[i].equals("인포마공")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoMagicalATK(stat)));
				}
				else if(Status.infoStatOrder[i].equals("힘(최종)")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoStrength(stat)));
				}
				else if(Status.infoStatOrder[i].equals("지능(최종)")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoIntellegence(stat)));
				}
				else if(Status.infoStatOrder[i].equals("독립공격")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoIndependentATK(stat)));
				}
				else if(Status.infoStatOrder[i].contains("속성강화")){
					infoStatusText[i].setTextString(String.valueOf(Calculator.getInfoElementReinforce(stat, Status.infoStatOrder[i])));
				}
				else{	
					String temp = String.format("%.2f", stat.getStat(Status.infoStatOrder[i]));
					if(temp.contains(".00")) temp=temp.substring(0, temp.length()-3);
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
}
