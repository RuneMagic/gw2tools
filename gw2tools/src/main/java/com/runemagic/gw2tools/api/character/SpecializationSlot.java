package com.runemagic.gw2tools.api.character;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.runemagic.gw2tools.api.GW2API;

public class SpecializationSlot
{
	private ObjectProperty<Integer> id=new SimpleObjectProperty<>();
	private ObjectProperty<SpecializationInfo> specInfo=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterTrait> trait1=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterTrait> trait2=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterTrait> trait3=new SimpleObjectProperty<>();

	public SpecializationSlot()
	{
		specInfo.bind(Bindings.createObjectBinding(()->GW2API.inst().getSpecialization(this.id.get()), this.id));
		//TODO bind id/trait changes and validate
	}

	public Integer getId()
	{
		return id.get();
	}

	public ObjectProperty<Integer> idProperty()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id.set(id);
	}

	public SpecializationInfo getSpecInfo()
	{
		return specInfo.get();
	}

	public ReadOnlyObjectProperty<SpecializationInfo> specInfoProperty()
	{
		return specInfo;
	}

	public CharacterTrait getTrait1()
	{
		return trait1.get();
	}

	public ObjectProperty<CharacterTrait> trait1Property()
	{
		return trait1;
	}

	public void setTrait1(CharacterTrait trait1)
	{
		this.trait1.set(trait1);
	}

	public CharacterTrait getTrait2()
	{
		return trait2.get();
	}

	public ObjectProperty<CharacterTrait> trait2Property()
	{
		return trait2;
	}

	public void setTrait2(CharacterTrait trait2)
	{
		this.trait2.set(trait2);
	}

	public CharacterTrait getTrait3()
	{
		return trait3.get();
	}

	public ObjectProperty<CharacterTrait> trait3Property()
	{
		return trait3;
	}

	public void setTrait3(CharacterTrait trait3)
	{
		this.trait3.set(trait3);
	}
}
