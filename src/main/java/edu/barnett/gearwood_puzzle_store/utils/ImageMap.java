package edu.barnett.gearwood_puzzle_store.utils;

import java.util.*;

/** Utility class to organize the provided image file paths by product code; This makes it easier to include the full list of product images on each product detail page*/
public final class ImageMap {
    private static final Map<String, List<String>> productImages = new HashMap<>();

    static {
        productImages.put("P1001", new ArrayList<>(List.of(
                "/product-images/P1001-01.jpg",
                "/product-images/P1001-02.jpg",
                "/product-images/P1001-03.jpg",
                "/product-images/P1001-04.jpg",
                "/product-images/P1001-05.jpg",
                "/product-images/P1001-06.jpg"
        )));
        productImages.put("P1002", new ArrayList<>(List.of(
                "/product-images/P1002-01.jpg",
                "/product-images/P1002-02.jpg",
                "/product-images/P1002-03.jpg",
                "/product-images/P1002-04.jpg",
                "/product-images/P1002-05.jpg",
                "/product-images/P1002-06.jpg",
                "/product-images/P1002-07.jpg",
                "/product-images/P1002-08.jpg"
        )));
        productImages.put("P1003", new ArrayList<>(List.of(
                "/product-images/P1003-01.jpg",
                "/product-images/P1003-02.jpg",
                "/product-images/P1003-03.jpg",
                "/product-images/P1003-04.jpg"
        )));
        productImages.put("P1004", new ArrayList<>(List.of(
                "/product-images/P1004-01.jpg",
                "/product-images/P1004-02.jpg",
                "/product-images/P1004-03.jpg",
                "/product-images/P1004-04.jpg",
                "/product-images/P1004-05.jpg",
                "/product-images/P1004-06.jpg"
        )));
        productImages.put("P1005", new ArrayList<>(List.of(
                "/product-images/P1005-01.jpg",
                "/product-images/P1005-02.jpg",
                "/product-images/P1005-03.jpg",
                "/product-images/P1005-04.jpg",
                "/product-images/P1005-05.jpg"
        )));
        productImages.put("P1006", new ArrayList<>(List.of(
                "/product-images/P1006-01.jpg",
                "/product-images/P1006-02.jpg",
                "/product-images/P1006-03.jpg",
                "/product-images/P1006-04.jpg",
                "/product-images/P1006-05.jpg",
                "/product-images/P1006-06.jpg"
        )));
        productImages.put("P1007", new ArrayList<>(List.of(
                "/product-images/P1007-01.jpg",
                "/product-images/P1007-02.jpg",
                "/product-images/P1007-03.jpg",
                "/product-images/P1007-04.jpg",
                "/product-images/P1007-05.jpg",
                "/product-images/P1007-06.jpg"
        )));
        productImages.put("P1008", new ArrayList<>(List.of(
                "/product-images/P1008-01.jpg",
                "/product-images/P1008-02.jpg",
                "/product-images/P1008-03.jpg",
                "/product-images/P1008-04.jpg",
                "/product-images/P1008-05.jpg"
        )));
        productImages.put("P1009", new ArrayList<>(List.of(
                "/product-images/P1009-01.jpg",
                "/product-images/P1009-02.jpg",
                "/product-images/P1009-03.jpg",
                "/product-images/P1009-04.jpg",
                "/product-images/P1009-05.jpg"
        )));
        productImages.put("P1010", new ArrayList<>(List.of(
                "/product-images/P1010-01.jpg",
                "/product-images/P1010-02.jpg",
                "/product-images/P1010-03.jpg",
                "/product-images/P1010-04.jpg",
                "/product-images/P1010-05.jpg",
                "/product-images/P1010-06.jpg",
                "/product-images/P1010-07.jpg"
        )));
        productImages.put("P1011", new ArrayList<>(List.of(
                "/product-images/P1011-01.jpg",
                "/product-images/P1011-02.jpg",
                "/product-images/P1011-03.jpg"
        )));
        productImages.put("P1012", new ArrayList<>(List.of(
                "/product-images/P1012-01.jpg",
                "/product-images/P1012-02.jpg",
                "/product-images/P1012-03.jpg",
                "/product-images/P1012-04.jpg",
                "/product-images/P1012-05.jpg"
        )));
        productImages.put("P1013", new ArrayList<>(List.of(
                "/product-images/P1013-01.jpg",
                "/product-images/P1013-02.jpg",
                "/product-images/P1013-03.jpg",
                "/product-images/P1013-04.jpg"
        )));
        productImages.put("P1014", new ArrayList<>(List.of(
                "/product-images/P1014-01.jpg",
                "/product-images/P1014-02.jpg",
                "/product-images/P1014-04.jpg",
                "/product-images/P1014-05.jpg",
                "/product-images/P1014-06.jpg"
        )));
        productImages.put("P1015", new ArrayList<>(List.of(
                "/product-images/P1015-01.jpg",
                "/product-images/P1015-02.jpg",
                "/product-images/P1015-03.jpg",
                "/product-images/P1015-04.jpg",
                "/product-images/P1015-05.jpg"
        )));
        productImages.put("P1016", new ArrayList<>(List.of(
                "/product-images/P1016-01.jpg"
        )));
        productImages.put("P1017", new ArrayList<>(List.of(
                "/product-images/P1017-01.jpg",
                "/product-images/P1017-02.jpg",
                "/product-images/P1017-03.jpg",
                "/product-images/P1017-04.jpg",
                "/product-images/P1017-05.jpg"
        )));
        productImages.put("P1018", new ArrayList<>(List.of(
                "/product-images/P1018-01.jpg",
                "/product-images/P1018-02.jpg",
                "/product-images/P1018-03.jpg",
                "/product-images/P1018-04.jpg"
        )));
        productImages.put("P1019", new ArrayList<>(List.of(
                "/product-images/P1019-01.jpg",
                "/product-images/P1019-02.jpg",
                "/product-images/P1019-03.jpg",
                "/product-images/P1019-04.jpg",
                "/product-images/P1019-05.jpg"
        )));
        productImages.put("P1020", new ArrayList<>(List.of(
                "/product-images/P1020-01.jpg",
                "/product-images/P1020-02.jpg",
                "/product-images/P1020-03.jpg",
                "/product-images/P1020-04.jpg",
                "/product-images/P1020-05.jpg"
        )));
        productImages.put("P1021", new ArrayList<>(List.of(
                "/product-images/P1021-01.jpg",
                "/product-images/P1021-02.jpg",
                "/product-images/P1021-03.jpg",
                "/product-images/P1021-04.jpg",
                "/product-images/P1021-05.jpg"
        )));
        productImages.put("P1022", new ArrayList<>(List.of(
                "/product-images/P1022-01.jpg",
                "/product-images/P1022-02.jpg",
                "/product-images/P1022-03.jpg",
                "/product-images/P1022-04.jpg",
                "/product-images/P1022-05.jpg"
        )));
        productImages.put("P1023", new ArrayList<>(List.of(
                "/product-images/P1023-01.jpg",
                "/product-images/P1023-02.jpg",
                "/product-images/P1023-03.jpg",
                "/product-images/P1023-04.jpg",
                "/product-images/P1023-05.jpg"
        )));
        productImages.put("P1024", new ArrayList<>(List.of(
                "/product-images/P1024-01.jpg"
        )));
        productImages.put("P1025", new ArrayList<>(List.of(
                "/product-images/P1025-01.jpg",
                "/product-images/P1025-02.jpg",
                "/product-images/P1025-03.jpg",
                "/product-images/P1025-04.jpg",
                "/product-images/P1025-05.jpg"
        )));
        productImages.put("P1026", new ArrayList<>(List.of(
                "/product-images/P1026-01.jpg",
                "/product-images/P1026-02.jpg",
                "/product-images/P1026-03.jpg",
                "/product-images/P1026-04.jpg"
        )));
        productImages.put("P1027", new ArrayList<>(List.of(
                "/product-images/P1027-01.jpg",
                "/product-images/P1027-02.jpg",
                "/product-images/P1027-03.jpg",
                "/product-images/P1027-04.jpg",
                "/product-images/P1027-05.jpg"
        )));
        productImages.put("P1028", new ArrayList<>(List.of(
                "/product-images/P1028-01.jpg",
                "/product-images/P1028-02.jpg",
                "/product-images/P1028-03.jpg",
                "/product-images/P1028-04.jpg",
                "/product-images/P1028-05.jpg"
        )));
        productImages.put("P1029", new ArrayList<>(List.of(
                "/product-images/P1029-01.jpg",
                "/product-images/P1029-02.jpg",
                "/product-images/P1029-03.jpg",
                "/product-images/P1029-04.jpg"
        )));
        productImages.put("P1030", new ArrayList<>(List.of(
                "/product-images/P1030-01.jpg",
                "/product-images/P1030-02.jpg",
                "/product-images/P1030-03.jpg",
                "/product-images/P1030-04.jpg",
                "/product-images/P1030-05.jpg"
        )));
    }

    public static Map<String, List<String>> getProductImages() {
        return Collections.unmodifiableMap(productImages);
    }

    public static String getPrimaryImage(String productCode) {
        List<String> images = productImages.get(productCode);
        return (images != null && !images.isEmpty()) ? images.get(0) : null;
    }
}
