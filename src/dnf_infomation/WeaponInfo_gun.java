package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Equipment;
import dnf_class.Weapon;

public class WeaponInfo_gun {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Weapon temp;
		Item_rarity rarity;
		Weapon_detailType type;
		
		//////////에픽
		rarity = Item_rarity.EPIC;

		/////캐넌
		type = Weapon_detailType.GUN_HCANON;
		
		//우요
		temp = new Weapon("우요의 황금 캐넌", null, rarity, type);
		temp.vStat.addStatList("힘", new StatusInfo(107), true);
		temp.vStat.addStatList("물공", new StatusInfo(1154), true);
		temp.vStat.addStatList("마공", new StatusInfo(692), true);
		temp.vStat.addStatList("독공", new StatusInfo(648), true);
		temp.vStat.addStatList("모속강", new StatusInfo(30), true);
		temp.vStat.addStatList("추뎀", new StatusInfo(30));
		temp.dStat.addStatList("물리마스터리", new StatusInfo(12), false, true);
		temp.dStat.addStatList("마법마스터리", new StatusInfo(12), false, true);
		temp.dStat.addStatList("독공뻥", new StatusInfo(12), false, true);
		equipList.add(temp);
		
		/////리볼버
		type = Weapon_detailType.GUN_REVOLVER;
		
		//로오레
		temp = new Weapon("로드 오브 레인저", null, rarity, type);
		temp.vStat.addStatList("힘", new StatusInfo(72), true);
		temp.vStat.addStatList("물공", new StatusInfo(988), true);
		temp.vStat.addStatList("마공", new StatusInfo(831), true);
		temp.vStat.addStatList("독공", new StatusInfo(648), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(12));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("증뎀", new StatusInfo(60), true);
		equipList.add(temp);
		
	}
}
