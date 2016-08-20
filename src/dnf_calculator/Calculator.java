package dnf_calculator;

public class Calculator {
	
	public static long percentDamage_physical(int skillPercent, Monster object, Char character, int mode)
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		double inc_strength=1+stat.getStat(StatList.STR)*stat.getStat(StatList.STR_INC)/250;						// 1+힘/250
		int inc_weapon1=(stat.getStat(StatList.WEP_PHY)*(100+stat.getStat(StatList.MAST_PHY_2)))/100;				// [무기물공*(100+마스터리2)/100]
		int inc_weapon2=(inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100;									// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
		int defIgnore=(stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100;	// 방어무시데미지
		double frontATK=inc_strength*inc_weapon2+defIgnore;															// (힘)*(물공+방무뎀)
		
		
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT_BACK))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100
		else inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+크증뎀)/100
		
		
		if(mode==1)//average damage mode
			return (long)(skillPercent*frontATK*inc_critical*damage_enhancing_avg(stat, object, character));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)(skillPercent*frontATK*inc_critical*damage_enhancing_avg(stat, object, character)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long percentDamage_physical(int skillPercent, Monster object, Char character)
	{ return percentDamage_physical(skillPercent, object, character, 1); }
	
	
	public static long fixedDamage_physical(int skillValue, int usedIndepValue , Monster object, Char character, int mode)
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		double inc_strength=1+stat.getStat(StatList.STR)*stat.getStat(StatList.STR_INC)/250;			// 1+힘/250
		int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
				+(double)stat.getStat(StatList.WEP_IND_REFORGE));										//독공*독공뻥+재련 
		double frontATK=inc_strength*inc_indep;															// (힘)*(독공)
		
		
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT_BACK))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100
		else inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+크증뎀)/100
		
		
		if(mode==1)//average damage mode
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long fixedDamage_physical(int skillValue, int usedIndepValue , Monster object, Char character)
	{ return fixedDamage_physical(skillValue, usedIndepValue , object, character, 1); }
	
	
	public static long percentDamage_magical(int skillPercent, Monster object, Char character, int mode)
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		double inc_strength=1+stat.getStat(StatList.INT)*stat.getStat(StatList.INT_INC)/250;							// 1+지능/250
		int inc_weapon1=(stat.getStat(StatList.WEP_MAG)*(100+stat.getStat(StatList.MAST_MAG_2)))/100;					// [무기마공*(100+마스터리2)/100]
		int inc_weapon2=(inc_weapon1*(stat.getStat(StatList.MAST_MAG)+100))/100;										// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100]
		int defIgnore=(stat.getStat(StatList.WEP_NODEF_MAG)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100;		// 방어무시데미지
		double frontATK=inc_strength*inc_weapon2+defIgnore;																// (지능)*(마공+방무뎀)
		
		
		double avgCritical=stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_MAG);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT_BACK))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100
		else inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+크증뎀)/100
		
		
		if(mode==1)//average damage mode
			return (long)(skillPercent*frontATK*inc_critical*damage_enhancing_avg(stat, object, character));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)(skillPercent*frontATK*inc_critical*damage_enhancing_avg(stat, object, character)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long percentDamage_magical(int skillPercent, Monster object, Char character)
	{ return percentDamage_magical(skillPercent, object, character, 1); }
	
	
	public static long fixedDamage_magical(int skillValue, int usedIndepValue , Monster object, Char character, int mode)
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		double inc_strength=1+stat.getStat(StatList.INT)*stat.getStat(StatList.INT_INC)/250;			// 1+지능/250
		int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
				+(double)stat.getStat(StatList.WEP_IND_REFORGE));										//독공*독공뻥+재련 
		double frontATK=inc_strength*inc_indep;															// (지능)*(독공)
		
		
		double avgCritical=stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_MAG);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT_BACK))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100
		else inc_critical=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT))/100)
					*(int)(100+stat.getStat(StatList.BUF_CRT))/100;										// (100-크리확률)+크리확률*1.5*(100+크증뎀)/100
		
		
		if(mode==1)//average damage mode
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long fixedDamage_magical(int skillValue, int usedIndepValue , Monster object, Char character)
	{ return fixedDamage_magical(skillValue, usedIndepValue , object, character, 1); }
	
	
	public static long damage_enhancing_avg(Status.PublicStatus stat, Monster object, Char character)  
	{
		
		int inc_damage=(100+stat.getStat(StatList.BUF_INC))/100;										// 증뎀버프
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_INC_BACK)>stat.getStat(StatList.DAM_INC))
			inc_damage*=(100+stat.getStat(StatList.DAM_INC_BACK))/100;									// (100+증뎀)/100
		else inc_damage*=(100+stat.getStat(StatList.DAM_INC))/100;										// (100+증뎀)/100
		
		//main variable :: inc_damage
		/////////////////////////////
		
		double inc_counter=1;
		if(object.getBool(Monster_StatList.COUNTER)) inc_counter=1.25;																// 카운터
	
		//main variable :: inc_counter
		//////////////////////////////
		
		double elem_fire=element_dmg(object, stat, StatList.ELEM_FIRE);
		double elem_water=element_dmg(object, stat, StatList.ELEM_WATER);
		double elem_light=element_dmg(object, stat, StatList.ELEM_LIGHT);
		double elem_darkness=element_dmg(object, stat, StatList.ELEM_DARKNESS);
		int mode = getElement(elem_fire, elem_water, elem_light, elem_darkness);						// 적용 속성
	
		double inc_elem;																				// 속강항
		switch(mode)
		{
			case StatList.ELEM_FIRE:
				inc_elem=elem_fire;
				break;
				
			case StatList.ELEM_WATER:
				inc_elem=elem_water;
				break;
				
			case StatList.ELEM_LIGHT:
				inc_elem=elem_light;
				break;
				
			case StatList.ELEM_DARKNESS:
				inc_elem=elem_darkness;
				break;
				
			default:
				inc_elem=1;
		}
		//main variable :: inc_elem
		/////////////////////////////
		
		double inc_add;																					// 추뎀항
		int add_whole=(int)(100+stat.getStat(StatList.DAM_ADD)+stat.getStat(StatList.DAM_ADD_FIRE)*elem_fire+stat.getStat(StatList.DAM_ADD_WATER)*elem_water
				+stat.getStat(StatList.DAM_ADD_LIGHT)*elem_light+stat.getStat(StatList.DAM_ADD_DARKNESS)*elem_darkness);		// 추뎀+속추뎀 총합
		if(object.getBool(Monster_StatList.BACKATK)) add_whole+=stat.getStat(StatList.DAM_ADD_BACK);							// 백어택 추뎀
		double avgCritical_add=stat.getStat(StatList.CRT_LOW)+3;									// 추뎀크리 확률
		if(avgCritical_add>100) avgCritical_add=100;
		double inc_critical_add=(100-avgCritical_add)+avgCritical_add*1.5*(int)(100+stat.getStat(StatList.BUF_CRT))/100;	// 추뎀크리 기댓값 적용
		
		inc_add=add_whole*inc_critical_add;
		
		//main variable :: inc_add
		///////////////////////////////
		
		double inc_skill = stat.getStat(StatList.DAM_SKILL);											// 스증뎀
		double inc_buf = stat.getStat(StatList.DAM_BUF);												// 버프 증뎀(투함포, 이그니스)
		//main variable :: inc_skill, inc_buf
		//////////////////////////////
		
		return (long)(inc_damage*inc_counter*inc_elem*inc_add*inc_skill*inc_buf);
	}
	
	public static double element_dmg(Monster object, Status.PublicStatus stat, int element)
	{
		if(!((ElementInfo)stat.publicInfo[element]).hasElement) return -1.0;
		
		double temp = ( 1.05+0.0045*(stat.getStat(element)-object.getStat(Monster_StatList.FIRE_RESIST+element-StatList.ELEM_FIRE) ) );
		if(temp<0) return 0;
		else return temp;
	}
	
	public static int getElement(double fire, double water, double light, double darkness)
	{
		int mode = -1;
		double temp = -0.5;
		if(temp<fire){
			temp=fire;
			mode=StatList.ELEM_FIRE;
		}
		if(temp<water){
			temp=water;
			mode=StatList.ELEM_WATER;
		}
		if(temp<light){
			temp=light;
			mode=StatList.ELEM_LIGHT;
		}
		if(temp<darkness){
			temp=darkness;
			mode=StatList.ELEM_DARKNESS;
		}
		
		return mode;
	}
	
}

class Char
{
	Status finalStatus;
}
