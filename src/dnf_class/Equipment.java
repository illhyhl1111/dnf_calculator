package dnf_class;

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
	public boolean enabled;
	
	public Equipment(String name, String icon,Item_rarity rarity, Equip_part part,
			Card card, SetName setName, Equip_type type, int level)
	{	
		super(name, icon, rarity);
		this.part=part;
		this.card=card;
		this.setName=setName;
		this.type=type;
		this.level=level;
		enabled=false;
		
		vStat.addStatList(StatList.NONE, new StatusInfo(0));					//임시 차원스탯
		
		if(part==Equip_part.AIDEQUIPMENT || part==Equip_part.MAGICSTONE)
		{
			vStat.addStatList("힘", new StatusInfo(0), true);
			vStat.addStatList("지능", new StatusInfo(0), true);
			vStat.addStatList("체력", new StatusInfo(0), true);
			vStat.addStatList("정신력", new StatusInfo(0), true);
		}
		else if(part==Equip_part.EARRING)
		{
			vStat.addStatList("무기물공", new StatusInfo(0), true);
			vStat.addStatList("무기마공", new StatusInfo(0), true);
			vStat.addStatList("독공", new StatusInfo(0), true);
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
		enabled=false;
		
		vStat.addStatList(StatList.NONE, new StatusInfo(0));
		if(part==Equip_part.AIDEQUIPMENT || part==Equip_part.MAGICSTONE)
		{
			vStat.addStatList("힘", new StatusInfo(0), true);
			vStat.addStatList("지능", new StatusInfo(0), true);
			vStat.addStatList("체력", new StatusInfo(0), true);
			vStat.addStatList("정신력", new StatusInfo(0), true);
		}
		
		else if(part==Equip_part.EARRING)
		{
			vStat.addStatList("무기물공", new StatusInfo(0), true);
			vStat.addStatList("무기마공", new StatusInfo(0), true);
			vStat.addStatList("독공", new StatusInfo(0), true);
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
		boolean throwException=false;
		try {
			if(dimStat!=Dimension_stat.NONE)
				vStat.statList.get(getDimStatIndex()).stat.setInfo(GetItemDictionary.getDimensionInfo(num, super.getRarity(), level));
		}
		catch(UnknownInformationException e){
			throwException = true;
		} catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
			
		try{
			int strIndex = getAidStatIndex();
			if(strIndex>=0){
				int str = GetItemDictionary.getReinforceAidInfo( num, super.getRarity(), level);
				vStat.statList.get(strIndex).stat.setInfo(str);
				vStat.statList.get(strIndex+1).stat.setInfo(str);
				vStat.statList.get(strIndex+2).stat.setInfo(str);
				vStat.statList.get(strIndex+3).stat.setInfo(str);
			}
			
			strIndex = getEarringStatIndex();
			if(strIndex>=0){
				int[] str = GetItemDictionary.getReinforceEarringInfo( num, super.getRarity(), level);
				vStat.statList.get(strIndex).stat.setInfo(str[0]);
				vStat.statList.get(strIndex+1).stat.setInfo(str[0]);
				vStat.statList.get(strIndex+2).stat.setInfo(str[1]);
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		reinforce=num;
		
		if(throwException) throw new UnknownInformationException();
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
	public int getEarringStatIndex()
	{
		if(part==Equip_part.EARRING)
		{
			int iter=1;
			for(StatusAndName s : vStat.statList.subList(1, vStat.statList.size())){
				if(s.equals("무기물공")){
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
		else if(getEarringStatIndex()!=-1) return getEarringStatIndex()+3;
		else if(getDimStatIndex()!=-1) return getDimStatIndex()+1;
		return 0;
	}
	
	@Override
	public String toString()
	{
		String out;
		out = "name : " + super.getName() +"\nrarity : "+super.getRarity()+"\nreinforce : "+reinforce+"\ntype : "+type.toString();
		out += "\npart : "+part.toString()+"\n\n";
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
	
	@Override
	public Equip_part getPart() {return part;}
	
	public Dimension_stat getDimentionStat() {
		return dimStat;
	}
	public int getReinforce() {
		return reinforce;
	}
	@Override
	public Card getCard() {
		return card;
	}
	@Override
	public boolean setCard(Card card) {
		if(card.available(this)){
			this.card=card;
			return true;
		}
		return false;
	}
	@Override
	public boolean getEnabled() {return enabled;}
	@Override
	public void setEnabled(boolean enabled){this.enabled=enabled;}
	@Override
	public SetName getSetName() {return setName;}
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 등급 : 에픽->레전더리->유니크->크로니클->레어->언커먼->커먼
	// 3. 재질 : 천->가죽->경갑->중갑->판금
	// 4. 레벨 : 고레벨->저레벨 
	// 4. 셋옵 : 있음(사전배열)->없음
	// 5. 부위 : 방어구->무기->목걸이->팔찌->반지->보조장비->마법석->귀걸이->칭호->기타
	// 6. 강화등급
	
	@Override
	public int compareTo(Item arg) {
		if(!(arg instanceof Equipment)) return 1;			// 1.
		Equipment arg2 = (Equipment)arg;
		
		if(arg2.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg2.getRarity().rarity;		// 2.
		if(arg2.type!=type) return type.order-arg2.type.order;												// 3.
		if(arg2.level!=level) return level-arg2.level;														// 4.
		if(arg2.setName!=setName) return arg2.setName.compare(setName);										// 5.
		if(arg2.part!=part) return part.order-arg2.part.order;												// 6.
		else return reinforce-arg2.getReinforce();															// 7.
	}
}
