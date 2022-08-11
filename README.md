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

#### Misc

- Unwanted Relics can be smelted down into Relic Shards, which can be smashed to drop Experience Points.
- Health Potions cannot be stored, they heal the instant that they are picked up.
- The Dragon Stone drops from the Ender Dragon upon death (configurable). Care should be taken when handling it as using it will reset the player's attributes, Levels and Skill Points. For this sake a safety is implemented, such that it must be right-clicked twice.

#### Contributors/Credits

- [Bonsaiheldin](https://opengameart.org/content/shiny-rpg-potions-16x16)
- [Joe Williamson](https://twitter.com/joecreates)