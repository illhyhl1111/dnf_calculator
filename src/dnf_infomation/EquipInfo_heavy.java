package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;

public class EquipInfo_heavy {
	
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Item_rarity rarity;
		SetName setName;
		String image ="image\\sprite_item_new_equipment_05_harmor\\";
		int level;
		
		//////////에픽
		rarity = Item_rarity.EPIC;
		
		///// 미다홀
		level=80;
		setName=SetName.DARKHOLE;
		//미닼상의
		temp = new Equipment("미지의 다크홀 상의", image+"미닼상의.png", rarity, Equip_part.ROBE, Equip_type.HEAVY, setName, level); 
		temp.vStat.addStatList("힘", new StatusInfo(44), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(4));
		temp.vStat.addStatList("지능", new StatusInfo(32), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(4));
		// TODO 45Lv 3, 60Lv 2
		equipList.add(temp);
		
		//미닼하의
		temp = new Equipment("미지의 다크홀 하의", image+"미닼하의.png", rarity, Equip_part.TROUSER, Equip_type.HEAVY, setName, level); 
		temp.vStat.addStatList("힘", new StatusInfo(44), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(4));
		temp.vStat.addStatList("지능", new StatusInfo(32), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(4));
		// TODO 45Lv 3, 60Lv 2
		equipList.add(temp);
		
		//미닼어깨
		temp = new Equipment("미지의 다크홀 어깨", image+"미닼어깨.png", rarity, Equip_part.SHOULDER, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(35), true);
		temp.vStat.addStatList("지능", new StatusInfo(25), true);
		temp.vStat.addStatList("증뎀", new StatusInfo(12));
		equipList.add(temp);
		
		//미닼벨트
		temp = new Equipment("미지의 다크홀 벨트", image+"미닼벨트.png", rarity, Equip_part.BELT, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(26), true);
		temp.vStat.addStatList("지능", new StatusInfo(20), true);
		temp.dStat.addStatList("물크", new DoubleStatusInfo(15));
		temp.dStat.addStatList("마크", new DoubleStatusInfo(15));
		equipList.add(temp);
			
		//미닼신발
		temp = new Equipment("미지의 다크홀 신발", image+"미닼신발.png", rarity, Equip_part.SHOES, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(81), true);
		temp.vStat.addStatList("지능", new StatusInfo(75), true);
		equipList.add(temp);
		
		///// 거미
		setName=SetName.SPIDERQUEEN;
		level=85;
		//거미상의
		temp = new Equipment("타란튤라 상의", image+"거미상의.png", rarity, Equip_part.ROBE, Equip_type.HEAVY, setName, level); 
		temp.vStat.addStatList("힘", new StatusInfo(200), true);
		temp.vStat.addStatList("지능", new StatusInfo(188), true);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000), false, true);
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000), false, true);
		temp.fStat.explanation.add("거미셋의 최대 방깍량 : 36000");
		equipList.add(temp);
		
		//거미하의
		temp = new Equipment("킹바분 하의", image+"거미하의.png", rarity, Equip_part.TROUSER, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(200), true);
		temp.vStat.addStatList("지능", new StatusInfo(188), true);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000), false, true);
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000), false, true);
		temp.fStat.explanation.add("거미셋의 최대 방깍량 : 36000");
		equipList.add(temp);
		
		//거미어깨
		temp = new Equipment("골리앗 버드이터 어깨", image+"거미어깨.png", rarity, Equip_part.SHOULDER, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(147), true);
		temp.vStat.addStatList("지능", new StatusInfo(138), true);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000), false, true);
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000), false, true);
		temp.fStat.explanation.add("거미셋의 최대 방깍량 : 36000");
		equipList.add(temp);
		
		//거미벨트
		temp = new Equipment("로즈헤어 벨트", image+"거미벨트.png", rarity, Equip_part.BELT, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(138), true);
		temp.vStat.addStatList("지능", new StatusInfo(130), true);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000), false, true);
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000), false, true);
		temp.fStat.explanation.add("거미셋의 최대 방깍량 : 36000");
		equipList.add(temp);
			
		//거미신발
		temp = new Equipment("인디언 오너멘탈 신발", image+"거미신발.png", rarity, Equip_part.SHOES, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(138), true);
		temp.vStat.addStatList("지능", new StatusInfo(130), true);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(12000), false, true);
		temp.dStat.addStatList("고정마방깍", new StatusInfo(12000), false, true);
		temp.fStat.explanation.add("거미셋의 최대 방깍량 : 36000");
		equipList.add(temp);
		
	
		/////금계
		setName = SetName.FORBIDDENCONTRACT;
		//금계상의, 피맹상의
		temp = new Equipment("피의 맹약 상의", image+"금계상의.png", rarity, Equip_part.ROBE, Equip_type.HEAVY, setName, level); 
		temp.vStat.addStatList("힘", new StatusInfo(266), true);
		temp.vStat.addStatList("지능", new StatusInfo(254), true);
		temp.dStat.addStatList("힘", new StatusInfo(200), true, true);
		equipList.add(temp);
		
		//금계하의, 서약하의
		temp = new Equipment("마나의 서약 하의", image+"금계하의.png", rarity, Equip_part.TROUSER, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(46), true);
		temp.vStat.addStatList("지능", new StatusInfo(34), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(15));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(15));
		temp.dStat.addStatList("물크", new DoubleStatusInfo(20), true, true);
		temp.dStat.addStatList("마크", new DoubleStatusInfo(20), true, true);
		equipList.add(temp);
		
		//금계어깨, 마계숄
		temp = new Equipment("마력의 계약 솔더", image+"금계어깨.png", rarity, Equip_part.SHOULDER, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(37), true);
		temp.vStat.addStatList("지능", new StatusInfo(28), true);
		temp.vStat.addStatList("증뎀", new StatusInfo(15));
		equipList.add(temp);
		
		//금계벨트, 협약벨트
		temp = new Equipment("체력의 협약 벨트", image+"금계벨트.png", rarity, Equip_part.BELT, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(28), true);
		temp.vStat.addStatList("지능", new StatusInfo(20), true);
		temp.dStat.addStatList("물공", new StatusInfo(100), false, true);
		temp.dStat.addStatList("마공", new StatusInfo(100), false, true);
		temp.dStat.addStatList("독공", new StatusInfo(100), false, true);
		equipList.add(temp);
			
		//금계신발, 피조부츠
		temp = new Equipment("피의 조약 부츠", image+"금계신발.png", rarity, Equip_part.SHOES, Equip_type.HEAVY, setName, level);
		temp.vStat.addStatList("힘", new StatusInfo(28), true);
		temp.vStat.addStatList("지능", new StatusInfo(20), true);
		temp.vStat.addStatList("모속강", new ElementInfo(14), true);
		temp.dStat.addStatList("물공", new StatusInfo(80));
		temp.dStat.addStatList("마공", new StatusInfo(80));
		temp.dStat.addStatList("독공", new StatusInfo(100));
		equipList.add(temp);
	}
}
