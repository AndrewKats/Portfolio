#ifndef SAVECONTROLLER_H
#define SAVECONTROLLER_H

#include "httprequesthandler.h"


class SaveController : public HttpRequestHandler {
    Q_OBJECT
public:
    SaveController(QObject* parent=0);
    void service(HttpRequest& request, HttpResponse& response);
};

#endif // SAVECONTROLLER_H
