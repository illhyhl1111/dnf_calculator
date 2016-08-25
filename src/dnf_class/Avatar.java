package dnf_class;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Avatar extends Item
{
	Avatar_part part;
	Emblem emblem1;
	Emblem emblem2;
	
	public Avatar(String name, String icon, Item_rarity rarity, Avatar_part part, Emblem emblem1, Emblem emblem2)
	{
		super(name, icon, rarity);
		this.part=part;
		this.emblem1=emblem1;
		this.emblem2=emblem2;
	}
	
	public Avatar(String name, String icon, Item_rarity rarity, Avatar_part part)
	{
		super(name, icon, rarity);
		this.part=part;
	}
	public Avatar(Avatar_part part) {
		super();
		this.part=part;
	}
}
