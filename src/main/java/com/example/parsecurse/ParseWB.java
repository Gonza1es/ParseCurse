package com.example.parsecurse;


import javafx.scene.control.Alert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Класс парсинга WildBerries
 * @author Gonza1es
 */

public class ParseWB implements Callable<Integer> {
    /** @see ParseAldorado */
    private static Document page;
    final String crutch = "\r\n";

    ArrayList<String> product;
    ArrayList<String> brand;

    public ParseWB(ArrayList<String> product, ArrayList<String> brand) {
        this.product = product;
        this.brand = brand;
    }

    /**
     * @see ParseAldorado#getPage(String)
     */
    private static Document getPage(String name) throws IOException {
        try {
            if (name.equals("Холодильник"))
                page = Jsoup.connect("https://www.wildberries.ru/catalog/bytovaya-tehnika/krupnaya-bytovaya-tehnika?sort=popular&page=1&xsubject=4681").get();
            if (name.equals("Телевизор"))
                page = Jsoup.connect("https://www.wildberries.ru/catalog/elektronika/tv-audio-foto-video-tehnika/televizory/televizory").get();
            if (name.equals("Стиральная машина"))
                page = Jsoup.connect("https://www.wildberries.ru/catalog/bytovaya-tehnika/krupnaya-bytovaya-tehnika?sort=popular&page=1&xsubject=4686").get();
            if (name.equals("Сплит-система"))
                page = Jsoup.connect("https://www.wildberries.ru/catalog/bytovaya-tehnika/krupnaya-bytovaya-tehnika?sort=popular&page=1&xsubject=5686").get();
            if (name.equals("Микроволновая печь"))
                page = Jsoup.connect("https://www.wildberries.ru/catalog/elektronika/tehnika-dlya-kuhni/prigotovlenie-blyud?page=1&xsubject=694").get();
            return page;
        } catch (UnknownHostException | NoRouteToHostException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка соединения!");
            alert.setHeaderText(null);
            alert.setContentText("Соединение разорвано. Не получилось подключиться к сайту.");

            alert.showAndWait();
        }
        return page;
    }

    private Boolean isFoundBrand(String brand, String title) {
        if (title.lastIndexOf(brand) != -1)
            return true;
        return false;
    }



    @Override
    public Integer call() {
        try {
            FileOutputStream outputProduct = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\WB\\productWB.txt");
            FileOutputStream outputPrice = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\WB\\priceWB.txt");
            for (String s : product) {
                Document web = getPage(s);
                Elements elementsBrand = web.getElementsByClass("brand-name"); //получение списка брендов товаров
                Elements elementsTitle = web.getElementsByClass("goods-name"); //получение списка наименований товаров
                Elements elementsPrice = web.getElementsByClass("lower-price"); //получение списка цен товаров
                for (int i = 0; i < 10; i++) {
                    if (brand.isEmpty()) {
                        if (i<elementsTitle.size()) {
                            outputProduct.write(elementsBrand.get(i).text().getBytes());
                            outputProduct.write(elementsTitle.get(i).text().getBytes());
                            outputProduct.write(crutch.getBytes());
                            outputPrice.write(elementsPrice.get(i + 1).text().getBytes());
                            outputPrice.write(crutch.getBytes());
                        }
                    } else {
                        for (String str : brand) {
                            if (i<elementsTitle.size()) {
                                if (isFoundBrand(str, elementsBrand.get(i).text())) {
                                    outputProduct.write(elementsTitle.get(i).text().getBytes());
                                    outputProduct.write(crutch.getBytes());
                                    outputPrice.write(elementsPrice.get(i + 1).text().getBytes());
                                    outputPrice.write(crutch.getBytes());
                                }
                            }
                        }
                    }
                }
                elementsTitle.clear();
                elementsPrice.clear();
            }
            outputPrice.close();
            outputProduct.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


