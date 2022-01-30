package io.manager.server.service;

import io.manager.server.model.Server;

import java.util.Collection;

/**
 * Basic server functionality interface.
 */

public interface ServerService {

    Server create(Server server);
    Server ping(String ipAddress);
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
}
