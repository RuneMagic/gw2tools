package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.apache.commons.collections4.iterators.PermutationIterator;

public class CompositionBuilder
{
	private final CompositionBase base;
	private final Set<RaidMember> raiders;

	public CompositionBuilder(CompositionBase base, Set<RaidMember> raiders)
	{
		this.base=base;
		this.raiders=new HashSet<>(raiders);
	}

	private int getMaxPreference(RaidRole role)
	{
		int ret=0;
		for (RaidMember raider:raiders)
		{
			int pref=raider.getRolePreference(role);
			if (pref>ret) ret=pref;
		}
		return ret;
	}

	private int getMaxSkill(RaidRole role)
	{
		int ret=0;
		for (RaidMember raider:raiders)
		{
			int pref=raider.getRoleSkill(role);
			if (pref>ret) ret=pref;
		}
		return ret;
	}

	public Set<RaidComposition> getCompositions(BiPredicate<RaidRole, RaidMember> roleFilter, Predicate<RaidComposition> compositionFilter)
	{
		Set<RaidComposition> result=new HashSet<>();
		PermutationIterator<RaidMember> perm=new PermutationIterator<RaidMember>(raiders);
		while (perm.hasNext())
		{
			List<RaidMember> list=perm.next();
			if (!matchesBaseComposition(list, roleFilter)) continue;
			//at this point we have a valid composition
			RaidComposition comp=createComposition(list);
			if (compositionFilter.test(comp)) result.add(comp);
		}
		return result;
	}

	private RaidComposition createComposition(List<RaidMember> list)
	{
		List<RaidSlot> slots=new LinkedList<>();
		List<RaidRole> roles=base.getRoles();
		for (int i=0;i<roles.size();i++)
		{
			RaidRole role=roles.get(i);
			RaidMember member=list.get(i);
			slots.add(new RaidSlot(role, member));
		}
		return new RaidComposition(slots);
	}

	private boolean matchesBaseComposition(List<RaidMember> list, BiPredicate<RaidRole, RaidMember> roleFilter)
	{
		List<RaidRole> roles=base.getRoles();
		for (int i=0;i<roles.size();i++)
		{
			RaidRole role=roles.get(i);
			RaidMember member=list.get(i);
			if (!member.hasRole(role)) return false;
			if (roleFilter!=null && !roleFilter.test(role, member)) return false;
		}
		return true;
	}

	public Set<RaidComposition> getRandomCompositions(int max)
	{
		Set<RaidComposition> comps=new HashSet<>();
		List<RaidMember> raiderList=new ArrayList<RaidMember>(raiders);
		int i=0;

		while (i<max)
		{
			Collections.shuffle(raiderList, new Random());
			List<RaidSlot> comp = new ArrayList<>();
			List<RaidMember> availableRaiders = new ArrayList<>(raiderList);
			LinkedList<RoleCandidates> slots = new LinkedList<>();
			for (RaidRole role : base.getRoles())
			{
				slots.add(new RoleCandidates(role, availableRaiders));
			}
			while (!slots.isEmpty())
			{
				Collections.sort(slots, (o1, o2) ->
				{
					return Integer.compare(o1.getMemberCount(), o2.getMemberCount());
				});
				RoleCandidates slot = slots.pop();
				RaidMember member = slot.getSlotMember();
				if (member == null) break;
				availableRaiders.remove(member);
				comp.add(new RaidSlot(slot.getRole(), member));
			}
			if (comp.size() == base.getRaidSize()) comps.add(new RaidComposition(comp));
		}
		return comps;
	}

	private final static class RoleCandidates
	{
		private final RaidRole role;
		private final Collection<RaidMember> pool;

		public RoleCandidates(RaidRole role, Collection<RaidMember> pool)
		{
			this.role = role;
			this.pool = pool;
		}

		public RaidRole getRole()
		{
			return role;
		}

		public int getMemberCount()
		{
			return getCandidates().size();
		}

		private float calculateSortValue(RaidMember member)
		{
			return member.getRolePreference(role)+member.getRoleSkill(role);
		}

		public List<RaidMember> getCandidates()
		{
			List<RaidMember> list=new ArrayList<>();
			for (RaidMember member:pool)
			{
				if (member.hasRole(role)) list.add(member);
			}
			return list;
		}

		public RaidMember getSlotMember()
		{
			List<RaidMember> list=getCandidates();
			if (list.isEmpty()) return null;
			if (list.size()>1)
			{
				Collections.sort(list, (o1, o2) ->
				{
					return Float.compare(calculateSortValue(o2), calculateSortValue(o1));
				});
			}
			return list.get(0);
		}
	}

}
