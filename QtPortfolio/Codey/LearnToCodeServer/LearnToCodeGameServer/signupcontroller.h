#ifndef SIGNUPCONTROLLER_H
#define SIGNUPCONTROLLER_H

#include "httprequesthandler.h"

class SignupController : public HttpRequestHandler {
    Q_OBJECT
public:
    SignupController(QObject* parent=0);
    void service(HttpRequest& request, HttpResponse& response);
};
#endif // SIGNUPCONTROLLER_H
