package dnf_calculator;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.UndefinedStatusKey;

@SuppressWarnings("serial")
public class StatusList implements java.io.Serializable {
	
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
	
	public void changeStat(int name, AbstractStatusInfo stat)
	{
		for(StatusAndName s : statList)
			if(s.name==name){
				if(s.changeable) s.stat=stat;
				break;
			}
		addStatList(name, stat);
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
			stat.addStat(s.name, s.stat);
	}
}