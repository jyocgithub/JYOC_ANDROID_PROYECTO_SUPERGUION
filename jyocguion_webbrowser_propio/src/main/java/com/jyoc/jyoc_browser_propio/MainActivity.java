package com.jyoc.jyoc_browser_propio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView miWebView;
    EditText etURL;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miWebView = findViewById(R.id.wvWebView);
        etURL = findViewById(R.id.etURL);

        WebSettings webSettings = miWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Forzamos el webview para que abra los enlaces internos dentro de la la APP
        // Si no , abre un browser en el movil y en el carga la pagina
        // Sin esta linea seria una llamada a la app nativa del movil
        miWebView.setWebViewClient(new MyWebViewClient());   // asi con clase propia, con comportamiento propio
        miWebView.setWebViewClient(new WebViewClient());  // asi con clase estandar ,sin comportamiento propio

        url = "https://www.google.com/maps/search/avenida+ferrol+7+madrid";
         miWebView.loadUrl(url);
    
    }


    public void enviarPulsado(View view) {

        url = etURL.getText().toString();
        // Url que carga la app (webview)
        if (url.startsWith("www") || url.startsWith("WWW")) {
            url = "http://" + url;
        }
        miWebView.loadUrl(url);

        //Sincronizamos la barra de progreso de la web
        final ProgressBar progressBar = findViewById(R.id.pbprogressBarWeb);

        miWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                MainActivity.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void atrasPulsado(View view) {
        if (miWebView.canGoBack()) {
            miWebView.goBack();
        }
        // existe igualmente canGoForward() y goForward()
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
            // Url base de la APP (al salir de esta url, abre el navegador) poner como se muestra, sin https://
            String url = (String.valueOf(request.getUrl()));

            if ("www.example.com".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
//
//
//class MyAppWebViewClient extends WebViewClient {
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
//        // Url base de la APP (al salir de esta url, abre el navegador) poner como se muestra, sin https://
//        String url = (String.valueOf(request.getUrl()));
//        if (Uri.parse(url).getHost().endsWith("google.com")) {
//            return false;
//        }
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        view.getContext().startActivity(intent);
//        return true;
//    }
//}

