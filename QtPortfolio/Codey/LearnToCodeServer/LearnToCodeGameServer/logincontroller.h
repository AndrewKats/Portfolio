#ifndef LOGINCONTROLLER_H
#define LOGINCONTROLLER_H

#include "httprequesthandler.h"

class LoginController : public HttpRequestHandler {
    Q_OBJECT
public:
    LoginController(QObject* parent=0);
    void service(HttpRequest& request, HttpResponse& response);
};

#endif // LOGINCONTROLLER_H
