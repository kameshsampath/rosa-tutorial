#!/usr/bin/env bash

set -e

RUNNER_PLAYBOOK=${RUNNER_PLAYBOOK:-playbook.yml}

ansible-runner run -p "$RUNNER_PLAYBOOK" /runner
