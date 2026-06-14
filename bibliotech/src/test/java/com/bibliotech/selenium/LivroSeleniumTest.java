package com.bibliotech.selenium;

import com.bibliotech.config.BaseSeleniumConfig;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LivroSeleniumTest extends BaseSeleniumConfig {
    @Test
    void deveCadastrarLivroComSucesso() {
        driver.get("http://localhost:8080/livros/novo");

        driver.findElement(By.id("titulo")).sendKeys("Código Limpo");
        driver.findElement(By.id("autor")).sendKeys("Robert C. Martin");
        driver.findElement(By.id("isbn")).sendKeys("978-0132350884");
        driver.findElement(By.id("editora")).sendKeys("Alta Books");
        driver.findElement(By.id("ano")).sendKeys("2009");
        driver.findElement(By.id("quantidadeExemplares")).sendKeys("5");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait aguarda =
                new WebDriverWait(driver, Duration.ofSeconds(5));
        aguarda.until(ExpectedConditions.urlContains("/livros"));

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/livros"),
                "Após salvar o livro com sucesso, deveria redirecionar para a listagem /livros");
    }

    @Test
    void deveFiltrarLivrosPorTitulo() {
        driver.get("http://localhost:8080/livros");

        Select selectTipo =
                new Select(driver.findElement(By.name("tipo")));
        selectTipo.selectByValue("titulo");
        WebElement inputBusca = driver.findElement(By.name("busca"));
        inputBusca.sendKeys("Código Limpo");
        driver.findElement(By.cssSelector("form[action='/livros'] button[type='submit']")).click();

        List<WebElement> linhasLivros = driver.findElements(By.className("livro-item"));
        assertFalse(linhasLivros.isEmpty(),
                "A tabela deveria listar pelo menos o livro correspondente ao termo pesquisado.");
    }

    @Test
    void deveEditarLivroComSucesso() {
        driver.get("http://localhost:8080/livros");

        WebElement botaoEditar = driver.findElement(By.cssSelector(".btn-warning"));
        botaoEditar.click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/livros/editar"), "Deveria estar na página de edição do livro.");
        WebElement inputTitulo = driver.findElement(By.id("titulo"));
        inputTitulo.clear();
        inputTitulo.sendKeys("Código Limpo - Edição Revisada");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait aguarda =
                new WebDriverWait(driver, Duration.ofSeconds(5));
        aguarda.until(ExpectedConditions.urlContains("/livros"));

        List<WebElement> alertasSucesso = driver.findElements(By.cssSelector(".alert-success"));
        assertFalse(alertasSucesso.isEmpty(),
                "A mensagem de sucesso (.alert-success) deveria aparecer após editar o livro.");
    }

    @Test
    void deveImpedirEnvioDoFormularioQuandoAnoForMaiorQuePermitido() {
        driver.get("http://localhost:8080/livros/novo");

        driver.findElement(By.id("titulo")).sendKeys("Livro do Futuro");
        driver.findElement(By.id("autor")).sendKeys("Autor Desconhecido");
        driver.findElement(By.id("isbn")).sendKeys("555-5555555555");
        driver.findElement(By.id("editora")).sendKeys("Tech Edições");
        driver.findElement(By.id("ano")).sendKeys("2500");
        driver.findElement(By.id("quantidadeExemplares")).sendKeys("1");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/novo"),
                "O formulário não deveria ter sido enviado, permanecendo na página de cadastro.");

        List<WebElement> alertasSucesso = driver.findElements(By.cssSelector(".alert-success"));
        List<WebElement> alertasErro = driver.findElements(By.cssSelector(".alert-danger"));

        assertTrue(alertasSucesso.isEmpty(),
                "Não deveria exibir mensagem de sucesso, pois o formulário possui dados inválidos.");
        assertTrue(alertasErro.isEmpty(),
                "Não deveria exibir mensagem de erro do servidor (.alert-danger), pois o bloqueio foi nativo do HTML5 no navegador.");
    }

    @Test
    void deveFiltrarLivrosPorAutor() {
        driver.get("http://localhost:8080/livros");

        Select selectTipo =
                new Select(driver.findElement(By.name("tipo")));
        selectTipo.selectByValue("autor");
        WebElement inputBusca = driver.findElement(By.name("busca"));
        inputBusca.sendKeys("Martin");
        driver.findElement(By.cssSelector("form[action='/livros'] button[type='submit']")).click();

        List<WebElement> linhasLivros = driver.findElements(By.className("livro-item"));
        assertFalse(linhasLivros.isEmpty(),
                "A tabela de livros deveria exibir os resultados correspondentes ao autor pesquisado.");
    }

    @Test
    void deveExibirMensagemDeErroQuandoIsbnForDuplicado() {
        driver.get("http://localhost:8080/livros/novo");
        driver.findElement(By.id("titulo")).sendKeys("Livro Original");
        driver.findElement(By.id("autor")).sendKeys("Autor A");
        driver.findElement(By.id("isbn")).sendKeys("978-8575226476");
        driver.findElement(By.id("editora")).sendKeys("Novatec");
        driver.findElement(By.id("ano")).sendKeys("2018");
        driver.findElement(By.id("quantidadeExemplares")).sendKeys("2");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait aguarda =
                new WebDriverWait(driver, Duration.ofSeconds(5));
        aguarda.until(ExpectedConditions.urlContains("/livros"));

        driver.get("http://localhost:8080/livros/novo");
        driver.findElement(By.id("titulo")).sendKeys("Livro Clone");
        driver.findElement(By.id("autor")).sendKeys("Autor B");
        driver.findElement(By.id("isbn")).sendKeys("978-8575226476");
        driver.findElement(By.id("editora")).sendKeys("Outra Editora");
        driver.findElement(By.id("ano")).sendKeys("2020");
        driver.findElement(By.id("quantidadeExemplares")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/livros/salvar") || urlAtual.contains("/novo"),
                "O sistema deveria bloquear o envio e permanecer na página do formulário.");

        List<WebElement> alertasErro = driver.findElements(By.cssSelector(".alert-danger"));

        assertFalse(alertasErro.isEmpty(),
                "A caixinha de erro (.alert-danger) deveria ser exibida informando que o ISBN já está cadastrado.");

        if (!alertasErro.isEmpty()) {
            String textoErro = alertasErro.get(0).getText().toLowerCase();
            assertTrue(textoErro.contains("isbn") || textoErro.contains("já existe") || textoErro.contains("cadastrado"),
                    "A mensagem de erro deveria alertar sobre a duplicação do ISBN.");
        }
    }
}
