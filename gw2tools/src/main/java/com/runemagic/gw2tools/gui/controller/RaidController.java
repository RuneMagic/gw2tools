package com.runemagic.gw2tools.gui.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.raid.CompositionBase;
import com.runemagic.gw2tools.raid.RaidBoss;
import com.runemagic.gw2tools.raid.RaidComposition;
import com.runemagic.gw2tools.raid.RaidManager;
import com.runemagic.gw2tools.raid.RaidMember;
import com.runemagic.gw2tools.raid.RaidRole;
import com.runemagic.gw2tools.raid.RaidSession;
import com.runemagic.gw2tools.raid.RaidSlot;
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
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.CONDI_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.CONDITION_DAMAGE,
				RaidRole.CONDITION_DAMAGE,
				RaidRole.HAMMER_DRAGONHUNTER,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.GORSEVAL, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.CONDITION_DAMAGE,
				RaidRole.RANGED_DPS,
				RaidRole.RANGED_DPS,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.SABETHA, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.HAMMER_DRAGONHUNTER,
				RaidRole.CONDITION_DAMAGE,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.SLOTHASOR, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
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
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.REVENANT,
				RaidRole.CONDITION_NECROMANCER,
				RaidRole.RANGED_DPS,
				RaidRole.RANGED_DPS
		));
		sessionBaseMap.put(RaidBoss.MATTHIAS, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
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
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE,
				RaidRole.CONDITION_DAMAGE,
				RaidRole.RANGED_DPS
		));
		sessionBaseMap.put(RaidBoss.KEEP_CONSTRUCT, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE,
				RaidRole.POWER_DAMAGE
		));
		sessionBaseMap.put(RaidBoss.XERA, new CompositionBase(
				RaidRole.CHRONOTANK,
				RaidRole.CHRONOMANCER,
				RaidRole.HEALER_DRUID,
				RaidRole.HEALER_DRUID,
				RaidRole.PS_WARRIOR,
				RaidRole.PS_WARRIOR,
				RaidRole.RANGED_DPS,
				RaidRole.RANGED_DPS,
				RaidRole.RANGED_DPS,
				RaidRole.HAMMER_DRAGONHUNTER
		));
		RaidManager manager = GW2Tools.inst().getRaidManager();
		manager.initialize();

		SessionBase base=new SessionBase(sessionBaseMap, RaidWing.SPIRIT_VALE, RaidWing.SALVATION_PASS, RaidWing.STRONGHOLD_OF_THE_FAITHFUL);
		SessionBuilder builder = new SessionBuilder(base, manager.getAllMembers());
		List<RaidSession> sessions=builder.getTopSessions();
		System.out.println("top sessions="+sessions.size());
		RaidSession session=sessions.get(0);
		System.out.println("##### Roles per member #####");
		for (RaidBoss boss:base.getBosses())
		{
			RaidComposition comp=session.getComposition(boss);
			System.out.println(boss);
			for (RaidSlot slot:comp.getSlots())
			{
				System.out.println(slot.getRole()+"="+slot.getMember().getName());
			}
		}
		System.out.println("###########################");
		List<RaidMember> members=new ArrayList<>(manager.getAllMembers());
		Collections.sort(members);
		for (RaidMember member:members)
		{
			System.out.print(member.getName());
			for (RaidBoss boss:base.getBosses())
			{
				RaidComposition comp=session.getComposition(boss);
				System.out.print("|");
				System.out.print(comp.getRole(member));
			}
			System.out.println();
		}
	}

}
