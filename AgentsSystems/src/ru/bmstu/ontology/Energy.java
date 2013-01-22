package ru.bmstu.ontology;
import jade.content.*;
public class Energy implements AgentAction  {


	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String needToBuy;
	private int price;
	private int quantity;
	
	public String getNeedToBuy() {
		return needToBuy;
	}
	public void setNeedToBuy(String needToBuy) {
		this.needToBuy = needToBuy;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	}
	
