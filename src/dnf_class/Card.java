package dnf_class;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Card extends Item implements java.io.Serializable
{
	private static final long serialVersionUID = 4716963298958477792L;
	private ArrayList<Equip_part> availableType;
	public Card(String name, Item_rarity rarity)
	{
		super(name, "image\\Card\\"+name+".png", rarity);
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
	
	@Override
	public int compareTo(Item arg) {
		if(arg instanceof Equipment || arg instanceof Title) return -1;
		else if(!(arg instanceof Card)) return 1;			// 1.
		Card arg2 = (Card)arg;
		
		if(arg2.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg2.getRarity().rarity;		// 2.
		return arg2.getName().compareTo(this.getName());
	}
}