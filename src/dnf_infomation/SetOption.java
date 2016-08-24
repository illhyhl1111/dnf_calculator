package dnf_infomation;

import dnf_InterfacesAndExceptions.SetName;

public class SetOption {
	private int setNum;
	private SetName setName;
	
	public SetOption(int num, SetName name)
	{
		setNum=num;
		setName=name;
	}
	public SetOption(SetName name)
	{
		setNum=1;
		setName=name;
	}
	
	public void increseNum() {setNum++;}
	public int getSetNum() { return setNum;}
	public SetName getName() { return setName;}
}
