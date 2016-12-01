package com.runemagic.gw2tools.gui.components;

import javafx.scene.control.Label;

import com.runemagic.gw2tools.raid.RaidSlot;

public class RaidSlotView extends Label
{
	private final RaidSlot slot;

	public RaidSlotView(RaidSlot slot)
	{
		super(slot.getRole().getName()+": "+slot.getMember().getName());
		this.slot = slot;
	}
}
