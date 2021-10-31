package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;

    @Mock
    private LeilaoDao leilaoDao;

    @BeforeEach
    public void beforeEach(){
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(leilaoDao);
    }

    @Test
    void deveriaFinalizarUmLeilao() {
        List<Leilao> leiloes = listaDeLeiloesParaOMock();
        // quando o método buscarLeiloesExpirados() for chamado, então o seu retorno será
        // a lista de leiloes que criei nessa própria classe de teste, que chamei de: listaDeLeiloesParaOMock
        // e que atribui a variável leiloes
        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        service.finalizarLeiloesExpirados();

        Leilao leilao = leiloes.get(0);
        Assert.assertTrue(leilao.isFechado());
        Assert.assertEquals(new BigDecimal("2650"), leilao.getLanceVencedor().getValor());

        // Verifico se o método do meu mock foi executado.
        Mockito.verify(leilaoDao).salvar(leilao);
    }

    private List<Leilao> listaDeLeiloesParaOMock(){
        List<Leilao> listaDeLeiloes = new ArrayList<>();

        Leilao leilao = new Leilao("PlayStation", new BigDecimal("3000"), new Usuario("Renato"));

        Lance primeiroLance = new Lance(new Usuario("Ednardo"), new BigDecimal("2600"));

        Lance segundoLance = new Lance(new Usuario("Beto"), new BigDecimal("2650"));

        leilao.propoe(primeiroLance);
        leilao.propoe(segundoLance);

        listaDeLeiloes.add(leilao);

        return listaDeLeiloes;
    }

}