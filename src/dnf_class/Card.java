package dnf_class;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Card extends Item implements java.io.Serializable
{
	private ArrayList<Equip_part> availableType;
	public Card(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
		availableType = new ArrayList<Equip_part>();
	}
	
	public Card()
	{
		super("카드없음", null, Item_rarity.NONE);
		availableType = new ArrayList<Equip_part>();
	}
	
	public void addPart(Equip_part part)
	{
		availableType.add(part);
	}
	
	public String getPartToString()
	{
		String temp="";
		for(Equip_part part : availableType)
			temp+=part.getName()+", ";
		if(temp.endsWith(", ")) return temp.substring(0, temp.length()-2);
		return "장착 가능 부위 없음";
	}
	
	public boolean available(Item item)
	{
		if(item instanceof Equipment)
		{
			if(availableType.contains(((Equipment)item).part)) return true;
			return false;
		}
		else if(item instanceof Title)
		{
			if(availableType.contains(Equip_part.TITLE)) return true;
			return false;
		}
		return false;
	}
	
	public boolean available(LinkedList<Equip_part> partList)
	{
		for(Equip_part part : partList)
			if(availableType.contains(part)) return true;
		return false;
	}
	
	@Override
	public String getTypeName() { return "보주";}
}