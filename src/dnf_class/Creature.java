package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Creature extends Item
{
	public Creature(String name, Item_rarity rarity)
	{
		super(name, "image\\Creature\\"+name+".png", rarity);
	}
	public Creature(){ super();}
	
	@Override
	public String getTypeName() { return "크리쳐";}
}
