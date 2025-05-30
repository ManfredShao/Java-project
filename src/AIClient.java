//import okhttp3.*;
//import org.json.*;
//
//import java.io.IOException;
//
//public class AIClient {
//    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions"; // 假设地址
//    private static final String API_KEY = "你的API密钥";
//
//    public static String chatWithAI(String userInput) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject message = new JSONObject()
//                .put("role", "user")
//                .put("content", userInput);
//
//        JSONObject payload = new JSONObject()
//                .put("model", "deepseek-chat") // 具体模型名视接口而定
//                .put("messages", new JSONArray().put(message));
//
//        RequestBody body = RequestBody.create(
//                payload.toString(),
//                MediaType.parse("application/json")
//        );
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            String responseBody = response.body().string();
//            JSONObject json = new JSONObject(responseBody);
//            String reply = json.getJSONArray("choices")
//                               .getJSONObject(0)
//                               .getJSONObject("message")
//                               .getString("content");
//            return reply.trim();
//        }
//    }
//}
