package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusInfo;

public class EquipInfo_heavy {
	
	public static void getInfo(HashSet<Equipment> equipList)
	{
		//거미상의
		Equipment temp = new Equipment("타란튤라 상의", null, Item_rarity.EPIC, Equip_part.ROBE, Equip_type.HEAVY); 
		temp.vStat.addStatList("힘", new StatusInfo(200));
		temp.vStat.addStatList("지능", new StatusInfo(188));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미하의
		temp = new Equipment("킹바분 하의", null, Item_rarity.EPIC, Equip_part.TROUSER, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(200));
		temp.vStat.addStatList("지능", new StatusInfo(188));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미어깨
		temp = new Equipment("골리앗 버드이터 어깨", null, Item_rarity.EPIC, Equip_part.SHOULDER, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(147));
		temp.vStat.addStatList("지능", new StatusInfo(138));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
		
		//거미벨트
		temp = new Equipment("로즈헤어 벨트", null, Item_rarity.EPIC, Equip_part.BELT, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(138));
		temp.vStat.addStatList("지능", new StatusInfo(130));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
		equipList.add(temp);
			
		//거미신발
		temp = new Equipment("인디언 오너멘탈 신발", null, Item_rarity.EPIC, Equip_part.SHOES, Equip_type.HEAVY);
		temp.vStat.addStatList("힘", new StatusInfo(138));
		temp.vStat.addStatList("지능", new StatusInfo(130));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000));
	}
}
