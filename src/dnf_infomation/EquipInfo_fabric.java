package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Equipment;

public class EquipInfo_fabric {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_type type = Equip_type.FABRIC;
		//////////에픽
		
		/////닼고, 다크고스
		//닼고상의
		temp = new Equipment("다크 고스 상의", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.DARKGOTH);
		temp.vStat.addStatList("지능", new StatusInfo(48), true);
		// TODO 70Lv 2
		temp.dStat.addStatList("암", new ElementInfo(30), true);
		temp.dStat.addStatList("마공", new StatusInfo(150), true);
		temp.dStat.addStatList("독공", new StatusInfo(150), true);
		equipList.add(temp);
		
		//닼고하의
		temp = new Equipment("다크 고스 하의", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.DARKGOTH);
		temp.vStat.addStatList("지능", new StatusInfo(48), true);
		// TODO 60Lv 2
		temp.dStat.addStatList("암", new ElementInfo(30), true);
		temp.dStat.addStatList("지능", new StatusInfo(300), true);
		equipList.add(temp);
		
		//닼고숄더
		temp = new Equipment("다크 고스 숄더", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.DARKGOTH);
		temp.vStat.addStatList("지능", new StatusInfo(39), true);
		// TODO 40~50Lv 1
		temp.dStat.addStatList("암", new ElementInfo(30), true);
		equipList.add(temp);
		
		//닼고벨트
		temp = new Equipment("다크 고스 벨트", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.DARKGOTH);
		temp.vStat.addStatList("지능", new StatusInfo(29), true);
		temp.dStat.addStatList("암", new ElementInfo(30), true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(20), true);
		equipList.add(temp);
		
		//닼고신발
		temp = new Equipment("다크 고스 샌들", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.DARKGOTH);
		temp.vStat.addStatList("지능", new StatusInfo(29), true);
		temp.dStat.addStatList("암", new ElementInfo(30), true);
		equipList.add(temp);
		
		
		/////불마력
		//불마상의, 마나번
		temp = new Equipment("마나 번 로브", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.BURNINGSPELL);
		temp.vStat.addStatList("지능", new StatusInfo(51), true);
		temp.dStat.addStatList("마법마스터리", new StatusInfo(15), false, true);
		temp.dStat.addStatList("독공뻥", new StatusInfo(15), false, true);
		equipList.add(temp);
		
		//불마하의
		temp = new Equipment("매직 번 트라우저", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.BURNINGSPELL);
		temp.vStat.addStatList("지능", new StatusInfo(48), true);
		temp.dStat.addStatList("크증뎀", new StatusInfo(15), false, true);
		equipList.add(temp);
		
		//불마어깨, 스펠번
		temp = new Equipment("스펠 번 숄더 패드", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.BURNINGSPELL);
		temp.vStat.addStatList("지능", new StatusInfo(41), true);
		temp.dStat.addStatList("마법마스터리", new StatusInfo(12), false, true);
		temp.dStat.addStatList("독공뻥", new StatusInfo(12), false, true);
		equipList.add(temp);
		
		//불마벨트
		temp = new Equipment("엘리멘탈 번 새쉬", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.BURNINGSPELL);
		temp.vStat.addStatList("지능", new StatusInfo(31), true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(20), false, true);
		equipList.add(temp);
		
		//불마신발, 소울번
		temp = new Equipment("소울 번 슈즈", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.BURNINGSPELL);
		temp.vStat.addStatList("지능", new StatusInfo(31), true);
		temp.dStat.addStatList("모속", new ElementInfo(20), false, true);
		equipList.add(temp);
		
		
		/////드로퍼
		//프컷로브
		temp = new Equipment("프리징 컷 로브", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.DROPPER);
		temp.vStat.addStatList("지능", new StatusInfo(51), true);
		temp.vStat.addStatList("수속", new ElementInfo(12), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.dStat.addStatList("수속깍", new StatusInfo(44), false, true);
		equipList.add(temp);
		
		//플드랍
		temp = new Equipment("플레임 드랍 트라우저", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.DROPPER);
		temp.vStat.addStatList("지능", new StatusInfo(51), true);
		temp.vStat.addStatList("화속", new ElementInfo(12), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.dStat.addStatList("화속깍", new StatusInfo(44), false, true);
		equipList.add(temp);
		
		//레디숄, 레이숄
		temp = new Equipment("레이 디크리즈 숄더", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.DROPPER);
		temp.vStat.addStatList("지능", new StatusInfo(85), true);
		temp.vStat.addStatList("명속", new ElementInfo(12), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.dStat.addStatList("명속깍", new StatusInfo(44), false, true);
		equipList.add(temp);
		
		//닼로새쉬
		temp = new Equipment("다크니스 로우 새쉬", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.DROPPER);
		temp.vStat.addStatList("지능", new StatusInfo(75), true);
		temp.vStat.addStatList("암속", new ElementInfo(12), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.dStat.addStatList("암속깍", new StatusInfo(44), false, true);
		equipList.add(temp);
		
		//페다슈, 페이스다운
		temp = new Equipment("페이스 다운 슈즈", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.DROPPER);
		temp.vStat.addStatList("지능", new StatusInfo(75), true);
		temp.vStat.addStatList("모속", new ElementInfo(30), true);
		equipList.add(temp);
	}
	
}
