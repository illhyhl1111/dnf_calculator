package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_class.SetOption;

public class SetOptionInfo {
	public static void getInfo(HashSet<SetOption> equipList)
	{
		SetOption temp;
		
		//////////에픽
		/////천
		//닼고
		temp = new SetOption("다크 고스 세트", SetName.DARKGOTH, 5);
		temp.dStat.addStatList("암속", new ElementInfo(true, 0), false, true);
		temp.dStat.addStatList("암추뎀", 10, false, true);
		temp.dStat.addStatList("암속깍", 30, false, true);
		equipList.add(temp);
		//불마력
		temp = new SetOption("불타오르는 마력 세트", SetName.BURNINGSPELL, 5);
		temp.vStat.addStatList("마공", 480);
		temp.vStat.addStatList("물공", 550);
		temp.dStat.addStatList("지능뻥", 18, false, true);
		equipList.add(temp);
		//드로퍼
		temp = new SetOption("엘레멘탈 드롭퍼 세트", SetName.DROPPER, 5);
		temp.dStat.addStatList("모속강", 50, false, true);
		temp.dStat.addStatList("지능", 400, false, true);
		temp.dStat.addStatList("스증뎀", 18, false, true);
		temp.dStat.addStatList("%마방깍_템", 20, false, true);
		equipList.add(temp);
		//오기일
		temp = new SetOption("오기일의 꽃 세트", SetName.OGGEILL, 3);
		temp.vStat.addStatList("모속강", 20);
		temp.dStat.addStatList("물리마스터리", 10);
		temp.dStat.addStatList("마법마스터리", 10);
		temp.dStat.addStatList("독공뻥", 10);
		equipList.add(temp);
		temp = new SetOption("오기일의 꽃 세트", SetName.OGGEILL, 5);
		temp.vStat.addStatList("모속강", 40);
		temp.dStat.addStatList("힘뻥", 10);
		temp.dStat.addStatList("지능뻥", 10);
		temp.dStat.addStatList("모공증", 25);
		equipList.add(temp);
		//게슈펜슈트
		temp = new SetOption("게슈펜슈트의 환각 세트", SetName.GESPENST, 5);
		temp.explanation.add("미구현");
		equipList.add(temp);
		
		//////////에픽
		/////가죽
		//카멜
		temp = new SetOption("무음 카멜레온 세트", SetName.CHAMELEON, 5);
		temp.vStat.addStatList("물공", 80);
		temp.vStat.addStatList("마공", 80);
		temp.vStat.addStatList("독공", 92);
		temp.dStat.addStatList("힘", 300, false, true);
		temp.dStat.addStatList("지능", 300, false, true);
		temp.dStat.addStatList("추뎀", 30, false, true);
		equipList.add(temp);
		//암살
		temp = new SetOption("암살자의 마음가짐 세트", SetName.ASSASSIN, 5);
		temp.vStat.addStatList("힘", 125);
		temp.vStat.addStatList("지능", 125);
		temp.dStat.addStatList("힘뻥", 10, false, true);
		temp.dStat.addStatList("지능뻥", 10, false, true);
		temp.dStat.addStatList("추뎀", 4, false, true);
		temp.dStat.addStatList("추뎀", 4, false, true);
		temp.dStat.addStatList("추뎀", 35, false, true);
		temp.explanation.add("딜러님 방넘어갈때 스겜좀 해주시면 안됨?");
		temp.explanation.add("아니 그게");
		equipList.add(temp);
		//택틱
		temp = new SetOption("웨슬리의 전술 세트", SetName.TACTICAL, 5);
		temp.dStat.addStatList("힘", 500);
		temp.dStat.addStatList("지능", 500);
		temp.dStat.addStatList("추뎀", 55);
		equipList.add(temp);
		//신사
		temp = new SetOption("마계의 정중한 신사 세트", SetName.BLACKFORMAL, 3);
		temp.vStat.addStatList("물공", 250);
		temp.vStat.addStatList("마공", 250);
		temp.vStat.addStatList("독공", 287);
		equipList.add(temp);
		temp = new SetOption("마계의 정중한 신사 세트", SetName.BLACKFORMAL, 5);
		temp.vStat.addStatList("물크", 25);
		temp.vStat.addStatList("마크", 25);
		temp.dStat.addStatList("힘뻥", 5);
		temp.dStat.addStatList("지능뻥", 5);
		temp.vStat.addStatList("증뎀", 33);
		equipList.add(temp);
		//핀드
		temp = new SetOption("핀드 베나토르 세트", SetName.FIENDVENATOR, 5);
		temp.explanation.add("미구현");
		equipList.add(temp);
		
		/////경갑
		//섭마
		temp = new SetOption("서브마린 볼케이노 세트", SetName.SUBMARINE, 5);
		temp.vStat.addStatList("화속", 50);
		temp.vStat.addStatList("화추뎀", 8);
		equipList.add(temp);
		//자수
		temp = new SetOption("자연의 수호자", SetName.NATURALGARDIAN, 5);
		temp.dStat.addStatList("화속", 50, false, true);
		temp.dStat.addStatList("수속", 50, false, true);
		temp.dStat.addStatList("명속", 50, false, true);
		temp.dStat.addStatList("암속", 50, false, true);
		temp.dStat.addStatList("화추뎀", 25, false, true);
		temp.dStat.addStatList("수추뎀", 25, false, true);
		temp.dStat.addStatList("명추뎀", 25, false, true);
		temp.dStat.addStatList("암추뎀", 25, false, true);
		temp.dStat.addStatList("추뎀", 45, false, true);
		temp.fStat.statList.add(new FunctionStat() {
			private static final long serialVersionUID = 6285990968608784856L;
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				StatusList statList = new StatusList();
				SetOption option=null;
				int count = 0;
				for(SetOption set : character.userItemList.setOptionList){
					if(set.setName==SetName.NATURALGARDIAN){
						option = set;
						break;
					}
				}
				
				if(option.dStat.findStat(StatList.DAM_ADD_FIRE).enabled){
					count++;
					statList.addStatList("화추뎀", -20);
				}
				else statList.addStatList("화추뎀", 5);
				if(option.dStat.findStat(StatList.DAM_ADD_WATER).enabled){
					count++;
					statList.addStatList("수추뎀", -20);
				}
				else statList.addStatList("수추뎀", 5);
				if(option.dStat.findStat(StatList.DAM_ADD_LIGHT).enabled){
					count++;
					statList.addStatList("명추뎀", -20);
				}
				else statList.addStatList("명추뎀", 5);
				if(option.dStat.findStat(StatList.DAM_ADD_DARKNESS).enabled){
					count++;
					statList.addStatList("암추뎀", -20);
				}
				else statList.addStatList("암추뎀", 5);
				if(option.dStat.findStat(StatList.DAM_ADD).enabled){
					count++;
					statList.addStatList("추뎀", -36);
				}
				else statList.addStatList("추뎀", 9);
				
				if(count>1) return statList;
				else return new StatusList();
			}
		});
		temp.explanation.add("추뎀 옵션에 2개 이상이 체크될 경우, 모든 추뎀의 평균값으로 설정됩니다");
		equipList.add(temp);
		//아이실드
		temp = new SetOption("아이실드 러데이니언 세트", SetName.EYESHIELD, 5);
		temp.vStat.addStatList("힘", 150);
		temp.vStat.addStatList("지능", 150);
		temp.dStat.addStatList("물리마스터리", 40, false, true);
		temp.dStat.addStatList("마법마스터리", 40, false, true);
		temp.dStat.addStatList("독공뻥", 40, false, true);
		temp.dStat.addStatList("고정물방깍", 10000, false, true);
		equipList.add(temp);
		//황갑
		temp = new SetOption("찬란한 명예의 상징 세트", SetName.GOLDENARMOR, 3);
		temp.vStat.addStatList("모속강", 25);
		temp.vStat.addStatList("추뎀", 15);
		equipList.add(temp);
		temp = new SetOption("찬란한 명예의 상징 세트", SetName.GOLDENARMOR, 5);
		temp.vStat.addStatList("크증뎀", 35);
		equipList.add(temp);
		//초대륙
		temp = new SetOption("초대륙의 붕괴 세트", SetName.SUPERCONTINENT, 5);
		temp.explanation.add("미구현");
		equipList.add(temp);
		
