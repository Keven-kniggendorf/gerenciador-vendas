package nevek.grupovendas.gerenciador_vendas.repos;

import java.util.List;
import nevek.grupovendas.gerenciador_vendas.domain.Cliente;
import nevek.grupovendas.gerenciador_vendas.domain.Produto;
import nevek.grupovendas.gerenciador_vendas.domain.Usuario;
import nevek.grupovendas.gerenciador_vendas.domain.Venda;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendaRepository extends JpaRepository<Venda, Long> {

    Venda findFirstByCliente(Cliente cliente);

    Venda findFirstByUsuario(Usuario usuario);

    Venda findFirstByProduto(Produto produto);

    List<Venda> findAllByProduto(Produto produto);

}
