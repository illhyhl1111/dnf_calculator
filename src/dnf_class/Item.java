package dnf_class;

import dnf_calculator.StatusList;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Item implements Cloneable, java.io.Serializable, Comparable<Item>
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
	
	public Card getCard() {return null;}
	public boolean setCard(Card card) {return false;}
	public Equip_part getPart() {return null;}
	
	public String getTypeName() { return "아이템";}
	public String getTypeName2() { return null;}
	
	public int getAidStatIndex() { return -1;}
	public int getDimStatIndex(){ return -1;}
	public int getEarringStatIndex() { return -1;}
	public int getIgnIndex(){ return -1;}
	public int getItemStatIndex() { return 0;}
	
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 등급 : 에픽->레전더리->유니크->크로니클->레어->언커먼->커먼

	@Override
	public int compareTo(Item arg) {
		return 0;
	}
}
