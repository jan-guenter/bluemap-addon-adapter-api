# Pilot contract

The first consumer cohort uses three distinct adapter shapes:

- Chipped, with Athena and render-core source modules and no block entities;
- Ender IO, with one block entity and the older inline registry helper; and
- LaserIO, with a shared runtime module, shared render core, and several block
  entity types.

Each migration must preserve the accepted gallery, local routes, renderer and
failure policy. The review gate compares unaffected archive entries byte for
byte, checks package-normalized behavior, and proves the shared source appears
once without a nested module JAR.

After isolated pull-request gates, one combined ATMons 1.2.0 test must run all
51 add-ons on two distinct server boots. It must observe 51 activation markers
on both boots and 51 asserted gallery passes with no registration, linkage, or
duplicate-class failure.

## Immutable registration-plan pilot

The alpha.3 helper is limited to XNet, Immersive Energistics, and Immersive
Engineering. These consumers have small all-or-nothing registration batches
and retain their existing failure strings. Immersive Engineering keeps its
base registrations and block-entity registrations in separate plans so a
block-entity failure remains distinguishable.

AE2 and FramedBlocks are excluded. Their optional routes, DTO-first ordering,
dynamic manifests, and route-specific diagnostics need a separate review and
must not be generalized by this pilot.
