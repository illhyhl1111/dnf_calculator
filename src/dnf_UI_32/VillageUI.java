package dnf_UI_32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_class.Characters;

public class VillageUI extends DnFComposite
{
	private TabFolder villageFolder;
	
	private EquipmentInfoUI equipUI;
	private TabItem equipTab;
	private EquipmentInfoUI avatarUI;
	private TabItem avatarTab;
	private Button toDungeonButton;
	private Button selectCharacterButton;
	private Characters character;
	
	VillageUI(Shell shell, Characters character)
	{
		this.character=character;
		mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setLayout(new FormLayout());
		
		toDungeonButton = new Button(mainComposite, SWT.PUSH);
		toDungeonButton.setText("수련의 방 입장");
		
		selectCharacterButton = new Button(mainComposite, SWT.PUSH);
		selectCharacterButton.setText("캐릭터 선택");
	}
	
	public void makeComposite(SkillTree skillTree, Vault vault)
	{
		villageFolder = new TabFolder(mainComposite, SWT.NONE);
		villageFolder.setLayoutData(new FormData());
		
		equipTab = new TabItem(villageFolder, SWT.NONE);
		String str1 = "장비";
		equipTab.setText(str1);
		equipUI = new EquipmentInfoUI(villageFolder, character, vault, skillTree, 0);
		equipTab.setControl(equipUI.getComposite());
		
		avatarTab = new TabItem(villageFolder, SWT.NONE);
		String str2 = "아바타/크리쳐/휘장";
		avatarTab.setText(str2);
		avatarUI = new EquipmentInfoUI(villageFolder, character, vault, skillTree, 1);
		avatarTab.setControl(avatarUI.getComposite());
		
		villageFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if(villageFolder.getSelection()[0].getText().equals(str1)) equipUI.itemInfo.renew();
				else if(villageFolder.getSelection()[0].getText().equals(str2)) avatarUI.itemInfo.renew();
			}
		});
		
		FormData dungeonBData = new FormData(100, 100);
		dungeonBData.left = new FormAttachment(villageFolder, 10);
		toDungeonButton.setLayoutData(dungeonBData);
		
		FormData selectCharBData = new FormData(100, 100);
		selectCharBData.left = new FormAttachment(villageFolder, 10);
		selectCharBData.top = new FormAttachment(toDungeonButton, 10);
		selectCharacterButton.setLayoutData(selectCharBData);
		
		mainComposite.layout();
	}
	
	@Override
	public void renew()
	{
		equipUI.renew();
		avatarUI.renew();
	}
	
	public void disposeContent()
	{
		villageFolder.dispose();
	}
	
	public Button get_toDungeonButton() {return toDungeonButton;}
	public Button get_selectCharacterButton() {return selectCharacterButton;}
}