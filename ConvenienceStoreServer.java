import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class ConvenienceStoreServer {
    private static final int PORT = 8081;
    private static final String HTML_FILE = "convin.html";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/stores", new StoreDataHandler());
        server.createContext("/api/stores/", new StoreFilterHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("서버가 포트 " + PORT + "에서 시작되었습니다.");
        System.out.println("브라우저에서 http://localhost:" + PORT + " 를 열어보세요.");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            if (requestPath.equals("/") || requestPath.equals("/index.html")) {
                requestPath = "/" + HTML_FILE;
            }

            try {
                String filePath = "." + requestPath;
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    String contentType = getContentType(filePath);
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    exchange.sendResponseHeaders(200, file.length());
                    try (FileInputStream fis = new FileInputStream(file);
                         OutputStream os = exchange.getResponseBody()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    String response = "404 Not Found";
                    exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                    byte[] bytes = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(404, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                }
            } catch (Exception e) {
                String response = "500 Internal Server Error";
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                byte[] bytes = response.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        }

        private String getContentType(String filePath) {
            if (filePath.endsWith(".html")) return "text/html; charset=UTF-8";
            if (filePath.endsWith(".css")) return "text/css; charset=UTF-8";
            if (filePath.endsWith(".js")) return "application/javascript; charset=UTF-8";
            if (filePath.endsWith(".json")) return "application/json; charset=UTF-8";
            return "text/plain; charset=UTF-8";
        }
    }

    static class StoreDataHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Map<String, Object>> stores = getConvenienceStores();
                String jsonResponse = convertToJson(stores);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                byte[] responseBytes = jsonResponse.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class StoreFilterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                String brand = path.substring(path.lastIndexOf("/") + 1);
                List<Map<String, Object>> filteredStores = getStoresByBrand(brand);
                String jsonResponse = convertToJson(filteredStores);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                byte[] responseBytes = jsonResponse.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private static List<Map<String, Object>> getConvenienceStores() {
        List<Map<String, Object>> stores = new ArrayList<>();

        Map<String, Object> store1 = new HashMap<>();
        store1.put("name", "GS25 이촌역점");
        store1.put("brand", "GS25");
        store1.put("address", "서울특별시 용산구 이촌동 306-2");
        store1.put("lat", 37.5194);
        store1.put("lng", 126.9750);
        store1.put("phone", "02-749-1234");
        store1.put("hours", "24시간");
        store1.put("description", "이촌역 근처 GS25 편의점");
        stores.add(store1);

        Map<String, Object> store2 = new HashMap<>();
        store2.put("name", "GS25 한강점");
        store2.put("brand", "GS25");
        store2.put("address", "서울특별시 용산구 이촌동 302-5");
        store2.put("lat", 37.5178);
        store2.put("lng", 126.9762);
        store2.put("phone", "02-749-3456");
        store2.put("hours", "24시간");
        store2.put("description", "한강 근처 GS25 편의점");
        stores.add(store2);

        Map<String, Object> store3 = new HashMap<>();
        store3.put("name", "GS25 용산역점");
        store3.put("brand", "GS25");
        store3.put("address", "서울특별시 용산구 한강대로 23길 55");
        store3.put("lat", 37.5298);
        store3.put("lng", 126.9648);
        store3.put("phone", "02-749-1111");
        store3.put("hours", "24시간");
        store3.put("description", "용산역 근처 GS25 편의점");
        stores.add(store3);

        Map<String, Object> store4 = new HashMap<>();
        store4.put("name", "GS25 삼각지점");
        store4.put("brand", "GS25");
        store4.put("address", "서울특별시 용산구 한강대로 23길 55");
        store4.put("lat", 37.5347);
        store4.put("lng", 126.9727);
        store4.put("phone", "02-749-2222");
        store4.put("hours", "24시간");
        store4.put("description", "삼각지역 근처 GS25 편의점");
        stores.add(store4);

        Map<String, Object> store5 = new HashMap<>();
        store5.put("name", "GS25 남영점");
        store5.put("brand", "GS25");
        store5.put("address", "서울특별시 용산구 남영동 1-1");
        store5.put("lat", 37.5450);
        store5.put("lng", 126.9720);
        store5.put("phone", "02-749-3333");
        store5.put("hours", "24시간");
        store5.put("description", "남영동 GS25 편의점");
        stores.add(store5);

        Map<String, Object> store6 = new HashMap<>();
        store6.put("name", "CU 이촌점");
        store6.put("brand", "CU");
        store6.put("address", "서울특별시 용산구 이촌동 305-1");
        store6.put("lat", 37.5185);
        store6.put("lng", 126.9748);
        store6.put("phone", "02-749-5678");
        store6.put("hours", "24시간");
        store6.put("description", "이촌동 CU 편의점");
        stores.add(store6);

        Map<String, Object> store7 = new HashMap<>();
        store7.put("name", "CU 한강공원점");
        store7.put("brand", "CU");
        store7.put("address", "서울특별시 용산구 이촌동 301-8");
        store7.put("lat", 37.5172);
        store7.put("lng", 126.9768);
        store7.put("phone", "02-749-7890");
        store7.put("hours", "24시간");
        store7.put("description", "한강공원 근처 CU 편의점");
        stores.add(store7);

        Map<String, Object> store8 = new HashMap<>();
        store8.put("name", "CU 용산점");
        store8.put("brand", "CU");
        store8.put("address", "서울특별시 용산구 용산동 2가 1-1");
        store8.put("lat", 37.5310);
        store8.put("lng", 126.9650);
        store8.put("phone", "02-749-4444");
        store8.put("hours", "24시간");
        store8.put("description", "용산동 CU 편의점");
        stores.add(store8);

        Map<String, Object> store9 = new HashMap<>();
        store9.put("name", "CU 한남점");
        store9.put("brand", "CU");
        store9.put("address", "서울특별시 용산구 한남동 1-1");
        store9.put("lat", 37.5310);
        store9.put("lng", 127.0080);
        store9.put("phone", "02-749-5555");
        store9.put("hours", "24시간");
        store9.put("description", "한남동 CU 편의점");
        stores.add(store9);

        Map<String, Object> store10 = new HashMap<>();
        store10.put("name", "CU 서빙고점");
        store10.put("brand", "CU");
        store10.put("address", "서울특별시 용산구 서빙고동 1-1");
        store10.put("lat", 37.5190);
        store10.put("lng", 126.9880);
        store10.put("phone", "02-749-6666");
        store10.put("hours", "24시간");
        store10.put("description", "서빙고동 CU 편의점");
        stores.add(store10);

        Map<String, Object> store11 = new HashMap<>();
        store11.put("name", "세븐일레븐 이촌동점");
        store11.put("brand", "세븐일레븐");
        store11.put("address", "서울특별시 용산구 이촌동 307-3");
        store11.put("lat", 37.5201);
        store11.put("lng", 126.9755);
        store11.put("phone", "02-749-9012");
        store11.put("hours", "24시간");
        store11.put("description", "이촌동 세븐일레븐 편의점");
        stores.add(store11);

        Map<String, Object> store12 = new HashMap<>();
        store12.put("name", "세븐일레븐 용산점");
        store12.put("brand", "세븐일레븐");
        store12.put("address", "서울특별시 용산구 이촌동 308-2");
        store12.put("lat", 37.5210);
        store12.put("lng", 126.9745);
        store12.put("phone", "02-749-2468");
        store12.put("hours", "24시간");
        store12.put("description", "용산구 세븐일레븐 편의점");
        stores.add(store12);

        Map<String, Object> store13 = new HashMap<>();
        store13.put("name", "세븐일레븐 효창점");
        store13.put("brand", "세븐일레븐");
        store13.put("address", "서울특별시 용산구 효창동 1-1");
        store13.put("lat", 37.5400);
        store13.put("lng", 126.9620);
        store13.put("phone", "02-749-7777");
        store13.put("hours", "24시간");
        store13.put("description", "효창동 세븐일레븐 편의점");
        stores.add(store13);

        Map<String, Object> store14 = new HashMap<>();
        store14.put("name", "세븐일레븐 원효점");
        store14.put("brand", "세븐일레븐");
        store14.put("address", "서울특별시 용산구 원효로 1가 1-1");
        store14.put("lat", 37.5380);
        store14.put("lng", 126.9700);
        store14.put("phone", "02-749-8888");
        store14.put("hours", "24시간");
        store14.put("description", "원효로 세븐일레븐 편의점");
        stores.add(store14);

        Map<String, Object> store15 = new HashMap<>();
        store15.put("name", "이마트24 용산점");
        store15.put("brand", "이마트24");
        store15.put("address", "서울특별시 용산구 용산동 2가 1-1");
        store15.put("lat", 37.5320);
        store15.put("lng", 126.9660);
        store15.put("phone", "02-749-9999");
        store15.put("hours", "24시간");
        store15.put("description", "용산동 이마트24 편의점");
        stores.add(store15);

        Map<String, Object> store16 = new HashMap<>();
        store16.put("name", "이마트24 이촌점");
        store16.put("brand", "이마트24");
        store16.put("address", "서울특별시 용산구 이촌동 1-1");
        store16.put("lat", 37.5200);
        store16.put("lng", 126.9760);
        store16.put("phone", "02-749-0000");
        store16.put("hours", "24시간");
        store16.put("description", "이촌동 이마트24 편의점");
        stores.add(store16);

        Map<String, Object> store17 = new HashMap<>();
        store17.put("name", "미니스톱 용산점");
        store17.put("brand", "미니스톱");
        store17.put("address", "서울특별시 용산구 한강대로 405");
        store17.put("lat", 37.5250);
        store17.put("lng", 126.9700);
        store17.put("phone", "02-777-8888");
        store17.put("hours", "24시간");
        store17.put("description", "한강대로 미니스톱 편의점");
        stores.add(store17);

        Map<String, Object> store18 = new HashMap<>();
        store18.put("name", "미니스톱 삼각지점");
        store18.put("brand", "미니스톱");
        store18.put("address", "서울특별시 용산구 한강대로 23길 55");
        store18.put("lat", 37.5350);
        store18.put("lng", 126.9730);
        store18.put("phone", "02-777-9999");
        store18.put("hours", "24시간");
        store18.put("description", "삼각지역 미니스톱 편의점");
        stores.add(store18);

        return stores;
    }

    private static List<Map<String, Object>> getStoresByBrand(String brand) {
        List<Map<String, Object>> allStores = getConvenienceStores();
        List<Map<String, Object>> filteredStores = new ArrayList<>();
        for (Map<String, Object> store : allStores) {
            if (store.get("brand").equals(brand)) {
                filteredStores.add(store);
            }
        }
        return filteredStores;
    }

    private static String convertToJson(List<Map<String, Object>> stores) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < stores.size(); i++) {
            Map<String, Object> store = stores.get(i);
            json.append("{");
            json.append("\"name\":\"").append(store.get("name")).append("\",");
            json.append("\"brand\":\"").append(store.get("brand")).append("\",");
            json.append("\"address\":\"").append(store.get("address")).append("\",");
            json.append("\"lat\":").append(store.get("lat")).append(",");
            json.append("\"lng\":").append(store.get("lng")).append(",");
            json.append("\"phone\":\"").append(store.get("phone")).append("\",");
            json.append("\"hours\":\"").append(store.get("hours")).append("\",");
            json.append("\"description\":\"").append(store.get("description")).append("\"");
            json.append("}");
            if (i < stores.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}

