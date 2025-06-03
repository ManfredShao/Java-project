package controller;

import okhttp3.*;
import org.json.*;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AIClient {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static volatile Call currentCall; // 当前网络请求（支持取消）

    // 配置HTTP客户端（包含超时设置）
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)  // 连接超时
            .readTimeout(120, TimeUnit.SECONDS)    // 读取超时
            .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时
            .callTimeout(180, TimeUnit.SECONDS)    // 总超时
            .build();

    /**
     * 异步调用AI接口
     *
     * @param question  用户问题
     * @param onSuccess 成功回调
     * @param onError   失败回调
     */
    public static void chatWithAIAsync(String question, Consumer<String> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                // 1. 构建请求体
                JSONObject message = new JSONObject().put("role", "user").put("content", question);

                JSONObject requestBody = new JSONObject().put("model", "deepseek-chat").put("messages", new JSONArray().put(message)).put("temperature", 0.7).put("max_tokens", 1000);

                RequestBody body = RequestBody.create(requestBody.toString(), MediaType.get("application/json"));

                // 2. 构建请求
                Request request = new Request.Builder().url(API_URL).addHeader("Authorization", "Bearer " + getApiKey()).post(body).build();

                // 3. 执行请求（记录当前Call以便取消）
                currentCall = client.newCall(request);
                try (Response response = currentCall.execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("HTTP错误: " + response.code() + " - " + response.message());
                    }

                    // 4. 解析响应
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);

                    if (!json.has("choices")) {
                        throw new IOException("无效的API响应格式");
                    }

                    String answer = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();

                    // 5. 回调到UI线程
                    SwingUtilities.invokeLater(() -> onSuccess.accept(answer));
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e));
            } finally {
                currentCall = null; // 清除当前请求
            }
        }).start();
    }

    /**
     * 取消当前正在进行的AI请求
     */
    public static void cancelCurrentRequest() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    /**
     * 从配置获取API密钥（安全实践）
     */
    private static String getApiKey() {
        // 实际项目中应从安全配置读取
        return "///";
    }

    /**
     * 检查网络可用性（可选）
     */
    public static boolean isNetworkAvailable() {
        try {
            Request request = new Request.Builder().url("https://www.baidu.com").head().build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            return false;
        }
    }
}