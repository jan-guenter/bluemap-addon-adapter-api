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
