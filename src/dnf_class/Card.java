package dnf_class;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Card extends Item implements java.io.Serializable
{
	AddOn type;
	public Card(String name, String icon, Item_rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
	
	public Card()
	{
		super("카드없음", null, Item_rarity.NONE);
		type = AddOn.NONE;
	}
}