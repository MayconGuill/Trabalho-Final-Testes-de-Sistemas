package com.bibliotech.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioSeleniumTest {

    public static WebDriver driver;

    @BeforeEach
    void init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
    }

    @Test
    void deveFazerLoginComSucesso() {
        driver.get("http://localhost:8080/login");

        driver.findElement(By.id("email")).sendKeys("admin@bibliotech.com");
        driver.findElement(By.id("senha")).sendKeys("admin123");
        driver.findElement(By.id("btn-login")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/dashboard"),
                "Após login válido, deveria redirecionar para /dashboard");

        WebElement mensagem = driver.findElement(By.id("mensagem-bemvindo"));
        assertTrue(mensagem.isDisplayed(),
                "Mensagem de boas-vindas deveria estar visível");
    }

    @Test
    void deveCadastrarUmUsuarioComSucesso() {
        driver.get("http://localhost:8080/usuarios/novo");

        driver.findElement(By.id("nome")).sendKeys("administrador 2");
        driver.findElement(By.id("email")).sendKeys("admin2@bibliotech.com");
        driver.findElement(By.id("cpf")).sendKeys("123.456.789-00");
        Select comboTipo = new Select(driver.findElement(By.id("tipo")));
        comboTipo.selectByVisibleText("Aluno");
        driver.findElement(By.id("senha")).sendKeys("admin123");
        driver.findElement(By.cssSelector(".btn.btn-primary")).click();

        WebDriverWait aguardaCadastro = new WebDriverWait(driver, Duration.ofSeconds(5));
        aguardaCadastro.until(ExpectedConditions.urlContains("/usuarios"));

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/usuarios/salvar") || urlAtual.contains("/novo"),
                "Após cadastrar usuario, deve redirecionar para /usuarios");
    }

    @Test
    void deveExibirMensagemDeErroQuandoEmailJaExistir() {
        driver.get("http://localhost:8080/usuarios/novo");

        driver.findElement(By.id("nome")).sendKeys("administrador 2");
        driver.findElement(By.id("email")).sendKeys("admin2@bibliotech.com");
        driver.findElement(By.id("cpf")).sendKeys("123.456.789-01");
        Select comboTipo = new Select(driver.findElement(By.id("tipo")));
        comboTipo.selectByVisibleText("Aluno");
        driver.findElement(By.id("senha")).sendKeys("admin123");
        driver.findElement(By.cssSelector(".btn.btn-primary")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/usuarios/salvar") || urlAtual.contains("/novo"),
                "Após tentar cadastrar e-mail duplicado, deveria permanecer na página do formulário");

        List<WebElement> mensagens = driver.findElements(By.cssSelector(".alert-danger"));

        assertFalse(mensagens.isEmpty(),
                "A mensagem de erro (.alert-danger) deveria ser exibida na tela, mas não foi encontrada.");
    }

    @Test
    void deveExibirMensagemDeErroQuandoCpfJaExistir() {
        driver.get("http://localhost:8080/usuarios/novo");

        driver.findElement(By.id("nome")).sendKeys("administrador 3");
        driver.findElement(By.id("email")).sendKeys("admin3@bibliotech.com");
        driver.findElement(By.id("cpf")).sendKeys("123.456.789-00");

        Select comboTipo = new Select(driver.findElement(By.id("tipo")));
        comboTipo.selectByVisibleText("Aluno");
        driver.findElement(By.id("senha")).sendKeys("admin123");
        driver.findElement(By.cssSelector(".btn.btn-primary")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/usuarios/salvar") || urlAtual.contains("/novo"),
                "Após tentar cadastrar CPF duplicado, deveria permanecer na página do formulário");

        List<WebElement> mensagens = driver.findElements(By.cssSelector(".alert-danger"));

        assertFalse(mensagens.isEmpty(),
                "A mensagem de erro (.alert-danger) para CPF duplicado deveria ser exibida na tela, mas não foi encontrada.");
    }

    @Test
    void deveExibirMensagemDeErroQuandoCpfEstiverEmFormatoInvalido() {
        driver.get("http://localhost:8080/usuarios/novo");
        driver.findElement(By.id("nome")).sendKeys("Usuário Formato Errado");
        driver.findElement(By.id("email")).sendKeys("usuario.formato@bibliotech.com");
        driver.findElement(By.id("cpf")).sendKeys("12345678901");
        Select comboTipo = new Select(driver.findElement(By.id("tipo")));
        comboTipo.selectByVisibleText("Aluno");

        driver.findElement(By.id("senha")).sendKeys("admin123");
        driver.findElement(By.cssSelector(".btn.btn-primary")).click();

        String urlAtual = driver.getCurrentUrl();
        assertTrue(urlAtual.contains("/usuarios/salvar") || urlAtual.contains("/novo"),
                "O sistema não deveria salvar um usuário com o CPF fora do padrão XXX.XXX.XXX-XX.");

        List<WebElement> alertasErro = driver.findElements(By.cssSelector(".alert-danger"));

        assertFalse(alertasErro.isEmpty(),
                "A mensagem de erro (.alert-danger) validando o formato correto do CPF deveria ser exibida na tela.");

        if (!alertasErro.isEmpty()) {
            String textoErro = alertasErro.get(0).getText().toLowerCase();
            assertTrue(textoErro.contains("cpf") || textoErro.contains("formato") || textoErro.contains("inválido"),
                    "A mensagem de erro deveria alertar sobre o formato incorreto do CPF.");
        }
    }
}
