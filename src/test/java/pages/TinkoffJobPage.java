package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TinkoffJobPage extends Page {
    /**
     * Для всех полей, кроме вакансии методы для клика/отправки/получения текста ошибки унифицированы
     * Так как для этих полей определено name
     * И по каким-то причинам поле вакансия его не имеет
     */

    public TinkoffJobPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.navigate().to("https://moscow-job.tinkoff.ru");
        isLoaded("Работа в Тинькофф Банке в Москве");
    }

    public void clickOnFreeSpace() {
        driver.findElement(By.xpath("//div[contains(@class,'Header__centered')]")).click();
        logger.info("Нажатие на пустое место формы");

    }

    public void clickOnFieldByName(String nameField) {
        By item = By.xpath(String.format("//input[@name='%s']", nameField));
        wait.until( d-> {
            driver.findElement(item).click();
            logger.info("Нажатие на '" + nameField + "'");
            return true;
        });
    }

    public void clickOnFieldVacancy() {
        By item = By.xpath("//div[contains(@class, 'SelectItem') and text()='Выберите вакансию']");
        wait.until( d-> {
            driver.findElement(item).click();
            logger.info("Нажатие на поле 'Вакансия'");
            return true;
        });
    }

    public void sendStringInFieldByName(String nameField, String message) {
        logger.info("Отправка '" + message + "' в поле '" + nameField + "'....");
        By item = By.xpath(String.format("//input[@name='%s']", nameField));
        wait.until( d-> {
            driver.findElement(item).sendKeys(message);
            logger.info("Сообщение '" + message + "' успешно отправлено в поле '" + nameField + "'");
            return true;
        });
    }

    public void selectItemInVacancyByText (String text) {
        By item = By.xpath(String.format("//div[contains(@class, 'SelectItem') and contains(text(),'%s')]", text));
        wait.until( d-> {
            driver.findElement(item).click();
            logger.info("В поле 'Вакансия' выбрано " + driver.findElement(item).getText());
            return true;
        });
    }

    public boolean checkErrorMessageByName(String nameField, String errorMessage) {
        /**
         * Если вдруг напугал xpath, то он значит это:
         * Сначала нааходит все элементы Row, среди этих элементов находит один
         * У которого в потомках имеется что-то, чье имя содержит искомое поле (fio, city...)
         * И после этого находит элемент веб-страницы, связанный с ошибкой
         */
        logger.info("Проверка наличия ошибки '" + errorMessage + "' у поля '" + nameField + "'....");
        String path = String.format(
                "//div[contains(@class,'Row') and descendant::node()[contains(@name,'%s')]]" +
                        "//div[contains(@class, 'Error')]",
                nameField
        );
        By items = By.xpath(path);
        List<WebElement> listElements = driver.findElements(items);
        wait.until(d -> listElements.size()>0);
        if (listElements.size() == 1) {
            return listElements.get(0).getText().equals(errorMessage) ;
        } else
            return listElements.get(0).getText().equals(errorMessage)
                    || listElements.get(1).getText().equals(errorMessage);
    }

    public boolean checkErrorMessageInVacancy(String errorMessage) {
        logger.info("Проверка наличия ошибки '" + errorMessage + "' у поля 'Вакансия'....");
        String path = "//div[contains(@class,'Row') and descendant::node()[contains(@class,'Select')]]" +
                "//div[contains(@class, 'Error')]";
        By item = By.xpath(path);
        wait.until( d-> {
            driver.findElement(item);
            return true;
        });
        return driver.findElement(item).getText().equals(errorMessage);
    }


}
