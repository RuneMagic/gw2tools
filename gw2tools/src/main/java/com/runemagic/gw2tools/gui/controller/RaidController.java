package com.runemagic.gw2tools.gui.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.raid.CompositionBase;
import com.runemagic.gw2tools.raid.CompositionBuilder;
import com.runemagic.gw2tools.raid.RaidBoss;
import com.runemagic.gw2tools.raid.RaidComposition;
import com.runemagic.gw2tools.raid.RaidManager;
import com.runemagic.gw2tools.raid.RaidRole;
import com.runemagic.gw2tools.raid.SessionBase;

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
				RaidRole.NONSTAFF_ELEMENTALIST,
				RaidRole.NONSTAFF_ELEMENTALIST
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
				RaidRole.NONSTAFF_ELEMENTALIST
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

		//Skill:
		//5 - Skilled (perfect rotation/skill usage in every situation, knows every hidden tricks and such)
		//4 - Good (golem rotation is perfect, might make mistakes in raids but still good dps and utility usage)
		//3 - Okay (okay golem rotation, might have issues under pressure, barely or haven't played it in raids)
		//2 - Bad (bad golem rotation, never played in raids before)
		//1 - Terrible (geared but not used)

		//Preference:
		//5 - Very preferred
		//4 - If possible, want to play it
		//3 - Indifferent
		//2 - If possible, want to avoid it
		//1 - Play it only if it's really neccessary
		RaidManager manager = GW2Tools.inst().getRaidManager();
		manager.initialize();
		/*Set<RaidMember> members=new HashSet<RaidMember>();
		members.add(new RaidMember("Faeryn",
				new MemberBuild(RaidBuild.ELEMENTALIST_STAFF, 4, 3),
				new MemberBuild(RaidBuild.ELEMENTALIST_DAGGERWARHORN, 4, 3),
				new MemberBuild(RaidBuild.GUARDIAN_HAMMER, 5, 5),
				new MemberBuild(RaidBuild.GUARDIAN_SCEPTERTORCH, 3, 3),
				new MemberBuild(RaidBuild.GUARDIAN_SWORDTORCH, 3, 3),
				new MemberBuild(RaidBuild.MESMER_CHRONOMANCER, 2, 2),
				new MemberBuild(RaidBuild.MESMER_TANK, 2, 1),
				new MemberBuild(RaidBuild.MESMER_CONDITION_DAMAGE, 3, 3),
				new MemberBuild(RaidBuild.NECROMANCER_CONDITION_DAMAGE, 4, 5),
				new MemberBuild(RaidBuild.REVENANT_POWER, 5, 2),
				new MemberBuild(RaidBuild.RANGER_HEALER, 3, 4)
				));*/

		SessionBase base=new SessionBase(sessionBaseMap);
		for (RaidBoss boss:RaidBoss.values())
		{
			CompositionBuilder builder=new CompositionBuilder(base.getCompositionBase(boss), manager.getAllMembers());
			List<RaidComposition> comps=builder.getCompositions((raidRole, raidMember) -> raidMember.getRoleSkill(raidRole)>2 && raidMember.getRolePreference(raidRole)>2);
			System.out.println(boss+"="+comps.size());
		}
	}
}
