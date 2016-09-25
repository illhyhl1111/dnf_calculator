package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_calculator.StatusList;
import dnf_class.Monster;
import dnf_infomation.GetDictionary;

public class TrainingRoom extends DnFComposite
{
	StatusList buffList;
	Monster monster;
	Label monsterImage;
	
	public TrainingRoom(Composite parent)
	{
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		buffList = new StatusList();
		monster = new Monster("default");
		
		monsterImage = new Label(mainComposite, SWT.BORDER);
		FormData monsterFormData = new FormData();
		monsterFormData.bottom = new FormAttachment(100, -5);
		monsterFormData.left = new FormAttachment(0, 10);
		
		monsterImage.setLayoutData(monsterFormData);
		monsterImage.setAlignment(SWT.CENTER);
		
		renew();
	}

	@Override
	public void renew() {
		Image image;
		if(monster.getIcon()==null) image = GetDictionary.iconDictionary.get("디폴트");
		else image = GetDictionary.iconDictionary.get(monster.getItemName());
		monsterImage.setImage(image);
	}
}
