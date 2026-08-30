# Releasing

1. Raise `module_version` in a reviewed pull request.
2. Run both supported Gradle versions against the exact 5.22 checkout and the
   focused compatibility gate against the exact 5.23 feature checkout.
3. Rebuild the production JAR, sources JAR, POM, and Gradle metadata twice
   with Gradle 9.6.1 and compare every byte.
4. Merge through a true two-parent commit and wait for exact-main CI.
5. Create a signed annotated `v<module_version>` tag at that main commit.
6. Let the release workflow attest the files, publish the Maven review
   artifact, compare the downloaded assets, and publish the prerelease.

The standalone JAR is never added to an ATMons server manifest.
