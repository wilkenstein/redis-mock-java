# redis-mock-java
An in-memory redis-compatible implementation written in pure Java. Part of the [Rarefied Redis Project](http://wilkenstein.github.io/rarefied-redis/).

### Status
[![Build Status](https://travis-ci.org/wilkenstein/redis-mock-java.svg?branch=master)](https://travis-ci.org/wilkenstein/redis-mock-java)

## Installation

### maven

TODO

## Usage

TODO

## Thread Safety

The `RedisMock` object is itself completely thread-safe. Every command is `synchronized` on
the object, so the internal data structures can never run into concurrent modifications.

Because of the way `RedisMock` operates, we cannot, say, clone this object and maintain thread-safety.
If two `RedisMock` objects were to share the same internal data structures, it would be possible
for a concurrent modification to occur between the two objects. Thus, `clone` is unsupported on
`RedisMock`.

## Supported Commands

The goal is to have one-to-one feature parity with all redis commands, so that this implementation can simply be dropped into an existing redis-backed codebase. Redis has a lot of commands! Some of them are easy, and some are quite complex.

Version 0.1.0 should have support for almost all redis commands, minus the hyperloglog commands and the key-movement commands, such as migrate.

To find out what commands a particular version of redis-java supports, run the following commands:

TODO

## Contributing

All contributions welcome! Issues or Pull Requests. For PRs, `mvn test` must
succeed and test the new code before the PR will be considered.

## Testing

This project uses maven.

To run the full test suite from source, issue the standard maven command:

````bash
$ mvn test
````

## Roadmap

* 0.1.0
  - Support for most redis commands.
  - Unit tests for all supported commands.
* 1.0.0
  - Support for different versions of mock redis that mimic different
    redis versions.
  - Support for multiple mock redis instances.
  - Support for migrating data between mock redis instances.
  - Server support.
* 2.0.0
  - Support for migrating data from a mock redis instance to a real
    redis instance.
  - Support for persisting a mock redis instance.
  - HyperLogLog support.

## Versions

* 0.0.4
  - All hash commands implemented.
  - All hash commands unit-tested.
* 0.0.3
  - All set commands implemented.
  - All set commands unit-tested.
* 0.0.2
  - All list commands implemented.
  - All list commands unit-tested. 
* 0.0.1
  - All string commands implemented.
  - All string commands unit-tested.