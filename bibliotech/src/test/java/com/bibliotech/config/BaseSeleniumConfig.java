package com.bibliotech.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public abstract class BaseSeleniumConfig {
    protected static WebDriver driver;

    @RegisterExtension
    static final ScreenshotEFechamentoExtension junitOficial = new ScreenshotEFechamentoExtension();

    @BeforeEach
    void init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
    }

    private static class ScreenshotEFechamentoExtension implements AfterEachCallback {

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            if (driver != null) {
                boolean testeFalhou = context.getExecutionException().isPresent();

                if (testeFalhou) {
                    String nomeClasse = context.getRequiredTestClass().getSimpleName();
                    String nomeTeste = context.getRequiredTestMethod().getName();
                    try {
                        File arquivoTemporario = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        File destino = new File("evidencias/screenshots/" + nomeClasse + "/" + nomeTeste + ".png");
                        destino.getParentFile().mkdirs();

                        Files.copy(arquivoTemporario.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("📸 [FALHA] Print salvo em: " + destino.getAbsolutePath());
                    } catch (Exception e) {
                        System.err.println("❌ Erro ao tirar print: " + e.getMessage());
                    }
                } else {
                    System.out.println("☀️ [SUCESSO] Teste passou sem falhas.");
                }

                driver.quit();
                System.out.println("✅ Navegador encerrado.");
            }
        }
    }
}