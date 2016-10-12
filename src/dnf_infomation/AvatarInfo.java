package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
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
		temp = new Avatar("레어 아바타 상의", rarity, Avatar_part.COAT, setName);
		//TODO 스킬레벨
		avatarList.add(temp);
		
		//레압하의
		temp = new Avatar("레어 아바타 하의", rarity, Avatar_part.PANTS, setName);
		avatarList.add(temp);
		
		//레압머리
		temp = new Avatar("레어 아바타 머리", rarity, Avatar_part.HAIR, setName);
		temp.vStat.addStatList("지능", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//레압모자
		temp = new Avatar("레어 아바타 모자", rarity, Avatar_part.CAP, setName);
		temp.vStat.addStatList("지능", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//레압얼굴
		temp = new Avatar("레어 아바타 얼굴", rarity, Avatar_part.FACE, setName);
		avatarList.add(temp);
		
		//레압목가슴
		temp = new Avatar("레어 아바타 목가슴", rarity, Avatar_part.NECK, setName);
		avatarList.add(temp);
		
		//레압허리
		temp = new Avatar("레어 아바타 허리", rarity, Avatar_part.BELT, setName);
		avatarList.add(temp);
		
		//레압신발
		temp = new Avatar("레어 아바타 신발", rarity, Avatar_part.SHOES, setName);
		temp.vStat.addStatList("힘", new StatusInfo(55), false, true);
		avatarList.add(temp);
		
		//////////상급
		rarity = Item_rarity.AVATAR;
		setName = SetName.AVATAR;
		
		//상압상의
		temp = new Avatar("상급 아바타 상의", rarity, Avatar_part.COAT, setName);
		//TODO 스킬레벨
		avatarList.add(temp);
		
		//상압하의
		temp = new Avatar("상급 아바타 하의", rarity, Avatar_part.PANTS, setName);
		avatarList.add(temp);
		
		//상압머리
		temp = new Avatar("상급 아바타 머리", rarity, Avatar_part.HAIR, setName);
		temp.vStat.addStatList("지능", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//상압모자
		temp = new Avatar("상급 아바타 모자", rarity, Avatar_part.CAP, setName);
		temp.vStat.addStatList("지능", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//상압얼굴
		temp = new Avatar("상급 아바타 얼굴", rarity, Avatar_part.FACE, setName);
		avatarList.add(temp);
		
		//상압목가슴
		temp = new Avatar("상급 아바타 목가슴", rarity, Avatar_part.NECK, setName);
		avatarList.add(temp);
		
		//상압허리
		temp = new Avatar("상급 아바타 허리", rarity, Avatar_part.BELT, setName);
		avatarList.add(temp);
		
		//상압신발
		temp = new Avatar("상급 아바타 신발", rarity, Avatar_part.SHOES, setName);
		temp.vStat.addStatList("힘", new StatusInfo(45), false, true);
		avatarList.add(temp);
		
		//피부
		temp = new Avatar("피부 아바타", rarity, Avatar_part.SKIN, SetName.NONE);
		avatarList.add(temp);
		
		
		////오라
		setName = SetName.NONE;
		
		Avatar_part part = Avatar_part.AURA;
		temp = new Avatar("임시 오라", rarity, part, setName);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.vStat.addStatList("모속강", new StatusInfo(15));
		avatarList.add(temp);
	}
}
