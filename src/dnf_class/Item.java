package dnf_class;

import dnf_calculator.StatusList;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Item implements Cloneable, java.io.Serializable
{
	private String name;										//이름
	private	String iconAddress;									//아이콘
	private Item_rarity rarity;									//희귀도
	public StatusList vStat;									//마을스탯
	public StatusList dStat;									//인던스탯
	
	public Item(String name, String icon, Item_rarity rarity)
	{
		this.name=name;
		iconAddress=icon;
		this.rarity=rarity;
		vStat = new StatusList();
		dStat = new StatusList();
	}
	public Item()
	{
		name="없음";
		iconAddress="image\\default.png";
		rarity=Item_rarity.NONE;
		vStat = new StatusList();
		dStat = new StatusList();
	}
	
	public String getName() { return name;}
	public void setName(String name) { this.name = name;}
	
	public String getIcon() { return iconAddress;}
	public void setIcon(String icon) { iconAddress = icon;}
	
	public Item_rarity getRarity() { return rarity;}
	public void setRarity(Item_rarity rarity) { this.rarity = rarity;}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Item temp = (Item) super.clone();
		temp.vStat = (StatusList) vStat.clone();
		temp.dStat = (StatusList) dStat.clone();
		return temp;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Item)
			if(name.equals(((Item) o).getName()) ) return true;
		return false;
	}
	
	public String getTypeName() { return "아이템";}
	public String getTypeName2() { return null;} 
}
