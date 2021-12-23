package com.example.parsecurse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

    /*
     * Класс, обслуживающий стартовый интерфейс
      @ autor Гнездилов Максим
     */

public class StartController {
    /** Кнопка "как пользоваться программой?"*/
    @FXML
    private Button FQButton;


    /** Поле выбора брэнда "LG"*/
    @FXML
    private CheckBox LGSelect;

    /** Поле выбора брэнда "Samsung"*/
    @FXML
    private CheckBox samsungSelect;

    /** Поле выбора категории "Телевизоры"*/
    @FXML
    private CheckBox TVSelect;

    /** Поле выбора брэнда "Ariston"*/
    @FXML
    private CheckBox aristonSelect;

    /** Поле выбора брэнда "Bosch"*/
    @FXML
    private CheckBox boschSelect;

    /** Поле выбора категории "Холодильники"*/
    @FXML
    private CheckBox freezSelect;

    /** Поле выбора брэнда "Hyundai"*/
    @FXML
    private CheckBox hendaSelect;

    /** Поле выбора категории "Микроволновые печи"*/
    @FXML
    private CheckBox mikroSelect;

    /** Кнопка настроек*/
    @FXML
    private Button optionButton;


    /** Поле выбора категории "Сплит-системы"*/
    @FXML
    private CheckBox splitSelect;

    /** Кнопка "Отслеживать цены"*/
    @FXML
    private Button startButton;

    /** Поле выбора категории "Стиральные машины"*/
    @FXML
    private CheckBox stirSelect;

    /**
     * Функция, проверяющая, пустой ли файл или нет
     * @param file - проверяемый файл
     * @return true - если пустой, false - если не пустой
     */
    private boolean isEmptyFile(File file){
        if (file.length()==0)
            return true;
        else return false;
    }

    /**
     * Функция, контролирующая действия с интерфейсом
     */
    @FXML
    void initialize() throws FileNotFoundException {
        /*
        В случае нажатия кнопки "Как использовать программу?"
        Открывается информационное окно
         */
        FQButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Как использовать программу?");
            alert.setHeaderText(null);
            alert.setContentText("Чтобы воспользовать программой, кликните по шестеренке и выберите магазины, в которых " +
                    "нужно искать товар. Затем нажмите кнопку \"закрыть\", в меню выберите категории товаров и брэнды, " +
                    "если необходимо, затем нажмите кнопку \"Отследить цены\".");

            alert.showAndWait();
        });
        /*
        В случае нажатия кнопки "настройки" открывается окно настроек
         */
        optionButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("OptionView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 298, 206);
                Stage stage = new Stage();
                stage.setTitle("Настройки");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        /*
        При нажатии кнопки "Отслеживать цены" программы выполняет основную задачу
         */
        startButton.setOnAction(actionEvent -> {
            ExecutorService es = Executors.newFixedThreadPool(3); //Создание pool для дочерних потоков
                ArrayList<String> product = productName();
                ArrayList<String> brand = brandName();
                if (!product.isEmpty()){
                    ParseAldorado parseAldorado = new ParseAldorado(product,brand); //Создание объекта класса парсинга Эльдорадо
                    ParseWB parseWB = new ParseWB(product,brand); //Создание объекта класса парсинга WildBerries
                    ParseImper parseImper = new ParseImper(product,brand); //Создание объекта класса парсинга ИмперияТехно
                    Future subAldor = null;
                    Future subWB = null;
                    Future subImper = null;
                    List<Future<Integer>> threads = new ArrayList<>(); //Список запущенных потоков
                    File option = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\options.txt");
                    if (option.exists() && !option.isDirectory() && !isEmptyFile(option)) { //Если файл option.txt существует, не является директорией и непустой
                        try {
                            FileReader fileReader = new FileReader(option);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String line = bufferedReader.readLine(); //Считывание данных из файла option.txt
                            while (line != null) {
                                if (line.equals("Эльдорадо")) { //Если line = "Эльдорадо"
                                    subAldor = es.submit(parseAldorado);
                                    threads.add(subAldor); //Добавить в списко запущенных потоков
                                }
                                if (line.equals("WildBerries")) {
                                    subWB = es.submit(parseWB);
                                    threads.add(subWB);
                                }
                                if (line.equals("ИмперияТехно")) {
                                    subImper = es.submit(parseImper);
                                    threads.add(subImper);
                                }
                                line = bufferedReader.readLine();
                            }
                                for (Future<Integer> task : threads){
                                    task.get();
                                }

                                /* Запуск окна с результатам парсинга*/
                                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ResultView.fxml"));
                                Scene scene = new Scene(fxmlLoader.load(), 384, 400);
                                Stage stage = new Stage();
                                stage.setTitle("Результат");
                                stage.setScene(scene);
                                stage.show();

                        } catch (IOException | InterruptedException | ExecutionException e) { //Обработка исключений
                            e.printStackTrace();
                        }

                    } else { //Запуск предупреждающего окна, если файл option.txt окажется пустым
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка!");
                        alert.setHeaderText(null);
                        alert.setContentText("Не выбраны магазины! Чтобы это исправить, кликнете " +
                                "по шестеренке и в настройках выберите нужные магазины");

                        alert.showAndWait();
                    }
                } else { //Запуск предупреждающего окна, если не были выбраны категории товаров
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка!");alert.setHeaderText(null);
                    alert.setContentText("Не выбраны категории товаров");

                    alert.showAndWait();
                }
            });
    }

    /**
     * Функция, которая проверяет, какие категории товаров были выбраны
     * @return productName - список выбранных категорий
     */
    private ArrayList<String> productName(){
        ArrayList<String> productName = new ArrayList<>();
        if (freezSelect.isSelected())
            productName.add("Холодильник");
        if (TVSelect.isSelected())
            productName.add("Телевизор");
        if (stirSelect.isSelected())
            productName.add("Стиральная машина");
        if (splitSelect.isSelected())
            productName.add("Сплит-система");
        if (mikroSelect.isSelected())
            productName.add("Микроволновая печь");
        return productName;
    }

    /**
     * Функция, которая проверяет, какие бренды были выбраны
     * @return brandName - список выбранных брендов
     */

    private  ArrayList<String> brandName(){
        ArrayList<String> brandName = new ArrayList<>();
        if (samsungSelect.isSelected())
            brandName.add("Samsung");
        if (LGSelect.isSelected())
            brandName.add("LG");
        if (aristonSelect.isSelected())
            brandName.add("Ariston");
        if (boschSelect.isSelected())
            brandName.add("Bosch");
        if (hendaSelect.isSelected())
            brandName.add("Hyundai");
        return brandName;
    }

}
