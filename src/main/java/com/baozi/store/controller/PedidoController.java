package com.baozi.store.controller;

import com.baozi.store.exception.ResourceNotFoundException;
import com.baozi.store.model.Cliente;
import com.baozi.store.model.Pedido;
import com.baozi.store.model.Produto;
import com.baozi.store.repository.ClienteRepository;
import com.baozi.store.repository.PedidoRepository;
import com.baozi.store.repository.ProdutoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Pedidos.
 * Recebe clienteId, produtoId e quantidade via DTO interno,
 * resolve as entidades e persiste o pedido completo.
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // DTO interno para receber dados do pedido via JSON
    public static class PedidoRequest {

        @NotNull(message = "O clienteId nao pode ser nulo")
        private Long clienteId;

        @NotNull(message = "O produtoId nao pode ser nulo")
        private Long produtoId;

        @NotNull(message = "A quantidade nao pode ser nula")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        private Integer quantidade;

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    // POST /pedidos - Registrar novo pedido
    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente nao encontrado com id: " + request.getClienteId()));

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado com id: " + request.getProdutoId()));

        if (!produto.getEstoque()) {
            throw new IllegalArgumentException(
                    "Produto '" + produto.getNome() + "' esta fora de estoque.");
        }

        Pedido pedido = new Pedido(cliente, produto, request.getQuantidade());
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /pedidos - Listar todos os pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return ResponseEntity.ok(pedidos);
    }

    // GET /pedidos/{id} - Consultar pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido nao encontrado com id: " + id));
        return ResponseEntity.ok(pedido);
    }

    // PUT /pedidos/{id} - Atualizar pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody PedidoRequest request) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido nao encontrado com id: " + id));

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente nao encontrado com id: " + request.getClienteId()));

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado com id: " + request.getProdutoId()));

        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setQuantidade(request.getQuantidade());

        Pedido atualizado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /pedidos/{id} - Cancelar/remover pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido nao encontrado com id: " + id);
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
