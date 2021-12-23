package com.example.parsecurse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Класс, парсящий сайт Эльдорадо
 * @author Gonza1es
 */
public class ParseAldorado implements Callable<Integer> {
    /** Переменная, хранящая код страницы*/
    private static Document page;
    /** Переменная, используемая для перевода строки в файл*/
    final String crutch = "\r\n";
    /** Список наименований продуктов*/
    ArrayList<String> product;
    /** Список наименований брендов*/
    ArrayList<String> brand;

    /**
     * Конструктор класса ParseAldor
     * @param product - список наименований продуктов
     * @param brand - список наименований брэндов
     */
    public ParseAldorado(ArrayList<String> product, ArrayList<String> brand){
        this.product=product;
        this.brand = brand;
    }

    /**
     * Функция, подключающаяся к необходимой странице и возвращающая ее код {@link ParseAldorado#getPage(String)}
     * @param name - наименование категории товаров
     * @return код необходимой страницы
     * @throws IOException - исключение для потоков
     */
    private static Document getPage(String name) throws IOException {
        if (name.equals("Холодильник"))
            page = Jsoup.connect("https://www.eldorado.ru/c/kholodilniki/").get();
        if (name.equals("Телевизор"))
            page = Jsoup.connect("https://www.eldorado.ru/c/televizory/").get();
        if (name.equals("Стиральная машина"))
            page = Jsoup.connect("https://www.eldorado.ru/c/stiralnye-mashiny/").get();
        if (name.equals("Сплит-система"))
            page = Jsoup.connect("https://www.eldorado.ru/c/konditsionery-split-sistemy/").get();
        if (name.equals("Микроволновая печь"))
            page = Jsoup.connect("https://www.eldorado.ru/c/mikrovolnovye-pechi/").get();
        return page;
    }

    /**
     * Функция, проверяющая, соответствует ли данный товар данному бренду
     * @param brand - наименование бренда
     * @param title - наименование товара
     * @return true, если соответвует, false - если нет
     */
    private Boolean isFoundBrand(String brand, String title){
        if (title.lastIndexOf(brand)!=-1)
            return true;
        return false;
    }

    /**
     * Функция, обрабатывающая алгоритм класса
     * @return 1
     */
    @Override
            public Integer call() {
                try {
                    FileOutputStream outputProduct = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Aldorado\\productAldorado.txt");
                    FileOutputStream outputPrice = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Aldorado\\priceAldorado.txt");
                    for (String s: product) { // Цикл по всем продуктам
                        Document web = getPage(s); // Получение кода страницы
                        Elements elementsTitle = web.getElementsByClass("sG"); //получения списка наименований товаров
                        Elements elementsPrice = web.getElementsByClass("lS"); //получение списка цен товаров
                        for (int i = 0; i<=10;i++) {
                            if (brand.isEmpty()){ //если список брендов пуст
                                outputProduct.write(elementsTitle.get(i).text().getBytes()); //запись наименования товара в файл
                                outputProduct.write(crutch.getBytes()); //запись переноса строки в файл
                                outputPrice.write(elementsPrice.get(i+1).text().getBytes()); // запись цены товара в файл
                                outputPrice.write(crutch.getBytes());
                            } else {
                                for (String str : brand) {
                                    if (i<elementsTitle.size()) {
                                        if (isFoundBrand(str, elementsTitle.get(i).text())) { //Проверка соответствия товара бренду
                                            outputProduct.write(elementsTitle.get(i).text().getBytes());
                                            outputProduct.write(crutch.getBytes());
                                            outputPrice.write(elementsPrice.get(i + 1).text().getBytes());
                                            outputPrice.write(crutch.getBytes());
                                        }
                                    }
                                }
                            }
                        }
                        elementsTitle.clear(); //очистка списка товаров
                        elementsPrice.clear(); //очистка списка цен
                    }
                    outputPrice.close(); //закрытие потока вывода
                    outputProduct.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return 0;
            }
    }