package com.noleme.vault.squared;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import com.noleme.vault.container.register.Definitions;
import com.noleme.vault.exception.VaultParserException;
import com.noleme.vault.parser.module.GenericModule;

import java.util.Map;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class HashicorpVaultModule extends GenericModule<HashicorpVaultConfig>
{
    public HashicorpVaultModule()
    {
        super("hashicorp_vault", HashicorpVaultConfig.class, HashicorpVaultModule::process);
    }

    private static void process(HashicorpVaultConfig config, Definitions definitions) throws VaultParserException
    {
        final VaultConfig hashicorpVaultConfig = buildConfig(config);
        final Vault hashicorpVault = new Vault(hashicorpVaultConfig);

        writeToVariables(config, hashicorpVault, definitions);
    }

    private static VaultConfig buildConfig(HashicorpVaultConfig config) throws VaultParserException
    {
        try {
            var vaultConfig = new VaultConfig();

            if (config.address() != null)
                vaultConfig.address(config.address());
            if (config.token() != null)
                vaultConfig.token(config.token());
            if (config.engineVersion() != null)
                vaultConfig.engineVersion(config.engineVersion());
            if (config.openTimeout() != null)
                vaultConfig.openTimeout(config.openTimeout());
            if (config.readTimeout() != null)
                vaultConfig.readTimeout(config.readTimeout());

            return vaultConfig.build();
        }
        catch (VaultException e) {
            throw new VaultParserException(e.getMessage(), e);
        }
    }

    private static void writeToVariables(HashicorpVaultConfig config, Vault vault, Definitions definitions) throws VaultParserException
    {
        try {
            for (Map.Entry<String, Map<String, String>> vaultToVariableSet : config.variables().mapping().entrySet())
            {
                if (vaultToVariableSet.getValue().isEmpty())
                    continue;

                String path = vaultToVariableSet.getKey();
                LogicalResponse response = vault.logical().read(path);

                for (Map.Entry<String, String> vaultToVariable : vaultToVariableSet.getValue().entrySet())
                {
                    if (!response.getData().containsKey(vaultToVariable.getKey()))
                        continue;
                    definitions.variables().set(vaultToVariable.getValue(), response.getData().get(vaultToVariable.getKey()));
                }
            }
        }
        catch (VaultException e) {
            throw new VaultParserException(e.getMessage(), e);
        }
    }
}
