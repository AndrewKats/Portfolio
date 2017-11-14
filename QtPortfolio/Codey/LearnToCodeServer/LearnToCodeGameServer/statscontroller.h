#ifndef STATSCONTROLLER_H
#define STATSCONTROLLER_H

#include <httprequesthandler.h>

class StatsController : public HttpRequestHandler
{
public:
    StatsController(QObject* parent=0);
    void service(HttpRequest& request, HttpResponse& response);
};

#endif // STATSCONTROLLER_H
