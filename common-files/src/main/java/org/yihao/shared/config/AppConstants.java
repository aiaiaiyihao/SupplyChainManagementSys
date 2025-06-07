package org.yihao.shared.config;

public class AppConstants {
    public static final String ADMINEMAIL = "aiaiaiyihao@gmail.com";

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "50";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_PRODUCTS_BY = "productId";
    public static final String SORT_DIR = "asc";

    private static final Integer PARTITION_NUMBER = 5;
    private static final Short REPLICATION_FACTOR = (short) 1;

    private static final String TOPIC_NAME = "JTSP-demo-3";

    public static final String TOPIC_NAME_ORDER = "orders";
    public static final String TOPIC_NAME_ORDERAPPROVAL = "ordersApproval";
    public static final String TOPIC_NAME_ORDERDENIAL = "ordersDenial";
    public static final String TOPIC_NAME_INVITATION = "Invitation";

    public static final String TOPIC_NAME_DELIVERY_DELAY = "deliveryDelay";
    public static final String TOPIC_NAME_DELIVERY_DELAY_DRIVER = "deliveryDelayDriver";

    private static final Integer PARTITION_NUMBER_ORDER = 2;


    public static final String TOPIC_NAME_DELIVERY = "deliverySupplier";
}
