#!/bin/bash
set -e

# NOTE: this build script is specific to the build system used at OPSWAT

salt-call --local state.highstate
unset JAVA_HOME

SOURCE_ROOT=${WORKSPACE}/source



${SOURCE_ROOT}/gradlew clean -p ${SOURCE_ROOT}
${SOURCE_ROOT}/gradlew build -p ${SOURCE_ROOT}
${SOURCE_ROOT}/gradlew uploadArchives -p ${SOURCE_ROOT} -PossrhUsername=${SSRH_USERNAME} -PossrhPassword=${SSRH_PASSWORD} -Psigning.keyId=${KEY_ID} -Psigning.password=${PASSWORD} -Psigning.secretKeyRingFile=${RING_FILE}

${SOURCE_ROOT}/gradlew closeAndPromoteRepository -p ${SOURCE_ROOT} -PossrhUsername=${SSRH_USERNAME} -PossrhPassword=${SSRH_PASSWORD} -Psigning.keyId=${KEY_ID} -Psigning.password=${PASSWORD} -Psigning.secretKeyRingFile=${RING_FILE}



