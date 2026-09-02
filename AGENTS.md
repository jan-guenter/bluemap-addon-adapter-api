# Agent guide for BlueMap Add-on Adapter API

Read this file, `README.md`, `docs/ARCHITECTURE.md`, and
`provenance/origins.json` before changing production code.

## Scope

Version `0.1.0-alpha.3` contains only five BlueMap-internal adapter helpers:

- an exact runtime identity check for the tested 5.23 feature backport;
- identity-safe registry admission and registration;
- an immutable ordered plan for the same guarded registry operations;
- a generic resource-pack extension type; and
- exact synthetic block-state dispatch validation.

Keep the production package under
`io.github.janguenter.bluemap.addon.adapter.api.bluemap523`. The package names
the sole feature-backport target. Do not broaden the runtime identity without
a source audit and a combined add-on gate.

Consumers compile this repository's production source into their own add-on
JAR. They do not install or nest the standalone module JAR.

## Boundaries

Do not add entrypoints, renderer implementations, routes, resources, profiles,
block-entity data, candidate-mod behavior, mutable global state, or an
installed service provider. Consumer registration candidates, ordering, plan
instances, and failure policy remain local. The shared plan type holds no
static or cross-add-on state and does not roll back registry mutations.

The frozen first-party origins are evidence, not production source. Keep their
bytes and hashes unchanged. A behavior change needs a new module version,
focused differential tests, consumer review, and another combined runtime
gate.

## Required gates

Use the shared Gradle lock and clean recursive BlueMap checkouts:

```bash
flock /tmp/bluemap-gradle.lock \
  gradle-9.4.0 --no-daemon \
  -PbluemapSourcePath=/path/to/bluemap-5.23 clean check verifyPublication

flock /tmp/bluemap-gradle.lock \
  gradle-9.6.1 --no-daemon \
  -PbluemapSourcePath=/path/to/bluemap-5.23 clean check verifyPublication
```

Before release, reproduce all publication files twice with Gradle 9.6.1 and
compare every byte. Inspect both JARs and run `actionlint` after workflow
changes.

Never commit build output, credentials, consumer artifacts, pack evidence, or
runtime results. A version increase and release require a reviewed pull
request. The release tag must be a signed annotated `v<module_version>` tag at
the reviewed main commit.
