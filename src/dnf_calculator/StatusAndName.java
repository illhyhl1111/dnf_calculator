package dnf_calculator;

import dnf_InterfacesAndExceptions.UndefinedStatusKey;

@SuppressWarnings("serial")
public class StatusAndName implements java.io.Serializable
{
	int name;
	AbstractStatusInfo stat;
	boolean changeable;
	boolean enableable;
	
	public StatusAndName(int name, AbstractStatusInfo stat)
	{
		this.name=name;
		this.stat=stat;
		changeable=false;
		enableable=false;
	}
	public StatusAndName(String name, AbstractStatusInfo stat) throws UndefinedStatusKey
	{
		this.stat=stat;
		changeable=false;
		enableable=false;
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
	}
	public StatusAndName(int name, AbstractStatusInfo stat, boolean changeable)
	{
		this.name=name;
		this.stat=stat;
		this.changeable=changeable;
		enableable=false;
	}
	public StatusAndName(String name, AbstractStatusInfo stat, boolean changeable) throws UndefinedStatusKey
	{
		this.stat=stat;
		this.changeable=changeable;
		enableable=false;
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
	}
	public StatusAndName(int name, AbstractStatusInfo stat, boolean changeable, boolean enableable)
	{
		this.name=name;
		this.stat=stat;
		this.changeable=changeable;
		this.enableable=enableable;
	}
	public StatusAndName(String name, AbstractStatusInfo stat, boolean changeable, boolean enableable) throws UndefinedStatusKey
	{
		this.stat=stat;
		this.changeable=changeable;
		this.enableable=enableable;
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
	}
}
