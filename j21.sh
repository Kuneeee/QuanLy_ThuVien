#!/usr/bin/env bash
set -euo pipefail

# Script to update project Java config from 11/older values to 21.
# It updates common Maven and Gradle version declarations.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_JAVA="21"

switch_runtime_java_if_possible() {
    local sdk_dir="${SDKMAN_DIR:-/usr/local/sdkman}"
    local candidates_dir=""
    local java_candidate=""

    if [[ -d "$sdk_dir/candidates/java" ]]; then
        candidates_dir="$sdk_dir/candidates/java"
    elif [[ -d "$HOME/.sdkman/candidates/java" ]]; then
        candidates_dir="$HOME/.sdkman/candidates/java"
    else
        echo "SDKMAN candidates directory not found. Runtime JDK was not changed."
        return
    fi

    java_candidate="$(ls -1 "$candidates_dir" 2>/dev/null | grep -E '^21(\.|$)' | head -n 1 || true)"

    if [[ -n "$java_candidate" ]]; then
        local target_path="${candidates_dir}/${java_candidate}"

        # Keep SDKMAN current symlink consistent.
        if [[ -L "${candidates_dir}/current" || ! -e "${candidates_dir}/current" ]]; then
            ln -sfn "$target_path" "${candidates_dir}/current" || true
        fi

        # Keep devcontainer java shortcut in sync when available.
        if [[ -L "$HOME/java/current" || ! -e "$HOME/java/current" ]]; then
            ln -sfn "$target_path" "$HOME/java/current" || true
        fi

        # Best-effort SDKMAN commands when sdk is available in this shell.
        if command -v sdk >/dev/null 2>&1; then
            sdk default java "$java_candidate" >/dev/null 2>&1 || true
            sdk use java "$java_candidate" >/dev/null 2>&1 || true
        fi

        echo "Runtime JDK switched to: $java_candidate"
        java -version || true
        return
    fi

    echo "SDKMAN Java 21 candidate not found. Runtime JDK was not changed."
}

update_file_if_exists() {
    local file="$1"
    local sed_script="$2"

    if [[ -f "$file" ]]; then
        sed -i -E "$sed_script" "$file"
        echo "Updated: $file"
    fi
}

echo "Switching Java config to version $TARGET_JAVA..."

# Maven
update_file_if_exists "$ROOT_DIR/quanLyBanHang/pom.xml" "
s#<java.version>[^<]+</java.version>#<java.version>${TARGET_JAVA}</java.version>#g;
s#<maven.compiler.source>[^<]+</maven.compiler.source>#<maven.compiler.source>${TARGET_JAVA}</maven.compiler.source>#g;
s#<maven.compiler.target>[^<]+</maven.compiler.target>#<maven.compiler.target>${TARGET_JAVA}</maven.compiler.target>#g;
s#<release>[0-9]+</release>#<release>${TARGET_JAVA}</release>#g
"

# Gradle build files
update_file_if_exists "$ROOT_DIR/quanLyBanHang/build.gradle" "
s#sourceCompatibility[[:space:]]*=[[:space:]]*'[^']*'#sourceCompatibility = '${TARGET_JAVA}'#g;
s#targetCompatibility[[:space:]]*=[[:space:]]*'[^']*'#targetCompatibility = '${TARGET_JAVA}'#g;
s#sourceCompatibility[[:space:]]*=[[:space:]]*\"[^\"]*\"#sourceCompatibility = '${TARGET_JAVA}'#g;
s#targetCompatibility[[:space:]]*=[[:space:]]*\"[^\"]*\"#targetCompatibility = '${TARGET_JAVA}'#g;
s#JavaVersion\.VERSION_[0-9]+#JavaVersion.VERSION_${TARGET_JAVA}#g;
s#options\.release[[:space:]]*=[[:space:]]*[0-9]+#options.release = ${TARGET_JAVA}#g
"

update_file_if_exists "$ROOT_DIR/quanLyBanHang/settings.gradle" "s#JavaVersion\.VERSION_[0-9]+#JavaVersion.VERSION_${TARGET_JAVA}#g"

switch_runtime_java_if_possible

echo "Done. Java version config is now set to $TARGET_JAVA where supported patterns were found."
