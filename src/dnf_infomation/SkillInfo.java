package dnf_infomation;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.FunctionStat;
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
		boolean isHoldingSkill;
		
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
			
			if((!isTPSkill && (type==Skill_type.DAMAGE_BUF || type==Skill_type.ACTIVE || type==Skill_type.ACTIVE_NOMARK || type==Skill_type.OPTION)) || isSubSkill){
				//속성
				temp = data[i++];
				if(temp instanceof Element_type) element = (Element_type) temp;
				else if(temp.equals(""));	//이전 값 유지
				else{
					if(type!=Skill_type.OPTION) throw new ParsingException(i-1, temp);
					else i--;
				}
			}
			
			if(data[i] instanceof Boolean)
				isHoldingSkill = (boolean) data[i++];
			else
				isHoldingSkill = false;
			
			if(data[i] instanceof String && ((String)data[i]).contains("ver_"))
				version = (String) data[i++];
			else
				version = CalculatorVersion.VER_1_0_a;
			
			if(jobDefined){
				if(isTPSkill) skill = new TPSkill(name, targets, job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], job, firstLevel, maxLevel, masterLevel, interval, element, isHoldingSkill, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.INPUT) skill = new SwitchingSkill(name, type, job, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, job, firstLevel, maxLevel, masterLevel, interval, element, isHoldingSkill, version);
			}
			else{
				if(isTPSkill) skill = new TPSkill(name, targets, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], charType, firstLevel, maxLevel, masterLevel, interval, element, isHoldingSkill, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.INPUT) skill = new SwitchingSkill(name, type, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, charType, firstLevel, maxLevel, masterLevel, interval, element, isHoldingSkill, version);
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
						//if(levelInfo.fStat.statList.size()!=0) compNum=3;
						
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
						
					if(skill.hasDamage() || skill.type==Skill_type.SWITCHING || skill.isOptionSkill()){
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
						
						/*if(temp instanceof FunctionStat){
							levelInfo.fStat.statList.add((FunctionStat) temp);
							temp = data[i++];
						}*/
						
						if(temp!=null){
							String[] statList = ((String)temp).split(" & ");
							for(String str : statList){
								if(str.isEmpty()) continue;
								if(str.contains("fStat_")){
									FunctionStat tempFuncion = FunctionInfo.fStat.get(((String)str).substring(6));
									if(tempFuncion==null){
										System.out.println("error at : "+data[i-1]);
										throw new ParsingException(i-1, str);
									}
									levelInfo.fStat.statList.add(((String)str).substring(6));
									continue;
								}
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
				System.out.println("error at : "+data[i-1]);
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
			System.out.println("error at : "+data[i-1]);
			System.out.println("앞1 : "+data[i-2]);
			System.out.println("앞2 : "+data[i-3]);
			System.out.println("뒤1 : "+data[i]);
			System.out.println("뒤2 : "+data[i+1]);
			throw new ParsingException(i-1, temp);
		}
	}
	
	public static Object[] skillInfo_swordman()
	{
		
		Object[] data = new Object[] {
				
				///////////////검마
				/////액티브
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
				"파계검 : 라그나로크", Skill_type.ACTIVE, Job.DEMONSLAYER, 85, 40, 30, 5, Element_type.NONE, true, 
				"2 3063*10+11336*3+42325 0 0 0", "+ 3640*10+13471*3+50293", "+ 4217*10+15604*3+58260", "+ 4794*10+17739*3+66228", "+ 5370*10+19874*3+74195", null,
				
				/////패시브
				"탐욕의 번제", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 3,
				"15", "증뎀버프 33", "+", "증뎀버프 +1.5", "반복 1",
				"마인의 검세", Skill_type.PASSIVE, "", 75, 40, 30, 3,
				"5", "스킬 폭류나선 % 32 & 스킬 혈화난무 % 48*0.596 & 스킬 사복검 - 조 % 32*0.6 & 스킬 검마격살 % 50*0.421+48*0.421 & 스킬 사복검 - 발 % 19 & 스킬 사복검 - 무 % 32 & 스킬 사형조수 % 32 & 스킬 암연검 : 기가블레이드 % 32 & "
						+ "스킬 포식자 갈로아 % 32 & 스킬 역천의 프놈 % 32 & 스킬 암연격 : 기가슬래쉬 % 32 & 스킬 비인외도 : 극 % 32 & 스킬 파계검 : 라그나로크 % 32 & 스킬 혈지군무 % 32",
				"6", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 50*0.421+50*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"7", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 62.5*0.421+52*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & " 
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"8", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 62.5*0.421+54*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"9", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 62.5*0.421+56*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"10", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 75*0.421+58*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"11", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 75*0.421+60*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"12", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 75*0.421+62*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"13", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % +1.2 & 스킬 검마격살 % 87.5*0.421+64*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2", null,
									
				"광폭화", Skill_type.SWITCHING, "", 20, 30, 20, 3,  CalculatorVersion.VER_1_0_f,
				"20", "증뎀버프 64 & 스킬 혈마인 % 43.2 & 스킬 혈지군무 % 43.2 & 스킬 검마격살 % 43.2 & 스킬 포식자 갈로아 % 43.2 & 스킬 역천의 프놈 % 43.2 & "
						+ "스킬 암연검 : 기가블레이드 % 43.2 & 스킬 암연격 : 기가슬래쉬 % 43.2 & 스킬 파계검 : 라그나로크 % 43.2", null,
				"사복검 - 강", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"10", "스킬 사복검 - 발 % 61.8 & 스킬 사복검 - 조 % 61.8 & 스킬 사복검 - 무 % 61.8 & 스킬 사형조수 % 61.8 & 스킬 폭류나선 % 61.8 & 스킬 혈화난무 % 61.8 & 스킬 비인외도 : 극 % 61.8 & 스킬 파계검 : 라그나로크 % 61.8", null,

				"혈지군무", Skill_type.ACTIVE, "", 40, 30, 20, 3, Element_type.NONE,
				"17 0 0 0 0", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100",
				"+", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100", "반복 1",
				"혈지군무 - 발", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 발 개체를 붙입니다",
				"17", "귀속/사복검 - 발/260 & fStat_혈지군무", "+", "귀속/사복검 - 발/+4 & fStat_혈지군무", "반복 1",
				"혈지군무 - 무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 무 개체를 붙입니다",
				"17", "귀속/사복검 - 무/236 & fStat_혈지군무", "+", "귀속/사복검 - 무/240 & fStat_혈지군무", "+", "귀속/사복검 - 무/244 & fStat_혈지군무", "+", "귀속/사복검 - 무/247 & fStat_혈지군무",
				"+", "귀속/사복검 - 무/251 & fStat_혈지군무", "+", "귀속/사복검 - 무/254 & fStat_혈지군무", "+", "귀속/사복검 - 무/258 & fStat_혈지군무", "+", "귀속/사복검 - 무/262 & fStat_혈지군무", "+", 
				"귀속/사복검 - 무/266 & fStat_혈지군무", "+", "귀속/사복검 - 무/269 & fStat_혈지군무", "반복 3",
				"혈지군무 - 사형조수", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사형조수 개체를 붙입니다",
				"17", "귀속/사형조수/234 & fStat_혈지군무", "+", "귀속/사형조수/238 & fStat_혈지군무", "+", "귀속/사형조수/242 & fStat_혈지군무", "+", "귀속/사형조수/245 & fStat_혈지군무",
				"+", "귀속/사형조수/249 & fStat_혈지군무", "+", "귀속/사형조수/252 & fStat_혈지군무", "+", "귀속/사형조수/256 & fStat_혈지군무", "+", "귀속/사형조수/259 & fStat_혈지군무",
				"+", "귀속/사형조수/263 & fStat_혈지군무", "+", "귀속/사형조수/266 & fStat_혈지군무", "반복 3",
				"혈지군무 - 폭류나선", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 폭류나선 개체를 붙입니다",
				"17", "귀속/폭류나선/130 & fStat_혈지군무", "+", "귀속/폭류나선/+2 & fStat_혈지군무", "반복 1",  
				"혈지군무 - 혈화난무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 혈화난무 개체를 붙입니다",
				"17", "귀속/혈화난무/65 & fStat_혈지군무", "+", "귀속/혈화난무/+1 & fStat_혈지군무", "반복 1",
				
				"사복검 강화", "사복검 - 발 & 사복검 - 조 & 사복검 - 무", "", 55, 7, 5, 11, CalculatorVersion.VER_1_0_e, null, 
				"폭류나선 강화", "폭류나선", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"사형조수 강화", "사형조수", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"혈마인 강화", "혈마인", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"혈화난무 강화", "혈화난무", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"검마격살 강화", "검마격살", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				
				/////////////버서커
				/////액티브
				"고어 크로스", Skill_type.ACTIVE, Job.BUSERKER, 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"38 3201 5.2695 0 0", "+ 3273 5.5375", "+ 3336 5.6397","+ 3406 5.6397","+ 3478 5.7494","+ 3550 5.8595", null,
				"붕산격", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"41 3924 23.56 0 0", "+ 4014 24.06", "+ 4095 24.5365", "+ 4173 25.03", null,
				"블러드 러스트", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"33 0 70.2093 0 0", "힘 413", "+ 71.8683", "힘 433", "+ 73.5286", "힘 453", "+ 75.239", "힘 464", "+ 76.8986", "힘 482",
				"+ 78.56", "힘 500", "+ 80.2587", "힘 520", "+ 81.9289", "힘 538", null,
				"레이징 퓨리", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"31 0 89.93886 0 0", "+ 91.3581", "+ 93.5871", "+ 95.7489", "+ 97.9900", "+ 100.2275", "+ 102.4677", null,
				"블러디 레이브", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"28 0 222.460 0 0", "+ 228.200", "+ 233.960", "+ 239.720", "+ 245.460", "+ 251.2400", null,
				"블러드 소드", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"26 0 144.459 0 0", "+ 148.609", "+ 152.710", "+ 156.849", "+ 160.996", null,
				"아웃레이지 브레이크", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"23 607 220.928 0 0", "+ 627 227.930", "+ 646 235.028", "+ 664 241.930", "+ 684 248.979", "+ 703 255.840", null,
				"익스트림 오버킬", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"10 0 774.1053 0 0", "+ 832.635", "+ 891.457", "+ 948.857", "+ 1000.46", "9 0 715.5757 0 0", null,
				"블러드 루인", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"16 0 185.018 0 0", "+ 192.474", "+ 199.889", "+ 207.308", "+ 214.808", "+ 222.2075", null,
				"버스트 퓨리", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"11 0 247.4 0 0", "+ 259.719", "+ 272.437", "+ 284.744", "+ 297.251", null,
				"블러드 붐", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"8 0 417.950 0 0", "+ 442.760", "+ 467.540", "+ 492.340", "+ 517.120", "+ 541.930", "+ 566.710", "+ 591.520", null,
				"레드 로제", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 디폴트 : 누골 기준 히트수, 2단계 충전",
				"6 0 0 0 0", "+ 0 0 0 0", "+ 0 0 0 0", "+ 0 0 0 0", "+ 0 0 0 0", null,
				"레드 로제 - 다단히트", "원본 - 레드 로제", "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"6 0 442.860 0 0", "+ 472.670", "+ 502.479", "+ 532.068", "+ 562.100", null,
				"레드 로제 - 대형몹", Skill_type.OPTION, "", 80, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 레드로제 풀히트 타격",
				"1", "스킬 레드 로제 - 다단히트 % 163.64", null,
				"레드 로제 - 폭발", "원본 - 레드 로제", "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"6 0 161.070 0 0", "+ 171.915", "+ 182.760", "+ 193.620", "+ 204.440", null,
				"레드 로제 - 3단계", Skill_type.OPTION, "", 80, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 레드로제 3단계 적용",
				"1", "스킬 레드 로제 - 폭발 % 30", null,
				"블러드 리븐", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"2 0 685.1095*1.1 0 0", "+ 807.2185*1.1", "+ 929.680*1.1", "+ 1052.00*1.1", "+ 1174.30*1.1", null,
			
				/////패시브
				"프렌지", Skill_type.PASSIVE, "", 15, 30, 20, 3, CalculatorVersion.VER_1_1_f,
				"20", "스킬 붕산격 % 22 & 스킬 블러드 러스트 % 22 & 스킬 레이징 퓨리 % 22 & 스킬 블러드 루인 % 22 & 스킬 버스트 퓨리 % 22",
				"+",  "스킬 붕산격 % +1 & 스킬 블러드 러스트 % +1 & 스킬 레이징 퓨리 % +1 & 스킬 블러드 루인 % +1 & 스킬 버스트 퓨리 % +1", "반복 1",
				"프렌지 - 캔슬", Skill_type.OPTION, "", 15, 1, 1, 3, CalculatorVersion.VER_1_1_g, "설명 프렌지의 광전사 이도류 캔슬 공격 증뎀 적용",
				"1", "스킬 블러드 러스트 % 20 & 스킬 블러드 루인 % 20 & 스킬 레이징 퓨리 % 20 & 스킬 버스트 퓨리 % 20 & 스킬 블러드 소드 % 20 & 스킬 붕산격 % 20", null,
				"혈십자", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_1_f,
				"10", "", "+", "", null,
				"혈십자 단계", Skill_type.INPUT, "", 15, 1, 1, 3, CalculatorVersion.VER_1_1_f, "설명 혈십자의 단계를 입력하세요",
				"1", "횟수(재료수) 3 & fStat_혈십자", null,
				"다이하드", Skill_type.BUF_ACTIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_1_f, "설명 TP 투자 시 힘 증가",
				"20", "fStat_다이하드", "+", "fStat_다이하드", "반복 1",
				"폭주", Skill_type.SWITCHING, "", 25, 30, 20, 3, CalculatorVersion.VER_1_1_f,
				"20", "힘 1231 & 증뎀버프 70", null,
				"폭주 - 피격", Skill_type.SWITCHING, "", 25, 30, 20, 3, CalculatorVersion.VER_1_1_f,
				"20", "힘 575", null,
				"갈증", Skill_type.PASSIVE, "", 35, 30, 20, 3, CalculatorVersion.VER_1_1_f,
				"19", "스킬 레이징 퓨리 % 16.5 & 스킬 버스트 퓨리 % 16.5 & 스킬 아웃레이지 브레이크 % 29.5 & 스킬 블러드 소드 % 41",
				"+", "스킬 레이징 퓨리 % +0.6 & 스킬 버스트 퓨리 % +0.6 & 스킬 아웃레이지 브레이크 % +1.4 & 스킬 블러드 소드 % +2",
				"+", "스킬 레이징 퓨리 % +0.6 & 스킬 버스트 퓨리 % +0.6 & 스킬 아웃레이지 브레이크 % +1.4 & 스킬 블러드 소드 % +2",
				"+", "스킬 레이징 퓨리 % +0.7 & 스킬 버스트 퓨리 % +0.7 & 스킬 아웃레이지 브레이크 % +1.3 & 스킬 블러드 소드 % +2", "반복 3",
				"선혈의 추억", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"15", "독공마스터리 33.9 & 물크 11.4", "+", "독공마스터리 +1.5 & 물크 +0.6", "반복 1",
				"블러드 인카넷", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"6", "스킬 블러드 러스트 % 30 & 스킬 블러드 소드 % 30 & 스킬 블러드 루인 % 30 & 스킬 블러디 레이브 % 30 & 스킬 레이징 퓨리 % 30 & "
				+ "스킬 아웃레이지 브레이크 % 30 & 스킬 익스트림 오버킬 % 30 & 스킬 레드 로제 % 30 & 스킬 블러드 붐 % 30 & 스킬 블러드 리븐 % 30",
				"+", "스킬 블러드 러스트 % +2 & 스킬 블러드 소드 % +2 & 스킬 블러드 루인 % +2 & 스킬 블러디 레이브 % +2 & 스킬 레이징 퓨리 % +2 & "
				+ "스킬 아웃레이지 브레이크 % +2 & 스킬 익스트림 오버킬 % +2 & 스킬 레드 로제 % +2 & 스킬 블러드 붐 % +2 & 스킬 블러드 리븐 % +2", "반복 1",
				
				/////TP
				"고어 크로스 강화", "고어 크로스", "", 50, 7, 5, 8, CalculatorVersion.VER_1_1_f, null,
				"붕산격 강화", "붕산격", "", 50, 7, 5, 8, CalculatorVersion.VER_1_1_f, null,
				"다이하드 강화", "다이하드", "", 50, 7, 5, 0, CalculatorVersion.VER_1_1_f, "설명 다이하드 발동 시 힘 증가", null,
				"블러드 러스트 강화", "블러드 러스트", "", 55, 7, 5, -1, CalculatorVersion.VER_1_1_f, "설명 블러드 러스트 힘 증가량 10*n% 증가",
				"1", "스킬 블러드 러스트 % 10 & fStat_러스트", "2","스킬 블러드 러스트 % 20 & fStat_러스트", "3","스킬 블러드 러스트 % 30 & fStat_러스트", 
				"4", "스킬 블러드 러스트 % 40 & fStat_러스트", "5", "스킬 블러드 러스트 % 50 & fStat_러스트", "6", "스킬 블러드 러스트 % 60 & fStat_러스트", "7", "스킬 블러드 러스트 % 70 & fStat_러스트", null,
				"레이징 퓨리 강화", "레이징 퓨리", "", 65, 7, 5, -1, CalculatorVersion.VER_1_1_f,
				"1", "스킬 레이징 퓨리 % 33.57", "+", "스킬 레이징 퓨리 % 45.71", "+", "스킬 레이징 퓨리 % 57.86", "+", "스킬 레이징 퓨리 % 70.00", 
				"+", "스킬 레이징 퓨리 % 82.14", "+", "스킬 레이징 퓨리 % 94.29", "+", "스킬 레이징 퓨리 % 105.43", null,
				"블러디 레이브 강화", "블러디 레이브", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"블러드 소드 강화", "블러드 소드", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"아웃레이지 브레이크 강화", "아웃레이지 브레이크", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				
		};
		return data;
	}
	
	public static Object[] skillInfo_gunner()
	{
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
				"아래공기포", Skill_type.OPTION, "", 80, 1, 1, 1, CalculatorVersion.VER_1_0_f, "설명 PT-15 프로토타입 아래 방향키를 적용합니다", "설명 설마 위공기포 쓰는사람은 없겠지",
				"1", "스킬 PT-15 프로토타입 % -9.32", null,
				//오퍼
				"오퍼레이션 레이즈", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.FIRE, "설명 진누골 기준 정타 타격",
				"4 1.794*37973 1.794*379.73 0 0", "3 1.794*32783 1.794*327.83", "5 1.794*43166 1.794*431.66", "6 1.794*48258 1.794*482.58",
				"7 1.794*53552 1.794*535.52", null,
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
				
				///////패시브
				//중화기다루기
				"중화기 다루기", Skill_type.PASSIVE, Job.LAUNCHER_F, 15, 1, 1, 3,
				"1", "스킬 M-137 개틀링건 1 & 스킬 바베~큐 1 & 스킬 M-3 화염방사기 1 & 스킬 슈타이어 대전차포 1 & 스킬 레이저 라이플 1 & 스킬 화염 강타 1 & 스킬 FM-31 그레네이드 런처 1 & 스킬 FM-92 mk2 랜서 1 & 스킬 캐넌볼 1 & "
				+ "스킬 양자 폭탄 1 & 스킬 X-1 익스트루더 1 & 스킬 에인션트 트리거 1 & 스킬 팜페로 부스터 1 & 스킬 FM-92 mk2 랜서 SW 1 & 스킬 PT-15 프로토타입 1 & 스킬 오퍼레이션 레이즈 1", null,
				//중화기 마스터리
				"중화기 마스터리", Skill_type.PASSIVE, "", 15, 20, 10, 3,
				"10", "물리무기마스터리 10 핸드캐넌 & 증뎀버프 20", "+", "물리무기마스터리 11 핸드캐넌 & 증뎀버프 22", "+", "물리무기마스터리 12 핸드캐넌 & 증뎀버프 24", "+", "물리무기마스터리 13 핸드캐넌 & 증뎀버프 26", 
				"+", "물리무기마스터리 14 핸드캐넌 & 증뎀버프 28", "+", "물리무기마스터리 15 핸드캐넌 & 증뎀버프 30", "+", "물리무기마스터리 16 핸드캐넌 & 증뎀버프 32", "+", "물리무기마스터리 17 핸드캐넌 & 증뎀버프 34",
				"+", "물리무기마스터리 18 핸드캐넌 & 증뎀버프 36", "+", "물리무기마스터리 19 핸드캐넌 & 증뎀버프 38", "+", "물리무기마스터리 20 핸드캐넌 & 증뎀버프 40", null,
				//옵힛
				"오버 히트", "", "", 48, 40, 30, 3, 
				"14", "증뎀버프 33", "15", "증뎀버프 34", "+", "증뎀버프 +2", "+", "증뎀버프 +1", "반복 2",
				//알파서폿
				"알파 서포트", "", "", 75, 40, 30, 3,
				"5", "물리마스터리 22 & 독공마스터리 22", "6", "물리마스터리 24 & 독공마스터리 24", "+", "물리마스터리 +2 & 독공마스터리 +2", "반복 1",
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
				"미라클 비전", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"10", "증뎀버프 76.9", null,
				//듀얼트리거
				"듀얼 트리거", Skill_type.PASSIVE, "", 35, 1, 1, 3, "설명 명, 화속성 중 높은 속성 강화 값으로 낮은 값이 상승한다.",
				"1", "fStat_듀얼트리거", null,
				
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
				"은탄", Skill_type.ACTIVE, Job.RANGER_F, 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 한 탄창의 딜량입니다",
				"43 697*25 0 0 0", "귀속/은탄(언악정)/100", "+ 711*25", "귀속/은탄(언악정)/100", "+ 725*25", "귀속/은탄(언악정)/100", "+ 739*25", "귀속/은탄(언악정)/100", "+ 754*25 ", "귀속/은탄(언악정)/100",
				"+ 768*25", "귀속/은탄(언악정)/100", "+ 782*25", "귀속/은탄(언악정)/100", "+ 796*25", "귀속/은탄(언악정)/100", "+ 810*25", "귀속/은탄(언악정)/100", "+ 824*25", "귀속/은탄(언악정)/100",
				"+ 838*25", "귀속/은탄(언악정)/100", "+ 853*25", "귀속/은탄(언악정)/100", null,
				"은탄(언악정)", Skill_type.OPTION, "", 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 언데드, 악마, 정령 타입 몬스터 공격 시 추가",
				"43 174*25 0 0 0", "", "+ 178*25", "", "+ 181*25", "", "+ 185*25", "", "+ 189*25", "", "+ 192*25", "", "+ 196*25", "", "+ 199*25", "", "+ 203*25", "", "+ 206*25", "", "+ 210*25", "", "+ 213*25", null,
				"헤드샷", Skill_type.ACTIVE, Job.RANGER_F, 15, 60, 50, 2, Element_type.NONE, 
				"38 3323 0 0 0", "+ 3394 ", "+ 3464", "+ 3535", "+ 3606 ", "+ 3677", "+ 3748 ", null,
				"웨스턴 파이어", Skill_type.ACTIVE, Job.RANGER_F, 30, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1 0 0 0 0", "귀속/헤드샷/120", null,
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
				"블러드 앤 체인", Skill_type.ACTIVE, Job.RANGER_F, 85, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_e,
				"2 92547 0 0 0", "+ 109968 ", "+ 127388","+ 144809", null,
				"소닉 스파이크", Skill_type.ACTIVE, Job.RANGER_F, 30, 60, 50, 2, Element_type.NONE, 
				"25 3185*3 0 0 0", "+ 3265*3 ", "+ 3345*3", "+ 3425*3", "+ 3505*3 ", "+ 3585*3", "+ 3665*3 ", "+ 3745*3 ", "+ 3825*3",null,
				
				/////패시브
				"베일드 컷", Skill_type.DAMAGE_BUF, Job.RANGER_F, 48, 60, 50, 3, Element_type.NONE, 
				"15 2240 0 0 0", "증뎀버프 33","+ 2330", "증뎀버프 35","+ 2419", "증뎀버프 37","+ 2509", "증뎀버프 39","+ 2598", "증뎀버프 41","+ 2688", "증뎀버프 43", null,
				"킬 포인트", Skill_type.DAMAGE_BUF, Job.RANGER_F, 75, 40, 30, 3, Element_type.NONE, true, CalculatorVersion.VER_1_0_g, "설명 만물크를 가정한 공격력입니다",
				"6 6105*3*1.14+18317 0 0 0", "크증버프 14 & 스킬 킬 포인트 % -14/1.14","+ 6647*3*1.16+19942", "크증버프 16 & 스킬 킬 포인트 % -16/1.16","+ 7198*3*1.18+21567", "크증버프 18 & 스킬 킬 포인트 % -18/1.18",
				"+ 7730*3*1.2+23192", "크증버프 20 & 스킬 킬 포인트 % -20/1.20","+ 8272*3*1.22+24817", "크증버프 22 & 스킬 킬 포인트 % -22/1.22","+ 8813*3*1.24+26441", "크증버프 24 & 스킬 킬 포인트 % -24/1.24", null,
				"쏘우 블레이드", Skill_type.BUF_ACTIVE, "", 75, 20, 10, 3,
				"6", "증뎀버프 24", "+", "증뎀버프 +2", "반복 1",
				"체인 글린트", Skill_type.BUF_ACTIVE, "", 80, 20, 10, 3,
				"4", "증뎀버프 30", "+", "증뎀버프 +3", "반복 1",
				"데스 바이 리볼버", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_1_b,
				"10","크증버프 105 & fStat_데바리", null,
				"트리플 클러치", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 3,
				"1", "스킬 탑스핀 % 10 & 스킬 라이징샷 % 10 & 스킬 니들 소배트 % 10 & 스킬 헤드샷 % 15", "+", "스킬 탑스핀 % +2 & 스킬 라이징샷 % +2 & 스킬 니들 소배트 % +2 & 스킬 헤드샷 % +3", "반복 1",
				"체인 파우더", Skill_type.BUF_ACTIVE, "", 40, 20, 10, 2,
				"10", "스킬 권총의 춤 % 0.4156*48 & 스킬 더블 건호크 % 50*0.52 & 스킬 이동사격 % 29 & 스킬 제압 사격 % 20 & 스킬 멀티 헤드샷 % 20 & 스킬 헤드샷 % 20",
				"11", "스킬 권총의 춤 % 0.4156*50 & 스킬 더블 건호크 % 53*0.52 & 스킬 이동사격 % 30 & 스킬 제압 사격 % 21 & 스킬 멀티 헤드샷 % 21 & 스킬 헤드샷 % 21",
				"12", "스킬 권총의 춤 % 0.4156*52 & 스킬 더블 건호크 % 54*0.52 & 스킬 이동사격 % 32 & 스킬 제압 사격 % 22 & 스킬 멀티 헤드샷 % 22 & 스킬 헤드샷 % 22",
				"13", "스킬 권총의 춤 % 0.4156*56 & 스킬 더블 건호크 % 57*0.52 & 스킬 이동사격 % 34 & 스킬 제압 사격 % 23 & 스킬 멀티 헤드샷 % 23 & 스킬 헤드샷 % 23",
				"14", "스킬 권총의 춤 % 0.4156*58 & 스킬 더블 건호크 % 60*0.52 & 스킬 이동사격 % 35 & 스킬 제압 사격 % 24 & 스킬 멀티 헤드샷 % 24 & 스킬 헤드샷 % 24",
				"15", "스킬 권총의 춤 % 0.4156*62 & 스킬 더블 건호크 % 63*0.52 & 스킬 이동사격 % 37 & 스킬 제압 사격 % 25 & 스킬 멀티 헤드샷 % 25 & 스킬 헤드샷 % 25",
				"16", "스킬 권총의 춤 % 0.4156*64 & 스킬 더블 건호크 % 66*0.52 & 스킬 이동사격 % 39 & 스킬 제압 사격 % 26 & 스킬 멀티 헤드샷 % 26 & 스킬 헤드샷 % 26", null,
				
				//TP
				"은탄 강화", "은탄", Job.RANGER_F, 50, 7, 5, -1,
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
				
				///////////////////////스핏
				///////액티브
				"교차 사격", Skill_type.ACTIVE, Job.SPITFIRE_F, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33", "귀속/오버 차지/1311*3", "+", "귀속/오버 차지/1343*3", "+", "귀속/오버 차지/1374*3", "+", "귀속/오버 차지/1405*3", "+", "귀속/오버 차지/1437*3", "+", "귀속/오버 차지/1468*3", "+", "귀속/오버 차지/1499*3", null, 
				"버스터 샷", Skill_type.ACTIVE,  "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28", "귀속/오버 차지/7418", "+", "귀속/오버 차지/7619", "+", "귀속/오버 차지/7820", "+", "귀속/오버 차지/8022", "+", "귀속/오버 차지/8223", "+", "귀속/오버 차지/8424", null,  
				"C4", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 0 239.35 0 239.35", "29 245.84 245.84", "30 252.34 252.34", "31 258.83 258.83", "32 265.32 265.32", "33 271.82 271.82", null,
				"네이팜 탄", Skill_type.ACTIVE, "", 40, 60, 50 , 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26", "귀속/오버 차지/6595", "+", "귀속/오버 차지/6784", "+", "귀속/오버 차지/6973", "+", "귀속/오버 차지/7163", "+", "귀속/오버 차지/7352", null,
				"네이팜 탄(장판)", "원본 - 네이팜 탄", "", 40, 60, 50 , 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26 0 14.50 0 14.50", "27 15.00 15.00", "28 15.40 15.40", "29 15.80 15.80", "30 16.20 16.20", null,
				"록 온 서포트", Skill_type.ACTIVE, "", 45, 60, 50, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"23 0 72.94*5 0 72.94*5", "24 75.23*5 75.23*5", "25 77.52*5 77.52*5", "26 79.81*5 79.81*5", "27 82.10*5 82.10*5", "28 84.39*5 84.39*5", "29 86.68*5 86.68*5", null,
				"EMP 스톰", Skill_type.DAMAGE_BUF, "", 50, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"9 0 999.836/0.96 0 999.836/0.96", "%물방깍_스킬 15 & %마방깍_스킬 15", "10 0 (270.316*3+13.507*20)/0.96 0 (270.316*3+13.507*20)/0.96", "%물방깍_스킬 15 & %마방깍_스킬 15",
				"11 (290.620*3+14.524*20)/0.96 (290.620*3+14.524*20)/0.96", "%물방깍_스킬 15 & %마방깍_스킬 15", "12 (310.934*3+15.542*20)/0.96 (310.934*3+15.542*20)/0.96", "%물방깍_스킬 15 & %마방깍_스킬 15", null,
				"G-61 중력류탄", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"16 0 3.94*30+118.25 0 3.94*30+118.25", "17 4.10*30+123.01 4.10*30+123.01", "18 4.25*30+127.77 4.25*30+127.77",
				"19 4.41*30+132.53 4.41*30+132.53", "20 4.57*30+137.28 4.57*30+137.28", "21 4.73*30+142.04 4.73*30+142.04", null,
				"피스톨 카빈", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"11", "귀속/오버 차지/665*17", "+", "귀속/오버 차지/699*17",  "+", "귀속/오버 차지/732*17",  null,
				"G-96 열압력유탄", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"5 0 542.5792 0 542.5792", "+ 581.7292 581.7292", "+ 620.8896 620.8896", "+ 660.0396 660.0396", null,
				"디-데이", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"2 0 31.4594*40 0 31.4594*40", "3 37.3792*40 37.3792*40", "4 43.3*40 43.3*40", "5 49.22*40 49.22*40", null,
				"G-35L 섬광류탄", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_g,
				"33 0 25.26 0 25.26", "크증버프 15 & 물크 10 & 마크 10", "34 25.86 25.86", "크증버프 16 & 물크 10 & 마크 10", "35 26.46 26.46", "크증버프 17 & 물크 10 & 마크 10", "36 27.07 27.07", "크증버프 18 & 물크 10 & 마크 10",
				"37 27.67 27.67", "크증버프 19 & 물크 10 & 마크 10", "38 28.27 28.27", "크증버프 20 & 물크 10 & 마크 10", null, 
				"G-18C 빙결류탄", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"31 0 52.2593 0 52.2593 ", "32 53.5792 53.5792", "33 54.8792 54.8792", "34 56.1990 56.1990", "35 60.375 60.3750", null,
				"M18 클레이모어", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"36 0 24.39*3 0 24.39", "37 24.94*3 24.94*3", "38 25.48*3 25.48*3", "39 26.03*3 26.03*3", "40 26.57*3 26.57*3", "41 27.11*3 27.11*3", null, 
				"평타", Skill_type.ACTIVE, "", 1, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 한발의 데미지입니다",
				"1 150 0 0 0", null,
				"폭발탄", Skill_type.ACTIVE_NOMARK, "", 30, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_1_d, "설명 한발의 데미지입니다",
				"10", "귀속/오버 차지/469", "+", "귀속/오버 차지/500", "+", "귀속/오버 차지/530", "+", "귀속/오버 차지/561", "+", "귀속/오버 차지/592", "+", "귀속/오버 차지/623", null,
				"평타(1사이클)", Skill_type.ACTIVE, "", 1, 25, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1", "귀속/평타/200 & 귀속/폭발탄/100", "+", "귀속/평타/+100 & 귀속/폭발탄/+100", "반복 1",
				
				//////패시브
				"오버 차지", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"설명 ※※※※※중요※※※※※\n물리/마법 고정데미지의 숫자는\n(스킬창의 수치)/(현재 자신의 독공)\n을 입력해주세요.\nex)10레벨의 기본수치는 4.27,\n 20레벨의 수치는 7.08입니다", "설명 스증 수치는 다 입력해주셔야합니다 죄송ㅎ",
				"10 0 7.78 0 7.78", "스킬 C4 % 64 & 스킬 네이팜 탄(장판) % 64 & 스킬 록 온 서포트 % 64 & 스킬 EMP 스톰 % 64 & 스킬 G-61 중력류탄 % 64 & 스킬 G-96 열압력유탄 % 64 & "
				+ "스킬 디-데이 % 64 & 스킬 G-35L 섬광류탄 % 64 & 스킬 G-18C 빙결류탄 % 64 & 스킬 M18 클레이모어 % 64 & 스킬 평타 % 64", null,
				"병기 숙련", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "fStat_병기숙련", "+", "fStat_병기숙련", "반복 1",
				"강화탄", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e, "설명 강화탄을 찍지 않을 경우 물리/마법공격력이 모두 들어가는 참사가 발생합니다 빼지마세요",
				"10", "fStat_강화탄", "+", "fStat_강화탄", "반복 1",
				"강화탄(화)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(수)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(명)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(무)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(컨버전)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 컨버전을 킬 경우, 마공으로 전환됩니다",
				"1", "", null,
				"유탄 마스터리", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 G-14 파열류탄 % 100 & 스킬 G-35L 섬광류탄 % 100 & 스킬 G-18C 빙결류탄 % 010", "+", "스킬 G-14 파열류탄 % +10 & 스킬 G-35L 섬광류탄 % +10 & 스킬 G-18C 빙결류탄 % +10", "반복 1",
				"병기 강화", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"15", "증뎀버프 33", "+", "증뎀버프 +2", "+", "증뎀버프 +1", "반복 2", 
				"증명의 열쇠", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"4", "증뎀버프 26", "+", "증뎀버프 +2", "반복 1",
				"패스티스트 건", Skill_type.PASSIVE, "", 30, 20, 5, 3, CalculatorVersion.VER_1_0_e,
				"5", "스킬 평타 % 12", "+", "스킬 평타 % +0.5", "반복 1",
				"공중사격", Skill_type.BUF_ACTIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10","스킬 평타 % 20 & fStat_매거진", "+", "스킬 평타 % +2 & fStat_매거진", "반복 1",
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_0_f,
				"90", "스킬 평타 % 512.2 & fStat_기숙", "+", "스킬 평타 % 518 & fStat_기숙", "+", "스킬 평타 % 523.7 & fStat_기숙", "+", "스킬 평타 % 529.5 & fStat_기숙",
				"+", "스킬 평타 % 535.2 & fStat_기숙", "+", "스킬 평타 % 541 & fStat_기숙", "+", "스킬 평타 % 546.7 & fStat_기숙", "+", "스킬 평타 % 552.5 & fStat_기숙",
				"+", "스킬 평타 % 558.2 & fStat_기숙", "+", "스킬 평타 % 564 & fStat_기숙", "+", "스킬 평타 % 518 & fStat_기숙", "+", "스킬 평타 % 569.7 & fStat_기숙", null,
				
				/////TP
				"류탄 강화", "G-14 파열류탄 & G-35L 섬광류탄 & G-18C 빙결류탄", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"교차 사격 강화", "교차 사격", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"버스터 샷 강화", "버스터 샷", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"M18 클레이모어 강화", "M18 클레이모어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"C4 강화", "C4", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"네이팜 탄 강화", "네이팜 탄", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"록 온 서포트 강화", "록 온 서포트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				
				
				
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
				"26 1450 0 1450 0", "27 1500 1500", "28 1540 1540", "29 1580 1580", "30 1620 1620", null,
				"록 온 서포트", Skill_type.ACTIVE, "", 45, 60, 50, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"23 7294*5 0 7294*5 0", "24 7523*5 7523*5", "25 7752*5 7752*5", "26 7981*5 7981*5", "27 8210*5 8210*5", "28 8439*5 8439*5", "29 8668*5 8668*5", null,
				"특수기동전대 '블랙 로즈'", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"9 121735 0 121735 0", "10 131630 0 131630 0", "11 141524 141524", "12 151434 151434", "13 161328 161328", "14 171222 171222", "15 181116 181116", null,
				"G-61 중력류탄", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"16 394*30+11825 0 394*30+11825 0", "17 410*30+12301 410*30+12301", "18 425*30+12777 425*30+12777", "19 441*30+13253 441*30+13253", "20 457*30+13728 457*30+13728", "21 473*30+14204 473*30+14204", null,
				"피스톨 카빈", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"11", "귀속/오버 차지/665*17", "+", "귀속/오버 차지/699*17",  "+", "귀속/오버 차지/721*17",  "+", "귀속/오버 차지/766*17",  
				"+", "귀속/오버 차지/799*17",  "+", "귀속/오버 차지/833*17",  "+", "귀속/오버 차지/866*17",  "+", "귀속/오버 차지/900*17", null,
				"데인저 클로즈", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_b,
				"8 66810 0 66810 0", "+ 70770 70770", "+ 74740 74740", "+ 78700 78700", "+ 82660 82660", "+ 86630 86630", "+ 90590 90590", null,   
				"G-38ARG 반응류탄", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"6 4101*10+17579 0 4101*10+17579 0", "7 4377*10+18762 4377*10+18762", "8 4653*10+19945 4653*10+19945",
				"9 4930*10+21128 4930*10+21128", "10 5206*10+22311 5206*10+22311", "11 5482*10+23495 5482*10+23495", null, 
				"슈퍼 노바", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"2 69827+1994*15 0 69827+1994*15 0", "3 82971+2370*15 82971+2370*15", "4 96115+2746*15 96115+2746*15", "5 109259+3121*15 109259+3121*15", "6 122403+3497*15 122403+3497*15", null,
				"G-35L 섬광류탄", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_g,
				"33 2526 0 2526 0", "크증버프 15 & 물크 10 & 마크 10", "34 2586 2586", "크증버프 16 & 물크 10 & 마크 10", "35 2646 2646", "크증버프 17 & 물크 10 & 마크 10", "36 2707 2707", "크증버프 18 & 물크 10 & 마크 10",
				"37 2767 2767", "크증버프 19 & 물크 10 & 마크 10", "38 2827 2827", "크증버프 20 & 물크 10 & 마크 10", null, 
				"G-18C 빙결류탄", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"31 6010 0 6010 0", "32 6162 6162", "33 6311 6311", "34 6463 6463", "35 6613 6613", null,
				"M18 클레이모어", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"36 2439*3 0 2439 0", "37 2494*3 2494*3", "38 2548*3 2548*3", "39 2603*3 2603*3", "40 2657*3 2657*3", "41 2711*3 2711*3", null, 
				"평타", Skill_type.ACTIVE, "", 1, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 한발의 데미지입니다",
				"1 150 0 0 0", null,
				"폭발탄", Skill_type.ACTIVE_NOMARK, "", 30, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_1_d, "설명 한발의 데미지입니다",
				"10", "귀속/오버 차지/469", "+", "귀속/오버 차지/500", "+", "귀속/오버 차지/530", "+", "귀속/오버 차지/561", "+", "귀속/오버 차지/592", "+", "귀속/오버 차지/623", null,
				"평타(1사이클)", Skill_type.ACTIVE, "", 1, 25, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"1", "귀속/평타/200 & 귀속/폭발탄/100", "+", "귀속/평타/+100 & 귀속/폭발탄/+100", "반복 1",
				
				//////패시브
				"오버 차지", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_1_c, "설명 수치 다 입력해주셔야합니다 죄송ㅎ",
				"10 778 0 778 0", "스킬 C4 % 64 & 스킬 네이팜 탄(장판) % 64 & 스킬 록 온 서포트 % 64 & 스킬 특수기동전대 '블랙 로즈' % 64 & 스킬 G-61 중력류탄 % 64 & 스킬 G-38ARG 반응류탄 % 64 & "
				+ "스킬 슈퍼 노바 % 64 & 스킬 G-35L 섬광류탄 % 64 & 스킬 G-18C 빙결류탄 % 64 & 스킬 M18 클레이모어 % 64 & 스킬 데인저 클로즈 % 64 & 스킬 평타 % 64", null,
				"병기 숙련", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", "fStat_병기숙련", "+", "fStat_병기숙련", "반복 1",
				"강화탄", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d, "설명 강화탄을 찍지 않을 경우 물리/마법공격력이 모두 들어가는 참사가 발생합니다 빼지마세요",
				"10", "fStat_강화탄", "+", "fStat_강화탄", "반복 1",
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
				"유탄 마스터리", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 G-14 파열류탄 % 100 & 스킬 G-35L 섬광류탄 % 100 & 스킬 G-18C 빙결류탄 % 100", "+", "스킬 G-14 파열류탄 % +10 & 스킬 G-35L 섬광류탄 % +10 & 스킬 G-18C 빙결류탄 % +10", "반복 1",
				"듀얼 플리커", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"15", "증뎀버프 33", "+", "증뎀버프 +1.5", "반복 1", 
				"전장의 영웅", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"4", "증뎀버프 26", "+", "증뎀버프 +2", "반복 1",
				"패스티스트 건", Skill_type.PASSIVE, "", 30, 20, 5, 3, CalculatorVersion.VER_1_0_d,
				"5", "스킬 평타 % 12", "+", "스킬 평타 % +0.5", "반복 1",
				"매거진 드럼", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", "스킬 평타 % 20 & fStat_매거진", "+", "스킬 평타 % +2 & fStat_매거진", "반복 1",
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_0_d,
				"90", "스킬 평타 % 512.2 & fStat_기숙", "+", "스킬 평타 % +5.8 & fStat_기숙", "반복 1",
				
				/////TP
				"류탄 강화", "G-14 파열류탄 & G-35L 섬광류탄 & G-18C 빙결류탄", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"교차 사격 강화", "교차 사격", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"버스터 샷 강화", "버스터 샷", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"M18 클레이모어 강화", "M18 클레이모어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"C4 강화", "C4", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"네이팜 탄 강화", "네이팜 탄", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"록 온 서포트 강화", "록 온 서포트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				
				
				////////////////////남런처
				//////액티브
				"M-137 개틀링건", Skill_type.ACTIVE, Character_type.GUNNER_M, 5, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e, 
				"44 213*20 1.78*20 0 0", "+ 218*20 1.819*20", "+ 222*20 1.849*20", "+ 226*20 1.88*20", "+ 230*20 1.913*20", null,
				"M-3 화염방사기", "", "", 15, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"39 575*10 4.779*10 0 0", "+ 588*10 4.899*10", "+ 602*10 4.99*10", "+ 615*10 5.12*10", "+ 628*10 5.228*10", null,
				"바베~큐", "", "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"42 7440 74.4 0 0", "+ 7570 81.75", "+ 7720 83.37", "+ 7880 85.10", null,
				"슈타이어 대전차포", Skill_type.ACTIVE, Job.LAUNCHER_M, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"37 976+3900 8.149+32.59 0 0", "+ 997+3987 8.309+33.26", "+ 1018+4072 8.48+33.93", "+ 1039+4158 8.67+34.7", 
				"+ 1061+4246 8.84+35.36", "+ 1083+4331 45.094", null,
				"레이저 라이플", "", "", 25, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"34 3912 32.59 0 0", "+ 4001 33.34", "+ 4092 34.11", "+ 4187 34.89", "+ 4278 35.65", "+ 4369 36.407", null,
				"화염 강타", "", "", 30, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"32 398*26 3.46*26 0 0", "+ 407*26 3.54*26", "+ 416*26 3.62*26", "+ 427*26 3.71*26", "+ 437*26 3.8*26", 
				"+ 446*26 3.879*26", "+ 457*26 3.786", null,
				"FM-31 그레네이드 런처", "", "", 35, 60, 50, 2, "", CalculatorVersion.VER_1_0_e,
				"29 2612*4 17.4*4 0 0", "+ 2681*4 17.89*4", "+ 2752*4 18.34*4", "+ 2821*4 18.82*4", "+ 2891*4 19.26*4", "+ 2962*4 19.72*4", null,
				"FM-92 스팅어", "", "", "", "", "", "", "", CalculatorVersion.VER_1_0_e,
				"29 2170*5 25.189*5 0 0", "+ 2227*5 25.75*5", "+ 2282*5 26.34*5", "+ 2340*5 26.91*5", "+ 2394*5 27.49*5", "+ 2451*5 28.07*5", null,
				"양자 폭탄", "", "", 40, "", "", "", Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"27 3762+11281 37.62+112.81 0 0", "+ 3866+11597 38.66+115.970", "+ 3973+11911 39.73+119.11", "+ 4076+12225 40.76+122.25",
				"+ 4180+12540 41.80+125.4", "+ 4285+12856 42.85+128.56", null,
				"X-1 익스트루더", "", "", 45, "", "", "", Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"24 20915 209.15 0 0", "+ 21555 215.550", "+ 22193 221.930", "+ 22834 228.340", "+ 23472 234.720", "+ 24113 241.130", "+ 24755 247.550", null,
				"새틀라이트 빔", Skill_type.DAMAGE_BUF, "", 50, 40, 30, 5, Element_type.LIGHT,  CalculatorVersion.VER_1_0_e,
				"11 7393+3192*42 0 0 0", "명속깍 26", "+ 7912+3387*43", "명속깍 +1", "+ 8433+3577*46", "명속깍 +1", "+ 8948+3763*46", 
				"명속깍 +1", "+ 9463+3951*47", "명속깍 +1", "+ 9982+4134*48", "명속깍 +1", "10 6874+2997*41 0 0 0", "명속깍 25", null,
				"팜페로 부스터", Skill_type.ACTIVE, "", 60, "", "", 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"17 1371*12 13.71*12 0 0", "+ 1436*12 14.36*12", "+ 1502*12 15.019*12", "+ 1567*12 15.67*12", "+ 1635*12 16.349*12", null,
				"FM-92 스팅어 SW", "", "", 70, 40, 30, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"12 5802*4 58.02*4 0 0", "+ 6081*4 60.809*4", "+ 6360*4 63.60*4", "+ 6638*4 66.38*4 0 0", "+ 6915*4 69.150*4", null,
				"사이즈믹 웨이브", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"8 2210 88.369+22.10+44.18*6+233.549 0 0", "+ 2340 93.332+23.34+46.67*6+246.640", "+ 2456 98.270+24.56+49.14*6+259.720", "+ 2580 103.229+25.80+51.60*6+272.800",
				"+ 2705 108.170+27.05+54.09*6+285.880", "+ 2829 113.119+28.289+56.55*6+298.96", null,
				"버스터 빔", "", "", 85, 40, 30, 5, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"2 20880+31293 208.80+312.93 0 0", "+ 24780+37184 247.79+371.84", "+ 28730+43074 287.30+430.74", "+ 32640+48966 326.40+489.66",
				"+ 36570+54854 365.70+548.54", "+ 40500+60746 405.00+607.459",null,

				///////패시브
				"중화기 다루기", Skill_type.PASSIVE, "", 15, 1, 1, 3, CalculatorVersion.VER_1_0_e,
				"1", "스킬 M-137 개틀링건 1 & 스킬 바베~큐 1 & 스킬 M-3 화염방사기 1 & 스킬 슈타이어 대전차포 1 & 스킬 레이저 라이플 1 & 스킬 화염 강타 1 & 스킬 FM-31 그레네이드 런처 1 & 스킬 FM-92 스팅어 1 & 스킬 캐넌볼 1 & "
				+ "스킬 양자 폭탄 1 & 스킬 X-1 익스트루더 1 & 스킬 새틀라이트 빔 1 & 스킬 팜페로 부스터 1 & 스킬 FM-92 스팅어 SW 1 & 스킬 사이즈믹 웨이브 1 & 스킬 버스터 빔 1", null,
				"중화기 마스터리", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "물리무기마스터리 10 핸드캐넌 & 증뎀버프 20", "+", "물리무기마스터리 11 핸드캐넌 & 증뎀버프 22", "+", "물리무기마스터리 12 핸드캐넌 & 증뎀버프 24", "+", "물리무기마스터리 13 핸드캐넌 & 증뎀버프 26", 
				"+", "물리무기마스터리 14 핸드캐넌 & 증뎀버프 28", "+", "물리무기마스터리 15 핸드캐넌 & 증뎀버프 30", "+", "물리무기마스터리 16 핸드캐넌 & 증뎀버프 32", "+", "물리무기마스터리 17 핸드캐넌 & 증뎀버프 34",
				"+", "물리무기마스터리 18 핸드캐넌 & 증뎀버프 36", "+", "물리무기마스터리 19 핸드캐넌 & 증뎀버프 38", "+", "물리무기마스터리 20 핸드캐넌 & 증뎀버프 40", null,
				"스펙트럴 서치 아이", "", "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"14", "크리저항감소 21.6", "+", "크리저항감소 +1.6", "반복 1",
				"근력 강화", "", "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"5", "스증 24", "+", "스증 +4", "반복 1",
				"중화기 개조", "", "", 80, "", "", "", CalculatorVersion.VER_1_0_e,
				"4", "스킬 레이저 라이플 % 31 & 스킬 화염 강타 % 42 & 스킬 슈타이어 대전차포 % 34 & 스킬 그레네이드 런처 % 52 & 스킬 M-3 화염방사기 % 39 & 스킬 양자 폭탄 % 33 & 스킬 X-1 익스트루더 % 33 & "
				+ "스킬 새틀라이트 빔 % 33 & 스킬 FM-92 스팅어 % 33 & 스킬 팜페로 부스터 % 33 & 스킬 FM-92 스팅어 SW % 33 & 스킬 버스터 빔 % 33 & 스킬 바베~큐 % 33 & "
				+ "스킬 캐넌볼 % 33 & 스킬 사이즈믹 웨이브 % 33 & 스킬 M-137 개틀링건 % 58",
				"+", "스킬 레이저 라이플 % +2 & 스킬 화염 강타 % +2 & 스킬 슈타이어 대전차포 % +4 & 스킬 그레네이드 런처 % +3 & 스킬 M-3 화염방사기 % +3 & 스킬 양자 폭탄 % +3 & 스킬 X-1 익스트루더 % +3 & "
				+ "스킬 새틀라이트 빔 % +3 & 스킬 FM-92 스팅어 % +3 & 스킬 팜페로 부스터 % +3 & 스킬 FM-92 스팅어 SW % +3 & 스킬 버스터 빔 % +3 & 스킬 바베~큐 % +3 & "
				+ "스킬 캐넌볼 % +3 & 스킬 사이즈믹 웨이브 % +3 & 스킬 M-137 개틀링건 % +7.5", "반복 1",
				"충전 레이저 라이플", "", "", 25, 11, 1, 2, CalculatorVersion.VER_1_0_e,
				"1", "스킬 레이저 라이플 % 90", "+", "스킬 레이저 라이플 % +10", "+", "스킬 레이저 라이플 % +5", "반복 2", 
				"미라클 비전", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"10", "증뎀버프 77.7", null,
				"듀얼 트리거", Skill_type.PASSIVE, "", 35, 1, 1, 3, CalculatorVersion.VER_1_0_e, "설명 명, 화속성 중 높은 속성 강화 값으로 낮은 값이 상승한다.", 
				"1", "fStat_듀얼트리거", null,
				"집중포화", Skill_type.BUF_ACTIVE, "", 40, 1, 1, 3,  CalculatorVersion.VER_1_0_e, "설명 그냥 넣어봤음", 
				"1", "", null,
				
				//////TP
				"M-137 개틀링건 강화", "M-137 개틀링건", Character_type.GUNNER_M, 50, 7, 5, 8, null,
				"바베~큐 강화", "바베~큐", Character_type.GUNNER_M, 50, 3, 1, 20, null,
				"M-3 화염방사기 강화", "M-3 화염방사기", Character_type.GUNNER_M, 50, 7, 5, 8, null,
				"슈타이어 대전차포 강화", "슈타이어 대전차포", Job.LAUNCHER_M, 55, 7, 5, 10, null,
				"캐넌볼 강화", "캐넌볼", "", 50, 7, 5, 10, null,
				"레이저 라이플 강화", "레이저 라이플", "", 50, 7, 5, 10, null,
				"화염 강타 강화", "화염 강타", "", 50, 7, 5, 10, null,
				"FM-92 스팅어 강화", "FM-92 스팅어", "", 65, 7, 5, 10, null,
				"양자 폭탄 강화", "양자 폭탄", "", 65, 7, 5, 10, null,
				"X-1 익스트루더 강화", "X-1 익스트루더", "", 65, 7, 5, 10, null,
				"FM-31 그레네이드 런처 강화", "FM-31 그레네이드 런처", "", 50, 1, 1, 20, null,
				
				
				/////////////남렝거
				/////액티브
				"라이징샷", Skill_type.ACTIVE, Job.RANGER_M, 1, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"45 2206 4.5896 0 0 ", "+ 2249 4.6792", "+ 2292 4.7698", "+ 2335 4.8594", "+ 2378 4.95", "+ 2421 5.04", null,
				"잭스파이크", "", "", 1, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"10 180 0.179 0 0", "+ 190 0.1896", "+ 200 0.2", "+ 210 0.2094", null,
				"은탄", Skill_type.ACTIVE, Job.RANGER_M, 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 한 탄창의 딜량입니다",
				"43 697*25 0 0 0", "귀속/은탄(언악정)/100", "+ 711*25", "귀속/은탄(언악정)/100", "+ 725*25", "귀속/은탄(언악정)/100", "+ 739*25", "귀속/은탄(언악정)/100", "+ 754*25 ", "귀속/은탄(언악정)/100",
				"+ 768*25", "귀속/은탄(언악정)/100", "+ 782*25", "귀속/은탄(언악정)/100", "+ 796*25", "귀속/은탄(언악정)/100", "+ 810*25", "귀속/은탄(언악정)/100", "+ 824*25", "귀속/은탄(언악정)/100",
				"+ 838*25", "귀속/은탄(언악정)/100", "+ 853*25", "귀속/은탄(언악정)/100", null,
				"은탄(언악정)", Skill_type.OPTION, "", 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 언데드, 악마, 정령 타입 몬스터 공격 시 추가",
				"43 174*25 0 0 0", "", "+ 178*25", "", "+ 181*25", "", "+ 185*25", "", "+ 189*25", "", "+ 192*25", "", "+ 196*25", "", "+ 199*25", "", "+ 203*25", "", "+ 206*25", "", "+ 210*25", "", "+ 213*25", null,
				"윈드밀", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"41 1098*2 18.3094*2 0 0", "+ 1120*2 18.6798*2", "+ 1142*2 19.0396*2", "+ 1164*2 19.4094*2", "+ 1186*2 19.7792*2", null,
				"퍼니셔", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"41 699*5 1.75*5 0 0", "+ 714*5 1.7792*5", "+ 728*5 1.8198*5", "+ 743*5 1.8594*5", "+ 757*5 1.8896*5", null,
				"헤드샷", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"38 3286 0 0 0", "+ 3359", "+ 3433", "+ 3506", "+ 3680", "+ 3654", "+ 3727", "+ 3800", null,
				"마하킥", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"38 3339 27.8792 0 0", "+ 3410 28.4698", "+ 3482 29.0698", "+ 3553 29.6656", "+ 3624 30.2594", "+ 3695 30.8490", null,
				"에어레이드", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33 4488*2 0 0 0", "+ 4595*2", "+ 4702*2", "+ 4809*2", "+ 4917*2", "+ 5024*2", "+ 5132*2", null,
				"트리플 탭", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"31 12421 0 0 0", "+ 12732", "+ 13044", "+ 13355", "+ 13666", "+ 13980", "+ 14291", "+ 14602", "+ 14914", null,
				"웨스턴 파이어", Skill_type.ACTIVE, "", 30, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1 0 0 0 0", "귀속/헤드샷/120", null,
				"이동사격", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 1283*30 0 0 0", "+ 1317*30", "+ 1352*30", "+ 1387*30", "+ 1422*30", "+ 1457*30", "+ 1492*30", "+ 1526*30", "+ 1561*30", "+ 1596*30", null,
				"난사", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 1587*20 0 0 0", "+ 1630*20", "+ 1673*20", "+ 1716*20", "+ 1759*20", "+ 1802*20", "+ 1845*20", "+ 1888*20", "+ 1931*20", "+ 1974*20",
				"+ 2017*20", "+ 2060*20", "+ 2103*20", null,
				"멀티 헤드샷", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26 4925*5 0 0 0 ", "+ 5066*5", "+ 5207*5", "+ 5349*5", "+ 5490*5", "+ 5631*5", "+ 5772*5", "+ 5914*5", null,
				"더블 건호크", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"23 68030 0 0 0", "+ 70170", "+ 72304", "+ 74444", "+ 76582", "+ 78700", "+ 80856", "+ 82996", "+ 85112", "+ 87252", "+ 89408", "+ 91526", null,
				"스커드 제노사이드", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"9 113770 0 0 0", "+ 122988", "+ 132273", "+ 141491", "+ 150709", "+ 159994", "+ 169212", null,
				"데들리 어프로치", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"16 9884*3 0 0 0", "+ 10282*3", "+ 10680*3", "+ 11077*3", "+ 11475*3", null,
				"제압 사격", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"11 41935 0 0 0", "+ 44037", "+ 46158", "+ 48260", "+ 50381", null,
				"패스트 드로우", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"8 2564*24 0 0 0", "+ 2716*24", "+ 2869*24", "+ 3021*24", "+ 3173*24", "+ 3325*24", "+ 3477*24", "+ 3629*24", null,
				"세븐스 플로우", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"2 89315 0 0 0", "+ 106137", "+ 122946", "+ 139769", "+ 156578", null,
				
				/////패시브
				"트리플 클러치", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 1, CalculatorVersion.VER_1_0_e,
				"1", "스킬 마하킥 % 10 & 스킬 라이징샷 % 15 & 스킬 헤드샷 % 15 & 물크 10 & 스킬 마하킥 % 30 & 스킬 윈드밀 % 30",
				"+", "스킬 마하킥 % +2 & 스킬 라이징샷 % +3 & 스킬 헤드샷 % +3 & 물크 +5 & 스킬 마하킥 % +5 & 스킬 윈드밀 % +5", "반복 1",
				"스타일리쉬", Skill_type.BUF_ACTIVE, "", 25, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 라이징샷 % 20 & 스킬 헤드샷 % 20 & 스킬 트리플 탭 % 20 & 스킬 난사 % 20 & 스킬 멀티 헤드샷 % 20 & 스킬 더블 건호크 % 20 & 스킬 제압 사격 % 20 & "
				+ "스킬 스커드 제노사이드 % 20 & 스킬 데들리 어프로치 % 20 & 스킬 패스트 드로우 % 20 & 스킬 세븐스 플로우 % 20 & 스킬 이동사격 % 20",
				"+", "스킬 라이징샷 % +1 & 스킬 헤드샷 % +1 & 스킬 트리플 탭 % +1 & 스킬 난사 % +1 & 스킬 멀티 헤드샷 % +1 & 스킬 더블 건호크 % +1 & 스킬 제압 사격 % +1 & "
				+ "스킬 스커드 제노사이드 % +1 & 스킬 데들리 어프로치 % +1 & 스킬 패스트 드로우 % +1 & 스킬 세븐스 플로우 % +1 & 스킬 이동사격 % +1", "반복 1",
				"데스 바이 리볼버", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_1_b,
				"10", "크증버프 105 & fStat_데바리", null,
				"죽음의 표식", Skill_type.BUF_ACTIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"15", "증뎀버프 33 & 물크 17.3", "+", "증뎀버프 +2 & 물크 +1.1", "+", "증뎀버프 +2 & 물크 +1.1", "+", "증뎀버프 +2 & 물크 +1.2", "반복 3",
				"강화 리볼버", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_e, "설명 리볼버 착용시 물공 (6+3.5*n)% 증가(소숫점 버림)",
				"6", "물리무기마스터리 27 리볼버", "+", "물리무기마스터리 30 리볼버", "+", "물리무기마스터리 34 리볼버", "+", "물리무기마스터리 37 리볼버",
				"+", "물리무기마스터리 41 리볼버", "+", "물리무기마스터리 44 리볼버", "+", "물리무기마스터리 48 리볼버", "+", "물리무기마스터리 51 리볼버",
				"+", "물리무기마스터리 55 리볼버", "+", "물리무기마스터리 58 리볼버", "+", "물리무기마스터리 62 리볼버", "+", "물리무기마스터리 65 리볼버",
				"+", "물리무기마스터리 69 리볼버", "+", "물리무기마스터리 72 리볼버", "+", "물리무기마스터리 76 리볼버", null,
				"사격술", Skill_type.PASSIVE, "", 80, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"4", "스킬 라이징샷 % 22 & 스킬 헤드샷 % 49 & 스킬 트리플 탭 % 22 & 스킬 난사 % 22 & 스킬 멀티 헤드샷 % 22 & 스킬 더블 건호크 % 22 & 스킬 제압 사격 % 22 & "
				+ "스킬 스커드 제노사이드 % 22 & 스킬 데들리 어프로치 % 22 & 스킬 패스트 드로우 % 22 & 스킬 세븐스 플로우 % 22 & 스킬 이동사격 % 22",
				"+", "스킬 라이징샷 % +2 & 스킬 헤드샷 % +4 & 스킬 트리플 탭 % +2 & 스킬 난사 % +2 & 스킬 멀티 헤드샷 % +2 & 스킬 더블 건호크 % +2 & 스킬 제압 사격 % +2 & "
				+ "스킬 스커드 제노사이드 % +2 & 스킬 데들리 어프로치 % +2 & 스킬 패스트 드로우 % +2 & 스킬 세븐스 플로우 % +2 & 스킬 이동사격 % +2",
				"+", "스킬 라이징샷 % +2 & 스킬 헤드샷 % +5 & 스킬 트리플 탭 % +2 & 스킬 난사 % +2 & 스킬 멀티 헤드샷 % +2 & 스킬 더블 건호크 % +2 & 스킬 제압 사격 % +2 & "
				+ "스킬 스커드 제노사이드 % +2 & 스킬 데들리 어프로치 % +2 & 스킬 패스트 드로우 % +2 & 스킬 세븐스 플로우 % +2 & 스킬 이동사격 % +2", "반복 2",
				
				/////TP
				"은탄 강화", "은탄", Job.RANGER_M, 50, 7, 5, -1, CalculatorVersion.VER_1_0_f,
				"1", "스킬 은탄 % 8.16", "+", "스킬 은탄 % 16.64", "+", "스킬 은탄 % 25.44", "+", "스킬 은탄 % 34.56", "+", "스킬 은탄 % 44",
				 "+", "스킬 은탄 % 53.76", "+", "스킬 은탄 % 63.84", null,
				"잭스파이크 강화", "잭스파이크", Job.RANGER_M, 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"라이징샷 강화", "라이징샷", Job.RANGER_M, 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"윈드밀 강화", "윈드밀", Job.RANGER_M, 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"퍼니셔 강화", "퍼니셔", Job.RANGER_M, 50, 3, 1, -1, CalculatorVersion.VER_1_0_f,
				"1", "스킬 퍼니셔 % 19", "+", "스킬 퍼니셔 % 26", "+", "스킬 퍼니셔 % 21", null,
				"마하킥 강화", "마하킥", Job.RANGER_M, 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"헤드샷 강화", "헤드샷", Job.RANGER_M, 60, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"에어레이드 강화", "에어레이드", Job.RANGER_M, 50, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"트리플 탭 강화", "트리플 탭", Job.RANGER_M, 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"이동사격 강화", "이동사격", Job.RANGER_M, 50, 7, 5, 10, CalculatorVersion.VER_1_1_b, null,
				//"1", "스킬 이동사격 % 16.6", "+", "스킬 이동사격 % 34.4", "+", "스킬 이동사격 % 53.4", "+", "스킬 이동사격 % 73.6", "+", "스킬 이동사격 % 95.0",
				//"+", "스킬 이동사격 % 117.6", "+", "스킬 이동사격 % 141.4", null,
				"난사 강화", "난사", Job.RANGER_M, 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"멀티 헤드샷 강화", "멀티 헤드샷", Job.RANGER_M, 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"더블 건호크 강화", "더블 건호크", Job.RANGER_M, 65, 7, 5, -1, CalculatorVersion.VER_1_0_f,
				"1", "스킬 더블 건호크 % 12.61", "+", "스킬 더블 건호크 % 22.06", "+", "스킬 더블 건호크 % 35.60", 
				"+", "스킬 더블 건호크 % 45.60", "+", "스킬 더블 건호크 % 65.37", "+", "스킬 더블 건호크 % 76.81", null,
				
				///공용스킬
				"고대의 기억", Skill_type.BUF_ACTIVE, Character_type.ALL, 15, 20, 10, 3,
				"10", "지능 150", "+", "지능 +15", "반복 1",
				"물리 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"10", "물크 10", "+", "물크 +1", "반복 1",
				"마법 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"10", "마크 10", "+", "마크 +1", "반복 1",
				"방어구 마스터리", Skill_type.PASSIVE, "", 15, 1, 1, 1, CalculatorVersion.VER_1_1_b,
				"1", "fStat_마스터리", null, 
		};
		
		return data;
	}
	
	public static Object[] skillInfo_mage()
	{
		Object[] data = new Object[] {
				/////////////우럭
				/////액티브
				"크리스탈 어택", Skill_type.ACTIVE, Job.ELEMENTALBOMBER, 20, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"36 0 0 1705*7 0", "+ 1740*7", "+ 1788*7", "+ 1817*7", "+ 1854*7", null,
				"파이어 로드", "", "", 25, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"33 0 0 1032*12 0", "+ 1059*12", "+ 1083*12", "+ 1107*12", "+ 1133*12", "+ 1157*12", null,
				"체인 라이트닝", "", "", 30, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"31 0 0 4242*4 0", "+ 4350*4", "+ 4458*4", "+ 4563*4", "+ 4669*4", null,
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
				"아마겟돈 스트라이크", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.ALL, true, CalculatorVersion.VER_1_0_d,
				"2 0 0 4988+416*13+89798 0", "+ 5928+494*13+106701", "+ 6867+571*13+123604", "+ 7806+651*13+140507", "+ 8745+729*13+157412", "+ 9684+808*13+174313", null,
				"속성 발동", "", "", 15, 60, 50, 5, Element_type.ALL, CalculatorVersion.VER_1_0_e,
				"16 0 0 1161 0", "+ 1221", "+ 1281", "+ 1341", null,
				"엘레멘탈 캐넌", "", "", 15, 11, 1, 1, Element_type.ALL, CalculatorVersion.VER_1_0_e,
				"1", "귀속/속성 발동/500", "+", "귀속/속성 발동/+10", "반복 1",
				
				/////패시브
				"원소 폭격", Skill_type.BUF_ACTIVE, "", 30, 5, 5, 5, CalculatorVersion.VER_1_1_b, "설명 검은 눈의 효과 적용",
				"5", "스킬 체인 라이트닝 % 10 & fStat_원소폭격", null, 
				"페이탈 엘레멘트", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_g,
				"15", "크증버프 24 & 마크 10.0", "+", "크증버프 +1.5 & 마크 +0.5", "반복 1",
				"검은 눈", "", "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_d, "설명 원소폭격 사용 시 스킬 데미지 증가",
				"6", "", "+", "", null,
				"엘레멘탈 포스", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 1, CalculatorVersion.VER_1_0_d,
				"1", "스킬 엘레멘탈 캐넌 % 50", "+", "스킬 엘레멘탈 캐넌 % +10", "반복 1",
				"마나 폭주", Skill_type.SWITCHING, "", 25, 30, 20, 2, CalculatorVersion.VER_1_0_f,
				"20", "증뎀버프 89", null,
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
				"엘레멘탈 레인 강화", "엘레멘탈 레인", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				
				////////////배메
				//////액티브
				"일반 체이서", Skill_type.ACTIVE_NOMARK, Job.BATTLEMAGE, 15, 60, 50, 2, Element_type.ALL, CalculatorVersion.VER_1_1_d,
				"38 0 0 0 19.38", "+ 19.8", "+ 20.22", "+ 20.62", "+ 21.03", "+ 21.45", "+ 21.87", "+ 22.28", null,
				"중 체이서", Skill_type.OPTION, "", 15, 1, 1, 1, CalculatorVersion.VER_1_0_f, "설명 중 체이서 데미지를 적용시킵니다(왕체이서 옵션 선택시 왕체이서 적용).",
				"1", "", null,
				"왕 체이서", Skill_type.OPTION, "", 15, 1, 1, 1, CalculatorVersion.VER_1_1_b, "설명 왕 체이서 데미지를 적용시킵니다.",
				"1", "fStat_왕체이서", null,
				"체이서 : 화", Skill_type.BUF_ACTIVE, "", 15, 60, 50, 2, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"38", "힘 410 & 지능 410", "+", "힘 420 & 지능 420", "+", "힘 430 & 지능 430", "+", "힘 420 & 지능 420", "+", "힘 441 & 지능 441",
				 "+", "힘 451 & 지능 451", "+", "힘 461 & 지능 461", "+", "힘 471 & 지능 471", "+", "힘 481 & 지능 481", null,
				"체이서 : 암", Skill_type.BUF_ACTIVE, "", 15, 60, 50, 2, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"38", "물크 37.2 & 마크 37.2", "+", "물크 38.2 & 마크 38.2", "+", "물크 39.2 & 마크 39.2", "+", "물크 40.2 & 마크 40.2", "+", "물크 41.1 & 마크 41.1",
				"+", "물크 42.1 & 마크 42.1", "+", "물크 43.1 & 마크 43.1", "+", "물크 44.1 & 마크 44.1", null,
				"낙화장", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"38 3370 14.29 3370 14.29", "+ 3445 14.62 3445 14.62", "+ 3520 14.95 3520 14.95", "+ 3596 15.25 3596 15.25",
				"+ 3672 15.57 3672 15.57", "+ 3747 15.90 3747 15.90", "+ 3822 16.23 3822 16.23", "+ 3898 16.54 3898 16.54", null,
				"용아", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"38 2143 7.129 2143 7.129", "+ 2191 7.270 2191 7.270", "+ 2240 7.401 2240 7.401", "+ 2290 7.570 2290 7.570",
				"+ 2337 7.709 2337 7.709", "+ 2386 7.836 2386 7.836", "+ 2434 7.979 2434 7.979", "+ 2483 8.150 2483 8.150", null,
				"원무곤", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"38 6017 0 0 0", "+ 6144", "+ 6273", "+ 6405", "+ 6527", "+ 6659", "+ 6789", "+ 6919", null,
				"천격", Skill_type.ACTIVE, "", 15, 30, 20, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 일반 체이서의 레벨을 따라갑니다",
				"20 250 0.2745 250 0.2745", "+ 257 0.2792 257 0.2792", "+ 264 0.2896 264 0.2896",
				"+ 271 0.2997 271 0.2997",  "+ 278 0.3099 278 0.3099", null,
				"더블 스윙", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"36 10924 0 0 0", "+ 11171", "+ 11409", "+ 11655", "+ 11900", "+ 12147", "+ 12390", null,
				"타이머 밤", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"33 0 174.01 0 174.01", "+ 177.77 177.77", "+ 182.71 182.71", "+ 186.86 186.86", "+ 193.68 193.68", "+ 201.40 201.40", "+ 201.89 201.89", "+ 209.47 209.47", null, 
				"퓨전 체이서", Skill_type.DAMAGE_BUF, "", 30, 11, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"1", "귀속/일반 체이서/538 & 귀속/사도화 체이서/538 & 힘 292 & 지능 292 & 물크 26 & 마크 26", "+", "귀속/일반 체이서/552 & 귀속/사도화 체이서/552 & 힘 302 & 지능 302 & 물크 27 & 마크 27",
				"+", "귀속/일반 체이서/566 & 귀속/사도화 체이서/566 & 힘 310 & 지능 310 & 물크 28 & 마크 28", "+", "귀속/일반 체이서/580 & 귀속/사도화 체이서/580 & 힘 320 & 지능 320 & 물크 29 & 마크 29",
				"+", "귀속/일반 체이서/592 & 귀속/사도화 체이서/592 & 힘 326 & 지능 326 & 물크 30 & 마크 30", null,
				"퓨전 체이서(완충)", Skill_type.ACTIVE, "", 30, 11, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"1", "귀속/일반 체이서/630 & 귀속/사도화 체이서/630", "+", "귀속/일반 체이서/630 & 귀속/사도화 체이서/630", "+", "귀속/일반 체이서/660 & 귀속/사도화 체이서/660",
				"+", "귀속/일반 체이서/690 & 귀속/사도화 체이서/690", "+", "귀속/일반 체이서/690 & 귀속/사도화 체이서/690", "+", "귀속/일반 체이서/715 & 귀속/사도화 체이서/715", null,
				"퓨전 체이서(완충설정)", Skill_type.OPTION, "", 30, 1, 1, 1, CalculatorVersion.VER_1_1_b, "설명 옵션을 선택할 경우 완충 퓨전 체이서가 딜표에 표기됩니당",
				"1", "fStat_퓨체", null,
				"쇄패", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"31 12195 0 0 0", "+ 12504", "+ 12811", "+ 13199", "+ 13424", "+ 13730", "+ 14039", null,
				"체이서 프레스", Skill_type.ACTIVE, "", 35, 11, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 체이서 프레스에 소모되는 체이서 숫자는 체이서 프레스(체이서 수)에서 설정하세요",
				"1", "귀속/일반 체이서/115 & 귀속/테아나 체이서/115 & 귀속/사도화 체이서/115", "+", "귀속/일반 체이서/121 & 귀속/테아나 체이서/121 & 귀속/사도화 체이서/121",
				"+", "귀속/일반 체이서/128 & 귀속/테아나 체이서/128 & 귀속/사도화 체이서/128", "+", "귀속/일반 체이서/133 & 귀속/테아나 체이서/133 & 귀속/사도화 체이서/133",
				"+", "귀속/일반 체이서/140 & 귀속/테아나 체이서/140 & 귀속/사도화 체이서/140", null,
				"체이서 프레스(체이서 수)", Skill_type.INPUT, "", 35, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 체이서 프레스에 소모되는 체이서 숫자를 입력합니다",
				"1", "횟수(재료수) 9 & fStat_체프", null,
				"뇌연격", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"28 26666 0 0 0", "+ 27426", "+ 28124", "+ 28898", "+ 29597", "+ 30279", "+ 31039", "+ 31765", null,
				"강습유성타", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"26 26300 0 0 0", "+ 27057", "+ 27811", "+ 28573", "+ 29329", null,
				"황룡천공", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, 
				"23 40730 0 0 383.06", "+ 42009 395.09", "+ 43280 407.04", "+ 44582 419.35", "+ 45878 431.45", "+ 47149 443.40", null,
				"트윙클 스매쉬", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"16 28969 0 0 0", "+ 30136", "+ 31301", "+ 32465", "+ 33632", null,
				"진 뇌연격", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"11 0 354.48 0 354.48", "+ 372.19 372.19", "+ 389.44 389.44", "+ 407.28 407.28", "+ 424.69 424.69", "+ 442.24 442.24", null,
				"체이서 강화 사출", Skill_type.ACTIVE, "", 80, 11, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"1", "귀속/일반 체이서/75*3 & 귀속/사도화 체이서/75*3", "+", "귀속/일반 체이서/81*3 & 귀속/사도화 체이서/81*3",
				"+", "귀속/일반 체이서/88*3 & 귀속/사도화 체이서/88*3", "+", "귀속/일반 체이서/94*3 & 귀속/사도화 체이서/94*3", null,
				"사도의 춤", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"6 39797 0 0 0", "+ 42476", "+ 45155", "+ 47832", "+ 50510", "+ 53192", null,
				"퀘이사 익스플로젼", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"10 80918 0 0 763.38", "+ 86997 820.73", "+ 93074 878.06", "+ 99153 935.41", "9 74839 0 0 706.03", null,
				"일기당천 쇄패", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"2 60667 0 0 572.33", "+ 72087 680.07", "+ 83507 787.80", "+ 94926 895.53", null,
				"사도화 체이서", Skill_type.ACTIVE_NOMARK, "", 85, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_1_d,
				"2 3471 0 0 34.71", "+ 3754 37.54", "+ 4035 40.35", "+ 4320 43.2", null,
				"천지쇄패", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"10 11236 0 0 112.36", "+ 12082 120.82", "+ 12928 129.28", "+ 13777 137.77", "9 10390 0 0 103.9", null,
				"테아나 체이서", Skill_type.ACTIVE_NOMARK, "", 50, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_1_d,
				"10 3575 0 0 35.75", "+ 3843 38.43", "+ 4114 41.14", "+ 4385 43.85", "9 3307 33.07", null,
				"대시공격", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"10 1553*4 0 0 15.53*4", "+ 1671*4 16.71*4", "+ 1789*4 17.89*4", "+ 1906*4 19.06*4", "9 1435*4 0 0 14.35*4", null,
				"체이서", Skill_type.INPUT, "", 15, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 사용자가 입력한 수만큼의 체이서 딜 합을 표기합니다",
				"1", "횟수(재료수) 1 & 귀속/일반 체이서/100 & 귀속/테아나 체이서/100 & 귀속/사도화 체이서/100 & fStat_체이서합딜", null,
				
				/////패시브
				"창 마스터리", Skill_type.PASSIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_0_f, "설명 물리공격력 증가 2n%",
				"20", "물리무기마스터리 40 창", "+", "물리무기마스터리 42 창", "+", "물리무기마스터리 44 창", "+", "물리무기마스터리 46 창",
				"+", "물리무기마스터리 48 창", "+", "물리무기마스터리 50 창", "+", "물리무기마스터리 52 창", "+", "물리무기마스터리 54 창",
				"+", "물리무기마스터리 56 창", "+", "물리무기마스터리 58 창", "+", "물리무기마스터리 60 창", null,
				"봉 마스터리", Skill_type.PASSIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_0_f, "설명 물리공격력 증가 2n%",
				"20", "물리무기마스터리 40 봉", "+", "물리무기마스터리 42 봉", "+", "물리무기마스터리 44 봉", "+", "물리무기마스터리 46 봉",
				"+", "물리무기마스터리 48 봉", "+", "물리무기마스터리 50 봉", "+", "물리무기마스터리 52 봉", "+", "물리무기마스터리 54 봉",
				"+", "물리무기마스터리 56 봉", "+", "물리무기마스터리 58 봉", "+", "물리무기마스터리 60 봉", null,
				"전장의 여신", Skill_type.PASSIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_1_c, "설명 창, 봉 마스터리의 독공 15%가 포함된 값입니다\n무기가 창, 봉이 아닐 시 실제로는 15%가 빠진 값이 적용됩니다",
				"20", "증뎀버프 25 & 독공마스터리 40 & fStat_전장의여신", "+", "증뎀버프 +2 & 독공마스터리 +2 & fStat_전장의여신", "반복 1",
				"배틀 그루브", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_f, "설명 3단계의 증가량입니다",
				"15", "스킬 천격 % 35.5 & 스킬 용아 % 35.5 & 스킬 원무곤 % 35.5 & 스킬 낙화장 % 35.5 & 스킬 더블 스윙 % 35.5 & 스킬 쇄패 % 35.5 & 스킬 뇌연격 % 35.5 & 스킬 강습유성타 % 35.5 & 스킬 황룡난무 % 35.5 & "
				+ "스킬 천지쇄패 % 35.5 & 스킬 퀘이사 익스플로젼 % 35.5 & 스킬 트윙클 스매쉬 % 35.5 & 스킬 사도의 춤 % 35.5 & 스킬 일기당천 쇄패 % 35.5 & 스킬 타이머 밤 % 35.5 & "
				+ "스킬 황룡천공 % 35.5 & 스킬 진 뇌연격 % 35.5 & 스킬 일반 체이서 % 12 & 스킬 테아나 체이서 % 12 & 스킬 사도화 체이서 % 12",
				"+", "스킬 천격 % +1.5 & 스킬 용아 % +1.5 & 스킬 원무곤 % +1.5 & 스킬 낙화장 % +1.5 & 스킬 더블 스윙 % +1.5 & 스킬 쇄패 % +1.5 & 스킬 뇌연격 % +1.5 & 스킬 강습유성타 % +1.5 & 스킬 황룡난무 % +1.5 & "
				+ "스킬 천지쇄패 % +1.5 & 스킬 퀘이사 익스플로젼 % +1.5 & 스킬 트윙클 스매쉬 % +1.5 & 스킬 사도의 춤 % +1.5 & 스킬 일기당천 쇄패 % +1.5 & 스킬 타이머 밤 % +1.5 & "
				+ "스킬 황룡천공 % +1.5 & 스킬 진 뇌연격 % +1.5 & 스킬 일반 체이서 % +0.5 & 스킬 테아나 체이서 % +0.5 & 스킬 사도화 체이서 % +0.5", "반복 1",
				"체이서 에볼루션", Skill_type.BUF_ACTIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_f,
				"6", "스킬 일반 체이서 % 26", "+", "스킬 일반 체이서 % +3", "반복 1",
				"문무겸비", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_b,
				"6", "크증버프 34 & fStat_문무겸비", "+", "크증버프 +2 & fStat_문무겸비", "반복 1",
				"테아나 변신~!!", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 5, CalculatorVersion.VER_1_0_f,
				"9", "스킬 일반 체이서 % -100 & 스킬 천격 % -100 & 스킬 용아 % -100 & 스킬 낙화장 % -100 & 스킬 더블 스윙 % -100 & 스킬 쇄패 % -100 & 스킬 진 뇌연격 % -100 & 스킬 원무곤 % -100 & 스킬 타이머 밤 % -100 & "
				+ "스킬 퓨전 체이서 % -100 & 스킬 퓨전 체이서(완충) % -100 & 스킬 트윙클 스매쉬 % -100 & 스킬 체이서 강화 사출 % -100 & 스킬 사도의 춤 % -100", 
				"+", "스킬 일반 체이서 % -100 & 스킬 천격 % -100 & 스킬 용아 % -100 & 스킬 낙화장 % -100 & 스킬 더블 스윙 % -100 & 스킬 쇄패 % -100 & 스킬 진 뇌연격 % -100 & 스킬 원무곤 % -100 & 스킬 타이머 밤 % -100 & "
				+ "스킬 퓨전 체이서 % -100 & 스킬 퓨전 체이서(완충) % -100 & 스킬 트윙클 스매쉬 % -100 & 스킬 체이서 강화 사출 % -100 & 스킬 사도의 춤 % -100", null,
				"사도화", Skill_type.BUF_ACTIVE, "", 85, 40, 30, 5, CalculatorVersion.VER_1_0_f,
				"2", "증뎀버프 18 & 스킬 퀘이사 익스플로젼 % -18/1.18 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 21 & 스킬 퀘이사 익스플로젼 % -21/1.21 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100",
				"+", "증뎀버프 24 & 스킬 퀘이사 익스플로젼 % -24/1.24 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 27 & 스킬 퀘이사 익스플로젼 % -27/1.27 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100",
				"+", "증뎀버프 30 & 스킬 퀘이사 익스플로젼 % -30/1.30 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 33 & 스킬 퀘이사 익스플로젼 % -33/1.33 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100",
				"+", "증뎀버프 36 & 스킬 퀘이사 익스플로젼 % -36/1.36 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 39 & 스킬 퀘이사 익스플로젼 % -39/1.39 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100",
				"+", "증뎀버프 42 & 스킬 퀘이사 익스플로젼 % -42/1.42 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 45 & 스킬 퀘이사 익스플로젼 % -45/1.45 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100",
				"+", "증뎀버프 48 & 스킬 퀘이사 익스플로젼 % -48/1.48 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", "+", "증뎀버프 51 & 스킬 퀘이사 익스플로젼 % -51/1.51 & 스킬 일반 체이서 % -100 & 스킬 테아나 체이서 % -100", null,
				"컨버전", Skill_type.OPTION,  "", 20, 1, 1, 1, CalculatorVersion.VER_1_1_b, "설명 옵션을 선택하면 마법(매지컬 테아나~!! 적용)\n선택하지 않으면 물리 컨버전(황룡난무 적용)",
				"1", "fStat_컨버전-배메", null,
				
				/////TP
				"천격 강화", "천격", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"용아 강화", "용아", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"낙화장 강화", "낙화장", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"원무곤 강화", "원무곤", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"체이서 사출 강화", "체이서(합딜)", "", 50, 4, 2, 5, CalculatorVersion.VER_1_0_f, "설명 사출되는 체이서의 딜만 강화시킵니다", null,
				"더블 스윙 강화", "더블 스윙", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"타이머 밤 강화", "타이머 밤", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"쇄패 강화", "쇄패", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"퓨전 체이서 강화", "퓨전 체이서 & 퓨전 체이서(완충)", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"뇌연격 강화", "뇌연격", "", 55, 7, 5, -1, CalculatorVersion.VER_1_0_f,
				"1", "스킬 뇌연격 % 9.7", "+", "스킬 뇌연격 % 19.3", "+", "스킬 뇌연격 % 29.0", "+", "스킬 뇌연격 % 38.7", "+", "스킬 뇌연격 % 48.3",
				"+", "스킬 뇌연격 % 58.0", "+", "스킬 뇌연격 % 67.7", null,
				"강습유성타 강화", "강습유성타", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"황룡천공 강화", "황룡천공", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				
				
				//////////////엘마
				/////액티브
				"매직 미사일", Skill_type.ACTIVE, Job.ELEMENTALMASTER, 1, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"45 0 0 687 7324/960", "+ 701 7478/960", "+ 714 7612/960", "+ 727 7756/960", "+ 741 7900/960", "+ 754 8044/960", null,
				"랜턴 파이어", Skill_type.ACTIVE, "", 5, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_f,
				"43 0 0 1876 5385/960", "+ 1912 5472/960", "+ 1952 5567/960", "+ 2027 5740/960", "+ 2065 5836/960", null,
				"플로레 비비기", Skill_type.ACTIVE, "", 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_f,
				"43 0 0 929*3 2889*3/960", "+ 948*3 2947*3/960", "+ 967*3 2985*3/960", "+ 986*3 3043*3/960", "+ 1004*3 3081*3/960", "+ 1024*3 3139*3/960", null,
				"프로스트 헤드", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_f,
				"41 0 0 1985 9110/960", "+ 2026 9302/960", "+ 2068 9484/960", "+ 2110 9676/960", "+ 2152 9878/960", null,
				"플루토", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_f,
				"41 0 0 967*2 3743*2/960", "+ 987*2 5001*2/960", "+ 1008*2 5097*2/960", "+ 1028*2 5203*2/960", "+ 1048*2 5308*2/960", null,
				"플레임 스트라이크", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_f,
				"38 0 0 8137 0", "+ 8310", "+ 8484","+ 8658","+ 8831","+ 9006","+ 9179","+ 9352","+ 9527","+ 9700", null,
				"보이드", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_f, "설명 8히트 기준입니다",
				"36 0 0 1447*8 0", "+ 1478*8", "+ 1512*8", "+ 1544*8", "+ 1576*8", "+ 1609*8", "+ 1641*8", "+ 1673*8", "+ 1704*8", null,
				"썬버스트", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_f,
				"33 0 0 7036 0", "+ 7204", "+ 7372","+ 7542","+ 7710","+ 7878","+ 8046","+ 8214","+ 8382", null,
				"칠링 펜스", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_f,
				"33 0 0 14735 0", "+ 15090", "+ 15441", "+ 15792", "+ 16144", "+ 16498", "+ 16849", "+ 17200", "+ 17552", null,
				"핼로윈 버스터", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_f,
				"28 0 0 3576+14309 0", "+ 3674+14693", "+ 3771+15084", "+ 3869+15470", "+ 3996+15858", "+ 4060+16249", "+ 4158+16635", "+ 4256+17024", null,
				"썬더 콜링", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_f,
				"28 0 0 7291*3 0", "+ 7493*3", "+ 7688*3", "+ 7886*3", "+ 8088*3", "+ 8283*3", "+ 8482*3", "+ 8683*3", null,
				"나이트 할로우", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_f,
				"26 0 0 1546*17+7739 0", "+ 1564*17+7963", "+ 1636*17+8187", "+ 1682*17+8411", "+ 1726*17+8634", "+ 1771*17+8857", "+ 1816*17+9082", null,
				"아크틱 피스트", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_f,
				"26 0 0 4549*8 0", "+ 4680*8", "+ 4813*8", "+ 4944*8", "+ 5073*8", "+ 5206*8", "+ 5337*8", null,
				"애스트럴 스톰", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_f,
				"10 0 0 371*40+1886*40+924*5+3692*5 0", "+ 398*40+1990*40+993*5+3969*5", "+ 425*40+2096*40+1062*5+4246*5", "+ 452*40+2201*40+1131*5+4523*5", "+ 482*40+2201*40+1131*5+4523*5"
				, "+ 510*40+2410*40+1271*5+5078*5", "+ 537*40+2514*40+1342*5+5355*5", "+ 566*40+2620*40+1411*5+5633*5", "+ 593*40+2725*40+1479*5+5910*5", "+ 620*40+2829*40+1550*5+6187*5"
				, "+ 650*40+2934*40+1619*5+6464*5", "+ 677*40+3038*40+1687*5+6742*5", "9 0 0 352*40+1782*40+855*5+3415*5 0", null,
				"샤이닝 칠링 펜스", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.WATER_LIGHT, CalculatorVersion.VER_1_0_f,
				"16 0 0 3535+1238*9+3888*5 0", "+ 3679+1286*9+4046*5", "+ 3821+1338*9+4202*5", "+ 3962+1385*9+4359*5",
				"+ 4106+1437*9+4514*5", "+ 4248+1485*9+4670*5", "+ 4388+1537*9+4827*5", null,
				"플레임 보이드 볼케이노", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.FIRE_DARKNESS, CalculatorVersion.VER_1_0_f, "설명 2줄 데미지입니다","설명 현재 표기오류로 딜측정이 혼파망이라 합니다 적당히 걸러서보세요",
				"11 0 0 2036+291*113+273*8+8819 0", "+ 2139+306*113+285*8+9261", "+ 2241+321*113+297*8+9707", "+ 2345+336*113+312*8+10151", "+ 2447+350*113+324*8+10593", "+ 2549+366*113+339*8+11037"
				, "+ 2651+380*113+354*8+11483", "+ 2756+393*113+366*8+11925", "+ 2858+410*113+378*8+12369", "+ 2960+423*113+393*8+12815", "+ 3062+440*113+405*8+13257", null,
				"더 게이트", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_f, 
				"6 0 0 3114*1.75*10 0", "+ 3321*1.75*10", "+ 3531*1.75*10", "+ 3740*1.75*10", "+ 3950*1.75*10", "+ 4161*1.75*10", "+ 4369*1.75*10", "+ 4580*1.75*10", "+ 4789*1.75*10", null,
				"제 6원소", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_f,
				"2 0 0 1098*20+51209 0", "+ 1304*20+60849", "+ 1511*20+70488", "+ 1717*20+80128", "+ 1924*20+89768", "+ 142007", null,
				
				/////패시브
				"속성 마스터리", Skill_type.PASSIVE, "", 30, 15, 5, 7, CalculatorVersion.VER_1_0_f,
				"5", "증뎀버프 40", "+", "증뎀버프 +4", "반복 1",
				"원소 집중", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_f,
				"15", "화속깍 66 & 수속깍 66 & 명속깍 66 & 암속깍 66", "+", "화속깍 +3 & 수속깍 +3 & 명속깍 +3 & 암속깍 +3", "반복 1",
				"엘레멘탈 포텐셜", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_f,
				"6", "스킬 랜턴 파이어 % 30 & 스킬 플로레 비비기 % 30 & 스킬 프로스트 헤드 % 30 & 스킬 플루토 % 30 & 스킬 플레임 스트라이크 % 30 & 스킬 보이드 % 30 & 스킬 썬버스트 % 30 & 스킬 칠링 펜스 % 30 & 스킬 썬더 콜링 % 20 & "
				+ "스킬 아크틱 피스트 % 16 & 스킬 핼로윈 버스터 % 12 & 스킬 핼로윈 버스터 % 4.6*4 & 스킬 나이트 할로우 % 15 & 스킬 나이트 할로우 % 4/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % 30 & 스킬 플레임 보이드 볼케이노 % 30 & 스킬 더 게이트 % 30 & 스킬 애스트럴 스톰 % 30 & 스킬 제 6원소 % 30",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 5.0*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 5/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 5.4*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 5/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 5.8*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 5/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 6.1*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 6/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 6.5*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 6/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2",
				"+", "스킬 랜턴 파이어 % +2 & 스킬 플로레 비비기 % +2 & 스킬 프로스트 헤드 % +2 & 스킬 플루토 % +2 & 스킬 플레임 스트라이크 % +2 & 스킬 보이드 % +2 & 스킬 썬버스트 % +2 & 스킬 칠링 펜스 % +2 & 스킬 썬더 콜링 % +2 & "
				+ "스킬 아크틱 피스트 % +2 & 스킬 핼로윈 버스터 % +2 & 스킬 핼로윈 버스터 % 6.9*4 & 스킬 나이트 할로우 % +2 & 스킬 나이트 할로우 % 7/17*77.25 & "
				+ "스킬 샤이닝 칠링 펜스 % +2 & 스킬 플레임 보이드 볼케이노 % +2 & 스킬 더 게이트 % +2 & 스킬 애스트럴 스톰 % +2 & 스킬 제 6원소 % +2", null,
				"엘레멘탈 번", Skill_type.SWITCHING, "", 20, 30, 20, 3, CalculatorVersion.VER_1_0_f, "설명 수치 입력 시 초월의 룬 증가량을 포함한 수치를 입력해주세요",
				"20", "지능 1567 & 증뎀버프 37.44", null,
				"초월의 룬", Skill_type.BUF_ACTIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"6", "스킬 속성 마스터리 % 11.9 & 스킬 원소 집중 % 28", "+", "스킬 속성 마스터리 % +1.2 & 스킬 원소 집중 % +2", "+", "스킬 속성 마스터리 % +1.2 & 스킬 원소 집중 % +2",
				"+", "스킬 속성 마스터리 % +1.3 & 스킬 원소 집중 % +2", "반복 2",
				"메모라이즈", Skill_type.PASSIVE, "", 15, 11, 1, 1, CalculatorVersion.VER_1_0_f,
				"1", "", "2", "", null,
				"쇼타임", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"10", "", "11", "", null,
				"무충검사", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_1_b, "설명 템상태가 무충조건을 만족하면 자동으로 아이콘이 활성화됩니다",
				"1", "fStat_무충", null,
				
				/////TP
				"프로스트 헤드 강화", "프로스트 헤드", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"랜턴 파이어 강화", "랜턴 파이어", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"플루토 강화", "플루토", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"플로레 비비기 강화", "플로레 비비기", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"보이드 강화", "보이드", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"플레임 스트라이크 강화", "플레임 스트라이크", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"썬버스트 강화", "썬버스트", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"칠링 펜스 강화", "칠링 펜스", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"핼로윈 버스터 강화", "핼로윈 버스터", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"아크틱 피스트 강화", "아크틱 피스트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"썬더 콜링 강화", "썬더 콜링", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"나이트 할로우 강화", "나이트 할로우", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				
				
				///////////////크리에이터
				/////액티브
				"파이어 월", Skill_type.ACTIVE, Job.CREATOR, 1, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_1_g,
				"48 0 0 0 3.36", "+ 3.419", "+ 3.47", "+ 3.53", "+ 3.59", "+ 3.65", "+ 3.649", "45 0 0 0 3.183", "+ 3.242", "+ 3.3", null,
				"운석 낙하", Skill_type.ACTIVE, "", 1, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_1_g,
				"48 0 0 0 22.28", "+ 22.67", "+ 23.06", "+ 23.45", "+ 23.85", "+ 24.24", "+ 24.24", "45 0 0 0 21.11", "+ 21.5", "+ 21.89", null,
				"아이스 스톤", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_1_g,
				"43 0 0 0 10.13*4", "+ 10.37*4", "+ 10.53*4", "+ 10.72*4", "+ 10.95*4", "+ 11.12*4", "41 0 0 0 9.65*4", "+ 9.89*4", null,
				"플레임 허리케인", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"33 0 0 0 42.37*12", "+ 43.39*12", "+ 44.4*12", "+ 52.13*12", "+ 53.336*12", "+ 54.603*12", "+ 55.765*12", "31 0 0 0 40.33*12", "+ 41.35*12", null,
				"윈드 프레스", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"28 0 0 0 21.92", "+ 22.51", "+ 23.11", "+ 23.7", "+ 24.29", "+ 24.89", "+ 25.49", "+ 26.08", "+ 26.68", "26 0 0 0 20.74", "+ 21.33", null,
				"윈드 스톰", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"28 0 0 0 88.98", "+ 91.4", "+ 93.81", "+ 96.23", "+ 98.65", "+ 101.06", "+ 103.48", "+ 105.88", "+ 108.23", "26 0 0 0 84.14", "+ 86.56", null,
				"에어볼", Skill_type.ACTIVE, "", 50, 40, 30, 3, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"16 0 0 0 63.59*16", "+ 66.59*16", "+ 69.58*16", "+ 72.56*16", "+ 75.56*16", "+ 78.55*16", "+ 81.53*16", "+ 84.53*16", "+ 87.52*16", "+ 90.51", "14 0 0 0 57.59*16", "+ 60.59*16", null,
				"에어볼(막타)", "원본 - 에어볼", "", 50, 40, 30, 3, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"16 0 0 0 296.65", "+ 310.61", "+ 324.53", "+ 338.48", "+ 352.44", "+ 366.38", "+ 380.32", "+ 394.26", "+ 408.22", "+ 422.169", "14 0 0 0 268.73", "+ 282.69", null,
				"에어볼 - 4면튕김", Skill_type.OPTION, "", 50, 1, 1, 3, CalculatorVersion.VER_1_1_f, "설명 에어볼 4면튕김 적용",
				"1", "스킬 에어볼(막타) % 66", null,
				"빙하시대", Skill_type.DAMAGE_BUF, "", 60, 40, 30, 3, Element_type.WATER, CalculatorVersion.VER_1_1_g,
				"12 0 0 0 27.89*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "+ 29.51*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "+ 31.13*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50",
				"+ 32.74*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "+ 34.35*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "+ 35.97*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", 
				"+ 37.58*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "+ 39.2*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", "11 0 0 0 26.27*22", "화속깍 50 & 수속깍 50 & 명속깍 50 & 암속깍 50", null,
				"빙하시대 - 범위증가", Skill_type.OPTION, "", 60, 1, 1, 3, CalculatorVersion.VER_1_1_f, "설명 빙하시대 범위 증가 기능 사용",
				"1", "스킬 빙하시대 % -20", null,
				"링크", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"11 0 0 0 33.66*37", "+ 35.36*37", "+ 37.05*37", "+ 38.75*37", "+ 40.45*37", "+ 42.14*37", "+ 43.84*37", "+ 45.52*37", "+ 47.22*37", "10 31.96", null,
				"타임 포워드", Skill_type.ACTIVE, "", 80, 40, 30, 3, Element_type.NONE, true, CalculatorVersion.VER_1_1_g,
				"4 0 0 0 13.65*30+409.54", "+ 14.71*30+441.38", "+ 15.77*30+473.23", "+ 16.84*30+505.10", "+ 17.9*30+536.95", 
				"+ 18.95*30+568.8", "+ 20.02*30+600.64", "+ 21.08*30+632.5", "+ 22.15*30+664.35", null,
				"창조의 공간", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 점프로 동시에 터트린 딜입니다",
				"2 0 0 0 331.52*6", "+ 393.93*6", "+ 456.34*6", "+ 518.74*6", "+ 581.14*6", "+ 643.55*6", "+ 705.95*6", "+ 768.36*6", null,
				
				/////패시브
				"화염", Skill_type.PASSIVE, "", 1, 60, 50, 3, CalculatorVersion.VER_1_1_f,
				"30", "스킬 파이어 월 % 45 & 스킬 운석 낙하 % 45", "+", "스킬 파이어 월 % +1 & 스킬 운석 낙하 % +1", "반복 1",
				"냉기", Skill_type.PASSIVE, "", 10, 60, 50, 3, CalculatorVersion.VER_1_1_f,
				"27", "스킬 아이스 스톤 % 45 & 스킬 아이스 플레이트 % 45", "+", "스킬 아이스 스톤 % +1 & 스킬 아이스 플레이트 % +1", "반복 1",
				"수호", Skill_type.PASSIVE, "", 30, 60, 50, 3, CalculatorVersion.VER_1_1_f,
				"21", "스킬 플레임 허리케인 % 45", "+", "스킬 플레임 허리케인 % +1", "반복 1",
				"바람", Skill_type.PASSIVE, "", 40, 60, 50, 3, CalculatorVersion.VER_1_1_f,
				"17", "스킬 윈드 프레스 % 45 & 스킬 윈드 스톰 % 45", "+", "스킬 윈드 프레스 % +1 & 스킬 윈드 스톰 % +1", "반복 1",
				"변이", Skill_type.PASSIVE, "", 50, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"14", "스킬 에어볼 % 80", "+", "스킬 에어볼 % +2", "반복 1",
				"재현", Skill_type.PASSIVE, "", 60, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"11", "스킬 빙하시대 % 80", "+", "스킬 빙하시대 % +2", "반복 1",
				"조율", Skill_type.PASSIVE, "", 70, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"7", "스킬 링크 % 80", "+", "스킬 링크 % +2", "반복 1",
				"왜곡", Skill_type.PASSIVE, "", 80, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"4", "스킬 타임 포워드 % 80", "+", "스킬 타임 포워드 % +2", "반복 1",
				"창조", Skill_type.PASSIVE, "", 85, 40, 30, 5, CalculatorVersion.VER_1_1_f,
				"2", "스킬 창조의 공간 % 80", "+", "스킬 창조의 공간 % +2", "반복 1",
				"파이어 월 횟수입력", Skill_type.INPUT, "", 1, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 파이어 월의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"운석 낙하 횟수입력", Skill_type.INPUT, "", 1, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 운석 낙하의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"아이스 스톤 횟수입력", Skill_type.INPUT, "", 10, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 아이스 스톤의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"플레임 허리케인 횟수입력", Skill_type.INPUT, "", 30, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 플레임 허리케인의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"윈드 프레스 횟수입력", Skill_type.INPUT, "", 40, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 윈드 프레스의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"윈드 스톰 횟수입력", Skill_type.INPUT, "", 40, 1, 1, 1, CalculatorVersion.VER_1_1_f, "설명 딜표에 표기될 윈드 스톰의 사용횟수를 입력합니다",
				"1", "횟수(재료수) 1", null,
				"창조의 공간 - 게이지", Skill_type.INPUT, "", 85, 1, 1, 1, CalculatorVersion.VER_1_1_f, 
				"설명 창조의 공간의 스킬 강화 상태로 변경", "설명 화염/냉기/바람스킬 딜표 표기는 강화상태에서의 최대 히트수 기준",
				"1", "fStat_창조의공간", null,
				
				/////TP
				"파이어 월 강화", "파이어 월", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"운석 낙하 강화", "운석 낙하", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"아이스 스톤 강화", "아이스 스톤", "", 55, 7, 5, 12, CalculatorVersion.VER_1_1_f, null,
				"윈드 프레스 강화", "윈드 프레스", "", 65, 7, 5, 12, CalculatorVersion.VER_1_1_f, null,
				"플레임 허리케인 강화", "플레임 허리케인", "", 55, 7, 5, 12, CalculatorVersion.VER_1_1_f, null,
				"윈드 스톰 강화", "윈드 스톰", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
		};
		
		return data;
	}
	
	public static Object[] skillInfo_priest()
	{
		Object[] data = new Object[] {
				////////////////////퇴마
				/////액티브
				"진격의 황룡", Skill_type.ACTIVE, Job.EXORCIST, 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_g,
				"2 104076 0 104134 0", "+ 123667 123730", "+ 143258 143327", "+ 162849 162923", "+ 182440 182520", "+ 202031 202116", "+ 221622 221747", "+ 241213 241343",  null,
				"창룡격", Skill_type.DAMAGE_BUF, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 버프 - 컨버젼이 적용된 쪽으로\n크리티컬 +(7+Lv)\n힘 +(120+5*Lv)\n속강 +(13+Lv)", 
				"9 87330 0 87330 0", "fStat_창룡격", "+ 94423 94423", "fStat_창룡격", "+ 94423 94423", "fStat_창룡격", "+ 94423 94423", "fStat_창룡격", "+ 101516 101516", "fStat_창룡격",
				"+ 108608 108608", "fStat_창룡격", "+ 115701 115701", "fStat_창룡격", "+ 122794 122794", "fStat_창룡격", "+ 129887 129887", "fStat_창룡격", "+ 136980 136980", "fStat_창룡격", 
				"+ 144073 144073", "fStat_창룡격", "+ 151165 151165", "fStat_창룡격", "+ 158258 158258", "fStat_창룡격", null,
				"식신의 군", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"23 55150 0 55150 0", "+ 56880 56880", "+ 58624 58624", "+ 60353 60353", "+ 62082 62082", "+ 63812 63812", "+ 65541 65541", "+ 67271 67271", "+ 69000 69000",
				"+ 70745 70745", "+ 72474 72474", "+ 74203 74203", "+ 75933 75933", "+ 77662 77662", "+ 79392 79392", "+ 81121 81121", "+ 82866 82866", "+ 84595 84595",null,
				"역전의 칠조격", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"6 63735 0 63735 0", "+ 68026 68026", "+ 72316 72316", "+ 76605 76605", "+ 80895 80895", "+ 85185 85185", "+ 89478 89478", "+ 93766 93766",
				"+ 98056 98056", "+ 102347 102347", "+ 106637 106637", "+ 110926 110926",null,
				"오망성진", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"6 60067 0 60067 0", "+ 64117 64117", "+ 68147 68147", "+ 72197 72197", "+ 76247 76247", "+ 80277 80277", "+ 84327 84327", "+ 88377 88377", "+ 92407 92407",
				"+ 96457 96457", "+ 100507 100507", "+ 104537 104537", null,
				"참격", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"11 37405 0 37405 0", "+ 39289 39289", "+ 41171 41171", "+ 43057 43057", "+ 44939 44939", "+ 46824 46824", "+ 48707 48707", "+ 50592 50592", "+ 52478 52478", "+ 54359 54359", null,
				"무쌍격", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"23 25637 0 25637 0", "+ 26443 26443", "+ 27247 27247","+ 28052 28052","+ 28857 28857","+ 29662 29662","+ 30466 30466","+ 31271 31271","+ 32076 32076","+ 32881 32881",
				"+ 33686 33686", "+ 34490 34490", "+ 35295 35925", "+ 36100 36100", "+ 36905 36905", null,
				"질풍타", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"38 5524 0 5524 0", "+ 5642 5642",  "+ 5760 5760", "+ 5878 5878", "+ 5996 5996", "+ 6114 6114", "+ 6232 6232", "+ 6350 6350", "+ 6468 6468", "+ 6586 6586", null,
				"성불", Skill_type.DAMAGE_BUF, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"38 4026 0 4026 0", "%마방깍_스킬 5", "+ 4110 4110", "%마방깍_스킬 5", "+ 4194 4194", "%마방깍_스킬 5", "+ 4284 4284", "%마방깍_스킬 5", "+ 4368 4368", "%마방깍_스킬 5", "+ 4452 4452", "%마방깍_스킬 5",
				"+ 4542 4542", "%마방깍_스킬 5", "+ 4626 4626", "%마방깍_스킬 5", "+ 4710 4710", "%마방깍_스킬 5", "+ 4794 4794", "%마방깍_스킬 5", "+ 4884 4884", "%마방깍_스킬 5", null,
				"낙봉추", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"38 1271 0 1271 0", "+ 1271+28 1271+28", null,
				"단층지격", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"36 4584 0 4584 0", "+ 4686 4686", "+ 4788 4788", "+ 4890 4890", "+ 4992 4992", "+ 5095 5095", "+ 5197 5197", "+ 5299 5299", "+ 5401 5401", "+ 5503 5503", "+ 5606 5606", null,
				"승천진", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"36 6150 0 6150 0", "+ 6287 6287", "+ 6424 6424", "+ 6560 6560", "+ 6698 6698", "+ 6835 6835", "+ 6972 6972", "+ 7109 7109", "+ 7247 7247", "+ 7384 7384", "+ 7520 7520", null,
				"어둠 가르기", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"33 7757 0 7757 0", "+ 7942 7942", "+ 8128 8128", "+ 8314 8314", "+ 8498 8498", "+ 8684 8684", "+ 8869 8869", "+ 9055 9055", "+ 9240 9240", "+ 9425 9425", "+ 9611 9611", null,
				"주작부", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"33 16784 0 16784 0", "+ 17186 17186", "+ 17586 17586", "+ 17988 17988", "+ 18388 18388", "+ 18790 18790", "+ 19190 19190", "+ 19592 19592", "+ 19992 19992", "+ 20394 20394", null,
				"저 하늘의 별", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"33 17658 0 17658 0", "+ 18080 18080", "+ 18501 18501", "+ 18923 18923", "+ 19345 19345", "+ 19767 19767", "+ 20189 20189", "+ 20611 20611", "+ 21033 21033", "+ 20611 20611", null,
				"거선풍", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"31 7607 0 7607 0", "+ 7798 7798", "+ 7989 7989", "+ 8180 8180", "+ 8371 8371", "+ 8563 8563", "+ 8754 8754", "+ 8945 8945", "+ 9136 9136", "+ 9327 9327", "+ 9518 9518", null,
				"낙뢰부", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 1:1 상황에서의 딜",
				"28 25768 0 25768 0", "+ 26464 26464", "+ 27168 27168", "+ 27864 27864", "+ 28560 28560", "+ 29264 29264", "+ 29960 29960", "+ 30664 30664", "+ 31360 31360", "+ 32056 32056", null,
				"난격", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"28 20941 0 20941 0", "+ 21509 21509", "+ 22077 22077", "+ 22645 22645", "+ 23213 23213", "+ 23781 23781", "+ 24349 24349", "+ 24917 24917", "+ 25486 25486", "+ 26054 26054", null,
				"대회전격", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"26 19899 0 19899 0", "+ 20470 20470", "+ 21040 21040", "+ 21611 21611", "+ 22182 22182", "+ 22753 22753", "+ 23324 23324", "+ 23895 23895", "+ 24466 24466", "+ 25037 25037", null,
				"멸혼부", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_f, "설명 1:1 상황에서의 딜",
				"16 29862 0 29862 0", "+ 31065 31065", "+ 32265 32265", "+ 33468 33468", "+ 34668 34668", "+ 35871 35871", "+ 37071 37071", "+ 38274 38274", "+ 39474 39474", "+ 40677 40677", null,
				"공참타", Skill_type.ACTIVE, "", 1, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_f,
				"10 180 0.2 180 0.2", "귀속/잠룡/100", "+ 190 0.21 190 0.21", "귀속/잠룡/100", "+ 200 0.22 200 0.22", "귀속/잠룡/100", "+ 210 0.23 210 0.23", "귀속/잠룡/100", null,
			
				/////패시브
				"광명의 챠크라", Skill_type.SWITCHING, Job.EXORCIST, 20, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"20", "지능 1747", null,
				"열정의 챠크라", Skill_type.SWITCHING, "", 20, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"20", "힘 1832", null,
				"컨버전", Skill_type.OPTION,  "", 20, 1, 1, 1, CalculatorVersion.VER_1_1_b,  "설명 옵션을 선택하면 마법(광챠 적용), 선택하지 않으면 물리(열챠 적용) 컨버전",
				"1", "fStat_컨버전-퇴마", null,
				"거병 마스터리", Skill_type.PASSIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"20", "물리마스터리 30 & 마법마스터리 30", "+", "물리마스터리 +1 & 마법마스터리 +1", "반복 1",
				"투기 발산", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_f,
				"15", "증뎀버프 22", "+", "증뎀버프 +1", "반복 1",
				"신선의 경지", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"6", "물리마스터리 20 & 마법마스터리 20 & 물크 20 & 마크 20", 
				"+", "물리마스터리 +2 & 마법마스터리 +2 & 물크 +2 & 마크 +2", "반복 1",
				"신의 챠크라", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_f,
				"6", "증뎀버프 34 & %물방깍_스킬 3", "+", "증뎀버프 +2 & %물방깍_스킬 3", "반복 1",
				"잠룡", Skill_type.DAMAGE_PASSIVE, "", 15, 11, 1, 5, CalculatorVersion.VER_1_0_f,
				"1 200 0 200 0", "스킬 공참타 % 40 & 스킬 거선풍 % 76.34 & 스킬 단층지격 % 30 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % 30 & %물방깍_스킬 5",
				"+ 208 208", "스킬 공참타 % 42 & 스킬 거선풍 % 84.85 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 220 220", "스킬 공참타 % 45 & 스킬 거선풍 % 89.83 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 228 228", "스킬 공참타 % 47 & 스킬 거선풍 % 97.67 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 240 240", "스킬 공참타 % 50 & 스킬 거선풍 % 102.73 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 248 248", "스킬 공참타 % 52 & 스킬 거선풍 % 110.80 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 260 260", "스킬 공참타 % 55 & 스킬 거선풍 % 115.94 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 268 268", "스킬 공참타 % 57 & 스킬 거선풍 % 125.29 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 280 280", "스킬 공참타 % 60 & 스킬 거선풍 % 130.53 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 288 288", "스킬 공참타 % 62 & 스킬 거선풍 % 139.08 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5",
				"+ 300 300", "스킬 공참타 % 65 & 스킬 거선풍 % 144.39 & 스킬 단층지격 % +3 & 스킬 대회전격 % 28.57 & 스킬 무쌍격 % +3 & %물방깍_스킬 5", null,
				"지의 식신 - 현무", Skill_type.SWITCHING, "", 40, 20, 10, 3, CalculatorVersion.VER_1_0_f,
				"10", "%물방깍_스킬 36.3 & %마방깍_스킬 36.3", null,
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_1_b,
				"90", "스킬 공참타 % 512.2 & fStat_기숙", "+", "스킬 공참타 % 518 & fStat_기숙", "+", "스킬 공참타 % 523.7 & fStat_기숙", "+", "스킬 공참타 % 529.5 & fStat_기숙",
				"+", "스킬 공참타 % 535.2 & fStat_기숙", "+", "스킬 공참타 % 541 & fStat_기숙", "+", "스킬 공참타 % 546.7 & fStat_기숙", "+", "스킬 공참타 % 552.5 & fStat_기숙", "+",
				"스킬 공참타 % 558.2 & fStat_기숙", "+", "스킬 공참타 % 564 & fStat_기숙", "+", "스킬 공참타 % 518 & fStat_기숙", "+", "스킬 공참타 % 569.7 & fStat_기숙", null,
				"공의 식신 - 백호", Skill_type.BUF_ACTIVE, "", 30, 60, 50, 2, CalculatorVersion.VER_1_0_f, "설명 데미지 측정 포기ㅜ",
				"31", "%마방깍_스킬 3", "32", "%마방깍_스킬 3", null,
				
				/////TP
				"공참타 강화", "공참타", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"낙봉추 강화", "낙봉추", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"승천진 강화", "승천진", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_f, null,
				"단층지격 강화", "단층지격", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"질풍타 강화", "질풍타", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"거선풍 강화", "거선풍", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"성불 강화", "성불", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"저 하늘의 별 강화", "저 하늘의 별", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"어둠 가르기 강화", "어둠 가르기", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"주작부 강화", "주작부", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"난격 강화", "난격", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"대회전격 강화", "대회전격", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"식신의 군 강화", "식신의 군", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"무쌍격 강화", "무쌍격", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_f, null,
				"낙뢰부 강화", "낙뢰부", "", 65, 7, 5, -1, CalculatorVersion.VER_1_0_f,
				"1", "스킬 낙뢰부 % 21.5", "+", "스킬 낙뢰부 % 45", "+", "스킬 낙뢰부 % 55", "+", "스킬 낙뢰부 % 81.5",
				"+", "스킬 낙뢰부 % 92.5", "+", "스킬 낙뢰부 % 122", "+", "스킬 낙뢰부 % 153.5", null,
		};
		
		return data;
	}
	
	public static Object[] skillInfo_fighter()
	{
		Object[] data = new Object[] {
				////////////패황
				/////액티브
				"로킥", Skill_type.ACTIVE, Job.STRIKER_M, 5, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"43 2404 7.87 0 0", "+ 2449 8.02", "+ 2495 8.17" ,"+ 2543 8.33", null,
				"본 크러셔", "", "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"33 9772 0 0 0", "36 10472", "37 10706", "38 10939", null, 
				"철산고", "", "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"33 8497 0 0 0", "+ 8700", "+ 8903", "36 9106", "37 9309", "38 9512", null, 
				"클로즈 킥", "", "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"28 18037 0 0 0", "+ 18526", "+ 19015", "31 19506", "32 19995", "33 20484", null, 
				"홍염 질풍각", "", "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"28 21496 0 0 0", "+ 22078", "+ 22660", "31 23242", "32 23824", "33 24406", null, 
				"라이트닝 댄스", "", "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"26 17546 0 0 0", "+ 18049", "28 18553", "29 19057", "30 19560", null, 
				"헥토파스칼 킥", Skill_type.ACTIVE_NOMARK, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"23 33921 0 0 0", "+ 34985", "+ 36049", "26 37115", "27 38180", "28 39245", "29 40310", "30 41374", "31 42439", null,
				"헥토파스칼 킥(딜표)", Skill_type.ACTIVE, "", 45, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"1", "귀속/헥토파스칼 킥/100 & 귀속/이중개방/100", null,
				"연환퇴", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_1_h,
				"16 20406*4/3 0 0 0", "+ 19479*4/3", "18 22050*4/3", "19 22869*4/3", "20 23691*4/3", "21 24513*4/3", null, 
				"스핀 킥", "", "", 70, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"11 32559 0 0 0", "+ 34198", "13 35837", "14 37478", "15 39115", "16 40757", null, 
				"낙화", "", "", 75, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"8 47920 0 0 0", "+ 50762", "+ 53604", "11 56448", "12 59291", "13 62134", "14 64977", "15 67819", null, 
				"아토믹 캐넌", Skill_type.ACTIVE_NOMARK, "", 80, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"6 51815 0 0 0", "+ 55302", "8 58790", "9 62278", "10 65766", "11 69253", "12 72741", null,
				"아토믹 캐넌(딜표)", Skill_type.ACTIVE, "", 80, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"1", "귀속/아토믹 캐넌/100 & 귀속/이중개방/100", null,
				"패황연격", "", "", 85, 40, 30, 5,  Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"2 69691 0 0 0", "3 82809", "4 95927", "5 109046", null, 
				
				/////패시브
				"강권", Skill_type.SWITCHING, "", 15, 30, 20, 3, CalculatorVersion.VER_1_1_d,
				"20", "힘 812 & 크증버프 56.25", null,
				"머슬 시프트", Skill_type.BUF_ACTIVE, "", 30, 15, 5, 5, CalculatorVersion.VER_1_1_d,
				"5", "증뎀버프 20", "+", "증뎀버프 20", "+", "증뎀버프 20", "+", "증뎀버프 20", "+", "증뎀버프 20",  null,
				"화염의 각", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 5, CalculatorVersion.VER_1_1_d,
				"9", "증뎀버프 34", "+", "증뎀버프 +4", "반복 1",
				"이중개방", Skill_type.OPTION, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"9 50100 0 0 0", "fStat_이중개방", "+ 54170", "fStat_이중개방", "+ 58241", "fStat_이중개방", "+ 62309", "fStat_이중개방", "+ 66378", "fStat_이중개방",
				"+ 70446", "fStat_이중개방", "+ 74517", "fStat_이중개방", "+ 78586", "fStat_이중개방", "+ 82654", "fStat_이중개방", "+ 86724", "fStat_이중개방", null,
				"효율적인 연소", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_d,
				"15", "물리마스터리 19", "+", "물리마스터리 +1", "반복 1",
				"화력개방", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_e,
				"6", "증뎀버프 29", "+", "증뎀버프 +2", "반복 1",
				"급소 지정", Skill_type.PASSIVE, "", 30, 30, 20, 3, CalculatorVersion.VER_1_1_d,
				"20", "물크 26", "+", "물크 +1", "반복 1",
				
				"로킥 강화", "로킥", "", 50, 7, 5, 8, CalculatorVersion.VER_1_1_d, null,
				"본 크러셔 강화", "본 크러셔", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"철산고 강화", "철산고", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"홍염 질풍각 강화", "홍염 질풍각", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"클로즈 킥 강화", "클로즈 킥", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"라이트닝 댄스 강화", "라이트닝 댄스", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"헥토파스칼 킥 강화", "헥토파스칼 킥", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,

				
				////////////염제
				"환영폭쇄", Skill_type.ACTIVE, Job.NENMASTER_F, 25, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_2_b,"설명 분신tp5기준",
				"36 0 0 0 13.5","+ 13.8" ,"+ 14.1", "+ 14.4", "+ 14.71", "+ 15", "+ 15.31", null, 				
				"사자후", Skill_type.ACTIVE, Job.NENMASTER_F, 35, 60, 50, 2, Element_type.ALL, CalculatorVersion.VER_1_2_b,
				"28 0 0 13090 0","+ 13445" ,"+ 13800", "+ 14155", "+ 14510", "+ 14865", "+ 15220","+ 14865", "+ 15220","+ 15576", "+ 15931", null, 
				"기공장", "", "", 40, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_2_b,
				"26 0 0 2480*7 0", "+ 2554*7", "+ 2626*7", "+ 2699*7", "+ 2773*7", "+ 2846*7", "+ 2919*7", "+ 2992*7", "+ 3065*7", null, 
				"광충노도", Skill_type.ACTIVE , "", 45, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_2_b, "설명 광충풀히트기준 추측치. 오퍼레이션 히트분석급 논문 존재시 첨부 부탁",
				"28 0 0 0 548.76*1.5", "+ 564.10*1.5", "+ 579.03*1.5", null, 
				"광충 즉폭", Skill_type.OPTION, "", 85, 1, 1, 1, Element_type.LIGHT, CalculatorVersion.VER_1_2_b, "설명 완충데미지는 옵션키세요 완충노충 사이 퍼뎀은 몰라서 생략",				
				"1", "스킬 광충노도 % -100/3", null,
				"넨화", Skill_type.DAMAGE_BUF, "", 50, 40, 30, 5, Element_type.LIGHT, CalculatorVersion.VER_1_2_b,
				"10 0 0 26432*5 0", "명속깍 40", "+ 28420*5", "명속깍 40","+ 30413*5", "명속깍 40", "+ 32047*5", "명속깍 45", 
				"+ 34395*5", "명속깍 45",null,
				"광룡십삼장", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.LIGHT, CalculatorVersion.VER_1_2_b,
				"15 0 0 3753*6+15012 0", "+ 3917*6+15669", "+ 4082*6+16326", "+ 4245*6+16982", "+ 4410*6+17639", "+ 4574*6+18296", null, 
				"태사자후",  Skill_type.DAMAGE_BUF, "", 70, 40, 30, 2,  Element_type.LIGHT, CalculatorVersion.VER_1_2_b, "설명 명속성 추가 데미지 포함",
				"10 0 0 11540+1080*2 115.4+10.8*2", "마크 10", "+ 12152+1160*2 121.52+11.6*2",  "마크 10", "+ 12765+1240*2 127.65+12.4*2",  "마크 10", 
				"+ 13376+1310*2 133.76+13.1*2",  "마크 10", "+ 13988+1390*2 139.88+13.9*2",  "마크 10", "+ 14600+1470*2 146+14.7*2", "마크 10", 
				"16 15212+1550*2 152.12+15.5*2",  "마크 10", "17 15824+1630*2 158.24+16.3*2", "마크 10", null, 
				"세인트 일루전", Skill_type.ACTIVE, "", 75, 40, 30, 2,  Element_type.LIGHT, CalculatorVersion.VER_1_2_b,
				"9 0 0 15517*4 0", "+ 16386*4", "+ 17254*4", "+ 18124*4", "+ 18993*4", "+ 19861*4 ", "+ 20730*4", null, 
				"흡기공탄", Skill_type.DAMAGE_BUF, "", 80, 40, 30, 2,  Element_type.LIGHT, CalculatorVersion.VER_1_2_b,"설명 3단계 공격력",
				"6 0 0 13961 0", "증뎀버프 27", "+ 14901", "증뎀버프 28.5","+ 15840","증뎀버프 30","+ 16779","증뎀버프 31.5", "+ 17719*5","증뎀버프 33", "+ 18659*5", "증뎀버프 34.5","+ 19598*5", "증뎀버프 36",null,
				"광휘의 넨", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_2_b, 
				"3 0 0 120952*1.165 0", "+ 140113*1.165", "+ 159274*1.165", "+ 178434*1.165", "+ 197595*1.165", "+ 216756*1.165", null, //노충기준 풀충옵션

				//////패시브
				"분신", Skill_type.PASSIVE, Job.NENMASTER_F, 5, 22, 10, 2, CalculatorVersion.VER_1_2_b,
				"10","스킬 환영폭쇄 % 1500", "+", "스킬 환영폭쇄 % +100", "반복 1",
				"축기", Skill_type.BUF_ACTIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"7", "스킬 기공장 % 45 & 스킬 사자후 % 45 & 스킬 태사자후 % 45 & 스킬 세인트 일루전 % 45 & 스킬 흡기공탄 % 45 & 스킬 광휘의 넨 % 45 & 스킬 광충노도 % 30", 
				"+", "스킬 기공장 % +3 & 스킬 사자후 % +3 & 스킬 태사자후 % +3 & 스킬 세인트 일루전 % +3 & 스킬 흡기공탄 % +3 & 스킬 광휘의 넨 % +3 & 스킬 광충노도 % +0",
				"반복 1",
				"흩날리는 천염화", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"15", "마크 14 & 크증버프 28",
				"+", "마크 +0.5 & 크증버프 +1", "반복 1",
				"카이", Skill_type.SWITCHING, "", 15, 30, 20, 3, CalculatorVersion.VER_1_2_b,
				"20", "물리마스터리 43 & 마법마스터리 43 & 독공마스터리 43", null,
				"태양의 커튼", Skill_type.SWITCHING, "", 20, 20, 10, 3, CalculatorVersion.VER_1_2_b,
				"10", "명속강 98", null,
				"나선의 넨", Skill_type.BUF_ACTIVE, "", 30, 60, 50, 2, CalculatorVersion.VER_1_2_b,
				"32", "스킬 기공장 % 54",
				"33", "스킬 기공장 % 54",
				"34", "스킬 기공장 % 56",
				"35", "스킬 기공장 % 56",
				"36", "스킬 기공장 % 56",
				"37", "스킬 기공장 % 58", null,
				"뇌명: 사나운 빛의 넨수", Skill_type.BUF_ACTIVE, "", 40, 60, 50, 2,CalculatorVersion.VER_1_2_b,
				"25", "스킬 환영폭쇄 % 30 & 스킬 사자후 % 30 & 스킬 광충노도 % 30 & 스킬 태사자후 % 30 & 스킬 넨화 % 30 & 스킬 광룡십삼장 % 30",
				"+", "스킬 환영폭쇄 % +1 & 스킬 사자후 % +1 & 스킬 광충노도 % +1 & 스킬 태사자후 % +1 & 스킬 넨화 % +1 & 스킬 광룡십삼장 % +1", "반복 1",
				
				"환영폭쇄 강화", "환영폭쇄", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"사자후 강화", "사자후", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"기공장 강화", "기공장", "", 65, 7, 5, -1, CalculatorVersion.VER_1_2_b,
				"1", "스킬 기공장 % 14.28", "+", "스킬 기공장 % 28.56", "+", "스킬 기공장 % 42.84", "+", "스킬 기공장 % 57.14", "+", "스킬 기공장 % 71.42",
				"+", "스킬 기공장 % 85.71", "+", "스킬 기공장 % 100", null,
				"광충노도 강화", "광충노도", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,				
				////////////그마
				/////액티브
				"무릎찍기", Skill_type.ACTIVE, Character_type.FIGHTER_M, 5, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"46 3651 73.5 0 0", "+ 3718 84.86 ", "+ 3796 86.66" ,"+ 3859 88.07", "+ 3935 89.75", null,
				"로플링", Skill_type.ACTIVE, Job.GRAPPLER_M, 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"41 4042 46.49 0 0", "+ 4123 47.39", "+ 4205 48.36" ,"+ 4285 49.29", "+ 4367 50.21", null,
				"넥스넵", Skill_type.DAMAGE_BUF, Job.GRAPPLER_M, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"38 5421 0 0 0", "크리저항감소 75.5", "+ 5537",  "크리저항감소 77", "+ 5653", "크리저항감소 78.5",
				"+ 5768", "크리저항감소 80", "+ 5884",  "크리저항감소 81.5",null,
				"숄더태클", Skill_type.ACTIVE, Job.GRAPPLER_M, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"36 10740 64.44 0 0", "+ 10982 65.89", "+ 11220 67.33" ,"+ 11463 68.75", "+ 11703 70.20", null,
				"브레이크 다운", Skill_type.ACTIVE, Job.GRAPPLER_M, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"36 10910 0 0 0", "+ 11154", "+ 11398" ,"+ 11640", "+ 11883", null,
				"에어슈타이너", Skill_type.ACTIVE, Job.GRAPPLER_M, 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 8347 0 0 0", "+ 8547 ", "+ 8746 " ,"+ 8945 ", "+ 9146 ", null,
				"스냅 샷", Skill_type.ACTIVE, Job.GRAPPLER_M, 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 12202 0 0 0", "+ 12492 ", "+ 12786 " ,"+ 13079 ", "+ 13372 ", null,
				"블로우 스트라이크", Skill_type.ACTIVE, Job.GRAPPLER_M, 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"31 22164 0 0 0", "+ 22712 ", "+ 23284 " ,"+ 23838 ", "+ 24400 ", null,
				"롤링 스파이크", Skill_type.ACTIVE, Job.GRAPPLER_M, 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"28 17017 102.09 0 0", "+ 17476 104.88", "+ 17940 107.66" ,"+ 18409 110.42", "+ 18873 113.23", null,
				"와일드 캐넌 스파이크", Skill_type.ACTIVE, Job.GRAPPLER_M, 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b, "설명 기본은 건물형",
				"28 42540 0 0 0", "+ 43766 ", "+ 44997 " ,"+ 46218 ", "+ 47541 ", null,
				"와일드 캐넌 스파이크(잡기가능)", Skill_type.OPTION, "", 45, 1, 1, 0, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"1", "스킬 와일드캐넌 스파이크 % -19.13", null,
				"나의 공격리듬은!!", Skill_type.DAMAGE_BUF, Job.GRAPPLER_M, 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_2_b, "설명 기본은 건물형",
				"9 105316 0 0 0", "힘 40", "+ 113243 ","힘 45", "+ 121332 " ,"힘 50","+ 129136 ","힘 55", "+ 137057 ","힘 60", null,
				"나의 공격리듬은!!(잡기가능)", Skill_type.OPTION, "", 50, 1, 1, 0, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"1", "스킬 나의 공격리듬은!! % -9.46", null,
				"피어스", Skill_type.ACTIVE, Job.GRAPPLER_M, 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"18 18605 186.05 0 0", "+ 19298 192.98", "+ 19990 199.90" ,"+ 20683 206.83", "+ 21376 213.76", null,
				"한놈만 팬다!!", Skill_type.ACTIVE, Job.GRAPPLER_M, 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"13 75561 0 0 0", "+ 79203 ", "+ 82845" ,"+ 89957", "+ 89382", null,			
				"흑진광풍", Skill_type.ACTIVE, Job.GRAPPLER_M, 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"11 75561 0 0 0", "+ 79203 ", "+ 82845" ,"+ 89957", "+ 89382", null, // 패치 내용 확인 필요			
				"오의: 질풍노도", Skill_type.ACTIVE, Job.GRAPPLER_M, 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"8 75561 0 0 0", "+ 79203 ", "+ 82845" ,"+ 89957", "+ 89382", null,	
				"일자전승: 극의 진천노호", Skill_type.ACTIVE, Job.GRAPPLER_M, 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"2 72388 0 0 0", "+ 89086", "+ 105869" ,"+ 122639", null,
				/////패시브
				"반드시 잡는다!", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_2_b,
				"10", "증뎀버프 90", null,
				"임팩트 콤비네이션1", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 5, CalculatorVersion.VER_1_2_b,
				"1", "크증버프 30", "+", "크증버프 +1", "반복 1",
				"임팩트 콤비네이션2", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 5, CalculatorVersion.VER_1_2_b,
				"1", "크증버프 25", "+", "크증버프 +1", "반복 1",
				"반격", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"16", "증뎀버프 34.5", "+", "증뎀버프 +1.5", "반복 1",
				"반격(카운터)", Skill_type.BUF_ACTIVE, "", 48, 1, 1, 3, CalculatorVersion.VER_1_2_b,
				"1", "증뎀버프 30", null,		
				"강렬한 테이크다운", Skill_type.PASSIVE, "", 15, 20, 30, 3, CalculatorVersion.VER_1_2_b,
				"10", "독공마스터리 50", "+", "독공마스터리 +5", "반복 1",
				"가장 강한 녀석은 누구냐?(보스)", Skill_type.BUF_ACTIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"7", "물크 26 & 증뎀버프 36", "+", "물크 +2 & 증뎀버프 +1.5", "반복 1",
				"가장 강한 녀석은 누구냐?(네임드)", Skill_type.BUF_ACTIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"7", "물크 26 & 증뎀버프 29", "+", "물크 +2 & 증뎀버프 +2", "+","물크 +2 & 증뎀버프 +1","반복 2",
				
				"로플링 강화", "로플링", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"브레이크다운 강화", "브레이크 다운", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"숄더태클 강화", "숄더태클", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"블로우 스트라이크 강화", "블로우 스트라이크", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,				
				"롤링 스파이크 강화", "롤링 스파이크", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,				
				"와일드 캐넌 스파이크 강화", "와일드 캐넌 스파이크", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,				
				"스냅샷 강화", "스냅샷", "", 55, 7, 5, 13, CalculatorVersion.VER_1_2_b, null,				
				"에어슈타이너 강화", "에어슈타이너", "", 55, 7, 5, 13, CalculatorVersion.VER_1_2_b, null,					
				
				//////////////카이저
				/////액티브

				"본 크러셔", Skill_type.ACTIVE, Job.STRIKER_F, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 11980 0 0 0", "+ 12266", "+ 12552", "+ 12839", "+ 13125", "+ 13411", "+ 13697", "+ 13983", null,
				"철산고", Skill_type.ACTIVE, Job.STRIKER_F, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 11717 0 0 0", "+ 11997", "+ 12276", "+ 12556", "+ 12836", "+ 13116", "+ 13396", "+ 13676", null,
				"원인치 펀치", Skill_type.ACTIVE, Job.STRIKER_F, 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"28 24234 0 0 0", "+ 24892", "+ 25550", "+ 26207", "+ 26865 ", "+ 27522", "+ 28180", "+ 28837", "+ 29495", null,
				"라이징 너클", Skill_type.ACTIVE, Job.STRIKER_F, 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"28 6203*4 0 0 0", "+ 6540*4", "+ 6708*4", "+ 6876*4", "+ 7045*4", "+ 7213*4", "+ 7381*4", "+ 7550*4", null,
				"라이트닝 댄스", Skill_type.ACTIVE, Job.STRIKER_F, 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"26 5256*7 0 0 0", "+ 5406*7", "+ 5557*7", "+ 5708*7", "+ 5859*7", "+ 6009*7", "+ 6160*7", "+ 6311*7", "+ 6462*7", null,
				"비트 드라이브", Skill_type.ACTIVE, Job.STRIKER_F, 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"23 2513*10+20565 0 0 0", "+ 2592*10+21212", "+ 2671*10+21858", "+ 2750*10+22503", "+ 2829*10+23149", "+ 2908*10+23794", "+ 2987*10+24440", "+ 3066*10+20565", "+ 3144*10+25731", "+ 3223*10+26377", null,
				"사상최강의 로킥", Skill_type.ACTIVE, Job.STRIKER_F, 50, 30, 40, 5, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"10 95713 0 0 0", "+ 102903 ", "+ 110092", "+ 117282", "+ 124472 ", "+ 131661", "+ 138851", "+ 146041 ", "+ 153231", null,
				"파쇄권", Skill_type.ACTIVE, Job.STRIKER_F, 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"16 35023 0 0 0", "+ 37841 ", "+ 36432", "+ 37841", "+ 39250 ", "+ 40659",null,
				"호격권", Skill_type.ACTIVE, Job.STRIKER_F, 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"8 2221*15+8021+14191+6170 0 0 0", "+ 2353*15+8497+15033+6536", "+ 2484*15+8972+15875+6902", "+ 2616*15+9448+16716+7268", 
				"+ 2748*15+9924+17558+7634", "+ 2880*15+10400+18400+8000", "+ 3011*15+10876+19242+8366", "+ 3143*15+11352+20084+8732", null,
				"라이징 어퍼", Skill_type.ACTIVE, Job.STRIKER_F, 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"11 7957+6085*6+2340 0 0 0", "+ 8358+6391*6+2458", "+ 8759+6698*6+2576", "+ 9160+7004*6+2694", "+ 9560+7311*6+2812", null,
				"체인디스트럭션", Skill_type.DAMAGE_BUF, Job.STRIKER_F, 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"6 1742+7552+10457+13943+24401 0 0 0", "%물방깍_스킬 6.5", "+ 1860+8061+11161+14882+26044", "%물방깍_스킬 7", "+ 1977+8569+11865+15821+27686","%물방깍_스킬 7.5",
				"+ 2094+9078+12569+16759+29329", "%물방깍_스킬 8", "+ 2212+9586+13273+17698+30971", "%물방깍_스킬 8.5",null,
				"필살패황권", Skill_type.ACTIVE, Job.STRIKER_F, 85, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_2_b,
				"2 7312*19+4237", "+ 8689*3+82544","+ 10065*3+95620","+ 11442*3+108696","+ 12818*3+121773", null,
				/////패시브

				"머슬 시프트", Skill_type.BUF_ACTIVE, "", 30, 5, 5, 5, CalculatorVersion.VER_1_2_b,
				"4", "증뎀버프 20", "5", "증뎀버프 20",null,
				"급소지정", Skill_type.BUF_ACTIVE, "", 30, 30, 20, 3, CalculatorVersion.VER_1_2_b,
				"20", "물크 32", "+", "물크 33.6", "+", "물크 35.2", "+", "물크 36.8", "+", "물크 38.4", "+", "물크 40.0", "+", "물크 41.6", null,
				"스탬피드", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"15", "스증 35", "+", "스증 +1.5", "반복 1",
				"리미트 브레이크", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_2_b,
				"6", "스증 34", "+", "스증 +2", "반복 1",
				"강권", Skill_type.SWITCHING, "", 15, 30, 20, 3,  CalculatorVersion.VER_1_2_b,
				"20", "힘 1567 & 크증버프 56.3", null,

				//TP
				"본 크러셔 강화", "본 크러셔", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"철산고 강화", "철산고", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"원인치 펀치 강화", "원인치 펀치", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"라이트닝 댄스 강화", "라이트닝 댄스", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"비트 드라이브 강화", "비트 드라이브", Job.STRIKER_F, 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null,
				"라이징 너클 강화", "라이징 너클", Job.STRIKER_F, 65, 7, 5, -1, CalculatorVersion.VER_1_2_b, 
				"1", "스킬 라이징 너클 % 5.6", "+", "스킬 라이징 너클 % 14.4", "+", "스킬 라이징 너클 % 23.2", "+", "스킬 라이징 너클 % 32", "+", "스킬 라이징 너클 % 40.8", null,
 

				
		};
		
		return data;
		
		
	}
	
	public static Object[] skillInfo_demonicLancer()
	{
		Object[] data = new Object[] {
				///////////워로드
				/////액티브
				"헤드온 챠지", Skill_type.ACTIVE, Job.VANGUARD, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"36 8501 0 0 0", "+ 8689", "+ 8880", "+ 9071", "+ 9259", "+ 9450", "+ 9639", null,
				"임팩트 스매쉬", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 3, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"22 7239 0 0 0", "힘 302", "+ 7505", "힘 321", "+ 7770", "힘 342", "+ 8036", "힘 363",
				"+ 8301", "힘 384", "+ 8566", "힘 405", "+ 8832", "힘 426", null,
				"브랜디쉬",  Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"33 8778 0 0 0", "+ 8988", "+ 9198", "+ 9408", "+ 9618", "+ 10036", "+ 10246", null,
				"크레센트 슬래쉬", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"31 14559 0 0 0", "+ 14925", "+ 15290", "+ 15656", "+ 16021", "+ 16387", "+ 16752", null,
				"비헤드", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"28 19549 0 0 0", "+ 20078", "+ 20609", "+ 21139", "+ 21670", "+ 22201", "+ 22731", "+ 23262", "+ 23792", null,
				"드레드보어", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"28 24347 0 0 0", "+ 25014", "+ 25668", "+ 26327", "+ 26994", "+ 27649", "+ 28315", "+ 28970", "+ 29363", null,
				"블레이드 스톰", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"26 29174 0 0 0", "+ 30001", "+ 30841", "+ 31667", "+ 32519", "+ 33358", "+ 34185", "+ 35036", "+ 35863", "+ 36702", null,
				"디베스테이트", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"23 39963 0 0 0", "+ 41225", "+ 42479", "+ 43732", "+ 44994", "+ 46241", "+ 47494", "+ 48749", "+ 50010", "+ 51264", null,
				"유 다이드", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_d,
				"10 126582 0 0 0", "+ 136091", "+ 145599", "+ 155108", "+ 164616", "+ 174125", "+ 183633", "+ 193141", "+ 202650",
				"+ 212158", "+ 221668", "9 117073 0 0 0", null,
				"런지 스트라이크", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"16 31516 0 0 0", "+ 32784", "+ 34052", "+ 35321", "+ 36588", "+ 37855", "+ 39225", null,
				"둠 글레이브", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"11 45955 0 0 0", "+ 48270", "+ 50583", "+ 52898", "+ 55213", "+ 57526", "+ 59841", "+ 62156", null,
				"둠 글레이브(충격파)", Skill_type.OPTION, "", 70, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_1_d, "설명 둠 글레이브에 충격파딜을 적용합니다",
				"1", "스킬 둠 글레이브 % -7.17", null,
				"데들리 매서커", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"8 52280 0 0 0", "+ 55384", "+ 58483", "+ 61584", "+ 64687", "+ 70887", "+ 73990", null,
				"버스트 슬래쉬", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_d,
				"6 52186 0 0 0", "+ 55706", "+ 59207", "+ 62716", "+ 66236", "+ 69746", "+ 73266", null,
				"데모닉 인페르노", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"2 119306 0 0 0", "+ 141768", "+ 164224", "+ 186682", "+ 209142", null,
				
				/////패시브
				"마창 제어", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_1_d, "설명 무기마스터리 10+2*n", 
				"10", "물리무기마스터리 30 미늘창", "+", "물리무기마스터리 32 미늘창", "+", "물리무기마스터리 34 미늘창", 
				"+", "물리무기마스터리 36 미늘창", "+", "물리무기마스터리 38 미늘창", "+", "물리무기마스터리 40 미늘창", 
				"+", "물리무기마스터리 42 미늘창", "+", "물리무기마스터리 44 미늘창", "+", "물리무기마스터리 46 미늘창", 
				"+", "물리무기마스터리 48 미늘창", "+", "물리무기마스터리 50 미늘창", null,
				"마창 해방",  Skill_type.SWITCHING, "", 20, 20, 10, 3, CalculatorVersion.VER_1_1_d,
				"10", "물크 30 & 크증버프 60", null,
				"마창 지배", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_d,
				"15", "증뎀버프 33", "+", "증뎀버프 +2", "+", "증뎀버프 +1", "반복 2",
				"마창 각성", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_d,
				"6", "증뎀버프 35", "+", "증뎀버프 +2", "반복 1",
				
				/////TP
				"헤드온 챠지 강화", "헤드온 챠지", "", 50, 7, 5, -1, CalculatorVersion.VER_1_1_d,
				"1", "스킬 헤드온 챠지 % 10", "+", "스킬 헤드온 챠지 % 21", "+", "스킬 헤드온 챠지 % 32", "+", "스킬 헤드온 챠지 % 43",
				"+", "스킬 헤드온 챠지 % 54", "+", "스킬 헤드온 챠지 % 65", "+", "스킬 헤드온 챠지 % 76", null,
				"브랜디쉬 강화", "브랜디쉬", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"크레센트 슬래쉬 강화", "", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"비헤드 강화", "비헤드", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"드레드보어 강화", "드레드보어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"블레이드 스톰 강화", "블레이드 스톰", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"디베스테이트 강화", "디베스테이트", "", 50, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				
				///////////듀란달
				/////액티브
				"스월링 스피어", Skill_type.ACTIVE, Job.DUALIST, 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"38 6962 0 0 0", "+ 7116", "+ 7263", "+ 7409", "+ 7556", null,
				"트윈 펀토", Skill_type.ACTIVE, Job.DUALIST, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"36 14709 0 0 0", "+ 15037", "+ 15365", "+ 15693", "+ 16021", null,
				"그라운드 씨커", Skill_type.ACTIVE, Job.DUALIST , 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"36 9096 0 0 0", "+ 9298", "+ 9501", "+ 9704", "+ 9907", null,
				"라이징 랜서", Skill_type.ACTIVE, Job.DUALIST, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 15871 0 0 0", "+ 16250", "+ 16629", "+ 17009", "+ 17388", null,
				"슈트롬", Skill_type.ACTIVE, Job.DUALIST, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"33 15906 0 0 0", "+ 16280", "+ 16665", "+ 17039", "+ 17424", null,
				"파동연환창", Skill_type.ACTIVE, Job.DUALIST, 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"31 15536 0 0 0", "+ 15925", "+ 16315", "+ 16705", "+ 17095", null,
				"파동연환창 날려버리기", Skill_type.OPTION, "", 30, 1, 1, 1, CalculatorVersion.VER_1_2_b, "설명 파동연환창으로 몹이 다운될경우",
				"1", "스킬 파동연환창 % -3.92", null,
				"공아", Skill_type.ACTIVE, Job.DUALIST, 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"28 23038 0 0 0", "+ 23664", "+ 24288", "+ 24913", "+ 25539", null,
				"메조싸이클론", Skill_type.ACTIVE, Job.DUALIST, 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"26 26055 0 0 0", "+ 26805", "+ 27540", "+ 28290", "+ 29040", null,
				"뇌격점혈섬", Skill_type.ACTIVE, Job.DUALIST, 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"23 66102 0 0 0", "+ 68182", "+ 70262", "+ 72324", "+ 74405", null,
				"오러 포스 재블린", Skill_type.ACTIVE, Job.DUALIST, 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"9 94610 0 0 0", "귀속/라이징 랜서/20", "+ 102292", "귀속/라이징 랜서/20", null,
				"롤링벌컨", Skill_type.ACTIVE, Job.DUALIST, 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"16 54700 0 0 0", "+ 56900", "+ 59120", "+ 61320", "+ 63520", null,
				"삼일참월", Skill_type.ACTIVE, Job.DUALIST, 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"11 44762 0 0 0", "+ 47012", "+ 49270", "+ 51528", "+ 53778", null,
				"쿼드러플 드라이브", Skill_type.ACTIVE, Job.DUALIST, 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"8 59824 0 0 0", "+ 63372", "+ 66920", "+ 70470", "+ 74018", null,
				"미스트 플레어", Skill_type.ACTIVE, Job.DUALIST, 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"6 52076 0 0 0", "+ 55581", "+ 59086", "+ 62591", "+ 66096", null,
				"살극쌍연무", Skill_type.ACTIVE, Job.DUALIST, 85, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_2_b,
				"2 108543 0 0 0", "+ 128974", null,
				
				/////패시브
				"장창 숙련", Skill_type.PASSIVE, "", 15, 40, 30, 3, CalculatorVersion.VER_1_2_b, 
				"26", "물리무기마스터리 25 장창", "+", "물리무기마스터리 26 장창", "+", "물리무기마스터리 27 장창", null,
				"오러 랜스",  Skill_type.SWITCHING, "", 20, 20, 10, 3, CalculatorVersion.VER_1_2_b,
				"10", "증뎀버프 70", null,
				"미라지 스트림", Skill_type.PASSIVE, "", 48, 40, 30, 3,CalculatorVersion.VER_1_2_b,
				"15", "스킬 스월링 스피어 % 36.5 & 스킬 그라운드 씨커 % 36.5 & 스킬 라이징 랜서 % 35.5 & 스킬 트윈 펀토 % 34 & 스킬 슈트롬 % 34 & 스킬 파동연환창 % 34 & 스킬 공아 % 34 & 스킬 메조싸이클론 % 34 & 스킬 뇌격점혈섬 % 34 & 스킬 오러 포스 재블린 % 34 & 스킬 롤링벌컨 % 34 & 스킬 삼일참월 % 34 & 스킬 쿼드러플 드라이브 % 34 & 스킬 미스트 플레어 % 34 & 스킬 살극쌍연무 % 34",
				"+", "스킬 스월링 스피어 % +1.5 & 스킬 그라운드 씨커 % +1.5 & 스킬 라이징 랜서 % +1.5 & 스킬 트윈 펀토 % +1.5 & 스킬 슈트롬 % +1.5 & 스킬 파동연환창 % +1.5 & 스킬 공아 % +1.5 & 스킬 메조싸이클론 % +1.5 & 스킬 뇌격점혈섬 % +1.5 & 스킬 오러 포스 재블린 % +1.5 & 스킬 롤링벌컨 % +1.5 & 스킬 삼일참월 % +1.5 & 스킬 쿼드러플 드라이브 % +1.5 & 스킬 미스트 플레어 % +1.5 & 스킬 살극쌍연무 % +1.5",
				"반복 1",
				"무형의 창술사", Skill_type.PASSIVE, "", 75, 40, 30, 2,CalculatorVersion.VER_1_2_b,
				"6", "스킬 스월링 스피어 % 35 & 스킬 그라운드 씨커 % 35 & 스킬 라이징 랜서 % 35 & 스킬 트윈 펀토 % 35 & 스킬 슈트롬 % 34 & 스킬 파동연환창 % 34 & 스킬 공아 % 34 & 스킬 메조싸이클론 % 34 & 스킬 뇌격점혈섬 % 34 & 스킬 오러 포스 재블린 % 34 & 스킬 롤링벌컨 % 34 & 스킬 삼일참월 % 34 & 스킬 쿼드러플 드라이브 % 34 & 스킬 미스트 플레어 % 34 & 스킬 살극쌍연무 % 34",
				"+", "스킬 스월링 스피어 % +2 & 스킬 그라운드 씨커 % +2 & 스킬 라이징 랜서 % +2 & 스킬 트윈 펀토 % +2 & 스킬 슈트롬 % +2 & 스킬 파동연환창 % +2 & 스킬 공아 % +2 & 스킬 메조싸이클론 % +2 & 스킬 뇌격점혈섬 % +2 & 스킬 오러 포스 재블린 % +2 & 스킬 롤링벌컨 % +2 & 스킬 삼일참월 % +2 & 스킬 쿼드러플 드라이브 % +2 & 스킬 미스트 플레어 % +2 & 스킬 살극쌍연무 % +2",
				"반복 1",
				/////tp
				"미라지 어택 강화", "스월링 스피어 & 그라운드 씨커 & 라이징 랜서", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"트윈 펀토 강화", "트윈펀토", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"파동연환창 강화", "파동연환창", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"메조싸이클론 강화", "메조싸이클론", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"슈트롬 강화", "슈트롬", "", 55, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"공아 강화", "공아", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 
				"뇌격점혈섬 강화", "뇌격점혈섬", "", 65, 7, 5, 10, CalculatorVersion.VER_1_2_b, null, 

		};
		return data;
	}
	
	public static Object[] skillInfo_knight()
	{
		Object[] data = new Object[] {
				//////////엘븐
				/////액티브
				"무장 해제", Skill_type.ACTIVE, Job.ELVENKNIGHT, 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"38 7186 0 0 0", "39 7340", "40 7505", "41 7667", "42 7825", null,
				"압도", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"33 6154 0 0 0", "%물방깍_스킬 7.3", "34 6301", "%물방깍_스킬 7.4", "35 6448", "%물방깍_스킬 7.5",
				"36 6595", "%물방깍_스킬 7.6", "37 6742", "%물방깍_스킬 7.7", null,
				"런지 에볼루션", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"26 7100*2+7640 0 0 0", "27 7303*2+7860", "28 7507*2+8080", "29 7711*2+8300", null,
				"분쇄", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"23 28903 0 0 0", "24 29810", "25 30717", "26 31625", "27 32533", "28 33440", null,
				"생명의 전조", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d, 
				"16 20319 0 0 0", "17 21137", "18 21955", "19 22772", null,
				"신뢰의 돌파", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d, 
				"11 26495 0 0 0", "12 27830", "13 29164", "14 30495",  null,
				"체인 스트라이크", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d, 
				"8 42419 0 0 0", "9 44936", "10 47452", "11 49969", "12 52485", "13 55001", "14 57518", "15 60034", "16 62551", null,
				"자연의 분노", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"6 38158 0 0 0", "7 40725", "8 43298", "9 45865", "10 48431", null,
				"천마 섬광", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_d,
				"10 86097 0 0 0", "11 92565", "12 99033", "9 79629 0 0 0", null,
				"트와일라잇 유니콘", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_d,
				"1 60835 0 0 0", "2 79373", "3 94312", "4 102594", "5 116621", "6 130654", null,
				
				/////패시브
				"용기의 방패", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_d,
				"6", "증뎀버프 30", "+", "증뎀버프 +2", "반복 1",
				"강인한 신념", Skill_type.PASSIVE, "", 15, 20, 10, 5, CalculatorVersion.VER_1_1_d,
				"10", "물리마스터리 30", "+", "물리마스터리 +1", "반복 1",
				"대자연의 가호", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_d,
				"15", "증뎀버프 30 & 스킬 천마 섬광 % 31.5 & 스킬 트와일라잇 유니콘 % 31.5",
				"+", "증뎀버프 +2 & 스킬 천마 섬광 % +0.5 & 스킬 트와일라잇 유니콘 % +0.5",
				"+", "증뎀버프 +1 & 스킬 천마 섬광 % +0.5 & 스킬 트와일라잇 유니콘 % +0.5", "반복 2",
				"워크라이", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_1_d,
				"10", "증뎀버프 75", null,
				"체인러쉬", Skill_type.PASSIVE, "", 30, 16, 6, 5, CalculatorVersion.VER_1_1_d, 
				"6", "", "7", "", null,
				"체인러쉬-체인수", Skill_type.INPUT, "", 30, 1, 1, 5, CalculatorVersion.VER_1_1_f, "설명 체인러쉬에 사용될 체인수를 입력해주세요",
				"1", "횟수(재료수) 6 & fStat_체인러쉬", null,
				
				/////TP
				"무장 해제 강화", "무장 해제", "", 50, 7, 5, 8, CalculatorVersion.VER_1_1_d, null,
				"압도 강화", "압도", "", 60, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"런지 에볼루션 강화", "런지 에볼루션", "", 60, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
				"분쇄 강화", "분쇄", "", 60, 7, 5, 10, CalculatorVersion.VER_1_1_d, null,
		};
		return data;
	}
	
	public static Object[] skillInfo_thief()
	{
		Object[] data = new Object[] {
				/////////////로그
				/////액티브
				"밴디트컷", Skill_type.ACTIVE, Job.ROUGE, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"36 7186 0 0 0", "귀속/히트엔드(딜)/1334", "+ 6824", "귀속/히트엔드(딜)/1362", "+ 6973", "귀속/히트엔드(딜)/1394", 
				"+ 7122", "귀속/히트엔드(딜)/1422", "+ 7270", "귀속/히트엔드(딜)/1454", null,
				"체인소", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"33 8982 0 0 0", "귀속/히트엔드(딜)/5388", "+ 9192", "귀속/히트엔드(딜)/5517", "+ 9408", "귀속/히트엔드(딜)/5646", 
				"+ 9621", "귀속/히트엔드(딜)/5775", "+ 9837", "귀속/히트엔드(딜)/5904", "+ 10050", "귀속/히트엔드(딜)/6033", null,
				"허리케인", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"31 13620 0 0 0", "귀속/히트엔드(딜)/2180", "+ 13960", "귀속/히트엔드(딜)/2234", "+ 14300", "귀속/히트엔드(딜)/2289", 
				"+ 14650", "귀속/히트엔드(딜)/2344", "+ 14990", "귀속/히트엔드(딜)/2398", null,
				"버티컬 스파이럴", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"29 15436 0 0 0", "귀속/히트엔드(딜)/3087", "+ 15845", "귀속/히트엔드(딜)/3169", "+ 16254", "귀속/히트엔드(딜)/3251", 
				"+ 16656", "귀속/히트엔드(딜)/3332", "+ 17065", "귀속/히트엔드(딜)/3414", "28 15027 0 0 0", "귀속/히트엔드(딜)/3006", null,
				"라이트닝 애로우", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"29 22728 0 0 0", "귀속/히트엔드(딜)/4545", "+ 23330", "귀속/히트엔드(딜)/4666", "+ 23930", "귀속/히트엔드(딜)/4786", 
				"+ 24530", "귀속/히트엔드(딜)/4906", "+ 25130", "귀속/히트엔드(딜)/5206", "28 22128 0 0 0", "귀속/히트엔드(딜)/4425", null,
				"소닉 어설트", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"26 30932 0 0 0", "귀속/히트엔드(딜)/6578", "+ 31817", "귀속/히트엔드(딜)/6767", "+ 32712", "귀속/히트엔드(딜)/6955", 
				"+ 33596", "귀속/히트엔드(딜)/7144", "+ 34481", "귀속/히트엔드(딜)/7333", null,
				"엑셀 스트라이크", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"23 30249 0 0 0", "귀속/히트엔드(딜)/6050", "+ 31198", "귀속/히트엔드(딜)/6240", "+ 32148", "귀속/히트엔드(딜)/6429", 
				"+ 33097", "귀속/히트엔드(딜)/6619", "+ 34048", "귀속/히트엔드(딜)/6809", "+ 34996", "귀속/히트엔드(딜)/6999", null,
				"실버스트림", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"9 88360 0 0 0", "+ 95535", "+ 102710", "+ 109885", "+ 117065", "+ 124240", null,
				"실버스트림(버프)", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 5, CalculatorVersion.VER_1_1_f,
				"9", "힘 510 & 크증버프 52.0875", "+", "힘 561 & 크증버프 52.0875", "+", "힘 612 & 크증버프 52.0875", "+", "힘 664 & 크증버프 52.0875",
				"+", "힘 706 & 크증버프 52.0875", "+", "힘 748 & 크증버프 52.0875", null,
				"실버스트림(스위칭)", Skill_type.INPUT, "", 50, 1, 1, 5, CalculatorVersion.VER_1_1_f, "설명 스위칭시의 스킬 레벨을 입력하세요", "설명 아이콘 비활성화시 실시간으로 적용",
				"1", "횟수(재료수) 13 & fStat_실버스트림", null,
				"체인소 러시", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"16 25442 0 0 0", "귀속/히트엔드(딜)/5085", "+ 26466", "귀속/히트엔드(딜)/5290", "+ 27488", "귀속/히트엔드(딜)/5495", 
				"+ 28513", "귀속/히트엔드(딜)/5700", "+ 29538", "귀속/히트엔드(딜)/5905", null,
				"어슬랜트 스파이럴", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"11 31035 0 0 0", "귀속/히트엔드(딜)/6207", "+ 32600", "귀속/히트엔드(딜)/6520", "+ 34162", "귀속/히트엔드(딜)/6832", 
				"+ 35724", "귀속/히트엔드(딜)/7145", "+ 37286", "귀속/히트엔드(딜)/7457", null,
				"엑조틱 램펀트", Skill_type.ACTIVE, "", 75, 40, 30, 3, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"5 23312 0 0 0", "귀속/히트엔드(딜)/4663", "+ 25581", "귀속/히트엔드(딜)/5116", "+ 27851", "귀속/히트엔드(딜)/5570", 
				"+ 30121", "귀속/히트엔드(딜)/6024", "+ 32391", "귀속/히트엔드(딜)/6478", "+ 34661", "귀속/히트엔드(딜)/6932", null,
				"디스트리뷰션", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 스킬 아이콘 제보받습니다",
				"6 39090 0 0 0", "귀속/히트엔드(딜)/7819", "+ 41720", "귀속/히트엔드(딜)/8345", "+ 44360", "귀속/히트엔드(딜)/8872", 
				"+ 46990", "귀속/히트엔드(딜)/9398", "+ 49620", "귀속/히트엔드(딜)/9924", null,
				"노바 램넌트", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"2 70118 0 0 0", "귀속/히트엔드(딜)/14083", "+ 83316", "귀속/히트엔드(딜)/16734", "+ 96514", "귀속/히트엔드(딜)/19384", 
				"+ 109719", "귀속/히트엔드(딜)/22035", "+ 122917", "귀속/히트엔드(딜)/24686", null,
				
				/////패시브
				"단검 마스터리", Skill_type.PASSIVE, "", 20, 30, 20, 4, CalculatorVersion.VER_1_1_f,
				"18", "물리무기마스터리 29.8 단검", "+", "물리무기마스터리 31.5 단검", "+", "물리무기마스터리 33 단검",
				"+", "물리무기마스터리 34.5 단검", "+", "물리무기마스터리 36.2 단검", "+", "물리무기마스터리 38.5 단검",
				"+", "물리무기마스터리 40.5 단검", "+", "물리무기마스터리 42.5 단검", "+", "물리무기마스터리 44.5 단검",
				"+", "물리무기마스터리 46.5 단검", "+", "물리무기마스터리 48.5 단검", "+", "물리무기마스터리 50.5 단검", null,
				"쌍검 마스터리", Skill_type.PASSIVE, "", 20, 30, 20, 4, CalculatorVersion.VER_1_1_f,
				"18", "물리무기마스터리 25.7 쌍검", "+", "물리무기마스터리 27.1 쌍검", "+", "물리무기마스터리 28.6 쌍검",
				"+", "물리무기마스터리 30.0 쌍검", "+", "물리무기마스터리 31.4 쌍검", "+", "물리무기마스터리 32.9 쌍검",
				"+", "물리무기마스터리 34.5 쌍검", "+", "물리무기마스터리 36.0 쌍검", "+", "물리무기마스터리 37.5 쌍검",
				"+", "물리무기마스터리 39.0 쌍검", "+", "물리무기마스터리 40.5 쌍검", "+", "물리무기마스터리 42.0 쌍검", null,
				"문아크", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_f,
				"15", "크증버프 40 & 물크 32", "+", "크증버프 41.5 & 물크 34", "+", "크증버프 43 & 물크 36", "+", "크증버프 44.5 & 물크 38", "+", "크증버프 +1.5 & 물크 +2", "반복 1",
				"히트 블리드", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_1_f,
				"6", "물리마스터리 20", "+", "물리마스터리 22", "+", "물리마스터리 24", "+", "물리마스터리 +2", "반복 1",
				"히트 블리드(발동)", Skill_type.OPTION, "", 75, 1, 1, 3, CalculatorVersion.VER_1_1_f, "설명 활성화시 엑셀 링크스 부분 버프 발동",
				"1", "", null,
				"히트엔드 - 버블수", Skill_type.INPUT, "", 15, 1, 1, 5, CalculatorVersion.VER_1_1_f, "설명 사용될 히트엔드 버블수를 입력해주세요", "설명 최대치를 초과할 경우 최대치 적용",
				"1", "횟수(재료수) 6 & fStat_히트엔드", null,
				"히트엔드", Skill_type.PASSIVE, "", 15, 30, 20, 2, CalculatorVersion.VER_1_1_f, "설명 히트엔드 딜 (n-1)% 증가",
				"20", "", "+", "", "반복 1",
				"히트엔드(딜)", Skill_type.ACTIVE_NOMARK, "", 15, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"1 1 0 0 0", null,			
				 
				/////TP
				"히트엔드 강화", "히트엔드", "", 55, 7, 5, 0, CalculatorVersion.VER_1_1_f, "설명 히트엔드 공격력 비율 6n% 증가", null,
				"밴디트컷 강화", "밴디트컷", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"체인소 강화", "체인소", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"허리케인 강화", "허리케인", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"버티컬 스파이럴 강화", "버티컬 스파이럴", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"라이트닝 애로우 강화", "라이트닝 애로우", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"소닉 어설트 강화", "소닉 어설트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"엑셀 스트라이크 강화", "엑셀 스트라이크", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				 
				 
				///////////////섀댄
				/////액티브
				"심장 찌르기", Skill_type.ACTIVE, Job.SHADOWDANCER, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"33 10254+5129 0 0 0", "+ 10498+5250", "+ 10745+5372", "+ 10990+5495", null,
				"칼날돌풍", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"28 868*18+14740 0 0 0", "+ 891*18+15140", "+ 914*18+15539", "+ 937*18+15940", "+ 962*18+16340", null,
				"파이널 디스트럭션", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"26 5609*4+20167 0 0 0", "+ 5771*4+20747", "+ 5934*4+21323", "+ 6090*4+21901", null,
				"암살", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"23 63165 0 0 0", "+ 65150", "+ 67131", "+ 69112", "+ 71097", "+ 73079", null,
				"암살 - HP 피해증폭", Skill_type.OPTION, "", 45, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 HP가 낮은 적 공격시 피해량 증가 적용",
				"1", "스킬 암살 % 25", null,
				"단검 투척", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"16 2632 0 0 0", "+ 2740", "+ 2844", "+ 2952", "+ 3055", null,
				"단검 투척(백어택)", "원본 - 단검 투척", "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"16 10531+21060 0 0 0", "+ 10954+21907", "+ 11377+22756", "+ 11801+23602", "+ 12224+24449", null,
				"섀도우 스팅", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"11 4502*9+18013 0 0 0", "+ 4728*9+18918", "+ 4956*9+19826", "+ 5183*9+20732", "+ 5411*9+21638", null,
				"죽음의 연무", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, true, CalculatorVersion.VER_1_1_f,
				"8 4327*3+51285 0 0 0", "+ 4583*3+54327", "+ 4841*3+57372", "+ 5097*3+60417", "+ 5354*3+63456", "+ 5609*3+66500", null,
				"페이탈 블리츠", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"6 87783 0 0 0", "+ 93693", "+ 99601", "+ 105507", "+ 111417", "+ 117325", null,
				"절명의 그림자", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f,
				"10 3098*21+48335 0 0 0", "+ 3328*21+51967", "+ 3562*21+55597", "+ 3793*21+59230", "9 2868*21+44703", null,
				"체인 리액션", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_1_f, "설명 피니시 공격의 공격력입니다",
				"2 93490 0 0 0", "+ 111089", "+ 128687", "+ 146284", null,
				
				/////패시브
				"날카로운 단검", Skill_type.PASSIVE, "", 20, 30, 20, 3, CalculatorVersion.VER_1_1_f, "설명 단검 착용시 마스터리 적용",
				"20", "물리무기마스터리 30 단검", "+", "물리무기마스터리 31.2 단검", "+", "물리무기마스터리 32.4 단검",
				"+", "물리무기마스터리 33.7 단검", "+", "물리무기마스터리 34.9 단검", "+", "물리무기마스터리 36.1 단검",
				"+", "물리무기마스터리 37.4 단검", "+", "물리무기마스터리 38.6 단검", "+", "물리무기마스터리 39.8 단검",
				"+", "물리무기마스터리 41.1 단검", "+", "물리무기마스터리 42.3 단검", null,
				"암살기술", Skill_type.PASSIVE, "", 15, 1, 1, 3, CalculatorVersion.VER_1_1_f,
				"1", "크증버프 20", null,
				"배후습격", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_1_f, 
				"15", "물크 15 & 스킬 심장 찌르기 % 32.7 & 스킬 칼날돌풍(막타) % 32.7 & 스킬 파이널 디스트럭션 % 32.7 & 스킬 암살 % 32.7 & 스킬 단검 투척(백어택) % 32.7 & 스킬 섀도우 스팅(백어택) % 32.7 & "
				+ "스킬 페이탈 블리츠 % 32.7 & 스킬 체인 리액션 % 32.7 & 스킬 죽음의 연무 % 32.7",
				"+", "물크 +1 & 스킬 심장 찌르기 % +2.1 & 스킬 칼날돌풍(막타) % +2.1 & 스킬 파이널 디스트럭션 % +2.1 & 스킬 암살 % +2.1 & 스킬 단검 투척(백어택) % +2.1 & 스킬 섀도우 스팅(백어택) % +2.1 & "
				+ "스킬 페이탈 블리츠 % +2.1 & 스킬 체인 리액션 % +2.1 & 스킬 죽음의 연무 % +2.1",
				"+", "물크 +1 & 스킬 심장 찌르기 % +2.1 & 스킬 칼날돌풍(막타) % +2.1 & 스킬 파이널 디스트럭션 % +2.1 & 스킬 암살 % +2.1 & 스킬 단검 투척(백어택) % +2.1 & 스킬 섀도우 스팅(백어택) % +2.1 & "
				+ "스킬 페이탈 블리츠 % +2.1 & 스킬 체인 리액션 % +2.1 & 스킬 죽음의 연무 % +2.1",
				"+", "물크 +1 & 스킬 심장 찌르기 % +2 & 스킬 칼날돌풍(막타) % +2 & 스킬 파이널 디스트럭션 % +2 & 스킬 암살 % +2 & 스킬 단검 투척(백어택) % +2 & 스킬 섀도우 스팅(백어택) % +2 & "
				+ "스킬 페이탈 블리츠 % +2 & 스킬 체인 리액션 % +2 & 스킬 죽음의 연무 % +2",
				"+", "물크 +1 & 스킬 심장 찌르기 % +2.1 & 스킬 칼날돌풍(막타) % +2.1 & 스킬 파이널 디스트럭션 % +2.1 & 스킬 암살 % +2.1 & 스킬 단검 투척(백어택) % +2.1 & 스킬 섀도우 스팅(백어택) % +2.1 & "
				+ "스킬 페이탈 블리츠 % +2.1 & 스킬 체인 리액션 % +2.1 & 스킬 죽음의 연무 % +2.1",
				"+", "물크 +1 & 스킬 심장 찌르기 % +2 & 스킬 칼날돌풍(막타) % +2 & 스킬 파이널 디스트럭션 % +2 & 스킬 암살 % +2 & 스킬 단검 투척(백어택) % +2 & 스킬 섀도우 스팅(백어택) % +2 & "
				+ "스킬 페이탈 블리츠 % +2 & 스킬 체인 리액션 % +2 & 스킬 죽음의 연무 % +2", "반복 5",
				"베일드 어택", Skill_type.PASSIVE, "", 75, 40, 30, 3, CalculatorVersion.VER_1_1_f, "설명 7스택 기준 증뎀율",
				"6", "증뎀버프 32", "+", "증뎀버프 +2", "반복 1",
				"암살자의 마음가짐", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_1_f,
				"20", "물크 30.9 & 크증버프 74", null,
				
				/////TP
				"심장 찌르기 강화", "심장 찌르기", "", 55, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"칼날돌풍 강화", "칼날돌풍", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"파이널 디스트럭션 강화", "파이널 디스트럭션", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
				"암살 강화", "암살", "", 65, 7, 5, 10, CalculatorVersion.VER_1_1_f, null,
		};
		return data;
	}
}
