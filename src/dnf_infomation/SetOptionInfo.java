package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_class.SetOption;

public class SetOptionInfo {
	public static void getInfo(LinkedList<SetOption> setOptionList)
	{
		SetOption temp;
		
		//////////에픽
		/////천
		//닼고
		temp = new SetOption(SetName.DARKGOTH, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("암속", new ElementInfo(true, 15), false, true);
		temp.dStat.addStatList("암추뎀", 10, false, true);
		temp.dStat.addStatList("암속깍", 30, false, true);
		setOptionList.add(temp);
		//불마력
		temp = new SetOption(SetName.BURNINGSPELL, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("마공", 480);
		temp.vStat.addStatList("독공", 550);
		temp.dStat.addStatList("지능뻥", 18, false, true);
		setOptionList.add(temp);
		//드로퍼
		temp = new SetOption(SetName.DROPPER, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("모속강", 50, false, true);
		temp.dStat.addStatList("지능", 400, false, true);
		temp.dStat.addStatList("스증뎀", 18, false, true);
		temp.dStat.addStatList("%마방깍_템", 20, false, true);
		setOptionList.add(temp);
		//오기일
		temp = new SetOption(SetName.OGGEILL, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 20);
		temp.dStat.addStatList("물공뻥", 10);
		temp.dStat.addStatList("마공뻥", 10);
		temp.dStat.addStatList("독공뻥", 10);
		setOptionList.add(temp);
		temp = new SetOption(SetName.OGGEILL, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 35);
		temp.dStat.addStatList("힘뻥", 10);
		temp.dStat.addStatList("지능뻥", 10);
		temp.dStat.addStatList("모공증", 25);
		setOptionList.add(temp);
		//게슈펜슈트
		temp = new SetOption(SetName.GESPENST, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모공증", 20);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GESPENST, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모공증", 35);
		setOptionList.add(temp);

		/////가죽
		//카멜
		temp = new SetOption(SetName.CHAMELEON, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물공", 92);
		temp.vStat.addStatList("마공", 92);
		temp.vStat.addStatList("독공", 92);
		temp.dStat.addStatList("힘", 300, false, true);
		temp.dStat.addStatList("지능", 300, false, true);
		temp.dStat.addStatList("추뎀", 30, false, true);
		setOptionList.add(temp);
		//암살
		temp = new SetOption(SetName.ASSASSIN, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 125);
		temp.vStat.addStatList("지능", 125);
		temp.dStat.addStatList("힘뻥", 10, false, true);
		temp.dStat.addStatList("지능뻥", 10, false, true);
		temp.dStat.addStatList("추뎀", 4, false, true);
		temp.dStat.addStatList("추뎀", 4, false, true);
		temp.dStat.addStatList("추뎀", 35, false, true);
		temp.explanation.add("딜러님 스겜좀요 ㅡㅡ");
		temp.explanation.add(".....");
		setOptionList.add(temp);
		//택틱
		temp = new SetOption(SetName.TACTICAL, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘", 500);
		temp.dStat.addStatList("지능", 500);
		temp.dStat.addStatList("추뎀", 55, true);
		setOptionList.add(temp);
		//신사
		temp = new SetOption(SetName.BLACKFORMAL, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물공", 287);
		temp.vStat.addStatList("마공", 287);
		temp.vStat.addStatList("독공", 287);
		setOptionList.add(temp);
		temp = new SetOption(SetName.BLACKFORMAL, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 25);
		temp.vStat.addStatList("마크", 25);
		temp.dStat.addStatList("힘뻥", 5);
		temp.dStat.addStatList("지능뻥", 5);
		temp.vStat.addStatList("증뎀", 33);
		setOptionList.add(temp);
		//핀드
		temp = new SetOption(SetName.FIENDVENATOR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물공", 74);
		temp.vStat.addStatList("마공", 74);
		temp.vStat.addStatList("독공", 74);
		temp.vStat.addStatList("모속강", 35);
		setOptionList.add(temp);
		temp = new SetOption(SetName.FIENDVENATOR, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("물공뻥", 8);
		temp.dStat.addStatList("마공뻥", 8);
		temp.dStat.addStatList("독공뻥", 8);
		temp.fStat.statList.add("핀드");
		temp.explanation.add("공격 시 가장 높은 속성 25% 속성 추가 데미지");
		setOptionList.add(temp);
		
		/////경갑
		//섭마
		temp = new SetOption(SetName.SUBMARINE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("화속", 50);
		temp.vStat.addStatList("화추뎀", 8);
		setOptionList.add(temp);
		//자수
		temp = new SetOption(SetName.NATURALGARDIAN, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList(Element_type.FIRE, 50, true, false, true);
		temp.dStat.addStatList(Element_type.WATER, 50, true, false, true);
		temp.dStat.addStatList(Element_type.LIGHT, 50, true, false, true);
		temp.dStat.addStatList(Element_type.DARKNESS, 50, true, false, true);
		temp.dStat.addStatList("화추뎀", 25, false, true);
		temp.dStat.addStatList("수추뎀", 25, false, true);
		temp.dStat.addStatList("명추뎀", 25, false, true);
		temp.dStat.addStatList("암추뎀", 25, false, true);
		temp.dStat.addStatList("추뎀", 45, false, true);
		temp.fStat.statList.add("자수");
		temp.explanation.add("추뎀 옵션에 2개 이상이 체크될 경우, 모든 추뎀의 평균값으로 설정됩니다");
		setOptionList.add(temp);
		//아이실드
		temp = new SetOption(SetName.EYESHIELD, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 150);
		temp.vStat.addStatList("지능", 150);
		temp.dStat.addStatList("물공뻥", 40, false, true);
		temp.dStat.addStatList("마공뻥", 40, false, true);
		temp.dStat.addStatList("독공뻥", 40, false, true);
		temp.dStat.addStatList("고정물방깍", 10000, false, true);
		setOptionList.add(temp);
		//황갑
		temp = new SetOption(SetName.GOLDENARMOR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 25);
		temp.vStat.addStatList("추뎀", 15);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GOLDENARMOR, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("크증뎀", 35);
		setOptionList.add(temp);
		//초대륙
		temp = new SetOption(SetName.SUPERCONTINENT, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 22);
		setOptionList.add(temp);
		temp = new SetOption(SetName.SUPERCONTINENT, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모공증", 35);
		setOptionList.add(temp);
		
		/////중갑
		//미닼
		temp = new SetOption(SetName.DARKHOLE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 30);
		temp.vStat.addStatList("추뎀", 20, false, true);
		setOptionList.add(temp);
		//거미
		temp = new SetOption(SetName.SPIDERQUEEN, 4, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("방깍제한 36000을 위한 더미셋옵입니다");
		temp.fStat.statList.add("거미");
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.SPIDERQUEEN, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("증뎀", new StatusInfo(45));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		temp.dStat.addStatList("물공뻥", 12);
		temp.dStat.addStatList("마공뻥", 12);
		temp.dStat.addStatList("독공뻥", 12);
		setOptionList.add(temp);
		//금계
		temp = new SetOption(SetName.FORBIDDENCONTRACT, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘", 300);
		temp.dStat.addStatList("지능", 300);
		temp.dStat.addStatList("물공뻥", 20);
		temp.dStat.addStatList("마공뻥", 20);
		temp.dStat.addStatList("독공뻥", 20);
		temp.dStat.addStatList("물크", 30, false, true);
		temp.dStat.addStatList("마크", 30, false, true);
		temp.dStat.addStatList("크증뎀", 20, false, true);
		setOptionList.add(temp);
		//고대전쟁
		temp = new SetOption(SetName.ANCIENTWAR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addSkillRange(50, 50, 3, false);
		temp.vStat.addStatList("증뎀", 25);
		setOptionList.add(temp);
		temp = new SetOption(SetName.ANCIENTWAR, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addSkillRange(85, 85, 2, false);
		temp.vStat.addStatList("크증뎀", 35);
		temp.explanation.add("ㅈ년ㅈ쟁");
		setOptionList.add(temp);
		//나가라자
		temp = new SetOption(SetName.NAGARAJA, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("모공증", 25, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.NAGARAJA, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 25);
		temp.dStat.addStatList("스증", 35, false, true);
		setOptionList.add(temp);
		
		/////판금
		//인피니티
		temp = new SetOption(SetName.INFINITY, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addSkillRange(60, 70, 1, false);
		temp.dStat.addStatList("물공뻥", 15, false, true);
		temp.dStat.addStatList("마공뻥", 15, false, true);
		temp.dStat.addStatList("독공뻥", 15, false, true);
		temp.vStat.addStatList("추뎀", 20);
		setOptionList.add(temp);
		//마소
		temp = new SetOption(SetName.MAELSTORM, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.dStat.addSkill_damage("기본기 숙련", 20);
		temp.vStat.addStatList("추뎀", 27);
		setOptionList.add(temp);
		//풀플
		temp = new SetOption(SetName.FULLPLATE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 220);
		temp.vStat.addStatList("지능", 220);
		temp.dStat.addStatList("물공", 300, true, true);
		temp.dStat.addStatList("마공", 300, true, true);
		temp.dStat.addStatList("독공", 300, true, true);		
		temp.dStat.addStatList("추뎀", 40);
		setOptionList.add(temp);
		//센츄리온
		temp = new SetOption(SetName.CENTURYONHERO, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 100);
		temp.vStat.addStatList("지능", 100);
		temp.vStat.addStatList("추뎀", 25);
		setOptionList.add(temp);
		temp = new SetOption(SetName.CENTURYONHERO, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 200);
		temp.vStat.addStatList("지능", 200);
		temp.vStat.addStatList("증뎀", 55);
		setOptionList.add(temp);
		//칠죄종
		temp = new SetOption(SetName.SEVENSINS, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추크증", 22);
		temp.vStat.addSkill_damage("영광의 축복", 20);
		setOptionList.add(temp);
		temp = new SetOption(SetName.SEVENSINS, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모공증", 40);
		temp.explanation.add("상시 슈퍼아머 발생, 자신을 포함한 랜덤한 파티원에게 20초간 슈퍼아머 시전");
		setOptionList.add(temp);
		
		
		/////////////악세서리
		//슈스
		temp = new SetOption(SetName.SUPERSTAR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 15);
		temp.dStat.addStatList("추뎀", 10, false, true);
		setOptionList.add(temp);
		//얼공
		temp = new SetOption(SetName.ICEQUEEN, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("수속강", 50);
		temp.vStat.addStatList("추뎀", 18);
		setOptionList.add(temp);
		//정마
		temp = new SetOption(SetName.REFINEDSTONE, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 18);
		temp.vStat.addStatList("물크", 12);
		temp.vStat.addStatList("마크", 12);
		temp.vStat.addStatList("추뎀", 20);
		setOptionList.add(temp);
		//하늘의 여행자
		temp = new SetOption(SetName.SKYTRAVELER, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 30);
		setOptionList.add(temp);
		//황홀경
		temp = new SetOption(SetName.ECSTATICSENCE, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("증뎀", 20);
		temp.vStat.addStatList("크증뎀", 20);
		setOptionList.add(temp);
		//군주
		temp = new SetOption(SetName.MONARCHOFHEVELON, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 200);
		temp.vStat.addStatList("지능", 200);
		temp.dStat.addStatList("스증뎀", 20, false, true);
		temp.fStat.statList.add("헤블론");
		temp.explanation.add("스증뎀 옵션 선택시 스증뎀 옵션 적용");
		temp.explanation.add("스증뎀 옵션 선택 해제시 가장 높은 속성 추가 데미지 15% 옵션 적용");
		setOptionList.add(temp);
		
		//////////////레전더리
		///////결장
		temp = new SetOption(SetName.REAL_PROFIGHTER_ACCESSORY, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("크증뎀", 15, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_SPECIALEQUIP, 2, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("독공", 165);
		temp.vStat.addStatList("모속강", 18);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_FABRIC, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.vStat.addStatList("모속강", 12);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_HARMOR, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.vStat.addStatList("모속강", 12);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_LEATHER, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.vStat.addStatList("모속강", 12);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_MAIL, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.vStat.addStatList("모속강", 12);
		setOptionList.add(temp);
		temp = new SetOption(SetName.REAL_PROFIGHTER_PLATE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 10);
		temp.vStat.addStatList("마크", 10);
		temp.vStat.addStatList("모속강", 12);
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.GODOFFIGHT, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 3);
		temp.vStat.addStatList("마크", 3);
		temp.vStat.addStatList("크증뎀", 15);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GODOFFIGHT, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘뻥", 5, false, true);
		temp.dStat.addStatList("지능뻥", 5, false, true);
		temp.vStat.addStatList("증뎀", 20);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GODOFFIGHT, 6, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("물크", 7);
		temp.vStat.addStatList("마크", 7);
		temp.vStat.addStatList("모속강", 20);
		setOptionList.add(temp);
		
		/////////////퀘전더리
		//위영
		temp = new SetOption(SetName.GREATGLORY, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘", 100, false, true);
		temp.dStat.addStatList("지능", 100, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GREATGLORY, 5, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘뻥", 5);
		temp.dStat.addStatList("지능뻥", 5);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GREATGLORY, 6, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("힘뻥", 3);
		temp.dStat.addStatList("지능뻥", 3);
		setOptionList.add(temp);
		//비탄
		temp = new SetOption(SetName.DEVASTEDGRIEF, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("증뎀", 20, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.DEVASTEDGRIEF, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 15);
		temp.explanation.add("증뎀옵션은 3셋옵션을 키세여");
		setOptionList.add(temp);
		temp = new SetOption(SetName.DEVASTEDGRIEF, 6, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("슈아쳐가 됩니다");
		setOptionList.add(temp);
		//그라시아
		temp = new SetOption(SetName.GRACIA, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 50);
		temp.vStat.addStatList("힘", 50);
		temp.vStat.addStatList("명속강", 10);
		temp.dStat.addStatList(Element_type.LIGHT, 0, true, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GRACIA, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("명속강", 12);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GRACIA, 6, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("명추뎀", 6);
		setOptionList.add(temp);
		//비명
		temp = new SetOption(SetName.BURIEDSCREAM, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 10);
		temp.dStat.addStatList("%물방깍_템", 10, false, true);
		temp.dStat.addStatList("%마방깍_템", 10, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.BURIEDSCREAM, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 20);
		setOptionList.add(temp);
		temp = new SetOption(SetName.BURIEDSCREAM, 6, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("고정물방깍", 12000, false, true);
		temp.dStat.addStatList("고정마방깍", 12000, false, true);
		setOptionList.add(temp);
		//해신
		temp = new SetOption(SetName.CURSEOFSEAGOD, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("수속강", 12);
		temp.vStat.addStatList("추뎀", 3);
		temp.dStat.addStatList(Element_type.WATER, 0, true, false, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.CURSEOFSEAGOD, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 17);
		setOptionList.add(temp);
		temp = new SetOption(SetName.CURSEOFSEAGOD, 6, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("공격 시 5% 확률로 저주받은 해신의 격노 발생");
		setOptionList.add(temp);
		//질병
		temp = new SetOption(SetName.ROOTOFDISEASE, 3, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("500px 범위 내의 모든 적 상태이상 내성 50 감소");
		setOptionList.add(temp);
		temp = new SetOption(SetName.ROOTOFDISEASE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 12);
		temp.dStat.addStatList("%물방깍_템", 10, true);
		temp.dStat.addStatList("%마방깍_템", 10, true);
		setOptionList.add(temp);
		temp = new SetOption(SetName.ROOTOFDISEASE, 6, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("추뎀", 5, true);
		setOptionList.add(temp);
		//로맨티스트
		temp = new SetOption(SetName.ROMANTICE, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 5);
		setOptionList.add(temp);
		temp = new SetOption(SetName.ROMANTICE, 5, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", 100);
		temp.vStat.addStatList("지능", 100);
		temp.vStat.addSkillRange(48, 48, 1, false);
		temp.vStat.addSkillRange(50, 50, 2, false);
		setOptionList.add(temp);
		temp = new SetOption(SetName.ROMANTICE, 6, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("추뎀", 12);
		setOptionList.add(temp);
		//거형
		temp = new SetOption(SetName.HUGEFORM, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("스증뎀", 10);
		temp.dStat.addStatList("힘뻥", 10, false, true);
		temp.dStat.addStatList("지능뻥", 10, false, true);
		setOptionList.add(temp);
		//루크
		temp = new SetOption(SetName.TACITCONSTRUCTOR, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("모공증", 10, false, true);
		temp.dStat.addStatList("모공증", 15, false, true);
		temp.explanation.add("모든 공격력 15% 증가 옵션의 조건 : 루크 던전");
		setOptionList.add(temp);
		//길던
		temp = new SetOption(SetName.GUILDACCESSORY_FIRE, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("화속강", 10);
		setOptionList.add(temp);
		temp = new SetOption(SetName.GUILDACCESSORY_WATER, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("수속강", 10);
		setOptionList.add(temp);

		///////에컨
		temp = new SetOption(SetName.EKERN, 3, CalculatorVersion.VER_1_2_a);
		temp.dStat.addStatList("증뎀", 15, true, true);
		temp.dStat.addStatList("크증뎀", 15, true, true);
		temp.dStat.addStatList("추뎀", 13, true, true);
		temp.dStat.addStatList("힘뻥", 15, true, true);
		temp.dStat.addStatList("지능뻥", 15, true, true);
		temp.vStat.addStatList("힘", 165, true, true);
		temp.vStat.addStatList("지능", 165, true, true);
		temp.dStat.addStatList("물공뻥", 15, true, true);
		temp.dStat.addStatList("마공뻥", 15, true, true);
		temp.dStat.addStatList("독공뻥", 15, true, true);
		temp.vStat.addStatList("모속강", 44, true, true);
		temp.vStat.addSkillRange(1, 30, 1, false, false, true);
		temp.explanation.add("본인 옵션에 맞게 수정이 필요합니다");
		temp.explanation.add("6셋 극옵 : 스탯뻥 15% + 물마공 15% + 추뎀 13%(혹은 증/크증 15%) + 모속강 11");
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		for(StatusAndName s : temp.dStat.statList)
			s.enabled=false;
		setOptionList.add(temp);
		/////황혼의 가도
		temp = new SetOption(SetName.TWIlIGHT, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("모속강", 40);
		setOptionList.add(temp);

		/////////////아바타
		//레압
		temp = new SetOption(SetName.RAREAVATAR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.RAREAVATAR, 5, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("데미지 증가 효과 없음");
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.RAREAVATAR, 8, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		setOptionList.add(temp);
		
		//상압
		temp = new SetOption(SetName.AVATAR, 3, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.AVATAR, 5, CalculatorVersion.VER_1_2_a);
		temp.explanation.add("데미지 증가 효과 없음");
		setOptionList.add(temp);
		
		temp = new SetOption(SetName.AVATAR, 8, CalculatorVersion.VER_1_2_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		setOptionList.add(temp);
	}
}
