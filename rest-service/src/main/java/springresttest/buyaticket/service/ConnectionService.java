package springresttest.buyaticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springresttest.buyaticket.exceptions.ConnectionNotFoundException;
import springresttest.buyaticket.exceptions.DuplicateConnectionException;
import springresttest.buyaticket.model.Connection;
import springresttest.buyaticket.repository.ConnectionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {
    private ConnectionRepository connectionRepository;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public Optional<Connection> findById(String id) {
        return connectionRepository.findById(id);
    }

    public void saveConnection(Connection connection) {
        List<Connection> connections = getConnections();
        if(connections.contains(connection)) throw new DuplicateConnectionException("Duplicate connection");
        connectionRepository.save(connection);
    }

    public List<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    public void deleteConnection(Connection connection) {
        connectionRepository.delete(connection);
    }

    public void updateConnection(Connection connection, String pathVariableId) {
        Connection connectionToUpdate =
                findById(pathVariableId).orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));
        connection.setConnectionId(connectionToUpdate.getConnectionId());
        connectionRepository.save(connection);
    }
}
