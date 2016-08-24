package dnf_infomation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveItemDictionary {
	public static void main(String[] args)
	{
		try{
			ItemDictionary itemDic = new ItemDictionary();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ItemDictionary.dfd"));
			out.writeObject(itemDic);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}