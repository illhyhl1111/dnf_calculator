package dnf_calculator;

import dnf_InterfacesAndExceptions.UndefinedStatusKey;

public class StatusAndName
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
