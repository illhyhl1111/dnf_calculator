package dnf_calculator;
import java.util.LinkedList;

class StatusAndName
{
	int name;
	AbstractStatusInfo stat;
	
	public StatusAndName(int name, AbstractStatusInfo stat)
	{
		this.name=name;
		this.stat=stat;
	}
	public StatusAndName(String name, AbstractStatusInfo stat) throws UndefinedStatusKey
	{
		this.stat=stat;
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
	}
}

public class StatusList {
	
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
	
	public void addListToStat(Status stat)
	{
		for(StatusAndName s : statList)
			stat.addStat(s.name, s.stat);
	}
}

class DungeonStatusList extends StatusList
{
	public DungeonStatusList()
	{
		super();
	}
}

class VillageStatusList extends StatusList
{
	public VillageStatusList()
	{
		super();
	}
	
	public void changeStat(int name, AbstractStatusInfo stat)
	{
		for(StatusAndName s : statList)
			if(s.name==name){
				s.stat=stat;
				break;
			}
		addStatList(name, stat);
	}
}