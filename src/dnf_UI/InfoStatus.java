package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


import dnf_calculator.Status;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

abstract class StatusUI
{
	Status stat;
	Composite infoStatusComposite;
	LabelAndInput[] infoStatusText;
	public abstract void setStatus(); 
	public Composite getComposite() {return infoStatusComposite;}
}

public class InfoStatus extends StatusUI
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
