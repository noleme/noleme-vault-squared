# Noleme Vault module² for Hashicorp Vault

[![Maven Build](https://github.com/noleme/noleme-vault-squared/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/noleme/noleme-vault-squared/actions/workflows/maven.yml)
[![Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-vault-squared/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.noleme/noleme-vault-squared)
[![javadoc](https://javadoc.io/badge2/com.noleme/noleme-vault-squared/javadoc.svg)](https://javadoc.io/doc/com.noleme/noleme-vault-squared)
![Coverage](.github/badges/jacoco.svg)
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

A basic example of using this library with a `yml` configuration file:

Given a dummy configuration file `my_conf.yml`:

```yaml
hashicorp_vault:
    address: "http://my-vault-instance:8200"
    token: "my-token"
    variables: # Here we list variables we want to recover from the vault and their corresponding (noleme) vault identifier
        secret/my-path: # Variables path
            some_key: my_var # Mapping (hashicorp) vault key to a (noleme) vault id
            some_other_key: another_var

variables:
  # In this example, this value will be overridden by the value found under secret/my-path.some_key in Hashicorp Vault unless it cannot be read
  my_var: "interesting" 
``` 

Afterwards, these will be injectable like any other variable in a [`noleme-vault`](https://github.com/noleme/noleme-vault) container.

_TODO_

## IV. Dev Installation

This project will require you to have the following:

* Java 11+
* Git (versioning)
* Maven (dependency resolving, publishing and packaging) 

