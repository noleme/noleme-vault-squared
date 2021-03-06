package com.noleme.vault.squared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 */
public class HashicorpVaultConfig
{
    /* defaults to VAULT_ADDR env var */
    private final String address;
    /* defaults to VAULT_TOKEN env var */
    private final String token;
    /* defaults to VAULT_OPEN_TIMEOUT env var */
    private final Integer openTimeout;
    /* defaults to VAULT_READ_TIMEOUT env var */
    private final Integer readTimeout;
    private final Integer engineVersion;
    private final VariableMapping variables;
    private final BehaviourOnFailure onFailure;

    @JsonCreator
    public HashicorpVaultConfig(
        @JsonProperty("address") String address,
        @JsonProperty("token") String token,
        @JsonProperty("open_timeout") Integer openTimeout,
        @JsonProperty("read_timeout") Integer readTimeout,
        @JsonProperty("engine_version") Integer engineVersion,
        @JsonProperty("variables") VariableMapping variables,
        @JsonProperty("on_failure") BehaviourOnFailure onFailure
    )
    {
        this.address = address;
        this.token = token;
        this.openTimeout = openTimeout;
        this.readTimeout = readTimeout;
        this.engineVersion = engineVersion;
        this.variables = variables != null ? variables : new VariableMapping();
        this.onFailure = onFailure != null ? onFailure : BehaviourOnFailure.ABORT;
    }

    public String address()
    {
        return this.address;
    }

    public String token()
    {
        return this.token;
    }

    public Integer openTimeout()
    {
        return this.openTimeout;
    }

    public Integer readTimeout()
    {
        return this.readTimeout;
    }

    public Integer engineVersion()
    {
        return this.engineVersion;
    }

    public VariableMapping variables()
    {
        return this.variables;
    }

    public BehaviourOnFailure onFailure()
    {
        return this.onFailure;
    }

    public static class VariableMapping
    {
        private final Map<String, Map<String, String>> mapping;

        public VariableMapping()
        {
            this(new HashMap<>());
        }

        @JsonCreator
        public VariableMapping(@JsonProperty Map<String, Map<String, String>> mapping)
        {
            this.mapping = mapping;
        }

        @JsonValue
        public Map<String, Map<String, String>> mapping()
        {
            return this.mapping;
        }
    }

    public enum BehaviourOnFailure
    {
        IGNORE,
        ABORT,
    }
}
