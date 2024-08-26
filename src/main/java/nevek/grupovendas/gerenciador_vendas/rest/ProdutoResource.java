package nevek.grupovendas.gerenciador_vendas.rest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import nevek.grupovendas.gerenciador_vendas.model.ProdutoDTO;
import nevek.grupovendas.gerenciador_vendas.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProdutoResource {

    private final ProdutoService produtoService;

    public ProdutoResource(final ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> getAllProdutos() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> getProduto(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(produtoService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createProduto(@RequestBody @Valid final ProdutoDTO produtoDTO) {
        final UUID createdId = produtoService.create(produtoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateProduto(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ProdutoDTO produtoDTO) {
        produtoService.update(id, produtoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable(name = "id") final UUID id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
