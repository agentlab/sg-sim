package ru.bmstu.objectToSell;

import java.io.Serializable;

public class Electricity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID =123443L;
	public int price;
	public int quantity;
	public int getPrice() {
		return price;
	}
	public Electricity(int price, int quantity) {
		this.price = price;
		this.quantity = quantity;
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
