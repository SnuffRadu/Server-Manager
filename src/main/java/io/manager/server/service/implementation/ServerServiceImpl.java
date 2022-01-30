package io.manager.server.service.implementation;

import io.manager.server.enums.Status;
import io.manager.server.model.Server;
import io.manager.server.repo.ServerRepo;
import io.manager.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Random;

/**
 * Implementation class for the ServerService interface.
 * Here we define all the characteristics of the Server we introduce in the database.
 */

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepo serverRepo;
    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageURL(setServerImageURL());
        return serverRepo.save(server);
    }

    @Override
    public Server ping(String ipAddress) {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        try{
            InetAddress address = InetAddress.getByName(ipAddress);
            server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
            serverRepo.save(server);
        }
        catch(UnknownHostException unknownHostException){
            log.debug("Unknown host");
        }
        catch(IOException ioException){
            log.debug("An IO Exception has occurred");
        }
        return null;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(PageRequest.of(0, limit)).toList();       //limiting the amount of servers returned
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        if(serverRepo.findById(id).isPresent()){
            return serverRepo.findById(id).get();
        }
        return null;
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server: {}", id);
        serverRepo.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageURL(){
        String[] imageNames = {"server 1.png, server 2.png, server 3.png, server 4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/images/" + imageNames[new Random().nextInt(4)]).toUriString(); //jesus christ
    }
}
