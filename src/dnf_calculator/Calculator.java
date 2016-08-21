package dnf_calculator;

public class Calculator {
	
	public static long percentDamage_physical(int skillPercent, Monster object, Char character, int mode) throws StatusTypeMismatch
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		double dec_defence=(1-getPhysicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
		CalculateElement elementCal = new CalculateElement(object, stat);								// 각 속강별 데미지 계산
		
		double inc_strength=1+stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0/250;		// 1+힘*(100+힘뻥)/100/250
		int inc_weapon1=(int)((stat.getStat(StatList.WEP_PHY)*(100+stat.getStat(StatList.MAST_PHY_2)))/100);				// [무기물공*(100+마스터리2)/100]
		int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100);										// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
		int defIgnore=(int)((stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);	// 방어무시데미지
		double frontATK=inc_strength*inc_weapon2*dec_defence*elementCal.get_inc_elem()+defIgnore;							// (힘*물공)*방어력*속강+방무뎀
		
		
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100.0) avgCritical=100.0;
		double inc_critical;																				// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT_BACK))/100.0
					*(100.0+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;										// ((100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100)/100
		else inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT))/100.0
					*(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;												// ((100-크리확률)+크리확률*1.5*(100+크증뎀)/100*(100+크증버프)/100)/100
		
		
		if(mode==1)//average damage mode
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long percentDamage_physical(int skillPercent, Monster object, Char character) throws StatusTypeMismatch
	{ return percentDamage_physical(skillPercent, object, character, 1); }
	
	
	public static long fixedDamage_physical(int skillValue, int usedIndepValue , Monster object, Char character, int mode) throws StatusTypeMismatch
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		double dec_defence=(1-getPhysicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
		CalculateElement elementCal = new CalculateElement(object, stat);								// 각 속강별 데미지 계산
		
		double inc_strength=1+stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0/250;		// 1+힘*(100+힘뻥)/100/250
		int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
				+(double)stat.getStat(StatList.WEP_IND_REFORGE));										// 독공*독공뻥+재련 
		double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 힘*독공*방어력*속강
		
		
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT_BACK))/100.0
					*(100.0+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;										// ((100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100)/100
		else inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT))/100.0
					*(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;												// ((100-크리확률)+크리확률*1.5*(100+크증뎀)/100*(100+크증버프)/100)/100
		
		
		if(mode==1)//average damage mode
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long fixedDamage_physical(int skillValue, int usedIndepValue , Monster object, Char character) throws StatusTypeMismatch
	{ return fixedDamage_physical(skillValue, usedIndepValue , object, character, 1); }
	
	
	public static long percentDamage_magical(int skillPercent, Monster object, Char character, int mode) throws StatusTypeMismatch
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		double dec_defence=(1-getMagicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
		CalculateElement elementCal = new CalculateElement(object, stat);								// 각 속강별 데미지 계산
		
		double inc_strength=1+stat.getStat(StatList.INT)*(100.0+stat.getStat(StatList.INT_INC))/100.0/250;									// 1+지능/250
		int inc_weapon1=(int)((stat.getStat(StatList.WEP_MAG)*(100+stat.getStat(StatList.MAST_MAG_2)))/100);					// [무기마공*(100+마스터리2)/100]
		int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_MAG)+100))/100);											// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100]
		int defIgnore=(int)((stat.getStat(StatList.WEP_NODEF_MAG)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);		// 방어무시데미지
		double frontATK=inc_strength*inc_weapon2*dec_defence*elementCal.get_inc_elem()+defIgnore;								// (지능*마공)*방어력*속강+방무뎀
		
		
		double avgCritical=stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_MAG);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT_BACK))/100.0
					*(100.0+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;										// ((100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100)/100
		else inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT))/100.0
					*(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;												// ((100-크리확률)+크리확률*1.5*(100+크증뎀)/100*(100+크증버프)/100)/100
		
		
		if(mode==1)//average damage mode
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long percentDamage_magical(int skillPercent, Monster object, Char character) throws StatusTypeMismatch
	{ return percentDamage_magical(skillPercent, object, character, 1); } 
	
	
	public static long fixedDamage_magical(int skillValue, int usedIndepValue , Monster object, Char character, int mode) throws StatusTypeMismatch
	{
		//calculate with status
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		double dec_defence=(1-getMagicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
		CalculateElement elementCal = new CalculateElement(object, stat);								// 각 속강별 데미지 계산
		double inc_strength=1+stat.getStat(StatList.INT)*(stat.getStat(StatList.INT_INC))/100.0/250;			// 1+지능/250
		int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
				+(double)stat.getStat(StatList.WEP_IND_REFORGE));										// 독공*독공뻥+재련 
		double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 지능*독공*방어력*속강
		
		
		double avgCritical=stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_MAG);
		if(avgCritical>100) avgCritical=100;
		double inc_critical;																			// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT_BACK))/100.0
					*(100.0+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;										// ((100-크리확률)+크리확률*1.5*(100+백크증뎀)/100*(100+크증버프)/100)/100
		else inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT))/100.0
					*(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;												// ((100-크리확률)+크리확률*1.5*(100+크증뎀)/100*(100+크증버프)/100)/100
		
		
		if(mode==1)//average damage mode
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
		else // I DONT KNOW, SOMEWHAT ELSE
			return (long)((double)skillValue/usedIndepValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
	}
	public static long fixedDamage_magical(int skillValue, int usedIndepValue , Monster object, Char character) throws StatusTypeMismatch
	{ return fixedDamage_magical(skillValue, usedIndepValue , object, character, 1); } 
	
	
	public static long damage_enhancing_avg(Status.PublicStatus stat, Monster object, Char character, CalculateElement elementCal) throws StatusTypeMismatch  	// 속강증크증스증추뎀카운터투함포기타등등
	{
		
		double inc_damage=(100.0+stat.getStat(StatList.BUF_INC))/100.0;										// 증뎀버프
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_INC_BACK)>stat.getStat(StatList.DAM_INC))
			inc_damage*=(100.0+stat.getStat(StatList.DAM_INC_BACK))/100.0;									// (100+증뎀)/100
		else inc_damage*=(100.0+stat.getStat(StatList.DAM_INC))/100.0;										// (100+증뎀)/100
		
		//main variable :: inc_damage
		/////////////////////////////
		
		double inc_counter=1;
		if(object.getBool(Monster_StatList.COUNTER)) inc_counter=1.25;																// 카운터
	
		//main variable :: inc_counter
		//////////////////////////////
		
		double inc_add;																												// 추뎀항
		double add_whole=(100.0+stat.getStat(StatList.DAM_ADD)+stat.getStat(StatList.DAM_ADD_FIRE)*elementCal.get_inc_fire()
				+stat.getStat(StatList.DAM_ADD_WATER)*elementCal.get_inc_water()
				+stat.getStat(StatList.DAM_ADD_LIGHT)*elementCal.get_inc_light()
				+stat.getStat(StatList.DAM_ADD_DARKNESS)*elementCal.get_inc_darkness())/100.0;										// 추뎀+속추뎀 총합
		if(object.getBool(Monster_StatList.BACKATK)) add_whole+=stat.getStat(StatList.DAM_ADD_BACK);								// 백어택 추뎀
		double avgCritical_add=stat.getStat(StatList.CRT_LOW)+3.0;																	// 추뎀크리 확률
		if(avgCritical_add>100.0) avgCritical_add=100.0;
		double inc_critical_add=((100.0-avgCritical_add)+avgCritical_add*1.5*(int)(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;	// 추뎀크리 기댓값 적용
		
		inc_add=(double)add_whole*inc_critical_add*inc_counter;
		
		//main variable :: inc_add
		///////////////////////////////
		
		double inc_skill = (100.0+stat.getStat(StatList.DAM_SKILL))/100.0;											// 스증뎀
		double inc_buf = (100.0+stat.getStat(StatList.DAM_BUF))/100.0;												// 버프 증뎀(투함포, 이그니스)
		//main variable :: inc_skill, inc_buf
		//////////////////////////////
		
		return (long)(inc_damage*inc_counter*inc_add*inc_skill*inc_buf);
	}
	
	public static double getPhysicalPercentDefence(Char character, Monster object) throws StatusTypeMismatch							// 몹의 물리퍼센트 방어력 구하기
	{
		int level = character.level;
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		int fixedDef=object.getStat(Monster_StatList.DEFENSIVE_PHY);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_PHY);															// 기본방어력-고정방깍
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_SKILL))/100.0);					// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0);		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		
		return ((double)(fixedDef))/((double)(fixedDef+level*200));													// %방어력=고정방어력/(고정방어력+레벨*200)
	}
	
	public static double getMagicalPercentDefence(Char character, Monster object) throws StatusTypeMismatch			// 몹의 마법퍼센트 방어력 구하기
	{
		int level = character.level;
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		int fixedDef=object.getStat(Monster_StatList.DEFENSIVE_MAG);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_MAG);															// 기본방어력-고정방깍
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_SKILL))/100.0);					// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0);		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		
		return ((double)(fixedDef))/((double)(fixedDef+level*200));													// %방어력=고정방어력/(고정방어력+레벨*200)
	}
}

