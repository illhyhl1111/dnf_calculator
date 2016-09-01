package dnf_calculator;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

@SuppressWarnings("serial")
public class StatusList implements java.io.Serializable, Cloneable {
	
	public LinkedList<StatusAndName> statList;
	
	public StatusList(){
		statList = new LinkedList<StatusAndName>();
	}
	
	public void addStatList(int name, AbstractStatusInfo addStat)
	{
		statList.add(new StatusAndName(name, addStat));
	}
	public void addStatList(String name, AbstractStatusInfo addStat)
	{
		try{
			statList.add(new StatusAndName(name, addStat));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	public void addStatList(int name, AbstractStatusInfo addStat, boolean changeable)
	{
		statList.add(new StatusAndName(name, addStat, changeable));
	}
	public void addStatList(String name, AbstractStatusInfo addStat, boolean changeable)
	{
		try{
			statList.add(new StatusAndName(name, addStat, changeable));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	public void addStatList(int name, AbstractStatusInfo addStat, boolean changeable, boolean enableable)
	{
		statList.add(new StatusAndName(name, addStat, changeable, enableable));
	}
	public void addStatList(String name, AbstractStatusInfo addStat, boolean changeable, boolean enableable)
	{
		try{
			statList.add(new StatusAndName(name, addStat, changeable, enableable));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	
	public void changeStat(int order, AbstractStatusInfo stat)
	{
		StatusAndName temp = statList.get(order);
			if(temp.changeable) temp.stat=stat;
	}
	public void changeStat(int order, double stat, boolean enable)
	{
		StatusAndName temp = statList.get(order);
		if(temp.changeable)
			try {
				temp.stat.setInfo(stat);
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
			}
		if(temp.enableable)
			temp.enabled=enable;
	}
	
	public boolean findChangeable(int name)
	{
		for(StatusAndName s : statList)
			if(s.name==name){
				if(s.changeable) return true;
				else return false;
			}
		return false;
	}
	
	public void addListToStat(Status stat)
	{
		for(StatusAndName s : statList)
			if(s.enabled) stat.addStat(s.name, s.stat);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		StatusList temp = (StatusList) super.clone();
		temp.statList = new LinkedList<StatusAndName>();
		for(StatusAndName s : statList)
		{
			temp.statList.add((StatusAndName)s.clone());
		}
		return temp;
	}
}