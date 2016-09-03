package dnf_class;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_infomation.GetItemDictionary;

@SuppressWarnings("serial")
public class Equipment extends Item
{
	public final Equip_part part;							//부위
	private Dimension_stat dimStat;							//차원작
	private int reinforce;									//강화수치
	private Card card;										//보주
	public final SetName setName;							//셋옵이름
	public final Equip_type type;							//재질
	public final int level;									//레벨
	
	public Equipment(String name, String icon,Item_rarity rarity, Equip_part part,
			Card card, SetName setName, Equip_type type, int level)
	{	
		super(name, icon, rarity);
		this.part=part;
		this.card=card;
		this.setName=setName;
		this.type=type;
		this.level=level;
		
		vStat.addStatList(StatList.NONE, new StatusInfo(0));					//임시 차원스탯
		
		if(part==Equip_part.AIDEQUIPMENT || part==Equip_part.MAGICSTONE)
		{
			vStat.addStatList("힘", new StatusInfo(0), true);
			vStat.addStatList("지능", new StatusInfo(0), true);
			vStat.addStatList("체력", new StatusInfo(0), true);
			vStat.addStatList("정신력", new StatusInfo(0), true);
		}
		
		try {
			setReinforce(0);
			setDimension(Dimension_stat.NONE);
		} catch (UnknownInformationException e) {
			e.printStackTrace();
		}
		
	}
	public Equipment(String name, String icon, Item_rarity rarity, Equip_part part, Equip_type type, int level)
	{
		this(name, icon, rarity, part, new Card("없음", null, Item_rarity.NONE), SetName.NONE, type, level);
	}
	public Equipment(String name, String icon, Item_rarity rarity, Equip_part part, Equip_type type, SetName setName, int level)
	{
		this(name, icon, rarity, part, new Card("없음", null, Item_rarity.NONE), setName, type, level);
	}
	public Equipment(Equip_part part) {
		super();
		this.part=part;
		type = Equip_type.NONE;
		setName=SetName.NONE;
		card = new Card("없음", null, Item_rarity.NONE);
		level=0;
		
		vStat.addStatList(StatList.NONE, new StatusInfo(0));
		if(part==Equip_part.AIDEQUIPMENT || part==Equip_part.MAGICSTONE)
		{
			vStat.addStatList("힘", new StatusInfo(0), true);
			vStat.addStatList("지능", new StatusInfo(0), true);
			vStat.addStatList("체력", new StatusInfo(0), true);
			vStat.addStatList("정신력", new StatusInfo(0), true);
		}
		
		try {
			setReinforce(0);
			setDimension(Dimension_stat.NONE);
		} catch (UnknownInformationException e) {
			if(level!=0) e.printStackTrace();
		}
	}
	
	public void setReinforceNum(int num) {reinforce=num;}
	public void setDimensionType(Dimension_stat type) {
		dimStat=type;
		switch(dimStat)
		{
		case STR:
			vStat.statList.getFirst().name=StatList.STR;
			break;
		case INT:
			vStat.statList.getFirst().name=StatList.INT;
			break;
		case STA:
			vStat.statList.getFirst().name=StatList.STA;
			break;
		case WILL:
			vStat.statList.getFirst().name=StatList.WILL;
			break;
		case NONE:
			vStat.statList.getFirst().name=StatList.NONE;
			break;
		}
	}
	
	public void setDimension(Dimension_stat dim) throws UnknownInformationException
	{
		int stat = GetItemDictionary.getDimensionInfo(reinforce, super.getRarity(), level);
		switch(dim)
		{
		case STR:
			vStat.statList.set(getDimStatIndex(), new StatusAndName(StatList.STR, new StatusInfo(stat), true));
			break;
		case INT:
			vStat.statList.set(getDimStatIndex(), new StatusAndName(StatList.INT, new StatusInfo(stat), true));
			break;
		case STA:
			vStat.statList.set(getDimStatIndex(), new StatusAndName(StatList.STA, new StatusInfo(stat), true));
			break;
		case WILL:
			vStat.statList.set(getDimStatIndex(), new StatusAndName(StatList.WILL, new StatusInfo(stat), true));
			break;
		default:
			vStat.statList.set(getDimStatIndex(), new StatusAndName(StatList.NONE, new StatusInfo(stat), true));
			break;
		}
		dimStat=dim;
	}
	
	public void setReinforce(int num) throws UnknownInformationException
	{
		try {
			if(dimStat!=Dimension_stat.NONE)
				vStat.statList.get(getDimStatIndex()).stat.setInfo(GetItemDictionary.getDimensionInfo(num, super.getRarity(), level));
			
			int strIndex = getAidStatIndex();
			if(strIndex>=0){
				int str = GetItemDictionary.getReinforceAidInfo( num, super.getRarity(), level);
				vStat.statList.get(strIndex).stat.setInfo(str);
				vStat.statList.get(strIndex+1).stat.setInfo(str);
				vStat.statList.get(strIndex+2).stat.setInfo(str);
				vStat.statList.get(strIndex+3).stat.setInfo(str);
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		reinforce=num;
	}
	
	@Override
	public int getAidStatIndex()
	{
		if(part==Equip_part.AIDEQUIPMENT || part==Equip_part.MAGICSTONE)
		{
			int iter=1;
			for(StatusAndName s : vStat.statList.subList(1, vStat.statList.size())){
				if(s.equals("힘")){
					return iter; 
				}
				iter++;
			}
		}
		return -1;
	}
	
	@Override
	public int getDimStatIndex()
	{
		return 0;
	}
	
	@Override
	public int getIgnIndex() { return -1;}
	
	@Override
	public int getItemStatIndex()
	{
		if(getAidStatIndex()!=-1) return getAidStatIndex()+4;
		else if(getDimStatIndex()!=-1) return getDimStatIndex()+1;
		return 0;
	}
	
	@Override
	public String toString()
	{
		String out;
		out = "name : " + super.getName() +"\nrarity : "+super.getRarity()+"\nreinforce : "+reinforce+"\ntype : "+type.toString();
		out += "\npart : "+part.toString()+"\ntype : "+type.toString()+"\n\n";
		return out;
	}
	
	@Override
	public String getTypeName() { return part.getName();}
	@Override
	public String getTypeName2()
	{
		if(type.getName().equals("없음")) return null;
		else return type.getName();
	}
	
	public Dimension_stat getDimentionStat() {
		return dimStat;
	}
	public int getReinforce() {
		return reinforce;
	}
	public Card getCard() {
		return card;
	}
	public boolean setCard(Card card) {
		if(card.available(this)){
			this.card=card;
			return true;
		}
		return false;
	}
}
