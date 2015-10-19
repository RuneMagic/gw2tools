package com.runemagic.gw2tools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.GW2APISourceProfiler;

public class GW2APIProfiler
{
	private static final Logger log = LoggerFactory.getLogger(GW2APIProfiler.class);

	private GW2APISourceProfiler src1;
	private GW2APISourceProfiler src2;

	private boolean enabled;

	public GW2APIProfiler()
	{
		enabled=GW2Tools.inst().getAppSettings().profilerEnabled.get();
	}

	public GW2APISource watchAPISource1(GW2APISource src)
	{
		if (!enabled) return src;
		if (!(src instanceof GW2APISourceProfiler)) src=new GW2APISourceProfiler(src);
		this.src1=(GW2APISourceProfiler)src;
		return src;
	}

	public GW2APISource watchAPISource2(GW2APISource src)
	{
		if (!enabled) return src;
		if (!(src instanceof GW2APISourceProfiler)) src=new GW2APISourceProfiler(src);
		this.src2=(GW2APISourceProfiler)src;
		return src;
	}

	public boolean hasSource1()
	{
		return src1!=null;
	}

	public boolean hasSource2()
	{
		return src2!=null;
	}

	public String compileStatistics()
	{
		if (!enabled) return "Profiler disabled";
		if (!hasSource1() && !hasSource2()) return "No API sources connected";
		if (!hasSource1()) return "No Source 1 connected"; //TODO make this "smarter"
		StringBuilder sb=new StringBuilder();//TODO consider using tokens...
		sb.append("##### GW2 API Profiler Start #####\n");
		sb.append("V2 API calls:\n");
		sb.append(" - Count: ");
		sb.append(getSource1APIv2Calls());
		if (hasSource2())
		{
			sb.append("->");
			sb.append(getSource2APIv2Calls());
			sb.append("(");
			sb.append(getSource2APIv2Calls()-getSource1APIv2Calls());
			sb.append(")");
		}
		sb.append("\n");
		sb.append("V2 API Authenticated calls:\n");
		sb.append(" - Count: ");
		sb.append(getSource1APIv2AuthCalls());
		if (hasSource2())
		{
			sb.append("->");
			sb.append(getSource2APIv2AuthCalls());
			sb.append("(");
			sb.append(getSource2APIv2AuthCalls()-getSource1APIv2AuthCalls());
			sb.append(")");
		}
		sb.append("\n");
		sb.append("V1 API calls:\n");
		sb.append(" - Count: ");
		sb.append(getSource1APIv1Calls());
		if (hasSource2())
		{
			sb.append("->");
			sb.append(getSource2APIv1Calls());
			sb.append("(");
			sb.append(getSource2APIv1Calls()-getSource1APIv1Calls());
			sb.append(")");
		}
		sb.append("\n");
		sb.append("Total calls:\n");
		sb.append(" - Count: ");
		sb.append(getSource1TotalCalls());
		if (hasSource2())
		{
			sb.append("->");
			sb.append(getSource2TotalCalls());
			sb.append("(");
			sb.append(getSource2TotalCalls()-getSource1TotalCalls());
			sb.append(")");
		}
		sb.append("\n");
		sb.append("##### GW2 API Profiler End #####");
		return sb.toString();
	}

	public void printStatistics()
	{
		if (!enabled) return;
		log.info(compileStatistics());
	}

	public long getSource1TotalCalls()
	{
		return src1.getTotalCalls();
	}

	public long getSource1APIv2Calls()
	{
		return src1.getAPIv2Calls();
	}

	public long getSource1APIv2AuthCalls()
	{
		return src1.getAPIv2AuthCalls();
	}

	public long getSource1APIv1Calls()
	{
		return src1.getAPIv1Calls();
	}

	public long getSource2TotalCalls()
	{
		return src2.getTotalCalls();
	}

	public long getSource2APIv2Calls()
	{
		return src2.getAPIv2Calls();
	}

	public long getSource2APIv2AuthCalls()
	{
		return src2.getAPIv2AuthCalls();
	}

	public long getSource2APIv1Calls()
	{
		return src2.getAPIv1Calls();
	}

	public boolean isEnabled()
	{
		return enabled;
	}
}
