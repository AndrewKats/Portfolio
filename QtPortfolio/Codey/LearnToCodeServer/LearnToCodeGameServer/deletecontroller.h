#ifndef DELETECONTROLLER_H
#define DELETECONTROLLER_H

#include "httprequesthandler.h"

class DeleteController : public HttpRequestHandler {
    Q_OBJECT
public:
    DeleteController(QObject* parent=0);
    void service(HttpRequest& request, HttpResponse& response);

};

#endif // DELETECONTROLLER_H
