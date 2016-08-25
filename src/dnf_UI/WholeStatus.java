package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import dnf_calculator.Status;

public class WholeStatus
{
	private InfoStatus infoStat;
	private NonInfoStatus nonInfoStat;
	private Composite wholeStatusComposite;
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
