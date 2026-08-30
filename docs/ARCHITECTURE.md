# Architecture

## Packaging

Each consumer compiles the pinned module source into its own add-on JAR. This
keeps one class identity per add-on classloader and avoids a new installed
dependency. The module has no services, entrypoint, registry singleton, cache,
or cross-add-on state.

## Compatibility

The production package names the BlueMap 5.23 feature-backport target. Runtime
admission remains exact by version and commit, so this is not a broad 5.23
compatibility claim.

`BlueMapRuntimeCompatibility` admits only the exact tested feature commit. A
later BlueMap commit requires a module release and an explicit consumer update.

## Registration

`RegistryGuard` checks the registry by key before mutation and accepts only an
empty slot or the same object identity. After registering into an empty slot,
it reads the slot again and accepts only the supplied object. Consumers still
preflight their complete heterogeneous registration plan before applying it,
and they retain all failure reasons.

`ResourceExtensionType` stores one exact key and factory. It does not register
itself or retain a resource pack.

## Dispatch

`SyntheticDispatch` accepts a resource block state only when it has no
multipart program, has one default variant and no competing selected variant,
and that variant uses the expected renderer, BlueMap's missing model, no
transform, no UV lock, and weight one. It performs no resource lookup or
consumer routing.

## Exclusions

Renderer construction, block-entity types, registration order, diagnostics,
activation, exact mod profiles, resource closures, texture collection,
culling, geometry, and fallback policy remain consumer code.
