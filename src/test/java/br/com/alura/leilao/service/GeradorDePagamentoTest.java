package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDePagamentoTest {


    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;

    @Mock
    private Clock clock;


    @BeforeEach
    public void beforeEach(){
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao(){
        Leilao leilao = leilaoParaOMock();
        Lance vencedor = leilaoParaOMock().getLanceVencedor();

        LocalDate data = LocalDate.of(2021, 11, 1);

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(vencedor);

        //Verifico se o método salvar() do pagamentoDao foi executado,
        // mas como possui um objeto que é criado apenas dentro desse método,
        // preciso "capturar" esse objeto, com o mockito,
        // vide: pagamentoCaptor
        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        //Verificando se as informações do pagamento estão corretas...
        Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
        Assert.assertFalse(pagamento.getPago());
        Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
    }

    private Leilao leilaoParaOMock(){

        Leilao leilao = new Leilao("PlayStation", new BigDecimal("3000"), new Usuario("Renato"));

        Lance unicoLance = new Lance(new Usuario("Ednardo"), new BigDecimal("2600"));

        leilao.propoe(unicoLance);
        leilao.setLanceVencedor(unicoLance);

        return leilao;
    }

}