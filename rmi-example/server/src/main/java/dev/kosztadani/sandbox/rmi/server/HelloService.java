package dev.kosztadani.sandbox.rmi.server;

import dev.kosztadani.sandbox.rmi.common.Hello;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote object implementation of the {@link Hello} interface.
 */
final class HelloService extends UnicastRemoteObject implements Hello {

    HelloService() throws RemoteException {
        super();
    }

    @Override
    public String hello(final String name) {
        return "Hello, " + name + "!";
    }
}
