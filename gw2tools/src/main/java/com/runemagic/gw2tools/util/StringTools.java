package com.runemagic.gw2tools.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class StringTools
{
 	private StringTools(){}

	public static String formatMoney(int coins)
	{
		return formatMoney(coins, false);
	}

	public static String formatMoney(int coins, boolean showAll)
	{
		boolean neg=false;
		if (coins<0)
		{
			neg=true;
			coins=Math.abs(coins);
		}
		int c=coins%100;
		coins/=100;
		int s=coins%100;
		coins/=100;
		int g=coins;
		StringBuilder sb=new StringBuilder();
		boolean force=showAll;
		if (neg) sb.append("-");
		if (g>0 || force)
		{
			sb.append(g);
			sb.append("g ");
			force=true;
		}
		if (s>0 || force)
		{
			sb.append(s);
			sb.append("s ");
			force=true;
		}
		if (c>0 || force)
		{
			sb.append(c);
			sb.append("c");
		}
		String ret=sb.toString().trim();
		if (ret.isEmpty()) return "0c";
		else return ret;
	}

	public static String formatSecondsLong(long time)
	{
		return formatSecondsLong(time, " ", false);
	}

	public static String formatSecondsLong(long time, String delimiter, boolean days)
	{
		if (time<0) time=0;
		//NumberFormat formatter = new DecimalFormat("00");
		long s=time%60;
		time/=60;
		long m=time%60;
		time/=60;
		long h;
		long d;
		if (days)
		{
			h=time%24;
			time/=24;
			d=time;
		}
		else
		{
			h=time;
			d=0;
		}


		StringBuilder sb=new StringBuilder();
		if (d>0) {sb.append(d);sb.append("d");sb.append(delimiter);}
		if (h>0) {sb.append(h);sb.append("h");sb.append(delimiter);}
		if (m>0) {sb.append(m);sb.append("m");sb.append(delimiter);}
		if (s>0) {sb.append(s);sb.append("s");}
		return sb.toString().trim();
	}

	public static String formatSeconds(int time)
	{
		if (time<0) time=0;
		NumberFormat formatter = new DecimalFormat("00");
		int s=time%60;
		time/=60;
		int m=time%60;
		time/=60;
		int h=time;
		return (h>0?h+":":"")+m+":"+formatter.format(s);
	}

	public static String formatMilliseconds(int time)
	{
		return formatMilliseconds(time, false);
	}

	public static String formatMilliseconds(int time, boolean seconds)
	{
		if (time<0) time=0;
		String sec=formatSeconds(time/1000);
		if (seconds) return sec;
		int ms=time%1000;
		return sec+":"+ms;
	}
}
