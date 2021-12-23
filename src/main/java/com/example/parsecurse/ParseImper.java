package com.example.parsecurse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Класс парсинга ИмперииТехно
 * @author Gonza1es
 */
public class ParseImper implements Callable<Integer> {
    /**
     * @see ParseAldorado
     */
    private static Document page;
    final String crutch = "\r\n";

    ArrayList<String> product;
    ArrayList<String> brand;
    /**
     * Конструктор класса ParseAldor
     *
     * @param product - список наименований продуктов
     * @param brand   - список наименований брэндов
     */

    public ParseImper(ArrayList<String> product, ArrayList<String> brand) {
        this.product = product;
        this.brand = brand;
    }

    private static Document getPage(String name) throws IOException {
        if (name.equals("Холодильник"))
            page = Jsoup.connect("https://www.imperiatechno.ru/Holodilyniki/").get();
        if (name.equals("Телевизор"))
            page = Jsoup.connect("https://www.imperiatechno.ru/Televizory_ot_32_do_47/").get();
        if (name.equals("Стиральная машина"))
            page = Jsoup.connect("https://www.imperiatechno.ru/Stiralynye_mashiny/").get();
        if (name.equals("Сплит-система"))
            page = Jsoup.connect("https://www.imperiatechno.ru/Split-sistemy/").get();
        if (name.equals("Микроволновая печь"))
            page = Jsoup.connect("https://www.imperiatechno.ru/Mikrovolnovye_pechi/").get();
        return page;
    }

    private Boolean isFoundBrand(String brand, String title){
        if (title.lastIndexOf(brand)!=-1)
            return true;
        return false;
    }


            @Override
            public Integer call() {
                try {
                    FileOutputStream outputProduct = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Imperiatechno\\productImper.txt");
                    FileOutputStream outputPrice = new FileOutputStream("C:\\Users\\1\\IdeaProjects\\parseCurse\\src\\main\\files\\com.example.parsecurse\\Imperiatechno\\priceImper.txt");
                    for (String s: product) {
                        Document web = getPage(s);
                        Elements elementsTitle = web.getElementsByClass("product-info__name"); //получение списка наименований товаров
                        Elements elementsPrice = web.getElementsByClass("text--bold"); //получение списка цен товаров
                        for (int i = 0; i<10;i++) {
                            if (brand.isEmpty()){
                                outputProduct.write(elementsTitle.get(i).text().getBytes());
                                outputProduct.write(crutch.getBytes());
                                outputPrice.write(elementsPrice.get(i).text().getBytes());
                                outputPrice.write(crutch.getBytes());
                            } else {
                                for (String str : brand) {
                                    if (i<elementsTitle.size()) {
                                        if (isFoundBrand(str, elementsTitle.get(i).text())) {
                                            outputProduct.write(elementsTitle.get(i).text().getBytes());
                                            outputProduct.write(crutch.getBytes());
                                            outputPrice.write(elementsPrice.get(i).text().getBytes());
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
                } catch (IOException e){
                    e.printStackTrace();
                }
                return 2;
            }


}
