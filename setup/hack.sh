#!/usr/bin/env bash

set -euo pipefail

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
CONTAINER_CMD=${CONTAINER_CMD:-docker}
ANSIBLE_RUNNER_IMAGE=quay.io/kameshsampath/openshift-demos-ansible-ee

if [ -f "$KUBECONFIG" ];
then
  mkdir -p "$CURRENT_DIR/.kube"
  < "$KUBECONFIG" tee "$CURRENT_DIR/.kube/config" > /dev/null
fi

$CONTAINER_CMD run -it  \
   -v "$CURRENT_DIR/project:/runner/project:z" \
   -v "$CURRENT_DIR/inventory:/runner/inventory:z" \
   -v "$CURRENT_DIR/env:/runner/env:z" \
   -v "$CURRENT_DIR/.kube:/home/runner/.kube:z" \
   --env-file "$CURRENT_DIR/.container-env" \
   "$ANSIBLE_RUNNER_IMAGE" /runner/project/run.sh