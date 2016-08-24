package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Emblem extends Item
{
	AddOn type;
	public Emblem(String name, Image icon, Item_rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
}