class CalculateElement
{
	private double dmg_fire;
	private double dmg_water;
	private double dmg_light;
	private double dmg_darkness;
	private int mode;
	private double inc_elem;																				// 속강항
	
	public CalculateElement(Monster object, Status.PublicStatus stat) throws StatusTypeMismatch
	{
		dmg_fire=element_dmg(object, stat, StatList.ELEM_FIRE);
		dmg_water=element_dmg(object, stat, StatList.ELEM_WATER);
		dmg_light=element_dmg(object, stat, StatList.ELEM_LIGHT);
		dmg_darkness=element_dmg(object, stat, StatList.ELEM_DARKNESS);
		mode = getElement(stat, dmg_fire, dmg_water, dmg_light, dmg_darkness);						// 적용 속성
		
		switch(mode)
		{
			case StatList.ELEM_FIRE:
				inc_elem=dmg_fire;
				break;
				
			case StatList.ELEM_WATER:
				inc_elem=dmg_water;
				break;
				
			case StatList.ELEM_LIGHT:
				inc_elem=dmg_light;
				break;
				
			case StatList.ELEM_DARKNESS:
				inc_elem=dmg_darkness;
				break;
				
			default:
				inc_elem=1;
		}
	}
	
	public double get_inc_elem() {return inc_elem;}
	public int get_mode() {return mode;}
	public double get_inc_fire() {return dmg_fire;}
	public double get_inc_water() {return dmg_water;}
	public double get_inc_light() {return dmg_light;}
	public double get_inc_darkness() {return dmg_darkness;}
	
