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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

public class NewExample {
	Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		NewExample a = new NewExample();
		System.out.println("Choose option 1. Insert To JSON file \n 2.Insert to Solr \n 3.Delete from Solr");
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		switch (i) {
		case 1:
			a.instertAllSolrData();
			break;
		case 2:
			a.deleteAllSolrData();
			break;
		case 3:
			a.createJson();
			break;
		default:
			break;
		}
	}

	private void deleteAllSolrData() {
		SolrClient server = new HttpSolrClient("http://localhost:8983/solr/Booktemp");
		try {
			server.deleteByQuery("author:Jostein Gaarder");
			server.commit();
			// server.close();
		} catch (SolrServerException e) {
			throw new RuntimeException("Failed to delete data in Solr. " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to delete data in Solr. " + e.getMessage(), e);
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
				i++;
				if (i % 100 == 0)
					server.commit();
			}
			server.commit();
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

	public void createJson() {
		SolrClient solrServer = new HttpSolrClient("http://localhost:8983/solr/Booktemp");
		SolrQuery qry = new SolrQuery("*:*");
		String solrID="";
		qry.setIncludeScore(true);
		qry.setShowDebugInfo(true);
		qry.setRows(100);
		QueryRequest qryReq = new QueryRequest(qry);
		QueryResponse resp;
		System.out.println("Enter Field Name");
		String field=sc.nextLine();
		try {
			resp = qryReq.process(solrServer);

			SolrDocumentList results = resp.getResults();
			System.out.println(results.getNumFound() + " total hits");
			int count = results.size();
			System.out.println(count + " received hits");
			System.out.println("Enter value to change of field "+field +" ");
			String temp="["+sc.nextLine()+"]";
			for (int i = 0; i < count; i++) {
				SolrDocument hitDoc = results.get(i);
				String temp2= hitDoc.getFieldValue(field).toString();
				//System.out.println(temp2);
				if(temp2.equalsIgnoreCase(temp)) {
					//System.out.println(hitDoc.getFieldValue("id"));
					solrID=hitDoc.getFieldValue("id").toString();
				for (Iterator<Entry<String, Object>> flditer = hitDoc.iterator(); flditer.hasNext();) {
					Entry<String, Object> entry = flditer.next();
					System.out.println(entry.getKey() + ": " + entry.getValue());
					
				}
				}				
			}
			update(solrID,field);
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void update( String solrID,String Field) throws SolrServerException, IOException
	{
		SolrClient cln = new HttpSolrClient("http://localhost:8983/solr/Bookex");
		System.out.println("Enter New Value for field "+Field+" : ");
		String ob=sc.nextLine();
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", solrID);
		Map<String, String> operation = new HashMap<String,String>();
		operation.put("set", ob);
		document.addField(Field, operation);
		cln.add(document);
		cln.commit();
		cln.close();
		
		
	} 

}
