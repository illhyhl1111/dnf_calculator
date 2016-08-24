package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;

public class SetOption extends Item{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6697625238787834124L;
	final SetName setName;
	final int requireNum;
	
	public SetOption(String name, SetName setName, int requireNum)
	{
		super(name, null, Item_rarity.NONE);
		this.setName=setName;
		this.requireNum=requireNum;
	}
	
	public boolean isEnabled(int setNum)
	{
		if(setNum>=requireNum) return true;
		else return false;
	}
	
	public SetName getSetName() { return setName;}
}
