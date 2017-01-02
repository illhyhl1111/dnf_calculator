package dnf_calculator;

import java.util.LinkedList;

public class TrackRecord implements java.io.Serializable{
	private static final long serialVersionUID = 3208123719217529220L;
	//public int statNum;
	public double strength;
	public final String source;
	public String description;
	public final char operator;
	public final boolean isOperator;
	public final boolean isStatList;
	public final LinkedList<TrackRecord> statList;
	
	public TrackRecord(String description, double strength, String source, boolean isCompound){
		if(isCompound) this.strength=1+strength/100;
		else this.strength=strength;
		this.source=source;
		this.description=description;
		operator='d';
		isOperator=false;
		isStatList=false;
		statList=null;
	}
	
	public TrackRecord(int statNum, double strength, String source, boolean isCompound){
		this(StatusAndName.getStatHash().get(statNum), strength, source, isCompound);
	}
	
	public TrackRecord(String description, double strength){
		this.strength=strength;
		this.source=null;
		this.description=description;
		operator='d';
		isOperator=false;
		isStatList=false;
		statList=null;
	}
	
	public TrackRecord(char operator){
		this.strength=-1;
		this.operator=operator;
		this.source=null;
		if(operator=='+') this.description="단리연산";
		else if(operator=='*') this.description="복리연산";
		else if(operator=='=') this.description="결과";
		else if(operator=='[' || operator==']') this.description="내림";
		else this.description="";
		isStatList=false;
		isOperator=true;
		statList=null;
	}
	
	public TrackRecord(String description, LinkedList<TrackRecord> list, char operator)
	{
		this.description=description;
		this.strength=-1;
		this.operator=operator;
		this.source=null;
		isOperator=false;
		isStatList=true;
		statList=list;
	}
	
	@Override
	public String toString(){
		if(isOperator) return Character.toString(operator);
		else if(isStatList) return "";
		else{
			String temp = String.format("%.4f", strength); 
			if(temp.contains(".0000")) temp=temp.substring(0, temp.length()-5);
			else if(temp.endsWith("000")) temp=temp.substring(0, temp.length()-3);
			else if(temp.endsWith("00")) temp=temp.substring(0, temp.length()-2);
			else if(temp.endsWith("0")) temp=temp.substring(0, temp.length()-1);
			String newStr;
			int index = temp.indexOf('.');
			String underDecimal, decimal;
			if(index<0){
				underDecimal = "";
				decimal = temp;
			}
			else{
				underDecimal = temp.substring(index);
				decimal = temp.substring(0, index);
			}
			if(decimal.length()>8){
				newStr = decimal.substring(0, decimal.length()-8)+"억 ";
				newStr += decimal.substring(decimal.length()-8, decimal.length()-4)+"만 ";
				newStr += decimal.substring(decimal.length()-4);
			}
			else if(decimal.length()>4){
				newStr = decimal.substring(0, decimal.length()-4)+"만 ";
				newStr += decimal.substring(decimal.length()-4);
			}
			else newStr = decimal;
			return newStr+underDecimal;
		}
	}
}
