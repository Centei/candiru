# Candiru (for Fabric)

## **Work In Progress** (still in early development phase)

Candiru is a free and open source Minecraft plugin that gives a unique taste of the hardcore gamemode, while still being
vanilla-like. **Note** that this plugin is not desinged to replace the hardcore gamemode itself, rather altering how
health works instead. It is a **server-side only** plugin as of release 0.1.2.

## What is different?

Candiru changes these aspects of the vanilla experience:

* The health bar is now dynamic; max health can increase and decrease
* All deaths decrease health by 2 (1 heart)
* Health can be increased by increments of 2 every 3 advancements collected
* Max health cannot go higher than regular (20 health)
* Upon reaching **12** advancements, one can collect a single heart
* The minimum number of advancements required is 12

TLDR: The concept is similar to how Lifesteal SMP works.

## How to use?

* Each player has a heart bank that stores the current amount of hearts they have

    * To check

  `/hearts`
    * To use

  `/hearts use <count>`


* Help command

`/hearts help`

* Set the number of advancments on any online player (OP)

`/hearts set`

* Directly set the amount of health for any online player (OP)

`/health set <count>`

Additionally, health and hearts (stored as total advancements collected) can be changed for any offline player in
the `JsonFileHearts.json` and the `JsonFileHeartsStorage.json` files respectively. These files can be found the `.\mods`
of the minecraft server root directory.

## Installation

Install Fabric loader on your server and drag the stable release of this plugin to the mods directory. For help, see the
community guides.

### Building from source

#### Requirements

* Gradle 7
* JDK 17

Clone the repository and run the gradle `build` task. Binaries can be found in `build\libs`.

## Configuration

In progress, finish date TBD.

## License

Candiru is licensed under [GNU AGPLv3](https://www.gnu.org/licenses/agpl-3.0.txt), a free and open source license. For
more information, please see the `LICENSE` file.

Candiru uses a template licensed under the CC0 license. Check out the original
template [here](https://github.com/FabricMC/fabric-example-mod). 




