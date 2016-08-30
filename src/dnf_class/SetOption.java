package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;

public class SetOption extends Item implements Comparable<SetOption>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6697625238787834124L;
	public final SetName setName;
	public final int requireNum;
	
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
	
	@Override
	public boolean equals(Object o) {
        if (!(o instanceof SetOption))
            return false;
        SetOption n = (SetOption) o;
        if(setName==n.setName && requireNum==n.requireNum) return true;
        return false;
    }

	@Override
    public int compareTo(SetOption n) {		
        int setCmp = setName.compareTo(n.setName)*100;
        return (setCmp != 0 ? setCmp*100 : requireNum-n.requireNum);
    }
}
