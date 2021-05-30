# Custom Configuration

Technically, instances of `OreBlock` and `RedstoneOreBlock` are configured as 30-level light.
However, you can overwrite it or make your custom blocks bright.
Just create a resource pack. Put your JSON file into `assets/<namespace>/ore_tags/<any_name_you_want>.json`.
Here's an example:

```json5
[
    "minecraft:raw_copper_block",                     // let raw copper block shine as 30-level light
    {
        "light": 15,
        "value": "minecraft:crying_obsidian",
        "required": false                             // if false, will be ignored if the block does not exist or any else error occurs.
    },
    {
        "light": 30,
        "value": "#guarded_by_piglins"                // can be a tag
    },
    "#minecraft:enderman_holdable",
    {
        "value": "shurlin:pear_log",
        "required": false                             // so that game won't crash when Shurlin Mod is not loaded
    }
]
```