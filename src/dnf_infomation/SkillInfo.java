package dnf_infomation;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.FunctionStat;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_class.SubSkill;
import dnf_class.SwitchingSkill;
import dnf_class.TPSkill;

public class SkillInfo {
	
	public static void getInfo(LinkedList<Skill> skillList, Object[] data) throws ParsingException
	{
		int i=0;
		String name=null;
		Skill_type type=null;
		String[] targets = null;
		boolean isTPSkill=false;
		boolean isSubSkill=false;
		Job job=null;
		Character_type charType=null;
		boolean jobDefined=true;
		int firstLevel=0;
		int maxLevel=0;
		int masterLevel=0;
		int interval=0;
		Element_type element=null;
		String version=null;
		Skill skill=null;
		
		SkillLevelInfo levelInfo = null;
		int skillLevel=0;
		double[] skillNum = new double[4];
		
		String[] stat=null;
		ArrayList<Double> prevStat=null;
		int statOrder=0;

		Object temp="first";
		
		try{
		while(i<data.length)
		{
			name = (String) data[i++];

			//타입
			temp = data[i++];
			if(temp instanceof Skill_type){
				type = (Skill_type) temp;
				isTPSkill=false;
				isSubSkill=false;
			}
			else if(temp.equals(""));	//이전 값 유지
			else if(temp instanceof String){
				if(((String) temp).contains("원본 - ")){
					targets = new String[]{((String)temp).substring(5)};
					isSubSkill=true;
					isTPSkill=false;
				}
				else{
					targets = ((String) temp).split(" & ");
					isTPSkill=true;
					isSubSkill=false;
				}
			}
			else throw new ParsingException(i-1, temp);
			
			//직업
			temp = data[i++];
			if(temp instanceof Job){
				job = (Job) temp;
				jobDefined=true;
			}
			else if(temp instanceof Character_type){
				charType = (Character_type) temp;
				jobDefined=false;
			}
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//시작레벨
			temp = data[i++];
			if(temp instanceof Integer) firstLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//맥스레벨
			temp = data[i++];
			if(temp instanceof Integer) maxLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//마스터레벨
			temp = data[i++];
			if(temp instanceof Integer) masterLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			
			//레벨구간 (TP스킬의 경우 증가폭)
			temp = data[i++];
			if(temp instanceof Integer) interval = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			if(!isTPSkill && (type==Skill_type.DAMAGE_BUF || type==Skill_type.ACTIVE || type==Skill_type.OPTION)){
				//속성
				temp = data[i++];
				if(temp instanceof Element_type) element = (Element_type) temp;
				else if(temp.equals(""));	//이전 값 유지
				else{
					if(type!=Skill_type.OPTION) throw new ParsingException(i-1, temp);
					else i--;
				}
			}
			
			if(data[i] instanceof String && ((String)data[i]).contains("ver_"))
				version = (String) data[i++];
			else
				version = CalculatorVersion.VER_1_0_a;
			
			if(jobDefined){
				if(isTPSkill) skill = new TPSkill(name, targets, job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, job, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, job, firstLevel, maxLevel, masterLevel, interval, element, version);
			}
			else{
				if(isTPSkill) skill = new TPSkill(name, targets, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, charType, firstLevel, maxLevel, masterLevel, interval, element, version);
			}
			
			while(true)
			{
				if(data[i]!=null && ((String)data[i]).contains("설명")){
					temp = data[i++];
					String explanation = ((String)temp).substring(3);
					skill.explanation.add(explanation);
				}
				else break;
			}
			
			prevStat=new ArrayList<Double>();
			try{
				//스킬 수치
				skillLevel=0;
				while(true)
				{	
					temp = data[i++];
					if(temp==null) break;
					
					else if(((String)temp).contains("반복"))
					{
						i--;
						int compNum=2;
						if(levelInfo.fStat.statList.size()!=0) compNum=3;
						
						int repNum = Integer.valueOf(((String)temp).split(" ")[1]);
						int levelDiff = skill.maxLevel-skillLevel;
						int startIndex = i-compNum*levelDiff;
						
						String[] strData = new String[repNum];
						FunctionStat[] fstatData = new FunctionStat[repNum];
						for(int j=0; j<repNum; j++){
							if(!((String) data[i-compNum*repNum+compNum*j]).equals("+")) throw new ParsingException(i-1, temp); 
							if(compNum==2)
								strData[j] = (String) data[i-compNum*repNum+compNum*j+1];
							else{
								fstatData[j] = (FunctionStat) data[i-compNum*repNum+compNum*j+1];
								strData[j] = (String) data[i-compNum*repNum+compNum*j+2];
							}
						}
						
						for(int j=0; j<levelDiff; j++)
						{
							data[startIndex+j*compNum] = "+";
							if(compNum==2) data[startIndex+j*compNum+1] = strData[j%repNum];
							else{
								data[startIndex+j*compNum+1] = fstatData[j%repNum];
								data[startIndex+j*compNum+2] = strData[j%repNum];
							}
						}
						
						data[i]=null;
						i-=compNum*levelDiff;
						temp = data[i++];
					}

					stat = ((String)temp).split(" ");
					if(stat[0].equals("+")) skillLevel++;
					else if(stat[0].equals("-")) skillLevel--;
					else skillLevel = Integer.parseInt(stat[0]);
						
					if(skill.hasDamage() || skill.type==Skill_type.SWITCHING){
						if(stat.length==5){
							for(int j=0; j<4; j++)
								skillNum[j]=Parser.parseForm(stat[j+1], skillNum[j]);
						}
						else if(stat.length!=1){
							int index=1;
							for(int j=0; j<4; j++){
								if(skillNum[j]==0) continue;
								else skillNum[j]=Parser.parseForm(stat[index++], skillNum[j]);
							}
						}
						else for(int j=0; j<4; j++) skillNum[j]=0;
						levelInfo = new SkillLevelInfo(skillLevel, (int)Math.round(skillNum[0]), skillNum[1], (int)Math.round(skillNum[2]), skillNum[3]);
					}
					else levelInfo = new SkillLevelInfo(skillLevel);
					
					if(skill.hasBuff() || (data[i] instanceof String && ((String)data[i]).startsWith("귀속"))){
						temp = data[i++];
						
						if(temp instanceof FunctionStat){
							levelInfo.fStat.statList.add((FunctionStat) temp);
							temp = data[i++];
						}
						
						if(temp!=null){
							String[] statList = ((String)temp).split(" & ");
							for(String str : statList){
								if(str.isEmpty()) continue;
								if(str.contains("귀속")){
									String[] skillAndPercent = str.split("/");
									
									int result;
									if(skillAndPercent[skillAndPercent.length-1].startsWith("+"))
										result = (int) (Parser.parseForm(skillAndPercent[skillAndPercent.length-1], prevStat.get(statOrder))+0.0001);
									else result = (int) (Parser.parseForm(skillAndPercent[skillAndPercent.length-1], 0)+0.0001);
									levelInfo.percentList.put(skillAndPercent[1], result);
									if(statOrder==prevStat.size()){
										prevStat.add((double)result);
										statOrder++;
									}
									else prevStat.set(statOrder++, (double)result);
									continue;
								}
								
								stat = ((String)str).split(" ");
								if(stat[stat.length-1].startsWith("+")){
									String compareStat = stat[stat.length-1];
									stat[stat.length-1]=Double.toString(Double.valueOf(compareStat) + prevStat.get(statOrder));
								}
								double result = Parser.parseStat(stat, levelInfo.stat, levelInfo.fStat);
								if(statOrder==prevStat.size()){
									prevStat.add(result);
									statOrder++;
								}
								else prevStat.set(statOrder++, result);
							}
						}
						else i--;
					}
					skill.skillInfo.add(levelInfo);
					statOrder=0;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("앞1 : "+data[i-2]);
				System.out.println("앞2 : "+data[i-3]);
				System.out.println("뒤1 : "+data[i]);
				System.out.println("뒤2 : "+data[i+1]);
				throw new ParsingException(i-1, temp);
			}
				
			skillList.add(skill);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("앞1 : "+data[i-2]);
			System.out.println("앞2 : "+data[i-3]);
			System.out.println("뒤1 : "+data[i]);
			System.out.println("뒤2 : "+data[i+1]);
			throw new ParsingException(i-1, temp);
		}
	}
	
	public static Object[] skillInfo_swordman()
	{
		FunctionStat fStat[] = new FunctionStat[2];
		
		//혈지군무 분신
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				int level = character.characterInfoList.getSkill("혈지군무").getSkillLevel(true, character.isBurning());
				if(level!=0) skill.setSkillLevel(level);
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
				skill.dungeonIncrease *= 100.0/(100+skillEnhance);
				return statList;
			}
		};
		
		Object[] data = new Object[] {
				
				///////////////검마
				"사복검 - 발", Skill_type.ACTIVE, Job.DEMONSLAYER, 15, 60, 50, 2, Element_type.NONE, 
				"38 1298*10 0 0 0", "+ 1325*10", "+ 1298*10","+ 1353*10","+ 1380*10","+ 1409*10","+ 1437*10","+ 1464*10",null,
				"사복검 - 무", Skill_type.ACTIVE, Job.DEMONSLAYER, 20, 60, 50, 2, Element_type.NONE, 
				"36 2750*2+3666 0 0 0", "+ 2811*2+3748" , "+ 2874*2+3831", "+ 2932*2+3911", "+ 2998*2+3997", "+ 3059*2+4078", "+ 3117*2+4158", "+ 3181*2+4241", null,
				"사복검 - 조", Skill_type.ACTIVE, Job.DEMONSLAYER, 25, 60, 50, 2, Element_type.NONE, 
				"33 6098+3050*3 0 0 0", "+ 6245+3122*3", "+ 6391+3196*3", "+ 6536+3269*3", "+ 6683+3340*3", "+ 6828+3414*3", "+ 6975+3488*3", "+ 7120+3561*3", "+ 7266+3632*3", null,
				//충격파 물리공격력은 직접타격엔 영향없고 독오 퍼뎀은 뜯기와 같음 (안맞은적만 적용)
				"사형조수", Skill_type.ACTIVE, Job.DEMONSLAYER, 25, 60, 50, 2, Element_type.NONE, 
				"33 5323+7987 0 0 0", "+ 5453+8178", "+ 5579+8370", "+ 5708+8564", "+ 5831+8749", "+ 5964+8944", "+ 6090+9133", "+ 6219+9329", "+ 6345+9517", null,
				"폭류나선", Skill_type.ACTIVE, Job.DEMONSLAYER, 30, 60, 50, 2, Element_type.NONE, 
				"31 1456*12 0 0 0", "+ 1491*12", "+ 1530*12", "+ 1564*12", "+ 1603*12", "+ 1639*12", "+ 1674*12", "+ 1712*12", "+ 1750*12", "+ 1785*12", "+ 1822*12", null,
				"혈화난무", Skill_type.ACTIVE, Job.DEMONSLAYER, 35, 60, 50, 2, Element_type.NONE, 
				"28 2092*11+5451 0 0 0", "+ 2150*11+5596", "+ 2206*11+5739", "+ 2264*11+5886", "+ 2320*11+6031", "+ 2378*11+6175",
				"+ 2435*11+6325", "+ 2492*11+6469", "+ 2549*11+6613", "+ 2604*11+6760", "+ 2663*11+6908",  "+ 2720*11+7050", null,
				"혈마인", Skill_type.ACTIVE, Job.DEMONSLAYER, 35, 60, 50, 2, Element_type.NONE, 
				"28 6091+14011+1942*16 0 0 0", "+ 6256+14391+1994*16", "+ 6256+14391+1994*16", "+ 6422+14771+2048*16", "+ 6587+15151+2099*16", "+ 6752+15531+2153*16", "+ 6917+15911+2205*16",
				"+ 7083+16291+2258*16", "+ 7248+16671+2311*16", "+ 7413+17052+2363*16", "+ 7578+17432+2416*16", "+ 7744+17812+2469*16", "+ 7909+18192+2521*16", null,
				"검마격살", Skill_type.ACTIVE, Job.DEMONSLAYER, 45, 60, 50, 2, Element_type.NONE, 
				"23 6693+17849+2232*8 0 0 0", "+ 6905+18413+2302*8", "+ 7106+18950+2369*8", "+ 7318+19515+2439*8", "+ 7529+20079+2510*8", "+ 7742+20644+2580*8",
				"+ 7953+21209+2652*8", "+ 8165+21773+2722*8", "+ 8376+22337+2792*8", null,
				"암연검 : 기가블레이드", Skill_type.ACTIVE, Job.DEMONSLAYER, 50, 40, 30, 5, Element_type.NONE, 
				"10 2867*20+2648*7+2042*7+11469+17203+22943 0 0 0", "+ 3082*20+2845*7+2159*7+12332+18496+24664 0 0 0", "+ 3297*20+3044*7+2280*7+13192+19790+26387 0 0 0", "+ 3514*20+3242*7+2280*7+14053+21082+28113 0 0 0",
				"+ 3729*20+3442*7+2403*7+14916+22374+29834 0 0 0", "+ 3945*20+3642*7+2523*7+15777+23666+31557 0 0 0", "9 2652*20+2451*7+1925*7+10606+15910+21222 0 0 0", null,
				"포식자 갈로아", Skill_type.ACTIVE, Job.DEMONSLAYER, 60, 40, 30, 2, Element_type.NONE, 
				"16 3239*12+5567*3 0 0 0", "+ 3420*12+5789*3", "+ 3553*12+6013*3", "+ 3685*12+6238*3", "+ 3818*12+6462*3", "+ 3950*12+6686*3",
				"+ 4081*12+6910*3", "+ 4214*12+7133*3", null,
				"역천의 프놈", Skill_type.ACTIVE, Job.DEMONSLAYER, 70, 40, 30, 2, Element_type.NONE, 
				"11 2873*5+2393*8+14361 0 0 0", "+ 3018*5+2513*8+15084", "+ 3162*5+2634*8+15808", "+ 3308*5+2754*8+16531", "+ 3453*5+2876*8+17254",
				"+ 3596*5+2996*8+17978", "+ 3742*5+3115*8+18701", "+ 3885*5+3237*8+19425", null,
				"암연격 : 기가슬래쉬", Skill_type.ACTIVE, Job.DEMONSLAYER, 75, 40, 30, 2, Element_type.NONE, 
				"8 64069 0 0 0", "+ 67870", "+ 71671", "+ 75472", "+ 79272", "+ 83073", "+ 86874", null,
				"비인외도 : 극", Skill_type.ACTIVE, Job.DEMONSLAYER, 80, 40, 30, 2, Element_type.NONE, 
				"6 64089 0 0 0", "+ 68402", "+ 72719", "+ 77032", "+ 81345", "+ 85659", null,
				"파계검 : 라그나로크", Skill_type.ACTIVE, Job.DEMONSLAYER, 85, 40, 30, 5, Element_type.NONE, 
				"2 3063*10+11336*3+42325 0 0 0", "+ 3640*10+13471*3+50293", "+ 4217*10+15604*3+58260", "+ 4794*10+17739*3+66228", "+ 5370*10+19874*3+74195", null,

				"탐욕의 번제", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 3,
				"15", "증뎀버프 33", "+", "증뎀버프 +1.5", "반복 1",
				"마인의 검세", Skill_type.PASSIVE, "", 75, 40, 30, 3,
				"5", "스킬 폭류나선 % 32 & 스킬 혈화난무 % 48*0.596 & 스킬 사복검 - 조 % 32*0.6 & 스킬 검마격살 % 50*0.421+48*0.421 & 스킬 사복검 - 발 % 19 & 스킬 사복검 - 무 % 32 & 스킬 사형조수 % 32 & 스킬 암연검 : 기가블레이드 % 32 & "
						+ "스킬 포식자 갈로아 % 32 & 스킬 역천의 프놈 % 32 & 스킬 암연격 : 기가슬래쉬 % 32 & 스킬 비인외도 : 극 % 32 & 스킬 파계검 : 라그나로크 % 32 & 스킬 혈지군무 % 32",
				"6", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 50*0.421+50*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"7", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+52*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & " 
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"8", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+54*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"9", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+56*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"10", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+58*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"11", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+60*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"12", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+62*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"13", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 87.5*0.421+64*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2", null,
									
				"광폭화", Skill_type.SWITCHING, "", 20, 30, 20, 3, 
				"20", "증뎀버프 40 & 스킬 혈마인 % 30 & 스킬 혈지군무 % 30 & 스킬 검마격살 % 30 & 스킬 포식자 갈로아 % 30 & 스킬 역천의 프놈 % 30 & 스킬 암연검 : 기가블레이드 % 30 & 스킬 암연격 : 기가슬래쉬 % 30 & 스킬 파계검 : 라그나로크 % 30", null,
				"사복검 - 강", Skill_type.SWITCHING, "", 30, 20, 10, 3, 
				"10", "스킬 사복검 - 발 % 25.7 & 스킬 사복검 - 조 % 25.7 & 스킬 사복검 - 무 % 25.7 & 스킬 사형조수 % 25.7 & 스킬 폭류나선 % 25.7 & 스킬 혈화난무 % 25.7 & 스킬 비인외도 : 극 % 25.7 & 스킬 파계검 : 라그나로크 % 25.7", null,

				"혈지군무", Skill_type.ACTIVE, "", 40, 30, 20, 3, Element_type.NONE,
				"17 0 0 0 0", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100",
				"+", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100", "반복 1",
				"혈지군무 - 발", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 발 개체를 붙입니다",
				"17", fStat[0], "귀속/사복검 - 발/260", "+", fStat[0], "귀속/사복검 - 발/+4", "반복 1",
				"혈지군무 - 무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 무 개체를 붙입니다",
				"17", fStat[0], "귀속/사복검 - 무/236", "+", fStat[0], "귀속/사복검 - 무/240", "+", fStat[0], "귀속/사복검 - 무/244", "+", fStat[0], "귀속/사복검 - 무/247", "+", fStat[0], "귀속/사복검 - 무/251", 
				"+", fStat[0], "귀속/사복검 - 무/254", "+", fStat[0], "귀속/사복검 - 무/258", "+", fStat[0], "귀속/사복검 - 무/262", "+", fStat[0], "귀속/사복검 - 무/266", "+", fStat[0], "귀속/사복검 - 무/269", "반복 3",
				"혈지군무 - 사형조수", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사형조수 개체를 붙입니다",
				"17", fStat[0], "귀속/사형조수/234", "+", fStat[0], "귀속/사형조수/238", "+", fStat[0], "귀속/사형조수/242", "+", fStat[0], "귀속/사형조수/245", "+", fStat[0], "귀속/사형조수/249", 
				"+", fStat[0], "귀속/사형조수/252", "+", fStat[0], "귀속/사형조수/256", "+", fStat[0], "귀속/사형조수/259", "+", fStat[0], "귀속/사형조수/263", "+", fStat[0], "귀속/사형조수/266", "반복 3",
				"혈지군무 - 폭류나선", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 폭류나선 개체를 붙입니다",
				"17", fStat[0], "귀속/폭류나선/130", "+", fStat[0], "귀속/폭류나선/+2", "반복 1",  
				"혈지군무 - 혈화난무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 혈화난무 개체를 붙입니다",
				"17", fStat[0], "귀속/혈화난무/65", "+", fStat[0], "귀속/혈화난무/+1", "반복 1",
				
				"사복검 강화", "사복검 - 발 & 사복검 - 조 & 사복검 - 무", "", 55, 5, 7, 11, null, 
				"폭류나선 강화", "폭류나선", "", 55, 5, 7, 10, null,
				"사형조수 강화", "사형조수", "", 55, 5, 7, 10, null,
				"혈마인 강화", "혈마인", "", 65, 5, 7, 10, null,
				"혈화난무 강화", "혈화난무", "", 65, 5, 7, 10, null,
				"검마격살 강화", "검마격살", "", 65, 5, 7, 10, null,
		};
		return data;
	}
	
