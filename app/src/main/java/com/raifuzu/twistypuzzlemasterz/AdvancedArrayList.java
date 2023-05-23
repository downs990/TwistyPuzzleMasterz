package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;

public class AdvancedArrayList<T> extends ArrayList<T> {


	/**
	 * The purpose of this class is to be able to create collections and cleanly
	 * add multiple elements to it just like you can with a normal array.
	 * 
	 */

	public AdvancedArrayList() {
		super();
	}

	@SafeVarargs
	public AdvancedArrayList(T[]... array) {

		for (T[] a : array) {
			for (int i = 0; i < a.length; i++) {
				this.add(a[i]);
			}
		}
	}

	// Deep copy constructor
	public AdvancedArrayList(AdvancedArrayList<T> oldList) {

		for (T a : oldList) {
			this.add(a);
		}
	}

	@SafeVarargs
	public AdvancedArrayList(AdvancedArrayList<T>... allList) {

		for (int i = 0; i < allList.length; i++) {
			for (T a : allList[i]) {
				this.add(a);
			}
		}

	}

	@SafeVarargs
	public AdvancedArrayList(ArrayList<T>... allList) {

		for (int i = 0; i < allList.length; i++) {
			for (T a : allList[i]) {
				this.add(a);
			}
		}

	}

	@SafeVarargs
	public AdvancedArrayList(T... elements) {
		for (T t : elements) {
			this.add(t);
		}

	}

	
	@SuppressWarnings("unchecked")
	public void addMultiple(T... elements){
		
		for(T element : elements){
			this.add(element);
		}
	}
	
	
	@Override
	public String toString() {
		String value = "";
		for (int i = 0; i < this.size(); i++) {
			if (i == this.size() - 1) {
				value += this.get(i);
			} else {
				value += this.get(i) + " ";
			}
		}
		return value.trim();
	}

}
