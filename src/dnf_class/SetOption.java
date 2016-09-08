package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.StatusList;

public class SetOption implements Cloneable, java.io.Serializable, Comparable<SetOption>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6697625238787834124L;
	public final SetName setName;
	public final int requireNum;
	public StatusList vStat;									//마을스탯
	public StatusList dStat;									//인던스탯
	public LinkedList<String> explanation;
	
	public SetOption(String name, SetName setName, int requireNum)
	{
		this.setName=setName;
		this.requireNum=requireNum;
		vStat = new StatusList();
		dStat = new StatusList();
		explanation = new LinkedList<String>();
	}
	
	public boolean isEnabled(int setNum)
	{
		if(setNum>=requireNum) return true;
		else return false;
	}
	
	public SetName getSetName() { return setName;}
	
	public void changeOption(SetOption target)
	{
		vStat = target.vStat;
		dStat = target.dStat;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Item temp = (Item) super.clone();
		temp.vStat = (StatusList) vStat.clone();
		temp.dStat = (StatusList) dStat.clone();
		return temp;
	}
	
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
