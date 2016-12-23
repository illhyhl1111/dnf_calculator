package dnf_calculator;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Monster;

public class CalculateElement
{
	private double dmg_fire;
	private double dmg_water;
	private double dmg_light;
	private double dmg_darkness;
	private Element_type type;
	private double inc_elem;																				// 속강항
	private Status stat;
	
	public CalculateElement(Monster object, Status stat, Element_type element) throws StatusTypeMismatch
	{
		this.stat=stat;
		dmg_fire=element_dmg(object, stat, StatList.ELEM_FIRE);
		dmg_water=element_dmg(object, stat, StatList.ELEM_WATER);
		dmg_light=element_dmg(object, stat, StatList.ELEM_LIGHT);
		dmg_darkness=element_dmg(object, stat, StatList.ELEM_DARKNESS);
		
		switch(element)
		{
		case NONE:
			type = getElement();																			// 적용 속성
			switch(type)
			{
				case FIRE:
					inc_elem=dmg_fire;
					break;
				case WATER:
					inc_elem=dmg_water;
					break;
				case LIGHT:
					inc_elem=dmg_light;
					break;
				case DARKNESS:
					inc_elem=dmg_darkness;
					break;
				default:
					inc_elem=1;
			}
			break;
		case FIRE:
			inc_elem=dmg_fire;
			type=Element_type.FIRE;
			break;
		case WATER:
			inc_elem=dmg_water;
			type=Element_type.WATER;
			break;
		case LIGHT:
			inc_elem=dmg_light;
			type=Element_type.LIGHT;
			break;
		case DARKNESS:
			inc_elem=dmg_darkness;
			type=Element_type.DARKNESS;
			break;
		case FIRE_LIGHT:
			type = getDualElement(element);
			if(type==Element_type.NONE) type=Element_type.FIRE;
			switch(type)
			{
			case FIRE:
				inc_elem=dmg_fire;
				break;
			case LIGHT:
				inc_elem=dmg_light;
				break;
			default:		
				inc_elem=dmg_fire;
			}
			break;
		case WATER_LIGHT:
			type = getDualElement(element);
			if(type==Element_type.NONE) type=Element_type.WATER;
			switch(type)
			{
			case WATER:
				inc_elem=dmg_water;
				break;
			case LIGHT:
				inc_elem=dmg_light;
				break;
			default:		
				inc_elem=dmg_water;
			}
			break;
		case FIRE_DARKNESS:
			type = getDualElement(element);
			if(type==Element_type.NONE) type=Element_type.FIRE;
			switch(type)
			{
			case FIRE:
				inc_elem=dmg_fire;
				break;
			case DARKNESS:
				inc_elem=dmg_darkness;
				break;
			default:		
				inc_elem=dmg_fire;
			}
			break;
		case ALL:
			type = getDualElement(element);
			if(type==Element_type.NONE) type=Element_type.FIRE;
			switch(type)
			{
				case FIRE:
					inc_elem=dmg_fire;
					break;
				case WATER:
					inc_elem=dmg_water;
					break;
				case LIGHT:
					inc_elem=dmg_light;
					break;
				case DARKNESS:
					inc_elem=dmg_darkness;
					break;
				default:
					inc_elem=dmg_fire;
			}
			break;
		}
	}
	
	public double get_inc_elem() {return inc_elem;}
	public Element_type get_type() {return type;}
	public double get_inc_fire() {return dmg_fire;}
	public double get_inc_water() {return dmg_water;}
	public double get_inc_light() {return dmg_light;}
	public double get_inc_darkness() {return dmg_darkness;}
	
	public static double element_dmg(Monster object, Status stat, int element) throws StatusTypeMismatch
	{
		int index = element-StatList.ELEM_FIRE;
		double temp = ( 1.05+0.0045*(stat.getStat(element)+stat.getStat(StatList.ELEM_ALL)+stat.getStat(StatList.ELEM_FIRE_DEC+index)-object.getStat(Monster_StatList.FIRE_RESIST+index) ) );
					  // 1+0.05(속성부여)+0.0045*(속강+모속강-속저오라-몹속저)
	
		if(temp<0) return 0;
		else return temp;
	}
	
	private Element_type getElement()
	{
		Element_type type = Element_type.NONE;
		double temp = -0.5;
		try{
			if(temp<dmg_fire && (stat.getEnabled(StatList.ELEM_FIRE))){
				temp=dmg_fire;
				type = Element_type.FIRE;
			}
			if(temp<dmg_water && (stat.getEnabled(StatList.ELEM_WATER))){
				temp=dmg_water;
				type = Element_type.WATER;
			}
			if(temp<dmg_light && (stat.getEnabled(StatList.ELEM_LIGHT))){
				temp=dmg_light;
				type = Element_type.LIGHT;
			}
			if(temp<dmg_darkness && (stat.getEnabled(StatList.ELEM_DARKNESS))){
				temp=dmg_darkness;
				type = Element_type.DARKNESS;
			}
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
		}
		
		return type;
	}
	
	private Element_type getDualElement(Element_type element)
	{
		Element_type type = Element_type.NONE;
		double temp = -0.5;
		if(temp<dmg_fire && (element==Element_type.ALL || element==Element_type.FIRE_LIGHT || element==Element_type.FIRE_DARKNESS)){
			temp=dmg_fire;
			type = Element_type.FIRE;
		}
		if(temp<dmg_water && (element==Element_type.ALL || element==Element_type.WATER_LIGHT)){
			temp=dmg_water;
			type = Element_type.WATER;
		}
		if(temp<dmg_light && (element==Element_type.ALL || element==Element_type.FIRE_LIGHT || element==Element_type.WATER_LIGHT)){
			temp=dmg_light;
			type = Element_type.LIGHT;
		}
		if(temp<dmg_darkness && (element==Element_type.ALL || element==Element_type.FIRE_DARKNESS)){
			temp=dmg_darkness;
			type = Element_type.DARKNESS;
		}
		return type;
	}
	
	public static Element_type getLargestType(Status stat)
	{
		int[] type = new int[4];
		int index=0;
		try {
			type[0]=(int) stat.getStat("화속강");
			type[1]=(int) stat.getStat("수속강");
			type[2]=(int) stat.getStat("명속강");
			type[3]=(int) stat.getStat("암속강");
			
			for(int i=0; i<3; i++)
				if(type[i]<type[i+1]) index=i+1;
			
			switch(index)
			{
			case 0:
				return Element_type.FIRE;
			case 1:
				return Element_type.WATER;
			case 2:
				return Element_type.LIGHT;
			case 3:
				return Element_type.DARKNESS;
			default:
				return Element_type.NONE;
			}
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
			return Element_type.NONE;
		}
	}
}
