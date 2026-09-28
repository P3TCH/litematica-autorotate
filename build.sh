#!/usr/bin/env bash
# Builds the mod with plain javac against the jars Prism Launcher already has (no Gradle).
# Usage: ./build.sh [--install "<Prism instance name>"]
set -euo pipefail
cd "$(dirname "$0")"

PRISM="$HOME/Library/Application Support/PrismLauncher"
JDK="$PRISM/java/java-runtime-epsilon/jre.bundle/Contents/Home/bin"
LIBS="$PRISM/libraries"
MODS="$PRISM/instances/26.2 (clone)/minecraft/mods"
MC=26.2
VERSION=1.1.0
OUT="build/litematica-autorotate-fabric-$VERSION+mc$MC.jar"

rm -rf build
mkdir -p build/classes build/deps

unzip -q -o "$(ls "$MODS"/fabric-api-*.jar | head -1)" \
  'META-INF/jars/fabric-lifecycle-events-v1-*' \
  'META-INF/jars/fabric-api-base-*' \
  -d build/deps
unzip -q -o "$LIBS/net/fabricmc/fabric-loader/0.19.3/fabric-loader-0.19.3.jar" \
  'META-INF/jars/mixinextras-*' \
  -d build/deps

CP=$(LIBS="$LIBS" python3 - <<'EOF'
import json, os
libs = os.environ["LIBS"]
meta = json.load(open(os.path.expanduser("~/Library/Application Support/PrismLauncher/meta/net.minecraft/26.2.json")))
paths = []
for lib in meta["libraries"]:
    parts = lib["name"].split(":")
    if len(parts) != 3:
        continue  # natives
    group, artifact, version = parts
    p = os.path.join(libs, *group.split("."), artifact, version, f"{artifact}-{version}.jar")
    if os.path.exists(p):
        paths.append(p)
print(":".join(paths))
EOF
)

add() { CP="$CP:$1"; }
add "$LIBS/com/mojang/minecraft/26.2/minecraft-26.2-client.jar"
add "$(ls "$LIBS"/net/fabricmc/fabric-loader/0.19.3/*.jar)"
add "$(find "$LIBS/net/fabricmc/sponge-mixin" -name '*.jar' | sort | tail -1)"
for j in "$LIBS"/org/lwjgl/lwjgl*/3.4.1/lwjgl*-3.4.1.jar; do add "$j"; done
for j in "$MODS"/litematica-fabric-*.jar "$MODS"/malilib-fabric-*.jar build/deps/META-INF/jars/*.jar; do add "$j"; done

"$JDK/javac" --release 25 -proc:none -nowarn -d build/classes -cp "$CP" $(find src/main/java -name '*.java')
cp -R src/main/resources/. build/classes/
"$JDK/jar" --create --file "$OUT" -C build/classes .
echo "Built $OUT"

if [[ "${1:-}" == "--install" ]]; then
  DEST="$PRISM/instances/${2:?instance name}/minecraft/mods"
  rm -f "$DEST"/litematica-autorotate-*.jar
  cp "$OUT" "$DEST/"
  echo "Installed to $DEST"
fi
