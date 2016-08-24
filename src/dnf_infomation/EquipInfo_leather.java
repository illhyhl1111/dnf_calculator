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

public class EquipInfo_leather {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_type type = Equip_type.LEATHER;
		//////////에픽
		
		/////카멜레온
		//카멜상의
		temp = new Equipment("교활한 카멜레온 가죽 상의", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.CHAMELEON);
		temp.vStat.addStatList("힘", new StatusInfo(41), true);
		temp.vStat.addStatList("지능", new StatusInfo(41), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.dStat.addStatList("고정물방깍", new StatusInfo(8000), true);
		temp.dStat.addStatList("증뎀", new StatusInfo(10));
		equipList.add(temp);
		
		//카멜하의
		temp = new Equipment("민첩한 카멜레온 가죽 하의", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.CHAMELEON);
		temp.vStat.addStatList("힘", new StatusInfo(41), true);
		temp.vStat.addStatList("지능", new StatusInfo(41), true);
		temp.dStat.addStatList("물크", new DoubleStatusInfo(30), true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(30), true);
		equipList.add(temp);
		
		//카멜숄더
		temp = new Equipment("날렵한 카멜레온 가죽 숄더", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.CHAMELEON);
		temp.vStat.addStatList("힘", new StatusInfo(32), true);
		temp.vStat.addStatList("지능", new StatusInfo(32), true);
		temp.dStat.addStatList("물크", new DoubleStatusInfo(10), false, true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(10), false, true);
		equipList.add(temp);
		
		//카멜벨트
		temp = new Equipment("은밀한 카멜레온 가죽 벨트", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.CHAMELEON);
		temp.vStat.addStatList("힘", new StatusInfo(24), true);
		temp.vStat.addStatList("지능", new StatusInfo(24), true);
		equipList.add(temp);
		
		//카멜신발
		temp = new Equipment("재빠른 카멜레온 가죽 신발", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.CHAMELEON);
		temp.vStat.addStatList("힘", new StatusInfo(24), true);
		temp.vStat.addStatList("지능", new StatusInfo(24), true);
		equipList.add(temp);
		
		
		/////택틱, 웨슬리
		//택틱상의, 택상의
		temp = new Equipment("택틱컬 커맨더 상의", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.TACTICAL);
		temp.vStat.addStatList("힘", new StatusInfo(76), true);
		temp.vStat.addStatList("지능", new StatusInfo(76), true);
		temp.dStat.addStatList("물공", new StatusInfo(80));
		temp.dStat.addStatList("마공", new StatusInfo(80));
		temp.dStat.addStatList("독공", new StatusInfo(80));
		equipList.add(temp);
		
		//택틱하의, 택하의
		temp = new Equipment("택틱컬 리더 하의", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.TACTICAL);
		temp.vStat.addStatList("힘", new StatusInfo(76), true);
		temp.vStat.addStatList("지능", new StatusInfo(76), true);
		temp.dStat.addStatList("물크", new DoubleStatusInfo(12));
		temp.dStat.addStatList("마크", new DoubleStatusInfo(12));
		equipList.add(temp);
		
		//택틱어깨, 택어깨
		temp = new Equipment("택틱컬 리더 숄더", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.TACTICAL);
		temp.vStat.addStatList("힘", new StatusInfo(67), true);
		temp.vStat.addStatList("지능", new StatusInfo(67), true);
		equipList.add(temp);
		
		//택틱벨트, 택벨
		temp = new Equipment("택틱컬 로드 벨트", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.TACTICAL);
		temp.vStat.addStatList("힘", new StatusInfo(58), true);
		temp.vStat.addStatList("지능", new StatusInfo(58), true);
		temp.dStat.addStatList("모속", new ElementInfo(20));
		equipList.add(temp);
		
		//택틱신발, 택신
		temp = new Equipment("택틱컬 치프 신발", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.TACTICAL);
		temp.vStat.addStatList("힘", new StatusInfo(58), true);
		temp.vStat.addStatList("지능", new StatusInfo(58), true);
		temp.dStat.addStatList("힘", new StatusInfo(120));
		temp.dStat.addStatList("지능", new StatusInfo(120));
		equipList.add(temp);
		
		
		/////암살자
		//암살상의, 밤그상의
		temp = new Equipment("밤의 그림자 상의", null, Item_rarity.EPIC, Equip_part.ROBE, type, SetName.ASSASSIN);
		temp.vStat.addStatList("힘", new StatusInfo(92), true);
		temp.vStat.addStatList("지능", new StatusInfo(92), true);
		temp.vStat.addStatList("증뎀", new StatusInfo(18));
		equipList.add(temp);
		
		//암살하의, 붉송하의
		temp = new Equipment("붉은 송곳니 하의", null, Item_rarity.EPIC, Equip_part.TROUSER, type, SetName.ASSASSIN);
		temp.vStat.addStatList("힘", new StatusInfo(92), true);
		temp.vStat.addStatList("지능", new StatusInfo(92), true);
		temp.vStat.addStatList("크증뎀", new StatusInfo(15));
		equipList.add(temp);
		
		//암살어깨
		temp = new Equipment("어둠의 칼날 어꺠", null, Item_rarity.EPIC, Equip_part.SHOULDER, type, SetName.ASSASSIN);
		temp.vStat.addStatList("힘", new StatusInfo(67), true);
		temp.vStat.addStatList("지능", new StatusInfo(67), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.dStat.addStatList("물크", new DoubleStatusInfo(10), false, true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(10), false, true);
		equipList.add(temp);
		
		//암살벨트
		temp = new Equipment("죽음의 장막 벨트", null, Item_rarity.EPIC, Equip_part.BELT, type, SetName.ASSASSIN);
		temp.vStat.addStatList("힘", new StatusInfo(58), true);
		temp.vStat.addStatList("지능", new StatusInfo(58), true);
		temp.dStat.addStatList("물공", new StatusInfo(100), false, true);
		temp.dStat.addStatList("마공", new StatusInfo(100), false, true);
		temp.dStat.addStatList("독공", new StatusInfo(100), false, true);
		equipList.add(temp);
		
		//암살신발, 황천신
		temp = new Equipment("황천의 바람 신발", null, Item_rarity.EPIC, Equip_part.SHOES, type, SetName.ASSASSIN);
		temp.vStat.addStatList("힘", new StatusInfo(58), true);
		temp.vStat.addStatList("지능", new StatusInfo(58), true);
		temp.dStat.addStatList("모속", new ElementInfo(8), false, true);
		temp.dStat.addStatList("모속", new ElementInfo(8), false, true);
		equipList.add(temp);
	}
}
