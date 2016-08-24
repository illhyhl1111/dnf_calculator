package dnf_calculator;

public class VillageStatusList extends StatusList
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
