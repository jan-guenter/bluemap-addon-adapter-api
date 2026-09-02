# Changelog

## 0.1.0-alpha.3

- Add an immutable, ordered registration plan that preflights heterogeneous
  BlueMap registry candidates and applies them with identity read-back.
- Preserve consumer-owned candidates, ordering, failure policy, and the
  existing no-rollback behavior.

## 0.1.0-alpha.2

- Remove the stale reference to the former 5.22 backport from packaged
  third-party metadata. The sole supported target is the exact tested 5.23
  feature-backport commit.

## 0.1.0-alpha.1

- Add an exact runtime identity check for the tested 5.23 feature backport.
- Add identity-safe registry helpers, a generic resource extension type, and
  exact synthetic dispatch validation.
- Publish the module as source compiled into consumers, never as an installed
  server dependency.
