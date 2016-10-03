package dnf_calculator;

import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;

@SuppressWarnings("serial")
public abstract class FunctionStat implements java.io.Serializable, Cloneable {

	public abstract StatusList function(Characters character, Monster monster, Equipment equipment);
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
