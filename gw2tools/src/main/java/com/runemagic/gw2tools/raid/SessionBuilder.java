package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class SessionBuilder
{
	private static final int MAX_SWAPS_BETWEEN_BOSSES=3;
	private static final int MAX_SWAPS_BETWEEN_WINGS=3;

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

	public List<RaidSession> getTopSessions()
	{
		List<RaidSession> sessions=buildSessions();
		System.out.println("sessions="+sessions.size());
		if (sessions.isEmpty()) return sessions;
		Collections.sort(sessions, (s1,s2)->{
			return Integer.compare(s1.getSwaps(),s2.getSwaps());
		});
		int lowestSwaps=sessions.get(0).getSwaps();
		System.out.println("lowest swaps="+lowestSwaps);
		int highestSwaps=sessions.get(3).getSwaps();
		System.out.println("highest swaps="+highestSwaps);
		List<RaidSession> tmp=new ArrayList<>();
		for (RaidSession session:sessions)
		{
			if (session.getSwaps()<=highestSwaps) tmp.add(session);
			else break;
		}
		Collections.sort(tmp, (s1,s2)->{
			return Float.compare(s2.getAveragePreference(),s1.getAveragePreference());
		});
		float highestPref=sessions.get(0).getAveragePreference();
		System.out.println("highest preference="+highestPref);
		float lowestPref=sessions.get(3).getAveragePreference();
		System.out.println("lowest preference="+lowestPref);
		List<RaidSession> ret=new ArrayList<>();
		for (RaidSession session:tmp)
		{
			if (session.getAveragePreference()>=lowestPref) ret.add(session);
			else break;
		}
		return ret;
	}

	public List<RaidSession> buildSessions()
	{
		Map<RaidBoss, List<RaidComposition>> comps=new HashMap<>();
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
			comps.put(boss, result);
		}

		Map<RaidWing, List<RaidWingComposition>> allWingComps=new HashMap<>();
		for (RaidWing wing:base.getRaidWings())
		{
			List<RaidWingComposition> wingComps=getAllWingCompositions(wing, comps);
			System.out.println(wing+"="+wingComps.size());
			allWingComps.put(wing, wingComps);
		}

		return getAllSessions(allWingComps);
	}

	private List<RaidSession> getAllSessions(Map<RaidWing, List<RaidWingComposition>> allComps)
	{
		List<RaidSession> ret=new LinkedList<>();
		List<RaidWing> wings=base.getRaidWings();
		int wingCount=wings.size();
		int[] indexes=new int[wingCount];
		int[] sizes=new int[wingCount];
		List<RaidWingComposition>[] comps=new List[wingCount];
		int i=0;
		for (RaidWing wing:wings)
		{
			indexes[i]=0;
			sizes[i]=allComps.get(wing).size();
			comps[i]=allComps.get(wing);
			i++;
		}
		int n=0;
		while (true)
		{
			Map<RaidBoss, RaidComposition> session=null;
			RaidWingComposition lastComp=null;
			int totalSwaps=0;
			for (i=0;i<wingCount;i++)
			{
				RaidWingComposition comp=comps[i].get(indexes[i]);
				if (lastComp!=null)
				{
					Integer swaps=getSwapsBetween(lastComp, comp, MAX_SWAPS_BETWEEN_WINGS);
					if (swaps==null)
					{
						session=null;
						break;
					}
					if (session==null)
					{
						session=new HashMap<>();
						session.putAll(lastComp.getCompositions());
						totalSwaps+=lastComp.getSwaps();
					}
					session.putAll(comp.getCompositions());
					totalSwaps+=comp.getSwaps();
				}
				lastComp=comp;
			}
			if (session!=null) ret.add(new RaidSession(base, session, totalSwaps));

			indexes[0]++;
			while (indexes[n] == sizes[n])
			{
				if (n == wingCount-1)
				{
					return ret;
				}
				indexes[n]=0;
				n++;
				indexes[n]++;
			}
			n=0;
		}
	}

	private List<RaidWingComposition> getAllWingCompositions(RaidWing wing, Map<RaidBoss, List<RaidComposition>> allComps)
	{
		List<RaidWingComposition> ret=new LinkedList<>();
		List<RaidBoss> bosses=wing.getBosses();
		int bossCount=bosses.size();
		int[] indexes=new int[bossCount];
		int[] sizes=new int[bossCount];
		List<RaidComposition>[] comps=new List[bossCount];
		int i=0;
		for (RaidBoss boss:bosses)
		{
			indexes[i]=0;
			sizes[i]=allComps.get(boss).size();
			comps[i]=allComps.get(boss);
			i++;
		}
		int n=0;
		while (true)
		{
			Map<RaidBoss, RaidComposition> wingComp=null;
			RaidComposition lastComp=null;
			int totalSwaps=0;
			for (i=0;i<bossCount;i++)
			{
				RaidComposition comp=comps[i].get(indexes[i]);
				if (lastComp!=null)
				{
					Integer swaps=getSwapsBetween(lastComp, comp, MAX_SWAPS_BETWEEN_BOSSES);
					if (swaps==null)
					{
						wingComp=null;
						break;
					}
					if (wingComp==null)
					{
						wingComp=new HashMap<>();
						wingComp.put(bosses.get(i-1), lastComp);
						totalSwaps+=swaps;
					}
					wingComp.put(bosses.get(i), comp);
					totalSwaps+=swaps;
				}
				lastComp=comp;
			}
			if (wingComp!=null) ret.add(new RaidWingComposition(wing, wingComp, totalSwaps));

			indexes[0]++;
			while (indexes[n] == sizes[n])
			{
				if (n == bossCount-1)
				{
					return ret;
				}
				indexes[n]=0;
				n++;
				indexes[n]++;
			}
			n=0;
		}
	}


	private Integer getSwapsBetween(RaidWingComposition comp1, RaidWingComposition comp2, int max)
	{
		return getSwapsBetween(comp1.getLastComposition(), comp2.getFirstComposition(), max);
	}

	private Integer getSwapsBetween(RaidComposition comp1, RaidComposition comp2, int max)
	{
		int swaps=0;
		for (RaidSlot slot2:comp2.getSlots())
		{
			if (!comp1.getSlots().contains(slot2)) swaps++;
			if (swaps>max) return null;
		}
		return swaps;
	}

	private static final class RaidWingComposition
	{
		private final RaidWing wing;
		private final Map<RaidBoss, RaidComposition> comps;
		private final int swaps;

		public RaidWingComposition(RaidWing wing, Map<RaidBoss, RaidComposition> comps, int swaps)
		{
			this.wing = wing;
			this.comps = comps;
			this.swaps=swaps;
		}

		public int getSwaps()
		{
			return swaps;
		}

		public RaidWing getWing()
		{
			return wing;
		}

		public RaidComposition getComposition(RaidBoss boss)
		{
			return comps.get(boss);
		}

		public RaidComposition getFirstComposition()
		{
			return comps.get(wing.getFirstBoss());
		}

		public RaidComposition getLastComposition()
		{
			return comps.get(wing.getLastBoss());
		}

		public Map<RaidBoss, RaidComposition> getCompositions()
		{
			return comps;
		}
	}
}