		/////중갑
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 4);
		temp.explanation.add("방깍제한 36000을 위한 더미셋옵입니다");
		temp.fStat.statList.add(new FunctionStat() {
			private static final long serialVersionUID = 6285990968608784856L;
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				int num=0;
				StatusList statList = new StatusList();
				if(character.getEquipmentList().get(Equip_part.ROBE).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.ROBE).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.TROUSER).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.TROUSER).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.SHOULDER).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.SHOULDER).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.BELT).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.BELT).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.SHOES).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.SHOES).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(num==5) statList.addStatList("고정물방깍", -24000);
				if(num==5) statList.addStatList("고정마방깍", -24000);
				if(num==4) statList.addStatList("고정물방깍", -12000);
				if(num==4) statList.addStatList("고정마방깍", -12000);
				
				return statList;
			}
		});
		equipList.add(temp);
		
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 5);
		temp.vStat.addStatList("증뎀", new StatusInfo(45));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		equipList.add(temp);
		//금계
		temp = new SetOption("금지된 계약 세트", SetName.FORBIDDENCONTRACT, 5);
		temp.dStat.addStatList("힘", 300);
		temp.dStat.addStatList("지능", 300);
		temp.dStat.addStatList("물리마스터리", 20);
		temp.dStat.addStatList("마법마스터리", 20);
		temp.dStat.addStatList("독공뻥", 20);
		temp.dStat.addStatList("물크", 30, false, true);
		temp.dStat.addStatList("마크", 30, false, true);
		temp.dStat.addStatList("크증뎀", 30, false, true);
		equipList.add(temp);
		//고대전쟁
		temp = new SetOption("고대전쟁의 여신 세트", SetName.ANCIENTWAR, 3);
		temp.vStat.addSkillRange(50, 50, 3, false);
		temp.vStat.addStatList("증뎀", 25);
		equipList.add(temp);
		temp = new SetOption("고대전쟁의 여신 세트", SetName.ANCIENTWAR, 5);
		temp.vStat.addSkillRange(85, 85, 2, false);
		temp.vStat.addStatList("크증뎀", 35);
		temp.explanation.add("ㅈ년ㅈ쟁");
		equipList.add(temp);
		//나가라자
		temp = new SetOption("나가라자의 탐식 세트", SetName.NAGARAJA, 5);
		temp.explanation.add("미구현");
		temp.explanation.add("중갑에픽을 살");
		equipList.add(temp);
		
		/////판금
		//인피니티
		temp = new SetOption("인피니티 레퀴엠 세트", SetName.INFINITY, 5);
		temp.vStat.addSkillRange(60, 70, 1, false);
		temp.dStat.addStatList("물리마스터리", 15, false, true);
		temp.dStat.addStatList("마법마스터리", 15, false, true);
		temp.dStat.addStatList("독공뻥", 15, false, true);
		temp.vStat.addStatList("추뎀", 20);
		equipList.add(temp);
		//마소
		temp = new SetOption("마력의 소용돌이 세트", SetName.MAELSTORM, 5);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.dStat.addSkill_damage("기본기 숙련", 20);
		temp.vStat.addStatList("추뎀", 27);
		equipList.add(temp);
		//풀플
		temp = new SetOption("풀 플레이트 아머 세트", SetName.FULLPLATE, 5);
		temp.vStat.addStatList("힘", 220);
		temp.vStat.addStatList("지능", 220);
		temp.dStat.addStatList("물공뻥", 300, true, true);
		temp.dStat.addStatList("마공뻥", 300, true, true);
		temp.dStat.addStatList("독공뻥", 300, true, true);		
		temp.dStat.addStatList("추뎀", 40);
		equipList.add(temp);
		//센츄리온
		temp = new SetOption("센츄리온 히어로 세트", SetName.CENTURYONHERO, 3);
		temp.vStat.addStatList("힘", 100);
		temp.vStat.addStatList("지능", 100);
		temp.vStat.addStatList("추뎀", 25);
		equipList.add(temp);
		temp = new SetOption("센츄리온 히어로 세트", SetName.CENTURYONHERO, 5);
		temp.vStat.addStatList("힘", 200);
		temp.vStat.addStatList("지능", 200);
		temp.vStat.addStatList("증뎀", 55);
		equipList.add(temp);
		//칠죄종
		temp = new SetOption("타락의 칠죄종 세트", SetName.SEVENSINS, 5);
		temp.explanation.add("미구현");
		equipList.add(temp);
		
		
		/////////////악세서리
		//슈스
		temp = new SetOption("슈퍼 스타 세트", SetName.SUPERSTAR, 3);
		temp.vStat.addStatList("추뎀", 15);
		temp.dStat.addStatList("추뎀", 10, false, true);
		equipList.add(temp);
		//얼공
		temp = new SetOption("얼음 공주의 숨결 세트", SetName.ICEQUEEN, 3);
		temp.vStat.addStatList("수속강", 50);
		temp.vStat.addStatList("추뎀", 18);
		equipList.add(temp);
		//정마
		temp = new SetOption("정제된 이계의 마석 세트", SetName.REFINEDSTONE, 3);
		temp.vStat.addStatList("모속강", 18);
		temp.vStat.addStatList("물크", 12);
		temp.vStat.addStatList("마크", 12);
		temp.vStat.addStatList("추뎀", 20);
		equipList.add(temp);
		//하늘의 여행자
		temp = new SetOption("하늘의 여행자 세트", SetName.SKYTRAVELER, 3);
		temp.vStat.addStatList("추뎀", 30);
		equipList.add(temp);
		//황홀경
		temp = new SetOption("오감의 황홀경 세트", SetName.ECSTATICSENCE, 3);
		temp.vStat.addStatList("증뎀", 20);
		temp.vStat.addStatList("크증뎀", 20);
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
