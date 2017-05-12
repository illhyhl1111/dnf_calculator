package dnf_infomation;

import java.util.HashMap;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.BooleanInfo;
import dnf_calculator.CalculateElement;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_class.SetOption;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_class.Weapon;

public class FunctionInfo {
	public static final HashMap<String, FunctionStat> fStat = new HashMap<String, FunctionStat>() {
		private static final long serialVersionUID = 6003853286215241336L;
	{
		//0, 혈지군무 분신
		put("혈지군무", new FunctionStat("혈지군무"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				Skill basicSkill = character.characterInfoList.getSkill("혈지군무");
				if(basicSkill.getCharSkillLevel()!=0){
					skill.dungeonLevel = basicSkill.dungeonLevel;
					skill.setSkillLevel(basicSkill.getCharSkillLevel());
				}
				
				double skillEnhance=0;
				SkillLevelInfo skillInfo = character.characterInfoList.getSkill("마인의 검세").getSkillLevelInfo(true, character.isBurning());
				if(character.characterInfoList.getSkill("마인의 검세").getSkillLevel(true, character.isBurning())!=0){
					switch(skill.getName()){
					case "혈지군무 - 발":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(4).stat).getIncrease();
						break;
					case "혈지군무 - 무":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(5).stat).getIncrease();
						break;
					case "혈지군무 - 사형조수":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(6).stat).getIncrease();
						break;
					case "혈지군무 - 폭류나선":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(0).stat).getIncrease();
						break;
					case "혈지군무 - 혈화난무":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(1).stat).getIncrease();
						break;
					}
				}
				//statList.addSkill_damage(skill.getName(, 100.0/(100+skillEnhance)*100-100);
				skill.dungeonIncrease *= 100.0/(100+skillEnhance);
				return statList;
			}
		});
		
		//1, 듀얼트리거
		put("듀얼트리거", new FunctionStat("듀얼트리거"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				try {
					int fire = (int)Math.round(character.dungeonStatus.getStat(StatList.ELEM_FIRE));
					int light = (int)Math.round(character.dungeonStatus.getStat(StatList.ELEM_LIGHT));
					if(fire>light) statList.addStatList(Element_type.LIGHT, fire-light, false, false, false);
					else statList.addStatList(Element_type.FIRE, light-fire, false, false, false);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		});
		
		/*put("미사용1", new FunctionStat("미사용1"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				try {
					statList.addStatList("재련독공", character.dungeonStatus.getStat("재련독공")
							*skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.get(0).stat.getStatToDouble()/100);
				} catch (UndefinedStatusKey | StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		});*/
		
		//3, 데바리
		put("데바리", new FunctionStat("데바리"){
			double save;
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				try {
					if(character.getItemSetting().weapon.weaponType!=Weapon_detailType.GUN_REVOLVER){
						double temp;
						temp = skill.skillInfo.getLast().stat.statList.getFirst().stat.getStatToDouble();
						if(temp!=0) save=temp;
						
						skill.skillInfo.getLast().stat.statList.getFirst().stat.setInfo(0.0);
					} 
					
					else{
						if(skill.skillInfo.getLast().stat.statList.getFirst().stat.getStatToDouble()==0)
							skill.skillInfo.getLast().stat.statList.getFirst().stat.setInfo(save);
					}
				}
				catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				
				return statList;
			}
		});
		
		//4, 강화탄
		put("강화탄", new FunctionStat("강화탄"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				int level = skill.getSkillLevel(true, character.isBurning());
				if(character.characterInfoList.getSkill("강화탄(무)").buffEnabled(true)){
					statList.addStatList("%물방깍_스킬", level);
					statList.addStatList("%마방깍_스킬", level);
				}
				else if(character.characterInfoList.getSkill("강화탄(화)").buffEnabled(true))
					statList.addStatList("화속깍", level*2+2);
				else if(character.characterInfoList.getSkill("강화탄(수)").buffEnabled(true))
					statList.addStatList("수속깍", level*2+2);
				else if(character.characterInfoList.getSkill("강화탄(명)").buffEnabled(true))
					statList.addStatList("명속깍", level*2+2);
					
				if(character.characterInfoList.getSkill("강화탄(컨버전)").buffEnabled(true)){
					statList.addStatList("마법컨버전", new BooleanInfo(true));
				}
				else{
					statList.addStatList("물리컨버전", new BooleanInfo(true));
				}
				return statList;
			}
		});
		
		//5, 병기숙련
		put("병기숙련", new FunctionStat("병기숙련"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.weaponType==Weapon_detailType.GUN_BOWGUN
						|| character.getItemSetting().weapon.weaponType==Weapon_detailType.GUN_MUSKET){
					int level = skill.getSkillLevel(true, character.isBurning());
					statList.addStatList("물리마스터리", level);
					statList.addStatList("마법마스터리", level);
					statList.addStatList("독공마스터리", level);
					statList.addStatList("물리방무뻥", level);
					statList.addStatList("마법방무뻥", level);
				}
					
				return statList;
			}
		});
		
		//6, 매거진, 공중사격
		put("매거진", new FunctionStat("매거진"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill hit = character.characterInfoList.getSkill("평타(1사이클)");
				switch(character.getItemSetting().weapon.weaponType)
				{
				case GUN_BOWGUN:
					hit.setSkillLevel(1);
					hit.dungeonLevel=20;
					break;
				case GUN_MUSKET:
					hit.setSkillLevel(10);
					hit.dungeonLevel=9;
					break;
				case GUN_REVOLVER:
					hit.setSkillLevel(1);
					hit.dungeonLevel=11;
					break;
				case GUN_HCANON:
					hit.setSkillLevel(1);
					hit.dungeonLevel=5;
					break;
				case GUN_AUTOPISTOL:
					hit.setSkillLevel(1);
					hit.dungeonLevel=17;
					break;
				default:
					break;
				}
				return statList;
			}
		});
		
		//7, 기숙
		put("기숙", new FunctionStat("기숙"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				if(character.hasContract()) skill.setSkillLevel(character.getLevel());
				return new StatusList();
			}
		});
		
		//8, 원소폭격
		put("원소폭격", new FunctionStat("원소폭격"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				int level = skill.getSkillLevel(true, character.isBurning());
				StatusList statList = new StatusList();
				for(Skill s : character.getSkillList())
					if(s.getName().equals("검은 눈"))
						if(s.getActiveEnabled()){
							for(Skill dSkill : character.getSkillList()){
								if(dSkill.hasDamage()){
									switch(dSkill.getName()){
									case "속성발동":
										break;
									case "파이어 로드": case "아이스 크리스탈 샤워": case "엘레멘탈 캐넌":
										statList.addSkill_damage(dSkill.getName(), 13+level);
										break;
									default:
										statList.addSkill_damage(dSkill.getName(), 25+2*level);
										break;
									}
								}
							}
						}
				return statList;
			}
		});
		
		//9, 컨버전, 테아나, 사도화 스킬별 레벨맞추기
		put("컨버전-배메", new FunctionStat("컨버전-배메"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				//컨버전 적용
				//천지쇄패, 대시공격, 테아나 체이서 레벨 맞추고 옵션 비활성화시 레벨 0
				//일기당천, 사도화 체이서 레벨 맞추고 옵션 비활성화시 레벨 0
				//퀘이사 익스플로젼 레벨 테아나에 맞추고 옵션 모두 비활성화시 레벨 0
				StatusList statList = new StatusList();
				Skill conversion = (Skill)item;
				if(conversion.buffEnabled(true)) 
					statList.addStatList(StatList.CONVERSION_NOPHY, new BooleanInfo(true));
				else
					statList.addStatList(StatList.CONVERSION_NOMAG, new BooleanInfo(true));
				Skill theana = null, theanaSkill1 = null, theanaSkill2 = null, theanaSkill3 = null;
				Skill apostle = null, apostleSkill1 = null, apostleSkill2 = null, quasar = null;
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "테아나 변신~!!": theana = s; break;
					case "천지쇄패": theanaSkill1 = s; break;
					case "대시공격": theanaSkill2 = s; break;
					case "테아나 체이서": theanaSkill3 = s; break;
					case "사도화": apostle = s; break;
					case "일기당천 쇄패": apostleSkill1 = s; break;
					case "사도화 체이서": apostleSkill2 = s; break;
					case "퀘이사 익스플로젼": quasar = s; break;
					};
				}
				
				int apostleLevel_char = apostle.getCharSkillLevel();
				int apostleLevel_dun = apostle.dungeonLevel;
				int theanaLevel_char = theana.getCharSkillLevel();
				int theanaLevel_dun = theana.dungeonLevel;
				if(!apostle.buffEnabled(true)){
					apostleSkill1.setSkillLevel(0);
					apostleSkill2.setSkillLevel(0);
				}
				else{
					apostleSkill1.dungeonLevel=apostleLevel_dun;
					apostleSkill1.setSkillLevel(apostleLevel_char);
					apostleSkill2.dungeonLevel=apostleLevel_dun;
					apostleSkill2.setSkillLevel(apostleLevel_char);
					theana.setBuffEnabled(false);
				}
				
				if(!theana.buffEnabled(true)){
					theanaSkill1.setSkillLevel(0);
					theanaSkill2.setSkillLevel(0);
					theanaSkill3.setSkillLevel(0);
				}
				else{
					theanaSkill1.dungeonLevel=theanaLevel_dun;
					theanaSkill1.setSkillLevel(theanaLevel_char);
					theanaSkill2.dungeonLevel=theanaLevel_dun;
					theanaSkill2.setSkillLevel(theanaLevel_char);
					theanaSkill3.dungeonLevel=theanaLevel_dun;
					theanaSkill3.setSkillLevel(theanaLevel_char);
				}
				
				if(!apostle.buffEnabled(true) && !theana.buffEnabled(true))
					quasar.setSkillLevel(0);
				else {
					quasar.dungeonLevel=theanaLevel_dun;
					quasar.setSkillLevel(theanaLevel_char);
				}
				
				return statList;
			}
		});
		
		//10, 체이서 프레스
		put("체프", new FunctionStat("체프"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill numSkill = (Skill)item;
				if(!numSkill.buffEnabled(true)) return new StatusList();
				Skill targetSkill = character.characterInfoList.getSkill("체이서 프레스");
				double num=1;
				try {
					num = numSkill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble();
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				targetSkill.dungeonIncrease*=num;
				return new StatusList();
			}
		});
		
		//11, 문무겸비
		put("문무겸비", new FunctionStat("문무겸비"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				try {
					int Str = (int)Math.round(character.dungeonStatus.getStat(StatList.STR));
					int Int = (int)Math.round(character.dungeonStatus.getStat(StatList.INT));
					Skill intBuff = character.characterInfoList.getSkill("고대의 기억");
					if(intBuff.buffEnabled(true)){
						int stat = (int) (intBuff.getSkillLevelInfo(true, character.isBurning())
								.stat.statList.getFirst().stat.getStatToDouble()+0.0001);
						Int+=stat;
					}
					if(Str>Int) statList.addStatList(StatList.INT, Str-Int);
					else statList.addStatList(StatList.STR, Int-Str);
					
					double crt_phy = character.dungeonStatus.getStat(StatList.CRT_PHY);
					double crt_mag = character.dungeonStatus.getStat(StatList.CRT_MAG);
					if(crt_phy>crt_mag) statList.addStatList(StatList.CRT_MAG, crt_phy-crt_mag);
					else statList.addStatList(StatList.CRT_PHY, crt_mag-crt_phy);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		});
		
		//12, 체이서합딜
		put("체이서합딜", new FunctionStat("체이서합딜"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				if(!skill.buffEnabled(true)) return new StatusList();
				double num=1;
				try {
					num = skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble();
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				StatusList statList = new StatusList();
				statList.addSkill_damage(skill.getName(), num*100-100);
				//skill.dungeonIncrease*=num;
				return statList;
			}
		});
		
		//13, 중체이서, 왕체이서
		put("왕체이서", new FunctionStat("왕체이서"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill kingChaser = (Skill)item;
				Skill chaser = null, middleChaser = null, fireChaser = null, darkChaser = null;
				Skill basic1 = null, basic2 = null, basic3 = null, basic4 = null;  
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "일반 체이서": chaser = s; break;
					case "중 체이서": middleChaser = s; break;
					case "체이서 : 화": fireChaser = s; break;
					case "체이서 : 암": darkChaser = s; break;
					case "천격": basic1 = s; break;
					case "용아": basic2 = s; break;
					case "낙화장": basic3 = s; break;
					case "원무곤": basic4 = s; break;
					}
				}
				if(kingChaser.buffEnabled(true)) chaser.dungeonIncrease*=1.5;
				else if(middleChaser.buffEnabled(true)) chaser.dungeonIncrease*=1.25;
				
				int level = chaser.getCharSkillLevel();
				fireChaser.setSkillLevel(level);
				fireChaser.dungeonLevel=chaser.dungeonLevel;
				darkChaser.setSkillLevel(level);
				darkChaser.dungeonLevel=chaser.dungeonLevel;
				basic2.setSkillLevel(level);
				basic2.dungeonLevel=chaser.dungeonLevel;
				basic3.setSkillLevel(level);
				basic3.dungeonLevel=chaser.dungeonLevel;
				basic4.setSkillLevel(level);
				basic4.dungeonLevel=chaser.dungeonLevel;
				basic1.setSkillLevel(level);
				basic1.dungeonLevel=chaser.dungeonLevel;
				return new StatusList();
			}
		});
		
		//14, 퓨전 체이서
		put("퓨체", new FunctionStat("퓨체"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill thisSkill = (Skill)item;
				Skill targetSkill = null, optionSkill = null, tpSkill = null;
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "퓨전 체이서": targetSkill = s; break;
					case "퓨전 체이서(완충)": optionSkill = s; break;
					case "퓨전 체이서 강화": tpSkill = s; break;
					}
				}
				
				if(!targetSkill.getActiveEnabled()){
					optionSkill.setSkillLevel(0);
					return statList;
				}
				
				if(targetSkill.getBuffEnabled()){
					int tpLevel = tpSkill.getSkillLevel(true, character.isBurning());
					try {
						for(StatusAndName s : targetSkill.getSkillLevelInfo(true, character.isBurning()).stat.statList){
							if(s.name==StatList.STR || s.name==StatList.INT)
								statList.addStatList(s.name, new StatusInfo((int) (s.stat.getStatToDouble()*(0.1*tpLevel))));
							else
								statList.addStatList(s.name, new DoubleStatusInfo(tpLevel));
						}
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				
				if(!thisSkill.buffEnabled(true)) optionSkill.dungeonIncrease=0;
				else{
					targetSkill.dungeonIncrease=0;
					optionSkill.dungeonLevel=targetSkill.dungeonLevel;
					optionSkill.setSkillLevel(targetSkill.getCharSkillLevel());
				}
				return statList;
			}
		});
		
		//15, 무충
		put("무충", new FunctionStat("무충"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				Skill memorize = null, showtime = null, transcendence = null;
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "메모라이즈": memorize = s; break;
					case "쇼타임": showtime = s; break;
					case "초월의 룬": transcendence = s; break;
					}
				}
				
				double showtimeDecrease=0, transcendenceIncrease=0, memorizeDecrease=0;
				switch(showtime.getSkillLevel(true, character.isBurning())){
				case 10: showtimeDecrease=56.2; break;
				case 11: showtimeDecrease=61.8; break;
				case 12: showtimeDecrease=67.4; break;
				case 13: showtimeDecrease=73.1; break;
				case 14: showtimeDecrease=75.6; break;
				case 15: showtimeDecrease=79.2; break;
				case 16: showtimeDecrease=82.8; break;
				case 17: showtimeDecrease=86.4; break;
				case 18: showtimeDecrease=90.0; break;
				case 19: showtimeDecrease=93.6; break;
				case 20: showtimeDecrease=97.2; break;
				}	
				switch(transcendence.getSkillLevel(true, character.isBurning())){
				case 6: transcendenceIncrease=14.6; break;
				case 7: transcendenceIncrease=16.0; break;
				case 8: transcendenceIncrease=17.4; break;
				case 9: transcendenceIncrease=18.8; break;
				case 10: transcendenceIncrease=20.1; break;
				case 11: transcendenceIncrease=21.6; break;
				case 12: transcendenceIncrease=22.9; break;
				case 13: transcendenceIncrease=24.4; break;
				case 14: transcendenceIncrease=25.7; break;
				case 15: transcendenceIncrease=27.2; break;
				case 16: transcendenceIncrease=28.5; break;
				}
				memorizeDecrease = 18+2*memorize.getSkillLevel(true, character.isBurning());
				
				double result = showtimeDecrease*(100+transcendenceIncrease)/100+memorizeDecrease;
				if(result>=100) skill.setBuffEnabled(true);
				else skill.setBuffEnabled(false);
				return new StatusList();
			}
		});

		//16, 컨버전
		put("컨버전-퇴마", new FunctionStat("컨버전-퇴마"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				
				if(skill.buffEnabled(true)){
					character.characterInfoList.getSkill("열정의 챠크라").setSkillLevel(0);
					character.characterInfoList.getSkill("광명의 챠크라").masterSkill(character.getLevel(), character.hasContract());
					statList.addStatList("마법컨버전", new BooleanInfo(true));
					statList.addStatList(Element_type.LIGHT, 0, true, false, false);
				}
				else{
					character.characterInfoList.getSkill("열정의 챠크라").masterSkill(character.getLevel(), character.hasContract());
					character.characterInfoList.getSkill("광명의 챠크라").setSkillLevel(0);
					statList.addStatList("물리컨버전", new BooleanInfo(true));
					statList.addStatList(Element_type.FIRE, 0, true, false, false);
				}
				return statList;
			}
		});
		
		//17, 창룡격
		put("창룡격", new FunctionStat("창룡격"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				int level = ((Skill)item).getSkillLevel(true, character.isBurning());
				
				if(character.characterInfoList.getSkill("광명의 챠크라").getActiveEnabled()) {
					statList.addStatList("마크", level+7);
					statList.addStatList("지능", 120+level*5);
					statList.addStatList("명속강", 13+level);
				}
				else if(character.characterInfoList.getSkill("열정의 챠크라").getActiveEnabled()) {
					statList.addStatList("물크", level+7);
					statList.addStatList("힘", 120+level*5);
					statList.addStatList("화속강", 13+level);
				}
				return statList;
			}
		});

		//19, 마스터리
		put("마스터리", new FunctionStat("마스터리"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				
				HashMap<Equip_part, Equipment> map = character.getItemSetting().equipmentList;
				LinkedList<Equipment> equipList = new LinkedList<Equipment>();
				equipList.add(map.get(Equip_part.ROBE));
				equipList.add(map.get(Equip_part.TROUSER));
				equipList.add(map.get(Equip_part.SHOES));
				equipList.add(map.get(Equip_part.SHOULDER));
				equipList.add(map.get(Equip_part.BELT));
				
				for(Equipment e : equipList){
					statList.addStatList("힘", ReinforceInfo.getMastery_strInfo(character.getJob(), e));
					statList.addStatList("지능", ReinforceInfo.getMastery_intInfo(character.getJob(), e));
					statList.addStatList("물크", ReinforceInfo.getMastery_phyCrtInfo(character.getJob(), e));
					statList.addStatList("마크", ReinforceInfo.getMastery_magCrtInfo(character.getJob(), e));
				}
				
				statList.addListToStat(character.villageStatus);
				return statList;
			}
		});
		
		//20, 전장의여신
		put("전장의여신", new FunctionStat("전장의여신"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				double value=15;
				try {
					value = skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getLast().stat.getStatToDouble();
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				
				if(character.getWeapon().weaponType!=Weapon_detailType.MAGE_POLE 
						&& character.getWeapon().weaponType!=Weapon_detailType.MAGE_SPEAR)
					statList.addStatList("독공마스터리", (85+value)/(100+value)*100-100);
				return statList;
			}
		});
		
		//21, 이중개방
		put("이중개방", new FunctionStat("이중개방"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				Skill basicSkill = character.characterInfoList.getSkill("화염의 각");
				skill.dungeonLevel = basicSkill.dungeonLevel;
				skill.setSkillLevel(basicSkill.getCharSkillLevel());
				return statList;
			}
		});
		
		//22, 체인러쉬
		put("체인러쉬", new FunctionStat("체인러쉬"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				Skill chainRush = character.characterInfoList.getSkill("체인러쉬");
				if(!skill.buffEnabled(true)) return statList;
				double inc=0, inc2=0;
				try {
					inc = 12+0.5*chainRush.getSkillLevel(true, character.isBurning());
					inc2 = inc+5;
					int num= (int) (skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()+0.0005);
					if(num>6) num=6;
					else if(num<0) num=0;
					inc*=num; inc2*=num;
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				statList.addSkill_damage("무장 해제", inc);
				statList.addSkill_damage("압도", inc);
				statList.addSkill_damage("런지 에볼루션", inc);
				statList.addSkill_damage("분쇄", inc);
				statList.addSkill_damage("생명의 전조", inc);
				statList.addSkill_damage("신뢰의 돌파", inc);
				statList.addSkill_damage("체인 스트라이크", inc2);
				statList.addSkill_damage("자연의 분노", inc);
				return statList;
			}
		});
		
		//23, 히트엔드
		put("히트엔드", new FunctionStat("히트엔드"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill TP = null, twinSword = null, hitand = null, hitbleed=null, hitand_num=null;
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "히트엔드 강화": TP = s; break;
					case "쌍검 마스터리": twinSword = s; break;
					case "히트엔드": hitand = s; break;
					case "히트엔드 - 버블수": hitand_num = s; break;
					case "히트 블리드(발동)": hitbleed = s; break;
					}
				}
				
				boolean isDagger = false, hasHitBleed = false, isTwinSword = false;
				if(character.getWeapon().weaponType==Weapon_detailType.THIEF_DAGGER) isDagger=true;
				else if(character.getWeapon().weaponType==Weapon_detailType.THIEF_TWINSWORD) isTwinSword=true;
				if(hitbleed.getBuffEnabled()) hasHitBleed = true; 
				
				int result = 100;
				try {
					int num = (int)(hitand_num.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()+0.00001);
					switch(num)
					{
					case 1: result=100; break;
					case 2: result=125; break;
					case 3: result=155; break;
					case 4: result=195; break;
					case 5: result=250; break;
					case 6:
						if(!(isDagger || hasHitBleed)) result = 250;
						else result=325;
						break;
					default:
						if(num<=0) result = 0;
						else if(isDagger && hasHitBleed) result = 425;
						else if(!(isDagger || hasHitBleed)) result = 250;
						else result=325;
						break;
					}
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				
				result *= (1+0.06*TP.getSkillLevel(true, character.isBurning()));
				result *= hitand.dungeonIncrease;
				if(isTwinSword){
					int inc = 25+twinSword.getSkillLevel(true, character.isBurning());
					if(inc>46) inc++;
					result *= (1+0.01*inc);
				}
				if(!hitand.getActiveEnabled() || !hitand_num.buffEnabled(true)) result=0;
				else result *= (0.99+0.01*hitand.getSkillLevel(true, character.isBurning()));

				StatusList statList = new StatusList();
				statList.addSkill_damage("히트엔드(딜)", (result-1)*100);
				return statList;
			}
		});
		
		//24, 혈십자
		put("혈십자", new FunctionStat("혈십자"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				if(!skill.buffEnabled(true)) return new StatusList();
				Skill target = character.characterInfoList.getSkill("혈십자");
				int input = 0;
				int addLevel = target.getSkillLevel(true, character.isBurning())-10;
				double result = 0;
				StatusList statList = new StatusList();
				if(skill.buffEnabled(true)){
					try {
						input = (int)(skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()+0.0005);
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
					if(input<=0) return statList;
					else if(input<=1) result = 14.5+0.5*addLevel;
					else if(input<=2) result = 21.8+1.5*(addLevel/2)+0.8*(addLevel%2);
					else result = 33.4+2.3*(addLevel/2)+1.2*(addLevel%2);
				}
				statList.addStatList("증뎀버프", result);
				return statList;
			};
		});
		
		//25, 다이하드
		put("다이하드", new FunctionStat("다이하드"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				Skill tpSkill = character.characterInfoList.getSkill("다이하드 강화");
				int level = skill.getSkillLevel(true, character.isBurning());
				int tp = tpSkill.getSkillLevel(true, character.isBurning());
				int result=0;
				if(tp>=1) result=219+(tp-1)*20+(int)(16.3*(level-20));
				StatusList statList = new StatusList();
				statList.addStatList("힘", result);
				return statList;
			};
		});
		
		//26, 러스트
		put("러스트", new FunctionStat("러스트"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				Skill target = character.characterInfoList.getSkill("블러드 러스트");
				StatusList statList = new StatusList();
				
				if(target.buffEnabled(true)){
					try {
						int str = (int)(target.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()+0.00005);
						str *= (0.1*skill.getSkillLevel(true, character.isBurning()));
						statList.addStatList("힘", str);
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				return statList;
			};
		});
		
		//27, 창조의 공간
		put("창조의공간", new FunctionStat("창조의공간"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				Skill fireWall = null, meteor = null, iceStone = null, windPress=null, windStorm=null, flameHurricane=null;
				for(Skill s : character.characterInfoList.skillList){
					switch(s.getName()){
					case "파이어 월 횟수입력": fireWall = s; break;
					case "운석 낙하 횟수입력": meteor = s; break;
					case "아이스 스톤 횟수입력": iceStone = s; break;
					case "윈드 프레스 횟수입력": windPress = s; break;
					case "윈드 스톰 횟수입력": windStorm = s; break;
					case "플레임 허리케인 횟수입력": flameHurricane = s; break;
					}
				}
				
				try {
					if(!skill.buffEnabled(true)){
						if(fireWall.buffEnabled(true))
							statList.addSkill_damage("파이어 월", fireWall.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
						if(meteor.buffEnabled(true))
							statList.addSkill_damage("운석 낙하", meteor.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
						if(iceStone.buffEnabled(true))
							statList.addSkill_damage("아이스 스톤", iceStone.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
						if(windPress.buffEnabled(true))
							statList.addSkill_damage("윈드 프레스", windPress.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
						if(windStorm.buffEnabled(true))
							statList.addSkill_damage("윈드 스톰", windStorm.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
						if(flameHurricane.buffEnabled(true))
							statList.addSkill_damage("플레임 허리케인", flameHurricane.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
					} 
				
					else{
						statList.addSkill_damage("파이어 월", 3896);
						statList.addSkill_damage("운석 낙하", 299);
						statList.addSkill_damage("아이스 스톤", 330.5);
						statList.addSkill_damage("윈드 프레스", 923);
						statList.addSkill_damage("윈드 스톰", 575);
						if(flameHurricane.buffEnabled(true))
							statList.addSkill_damage("플레임 허리케인", flameHurricane.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()*100-100);
					}
				}
				catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			};
		});
		
		//28, 실버스트림
		put("실버스트림", new FunctionStat("실버스트림"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Skill skill = (Skill)item;
				Skill target = character.characterInfoList.getSkill("실버스트림(버프)");
				if(skill.buffEnabled(true)){
					target.dungeonLevel = 0;
					try {
						target.setSkillLevel((int) (skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble()+0.0001));
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				else if(target.getActiveEnabled()){
					Skill target2 = character.characterInfoList.getSkill("실버스트림");
					target.setSkillLevel(target2.getCharSkillLevel());
					target.dungeonLevel=target2.dungeonLevel;
				}
				
				return new StatusList();
			};
		});
		
		////////////////////////////아이템		

		//익스포젼 헤비 각반
		put("익스포젼", new FunctionStat("익스포젼") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Equipment equipment =(Equipment)item;
				if(equipment.dStat.statList.get(0).enabled && equipment.dStat.statList.get(1).enabled)
					equipment.dStat.statList.get(0).enabled=false;
				StatusList statList = new StatusList();
				statList.addStatList("모공증", -15);
				return statList;
			}
		});
		
		//집척목, 암칼반
		put("집척목", new FunctionStat("집척목") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				int count=0;
				Equipment equipment =(Equipment)item;
				for(int i=0; i<4; i++)
					if(equipment.dStat.statList.get(i).enabled) count++;
				
				if(count>1){
					for(int i=0; i<3; i++)
						equipment.dStat.statList.get(i).enabled=false;
					equipment.dStat.statList.get(3).enabled=true;
				}
				return new StatusList();
			}
		});
		
		//황홀경
		put("황홀경", new FunctionStat("황홀경") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Equipment equipment =(Equipment)item;
				int num = equipment.getReinforce();
				if(num>12) num=12;
				StatusList statList = new StatusList();
				for(StatusAndName s: equipment.dStat.statList)
				statList.addStatList(s.name, new StatusInfo(num));
				return statList;
			}
		});
		
		//흑백마음
		put("흑백마음", new FunctionStat("흑백마음") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				statList.addStatList(type, 55, false, false, false);
				return statList;
			}
		});
		
		//조테카
		put("조테카", new FunctionStat("조테카") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - ") && character.getItemSetting().weapon.dStat.statList.getLast().enabled)
					statList.addStatList("스증뎀", 12.5);
				else if(character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - ") && character.getItemSetting().weapon.dStat.statList.getLast().enabled)
					statList.addStatList("스증뎀", (137.0/122.0-1)*100);
				return statList;
			}
		});
		
		//조로크
		put("조로크", new FunctionStat("조로크") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = character.getItemSetting().weapon;
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - ") || character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - ")){
					try {
						int phy = (int)Math.round(weapon.vStat.findStat(StatList.WEP_PHY, weapon.getItemStatIndex()).stat.getStatToDouble()*0.15);
						int mag = (int)Math.round(weapon.vStat.findStat(StatList.WEP_MAG, weapon.getItemStatIndex()).stat.getStatToDouble()*0.15);
						int ind = (int)Math.round(weapon.vStat.findStat(StatList.WEP_IND, weapon.getItemStatIndex()).stat.getStatToDouble()*0.18);
						statList.addStatList("물공", phy);
						statList.addStatList("마공", mag);
						statList.addStatList("독공", ind);						
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				return statList;
			}
		});
		
		//조그네스
		put("조그네스", new FunctionStat("조그네스") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - "))
					statList.addStatList("스증뎀", (155.0/135.0-1)*100);
				else if(character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - "))
					statList.addStatList("스증뎀", (160.0/140.0-1)*100);
				return statList;
			}
		});
		
		//고스로리 드레스
		put("고스로리", new FunctionStat("고스로리") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Equipment equipment =(Equipment)item;
				int num = equipment.getReinforce();
				if(num<11)
					statList.addStatList("모속강", num);
				else
					statList.addStatList("모속강", num>15 ? 20 : 10+(num-10)*2);
				return statList;
			}
		});
		
		//불꽃너울
		put("불꽃너울", new FunctionStat("불꽃너울"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				int epic=0;
				int legendary=0;			
				switch(character.getItemSetting().weapon.getRarity()){
				case EPIC:
					epic++;
					break;
				case LEGENDARY:
					legendary++;
					break;
				default:
					break;
				}				
				for(Equipment e : character.getItemSetting().equipmentList.values())
				{
					switch(e.getRarity()){
					case EPIC:
						epic++;
						break;
					case LEGENDARY:
						legendary++;
						break;
					default:
						break;
					}
				}
				statList.addStatList("화속강", epic*3+legendary*2);
				return statList;
			}
		});
		
		//파워드 철갑
		put("파워드철갑", new FunctionStat("파워드철갑"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					for(Skill skill : character.getSkillList()){
						if(skill.getName().equals("중화기 마스터리")){
							if(skill.getActiveEnabled()){
								Double buffAmount;
								try {
									buffAmount = skill.getSkillLevelInfo(true, character.isBurning()).stat.findStat(StatList.BUF_INC).stat.getStatToDouble();
									statList.addStatList("증뎀버프", ((buffAmount+100+5)/(buffAmount+100)-1)*100);
								} catch (StatusTypeMismatch e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return statList;
			}
		});
		
		//목각
		put("목각", new FunctionStat("목각"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Character_type type = character.getJob().charType;
				if(type!=Character_type.DEMONICLANCER && type!=Character_type.GUNNER_F && type!=Character_type.GUNNER_M &&
						type!=Character_type.MAGE_F && type!=Character_type.MAGE_M){
					statList.addSkillRange(45, 45, 4, false);
				}
				return statList;
			}
		});
		
		//나가자라
		put("나가라자", new FunctionStat("나가라자"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Equipment equip = (Equipment)item;
				int reinforce = equip.getReinforce()>15 ? 15 : equip.getReinforce();
				if(equip.getPart()==Equip_part.ROBE) statList.addStatList("추뎀", reinforce);
				else if(equip.getPart()==Equip_part.SHOULDER) statList.addStatList("스증뎀", reinforce);
				return statList;
			}
		});
	
		//칠죄종
		put("칠죄종", new FunctionStat("칠죄종"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Equipment equip = (Equipment)item;
				switch(equip.getPart()){
				case ROBE:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==45) statList.addSkill_damage(skill.getName(), 12);
					break;
				case TROUSER:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==40) statList.addSkill_damage(skill.getName(), 12);
					break;
				case SHOULDER:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==35) statList.addSkill_damage(skill.getName(), 15);
					break;
				case BELT:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==30) statList.addSkill_damage(skill.getName(), 15);
					break;
				case SHOES:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==25) statList.addSkill_damage(skill.getName(), 15);
					break;
				default:
					break;
				}
				return statList;
			}
		});
		

		//다중선택시 마지막 옵션으로 적용(단리연산항)
		put("멀티체크", new FunctionStat("멀티체크"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				int count=0;
				double sum=0;
				Equipment equipment = (Equipment)item;
				int num = equipment.dStat.statList.size();
				for(int i=0; i<num; i++){
					try {
						if(equipment.dStat.statList.get(i).enabled){
							count++;
							if(i!=num-1) sum-=equipment.dStat.statList.get(i).stat.getStatToDouble();
						}
						else if(i==num-1) sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
					}
					catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				if(count>1) statList.addStatList(equipment.dStat.statList.getFirst().name, sum);
				return statList; 
			}
		});
		
		//이기 속성
		put("이기속성", new FunctionStat("이기속성"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				statList.addStatList(type, 0, true, false, false);
				return statList;
			}
		});
		
		//레홀
		put("레홀", new FunctionStat("레홀"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				double percent=0;
				for(Skill skill : character.getSkillList())
				{
					if(skill.getName().equals("충전 레이저 라이플")){
						try {
							percent = skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble();
							break;
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
							break;
						}
					}
				}
				double inc = (100+percent*1.3)/(100+percent);
				StatusList statList = new StatusList();
				statList.addSkill_damage("레이저 라이플", inc);
				
				return statList;
			}
		});
		
		//실불
		put("실불", new FunctionStat("실불"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				//if(monster.getStat("종족"))			//TODO
				return statList;
			}
		});
		
		//골드럭스
		put("골드럭스", new FunctionStat("골드럭스"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.RANGER_F || character.getJob()==Job.RANGER_M)
					statList.addSkillRange(1, 80, 1, false);
				
				int sum=0;
				int epic=0;
				int legendary=0;
				int unique=0;				
				switch(character.getItemSetting().weapon.getRarity()){
				case EPIC:
					epic++;
					break;
				case LEGENDARY:
					legendary++;
					break;
				case UNIQUE:
					unique++;
					break;
				default:
					break;
				}				
				for(Equipment e : character.getItemSetting().equipmentList.values())
				{
					switch(e.getRarity()){
					case EPIC:
						epic++;
						break;
					case LEGENDARY:
						legendary++;
						break;
					case UNIQUE:
						unique++;
						break;
					default:
						break;
					}
				}
				
				sum+= (epic<=6 ? epic*9 : 54);
				epic= sum/9;
				sum+= (legendary<=6-epic ? legendary*7 : 7*(6-epic));
				legendary= (legendary<=6-epic ? legendary : 6-epic);
				sum+= (unique<=6-epic-legendary ? unique*7 : 7*(6-epic-legendary));
				
				statList.addStatList("추뎀", sum);
				return statList;
			}
		});
		
		//이온 리펄서
		put("이온리펄서", new FunctionStat("이온리펄서"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob().charType==Character_type.GUNNER_F) statList.addStatList("명속강", 40);
				else if(character.getJob().charType==Character_type.GUNNER_M) statList.addStatList("화속강", 40);
				return statList;
			}
		});
		
		//도굴왕
		put("도굴왕", new FunctionStat("도굴왕"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					for(Skill skill : character.getSkillList()){
						if(skill.getName().equals("중화기 마스터리")){
							if(skill.getActiveEnabled()){
								double buffAmount;
								int skillLevel;
								try {
									buffAmount = skill.getSkillLevelInfo(true, character.isBurning()).stat.findStat(StatList.BUF_INC).stat.getStatToDouble();
									statList.addStatList("증뎀버프", ((buffAmount+100+10)/(buffAmount+100) -1)*100 );
									skillLevel = skill.getSkillLevel(true, character.isBurning());
									if(skillLevel>=10 && skillLevel<15) statList.addStatList("물공뻥", 2);
									else statList.addStatList("물공뻥", 3);
								} catch (StatusTypeMismatch e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return statList;
			}
		});
		
		//테슬라
		put("테슬라", new FunctionStat("테슬라"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					statList.addSkill("M-3 화염방사기", 3);
					statList.addSkill("화염 강타", 4);
				}
				else if(character.getJob()==Job.LAUNCHER_M){
					statList.addSkill("레이저 라이플", 3);
					statList.addSkill("충전 레이저 라이플", 4);
				}
				return statList;
			}
		});
		
		//무게추(18%), 뉴링턴(18%), 히게-히자키리(20%)
		put("무게추", new FunctionStat("무게추"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				int value = 18;
				if(weapon.getName().equals("히게-히자키리")) value=20;
				switch(type){
				case FIRE:
					statList.addStatList("화추뎀", value);
					break;
				case WATER:
					statList.addStatList("수추뎀", value);
					break;
				case LIGHT:
					statList.addStatList("명추뎀", value);
					break;
				case DARKNESS:
					statList.addStatList("암추뎀", value);
					break;
				default:
					statList.addStatList("화추뎀", value);
					break;
				}
				return statList;
			}
		});
		
		//천총
		put("천총", new FunctionStat("천총"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.CHAOS){
					statList.addStatList("지능", 120);
					statList.addStatList("독공", 75);
				}
				return statList;
			}
		});
		
		//암별
		put("암별", new FunctionStat("암별"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				statList.addStatList(type, 25, false, false, false);
				return statList;
			}
		});
		
		//염화도
		put("염화도", new FunctionStat("염화도"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				boolean enabled_35 = weapon.dStat.statList.get(0).enabled;
				boolean enabled_10 = weapon.dStat.statList.get(1).enabled;
				boolean enabled_7 = weapon.dStat.statList.get(2).enabled;
				boolean enabled_5 = weapon.dStat.statList.get(3).enabled;
				if(enabled_7 && enabled_35) statList.addStatList("화추뎀", -35);
				if(enabled_5 && enabled_10) statList.addStatList("화추뎀", -10);
				return statList;
			}
		});
		
		//트리니티
		put("트리니티", new FunctionStat("트리니티"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				boolean enabled_33 = weapon.dStat.statList.get(0).enabled;
				boolean enabled_333 = weapon.dStat.statList.get(1).enabled;
				boolean enabled_21 = weapon.dStat.statList.get(2).enabled;
				if(enabled_21 && enabled_33) statList.addStatList("추뎀", -33);
				if(enabled_21 && enabled_333) statList.addStatList("추뎀", -333);
				return statList;
			}
		});
		
		//거누형
		put("거누형", new FunctionStat("거누형"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				if(weapon.getReinforce()>15) statList.addStatList("추뎀", 15);
				else statList.addStatList("추뎀", weapon.getReinforce());
				//TODO 맹룡 베기수 1 감소, 딜 50% 증가
				return statList;
			}
		});
		
		//이빨
		put("이빨", new FunctionStat("이빨"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				int checked=0;
				for(StatusAndName s: weapon.dStat.statList)
					if(s.enabled) checked++;
				if(checked>=4) statList.addStatList("추뎀", -18);
				else statList.addStatList("추뎀", -checked*5);
				return statList;
			}
		});
		
		//귀면도
		put("귀면도", new FunctionStat("귀면도"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				try {
					int STR = (int) character.dungeonStatus.getStat(StatList.STR);
					int INT = (int) character.dungeonStatus.getStat(StatList.INT);
					if(STR>INT){
						statList.addStatList(StatList.INT, new StatusInfo(-300));
						statList.addStatList(StatList.STR, new StatusInfo(300));
					}
					else{
						statList.addStatList(StatList.INT, new StatusInfo(300));
						statList.addStatList(StatList.STR, new StatusInfo(-300));
					}
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				for(Skill skill : character.getSkillList())
					if(skill.firstLevel==40 || skill.firstLevel==50)
						statList.addSkill_damage(skill.getName(), 30);
				return statList;
			}
		});
		
		//신라비
		put("신라비", new FunctionStat("신라비"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				int water=0, dark=0;
				try {
					water = (int) (character.dungeonStatus.getStat(StatList.ELEM_WATER)
							+character.dungeonStatus.getStat(StatList.ELEM_ALL));
					dark = (int) (character.dungeonStatus.getStat(StatList.ELEM_DARKNESS)
							+character.dungeonStatus.getStat(StatList.ELEM_ALL));
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				double dark_sum=0, water_sum=0, normal_sum=0;
				boolean dark_flag=false, water_flag=false;
				Equipment equipment = (Equipment)item;
				for(int i=0; i<7; i++){
					try {
						if(equipment.dStat.statList.get(i).enabled){
							if(i<3){
								if(!dark_flag && dark>=80) dark_flag=true;
								else dark_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
							}
							else if(i<6){
								if(!water_flag && (water>=80 && dark<80)) water_flag=true;
								else water_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
							}
							else if(dark_flag || water_flag || (dark<80 && water<80)) 
								normal_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
						}
					}
					catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				statList.addStatList(StatList.DAM_ADD_WATER, -water_sum);
				statList.addStatList(StatList.DAM_ADD_DARKNESS, -dark_sum);
				statList.addStatList(StatList.DAM_ADD, -normal_sum);
				return statList;
			}
		});
		
		//프리스트 스킬증가
		put("프리무기", new FunctionStat("프리무기"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Weapon weapon = (Weapon)item;
				StatusList statList = new StatusList();
				if(weapon.getName().equals("행성파괴자")){
					if(!character.getJob().equals(Job.EXORCIST)){
						int count=0;
						for(StatusAndName s: weapon.dStat.statList)
							if(s.enabled) count++;
						statList.addSkillRange(1, 85, -count, false);
					}
				}
				else if(weapon.getName().equals("음양사천")){
					if(!character.getJob().equals(Job.EXORCIST)){
						statList.addSkillRange(48, 80, -2, false);
					}
				}
				else if(weapon.getName().equals("탐라선인석")){
					if(!character.getJob().equals(Job.INFIGHTER)){
						statList.addSkillRange(1, 85, -1, false);
					}
				}
				return statList;
			}
		});
		
		//소울 디바우링
		put("디바우링", new FunctionStat("디바우링"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				int darkness =0;
				StatusList statList = new StatusList();
				try {
					darkness = (int) (character.dungeonStatus.getStat(StatList.ELEM_ALL)+character.dungeonStatus.getStat(StatList.ELEM_DARKNESS));
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				if(darkness>=120) statList.addStatList("마공뻥", 16);
				return statList;
			}
		});
		
		//크림슨로드, 서킷 세레브레이트
		put("크림슨로드", new FunctionStat("크림슨로드"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				Weapon weapon = (Weapon)item;
				int inc1 = weapon.getReinforce()*3;
				if(inc1>30) inc1=30;
				int inc2 = (weapon.getReinforce()-10)*5;
				if(inc2>10) inc2=10;
				else if(inc2<0) inc2=0;
				int inc3 = weapon.getReforge()*3;
				
				StatusList statList = new StatusList();
				statList.addStatList("증뎀", inc1);
				statList.addStatList("스증뎀", inc2);
				statList.addStatList("추뎀", inc3);
				return statList;
			}
		});
		
		//래쿤배큠
		put("래쿤", new FunctionStat("래쿤"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getJob().equals(Job.CREATOR))
					statList.addSkillRange(1, 85, 1, false);
				else
					statList.addSkillRange(15, 20, 3, false);
				return statList;
			}
		});
		
		//웨리
		put("웨리", new FunctionStat("웨리"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				try {
					if(Double.compare(weapon.dStat.statList.getFirst().stat.getStatToDouble(), 64)==0
							|| Double.compare(weapon.dStat.statList.getLast().stat.getStatToDouble(), 64)==0) {
						if(character.getJob().equals(Job.CREATOR))
							statList.addSkill("창조의 공간", 2);
						else statList.addSkillRange(85, 85, 2, false);
					}
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		});
		
		//스킹주문서
		put("스킹주문서", new FunctionStat("스킹주문서") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				statList.addStatList("물공", character.getLevel()/2);
				statList.addStatList("마공", character.getLevel()/2);
				return statList;
			}
		});
		
		//엘마스위칭
		put("엘마스위칭", new FunctionStat("엘마스위칭"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				Equipment[] equip;
				Buff buff = (Buff)item;
				if(buff.getName().contains("무기->")) equip = new Equipment[] {character.getWeapon()};
				else if(buff.getName().contains("방어구->"))
					equip = new Equipment[] { character.getEquipmentList().get(Equip_part.ROBE),
							character.getEquipmentList().get(Equip_part.TROUSER), character.getEquipmentList().get(Equip_part.SHOULDER),
							character.getEquipmentList().get(Equip_part.BELT), character.getEquipmentList().get(Equip_part.SHOES)};
				else if(buff.getName().contains("악세->"))
					equip = new Equipment[] { character.getEquipmentList().get(Equip_part.BRACELET),
							character.getEquipmentList().get(Equip_part.NECKLACE), character.getEquipmentList().get(Equip_part.RING) };
				else if(buff.getName().contains("특수장비->"))
					equip = new Equipment[] { character.getEquipmentList().get(Equip_part.AIDEQUIPMENT),
							character.getEquipmentList().get(Equip_part.MAGICSTONE), character.getEquipmentList().get(Equip_part.EARRING) };
				else equip = new Equipment[0];
				
				try {
					for(Equipment e : equip){
						for(StatusAndName s : e.vStat.statList){
							switch(s.name){
							case StatList.WEP_MAG: case StatList.WEP_PHY:
							case StatList.ELEM_ALL: case StatList.ELEM_DARKNESS: 
							case StatList.ELEM_FIRE: case StatList.ELEM_WATER: case StatList.ELEM_LIGHT:
							case StatList.SKILL: case StatList.SKILL_RANGE:
							case StatList.MAST_MAG_ITEM: case StatList.MAST_INDEP_ITEM: case StatList.MAST_PHY_ITEM:
							case StatList.INT_INC: case StatList.STR_INC:
								break;
							case StatList.DAM_SKILL:
								statList.addStatList(StatList.DAM_SKILL, -100+10000/(100.0+s.stat.getStatToDouble()));
								break;
							default:
								statList.addStatList(s.name, (int)-s.stat.getStatToDouble());	
							}
						}
						for(StatusAndName s : e.dStat.statList){
							switch(s.name){
							case StatList.WEP_MAG: case StatList.WEP_PHY:
							case StatList.ELEM_ALL: case StatList.ELEM_DARKNESS: 
							case StatList.ELEM_FIRE: case StatList.ELEM_WATER: case StatList.ELEM_LIGHT:
							case StatList.SKILL: case StatList.SKILL_RANGE:
							case StatList.MAST_MAG_ITEM: case StatList.MAST_INDEP_ITEM: case StatList.MAST_PHY_ITEM:
							case StatList.INT_INC: case StatList.STR_INC:
								break;
							case StatList.DAM_SKILL:
								statList.addStatList(StatList.DAM_SKILL, -100+10000/(100.0+s.stat.getStatToDouble()));
								break;
							default:
								statList.addStatList(s.name, (int)-s.stat.getStatToDouble());	
							}
						}
					}
				}
				catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		});
		
		//무기마스터리
		put("물리무기마스터리", new FunctionStat("물리무기마스터리"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.weaponType.getName().equals(args[2])){
					statList.addStatList("물리마스터리", Double.valueOf(args[1]));
				}
				statList.addListToStat(character.villageStatus);
				return statList;
			};
		});
		
		put("마법무기마스터리", new FunctionStat("마법무기마스터리"){
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.weaponType.getName().equals(args[2])){
					statList.addStatList("마법마스터리", Double.valueOf(args[1]));
				}
				statList.addListToStat(character.villageStatus);
				return statList;
			};
		});
		
		//핀드
		put("핀드", new FunctionStat("핀드") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) 
			{
				StatusList statList = new StatusList();
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				switch(type){
				case FIRE:
					statList.addStatList("화추뎀", 25);
					break;
				case WATER:
					statList.addStatList("수추뎀", 25);
					break;
				case LIGHT:
					statList.addStatList("명추뎀", 25);
					break;
				case DARKNESS:
					statList.addStatList("암추뎀", 25);
					break;
				default:
					statList.addStatList("화추뎀", 25);
				}
				return statList;
			}
		});
		
		//자수
		put("자수", new FunctionStat("자수") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
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
		
		put("거미", new FunctionStat("거미") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
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
		
		put("헤블론", new FunctionStat("헤블론") {
			@Override
			public StatusList function(Characters character, Monster monster, Object item, String[] args) {
				SetOption set =(SetOption)item;
				StatusList statList = new StatusList();
				if(!set.dStat.statList.get(0).enabled){
					Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
					switch(type){
					case FIRE:
						statList.addStatList("화추뎀", 15);
						break;
					case WATER:
						statList.addStatList("수추뎀", 15);
						break;
					case LIGHT:
						statList.addStatList("명추뎀", 15);
						break;
					case DARKNESS:
						statList.addStatList("암추뎀", 15);
						break;
					default:
						statList.addStatList("화추뎀", 15);
					}
				}
				return statList;
			}
		});
	}};
}
