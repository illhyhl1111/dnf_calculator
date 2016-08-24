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
		temp.dStat.addStatList("고정물방깍", new StatusInfo(-12000), true);
		equipList.add(temp);
		
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 5);
		temp.dStat.addStatList("고정물방깍", new StatusInfo(-12000), true);
		temp.vStat.addStatList("증뎀", new StatusInfo(45));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		equipList.add(temp);
	}
}
