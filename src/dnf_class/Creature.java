package dnf_class;

import org.eclipse.swt.graphics.Image;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Creature extends Item
{
	public Creature(String name, Image icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Creature(){ super();}
}
