package servlet;

import http.HttpRequest;
import http.HttpResponse;

public abstract class HttpServlet {
    public abstract void service(HttpResponse response, HttpRequest request);
}
