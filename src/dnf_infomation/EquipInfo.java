package dnf_infomation;

import java.util.Arrays;
import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_calculator.ElementInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;

public class EquipInfo {
	
	public static void getInfo(HashSet<Equipment> equipList, Object[] data) throws ParsingException
	{
		int i=0;
		String name=null;
		Item_rarity rarity=null;
		Equip_part part=null;
		Equip_type type=null;
		SetName setName=null;
		int level=0;
		String[] stat=null;
		
		Equipment equipment;
		Object temp="first";
		
		while(i<data.length)
		{
			try{
				name = (String) data[i++];
				
				//레어리티
				temp = data[i++];
				if(temp instanceof Item_rarity) rarity = (Item_rarity) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				//부위
				temp = data[i++];
				if(temp instanceof Equip_part) part = (Equip_part) temp;
				else if(temp.equals(""));	//이전 값 유지
				else if(temp.equals("--")){
					int order = part.order-1;
					part = Equip_part.getPartFromOrder(order);
				}
				else throw new ParsingException(i-1, temp);
				
				//재질
				temp = data[i++];
				if(temp instanceof Equip_type) type = (Equip_type) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				//셋옵
				temp = data[i++];
				if(temp instanceof SetName) setName = (SetName) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				//레벨
				temp = data[i++];
				if(temp instanceof Integer) level = (int) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				equipment = new Equipment(name, rarity, part, type, setName, level);
			
				//아이템 스탯
				while(true)
				{	
					temp = data[i++];
					if(temp==null) break;
					
					else if(temp instanceof FunctionStat)
						equipment.fStat.statList.add((FunctionStat) temp);
					
					else{
						stat = ((String)temp).split(" ");					
						switch(stat[0])
						{
						case "ㄷ": case "d":
							parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.dStat);
							break;
						case "설명":
							String explanation = ((String)temp).substring(3);
							equipment.explanation.add(explanation);
							break;
						case "ㅁ": case "v":
							parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.vStat);
							break;
						default:
							parseStat(stat, equipment.vStat);
							break;
						}
					}
				}
			}
			catch(Exception e)
			{
				throw new ParsingException(i-1, temp);
			}
			equipList.add(equipment);
		}
	}
	
	private static void parseStat(String[] data, StatusList list)
	{
		boolean changeable = false;
		boolean enableable = false;
		
		if(data.length>2 && (data[data.length-1].equals("가변") || data[data.length-2].equals("가변")) ) changeable = true;
		if(data.length>2 && (data[data.length-1].equals("선택") || data[data.length-2].equals("선택")) ) enableable = true;
		
		if(data[0].contains("부여"))
		{
			switch(data[0])
			{
			case "화속부여":
				list.addStatList("화속", new ElementInfo(true, 0));
				break;
			case "수속부여":
				list.addStatList("수속", new ElementInfo(true, 0));
				break;
			case "명속부여":
				list.addStatList("명속", new ElementInfo(true, 0));
				break;
			case "암속부여":
				list.addStatList("암속", new ElementInfo(true, 0));
				break;
			}
		}
		
		else if(data[0].contains("스킬"))
		{
			String[] skillRange = data[1].split("-");
			try{
				int start = Integer.valueOf(skillRange[0]);
				int end = start;
				if(skillRange.length>1) end = Integer.valueOf(skillRange[1]);
				boolean TP=false;
				if(data[0].equals("TP스킬")) TP=true;
				list.addSkillRange(start, end, Integer.valueOf(data[2]), TP);
			}
			catch(NumberFormatException e) {
				String skillName = data[1];
				for(int i=2; i<data.length-1; i++) skillName = skillName+" "+data[i];
				
				if(data[data.length-1].contains("%")){
					String inc = data[data.length-1].substring(0, data[data.length-1].length()-1);
					list.addSkill_damage(skillName, Integer.valueOf(inc));
				}
				else list.addSkill(data[1], Integer.valueOf(data[data.length-1]));
			}
		}
		else if(!data[1].contains("."))
			list.addStatList(data[0], Integer.valueOf(data[1]), changeable, enableable);
		else
			list.addStatList(data[0], Double.valueOf(data[1]), changeable, enableable);
	}
	
	public static Object[] equipInfo()
	{
		FunctionStat fStat[] = new FunctionStat[4];
		
		//익스포젼 헤비 각반
		fStat[0] = new FunctionStat() {
			private static final long serialVersionUID = -4590364678263064444L;

			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				if(equipment.dStat.statList.get(0).enabled && equipment.dStat.statList.get(1).enabled)
					equipment.dStat.statList.get(0).enabled=false;
				return new StatusList();
			}
		};
		
		//집척목, 암칼반
		fStat[1] = new FunctionStat() {
			private static final long serialVersionUID = 4036286523104766974L;

			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				int count=0;

				for(int i=0; i<4; i++)
					if(equipment.dStat.statList.get(i).enabled) count++;
				
				if(count>1){
					for(int i=0; i<3; i++)
						equipment.dStat.statList.get(i).enabled=false;
					equipment.dStat.statList.get(3).enabled=true;
				}
				return new StatusList();
			}
		};
		
		//황홀경
		fStat[2] = new FunctionStat() {
			private static final long serialVersionUID = -7780279158612957210L;
			
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				int num = equipment.getReinforce();
				if(num>12) num=12;
				StatusList statList = new StatusList();
				for(StatusAndName s: equipment.dStat.statList)
				statList.addStatList(s.name, new StatusInfo(num));
				return statList;
			}
		};
		
		//흑백마음
		fStat[3] = new FunctionStat() {
			private static final long serialVersionUID = -7780279158612957210L;
			
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				int[] stat = new int[4];
				int index=0;
				StatusList statList = new StatusList();
				try {
					stat[0]=(int) character.dungeonStatus.getStat("화속강");
					stat[1]=(int) character.dungeonStatus.getStat("수속강");
					stat[2]=(int) character.dungeonStatus.getStat("명속강");
					stat[3]=(int) character.dungeonStatus.getStat("암속강");
					
					for(int i=0; i<3; i++)
						if(stat[i]<stat[i+1]) index=i+1;
					
					switch(index)
					{
					case 0:
						statList.addStatList("화속강", 55);
						break;
					case 1:
						statList.addStatList("수속강", 55);
						break;
					case 2:
						statList.addStatList("명속강", 55);
						break;
					case 3:
						statList.addStatList("암속강", 55);
						break;
					}
				} catch (StatusTypeMismatch | UndefinedStatusKey e) {
					e.printStackTrace();
				}
				return statList;
			}
		};
		
		/* Format
		"", "", Equip_part.ROBE, "", SetName., ,
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "",
		"힘  가변", "지능  가변", null,
		*/
		
		
		Object[] data = new Object[] {
			
				////////////////////// 천
				//////////에픽
				/////닼고
				"다크 고스 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.FABRIC, SetName.DARKGOTH, 80,
				"지능 48 가변", "ㄷ 암 30 가변", "ㄷ 마공 150 가변", "ㄷ 독공 150 가변", "스킬 70 2", null,
				"다크 고스 하의", "", "--", "", "", "",
				"지능 48 가변", "ㄷ 암 30 가변", "ㄷ 지능 300 가변", "스킬 60 2", null,
				"다크 고스 숄더", "", "--", "", "", "",
				"지능 39 가변", "스킬 40-50 1", "ㄷ 암 30 가변", null,
				"다크 고스 벨트", "", "--", "", "", "",
				"지능 29 가변", "ㄷ 마크 20", "ㄷ 암 30 가변", null,
				"다크 고스 샌들", "", "--", "", "", "",
				"지능 29 가변", "ㄷ 암 30 가변", null,
				/////불마력
				"마나 번 로브", "", Equip_part.ROBE, "", SetName.BURNINGSPELL, 85,
				"지능 51 가변", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15 선택", null,
				"매직 번 트라우저", "", "--", "", "", "",
				"지능 48 가변", "ㄷ 크증뎀 15 선택", null,
				"스펠 번 숄더 패드", "", "--", "", "", "",
				"지능 41 가변", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", null,
				"엘리멘탈 번 새쉬", "", "--", "", "", "",
				"지능 31 가변", "ㄷ 마크 20 선택", null,
				"소울 번 슈즈", "", "--", "", "", "",
				"지능 31 가변", "ㄷ 모속강 20 선택", null,
				/////드로퍼
				"프리징 컷 로브", "", Equip_part.ROBE, "", SetName.DROPPER, 85,
				"지능 51 가변", "수속 12 가변", "마크 5", "ㄷ 수속깍 44 선택", null,
				"플레임 드랍 트라우저", "", "--", "", "", "",
				"지능 51 가변", "화속 12 가변", "마크 5", "ㄷ 화속깍 44 선택", null,
				"레이 디크리즈 숄더", "", "--", "", "", "",
				"지능 85 가변", "명속 12 가변", "마크 5", "ㄷ 명속깍 44 선택", null,
				"다크니스 로우 새쉬", "", "--", "", "", "",
				"지능 75 가변", "암속 12 가변", "마크 5", "ㄷ 암속깍 44 선택", null,
				"페이스 다운 슈즈", "", "--", "", "", "",
				"지능 75 가변", "모속 30", null,
				/////단일
				"풀 브라이트 로브", "", Equip_part.ROBE, "", SetName.NONE, 90,
				"힘 110 가변", "지능 163 가변", "모속강 35 가변", null,
				"글래시 오브 실크 하의", "", "--", "", "", "",
				"힘 83 가변", "지능 135 가변", "물크 12", "마크 12", "스킬 1-70 1", null,
				"인비지블 케이프", "", "--", "", "", "",
				"지능 43 가변", "증뎀 15", "모공증 3", null,
				"실크 패브릭 벨트", "", "--", "", "", "",
				"지능 32 가변", "크증뎀 18", null,
				"옵틱 파이버 슈즈", "", "--", "", "", "",
				"지능 32 가변", "물공 99 가변", "마공 99 가변", "독공 113 가변", "모속강 18 가변", null,
				/////오기일
				"오기일의 색동 저고리", "", Equip_part.ROBE, "", SetName.OGGEILL, 90,
				"힘 83 가변", "지능 135 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 스란 치마", "", "--", "", "", "",
				"힘 83 가변", "지능 135 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 비단 장옷", "", "--", "", "", "",
				"힘 83 가변", "지능 125 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 짧은 고름", "", "--", "", "", "",
				"힘 83 가변", "지능 114 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 꽃 버선", "", "--", "", "", "",
				"힘 83 가변", "지능 114 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				/////게슈펜슈트
				"망상의 파라노이아", "", Equip_part.ROBE, "", SetName.GESPENST, 90,
				"설명 (미구현)", null,
				"애착의 나르시즘", "", "--", "", "", "",
				"설명 (미구현)", null,
				"붕괴의 게슈탈트", "", "--", "", "", "",
				"설명 (미구현)", null,
				"죽음의 타나토스", "", "--", "", "", "",
				"설명 (미구현)", null,
				"인격의 페르소나", "", "--", "", "", "",
				"설명 (미구현)", null,
				
				//////////////////////가죽
				//////////에픽
				/////카멜
				"교활한 카멜레온 가죽 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.LEATHER, SetName.CHAMELEON, 80,
				"힘 41 가변", "지능 41 가변", "물크 3", "마크 3", "ㄷ 고정물방깍 8000 가변", null,
				"민첩한 카멜레온 가죽 하의", "", "--", "", "", "",
				"힘 41 가변", "지능 41 가변", "ㄷ 물크 30 가변", "ㄷ 마크 30 가변", null,
				"날렵한 카멜레온 가죽 숄더", "", "--", "", "", "",
				"힘 32 가변", "지능 32 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"은밀한 카멜레온 가죽 벨트", "", "--", "", "", "",
				"힘 24 가변", "지능 24 가변", null,
				"재빠른 카멜레온 가죽 신발", "", "--", "", "", "",
				"힘 24 가변", "지능 24 가변", null,
				/////택틱
				"택틱컬 커맨더 상의", "", Equip_part.ROBE, "", SetName.TACTICAL, 85,
				"힘 76 가변", "지능 76 가변", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 80", null,
				"택틱컬 리더 하의", "", "--", "", "", "",
				"힘 76 가변", "지능 76 가변", "ㄷ 물크 12", "ㄷ 마크 12", null,
				"택틱컬 오피서 어깨", "", "--", "", "", "",
				"힘 67 가변", "지능 67 가변", null,
				"택틱컬 로드 벨트", "", "--", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 모속강 20", null,
				"택틱컬 치프 신발", "", "--", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 힘 120", "ㄷ 지능 120", null,
				/////암살
				"밤의 그림자 상의", "", Equip_part.ROBE, "", SetName.ASSASSIN, 85,
				"힘 92 가변", "지능 92 가변", "증뎀 18", null,
				"붉은 송곳니 하의", "", "--", "", "", "",
				"힘 92 가변", "지능 92 가변", "크증뎀 18", null,
				"어둠의 칼날 어깨", "", "--", "", "", "",
				"힘 67 가변", "지능 67 가변", "물크 5", "마크 5", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"죽음의 장막 벨트", "", "--", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 물공 100", "ㄷ 마공 100", "ㄷ 독공 100", null,
				"황천의 바람 신발", "", "--", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 8 선택", "모속강 8 선택", null,
				/////단일
				"용맹한 범무늬 가죽 재킷", "", Equip_part.ROBE, "", SetName.NONE, 90,
				"힘 45 가변", "지능 45 가변", "ㄷ 추뎀 12 선택", null,
				"바다의 포식자 가죽 바지", "", "--", "", "", "",
				"힘 45 가변", "지능 45 가변", "물공 110 가변", "마공 110 가변", "독공 127 가변", "ㄷ 물공뻥 7", "ㄷ 마공뻥 7", "ㄷ 독공뻥 7", null,
				"강인한 레이온 갈기 숄더", "", "--", "", "", "",
				"힘 35 가변", "지능 35 가변", "추크증 12", null,
				"저먼채널 레이 휩 벨트", "", "--", "", "", "",
				"힘 26 가변", "지능 26 가변", "물공 132 가변", "마공 132 가변", "독공 132 가변", "증뎀 10", null,
				"창공의 알바트로스 깃털 부츠", "", "--", "", "", "",
				"힘 26 가변", "지능 26 가변", "물크 5", "마크 5", "크증뎀 15", null,
				/////블랙포멀, 신사
				"블랙 포멀 재킷", "", Equip_part.ROBE, "", SetName.BLACKFORMAL, 90,
				"힘 177 가변", "지능 177 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 팬츠", "", "--", "", "", "",
				"힘 177 가변", "지능 177 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 숄더패드", "", "--", "", "", "",
				"힘 167 가변", "지능 167 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 벨트", "", "--", "", "", "",
				"힘 158 가변", "지능 158 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 부츠", "", "--", "", "", "",
				"힘 158 가변", "지능 158 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				/////핀드
				"니힐룸의 이공간", "", Equip_part.ROBE, "", SetName.FIENDVENATOR, 90,
				"설명 (미구현)", null,
				"니겔루스의 초합금", "", "--", "", "", "",
				"설명 (미구현)", null,
				"갈바누스의 성장", "", "--", "", "", "",
				"설명 (미구현)", null,
				"위로르의 증기", "", "--", "", "", "",
				"설명 (미구현)", null,
				"루벨루스의 염화", "", "--", "", "", "",
				"설명 (미구현)", null,

				///////////////////// 경갑
				/////////에픽
				/////서브마린
				"서브마린 볼케이노 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.MAIL, SetName.SUBMARINE, 80,
				"힘 48 가변", "지능 32 가변", "ㄷ 물공뻥 8 선택", "ㄷ 마공뻥 8 선택", "ㄷ 독공뻥 8 선택", null,
				"서브마린 볼케이노 하의", "", "--", "", "", "",
				"힘 48 가변", "지능 32 가변", "ㄷ 물공뻥 8 선택", "ㄷ 마공뻥 8 선택", "ㄷ 독공뻥 8 선택", null,
				"서브마린 볼케이노 어깨", "", "--", "", "", "",
				"힘 39 가변", "지능 25 가변", "물크 15", "마크 15", "스킬 45-50 1", null,
				"서브마린 볼케이노 벨트", "", "--", "", "", "",
				"힘 29 가변", "지능 20 가변", "ㄷ 화속강 40", "ㄷ 화속부여", null,
				"서브마린 볼케이노 신발", "", "--", "", "", "",
				"힘 29 가변", "지능 20 가변", "화속강 24", null,
				/////자수
				"라이트니스 오토 상의", "", Equip_part.ROBE, "", SetName.NATURALGARDIAN, 85,
				"힘 51 가변", "지능 34 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "명속강 24 가변", null,
				"파이어니스 오토 하의", "", "--", "", "", "",
				"힘 51 가변", "지능 34 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "화속강 24 가변", null,
				"블랙니스 오토 어깨", "", "--", "", "", "",
				"힘 41 가변", "지능 28 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "암속강 24 가변", null,
				"아이니스 오토 벨트", "", "--", "", "", "",
				"힘 41 가변", "지능 28 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "수속강 24 가변", null,
				"윈드니스 오토 신발", "", "--", "", "", "",
				"힘 31 가변", "지능 20 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "모속강 14 가변", null,
				/////아이실드
				"사일런스 테이커 상의", "", Equip_part.ROBE, "", SetName.EYESHIELD, 85,
				"힘 51 가변", "지능 34 가변", "증뎀 18", null,
				"뉴타입 어태커 하의", "", "--", "", "", "",
				"힘 51 가변", "지능 34 가변", "크증뎀 15", "백증뎀 20", null,
				"디펜스 리시버 보호구", "", "--", "", "", "",
				"힘 371 가변", "지능 358 가변", null,
				"타이트엔드 블로커 벨트", "", "--", "", "", "",
				"힘 31 가변", "지능 20 가변", "물크 15", "마크 15", null,
				"테일백 러너 부츠", "", "--", "", "", "",
				"힘 31 가변", "지능 20 가변", "설명 난다", null,
				/////단일
				"이블 립 메일", "", Equip_part.ROBE, "", SetName.NONE, 90,
				"힘 53 가변", "지능 35 가변", "ㄷ 힘뻥 10", "ㄷ 지능뻥 10", "모공증 5", null,
				"빅 세크럼 각반", "", "--", "", "", "",
				"힘 174 가변", "지능 156 가변", "스킬 15-40 1", "추증뎀 5", null,
				"스카퓰러 본 숄더", "", "--", "", "", "",
				"힘 43 가변", "지능 29 가변", "물크 18", "마크 18", "ㄷ 물공뻥 8", "ㄷ 마공뻥 8", "ㄷ 독공뻥 8", null,
				"럼버 스켈 웨이스트", "", "--", "", "", "",
				"힘 32 가변", "지능 21 가변", "증뎀 18", null,
				"타이비아 본 부츠", "", "--", "", "", "",
				"힘 87 가변", "지능 76 가변", "스킬 50-80 2", null,
				/////황갑
				"눈부신 황금 갑주 상의", "", Equip_part.ROBE, "", SetName.GOLDENARMOR, 90,
				"힘 130 가변", "지능 112 가변", "모속강 22", "추증뎀 5", null,
				"눈부신 황금 갑주 하의", "", "--", "", "", "",
				"힘 130 가변", "지능 112 가변", "모속강 22", "추크증 5", null,
				"눈부신 황금 갑주 어깨", "", "--", "", "", "",
				"힘 120 가변", "지능 106 가변", "모속강 22", "모공증 5", null,
				"눈부신 황금 갑주 허리", "", "--", "", "", "",
				"힘 109 가변", "지능 98 가변", "모속강 22", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", null,
				"눈부신 황금 갑주 신발", "", "--", "", "", "",
				"힘 109 가변", "지능 98 가변", "모속강 22", "ㄷ 힘뻥 5", "ㄷ 지능뻥 5", null,
				/////초대륙
				"초대륙 - 발바라의 대지", "", Equip_part.ROBE, "", SetName.SUPERCONTINENT, 90,
				"설명 (미구현)", null,
				"초대륙 - 판게아의 지진", "", "--", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 파노티아의 화산", "", "--", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 로디니아의 용암", "", "--", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 케놀랜드의 지각", "", "--", "", "", "",
				"설명 (미구현)", null,
				
				///////////////////// 중갑
				/////////에픽
				/////미다홀
				"미지의 다크홀 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.HEAVY, SetName.DARKHOLE, 80,
				"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
				"미지의 다크홀 하의", "", "--", "", "", "",
				"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
				"미지의 다크홀 어깨", "", "--", "", "", "",
				"힘 35 가변", "지능 25 가변", "증뎀 12", null,
				"미지의 다크홀 벨트", "", "--", "", "", "",
				"힘 26 가변", "지능 20 가변", "ㄷ 물크 15", "ㄷ 마크 15", null,
				"미지의 다크홀 슈즈", "", "--", "", "", "",
				"힘 81 가변", "지능 75 가변", null,
				/////거미
				"타란튤라 상의", "", Equip_part.ROBE, "", SetName.SPIDERQUEEN, 85,
				"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"킹바분 하의", "", "--", "", "", "",
				"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"골리앗 버드이터 어깨", "", "--", "", "", "",
				"힘 147 가변", "지능 138 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"로즈헤어 벨트", "", "--", "", "", "",
				"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"인디언 오너멘탈 신발", "", "--", "", "", "",
				"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				/////금계
				"피의 맹약 상의", "", Equip_part.ROBE, "", SetName.FORBIDDENCONTRACT, "",
				"힘 266 가변", "지능 254 가변", "d 힘 200 가변", "d 지능 200 가변", null,
				"마나의 서약 하의", "", "--", "", "", "",
				"힘 46 가변", "지능 34 가변", "물크 15", "마크 15", "ㄷ 물크 20 가변", "ㄷ 마크 20 가변", null,
				"마력의 계약 숄더", "", "--", "", "", "",
				"힘 37 가변", "지능 28 가변", "증뎀 15", null,
				"체력의 협약 벨트", "", "--", "", "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 물공 100 가변", "ㄷ 마공 100 가변", "ㄷ 독공 100 가변", null,
				"피의 조약 부츠", "", "--", "", "", "",
				"힘 28 가변", "지능 20 가변", "모속강 14", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 100", null,
				/////단일
				"리엑터 코어 메일", "", Equip_part.ROBE, "", SetName.NONE, 90,
				"힘 48 가변", "지능 35 가변", "증뎀 12", "크증뎀 10", null,
				"익스포젼 헤비 각반", "", "--", "", "", "",
				"힘 48 가변", "지능 35 가변", "모공증 15 선택", "스증 15 선택", fStat[0], "설명 옵션 2개 모두 선택시 스증옵션만 적용", null,
				"컨테미네이션 폴드런", "", "--", "", "", "",
				"힘 122 가변", "지능 111 가변", "모공증 12 선택", null,
				"퓨어로드 코일", "", "--", "", "", "",
				"힘 112 가변", "지능 103 가변", "ㄷ 물공 100 가변", "ㄷ 마공 100 가변", "ㄷ 독공 100 가변", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 80", null,
				"멜트다운 사바톤", "", "--", "", "", "",
				"힘 525 가변", "지능 516 가변", null,
				/////고대전쟁의 여신
				"천년전쟁 영웅의 체인 레지스트", "", Equip_part.ROBE, "", SetName.ANCIENTWAR, "",
				"힘 213 가변", "지능 200 가변", "물크 10", "마크 10", "스킬 75 2", null,
				"천년전쟁 영웅의 그리브", "", "--", "", "", "",
				"힘 213 가변", "지능 200 가변", "물크 10", "마크 10", "스킬 48 2", null,
				"천년전쟁 영웅의 체인 숄더", "", "--", "", "", "",
				"힘 40 가변", "지능 29 가변", "모속강 26", "물크 10", "마크 10", null,
				"천년전쟁 영웅의 체인 벨트", "", "--", "", "", "",
				"힘 30 가변", "지능 21 가변", "모속강 26", "물크 10", "마크 10", null,
				"천년전쟁 영웅의 체인슈즈", "", "--", "", "", "",
				"힘 30 가변", "지능 21 가변", "모속강 26", "물크 10", "마크 10", null,
				/////나자라라
				"역린의 마나스빈", "", Equip_part.ROBE, "", SetName.NAGARAJA, 90,
				"설명 (미구현)", null,
				"유해교반의 바스키", "", "--", "", "", "",
				"설명 (미구현)", null,
				"해룡왕 사가라", "", "--", "", "", "",
				"설명 (미구현)", null,
				"자객의 탁샤카", "", "--", "", "", "",
				"설명 (미구현)", null,
				"거련의 우트파라카", "", "--", "", "", "",
				"설명 (미구현)", null,
				
				///////////////////// 판금
				/////////에픽
				/////인피티니
				"인피니티 레퀴엠 판금 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.PLATE, SetName.INFINITY, 80,
				"힘 41 가변", "지능 41 가변", "모속강 12", "스킬 70 1", null,
				"인피니티 레퀴엠 판금 하의", "", "--", "", "", "",
				"힘 41 가변", "지능 41 가변", "모속강 12", "스킬 60 1", null,
				"인피니티 레퀴엠 판금 아미스", "", "--", "", "", "",
				"힘 32 가변", "지능 32 가변", "모속강 10", null,
				"인피니티 레퀴엠 판금 코일", "", "--", "", "", "",
				"힘 24 가변", "지능 24 가변", "모속강 10", null,
				"인피니티 레퀴엠 판금 부츠", "", "--", "", "", "",
				"힘 24 가변", "지능 24 가변", "모속강 10", null,
				/////마소
				"마력의 폭풍우", "", Equip_part.ROBE, "", SetName.MAELSTORM, 85,
				"힘 43 가변", "지능 43 가변", "증뎀 10", null,
				"영력의 회오리", "", "--", "", "", "",
				"힘 43 가변", "지능 43 가변", "스킬 85 1", null,
				"마법의 대격변", "", "--", "", "", "",
				"힘 34 가변", "지능 34 가변", "스킬 30-45 2", "스킬 기본기 숙련 150%", null,
				"마나의 소용돌이", "", "--", "", "", "",
				"힘 25 가변", "지능 25 가변", "스킬 80 1", null,
				"정수의 태풍", "", "--", "", "", "",
				"힘 113 가변", "지능 113 가변", "물크 5", "마크 5", "물공뻥 4 선택", "마공뻥 4 선택", "독공뻥 4 선택", null,
				/////풀플
				"플레이트 파워아머 상의", "", Equip_part.ROBE, "", SetName.FULLPLATE, 85,
				"힘 98 가변", "지능 98 가변", "ㄷ 물공뻥 100 선택", "ㄷ 마공뻥 100 선택", "ㄷ 독공뻥 100 선택", null,
				"플레이트 매직아머 하의", "", "--", "", "", "",
				"힘 98 가변", "지능 98 가변", "ㄷ 물공뻥 100 선택", "ㄷ 마공뻥 100 선택", "ㄷ 독공뻥 100 선택", null,
				"플레이트 레인지아머 보호대", "", "--", "", "", "",
				"힘 34 가변", "지능 34 가변", "물크 7", "마크 7", "ㄷ 크증뎀 15", null,
				"플레이트 앱솔루트아머 벨트", "", "--", "", "", "",
				"힘 190 가변", "지능 190 가변", null,
				"플레이트 윙아머 부츠", "", "--", "", "", "",
				"힘 25 가변", "지능 25 가변", "물크 10", "마크 10", null,
				/////단일
				"라이트 로즈 판금 상갑", "", Equip_part.ROBE, "", SetName.NONE, 90,
				"힘 45 가변", "지능 45 가변", "모속강 24 가변", "ㄷ 모속강 18 선택",  null,
				"섀도우 사파이어 판금 하갑", "", "--", "", "", "",
				"힘 45 가변", "지능 45 가변", "증뎀 18", null,
				"스모키 토파즈 판금 숄더", "", "--", "", "", "",
				"힘 35 가변", "지능 35 가변", "물공 88 가변", "마공 88 가변", "독공 101 가변", "스킬 40-50 2", null,
				"선 토파즈 판금 코일", "", "--", "", "", "",
				"힘 466 가변", "지능 466 가변", null,
				"페리도트 판금 그리브", "", "--", "", "", "",
				"힘 26 가변", "지능 26 가변", "스킬 5 2", "ㄷ 힘뻥 7", "ㄷ 지능뻥 7", null,
				/////센츄리온
				"메탈라인 아머", "", Equip_part.ROBE, "", SetName.CENTURYONHERO, 90,
				"힘 155 가변", "지능 155 가변", "물크 7", "마크 7", "스킬 5-30 1", null,
				"메탈라인 각반", "", "--", "", "", "",
				"힘 155 가변", "지능 155 가변", "물크 7", "마크 7", "스킬 5-30 1", null,
				"메탈라인 폴드런", "", "--", "", "", "",
				"힘 145 가변", "지능 145 가변", "물크 7", "마크 7", "스킬 45-50 1", null,
				"메탈라인 플레이트 코일", "", "--", "", "", "",
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 45-40 1", null,
				"메탈라인 그리브", "", "--", "", "", "",
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 35-48 1", null,
				/////칠죄종
				"오만에 가득찬 눈", "", Equip_part.ROBE, "", SetName.SEVENSINS, 90,
				"설명 (미구현)", null,
				"폭식하는 입", "", "--", "", "", "",
				"설명 (미구현)", null,
				"탐식을 쥐는 손", "", "--", "", "", "",
				"설명 (미구현)", null,
				"질투를 말하는 혀", "", "--", "", "", "",
				"설명 (미구현)", null,
				"나태함을 가진 발", "", "--", "", "", "",
				"설명 (미구현)", null,
				
				///////////////////// 악세서리
				/////////에픽
				/////슈스
				"슈퍼 스타 네클레스", Item_rarity.EPIC, Equip_part.NECKLACE, Equip_type.NONE, SetName.SUPERSTAR, 80,
				"지능 40 가변", "모속강 12 가변", null,
				"슈퍼 스타 암릿", "", "--", "", "", "",
				"힘 40 가변", "모속강 12 가변", null,
				"슈퍼 스타 링", "", "--", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 12 가변", null,
				/////얼공
				"차가운 공주의 목걸이", "", Equip_part.NECKLACE, "", SetName.ICEQUEEN, 85,
				"지능 41 가변", "수속강 22 가변", "ㄷ 수속부여", null,
				"냉정한 공주의 팔찌", "", "--", "", "", "",
				"힘 41 가변", "수속강 30 가변", null,
				"싸늘한 공주의 반지", "", "--", "", "", "",
				"힘 62 가변", "지능 62 가변", "수속강 30 가변", null,
				/////정마
				"정제된 혼돈의 마석 목걸이", "", Equip_part.NECKLACE, "", SetName.REFINEDSTONE, 85,
				"지능 41 가변", "ㄷ 증뎀 20 선택", null,
				"정제된 파괴의 마석 팔찌", "", "--", "", "", "",
				"힘 41 가변", "ㄷ 스증뎀 5", null,
				"정제된 망각의 마석 반지", "", "--", "", "", "",
				"힘 62 가변", "지능 62 가변", null,
				/////85단일
				"칼날 여왕의 목걸이", "", Equip_part.NECKLACE, "", SetName.NONE, 85,
				"지능 41 가변", "ㄷ 증뎀 20 선택", null,
				"집행인의 척살 목걸이", "", "", "", "", "",
				"지능 41 가변", "물크 7", "마크 7", "ㄷ 증뎀 10 선택", "ㄷ 증뎀 20 선택", "ㄷ 증뎀 30 선택",
				"ㄷ 증뎀 19 선택", fStat[1], "설명 증뎀 옵션에 2개 이상이 체크될 경우, 기댓값(19%)으로 설정됩니다", null,
				"피묻은 수갑", "", "--", "", "", "",
				"힘 283 가변", "지능 242 가변", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", null,
				"화염술사의 포락 팔찌", "", "", "", "", "",
				"힘 96 가변", "지능 55 가변", "화속강 12 가변", "물크 10", "마크 10", "ㄷ 증뎀 20 선택", null,
				"암살자의 칼날 반지", "", "--", "", "", "",
				"힘 62 가변", "지능 62 가변", "물크 7", "마크 7", "ㄷ 크증뎀 10 선택", "ㄷ 크증뎀 20 선택", "ㄷ 크증뎀 30 선택",
				"ㄷ 크증뎀 19 선택", fStat[1], "설명 크증뎀 옵션에 2개 이상이 체크될 경우, 기댓값(19%)으로 설정됩니다", null,
				/////90단일
				"카프리 엠퍼 네클리스", "", Equip_part.NECKLACE, "", SetName.NONE, 90,
				"지능 43 가변", "ㄷ 모공증 20 가변", null,
				"사이얀 홉 암릿", "", "--", "", "", "",
				"힘 43 가변", "물공 105 가변", "마공 105 가변", "독공 120 가변", "ㄷ 스증뎀 8", null,
				"아쿠아 게이트 링", "", "--", "", "", "",
				"힘 64 가변", "지능 64 가변", "모속강 30 가변", "물크 15", "마크 15", null,
				/////이정표
				"하늘의 이정표 : 루크바", "", Equip_part.NECKLACE, "", SetName.SKYTRAVELER, 90,
				"지능 43 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				"하늘의 등대 : 쉐다르", "", "--", "", "", "",
				"힘 43 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				"하늘의 길잡이 : 카프", "", "--", "", "", "",
				"힘 64 가변", "지능 64 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				/////황홀경
				"반짝임의 향기", "", Equip_part.NECKLACE, "", SetName.ECSTATICSENCE, 90,
				"지능 43 가변", "ㄷ 모공증 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 모든 공격력 1% 추가 증가 (최대 12강까지 증가)", null,
				"샛별의 숨소리", "", "--", "", "", "",
				"힘 43 가변", "ㄷ 스증뎀 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 스킬 공격력 1% 추가 증가 (최대 12강까지 증가)", null,
				"물소리의 기억", "", "--", "", "", "",
				"힘 64 가변", "지능 64 가변", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 물리, 마법, 독립 공격력 1% 추가 증가 (최대 12강까지 증가)", null,

				///////////////////// 특수장비
				/////////에픽
				/////보장
				"각성한 자의 각오 -", "", Equip_part.AIDEQUIPMENT, "", SetName.NONE, 80,
				"힘 88 가변", "지능 88 가변", "설명 스킬 정보 미구현", null,
				"시간 여행자의 은시계", "", "", "", "", 85,
				"힘 40 가변", "지능 40 가변", "스킬 1-80 1", null,
				"고명한 장군의 전략서", "", "", "", "", "",
				"힘 146 가변", "지능 146 가변", "스킬 85 1", "스킬 48-50 1", "TP스킬 1-85 1", null,
				"피쉬 볼 라인", "", "", "", "", 90,
				"힘 42 가변", "지능 42 가변", "물크 5", "마크 5", "스킬 45-80 2", null,
				"흑백의 경계 : 가면", "", "", "", "", "",
				"힘 42 가변", "지능 42 가변", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", "설명 함정카드 발동", "설명 딴딴딴따 따~라라 따~라라", null,
				"파르스의 황금잔", "", "", "", "", "",
				"힘 42 가변", "지능 42 가변", "모속강 18 가변", "ㄷ 힘뻥 18 선택", "ㄷ 지능뻥 18 선택", "설명 운빨", null,
				/////마법석
				"폐왕의 눈물", "", Equip_part.MAGICSTONE, "", "", 85,
				"힘 123 가변", "지능 123 가변", "추뎀 18", null,
				"비뮤트 스톤", "", "", "", "", 90,
				"힘 118 가변", "지능 118 가변", "모공증 18", null,
				"잭오랜턴의 기억", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "화속강 55 가변", null,
				"잭프로스트의 기억", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "수속강 55 가변", null,
				"플로레상의 기억", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "명속강 55 가변", null,
				"플루토의 기억", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "암속강 55 가변", null,
				"카발라의 기억", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "모속강 55 가변", null,
				"흑백의 경계 : 마음", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", fStat[3], "설명 화/수/명/암 속성 강화 중 높은 속성강화 55 증가", null,
				"로제타스톤", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "모속강 18 가변", "ㄷ 스증뎀 18 선택", "설명 ㅈ망", null,
				
				//////////귀걸이
				/////레어
				"마법의 줄타나이트 이어링", Item_rarity.RARE, Equip_part.EARRING, "", "", 90,
				"힘 56 가변", "지능 56 가변", "모속강 6 가변", "힘 32 가변", "지능 32 가변", null,
				/////유니크
				"성물 : 모락스의 귀걸이", Item_rarity.UNIQUE, "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 11 가변", "물크 2", "마크 2", null,
				"레인보우 이어링", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 12 가변", "힘 100 선택", "지능 100 선택", null,
				/////레전더리
				"브라키움 기어링", Item_rarity.LEGENDARY, "", "", "", "",
				"힘 62 가변", "지능 62 가변", "물공 121 가변", "마공 121 가변", "독공 139 가변", "물크 10", "마크 10", null,
				"무언의 건설자 귀걸이", "", "", "", "", "",
				"힘 62 가변", "지능 62 가변", "물공 149 가변", "마공 149 가변", "독공 171 가변", null,
				/////에픽
				"브라이들 펄", Item_rarity.EPIC, "", "", "", "",
				"힘 63 가변", "지능 63 가변", "물공 165 가변", "마공 165 가변", "독공 189 가변", "추크증 8", null,
				"흑백의 경계 : 혼돈", "", "", "", "", "",
				"힘 184 가변", "지능 184 가변", "ㄷ 물공뻥 15 선택", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15", null,
				"바벨로니아의 상징", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "모속강 18 가변", "모공증 18 가변", "설명 겜", null,

		};
		
		return data;
	}
}
