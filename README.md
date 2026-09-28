# Litematica Auto Rotate

Client-side add-on for Litematica. Right before a block is placed, it briefly turns the
player so the placed block faces the same way as in the schematic, then turns back.
Useful on servers without Carpet (set Litematica's `easyPlaceProtocol` to `None`).

The toggle (`easyPlaceAutoRotate`, default hotkey `J`) appears in Litematica's own
**Generic** config tab and is saved in `litematica.json`.

## Branches

Each loader + Minecraft version lives on its own branch and produces its own jar.

| Branch | Loader | Minecraft | Depends on | Jar |
|---|---|---|---|---|
| [`fabric+mc26.2`](../../tree/fabric+mc26.2) | Fabric | 26.2 | Litematica 0.28.x, MaLiLib 0.29.x, Fabric API | `litematica-autorotate-fabric-<ver>+mc26.2.jar` |
| [`neoforge+mc26.2`](../../tree/neoforge+mc26.2) | NeoForge | 26.2 | Forgematica 0.5.x, MaFgLib 0.5.x, FoxifiedClassTweaker | `litematica-autorotate-neoforge-<ver>+mc26.2.jar` |
| [`neoforge+mc1.21.1`](../../tree/neoforge+mc1.21.1) | NeoForge | 1.21.1 | Forgematica 0.4.x, MaFgLib 0.4.x | `litematica-autorotate-neoforge-<ver>+mc1.21.1.jar` |

Downloads: see [Releases](../../releases).

## Building

**Fabric** (`fabric+mc26.2` branch): `./build.sh` compiles with plain `javac` against the jars in
your Prism Launcher install. `./build.sh --install "<instance name>"` also copies the jar
into that instance's `mods` folder.

**NeoForge** (`neoforge+mc26.2` and `neoforge+mc1.21.1` branches): `./gradlew build` (Gradle needs JDK 21 to run;
the 26.2 branch compiles with a Java 25 toolchain, downloaded automatically if missing). The jar lands in
`build/libs/`. `./gradlew runClient` starts a dev client with Forgematica and MaFgLib.

## License

CC0-1.0
