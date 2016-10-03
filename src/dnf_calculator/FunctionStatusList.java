package dnf_calculator;

import java.util.LinkedList;

import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;


public class FunctionStatusList implements java.io.Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1108403569293695416L;
	public LinkedList<FunctionStat> statList;
	
	public FunctionStatusList()
	{
		statList = new LinkedList<FunctionStat>();
	}
	
	public void addListToStat(Status stat, Characters character, Monster monster, Equipment equipment)
	{
		for(FunctionStat s : statList)
			s.function(character, monster, equipment).addListToStat(stat);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		FunctionStatusList temp = (FunctionStatusList) super.clone();
		temp.statList = new LinkedList<FunctionStat>();
		for(FunctionStat s : statList)
		{
			temp.statList.add((FunctionStat)s.clone());
		}
		return temp;
	}
}