package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Creature extends Item
{
	public Creature(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Creature(){ super();}
}
