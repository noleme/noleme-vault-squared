package com.noleme.vault.squared;

import com.noleme.vault.Vault;
import com.noleme.vault.container.register.index.Variables;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.vault.VaultContainer;

import javax.inject.Inject;
import javax.inject.Named;

import static com.noleme.vault.parser.adjuster.VaultAdjuster.variables;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class HashicorpVaultModuleTest
{
    @ClassRule
    private static final VaultContainer<?> vaultContainer = new VaultContainer<>("vault:1.10.0")
        .withVaultToken("my-token")
        .withSecretInVault("secret/testing", "first=mnopqrst","sec=xyz", "3rd=847", "f=true")
    ;

    static {
        vaultContainer.start();
        VaultFactory.defaultParser.register(new HashicorpVaultModule());
    }

    @Test
    public void testCompilation()
    {
        Assertions.assertDoesNotThrow(() -> {
            Vault.with(
                variables(HashicorpVaultModuleTest::setVaultTestCredentials),
                "com/noleme/vault/squared/default-variables.yml",
                "com/noleme/vault/squared/vault-override.ignore.yml"
            );
        });
    }

    @Test
    public void testLoading_authenticated_shouldMatch() throws VaultException
    {
        var vault = Vault.with(
            variables(HashicorpVaultModuleTest::setVaultTestCredentials),
            "com/noleme/vault/squared/default-variables.yml",
            "com/noleme/vault/squared/vault-override.ignore.yml"
        );

        var values = vault.instance(Values.class);

        Assertions.assertEquals("mnopqrst", values.first);
        Assertions.assertEquals("xyz", values.second);
        Assertions.assertEquals(847, values.third);
        Assertions.assertEquals(true, values.fourth);
    }

    @Test
    public void testLoading_authenticated_shouldMatchWithDefaults() throws VaultException
    {
        var vault = Vault.with(
            variables(HashicorpVaultModuleTest::setVaultTestCredentials),
            "com/noleme/vault/squared/default-variables.yml",
            "com/noleme/vault/squared/vault-override.default.yml"
        );

        var values = vault.instance(Values.class);

        Assertions.assertEquals("mnopqrst", values.first);
        Assertions.assertEquals("xyz", values.second);
        Assertions.assertEquals(847, values.third);
        Assertions.assertEquals(true, values.fourth);
    }

    @Test
    public void testLoading_unauthenticated_shouldNotMatch() throws VaultException
    {
        var vault = Vault.with(
            variables(defs -> defs
                .set("hashicorp_vault.host", vaultContainer.getHost())
                .set("hashicorp_vault.port", vaultContainer.getMappedPort(8200))
                .set("hashicorp_vault.token", "not-my-token")
            ),
            "com/noleme/vault/squared/default-variables.yml",
            "com/noleme/vault/squared/vault-override.ignore.yml"
        );

        var values = vault.instance(Values.class);

        Assertions.assertEquals("abcdefgh", values.first);
        Assertions.assertEquals("ijklmnop", values.second);
        Assertions.assertEquals(1234, values.third);
        Assertions.assertEquals(false, values.fourth);
    }

    @Test
    public void testLoading_unauthenticated_shouldFail()
    {
        Assertions.assertThrows(VaultException.class, () -> {
            Vault.with(
                variables(defs -> defs
                    .set("hashicorp_vault.host", vaultContainer.getHost())
                    .set("hashicorp_vault.port", vaultContainer.getMappedPort(8200))
                    .set("hashicorp_vault.token", "not-my-token")
                ),
                "com/noleme/vault/squared/default-variables.yml",
                "com/noleme/vault/squared/vault-override.abort.yml"
            );
        });
    }

    private static void setVaultTestCredentials(Variables defs)
    {
        defs
            .set("hashicorp_vault.host", vaultContainer.getHost())
            .set("hashicorp_vault.port", vaultContainer.getMappedPort(8200))
            .set("hashicorp_vault.token", "my-token")
        ;
    }

    private static class Values
    {
        @Inject @Named("first") String first;
        @Inject @Named("second") String second;
        @Inject @Named("third") Integer third;
        @Inject @Named("fourth") Boolean fourth;
    }
}
