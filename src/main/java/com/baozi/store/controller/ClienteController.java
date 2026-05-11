package com.baozi.store.controller;

import com.baozi.store.exception.ResourceNotFoundException;
import com.baozi.store.model.Cliente;
import com.baozi.store.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Clientes.
 * Endpoints: POST, GET (lista), GET (por id), PUT, DELETE
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // POST /clientes - Cadastrar novo cliente
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        Cliente salvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /clientes - Listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // GET /clientes/{id} - Consultar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente nao encontrado com id: " + id));
        return ResponseEntity.ok(cliente);
    }

    // PUT /clientes/{id} - Atualizar cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
                                              @Valid @RequestBody Cliente dadosNovos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente nao encontrado com id: " + id));

        cliente.setNome(dadosNovos.getNome());
        cliente.setClienteDesde(dadosNovos.getClienteDesde());

        Cliente atualizado = clienteRepository.save(cliente);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /clientes/{id} - Remover cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente nao encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
