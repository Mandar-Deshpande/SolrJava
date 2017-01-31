package com.morningstar.solrpract;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Practice_JSON {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {

			JSONArray a = (JSONArray) parser.parse(new FileReader("C:\\myjson\\books.json"));

			for (Object obj : a) {
				JSONObject jsonObject = (JSONObject) obj;
				System.out.println(jsonObject);

				String id = (String) jsonObject.get("id");
				System.out.println(id);

				String name = (String) jsonObject.get("name");
				System.out.println(name);

				String author = (String) jsonObject.get("author");
				System.out.println(author);

				String series_t = (String) jsonObject.get("series_t");
				System.out.println(series_t);

				String price = (String) jsonObject.get("price");
				System.out.println(price);

				long pages_i = (Long) jsonObject.get("pages_i");
				System.out.println(pages_i);
			}
			/*
			 * // loop array JSONArray msg = (JSONArray) jsonObject.get("cat");
			 * Iterator<String> iterator = msg.iterator(); while
			 * (iterator.hasNext()) { System.out.println(iterator.next()); }
			 */

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
