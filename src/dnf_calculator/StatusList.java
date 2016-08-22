package dnf_calculator;
import java.util.Iterator;
import java.util.LinkedList;

public class StatusList {
	
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
			if(Status.statHash.containsKey(stat)) this.name = Status.statHash.get(name);
			else throw new UndefinedStatusKey(name);
		}
	}
	
	private LinkedList<StatusAndName> statList;
	private Iterator<StatusAndName> iter;
	
	public StatusList(){
		statList = new LinkedList<StatusAndName>();
		iter = statList.iterator();
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
		while(iter.hasNext())
		{
			StatusAndName tempElement = iter.next();
			//TODO
			stat.addDoubleStat(tempElement.name, tempElement.stat);
		}
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
	
	//TODO implement changeStat
}