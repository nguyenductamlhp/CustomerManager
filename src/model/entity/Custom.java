package model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the custom database table.
 * 
 */
@Entity
@NamedQuery(name="Custom.findAll", query="SELECT c FROM Custom c")
public class Custom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String phone;

	private String address;

	private String name;

	private String note;

	public Custom() {
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}