	public static double element_dmg(Monster object, Status.PublicStatus stat, int element) throws StatusTypeMismatch
	{
		int index = element-StatList.ELEM_FIRE;
		double temp = ( 1.05+0.0045*(stat.getStat(element)+stat.getStat(StatList.ELEM_FIRE_DEC+index)-object.getStat(Monster_StatList.FIRE_RESIST+index) ) );
					  // 1+0.05(속성부여)+0.0045*(속강-속저오라-몹속저)
	
		if(temp<0) return 0;
		else return temp;
	}
	
	public static int getElement(Status.PublicStatus stat, double fire, double water, double light, double darkness)
	{
		int mode = -1;
		double temp = -0.5;
		if(temp<fire && ((ElementInfo)stat.publicInfo[StatList.ELEM_FIRE]).hasElement){
			temp=fire;
			mode=StatList.ELEM_FIRE;
		}
		if(temp<water && ((ElementInfo)stat.publicInfo[StatList.ELEM_WATER]).hasElement){
			temp=water;
			mode=StatList.ELEM_WATER;
		}
		if(temp<light && ((ElementInfo)stat.publicInfo[StatList.ELEM_LIGHT]).hasElement){
			temp=light;
			mode=StatList.ELEM_LIGHT;
		}
		if(temp<darkness && ((ElementInfo)stat.publicInfo[StatList.ELEM_DARKNESS]).hasElement){
			temp=darkness;
			mode=StatList.ELEM_DARKNESS;
		}
		
		return mode;
	}
}

class Char
{
	Status finalStatus;
	int level;
	
	public Char(Status stat, int lev)
	{
		finalStatus=stat;
		level=lev;
	}
}
