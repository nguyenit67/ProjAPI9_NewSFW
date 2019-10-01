package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

  public static final String CONTENT_MSG = "com.example.test.content_url";
  final String FAIL = "FUCKED UP!!! :<<<";

  ListView listview;
  ArrayList<Noti> arraynoti = new ArrayList<Noti>();
  NotiAdapter adapter = new NotiAdapter(this, R.layout.item, arraynoti);
  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder;
  RequestQueue reqq;
  NodeList nodelist;
  Element item;
  org.jsoup.nodes.Document htm;
  org.w3c.dom.Document doc;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.list_activity);

      listview = (ListView) this.findViewById(R.id.notilist);

      reqq = Volley.newRequestQueue(this);

      getXhtml("https://tuoitre.vn/rss.htm", new Caller() {
          @Override
          public void onSuccess(String xhtml) {
              //alert("Hell yeah!!!!!");
              htm = Jsoup.parse(xhtml);
              for ( org.jsoup.nodes.
                      Element topic: htm.select("a[href$=.rss]")) {
                  String link = "https://tuoitre.vn" + topic.attr("href");
                  extractArticle(link);
                  alert(link);
                  break;
              }
              //alert("End for all!!!!!!");
          }
      });

      listview.setAdapter(adapter);

      listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Intent intent = new Intent(MainActivity.this, ShowContentActivity.class );
              intent.putExtra(CONTENT_MSG,((Noti)adapterView.getItemAtPosition(i)).getContent_url() );

              startActivity(intent);
              /**
              getXhtml (
                      /**((Noti)adapterView.getItemAtPosition(i)).getContent_url(),
                new Caller() {
                  @Override
                  public void onSuccess(String xhtml) {

                      htm = Jsoup.parse(xhtml);
                      String content =
                        "<html id=\"mainHtml\" lang=\"vi\" xmlns=\"http://www.w3.org/1999/xhtml\" itemtype=\"http://schema.org/NewsArticle\" class=\"windows desktop landscape\">"
                       +  "<body>"
                       +     htm.select("div.main-content-body").toString()
                       +  "</body>"
                       +"</html>";


                  }
              });*/
          }
      });
  }

  void extractArticle(String link) {
      getXhtml(link, new Caller() {
          @Override
          public void onSuccess(String xhtml) {
              try {
                  builder = dbFactory.newDocumentBuilder();
                  doc = builder.parse(toInSM(xhtml));
                  doc.getDocumentElement().normalize();
                  nodelist = doc.getElementsByTagName("item");
                  //alert(xhtml);
                  for (int i = 0; i < nodelist.getLength(); ++i) {
                      //if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                      item = (Element) nodelist.item(i);
                      htm = Jsoup.parse(gettext(item, "description"));
                      arraynoti.add(
                              new Noti( htm.select("img").attr("src"),
                                      gettext(item, "title"),
                                      htm.text(),
                                      gettext(item, "pubDate"),
                                      gettext(item, "link"))
                      );
                      //alert(gettext(item, "link"));
                      adapter.notifyDataSetChanged();
                  }
                  //alert("End For It(em)!!!");
              } catch (Exception e) {}
          }
      });
  }

  void alert(String msg) {
      Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }

  String gettext(Element item, String tag) throws Exception {
      return ((Element) item.getElementsByTagName(tag).item(0)).getTextContent();
  }

  InputStream toInSM(String s) {
      try {
          return new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
      } catch (Exception e) {}
      return null;
  }

  public interface Caller {
      void onSuccess(String xhtml);
  }

  void getXhtml(String link, final Caller callback) {
      reqq.add(new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {

              @Override
              public void onResponse(String response) {
                  callback.onSuccess(response);
              }
          }, new Response.ErrorListener(){@Override public void onErrorResponse(VolleyError error) {}}));
  }
}
