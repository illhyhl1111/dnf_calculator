package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_calculator.DungeonStatusList;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.VillageStatusList;

public abstract class Item implements Cloneable
{
	private String name;										//이름
	private	Image icon;											//아이콘
	private Item_rarity rarity;									//희귀도
	public VillageStatusList vStat;								//마을스탯
	public DungeonStatusList dStat;								//인던스탯
	
	public Item(String name, Image icon, Item_rarity rarity)
	{
		this.name=name;
		this.icon=icon;
		this.rarity=rarity;
		vStat = new VillageStatusList();
		dStat = new DungeonStatusList();
	}
	public Item()
	{
		name="이름없음";
		icon=null; 			//TODO
		rarity=Item_rarity.NONE;
		vStat = new VillageStatusList();
		dStat = new DungeonStatusList();
	}
	
	public String getName() { return name;}
	public void setName(String name) { this.name = name;}
	
	public Image getIcon() { return icon;}
	public void setIcon(Image icon) { this.icon = icon;}
	
	public Item_rarity getRarity() { return rarity;}
	public void setRarity(Item_rarity rarity) { this.rarity = rarity;}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Item)
			if(name.equals(((Item) o).name)) return true;
		return false;
	}
}
