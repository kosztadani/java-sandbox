/**
 * Provides the RMI server application.
 */
module dev.kosztadani.sandbox.rmi.server {
    requires java.rmi;
    requires dev.kosztadani.sandbox.rmi.common;
    exports dev.kosztadani.sandbox.rmi.server;
}