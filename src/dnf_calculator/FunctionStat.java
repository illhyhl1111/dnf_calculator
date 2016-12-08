package dnf_calculator;

import dnf_class.Characters;
import dnf_class.Monster;

public abstract class FunctionStat implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = -8928317343855455641L;

	public abstract StatusList function(Characters character, Monster monster, Object item);
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
