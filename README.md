# Noleme Vault module² for Hashicorp Vault

[![Maven Build](https://github.com/noleme/noleme-vault-squared/actions/workflows/maven-build.yml/badge.svg?branch=master)](https://github.com/noleme/noleme-vault-squared/actions/workflows/maven-build.yml)
[![Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-vault-squared/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-vault-squared)
[![javadoc](https://javadoc.io/badge2/com.noleme/noleme-vault-squared/javadoc.svg)](https://javadoc.io/doc/com.noleme/noleme-vault-squared)
[![coverage](https://codecov.io/gh/noleme/noleme-vault-squared/branch/master/graph/badge.svg?token=R4U1JM1K85)](https://codecov.io/gh/noleme/noleme-vault-squared)
![GitHub](https://img.shields.io/github/license/noleme/noleme-vault-squared)

A [`noleme-vault`](https://github.com/noleme/noleme-vault) module for loading variables from a [Hashicorp Vault](https://github.com/hashicorp/vault) instance.

_Note: This library is considered as "in beta" and as such significant API changes may occur without prior warning._

## I. Installation

Add the following in your `pom.xml`:

```xml
<dependency>
    <groupId>com.noleme</groupId>
    <artifactId>noleme-vault-squared</artifactId>
    <version>0.1</version>
</dependency>
```

## II. Notes on Structure and Design

_TODO_

## III. Usage

In order to use a `noleme-vault` module, you need to register it on the `VaultParser`, the simplest way is to register it in the `defaultParser`, but you may of course do it on a dedicated `VaultFactory`/`VaultParser` pair.

```java
VaultFactory.defaultParser.register(new HashicorpVaultModule());
```

Afterwards, `noleme-vault` will be able to understand `hashicorp_vault` sections. Here is a basic example of using it in a `yml` configuration file:

```yaml
hashicorp_vault:
    address: "http://my-vault-instance:8200"
    token: "my-token"
    variables: # Here we list variables we want to recover from the vault and their corresponding (noleme) vault identifier
        secret/my-path: # Variables path
            some_key: my_var # Mapping (hashicorp) vault key to a (noleme) vault id
            some_other_key: another_var

variables:
  # In this example, this value will be overridden by the value found under secret/my-path.some_key in Hashicorp Vault
  my_var: "interesting" 
``` 

Afterwards, these will be injectable like any other variable in a [`noleme-vault`](https://github.com/noleme/noleme-vault) container.

At the time of this writing, here are the available vault options you can specify:

```yaml
hashicorp_vault:
    address: "http://my-vault-instance:8200" # defaults to the VAULT_ADDR env var
    token: "my-token" # defaults to the VAULT_TOKEN env var
    engine_version: 2 # defaults to 2
    open_timeout: 10 # in seconds, defaults to the VAULT_OPEN_TIMEOUT env var
    read_timeout: 10 # in seconds, defaults to the VAULT_READ_TIMEOUT env var
    on_failure: ABORT # available values are IGNORE and ABORT, defaults to ABORT
```

Note on `on_failure`: a "failure" can occur if the specified secret cannot be found, in which case:
* `ABORT` will fail the configuration loading
* `IGNORE` will simply keep on trucking and whatever value previously available in the `Definitions` container will remain

_TODO_

## IV. Dev Installation

This project will require you to have the following:

* Java 11+
* Git (versioning)
* Maven (dependency resolving, publishing and packaging) 

