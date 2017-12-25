package de.crazymonkey.finanzinformation.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RequestUtils {

	public static String get(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpEntity entity = null;
		HttpGet httpGet = new HttpGet(url);
		String text = null;
		try {
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			entity = response1.getEntity();

			try (Scanner scanner = new Scanner(entity.getContent(), StandardCharsets.UTF_8.name())) {
				text = scanner.useDelimiter("\\A").next();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
