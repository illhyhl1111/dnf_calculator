package dnf_UI;

import org.eclipse.swt.widgets.Composite;

public abstract class DnFComposite {
	protected Composite mainComposite;
	
	public Composite getComposite() {return mainComposite;}
	public abstract void renew();
}
