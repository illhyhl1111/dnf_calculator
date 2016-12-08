package dnf_infomation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;
import dnf_class.Monster;
import dnf_class.PartyCharacter;
import dnf_class.Skill;

public class CharacterDictionary implements java.io.Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2541411296068975333L;
	public final LinkedList<Skill> skillList;
	private LinkedList<CharInfoBox> basicStatList;
	public final LinkedList<Monster> monsterList;
	public LinkedList<PartyCharacter> partyList;
	
	private String VERSION = CalculatorVersion.CHARACTER_VERSION;
	public static final String Directory = "data\\CharacterDictionary-"+CalculatorVersion.CHARACTER_VERSION+".dfd";
	
	public CharacterDictionary() 
	{
		skillList = new LinkedList<Skill>();
		try {
			SkillInfo.getInfo(skillList, SkillInfo.skillInfo_gunner());
			SkillInfo.getInfo(skillList, SkillInfo.skillInfo_swordman());
			SkillInfo.getInfo(skillList, SkillInfo.skillInfo_mage());
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		
		basicStatList = new LinkedList<CharInfoBox>();
		CharacterInfo.getInfo(basicStatList);
		
		monsterList = new LinkedList<Monster>();
		MonsterInfo.getInfo(monsterList);
		
		partyList = new LinkedList<PartyCharacter>();
		PartyCharacterInfo.getInfo(partyList);
	}
	
	public Skill getSkill(String name){
		for(Skill skill : skillList){
			if(skill.getName().equals(name)) return skill;
		}
		return null;
	}
	
	public LinkedList<String> getAvatarSkillList(Job job){
		LinkedList<String> list= new LinkedList<String>();
		
		for(Skill skill : GetDictionary.getSkillList(job, 90)){
			if(skill.type!=Skill_type.TP) list.add(skill.getItemName());
		}
		return list;
	}
	
	public StatusList getBasicStat(Job job, int level) throws ItemNotFoundedException
	{
		for(CharInfoBox b : basicStatList)
			if(b.job==job && b.level==level) return b.statList;
		
		throw new ItemNotFoundedException(job.toString()+", level "+level);
	}
	
	public Monster getMonsterInfo(String name) throws ItemNotFoundedException
	{
		for(Monster monster : monsterList)
			if(name.equals(monster.getName())) return monster;
		
		throw new ItemNotFoundedException(name);
	}
	
	public LinkedList<PartyCharacter> getPartyList(Job job)
	{
		LinkedList<PartyCharacter> list = new LinkedList<PartyCharacter>();
		for(PartyCharacter b : partyList)
			if(b.job!=job) list.add(b);					//TODO 홀리제외
		
		return list;
	}
	
	public PartyCharacter getPartyCharacter(String name) throws ItemNotFoundedException
	{
		for(PartyCharacter p : partyList)
			if(p.getName().equals(name)) return p;
		throw new ItemNotFoundedException(name);
	}
	
	@Override
	public Object clone()
	{
		CharacterDictionary charDictionary;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(CharacterDictionary.Directory));
			Object temp = in.readObject();

			charDictionary = (CharacterDictionary)temp;
			in.close();
			return charDictionary;
		}
		catch(FileNotFoundException e)
		{	
			charDictionary = new CharacterDictionary();
			SaveCharacterDictionary.main(null);
			return charDictionary;
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getVERSION() { return VERSION;}
}

class SaveCharacterDictionary {
	public static void main(String[] args)
	{
		try{
			CharacterDictionary charDic = new CharacterDictionary();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CharacterDictionary.Directory));
			out.writeObject(charDic);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

class CharInfoBox implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6833996468696237014L;
	StatusList statList;
	int level;
	Job job;
	
	CharInfoBox(Job job, int level, StatusList statList)
	{
		this.statList=statList;
		this.job=job;
		this.level=level;
	}
}