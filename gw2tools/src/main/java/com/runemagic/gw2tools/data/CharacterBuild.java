package com.runemagic.gw2tools.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CharacterBuild
{
	private ObjectProperty<CharacterSpecialization> spec1=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterSpecialization> spec2=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterSpecialization> spec3=new SimpleObjectProperty<>();


	public CharacterBuild()
	{

	}

	public CharacterSpecialization getSpec1()
	{
		return spec1.get();
	}

	public ReadOnlyObjectProperty<CharacterSpecialization> spec1Property()
	{
		return spec1;
	}

	public void setSpec1(CharacterSpecialization spec1)
	{
		this.spec1.set(spec1);
	}

	public CharacterSpecialization getSpec2()
	{
		return spec2.get();
	}

	public ReadOnlyObjectProperty<CharacterSpecialization> spec2Property()
	{
		return spec2;
	}

	public void setSpec2(CharacterSpecialization spec2)
	{
		this.spec2.set(spec2);
	}

	public CharacterSpecialization getSpec3()
	{
		return spec3.get();
	}

	public ReadOnlyObjectProperty<CharacterSpecialization> spec3Property()
	{
		return spec3;
	}

	public void setSpec3(CharacterSpecialization spec3)
	{
		this.spec3.set(spec3);
	}
}
