package com.github.clevernucleus.relicex.config;

import java.util.ArrayList;
import java.util.List;

import com.github.clevernucleus.relicex.RelicEx;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = RelicEx.MODID)
public final class RelicExConfig implements ConfigData {
	
	@ConfigEntry.Gui.Tooltip
	public boolean chestsHaveLoot = true;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsHaveRelicChance = 15;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsHaveLesserOrbChance = 5;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsHaveGreaterOrbChance = 1;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int chestsHaveTomeChance = 5;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dropsOnlyFromPlayerKills = false;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dragonDropsStone = true;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobsDropLootChance = 5;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobDropIsRelicChance = 50;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobDropIsPotionChance = 30;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobDropIsLesserOrbChance = 5;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobDropIsGreaterOrbChance = 1;
	
	@ConfigEntry.BoundedDiscrete(min = 0, max = 100)
	@ConfigEntry.Gui.Tooltip
	public int mobDropIsTomeChance = 10;
	
	@ConfigEntry.Gui.Tooltip
	public List<String> mobDropBlacklist = new ArrayList<String>();
}
