package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Consumeable extends Item
{
	public Consumeable(String name, Image icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Consumeable(){ super();}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Consumeable)
			if(this.getName().equals(((Consumeable) o).getName())) return true;
		return false;
	}
}
