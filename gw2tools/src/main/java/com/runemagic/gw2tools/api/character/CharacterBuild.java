package com.runemagic.gw2tools.api.character;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CharacterBuild
{
	private ObjectProperty<SpecializationSlot> spec1=new SimpleObjectProperty<>();
	private ObjectProperty<SpecializationSlot> spec2=new SimpleObjectProperty<>();
	private ObjectProperty<SpecializationSlot> spec3=new SimpleObjectProperty<>();


	public CharacterBuild()
	{
		spec1.set(new SpecializationSlot());
		spec2.set(new SpecializationSlot());
		spec3.set(new SpecializationSlot());
	}

	public SpecializationSlot getSpec1()
	{
		return spec1.get();
	}

	public ReadOnlyObjectProperty<SpecializationSlot> spec1Property()
	{
		return spec1;
	}

	public SpecializationSlot getSpec2()
	{
		return spec2.get();
	}

	public ReadOnlyObjectProperty<SpecializationSlot> spec2Property()
	{
		return spec2;
	}

	public SpecializationSlot getSpec3()
	{
		return spec3.get();
	}

	public ReadOnlyObjectProperty<SpecializationSlot> spec3Property()
	{
		return spec3;
	}

}
