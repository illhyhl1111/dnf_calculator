package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Buff extends Item 
{
	public Buff(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Buff(){ super();}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Buff)
			if(this.getName().equals(((Buff) o).getName())) return true;
		return false;
	}
	
	@Override
	public int compareTo(Item arg) {
		return 1;
	}
	
	@Override
	public String getTypeName() { return "소모품";}
}
