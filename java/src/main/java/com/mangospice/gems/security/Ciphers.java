package com.mangospice.gems.security;

import javax.net.ssl.SSLServerSocketFactory;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class Ciphers {
    public static void main(String[] args) throws Exception {
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println();

        System.out.println("Security Providers");
        Provider[] providers = Security.getProviders();

        for (Provider provider : providers) {
            System.out.println(provider.getName());

            Set<Service> services = provider.getServices();
            for (Service service : services) {
                System.out.println("\t" + service.getType() + ", " +  service.getAlgorithm());
            }
            System.out.println();
        }




        System.out.println("\nCiphers");
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        // collect all supported ciphers
        Map<String, Boolean> ciphers = Arrays.stream(ssf.getSupportedCipherSuites())
                .sorted()
                .collect(toMap(identity(), c -> false));


        // mark the ones that are used by default
        stream(ssf.getDefaultCipherSuites())
                .collect(
                        toMap(identity(),
                                s -> true,
                                (oldValue, newValue) -> newValue,
                                () -> ciphers));



        System.out.println("Default\t\tCipher");
        for (Entry<String, Boolean> cipher : ciphers.entrySet()) {
            if (cipher.getValue()) {
                System.out.print('*');
            } else {
                System.out.print(' ');
            }

            System.out.print("\t\t\t");
            System.out.println(cipher.getKey());
        }




    }
}