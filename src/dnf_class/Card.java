package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Card extends Item
{
	AddOn type;
	public Card(String name, Image icon, Item_rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
}