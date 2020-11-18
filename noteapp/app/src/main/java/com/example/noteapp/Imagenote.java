package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.URLConnection.guessContentTypeFromName;

public class Imagenote extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 123;
    public static final int REQUEST_CODE_PHOTO = 111;
    int index=1;

    EditText imagetitle;
    ListView imagelistview;
    LinearLayout linearLayout;

    public String filerealpath;
    ArrayAdapter<Bitmap> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenote);
        init();

    }
    public void init(){
        imagetitle=(EditText) findViewById(R.id.imageTitle);
        linearLayout=(LinearLayout) findViewById(R.id.linnear);
    }


    //tao menu toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuimagenote,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //event toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent((Intent.ACTION_PICK));
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_PHOTO);
                return true;
            case R.id.menu2:
                MultipartUtility utility;
                try {
                    utility=new MultipartUtility(new URL("http://192.168.1.101:58938/api/note"));
                    utility.addFormField("title",imagetitle.getText().toString());
                    utility.addFilePart("f1",new File(filerealpath));
                    byte[] response=utility.finish();
                    Log.e("kq", " "+response+"" );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    //tra ve bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //lấy ảnh từ camera
           // image.setImageBitmap(bitmap);
        }
        else if(requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            filerealpath=getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
               // image.setImageBitmap(bitmap);
                ImageView item=new ImageView(this);
                item.setImageBitmap(bitmap);
                linearLayout.addView(item,index);
                index++;
                File file=new File(filerealpath);
                String filepath=file.getAbsolutePath();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    // get real file path
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public class HttpPost extends AsyncTask<String,Void,String>{
        private static final String CRLF = "\r\n";
        private static final String CHARSET = "UTF-8";

        private static final int CONNECT_TIMEOUT = 15000;
        private static final int READ_TIMEOUT = 10000;

        private final HttpURLConnection connection;
        private final OutputStream outputStream;
        private final PrintWriter writer;
        private final String boundary;

        // for log formatting only
        private final URL urllog;
        private final long start;
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url=new URL(strings[0]);
                start = currentTimeMillis();
                boundary = "---------------------------" + currentTimeMillis();

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Charset", CHARSET);
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                outputStream = connection.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET),
                        true);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
*/

    //postdata
    public static class MultipartUtility {

        private static final Logger log = Logger.getLogger(MultipartUtility.class
                .getSimpleName());

        private static final String CRLF = "\r\n";
        private static final String CHARSET = "UTF-8";

        private static final int CONNECT_TIMEOUT = 15000;
        private static final int READ_TIMEOUT = 10000;

        private final HttpURLConnection connection;
        private final OutputStream outputStream;
        private final PrintWriter writer;
        private final String boundary;

        // for log formatting only
        private final URL url;
        private final long start;

        public MultipartUtility(final URL url) throws IOException {
            start = currentTimeMillis();
            this.url = url;

            boundary = "---------------------------" + currentTimeMillis();

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET),
                    true);
        }

        public void addFormField(final String name, final String value) {
            writer.append("--").append(boundary).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"").append(name)
                    .append("\"").append(CRLF)
                    .append("Content-Type: text/plain; charset=").append(CHARSET)
                    .append(CRLF).append(CRLF).append(value).append(CRLF);
        }

        public void addFilePart(final String fieldName, final File uploadFile)
                throws IOException {
            final String fileName = uploadFile.getName();
            writer.append("--").append(boundary).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"")
                    .append(fieldName).append("\"; filename=\"").append(fileName)
                    .append("\"").append(CRLF).append("Content-Type: ")
                    .append(guessContentTypeFromName(fileName)).append(CRLF)
                    .append("Content-Transfer-Encoding: binary").append(CRLF)
                    .append(CRLF);

            writer.flush();
            outputStream.flush();
            try (final FileInputStream inputStream = new FileInputStream(uploadFile);) {
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            writer.append(CRLF);
        }

        public void addHeaderField(String name, String value) {
            writer.append(name).append(": ").append(value).append(CRLF);
        }

        public byte[] finish() throws IOException {
            writer.append(CRLF).append("--").append(boundary).append("--")
                    .append(CRLF);
            writer.close();
            final int status = connection.getResponseCode();
            if (status != HTTP_OK) {
                throw new IOException(format("{0} failed with HTTP status: {1}",
                        url, status));
            }

            try (final InputStream is = connection.getInputStream()) {
                final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    bytes.write(buffer, 0, bytesRead);
                }
                return bytes.toByteArray();
            } finally {
                connection.disconnect();
            }
        }
    }
}