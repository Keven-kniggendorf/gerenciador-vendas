package nevek.grupovendas.gerenciador_vendas.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import nevek.grupovendas.gerenciador_vendas.domain.Categoria;
import nevek.grupovendas.gerenciador_vendas.domain.Produto;
import nevek.grupovendas.gerenciador_vendas.domain.Usuario;
import nevek.grupovendas.gerenciador_vendas.model.ProdutoDTO;
import nevek.grupovendas.gerenciador_vendas.repos.CategoriaRepository;
import nevek.grupovendas.gerenciador_vendas.repos.ProdutoRepository;
import nevek.grupovendas.gerenciador_vendas.repos.UsuarioRepository;
import nevek.grupovendas.gerenciador_vendas.repos.VendaRepository;
import nevek.grupovendas.gerenciador_vendas.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final VendaRepository vendaRepository;

    public ProdutoService(final ProdutoRepository produtoRepository,
            final UsuarioRepository usuarioRepository,
            final CategoriaRepository categoriaRepository, final VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<ProdutoDTO> findAll() {
        final List<Produto> produtoes = produtoRepository.findAll(Sort.by("id"));
        return produtoes.stream()
                .map(produto -> mapToDTO(produto, new ProdutoDTO()))
                .toList();
    }

    public ProdutoDTO get(final UUID id) {
        return produtoRepository.findById(id)
                .map(produto -> mapToDTO(produto, new ProdutoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ProdutoDTO produtoDTO) {
        final Produto produto = new Produto();
        mapToEntity(produtoDTO, produto);
        return produtoRepository.save(produto).getId();
    }

    public void update(final UUID id, final ProdutoDTO produtoDTO) {
        final Produto produto = produtoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(produtoDTO, produto);
        produtoRepository.save(produto);
    }

    public void delete(final UUID id) {
        final Produto produto = produtoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        vendaRepository.findAllByProduto(produto)
                .forEach(venda -> venda.getProduto().remove(produto));
        produtoRepository.delete(produto);
    }

    private ProdutoDTO mapToDTO(final Produto produto, final ProdutoDTO produtoDTO) {
        produtoDTO.setId(produto.getId());
        produtoDTO.setNome(produto.getNome());
        produtoDTO.setDescricao(produto.getDescricao());
        produtoDTO.setPreco(produto.getPreco());
        produtoDTO.setQuantidade(produto.getQuantidade());
        produtoDTO.setDataCriacao(produto.getDataCriacao());
        produtoDTO.setUsuario(produto.getUsuario() == null ? null : produto.getUsuario().getId());
        produtoDTO.setCategoria(produto.getCategoria() == null ? null : produto.getCategoria().getId());
        return produtoDTO;
    }

    private Produto mapToEntity(final ProdutoDTO produtoDTO, final Produto produto) {
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidade(produtoDTO.getQuantidade());
        produto.setDataCriacao(produtoDTO.getDataCriacao());
        final Usuario usuario = produtoDTO.getUsuario() == null ? null : usuarioRepository.findById(produtoDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        produto.setUsuario(usuario);
        final Categoria categoria = produtoDTO.getCategoria() == null ? null : categoriaRepository.findById(produtoDTO.getCategoria())
                .orElseThrow(() -> new NotFoundException("categoria not found"));
        produto.setCategoria(categoria);
        return produto;
    }

}
