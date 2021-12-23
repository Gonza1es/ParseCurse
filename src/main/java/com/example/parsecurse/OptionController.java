package com.example.parsecurse;

import java.io.*;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * Класс, обслуживающий окно настроек
 * @author Gonza1es
 */
public class OptionController {

    /**
     * Поле выбора Эльдорадо
     */
    @FXML
    private CheckBox aldorSelect;

    /**
     * Кнопка закрытия окна настроек
     */
    @FXML
    private Button closeButton;

    /**
     * Поле выбора WildBerries
     */
    @FXML
    private CheckBox WBSelect;

    /**
     * Поле выбора ИмперияТехно
     */
    @FXML
    private CheckBox ImperSelect;

    /**
     * Функция, которая передает true CheckBox-полям, если они были выбраны при предыдущем запуске программы
     * @throws IOException
     */
    private void selectedCheckBox() throws IOException {
        File file = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\options.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        while (line!=null){
            if (line.equals("Эльдорадо"))
                aldorSelect.setSelected(true);
            if (line.equals("WildBerries"))
                WBSelect.setSelected(true);
            if (line.equals("ИмперияТехно"))
                ImperSelect.setSelected(true);
            line = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();
    }

    /**
     * Функция, контролирующая действия с интерфейсом
     * @throws IOException
     */
    @FXML
    void initialize() throws IOException {
        selectedCheckBox();

        //Запуск алгоритма программы при нажание кнопки "Закрыть"
        closeButton.setOnAction(actionEvent -> {
            try {
                File file = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\options.txt");
                ArrayList<String> selectedShops = checkSelected();
                FileOutputStream output = new FileOutputStream(file);
                for (String shops: selectedShops){ //Цикл по всем выбранным магазинам
                    try {
                        output.write(shops.getBytes()); //Запись в файл option.txt, какие магазины были выбраны
                    } catch (IOException e){
                        System.out.println("Error");
                    }
                }
                try {
                    output.close(); //закрытие потока ввода
                } catch (IOException e){
                    System.out.println("error");
                }
            } catch (FileNotFoundException e){
                System.out.println("file not found");
            }
            Stage stage = (Stage) closeButton.getScene().getWindow(); //Закрытие окна
            stage.close();
        });
    }

    /**
     * Функция, заполняющая option.txt, если были выбораны магазины
     * @return check - список выбранных магазинов
     */
    private ArrayList<String> checkSelected(){
        ArrayList<String> check = new ArrayList<>();
        if (aldorSelect.isSelected())
            check.add("Эльдорадо\r\n");
        if (WBSelect.isSelected())
            check.add("WildBerries\r\n");
        if (ImperSelect.isSelected())
            check.add("ИмперияТехно\r\n");
        return check;
    }

}
