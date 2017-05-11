package dnf_calculator;

import java.util.LinkedList;

import dnf_class.Characters;
import dnf_class.IconObject;
import dnf_class.Monster;

public class FunctionStatusList implements java.io.Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1108403569293695416L;
	public LinkedList<String> statList;
	
	public FunctionStatusList()
	{
		statList = new LinkedList<String>();
	}
	
	public void addListToStat(Status stat, Characters character, Monster monster, Object item)
	{
		
		for(String s : statList){
			FunctionStat fStat = FunctionStat.getFunction(s);
			String[] args = FunctionStat.parseArgs(s);
			
			if(stat instanceof TrackableStatus){
				if(item instanceof IconObject)
					fStat.function(character, monster, item, args).addListToStat(stat, ((IconObject)item).getName());
				else
					fStat.function(character, monster, item, args).addListToStat(stat, item.toString());
			}
			else {
				fStat.function(character, monster, item, args).addListToStat(stat);
			}
		}
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		FunctionStatusList temp = (FunctionStatusList) super.clone();
		temp.statList = new LinkedList<String>();
		for(String s : statList)
		{
			temp.statList.add(s);
		}
		return temp;
	}
}