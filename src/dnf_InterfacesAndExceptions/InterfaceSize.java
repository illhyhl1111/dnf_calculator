package dnf_InterfacesAndExceptions;

public interface InterfaceSize {
	int INFO_BUTTON_SIZE=40;
	int INVENTORY_BUTTON_SIZE=35;
	int SKILL_BUTTON_SIZE=40;
	
	int USER_INFO_INTERVAL=5;
	int USER_INFO_ITEM_SIZE_X=360; int USER_INFO_ITEM_SIZE_Y=210;
	int USER_STAT_MODE_SIZE_X=360; int USER_STAT_MODE_SIZE_Y=30;
	int USER_INFO_STAT_SIZE_X=360; int USER_INFO_STAT_SIZE_Y=180;
	int USER_INFO_NONSTAT_SIZE_X=380; int USER_INFO_NONSTAT_SIZE_Y=USER_INFO_ITEM_SIZE_Y+USER_INFO_INTERVAL+USER_STAT_MODE_SIZE_Y+USER_INFO_INTERVAL+USER_INFO_STAT_SIZE_Y+8;
	int VAULT_SIZE_Y=USER_INFO_NONSTAT_SIZE_Y+40;
	
	int ITEM_INFO_SIZE=250;
	int SET_INFO_SIZE=250; int SET_ITEM_INTERVAL=5;
	
	int MARGIN=5;
}
