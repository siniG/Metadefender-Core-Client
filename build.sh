#!/bin/bash
set -e

# NOTE: this build script is specific to the build system used at OPSWAT

salt-call --local state.highstate
unset JAVA_HOME

ARTIFACT_ROOT=${WORKSPACE}/artifact
SOURCE_ROOT=${WORKSPACE}/source


mkdir -p ${ARTIFACT_ROOT}

rm -rf ${ARTIFACT_ROOT}/*


${SOURCE_ROOT}/gradlew clean -p ${SOURCE_ROOT}
${SOURCE_ROOT}/gradlew build -p ${SOURCE_ROOT}
${SOURCE_ROOT}/gradlew createJar -p ${SOURCE_ROOT}


cp ${SOURCE_ROOT}/build/test-results/*.xml ${ARTIFACT_ROOT}/

cp -r ${SOURCE_ROOT}/build/docs/ ${ARTIFACT_ROOT}/


cp ${SOURCE_ROOT}/build/libs/* ${ARTIFACT_ROOT}/

