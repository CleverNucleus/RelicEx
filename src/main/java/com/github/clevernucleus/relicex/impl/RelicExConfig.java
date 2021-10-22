package com.github.clevernucleus.relicex.impl;

import com.github.clevernucleus.relicex.RelicEx;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = RelicEx.MODID)
public final class RelicExConfig implements ConfigData {
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int monstersDropRelicsChance = 15;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int monstersDropHealsChance = 10;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int monstersDropLesserOrbChance = 2;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int monstersDropGreaterOrbChance = 1;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int monstersDropTome = 5;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsDropRelicsChance = 25;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsDropLesserOrbChance = 5;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsDropGreaterOrbChance = 1;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsDropTome = 5;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dragonDropsStone = true;
	
	@ConfigEntry.Gui.Tooltip
	public boolean chestsHaveLoot = true;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dropsOnlyFromPlayerKill = false;
}
