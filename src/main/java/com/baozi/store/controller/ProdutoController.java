package com.baozi.store.controller;

import com.baozi.store.exception.ResourceNotFoundException;
import com.baozi.store.model.Produto;
import com.baozi.store.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Produtos.
 * Endpoints: POST, GET (lista), GET (por id), PUT, DELETE
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // POST /produtos - Cadastrar novo produto
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        Produto salvo = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /produtos - Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    // GET /produtos/{id} - Consultar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado com id: " + id));
        return ResponseEntity.ok(produto);
    }

    // PUT /produtos/{id} - Atualizar produto existente
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,
                                              @Valid @RequestBody Produto dadosNovos) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto nao encontrado com id: " + id));

        produto.setNome(dadosNovos.getNome());
        produto.setPreco(dadosNovos.getPreco());
        produto.setEstoque(dadosNovos.getEstoque());

        Produto atualizado = produtoRepository.save(produto);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /produtos/{id} - Remover produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto nao encontrado com id: " + id);
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
