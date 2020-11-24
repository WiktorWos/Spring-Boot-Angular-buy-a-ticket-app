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

    @GetMapping("/connection")
    public List<Connection> getAllConnections() {
        return connectionService.getConnections();
    }

    @GetMapping("/connection/{id}")
    public Connection getOneConnection(@PathVariable String id) {
        return connectionService.findById(id).orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));
    }

    @PostMapping("/connection")
    public Connection addConnection(@Valid @RequestBody Connection connection) {
        connectionService.saveConnection(connection);
        return connection;
    }

    @PutMapping("/connection/{id}")
    public Connection updateConnection(@RequestBody @Valid Connection updatedConnection, @PathVariable String id) {
        connectionService.updateConnection(updatedConnection, id);
        return updatedConnection;
    }

    @DeleteMapping("/connection/{id}")
    public Connection deleteConnection(@PathVariable String id) {
        Optional<Connection> optionalConnection = connectionService.findById(id);
        optionalConnection.ifPresent(user -> connectionService.deleteConnection(user));
        return optionalConnection.orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));
    }
}
