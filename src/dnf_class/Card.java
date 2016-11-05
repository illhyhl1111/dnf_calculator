package dnf_class;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Card extends Item implements java.io.Serializable
{
	private static final long serialVersionUID = 4716963298958477792L;
	private ArrayList<Equip_part> availableType;
	public Card(String name, Item_rarity rarity, String version)
	{
		super(name, "image\\Card\\"+name+".png", rarity, version);
		availableType = new ArrayList<Equip_part>();
	}
	
	public Card()
	{
		super("카드없음", null, Item_rarity.NONE, CalculatorVersion.DEFAULT);
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
	
	public int availableOrder()
	{
		int result=0;
		for(Equip_part part : availableType)
			if(part.order>result) result=part.order;
		return result;
	}
	
	@Override
	public String getTypeName() { return "보주";}
	
	@Override
	public int compareTo(Item arg) {
		if(arg instanceof Equipment || arg instanceof Title) return -1;
		else if(!(arg instanceof Card)) return 1;			// 1.
		Card arg2 = (Card)arg;
		
		if(arg2.availableOrder()!=availableOrder()) return arg2.availableOrder()-availableOrder();
		if(arg2.getRarity()!=this.getRarity()) return arg2.getRarity().rarity-this.getRarity().rarity;		// 2.
		return this.getName().compareTo(arg2.getName());
	}
}