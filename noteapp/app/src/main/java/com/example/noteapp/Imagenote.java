package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.URLConnection.guessContentTypeFromName;

public class Imagenote extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 123;
    public static final int REQUEST_CODE_PHOTO = 111;
    int index = 1;

    EditText imagetitle;
    ListView imagelistview;
    LinearLayout linearLayout;

    public String filerealpath;
    ArrayList<String> fileslist;
    String Urifile;
    ArrayAdapter<Bitmap> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenote);
        init();
        fileslist=new ArrayList<>();


    }

    public void init() {
        imagetitle = (EditText) findViewById(R.id.imageTitle);
        linearLayout = (LinearLayout) findViewById(R.id.linnear);
    }


    //tao menu toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuimagenote, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //event toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent((Intent.ACTION_PICK));
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
                return true;
            case R.id.menu2:
                checkPermission();
                HttpPostAsync task = new HttpPostAsync(fileslist,imagetitle.getText().toString(),1,Mylocationchage.GPS);
                task.execute(""+Const.URL+"/uploadimg");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //tra ve bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //lấy ảnh từ camera
            // image.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            filerealpath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // image.setImageBitmap(bitmap);
                ImageView item = new ImageView(this);
                item.setImageBitmap(bitmap);
                linearLayout.addView(item, index);
                index++;
                File file = new File(filerealpath);
                Urifile = file.getAbsolutePath();
                fileslist.add(Urifile);
                Log.e("danh sach file", ""+fileslist.size()+" " );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    void checkPermission() {
        Log.d("PERM", "RUN");
        // Send SMS to 5556

        String[] perm_array = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            List<String> permissions = new ArrayList<String>();

            for (int i = 0; i < perm_array.length; i++) {
                if (checkSelfPermission(perm_array[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(perm_array[i]);
                }
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 9999);
            } else {

            }
        } else {

        }
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

    public class HttpPostAsync extends AsyncTask<String,Void,String>{
        private static final String CRLF = "\r\n";
        private static final String CHARSET = "UTF-8";
        ArrayList<String> files;
        String titlevalue;
        int useridvalue;
        String gpsvalue;
        private ProgressDialog pDialog;
        public HttpPostAsync(ArrayList<String> f,String value,int userid,String gps){
            this.files=f;
            this.titlevalue=value;
            this.useridvalue=userid;
            this.gpsvalue=gps;

        }
        // for log formatting only
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url=new URL(strings[0]);
                long  start = currentTimeMillis();
                int CONNECT_TIMEOUT = 15000;
                int READ_TIMEOUT = 10000;
                String boundary = "---------------------------" + currentTimeMillis();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Charset", CHARSET);
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET),
                        true);
                writer.append("--").append(boundary).append(CRLF)
                        .append("Content-Disposition: form-data; name=\"").append("title")
                        .append("\"").append(CRLF)
                        .append("Content-Type: text/plain; charset=").append(CHARSET)
                        .append(CRLF).append(CRLF).append(titlevalue).append(CRLF);

                writer.append("--").append(boundary).append(CRLF)
                        .append("Content-Disposition: form-data; name=\"").append("id")
                        .append("\"").append(CRLF)
                        .append("Content-Type: text/plain; charset=").append(CHARSET)
                        .append(CRLF).append(CRLF).append(Integer.toString(useridvalue)).append(CRLF);
                writer.append("--").append(boundary).append(CRLF)
                        .append("Content-Disposition: form-data; name=\"").append("gps")
                        .append("\"").append(CRLF)
                        .append("Content-Type: text/plain; charset=").append(CHARSET)
                        .append(CRLF).append(CRLF).append(gpsvalue).append(CRLF);
                //file
                int poss=0;
                for(int i=0;i<files.size();i++){
                    final String fileName = new File(files.get(i)).getName();
                    writer.append("--").append(boundary).append(CRLF)
                            .append("Content-Disposition: form-data; name=\"")
                            .append("img").append(Integer.toString(poss)).append("\"; filename=\"").append(fileName)
                            .append("\"").append(CRLF).append("Content-Type: ")
                            .append(guessContentTypeFromName(fileName)).append(CRLF)
                            .append("Content-Transfer-Encoding: binary").append(CRLF)
                            .append(CRLF);

                    writer.flush();
                    outputStream.flush();
                    try (final FileInputStream inputStream = new FileInputStream(new File(files.get(i)));) {
                        final byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.flush();
                    }
                    writer.append(CRLF);
                    poss++;
                }


                writer.append(CRLF).append("--").append(boundary).append("--")
                        .append(CRLF);
                writer.close();
                int statusCode = connection.getResponseCode();

                if (statusCode ==  200) {

                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                    String response = convertInputStreamToString(inputStream);
                    //Log.d("res", response);
                    //JSONObject jsonObject = new JSONObject(response);
                    //String res = jsonObject.getString("result_message");
                    return  response;
                    //Toast.makeText(DoiMkActivity.this, "result_message: " + res, Toast.LENGTH_SHORT).show();
                } else {
                    // Status code is not 200
                    // Do something to handle the error
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();


        } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

    }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Imagenote.this);
            pDialog.setMessage("Waitting");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        // When the task is completed, this method will be called
        // Download complete. Lets update UI
        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                        // load.dissloadding();
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                    }
            }
        }
    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //postdata
   /* public static class MultipartUtility {

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
    }*/

}
