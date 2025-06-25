
# Revolution Mod by Tr3yMix

A story-driven progression mod for Minecraft 1.20.1 that overhauls early survival with realistic crafting, clay mold systems, and dimension-based narrative goals.

## Features
- Primitive crafting with clay molds and flint tools
- Dimension restoration with portal crafting and storyline quests
- Realistic survival mechanics and time-based world progression
- Custom recipe types: carving, casting, shaping
- Villager questlines and portal hub dimension (Emerald Isle)

## Development Setup

### Requirements
- Minecraft 1.20.1
- Forge MDK 47.x.x
- Java 17+
- IntelliJ IDEA or Eclipse

### Setup Instructions

#### IntelliJ IDEA
1. Clone the repository
2. Run `./gradlew genIntellijRuns`
3. Open `build.gradle` in IntelliJ and let it import
4. Sync Gradle and refresh the project

#### Eclipse
1. Clone the repository
2. Run `./gradlew genEclipseRuns`
3. Open Eclipse > Import Gradle Project > Select project folder

If libraries fail to load, use:
```sh
./gradlew --refresh-dependencies
./gradlew clean
