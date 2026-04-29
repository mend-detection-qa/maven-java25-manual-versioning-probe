# maven-java25-manual-versioning-probe

## Feature exercised

This probe exercises Maven 4's new POM model (`modelVersion 4.1.0`), the
`<subprojects>` aggregator element (Maven 4 rename of `<modules>`), CI-friendly
`${revision}` version placeholders in child POMs, JPMS `module-info.java`, Java 25
language features (records, sealed interfaces, pattern-matching switch), and strict
manual dependency versioning — every direct dependency carries an explicit `<version>`
with no BOM imports and no version ranges.

## Probe metadata

| Field            | Value                                                   |
|------------------|---------------------------------------------------------|
| Pattern          | maven4-java25-manual-versioning                         |
| groupId          | com.example.probe                                       |
| artifactId       | maven-java25-manual-versioning-probe                    |
| version          | 1.0.0                                                   |
| modelVersion     | 4.1.0 (Maven 4 only)                                    |
| Java release     | 25                                                      |
| Modules          | core, app                                               |

## Maven 4 features present

- `<modelVersion>4.1.0</modelVersion>` in every POM (parent + children)
- `<subprojects>` / `<subproject>` in aggregator POM instead of `<modules>` / `<module>`
- `${revision}` CI-friendly version in parent `<properties>` and child `<version>` inheritance
- `maven-compiler-plugin 3.13.0` with `<release>25</release>`

## Build requirements

- Maven 4.0.0-rc-3 or later (modelVersion 4.1.0 and `<subprojects>` are Maven 4 features)
- JDK 25 or later (required for `--release 25`)
- Maven 3.9.x with JDK 17: structurally reads POMs but WILL fail at the
  modelVersion parse step — see compatibility caveats below.

## Expected dependency tree

### core module (compile scope direct + transitive)

| groupId                          | artifactId              | version | scope   | direct |
|----------------------------------|-------------------------|---------|---------|--------|
| org.slf4j                        | slf4j-api               | 2.0.16  | compile | yes    |
| com.fasterxml.jackson.core       | jackson-databind         | 2.18.2  | compile | yes    |
| com.fasterxml.jackson.core       | jackson-core             | 2.18.2  | compile | no (transitive of jackson-databind) |
| com.fasterxml.jackson.core       | jackson-annotations      | 2.18.2  | compile | no (transitive of jackson-databind) |

### app module (compile + runtime + test, direct + transitive)

| groupId                          | artifactId              | version | scope   | direct |
|----------------------------------|-------------------------|---------|---------|--------|
| com.example.probe                | core                    | 1.0.0   | compile | yes    |
| org.slf4j                        | slf4j-api               | 2.0.16  | compile | no (transitive via core) |
| com.fasterxml.jackson.core       | jackson-databind         | 2.18.2  | compile | no (transitive via core) |
| com.fasterxml.jackson.core       | jackson-core             | 2.18.2  | compile | no |
| com.fasterxml.jackson.core       | jackson-annotations      | 2.18.2  | compile | no |
| ch.qos.logback                   | logback-classic         | 1.5.12  | runtime | yes    |
| ch.qos.logback                   | logback-core            | 1.5.12  | runtime | no (transitive of logback-classic) |
| org.slf4j                        | slf4j-api               | 2.0.16  | runtime | no (transitive of logback-classic — version mediated by core's compile dep) |
| org.junit.jupiter                | junit-jupiter            | 5.11.4  | test    | yes    |
| org.junit.jupiter                | junit-jupiter-api        | 5.11.4  | test    | no |
| org.junit.jupiter                | junit-jupiter-params     | 5.11.4  | test    | no |
| org.junit.jupiter                | junit-jupiter-engine     | 5.11.4  | test    | no |
| org.junit.platform               | junit-platform-engine    | 1.11.4  | test    | no |
| org.junit.platform               | junit-platform-commons   | 1.11.4  | test    | no |
| org.opentest4j                   | opentest4j              | 1.3.0   | test    | no |
| org.apiguardian                  | apiguardian-api          | 1.1.2   | test    | no |

## Compatibility caveats

1. **modelVersion 4.1.0**: Maven 3.9.x rejects this at parse time with an
   "Unrecognised model version" error.  The probe is intentionally written for
   Maven 4 to exercise Mend's ability to parse Maven 4 POMs.

2. **`<subprojects>`**: Maven 3.9.x does not recognise the `<subprojects>`
   element and therefore will not build child modules even if modelVersion were
   downgraded.

3. **`<release>25`**: Requires JDK 25 at compile time.  Maven 3.9.12 + JDK 17
   (the environment available at probe-generation time) will fail compilation
   with "invalid release: 25".  The POMs and source files are structurally
   correct; the build error is environmental, not a defect in the probe.

4. **`${revision}` in child version**: This CI-friendly feature works in both
   Maven 3.3+ and Maven 4 as long as the flatten-maven-plugin or Maven 4's
   built-in consumer POM handling is used for publishing.  For local builds and
   Mend scanning it resolves correctly without the flatten plugin.