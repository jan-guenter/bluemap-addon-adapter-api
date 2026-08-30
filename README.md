# BlueMap Add-on Adapter API

This Java 21 source module holds the repeated BlueMap-internal bootstrap code
used by independent BlueMap add-ons. Its first release covers the internal ABI
shared by the released 5.22 backport and the exact tested 5.23 feature commit.

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

## Exact runtime identities

`BlueMapRuntimeCompatibility` publishes only these audited pairs:

| Runtime | Version string | Commit |
| --- | --- | --- |
| Upstream BlueMap 5.22 | `5.22` | `fe5115d5548a30d34175b8e0449aaca280af199f` |
| ATMons Java 21 backport | `5.22-agent.backport-5.22-mc1.21.1-2` | `9be321df995a1103808621d529eb72773e719d4d` |
| Tested 5.23 feature backport | `5.22-feature.backport-5.23-stateless-java-web-server-46` | `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` |

Each consumer explicitly selects its accepted subset when calling
`matchesCurrent(...)`. That preserves narrower gates such as FramedBlocks'
backport-only policy. A match means that the adapter ABI was audited; it does
not replace any additional mod-specific compatibility condition.

The BlueMap and BlueMapAPI source preflight rejects every other checkout. The
relevant core classes are byte-identical across the released 5.22 backport,
upstream 5.23, and the accepted feature commit. A later commit remains
unavailable until it receives its own review, module release, consumer opt-in,
and integration run.

## API boundary

- `BlueMapRuntimeCompatibility` owns the exact runtime identities while each
  consumer chooses its permitted subset.
- `RegistryGuard` preserves identity-safe, idempotent registration within one
  add-on classloader.
- `ResourceExtensionType` removes the repeated one-key, one-factory resource
  extension wrapper.
- `SyntheticDispatch` validates the exact one-variant missing-model dispatch
  used by the add-ons' synthetic block states.

Add-on entrypoints, renderer instances, block-entity registrations, failure
reasons, resource admission, routes, profiles, and fallback policy remain in
each consumer.

## Build

Use Java 21, Gradle 9.4.0 or 9.6.1, and a clean recursive checkout of one
accepted BlueMap commit:

```bash
gradle --no-daemon \
  -PbluemapSourcePath=/path/to/bluemap clean check verifyPublication
```

The standalone JAR is publication and review evidence. Its POM and Gradle
module metadata intentionally have no production dependencies.
