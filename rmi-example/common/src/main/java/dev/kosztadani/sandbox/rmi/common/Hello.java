package dev.kosztadani.sandbox.rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface between RMI server and client.
 */
public interface Hello extends Remote {

    /**
     * The name of the RMI registry.
     */
    String REGISTRY_NAME = "Hello";

    /**
     * Computes a greeting for the given name.
     *
     * @param name The name to generate the greeting for.
     * @return The generated greeting.
     * @throws RemoteException if RMI communication fails.
     */
    String hello(String name) throws RemoteException;
}
