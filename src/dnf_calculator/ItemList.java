package dnf_calculator;
import java.util.HashSet;
import java.util.Iterator;

public class ItemList {												// 기본 아이템 파일 제작 (유저 디자인과는 분리된 파일)
	
	public HashSet<Equipment> equipList = new HashSet<Equipment>();
	
	public ItemList()
	{
		//거미상의
		Equipment temp = new Equipment("타란튤라 상의", null, Item_Rarity.EPIC, Equip_part.ROBE, Equip_type.HEAVY); 
		temp.vStat.addStatList("힘", new StatusInfo(200));
		temp.vStat.addStatList("지능", new StatusInfo(188));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미하의
		temp = new Equipment("킹바분 하의", null, Item_Rarity.EPIC, Equip_part.TROUSER, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(200));
		temp.vStat.addStatList("지능", new StatusInfo(188));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미어깨
		temp = new Equipment("골리앗 버드이터 어깨", null, Item_Rarity.EPIC, Equip_part.SHOULDER, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(147));
		temp.vStat.addStatList("지능", new StatusInfo(138));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미벨트
		temp = new Equipment("로즈헤어 벨트", null, Item_Rarity.EPIC, Equip_part.BELT, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(138));
		temp.vStat.addStatList("지능", new StatusInfo(130));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
			
		//거미신발
		temp = new Equipment("인디언 오너멘탈 신발", null, Item_Rarity.EPIC, Equip_part.SHOES, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(138));
		temp.vStat.addStatList("지능", new StatusInfo(130));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
	}

}

class GetItem
{
	static HashSet<Equipment> equipList;
	static Iterator<Equipment> equipIter;
	static boolean readed=false;
	
	public static void readFile()
	{
		if(readed) return;
		
		//read file
		equipList = (new ItemList()).equipList;
		equipIter = equipList.iterator();
	}
	
	private static Item findItem(String name, Iterator<? extends Item> iter) throws ItemFileNotFounded
	{
		while(iter.hasNext()){
			Item temp = iter.next();
			if(temp.getName().equals(name)) return temp;
		}
		throw new ItemFileNotFounded(name);	
	}
	
	public static Equipment getEquipment(String name) throws ItemFileNotReaded, ItemFileNotFounded
	{
		if(!readed) throw new ItemFileNotReaded();
		return (Equipment)findItem(name, equipIter);
	}
}

@SuppressWarnings("serial")
class ItemFileNotReaded extends Exception
{
	public ItemFileNotReaded()
	{
		super();
	}
}

@SuppressWarnings("serial")
class ItemFileNotFounded extends Exception
{
	public ItemFileNotFounded(String str)
	{
		super(str+" not found");
	}
}