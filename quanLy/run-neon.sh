#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env.local"

if [[ -f "${ENV_FILE}" ]]; then
  set -a
  # shellcheck disable=SC1090
  source "${ENV_FILE}"
  set +a
fi

: "${SPRING_PROFILES_ACTIVE:=cloud}"
: "${SPRING_DATASOURCE_URL:?Missing SPRING_DATASOURCE_URL. Put it in .env.local or export it before running ./run-neon.sh}"
: "${SPRING_DATASOURCE_USERNAME:?Missing SPRING_DATASOURCE_USERNAME. Put it in .env.local or export it before running ./run-neon.sh}"
: "${SPRING_DATASOURCE_PASSWORD:?Missing SPRING_DATASOURCE_PASSWORD. Put it in .env.local or export it before running ./run-neon.sh}"
: "${SERVER_PORT:=0}"

export SPRING_DATASOURCE_URL
export SPRING_DATASOURCE_USERNAME
export SPRING_DATASOURCE_PASSWORD
export SPRING_PROFILES_ACTIVE
exec mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=${SERVER_PORT}"
