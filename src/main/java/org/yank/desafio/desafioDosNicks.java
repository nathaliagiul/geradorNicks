package org.yank.desafio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class desafioDosNicks {
	
	private static final String URL_FILE_DRIVER = "src/main/resources/drivers/phantomjs.exe";
	private static final String URL_FILE_RESULTADO = "src/main/resources/result/result.txt";
	
	private static final String URL_GERADOR_NICKS = "https://www.4devs.com.br/gerador_de_nicks";
	private static final String URL_GERADOR_CPFS = "https://www.4devs.com.br/gerador_de_cpf";
		
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {		
		WebDriver driver = initializePhantomjsDriver();
		WebDriver driver2 = initializePhantomjsDriver();
		
		gerarNick(driver);
		gerarCPF(driver, driver2);
		
		System.out.println("Finalizado!");
	}
	
	/**
	 * Método responsável por inicializar um Driver PhantomJS (sem interface
	 * gráfica) 
	 * 
	 */
	public static WebDriver initializePhantomjsDriver() {
		System.setProperty("phantomjs.binary.path", URL_FILE_DRIVER);
		return new PhantomJSDriver();
	}
	
	/**
	 * Método responsável por gerar os nicks aleatóriamente.
	 * 
	 * Passos:
	 * 1 - Acessar o gerador de nicks
	 * 2 - Selecionar o tipo do método aleatório
	 * 3 - Setar a quantidade de nicks gerados
	 * 4 - Setar o número de caracteres de cada Nick
	 * 5 - Clicar em gerar nick
	 * 
	 */
	public static void gerarNick(WebDriver driver) {
		driver.get(URL_GERADOR_NICKS);
		driver.findElement(By.xpath("//*[@id=\"method\"]/option[2]")).click();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).sendKeys("50");
		Select nrLetras = new Select(driver.findElement(By.id("limit")));
		nrLetras.selectByValue("8");
		driver.findElement(By.xpath("//*[@id=\"bt_gerar_nick\"]")).click();	
	}
	
	/**
	 * Método responsável por gerar os cpfs aleatoriamente e gravar em um arquivo os nicks e cpfs concatenados
	 * 
	 * Passos:
	 * 1 - Capturar nicks gerados
	 * 2 - Criar o arquivo
	 * 3 - Acessar o gerador de CPFs
	 * 4 - Clicar em gerar CPF
	 * 5 - Aguardar a presença do CPF após o estado "Gerando..."
	 * 6 - Capturar CPF
	 * 7 - Escrever no arquivo os dados
	 * 
	 */
	private static void gerarCPF(WebDriver driver, WebDriver driver2) throws FileNotFoundException {
		WebElement allElements = driver.findElement(By.xpath("//*[@id=\"nicks\"]/ul"));
		List<WebElement> Elements = allElements.findElements(By.tagName("li")); 
		
	    PrintStream stream = new PrintStream(new FileOutputStream(URL_FILE_RESULTADO));
	    for (int i = 0; i < Elements.size(); i++)
		{
	    	driver2.get(URL_GERADOR_CPFS);
			driver2.findElement(By.xpath("//*[@id=\"bt_gerar_cpf\"]")).click();

	      	new WebDriverWait(driver2, 10)
	      	.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"texto_cpf\"]/span")));

	      	String cpf = driver2.findElement(By.xpath("//*[@id=\"texto_cpf\"]")).getText();
	      	stream.println(Elements.get(i).getText()+"; "+cpf);
		}
	}
}
