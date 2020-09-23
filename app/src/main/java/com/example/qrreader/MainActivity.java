package com.example.qrreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.qrreader.Adapter.CertBlockAdapter;
import com.example.qrreader.Models.CertRes;
import com.example.qrreader.Parser.LinkParser;
import com.example.qrreader.Parser.CertificateGrabber;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    public QRCodeReaderView qrCodeReaderView;
    ArrayList<String> links= new ArrayList<String>();
    ArrayList<CertRes> certificates = new ArrayList<CertRes>();
    private Boolean readingStatus = false;
    private Button validate;
    private TextView linksResult;
    private RecyclerView certResults;

    Timer timer;
    TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validate = (Button) findViewById(R.id.button_validate);
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permissionStatus== PackageManager.PERMISSION_GRANTED)
        {
            qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
            qrCodeReaderView.setOnQRCodeReadListener(this);

            // Use this function to enable/disable decoding
            qrCodeReaderView.setQRDecodingEnabled(false);

            // Use this function to change the autofocus interval (default is 5 secs)
            qrCodeReaderView.setAutofocusInterval(2000L);

            // Use this function to enable/disable Torch
            qrCodeReaderView.setTorchEnabled(false);

            // Use this function to set back camera preview
            qrCodeReaderView.setBackCamera();

        }else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
            }

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points)
    {
        readingStatus = true;
        Toast toast = Toast.makeText(getApplicationContext(),
                "QR Succsessfully readed", Toast.LENGTH_SHORT);
        toast.show();

        links = LinkParser.getLinks(text);

        if(links.size()>0) {
            validate.setEnabled(true);
            TextView result = findViewById(R.id.links_result);
            result.setText("");
            for(String r: links){
                result.append(r+"\n");
            }
            readingStatus=false;

        }
        else
            {
                toast = Toast.makeText(getApplicationContext(),
                        "QR Succsessfully readed, but there is no viable links", Toast.LENGTH_SHORT);
                toast.show();
            }
    }

    public void onReadClick(View view) throws Exception {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Trying to read QR", Toast.LENGTH_SHORT);
        toast.show();
        readingStatus=false;

        qrCodeReaderView.setQRDecodingEnabled(true);

        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask, 5000);
    }

    public void onValidateClick(View view)
    {
        ArrayList<CertificateGrabber> calcs = new ArrayList<CertificateGrabber>();
        for(String link: links)
        {
            if(!link.startsWith("http://"))
            calcs.add(new CertificateGrabber());
            calcs.get(calcs.size() - 1).execute(link);
        }

        if(calcs.size()>0){
            for(CertificateGrabber res: calcs)
                {
                    try {
                        certificates.add(res.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    certResults = findViewById(R.id.certificate_results);
                    CertBlockAdapter cla = new CertBlockAdapter( certificates, this);
                    certResults.setAdapter(cla);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    certResults.setLayoutManager(layoutManager);
                }
            }   else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You have only http:\\ links", Toast.LENGTH_SHORT);
                    toast.show();
                }
        }


    class MyTimerTask extends TimerTask {
        @Override
        public void run() {


            runOnUiThread(new Runnable(){

                // Отображаем информацию в текстовом поле count:
                @Override
                public void run() {
                   if(!readingStatus)
                   {
                       Toast toast = Toast.makeText(getApplicationContext(),
                               "Failed to read QR", Toast.LENGTH_SHORT);
                       toast.show();

                   }
                    qrCodeReaderView.setQRDecodingEnabled(false);
                }});
        }
    }
}


