package com.example.parsecurse;

/**
 * Класс информации о продукте
 * @author Gonza1es
 */
public class Table {
    /** Название продукта*/
    private String product;
    /** Цена продукта*/
    private String value;
    /** Название магазина*/
    private String shopName;

    /**
     * Конструктор
     * @param shopName название магазина
     * @param product название товара
     * @param value цена товара
     */
    public Table(String shopName,String product, String value) {
        this.shopName = shopName;
        this.product = product;
        this.value = value;
    }

    /**
     * Функция возращения названия продукта
     * @return название продукта
     */
    public String getProduct() {
        return product;
    }

    /**
     * Функция возращения цена продукта
     * @return цена продукта
     */
    public String getValue() {
        return value;
    }

    /**
     * Функция возращения названия магазинов
     * @return название магазина
     */
    public String getShopName() {
        return shopName;
    }
}
