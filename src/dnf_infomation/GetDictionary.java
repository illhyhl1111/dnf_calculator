package dnf_infomation;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import dnf_class.Buff;
import dnf_class.Card;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.PartyCharacter;
import dnf_class.SetOption;
import dnf_class.Skill;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;

public class GetDictionary
{
	public static ItemDictionary itemDictionary;
	public static CharacterDictionary charDictionary;
	public static HashMap<String, Image> iconDictionary;
	static boolean readed=false;

	public static void readFile(Job job)
	{
		if(readed) return;
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ItemDictionary.dfd"));
			Object temp = in.readObject();

			itemDictionary = (ItemDictionary)temp;
			
			readed=true;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			itemDictionary = new ItemDictionary();
			readed=true;
			SaveItemDictionary.main(null);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("CharacterDictionary.dfd"));
			Object temp = in.readObject();

			charDictionary = (CharacterDictionary)temp;
			
			readed=true;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			charDictionary = new CharacterDictionary();
			readed=true;
			SaveCharacterDictionary.main(null);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		iconDictionary = new HashMap<String, Image>();
		
		//디폴트 아이콘
		Image image = new Image(Display.getCurrent(), "image\\default.png");
		iconDictionary.put("디폴트", resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
		image.dispose();
		
		image = createTransparentImage(Display.getCurrent(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
		iconDictionary.put("아이템_투명", image);
		
		//장비 아이템
		for(Item item : itemDictionary.getVaultItemList(job)){
			String icon = item.getIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			iconDictionary.put(item.getName(), resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
			image.dispose();
		}
		
		//기타등등
		LinkedList<LinkedList<? extends Item>> list = new LinkedList<LinkedList<? extends Item>>(); 
		list.add(itemDictionary.avatarList);
		list.add(itemDictionary.cardList);
		list.add(itemDictionary.creatureList);
		list.add(itemDictionary.drapeList);
		list.add(itemDictionary.emblemList);
		list.add(itemDictionary.jamList);
		list.add(itemDictionary.titleList);
		list.add(itemDictionary.buffList);
		
		for(LinkedList<? extends Item> list2 : list)
		{
			for(Item item : list2){
				String icon = item.getIcon();
				if(icon==null) icon = "image\\default.png";
				image = new Image(Display.getCurrent(), icon);
				iconDictionary.put(item.getName(), resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
				image.dispose();
			}	
		}
		
		for(PartyCharacter party : itemDictionary.partyList)
		{
			for(HashMap<String, Buff> entry : party.getBuffHash().values())
			{
				Buff buff = entry.values().iterator().next();
				String icon = buff.getIcon();
				image = new Image(Display.getCurrent(), icon);
				iconDictionary.put(buff.getName(), resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
				image.dispose();
				
				icon = buff.getDisabledIcon();
				image = new Image(Display.getCurrent(), icon);
				iconDictionary.put(buff.getDisabledName(), resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
				image.dispose();
			}
		}
		
		//스킬
		for(Skill skill : charDictionary.getSkillList(job, 90)){
			String icon = skill.getIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			iconDictionary.put(skill.getName(), resizeImage(image, InterfaceSize.SKILL_BUTTON_SIZE));
			image.dispose();
			 
			icon = skill.getDisabledIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			iconDictionary.put(skill.getDisabledName(), resizeImage(image, InterfaceSize.SKILL_BUTTON_SIZE));
			image.dispose();
		}
	}
	
	static Image createTransparentImage(Display display, int width, int height) {
	    // allocate an image data
	    ImageData imData = new ImageData(width, height, 24, new PaletteData(0xff0000,0x00ff00, 0x0000ff));
	    imData.setAlpha(0, 0, 0); // just to force alpha array allocation with the right size
	    Arrays.fill(imData.alphaData, (byte) 0); // set whole image as transparent

	    // Initialize image from transparent image data
	    return new Image(display, imData);
	}
	
	private static Image resizeImage(Image image, int size)
	{
		ImageData data = image.getImageData();
		data = data.scaledTo(size, size);
		return new Image(Display.getCurrent(), data);
	}
	
	public static Equipment getEquipment(String name) throws ItemFileNotReaded, ItemNotFoundedException
	{
		if(!readed) throw new ItemFileNotReaded();
		return itemDictionary.getEquipment(name);
	}
	
	public static Card getCard(String name) throws ItemFileNotReaded, ItemNotFoundedException
	{
		if(!readed) throw new ItemFileNotReaded();
		return itemDictionary.getCard(name);
	}
	
	public static Item getTitle(String name) throws ItemNotFoundedException, ItemFileNotReaded {
		if(!readed) throw new ItemFileNotReaded();
		return itemDictionary.getTitle(name);
	}
	
	public static LinkedList<SetOption> getSetOptions(SetName setName) throws ItemFileNotReaded, ItemNotFoundedException
	{
		if(!readed) throw new ItemFileNotReaded();
		return itemDictionary.getSetOptions(setName);
	}
	
	public static int getDimensionInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getDimensionInfo(num, rarity, level);
	}
	
	public static int getReinforceAidInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getAidStatInfo(num, rarity, level);
	}
	
	public static int getReinforceInfo_phy (int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return ReinforceInfo.getReinforceWeaponInfo_phy(num, rarity, level, type);
	}
	
	public static int getReinforceInfo_mag (int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return ReinforceInfo.getReinforceWeaponInfo_mag(num, rarity, level, type);
	}
	
	public static int[] getReinforceEarringInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getEarringStatInfo(num, rarity, level);
	}
	
	public static int getReforgeInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getReforgeInfo(num, rarity, level);
	}
}
