package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_class.Avatar;

public class AvatarInfo{
	public static void getInfo(LinkedList<Avatar> avatarList)
	{
		Avatar temp;
		Item_rarity rarity;
		SetName setName;
		
		//////////레어
		rarity = Item_rarity.RARE;
		setName = SetName.RAREAVATAR;
		
		//레압상의
		temp = new Avatar("레어 아바타 상의", rarity, Equip_part.ACOAT, setName, CalculatorVersion.VER_1_0_a);
		//TODO 스킬레벨
		avatarList.add(temp);
		
		//레압하의
		temp = new Avatar("레어 아바타 하의", rarity, Equip_part.APANTS, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//레압머리
		temp = new Avatar("레어 아바타 머리", rarity, Equip_part.AHAIR, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//레압모자
		temp = new Avatar("레어 아바타 모자", rarity, Equip_part.ACAP, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//레압얼굴
		temp = new Avatar("레어 아바타 얼굴", rarity, Equip_part.AFACE, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//레압목가슴
		temp = new Avatar("레어 아바타 목가슴", rarity, Equip_part.ANECK, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//레압허리
		temp = new Avatar("레어 아바타 허리", rarity, Equip_part.ABELT, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//레압신발
		temp = new Avatar("레어 아바타 신발", rarity, Equip_part.ASHOES, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//////////상급
		rarity = Item_rarity.AVATAR;
		setName = SetName.AVATAR;
		
		//상압상의
		temp = new Avatar("상급 아바타 상의", rarity, Equip_part.ACOAT, setName, CalculatorVersion.VER_1_0_a);
		//TODO 스킬레벨
		avatarList.add(temp);
		
		//상압하의
		temp = new Avatar("상급 아바타 하의", rarity, Equip_part.APANTS, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//상압머리
		temp = new Avatar("상급 아바타 머리", rarity, Equip_part.AHAIR, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//상압모자
		temp = new Avatar("상급 아바타 모자", rarity, Equip_part.ACAP, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//상압얼굴
		temp = new Avatar("상급 아바타 얼굴", rarity, Equip_part.AFACE, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//상압목가슴
		temp = new Avatar("상급 아바타 목가슴", rarity, Equip_part.ANECK, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//상압허리
		temp = new Avatar("상급 아바타 허리", rarity, Equip_part.ABELT, setName, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		//상압신발
		temp = new Avatar("상급 아바타 신발", rarity, Equip_part.ASHOES, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//피부
		temp = new Avatar("피부 아바타", rarity, Equip_part.ASKIN, SetName.NONE, CalculatorVersion.VER_1_0_a);
		avatarList.add(temp);
		
		
		////오라
		setName = SetName.NONE;
		
		Equip_part part = Equip_part.AURA;
		temp = new Avatar("오라-속강", rarity, part, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(15), true, true);
		temp.vStat.addStatList("지능", new StatusInfo(15), true, true);
		temp.vStat.addStatList("화속강", 20, true, true);
		temp.vStat.addStatList("수속강", 20, true, true);
		temp.vStat.addStatList("명속강", 20, true, true);
		temp.vStat.addStatList("암속강", 20, true, true);
		temp.vStat.addStatList("물크", 3, true, true);
		temp.vStat.addStatList("마크", 3, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		avatarList.add(temp);
		
		temp = new Avatar("오라-물마독", rarity, part, setName, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(15), true, true);
		temp.vStat.addStatList("지능", new StatusInfo(15), true, true);
		temp.vStat.addStatList("물공", 45, true, true);
		temp.vStat.addStatList("마공", 45, true, true);
		temp.vStat.addStatList("독공", 60, true, true);
		temp.vStat.addStatList("물크", 10, true, true);
		temp.vStat.addStatList("마크", 10, true, true);
		temp.vStat.addStatList("모속강", 5, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		avatarList.add(temp);
		
		temp = new Avatar("오라-스탯", rarity, part, setName, CalculatorVersion.VER_1_0_a);		
		temp.vStat.addStatList("힘", 50, true, true);
		temp.vStat.addStatList("지능", 50, true, true);
		avatarList.add(temp);
	}
}
