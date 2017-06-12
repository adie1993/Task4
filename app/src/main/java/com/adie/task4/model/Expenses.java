package com.adie.task4.model;


import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Expenses {

	public String description;
	public int amount;


	public Expenses() {

	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}


}
