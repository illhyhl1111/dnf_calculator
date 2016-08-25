package dnf_class;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Emblem extends Item
{
	AddOn type;
	public Emblem(String name, String icon, Item_rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
}
