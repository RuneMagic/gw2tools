package com.runemagic.gw2tools.gui.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.raid.RaidBoss;
import com.runemagic.gw2tools.raid.RaidComposition;
import com.runemagic.gw2tools.raid.RaidManager;
import com.runemagic.gw2tools.raid.RaidMember;
import com.runemagic.gw2tools.raid.RaidSession;
import com.runemagic.gw2tools.raid.RaidSlot;
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
		RaidManager manager = GW2Tools.inst().getRaidManager();
		manager.loadData();

		SessionBase base=manager.getSessionBase();
		SessionBuilder builder=manager.getSessionBuilder();

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
		List<RaidMember> members=new ArrayList<>(manager.getMembers());
		Collections.sort(members);
		for (RaidMember member:members)
		{
			System.out.print(member.getName());
			for (RaidBoss boss:base.getBosses())
			{
				RaidComposition comp=session.getComposition(boss);
				System.out.print(",");
				System.out.print(comp.getRole(member).getShortName());
			}
			System.out.println();
		}
	}

}
