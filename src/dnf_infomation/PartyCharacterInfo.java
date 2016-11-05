package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusList;
import dnf_class.PartyCharacter;
import dnf_class.Saint;

public class PartyCharacterInfo {
	public static void getInfo(LinkedList<PartyCharacter> partyList)
	{
		PartyCharacter temp;
		StatusList statList;
		
		/////홀리
		temp = new Saint();
		statList = new StatusList();
		statList.addStatList("힘", 198);
		statList.addStatList("지능", 198);
		temp.setBuff("신념의 오라", "신념/15렙", statList, CalculatorVersion.VER_1_0_a);
		
		((Saint)temp).setStat(new int[] {3300, 3300, 9, 29, 26, 26, 45, 18, 2, 15, 60},
				new boolean[] {true, false, true, true}, "3300/45/18/이기", CalculatorVersion.VER_1_0_a);
		((Saint)temp).setStat(new int[] {2800, 2800, 9, 29, 26, 26, 43, 16, 2, 15, 60},
				new boolean[] {true, false, false, true}, "2800/43/16", CalculatorVersion.VER_1_0_a);
		((Saint)temp).setStat(new int[] {2500, 2500, 9, 29, 26, 26, 41, 14, 2, 15, 60},
				new boolean[] {true, false, false, true}, "2500/41/14", CalculatorVersion.VER_1_0_a);
		((Saint)temp).setStat(new int[] {2200, 200, 9, 29, 26, 26, 39, 13, 2, 15, 0},
				new boolean[] {true, false, false, true}, "2200/39/13/스택X", CalculatorVersion.VER_1_0_a);
		
		((Saint)temp).setStat(new int[] {2000, 2000, 0, HolyInfo.firstLevel_striking, HolyInfo.firstLevel_wisebless, HolyInfo.firstLevel_dawnbless,
				HolyInfo.firstLevel_glorybless, HolyInfo.firstLevel_aporkalypse, 1, HolyInfo.firstLevel_beliefAura, 0},
				new boolean[Saint.boolStatNum], Saint.settingFeatureName, CalculatorVersion.VER_1_0_a);
		
		partyList.add(temp);
		
		/////검신
		temp = new PartyCharacter("검신", Job.WEAPONMASTER);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 25.9);
		temp.setBuff("참철식", "16렙(달계마스터)", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 26.5);
		temp.setBuff("참철식", "17렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 27.2);
		temp.setBuff("참철식", "18렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 27.9);
		temp.setBuff("참철식", "19렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 28.6);
		temp.setBuff("참철식", "20렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 29.2);
		temp.setBuff("참철식", "21렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 29.9);
		temp.setBuff("참철식", "22렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 30.6);
		temp.setBuff("참철식", "23렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 31.2);
		temp.setBuff("참철식", "24렙", statList, CalculatorVersion.VER_1_0_a);
		partyList.add(temp);
		
		
		/////얼디
		temp = new PartyCharacter("얼디", Job.GRAPPLER_F);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 16);
		statList.addStatList("크리저항감소", 40);
		temp.setBuff("넥스냅", "물방깍 16%/크리 40%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 17);
		statList.addStatList("크리저항감소", 42);
		temp.setBuff("넥스냅", "물방깍 17%/크리 42%", statList, CalculatorVersion.VER_1_0_a);
		
		statList = new StatusList();
		statList.addStatList("증뎀버프", 22);
		temp.setBuff("옷깃", "16렙(달계마스터)", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 23);
		temp.setBuff("옷깃", "17렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 24);
		temp.setBuff("옷깃", "18렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 25);
		temp.setBuff("옷깃", "19렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 26);
		temp.setBuff("옷깃", "20렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 27);
		temp.setBuff("옷깃", "21렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 28);
		temp.setBuff("옷깃", "22렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		partyList.add(temp);
		
		
		/////넨마
		temp = new PartyCharacter("넨마", Job.NENMASTER_F);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 38);
		statList.addStatList("마법마스터리", 38);
		statList.addStatList("독공마스터리", 38);
		temp.setBuff("카이", "38%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 39);
		statList.addStatList("마법마스터리", 39);
		statList.addStatList("독공마스터리", 39);
		temp.setBuff("카이", "39%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 40);
		statList.addStatList("마법마스터리", 40);
		statList.addStatList("독공마스터리", 40);
		temp.setBuff("카이", "40%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 41);
		statList.addStatList("마법마스터리", 41);
		statList.addStatList("독공마스터리", 41);
		temp.setBuff("카이", "41%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 42);
		statList.addStatList("마법마스터리", 42);
		statList.addStatList("독공마스터리", 42);
		temp.setBuff("카이", "42%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 43);
		statList.addStatList("마법마스터리", 43);
		statList.addStatList("독공마스터리", 43);
		temp.setBuff("카이", "43%", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("물리마스터리", 44);
		statList.addStatList("마법마스터리", 44);
		statList.addStatList("독공마스터리", 44);
		temp.setBuff("카이", "44%", statList, CalculatorVersion.VER_1_0_a);
		partyList.add(temp);
	}
}
