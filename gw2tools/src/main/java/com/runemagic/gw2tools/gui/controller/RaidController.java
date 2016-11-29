package com.runemagic.gw2tools.gui.controller;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.raid.CompositionBase;
import com.runemagic.gw2tools.raid.RaidBoss;
import com.runemagic.gw2tools.raid.RaidManager;
import com.runemagic.gw2tools.raid.RaidRole;
import com.runemagic.gw2tools.raid.RaidWing;
import com.runemagic.gw2tools.raid.SessionBase;
import com.runemagic.gw2tools.raid.SessionBuilder;

public class RaidController
{

	@FXML
	private ListView<?> lstCompositions;

	public void initialize()
	{

	}

	@FXML
	void btnDoitAction(ActionEvent event)
	{
		Map<RaidBoss, CompositionBase> sessionBaseMap=new HashMap<>();
		sessionBaseMap.put(RaidBoss.VALE_GUARDIAN, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.HAMMER_DRAGONHUNTER,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.GORSEVAL, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.SABETHA, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.HAMMER_DRAGONHUNTER,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.SLOTHASOR, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.BANDIT_TRIO, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.REVENANT,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.STAFF_ELEMENTALIST
		));
		sessionBaseMap.put(RaidBoss.MATTHIAS, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.REVENANT,
				RaidRole.REVENANT,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.MCLEOD, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.STAFF_ELEMENTALIST
		));
		sessionBaseMap.put(RaidBoss.KEEP_CONSTRUCT, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.POWER_DAMAGE,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.STAFF_ELEMENTALIST
		));
		sessionBaseMap.put(RaidBoss.XERA, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER,
				RaidRole.HEALER,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.STAFF_ELEMENTALIST,
				RaidRole.HAMMER_DRAGONHUNTER
		));
		RaidManager manager = GW2Tools.inst().getRaidManager();
		manager.initialize();

		SessionBase base=new SessionBase(sessionBaseMap, RaidWing.SPIRIT_VALE, RaidWing.SALVATION_PASS, RaidWing.STRONGHOLD_OF_THE_FAITHFUL);
		SessionBuilder builder = new SessionBuilder(base, manager.getAllMembers());
		System.out.println("sessions="+builder.buildSessions().size());
	}
}
