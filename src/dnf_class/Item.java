package dnf_class;

import java.util.LinkedList;

import dnf_calculator.FunctionStatusList;
import dnf_calculator.StatusList;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;

@SuppressWarnings("serial")
public class Item extends IconObject implements Comparable<Item>
{
	private Item_rarity rarity;									//희귀도
	public StatusList vStat;									//마을스탯
	public StatusList dStat;									//인던스탯
	public FunctionStatusList fStat;							//조건부 스탯
	public LinkedList<String> explanation;						//설명
	public int replicateNum;
	
	public Item(String name, String icon, Item_rarity rarity)
	{
		super();
		this.setName(name);
		this.setIcon(icon);
		this.rarity=rarity;
		vStat = new StatusList();
		dStat = new StatusList();
		fStat = new FunctionStatusList();
		explanation = new LinkedList<String>();
		replicateNum=0;
	}
	public Item()
	{
		super();
		rarity=Item_rarity.NONE;
		vStat = new StatusList();
		dStat = new StatusList();
		fStat = new FunctionStatusList();
		explanation = new LinkedList<String>();
		replicateNum=0;
	}
	
	public Item_rarity getRarity() { return rarity;}
	public void setRarity(Item_rarity rarity) { this.rarity = rarity;}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone()
	{
		try {
			Item temp = (Item) super.clone();
			temp.vStat = (StatusList) vStat.clone();
			temp.dStat = (StatusList) dStat.clone();
			temp.explanation = (LinkedList<String>) explanation.clone();
			return temp;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Item)
			if(getName().equals(((Item) o).getName()) ) return true;
		return false;
	}
	
	public Card getCard() {return null;}
	public boolean setCard(Card card) {return false;}
	public Emblem[] getEmblem() {return null;}
	public boolean setEmblem1(Emblem emblem) {return false;}
	public boolean setEmblem2(Emblem emblem) {return false;}
	public boolean setPlatinum(Emblem emblem) {return false;}
	public Equip_part getPart() {return null;}
	public boolean getEnabled() {return true;}
	public void setEnabled(boolean enabled){}
	public SetName getSetName() {return SetName.NONE;}
	public Equip_type getEquipType() {return Equip_type.NONE;}
	
	public String getTypeName() { return "아이템";}
	public String getTypeName2() { return null;}
	
	public int getAidStatIndex() { return -1;}
	public int getDimStatIndex(){ return -1;}
	public int getEarringStatIndex() { return -1;}
	public int getIgnIndex(){ return -1;}
	public int getReforgeIndex() { return -1;}
	public int getItemStatIndex() { return 0;}
	
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 등급 : 에픽->레전더리->유니크->크로니클->레어->언커먼->커먼

	@Override
	public int compareTo(Item arg) {
		return 0;
	}
}
