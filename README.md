<img src="util/logo.png" alt="RelicEx" height="100" />
<hr />

[![GitHub license](https://img.shields.io/github/license/CleverNucleus/RelicEx?style=flat-square)](https://github.com/CleverNucleus/RelicEx/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/CleverNucleus/RelicEx?style=flat-square)](https://github.com/CleverNucleus/RelicEx/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/CleverNucleus/RelicEx?style=flat-square)](https://github.com/CleverNucleus/RelicEx/network)
[![GitHub issues](https://img.shields.io/github/issues/CleverNucleus/RelicEx?style=flat-square)](https://github.com/CleverNucleus/RelicEx/issues)

RelicEx is a Minecraft mod that implements Trinkets compatibility with PlayerEx, along a RPG theme. The mod adds the following:

| Item | Equipable | Obtainable | Description |
|:----:|:----:|:---------:|:------------|
| Helmet Relic | Helmet | Chests/Mobs | Provides 1-5 attribute bonuses.* |
| Amulet Relic | Necklace | Chests/Mobs | Provides 1-5 attribute bonuses.* |
| Chestplate Relic | Chest | Chests/Mobs | Provides 1-5 attribute bonuses.* |
| Ring Relic | Ring/Offhand Ring | Chests/Mobs | Provides 1-5 attribute bonuses.* |
| Relic Shard | None | Smelting Relic | Use to gain some Experience Points. |
| Tome | None | Chests/Mobs | Use to gain a free level. |
| Lesser Orb of Regret | None | Chests/Mobs | Use to refund a Skill Point. |
| Greater Orb of Regret | None | Chests/Mobs | Use to refund all Skill Points. |
| Dragon Stone | None | Ender Dragon | Use to reset all attributes, Skill Points and Level. |
| Small Health Potion | None | Mobs | Heals the player for 2 Hearts. |
| Medium Health Potion | None | Mobs | Heals the player for 3 Hearts. |
| Large Health Potion | None | Mobs | Heals the player for 4 hearts. |

#### Relics*

Relics have a chance to drop from mobs (any instance of `Monster`) and generate in loot chests (any loot table of type `chests/`). Relics have randomly generated attributes assigned to them, that are applied when equipped in their respective Trinket or Armour slot. The randomly generated attributes are taken from PlayerEx attributes, and have randomly generated values as well.

Relics come with a rarity system: Common, Uncommon, Rare, Epic, Mythical, Legendary and Immortal. Relics can have up to five attribute bonuses attached to them. Factors that influence a relic's rarity: the number of attributes attached to the relic, the bonus value attached to each attribute, and the attributes attached themselves also have a weight. 


All randomness and chance to drop is weighted and fully configurable.

#### Configuring Relic Attributes with Datapacks

The following can be configured using Datapacks:

- Which attributes are allowed to be attached to Relic items.
- The chance that a specific attribute will be attached to a Relic item.
- The attribute modifier type (`ADDITION` or `MULTIPLY_TOTAL`).
- The chance that the attribute modifier will be of a given modifier type.
- The minimum and maximum values allowed for a given attribute and modifier type.
- The increment value for attribute values.

Your datapack should be such that it contains `data/relicex/attributes/properties.json`. The `properties.json` file should be a copy/paste of the one contained in the relicex mod jar (see [here](https://github.com/CleverNucleus/RelicEx/blob/main/src/main/resources/data/relicex/attributes/properties.json)), and in this file you may add or subtract entries to add or remove attributes. Any attribute with the `weight` property will have a chance to be attached to a Relic item in-game.

The contents of the `weight` property defines all the aforementioned configuring options:

```json
"weight": "r65:a70:x1:y10:z1:u0.05:v0.5:w0.01"
```

What do these numbers mean?

| Letter | Min/Max | Description |
| :----: | :---: | :---------- |
| `r` | 1/100 | How rarely this attribute should be attached to a Relic item. |
| `a` | 0/100 | The chance that this attribute modifier will be `ADDITION`. Set to 100 to disable `MULTIPLY_TOTAL` modifier types, or 0 to disable `ADDITION` types. The modifier can only be either `ADDITION` or `MULTIPLY_TOTAL`. |
| `x` | 0/* | The minimum value this attribute modifier can have, for `ADDITION` type modifiers. |
| `y` | 0/* | The maximum value this attribute modifier can have, for `ADDITION` type modifiers. |
| `z` | 0/* | The increment** value for this attribute modifier, for `ADDITION` type modifiers. |
| `u` | 0/* | The minimum value this attribute modifier can have, for `MULTIPLY_TOTAL` type modifiers. |
| `v` | 0/* | The maximum value this attribute modifier can have, for `MULTIPLY_TOTAL` type modifiers. |
| `w` | 0/* | The increment** value for this attribute modifier, for `MULTIPLY_TOTAL` type modifiers. |

___*The minimum value should be less than both the increment and max values. The maximum value should be greater than the minimum and increment values, such that `min < increment < max`.___

___**The increment value should be a multiple of the max value, such that if the max value is `10`, the increment value cannot be `1.5`. Or, if the max value is `0.5`, the increment value could be `0.01`, but not `0.03`.___

Note that the colon, `:`, acts as a separator, and the letters are there to help the user know which numbers mean what. The order of the numbers ___must not change___. 

#### Misc

- Unwanted Relics can be smelted down into Relic Shards, which can be smashed to drop Experience Points.
- Health Potions cannot be stored, they heal the instant that they are picked up.
- The Dragon Stone drops from the Ender Dragon upon death (configurable). Care should be taken when handling it as using it will reset the player's attributes, Levels and Skill Points. For this sake a safety is implemented, such that it must be right-clicked twice.

#### Contributors/Credits

- [Bonsaiheldin](https://opengameart.org/content/shiny-rpg-potions-16x16)
- [Joe Williamson](https://twitter.com/joecreates)