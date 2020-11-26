package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.ConnectionNotFoundException;
import springresttest.buyaticket.model.Connection;
import springresttest.buyaticket.service.ConnectionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ConnectionController {
    private ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/connections")
    public List<Connection> getAllConnections() {
        return connectionService.getConnections();
    }

    @GetMapping("/connections/{id}")
    public Connection getOneConnection(@PathVariable String id) {
        return connectionService.findById(id).orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));
    }

    @PostMapping("/connections")
    public Connection addConnection(@Valid @RequestBody Connection connection) {
        connectionService.saveConnection(connection);
        return connection;
    }

    @PutMapping("/connections/{id}")
    public Connection updateConnection(@RequestBody @Valid Connection updatedConnection, @PathVariable String id) {
        connectionService.updateConnection(updatedConnection, id);
        return updatedConnection;
    }

    @DeleteMapping("/connections/{id}")
    public Connection deleteConnection(@PathVariable String id) {
        Optional<Connection> optionalConnection = connectionService.findById(id);
        optionalConnection.ifPresent(user -> connectionService.deleteConnection(user));
        return optionalConnection.orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));
    }
}
