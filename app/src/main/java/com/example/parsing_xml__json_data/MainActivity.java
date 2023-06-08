package com.example.parsing_xml__json_data;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    Button btn_xml,btn_json;
    TextView xmltxt,jsontxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_xml=findViewById(R.id.btnxml);
        btn_json=findViewById(R.id.btnjson);

        xmltxt=findViewById(R.id.xmltxt);
        jsontxt=findViewById(R.id.jsontxt);

        btn_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    InputStream is=getAssets().open("parse.xml");
                    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                    DocumentBuilder db=dbf.newDocumentBuilder();
                    Document d=db.parse(is);
                    StringBuilder s=new StringBuilder();
                    s.append("XML DATA");
                    s.append("\n --------");
                    NodeList nodeList = d.getElementsByTagName("location");
                    int i;
                    for (i=0; i<nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if(node.getNodeType() == Node.ELEMENT_NODE){
                            Element element=(Element)node;
                            s.append("\n Place:").append(getValue("place",element));
                            StringBuilder latitude = s.append("\n Latitude:").append(getValue("latitude",element));
                            final StringBuilder longitude = s.append("\n Longitude:").append(getValue("longitude",element));
                            s.append("\n Temperature:").append(getValue("temperature",element));
                            s.append("\n Humidity:").append(getValue("humidity",element));

                        }
                    }
                    xmltxt.setText(s.toString());
                } catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Error Parsing XML", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btn_json.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String str=null;
                try{
                    InputStream is=getAssets().open("parse.json");
                    int size = is.available();
                    byte buffer[]= new byte[size];
                    is.read(buffer);
                    str=new String(buffer);
                    JSONObject j=new JSONObject(str);
                    JSONObject e=j.getJSONObject("location");
                    jsontxt.setText("JSON DATA\n-------");
                    jsontxt.append("\n Place"+e.getString("place"));
                    jsontxt.append("\n Latitude"+e.getString("latitude"));
                    jsontxt.append("\n Longitude"+e.getString("longitude"));
                    jsontxt.append("\n Temperature"+e.getString("temperature"));
                    jsontxt.append("\n Humidity"+e.getString("humidity"));
                } catch (IOException | JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }private String getValue(String tag, Element element){
        return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
    }
}
