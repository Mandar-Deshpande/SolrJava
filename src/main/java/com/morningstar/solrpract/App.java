package com.morningstar.solrpract;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Hello world!
 *
 */

public class App {
	Scanner sc;
	String ID;
	String[] uq;
	private App() {
		sc = new Scanner(System.in);
		ID = "";
	}

	public static void main(String[] args) {
		App a = new App();
		System.out.println(
				"Choose option 1.Insert to Solr \n 2.Add New Record \n 3.Update In Solr \n 5.Delete all Records from Solr");
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i) {
		case 1:
			a.instertAllSolrData();
			break;
		case 2:
			a.AddNewRecord();
			break;
		case 3:
			a.deleteAllSolrData();
			break;
		case 4:
			a.print_field_names();
			break;
		default:
			break;
		}
		sc.close();
	}

	private void deleteAllSolrData() {
		SolrClient server = new HttpSolrClient("http://localhost:8983/solr/Booktemp");
		try {
			server.deleteByQuery("*:*");
			server.commit();
		} catch (SolrServerException e) {
			throw new RuntimeException("Failed to delete data in Solr. " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to delete data in Solr. " + e.getMessage(), e);
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void instertAllSolrData() {
		JSONParser parser = new JSONParser();

		try {
			SolrClient server = new HttpSolrClient("http://localhost:8983/solr/Booktemp");

			int i = 0;
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

				long sequence_i = (Long) jsonObject.get("sequence_i");
				System.out.println(sequence_i);

				String series_t = (String) jsonObject.get("series_t");
				System.out.println(series_t);

				double price = (double) jsonObject.get("price");
				System.out.println(price);

				long pages_i = (long) jsonObject.get("pages_i");
				System.out.println(pages_i);

				JSONArray msg = (JSONArray) jsonObject.get("cat");
				ArrayList<String> as = new ArrayList<String>();
				Iterator<String> iterator = msg.iterator();
				while (iterator.hasNext()) {
					String temp = iterator.next();
					System.out.println(temp);
					as.add(temp);
				}

				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", id);
				doc.addField("name", name);
				doc.addField("author", author);
				doc.addField("sequence_i", sequence_i);
				doc.addField("series_t", series_t);
				doc.addField("price", price);
				doc.addField("pages_i", pages_i);
				doc.addField("cat", as);
				server.add(doc);
				server.commit();
			}
			server.close();
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
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

	private String fieldNames() {
		String Arr = "";
		SolrClient solrServer = new HttpSolrClient("http://localhost:8983/solr/Bookex");
		SolrQuery qry = new SolrQuery("*:*");
		String solrID = "";
		qry.setIncludeScore(true);
		qry.setShowDebugInfo(true);
		qry.setRows(100);
		QueryRequest qryReq = new QueryRequest(qry);
		QueryResponse resp;
		try {
			resp = qryReq.process(solrServer);

			SolrDocumentList results = resp.getResults();
			for (int i = 0; i < results.size(); i++) {
				SolrDocument hitDoc = results.get(i);
				solrID = hitDoc.getFieldValue("id").toString();
				for (Iterator<Entry<String, Object>> flditer = hitDoc.iterator(); flditer.hasNext();) {
					Entry<String, Object> entry = flditer.next();
					Arr += entry.getKey() + " || ";
					System.out.println(Arr);
					if (entry.getKey() == "id") {
						ID += entry.getValue() + " || ";
					}
				}
			}
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Arr;
	}

	public void print_field_names() {
		String s = fieldNames();
		System.out.println("This not " + s);
		String[] arr = s.split(" || ");
		Set<String> temp = new HashSet<String>(Arrays.asList(arr));
		uq = temp.toArray(new String[temp.size()]);
		for (int i = 0; i < uq.length; i++) {
			System.out.println(uq[i]);
		}
	}

	public void AddNewRecord() {
		JSONObject json = new JSONObject();
		
		json.put("title", "Harry Potter and Half Blood Prince");
		json.put("author", "J. K. Rolling");
		json.put("price", 20);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add("Harry");
		jsonArray.add("Ron");
		jsonArray.add("Hermoinee");
		json.put("characters", jsonArray);
		try {
			System.out.println("Writting JSON into file ...");
			System.out.println(json);
			FileWriter jsonFileWriter = new FileWriter("C:\\myjson\\books.json");
			jsonFileWriter.append(json.toJSONString());
			jsonFileWriter.flush();
			jsonFileWriter.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
