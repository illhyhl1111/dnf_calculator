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
		statList = new StatusList();
		statList.addStatList("힘", 212);
		statList.addStatList("지능", 212);
		temp.setBuff("신념의 오라", "신념/16렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 226);
		statList.addStatList("지능", 226);
		temp.setBuff("신념의 오라", "신념/17렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 242);
		statList.addStatList("지능", 242);
		temp.setBuff("신념의 오라", "신념/18렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 258);
		statList.addStatList("지능", 258);
		temp.setBuff("신념의 오라", "신념/19렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 273);
		statList.addStatList("지능", 273);
		temp.setBuff("신념의 오라", "신념/20렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 290);
		statList.addStatList("지능", 290);
		temp.setBuff("신념의 오라", "신념/21렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 306);
		statList.addStatList("지능", 306);
		temp.setBuff("신념의 오라", "신념/22렙", statList, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 323);
		statList.addStatList("지능", 323);
		temp.setBuff("신념의 오라", "신념/23렙", statList, CalculatorVersion.VER_1_0_a);
		
		((Saint)temp).setStat(new int[] {4500, 4500, 9, 33, 30, 30, 52, 26, 3, 17, 60},
				new boolean[] {true, false, true, true}, "4500/52/26/이기(극)", CalculatorVersion.VER_1_1_a);
		((Saint)temp).setStat(new int[] {3800, 3800, 9, 31, 28, 28, 49, 22, 3, 17, 60},
				new boolean[] {true, false, true, true}, "3800/49/22/이기", CalculatorVersion.VER_1_1_a);
		((Saint)temp).setStat(new int[] {3300, 3300, 9, 30, 27, 27, 45, 18, 2, 16, 60},
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
		statList.addStatList("증뎀버프", 21);
		temp.setBuff("옷깃", "15렙(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 22);
		temp.setBuff("옷깃", "16렙", statList, CalculatorVersion.VER_1_0_a);
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
		

		/////암제
		temp = new PartyCharacter("암제", Job.DARKTEMPELAR);
		statList = new StatusList();
		statList.addStatList("추뎀", 10);
		temp.setBuff("우시르", "게임딜표에적용X", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 21.5);
		statList.addStatList("%마방깍_스킬", 21.5);
		temp.setBuff("하베스트", "36레벨(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 22.2);
		statList.addStatList("%마방깍_스킬", 22.2);
		temp.setBuff("하베스트", "37레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 22.9);
		statList.addStatList("%마방깍_스킬", 22.9);
		temp.setBuff("하베스트", "38레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 23.7);
		statList.addStatList("%마방깍_스킬", 23.7);
		temp.setBuff("하베스트", "39레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 24.4);
		statList.addStatList("%마방깍_스킬", 24.4);
		temp.setBuff("하베스트", "40레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 25.1);
		statList.addStatList("%마방깍_스킬", 25.1);
		temp.setBuff("하베스트", "41레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 32.8);
		statList.addStatList("%마방깍_스킬", 32.8);
		temp.setBuff("하베스트", "46레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 33.6);
		statList.addStatList("%마방깍_스킬", 33.6);
		temp.setBuff("하베스트", "47레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 34.3);
		statList.addStatList("%마방깍_스킬", 34.3);
		temp.setBuff("하베스트", "48레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 35.0);
		statList.addStatList("%마방깍_스킬", 35.0);
		temp.setBuff("하베스트", "49레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 35.8);
		statList.addStatList("%마방깍_스킬", 35.8);
		temp.setBuff("하베스트", "50레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 36.5);
		statList.addStatList("%마방깍_스킬", 36.5);
		temp.setBuff("하베스트", "51레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 37.3);
		statList.addStatList("%마방깍_스킬", 37.3);
		temp.setBuff("하베스트", "52레벨+블커3", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);

		/////소울
		temp = new PartyCharacter("소울", Job.SOULMASTER);
		statList = new StatusList();
		statList.addStatList("힘", 708);
		statList.addStatList("지능", 708);
		statList.addStatList("암속깍", 68);
		statList.addStatList("%마방깍_스킬", 22);
		temp.setBuff("장판+암속깍", "달계마스터", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 768);
		statList.addStatList("지능", 768);
		statList.addStatList("암속깍", 76);
		statList.addStatList("%마방깍_스킬", 23);
		temp.setBuff("장판+암속깍", "달계마스터+1", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 828);
		statList.addStatList("지능", 828);
		statList.addStatList("암속깍", 83);
		statList.addStatList("%마방깍_스킬", 24);
		temp.setBuff("장판+암속깍", "달계마스터+2", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 889);
		statList.addStatList("지능", 889);
		statList.addStatList("암속깍", 91);
		statList.addStatList("%마방깍_스킬", 25);
		temp.setBuff("장판+암속깍", "달계마스터+3", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 708);
		statList.addStatList("지능", 708);
		statList.addStatList("암속깍", 68);
		statList.addStatList("%마방깍_스킬", 38.75);
		temp.setBuff("장판+암속깍", "달계마스터+29/영지", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 708);
		statList.addStatList("지능", 708);
		statList.addStatList("암속깍", 68);
		statList.addStatList("%마방깍_스킬", 40);
		temp.setBuff("장판+암속깍", "달계마스터+30/영지", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 70);
		temp.setBuff("툼트앵", "13레벨(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 80);
		temp.setBuff("툼트앵", "15레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 180);
		temp.setBuff("툼트앵", "17레벨+망무9", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 190);
		temp.setBuff("툼트앵", "18레벨+망무9", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 200);
		temp.setBuff("툼트앵", "19레벨+망무9", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("암속깍", 210);
		temp.setBuff("툼트앵", "20레벨+망무9", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////마도
		temp = new PartyCharacter("마도", Job.WITCH);
		statList = new StatusList();
		statList.addStatList("지능뻥", 10);
		statList.addStatList("지능", 823);
		temp.setBuff("대파+도서관", "17레벨+선구6", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("지능뻥", 10);
		statList.addStatList("지능", 939);
		temp.setBuff("대파+도서관", "20레벨+선구6", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 45);
		statList.addStatList("수속깍", 45);
		statList.addStatList("명속깍", 45);
		statList.addStatList("암속깍", 45);
		statList.addStatList("%물방깍_스킬", 16.7);
		statList.addStatList("%마방깍_스킬", 18);
		temp.setBuff("디버프", "달계마스터", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 49);
		statList.addStatList("수속깍", 49);
		statList.addStatList("명속깍", 49);
		statList.addStatList("암속깍", 49);
		statList.addStatList("%물방깍_스킬", 18.3);
		statList.addStatList("%마방깍_스킬", 20);
		temp.setBuff("디버프", "달계마스터+2", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
	
		/////퇴마
		temp = new PartyCharacter("퇴마사", Job.EXORCIST);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 12);
		statList.addStatList("%마방깍_스킬", 12);
		temp.setBuff("현무", "10레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 24);
		statList.addStatList("%마방깍_스킬", 24);
		temp.setBuff("현무", "20레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 32);
		statList.addStatList("%마방깍_스킬", 32);
		temp.setBuff("현무", "20레벨+구속", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 36.3);
		statList.addStatList("%마방깍_스킬", 36.3);
		temp.setBuff("현무", "극세팅", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 8);
		statList.addStatList("%마방깍_스킬", 8);
		temp.setBuff("기타방깍", "기타방깍", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);

		/////엘븐
		temp = new PartyCharacter("엘븐", Job.ELVENKNIGHT);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 30);
		temp.setBuff("워크라이", "30레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 33);
		temp.setBuff("워크라이", "30레벨+균차", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 7.6);
		temp.setBuff("압도", "36레벨(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 7.8);
		temp.setBuff("압도", "38레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 8);
		temp.setBuff("압도", "40레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);		
		
		/////디트
		temp = new PartyCharacter("디트", Job.LAUNCHER_M);
		statList = new StatusList();
		statList.addStatList("크리저항감소", 23.2);
		temp.setBuff("서치아이", "15레벨(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("크리저항감소", 26.4);
		temp.setBuff("서치아이", "17레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("크리저항감소", 29.6);
		temp.setBuff("서치아이", "17레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////스핏
		temp = new PartyCharacter("스핏", Job.SPITFIRE_F);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 10);
		statList.addStatList("%마방깍_스킬", 10);
		temp.setBuff("강화탄(무)", "10레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 12);
		statList.addStatList("%마방깍_스킬", 12);
		temp.setBuff("강화탄(무)", "12레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("%물방깍_스킬", 14);
		statList.addStatList("%마방깍_스킬", 14);
		temp.setBuff("강화탄(무)", "14레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////여렝
		temp = new PartyCharacter("로제", Job.RANGER_F);
		statList = new StatusList();
		statList.addStatList("크증버프", 14);
		temp.setBuff("킬 포인트", "6레벨(달계마스터)", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("크증버프", 18);
		temp.setBuff("킬 포인트", "8레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("크증버프", 22);
		temp.setBuff("킬 포인트", "10레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////인파
		temp = new PartyCharacter("인파", Job.INFIGHTER);
		statList = new StatusList();
		statList.addStatList("힘", 850);
		statList.addStatList("물크", 15);
		temp.setBuff("윌드라이버", "10레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 925);
		statList.addStatList("물크", 16);
		temp.setBuff("윌드라이버", "11레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 1000);
		statList.addStatList("물크", 17);
		temp.setBuff("윌드라이버", "12레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("힘", 1075);
		statList.addStatList("물크", 18);
		temp.setBuff("윌드라이버", "13레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////스파
		temp = new PartyCharacter("용독", Job.STREETFIGHTER_F);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 30);
		temp.setBuff("천라지망", "천라지망", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		temp = new PartyCharacter("천수", Job.STREETFIGHTER_M);
		statList = new StatusList();
		statList.addStatList("증뎀버프", 25);
		temp.setBuff("그물 투척", "그물 투척", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
		
		/////엘마
		temp = new PartyCharacter("엘마", Job.ELEMENTALMASTER);
		statList = new StatusList();
		statList.addStatList("화속깍", 85);
		statList.addStatList("수속깍", 85);
		statList.addStatList("명속깍", 85);
		statList.addStatList("암속깍", 85);
		temp.setBuff("원소 집중", "15레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 89);
		statList.addStatList("수속깍", 89);
		statList.addStatList("명속깍", 89);
		statList.addStatList("암속깍", 89);
		temp.setBuff("원소 집중", "16레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 93);
		statList.addStatList("수속깍", 93);
		statList.addStatList("명속깍", 93);
		statList.addStatList("암속깍", 93);
		temp.setBuff("원소 집중", "17레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 97);
		statList.addStatList("수속깍", 97);
		statList.addStatList("명속깍", 97);
		statList.addStatList("암속깍", 97);
		temp.setBuff("원소 집중", "18레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 101);
		statList.addStatList("수속깍", 101);
		statList.addStatList("명속깍", 101);
		statList.addStatList("암속깍", 101);
		temp.setBuff("원소 집중", "19레벨", statList, CalculatorVersion.VER_1_1_a);
		statList = new StatusList();
		statList.addStatList("화속깍", 105);
		statList.addStatList("수속깍", 105);
		statList.addStatList("명속깍", 105);
		statList.addStatList("암속깍", 105);
		temp.setBuff("원소 집중", "20레벨", statList, CalculatorVersion.VER_1_1_a);
		partyList.add(temp);
	}
}