	public static Object[] skillInfo_gunner()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//듀얼트리거
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
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
		};
		
		fStat[1] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
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
		};
		
		//데바리
		fStat[2] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			double save;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
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
		};
		
		//강화탄
		fStat[4] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				if(character.characterInfoList.getSkill("강화탄(무)").buffEnabled(true)){
					statList.addStatList("%물방깍_스킬", skill.getSkillLevel(true, character.isBurning()));
					statList.addStatList("%마방깍_스킬", skill.getSkillLevel(true, character.isBurning()));
				}
				else if(character.characterInfoList.getSkill("강화탄(화)").buffEnabled(true))
					statList.addStatList("화속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
				else if(character.characterInfoList.getSkill("강화탄(수)").buffEnabled(true))
					statList.addStatList("수속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
				else if(character.characterInfoList.getSkill("강화탄(명)").buffEnabled(true))
					statList.addStatList("명속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
					
				if(character.characterInfoList.getSkill("강화탄(컨버전)").buffEnabled(true))
					statList.addStatList("물리마스터리", -100);
				else statList.addStatList("마법마스터리", -100);
				return statList;
			}
		};
		
		//병기숙련
		fStat[5] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
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
		};
		
		//매거진
		fStat[6] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill hit = character.characterInfoList.getSkill("평타(1사이클)");
				switch(character.getItemSetting().weapon.weaponType)
				{
				case GUN_BOWGUN:
					hit.setSkillLevel(21);
					hit.dungeonLevel=0;
					break;
				case GUN_MUSKET:
					hit.setSkillLevel(10);
					hit.dungeonLevel=0;
					break;
				case GUN_REVOLVER:
					hit.setSkillLevel(12);
					hit.dungeonLevel=0;
					break;
				case GUN_HCANON:
					hit.setSkillLevel(6);
					hit.dungeonLevel=0;
					break;
				case GUN_AUTOPISTOL:
					hit.setSkillLevel(18);
					hit.dungeonLevel=0;
					break;
				default:
					break;
				}
				return statList;
			}
		};
		
		//기숙
		fStat[7] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				if(character.hasContract()) skill.setSkillLevel(character.getLevel());
				return new StatusList();
			}
		};
		
		Object[] data = new Object[] {
				
				///////////////런처
				//캐넌볼
				"캐넌볼", Skill_type.ACTIVE, Job.LAUNCHER_F, 20, 60, 50, 2, Element_type.NONE,
				"36 3964*2 0 0 0", "+ 4052*2", "+ 4138*2", "+ 4225*2", "+ 4313*2", "+ 4398*2", null,
				//슈타
				"슈타이어 대전차포", "", "", 20, 60, 50, 2, Element_type.FIRE,
				"36 266+1592*3 2.659+15.92*3 0 0", "+ 271+1627*3 2.71+16.27*3", "+ 277+1663*3 2.77+16.63*3",
				"+ 284+1698*3 2.84+16.98*3", "+ 289+1735*3 2.889+17.35*3", "+ 295+1770*3 2.949+17.699*3", "+ 301+1806*3 3.010+18.060*3", null,
				//레이저
				"레이저 라이플", "", "", 25, 60, 50, 2, Element_type.LIGHT,
				"33 3876 25.83 0 0", "+ 3969 26.449", "+ 4060 27.08", "+ 4154 27.689", "+ 4246 28.310", "+ 4338 28.93", "+ 4431 29.539", null,
				//화강
				"화염 강타", "", "", 30, 60, 50, 2, Element_type.FIRE,
				"31 320*35 3.2*35 0 0", "+ 328*35 3.28*35", "+ 336*35 3.36*35", "+ 344*35 3.44*35", "+ 353*35 3.53*35", 
				"+ 361*35 3.61*35", "+ 369*35 3.69*35", "+ 377*35 3.77*35", "+ 385*35 3.85*35", null,
				//그레기
				"FM-31 그레네이드 런처", "", "", 35, 60, 50, 2, "",
				"28 1487*8 9.92*8 0 0", "+ 1527*8 10.19*8", "+ 1568*8 10.469*8", "+ 1609*8 10.719*8", "+ 1649*8 11*8",
				"+ 1690*8 11.28*8", "+ 1730*8 11.53*8", "+ 1773*8 11.82*8", "+ 1811*8 12.059*8", null,
				//랜서
				"FM-92 mk2 랜서", "", "", "", "", "", "", "",
				"28 1655*8 14.059*8 0 0", "+ 1700*8 14.45*8", "+ 1745*8 14.84*8", "+ 1787*8 15.19*8", "+ 1831*8 15.58*8",
				"+ 1876*8 15.95*8", "+ 1918*8 16.32*8", "+ 1964*8 16.71*8", "+ 2009*8 17.05*8", null,
				//양자
				"양자 폭탄", "", "", 40, "", "", "", Element_type.LIGHT,
				"26 838+10778 8.379+107.78 0 0", "+ 841+11091 8.41+110.909", "+ 842+11405 8.42+114.049", "+ 844+11715 8.44+117.149",
				"+ 845+12025 8.45+120.250", "+ 847+12335 8.469+123.350", "+ 848+12646 8.48+126.460", "+ 851+12956 8.51+129.560", null,
				//익스
				"X-1 익스트루더", "", "", 45, "", "", "", Element_type.NONE,
				"23 18229 182.29 0 0", "+ 18801 188.010", "+ 19374 193.739", "+ 19945 199.450", "+ 20517 205.170", "+ 21090 210.900",
				"+ 21662 216.620", "+ 22234 222.340", "+ 22807 228.069", "+ 23379 233.790", null,
				//엔션
				"에인션트 트리거", "", "", 50, 40, 30, 5, "", "설명 7초짜리 신의가호",
				"11 273*16+710+2841*11 2.73*16+7.1+28.41*11 0 0", "+ 292*16+757+3028*11 2.92*16+7.569+30.279*11", "+ 311*16+805+3218*11 3.11*16+8.049+32.180*11",
				"+ 330*16+852+3408*11 3.30*16+8.520+34.079*11", "+ 350*16+900+3597*11 3.50*16+9.000+35.970*11", "+ 369*16+947+3785*11 3.69*16+9.470+37.850*11", 
				"10 254*16+663+2654*11 2.54*16+6.63+26.54*11", null,
				//팜페로
				"팜페로 부스터", "", "", 60, "", "", 2, Element_type.WATER,
				"16 504*31 5.04*31 0 0", "+ 524*31 5.24*31", "+ 545*31 5.45*31", "+ 564*31 5.64*31", "+ 586*31 5.859*31", "+ 605*31 6.049*31", null,
				//특랜
				"FM-92 mk2 랜서 SW", "", "", 70, 40, 30, 2, Element_type.FIRE,
				"11 2832*8 28.32*8 0 0", "+ 2974*8 29.74*8", "+ 3116*8 31.16*8",
				"14 3259*8 32.59*8 0 0", "+ 3399*8 33.989*8", "+ 3544*8 35.440*8", "+ 3685*8 36.850*8", null,
				//공기포
				"PT-15 프로토타입", "", "", 80, 40, 30, 2, Element_type.NONE,
				"6 20942+8976 209.42+89.759 0 0", "+ 22352+9579 223.519+95.79",
				"+ 23762+10184 237.620+101.840", "+ 25172+10788 251.720+107.880",
				"+ 26580+11393 265.800+113.930", "+ 27990+11995 279.900+119.950", "+ 29400+12600 294.000+126.000", "+ 30811+13204 308.110+132.040", null,
				//오퍼
				"오퍼레이션 레이즈", "", "", 85, 40, 30, 5, Element_type.FIRE, "설명 진누골 기준 정타 타격",
				"4 1.794*37973 1.794*379.73 0 0", "3 1.794*32783 1.794*327.83", "5 1.794*43166 1.794*431.66", "6 1.794*48258 1.794*482.58",
				"7 1.794*53552 1.794*535.52", null,
				
				///////테스트 스킬
				/*"테스트 스킬_f", Skill_type.ACTIVE, Job.LAUNCHER_F, 85, 60, 50, 5, Element_type.NONE,
				"2 0 1 0 0", null,
				"테스트 스킬_%", Skill_type.ACTIVE, Job.LAUNCHER_F, 85, 60, 50, 5, Element_type.NONE,
				"2 100 0 0 0", null,
				"테스트 스킬_%화", Skill_type.ACTIVE, Job.LAUNCHER_F, 85, 60, 50, 5, Element_type.FIRE,
				"2 100 0 0 0", null,*/
				
				///////패시브
				//중화기다루기
				"중화기 다루기", Skill_type.PASSIVE, "", 15, 1, 1, 3,
				"1", "스킬 M-137 개틀링건 1 & 스킬 바베~큐 1 & 스킬 M-3 화염방사기 1 & 스킬 슈타이어 대전차포 1 & 스킬 레이저 라이플 1 & 스킬 화염 강타 1 & 스킬 FM-31 그레네이드 런처 1 & 스킬 FM-92 mk2 랜서 1 & 스킬 캐넌볼 1 & "
				+ "스킬 양자 폭탄 1 & 스킬 X-1 익스트루더 1 & 스킬 에인션트 트리거 1 & 스킬 팜페로 부스터 1 & 스킬 FM-92 mk2 랜서 SW 1 & 스킬 PT-15 프로토타입 1 & 스킬 오퍼레이션 레이즈 1", null,
				//중화기 마스터리
				"중화기 마스터리", Skill_type.PASSIVE, "", 15, 20, 10, 3,
				"10", "무기마스터리 10 핸드캐넌 & 증뎀버프 20", "+", "무기마스터리 11 핸드캐넌 & 증뎀버프 22", "+", "무기마스터리 12 핸드캐넌 & 증뎀버프 24", "+", "무기마스터리 13 핸드캐넌 & 증뎀버프 26", 
				"+", "무기마스터리 14 핸드캐넌 & 증뎀버프 28", "+", "무기마스터리 15 핸드캐넌 & 증뎀버프 30", "+", "무기마스터리 16 핸드캐넌 & 증뎀버프 32", "+", "무기마스터리 17 핸드캐넌 & 증뎀버프 34",
				"+", "무기마스터리 18 핸드캐넌 & 증뎀버프 36", "+", "무기마스터리 19 핸드캐넌 & 증뎀버프 38", "+", "무기마스터리 20 핸드캐넌 & 증뎀버프 40", null,
				//옵힛
				"오버 히트", "", "", 48, 40, 30, 3, 
				"14", "증뎀버프 33", "15", "증뎀버프 34", "+", "증뎀버프 +2", "+", "증뎀버프 +1", "반복 2",
				//알파서폿
				"알파 서포트", "", "", 75, 40, 30, 3,
				"5", "물리마스터리 22 & 독공마스터리 22 & 물리방무뻥 22", "6", "물리마스터리 24 & 독공마스터리 24 & 물리방무뻥 24", "+", "물리마스터리 +2 & 독공마스터리 +2 & 물리방무뻥 +2", "반복 1",
				//애자파츠
				"AJ 강화파츠", "", "", "", "", "", "",
				"6", "스킬 M-3 화염방사기 % 61.18 & 스킬 화염 강타 % 28.74 & 스킬 팜페로 부스터 % 60.29 & 스킬 M-137 개틀링건 % 69.35 & 스킬 바베~큐 % 36 & 스킬 슈타이어 대전차포 % 36 & 스킬 FM-31 그레네이드 런처 % 36 & "
				+ "스킬 FM-92 mk2 랜서 % 36 & 스킬 FM-92 mk2 랜서 SW % 36 & 스킬 양자 폭탄 % 36 & 스킬 에인션트 트리거 % 36 & 스킬 PT-15 프로토타입 % 36 & 스킬 오퍼레이션 레이즈 % 36 & "
				+ "스킬 레이저 라이플 % 36 & 스킬 X-1 익스트루더 % 36",
				"+", "스킬 M-3 화염방사기 % +2.16 & 스킬 화염 강타 % +1.66 & 스킬 팜페로 부스터 % +2.16 & 스킬 M-137 개틀링건 % +2.15 & 스킬 바베~큐 % +2 & 스킬 슈타이어 대전차포 % +2 & 스킬 FM-31 그레네이드 런처 % +2 & "
				+ "스킬 FM-92 mk2 랜서 % +2 & 스킬 FM-92 mk2 랜서 SW % +2 & 스킬 양자 폭탄 % +2 & 스킬 에인션트 트리거 % +2 & 스킬 PT-15 프로토타입 % +2 & 스킬 오퍼레이션 레이즈 % +2 & "
				+ "스킬 레이저 라이플 % +6 & 스킬 X-1 익스트루더 % +6", "반복 1",
				//충레라
				"충전 레이저 라이플", "", "", 25, 11, 1, 2,
				"1", "스킬 레이저 라이플 % 60", "+", "스킬 레이저 라이플 % +10", "+", "스킬 레이저 라이플 % +5", "반복 2", 
				//미비
				"미라클 비전", Skill_type.SWITCHING, "", 30, 20, 10, 3,
				"10", "증뎀버프 41", null,
				//듀얼트리거
				"듀얼 트리거", Skill_type.PASSIVE, "", 35, 1, 1, 3, "설명 명, 화속성 중 높은 속성 강화 값으로 낮은 값이 상승한다.",
				"1", fStat[0], null,
				
				////////TP
				"M-137 개틀링건 강화", "M-137 개틀링건", "", 50, 7, 5, 8, null,
				"바베~큐 강화", "바베~큐", "", 50, 3, 1, 20, null,
				"M-3 화염방사기 강화", "M-3 화염방사기", "", 50, 7, 5, 8, "설명 AJ강화파츠로 인해 실제 증가율은 조금 더 높습니다", null,
				"슈타이어 대전차포 강화", "슈타이어 대전차포", "", 55, 7, 5, 10, null,
				"캐넌볼 강화", "캐넌볼", "", 50, 7, 5, 10, null,
				"레이저 라이플 강화", "레이저 라이플", "", 50, 7, 5, 10, null,
				"화염 강타 강화", "화염 강타", "", 50, 7, 5, 10, null,
				"FM-92 mk2 랜서 강화", "FM-92 mk2 랜서", "", 65, 7, 5, 10, null,
				"양자 폭탄 강화", "양자 폭탄", "", 65, 7, 5, 10, null,
				"X-1 익스트루더 강화", "X-1 익스트루더", "", 65, 7, 5, 10, null,
				"FM-31 그레네이드 런처 강화", "FM-31 그레네이드 런처", "", 50, 1, 1, 20, null,
				
				//////////////////레인저
				/////액티브
				"은탄", Skill_type.ACTIVE, Character_type.GUNNER_F, 5, 60, 50, 2, Element_type.LIGHT, "설명 한 탄창의 딜량입니다",
				"43 697*25 0 0 0", "+ 711*25", "+ 725*25", "+ 739*25", "+ 754*25 ", "+ 768*25", "+ 782*25 ", "+ 782*25 ", "+ 796*25", null,
				"헤드샷", Skill_type.ACTIVE, Job.RANGER_F, 15, 60, 50, 2, Element_type.NONE, 
				"38 3323 0 0 0", "+ 3394 ", "+ 3464", "+ 3535", "+ 3606 ", "+ 3677", "+ 3748 ", null,
				"권총의 춤", Skill_type.ACTIVE, Job.RANGER_F, 35, 60, 50, 2, Element_type.NONE,
				"28 654*20+1163*8 0 0 0", "+ 672*20+1194*8 ", "+ 689*20+1226*8", "+ 707*20+1257*8", "+ 725*20+1289*8 ", "+ 743*20+1321*8", "+ 760*20+1352*8", "+ 778*20+1384*8", "+ 796*20+1415*8", null,
				"이동사격", Skill_type.ACTIVE, Job.RANGER_F, 35, 60, 50, 2, Element_type.NONE,
				"28 1238*30 0 0 0", "+ 1271*30 ", "+ 1305*30", "+ 1338*30", "+ 1372*30 ", "+ 1406*30", "+ 1439*30 ", "+ 1473*30 ", "+ 1506*30", null,
				"멀티 헤드샷", Skill_type.ACTIVE, Job.RANGER_F, 40, 60, 50, 2, Element_type.NONE,
				"26 4752*5 0 0 0", "+ 4888*5 ", "+ 5025*5", "+ 5161*5", "+ 5297*5 ", "+ 5434*5", "+ 5570*5 ", "+ 5706*5 ", "+ 5843*5",null,
				"더블 건호크", Skill_type.ACTIVE, Job.RANGER_F, 45, 60, 50, 2, Element_type.NONE,
				"23 1050*14+1066*18+1155*30 0 0 0", "+ 1083*14+1100*18+1191*30", "+ 1116*14+1133*18+1228*30","+ 1149*14+1167*18+1264*30","+ 1182*14+1200*18+1300*30","+ 1215*14+1234*18+1337*30",
				"+ 1248*14+1267*18+1373*30","+ 1281*14+1301*18+1409*30","+ 1314*14+1334*18+1445*30", null,
				"블러디 카니발", Skill_type.ACTIVE, Job.RANGER_F, 50, 40, 30, 5, Element_type.NONE, 
				"10 4479*24+13286 0 0 0", "+ 4815*24+14284", "+ 5152*24+15282", "+ 5488*24+16280", "+ 5825*24+17278 ", "+ 6161*24+18276", "+ 6497*24+19274", null,
				"블러디 스파이크", Skill_type.ACTIVE, Job.RANGER_F, 60, 60, 50, 2, Element_type.NONE, 
				"16 2761*5+13806 0 0 0", "+ 2872*5+14362 ", "+ 2983*5+14917 ","+ 3094*5+15473 ","+ 3205*5+16028 ","+ 3316*5+16584 ","+ 3427*5+17139 ","+ 3539*5+17695",null,
				"제압 사격", Skill_type.ACTIVE, Job.RANGER_F, 70, 40, 30, 2, Element_type.NONE,
				"11 1906*19+4237 0 0 0", "+ 2002*19+4450","+ 2098*19+4663","+ 2194*19+4877","+ 2290*19+5090","+ 2386*19+5304","+ 2482*19+5517",
				"+ 2578*19+5730","+ 2674*19+5944","+ 2770*19+6157", null,
				"블러드 앤 체인", Skill_type.ACTIVE, Job.RANGER_F, 85, 40, 30, 5, Element_type.NONE, 
				"2 92547 0 0 0", "+ 109968 ", "+ 127388","+ 144809", null,
				"소닉 스파이크", Skill_type.ACTIVE, Job.RANGER_F, 30, 60, 50, 2, Element_type.NONE, 
				"25 3185*3 0 0 0", "+ 3265*3 ", "+ 3345*3", "+ 3425*3", "+ 3505*3 ", "+ 3585*3", "+ 3665*3 ", "+ 3745*3 ", "+ 3825*3",null,
				
				/////패시브
				"베일드 컷", Skill_type.DAMAGE_BUF, Job.RANGER_F, 48, 60, 50, 3, Element_type.NONE, 
				"15 2240 0 0 0", "증뎀버프 33","+ 2330", "증뎀버프 35","+ 2419", "증뎀버프 37","+ 2509", "증뎀버프 39","+ 2598", "증뎀버프 41","+ 2688", "증뎀버프 43", null,
				"킬 포인트", Skill_type.DAMAGE_BUF, Job.RANGER_F, 75, 40, 30, 3, Element_type.NONE, "설명 만크리를 가정한 공격력입니다",
				"6 6105*3*1.14+18317 0 0 0", "크증버프 14 & 스킬 킬 포인트 % -14/1.14","+ 6647*3*1.16+19942", "크증버프 16 & 스킬 킬 포인트 % -16/1.16","+ 7198*3*1.18+21567", "크증버프 18 & 스킬 킬 포인트 % -18/1.18",
				"+ 7730*3*1.2+23192", "크증버프 20 & 스킬 킬 포인트 % -20/1.20","+ 8272*3*1.22+24817", "크증버프 22 & 스킬 킬 포인트 % -22/1.22","+ 8813*3*1.24+26441", "크증버프 24 & 스킬 킬 포인트 % -24/1.24", null,
				"쏘우 블레이드", Skill_type.BUF_ACTIVE, "", 75, 20, 10, 3,
				"6", "증뎀버프 24", "+", "증뎀버프 +2", "반복 1",
				"체인 글린트", Skill_type.BUF_ACTIVE, "", 80, 20, 10, 3,
				"4", "증뎀버프 30", "+", "증뎀버프 +3", "반복 1",
				"데스 바이 리볼버", Skill_type.SWITCHING, "", 30, 20, 10, 3, 
				"10", fStat[2], "크증버프 43", null,
				"트리플 클러치", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 3,
				"1", "스킬 탑스핀 % 10 & 스킬 라이징샷 % 10 & 스킬 니들 소배트 % 10 & 스킬 헤드샷 % 15", "+", "스킬 탑스핀 % +2 & 스킬 라이징샷 % +2 & 스킬 니들 소배트 % +2 & 스킬 헤드샷 % +3", "반복 1",
				"웨스턴 파이어", Skill_type.BUF_ACTIVE, "", 30, 1, 1, 3, "설명 편의상 헤드샷의 증뎀버프로 구현함",
				"1", "스킬 헤드샷 % 20", null,
				"체인 파우더", Skill_type.BUF_ACTIVE, "", 40, 20, 10, 2,
				"10", "스킬 권총의 춤 % 0.4156*48 & 스킬 더블 건호크 % 50*0.52 & 스킬 이동사격 % 29 & 스킬 제압 사격 % 20 & 스킬 멀티 헤드샷 % 20 & 스킬 헤드샷 % 20",
				"11", "스킬 권총의 춤 % 0.4156*50 & 스킬 더블 건호크 % 53*0.52 & 스킬 이동사격 % 30 & 스킬 제압 사격 % 21 & 스킬 멀티 헤드샷 % 21 & 스킬 헤드샷 % 21",
				"12", "스킬 권총의 춤 % 0.4156*52 & 스킬 더블 건호크 % 54*0.52 & 스킬 이동사격 % 32 & 스킬 제압 사격 % 22 & 스킬 멀티 헤드샷 % 22 & 스킬 헤드샷 % 22",
				"13", "스킬 권총의 춤 % 0.4156*56 & 스킬 더블 건호크 % 57*0.52 & 스킬 이동사격 % 34 & 스킬 제압 사격 % 23 & 스킬 멀티 헤드샷 % 23 & 스킬 헤드샷 % 23",
				"14", "스킬 권총의 춤 % 0.4156*58 & 스킬 더블 건호크 % 60*0.52 & 스킬 이동사격 % 35 & 스킬 제압 사격 % 24 & 스킬 멀티 헤드샷 % 24 & 스킬 헤드샷 % 24",
				"15", "스킬 권총의 춤 % 0.4156*62 & 스킬 더블 건호크 % 63*0.52 & 스킬 이동사격 % 37 & 스킬 제압 사격 % 25 & 스킬 멀티 헤드샷 % 25 & 스킬 헤드샷 % 25",
				"16", "스킬 권총의 춤 % 0.4156*64 & 스킬 더블 건호크 % 66*0.52 & 스킬 이동사격 % 39 & 스킬 제압 사격 % 26 & 스킬 멀티 헤드샷 % 26 & 스킬 헤드샷 % 26", null,
				
				//TP
				"은탄 강화", "은탄", Character_type.GUNNER_F, 50, 7, 5, -1,
				"1", "스킬 은탄 % 8.16", "+", "스킬 은탄 % 16.64", "+", "스킬 은탄 % 25.44", "+", "스킬 은탄 % 34.56", "+", "스킬 은탄 % 44",
				 "+", "스킬 은탄 % 53.76", "+", "스킬 은탄 % 63.84", null,
				"마릴린 로즈 강화", "마릴린 로즈", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"라이징샷 강화", "라이징샷", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"탑스핀 강화", "탑스핀", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				//"스프리건 강화", "스프리건", Character_type.GUNNER_F, 50, 3, 1, 8, null,
				"니들 소배트 강화", "니들 소배트", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"헤드샷 강화", "헤드샷", Job.RANGER_F, 60, 7, 5, 10, null,
				"리벤저 강화", "리벤저", Job.RANGER_F, 60, 7, 5, 10, null,
				"소닉 스파이크 강화", "소닉 스파이크", Job.RANGER_F, 50, 7, 5, 10, null,
				"이동사격 강화", "이동사격", Job.RANGER_F, 50, 7, 5, 10, null,
				"권총의 춤 강화", "권총의 춤", Job.RANGER_F, 65, 7, 5, 10, null,
				"멀티 헤드샷 강화", "멀티 헤드샷", Job.RANGER_F, 65, 7, 5, 10, null,
				"더블 건호크 강화", "더블 건호크", Job.RANGER_F, 65, 7, 5, 10, null,
				
				
				
				//////////////////남스핏
				/////액티브
				"교차 사격", Skill_type.ACTIVE, Job.SPITFIRE_M, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"33", "귀속/오버 차지/1311*3", "+", "귀속/오버 차지/1343*3", "+", "귀속/오버 차지/1374*3", "+", "귀속/오버 차지/1405*3", "+", "귀속/오버 차지/1437*3", "+", "귀속/오버 차지/1468*3", "+", "귀속/오버 차지/1499*3", null, 
				"버스터 샷", Skill_type.ACTIVE,  "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"28", "귀속/오버 차지/7418", "+", "귀속/오버 차지/7619", "+", "귀속/오버 차지/7820", "+", "귀속/오버 차지/8022", "+", "귀속/오버 차지/8223", "+", "귀속/오버 차지/8424", null,  
				"C4", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"28 23935 0 23935 0", "29 24584 24584", "30 25234 25234", "31 25883 25883", "32 26532 26532", "33 27182 27182", null,
				"네이팜 탄", Skill_type.ACTIVE, "", 40, 60, 50 , 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"26", "귀속/오버 차지/6595", "+", "귀속/오버 차지/6784", "+", "귀속/오버 차지/6973", "+", "귀속/오버 차지/7163", "+", "귀속/오버 차지/7352", null,
				"네이팜 탄(장판)", "원본 - 네이팜 탄", "", 40, 60, 50 , 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"26 1450 0 1450 0", "", "27 1500 1500", "", "28 1540 1540", "", "29 1580 1580", "", "30 1620 1620", "", null,
				"록 온 서포트", Skill_type.ACTIVE, "", 45, 60, 50, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"23 7294*5 0 7294*5 0", "24 7523*5 7523*5", "25 7752*5 7752*5", "26 7981*5 7981*5", "27 8210*5 8210*5", "28 8439*5 8439*5", "29 8668*5 8668*5", null,
				"특수기동전대 '블랙 로즈'", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"9 121735 0 121735 0", "10 131630 0 131630 0", "11 141524 141524", "12 151434 151434", "13 161328 161328", "14 171222 171222", "15 181116 181116", null,
				"G-61 중력류탄", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"16 394*30+11825 0 394*30+11825 0", "17 410*30+12301 410*30+12301", "18 425*30+12777 425*30+12777", "19 441*30+13253 441*30+13253", "20 457*30+13728 457*30+13728", "21 473*30+14204 473*30+14204", null,
				"피스톨 카빈", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"11", "귀속/오버 차지/665*17", "+", "귀속/오버 차지/699*17",  "+", "귀속/오버 차지/721*17",  "+", "귀속/오버 차지/766*17",  
				"+", "귀속/오버 차지/799*17",  "+", "귀속/오버 차지/833*17",  "+", "귀속/오버 차지/866*17",  "+", "귀속/오버 차지/900*17", null,
				"데인저 클로즈", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"8", "귀속/오버 차지/6681*10/7.08", "+", "귀속/오버 차지/7077*10/7.08", "+", "귀속/오버 차지/7474*10/7.08", "+", "귀속/오버 차지/7870*10/7.08",    
				"+", "귀속/오버 차지/8266*10/7.08", "+", "귀속/오버 차지/8663*10/7.08", "+", "귀속/오버 차지/9059*10/7.08", null,   
				"G-38ARG 반응류탄", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"6 4101*10+17579 0 4101*10+17579 0", "7 4377*10+18762 4377*10+18762", "8 4653*10+19945 4653*10+19945",
				"9 4930*10+21128 4930*10+21128", "10 5206*10+22311 5206*10+22311", "11 5482*10+23495 5482*10+23495", null, 
				"슈퍼 노바", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"2 69827+1994*15 0 69827+1994*15 0", "3 82971+2370*15 82971+2370*15", "4 96115+2746*15 96115+2746*15", "5 109259+3121*15 109259+3121*15", "6 122403+3497*15 122403+3497*15", null,
				"G-35L 섬광류탄", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_d,
				"33 2526 0 2526 0", "크증버프 15 & 크리저항감소 10", "34 2586 2586", "크증버프 16 & 크리저항감소 10", "35 2646 2646", "크증버프 17 & 크리저항감소 10", "36 2707 2707", "크증버프 18 & 크리저항감소 10",
				"37 2767 2767", "크증버프 19 & 크리저항감소 10", "38 2827 2827", "크증버프 20 & 크리저항감소 10", null, 
				"G-18C 빙결류탄", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"31 6010 0 6010 0", "32 6162 6162", "33 6311 6311", "34 6463 6463", "35 6613 6613", null,
				"M18 클레이모어", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"36 2439*3 0 2439 0", "37 2494*3 2494*3", "38 2548*3 2548*3", "39 2603*3 2603*3", "40 2657*3 2657*3", "41 2711*3 2711*3", null, 
				"평타", Skill_type.ACTIVE, "", 1, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_d, "설명 한발의 데미지입니다",
				"1 150 0 150 0", null,
				"폭발탄", Skill_type.ACTIVE, "", 30, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_d, "설명 한발의 데미지입니다",
				"10", "귀속/오버 차지/469", "+", "귀속/오버 차지/530", "+", "귀속/오버 차지/561", "+", "귀속/오버 차지/592", "+", "귀속/오버 차지/623", null,
				"평타(1사이클)", Skill_type.ACTIVE, "", 1, 25, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"1", "귀속/평타/200 & 귀속/폭발탄/100", "+", "귀속/평타/+100 & 귀속/폭발탄/+100", "반복 1",
				
				//////패시브
				"오버 차지", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d, "설명 수치 다 입력해주셔야합니다 죄송ㅎ",
				"10 427 0 427 0", "스킬 C4 % 34 & 스킬 네이팜 탄(장판) % 34 & 스킬 록 온 서포트 % 34 & 스킬 특수기동전대 '블랙 로즈' % 34 & 스킬 G-61 중력류탄 % 34 & 스킬 G-38ARG 반응류탄 % 34 & "
				+ "스킬 슈퍼 노바 % 34 & 스킬 G-35L 섬광류탄 % 34 & 스킬 G-18C 빙결류탄 % 34 & 스킬 M18 클레이모어 % 34 & 스킬 평타 % 34", null,
				"병기 숙련", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", fStat[5], "", "+", fStat[5], "", "반복 1",
				"강화탄", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d, "설명 강화탄을 찍지 않을 경우 물리/마법공격력이 모두 들어가는 참사가 발생합니다 빼지마세요",
				"10", fStat[4], "", "+", fStat[4], "", "반복 1",
				"강화탄(화)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(수)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(명)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(무)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(컨버전)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 컨버전을 킬 경우, 마공으로 전환됩니다",
				"1", "", null,
				"유탄 마스터리", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", "스킬 G-14 파열류탄 % 10 & 스킬 G-35L 섬광류탄 % 10 & 스킬 G-18C 빙결류탄 % 10", "+", "스킬 G-14 파열류탄 % +1 & 스킬 G-35L 섬광류탄 % +1 & 스킬 G-18C 빙결류탄 % +1", "반복 1",
				"듀얼 플리커", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_d,
				"15", "스증뎀 33", "+", "스증뎀 +1.5", "반복 1", 
				"전장의 영웅", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"4", "스증뎀 30", "+", "스증뎀 +2", "반복 1",
				"패스티스트 건", Skill_type.PASSIVE, "", 30, 20, 5, 3, CalculatorVersion.VER_1_0_d,
				"5", "스킬 평타 % 12", "+", "스킬 평타 % +0.5", "반복 1",
				"매거진 드럼", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", fStat[6], "스킬 평타 % 20", "+", fStat[6], "스킬 평타 % +2", "반복 1",
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_0_d,
				"90", fStat[7], "스킬 평타 % 512.2", "+", fStat[7], "스킬 평타 % +5.8", "반복 1",
				
				/////TP
				"류탄 강화", "G-14 파열류탄 & G-35L 섬광류탄 & G-18C 빙결류탄", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"교차 사격 강화", "교차 사격", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"버스터 샷 강화", "버스터 샷", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"M18 클레이모어 강화", "M18 클레이모어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"C4 강화", "C4", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"네이팜 탄 강화", "네이팜 탄", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"록 온 서포트 강화", "록 온 서포트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				
				///공용스킬
				"고대의 기억", Skill_type.BUF_ACTIVE, Character_type.ALL, 15, 20, 10, 3,
				"1", "지능 15", "+", "지능 +15", "반복 1",
				"물리 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"1", "물크 1", "+", "물크 +1", "반복 1",
				"마법 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"1", "마크 1", "+", "마크 +1", "반복 1",
				//개틀
				"M-137 개틀링건", Skill_type.ACTIVE, Character_type.GUNNER_F, 5, 60, 50, 2, Element_type.NONE,
				"42 223*20 1.859*20 0 0", "+ 228*20 1.900*20", "+ 232*20 1.940*20", "+ 237*20 1.970*20", "+ 241*20 2.010*20", "+ 246*20 2.06*20", "+ 251*20 2.09*20", null,
				//바베큐
				"바베~큐", "", "", 10, 60, 50, 2, "",
				"41 7200 72 0 0", "+ 7350 73.5", "+ 7480 74.8", "+ 7630 76.29", "+ 7790 77.9", null,
				//화방
				"M-3 화염방사기", "", "", 15, 60, 50, 2, Element_type.FIRE,
				"38 446*13 3.720*13 0 0", "+ 456*13 3.8*13", "+ 466*13 3.89*13", "+ 476*13 3.96*13", "+ 488*13 4.06*13", "+ 498*13 4.149*13", null,
				"기본기 숙련 강화", "기본기 숙련", Character_type.ALL, 50, 5, 3, 10, CalculatorVersion.VER_1_0_d, null,
			
		};
		
		return data;
	}
	
	public static Object[] skillInfo_mage()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//원소폭격
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
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
									default:
										statList.addSkill_damage(dSkill.getName(), 25+2*level);
									}
								}
							}
						}
				return statList;
			}
		};
		
		Object[] data = new Object[] {
				/////////////우럭
				/////액티브
				"크리스탈 어택", Skill_type.ACTIVE, Job.ELEMENTALBOMBER, 20, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"36 0 0 1705*7 0", "+ 1740", "+ 1788", "+ 1817", "+ 1854", null,
				"파이어 로드", "", "", 25, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_d,
				"33 0 0 1032*12 0", "+ 1059", "+ 1083", "+ 1107", "+ 1133", "+ 1157", null,
				"체인 라이트닝", "", "", 30, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_d,
				"31 0 0 4242*4 0", "+ 4350", "+ 4458", "+ 4563", "+ 4669", null,
				"암전", "", "", 30, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_d,
				"31 0 0 15251 0", "+ 15630", "+ 16017", "+ 16401", "+ 16783", null,
				"아이스 크리스탈 샤워", "", "", 35, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"28 0 0 1241*14 0", "+ 1276*14", "+ 1310*14", "+ 1342*14", "+ 1377*14", "+ 1411*14", null,
				"플레임 서클", "", "", 35, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_d,
				"28 0 0 1914*6+7654 0", "+ 1965*6+7863", "+ 2016*6+8071", "+ 2069*6+8280", "+ 2122*6+8489", "+ 2174*6+8697", null,
				"라이트닝 월", "", "", 40, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_d,
				"26 0 0 15025 0", "+ 15456", "+ 15893", "+ 16327", "+ 16757", null,
				"다크니스 맨틀", "", "", 40, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_d,
				"26 0 0 20620 0", "+ 21224", "+ 21813", "+ 22411", "+ 23004", null,
				"엘레멘탈 레인", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 모속적용이 기본으로 되어있습니다",
				"23 0 0 902*45+3701+12213 0", "+ 929*45+3817+12598", "+ 957*45+3934+12982", "+ 986*45+4051+13369", "+ 1014*45+4170+13756", "+ 1043*45+4286+14142", null,
				"엘레멘탈 버스터", "", "", 50, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 모속적용이 기본으로 되어있습니다",
				"9 0 0 3916*30 0", "+ 4225*30", "+ 4534*30", "+ 4841*30", null,
				"컨센트레이트", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"16 0 0 7297+17028 0", "+ 7592+17712", "+ 7886+18396", "+ 8179+19083", null,
				"엘레멘탈 스트라이크", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"11 0 0 1865+35424 0", "+ 1958+37210", "+ 2053+38991", "+ 2148+40776", null,
				"엘레멘탈 필드", Skill_type.DAMAGE_BUF, "", 75, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"8 0 0 35737 0", "화속깍 40 & 수속깍 40 & 명속깍 40 & 암속깍 40", "+ 37857", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", "+ 39978", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2",
				"+ 42098", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", "+ 44217", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", null,
				"컨버젼스 캐넌", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"6 0 0 56526 0", "+ 60330", "+ 64135", "+ 67940", null,
				"아마겟돈 스트라이크", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"2 0 0 4988+416*13+89798 0", "+ 5928+494*13+106701", "+ 6867+571*13+123604", "+ 7806+651*13+140507", "+ 8745+729*13+157412", "+ 9684+808*13+174313", null,
				"속성발동", "", "", 15, 60, 50, 5, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"16 0 0 1161 0", "+ 1221", "+ 1281", "+ 1341", null,
				"엘레멘탈 캐넌", "", "", 15, 11, 1, 1, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"1", "귀속/속성발동/500", "+", "귀속/속성발동/+10", "반복 1",
				
				/////패시브
				"원소 폭격", Skill_type.BUF_ACTIVE, "", 30, 5, 5, 5, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 효과 적용",
				"5", fStat[0], "스킬 체인 라이트닝 % 10", null, 
				"페이탈 엘레멘트", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_d,
				"15", "크증버프 24 & 마크 10.0", "+", "크증버프 +1.5 & 마크 +0.5", "반복 1",
				"검은 눈", "", "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_d, "설명 원소폭격 사용 시 스킬 데미지 증가",
				"6", "", "+", "", null,
				"엘레멘탈 포스", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 1, CalculatorVersion.VER_1_0_d,
				"1", "스킬 엘레멘탈 캐넌 % 50", "+", "스킬 엘레멘탈 캐넌 % +10", "반복 1",
				"마나 폭주", Skill_type.SWITCHING, "", 25, 30, 20, 2, CalculatorVersion.VER_1_0_d,
				"20", "스증뎀 42", null,
				"엘레멘탈 실드", Skill_type.BUF_ACTIVE, "", 25, 20, 10, 2, CalculatorVersion.VER_1_0_d,
				"10", "모속강 26", "+", "모속강 +2", "반복 1",
				
				//TP
				"속성 발동 강화", "속성 발동", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_d, null,
				"체인 라이트닝 강화", "체인 라이트닝", "", 55, 7, 5, 20, CalculatorVersion.VER_1_0_d, null,
				"크리스탈 어택 강화", "크리스탈 어택", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"암전 강화", "암전", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"아이스 크리스탈 샤워 강화", "아이스 크리스탈 샤워", "", 55, 1, 1, 0, CalculatorVersion.VER_1_0_d, "설명 찍으면 버그로 딜이 감소한답니다 세상에", null,
				"파이어 로드 강화", "파이어 로드", "", 55, 7, 5, 4, CalculatorVersion.VER_1_0_d, null,
				"플레임 서클 강화", "플레임 서클", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"다크니스 맨틀 강화", "다크니스 맨틀", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"라이트닝 월 강화", "라이트닝 월", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"엘레멘탈 레인 강화", "엘레멘탈 레인", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null
		};
		
		return data;
	}
}
