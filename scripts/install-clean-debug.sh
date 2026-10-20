#!/usr/bin/env bash
# Explicit clean installation for development/review: deletes this app's existing local records.
set -euo pipefail
project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
apk_path="${1:-$project_dir/artifacts/fix/brulee-debug.apk}"
sdk_dir="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Android/Sdk}}"
adb_path="$sdk_dir/platform-tools/adb"
if [[ ! -x "$adb_path" ]]; then adb_path="adb"; fi
printf 'Instalando %s e limpando os dados locais do Brûlée Telie.\n' "$apk_path"
"$adb_path" install -r "$apk_path"
"$adb_path" shell pm clear com.bruleetelie.app
"$adb_path" shell am start -n com.bruleetelie.app/.MainActivity
