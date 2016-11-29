package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class SessionBuilder
{
	private final SessionBase base;
	private final Set<RaidMember> members;

	public SessionBuilder(SessionBase base, Set<RaidMember> members)
	{
		this.base=base;
		this.members = ImmutableSet.copyOf(members);
	}

	private boolean filterComp(RaidComposition comp)
	{
		return comp.getAverageSkill()>4
				&& comp.getAveragePreference()>3
				&& comp.getMinimumSkill()>3
				&& comp.getMinimumPreference()>2;
	}

	public List<RaidSession> buildSessions()
	{
		Set<RaidSession> sessions=new HashSet<>();
		List<BossCompositionCandidates> comps=new LinkedList<>();
		Iterator<RaidComposition> lastBossIter=null;
		for (RaidBoss boss:base.getBosses())
		{
			CompositionBuilder builder=new CompositionBuilder(base.getCompositionBase(boss), members);
			Set<RaidComposition> bossComp=builder.getCompositions(
					(raidRole, raidMember) -> raidMember.getRoleSkill(raidRole)>2 && raidMember.getRolePreference(raidRole)>2,
					this::filterComp);
			if (bossComp.isEmpty()) continue;
			//System.out.println(boss+"="+bossComp.size());
			List<RaidComposition> sorted = new ArrayList<>(bossComp);
			Collections.sort(sorted, (RaidComposition comp1, RaidComposition comp2) ->
			{
				return Float.compare(comp2.getAveragePreference(), comp1.getAveragePreference());
			});
			List<RaidComposition> result = new LinkedList<>();
			int n=0;
			while (result.size()<10 && n<sorted.size())
			{
				float highestAvgPref = sorted.get(n).getAveragePreference();
				for (;n<sorted.size();n++)
				{
					RaidComposition comp=sorted.get(n);
					if (Math.abs(comp.getAveragePreference() - highestAvgPref) >= 0.00001f)
					{
						n++;
						break;
					}
					result.add(comp);
				}
			}
			System.out.println(boss+"="+result.size());
			comps.add(new BossCompositionCandidates(boss, result));
		}

		/*int minSize=Integer.MAX_VALUE;
		RaidBoss minBoss;
		for (BossCompositionCandidates comp:comps)
		{
			int size=comp.count();
			if (size<minSize)
			{
				minSize=size;
				minBoss=comp.getBoss();
			}
		}*/

	/*	int maxAllowedSwaps=3;
		BossCompositionCandidates lastCompCandidates=null;
		Map<RaidBoss, RaidComposition> bossComps;
		for (BossCompositionCandidates compCandidates:comps)
		{
			if (lastCompCandidates!=null)
			{
				for (RaidComposition comp2:lastCompCandidates.getComps())
				{

					Iterator<RaidComposition> compIter=compCandidates.getComps().iterator();
					while (compIter.hasNext())
					{
						RaidComposition comp=compIter.next();
						if (comp.equals(comp2))
						{
							compIter.remove();
							//bossComps
							break;
						}
						int swaps = 0;
						for (RaidSlot slot:comp.getSlots())
						{
							for (RaidSlot slot2:comp2.getSlots())
							{
								if (!slot.equals(slot2))
								{
									swaps++;
									break;
								}
							}
							//if (swaps>maxAllowedSwaps)
						}

					}
				}
			}
			lastCompCandidates=compCandidates;
		}*/

		return new ArrayList<RaidSession>(sessions);
	}

	/*private List<RaidSession> findClosestCompChains(RaidComposition comp, BossCompositionCandidates compList, int maxAllowedSwaps)
	{
		List<RaidComposition> ret=new ArrayList<RaidComposition>();
		Map<RaidBoss, RaidComposition> bossComps = new HashMap<>();
		Set<RaidSlot> slots=comp.getSlots();
		//we are trying to find compositions from compList that are closer than maxAllowedSwaps
		for (RaidComposition comp2:compList.getComps())//for every composition in compList
		{
			int swaps=0;
			for (RaidSlot slot2:comp2.getSlots())
			{
				if (!slots.contains(slot2)) swaps++;
				if (swaps>maxAllowedSwaps) break;
			}
			if (swaps<=maxAllowedSwaps)
			{

			}
		}

	}*/

	/*private int getSwapsBetween(RaidComposition comp1, RaidComposition comp2)
	{

	}

	private boolean matcher(RaidComposition comp1, RaidComposition comp2)
	{
		return getSwapsBetween(comp1, comp2)<3;
	}

	private Iterator<RaidComposition> getMatchingCompositions(Iterator<RaidComposition> iter1, Iterator<RaidComposition> iter2, BiPredicate<RaidComposition, RaidComposition> matcher)
	{
		return Iterators.filter(iter2, (RaidComposition item2)->{
			while (iter1.hasNext())
			{
				RaidComposition item1=iter1.next();
				if (!matcher.test(item1, item2)) return false;
			}
			return true;
		});
	}*/
	private static final class BossCompositionCandidates
	{
		private final RaidBoss boss;
		private final List<RaidComposition> comps;

		public BossCompositionCandidates(RaidBoss boss, List<RaidComposition> comps)
		{
			this.boss = boss;
			this.comps = comps;
		}

		public RaidBoss getBoss()
		{
			return boss;
		}

		public List<RaidComposition> getComps()
		{
			return comps;
		}

		public int count()
		{
			return comps.size();
		}
	}
}
