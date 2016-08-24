package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Title extends Item
{
	public Title(String name, Image icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Title(){ super();}
}
