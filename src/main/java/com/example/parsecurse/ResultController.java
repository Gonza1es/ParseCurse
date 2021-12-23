package com.example.parsecurse;


import java.io.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Класс, обслуживающий окно результата
 */
public class ResultController {

    /** Видимый список*/
    ObservableList<Table>  list;

    /** Столбец цен*/
    @FXML
    private TableColumn<Table, String> data1;

    /** Кнопка скачивания таблицы*/
    @FXML
    private Button downloadButton;

    /** Столбец названия продуктов*/
    @FXML
    private TableColumn<Table, String> productName;

    /** Столбец названия магазинов*/
    @FXML
    private TableColumn<Table,String> shopName;

    /** Таблица*/
    @FXML
    private TableView<Table> tableArea;

    /**
     * Функция, проверяющая пустой ли файл
     * @param file проверяемый файл
     * @return true, если пустой, false, если не пустой
     */
    private boolean isEmptyFile(File file){
        if (file.length()==0)
            return true;
        else return false;
    }

    /**
     * Функция, обрабатывающая алгоритм класса
     * @throws IOException
     */
    @FXML
    void initialize() throws IOException {
        ArrayList<Table> result = inputFileToString(); // список объектов класса Table
        list = FXCollections.observableArrayList(result); //инициализация видимого списка

        shopName.setCellValueFactory(new PropertyValueFactory<Table,String>("shopName")); // инициализация столбца названия магазинов
        productName.setCellValueFactory(new PropertyValueFactory<Table, String>("product")); // инициализация столбца продуктов
        data1.setCellValueFactory(new PropertyValueFactory<Table, String>("value")); //инициализация столбца цен

        tableArea.setItems(list); //вывод таблицы на экран

        downloadButton.setOnAction(actionEvent -> { //При нажатии на кнопку скачивается таблица
            Workbook workbook = new HSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("sample");

            Row row = spreadsheet.createRow(0); //нумерация столбцоа начинается с 0

            for (int j = 0; j < tableArea.getColumns().size(); j++) {
                row.createCell(j).setCellValue(tableArea.getColumns().get(j).getText()); //создание столбцов
            }

            for (int i = 0; i < tableArea.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1); //заполнение столбцов
                for (int j = 0; j < tableArea.getColumns().size(); j++) {
                    if(tableArea.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(tableArea.getColumns().get(j).getCellData(i).toString());
                    }
                    else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }
            try{
                FileOutputStream fileOut = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Результаты.xls");
                workbook.write(fileOut);
                fileOut.close(); //закрытие потока вывода
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Функция нахождения пути к нужной директории
     * @param shopName название магазина
     * @return директория, связанная с магазином
     */
    private File findDirectory(String shopName) {
        File file = null;
        if (shopName.equals("Эльдорадо")) { //если магазин "Эльдорадо"
            file = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Aldorado");
            return file;
        }
        if (shopName.equals("WildBerries")) { //если магазин "WildBerries"
            file = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\WB");
            return file;
        }
        if (shopName.equals("ИмперияТехно")) { //если магазин "ИмперияТехно"
            file = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Imperiatechno");
            return file;
        }
        return file;
    }

    /**
     * Функция заполняющая список объектов Table, содержащих информацию о товарах и магазинах
     * @return список объектов класса Table
     * @throws IOException
     */
    private ArrayList<Table> inputFileToString() throws IOException {
        int count; //монитор номера файла в директории
        ArrayList<String> priceColumn = new ArrayList<>();
        ArrayList<String> productNameColumn = new ArrayList<>();
        ArrayList<Table> table = new ArrayList();
        File option = new File("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\options.txt");
        FileReader optionReader = new FileReader(option);
        BufferedReader bufOptionReader = new BufferedReader(optionReader);
        String line = bufOptionReader.readLine(); //считывания названия магазина
        while (line != null) { //пока файл option.txt не закончится
            File path = findDirectory(line);
            String shopNameString = line;
            if (path.isDirectory()) {
                count = 1;
                for (File item : path.listFiles()) { //возращения списка всех файлов директории path
                    if (item.exists() && !item.isDirectory()) {
                        if (!isEmptyFile(item)) { //если файл не пустой
                            FileReader fileReader = new FileReader(item);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String lineProduct = bufferedReader.readLine(); //считывания названия продукта
                            while (lineProduct != null) {
                                if (count % 2 == 0) { //если номер магазина четный, то
                                    productNameColumn.add(lineProduct); // заполняем список продуктов
                                } else { //иначе
                                    priceColumn.add(lineProduct); //заполняем список цен
                                }
                                lineProduct = bufferedReader.readLine(); //считыванием следуюбщий продукт
                            }
                            fileReader.close();
                            bufferedReader.close();
                            count++;
                            if (!productNameColumn.isEmpty()) {
                                for (int i = 0; i < productNameColumn.size(); i++) {
                                    table.add(new Table(shopNameString, productNameColumn.get(i), priceColumn.get(i))); //инициализация списка объектов Table
                                }
                            }
                        }
                    }
                }
            }
            line = bufOptionReader.readLine();
            productNameColumn.clear();
            priceColumn.clear();
        }
        optionReader.close();
        bufOptionReader.close();
        return table;
    }
}
