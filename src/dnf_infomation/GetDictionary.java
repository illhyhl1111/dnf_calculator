package dnf_infomation;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.OverlayIcon;

import dnf_class.Avatar;
import dnf_class.Buff;
import dnf_class.Card;
import dnf_class.Emblem;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Monster;
import dnf_class.MonsterOption;
import dnf_class.PartyCharacter;
import dnf_class.SetOption;
import dnf_class.Skill;
import dnf_class.Weapon;
import dnf_InterfacesAndExceptions.Character_type;
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
	public static HashMap<String, Image> skillIconDictionary;
	static boolean readed=false;

	public static void readFile()
	{
		if(readed) return;
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\ItemDictionary.dfd"));
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
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\CharacterDictionary.dfd"));
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
		skillIconDictionary = new HashMap<String, Image>();
		
		Image image;
		String path = "image\\";
		String[] defaultIcon = new String[] {
				path+"default.png", path+"common.png", path+"uncommon.png", path+"rare.png", path+"chronicle.png",
				path+"unique.png", path+"legendary.png", path+"epic.png", path+"none.png", path+"none.png"
		};
		
		String[] defaultName = new String[] {
				"디폴트", "COMMON", "UNCOMMON", "RARE", "CHRONICLE", "UNIQUE", "LEGENDARY", "EPIC", "NONE", "AVATAR"
		};
		
		int i=0;
		//디폴트 아이콘
		for(String add : defaultIcon){
			image = new Image(Display.getCurrent(), add);
			iconDictionary.put(defaultName[i], resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
			image.dispose();
			i++;
		}
		
		//장비 아이템
		LinkedList<LinkedList<? extends Item>> list = new LinkedList<LinkedList<? extends Item>>();
		list.add(itemDictionary.equipList);
		list.add(itemDictionary.titleList);
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
				Image resized = resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE);
				
				OverlayIcon mergedIcon = new OverlayIcon(ImageDescriptor.createFromImage(resized),
						ImageDescriptor.createFromImage(iconDictionary.get(item.getRarity().name())),
						new Point(InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE));

				iconDictionary.put(item.getName(), mergedIcon.createImage());
				image.dispose();
				resized.dispose();
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
		
		for(Buff buff : itemDictionary.buffList)
		{
			String icon = buff.getDisabledIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			iconDictionary.put(buff.getDisabledName(), resizeImage(image, InterfaceSize.INFO_BUTTON_SIZE));
			image.dispose();
		}
		
		//몹
		for(Monster monster : charDictionary.monsterList){
			String icon = monster.getIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			iconDictionary.put(monster.getName(), resizeMonsterImage(image));
			image.dispose();
			
			for(MonsterOption monsterOption : monster.monsterFeature.keySet()){
				icon = monsterOption.getIcon();
				if(icon==null) icon = "image\\default.png";
				image = new Image(Display.getCurrent(), icon);
				iconDictionary.put(monsterOption.getName(), resizeImage(image, InterfaceSize.SUB_MONSTER_SIZE_X));
				image.dispose();
			}
		}
		
		//캐릭
		for(Character_type type : Character_type.values()){
			if(type==Character_type.ALL) continue;
			else if(type==Character_type.NONE){
				iconDictionary.put("NONE", null);
			}
			else{
				String icon = "image\\Character\\"+type.name()+".png";
				image = new Image(Display.getCurrent(), icon);
				iconDictionary.put(type.name(), image);
				iconDictionary.put(type.name()+" - filp", filp(image, false));
			}
		}
	}
	
	public static void getSkillIcon(Job job)
	{
		Image image;
		for(Image i : skillIconDictionary.values())
			i.dispose();
		
		skillIconDictionary = new HashMap<String, Image>();
		for(Skill skill : charDictionary.skillList){
			if(!skill.isSkillOfChar(job)) continue;
			
			String icon = skill.getIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			skillIconDictionary.put(skill.getName(), resizeImage(image, InterfaceSize.SKILL_BUTTON_SIZE));
			image.dispose();
			 
			icon = skill.getDisabledIcon();
			if(icon==null) icon = "image\\default.png";
			image = new Image(Display.getCurrent(), icon);
			skillIconDictionary.put(skill.getDisabledName(), resizeImage(image, InterfaceSize.SKILL_BUTTON_SIZE));
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
	
	private static Image resizeMonsterImage(Image image)
	{
		ImageData data = image.getImageData();
		int width = data.width;
		int height = data.height;
		if(width>InterfaceSize.MONSTER_SIZE_X || height>InterfaceSize.MONSTER_SIZE_Y){
			double wRate = (double)InterfaceSize.MONSTER_SIZE_X/width;
			double hRate = (double)InterfaceSize.MONSTER_SIZE_Y/height;
			double rate = wRate>hRate ? hRate : wRate;
			data = data.scaledTo((int)(width*rate), (int)(height*rate));
		}
		return new Image(Display.getCurrent(), data);
	}
	
	private static Image filp(Image image, boolean vertical)
	{
		ImageData srcData = image.getImageData();
		int bytesPerPixel = srcData.bytesPerLine / srcData.width;
		int destBytesPerLine = srcData.width * bytesPerPixel;
		byte[] newData = new byte[srcData.data.length];
		for (int srcY = 0; srcY < srcData.height; srcY++) {
			for (int srcX = 0; srcX < srcData.width; srcX++) {
				int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
				if (vertical){
					destX = srcX;
					destY = srcData.height - srcY - 1;
				} else {
					destX = srcData.width - srcX - 1;
					destY = srcY;
				}
				destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
				srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
				System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
			}
		}
		// destBytesPerLine is used as scanlinePad to ensure that no padding is required
		ImageData result = new ImageData(srcData.width, srcData.height, srcData.depth, srcData.palette, srcData.scanlinePad, newData);
		return new Image(Display.getCurrent(), result, result.getTransparencyMask());
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
	
	public static ItemDictionary getNewItemDictionary(Job job)
	{
		ItemDictionary itemDictionary=null;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\ItemDictionary.dfd"));
			Object temp = in.readObject();

			itemDictionary = (ItemDictionary)temp;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			itemDictionary = new ItemDictionary();
			SaveItemDictionary.main(null);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(int i=0; i<itemDictionary.equipList.size(); i++){
			if(itemDictionary.equipList.get(i) instanceof Weapon){
				if(!((Weapon)itemDictionary.equipList.get(i)).enabled(job)) itemDictionary.equipList.remove(i--);
			}
		}
		
		for(Avatar avatar : itemDictionary.avatarList){
			avatar.setCoatOptionList(job);
		}
		for(Emblem emblem : itemDictionary.emblemList){
			emblem.setPlatinumOptionList(job);
		}
		
		return itemDictionary;
	}
	
	public static CharacterDictionary getNewCharDictionary(Job job)
	{
		CharacterDictionary charDictionary=null;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\CharacterDictionary.dfd"));
			Object temp = in.readObject();

			charDictionary = (CharacterDictionary)temp;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			charDictionary = new CharacterDictionary();
			SaveCharacterDictionary.main(null);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(int i=0; i<charDictionary.skillList.size(); i++){
			if(!charDictionary.skillList.get(i).isSkillOfChar(job))
				charDictionary.skillList.remove(i--);
		}
		
		return charDictionary;
	}
}
