package com.runemagic.gw2tools.api.character;

public enum EquipmentSlot
{
	HELM_AQUATIC("HelmAquatic"),
	BACKPACK("Backpack"),
	COAT("Coat"),
	BOOTS("Boots"),
	GLOVES("Gloves"),
	HELM("Helm"),
	LEGGINGS("Leggings"),
	SHOULDERS("Shoulders"),
	ACCESSORY1("Accessory1"),
	ACCESSORY2("Accessory2"),
	RING1("Ring1"),
	RING2("Ring2"),
	AMULET("Amulet"),
	WEAPON_AQUATIC_A("WeaponAquaticA"),
	WEAPON_AQUATIC_B("WeaponAquaticB"),
	WEAPON_A1("WeaponA1"),
	WEAPON_A2("WeaponA2"),
	WEAPON_B1("WeaponB1"),
	WEAPON_B2("WeaponB2"),
	SICKLE("Sickle"),
	AXE("Axe"),
	PICK("Pick");

	private final String name;

	EquipmentSlot(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}
}
