package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import dnf_calculator.Status;
import dnf_class.Characters;

public class WholeStatus
{
	private InfoStatus infoStat;
	private NonInfoStatus nonInfoStat;
	private Composite wholeStatusComposite;
	public WholeStatus(Composite parent, Characters character, boolean isDungeon)
	{
		wholeStatusComposite = new Composite(parent, SWT.BORDER);
		RowLayout wholeLayout = new RowLayout();
		wholeLayout.spacing=10;
		wholeLayout.wrap=false;
		wholeLayout.pack=true;
		wholeStatusComposite.setLayout(wholeLayout);
		
		infoStat = new InfoStatus(wholeStatusComposite, character, isDungeon);
		nonInfoStat = new NonInfoStatus(wholeStatusComposite, character, isDungeon);
	}
	
	public Composite getComposite() {return wholeStatusComposite;}
	public void setStatus()
	{
		infoStat.setStatus();
		nonInfoStat.setStatus();
	}
}
