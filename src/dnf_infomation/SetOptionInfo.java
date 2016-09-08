package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.SetOption;

public class SetOptionInfo {
	public static void getInfo(HashSet<SetOption> equipList)
	{
		SetOption temp;
		
		//////////에픽
		/////천
		//불마력
		
		/////중갑
		//거미
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 4);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(-12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(-12000));
		temp.explanation.add("(방어력 제한 36000을 위한 더미옵션입니다)");
		equipList.add(temp);
		
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 5);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(-12000));
		temp.dStat.addStatList("고정마방깍", new StatusInfo(-12000));
		temp.vStat.addStatList("증뎀", new StatusInfo(45));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		equipList.add(temp);
		
		//금계
		temp = new SetOption("금지된 계약 세트", SetName.FORBIDDENCONTRACT, 5);
		equipList.add(temp);
		
		
		
		
		/////////////아바타
		//레압
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 3);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		equipList.add(temp);
		
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 5);
		temp.explanation.add("데미지 증가 효과 없음");
		equipList.add(temp);
		
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 8);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		equipList.add(temp);
		
		//상압
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 3);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		equipList.add(temp);
		
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 5);
		temp.explanation.add("데미지 증가 효과 없음");
		equipList.add(temp);
		
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 8);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		equipList.add(temp);
	}
}
