package dnf_infomation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.JobList;
import dnf_calculator.StatusList;
import dnf_class.Monster;
import dnf_class.Skill;

class CharInfoBox implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6833996468696237014L;
	StatusList statList;
	int level;
	JobList job;
	
	CharInfoBox(JobList job, int level, StatusList statList)
	{
		this.statList=statList;
		this.job=job;
		this.level=level;
	}
}

public class CharacterDictionary implements java.io.Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2541411296068975333L;
	public final HashSet<Skill> skillList;
	private HashSet<CharInfoBox> basicStatList;
	public final HashSet<Monster> monsterList;
	
	public CharacterDictionary() 
	{
		skillList = new HashSet<Skill>();
		SkillInfo_gunner.getInfo(skillList);
		
		basicStatList = new HashSet<CharInfoBox>();
		CharacterInfo.getInfo(basicStatList);
		
		monsterList = new HashSet<Monster>();
		MonsterInfo.getInfo(monsterList);
	}
	
	public LinkedList<Skill> getSkillList(JobList job, int level)
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		for(Skill s : skillList){
			if(s.isSkillOfChar(job)){
				s.masterSkill(level);
				list.add(s);
			}
		}
		
		Collections.sort(list);
		return list;
	}
	
	public StatusList getBasicStat(JobList job, int level) throws ItemNotFoundedException
	{
		for(CharInfoBox b : basicStatList)
			if(b.job==job && b.level==level) return b.statList;
		
		throw new ItemNotFoundedException(job.toString()+", level "+level);
	}
	
	public Monster getMonsterInfo(String name) throws ItemNotFoundedException
	{
		for(Monster monster : monsterList)
			if(name.equals(monster.name)) return monster;
		
		throw new ItemNotFoundedException(name);
	}
	
	@Override
	public Object clone()
	{
		CharacterDictionary charDictionary;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("CharacterDictionary.dfd"));
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
}

class SaveCharacterDictionary {
	public static void main(String[] args)
	{
		try{
			CharacterDictionary charDic = new CharacterDictionary();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("CharacterDictionary.dfd"));
			out.writeObject(charDic);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}