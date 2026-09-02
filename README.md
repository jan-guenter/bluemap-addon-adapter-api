# BlueMap Add-on Adapter API

This Java 21 source module holds the repeated BlueMap-internal bootstrap code
used by independent BlueMap add-ons. Its first release targets only the exact
tested 5.23 feature backport.

## Consumer model

BlueMap loads add-ons through separate classloaders and provides no dependable
shared-library version contract. Consumers pin this repository at an exact
commit and compile `src/main/java` into their own production JARs. Do not copy
`bluemap-addon-adapter-api-*.jar` to a server and do not nest it in an add-on.

For a module checkout at `modules/bluemap-addon-adapter-api`:

```groovy
sourceSets {
    main.java.srcDir 'modules/bluemap-addon-adapter-api/src/main/java'
}
```

The consumer must verify the gitlink, module HEAD, clean checkout, and
`HEAD:src/main/java` tree before compilation. Its archive gate must require one
copy of every selected shared class, reject displaced local copies, and reject
a nested module JAR.

## Exact runtime identity

`BlueMapRuntimeCompatibility` accepts only this audited pair:

| Runtime | Version string | Commit |
| --- | --- | --- |
| Tested 5.23 feature backport | `5.22-feature.backport-5.23-stateless-java-web-server-46` | `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` |

The BlueMap and BlueMapAPI source preflight rejects every other checkout. A
later feature commit remains unavailable until it receives its own review,
module release, consumer update, and integration run.

## API boundary

- `BlueMapRuntimeCompatibility` owns the sole exact runtime identity.
- `RegistryGuard` preserves identity-safe, idempotent registration within one
  add-on classloader.
- `RegistrationPlan` creates immutable, ordered, consumer-owned batches of the
  same guarded operations. It preflights the complete batch before mutation
  and does not attempt rollback after a failed identity read-back.
- `ResourceExtensionType` removes the repeated one-key, one-factory resource
  extension wrapper.
- `SyntheticDispatch` validates the exact one-variant missing-model dispatch
  used by the add-ons' synthetic block states.

Add-on entrypoints, renderer instances, registration candidates and ordering,
failure reasons, resource admission, routes, profiles, and fallback policy
remain in each consumer.

## Build

Use Java 21, Gradle 9.4.0 or 9.6.1, and a clean recursive checkout of one
target BlueMap commit:

```bash
gradle --no-daemon \
  -PbluemapSourcePath=/path/to/bluemap clean check verifyPublication
```

The standalone JAR is publication and review evidence. Its POM and Gradle
module metadata intentionally have no production dependencies.
